package fr.irit.smac.amak.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import fr.irit.smac.amak.tools.Log;
import fr.irit.smac.amak.ui.drawables.Drawable;
import fr.irit.smac.amak.ui.drawables.DrawablePoint;
import fr.irit.smac.amak.ui.drawables.DrawableRectangle;

public class VUI {



	private List<Drawable> drawables = new ArrayList<>();
	private ReentrantLock drawablesLock = new ReentrantLock();

	private String title;
	private static Map<String, VUI> instances = new HashMap<>();

	private double worldOffsetX;

	private double worldOffsetY;

	protected Integer lastDragX;

	protected Integer lastDragY;


	private JPanel panel, canvas;

	private JLabel statusLabel;
	private double defaultZoom = 100;
	private double defaultWorldCenterX = 0;
	private double defaultWorldCenterY = 0;
	protected double zoom = defaultZoom;
	private double worldCenterX = defaultWorldCenterX, worldCenterY = defaultWorldCenterY;

	public static VUI get() {
		return get("Default");
	}

	public static VUI get(String id) {
		if (!instances.containsKey(id)) {
			VUI value = new VUI(id);
			instances.put(id, value);
			return value;
		}
		return instances.get(id);
	}

	private VUI(String _title) {
		this.title = _title;
		onInitialConfiguration();
		panel = new JPanel(new BorderLayout());
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(l->{
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
				setWorldOffsetY(worldCenterY  + screenToWorldDistance(h / 2));

				g.setColor(new Color(0.96f, 0.96f, 0.96f));
				g.fillRect(0, 0, w, h);
				drawablesLock.lock();
				Collections.sort(drawables, new Comparator<Drawable>() {

					@Override
					public int compare(Drawable o1, Drawable o2) {
						return o1.getLayer()-o2.getLayer();
					}
				});
				for (Drawable d : drawables) {
					d.onDraw(g2);
				}
				drawablesLock.unlock();
			}
		};
		canvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
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
				
				worldCenterX += screenToWorldDistance(e.getX() - lastDragX);
				worldCenterY += screenToWorldDistance(e.getY() - lastDragY);
				lastDragX = e.getX();
				lastDragY = e.getY();
				updateCanvas();
				onMouseDragged(e.getX(), e.getY());
			}
		});
		canvas.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				double wdx = screenToWorldDistance((int) (canvas.getSize().getWidth()/2 -e.getX()));
				double wdy = screenToWorldDistance((int) (canvas.getSize().getHeight()/2 -e.getY()));
				
				zoom += e.getWheelRotation()*10;
				if (zoom<10)
					zoom=10;

				double wdx2 = screenToWorldDistance((int) (canvas.getSize().getWidth()/2 -e.getX()));
				double wdy2 = screenToWorldDistance((int) (canvas.getSize().getHeight()/2 -e.getY()));

				worldCenterX -= wdx2-wdx;
				worldCenterY -= wdy2-wdy;
				updateCanvas();
			}
		});
		canvas.setPreferredSize(new Dimension(800, 600));
		panel.add(canvas, BorderLayout.CENTER);
		panel.add(statusPanel, BorderLayout.SOUTH);
		MainWindow.addTabbedPanel("VUI #" + title, panel);

	}

	protected void onMouseDragged(int x, int y) {
	}

	protected void onClick(int x, int y) {
		// TODO Auto-generated method stub

	}

	private void onInitialConfiguration() {
		// TODO Auto-generated method stub

	}


	public int worldToScreenDistance(double d) {
		return (int) (d*getZoomFactor());
	}
	public double screenToWorldDistance(int d) {
		return  ((double)d/getZoomFactor());
	}

	public int worldToScreenX(double x) {
		return (int) ((x+getWorldOffsetX())*getZoomFactor());
	}

	private double getZoomFactor() {
		return zoom/100;
	}

	public int worldToScreenY(double y) {
		return (int) ((y+getWorldOffsetY())*getZoomFactor());
	}
	public double screenToWorldX(double x) {
		return  (x)/getZoomFactor() - getWorldOffsetX();
	}

	public double screenToWorldY(double y) {
		return  (y)/getZoomFactor() - getWorldOffsetY();
	}

	public void add(Drawable d) {
		d.setPanel(this);
		drawablesLock.lock();
		drawables.add(d);
		drawablesLock.unlock();
		updateCanvas();
	}
	public void updateCanvas() {
		canvas.repaint();
		
		statusLabel.setText(String.format("Zoom: %.2f Center: (%.2f,%.2f)", zoom, worldCenterX, worldCenterY));
	}
	public double getWorldOffsetX() {
		return worldOffsetX;
	}

	public void setWorldOffsetX(double offsetX) {
		this.worldOffsetX = offsetX;
	}

	public double getWorldOffsetY() {
		return worldOffsetY;
	}

	public void setWorldOffsetY(double offsetY) {
		this.worldOffsetY = offsetY;
	}

	public DrawablePoint createPoint(double dx, double dy) {
		DrawablePoint drawablePoint = new DrawablePoint(this, dx, dy);
		add(drawablePoint);
		return drawablePoint;
	}

	public DrawableRectangle createRectangle(double x, double y, double w, double h) {
		DrawableRectangle d = new DrawableRectangle(this, x, y, w, h);
		add(d);
		return d;
	}
	public void setDefaultView(double zoom, double worldCenterX, double worldCenterY) {
		this.zoom = zoom;
		this.worldCenterX = worldCenterX;
		this.worldCenterY = worldCenterY;
		this.defaultZoom = zoom;
		this.defaultWorldCenterX = worldCenterX;
		this.defaultWorldCenterY = worldCenterY;
	}
}
