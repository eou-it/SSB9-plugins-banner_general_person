-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************


--  svq_sprtele.sql
--
-- AUDIT TRAIL: 9.0
-- Read only view over SPRTELE table           mhitrik 15-FEB-2012
-- AUDIT TRAIL END
--

SET SCAN OFF;

CREATE OR REPLACE FORCE VIEW SVQ_SPRTELE
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
    TELE_CODE,
    PHONE_SEARCH,
    PHONE,
    SURNAME_PREFIX,
    PREFERRED_FIRST_NAME,
    NAME_PREFIX,
    NAME_SUFFIX ,
    row_number
)
AS
SELECT   SPRIDEN_SURROGATE_ID,
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
         SPRTELE_TELE_CODE,
         TRIM(SPRTELE_CTRY_CODE_PHONE || SPRTELE_PHONE_AREA || SPRTELE_PHONE_NUMBER),
         TRIM(SPRTELE_CTRY_CODE_PHONE || ' ' || SPRTELE_PHONE_AREA  || ' ' || SPRTELE_PHONE_NUMBER),
         SPRIDEN_SURNAME_PREFIX,
         SPBPERS_PREF_FIRST_NAME,
         SPBPERS_NAME_PREFIX,
         SPBPERS_NAME_SUFFIX  ,
         rownum
 FROM SPRIDEN,SPBPERS,SPRTELE
       where SPRTELE_PIDM = SPRIDEN_PIDM
       AND SPRTELE_PIDM = SPBPERS_PIDM(+)
       AND SPRIDEN_CHANGE_IND IS NULL
     ORDER BY SPRIDEN_SEARCH_LAST_NAME, SPRIDEN_SEARCH_FIRST_NAME, SPRIDEN_SEARCH_MI,
              SPRIDEN_ID, SPRTELE_TELE_CODE, SPRTELE_CTRY_CODE_PHONE, SPRTELE_PHONE_AREA, SPRTELE_PHONE_NUMBER;

COMMENT ON TABLE SVQ_SPRTELE IS 'View On SPRTELE';
COMMENT ON COLUMN SVQ_SPRTELE.SURROGATE_ID IS 'SURROGATE ID: Immutable unique key';
COMMENT ON COLUMN SVQ_SPRTELE.VERSION IS 'VERSION: Optimistic lock token.';
COMMENT ON COLUMN SVQ_SPRTELE.ACTIVITY_DATE IS 'ACTIVITY_DATE:Date information was last created or modified.';
COMMENT ON COLUMN SVQ_SPRTELE.USER_ID IS 'USER ID: The user ID of the person who inserted or last updated this record.';
COMMENT ON COLUMN SVQ_SPRTELE.DATA_ORIGIN IS 'DATA ORIGIN: Source system that created or updated the data.';
COMMENT ON COLUMN SVQ_SPRTELE.PIDM IS 'PIDM:Internal identification number of the person in SPRIDEN.';
COMMENT ON COLUMN SVQ_SPRTELE.ID IS 'ID: This field defines the identification number used to access person on-line.';
COMMENT ON COLUMN SVQ_SPRTELE.LAST_NAME IS 'LAST NAME: This field identifies the last name of person.';
COMMENT ON COLUMN SVQ_SPRTELE.FIRST_NAME IS 'FIRST NAME: This field identifies the first name of person.';
COMMENT ON COLUMN SVQ_SPRTELE.MI IS 'MI: This field identifies the middle name of person.';
COMMENT ON COLUMN SVQ_SPRTELE.BIRTH_DATE IS 'EMAIL_ADDRESS:This field maintains the birth date of the Person.';
COMMENT ON COLUMN SVQ_SPRTELE.TELE_CODE IS 'TELE_CODE:This field maintains the phone code type.';
COMMENT ON COLUMN SVQ_SPRTELE.PHONE_SEARCH IS 'PHONE_SEARCH:This field is used for searching phones of the Person.';
COMMENT ON COLUMN SVQ_SPRTELE.PHONE IS 'PHONE:This field is used for displaying phones of the Person.';
CREATE OR REPLACE PUBLIC SYNONYM SVQ_SPRTELE FOR SVQ_SPRTELE;
SHOW ERRORS VIEW SVQ_SPRTELE;
SET SCAN ON;

