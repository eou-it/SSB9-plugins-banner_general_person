
/** *****************************************************************************
 Copyright 2009-2012 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

modules = {

    'survey' {
        dependsOn "bannerSelfService, i18n-core"
        defaultBundle environment == "development" ? false : "survey"
        //defaultBundle false

        resource url: [plugin: 'banner-general-person', file: 'css/views/survey/survey.css'], attrs: [media: 'screen, projection']
        resource url: [plugin: 'banner-general-person', file: 'js/views/survey/survey.js']
    }

}
