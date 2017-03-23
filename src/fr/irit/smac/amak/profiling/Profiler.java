package fr.irit.smac.amak.profiling;

import java.util.HashMap;
import java.util.Map;

public class Profiler {
	private static Map<String, Long> starts = new HashMap<>();
	public static void start(String name) {
		starts.put(name, System.currentTimeMillis());
	}
	public static long end(String name) {
		return System.currentTimeMillis()-starts.get(name);
	}
}
