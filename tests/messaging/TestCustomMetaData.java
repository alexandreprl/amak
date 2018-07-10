package messaging;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.irit.smac.amak.aid.AddressableAID;
import fr.irit.smac.amak.internal.messaging.ImplMessagingServiceAgentMessaging;
import fr.irit.smac.amak.messaging.IAmakAddress;
import fr.irit.smac.amak.messaging.IAmakEnvelope;
import fr.irit.smac.amak.messaging.IAmakMessage;
import fr.irit.smac.amak.messaging.IAmakMessageBox;
import fr.irit.smac.amak.messaging.IAmakMessageMetaData;
import fr.irit.smac.amak.messaging.MessagingTechnicalException;
import fr.irit.smac.amak.messaging.SimpleAmakMessageMetaData;

public class TestCustomMetaData {
	private static final float SENDING_COST_METADATA = 0.65f;
	private static final String MSG_VAL = "Hi !";
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
	public void testSendingWithCustomMetadataSuccessful() {
		try {
			IAmakMessage msg1 = new MyMsg(MSG_VAL);
			msgbox1.sendMessage(msg1, addressableAID2, new MyCustomMetadata(SENDING_COST_METADATA));
			List<IAmakEnvelope> allEnv = msgbox2.getReceivedMessages();
			assertEquals(1, allEnv.size());
			IAmakEnvelope env = allEnv.get(0);
			IAmakMessage msg = env.getMessage();
			assertTrue(msg instanceof MyMsg);
			assertEquals(MSG_VAL, ((MyMsg) msg).getAgentMsg());
			assertEquals(this.addressableAID1, env.getMessageSenderAID());
			IAmakMessageMetaData metadata = env.getMetadata();
			assertTrue(metadata instanceof MyCustomMetadata);
			assertEquals(SENDING_COST_METADATA, ((MyCustomMetadata) metadata).getSendingCost());

		} catch (MessagingTechnicalException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	
	private class MyCustomMetadata extends SimpleAmakMessageMetaData {
		// SimpleAmakMessageMetaData hasn't a sendingCost attribute
		private final float sendingCost;
		
		public MyCustomMetadata(float sendingCost) {
			this.sendingCost = sendingCost;
		}

		public float getSendingCost() {
			return sendingCost;
		}
		
	}
	
	private class MyMsg implements IAmakMessage{
		private final String agentMsg;
		
		public MyMsg(String agentMsg) {
			this.agentMsg = agentMsg;
		}

		public String getAgentMsg() {
			return agentMsg;
		}
		
	}
}
