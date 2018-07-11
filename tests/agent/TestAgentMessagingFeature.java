package agent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.After;
import org.junit.Test;

import fr.irit.smac.amak.messaging.IAmakEnvelope;
import testutils.ObjectsForAgentTesting;
import testutils.ObjectsForMessagingTesting;
import testutils.ObjectsForMessagingTesting.MyMsg;

public class TestAgentMessagingFeature extends ObjectsForAgentTesting {

	public ObjectsForMessagingTesting omt = new ObjectsForMessagingTesting();
	
	
	@Test
	public void getAllReceivedMessages() throws InterruptedException {
		agent1.run();
		boolean sendingSuccessful = agent1.sendMessage(omt.MSG_1, agent2.getAID());
		assertTrue(sendingSuccessful);

		agent2.run();
		Collection<IAmakEnvelope> receivedMsg = agent2.getAllReceivedMessages();
		assertEquals(1, receivedMsg.size());
		IAmakEnvelope env = receivedMsg.iterator().next();
		assertEquals(omt.MSG_1, env.getMessage());
		assertEquals(agent1.getAID(), env.getMessageSenderAID());
	}

	@Test
	public void getReceivedMessagesGivenType() {
		agent1.run();

		boolean sendingSuccessful = agent1.sendMessage(omt.MSG_1, agent2.getAID());
		assertTrue(sendingSuccessful);

		agent2.run();
		Collection<MyMsg> receivedMsg = agent2.getReceivedMessagesGivenType(MyMsg.class);
		assertEquals(1, receivedMsg.size());

		MyMsg msg = receivedMsg.iterator().next();
		assertEquals(omt.MSG_1, msg);
	}

	@After
	public void teardown() {
		omt.teardown();
	}

}
