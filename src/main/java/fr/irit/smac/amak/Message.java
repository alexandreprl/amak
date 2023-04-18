package fr.irit.smac.amak;

import lombok.Getter;

public class Message {
	@Getter
	private final Agent sender;

	public Message(Agent sender) {
		this.sender = sender;
	}
}
