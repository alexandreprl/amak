package fr.irit.smac.amak.tests;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Configuration;
import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.tools.FileHandler;
import fr.irit.smac.amak.tools.Log;
import fr.irit.smac.amak.tools.Profiler;

public class ThreadTest {

	public static void main(String[] args) {
		new ThreadTest();

	}


	public ThreadTest() {
		Configuration.allowedSimultaneousAgentsExecution = 4;
		Log.minLevel = Log.Level.INFORM;
		runAmas();
	}

	private void runAmas() {
		MyAMAS amas = new MyAMAS(new MyEnvironment(), Scheduling.HIDDEN);
		amas.getScheduler().setOnStop(s -> {
			Log.inform("time", "Threads used: %d", Configuration.allowedSimultaneousAgentsExecution);
			Log.inform("time", "Elapsed time: %s", Profiler.endHR("amas"));
		});
		Profiler.start("amas");
		amas.start();
	}

	public class MyAMAS extends Amas<MyEnvironment> {

		public MyAMAS(MyEnvironment environment, Scheduling scheduling, Object... params) {
			super(environment, scheduling);
		}

		@Override
		protected void onInitialAgentsCreation() {
			for (int i = 0; i < 100; i++) {
				new MyAgent(this);
			}
		}

		@Override
		public boolean stopCondition() {
			return cycle == 10;
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
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public class MyEnvironment extends Environment {

		public MyEnvironment(Object...params) {
			super(Scheduling.HIDDEN, params);
		}

	}
}
