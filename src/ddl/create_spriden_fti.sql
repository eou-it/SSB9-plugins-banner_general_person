
-- *****************************************************************************************
-- * Copyright 2010-2013 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************


REM
REM create_spriden_fti.sql
REM
REM This script creates index for idname searching
REM
REM Project: Student id/name search for Banner Student
REM Audit Trail: 9.9 8/11/2013
REM 1. Added indexes for name and id searching.
REM
--
-- Create the stoplist for idname search.
--
whenever sqlerror continue;
begin
     ctx_ddl.create_stoplist('STOPLIST_IDNAMESEARCH');
exception
		when others then null;
end;
/
whenever sqlerror exit rollback;

--
-- Create the lexer for idname search.
--
whenever sqlerror continue;
begin
     ctx_ddl.create_preference('LEXER_IDNAMESEARCH', 'BASIC_LEXER');
     ctx_ddl.set_attribute('LEXER_IDNAMESEARCH','printjoins','-_&=''?:;,."|~$!*#@(){}[]>\/');
exception
		when others then null;
end;
/
whenever sqlerror exit rollback;

--
-- Create the domain index on SPRIDEN_SEARCH_LAST_NAME.
--
whenever sqlerror continue;
CREATE INDEX SPRIDEN_INDEX_LAST_NAME ON SPRIDEN (SPRIDEN_SEARCH_LAST_NAME) INDEXTYPE IS ctxsys.context parameters('STOPLIST STOPLIST_IDNAMESEARCH');

whenever sqlerror exit rollback;
--
-- Create the domain index on SPRIDEN_SEARCH_FIRST_NAME.
--
whenever sqlerror continue;
CREATE INDEX SPRIDEN_INDEX_FIRST_NAME ON SPRIDEN (SPRIDEN_SEARCH_FIRST_NAME) INDEXTYPE IS ctxsys.context parameters('STOPLIST STOPLIST_IDNAMESEARCH');

whenever sqlerror exit rollback;
--
-- Create the domain index on SPRIDEN_SEARCH_MI.
--
whenever sqlerror continue;
CREATE INDEX SPRIDEN_INDEX_MI ON SPRIDEN (SPRIDEN_SEARCH_MI) INDEXTYPE IS ctxsys.context parameters('STOPLIST STOPLIST_IDNAMESEARCH');

whenever sqlerror exit rollback;
--
-- Create the domain index on SPRIDEN_ID.
--
whenever sqlerror continue;
CREATE INDEX SPRIDEN_SEARCH_ID ON SPRIDEN (SPRIDEN_ID) INDEXTYPE IS ctxsys.context parameters('STOPLIST STOPLIST_IDNAMESEARCH LEXER LEXER_IDNAMESEARCH');
whenever sqlerror exit rollback;
--
-- Create the function based index on SPRIDEN_CHANGE_IND.
--
whenever sqlerror continue;
CREATE INDEX SPRIDEN_CHANGE_IND ON SPRIDEN (nvl(SPRIDEN_CHANGE_IND,'null'));

whenever sqlerror exit rollback;
--
-- Create the domain index on SPBPERS_SSN.
--
whenever sqlerror continue;
CREATE INDEX SPBPERS_SSN_TEXT_INDEX ON SPBPERS(SPBPERS_SSN) INDEXTYPE IS ctxsys.context parameters('STOPLIST STOPLIST_IDNAMESEARCH LEXER LEXER_IDNAMESEARCH');

whenever sqlerror exit rollback;
