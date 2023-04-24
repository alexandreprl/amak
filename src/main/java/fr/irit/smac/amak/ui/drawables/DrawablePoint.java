package fr.irit.smac.amak.ui.drawables;

import fr.irit.smac.amak.ui.VectorialGraphicsPanel;

import java.awt.*;

public class DrawablePoint extends Drawable {

	public DrawablePoint(VectorialGraphicsPanel vectorialGraphicsPanel, double dx, double dy) {
		super(vectorialGraphicsPanel, dx, dy, 2, 2);
	}
	@Override
	public void _onDraw(Graphics2D graphics) {
		graphics.setColor(color);
		graphics.fillRect((int)left(), (int)top(), 2, 2);
	}
}
