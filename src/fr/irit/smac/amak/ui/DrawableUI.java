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


public class DrawableUI extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7895752986790657855L;
	
	private long sleepTime = 100;
	private boolean running = true;

	private BufferStrategy bufferStrategy;
	private JPanel contentPane;
	private Canvas canvas;

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
		new Thread(this).start();
	}

	@Override
	public void run() {
		onInitialization();
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
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

	protected void onStoppedRunning() {
	}

	protected void onDraw(Graphics2D graphics2D) {
	}

	protected void onInitialization() {
	}

}
