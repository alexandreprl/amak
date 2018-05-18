package fr.irit.smac.amak.ui.drawables;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import fr.irit.smac.amak.tools.Log;
import fr.irit.smac.amak.ui.VUI;

public class DrawableImage extends Drawable {

	private String filename;
	private BufferedImage image;
	private static Map<String, BufferedImage> loadedImages = new HashMap<>();

	public DrawableImage(VUI vui, double dx, double dy, String filename) {
		super(vui, dx, dy, 0, 0);
		this.setFilename(filename);

	}

	private BufferedImage loadByFilename(String filename) throws IOException {
		if (!loadedImages.containsKey(filename)) {
			loadedImages.put(filename, ImageIO.read(new File(filename)));
		}
		return loadedImages.get(filename);
	}

	private void setFilename(String filename) {
		this.filename = filename;
		try {
			this.image = loadByFilename(this.filename);
		} catch (IOException e) {
			Log.error("AMAK", "Can't find/load the file %s", this.filename);
			try {
				this.image = loadByFilename("Resources/unavailable.png");
			} catch (IOException e1) {
				Log.fatal("AMAK", "Can't load resources belonging to AMAK. Bad things may happen.");
			}
		}
		setWidth(this.image.getWidth());
		setHeight(this.image.getHeight());

	}

	@Override
	public void onDraw(Graphics2D graphics) {
		AffineTransform identity = new AffineTransform();
		AffineTransform trans = new AffineTransform();
		trans.setTransform(identity);
		trans.translate(left(), top());
		if (!isFixed())
			trans.scale(vui.getZoomFactor(), vui.getZoomFactor());
		trans.rotate(getAngle(), getWidth() / 2, getHeight() / 2);
		graphics.drawImage(image, trans, null);
	}

}
