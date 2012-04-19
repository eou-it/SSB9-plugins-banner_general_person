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
--  gvq_goremal.sql
--
-- AUDIT TRAIL: 9.0
-- Read only view over GOREMAL table           mhitrik 15-FEB-2012
-- AUDIT TRAIL END
--

SET SCAN OFF;
whenever sqlerror continue;
DROP PUBLIC SYNONYM GVQ_GOREMAL;
Whenever sqlerror exit rollback;
CREATE OR REPLACE FORCE VIEW GVQ_GOREMAL
   (SURROGATE_ID,
    VERSION,
    ACTIVITY_DATE,
    USER_ID,
    DATA_ORIGIN,
    PIDM,
    ID,
    LAST_NAME,
    FIRST_NAME,
    MI,
    BIRTH_DATE,
    EMAIL_ADDRESS,
    EMAIL_CODE,
    SURNAME_PREFIX,
    PREFERRED_FIRST_NAME,
    NAME_PREFIX,
    NAME_SUFFIX
)
AS
SELECT   ROWNUM,
         SPRIDEN_VERSION,
         SPRIDEN_ACTIVITY_DATE,
         SPRIDEN_USER_ID,
         SPRIDEN_DATA_ORIGIN,
         SPRIDEN_PIDM,
         SPRIDEN_ID,
         SPRIDEN_LAST_NAME,
         SPRIDEN_FIRST_NAME,
         SPRIDEN_MI,
	     SPBPERS_BIRTH_DATE,
	     GOREMAL_EMAIL_ADDRESS,
         GOREMAL_EMAL_CODE,
         SPRIDEN_SURNAME_PREFIX,
         SPBPERS_PREF_FIRST_NAME,
         SPBPERS_NAME_PREFIX,
         SPBPERS_NAME_SUFFIX
 FROM SPRIDEN, GOREMAL, SPBPERS
       where GOREMAL_PIDM = SPRIDEN_PIDM
       AND GOREMAL_PIDM = SPBPERS_PIDM(+)
       AND SPRIDEN_CHANGE_IND IS NULL
     ORDER BY SPRIDEN_SEARCH_LAST_NAME, SPRIDEN_SEARCH_FIRST_NAME, SPRIDEN_SEARCH_MI,
              SPRIDEN_ID, GOREMAL_EMAL_CODE, GOREMAL_EMAIL_ADDRESS;

COMMENT ON TABLE GVQ_GOREMAL IS 'View On GOREMAL';
COMMENT ON COLUMN GVQ_GOREMAL.SURROGATE_ID IS 'SURROGATE ID: Immutable unique key';
COMMENT ON COLUMN GVQ_GOREMAL.VERSION IS 'VERSION: Optimistic lock token.';
COMMENT ON COLUMN GVQ_GOREMAL.ACTIVITY_DATE IS 'ACTIVITY_DATE:Date information was last created or modified.';
COMMENT ON COLUMN GVQ_GOREMAL.USER_ID IS 'USER ID: The user ID of the person who inserted or last updated this record.';
COMMENT ON COLUMN GVQ_GOREMAL.DATA_ORIGIN IS 'DATA ORIGIN: Source system that created or updated the data.';
COMMENT ON COLUMN GVQ_GOREMAL.PIDM IS 'PIDM:Internal identification number of the person in SPRIDEN.';
COMMENT ON COLUMN GVQ_GOREMAL.ID IS 'ID: This field defines the identification number used to access person on-line.';
COMMENT ON COLUMN GVQ_GOREMAL.LAST_NAME IS 'LAST NAME: This field identifies the last name of person.';
COMMENT ON COLUMN GVQ_GOREMAL.FIRST_NAME IS 'FIRST NAME: This field identifies the first name of person.';
COMMENT ON COLUMN GVQ_GOREMAL.MI IS 'MI: This field identifies the middle name of person.';
COMMENT ON COLUMN GVQ_GOREMAL.EMAIL_ADDRESS IS 'EMAIL_ADDRESS:This field maintains the email address.';
COMMENT ON COLUMN GVQ_GOREMAL.EMAIL_CODE IS 'EMAIL_CODE:This field maintains the email address.';
CREATE PUBLIC SYNONYM GVQ_GOREMAL FOR GVQ_GOREMAL;
SHOW ERRORS VIEW GVQ_GOREMAL;
SET SCAN ON;

