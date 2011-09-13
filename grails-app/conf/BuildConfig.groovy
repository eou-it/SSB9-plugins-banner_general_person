/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard, Banner and Luminis are either 
 registered trademarks or trademarks of SunGard Higher Education in the U.S.A. 
 and/or other regions and/or countries.
 **********************************************************************************/
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
        compile 'com.sungardhe:banner-core:0.3.16'   // Note: Also update version within 'application.properties'
        compile 'com.sungardhe:banner-general-validation-common:0.0.11' // Note: Also update version within 'application.properties'

    }
    dependencies {

    }

}
