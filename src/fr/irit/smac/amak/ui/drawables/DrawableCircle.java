package fr.irit.smac.amak.ui.drawables;

import java.awt.Graphics2D;

import fr.irit.smac.amak.ui.VUI;

public class DrawableCircle extends Drawable {
	public DrawableCircle(VUI vui, double dx, double dy, double size) {
		super(vui, dx, dy, size, size);
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
