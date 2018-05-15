This repository contains a framework made to facilitate the development of multi-agent system. 

Examples are available in the package fr.irit.smac.amak.examples.

# [Click here to download the latest standalone version](https://bitbucket.org/perlesa/amak/raw/master/Release/AmakFramework-standalone.jar?at=master) #

# Recent changes #


## 1.4 (05/15/2018) ##

A new visualization system called VUI has been added. 
VUI features:
- Zoom
- Move view
- Optimized rendering

Example:
```
//Initialization
drawablePoint = VUI.get().createPoint(dx, dy);
drawablePoint.setStrokeOnly();

//At runtime
drawablePoint.move(newX, newY);
```

## 1.3 (04/10/2018) ##

Now you can tell agents to execute in two phases. During the first phase, every agent perceives. Then, once they have all perceived, they decide and act. And finally, they wait for each other before starting the next cycle. By default, the execution policy is ONE_PHASE. 

Example:
```
Configuration.executionPolicy = ExecutionPolicy.TWO_PHASES;
MyAMAS amas = new MyAMAS(new MyEnvironment(), Scheduling.DEFAULT);
```


## 1.2 (04/09/2018) ##

AMAK now includes the possibility to execute simultaneously multiple agents. By default, it is disabled.
Before any calls, change the value of Configuration.allowedSimultaneousAgentsExecution to the amount of threads you wish to use for agent cycles.

Example:
```
Configuration.allowedSimultaneousAgentsExecution = 4;
MyAMAS amas = new MyAMAS(new MyEnvironment(), Scheduling.DEFAULT);
```

Also, the methods onSystemCycleBegin and onSystemCycleEnd are not called anymore. It is recommended to use onAgentCycleBegin and onAgentCycleEnd as it should produce the exact same results.


## 1.1.1 (02/20/2018)##

Please note that the environment is now schedulable as the Amas and DrawableUI.
To ensure compatibility, check that every constructor of your classes inheriting Amas, Environment or DrawableUI make a call to their super constructor and pass a valid Scheduling option. In most cases, it is recommend to use default scheduler.

Example:
```
public class MyEnvironment extends Environment {
	public MyEnvironment() {
		super(Scheduling.DEFAULT);
	}
}
```


# General #

Amak is a framework aiming at facilitating the development of multi-agent systems.
For the framework to work properly, you need to create a type of environment, a type of agent and a type of multi-agent system.

Once you've created your three classes, you have to complete them by overriding methods present in higher level classes. Go into your classes, start typing "on" and press ctrl+space, a list of the most useful methods to override will show.
At first, you will probably need to override onPerceive, onDecide and onAct in your agents, and onInitialAgentsCreation in your multi-agent system.
All of these methods are called at the right time by the scheduler, you only have to implement them.

The scheduling of agents is loaded automatically when you create an instance of your multi-agent system.

Amak is packaged with other useful libraries (lx-plot, avt, ...). Also, if you need to draw something easily, have a look at the abstract class DrawableUI.

# Quick start (with eclipse)#

In this quick start example, we will do the first steps aiming at creating an adaptive multi-agent system to solve the [philosopher's dinner problem](https://en.wikipedia.org/wiki/Dining_philosophers_problem).

## Create the eclipse prject ##

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
	protected void onDecideAct() {
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
	protected void onDraw() {
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