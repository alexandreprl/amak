package fr.irit.smac.amak.ui;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

public class ToolbarWindow extends JFrame {

	private JPanel contentPane;


	/**
	 * Create the frame.
	 */
	public ToolbarWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		setVisible(true);
			
	}
	public void addToolbar(JToolBar toolbar) {
		contentPane.add(toolbar);
		pack();
		setVisible(true);
	}
}
