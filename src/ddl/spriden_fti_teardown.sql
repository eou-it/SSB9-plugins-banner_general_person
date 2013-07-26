
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
