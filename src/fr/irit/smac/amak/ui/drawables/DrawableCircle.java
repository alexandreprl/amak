package fr.irit.smac.amak.ui.drawables;

import java.awt.Graphics2D;

import fr.irit.smac.amak.ui.VUI;

public class DrawableCircle extends Drawable {
	public DrawableCircle(VUI vui, double dx, double dy, double size) {
		super(vui, dx, dy, size, size);
	}

	@Override
	public void onDraw(Graphics2D graphics) {
		graphics.setColor(color);
		if (strokeMode)
			graphics.drawOval(vui.worldToScreenX(left()), vui.worldToScreenY(top()), (int) width, (int) height);
		else
			graphics.fillOval(vui.worldToScreenX(left()), vui.worldToScreenY(top()), (int) width, (int) height);
	}


}
