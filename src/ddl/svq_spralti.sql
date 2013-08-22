-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
--  svq_spralti.sql
--
-- AUDIT TRAIL: 9.0
-- Read only view over SPRIDEN table           mhitrik 28-FEB-2012
-- AUDIT TRAIL END
--

SET SCAN OFF;

CREATE OR REPLACE FORCE VIEW SVQ_SPRALTI
   (SURROGATE_ID,
    VERSION,
    ACTIVITY_DATE,
    USER_ID,
    DATA_ORIGIN,
    SSN,
    PIDM,
    ID,
    LAST_NAME,
    FIRST_NAME,
    MI,
    BIRTH_DATE,
    CHANGE_INDICATOR,
    ENTITY_INDICATOR,
    SEARCH_LAST_NAME,
    SEARCH_FIRST_NAME,
    SEARCH_MI,
    SURNAME_PREFIX,
    PREFERRED_FIRST_NAME,
    NAME_PREFIX,
    NAME_SUFFIX
)
AS
SELECT  SPRIDEN_SURROGATE_ID,
         SPRIDEN_VERSION,
         SPRIDEN_ACTIVITY_DATE,
         SPRIDEN_USER_ID,
         SPRIDEN_DATA_ORIGIN,
         SPBPERS_SSN,
         SPRIDEN_PIDM,
         SPRIDEN_ID,
         SPRIDEN_LAST_NAME,
         SPRIDEN_FIRST_NAME,
         SPRIDEN_MI,
	     SPBPERS_BIRTH_DATE,
         SPRIDEN_CHANGE_IND,
         SPRIDEN_ENTITY_IND,
         SPRIDEN_SEARCH_LAST_NAME,
         SPRIDEN_SEARCH_FIRST_NAME,
         SPRIDEN_SEARCH_MI,
         SPRIDEN_SURNAME_PREFIX,
         SPBPERS_PREF_FIRST_NAME,
         SPBPERS_NAME_PREFIX,
         SPBPERS_NAME_SUFFIX
   FROM SPBPERS, SPRIDEN
  WHERE SPBPERS_PIDM(+) = SPRIDEN_PIDM;

COMMENT ON TABLE SVQ_SPRALTI IS 'View On SPRIDEN Alternate ID';
COMMENT ON COLUMN SVQ_SPRALTI.SURROGATE_ID IS 'SURROGATE ID: Immutable unique key';
COMMENT ON COLUMN SVQ_SPRALTI.VERSION IS 'VERSION: Optimistic lock token.';
COMMENT ON COLUMN SVQ_SPRIDEN.ACTIVITY_DATE IS 'ACTIVITY_DATE:Date information was last created or modified.';
COMMENT ON COLUMN SVQ_SPRALTI.USER_ID IS 'USER ID: The user ID of the person who inserted or last updated this record.';
COMMENT ON COLUMN SVQ_SPRALTI.DATA_ORIGIN IS 'DATA ORIGIN: Source system that created or updated the data.';
COMMENT ON COLUMN SVQ_SPRALTI.SSN IS 'SSN:Internal identification number of the person in SPBPERS.';
COMMENT ON COLUMN SVQ_SPRALTI.PIDM IS 'PIDM:Internal identification number of the person in SPRIDEN.';
COMMENT ON COLUMN SVQ_SPRALTI.ID IS 'ID: This field defines the identification number used to access person on-line.';
COMMENT ON COLUMN SVQ_SPRALTI.LAST_NAME IS 'LAST NAME: This field identifies the last name of person.';
COMMENT ON COLUMN SVQ_SPRALTI.FIRST_NAME IS 'FIRST NAME: This field identifies the first name of person.';
COMMENT ON COLUMN SVQ_SPRALTI.MI IS 'MI: This field identifies the middle name of person.';
COMMENT ON COLUMN SVQ_SPRALTI.BIRTH_DATE IS 'EMAIL_ADDRESS:This field maintains the birth date of the Person.';
COMMENT ON COLUMN SVQ_SPRALTI.CHANGE_INDICATOR IS 'CHANGE_INDICATOR: This field maintains the spriden change indicator.';
COMMENT ON COLUMN SVQ_SPRALTI.ENTITY_INDICATOR IS 'ENTITY_INDICATOR: This field maintains the spriden entity indicator.';
COMMENT ON COLUMN SVQ_SPRALTI.SEARCH_LAST_NAME IS 'SEARCH_LAST_NAME: This field maintains the spriden search last name.';
COMMENT ON COLUMN SVQ_SPRALTI.SEARCH_FIRST_NAME IS 'SEARCH_FIRST_NAME: This field maintains the spriden search first name.';
COMMENT ON COLUMN SVQ_SPRALTI.SEARCH_MI IS 'SEARCH_MI: This field maintains the spriden search middle name.';
COMMENT ON COLUMN SVQ_SPRALTI.SURNAME_PREFIX IS 'SURNAME_PREFIX: This field maintains the spriden surname prefix.';
COMMENT ON COLUMN SVQ_SPRALTI.PREFERRED_FIRST_NAME IS 'PREFERRED_FIRST_NAME: This field maintains the spbpers preferred first name.';
COMMENT ON COLUMN SVQ_SPRALTI.NAME_SUFFIX IS 'NAME_SUFFIX: This field maintains the spbpers name suffix.';
COMMENT ON COLUMN SVQ_SPRALTI.NAME_PREFIX IS 'NAME_PREFIX: This field maintains the spbpers name prefix.';
CREATE OR REPLACE PUBLIC SYNONYM SVQ_SPRALTI FOR SVQ_SPRALTI;
SHOW ERRORS VIEW SVQ_SPRALTI;
SET SCAN ON;

