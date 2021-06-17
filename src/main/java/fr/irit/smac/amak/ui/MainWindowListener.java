package fr.irit.smac.amak.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Listener for the {@link MainWindow}
 * 
 * @author Perles Alexandre
 *
 */
public class MainWindowListener implements WindowListener {
	/**
	 * Actions that must be executed when the MainWindow is closed
	 */
	private final List<Consumer<MainWindow>> onCloseActions = new ArrayList<>();
	/**
	 * The MainWindow linked to this listener
	 */
	private MainWindow mainWindow;

	/**
	 * The constructor of the listener
	 * 
	 * @param mainWindow
	 *            The MainWindow linked to this listener
	 */
	public MainWindowListener(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// At the time of the creation of this listener no need has been expressed for
		// this method
	}

	/**
	 * Add the action "action" in the list of actions that must be executed when the
	 * window is closed
	 * 
	 * @param action
	 *            The action to be executed
	 */
	public void addOnCloseAction(Consumer<MainWindow> action) {
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
		// At the time of the creation of this listener no need has been expressed for
		// this method
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// At the time of the creation of this listener no need has been expressed for
		// this method
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// At the time of the creation of this listener no need has been expressed for
		// this method
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// At the time of the creation of this listener no need has been expressed for
		// this method
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// At the time of the creation of this listener no need has been expressed for
		// this method
	}

}
