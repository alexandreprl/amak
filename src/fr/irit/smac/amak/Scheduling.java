package fr.irit.smac.amak;

/**
 * The scheduling of a system can be manual or automatic
 */
public enum Scheduling {
	MANUAL, AUTOMATIC, MANUAL_AUTOSTART, STOPPED;

	public static boolean hasAutostart(Scheduling scheduling) {
		return scheduling == Scheduling.AUTOMATIC||scheduling==MANUAL_AUTOSTART;
	}

	public static boolean isManual(Scheduling scheduling) {
		return scheduling == MANUAL||scheduling==MANUAL_AUTOSTART;
	}
}