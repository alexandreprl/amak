package fr.irit.smac.amak.scheduling;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * A scheduler associated to a MAS
 *
 * @author Alexandre Perles
 */
public class Scheduler implements Runnable {
	/**
	 * The schedulables object handled by the scheduler
	 */
	private final Set<Schedulable> schedulables = new LinkedHashSet<>();
	/**
	 * A lock to protect the state
	 */
	private final ReentrantLock stateLock = new ReentrantLock();
	/**
	 * The methods called when the speed is changed
	 */
	private final List<Consumer<Scheduler>> onChange = new ArrayList<>();
	/**
	 * The schedulables that must be added
	 */
	private final Queue<Schedulable> pendingAdditionSchedulables = new LinkedList<>();
	/**
	 * The schedulables that must be removed
	 */
	private final Queue<Schedulable> pendingRemovalSchedulables = new LinkedList<>();
	/**
	 * The state of the scheduler {@link State}
	 */
	private State state;
	/**
	 * The sleep time in ms between each cycle
	 */
	private int sleep;
	/**
	 * Method that is called when the scheduler stops
	 */
	private Consumer<Scheduler> onStop;
	/**
	 * The idea is to prevent scheduler from launching if the schedulables are not
	 * yet fully ready
	 */
	private int locked = 0;

	/**
	 * Constructor which set the initial state
	 *
	 * @param schedulables the corresponding schedulables
	 */
	public Scheduler(Schedulable... schedulables) {

		for (Schedulable schedulable : schedulables) {
			this.add(schedulable);
		}
		this.state = State.IDLE;
	}

	/**
	 * Set the delay between two cycles and launch the scheduler if it is not
	 * running
	 *
	 * @param i the delay between two cycles
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
		if (Objects.requireNonNull(state) == State.IDLE) {
			state = State.RUNNING;
			new Thread(this).start();
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
		if (Objects.requireNonNull(state) == State.IDLE) {
			state = State.PENDING_STOP;
			new Thread(this).start();
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
		if (Objects.requireNonNull(state) == State.RUNNING) {
			state = State.PENDING_STOP;
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
		try {
			do {
				for (Schedulable schedulable : schedulables) {
					schedulable.cycle();
				}
				if (getSleep() != 0) {
					Thread.sleep(getSleep());
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
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
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
	 * @param onStop Consumer method
	 */
	public final void setOnStop(Consumer<Scheduler> onStop) {
		this.onStop = onStop;
	}

	/**
	 * Add a method that must be executed when the scheduler speed is changed
	 *
	 * @param onChangeConsumer Consumer method
	 */
	public final void addOnChange(Consumer<Scheduler> onChangeConsumer) {
		synchronized (this.onChange) {
			this.onChange.add(onChangeConsumer);
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
	 * @param sleep The time between each cycle
	 */
	public void setSleep(int sleep) {
		this.sleep = sleep;
	}

	/**
	 * Plan to add a schedulable
	 *
	 * @param schedulable the schedulable to add
	 */
	public void add(Schedulable schedulable) {
		this.pendingAdditionSchedulables.add(schedulable);
	}

	/**
	 * Plan to remove a schedulable
	 *
	 * @param schedulable the schedulable to remove
	 */
	public void remove(Schedulable schedulable) {
		this.pendingRemovalSchedulables.add(schedulable);
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

	/**
	 * State of the scheduler
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

}
