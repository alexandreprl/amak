package fr.irit.smac.amak.messaging;

import java.util.List;

public interface IAmakReadableMessageBox {
	List<IAmakEnvelope> getReceivedMessages();
}
