package grails.plugin.concurrenttest

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class NamedThreadFactory implements ThreadFactory {
    static final AtomicInteger poolNumber = new AtomicInteger(1)
    final AtomicInteger threadNumber = new AtomicInteger(1)
    final ThreadGroup group

    NamedThreadFactory(String poolName) {
        group = new ThreadGroup("$poolName-${poolNumber.getAndIncrement()}")
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(group, r, "${group.name}-thread-${threadNumber.getAndIncrement()}", 0)
    }
}
