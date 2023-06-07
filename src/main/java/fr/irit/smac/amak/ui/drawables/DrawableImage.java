package fr.irit.smac.amak.ui.drawables;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import fr.irit.smac.amak.ui.VectorialGraphicsPanel;

public class DrawableImage extends Drawable {

	private BufferedImage image;
	private static final Map<String, BufferedImage> loadedImages = new HashMap<>();
	private String lastFilename;

	public DrawableImage(VectorialGraphicsPanel vectorialGraphicsPanel, double dx, double dy, String filename) {
		super(vectorialGraphicsPanel, dx, dy, 0, 0);
		this.setFilename(filename);

	}

	private BufferedImage loadByFilename(String filename) throws IOException {
		if (!loadedImages.containsKey(filename)) {
			loadedImages.put(filename, ImageIO.read(new File(filename)));
		}
		return loadedImages.get(filename);
	}

	public void setFilename(String filename) {
		if (filename.equals(lastFilename))
			return;
		lastFilename = filename;
		try {
			this.image = loadByFilename(filename);
		} catch (IOException e) {
			try {
				this.image = loadByFilename("Resources/unavailable.png");
			} catch (IOException e1) {
			}
		}
		setWidth(this.image.getWidth());
		setHeight(this.image.getHeight());

	}

	@Override
	public void _onDraw(Graphics2D graphics) {
		if (this.image == null)
			return;
		AffineTransform identity = new AffineTransform();
		AffineTransform trans = new AffineTransform();
		trans.setTransform(identity);
		trans.translate(left(), top());
		trans.rotate(getAngle(), getRenderedWidth() / 2, getRenderedHeight() / 2);
		if (!isFixed())
			trans.scale(vectorialGraphicsPanel.getZoomFactor()*getWidth()/this.image.getWidth(), vectorialGraphicsPanel.getZoomFactor()*getHeight()/this.image.getHeight());
		graphics.drawImage(image, trans, null);
		
	}


}
