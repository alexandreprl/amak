# AMAK Version 3 - Major update 2023 #

[![DOI](https://zenodo.org/badge/632796095.svg)](https://doi.org/10.5281/zenodo.14054179)

This repository contains a framework made to facilitate the development of multi-agent system.

**Note: Version 3 introduces a lot of breaking changes if you are having trouble migrating your project or creating a new one, do not hesitate to contact me.**

The previous version of AMAK is still accessible on branch **v2**

4 examples/exercises are available:
- **Philosopher's dinner**: Follow the quick start section below
- **Randomly moving ants** at https://github.com/alexandreprl/amak-example-ants
- **Drone exercise** at https://github.com/alexandreprl/amak-exercise-drone
- **Transporter robots** at https://github.com/alexandreprl/amak-exercise-transporter-robots
- **Color Cubes** at https://github.com/alexandreprl/amak-exercise-color-cubes-game

An article is available at https://www.researchgate.net/publication/325851056_AMAK_-_A_Framework_for_Developing_Robust_and_Open_Adaptive_Multi-agent_Systems

# General #

Amak is a framework aiming at facilitating the development of multi-agent systems.
For the framework to work properly, you need to create a type of environment, a type of agent and a type of multi-agent system.

Once you've created your three classes, you have to complete them by overriding methods present in higher level classes. Go into your classes, start typing "on" and press ctrl+space, a list of the most useful methods to override will show.
At first, you will probably need to override onPerceive, onDecide and onAct in your agents, and onInitialAgentsCreation in your multi-agent system.
All of these methods are called at the right time by the scheduler, you only have to implement them.

# Quick start (with IntelliJ IDEA)#

In this quick start example, we will do the first steps aiming at creating an adaptive multi-agent system to solve the [philosopher's dinner problem](https://en.wikipedia.org/wiki/Dining_philosophers_problem).

## Create the gradle project ##

- Click on File -> New -> Project
- Enter a name : "Name of your project"
- Pick the language : Java
- Pick the build system : Gradle
- Pick the JDK : 17 (recommended)
- Pick the Gradle DSL : Groovy
- Click on **Create**

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

* Add AMAK and LxPlot to the dependencies
```
dependencies {

    // Add the 4 following lines
    // AMAK
    implementation 'com.github.alexandreprl:amak:3.1.0'
    // LxPlot
    implementation 'com.github.alexandreprl:lxplot:2.0.0'
    
    ...
}
```

* Click on the Gradle Refresh button

## Create your first multi-agent system ##

In order to work, the Amak framework needs a type of environment, a type of agent and a type of multi-agent system.


### Create your first environment ###

Create a new class named "MyEnvironment" which extends the abstract class "Environment".


```
#!Java
import fr.irit.smac.amak.Environment;

public class MyEnvironment extends Environment {

	public MyEnvironment() {
		// Setup your environment here
	}
}
```

### Create your multi-agent system ###

Let's call the multi-agent system "MyAMAS".
Create a class "MyAMAS" which extends the abstract class `Amas<MyEnvironment>`.

```
#!Java
import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.scheduling.Scheduler;

public class MyAMAS extends Amas<MyEnvironment> {
	public MyAMAS(MyEnvironment env) {
		super(env, 1, Amas.ExecutionPolicy.ONE_PHASE);
	}

	@Override
	protected void onInitialAgentsCreation() {
		// Instantiate your agents here
	}
}
```

### Create your agent ###


```
#!Java
import fr.irit.smac.amak.Agent;
import fr.irit.smac.lxplot.LxPlot;
import fr.irit.smac.lxplot.commons.ChartType;

import java.util.Random;

public class MyAgent extends Agent<MyAMAS, MyEnvironment> {

	public MyAgent(MyAMAS amas) {
		super(amas);
	}

	@Override
	protected void onPerceive() {
	    // Perceive the environment and other agents here
	}

	@Override
	protected void onDecideAndAct() {
	    // Decide and act here
	}
}
```


### Launch your system ###

In any class, create the environment, the multi-agent system and a scheduler and run the main.


```
#!Java

public static void main(String[] args) {
	var env = new MyEnvironment();
	var myAmas = new MyAMAS(env);
	var scheduler = new Scheduler(myAmas, env);
	scheduler.start();
}
```
