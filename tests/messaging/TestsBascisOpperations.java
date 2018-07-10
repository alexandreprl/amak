package messaging;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.irit.smac.amak.aid.AddressableAID;
import fr.irit.smac.amak.internal.messaging.ImplMessagingServiceAgentMessaging;
import fr.irit.smac.amak.messaging.IAmakAddress;
import fr.irit.smac.amak.messaging.IAmakEnvelope;
import fr.irit.smac.amak.messaging.IAmakMessage;
import fr.irit.smac.amak.messaging.IAmakMessageBox;
import fr.irit.smac.amak.messaging.MessagingTechnicalException;

public class TestsBascisOpperations {

	private ImplMessagingServiceAgentMessaging messagingServices;
	private IAmakMessageBox msgbox2;
	private IAmakMessageBox msgbox1;
	private IAmakAddress address1;
	private IAmakAddress address2;
	private AddressableAID addressableAID1;
	private AddressableAID addressableAID2;

	@Before
	public void setUp() {
		messagingServices = new ImplMessagingServiceAgentMessaging();

		final String rawID1 = "testAID1";
		final String rawID2 = "testAID2";

		address1 = createAddress(rawID1);
		addressableAID1 = createAddressableAID(rawID1, address1);
		msgbox1 = createMsgbox(addressableAID1);
		address2 = createAddress(rawID2);
		addressableAID2 = createAddressableAID(rawID2, address2);
		msgbox2 = createMsgbox(addressableAID2);
	}

	private IAmakMessageBox createMsgbox(AddressableAID addressableAID) {
		return messagingServices.buildNewAmakMessageBox(addressableAID);
	}

	private AddressableAID createAddressableAID(String rawID1, IAmakAddress address) {
		AddressableAID addressableAID = new AddressableAID(address, rawID1);
		return addressableAID;
	}

	private IAmakAddress createAddress(String rawID1) {
		IAmakAddress address = messagingServices.buildNewAmakAddress(rawID1);
		return address;
	}

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

	@After
	public void teardown() {
		messagingServices.dispose();
	}
}
