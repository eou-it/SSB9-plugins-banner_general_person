package net.hedtech.banner.loginworkflow

/*import net.hedtech.banner.general.person.PersonBasicPersonBase*/
/*import net.hedtech.banner.general.system.SdaCrosswalkConversion*/
/*import net.hedtech.banner.general.system.Subject*/

/** *****************************************************************************
 © 2013 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
class RaceAndEthnicityFlow { // implements PostLoginWorkflow {

    /*public boolean showPage(request) {
        def session = request.getSession();
        String isDone = session.getAttribute("surveydone")
        if(isDone == "true") {
            return false
        }
        else {
            return true
        }
    }*/


    public static boolean showPage(request) {
//        ApplicationContext ctx1 = (ApplicationContext) ApplicationHolder.getApplication().getMainContext()
//        dataSource = (BannerDS)ctx1.getBean("dataSource")
//
//        dataSource = SpringContextUtils.getApplicationContext().getBean("dataSource")
//        /* for groovy sql */
//        def ctx = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
//        def sessionFactory = ctx.sessionFactory
//        def session1 = sessionFactory.currentSession // TODO: Find out if this session is same as request.getSession() below
//        def sql = new Sql(session1.connection())
//        /* for groovy sql ends */
//
//        def session = request.getSession()
//        String isDone = session.getAttribute("surveydone")
//        def pushSurvey = false
//        if (isDone != "true") {
//            // Survey is not yet taken.
//            // TODO: Uncomment this block if you want to use named queries
//            /*def sdaxForSurveyStartDate = SdaCrosswalkConversion.fetchSurveyDates('RESTARTDAT', 1, 'SSMREDATE')[0]
//            def sdaxForSurveyEndDate = SdaCrosswalkConversion.fetchSurveyDates('REENDDATE', 1, 'SSMREDATE')[0]
//            if (sdaxForSurveyStartDate && sdaxForSurveyEndDate) {
//                def today = new Date()
//                def surveyStartDate = sdaxForSurveyStartDate?.reportingDate
//                def surveyEndDate = sdaxForSurveyEndDate?.reportingDate ?: today
//                // Survey start date is not null & Today is between Survey start and end dates
//                if (surveyStartDate && (surveyStartDate <= today && today <= surveyEndDate)) {
//                    if (PersonBasicPersonBase.fetchByPidm(34525)?.confirmedRe != 'Y') {
//                        pushSurvey = true
//                    }
//                }
//            }*/
//            def surveyStartDateSql = """ select gtvsdax_reporting_date from gtvsdax
//                                            where gtvsdax_internal_code = ?
//                                            and gtvsdax_internal_code_seqno = ?
//                                            and gtvsdax_internal_code_group = ?
//                                            and gtvsdax_sysreq_ind = 'Y' """
//            def surveyStartDate = sql.firstRow(surveyStartDateSql, ['RESTARTDAT', 1, 'SSMREDATE'])
//            return pushSurvey
//        } else {
//            // Do not show Survey page as Survey has already been taken.
//            return pushSurvey
//        }

        /*def subject = Subject.findByCode('HIST')*/
        /*def sdaxForSurveyStartDate = SdaCrosswalkConversion.fetchSurveyDates('RESTARTDAT', 1, 'SSMREDATE')[0]
        def sdaxForSurveyEndDate = SdaCrosswalkConversion.fetchSurveyDates('REENDDATE', 1, 'SSMREDATE')[0]*/
        return true
    }


    public String getControllerUri() {
        return "/ssb/survey"
    }
}
