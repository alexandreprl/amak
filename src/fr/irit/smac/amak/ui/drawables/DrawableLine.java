package fr.irit.smac.amak.ui.drawables;

import java.awt.Graphics2D;

import fr.irit.smac.amak.ui.VUI;

public class DrawableLine extends Drawable {

	private double x2;
	private double y2;

	public DrawableLine(VUI vui, double dx, double dy, double tx, double ty) {
		super(vui, dx, dy, 0, 0);
		x2 = tx;
		y2 = ty;
	}

	@Override
	public void onDraw(Graphics2D graphics) {
		graphics.setColor(color);
		graphics.drawLine(vui.worldToScreenX(x), vui.worldToScreenY(y), vui.worldToScreenX(x2), vui.worldToScreenY(y2));
	}

	public void move(double dx, double dy, double tx, double ty) {
		x2 = tx;
		y2 = ty;
		super.move(dx, dy);
	}
}
