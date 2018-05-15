package fr.irit.smac.amak.ui.drawables;

import java.awt.Graphics2D;

import fr.irit.smac.amak.ui.VUI;

public class DrawableRectangle extends Drawable {
	public DrawableRectangle(VUI vui, double dx, double dy, double width, double height) {
		super(vui, dx, dy, width, height);
	}

	@Override
	public void onDraw(Graphics2D graphics) {
		graphics.setColor(color);
		if (strokeMode)
			graphics.drawRect(vui.worldToScreenX(left()), vui.worldToScreenY(top()), vui.worldToScreenDistance(width), vui.worldToScreenDistance(height));
		else
			graphics.fillRect(vui.worldToScreenX(left()), vui.worldToScreenY(top()), vui.worldToScreenDistance(width), vui.worldToScreenDistance(height));
	}


}
