package fr.irit.smac.amak

import spock.lang.Specification

class EnvironmentTest extends Specification {
	def "The initialization code should be run only once"() {
		given:
		def numberOfTimesOnReadyWasCalled = 0
		def numberOfTimesOnCycleWasCalled = 0
		def environment = new Environment() {
			@Override
			void onReady() {
				numberOfTimesOnReadyWasCalled++
			}

			@Override
			void onCycle() {
				numberOfTimesOnCycleWasCalled++
			}
		}

		when:
		environment.cycle()
		environment.cycle()

		then:
		numberOfTimesOnReadyWasCalled == 1
		numberOfTimesOnCycleWasCalled == 2
	}
	def "setRandom"() {
		given:
		def environment = new Environment() {
			@Override
			void onCycle() {
				super.onCycle()
			}
		}
		environment.setSeed(seed)

		expect:
		environment.getRandom().nextInt() == firstExpectedInt

		where:
		seed       | firstExpectedInt
		1L         | -1155869325
		123456789L | -1442945365
	}
}
