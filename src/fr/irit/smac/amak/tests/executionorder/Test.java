package fr.irit.smac.amak.tests.executionorder;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.tools.Log;

public class Test {

	public static void main(String[] args) {
		Log.minLevel = Log.Level.DEBUG;
		new Test();
	}

	public Test() {
		MyAMAS amas = new MyAMAS(new MyEnvironment());

		new Agent1(amas);
		new Agent2(amas);
		new Agent2(amas);
		new Agent1(amas);

		amas.start();
	}

	private class Agent1 extends Agent<MyAMAS, MyEnvironment> {

		public Agent1(MyAMAS amas) {
			super(amas);
		}

		@Override
		protected int computeExecutionOrderLayer() {
			return 1;
		}

		@Override
		protected void onAct() {
			Log.debug(toString(), getExecutionOrder()+"");
		}
	}

	private class Agent2 extends Agent<MyAMAS, MyEnvironment> {

		public Agent2(MyAMAS amas) {
			super(amas);
		}

		@Override
		protected int computeExecutionOrderLayer() {
			return 2;
		}

		@Override
		protected void onAct() {
			Log.debug(toString(), getExecutionOrder()+"");
		}
	}

	private class MyAMAS extends Amas<MyEnvironment> {

		public MyAMAS(MyEnvironment environment) {
			super(environment, Scheduling.HIDDEN);
		}
		@Override
		protected void onSystemCycleBegin() {
			System.out.println("--");
		}

	}

	private class MyEnvironment extends Environment {

		public MyEnvironment(Object...params) {
			super(Scheduling.DEFAULT, params);
			// TODO Auto-generated constructor stub
		}

	}

}
