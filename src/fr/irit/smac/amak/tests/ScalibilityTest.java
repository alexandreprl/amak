package fr.irit.smac.amak.tests;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.tools.FileHandler;
import fr.irit.smac.amak.tools.Log;

public class ScalibilityTest {

	public static void main(String[] args) {
		new ScalibilityTest();

	}

	private long startTime;

	public ScalibilityTest() {
		Log.minLevel = Log.Level.INFORM;
		runAmasWithNAgents(1);
	}

	private void runAmasWithNAgents(int i) {
		Log.inform("Global state", "start i:%d", i);
		MyAMAS amas = new MyAMAS(new MyEnvironment(), Scheduling.HIDDEN, new Integer(i));
		amas.getScheduler().setOnStop(s -> {
			int nextI = i;
			FileHandler.writeCSVLine("scalability_results.csv", i+"", (System.currentTimeMillis() - startTime)+"");
			switch (i) {
			case 1:
				nextI = 5;
				break;
			case 5:
				nextI = 10;
				break;
			case 10:
				nextI = 100;
				break;
			case 100:
				nextI = 1000;
				break;
			default:
				Log.inform("Global state", "end");
				return;
			}
			runAmasWithNAgents(nextI);
		});
		startTime = System.currentTimeMillis();
		amas.start();
	}

	public class MyAMAS extends Amas<MyEnvironment> {

		public MyAMAS(MyEnvironment environment, Scheduling scheduling, Object... params) {
			super(environment, scheduling, params);
		}

		@Override
		protected void onInitialAgentsCreation() {
			for (int i = 0; i < (Integer) params[0]; i++) {
				new MyAgent(this);
			}
		}

		@Override
		public boolean stopCondition() {
			return cycle == 100;
		}
	}

	public class MyAgent extends Agent<MyAMAS, MyEnvironment> {

		public MyAgent(MyAMAS amas, Object... params) {
			super(amas, params);
		}

		@Override
		protected void onAct() {
			// Sleep the thread to simulate a behavior
			try {
				Thread.sleep((long) ((amas.getEnvironment().getRandom().nextDouble() + 1) * 3));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public class MyEnvironment extends Environment {

		public MyEnvironment(Object...params) {
			super(Scheduling.DEFAULT, params);
		}

	}
}
