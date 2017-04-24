package fr.irit.smac.amak.examples.randomants;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.ui.DrawableUI;

public class AntViewer extends DrawableUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3703452662387302999L;
	private AntHill antHill;

	public AntViewer(AntHill antHill) {
		super(Scheduling.MANUAL);
		this.antHill = antHill;
	}

	@Override
	protected void onDraw(Graphics2D graphics2d) {
		graphics2d.setColor(Color.WHITE);

		for (Agent<?, World> agent : antHill.getAgents()) {
			Ant ant = (Ant) agent;
			graphics2d.fillRect((int) (ant.dx + 400), (int) (ant.dy + 300), 3, 3);
		}
	}

}
