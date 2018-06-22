package fr.irit.smac.amak.examples.philosophers;

/**
 * 
 * Fork resource
 * 
 * @author perles
 *
 */
public class ForkExample {
	/**
	 * If the fork is taken by someone, the takenBy attribute will refer to this
	 * person
	 */
	private PhilosopherExample takenBy;

	/**
	 * The philosopher asker tries to take the fork
	 * 
	 * @param asker
	 *            The philosopher who tries
	 * @return true if the fork has been taken
	 */
	public synchronized boolean tryTake(PhilosopherExample asker) {
		if (takenBy != null)
			return false;
		takenBy = asker;
		return true;
	}

	/**
	 * The philosopher asker releases the fork
	 * 
	 * @param asker
	 *            The philosopher who tries
	 */
	public synchronized void release(PhilosopherExample asker) {
		if (takenBy == asker) {
			takenBy = null;
		}
	}

	/**
	 * Is the fork owned by the asker
	 * 
	 * @param asker
	 *            a philosopher
	 * @return true if the fork is taken by asker
	 */
	public synchronized boolean owned(PhilosopherExample asker) {
		return takenBy == asker;
	}
}
