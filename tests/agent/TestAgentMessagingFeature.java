package agent;

import org.junit.Before;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Environment;

public class TestAgentMessagingFeature {

	private Agent<Amas<Environment>, Environment> agent;


	@Before
	public void setUp() {
		agent = new Agent<Amas<Environment>, Environment>(null, null) {
		};
	}
	
	
	
	//TODO add more tests
}
