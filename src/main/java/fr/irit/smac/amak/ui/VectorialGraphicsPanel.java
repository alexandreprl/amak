package fr.irit.smac.amak.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import fr.irit.smac.amak.ui.drawables.Drawable;
import fr.irit.smac.amak.ui.drawables.DrawableImage;
import fr.irit.smac.amak.ui.drawables.DrawablePoint;
import fr.irit.smac.amak.ui.drawables.DrawableRectangle;
import fr.irit.smac.amak.ui.drawables.DrawableString;

/**
 * 
 * Vectorial UI: This class allows to create dynamic rendering with zoom and
 * move capacities
 *
 */
public class VectorialGraphicsPanel extends JPanel {
	/**
	 * List of objects currently being drawn by the VUI
	 */
	private final List<Drawable> drawables = new ArrayList<>();
	/**
	 * Lock to avoid concurrent modification on the list {@link #drawables}
	 */
	private final ReentrantLock drawablesLock = new ReentrantLock();

	/**
	 * A static map to facilitate access to different instances of VUI
	 */
	private static final Map<String, VectorialGraphicsPanel> instances = new HashMap<>();

	/**
	 * The horizontal offset of the drawing zone. Used to allow the user to move the
	 * view.
	 */
	private double worldOffsetX;

	/**
	 * The vertical offset of the drawing zone. Used to allow the user to move the
	 * view.
	 */
	private double worldOffsetY;

	/**
	 * The last horizontal position of the mouse when dragging
	 */
	protected Integer lastDragX;

	/**
	 * The last vertical position of the mouse when dragging
	 */
	protected Integer lastDragY;

	/**
	 * The canvas on which all is drawn
	 */
	private final JPanel canvas;

	/**
	 * Label aiming at showing information about the VUI (zoom and offset)
	 */
	private final JLabel statusLabel;

	/**
	 * The default value of the {@link #zoom}
	 */
	private double defaultZoom = 100;
	/**
	 * The default horizontal position of the view
	 */
	private double defaultWorldCenterX = 0;
	/**
	 * The default vertical position of the view
	 */
	private double defaultWorldCenterY = 0;
	/**
	 * The value of the zoom. 100 means 1/1 scale
	 */
	protected double zoom = defaultZoom;

	/**
	 * The horizontal position of the view
	 */
	private double worldCenterX = defaultWorldCenterX;

	/**
	 * The vertical position of the view
	 */
	private double worldCenterY = defaultWorldCenterY;

	/**
	 * Constructor of the VUI. This one is private as it can only be created through
	 * static method.
	 * 
	 * @param title
	 *            The title used for the vui
	 */
	public VectorialGraphicsPanel(String title) {
		super(new BorderLayout());

		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(l -> {
			zoom = defaultZoom;
			worldCenterX = defaultWorldCenterX;
			worldCenterY = defaultWorldCenterY;
			updateCanvas();
		});
		statusPanel.add(resetButton);

		canvas = new JPanel() {

			@Override
			protected void paintComponent(Graphics g) {

				final Graphics2D g2 = (Graphics2D) g;

				final int w = getSize().width;
				final int h = getSize().height;

				setWorldOffsetX(worldCenterX + screenToWorldDistance(w / 2));
				setWorldOffsetY(worldCenterY + screenToWorldDistance(h / 2));

				g.setColor(new Color(0.96f, 0.96f, 0.96f));
				g.fillRect(0, 0, w, h);
				drawablesLock.lock();
				drawables.sort(Comparator.comparingInt(Drawable::getLayer));
				for (Drawable d : drawables) {
					d.onDraw(g2);
				}
				drawablesLock.unlock();
			}
		};
		canvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// This method is not meant to be used as everything is handled by other methods
			}

			@Override
			public void mousePressed(MouseEvent e) {

				lastDragX = e.getX();
				lastDragY = e.getY();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lastDragX = null;
				lastDragY = null;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// This method is not meant to be used as everything is handled by other methods
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// This method is not meant to be used yet
			}
		});
		canvas.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// This method is not meant to be used as everything is handled by other methods
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				try {
					worldCenterX += screenToWorldDistance(e.getX() - lastDragX);
					worldCenterY += screenToWorldDistance(e.getY() - lastDragY);
					lastDragX = e.getX();
					lastDragY = e.getY();
					updateCanvas();
				} catch (Exception ez) {
					// Catch exception occurring when mouse is out of the canvas
				}
			}
		});
		canvas.addMouseWheelListener(e -> {
			double wdx = screenToWorldDistance((int) (canvas.getSize().getWidth() / 2 - e.getX()));
			double wdy = screenToWorldDistance((int) (canvas.getSize().getHeight() / 2 - e.getY()));

			zoom += e.getWheelRotation() * 10;
			if (zoom < 10)
				zoom = 10;

			double wdx2 = screenToWorldDistance((int) (canvas.getSize().getWidth() / 2 - e.getX()));
			double wdy2 = screenToWorldDistance((int) (canvas.getSize().getHeight() / 2 - e.getY()));

			worldCenterX -= wdx2 - wdx;
			worldCenterY -= wdy2 - wdy;
			updateCanvas();
		});
		canvas.setPreferredSize(new Dimension(800, 600));
		add(canvas, BorderLayout.CENTER);
		add(statusPanel, BorderLayout.SOUTH);
	}

	/**
	 * Convert a distance in the world to its equivalent on the screen
	 * 
	 * @param d
	 *            the in world distance
	 * @return the on-screen distance
	 */
	public int worldToScreenDistance(double d) {
		return (int) (d * getZoomFactor());
	}

	/**
	 * Convert a distance on the screen to its equivalent in the world
	 * 
	 * @param d
	 *            the on-screen distance
	 * @return the in world distance
	 */
	public double screenToWorldDistance(int d) {
		return ((double) d / getZoomFactor());
	}

	/**
	 * Convert an X in the world to its equivalent on the screen
	 * 
	 * @param x
	 *            the X in world
	 *
	 * @return the X on screen distance
	 */
	public int worldToScreenX(double x) {
		return (int) ((x + getWorldOffsetX()) * getZoomFactor());
	}

	/**
	 * A value that must be multiplied to scale objects
	 * 
	 * @return the zoom factor
	 */
	public double getZoomFactor() {
		return zoom / 100;
	}

	/**
	 * Convert a Y in the world to its equivalent on the screen
	 * 
	 * @param y
	 *            the Y in world
	 *
	 * @return the Y on screen distance
	 */
	public int worldToScreenY(double y) {
		return (int) ((y + getWorldOffsetY()) * getZoomFactor());
	}

	/**
	 * Convert an X on the screen to its equivalent in the world
	 * 
	 * @param x
	 *            the X on screen
	 *
	 * @return the X in the world distance
	 */
	public double screenToWorldX(double x) {
		return (x) / getZoomFactor() - getWorldOffsetX();
	}

	/**
	 * Convert a Y on the screen to its equivalent in the world
	 * 
	 * @param y
	 *            the Y on screen
	 *
	 * @return the Y in the world distance
	 */
	public double screenToWorldY(double y) {
		return (y) / getZoomFactor() - getWorldOffsetY();
	}

	/**
	 * Add an object to the VUI and repaint it
	 * 
	 * @param d
	 *            the new object
	 */
	public void add(Drawable d) {
		d.setPanel(this);
		drawablesLock.lock();
		drawables.add(d);
		drawablesLock.unlock();
		updateCanvas();
	}

	public void remove(Drawable d) {
		drawablesLock.lock();
		drawables.remove(d);
		drawablesLock.unlock();
		updateCanvas();
	}

	/**
	 * Refresh the canvas
	 */
	public void updateCanvas() {
		canvas.repaint();

		statusLabel.setText(String.format("Zoom: %.2f Center: (%.2f,%.2f)", zoom, worldCenterX, worldCenterY));
	}

	/**
	 * Get the width of the canvas
	 * 
	 * @return the canvas width
	 */
	public double getCanvasWidth() {
		return canvas.getSize().getWidth();
	}

	/**
	 * Get the height of the canvas
	 * 
	 * @return the canvas height
	 */
	public double getCanvasHeight() {
		return canvas.getSize().getHeight();
	}

	/**
	 * Get the value that must be added to the X coordinate of in world object
	 * 
	 * @return the X offset
	 */
	public double getWorldOffsetX() {
		return worldOffsetX;
	}

	/**
	 * Set the value that must be added to the X coordinate of in world object
	 * 
	 * @param offsetX
	 *            the X offset
	 */
	public void setWorldOffsetX(double offsetX) {
		this.worldOffsetX = offsetX;
	}

	/**
	 * Get the value that must be added to the Y coordinate of in world object
	 * 
	 * @return the Y offset
	 */
	public double getWorldOffsetY() {
		return worldOffsetY;
	}

	/**
	 * Set the value that must be added to the Y coordinate of in world object
	 * 
	 * @param offsetY
	 *            the Y offset
	 */
	public void setWorldOffsetY(double offsetY) {
		this.worldOffsetY = offsetY;
	}

	/**
	 * Set the default configuration of the view
	 * 
	 * @param zoom
	 *            the initial zoom value
	 * @param worldCenterX
	 *            the initial X center value
	 * @param worldCenterY
	 *            the initial Y center value
	 */
	public void setDefaultView(double zoom, double worldCenterX, double worldCenterY) {
		this.zoom = zoom;
		this.worldCenterX = worldCenterX;
		this.worldCenterY = worldCenterY;
		this.defaultZoom = zoom;
		this.defaultWorldCenterX = worldCenterX;
		this.defaultWorldCenterY = worldCenterY;
	}
}
