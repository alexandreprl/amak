package fr.irit.smac.amak.ui;

import fr.irit.smac.amak.Information;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

/**
 * This window is the main one of an AMAS developed using AMAK. It contains a
 * toolbar panel and various spaces for panels
 *
 */
public class MainWindow {
	/**
	 * The main frame
	 */
	private final JFrame mainFrame;
	/**
	 * The panel which contains the toolbar
	 */
	private final JPanel toolbarPanel;
	/**
	 * The main panel is split in two panels. This allows to dynamically resize
	 * these two panels.
	 */
	private final JSplitPane splitPane;
	/**
	 * The option menu
	 */
	private final JMenu optionsMenu;
	/**
	 * The panel in which panels with tab can be added
	 */
	private final JTabbedPane tabbedPanel;
	/**
	 * The listener of the window here to detect the closing of the window and
	 * execute specific actions.
	 */
	private final MainWindowListener mainWindowListener;

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindowListener = new MainWindowListener(this);
		mainFrame.addWindowListener(mainWindowListener);
		mainFrame.setBounds(100, 100, 450, 300);
		toolbarPanel = new JPanel();
		mainFrame.getContentPane().add(toolbarPanel, BorderLayout.SOUTH);
		toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainFrame.getContentPane().add(splitPane, BorderLayout.CENTER);

		tabbedPanel = new JTabbedPane();
		splitPane.setRightComponent(tabbedPanel);

		optionsMenu = setupMenu();

		mainFrame.setVisible(true);

	}

	private JMenu setupMenu() {
		final JMenu optionsMenu;
		JMenuBar menuBar = new JMenuBar();
		optionsMenu = new JMenu("Options");
		menuBar.add(optionsMenu);
		mainFrame.getContentPane().add(menuBar, BorderLayout.NORTH);

		JMenuItem menuItem = new JMenuItem("Close");
		menuItem.addActionListener(l -> System.exit(0));
		optionsMenu.add(menuItem);

		menuBar.add(new JMenu("AMAK v" + Information.VERSION));
		return optionsMenu;
	}

	/**
	 * Add a close action to the listener
	 * 
	 * @param onClose
	 *            The action to be executed when the window is closed
	 */
	public void addOnCloseAction(Consumer<MainWindow> onClose) {
		mainWindowListener.addOnCloseAction(onClose);
	}

	/**
	 * Change the icon of the window
	 * 
	 * @param filename
	 *            The filename of the icon
	 */
	public void setWindowIcon(String filename) {
		ImageIcon img = new ImageIcon(filename);
		mainFrame.setIconImage(img.getImage());
	}

	/**
	 * Change the title of the main window
	 * 
	 * @param title
	 *            The new title
	 */
	public void setWindowTitle(String title) {
		mainFrame.setTitle(title);
	}

	/**
	 * Add a button in the menu options
	 * 
	 * @param title
	 *            The title of the button
	 * @param listener
	 *            The action to be executed
	 */
	public void addMenuItem(String title, ActionListener listener) {
		JMenuItem menuItem = new JMenuItem(title);
		menuItem.addActionListener(listener);
		optionsMenu.add(menuItem);
	}

	/**
	 * Add a toolBar
	 *
	 * @param toolbar
	 *            The ToolBar.
	 */
	public void addToolbar(JToolBar toolbar) {
		toolbarPanel.add(toolbar);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	/**
	 * Remove all toolbars
	 *
	 */
	public void removeToolbars() {
		toolbarPanel.removeAll();
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	/**
	 * Set a panel to the left
	 * 
	 * @param panel
	 *            The panel
	 */

	public void setLeftPanel(JPanel panel) {
		splitPane.setLeftComponent(panel);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	/**
	 * Add a panel with a tab
	 * 
	 * @param title
	 *            The title of the tab
	 * @param panel
	 *            The panel to add
	 */
	public void addTabbedPanel(String title, JPanel panel) {
		tabbedPanel.addTab(title, panel);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
}
