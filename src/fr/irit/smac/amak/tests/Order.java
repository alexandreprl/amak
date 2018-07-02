package fr.irit.smac.amak.tests;

import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Configuration;
import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.tools.Log;
import fr.irit.smac.lxplot.LxPlot;

public class Order {
	public boolean lastExecutedWasEnvironment;
	public boolean firstFound = false;
	public static void main(String[] args) {
		new Order();
		
		
	}
	public Order() {
		Configuration.commandLineMode = true;
		new MyAMAS().start();
	}
	
	public class MyAMAS extends Amas<MyEnv> {

		public MyAMAS() {
			super(new MyEnv(), Scheduling.DEFAULT);
		}
		@Override
		public boolean stopCondition() {
			return cycle==100000000;
		}
		@Override
		protected void onSystemCycleEnd() {
			if (!firstFound) {
				Log.debug("test","First is MAS");
				firstFound = true;
			}
			
			if (!lastExecutedWasEnvironment) {
				Log.fatal("test", "last executed was not the environment");
				System.exit(-1);
			}
			lastExecutedWasEnvironment = false;
		}
	}
	public class MyEnv extends Environment {

		public MyEnv() {
			super(Scheduling.DEFAULT);
		}
		@Override
		public void onCycle() {
			if (!firstFound) {
				Log.debug("test","First is Environment");
				firstFound = true;
			}
			
			lastExecutedWasEnvironment = true;
		}
	}

}
