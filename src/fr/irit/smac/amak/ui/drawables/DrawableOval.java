package fr.irit.smac.amak.ui.drawables;

import java.awt.Graphics2D;

import fr.irit.smac.amak.ui.VUI;

public class DrawableOval extends Drawable {
	public DrawableOval(VUI vui, double dx, double dy, double width, double height) {
		super(vui, dx, dy, width, height);
	}

	@Override
	public void _onDraw(Graphics2D graphics) {
		graphics.setColor(color);
		if (strokeMode)
			graphics.drawOval((int)left(), (int)top(), (int) getRenderedWidth(), (int) getRenderedHeight());
		else
			graphics.fillOval((int)left(), (int)top(), (int) getRenderedWidth(), (int) getRenderedHeight());
	}


}
