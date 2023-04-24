package fr.irit.smac.amak.ui.drawables;

import java.awt.*;

import fr.irit.smac.amak.ui.VectorialGraphicsPanel;

public class DrawableLine extends Drawable {

	private double x2, x1;
	private double y2, y1;

	public DrawableLine(VectorialGraphicsPanel vectorialGraphicsPanel, double dx, double dy, double tx, double ty) {
		super(vectorialGraphicsPanel, 0, 0, 0, 0);

		x1 = dx;
		y1 = dy;
		x2 = tx;
		y2 = ty;
	}

	@Override
	public void _onDraw(Graphics2D graphics) {
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(1f));
		if (isFixed())
			graphics.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		else
			graphics.drawLine(vectorialGraphicsPanel.worldToScreenX(x1), vectorialGraphicsPanel.worldToScreenY(y1), vectorialGraphicsPanel.worldToScreenX(x2),
			                  vectorialGraphicsPanel.worldToScreenY(y2));
	}

	public void move(double dx, double dy, double tx, double ty) {
		x1 = dx;
		y1 = dy;
		x2 = tx;
		y2 = ty;
		update();
	}
}
