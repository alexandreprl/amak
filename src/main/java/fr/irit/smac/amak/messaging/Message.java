package fr.irit.smac.amak.messaging;

import fr.irit.smac.amak.Agent;
import lombok.Getter;

public class Message<SenderType extends Agent> {
	@Getter
	private final SenderType sender;

	public Message(SenderType sender) {
		this.sender = sender;
	}
}
