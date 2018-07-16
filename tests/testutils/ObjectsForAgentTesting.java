package testutils;

import org.junit.Before;

import fr.irit.smac.amak.AgentCommunicant;
import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;

public class ObjectsForAgentTesting {

	public AgentCommunicant<TestAMAS, TestEnv> communicantAgent1;
	public AgentCommunicant<TestAMAS, TestEnv> communicantAgent2;
	public TestAMAS amas;


	@Before
	public void setup() {
		TestEnv env = new TestEnv();
		amas = new TestAMAS(env);
		Object params[] = {};
		communicantAgent1 = new AgentCommunicant<TestAMAS, TestEnv>(amas, params) {
		};

		communicantAgent2 = new AgentCommunicant<TestAMAS, TestEnv>(amas, params) {
		};
	}

	private class TestEnv extends Environment {

		public TestEnv() {
			super(Scheduling.DEFAULT);
		}
	}

	public class TestAMAS extends Amas<TestEnv> {
		public TestAMAS(TestEnv environment) {
			super(environment, Scheduling.HIDDEN);
		}
	}
}
