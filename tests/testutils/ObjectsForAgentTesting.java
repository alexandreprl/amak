package testutils;

import org.junit.Before;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;

public class ObjectsForAgentTesting {

	public Agent<TestAMAS, TestEnv> agent1;
	public Agent<TestAMAS, TestEnv> agent2;
	public TestAMAS amas;


	@Before
	public void setup() {
		TestEnv env = new TestEnv();
		amas = new TestAMAS(env);
		Object params[] = {};
		agent1 = new Agent<TestAMAS, TestEnv>(amas, params) {
		};

		agent2 = new Agent<TestAMAS, TestEnv>(amas, params) {
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
