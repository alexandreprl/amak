package fr.irit.smac.amak.tests;

import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Configuration;
import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.tools.Log;
import fr.irit.smac.lxplot.LxPlot;

public class CommandLineOnlyResolution {

	public static void main(String[] args) {
		new CommandLineOnlyResolution();
	}
	public CommandLineOnlyResolution() {
		Configuration.commandLineMode = true;
		new MyAMAS().start();
	}
	
	public class MyAMAS extends Amas<MyEnv> {

		public MyAMAS() {
			super(new MyEnv(), Scheduling.DEFAULT);
		}
		@Override
		public boolean stopCondition() {
			return cycle==100;
		}
		@Override
		protected void onSystemCycleEnd() {
			Log.debug("test", "yolo");
		}
		@Override
		protected void onUpdateRender() {
			LxPlot.getChart("amas").add(0, 1);
		}
	}
	public class MyEnv extends Environment {

		public MyEnv() {
			super(Scheduling.DEFAULT);
		}
		@Override
		protected void onUpdateRender() {
			LxPlot.getChart("test").add(0, 1);
		}
	}

}
