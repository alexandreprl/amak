package fr.irit.smac.amak;

/**
 * The scheduling of a system can be manual or automatic
 */
public enum Scheduling {
	MANUAL, AUTO, MANUAL_AUTOSTART;

	public static boolean hasAutostart(Scheduling scheduling) {
		return scheduling == AUTO||scheduling==MANUAL_AUTOSTART;
	}

	public static boolean isManual(Scheduling scheduling) {
		return scheduling == MANUAL||scheduling==MANUAL_AUTOSTART;
	}
}