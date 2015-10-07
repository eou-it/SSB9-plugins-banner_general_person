/** *******************************************************************************
 Copyright 2009-2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
grails.project.dependency.resolver = "maven"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.plugin.location.'banner-general-validation-common' = "../banner_general_validation_common.git"

grails.project.dependency.resolver = "maven" // or maven

grails.project.dependency.resolution = {

    inherits("global") {

    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        if (System.properties['PROXY_SERVER_NAME']) {
            mavenRepo "${System.properties['PROXY_SERVER_NAME']}"
        } else {
            grailsCentral()
            mavenCentral()
            mavenRepo "http://repository.jboss.org/maven2/"
            mavenRepo "https://code.lds.org/nexus/content/groups/main-repo"
        }
    }


    dependencies {
        compile 'com.googlecode.libphonenumber:libphonenumber:6.2'

    }

    plugins {
        
    }

}

// CodeNarc rulesets
codenarc.ruleSetFiles="rulesets/banner.groovy"
codenarc.reportName="target/CodeNarcReport.html"
codenarc.propertiesFile="grails-app/conf/codenarc.properties"
