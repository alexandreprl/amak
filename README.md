This repository contains a framework made to facilitate the development of multi-agent system. 

Examples are available in the package fr.irit.smac.amak.examples.

# [Click here to download the latest standalone version](https://bitbucket.org/perlesa/amak/raw/master/Release/AmakFramework-standalone.jar?at=master) #

# General #

Amak is a framework aiming at facilitating the development of multi-agent systems.
For the framework to work properly, you need to create a type of environment, a type of agent and a type of multi-agent system.

Once you've created your three classes, you have to complete them by overriding methods present in higher level classes. Go into your classes, start typing "on" and press ctrl+space, a list of the most useful methods to override will appear.
At first, you will probably need to override onPerceive, onDecide and onAct in your agents, and onInitialAgentsCreation in your multi-agent system.
All of these methods are called at the right time by the scheduler, you only have to implement them.

The scheduling of agents is loaded automatically when you create an instance of your multi-agent system.

Amak is packaged with other useful libraries (lx-plot, avt, ...). Also, if you need to draw something easily, have a look at the abstract class DrawableUI.

# Quick start (with eclipse)

In this quick start example, we will do the first steps aiming at creating an adaptive multi-agent system to solve the [philosopher's dinner problem](https://en.wikipedia.org/wiki/Dining_philosophers_problem).

## Create the eclipse project ##

Click on File -> New -> Java Project
Enter a name : "Philosopher's dinner"
Click on Finish

## Download the Amak framework ##

Download the [latest jar file](https://bitbucket.org/perlesa/amak/raw/master/Release/AmakFramework-standalone.jar?at=master) and add it to your project folder (drag and drop).

Right click on it, Build path ... -> Add to build path

## Create your first multi-agent system ##

In order to work, the Amak framework needs a type of environment, a type of agent and a type of multi-agent system.

### Create resources ###

Create the resource Fork. A fork can be taken or released by a philosopher.


```
#!Java

public class Fork {
	private Philosopher takenBy;

	public synchronized boolean tryTake(Philosopher asker) {
		if (takenBy != null)
			return false;
		takenBy = asker;
		return true;
	}

	public synchronized void release(Philosopher asker) {
		if (takenBy == asker) {
			takenBy = null;
		}
	}

	public synchronized boolean owned(Philosopher asker) {
		return takenBy == asker;
	}
}
```


### Create your first environment ###

Create a new class named "Table" which extends the abstract class "Environment".
A table is initialized with 10 forks.

```
#!Java

	private Fork[] forks;
	public Table() {
		super(Scheduling.DEFAULT);
	}
	@Override
	public void onInitialization() {
		// Set 10 forks on the table
		forks = new Fork[10];
		for (int i = 0; i < forks.length; i++)
			forks[i] = new Fork();
	}

	public Fork[] getForks() {
		return forks;
	}
```

### Create your multi-agent system ###

Let's call the multi-agent system "MyAMAS".
Create a class "MyAMAS" which extends the abstract class Amas<Table>.

During the initialization, we want to create a philosopher per fork and let them know their neighbors.


```
#!Java

	public MyAMAS(Table env) {
		super(env, Scheduling.DEFAULT);
	}
	@Override
	protected void onInitialAgentsCreation() {
		Philosopher[] p = new Philosopher[getEnvironment().getForks().length];
		//Create one agent per fork
		for (int i=0;i<getEnvironment().getForks().length-1;i++) {
			p[i] =new Philosopher(i, this, getEnvironment().getForks()[i], getEnvironment().getForks()[i+1]);
		}

		//Let the last philosopher takes the first fork (round table) 
		p[getEnvironment().getForks().length-1]=new Philosopher(getEnvironment().getForks().length-1, this, getEnvironment().getForks()[getEnvironment().getForks().length-1], getEnvironment().getForks()[0]);
		
		
		//Add neighborhood
		for (int i=1;i<p.length;i++) {
			p[i].addNeighbor(p[i-1]);
			p[i-1].addNeighbor(p[i]);
		}
		p[0].addNeighbor(p[p.length-1]);
		p[p.length-1].addNeighbor(p[0]);
	}
```

### Create your agent ###

Create a class "Philosopher" which extends the class Agent<MyAMAS, Table>.


```
#!Java

	private Fork left;
	private Fork right;
	private double hungerDuration;
	private double eatenPastas;
	private int id;

	public enum State {
		THINK, HUNGRY, EATING
	}

	private State state = State.THINK;

	public Philosopher(int id, MyAMAS amas, Fork left, Fork right) {
		super(amas);
		this.id = id;
		this.left = left;
		this.right = right;
	}
	
	@Override
	protected void onPerceive() {
		// Nothing goes here as the perception of neighbors criticality is already made
		// by the framework
	}
	
	@Override
	protected void onDecideAndAct() {
		State nextState = state;
		switch (state) {
		case EATING:
			eatenPastas++;
			if (new Random().nextInt(101) > 50) {
				left.release(this);
				right.release(this);
				nextState = State.THINK;
			}
			break;
		case HUNGRY:
			hungerDuration++;
			if (getMostCriticalNeighbor(true) == this) {
				if (left.tryTake(this) && right.tryTake(this))
					nextState = State.EATING;
				else{
					left.release(this);
					right.release(this);
				}
			} else {
				left.release(this);
				right.release(this);
			}
			break;
		case THINK:
			if (new Random().nextInt(101) >50) {
				hungerDuration = 0;
				nextState = State.HUNGRY;
			}
			break;
		default:
			break;

		}

		state = nextState;
	}

	@Override
	protected double computeCriticality() {
		return hungerDuration;
	}

	@Override
	protected void onUpdateRender() {
		LxPlot.getChart("Eaten Pastas", ChartType.BAR).add(id, eatenPastas);
	}
```


### Launch your system ###

In any class, create the environment and then the multi-agent system and launch it.


```
#!Java

public static void main(String[] args) {
		Table env = new Table();
		new MyAMAS(env);
	}
```


# Agent messaging #
If your agents need to exchange information by message, we recommend to use the `CommunicatingAgent` class. The implementation of your agents must inherit this class. Otherwise you must manage the mailbox yourself (see `CommunicatingAgent` implementation for an example).

## Communication agent through `CommunicatingAgent` class ##
When your agent inherits the `CommunicatingAgent` class, an address is attached to your agent, it is called `AID` (Agent ID).  You should use the `AID` to deliver messages.
To get the `AID`, simply call `getAID()` on the agent.

### Main methods
Various messaging methods are available, divided into two main concerns:

1. Received messages :

* 	Get all received messages : 

```
#!Java

Collection<IAmakEnvelope> getAllMessages()
```

an AmakEnvelope contain :
 	the sent message `IAmakMessage`,
    the sender `AddressableAID` (it is an `AID`),
    the metadata related to the sending.
  
  
* 	Alternatively, you can retrieve messages given a specific type:

```
#!Java

	<M extends IAmakMessage> Collection<M> getReceivedMessagesGivenType(Class<M>)
	<M extends IAmakMessage> Collection<IAmakReceivedEnvelope<M, IAmakMessageMetaData, AddressableAID>> getReceivedEnvelopesGivenMessageType(Class<M>)
```

2.Sending messages:

* Sending with the `AID` of receiver
```
#!Java
sendMessage(IAmakMessage messageToSend, AID receiver)
sendMessage(IAmakMessage messageToSend, AID receiver, IAmakMessageMetaData metadata)
```

*   Sending with the "raw id" of receiver
```
#!Java

sendMessage(IAmakMessage messageToSend, String receiverRawID)
```	


### Make messages
An Amak message should implement the `IAmakMessage` interface, like this :

```
#!Java

	public static class MyMsg implements IAmakMessage {
		private final String content;

		public MyMsg(String content) {
			this.content = content;
		}

		public String getContent() {
			return content;
		}
	}
```


### Example
The agent 1 create a new message and send it to agent 2. Agent 2 receive and display the content of this message.

```
#!Java
CommunicatingAgent<TestAMAS, TestEnv> communicantAgent1 = new CommunicatingAgent<TestAMAS, TestEnv>(amas, params) { ...
	};
	
CommunicatingAgent<TestAMAS, TestEnv> communicantAgent2 = new CommunicatingAgent<TestAMAS, TestEnv>(amas, params) { ...
	};
		
communicantAgent1.run();
	
MyMsg msg = new MyMsg("Hello Agent 2!");	
boolean sendingSuccessful = communicantAgent1.sendMessage(msg, communicantAgent2.getAID());
//can make something if sendingSuccessful == false

//the agent 2 must make a execution cycle to perceive the msg
communicantAgent2.run();
Collection<IAmakReceivedEnvelope<MyMsg, IAmakMessageMetaData, AddressableAID>> allMyMgsReceived = communicantAgent2
				.getReceivedEnvelopesGivenMessageType(MyMsg.class);

allMyMgsReceived.forEach(msg -> System.out.println("Agent 2 : I received a MyMsg from : "
				+ msg.getMessageSenderAID() + ", he tells me : \"" + msg.getMessage().getContent() + "\""));
//Agent 2 : I receive a MyMsg from : 06fcd4fc-1674-4bc0-bffc-c6c33cbbaa53, he tells me : "Hello Agent 2!"
```	

To ensure the uniqueness of agent identifiers, by default, the UUID class is used. Therefore, in the example, the identifier is "06fcd4fc-1674-4bc0-bffc-c6c33cbbaa53".

You can define your own agent name by specifying it in at creation with parameters:

```
#!Java

Object params[] = { CommunicatingAgent.RAW_ID_PARAM_NAME_PREFIX + "Agent 1" };
communicantAgent1 = new CommunicatingAgent<TestAMAS, TestEnv>(amas, params) {...
};

//same code...

//Agent 2 : I received a message from : Agent 1, he tells me : "Hello Agent 2!"
```

The uniqueness of the "raw id" is the responsibility of the developer.

When you use your own agent ID, you can use this "raw id" to send messages:

```
#!Java


communicantAgent2.sendMessage(messageToSend, "Agent 1");
```
    
However, the performance of this method can be degraded when the system has a lot of agents. You should prefer the `AID` usage.

### Define custom metadata for Amak message
Metadata is attached to a sent message but is not contained in the message. Metadata is of particular interest when certain technical considerations must be taken into account but are not relevant to the agent. Metadata processing must be delegated to the technical layer of an agent or to the lower layer of an infrastructure.
To customize metadata, simply inherit the `SimpleAmakMessageMetaData` interface or implement `IAmakMessageMetaData`, like this :

```
#!Java


public static class MyCustomMetadata extends SimpleAmakMessageMetaData {
	// SimpleAmakMessageMetaData hasn't a sendingCost attribute
	private final float sendingCost;

	public MyCustomMetadata(float sendingCost) {
		this.sendingCost = sendingCost;
	}

	public float getSendingCost() {
		return sendingCost;
	}
}
```

## Implementation consideration
### Perception policy and message retention duration [**IMPORTANT**]
Messages are perceived during the perception phase. Thus, when an agent finishes his perception phase, any new incoming messages are not perceptible during the rest of the cycle (i.e. decision and action). They will be perceived for the next cycle.

By default, all received messages can only be processed during the current cycle. At the beginning of the next cycle, all messages perceived in the previous cycle are deleted. Message retention duration is only one cycle. If you want to change this policy, please refer to the following paragraph.


### Modification of the message retention policy
At the communicating agent creation, it is possible to indicate a mailbox reading strategy (use the constructor `public CommunicatingAgent(A amas, IMessagingReader msgReader, Object... params)`). By default, this strategy is: `MessagingReaderAllMsgsOfCycle`.
To redefine this strategy, you simply need to implement `IMessagingReader` and give it at the agent creation.

### Other consideration
All agents share the same messaging infrastructure.
The infrastructures are completely thread-safe and optimized for this purpose.
Amak uses the agent-messaging library : https://github.com/IRIT-SMAC/agent-tooling/tree/master/agent-messaging


