package fr.irit.smac.amak.examples.philosopherslinks;

public class Launch {

	public static void main(String[] args) {
		TableEnvironment env = new TableEnvironment();
		new MyAMAS(env);
	}

}
