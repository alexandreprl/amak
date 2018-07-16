package fr.irit.smac.amak.aid;

import java.util.UUID;

public abstract class SimpleAID implements AID {
	private final String uniqueAgentID;

	protected SimpleAID(String uniqueAgentID) {
		this.uniqueAgentID = uniqueAgentID;
	}

	/**
	 * A unique agent ID will be automatically created.
	 */
	protected SimpleAID() {
		this.uniqueAgentID = UUID.randomUUID().toString();
	}

	@Override
	public String getID() {
		return uniqueAgentID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uniqueAgentID == null) ? 0 : uniqueAgentID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleAID other = (SimpleAID) obj;
		if (uniqueAgentID == null) {
			if (other.uniqueAgentID != null)
				return false;
		} else if (!uniqueAgentID.equals(other.uniqueAgentID))
			return false;
		return true;
	}

}
