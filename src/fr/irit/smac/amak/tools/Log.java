package fr.irit.smac.amak.tools;


import java.util.function.Consumer;

/**
 * Log tools
 * 
 * @author Alexandre Perles
 *
 */
public class Log {
	public enum Level {
		FATAL(60), ERROR(50), IMPORTANT(40), WARNING(30), INFORM(20), DEBUG(10);

		private final int order;

		Level(final int order) {
			this.order = order;
		}

		public boolean isGE(final Level _other) {
			return order >= _other.order;
		}
	}

	public static boolean enabled = true;
	public static String debugFilter = "^.*$";
	private static int idx = 1;
	private static Consumer<String> action = null;
	private static Consumer<String> defaultAction = (s) -> {
		System.out.println(s);
		System.out.flush();
	};
	public static Level minLevel = Level.IMPORTANT;
	private static boolean alreadyInformed = false;

	public static void debug(final String _tag, final String _text, final Object... params) {
		log(Level.DEBUG, _tag, _text, params);
	}

	public static void error(final String _tag, final String _text, final Object... params) {
		log(Level.ERROR, _tag, _text, params);
	}

	public static void fatal(final String _tag, final String _text, final Object... params) {
		log(Level.FATAL, _tag, _text, params);
	}

	private static Consumer<String> getAction() {
		return action == null ? defaultAction : action;
	}

	public static void important(final String _tag, final String _text, final Object... params) {
		log(Level.IMPORTANT, _tag, _text, params);
	}

	public static void inform(final String _tag, final String _text, final Object... params) {
		log(Level.INFORM, _tag, _text, params);
	}

	private static void informOnce() {
		if (!alreadyInformed) {
			System.out.println("/!\\ Logs are disabled. Prefer the use of minimum level.");
			alreadyInformed = true;
		}
	}

	private static void log(final Level _level, final String _tag, final String _text, final Object... params) {
		if (enabled) {
			if (_level.isGE(minLevel)) {
				if (_level != Level.DEBUG || _tag.matches(debugFilter)) {
					getAction().accept(
							String.format("[%6s] %6d/%s: %s", _tag, idx++, _level, String.format(_text, params)));
				}
			}
		} else {
			informOnce();
		}
	}

	public static void setCallback(final Consumer<String> _action) {
		action = _action;
	}

	public static void warning(final String _tag, final String _text, final Object... params) {
		log(Level.WARNING, _tag, _text, params);
	}

}
