package fr.irit.smac.amak.runner;

import fr.irit.smac.amak.Amas;

public class AutoRunner extends Runner implements Runnable {

	private long delayBetweenCycle;
	private boolean running = false;

	public AutoRunner(Amas<?> amas) {
		this(amas, 100);
	}

	public AutoRunner(Amas<?> amas, long _delayBetweenCycle) {
		super(amas);
		delayBetweenCycle = _delayBetweenCycle;
		running = true;
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (running) {
			cycle();
			try {
				Thread.sleep(delayBetweenCycle);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
