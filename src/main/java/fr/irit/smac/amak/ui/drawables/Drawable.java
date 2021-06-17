package fr.irit.smac.amak.ui.drawables;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.irit.smac.amak.ui.VUI;

/**
 * A drawable is an object that can be drawn by the {@link VUI} system
 * 
 * @author Alexandre Perles
 *
 */
public abstract class Drawable {

	/**
	 * The horizontal position of the object
	 */
	private double x;
	/**
	 * The vertical position of the object
	 */
	private double y;
	/**
	 * The width of the object
	 */
	private double width;

	/**
	 * Compute the width as it must be displayed on screen. Given the zoom factor,
	 * the width displayed can be different than the real width.
	 * 
	 * @return the width
	 */
	public double getRenderedWidth() {
		if (isFixed())
			return width;
		else
			return vui.worldToScreenDistance(width);
	}

	/**
	 * Set the real width of the object
	 * 
	 * @param width
	 *            The new width
	 */
	public void setWidth(double width) {
		this.width = width;
		update();
	}

	/**
	 * Compute the height as it must be displayed on screen. Given the zoom factor,
	 * the height displayed can be different than the real height.
	 * 
	 * @return the width
	 */
	public double getRenderedHeight() {
		if (isFixed())
			return height;
		else
			return vui.worldToScreenDistance(height);
	}

	/**
	 * Set the real height of the object
	 * 
	 * @param height
	 *            The new height
	 */
	public void setHeight(double height) {
		this.height = height;
		update();
	}

	/**
	 * Get the real width
	 * 
	 * @return the real width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Get the real height
	 * 
	 * @return the real height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * The real height
	 */
	protected double height;

	/**
	 * Does only the border must be displayed ?
	 */
	protected boolean strokeMode = false;

	/**
	 * The color of the object
	 */
	protected Color color = Color.black;
	/**
	 * The VUI on which the object is drawn
	 */
	protected VUI vui;
	/**
	 * The order of drawing. An higher layer is drawn on top of the other.
	 */
	protected int layer = 0;
	/**
	 * The angle of rotation of the object
	 */
	private double angle;
	/**
	 * A fixed object doesn't move with the view. It can be used for HUD
	 */
	private boolean fixed = false;
	/**
	 * Must the object be drawn ?
	 */
	private boolean visible = true;

	/**
	 * Getter for the fixed attribute
	 * 
	 * @return if the obejct is fixed
	 */
	public boolean isFixed() {
		return fixed;
	}

	/**
	 * Getter for the angle attribute
	 * 
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Getter for the layer attribute
	 * 
	 * @return the layer
	 */
	public int getLayer() {
		return layer;
	}

	/**
	 * Set the layer and update
	 * 
	 * @param layer
	 *            the new layer
	 * @return the object for chained methods
	 */
	public Drawable setLayer(int layer) {
		this.layer = layer;
		update();
		return this;
	}

	/**
	 * Set the new angle
	 * 
	 * @param angle2
	 *            the new angle
	 * @return the object for chained methods
	 */
	public Drawable setAngle(double angle2) {
		this.angle = angle2;
		update();
		return this;
	}

	/**
	 * Constructor of the obejct
	 * 
	 * @param vui
	 *            the VUI on which the object must be drawn
	 * @param dx
	 *            the x real position
	 * @param dy
	 *            the y real position
	 * @param width
	 *            the real width
	 * @param height
	 *            the real height
	 */
	protected Drawable(VUI vui, double dx, double dy, double width, double height) {
		this.vui = vui;
		x = dx;
		y = dy;
		this.width = width;
		this.height = height;
	}

	/**
	 * Draw the object if visible and if on screen
	 * 
	 * @param graphics
	 *            The graphics object that must be used to draw
	 */
	public void onDraw(Graphics2D graphics) {
		if (isVisible() && isOnScreen()) {
			_onDraw(graphics);
		}
	}

	/**
	 * Is the object on screen ?
	 * 
	 * @return true if object is on the screen
	 */
	private boolean isOnScreen() {
		return (isPointOnScreen(left(), top()) || isPointOnScreen(left(), bottom())
				|| isPointOnScreen(right(), bottom()) || isPointOnScreen(right(), top()));
	}

	/**
	 * Is a given coordinate on screen
	 * 
	 * @param x
	 *            the horitontal position
	 * @param y
	 *            the vertical position
	 * @return true if the point is on the screen
	 */
	private boolean isPointOnScreen(double x, double y) {
		return x >= 0 && x <= vui.getCanvasWidth() && y >= 0 && y <= vui.getCanvasHeight();
	}

	/**
	 * Method that must be overrided to draw
	 * 
	 * @param graphics
	 *            the Graphics2D object
	 */
	public abstract void _onDraw(Graphics2D graphics);

	/**
	 * Method called when the VUI must be refreshed
	 */
	public void update() {
		if (vui != null)
			vui.updateCanvas();
	}

	/**
	 * Set the associated VUI
	 * 
	 * @param vectorialUI
	 */
	public void setPanel(VUI vectorialUI) {
		vui = vectorialUI;
	}

	/**
	 * Get the top y coordinate
	 * 
	 * @return the top y coordinate
	 */
	public double top() {
		if (isFixed())
			return y - height / 2;
		else
			return vui.worldToScreenY(y - height / 2);
	}

	/**
	 * Get the left x coordinate
	 * 
	 * @return the left x coordinate
	 */
	public double left() {
		if (isFixed())
			return x - width / 2;
		else
			return vui.worldToScreenX(x - width / 2);
	}

	/**
	 * Get the bottom y coordinate
	 * 
	 * @return the bottom y coordinate
	 */
	public double bottom() {
		if (isFixed())
			return y + height / 2;
		else
			return vui.worldToScreenY(y + height / 2);
	}

	/**
	 * Get the right x coordinate
	 * 
	 * @return the right x coordinate
	 */
	public double right() {
		if (isFixed())
			return x + width / 2;
		else
			return vui.worldToScreenX(x + width / 2);
	}

	/**
	 * Only draw the border of the object
	 * 
	 * @return the object for chained methods
	 */
	public Drawable setStrokeOnly() {
		strokeMode = true;
		update();
		return this;
	}

	/**
	 * 
	 * @param color
	 * @return the object for chained methods
	 */
	public Drawable setColor(Color color) {
		if (color == this.color)
			return this;
		this.color = color;
		update();
		return this;
	}

	/**
	 * 
	 * @param dx
	 * @param dy
	 * @return the object for chained methods
	 */
	public Drawable move(double dx, double dy) {
		if (x == dx && y == dy)
			return this;
		this.x = dx;
		this.y = dy;
		update();
		return this;
	}

	/**
	 * 
	 * @return the object for chained methods
	 */
	public Drawable setFixed() {
		this.fixed = true;
		update();
		return this;
	}

	/**
	 * 
	 * @return the object for chained methods
	 */
	public Drawable show() {
		return this.setVisible(true);
	}

	/**
	 * 
	 * @return
	 */
	public Drawable hide() {
		return this.setVisible(false);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * 
	 * @param visible
	 * @return the object for chained methods
	 */
	public Drawable setVisible(boolean visible) {
		this.visible = visible;
		update();
		return this;
	}
}
