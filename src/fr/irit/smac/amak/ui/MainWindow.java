package fr.irit.smac.amak.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.OptionalDouble;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

/**
 * 
 * @author Marcillaud Guilhem
 *
 */
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2607956693857748227L;
	private JPanel toolbarPanel;
	private DraggableTabbedPane tabbedPane;
	private JMenuBar menuBar;
	private JMenu optionsMenu;
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
		
		tabbedPane = new DraggableTabbedPane();
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		menuBar = new JMenuBar();
		optionsMenu = new JMenu("Options");
		menuBar.add(optionsMenu);
		getContentPane().add(menuBar, BorderLayout.NORTH);
		
		JMenuItem menuItem = new JMenuItem("Close");
		menuItem.addActionListener(l->{
			System.exit(0);
		});
		optionsMenu.add(menuItem);
		
		setVisible(true);
			
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
	 * 			The ToolBar.
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
	 * 			The title
	 * @param panel
	 * 			The panel
	 */
	public static void addTabbedPanel(String title, JPanel panel) {
		instance().tabbedPane.addTab(title, panel);
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
}
