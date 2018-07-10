package messaging;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;

import fr.irit.smac.amak.messaging.IAmakEnvelope;
import fr.irit.smac.amak.messaging.IAmakMessage;
import fr.irit.smac.amak.messaging.IAmakMessageMetaData;
import fr.irit.smac.amak.messaging.MessagingTechnicalException;
import testutils.ObjectsForMessagingTesting;

public class TestCustomMetaData extends ObjectsForMessagingTesting {


	@Test
	public void testSendingWithCustomMetadataSuccessful() {
		try {
			msgbox1.sendMessage(MSG_1, addressableAID2, metadata1);
			List<IAmakEnvelope> allEnv = msgbox2.getReceivedMessages();
			assertEquals(1, allEnv.size());
			IAmakEnvelope env = allEnv.get(0);
			IAmakMessage msg = env.getMessage();
			assertTrue(msg instanceof MyMsg);
			assertEquals(MSG_VAL, ((MyMsg) msg).getAgentMsg());
			assertEquals(addressableAID1, env.getMessageSenderAID());
			IAmakMessageMetaData metadata = env.getMetadata();
			assertTrue(metadata instanceof MyCustomMetadata);
			assertEquals(SENDING_COST_METADATA, ((MyCustomMetadata) metadata).getSendingCost());

		} catch (MessagingTechnicalException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	

}
