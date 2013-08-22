-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
--  SVQ_ALTISRC.sql
--
-- AUDIT TRAIL: 9.0
-- Read only view for Advanced Search Filter UI Component -- SSN Search    mhitrik 10-MAY-2012
-- AUDIT TRAIL END
--

SET SCAN OFF;

CREATE OR REPLACE FORCE VIEW SVQ_ALTISRC
   (SURROGATE_ID,
    PIDM,
    ID,
    LAST_NAME,
    FIRST_NAME,
    MI,
    CHANGE_INDICATOR,
    VERSION   ,
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
from SPRIDEN where SPRIDEN_PIDM in (
            select a.SPBPERS_PIDM
              from SPBPERS a
              where contains(a.SPBPERS_SSN,soknsut.f_get_search_filter) >0
              );

COMMENT ON TABLE SVQ_ALTISRC IS 'Read only view for Advanced Search Filter UI Component';
COMMENT ON COLUMN SVQ_ALTISRC.SURROGATE_ID IS 'SURROGATE ID: Immutable unique key';
COMMENT ON COLUMN SVQ_ALTISRC.VERSION IS 'VERSION: Optimistic lock token.';
COMMENT ON COLUMN SVQ_ALTISRC.PIDM IS 'PIDM:Internal identification number of the person in SPRIDEN.';
COMMENT ON COLUMN SVQ_ALTISRC.ID IS 'ID: This field defines the identification number used to access person on-line.';
COMMENT ON COLUMN SVQ_ALTISRC.LAST_NAME IS 'LAST NAME: This field identifies the last name of person.';
COMMENT ON COLUMN SVQ_ALTISRC.FIRST_NAME IS 'FIRST NAME: This field identifies the first name of person.';
COMMENT ON COLUMN SVQ_ALTISRC.MI IS 'MI: This field identifies the middle name of person.';
COMMENT ON COLUMN SVQ_ALTISRC.CHANGE_INDICATOR IS 'CHANGE_INDICATOR: This field maintains the spriden change indicator.';
CREATE OR REPLACE PUBLIC SYNONYM SVQ_ALTISRC FOR SVQ_ALTISRC;
SHOW ERRORS VIEW SVQ_ALTISRC;
SET SCAN ON;

