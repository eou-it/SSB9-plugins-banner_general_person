
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
-- Create the stoplist for name search.
--
begin
     ctx_ddl.create_stoplist('STOPLIST_NAMESEARCH');
end;
/

--
-- Drop the fulltext index on spriden search last name if it already exists.
--
declare
  index_does_not_exist EXCEPTION;
  pragma exception_init(index_does_not_exist,-01418);
begin
   execute immediate 'drop index SPRIDEN_INDEX_LAST_NAME';
exception
	when index_does_not_exist then null;
end;
/

--
-- Drop the fulltext index on spriden search first name if it already exists.
--
declare
  index_does_not_exist EXCEPTION;
  pragma exception_init(index_does_not_exist,-01418);
begin
   execute immediate 'drop index SPRIDEN_INDEX_FIRST_NAME';
exception
	when index_does_not_exist then null;
end;
/

--
-- Drop the domain index on spriden search middle name if it already exists.
--
declare
  index_does_not_exist EXCEPTION;
  pragma exception_init(index_does_not_exist,-01418);
begin
   execute immediate 'drop index SPRIDEN_INDEX_MI';
exception
	when index_does_not_exist then null;
end;
/

--
-- Drop the domain index on spriden id if it already exists.
--
declare
  index_does_not_exist EXCEPTION;
  pragma exception_init(index_does_not_exist,-01418);
begin
   execute immediate 'drop index SPRIDEN_SEARCH_ID';
exception
	when index_does_not_exist then null;
end;
/

--
-- Drop the domain index on spriden change indicator name if it already exists.
--
declare
  index_does_not_exist EXCEPTION;
  pragma exception_init(index_does_not_exist,-01418);
begin
   execute immediate 'drop index SPRIDEN_CHANGE_IND';
exception
	when index_does_not_exist then null;
end;
/

--
-- Create the domain index on SPRIDEN_SEARCH_LAST_NAME.
--
CREATE INDEX SPRIDEN_INDEX_LAST_NAME ON SPRIDEN (SPRIDEN_SEARCH_LAST_NAME) INDEXTYPE IS ctxsys.context parameters('STOPLIST STOPLIST_NAMESEARCH');
/

--
-- Create the domain index on SPRIDEN_SEARCH_FIRST_NAME.
--
CREATE INDEX SPRIDEN_INDEX_FIRST_NAME ON SPRIDEN (SPRIDEN_SEARCH_FIRST_NAME) INDEXTYPE IS ctxsys.context parameters('STOPLIST STOPLIST_NAMESEARCH');
/

--
-- Create the domain index on SPRIDEN_SEARCH_MI.
--
CREATE INDEX SPRIDEN_INDEX_MI ON SPRIDEN (SPRIDEN_SEARCH_MI) INDEXTYPE IS ctxsys.context parameters('STOPLIST STOPLIST_NAMESEARCH');
/

--
-- Create the domain index on SPRIDEN_ID.
--
CREATE INDEX SPRIDEN_SEARCH_ID ON SPRIDEN (SPRIDEN_ID) INDEXTYPE IS ctxsys.context;
/
--
-- Create the function based index on SPRIDEN_CHANGE_IND.
--

CREATE INDEX SPRIDEN_CHANGE_IND ON SPRIDEN (nvl(SPRIDEN_CHANGE_IND,'null'));

