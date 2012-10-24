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
--  SVQ_IDSRCH.sql
--
-- AUDIT TRAIL: 9.0
-- Read only view for Advanced Search Filter UI Component - ID Search    mhitrik 10-MAY-2012
-- AUDIT TRAIL END
--

SET SCAN OFF;


CREATE OR REPLACE FORCE VIEW SVQ_IDSRCH
   (SURROGATE_ID,
    PIDM,
    ID,
    LAST_NAME,
    FIRST_NAME,
    MI,
    CHANGE_INDICATOR,
    VERSION ,
    row_number
)
AS
select SPRIDEN_SURROGATE_ID,
       SPRIDEN_PIDM,
       SPRIDEN_ID,
       SPRIDEN_LAST_NAME,
       SPRIDEN_FIRST_NAME,
       SPRIDEN_MI,
       SPRIDEN_CHANGE_IND,
       SPRIDEN_VERSION ,
       rownum
from SPRIDEN where SPRIDEN_ID like soknsut.f_get_search_filter;

COMMENT ON TABLE SVQ_IDSRCH IS 'Read only view for Advanced Search Filter UI Component';
COMMENT ON COLUMN SVQ_IDSRCH.SURROGATE_ID IS 'SURROGATE ID: Immutable unique key';
COMMENT ON COLUMN SVQ_IDSRCH.VERSION IS 'VERSION: Optimistic lock token.';
COMMENT ON COLUMN SVQ_IDSRCH.PIDM IS 'PIDM:Internal identification number of the person in SPRIDEN.';
COMMENT ON COLUMN SVQ_IDSRCH.ID IS 'ID: This field defines the identification number used to access person on-line.';
COMMENT ON COLUMN SVQ_IDSRCH.LAST_NAME IS 'LAST NAME: This field identifies the last name of person.';
COMMENT ON COLUMN SVQ_IDSRCH.FIRST_NAME IS 'FIRST NAME: This field identifies the first name of person.';
COMMENT ON COLUMN SVQ_IDSRCH.MI IS 'MI: This field identifies the middle name of person.';
COMMENT ON COLUMN SVQ_IDSRCH.CHANGE_INDICATOR IS 'CHANGE_INDICATOR: This field maintains the spriden change indicator.';
CREATE OR REPLACE PUBLIC SYNONYM SVQ_IDSRCH FOR SVQ_IDSRCH;
SHOW ERRORS VIEW SVQ_IDSRCH;
SET SCAN ON;

