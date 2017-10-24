package fr.irit.smac.amak.examples.randomants;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.ui.DrawableUI;

public class AntViewerExample extends DrawableUI {

	protected AntHillExample antHill;

	public AntViewerExample() {
		super(Scheduling.UI);
	}

	@Override
	protected void onDraw(Graphics2D graphics2d) {
		graphics2d.setColor(Color.WHITE);
		ArrayList<Agent<?, WorldExample>> renderingAgents = new ArrayList<>(antHill.getAgents());
		for (Agent<?, WorldExample> agent : renderingAgents) {
			AntExample ant = (AntExample) agent;
			graphics2d.fillRect((int) (ant.dx + 400), (int) (ant.dy + 300), 3, 3);
		}
	}

}
