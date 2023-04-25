package fr.irit.smac.amak.messaging;

import fr.irit.smac.amak.Agent;
import lombok.Getter;

public class Message {
	@Getter
	private final Agent sender;

	public Message(Agent sender) {
		this.sender = sender;
	}
}
