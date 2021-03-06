package grails.plugin.concurrenttest

import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher
import org.codehaus.groovy.grails.test.io.SystemOutAndErrSwapper
import org.codehaus.groovy.grails.test.report.junit.JUnitReportsFactory
import org.junit.runner.Description
import org.junit.runner.Result
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunListener

class ConcurrentSuiteRunListener extends RunListener {
    Map<String, PerTestCachingRunListener> specListeners
    Map<String, Integer> testCountForTestClasses

    ConcurrentSuiteRunListener(GrailsTestEventPublisher eventPublisher, JUnitReportsFactory reportsFactory, SystemOutAndErrSwapper outAndErrSwapper, Map<String, Integer> testCountForTestClasses) {
        this.testCountForTestClasses = testCountForTestClasses
        specListeners = [:].withDefault { String name ->
            def listener = new PerTestCachingRunListener(name, eventPublisher, reportsFactory.createReports(name), outAndErrSwapper)
            listener.testCount = testCountForTestClasses[name]
            listener.start()
            listener
        }
    }

    void testRunStarted(Description description) {
        // nothing to do
    }

    void testStarted(Description description) {
        specListeners[description.className].testStarted(description)
    }

    void testFailure(Failure failure) {
        specListeners[failure.description.className].testFailure(failure)
    }

    void testAssumptionFailure(Failure failure) {
        specListeners[failure.description.className].testFailure(failure)
    }

    synchronized void testFinished(Description description) {
        specListeners[description.className].testFinished(description)
        checkIfLastTestAndFinish()
    }

    void testIgnored(Description description) {
        //nothing to do
    }

    private void checkIfLastTestAndFinish() {
        Map.Entry<String, PerTestCachingRunListener> finished = specListeners.each { String name, PerTestCachingRunListener listener ->
            listener.updateCounters()
        }.find { String name, PerTestCachingRunListener listener -> listener.allTestsRun }
        if (finished) {
            finished.value.finish()
            specListeners.remove(finished.key)
        }
    }

    void testRunFinished(Result result) {
        //nothing to do
    }
}
