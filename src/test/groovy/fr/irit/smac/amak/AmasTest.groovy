package fr.irit.smac.amak

import fr.irit.smac.amak.scheduling.SchedulableExecutionException
import spock.lang.Specification
import spock.lang.Timeout

class AmasTest extends Specification {
	@Timeout(10)
	def "When a cycle is executed and an agent throws a runtime exception then the cycle method should end and an exception should be thrown"() {
		given:
		def amas = new Amas(Mock(Environment), 1, executionPolicy)

		def agent = Mock(Agent)
		amas.addAgent(agent)
		agent.cycle() >> {throw new RuntimeException("Exception during cycle")}
		agent.phase1() >> {throw new RuntimeException("Exception during cycle")}
		agent.phase2() >> {throw new RuntimeException("Exception during cycle")}

		when:
		amas.cycle()

		then:
		def e = thrown(SchedulableExecutionException)
		e.causes.size() == 1
		e.causes[0] instanceof RuntimeException
		e.causes[0].message == "Exception during cycle"

		where:
		executionPolicy << [Amas.ExecutionPolicy.ONE_PHASE, Amas.ExecutionPolicy.TWO_PHASES]
	}
	def "getAgents"() {
		given:
		def amas = new Amas(Mock(Environment), 1, Amas.ExecutionPolicy.ONE_PHASE)
		new Agent1(amas)
		new Agent1(amas)
		new Agent2(amas)

		amas.cycle()

		expect:
		amas.getAgents(Agent1).size() == 2
		amas.getAgents(Agent2).size() == 1
	}

	class Agent1 extends Agent {
		protected Agent1(Amas amas) {
			super(amas)
		}
	}
	class Agent2 extends Agent {
		protected Agent2(Amas amas) {
			super(amas)
		}
	}
}
