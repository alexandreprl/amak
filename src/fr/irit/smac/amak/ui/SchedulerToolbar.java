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
	 * 
	 */
	private static final long serialVersionUID = 2152445838931621997L;

	private JSlider runController;

	private Scheduler scheduler;

	private String title;

	public SchedulerToolbar(String _title, Scheduler scheduler) {
		this.title = _title;
		this.scheduler = scheduler;
		this.scheduler.setOnStop(s -> {
			getSlider().setValue(1);
		});
		this.scheduler.setOnChange(s -> {
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

	public JSlider getSlider() {
		if (runController == null) {
			runController = new JSlider(SwingConstants.HORIZONTAL, 0, 7, 1);
			runController.setBorder(BorderFactory.createTitledBorder(this.title));

			final Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
			labelTable.put(new Integer(0), new JLabel("Step"));
			labelTable.put(new Integer(1), new JLabel("Stop"));
			labelTable.put(new Integer(2), new JLabel("x1"));
			labelTable.put(new Integer(3), new JLabel("x10"));
			labelTable.put(new Integer(4), new JLabel("x50"));
			labelTable.put(new Integer(5), new JLabel("x100"));
			labelTable.put(new Integer(6), new JLabel("x500"));
			labelTable.put(new Integer(7), new JLabel("MAX"));
			runController.setLabelTable(labelTable);

			runController.setPaintLabels(true);
			runController.addChangeListener(l -> {
				switch (runController.getValue()) {
				case 0:
					scheduler.step();
					break;
				case 1:
					scheduler.stop();
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
				}
			});
		}
		return runController;
	}

}
