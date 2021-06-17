package fr.irit.smac.amak.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

	/**
	 * Format the time in more readable format
	 * 
	 * @param name
	 *            Unique name for time measure (use the same as in start)
	 * @return the amount of time since the start formatted in a human-readable way
	 */
	public static String endHR(String name) {
		long nanoTime = end(name);

	    long hours = TimeUnit.NANOSECONDS.toHours(nanoTime);
	    nanoTime -= TimeUnit.HOURS.toNanos(hours);

	    long minutes = TimeUnit.NANOSECONDS.toMinutes(nanoTime);
	    nanoTime -= TimeUnit.MINUTES.toNanos(minutes);

	    long seconds = TimeUnit.NANOSECONDS.toSeconds(nanoTime);
	    nanoTime -= TimeUnit.SECONDS.toNanos(seconds);

	    long milliseconds = TimeUnit.NANOSECONDS.toMillis(nanoTime);
	    nanoTime -= TimeUnit.MILLISECONDS.toNanos(milliseconds);
	    
	    
		return String.format("%d h. %d m. %d s. %d ms. %d ns.", hours, minutes, seconds, milliseconds, nanoTime);
	}
}
