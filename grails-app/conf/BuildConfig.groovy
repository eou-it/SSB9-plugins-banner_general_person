grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir	= "target/test-reports"
grails.project.dependency.resolution = {

    inherits( "global" ) {

    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {

        mavenRepo "http://m038083.sungardhe.com:8081/nexus/content/repositories/releases/"
        mavenRepo "http://m038083.sungardhe.com:8081/nexus/content/repositories/snapshots/"
        mavenRepo "http://m038083.sungardhe.com:8081/nexus/content/repositories/thirdparty/"
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenRepo "http://repository.jboss.org/maven2/"
        mavenRepo "http://repository.codehaus.org"
    }

      plugins {
        compile 'com.sungardhe:banner-core:0.3.7'   // Note: Also update version within 'application.properties'
        compile 'com.sungardhe:spring-security-cas:1.0.2'
        compile 'com.sungardhe:banner-general-validation-common:0.0.7' // Note: Also update version within 'application.properties'

		compile 'com.sungardhe:banner-db-main:0.0.11' // Note: Also update version within 'application.properties'
        compile 'com.sungardhe:banner-seeddata-catalog:0.0.9'
    }
    dependencies {

    }

}
