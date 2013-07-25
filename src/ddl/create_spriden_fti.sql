
-- *****************************************************************************************
-- * Copyright 2010-2013 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************


REM
REM create_spriden_fti.sql
REM
REM This script creates index on spriden for name searching
REM
REM Project: Student Registration
REM Audit Trail: 9.1 2/13/2012
REM 1. Added triggers for the long title and short description tables for keyword searching.
REM

--
-- Drop the stoplist for name search.
--
whenever sqlerror continue;
begin
  ctx_ddl.drop_stoplist('STOPLIST_NAMESEARCH'); 
exception
	when others then null;
end;
/
whenever sqlerror exit rollback;
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
-- Drop the fulltext index on spriden search last name if it already exists.
--
whenever sqlerror continue;
declare
  index_does_not_exist EXCEPTION;
  pragma exception_init(index_does_not_exist,-01418);
begin
   execute immediate 'drop index SPRIDEN_INDEX_LAST_NAME';
exception
	when index_does_not_exist then null;
end;
/
whenever sqlerror exit rollback;
--
-- Drop the fulltext index on spriden search first name if it already exists.
--
whenever sqlerror continue;
declare
  index_does_not_exist EXCEPTION;
  pragma exception_init(index_does_not_exist,-01418);
begin
   execute immediate 'drop index SPRIDEN_INDEX_FIRST_NAME';
exception
	when index_does_not_exist then null;
end;
/
whenever sqlerror exit rollback;
--
-- Drop the domain index on spriden search middle name if it already exists.
--
whenever sqlerror continue;
declare
  index_does_not_exist EXCEPTION;
  pragma exception_init(index_does_not_exist,-01418);
begin
   execute immediate 'drop index SPRIDEN_INDEX_MI';
exception
	when index_does_not_exist then null;
end;
/
whenever sqlerror exit rollback;
--
-- Drop the domain index on spriden id if it already exists.
--
whenever sqlerror continue;
declare
  index_does_not_exist EXCEPTION;
  pragma exception_init(index_does_not_exist,-01418);
begin
   execute immediate 'drop index SPRIDEN_SEARCH_ID';
exception
	when index_does_not_exist then null;
end;
/
whenever sqlerror exit rollback;
--
-- Drop the domain index on spriden change indicator name if it already exists.
--
whenever sqlerror continue;
declare
  index_does_not_exist EXCEPTION;
  pragma exception_init(index_does_not_exist,-01418);
begin
   execute immediate 'drop index SPRIDEN_CHANGE_IND';
exception
	when index_does_not_exist then null;
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