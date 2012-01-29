class ConcurrentTestGrailsPlugin {
    def version = "0.1"

    def grailsVersion = "1.3.2 > *"
    def dependsOn = [:]
    def pluginExcludes = ["web-app"]

    def author = "Marcin Erdmann"
    def authorEmail = "marcin.erdmann@proxerd.pl"
    def title = "Cuncurrent Test plugin"
    def description = '''Allows to run Grails JUnit tests concurrently.'''
    def documentation = "http://grails.org/plugin/concurrent-test-type"
}
