package fr.irit.smac.amak;

public class SchedulableExecutionException extends Exception {
	private final RuntimeException[] causes;

	public SchedulableExecutionException(RuntimeException[] causes) {
		this.causes = causes;
	}
}
