# AMAK Version 3.0 - Major update 2023 #

This repository contains a framework made to facilitate the development of multi-agent system.

**Note: Version 3.0 introduces a lot of breaking changes if you are having trouble migrating your project or creating a new one, do not hesitate to contact me.**

The previous version of AMAK is still accessible on branch **v2**

# General #

Amak is a framework aiming at facilitating the development of multi-agent systems.
For the framework to work properly, you need to create a type of environment, a type of agent and a type of multi-agent system.

Once you've created your three classes, you have to complete them by overriding methods present in higher level classes. Go into your classes, start typing "on" and press ctrl+space, a list of the most useful methods to override will show.
At first, you will probably need to override onPerceive, onDecide and onAct in your agents, and onInitialAgentsCreation in your multi-agent system.
All of these methods are called at the right time by the scheduler, you only have to implement them.

# Quick start (with IntelliJ IDEA)#

In this quick start example, we will do the first steps aiming at creating an adaptive multi-agent system to solve the [philosopher's dinner problem](https://en.wikipedia.org/wiki/Dining_philosophers_problem).

## Create the gradle project ##

Click on File -> New -> Project
Enter a name : "Philosopher's dinner"
Pick the language : Java
Pick the build system : Gradle
Pick the JDK : 17 (recommended)
Pick the Gradle DSL : Groovy
Click on **Create**

## Set up the Amak framework ##

* Open build.gradle

* Add jitpack to the repositories
```
repositories {
    ...
    // Add the following line
    maven { url "https://jitpack.io" }
}
```

* Add AMAK to the dependencies
```
dependencies {

    // Add the two following lines
    // AMAK
    implementation 'org.bitbucket.perlesa:amak:v3-SNAPSHOT'
    
    ...
}
```

* Click on the Gradle Refresh button

## Create your first multi-agent system ##

In order to work, the Amak framework needs a type of environment, a type of agent and a type of multi-agent system.

### Create the resource Fork ###

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

public class Table extends Environment {
	private Fork[] forks;

	public Table() {
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
}
```

### Create your multi-agent system ###

Let's call the multi-agent system "MyAMAS".
Create a class "MyAMAS" which extends the abstract class `Amas<Table>`.

During the initialization, we want to create a philosopher per fork and let them know their neighbors.


```
#!Java

public class MyAMAS extends Amas<Table> {
	public MyAMAS(Table env) {
		super(env, 1, ExecutionPolicy.ONE_PHASE);
	}

	@Override
	protected void onInitialAgentsCreation() {
		Philosopher[] p = new Philosopher[getEnvironment().getForks().length];
		//Create one agent per fork
		for (int i = 0; i < getEnvironment().getForks().length - 1; i++) {
			p[i] = new Philosopher(i, this, getEnvironment().getForks()[i], getEnvironment().getForks()[i + 1]);
		}

		//Let the last philosopher takes the first fork (round table)
		p[getEnvironment().getForks().length - 1] = new Philosopher(getEnvironment().getForks().length - 1, this, getEnvironment().getForks()[getEnvironment().getForks().length - 1], getEnvironment().getForks()[0]);


		//Add neighborhood
		for (int i = 1; i < p.length; i++) {
			p[i].addNeighbor(p[i - 1]);
			p[i - 1].addNeighbor(p[i]);
		}
		p[0].addNeighbor(p[p.length - 1]);
		p[p.length - 1].addNeighbor(p[0]);
	}
}
```

### Create your agent ###

Create a class "Philosopher" which extends the class Agent<MyAMAS, Table>.


```
#!Java

public class Philosopher extends Agent<MyAMAS, Table> {

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
					else {
						left.release(this);
						right.release(this);
					}
				} else {
					left.release(this);
					right.release(this);
				}
				break;
			case THINK:
				if (new Random().nextInt(101) > 50) {
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
}
```


### Launch your system ###

In any class, create the environment and then the multi-agent system and launch it.


```
#!Java

public static void main(String[] args) {
	var env = new Table();
	var myAmas = new MyAMAS(env);
	var scheduler = new Scheduler(myAmas, env);
	scheduler.start();
}
```
