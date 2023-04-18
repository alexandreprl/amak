package fr.irit.smac.amak.scheduling

import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.ExecutorService
import java.util.function.Consumer

class SchedulerTest extends Specification {
    def "Constructor"() {
        given:
        def schedulable1 = Mock(Schedulable)
        def schedulable2 = Mock(Schedulable)
        def executorService = Mock(ExecutorService)

        when:
        def scheduler = new Scheduler(executorService, schedulable1, schedulable2)

        then:
        scheduler.pendingAdditionSchedulables.size() == 2
        scheduler.pendingAdditionSchedulables[0] == schedulable1
        scheduler.pendingAdditionSchedulables[1] == schedulable2
        scheduler.state == Scheduler.SchedulerState.IDLE
        scheduler.executorService == executorService
    }

    def "StartWithSleep"() {
        given:
        def executorService = Mock(ExecutorService)
        def scheduler = new Scheduler(executorService)
        def consumer = Mock(Consumer)
        scheduler.addOnChange(consumer)

        when:
        scheduler.startWithSleep(123)

        then:
        !scheduler.stateLock.locked
        scheduler.sleep == 123
        1 * consumer.accept(_)
        1 * executorService.submit(scheduler)
    }

    @Unroll
    def "when StartWithSleep is called and the scheduler is in #initialState state, then the executor should be run #runTimes"() {
        given:
        def executorService = Mock(ExecutorService)
        def scheduler = new Scheduler(executorService)
        scheduler.state = initialState
        def consumer = Mock(Consumer)
        scheduler.addOnChange(consumer)

        when:
        scheduler.startWithSleep(123)

        then:
        runTimes * executorService.submit(scheduler)

        where:
        initialState                          | runTimes
        Scheduler.SchedulerState.IDLE         | 1
        Scheduler.SchedulerState.PENDING_STOP | 0
        Scheduler.SchedulerState.RUNNING      | 0
    }

    def "Start"() {
        given:
        def executorService = Mock(ExecutorService)
        def scheduler = new Scheduler(executorService)
        def consumer = Mock(Consumer)
        scheduler.addOnChange(consumer)

        when:
        scheduler.start()

        then:
        !scheduler.stateLock.locked
        1 * consumer.accept(_)
        scheduler.sleep == 0
        1 * executorService.submit(scheduler)
    }

    def "When step is called and the scheduler is in state #initialState, then the executor should be run #runTimes and the next state should be #nextState"() {
        given:
        def executorService = Mock(ExecutorService)
        def scheduler = new Scheduler(executorService)
        scheduler.state = initialState
        def consumer = Mock(Consumer)
        scheduler.addOnChange(consumer)

        when:
        scheduler.step()

        then:
        runTimes * executorService.execute(scheduler)
        scheduler.state == nextState
        !scheduler.stateLock.locked
        1 * consumer.accept(_)

        where:
        initialState                          | runTimes | nextState
        Scheduler.SchedulerState.IDLE         | 1        | Scheduler.SchedulerState.PENDING_STOP
        Scheduler.SchedulerState.PENDING_STOP | 0        | Scheduler.SchedulerState.PENDING_STOP
        Scheduler.SchedulerState.RUNNING      | 0        | Scheduler.SchedulerState.RUNNING
    }

    def "When stop is called and the scheduler is in state #initialState, then the executor should the next state should be #nextState"() {
        given:
        def executorService = Mock(ExecutorService)
        def scheduler = new Scheduler(executorService)
        scheduler.state = initialState
        def consumer = Mock(Consumer)
        scheduler.addOnChange(consumer)

        when:
        scheduler.stop()

        then:
        scheduler.state == nextState
        !scheduler.stateLock.locked
        1 * consumer.accept(_)

        where:
        initialState                          | nextState
        Scheduler.SchedulerState.IDLE         | Scheduler.SchedulerState.IDLE
        Scheduler.SchedulerState.PENDING_STOP | Scheduler.SchedulerState.PENDING_STOP
        Scheduler.SchedulerState.RUNNING      | Scheduler.SchedulerState.PENDING_STOP
    }

    @Unroll
    def "When trying to run #cycles cycles from state #initialState"() {
        given:
        def schedulable1 = Mock(Schedulable)
        def schedulable2 = Mock(Schedulable)
        def executorService = Mock(ExecutorService)
        def scheduler = new Scheduler(executorService)
        scheduler.sleep = 1
        scheduler.state = Scheduler.SchedulerState.RUNNING
        def i = 0
        schedulable1.stopCondition() >> {
            i++
            return (i == cycles)
        }
        schedulable2.stopCondition() >> {
            scheduler.remove(schedulable2)
        }
        scheduler.add(schedulable1)
        scheduler.add(schedulable2)
        def consumer = Mock(Consumer)
        scheduler.onStop = consumer

        when:
        scheduler.run()

        then:
        !scheduler.stateLock.locked

        scheduler.pendingAdditionSchedulables.size() == 0
        scheduler.pendingRemovalSchedulables.size() == 0

        scheduler.schedulables.size() == 1
        scheduler.schedulables[0] == schedulable1

        1 * schedulable1.onSchedulingStarts()
        1 * schedulable2.onSchedulingStarts()

        cycles * schedulable1.cycle()
        cycles * schedulable2.cycle()

        1 * schedulable1.onSchedulingStops()
        1 * schedulable2.onSchedulingStops()

        1 * consumer.accept(_)

        where:
        cycles | initialState                          | effectivelyExecutedCycles
        1      | Scheduler.SchedulerState.RUNNING      | 1
        10     | Scheduler.SchedulerState.RUNNING      | 10
        10     | Scheduler.SchedulerState.PENDING_STOP | 1
    }
    def "When a schedulable throws an InterruptedException, the thread should stop and ending methods should be called"() {
        given:
        def schedulable1 = Mock(Schedulable)
        def executorService = Mock(ExecutorService)
        def scheduler = new Scheduler(executorService)
        scheduler.state == Scheduler.SchedulerState.RUNNING
        schedulable1.stopCondition() >> false
        schedulable1.cycle() >> {
            throw new InterruptedException();
        }
        scheduler.add(schedulable1)
        def consumer = Mock(Consumer)
        scheduler.onStop = consumer

        when:
        scheduler.run()

        then:
        1 * schedulable1.onSchedulingStops()
        1 * consumer.accept(_)
    }
}
