/** *******************************************************************************
 Copyright 2009-2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
grails.project.dependency.resolver = "ivy"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.plugin.location.'banner-core' = "../banner_core.git"
grails.plugin.location.'banner-seeddata-catalog'="../banner_seeddata_catalog.git"
grails.plugin.location.'banner-general-validation-common' = "../banner_general_validation_common.git"
grails.plugin.location.'i18n-core'="../i18n_core.git"

grails.project.dependency.resolver = "ivy" // or maven

grails.project.dependency.resolution = {

    inherits("global") {

    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        if (System.properties['PROXY_SERVER_NAME']) {
            mavenRepo "${System.properties['PROXY_SERVER_NAME']}"
        } else {
            grailsPlugins()
            grailsHome()
            grailsCentral()
            mavenCentral()
            mavenRepo "http://repository.jboss.org/maven2/"
            mavenRepo "http://repository.codehaus.org"
        }
    }


    dependencies {

    }

    plugins {
        test ':code-coverage:1.2.5'
        runtime  ":hibernate:3.6.10.10"
        compile ":tomcat:7.0.52.1"
        compile ':resources:1.2.7' // If the functional-test plugin is being used
        compile ":functional-test:2.0.0" // If the functional-test plugin is being used
    }

}
