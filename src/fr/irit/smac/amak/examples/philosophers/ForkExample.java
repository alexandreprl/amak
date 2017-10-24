package fr.irit.smac.amak.examples.philosophers;

public class ForkExample {
	private PhilosopherExample takenBy;

	public synchronized boolean tryTake(PhilosopherExample asker) {
		if (takenBy != null)
			return false;
		takenBy = asker;
		return true;
	}

	public synchronized void release(PhilosopherExample asker) {
		if (takenBy == asker) {
			takenBy = null;
		}
	}

	public synchronized boolean owned(PhilosopherExample asker) {
		return takenBy == asker;
	}
}
