package agent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.Test;

import fr.irit.smac.amak.messaging.IAmakEnvelope;
import testutils.ObjectsForAgentTesting;
import testutils.ObjectsForMessagingTesting;

public class TestAgentMessagingFeature extends ObjectsForAgentTesting {

	public ObjectsForMessagingTesting omt = new ObjectsForMessagingTesting();
	
	
	@Test
	public void sendSimpleMsg() {
		boolean sendingSuccessful = agent1.sendMessage(omt.MSG_1, agent2.getAID());
		assertTrue(sendingSuccessful);

		Collection<IAmakEnvelope> receivedMsg = agent2.pollAllReceivedMessages();
		assertEquals(1, receivedMsg.size());

		IAmakEnvelope env = receivedMsg.iterator().next();
		assertEquals(omt.MSG_1, env.getMessage());
		assertEquals(agent1.getAID(), env.getMessageSenderAID());
	}


}
