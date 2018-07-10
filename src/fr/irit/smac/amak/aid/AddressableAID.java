package fr.irit.smac.amak.aid;

import fr.irit.smac.amak.messaging.IAmakAddress;

public class AddressableAID extends SimpleAID {

	private final IAmakAddress agentAdress;

	public AddressableAID(IAmakAddress agentAdress, String uniqueAgentID) {
		super(uniqueAgentID);
		this.agentAdress = agentAdress;
	}

	public IAmakAddress getAgentAdress() {
		return agentAdress;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((agentAdress == null) ? 0 : agentAdress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddressableAID other = (AddressableAID) obj;
		if (agentAdress == null) {
			if (other.agentAdress != null)
				return false;
		} else if (!agentAdress.equals(other.agentAdress))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return agentAdress.toString();
	}

}
