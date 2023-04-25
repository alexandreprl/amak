package fr.irit.smac.amak.scheduling;

public class SchedulableExecutionException extends Exception {
	private final RuntimeException[] causes;

	public SchedulableExecutionException(RuntimeException[] causes) {
		this.causes = causes;
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
		for (var e :
				causes) {
			e.printStackTrace();
		}
	}
}
