package grails.plugin.concurrenttest

import org.codehaus.groovy.grails.test.junit4.JUnit4GrailsTestType

class ConcurrentJUnitTestType extends JUnit4GrailsTestType {
    Map<String, Integer> testCountForTestClasses
    ConfigObject config
    boolean implicit

    ConcurrentJUnitTestType(String name, String sourceDirectory, ConfigObject config, boolean implicit) {
        super(name, sourceDirectory)
        this.config = config
        this.implicit = implicit
    }

    @Override
    protected Object createSuite(classes) {
        ConcurrentSuite suite = createConcurrentSuite(classes)
        testCountForTestClasses = suite.testCountForTestClasses
        suite
    }

    @Override
    protected Object createListener(eventPublisher) {
        new ConcurrentSuiteRunListener(eventPublisher, createJUnitReportsFactory(), createSystemOutAndErrSwapper(), testCountForTestClasses)
    }

    protected ConcurrentSuite createConcurrentSuite(classes) {
        new ConcurrentSuite(createRunnerBuilder(), classes as Class[], config, implicit)
    }
}
