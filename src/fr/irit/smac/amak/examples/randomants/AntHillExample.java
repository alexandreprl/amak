package fr.irit.smac.amak.examples.randomants;


import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.DrawableRectangle;

public class AntHillExample extends Amas<WorldExample> {

	public AntHillExample(WorldExample env) {
		super(env, Scheduling.DEFAULT);
	}
	@Override
	protected void onInitialConfiguration() {
		DrawableRectangle d = VUI.get().createRectangle(0, 0, 800, 600);
		d.setStrokeOnly();
	}
	@Override
	protected void onInitialAgentsCreation() {
		for (int i=0;i<1000;i++)
			new AntExample(this);
	}
	@Override
	protected void onSystemCycleEnd() {

	}
}
