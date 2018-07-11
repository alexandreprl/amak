package fr.irit.smac.amak.ui;

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

import fr.irit.smac.amak.Information;

/**
 * This window is the main one of an AMAS developed using AMAK. It contains a
 * toolbar panel and various spaces for panels
 * 
 * @author Alexandre Perles, Marcillaud Guilhem
 *
 */
public class MainWindow extends JFrame {

	/**
	 * Unique ID meant to handle serialization correctly
	 */
	private static final long serialVersionUID = 2607956693857748227L;
	/**
	 * The panel which contains the toolbar
	 */
	private JPanel toolbarPanel;
	/**
	 * The main panel is split in two panels. This allows to dynamically resize
	 * these two panels.
	 */
	private JSplitPane splitPane;
	/**
	 * The menu bar of the window
	 */
	private JMenuBar menuBar;
	/**
	 * The option menu
	 */
	private JMenu optionsMenu;
	/**
	 * The panel in which panels with tab can be added
	 */
	private JTabbedPane tabbedPanel;
	/**
	 * The listener of the window here to detect the closing of the window and
	 * execute specific actions.
	 */
	private final MainWindowListener mainWindowListener;
	/**
	 * For an AMAK process it can only be one instance of MainWindow
	 */
	private static MainWindow instance;
	/**
	 * Lock present to avoid the creation of a MainWindow while another is creating
	 */
	private static ReentrantLock instanceLock = new ReentrantLock();

	/**
	 * Create the frame.
	 */
	private MainWindow() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainWindowListener = new MainWindowListener(this);
		addWindowListener(mainWindowListener);
		setBounds(100, 100, 450, 300);
		toolbarPanel = new JPanel();
		getContentPane().add(toolbarPanel, BorderLayout.SOUTH);
		toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		tabbedPanel = new JTabbedPane();
		splitPane.setRightComponent(tabbedPanel);

		menuBar = new JMenuBar();
		optionsMenu = new JMenu("Options");
		menuBar.add(optionsMenu);
		getContentPane().add(menuBar, BorderLayout.NORTH);

		JMenuItem menuItem = new JMenuItem("Close");
		menuItem.addActionListener(l -> System.exit(0));
		optionsMenu.add(menuItem);

		menuBar.add(new JMenu("AMAK v" + Information.VERSION));

		setVisible(true);

	}

	/**
	 * Add a close action to the listener
	 * 
	 * @param onClose
	 *            The action to be executed when the window is closed
	 */
	public static void addOnCloseAction(Consumer<MainWindow> onClose) {
		instance().mainWindowListener.addOnCloseAction(onClose);
	}

	/**
	 * Change the icon of the window
	 * 
	 * @param filename
	 *            The filename of the icon
	 */
	public static void setWindowIcon(String filename) {
		ImageIcon img = new ImageIcon(filename);
		instance().setIconImage(img.getImage());
	}

	/**
	 * Change the title of the main window
	 * 
	 * @param title
	 *            The new title
	 */
	public static void setWindowTitle(String title) {
		instance().setTitle(title);
	}

	/**
	 * Add a button in the menu options
	 * 
	 * @param title
	 *            The title of the button
	 * @param listener
	 *            The action to be executed
	 */
	public static void addMenuItem(String title, ActionListener listener) {
		JMenuItem menuItem = new JMenuItem(title);
		menuItem.addActionListener(listener);
		instance().optionsMenu.add(menuItem);
	}

	/**
	 * Add a toolBar
	 * 
	 * @param toolbar
	 *            The ToolBar.
	 */
	public static void addToolbar(JToolBar toolbar) {
		instance().toolbarPanel.add(toolbar);
		instance().pack();
		instance().setVisible(true);
	}

	/**
	 * Set a panel to the left
	 * 
	 * @param panel
	 *            The panel
	 */

	public static void setLeftPanel(JPanel panel) {
		instance().splitPane.setLeftComponent(panel);
		instance().pack();
		instance().setVisible(true);
	}

	/**
	 * Set a panel to the right
	 * 
	 * @param panel
	 *            The panel
	 */
	public static void setRightPanel(JPanel panel) {
		instance().splitPane.setRightComponent(panel);
		instance().pack();
		instance().setVisible(true);
	}

	/**
	 * Return the unique instance of MainWindow, may create it.
	 * 
	 * @return instance
	 */
	public static MainWindow instance() {
		instanceLock.lock();
		if (instance == null) {
			instance = new MainWindow();
		}
		instanceLock.unlock();
		return instance;
	}

	/**
	 * Add a panel with a tab
	 * 
	 * @param title
	 *            The title of the tab
	 * @param panel
	 *            The panel to add
	 */
	public static void addTabbedPanel(String title, JPanel panel) {
		instance().tabbedPanel.addTab(title, panel);
		instance().pack();
		instance().setVisible(true);
	}
}
