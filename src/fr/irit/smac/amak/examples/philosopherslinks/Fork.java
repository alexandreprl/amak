package fr.irit.smac.amak.examples.philosopherslinks;

public class Fork {
	private Philosopher takenBy;

	public synchronized boolean tryTake(Philosopher asker) {
		if (takenBy != null)
			return false;
		takenBy = asker;
		return true;
	}

	public synchronized void release(Philosopher asker) {
		if (takenBy == asker) {
			takenBy = null;
		}
	}

	public synchronized boolean owned(Philosopher asker) {
		return takenBy == asker;
	}
}
