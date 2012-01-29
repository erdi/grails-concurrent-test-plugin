package grails.plugin.concurrenttest.test

import grails.plugin.concurrenttest.ConcurrentRunnerScheduler

abstract class AbstractConcurrentTest extends GroovyTestCase {
    void testIsRunConcurrently() {
        assert Thread.currentThread().name.startsWith(ConcurrentRunnerScheduler.THREAD_FACTORY_NAME)
    }
}
