package agent;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.CommunicatingAgent;
import testutils.ObjectsForAgentTesting;

public class TestAgentBasicsOperations extends ObjectsForAgentTesting {

	private static final String STR_ID_BASE = "Agent #";
	private Agent<TestAMAS, TestEnv> agent;


	@Before
	public void setUp() {
		super.setup();
		
		agent = new CommunicatingAgent<TestAMAS, TestEnv>(amas, env) {

			@Override
			protected void onPerceive() {
				super.onPerceive();
				getAllMessages();
			}
			
		};
	}
	
	
	@Test
	public void toStringTest() {
		String strAID = agent.toString();
		assertNotNull(strAID);
		assertTrue(strAID.contains(STR_ID_BASE) && !strAID.equals(STR_ID_BASE));
	}
	
	@Test
	public void nothingToPerceiveTest() {
		agent.onePhaseCycle();
		// should not crash
	}
	
	//TODO add more tests
}
