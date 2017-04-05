package fr.irit.smac.amak.ui;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class Toolbar extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2607956693857748227L;
	private JPanel contentPane;
	private static Toolbar instance;

	/**
	 * Create the frame.
	 */
	private Toolbar() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		setVisible(true);
			
	}
	public static void add(JToolBar toolbar) {
		instance().getContentPane().add(toolbar);
		instance().pack();
		instance().setVisible(true);
	}
	private static Toolbar instance() {
		if (instance == null) {
			instance = new Toolbar();
		}
		return instance;
	}
}
