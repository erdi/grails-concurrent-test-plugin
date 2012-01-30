package grails.plugin.concurrenttest

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder
import org.junit.runner.Description
import org.junit.runner.Runner
import org.junit.runner.notification.RunNotifier
import org.junit.runners.Suite
import org.junit.runners.model.Statement

class ConcurrentSuite extends Suite {

    private ConcurrentRunnerScheduler customScheduler
    private Map<String, Class> testClasses

    ConcurrentSuite(Class<?>[] classes, ConfigObject config, boolean implicit) {
        super(new AllDefaultPossibilitiesBuilder(true), classes)
        customScheduler = new ConcurrentRunnerScheduler(config, implicit)
        testClasses = classes.inject([:]) {Map nameToClass, Class c ->
            nameToClass << new MapEntry(c.name, c)
        }
    }

    protected Statement childrenInvoker(final RunNotifier notifier) {
        return new Statement() {
            @Override
            public void evaluate() {
                ConcurrentSuite.this.children.each { Runner runner ->
                    customScheduler.schedule(createPossiblyConcurrentRunnable({
                        runChild(runner, notifier)
                    }, testClasses[describeChild(runner).className]))
                }
                customScheduler.finished()
            }
        };
    }

    Map<String, Integer> getTestCountForTestClasses() {
        children.inject([:]) { Map map, Runner child ->
            Description classDescription = describeChild(child)
            map << new MapEntry(classDescription.className, classDescription.testCount())
        }
    }

    protected PossiblyConcurrentRunnable createPossiblyConcurrentRunnable(Closure runClosure, Class testClass) {
        new PossiblyConcurrentRunnable(runClosure, testClass)
    }
}

