-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
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
-- COPYRIGHT..: Copyright 2009-2013 Ellucian Company L.P. and its affiliates.

--
-- DESCRIPTION:
--
-- This Utility Package supports Banner ID/Name Search
--
-- DESCRIPTION END
--
-- Functions
--
FUNCTION f_split
(
   chaine IN VARCHAR2,         -- input string
   pos IN PLS_INTEGER,         -- token number
   sep IN VARCHAR2 DEFAULT ',' -- separator character
)
RETURN VARCHAR2
IS
  LC$Chaine VARCHAR2(32767) := sep || chaine ;
  LI$I      PLS_INTEGER ;
  LI$I2     PLS_INTEGER ;
BEGIN
  LI$I := INSTR( LC$Chaine, sep, 1, pos ) ;
  IF LI$I > 0 THEN
    LI$I2 := INSTR( LC$Chaine, sep, 1, pos + 1) ;
    IF LI$I2 = 0 THEN LI$I2 := LENGTH( LC$Chaine ) + 1 ; END IF ;
    RETURN( SUBSTR( LC$Chaine, LI$I+1, LI$I2 - LI$I-1 ) ) ;
  ELSE
    RETURN NULL ;
  END IF ;
END f_split;
--
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
END name_search_booster;
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

PROCEDURE p_set_page_size(search_var NUMBER) IS
BEGIN
   page_size := search_var;
END p_set_page_size;
--
FUNCTION f_get_page_size RETURN NUMBER IS
BEGIN
     RETURN page_size;
END f_get_page_size;
--

PROCEDURE p_set_name1(search_var VARCHAR2) IS
BEGIN
   name1 := search_var;
END p_set_name1;
--
FUNCTION f_get_name1 RETURN VARCHAR2 IS
BEGIN
     RETURN name1;
END f_get_name1;
--

PROCEDURE p_set_name2(search_var VARCHAR2) IS
BEGIN
   name2 := search_var;
END p_set_name2;
--

PROCEDURE p_set_search_id(search_var VARCHAR2) IS
BEGIN
   search_id := search_var;
END p_set_search_id;
--
FUNCTION f_get_name2 RETURN VARCHAR2 IS
BEGIN
     RETURN name2;
END f_get_name2;
--

PROCEDURE p_set_name3(search_var VARCHAR2) IS
BEGIN
   name3 := search_var;
END p_set_name3;
--
FUNCTION f_get_name3 RETURN VARCHAR2 IS
BEGIN
     RETURN name3;
END f_get_name3;
--

FUNCTION f_get_search_id RETURN VARCHAR2 IS
BEGIN
     RETURN search_id;
END f_get_search_id;

--

FUNCTION f_match_name(filter_str VARCHAR2) RETURN NUMBER IS
 LC$String  VARCHAR2(2000) := f_get_search_filter ;
 LC$Token   VARCHAR2(100) ;
  i   PLS_INTEGER := 1 ;
  name_match PLS_INTEGER := 1 ;
BEGIN
    LOOP
       LC$Token := soknsut.f_split( LC$String, i , '|') ;
       EXIT WHEN LC$Token IS NULL ;
          IF filter_str NOT LIKE '%'||LC$Token||'%' THEN
            name_match := 2;
            EXIT;
        END IF;
       i := i + 1 ;
    END LOOP ;
--
    RETURN name_match;
--
END f_match_name;
--
END soknsut;
/
SHOW ERRORS
