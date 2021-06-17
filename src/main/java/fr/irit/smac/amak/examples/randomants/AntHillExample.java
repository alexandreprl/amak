package fr.irit.smac.amak.examples.randomants;

import java.awt.Color;

import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.DrawableRectangle;
import fr.irit.smac.amak.ui.drawables.DrawableString;

public class AntHillExample extends Amas<WorldExample> {

	private DrawableString antsCountLabel;

	public AntHillExample(WorldExample env) {
		super(env, Scheduling.DEFAULT);
	}

	@Override
	protected void onInitialConfiguration() {
		DrawableRectangle d = VUI.get().createRectangle(0, 0, 800, 600);
		d.setStrokeOnly();
	}

	@Override
	protected void onRenderingInitialization() {

		VUI.get().createRectangle(90, 20, 180, 40).setColor(new Color(0.9f, 0.9f, 0.9f, 0.8f)).setFixed().setLayer(5);

		VUI.get().createImage(20, 20, "Resources/ant.png").setFixed().setLayer(10);
		antsCountLabel = (DrawableString) VUI.get().createString(45, 25, "Ants count").setFixed().setLayer(10);
	}

	@Override
	protected void onInitialAgentsCreation() {
		for (int i = 0; i < 50; i++)
			new AntExample(this, 0, 0);
	}

	@Override
	protected void onSystemCycleEnd() {
		antsCountLabel.setText("Ants count: " + getAgents().size());
	}
}
