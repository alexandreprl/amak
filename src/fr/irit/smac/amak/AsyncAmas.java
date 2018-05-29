package fr.irit.smac.amak;

import fr.irit.smac.amak.tools.Log;


/**
 * An AsyncAmas has no sense. Therefore this class must not be overridden.
 * 
 * @author perles
 *
 * @param <T> The environment of the Amas
 */
public final class AsyncAmas<T extends Environment> extends Amas<T> {

	/**
	 * Fake constructor for the AsyncAmas
	 */
	public AsyncAmas() {
		super(null, Scheduling.HIDDEN);
		Log.fatal("AMAK", "An AsyncAmas has no sense. AsyncAgent are asynchronous which means that it shouldn't be linked with any amas.");
	}

}
