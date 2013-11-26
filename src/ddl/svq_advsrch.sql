-- *****************************************************************************************
-- * Copyright 2009-2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************

--  SVQ_ADVSRCH.sql
--
-- AUDIT TRAIL: 9.0
-- Read only view for Advanced Search Filter UI Component -Name Search    mhitrik 10-MAY-2012
-- AUDIT TRAIL END
--

SET SCAN OFF;


CREATE OR REPLACE FORCE VIEW SVQ_ADVSRCH
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
select /*+ RULE */ SPRIDEN_SURROGATE_ID,
       SPRIDEN_PIDM,
       SPRIDEN_ID,
       SPRIDEN_LAST_NAME,
       SPRIDEN_FIRST_NAME,
       SPRIDEN_MI,
       SPRIDEN_CHANGE_IND,
       SPRIDEN_VERSION ,
       rownum
from SPRIDEN  where soknsut.f_match_name(UPPER(SPRIDEN_SEARCH_LAST_NAME||'::'||SPRIDEN_SEARCH_FIRST_NAME||'::'||SPRIDEN_SEARCH_MI)) = 1;

COMMENT ON TABLE SVQ_ADVSRCH IS 'Read only view for Advanced Search Filter UI Component';
COMMENT ON COLUMN SVQ_ADVSRCH.SURROGATE_ID IS 'SURROGATE ID: Immutable unique key';
COMMENT ON COLUMN SVQ_ADVSRCH.VERSION IS 'VERSION: Optimistic lock token.';
COMMENT ON COLUMN SVQ_ADVSRCH.PIDM IS 'PIDM:Internal identification number of the person in SPRIDEN.';
COMMENT ON COLUMN SVQ_ADVSRCH.ID IS 'ID: This field defines the identification number used to access person on-line.';
COMMENT ON COLUMN SVQ_ADVSRCH.LAST_NAME IS 'LAST NAME: This field identifies the last name of person.';
COMMENT ON COLUMN SVQ_ADVSRCH.FIRST_NAME IS 'FIRST NAME: This field identifies the first name of person.';
COMMENT ON COLUMN SVQ_ADVSRCH.MI IS 'MI: This field identifies the middle name of person.';
COMMENT ON COLUMN SVQ_ADVSRCH.CHANGE_INDICATOR IS 'CHANGE_INDICATOR: This field maintains the spriden change indicator.';
CREATE OR REPLACE PUBLIC SYNONYM SVQ_ADVSRCH FOR SVQ_ADVSRCH;
SHOW ERRORS VIEW SVQ_ADVSRCH;
SET SCAN ON;

