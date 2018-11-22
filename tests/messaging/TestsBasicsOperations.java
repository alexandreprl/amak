package messaging;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;

import fr.irit.smac.amak.messaging.IAmakEnvelope;
import fr.irit.smac.amak.messaging.IAmakMessage;
import fr.irit.smac.amak.messaging.MessagingTechnicalException;
import testutils.ObjectsForMessagingTesting;

public class TestsBasicsOperations extends ObjectsForMessagingTesting {

	@Test
	public void testSendingAloneSuccessful() {
		try {
			msgbox1.sendMessage(new IAmakMessage() {
			}, addressableAID2);
		} catch (MessagingTechnicalException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testReceivingAloneSuccessful() {
		assertTrue(msgbox1.getReceivedMessages().isEmpty());
	}

	@Test
	public void testSendingAndReceivingSuccessful() {
		try {
			final IAmakMessage messageToSend = new IAmakMessage() {
			};
			msgbox1.sendMessage(messageToSend, addressableAID2);
			List<IAmakEnvelope> allReceivedMsg = msgbox2.getReceivedMessages();
			assertEquals(1, allReceivedMsg.size());

			IAmakEnvelope actualMsg = allReceivedMsg.get(0);
			assertEquals(addressableAID1, actualMsg.getMessageSenderAID());
			assertEquals(messageToSend, actualMsg.getMessage());

		} catch (MessagingTechnicalException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testSendingAndReceivingSuccessfulWithAddress() {
		try {
			final IAmakMessage messageToSend = new IAmakMessage() {
			};
			msgbox1.sendMessage(messageToSend, address2);
			List<IAmakEnvelope> allReceivedMsg = msgbox2.getReceivedMessages();
			assertEquals(1, allReceivedMsg.size());

			IAmakEnvelope actualMsg = allReceivedMsg.get(0);
			assertEquals(addressableAID1, actualMsg.getMessageSenderAID());
			assertEquals(messageToSend, actualMsg.getMessage());

		} catch (MessagingTechnicalException e) {
			e.printStackTrace();
			fail();
		}
	}


}
