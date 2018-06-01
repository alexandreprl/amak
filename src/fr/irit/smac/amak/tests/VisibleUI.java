package fr.irit.smac.amak.tests;

import fr.irit.smac.amak.ui.MainWindow;
import fr.irit.smac.amak.ui.VUI;

public class VisibleUI {
	public static void main(String[] args) {
		MainWindow.setWindowTitle("VUI Example");
		MainWindow.setWindowIcon("Resources/ant.png");
		VUI.get().createRectangle(0, 0, 200, 150);
	}
}
