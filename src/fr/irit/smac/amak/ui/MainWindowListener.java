package fr.irit.smac.amak.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainWindowListener implements WindowListener {
	private final List<Consumer<MainWindow>> onCloseActions = new ArrayList<>();
	private MainWindow mainWindow;

	public MainWindowListener(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	public void addOnCLoseAction(Consumer<MainWindow> action) {
		onCloseActions.add(action);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		for (Consumer<MainWindow> consumer : onCloseActions) {
			consumer.accept(this.mainWindow);
		}
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}
