# Recent changes #

## 1.5.3 (11/28/2018) ##
### New features: none

### Possible breaking changes:
*Maybe unexpected amas behaviour if the fixed bug was used as a feature ! (see bug-fix `3da519a`)*

### Improvements and bug fixes:
+ Bug-fix (`fccf75e0`) on messaging (PNE)
+ Bug-Fix (`3da519a`) : on ExecutionPolicy.TWO_PHASES => before the correction, the execution policy was not executed correctly.
(Before : perception agent 1 -> decision&action agent 1 -> perception agent 2 -> decision&action agent 2 | After : perception agent 1 -> perception agent 2 -> decision&action agent 1 -> decision&action agent 2)



## 1.5.2 (11/22/2018) ##
### New features:
  + The agent ID can be specified when the agent is create: it is called "raw id".
  + The possibility to send a message directly with the raw id (not only with `AID`).
  + The `getReceivedEnvelopeGivenMessageType()` method and `IAmakReceivedEnvelope` method  simplify the message retrieval.

### Possible breaking changes:
* `IAmakMessagingService.buildNewAmakAddress(String)` --> `IAmakMessagingService.getOrCreateAmakAddress(String)`
* `IAmakMessagingService.dispose()` --> `IAmakMessagingService.disposeAll()`

### Improvements and bug fixes:
* Added the section "Communication agent with messages" in the readme file.
* When an agent is destroyed, its msgbox is deleted too.
* Some improvements in code quality using sonar reports
* All new features are tested
* Hot fix on messaging-agent library (0.1 > 0.2)

----

## 1.5.1 (09/04/2018) ##
Add agent messaging

## 1.5 (07/16/2018) ##
New AMAK version : new synchronous mode.

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


## 1.1.1 (02/20/2018)

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