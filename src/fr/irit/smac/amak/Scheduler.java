package fr.irit.smac.amak;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class Scheduler implements Runnable {
	private Amas<? extends Environment> amas;
	private State state;
	private int sleep;
	private ReentrantLock stateLock = new ReentrantLock();
	private boolean autorun;
	private Consumer<Scheduler> onStop;

	public enum State {
		RUNNING, IDLE, PENDING_STOP
		
	}

	public Scheduler(Amas<? extends Environment> amas, boolean _autorun) {
		this.autorun = _autorun;
		this.amas = amas;
		this.state = State.IDLE;
		if (this.autorun)
			this.start();
	}

	public void startWithSleep(int i) {
		sleep = i;
		stateLock.lock();
		switch(state) {
		case IDLE:
			state = State.RUNNING;
			new Thread(this).start();
			break;
			
		}
		stateLock.unlock();
	}
	public void start() {
		startWithSleep(0);
	}

	public void step() {
		stateLock.lock();
		switch(state) {
		case IDLE:
			this.sleep = 0;
			state = State.PENDING_STOP;
			new Thread(this).start();
			break;
			
		}
		stateLock.unlock();
	}
	public void stop() {
		stateLock.lock();
		switch(state) {
		case RUNNING:
			state = State.PENDING_STOP;
			break;
			
		}
		stateLock.unlock();
	}

	@Override
	public void run() {
		do {
			amas.getEnvironment().onCycleBegin();
			amas.cycle();
			amas.getEnvironment().onCycleEnd(amas);
			if (sleep != 0) {
				try {
					Thread.sleep(sleep);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
		}while(state==State.RUNNING);
		stateLock.lock();
		if (onStop != null)
			onStop.accept(this);
		state = State.IDLE;
		stateLock.unlock();
	}

	public boolean isAutorun() {
		return autorun;
	}

	public void setOnStop(Consumer<Scheduler> _onStop) {
		this.onStop = _onStop;
	}


}
