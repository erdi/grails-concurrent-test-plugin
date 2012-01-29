package grails.plugin.concurrenttest

class PossiblyConcurrentRunnable implements Runnable {
    final Closure runClosure
    Class testClass

    PossiblyConcurrentRunnable(Closure runClosure, Class testClass) {
        this.runClosure = runClosure
        this.testClass = testClass
    }

    boolean getCanRunConcurrently() {
        testClass.getAnnotation(RunConcurrently)
    }

    boolean getCannotRunConcurrently() {
        testClass.getAnnotation(RunSequentially)
    }

    void run() {
        runClosure()
    }
}
