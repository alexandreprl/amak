package fr.irit.smac.amak.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * This tool allows to measure the amount of time required for an action. For
 * example, to measure the time of an execution or the time of a cycle.
 * 
 * @author Alexandre Perles
 *
 */
public class Profiler {
	/**
	 * Map storing start times
	 */
	private static Map<String, Long> starts = new HashMap<>();

	/**
	 * Start the stopwatch
	 * 
	 * @param name
	 *            Unique name for time measure
	 */
	public static void start(String name) {
		starts.put(name, System.nanoTime());
	}

	/**
	 * Stop the stopwatch
	 * 
	 * @param name
	 *            Unique name for time measure (use the same as in start)
	 * @return the amount of time since the start in nano seconds
	 */
	public static long end(String name) {
		return System.nanoTime() - starts.get(name);
	}
}
