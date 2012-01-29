package grails.plugin.concurrenttest.test
class SequentialTest extends GroovyTestCase{
    void testIsRunSerially() {
        assert Thread.currentThread().name == 'main'
    }
}
