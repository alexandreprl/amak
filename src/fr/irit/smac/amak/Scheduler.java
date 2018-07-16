package fr.irit.smac.amak;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import fr.irit.smac.amak.ui.MainWindow;
import fr.irit.smac.amak.ui.SchedulerToolbar;

/**
 * A scheduler associated to a MAS
 * 
 * @author Alexandre Perles
 *
 */
public class Scheduler implements Runnable, Serializable {
	/**
	 * Unique ID meant to handle serialization correctly
	 */
	private static final long serialVersionUID = -4765899565369100376L;
	/**
	 * The schedulables object handled by the scheduler
	 */
	private final Set<Schedulable> schedulables = new LinkedHashSet<>();
	/**
	 * The state of the scheduler {@link State}
	 */
	private State state;
	/**
	 * The sleep time in ms between each cycle
	 */
	private int sleep;
	/**
	 * A lock to protect the state
	 */
	private final ReentrantLock stateLock = new ReentrantLock();
	/**
	 * Method that is called when the scheduler stops
	 */
	private Consumer<Scheduler> onStop;
	/**
	 * The methods called when the speed is changed. Useful to change the value of
	 * the GUI slider of {@link SchedulerToolbar}
	 */
	private List<Consumer<Scheduler>> onChange = new ArrayList<>();
	/**
	 * The idea is to prevent scheduler from launching if the schedulables are not
	 * yet fully ready
	 */
	private int locked = 0;
	/**
	 * The default scheduler
	 */
	private static Scheduler defaultScheduler;
	/**
	 * The schedulables that must be added
	 */
	private Queue<Schedulable> pendingAdditionSchedulables = new LinkedList<>();
	/**
	 * The schedulables that must be removed
	 */
	private Queue<Schedulable> pendingRemovalSchedulables = new LinkedList<>();

	/**
	 * State of the scheduler
	 *
	 */
	public enum State {
		/**
		 * The scheduler is running
		 */
		RUNNING,
		/**
		 * The scheduler is paused
		 */
		IDLE,
		/**
		 * The scheduler is expected to stop at the end at the current cycle
		 */
		PENDING_STOP

	}

	/**
	 * Constructor which set the initial state and auto start if requested
	 * 
	 * @param _schedulables
	 *            the corresponding schedulables
	 */
	public Scheduler(Schedulable... _schedulables) {

		for (Schedulable schedulable : _schedulables) {
			this.add(schedulable);
		}
		this.state = State.IDLE;
	}

	/**
	 * Create or return the default scheduler
	 * 
	 * @return The default scheduler
	 */
	public static Scheduler getDefaultScheduler() {
		if (defaultScheduler == null) {
			defaultScheduler = new Scheduler();
			if (!Configuration.commandLineMode)
				MainWindow.addToolbar(new SchedulerToolbar("Default", defaultScheduler));
		}
		return defaultScheduler;
	}

	/**
	 * Set the delay between two cycles and launch the scheduler if it is not
	 * running
	 * 
	 * @param i
	 *            the delay between two cycles
	 */
	public void startWithSleep(int i) {
		if (locked > 0) {
			
			synchronized (onChange) {
				onChange.forEach(c -> c.accept(this));
			}
			return;
		}
		setSleep(i);
		stateLock.lock();
		switch (state) {
		case IDLE:
			state = State.RUNNING;
			new Thread(this).start();
			break;
		default:
			break;

		}
		stateLock.unlock();
		synchronized (onChange) {
			onChange.forEach(c -> c.accept(this));
		}
	}

	/**
	 * Start (or continue) with no delay between cycles
	 */
	public void start() {
		startWithSleep(Schedulable.DEFAULT_SLEEP);
	}

	/**
	 * Execute one cycle
	 */
	public void step() {
		if (locked > 0) {
			synchronized (onChange) {
				onChange.forEach(c -> c.accept(this));
			}
			return;
		}
		this.setSleep(0);
		stateLock.lock();
		switch (state) {
		case IDLE:
			state = State.PENDING_STOP;
			new Thread(this).start();
			break;
		default:
			break;

		}
		stateLock.unlock();
		synchronized (onChange) {
			onChange.forEach(c -> c.accept(this));
		}
	}

	/**
	 * Stop the scheduler if it is running
	 */
	public void stop() {
		stateLock.lock();
		switch (state) {
		case RUNNING:
			state = State.PENDING_STOP;
			break;
		default:
			break;

		}
		stateLock.unlock();
		synchronized (onChange) {
			onChange.forEach(c -> c.accept(this));
		}
	}

	/**
	 * Threaded run method
	 */
	@Override
	public void run() {
		treatPendingSchedulables();
		for (Schedulable schedulable : schedulables) {
			schedulable.onSchedulingStarts();
		}
		boolean mustStop;
		do {
			for (Schedulable schedulable : schedulables) {
				schedulable.cycle();
			}
			if (getSleep() != 0) {
				try {
					Thread.sleep(getSleep());
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
			mustStop = false;
			for (Schedulable schedulable : schedulables) {
				mustStop |= schedulable.stopCondition();
			}
		} while (state == State.RUNNING && !mustStop);
		stateLock.lock();
		state = State.IDLE;
		stateLock.unlock();

		for (Schedulable schedulable : schedulables) {
			schedulable.onSchedulingStops();
		}
		treatPendingSchedulables();
		if (onStop != null)
			onStop.accept(this);
	}

	/**
	 * Effectively Add or Remove the schedulables that were added or removed during
	 * a cycle to avoid {@link ConcurrentModificationException}
	 */
	private void treatPendingSchedulables() {
		while (!pendingAdditionSchedulables.isEmpty())
			schedulables.add(pendingAdditionSchedulables.poll());
		while (!pendingRemovalSchedulables.isEmpty())
			schedulables.remove(pendingRemovalSchedulables.poll());

	}

	/**
	 * Set the method that must be executed when the system is stopped
	 * 
	 * @param _onStop
	 *            Consumer method
	 */
	public final void setOnStop(Consumer<Scheduler> _onStop) {
		this.onStop = _onStop;
	}

	/**
	 * Add a method that must be executed when the scheduler speed is changed
	 * 
	 * @param _onChange
	 *            Consumer method
	 */
	public final void addOnChange(Consumer<Scheduler> _onChange) {
		synchronized (onChange) {
			this.onChange.add(_onChange);
		}
	}

	/**
	 * Is the scheduler running ?
	 * 
	 * @return true if the scheduler is running
	 */
	public boolean isRunning() {
		return state == State.RUNNING;
	}

	/**
	 * Getter for the sleep time
	 * 
	 * @return the sleep time
	 */

	public int getSleep() {
		return sleep;
	}

	/**
	 * Setter for the sleep time
	 * 
	 * @param sleep
	 *            The time between each cycle
	 */
	public void setSleep(int sleep) {
		this.sleep = sleep;
	}

	/**
	 * Plan to add a schedulable
	 * 
	 * @param _schedulable
	 *            the schedulable to add
	 */
	public void add(Schedulable _schedulable) {
		this.pendingAdditionSchedulables.add(_schedulable);
	}

	/**
	 * Plan to remove a schedulable
	 * 
	 * @param _schedulable
	 *            the schedulable to remove
	 */
	public void remove(Schedulable _schedulable) {
		this.pendingRemovalSchedulables.add(_schedulable);
	}

	/**
	 * Soft lock the scheduler to avoid a too early running
	 */
	public void lock() {
		locked++;
	}

	/**
	 * Soft unlock the scheduler to avoid a too early running
	 */
	public void unlock() {
		locked--;
	}

}
