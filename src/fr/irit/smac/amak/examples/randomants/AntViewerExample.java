package fr.irit.smac.amak.examples.randomants;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.ui.DrawableUI;

public class AntViewerExample extends DrawableUI<AntHillExample> {

	public AntViewerExample(AntHillExample _amas) {
		super(Scheduling.SYNC_WITH_AMAS, _amas);
	}
	@Override
	protected void onInitialConfiguration() {
		setSize(getAmas().getEnvironment().getWidth(), getAmas().getEnvironment().getHeight());
	}
	@Override
	protected void onDraw(Graphics2D graphics2d) {
		graphics2d.setColor(Color.WHITE);
		ArrayList<Agent<?, WorldExample>> renderingAgents = new ArrayList<>(getAmas().getAgents());
		for (Agent<?, WorldExample> agent : renderingAgents) {
			AntExample ant = (AntExample) agent;
			graphics2d.fillRect((int) (ant.dx + getWidth()/2), (int) (ant.dy + getHeight()/2), 3, 3);
		}
	}

}
