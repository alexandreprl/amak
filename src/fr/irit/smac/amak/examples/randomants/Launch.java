package fr.irit.smac.amak.examples.randomants;

public class Launch {

	public static void main(String[] args) {
		World env = new World();
		
		AntHill _antHill = new AntHill(env);
		new AntViewer(){
			protected void onInitialConfiguration() {
				this.antHill = _antHill;
			}
		};
	}
}
