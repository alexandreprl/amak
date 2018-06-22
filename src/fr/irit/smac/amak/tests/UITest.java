package fr.irit.smac.amak.tests;

import java.awt.event.WindowEvent;

import fr.irit.smac.amak.ui.MainWindow;

public class UITest {
	boolean closeActionTriggered;
	
	public void testClose() {
		closeActionTriggered = false;
		MainWindow.setWindowTitle("hello");
		MainWindow.addOnCloseAction(w->{
			System.out.println("closed action called");
		});
		MainWindow.instance().dispatchEvent(new WindowEvent(MainWindow.instance(), WindowEvent.WINDOW_CLOSING));
	}
}
