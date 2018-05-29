package fr.irit.smac.amak.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Environment;
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
@Deprecated public abstract class DrawableUI<T extends Amas<? extends Environment>> implements Schedulable {
	/**
	 * set fps to 10 to avoid CPU overload
	 */
	public int defaultSleep = 100;

	/**
	 * Drawable canvas
	 */
	private final JPanel canvas;

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
	 * Parameters to initialize the drawable UI
	 */
	protected Object[] params;

	private T amas;

	private int width = 800, height = 600;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * Create and initialize the frame and the canvas
	 * 
	 * @param _scheduling
	 *            the scheduling mode
	 * @param amas
	 *            The amas linked to the drawable UI
	 * @param params
	 *            the parameters that should be passed to the drawable UI
	 *            
	 * @deprecated Should be replaced by VUI
	 */
	@Deprecated  public DrawableUI(Scheduling _scheduling, T amas, Object... params) {
		this.amas = amas;
		this.params = params;

		onInitialConfiguration();
		// setTitle("Drawable #" + id);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(100, 300, 611, 466);
		// contentPane = new JPanel();
		// contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		// contentPane.setLayout(new BorderLayout(0, 0));
		// setContentPane(contentPane);

		canvas = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void paintComponent(java.awt.Graphics g) {

				Image buffer = createImage(this.getSize().width, this.getSize().height);
				Graphics graphics = buffer.getGraphics();
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, this.getSize().width, this.getSize().height);
				graphics.setColor(Color.WHITE);
				graphics.drawRect(0, 0, width, height);
				onDraw((Graphics2D) graphics);
				g.drawImage(buffer, 0, 0, null);
			}
		};
		canvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				onClick(e.getX(), e.getY());
			}
		});
		canvas.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				onMouseDragged(e.getX(), e.getY());
			}
		});

		canvas.setIgnoreRepaint(true);
		canvas.setPreferredSize(new Dimension(width, height));
		// contentPane.add(canvas, BorderLayout.CENTER);

		MainWindow.addTabbedPanel("Drawable #" + id, canvas);
		// pack();
		// setVisible(true);

		if (_scheduling == Scheduling.DEFAULT) {

			this.scheduler = Scheduler.getDefaultScheduler();
			this.scheduler.add(this);
		} else {

			scheduler = new Scheduler(this);

			if (_scheduling == Scheduling.UI)
				MainWindow.addToolbar(new SchedulerToolbar("Drawable #" + id, scheduler));
		}

	}

	/**
	 * This method is called at the very beginning of the DrawableUI creation. Any
	 * configuration should be made here.
	 */
	protected void onInitialConfiguration() {
	}

	protected void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public final void cycle() {
		canvas.repaint();

	}

	/**
	 * This method is called when the canvas must be drawn again
	 * 
	 * @param graphics2D
	 *            Object used for drawing on the canvas
	 */
	protected abstract void onDraw(Graphics2D graphics2D);

	/**
	 * This method is called when the mouse is clicked on the canvas
	 * 
	 * @param x
	 *            X position of the mouse
	 * @param y
	 *            Y position of the mouse
	 */
	protected void onClick(int x, int y) {

	}

	/**
	 * This method is called when the mouse is dragged on the canvas
	 * 
	 * @param x
	 *            X position of the mouse
	 * @param y
	 *            Y position of the mouse
	 */
	protected void onMouseDragged(int x, int y) {

	}

	/**
	 * This method gives access to the scheduler of the DrawableUI
	 * 
	 * @return the scheduler
	 */
	public Scheduler getScheduler() {
		return scheduler;
	}

	/**
	 * Helper method to launch the scheduler
	 */
	public void start() {
		getScheduler().start();
	}

	/**
	 * This method allows the system to stop the scheduler on certain conditions
	 * 
	 * @return whether or not the scheduler must stop.
	 */
	@Override
	public boolean stopCondition() {
		return false;
	}

	/**
	 * Getter for amas
	 * 
	 * @return the linked amas
	 */
	public T getAmas() {
		return amas;
	}

	@Override
	public void onSchedulingStarts() {
	}

	@Override
	public void onSchedulingStops() {
	}
}
