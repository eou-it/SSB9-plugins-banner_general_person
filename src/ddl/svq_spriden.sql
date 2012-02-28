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
--  svq_spriden.sql
--
-- AUDIT TRAIL: 9.0
-- Read only view over SPRIDEN table           mhitrik 28-FEB-2012
-- AUDIT TRAIL END
--

SET SCAN OFF;
whenever sqlerror continue;
DROP PUBLIC SYNONYM SVQ_SPRIDEN;
Whenever sqlerror exit rollback;
CREATE OR REPLACE FORCE VIEW "BANINST1"."SVQ_SPRIDEN"
   ("SURROGATE_ID",
    "VERSION",
    "ACTIVITY_DATE",
    "USER_ID",
    "DATA_ORIGIN",
    "PIDM",
    "ID",
    "LAST_NAME",
    "FIRST_NAME",
    "MI",
    "BIRTH_DATE",
    "CHANGE_INDICATOR",
    "ENTITY_INDICATOR",
    "SEARCH_LAST_NAME",
    "SEARCH_FIRST_NAME",
    "SEARCH_MI",
    "SOUNDEX_LAST_NAME",
    "SOUNDEX_FIRST_NAME",
    "NAME_TYPE"
)
AS
SELECT SPRIDEN_SURROGATE_ID,
         SPRIDEN_VERSION,
         SPRIDEN_ACTIVITY_DATE,
         SPRIDEN_USER_ID,
         SPRIDEN_DATA_ORIGIN,
         SPRIDEN_PIDM,
         SPRIDEN_ID,
         SPRIDEN_LAST_NAME,
         SPRIDEN_FIRST_NAME,
         SPRIDEN_MI,
	     TO_CHAR(SPBPERS_BIRTH_DATE , G$_DATE.GET_NLS_DATE_FORMAT),
         SPRIDEN_CHANGE_IND,
         SPRIDEN_ENTITY_IND,
         SPRIDEN_SEARCH_LAST_NAME,
         SPRIDEN_SEARCH_FIRST_NAME,
         SPRIDEN_SEARCH_MI,
         SPRIDEN_SOUNDEX_LAST_NAME,
         SPRIDEN_SOUNDEX_FIRST_NAME,
         SPRIDEN_NTYP_CODE
       FROM SPRIDEN,SPBPERS
       WHERE SPRIDEN_PIDM = SPBPERS_PIDM(+);

COMMENT ON TABLE SVQ_SPRIDEN IS 'View On SPRIDEN';
COMMENT ON COLUMN "SVQ_SPRIDEN"."SURROGATE_ID" IS 'SURROGATE ID: Immutable unique key';
COMMENT ON COLUMN "SVQ_SPRIDEN"."VERSION" IS 'VERSION: Optimistic lock token.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."ACTIVITY_DATE" IS 'ACTIVITY_DATE:Date information was last created or modified.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."USER_ID" IS 'USER ID: The user ID of the person who inserted or last updated this record.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."DATA_ORIGIN" IS 'DATA ORIGIN: Source system that created or updated the data.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."PIDM" IS 'PIDM:Internal identification number of the person in SPRIDEN.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."ID" IS 'ID: This field defines the identification number used to access person on-line.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."LAST_NAME" IS 'LAST NAME: This field identifies the last name of person.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."FIRST_NAME" IS 'FIRST NAME: This field identifies the first name of person.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."MI" IS 'MI: This field identifies the middle name of person.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."BIRTH_DATE" IS 'EMAIL_ADDRESS:This field maintains the birth date of the Person.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."CHANGE_INDICATOR" IS 'CHANGE_INDICATOR: This field maintains the spriden change indicator.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."ENTITY_INDICATOR" IS 'ENTITY_INDICATOR: This field maintains the spriden entity indicator.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."SEARCH_LAST_NAME" IS 'SEARCH_LAST_NAME: This field maintains the spriden search last name.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."SEARCH_FIRST_NAME" IS 'SEARCH_FIRST_NAME: This field maintains the spriden search first name.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."SEARCH_MI" IS 'SEARCH_MI: This field maintains the spriden search middle name.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."SOUNDEX_LAST_NAME" IS 'SOUNDEX_LAST_NAME: This field maintains the spriden soundex last name.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."SOUNDEX_FIRST_NAME" IS 'SOUNDEX_FIRST_NAME: This field maintains the spriden soundex first name.';
COMMENT ON COLUMN "SVQ_SPRIDEN"."NAME_TYPE" IS 'NAME_TYPE: This field maintains the spriden name type.';
CREATE PUBLIC SYNONYM "SVQ_SPRIDEN" FOR "BANINST1"."SVQ_SPRIDEN";
SHOW ERRORS VIEW SVQ_SPRIDEN;
SET SCAN ON;

