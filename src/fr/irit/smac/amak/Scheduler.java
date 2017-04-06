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
	private final Amas<? extends Environment> amas;
	private State state;
	private int sleep;
	private final ReentrantLock stateLock = new ReentrantLock();
	private boolean autorun;
	private Consumer<Scheduler> onStop;

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
	 * @param _amas
	 *            the corresponding amas
	 * @param _autorun
	 *            should the scheduler automatically starts
	 */
	public Scheduler(Amas<? extends Environment> _amas, boolean _autorun) {
		this.autorun = _autorun;
		this.amas = _amas;
		this.state = State.IDLE;
		if (this.autorun)
			this.start();
	}

	/**
	 * Set the delay between two cycles and launch the scheduler if it is not
	 * running
	 * 
	 * @param i
	 *            the delay between two cycles
	 */
	public void startWithSleep(int i) {
		sleep = i;
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
	}

	/**
	 * Start (or continue) with no delay between cycles
	 */
	public void start() {
		startWithSleep(0);
	}

	/**
	 * Execute one cycle
	 */
	public void step() {
		this.sleep = 0;
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
			amas.getEnvironment().onCycleBegin();
			amas.cycle();
			amas.getEnvironment().onCycleEnd();
			if (sleep != 0) {
				try {
					Thread.sleep(sleep);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while (state == State.RUNNING);
		stateLock.lock();
		if (onStop != null)
			onStop.accept(this);
		state = State.IDLE;
		stateLock.unlock();
	}

	/**
	 * Should the scheduler starts automatically ?
	 * 
	 * @return whether or not the autorun is activated
	 */
	public boolean isAutorun() {
		return autorun;
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

}
