
-- *****************************************************************************************
-- * Copyright 2010-2013 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************

REM
REM spriden_080602_01.sql
REM
REM This script creates index for idname searching
REM
REM Project: Student id/name search for Banner General
REM AUDIT TRAIL: 8.6.2
REM 8/11/2013
REM 1. Added indexes for name and id searching.
REM AUDIT TRAIL END
--
--
-- Create the stoplist for idname search.
-- Create the lexer for idname search.
--
whenever sqlerror continue;
begin
     ctx_ddl.create_stoplist('STOPLIST_IDNAMESEARCH');
     ctx_ddl.create_preference('LEXER_IDNAMESEARCH', 'BASIC_LEXER');
     ctx_ddl.set_attribute('LEXER_IDNAMESEARCH','printjoins','-_&=''?:;,."|~$!*#@(){}[]>\/');
exception
		when others then null;
end;
/
whenever sqlerror exit rollback;
