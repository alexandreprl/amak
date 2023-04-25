package fr.irit.smac.amak.messaging

import fr.irit.smac.amak.Agent
import spock.lang.Specification

class MailboxTest extends Specification {
	def "When a mailbox is created, then it should contain no message"() {
		given:
		def mailbox = new Mailbox<TestMessage>()

		expect:
		mailbox.isEmpty()
		mailbox.size() == 0
	}
	def "When trying to retrieve a message on an empty mailbox, then an empty response should be returned"() {
		given:
		def mailbox = new Mailbox<TestMessage>()

		when:
		def receivedMessage = mailbox.retrieve()

		then:
		receivedMessage.isEmpty()
	}

	def "When a message is sent, then the mailbox should contain one message"() {
		given:
		def mailbox = new Mailbox<TestMessage>()
		def testMessage = new TestMessage(null, 123)

		when:
		mailbox.send(testMessage)

		then:
		!mailbox.isEmpty()
		mailbox.size() == 1
	}

	def "When a message is sent, then the message should be returned"() {
		given:
		def mailbox = new Mailbox<TestMessage>()
		def sender = Mock(Agent)
		def testMessage = new TestMessage(sender, 123)
		mailbox.send(testMessage)

		when:
		def receivedMessage = mailbox.retrieve()

		then:
		receivedMessage.isPresent()
		receivedMessage.get() == testMessage
		receivedMessage.get().getSender() == sender
	}

	class TestMessage extends Message {
		private final int content

		TestMessage(Agent sender, int content) {
			super(sender)
			this.content = content
		}
	}
}
