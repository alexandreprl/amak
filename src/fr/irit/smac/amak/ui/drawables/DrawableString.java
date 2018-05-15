package fr.irit.smac.amak.ui.drawables;

import java.awt.Graphics2D;

import fr.irit.smac.amak.ui.VUI;

public class DrawableString extends Drawable {
	private String text;

	public DrawableString(VUI vui, double dx, double dy, String text) {
		super(vui, dx, dy, 0, 0);
		this.text = text;
	}

	@Override
	public void onDraw(Graphics2D graphics) {
		graphics.setColor(color);
		graphics.drawString(text, vui.worldToScreenX(x), vui.worldToScreenY(y));
	}

	public void setText(String text) {
		this.text = text;
	}

}
