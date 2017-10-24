package fr.irit.smac.amak.examples.randomants;

public class AntsLaunchExample {

	public static void main(String[] args) {
		WorldExample env = new WorldExample();
		
		AntHillExample _antHill = new AntHillExample(env);
		new AntViewerExample(){
			protected void onInitialConfiguration() {
				this.antHill = _antHill;
			}
		}.start();
	}
}
