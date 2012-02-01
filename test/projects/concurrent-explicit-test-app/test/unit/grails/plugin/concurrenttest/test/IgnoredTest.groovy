package grails.plugin.concurrenttest.test

import org.junit.Ignore

class IgnoredTest extends GroovyTestCase {
    @Ignore
    void testIgnored() {
        assert false
    }
}
