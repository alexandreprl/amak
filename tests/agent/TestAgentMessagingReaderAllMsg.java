package agent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Test;

import fr.irit.smac.amak.messaging.IAmakEnvelope;
import fr.irit.smac.amak.messaging.IAmakMessage;
import testutils.ObjectsForAgentTesting;
import testutils.ObjectsForMessagingTesting;

public class TestAgentMessagingReaderAllMsg extends ObjectsForAgentTesting {

	public ObjectsForMessagingTesting omt = new ObjectsForMessagingTesting();
	
	
	@Test
	public void getAllReceivedMessages() throws InterruptedException {
		communicantAgent2.run();
		Collection<IAmakEnvelope> receivedMsg = communicantAgent2.getAllMessages();
		assertEquals(0, receivedMsg.size());

		communicantAgent1.run();
		boolean sendingSuccessful = communicantAgent1.sendMessage(omt.MSG_1, communicantAgent2.getAID());
		assertTrue(sendingSuccessful);
		sendingSuccessful = communicantAgent1.sendMessage(omt.MSG_2, communicantAgent2.getAID());
		assertTrue(sendingSuccessful);

		communicantAgent2.run();
		receivedMsg = communicantAgent2.getAllMessages();
		assertEquals(2, receivedMsg.size());
		IAmakEnvelope env = receivedMsg.iterator().next();
		assertEquals(communicantAgent1.getAID(), env.getMessageSenderAID());
		Set<IAmakMessage> expectedMsgs = new HashSet<>();
		expectedMsgs.add(omt.MSG_1);
		expectedMsgs.add(omt.MSG_2);
		Set<IAmakMessage> actualMsgs = new HashSet<>();
		actualMsgs.add(env.getMessage());
		env = receivedMsg.iterator().next();
		actualMsgs.add(env.getMessage());
		assertTrue(expectedMsgs.containsAll(actualMsgs));
		assertEquals(communicantAgent1.getAID(), env.getMessageSenderAID());

		communicantAgent2.run();
		receivedMsg = communicantAgent2.getAllMessages();
		assertEquals(0, receivedMsg.size());
	}


	@After
	public void teardown() {
		omt.teardown();
	}

}
