package fr.irit.smac.amak.ui.drawables;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.irit.smac.amak.ui.VUI;

public abstract class Drawable {
	protected VUI panel;

	protected double x, y;

	protected double width;

	protected double height;

	protected boolean strokeMode = false;

	protected Color color = Color.black;

	protected VUI vui;
	protected int layer = 0;

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	protected Drawable(VUI vui, double dx, double dy, double width, double height) {
		this.vui = vui;
		x = dx;
		y = dy;
		this.width = width;
		this.height = height;
	}

	public abstract void onDraw(Graphics2D graphics);

	public void update() {
		panel.updateCanvas();
	}

	public void setPanel(VUI vectorialUI) {
		panel = vectorialUI;
	}
	public double top() {
		return y-height/2;
	}
	public double left() {
		return x-width/2;
	}

	public void setStrokeOnly() {
		strokeMode = true;
	}

	public void setColor(Color color) {
		if (color == this.color)
			return;
		this.color = color;
		update();
	}

	public void move(double dx, double dy) {
		if (x == dx && y == dy)
			return;
		this.x = dx;
		this.y = dy;
		update();
	}
}
