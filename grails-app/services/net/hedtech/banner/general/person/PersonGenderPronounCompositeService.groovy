/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.person

import groovy.sql.Sql
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
        def personalDetails = personBasicPersonBaseService.getPersonalDetails(pidm)

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
            boolean isDirty = (person.gender?.code != persistedPerson.gender.code) ||
                    (person.pronoun?.code != persistedPerson.pronoun.code)
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

            def baseUpdateResult = personBasicPersonBaseService.update(person)
            int version = person.version + 1 == baseUpdateResult.version ? baseUpdateResult.version : person.version

            if(isDirty) {
                Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
                def personUpdateSql = 'update spbpers set spbpers_gndr_code = ?, spbpers_pprn_code = ? where spbpers_surrogate_id = ? and spbpers_version = ?'
                try {
                    int rowsUpdated = sql.executeUpdate(personUpdateSql, [genderCode, pronounCode, persistedPerson.id, version])
                    if (rowsUpdated == 0) {
                        throw new ApplicationException( this.class, new HibernateOptimisticLockingFailureException( new StaleObjectStateException( PersonBasicPersonBase.class.simpleName, persistedPerson.id ) ) )
                    }
                }
                finally {
                    sql?.close()
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
            Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
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
                    'where rnum >= ?'
            String preppedSearchString = '%' + searchString.toUpperCase() + '%'

            try {
                resultList = sql.rows(genderSql, [preppedSearchString, max, offset]).collect {
                    it = [code: it.gtvgndr_gndr_code, description: it.gtvgndr_gndr_desc]
                }
            } finally {
                sql?.close()
            }
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
            isGenderPronounInstalled = checkForGndrPprnTablesAndColumns()
            session.setAttribute("isGenderPronounInstalled", isGenderPronounInstalled)
        }
        return isGenderPronounInstalled
    }

    private def fetchGenderByCode(code) {
        if (code != null) {
            def genderResult = [:]
            Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
            def genderSql = 'select GTVGNDR_GNDR_CODE, GTVGNDR_GNDR_DESC, GTVGNDR_ACTIVE_IND, GTVGNDR_WEB_IND ' +
                    'from gtvgndr where gtvgndr_gndr_code = ? and gtvgndr_active_ind = \'Y\' and gtvgndr_web_ind = \'Y\''

            try {
                genderResult = sql.firstRow(genderSql, [code])
            } finally {
                sql?.close()
            }

            if (genderResult == null) {
                throw new ApplicationException(this, "@@r1:invalidGenderCode@@")
            }

            return [code: genderResult.gtvgndr_gndr_code, description: genderResult.gtvgndr_gndr_desc]
        }
        else {
            throw new ApplicationException(this, "@@r1:invalidGenderCode@@")
        }
    }

    private def fetchPronounByCode(code) {
        if(code != null) {
            def pronounResult
            Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
            def genderSql = 'select GTVPPRN_PPRN_CODE, GTVPPRN_PPRN_DESC, GTVPPRN_ACTIVE_IND, GTVPPRN_WEB_IND ' +
                    'from gtvpprn where gtvpprn_pprn_code = ? and gtvpprn_active_ind = \'Y\' and gtvpprn_web_ind = \'Y\''

            try {
                pronounResult = sql.firstRow(genderSql, [code])
            } finally {
                sql?.close()
            }

            if (pronounResult == null) {
                throw new ApplicationException(this, "@@r1:invalidPronounCode@@")
            }

            return [code: pronounResult.gtvpprn_pprn_code, description: pronounResult.gtvpprn_pprn_desc]
        }
        else {
            throw new ApplicationException(this, "@@r1:invalidPronounCode@@")
        }
    }


    private def fetchPersonsGenderPronoun(personBaseId) {
        def result
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def fetchGenderPronounSql = 'select spbpers_gndr_code, spbpers_pprn_code, gtvgndr_gndr_desc, gtvpprn_pprn_desc ' +
                'from spbpers ' +
                'left join gtvgndr on spbpers_gndr_code = gtvgndr_gndr_code ' +
                'left join gtvpprn on spbpers_pprn_code = gtvpprn_pprn_code ' +
                'where spbpers_surrogate_id = ?'

        try {
            result = sql.firstRow(fetchGenderPronounSql, [personBaseId])
        } finally {
            sql?.close()
        }

        return [gender: [code: result?.spbpers_gndr_code, description: result?.gtvgndr_gndr_desc],
                pronoun: [code: result?.spbpers_pprn_code, description: result?.gtvpprn_pprn_desc]]
    }

    private boolean checkForGndrPprnTablesAndColumns() {
        boolean gndrPprnTablesAndColumnsExist = false;
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def genderColumnSql = 'select count(*) from all_tab_cols ' +
                'where table_name = \'SPBPERS\' and column_name = \'SPBPERS_GNDR_CODE\''

        try {
            gndrPprnTablesAndColumnsExist = sql.firstRow(genderColumnSql)[0] == 1
        } finally {
            sql?.close()
        }

        return gndrPprnTablesAndColumnsExist
    }

}