package grails.plugin.concurrenttest

import java.util.concurrent.*

class ConcurrentRunnerScheduler {
    static final String THREAD_FACTORY_NAME = 'concurrent-suite'

    ExecutorService executorService
    CompletionService<Void> completionService
    Queue<Future<Void>> tasks = new LinkedList<Future<Void>>()
    List<Runnable> tasksToBeRunSerially = []
    boolean implicit

    ConcurrentRunnerScheduler(ConfigObject config, boolean implicit) {
        def threadCountConfig = config.grails.plugin.concurrentTest.threadCount
        int threadCount = threadCountConfig in Integer ? threadCountConfig : Runtime.runtime.availableProcessors() * 1.5
        executorService = Executors.newFixedThreadPool(threadCount, new NamedThreadFactory(THREAD_FACTORY_NAME))
        completionService = new ExecutorCompletionService<Void>(executorService)
        this.implicit = implicit
    }

    void schedule(PossiblyConcurrentRunnable childStatement) {
        if ((implicit && !childStatement.cannotRunConcurrently) || childStatement.canRunConcurrently) {
            tasks.offer(completionService.submit(childStatement, null))
        } else {
            tasksToBeRunSerially << childStatement
        }
    }

    void finished() {
        try {
            while (!tasks.isEmpty()) {
                tasks.remove(completionService.take())
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt()
        } finally {
            while (!tasks.isEmpty()) {
                tasks.poll().cancel(true)
            }
            executorService.shutdownNow()
        }
        tasksToBeRunSerially.each { it.run() }
    }
}
