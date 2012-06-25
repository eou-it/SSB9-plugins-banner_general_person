-- *****************************************************************************************
-- * Copyright 2009-2012 SunGard Higher Education. All Rights Reserved.                    *
-- * This copyrighted software contains confidential and proprietary information of        *
-- * SunGard Higher Education and its subsidiaries. Any use of this software is limited    *
-- * solely to SunGard Higher Education licensees, and is further subject to the terms     *
-- * and conditions of one or more written license agreements between SunGard Higher       *
-- * Education and the licensee in question. SunGard is either a registered trademark or   *
-- * trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.*
-- * Banner and Luminis are either registered trademarks or trademarks of SunGard Higher   *
-- * Education in the U.S.A. and/or other regions and/or countries.                        *
-- *****************************************************************************************
--  svq_SADVSRC.sql
--
-- AUDIT TRAIL: 9.0
-- Read only view over SPRIDEN table           mhitrik 18-MAR-2012
-- AUDIT TRAIL END
--

SET SCAN OFF;
whenever sqlerror continue;
DROP PUBLIC SYNONYM SVQ_SADVSRC;
Whenever sqlerror exit rollback;
CREATE OR REPLACE FORCE VIEW SVQ_SADVSRC
   (SURROGATE_ID,
    VERSION,
    ACTIVITY_DATE,
    USER_ID,
    DATA_ORIGIN,
    SSN,
    PIDM,
    ID,
    CURRENT_ID,
    LAST_NAME,
    FIRST_NAME,
    MI,
    BIRTH_DATE,
    CHANGE_INDICATOR,
    ENTITY_INDICATOR,
    SEARCH_LAST_NAME,
    SEARCH_FIRST_NAME,
    SEARCH_MI,
    NAME_TYPE,
    CITY,
    STATE,
    ZIP,
    SEX,
    SURNAME_PREFIX,
    PREFERRED_FIRST_NAME,
    NAME_PREFIX,
    NAME_SUFFIX
)
AS
SELECT   ROWNUM,
         o.SPRIDEN_VERSION,
         o.SPRIDEN_ACTIVITY_DATE,
         o.SPRIDEN_USER_ID,
         o.SPRIDEN_DATA_ORIGIN,
         SPBPERS_SSN,
         o.SPRIDEN_PIDM,
         o.SPRIDEN_ID,
         n.SPRIDEN_ID,
         o.SPRIDEN_LAST_NAME,
         o.SPRIDEN_FIRST_NAME,
         o.SPRIDEN_MI,
         SPBPERS_BIRTH_DATE,
         o.SPRIDEN_CHANGE_IND,
         o.SPRIDEN_ENTITY_IND,
         o.SPRIDEN_SEARCH_LAST_NAME,
         o.SPRIDEN_SEARCH_FIRST_NAME,
         o.SPRIDEN_SEARCH_MI,
         o.SPRIDEN_NTYP_CODE,
         SPRADDR_CITY,
         SPRADDR_STAT_CODE,
         SPRADDR_ZIP,
         SPBPERS_SEX,
         o.SPRIDEN_SURNAME_PREFIX,
         SPBPERS_PREF_FIRST_NAME,
         SPBPERS_NAME_PREFIX,
         SPBPERS_NAME_SUFFIX
   FROM SPBPERS, SPRIDEN n, SPRIDEN o, spraddr
  WHERE SPBPERS_PIDM(+) = o.SPRIDEN_PIDM
    AND SPRADDR_PIDM(+) = o.SPRIDEN_PIDM
    AND n.spriden_pidm = o.spriden_pidm
    AND n.spriden_change_ind IS NULL
    AND o.spriden_entity_ind = n.spriden_entity_ind
    ORDER BY n.spriden_search_last_name, n.spriden_search_first_name,
             n.spriden_search_mi, n.spriden_id, o.spriden_change_ind DESC;
COMMENT ON TABLE SVQ_SADVSRC IS 'View On Person Advanced Search';
COMMENT ON COLUMN SVQ_SADVSRC.SURROGATE_ID IS 'SURROGATE ID: Immutable unique key';
COMMENT ON COLUMN SVQ_SADVSRC.VERSION IS 'VERSION: Optimistic lock token.';
COMMENT ON COLUMN SVQ_SADVSRC.ACTIVITY_DATE IS 'ACTIVITY_DATE:Date information was last created or modified.';
COMMENT ON COLUMN SVQ_SADVSRC.USER_ID IS 'USER ID: The user ID of the person who inserted or last updated this record.';
COMMENT ON COLUMN SVQ_SADVSRC.DATA_ORIGIN IS 'DATA ORIGIN: Source system that created or updated the data.';
COMMENT ON COLUMN SVQ_SADVSRC.SSN IS 'SSN:Internal identification number of the person in SPBPERS.';
COMMENT ON COLUMN SVQ_SADVSRC.PIDM IS 'PIDM:Internal identification number of the person in SPRIDEN.';
COMMENT ON COLUMN SVQ_SADVSRC.ID IS 'ID: This field defines the identification number used to access person on-line.';
COMMENT ON COLUMN SVQ_SADVSRC.LAST_NAME IS 'LAST NAME: This field identifies the last name of person.';
COMMENT ON COLUMN SVQ_SADVSRC.FIRST_NAME IS 'FIRST NAME: This field identifies the first name of person.';
COMMENT ON COLUMN SVQ_SADVSRC.MI IS 'MI: This field identifies the middle name of person.';
COMMENT ON COLUMN SVQ_SADVSRC.BIRTH_DATE IS 'BIRTH_DATE:This field maintains the birth date of the Person.';
COMMENT ON COLUMN SVQ_SADVSRC.CHANGE_INDICATOR IS 'CHANGE_INDICATOR: This field maintains the spriden change indicator.';
COMMENT ON COLUMN SVQ_SADVSRC.ENTITY_INDICATOR IS 'ENTITY_INDICATOR: This field maintains the spriden entity indicator.';
COMMENT ON COLUMN SVQ_SADVSRC.SEARCH_LAST_NAME IS 'SEARCH_LAST_NAME: This field maintains the spriden search last name.';
COMMENT ON COLUMN SVQ_SADVSRC.SEARCH_FIRST_NAME IS 'SEARCH_FIRST_NAME: This field maintains the spriden search first name.';
COMMENT ON COLUMN SVQ_SADVSRC.SEARCH_MI IS 'SEARCH_MI: This field maintains the spriden search middle name.';
COMMENT ON COLUMN SVQ_SADVSRC.NAME_TYPE IS 'NAME_TYPE: This field maintains the name type.';
COMMENT ON COLUMN SVQ_SADVSRC.CITY IS 'CITY: This field maintains the city.';
COMMENT ON COLUMN SVQ_SADVSRC.STATE IS 'STATE: This field maintains the state code.';
COMMENT ON COLUMN SVQ_SADVSRC.ZIP IS 'ZIP: This field maintains the zip code.';
COMMENT ON COLUMN SVQ_SADVSRC.SEX IS 'SEX: This field maintains the sex code.';
CREATE PUBLIC SYNONYM SVQ_SADVSRC FOR SVQ_SADVSRC;
SHOW ERRORS VIEW SVQ_SADVSRC;
SET SCAN ON;

