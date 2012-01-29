package grails.plugin.concurrenttest.alltest

import grails.plugin.concurrenttest.RunSequentially

@RunSequentially
class SequentialTest extends GroovyTestCase{
    void testIsRunSerially() {
        assert Thread.currentThread().name == 'main'
    }
}
