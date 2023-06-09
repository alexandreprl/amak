package fr.irit.smac.amak.scheduling;

import lombok.Getter;
import lombok.Setter;

import java.lang.module.ModuleReader;
import java.util.*;
import java.util.concurrent.*;
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
	@Getter
	private int sleep;
	/**
	 * Method that is called when the scheduler stops
	 */
	@Setter
	private Consumer<Scheduler> onStop;
	private Semaphore runSemaphore = new Semaphore(0);

	/**
	 * Constructor which set the initial state
	 *
	 * @param schedulables the corresponding schedulables
	 */
	public Scheduler(Schedulable... schedulables) {
		this(Executors.newSingleThreadExecutor(), schedulables);
	}

	/**
	 * Constructor which set the initial state
	 *
	 * @param executorService the thread executor for the scheduler
	 * @param schedulables    the corresponding schedulables
	 */
	public Scheduler(ExecutorService executorService, Schedulable... schedulables) {
		for (Schedulable schedulable : schedulables) {
			this.add(schedulable);
		}
		setState(SchedulerState.IDLE);
		this.executorService = executorService;
	}

	/**
	 * Set the delay between two cycles and launch the scheduler if it is not
	 * running
	 *
	 * @param i the delay between two cycles
	 */
	public Future<?> startWithSleep(int i) {
		sleep = i;
		stateLock.lock();
		Future<?> task = CompletableFuture.completedFuture(0L);
		if (state == SchedulerState.IDLE) {
			setState(SchedulerState.RUNNING);
			task = executorService.submit(this);
		}
		stateLock.unlock();
		return task;
	}
	public void startWithSleepSync(int i) {
		sleep = i;
		stateLock.lock();
		Future<?> task = CompletableFuture.completedFuture(0L);
		if (state == SchedulerState.IDLE) {
			setState(SchedulerState.RUNNING);
		stateLock.unlock();
			run();
		}else
			stateLock.unlock();
	}

	/**
	 * Start (or continue) with no delay between cycles
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
			setState(SchedulerState.PENDING_STOP);
			executorService.execute(this);
		}
		stateLock.unlock();
	}

	/**
	 * Stop the scheduler if it is running
	 */
	public void stop() {
		stateLock.lock();
		if (state == SchedulerState.RUNNING) {
			setState(SchedulerState.PENDING_STOP);
		}
		stateLock.unlock();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		setState(SchedulerState.IDLE);

		for (Schedulable schedulable : schedulables) {
			schedulable.onSchedulingStops();
		}
		treatPendingSchedulables();
		if (onStop != null)
			onStop.accept(this);

		runSemaphore.release();
	}

	/**
	 * When a scheduler is started, it is executed in a different thread. Use this method if you need to wait for the process to be finished.
	 */
	public void waitUntilStop() {
		try {
			runSemaphore.acquire();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void setState(SchedulerState newState) {
		stateLock.lock();
		state = newState;
		synchronized (onChange) {
			onChange.forEach(c -> c.accept(this));
		}
		stateLock.unlock();
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
	public final void addOnChange(Consumer<Scheduler> onChangeConsumer) {
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

	public boolean isRunning() {
		return state == SchedulerState.RUNNING;
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
