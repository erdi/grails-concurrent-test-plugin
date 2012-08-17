class ConcurrentTestGrailsPlugin {
    def version = "0.2"

    def grailsVersion = "1.3.2 > *"
    def dependsOn = [:]
    def pluginExcludes = ["web-app", "src/docs/**"]

    def author = "Marcin Erdmann"
    def authorEmail = "marcin.erdmann@proxerd.pl"
    def title = "Concurrent Test Plugin"
    def description = '''Allows users to run Grails JUnit tests concurrently.'''
    def documentation = "http://erdi.github.com/grails-concurrent-test-plugin/"

    def issueManagement = [ system: "Github", url: "http://github.com/erdi/grails-concurrent-test-plugin/issues" ]
    def scm = [ url: "http://github.com/erdi/grails-concurrent-test-plugin" ]
}
