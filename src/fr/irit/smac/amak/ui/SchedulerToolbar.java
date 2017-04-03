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

	public SchedulerToolbar(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.scheduler.setOnStop(s->{
			getSlider().setValue(1);
		});
		add(getSlider());
		if (this.scheduler.isAutorun()) {
			getSlider().setValue(6);
		}
	}

	public JSlider getSlider() {
		if (runController == null) {
			runController = new JSlider(SwingConstants.HORIZONTAL, 0, 6, 1);
			runController.setBorder(BorderFactory.createTitledBorder("Speed"));
			runController.setSize(new Dimension(300, 100));
			final Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
			labelTable.put(new Integer(0), new JLabel("Step"));
			labelTable.put(new Integer(1), new JLabel("Stop"));
			labelTable.put(new Integer(2), new JLabel("x1"));
			labelTable.put(new Integer(3), new JLabel("x10"));
			labelTable.put(new Integer(4), new JLabel("x50"));
			labelTable.put(new Integer(5), new JLabel("x100"));
			labelTable.put(new Integer(6), new JLabel("MAX"));
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
					scheduler.startWithSleep(0);
					break;
				}
			});
		}
		return runController;
	}

}
