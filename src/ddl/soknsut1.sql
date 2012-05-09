-- Copyright (c) SunGard Corporation 2012. All rights reserved.
-- SunGard Higher Education
--
-- soknsut1.sql
--
--
-- AUDIT TRAIL: 1.0
-- 1. mhitrik 03/18/2012
--
-- AUDIT TRAIL END
--

CREATE OR REPLACE PACKAGE BODY soknsut
AS
--
-- FILE NAME..: soknsut1.sql
-- RELEASE....: 8.5
-- OBJECT NAME: soknsut
-- PRODUCT....: Student
-- USAGE......: Utility Package to support Banner ID/Name Search
-- COPYRIGHT..: Copyright (c) SunGard Corporation 2012. All rights reserved.
--
-- DESCRIPTION:
--
-- This Utility Package supports Banner ID/Name Search
--
-- DESCRIPTION END
--
-- Functions
--
FUNCTION name_search_booster(search_var VARCHAR2, expession_var VARCHAR2) RETURN NUMBER
IS
--
 regex_var VARCHAR2(100);
 --
 cursor regex_cursor is
   SELECT 'Y'
     FROM dual
    WHERE REGEXP_LIKE(search_var, regex_var );
    --
 hold_var varchar2(1);
 --
BEGIN
--
  regex_var := '('|| expession_var|| ')';
  --
OPEN regex_cursor;
FETCH regex_cursor INTO hold_var;
--
  IF regex_cursor%FOUND THEN
    RETURN 1;
  ELSE
    RETURN 2;
  END IF;
--
END;
--
--
PROCEDURE p_set_search_filter(search_var VARCHAR2) IS
BEGIN
   search_filter := search_var;
END p_set_search_filter;
--
FUNCTION f_get_search_filter RETURN VARCHAR2 IS
BEGIN
     RETURN search_filter;
END f_get_search_filter;
--
END soknsut;
/
SHOW ERRORS
