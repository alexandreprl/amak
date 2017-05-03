package fr.irit.smac.amak.ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fr.irit.smac.amak.Schedulable;
import fr.irit.smac.amak.Scheduler;
import fr.irit.smac.amak.Scheduling;

/**
 * This class should be overridden by classes aiming at rendering stuffs on a
 * canvas
 * 
 * @author Alexandre Perles
 *
 */
public abstract class DrawableUI extends JFrame implements Schedulable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7895752986790657855L;

	/**
	 * Buffer strategy aiming at increasing the performance of the rendering
	 * system
	 */
	private final BufferStrategy bufferStrategy;
	/**
	 * Panel for the canvas
	 */
	private final JPanel contentPane;
	/**
	 * Drawable canvas
	 */
	private final Canvas canvas;

	/**
	 * Scheduler for the drawing frame
	 */
	private Scheduler scheduler;

	/**
	 * Unique index for giving unique id to each drawable ui
	 */
	private static int uniqueIndex;

	/**
	 * Unique id of the drawable ui
	 */
	private final int id = uniqueIndex++;

	/**
	 * Create and initialize the frame and the canvas
	 * 
	 * @param _scheduling
	 *            the scheduling mode
	 */
	public DrawableUI(Scheduling _scheduling) {
		setTitle("Drawable #" + id);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 611, 466);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(800, 600));
		contentPane.add(canvas, BorderLayout.CENTER);

		pack();
		setVisible(true);

		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		

		scheduler = new Scheduler(this, Scheduling.hasAutostart(_scheduling));

		if (Scheduling.isManual(_scheduling))
			Toolbar.add(new SchedulerToolbar("Drawable #" + id, scheduler));
	}

	@Override
	public final void cycle() {
		Graphics2D current = (Graphics2D) bufferStrategy.getDrawGraphics();
		current.setColor(Color.BLACK);
		current.fillRect(0, 0, 800, 600);
		onDraw(current);
		bufferStrategy.show(); // flip back buffer with front buffer?
		current.dispose();
		Toolkit.getDefaultToolkit().sync();
	}

	/**
	 * This method is called when the canvas must be drawn again
	 * 
	 * @param graphics2D
	 *            Object used for drawing on the canvas
	 */
	protected abstract void onDraw(Graphics2D graphics2D);
}
