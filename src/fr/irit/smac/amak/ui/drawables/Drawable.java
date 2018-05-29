package fr.irit.smac.amak.ui.drawables;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.irit.smac.amak.ui.VUI;

public abstract class Drawable {
	protected VUI panel;

	private double x, y;

	private double width;

	public double getRenderedWidth() {
		if (isFixed())
			return width;
		else
			return vui.worldToScreenDistance(width);
	}

	public void setWidth(double width) {
		this.width = width;
		update();
	}

	public double getRenderedHeight() {
		if (isFixed())
			return height;
		else
			return vui.worldToScreenDistance(height);
	}

	public void setHeight(double height) {
		this.height = height;
		update();
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	protected double height;

	protected boolean strokeMode = false;

	protected Color color = Color.black;

	protected VUI vui;
	protected int layer = 0;

	private double angle;

	private boolean fixed = false;

	private boolean visible = true;

	public boolean isFixed() {
		return fixed;
	}

	public double getAngle() {
		return angle;
	}

	public int getLayer() {
		return layer;
	}

	public Drawable setLayer(int layer) {
		this.layer = layer;
		update();
		return this;
	}

	public Drawable setAngle(double angle2) {
		this.angle = angle2;
		update();
		return this;
	}

	protected Drawable(VUI vui, double dx, double dy, double width, double height) {
		this.vui = vui;
		x = dx;
		y = dy;
		this.width = width;
		this.height = height;
	}
	public void onDraw(Graphics2D graphics) {
		if (isVisible() && isOnScreen()) {
			_onDraw(graphics);	
		}
	}

	private boolean isOnScreen() {
		return (isPointOnScreen(left(), top()) ||
				isPointOnScreen(left(), bottom()) ||
				isPointOnScreen(right(), bottom()) ||
				isPointOnScreen(right(), top()));
	}
	private boolean isPointOnScreen(double x, double y) {
		return x>=0 && x<=panel.getCanvasWidth() && y>=0 && y<=panel.getCanvasHeight();
	}

	public abstract void _onDraw(Graphics2D graphics);

	public void update() {
		if (panel != null)
			panel.updateCanvas();
	}

	public void setPanel(VUI vectorialUI) {
		panel = vectorialUI;
	}

	public double top() {
		if (isFixed())
			return y - height / 2;
		else
			return vui.worldToScreenY(y - height / 2);
	}

	public double left() {
		if (isFixed())
			return x - width / 2;
		else
			return vui.worldToScreenX(x - width / 2);
	}

	public double bottom() {
		if (isFixed())
			return y + height / 2;
		else
			return vui.worldToScreenY(y + height / 2);
	}

	public double right() {
		if (isFixed())
			return x + width / 2;
		else
			return vui.worldToScreenX(x + width / 2);
	}

	public Drawable setStrokeOnly() {
		strokeMode = true;
		update();
		return this;
	}

	public Drawable setColor(Color color) {
		if (color == this.color)
			return this;
		this.color = color;
		update();
		return this;
	}

	public Drawable move(double dx, double dy) {
		if (x == dx && y == dy)
			return this;
		this.x = dx;
		this.y = dy;
		update();
		return this;
	}

	public Drawable setFixed() {
		this.fixed = true;
		update();
		return this;
	}

	public Drawable show() {
		return this.setVisible(true);
	}

	public Drawable hide() {
		return this.setVisible(false);
	}

	public boolean isVisible() {
		return visible;
	}

	public Drawable setVisible(boolean visible) {
		this.visible = visible;
		update();
		return this;
	}
}
