package fr.irit.smac.amak.examples.philosophers;

public class Launch {

	public static void main(String[] args) {
		TableEnvironment env = new TableEnvironment();
		new MyAMAS(env);
	}

}
