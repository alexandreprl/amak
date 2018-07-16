package testutils;

import org.junit.After;

import fr.irit.smac.amak.aid.AddressableAID;
import fr.irit.smac.amak.internal.messaging.ImplMessagingServiceAgentMessaging;
import fr.irit.smac.amak.messaging.IAmakAddress;
import fr.irit.smac.amak.messaging.IAmakMessage;
import fr.irit.smac.amak.messaging.IAmakMessageBox;
import fr.irit.smac.amak.messaging.IAmakMessageMetaData;
import fr.irit.smac.amak.messaging.SimpleAmakMessageMetaData;

public class ObjectsForMessagingTesting {

	public static final float SENDING_COST_METADATA = 0.65f;
	public static final String MSG_VAL = "Hi !";
	public final ImplMessagingServiceAgentMessaging messagingServices;
	public final IAmakMessageBox msgbox2;
	public final IAmakMessageBox msgbox1;
	public final IAmakAddress address1;
	public final IAmakAddress address2;
	public final AddressableAID addressableAID1;
	public final AddressableAID addressableAID2;
	public final MyMsg MSG_1 = new MyMsg(MSG_VAL);
	public final IAmakMessageMetaData metadata1 = new MyCustomMetadata(SENDING_COST_METADATA);

	public ObjectsForMessagingTesting() {
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

	private static AddressableAID createAddressableAID(String rawID1, IAmakAddress address) {
		AddressableAID addressableAID = new AddressableAID(address, rawID1);
		return addressableAID;
	}

	private IAmakAddress createAddress(String rawID1) {
		IAmakAddress address = messagingServices.buildNewAmakAddress(rawID1);
		return address;
	}

	@After
	public void teardown() {
		messagingServices.dispose();
	}

	public static class MyCustomMetadata extends SimpleAmakMessageMetaData {
		// SimpleAmakMessageMetaData hasn't a sendingCost attribute
		private final float sendingCost;

		public MyCustomMetadata(float sendingCost) {
			this.sendingCost = sendingCost;
		}

		public float getSendingCost() {
			return sendingCost;
		}

	}

	public static class MyMsg implements IAmakMessage {
		private final String agentMsg;

		public MyMsg(String agentMsg) {
			this.agentMsg = agentMsg;
		}

		public String getAgentMsg() {
			return agentMsg;
		}

	}
}
