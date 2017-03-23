package fr.irit.smac.amak.runner;

import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import fr.irit.smac.amak.Amas;

/**
 * Runner to control manually the execution of the system Atena
 * 
 * @author Alexandre Perles
 *
 */
public class ControllableRunner extends Runner implements Runnable {
	private enum State {
		NONE, INITIALIZED, RUNNING, STEPPING, STOPPED
	}

	private State state = State.NONE;
	private final JFrame frame;
	private JSlider runController;
	private int sleep;
	private boolean started = false;

	public ControllableRunner(Amas<?> amas) {
		this(amas, false);
	}

	public ControllableRunner(Amas<?> amas, final boolean autorun) {
		super(amas);
		frame = new JFrame("Controllable runner");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(getRunController());
		frame.getContentPane().setPreferredSize(new Dimension(300, 100));
		frame.pack();
		frame.setVisible(true);
		state = State.INITIALIZED;
		if (autorun) {
			getRunController().setValue(6);
			startWithSleep(0);
		}
	}

	public JSlider getRunController() {
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
			// runController.setEnabled(false);
			runController.setPaintLabels(true);
			runController.addChangeListener(l -> {
				switch (runController.getValue()) {
				case 0:
					step();
					runController.setValue(1);
					break;
				case 1:
					stop();
					break;
				case 2:
					startWithSleep(1000);
					break;
				case 3:
					startWithSleep(100);
					break;
				case 4:
					startWithSleep(20);
					break;
				case 5:
					startWithSleep(10);
					break;
				case 6:
					startWithSleep(0);
					break;
				}
			});
		}
		return runController;
	}

	@Override
	public void run() {
		if (!started) {
			started = true;
		}
		boolean stopCondition;
		do {
			cycle();
			if (sleep != 0) {
				try {
					Thread.sleep(sleep);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
			 stopCondition = amas.stopCondition();
		} while (state == State.RUNNING && !stopCondition);
		
		setState(State.STOPPED);
		if (stopCondition) {
			getRunController().setValue(1);
		}
	}

	private void setState(final State _state) {
		switch (_state) {
		case RUNNING:
			if (state != State.RUNNING && state != State.STEPPING) {
				new Thread(this).start();
			}
			break;
		case STEPPING:
			if (state != State.RUNNING && state != State.STEPPING) {
				new Thread(this).start();
			}
			break;
		default:
			break;
		}
		state = _state;
	}

	private void startWithSleep(int _sleep) {
		sleep = _sleep;
		setState(State.RUNNING);
	}

	private void step() {
		setState(State.STEPPING);
	}

	private void stop() {
		setState(State.STOPPED);
	}
}
