/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import net.hedtech.banner.exceptions.ApplicationException
import org.hibernate.StaleObjectStateException
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.RequestContextHolder

@Transactional(readOnly = false, propagation = Propagation.REQUIRED )
class PersonGenderPronounCompositeService {
    def sessionFactory
    def personBasicPersonBaseService
    def maritalStatusService

    def fetchPersonalDetails(pidm) {
        def personalDetails = personBasicPersonBaseService.getPersonalDetailsForPersonalInformation(pidm)

        if(checkGenderPronounInstalled()) {
            def fetchResult = fetchPersonsGenderPronoun(personalDetails.id)
            personalDetails.gender = fetchResult.gender
            personalDetails.pronoun = fetchResult.pronoun
        }
        return personalDetails
    }

    def updatePerson(Map person) {
        person.maritalStatus = maritalStatusService.fetchByCode(person.maritalStatus.code)

        if(checkGenderPronounInstalled()) {

            def persistedPerson = fetchPersonalDetails(person.pidm)
            def genderCode = null
            def pronounCode = null

            if (!person.gender) {
                genderCode = persistedPerson.gender.code
            }
            else if (person.gender.code) {
                if(person.gender.code != persistedPerson.gender.code) {
                    fetchGenderByCode(person.gender.code)
                    genderCode = person.gender.code
                }
                else {
                    genderCode = persistedPerson.gender.code
                }
            }

            if(!person.pronoun) {
                pronounCode = persistedPerson.pronoun.code
            }
            else if (person.pronoun.code) {
                if(person.pronoun.code != persistedPerson.pronoun.code) {
                    fetchPronounByCode(person.pronoun.code)
                    pronounCode = person.pronoun.code
                }
                else {
                    pronounCode = persistedPerson.pronoun.code
                }
            }

            boolean isDirty = (genderCode != persistedPerson.gender.code) ||
                    (pronounCode != persistedPerson.pronoun.code)
            def baseUpdateResult = personBasicPersonBaseService.update(person)
            int version = person.version + 1 == baseUpdateResult.version ? baseUpdateResult.version : person.version

            if(isDirty) {
                def personUpdateSql = 'update spbpers set spbpers_gndr_code = ?, spbpers_pprn_code = ? where spbpers_surrogate_id = ? and spbpers_version = ?'
                int rowsUpdated = sessionFactory.getCurrentSession().createSQLQuery(personUpdateSql)
                            .setString(0, genderCode)
                            .setString(1, pronounCode)
                            .setLong(2, persistedPerson.id)
                            .setInteger(3, version)
                            .executeUpdate()

                if (rowsUpdated == 0) {
                    throw new ApplicationException( this.class, new HibernateOptimisticLockingFailureException( new StaleObjectStateException( PersonBasicPersonBase.class.simpleName, persistedPerson.id ) ) )
                }
            }
        }
        else {
            personBasicPersonBaseService.update(person)
        }
    }

    def fetchGenderList(int max = 10, int offset = 0, String searchString = '') {
        def resultList = []
        if (checkGenderPronounInstalled()) {
            def genderSql = 'select gtvgndr_gndr_code, gtvgndr_gndr_desc ' +
                    'from ' +
                    '(select a.*, rownum rnum ' +
                       'from ' +
                       '(select gtvgndr_gndr_code, gtvgndr_gndr_desc ' +
                          'from gtvgndr ' +
                          'where gtvgndr_active_ind = \'Y\' and gtvgndr_web_ind = \'Y\' ' +
                          'and upper(gtvgndr_gndr_desc) like ? ' +
                          'order by gtvgndr_gndr_desc, gtvgndr_gndr_code) a ' +
                      'where rownum <= ?) ' +
                    'where rnum > ?'
            String preppedSearchString = '%' + searchString.toUpperCase() + '%'

            resultList = sessionFactory.getCurrentSession().createSQLQuery(genderSql)
                        .setString(0, preppedSearchString)
                        .setInteger(1, max+offset)
                        .setInteger(2, offset).list().collect { it = [code: it[0], description: it[1]] }
        }

        resultList
    }

    def fetchPronounList(int max = 10, int offset = 0, String searchString = '') {
        def resultList = []
        if (checkGenderPronounInstalled()) {
            def pronounSql = 'select gtvpprn_pprn_code, gtvpprn_pprn_desc ' +
                    'from ' +
                    '(select a.*, rownum rnum ' +
                       'from ' +
                       '(select gtvpprn_pprn_code, gtvpprn_pprn_desc ' +
                          'from gtvpprn ' +
                          'where gtvpprn_active_ind = \'Y\' and gtvpprn_web_ind = \'Y\' ' +
                          'and upper(gtvpprn_pprn_desc) like ? ' +
                          'order by gtvpprn_pprn_desc, gtvpprn_pprn_code) a ' +
                      'where rownum <= ?) ' +
                    'where rnum > ?'
            String preppedSearchString = '%' + searchString.toUpperCase() + '%'

                resultList = sessionFactory.getCurrentSession().createSQLQuery(pronounSql)
                        .setString(0, preppedSearchString)
                        .setInteger(1, max+offset)
                        .setInteger(2, offset).list().collect { it = [code: it[0], description: it[1]] }
        }

        resultList
    }

    boolean checkGenderPronounInstalled() {
        boolean isGenderPronounInstalled
        def session = RequestContextHolder?.currentRequestAttributes()?.request?.session

        if (session?.getAttribute("isGenderPronounInstalled") != null) {
            isGenderPronounInstalled = session.getAttribute("isGenderPronounInstalled")
        }
        else {
            isGenderPronounInstalled = checkForGtvndrGtvpprnTablesAndColumns()
            session.setAttribute("isGenderPronounInstalled", isGenderPronounInstalled)
        }
        return isGenderPronounInstalled
    }

    private def fetchGenderByCode(code) {
        if (code != null) {
            def genderResult = []
            def genderSql = 'select GTVGNDR_GNDR_CODE, GTVGNDR_GNDR_DESC, GTVGNDR_ACTIVE_IND, GTVGNDR_WEB_IND ' +
                    'from gtvgndr where gtvgndr_gndr_code = ? and gtvgndr_active_ind = \'Y\' and gtvgndr_web_ind = \'Y\''

            genderResult = sessionFactory.getCurrentSession().createSQLQuery(genderSql).setString(0, code).list()[0]

            if (genderResult == null) {
                throw new ApplicationException(this, "@@r1:invalidGenderCode@@")
            }

            return [code: genderResult[0], description: genderResult[1]]
        }
        else {
            throw new ApplicationException(this, "@@r1:invalidGenderCode@@")
        }
    }

    private def fetchPronounByCode(code) {
        if(code != null) {
            def pronounResult
            def pronounSql = 'select GTVPPRN_PPRN_CODE, GTVPPRN_PPRN_DESC, GTVPPRN_ACTIVE_IND, GTVPPRN_WEB_IND ' +
                    'from gtvpprn where gtvpprn_pprn_code = ? and gtvpprn_active_ind = \'Y\' and gtvpprn_web_ind = \'Y\''

            pronounResult = sessionFactory.getCurrentSession().createSQLQuery(pronounSql).setString(0, code).list()[0]

            if (pronounResult == null) {
                throw new ApplicationException(this, "@@r1:invalidPronounCode@@")
            }

            return [code: pronounResult[0], description: pronounResult[1]]
        }
        else {
            throw new ApplicationException(this, "@@r1:invalidPronounCode@@")
        }
    }


    private def fetchPersonsGenderPronoun(personBaseId) {
        def result
        def fetchGenderPronounSql = 'select spbpers_gndr_code, spbpers_pprn_code, gtvgndr_gndr_desc, gtvpprn_pprn_desc ' +
                'from spbpers ' +
                'left join gtvgndr on spbpers_gndr_code = gtvgndr_gndr_code ' +
                'left join gtvpprn on spbpers_pprn_code = gtvpprn_pprn_code ' +
                'where spbpers_surrogate_id = ?'

        result = sessionFactory.getCurrentSession().createSQLQuery(fetchGenderPronounSql).setLong(0, personBaseId).list()[0]

        return [gender: [code: result[0], description: result[2]],
                pronoun: [code: result[1], description: result[3]]]
    }

    private boolean checkForGtvndrGtvpprnTablesAndColumns() {
        boolean gtvgndrGtvpprnTablesAndColumnsExist = false;
        def genderColumnSql = 'select count(*) from all_tab_cols ' +
                'where table_name = \'SPBPERS\' and column_name = \'SPBPERS_GNDR_CODE\''

        gtvgndrGtvpprnTablesAndColumnsExist = sessionFactory.getCurrentSession().createSQLQuery(genderColumnSql).list()[0] == 1

        return gtvgndrGtvpprnTablesAndColumnsExist
    }

}
