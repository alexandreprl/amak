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

/**
 * This class should be overridden by classes aiming at rendering stuffs on a
 * canvas
 * 
 * @author Alexandre Perles
 *
 */
public class DrawableUI extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7895752986790657855L;

	/**
	 * Refresh delay
	 */
	private long sleepTime = 100;
	/**
	 * Is rendering loop running ?
	 */
	private boolean running = true;

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
	 * Create and initialize the frame and the canvas
	 */
	public DrawableUI() {
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

		new Thread(this).start();
	}

	/**
	 * Rendering loop
	 */
	@Override
	public final void run() {
		onInitialization();
		while (running) {
			Graphics2D current = (Graphics2D) bufferStrategy.getDrawGraphics();
			current.setColor(Color.BLACK);
			current.fillRect(0, 0, 800, 600);
			onDraw(current);
			bufferStrategy.show(); // flip back buffer with front buffer?
			current.dispose();
			Toolkit.getDefaultToolkit().sync();
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		onStoppedRunning();
	}

	/**
	 * This method is called when the rendering loop stops
	 */
	protected void onStoppedRunning() {
	}

	/**
	 * This method is called when the canvas must be drawn again
	 * 
	 * @param graphics2D Object used for drawing on the canvas
	 */
	protected void onDraw(Graphics2D graphics2D) {
	}

	/**
	 * This method aims at initializing the rendering system
	 */
	protected void onInitialization() {
	}

}
