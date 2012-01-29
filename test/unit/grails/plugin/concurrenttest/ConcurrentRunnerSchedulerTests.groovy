package grails.plugin.concurrenttest

import grails.test.*

class ConcurrentRunnerSchedulerTests extends GrailsUnitTestCase {
    @Override
    protected void setUp() {
        super.setUp()
        Runtime.runtime.metaClass.availableProcessors = { -> 4 }
    }

    @Override
    protected void tearDown() {
        super.tearDown()
        Runtime.runtime.metaClass = null
    }

    int checkHowManyThreadsIsScheduled(ConcurrentRunnerScheduler scheduler) {
        Set threads = [] as Set
        100.times {
            scheduler.schedule(new PossiblyConcurrentRunnable({ threads << Thread.currentThread() }, Object))
        }
        scheduler.finished()
        threads.size()
    }

    void testDefaultNumberOfThreads() {
        def scheduler = new ConcurrentRunnerScheduler(new ConfigObject(), true)

        assert checkHowManyThreadsIsScheduled(scheduler) == 6
    }

    void testNumberOfThreadsFromConfiguration() {
        ConfigSlurper slurper = new ConfigSlurper()
        def scheduler = new ConcurrentRunnerScheduler(slurper.parse('grails.plugin.concurrentTest.threadCount = 10'), true)

        assert checkHowManyThreadsIsScheduled(scheduler) == 10
    }
}
