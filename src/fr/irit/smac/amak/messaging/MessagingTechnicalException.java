package fr.irit.smac.amak.messaging;

public class MessagingTechnicalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3381348762571712415L;
	private String cause;

	public MessagingTechnicalException(String cause) {
		this.cause = cause;
	}

	@Override
	public String getMessage() {
		return super.getMessage() + " unable to performe messaging opperation !\n" + cause;
	}

}
