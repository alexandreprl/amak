package fr.irit.smac.amak;

import fr.irit.smac.amak.Amas.ExecutionPolicy;

/**
 * This class is used to define global configuration BEFORE calling any other
 * classes/methods of the framework.
 * 
 * @author Alexandre Perles
 *
 */
public class Configuration {
	/**
	 * The maximal number of threads that can be executed simultaneously
	 */
	public static int allowedSimultaneousAgentsExecution = 1;
	/**
	 * The execution policy refers to the synchronization between agents execution.
	 * ONE_PHASE means that agents can realize simultaneous cycles and wait for each
	 * other after the action. TWO_PHASES means that agents perceive simultaneously
	 * then wait for each others and then decide and act simultaneously and finally
	 * wait for each others.
	 */
	public static ExecutionPolicy executionPolicy = ExecutionPolicy.ONE_PHASE;

	/**
	 * By default, mas made with amak are meant to be executed on a graphical
	 * environment. However, some may need to execute a mas without any GUI. To
	 * guarantee the well functioning of this option, you have to check that you
	 * only render stuff in methods such as onUpdaterRender
	 * onRenderingInitialization... Also, do not forget to set your schedulers to
	 * HIDDEN or DEFAULT
	 */
	public static boolean commandLineMode = false;
}
