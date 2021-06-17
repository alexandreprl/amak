package fr.irit.smac.amak.ui.drawables;

import java.awt.Graphics2D;

import fr.irit.smac.amak.ui.VUI;

public class DrawableLine extends Drawable {

	private double x2, x1;
	private double y2, y1;

	public DrawableLine(VUI vui, double dx, double dy, double tx, double ty) {
		super(vui, 0, 0, 0, 0);

		x1 = dx;
		y1 = dy;
		x2 = tx;
		y2 = ty;
	}

	@Override
	public void _onDraw(Graphics2D graphics) {
		graphics.setColor(color);
		if (isFixed())
			graphics.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		else
			graphics.drawLine(vui.worldToScreenX(x1), vui.worldToScreenY(y1), vui.worldToScreenX(x2),
					vui.worldToScreenY(y2));
	}

	public void move(double dx, double dy, double tx, double ty) {
		x1 = dx;
		y1 = dy;
		x2 = tx;
		y2 = ty;
		update();
	}
}
