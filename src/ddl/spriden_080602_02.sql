
-- *****************************************************************************************
-- * Copyright 2010-2013 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************
REM
REM spriden_080602_02.sql
REM
REM This script creates index for idname searching
REM
REM Project: Student id/name search for Banner General
REM AUDIT TRAIL: 8.6.2
REM 8/11/2013
REM 1. Added indexes for name and id searching.
REM AUDIT TRAIL END
 --
-- Create the domain index on SPRIDEN_SEARCH_LAST_NAME.
--
whenever sqlerror continue;
CREATE INDEX SPRIDEN_INDEX_LAST_NAME ON SPRIDEN (SPRIDEN_SEARCH_LAST_NAME) INDEXTYPE IS ctxsys.context parameters('STOPLIST STOPLIST_IDNAMESEARCH');

whenever sqlerror exit rollback;