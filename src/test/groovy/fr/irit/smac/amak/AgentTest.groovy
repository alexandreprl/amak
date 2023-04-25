package fr.irit.smac.amak

import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.BlockingVariable

class AgentTest extends Specification {

	def "Agent constructor"() {
		given:
		def mockAmas = Mock(Amas)

		when:
		def agent = new Agent(mockAmas) {
		}

		then:
		1 * mockAmas.addAgent(_)
		agent.neighborhood.size() == 1
		agent.neighborhood.get(0) == agent
	}

	def "addNeighbor"() {
		given:
		def mockAmas = Mock(Amas)
		def agent = new Agent(mockAmas) {
		}
		def agent2 = Mock(Agent)
		def agent3 = Mock(Agent)

		when:
		agent.addNeighbor(agent2, agent3)

		then:
		agent.neighborhood.size() == 3
		agent.criticalities.size() == 2
	}
}
