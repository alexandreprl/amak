package fr.irit.smac.amak.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.ReentrantLock;

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
 * 
 * @author Alexandre Perles, Marcillaud Guilhem
 *
 */
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2607956693857748227L;
	private JPanel toolbarPanel;
	private JSplitPane splitPane;
	private JMenuBar menuBar;
	private JMenu optionsMenu;
	private JTabbedPane tabbedPanel;
	private static MainWindow instance;
	private static ReentrantLock instanceLock = new ReentrantLock();

	/**
	 * Create the frame.
	 */
	private MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		menuItem.addActionListener(l -> {
			System.exit(0);
		});
		optionsMenu.add(menuItem);

		menuBar.add(new JMenu("AMAK v" + Information.VERSION));

		setVisible(true);

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
	 * @param title The new title
	 */
	public static void setWindowTitle(String title) {
		instance().setTitle(title);
	}

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
	 * Add a tabbedPanel
	 * 
	 * @param title
	 *            The title
	 * @param panel
	 *            The panel
	 */

	public static void setLeftPanel(String title, JPanel panel) {
		instance().splitPane.setLeftComponent(panel);
		instance().pack();
		instance().setVisible(true);
	}

	public static void setRightPanel(String title, JPanel panel) {
		instance().splitPane.setRightComponent(panel);
		instance().pack();
		instance().setVisible(true);
	}

	/**
	 * Return the unique instance of MainWindow, may create it.
	 * 
	 * @return instance
	 */
	private static MainWindow instance() {
		instanceLock.lock();
		if (instance == null) {
			instance = new MainWindow();
		}
		instanceLock.unlock();
		return instance;
	}

	public static void addTabbedPanel(String title, JPanel panel) {
		instance().tabbedPanel.addTab(title, panel);
		instance().pack();
		instance().setVisible(true);
	}
}
