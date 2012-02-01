package grails.plugin.concurrenttest

import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import org.codehaus.groovy.grails.test.io.SystemOutAndErrSwapper
import org.codehaus.groovy.grails.test.junit4.listener.PerTestRunListener
import org.codehaus.groovy.grails.test.report.junit.JUnitReports
import org.junit.runner.Description
import org.junit.runner.notification.Failure

class PerTestCachingRunListener {
    List<Closure> methodCalls = []
    List<Closure> counterCalls = []
    PerTestRunListener listener
    int testCount
    private int testsRunCount

    PerTestCachingRunListener(String name, GrailsTestEventPublisher eventPublisher, JUnitReports reports,
                     SystemOutAndErrSwapper outAndErrSwapper) {
        listener = new PerTestRunListener(name, eventPublisher, reports, outAndErrSwapper)
    }

    boolean getAllTestsRun() {
        testCount == testsRunCount
    }

    private void queueCounterIncrement() {
        testsRunCount++
    }

    private void queueListenerCall(Closure closure) {
        methodCalls << closure
    }

    void testFailure(Failure failure) {
        queueCounterIncrement()
        queueListenerCall { listener.testFailure(failure) }
    }

    void testFinished(Description description) {
        queueCounterIncrement()
        queueListenerCall { listener.testFinished(description) }
    }

    void updateCounters() {
        counterCalls.each { it.call() }.clear()
    }

    void finish() {
        methodCalls.each { it.call() }.clear()
        listener.finish()
    }

    def methodMissing(String name, args) {
        queueListenerCall { listener.invokeMethod(name, args) }
    }
}
