
-- *****************************************************************************************
-- * Copyright 2010-2013 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************


REM
REM create_spriden_fti.sql
REM
REM This script creates index on spriden for name searching
REM
REM Project: Student id/name search for Banner Student
REM Audit Trail: 9.1 2/13/2012
REM 1. Added indexes for name and id searching.
REM
--
-- Create the stoplist for name search.
--
whenever sqlerror continue;
begin
     ctx_ddl.create_stoplist('STOPLIST_NAMESEARCH');
exception
		when others then null;
end;
/
whenever sqlerror exit rollback;

--
-- Create the domain index on SPRIDEN_SEARCH_LAST_NAME.
--
whenever sqlerror continue;
CREATE INDEX SPRIDEN_INDEX_LAST_NAME ON SPRIDEN (SPRIDEN_SEARCH_LAST_NAME) INDEXTYPE IS ctxsys.context parameters('STOPLIST STOPLIST_NAMESEARCH');

whenever sqlerror exit rollback;
--
-- Create the domain index on SPRIDEN_SEARCH_FIRST_NAME.
--
whenever sqlerror continue;
CREATE INDEX SPRIDEN_INDEX_FIRST_NAME ON SPRIDEN (SPRIDEN_SEARCH_FIRST_NAME) INDEXTYPE IS ctxsys.context parameters('STOPLIST STOPLIST_NAMESEARCH');

whenever sqlerror exit rollback;
--
-- Create the domain index on SPRIDEN_SEARCH_MI.
--
whenever sqlerror continue;
CREATE INDEX SPRIDEN_INDEX_MI ON SPRIDEN (SPRIDEN_SEARCH_MI) INDEXTYPE IS ctxsys.context parameters('STOPLIST STOPLIST_NAMESEARCH');

whenever sqlerror exit rollback;
--
-- Create the domain index on SPRIDEN_ID.
--
whenever sqlerror continue;
CREATE INDEX SPRIDEN_SEARCH_ID ON SPRIDEN (SPRIDEN_ID) INDEXTYPE IS ctxsys.context;
whenever sqlerror exit rollback;
--
-- Create the function based index on SPRIDEN_CHANGE_IND.
--
whenever sqlerror continue;
CREATE INDEX SPRIDEN_CHANGE_IND ON SPRIDEN (nvl(SPRIDEN_CHANGE_IND,'null'));

whenever sqlerror exit rollback;