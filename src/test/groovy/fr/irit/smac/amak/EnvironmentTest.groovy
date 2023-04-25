package fr.irit.smac.amak

import spock.lang.Specification
import spock.util.concurrent.BlockingVariable

class EnvironmentTest extends Specification {
	def "When an environment is created then the params should be accessible from the onInitialization method"()
	{
		given:
		def blockingVariable = new BlockingVariable<Object[]>(0.2)
		def param1 = "param1"
		def param2 = "param2"
		def environment = new Environment(param1, param2) {
			@Override
			void onInitialization() {
				blockingVariable.set(params)
			}
		}

		expect:
		blockingVariable.get().toString() == "[param1, param2]"
	}
}
