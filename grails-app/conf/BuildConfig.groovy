grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    inherits("global") {
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
    }
    dependencies {
        build('xom:xom:1.2.5') {
            excludes 'xml-apis'
            export = false
        }
    }
    plugins {
        build(':new-doc:0.3.2') {
            excludes 'junit', 'xom', 'release'
            export = false
        }
        build(':release:1.0.0.RC3') {
            export = false
        }
    }
}
