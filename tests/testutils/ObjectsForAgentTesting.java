package testutils;

import org.junit.Before;

import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.CommunicatingAgent;
import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;

public class ObjectsForAgentTesting {

	public static final String RAW_ID3 = "agent_ID3";
	public CommunicatingAgent<TestAMAS, TestEnv> communicantAgent1;
	public CommunicatingAgent<TestAMAS, TestEnv> communicantAgent2;
	public CommunicatingAgent<TestAMAS, TestEnv> communicantAgent3;
	public TestAMAS amas;
	public TestEnv env;


	@Before
	public void setup() {
		env = new TestEnv();
		amas = new TestAMAS(env);
		Object params[] = {};
		communicantAgent1 = new CommunicatingAgent<TestAMAS, TestEnv>(amas, params) {
		};

		communicantAgent2 = new CommunicatingAgent<TestAMAS, TestEnv>(amas, params) {
		};

		Object params2[] = { CommunicatingAgent.RAW_ID_PARAM_NAME_PREFIX + RAW_ID3 };
		communicantAgent3 = new CommunicatingAgent<TestAMAS, TestEnv>(amas, params2) {
		};
	}

	public class TestEnv extends Environment {
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
