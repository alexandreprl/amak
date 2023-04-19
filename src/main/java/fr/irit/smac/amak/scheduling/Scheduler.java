package fr.irit.smac.amak.scheduling;

import fr.irit.smac.amak.SchedulableExecutionException;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * A scheduler associated to a MAS
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
	 * The executor service on which the scheduler should run
	 */
	private final ExecutorService executorService;
	/**
	 * The state of the scheduler {@link SchedulerState}
	 */
	private SchedulerState state;
	/**
	 * The sleep time in ms between each cycle
	 */
	private int sleep;
	/**
	 * Method that is called when the scheduler stops
	 */
	@Setter
	private Consumer<Scheduler> onStop;

	/**
	 * Constructor which set the initial state
	 *
	 * @param schedulables the corresponding schedulables
	 */
	public Scheduler(ExecutorService executorService, Schedulable... schedulables) {
		for (Schedulable schedulable : schedulables) {
			this.add(schedulable);
		}
		this.state = SchedulerState.IDLE;
		this.executorService = executorService;
	}

	/**
	 * Set the delay between two cycles and launch the scheduler if it is not
	 * running
	 *
	 * @param i the delay between two cycles
	 * @return
	 */
	public Future<?> startWithSleep(int i) {
		sleep = i;
		stateLock.lock();
		Future<?> task = CompletableFuture.completedFuture(0L);
		if (state == SchedulerState.IDLE) {
			state = SchedulerState.RUNNING;
			task = executorService.submit(this);
		}
		stateLock.unlock();
		synchronized (onChange) {
			onChange.forEach(c -> c.accept(this));
		}
		return task;
	}

	/**
	 * Start (or continue) with no delay between cycles
	 *
	 * @return
	 */
	public Future<?> start() {
		return startWithSleep(0);
	}

	/**
	 * Execute one cycle
	 */
	public void step() {
		sleep = 0;
		stateLock.lock();
		if (state == SchedulerState.IDLE) {
			state = SchedulerState.PENDING_STOP;
			executorService.execute(this);
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
		if (state == SchedulerState.RUNNING) {
			state = SchedulerState.PENDING_STOP;
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
				if (sleep != 0) {
					Thread.sleep(sleep);
				}
				mustStop = false;
				for (Schedulable schedulable : schedulables) {
					mustStop |= schedulable.stopCondition();
				}
			} while (state == SchedulerState.RUNNING && !mustStop);
		} catch (InterruptedException | SchedulableExecutionException e) {
			e.printStackTrace();
		}
		stateLock.lock();
		state = SchedulerState.IDLE;
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
	 * Add a method that must be executed when the scheduler speed is changed
	 *
	 * @param onChangeConsumer Consumer method
	 */
	public final void addOnChange(Consumer onChangeConsumer) {
		synchronized (this.onChange) {
			this.onChange.add(onChangeConsumer);
		}
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
	 * State of the scheduler
	 */
	public enum SchedulerState {
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
