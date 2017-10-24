package fr.irit.smac.amak;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * A scheduler associated to a MAS
 * 
 * @author Alexandre Perles
 *
 */
public class Scheduler implements Runnable {
	private final Schedulable schedulable;
	private State state;
	private int sleep;
	private final ReentrantLock stateLock = new ReentrantLock();
	private Consumer<Scheduler> onStop;
	private Consumer<Scheduler> onChange;

	/**
	 * State of the scheduler
	 *
	 */
	public enum State {
		RUNNING, IDLE, PENDING_STOP

	}

	/**
	 * Constructor which set the initial state and auto start if requested
	 * 
	 * @param _schedulable
	 *            the corresponding schedulable
	 */
	public Scheduler(Schedulable _schedulable) {
		this.schedulable = _schedulable;
		this.state = State.IDLE;
	}

	/**
	 * Set the delay between two cycles and launch the scheduler if it is not
	 * running
	 * 
	 * @param i
	 *            the delay between two cycles
	 */
	public void startWithSleep(int i) {
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
		if (onChange != null)
			onChange.accept(this);
	}

	/**
	 * Start (or continue) with no delay between cycles
	 */
	public void start() {
		startWithSleep(Schedulable.defaultSleep);
	}

	/**
	 * Execute one cycle
	 */
	public void step() {
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
	}

	/**
	 * Threaded run method
	 */
	@Override
	public void run() {
		do {
			schedulable.cycle();
			if (getSleep() != 0) {
				try {
					Thread.sleep(getSleep());
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while (state == State.RUNNING && !schedulable.stopCondition());
		stateLock.lock();
		if (onStop != null)
			onStop.accept(this);
		state = State.IDLE;
		stateLock.unlock();
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
	 * Set the method that must be executed when the scheduler speed is changed
	 * 
	 * @param _onChange
	 *            Consumer method
	 */
	public final void setOnChange(Consumer<Scheduler> _onChange) {
		this.onChange = _onChange;
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

}
