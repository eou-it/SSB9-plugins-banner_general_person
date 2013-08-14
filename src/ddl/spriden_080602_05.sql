
-- *****************************************************************************************
-- * Copyright 2010-2013 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************
REM
REM spriden_080602_05.sql
REM
REM This script creates index for idname searching
REM
REM Project: Student id/name search for Banner General
REM AUDIT TRAIL: 8.6.2
REM 8/11/2013
REM 1. Added indexes for name and id searching.
REM AUDIT TRAIL END
 --
-- Create the domain index on SPRIDEN_ID.
--
whenever sqlerror continue;
CREATE INDEX SPRIDEN_SEARCH_ID ON SPRIDEN (SPRIDEN_ID) INDEXTYPE IS ctxsys.context parameters('STOPLIST STOPLIST_IDNAMESEARCH LEXER LEXER_IDNAMESEARCH');
whenever sqlerror exit rollback;
