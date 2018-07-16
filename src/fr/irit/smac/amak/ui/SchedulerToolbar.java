package fr.irit.smac.amak.ui;

import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import fr.irit.smac.amak.Scheduler;

/**
 * Runner to control manually the execution of the mas
 * 
 * @author Alexandre Perles
 *
 */
public class SchedulerToolbar extends JToolBar {
	/**
	 * Unique ID meant to handle serialization correctly
	 */
	private static final long serialVersionUID = 2152445838931621997L;

	/**
	 * The slider which controls the speed
	 */
	private JSlider runController;

	/**
	 * The scheduler to which the toolbar is associated
	 */
	private Scheduler scheduler;

	/**
	 * The title of the toolbar
	 */
	private String title;

	/**
	 * Constructor of the toolbar
	 * 
	 * @param title
	 *            The title of the toolbar
	 * @param scheduler
	 *            The scheduler to which the toolbar is associated
	 * 
	 */
	public SchedulerToolbar(String title, Scheduler scheduler) {
		this.title = title;
		this.scheduler = scheduler;
		this.scheduler.setOnStop(s -> getSlider().setValue(1));
		this.scheduler.addOnChange(s -> {
			if (s.isRunning()) {
				switch (s.getSleep()) {
				case 1000:
					getSlider().setValue(2);
					break;
				case 100:
					getSlider().setValue(3);
					break;
				case 20:
					getSlider().setValue(4);
					break;
				case 10:
					getSlider().setValue(5);
					break;
				case 2:
					getSlider().setValue(6);
					break;
				case 0:
					getSlider().setValue(7);
					break;
				default:
					getSlider().setValue(1);
				}
			} else {
				getSlider().setValue(1);
			}
		});
		add(getSlider());
		setPreferredSize(new Dimension(300, 100));
	}

	/**
	 * Get or create the slider component
	 * 
	 * @return the slider
	 */
	public JSlider getSlider() {
		if (runController == null) {
			runController = new JSlider(SwingConstants.HORIZONTAL, 0, 7, 1);
			runController.setBorder(BorderFactory.createTitledBorder(this.title));

			// Hashtable is not recommended anymore and should be replaced by an HashMap but
			// JSlider requires Hashtable so Hashtable it will have.
			final Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
			labelTable.put(0, new JLabel("Step"));
			labelTable.put(1, new JLabel("Stop"));
			labelTable.put(2, new JLabel("x1"));
			labelTable.put(3, new JLabel("x10"));
			labelTable.put(4, new JLabel("x50"));
			labelTable.put(5, new JLabel("x100"));
			labelTable.put(6, new JLabel("x500"));
			labelTable.put(7, new JLabel("MAX"));

			runController.setLabelTable(labelTable);

			runController.setPaintLabels(true);
			runController.addChangeListener(l -> {
				switch (runController.getValue()) {
				case 0:
					scheduler.step();
					break;
				case 2:
					scheduler.startWithSleep(1000);
					break;
				case 3:
					scheduler.startWithSleep(100);
					break;
				case 4:
					scheduler.startWithSleep(20);
					break;
				case 5:
					scheduler.startWithSleep(10);
					break;
				case 6:
					scheduler.startWithSleep(2);
					break;
				case 7:
					scheduler.startWithSleep(0);
					break;
				case 1:
				default:
					scheduler.stop();
					break;
				}
			});
		}
		return runController;
	}

}
