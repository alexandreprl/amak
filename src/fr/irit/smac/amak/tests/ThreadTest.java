package fr.irit.smac.amak.tests;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Amas.ExecutionPolicy;
import fr.irit.smac.amak.Configuration;
import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.tools.Log;
import fr.irit.smac.amak.tools.Profiler;
import fr.irit.smac.amak.ui.DrawableUI;

public class ThreadTest {

	public static void main(String[] args) {
		new ThreadTest();

	}

	public ThreadTest() {
		Configuration.allowedSimultaneousAgentsExecution = 1;
		Configuration.executionPolicy = ExecutionPolicy.ONE_PHASE;
		Log.minLevel = Log.Level.INFORM;
		runAmas();
	}

	private void runAmas() {
		MyAMAS amas = new MyAMAS(new MyEnvironment(), Scheduling.DEFAULT);
		amas.getScheduler().setOnStop(s -> {
			Log.inform("time", "Threads used: %d", Configuration.allowedSimultaneousAgentsExecution);
			Log.inform("time", "Elapsed time: %s", Profiler.endHR("amas"));
		});
		new AgentPhaseDrawer(amas);
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
			return cycle == 100000;
		}
	}

	public class MyAgent extends Agent<MyAMAS, MyEnvironment> {

		public MyAgent(MyAMAS amas, Object... params) {
			super(amas, params);
		}

		@Override
		protected void onPerceive() {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onAct() {
			// Sleep the thread to simulate a behavior
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public class MyEnvironment extends Environment {

		public MyEnvironment(Object... params) {
			super(Scheduling.DEFAULT, params);
		}
	}

	public class AgentPhaseDrawer extends DrawableUI<MyAMAS> {

		public AgentPhaseDrawer(MyAMAS amas) {
			super(Scheduling.UI, amas);
		}

		@Override
		protected void onDraw(Graphics2D graphics2d) {
			int x = 0, y = 0;
			for (Agent<? extends Amas<MyEnvironment>, MyEnvironment> a : getAmas().getAgents()) {
				switch (a.getCurrentPhase()) {
				case DECISION_AND_ACTION:
					graphics2d.setColor(Color.GREEN);
					break;
				case DECISION_AND_ACTION_DONE:
					graphics2d.setColor(Color.BLUE);
					break;
				case PERCEPTION:
					graphics2d.setColor(Color.ORANGE);
					break;
				case PERCEPTION_DONE:
					graphics2d.setColor(Color.RED);
					break;
				default:
					break;
				
				}
				x = (a.getId()%10)*10;
				y = ((int)(a.getId()/10))*10;
				graphics2d.fillRect(x, y, 9, 9);

				graphics2d.setColor(Color.WHITE);
				graphics2d.drawString(""+getAmas().getPerceptionPhaseSemaphore().availablePermits(), 10,200);
				graphics2d.drawString(""+getAmas().getDecisionAndActionPhasesSemaphore().availablePermits(), 10,220);
			}
		}

	}
}
