package agent;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Environment;

public class TestAgent {

	private static final String STR_ID_BASE = "Agent #";
	private Agent<Amas<Environment>, Environment> agent;


	@Before
	public void setUp() {
		agent = new Agent<Amas<Environment>, Environment>(null, null) {
		};
	}
	
	
	@Test
	public void toStringTest() {
		String strAID = agent.toString();
		assertNotNull(strAID);
		assertTrue(strAID.contains(STR_ID_BASE) && !strAID.equals(STR_ID_BASE));
	}
	
}
