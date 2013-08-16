-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************


--  gvq_goradid.sql
--
-- AUDIT TRAIL: 9.0
-- Read only view over GORADID table           mhitrik 15-FEB-2012
-- AUDIT TRAIL END
--
SET SCAN OFF;

CREATE OR REPLACE FORCE VIEW GVQ_GORADID
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
    ADID_CODE,
    ADDITIONAL_ID,
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
         GORADID_ADID_CODE,
         GORADID_ADDITIONAL_ID,
         SPRIDEN_SURNAME_PREFIX,
         SPBPERS_PREF_FIRST_NAME,
         SPBPERS_NAME_PREFIX,
         SPBPERS_NAME_SUFFIX
 FROM SPRIDEN, GORADID, SPBPERS, GTVADID
       WHERE GORADID_ADID_CODE = GTVADID_CODE
       AND NVL(GTVADID_GUISRCH_BYPASS,'N') = 'N'
       AND GORADID_PIDM = SPRIDEN_PIDM
       AND GORADID_PIDM = SPBPERS_PIDM(+)
       AND SPRIDEN_CHANGE_IND IS NULL
     ORDER BY SPRIDEN_SEARCH_LAST_NAME, SPRIDEN_SEARCH_FIRST_NAME, SPRIDEN_SEARCH_MI,
              SPRIDEN_ID, GORADID_ADID_CODE, GORADID_ADDITIONAL_ID;

COMMENT ON TABLE GVQ_GORADID IS 'View On GORADID';
COMMENT ON COLUMN GVQ_GORADID.SURROGATE_ID IS 'SURROGATE ID: Immutable unique key';
COMMENT ON COLUMN GVQ_GORADID.VERSION IS 'VERSION: Optimistic lock token.';
COMMENT ON COLUMN GVQ_GORADID.ACTIVITY_DATE IS 'ACTIVITY_DATE:Date information was last created or modified.';
COMMENT ON COLUMN GVQ_GORADID.USER_ID IS 'USER ID: The user ID of the person who inserted or last updated this record.';
COMMENT ON COLUMN GVQ_GORADID.DATA_ORIGIN IS 'DATA ORIGIN: Source system that created or updated the data.';
COMMENT ON COLUMN GVQ_GORADID.PIDM IS 'PIDM:Internal identification number of the person in SPRIDEN.';
COMMENT ON COLUMN GVQ_GORADID.ID IS 'ID: This field defines the identification number used to access person on-line.';
COMMENT ON COLUMN GVQ_GORADID.LAST_NAME IS 'LAST NAME: This field identifies the last name of person.';
COMMENT ON COLUMN GVQ_GORADID.FIRST_NAME IS 'FIRST NAME: This field identifies the first name of person.';
COMMENT ON COLUMN GVQ_GORADID.MI IS 'MI: This field identifies the middle name of person.';
COMMENT ON COLUMN GVQ_GORADID.ADID_CODE IS 'ADID_CODE:Additional id code.';
COMMENT ON COLUMN GVQ_GORADID.ADDITIONAL_ID IS 'ADDITIONAL_ID:Additional id.';
CREATE OR REPLACE PUBLIC SYNONYM GVQ_GORADID FOR GVQ_GORADID;
SHOW ERRORS VIEW GVQ_GORADID;
SET SCAN ON;

