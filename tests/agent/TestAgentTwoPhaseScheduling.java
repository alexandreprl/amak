package agent;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.CommunicatingAgent;
import fr.irit.smac.amak.Configuration;
import fr.irit.smac.amak.Scheduling;
import testutils.ObjectsForAgentTesting;
import testutils.ObjectsForAgentTesting.TestEnv;

public class TestAgentTwoPhaseScheduling {

	private AtomicInteger perceptionIsDone;
	private AtomicInteger decideAndActDoneWithoutAllPerceptions;
	private Semaphore semaphoreToWaitAgentEnding;
	private TestAMAS amas;
	private static final Semaphore semaphoreToCreateBlockingState = new Semaphore(1);

	@Before
	public void setup() {
		ObjectsForAgentTesting o = new ObjectsForAgentTesting();
		TestEnv env = o.new TestEnv();
		amas = new TestAMAS(env);
		perceptionIsDone = new AtomicInteger(0);
		decideAndActDoneWithoutAllPerceptions = new AtomicInteger(0);
		semaphoreToWaitAgentEnding = new Semaphore(2);
	}

	@Test
	public void testTwoPhaseSchedulingBlockingState() throws InterruptedException {
		Object[] params = { CommunicatingAgent.RAW_ID_PARAM_NAME_PREFIX + "1" };
		new MyAgentTest(amas, params);
		Object[] params2 = { CommunicatingAgent.RAW_ID_PARAM_NAME_PREFIX + "2" };
		new MyAgentTest(amas, params2);
		semaphoreToWaitAgentEnding.acquire();
		semaphoreToWaitAgentEnding.acquire();

		Runnable r = () -> {
				amas.cycle();
				amas.cycle();
		};
		(new Thread(r)).start();

		System.out.println("main : wait for token");
		boolean hasToken = semaphoreToWaitAgentEnding.tryAcquire(2, 10, TimeUnit.SECONDS);
		System.out.println("main : token release or expire, hasToken=" + hasToken);
		assertFalse("None token should be available !", hasToken);
		assertEquals(1, perceptionIsDone.get());
		assertEquals(0, decideAndActDoneWithoutAllPerceptions.get());
	}

	private class MyAgentTest extends CommunicatingAgent<TestAMAS, TestEnv> {

		public MyAgentTest(TestAMAS amas, Object[] params) {
			super(amas, params);
		}

		@Override
		protected void onPerceive() {
			super.onPerceive();
			System.out.println("onPerceive " + getAID() + " a = " + perceptionIsDone.get());
			try {
				// only the first agent can acquire a token
				// a blocking state should be create
				semaphoreToCreateBlockingState.acquire();
				perceptionIsDone.incrementAndGet();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("onPerceive " + getAID() + " finish.");
		}

		@Override
		protected void onDecideAndAct() {
			super.onDecideAndAct();
			// should not happen because agent should be block indefinitely
			semaphoreToCreateBlockingState.release();
			System.out.println("onDecideAndAct " + getAID() + " a = " + perceptionIsDone.get());
			decideAndActDoneWithoutAllPerceptions.incrementAndGet();
			semaphoreToWaitAgentEnding.release();
			System.out.println("onDecideAndAct " + getAID() + " token release.");
		}
	};

	public class TestAMAS extends Amas<TestEnv> {
		public TestAMAS(TestEnv environment) {
			super(environment, Scheduling.HIDDEN);
		}

		@Override
		protected void onInitialConfiguration() {
			super.onInitialConfiguration();
			Configuration.executionPolicy = ExecutionPolicy.TWO_PHASES;
		}

	}

}
