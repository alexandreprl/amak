package testutils;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;

public class ObjectsForAgentTesting {

	public final Agent<TestAMAS, TestEnv> agent1;
	public final Agent<TestAMAS, TestEnv> agent2;

	public ObjectsForAgentTesting() {
		TestEnv env = new TestEnv();
		TestAMAS amas = new TestAMAS(env);
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

	private class TestAMAS extends Amas<TestEnv> {
		public TestAMAS(TestEnv environment) {
			super(environment, Scheduling.HIDDEN);
		}
	}
}
