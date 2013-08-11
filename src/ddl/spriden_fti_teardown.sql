
-- *****************************************************************************************
-- * Copyright 2010-2013 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************


REM
REM create_spriden_teardown.sql
REM
REM This script removes index for idname searching
REM


--
-- Drop the stoplist for idname search.
--
whenever sqlerror continue;
begin
  ctx_ddl.drop_stoplist('STOPLIST_IDNAMESEARCH');
exception
	when others then null;
end;
/
whenever sqlerror exit rollback;

--
-- Drop the lexer for idname search.
--
whenever sqlerror continue;
begin
  ctx_ddl.drop_preference('LEXER_IDNAMESEARCH');
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
-- Drop the function-based index on spriden change indicator name if it already exists.
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
-- Drop the domain index on spbpers ssn if it already exists.
--
whenever sqlerror continue;
declare
  index_does_not_exist EXCEPTION;
  pragma exception_init(index_does_not_exist,-01418);
begin
   execute immediate 'drop index SPBPERS_SSN_TEXT_INDEX';
exception
	when index_does_not_exist then null;
end;
/
whenever sqlerror exit rollback;
