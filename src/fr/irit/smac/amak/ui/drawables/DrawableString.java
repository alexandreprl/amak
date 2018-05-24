package fr.irit.smac.amak.ui.drawables;

import java.awt.Graphics2D;

import fr.irit.smac.amak.ui.VUI;

public class DrawableString extends Drawable {
	private String text;

	public DrawableString(VUI vui, double dx, double dy, String text) {
		super(vui, dx, dy, 1, 1);
		this.text = text;
	}

	@Override
	public void _onDraw(Graphics2D graphics) {
		graphics.setColor(color);
		graphics.drawString(text, (int)left(), (int)top());
	}

	public void setText(String text) {
		this.text = text;
	}


}
