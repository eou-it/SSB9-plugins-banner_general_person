-- Copyright (c) SunGard Corporation 2012. All rights reserved.
-- SunGard Higher Education
--
-- soknsut.sql
--
--
-- AUDIT TRAIL: 9.0
-- 1. mhitrik 03/18/2012
--    Create initial package
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE PACKAGE soknsut
AS

   -- FILE NAME..: soknsut.sql
   -- RELEASE....: 8.5
   -- OBJECT NAME: soknsut
   -- PRODUCT....: STUDENT
   -- USAGE......: Utility Package to support Banner ID/Name Search
   -- COPYRIGHT..: Copyright (c) SunGard Corporation 2012. All rights reserved.
   --
   -- DESCRIPTION:
   --
   -- This Utility Package supports Banner ID/Name Search
   --
   -- DESCRIPTION END
   -- Variables
      search_filter       VARCHAR2(2000);
      page_size           NUMBER;
   --
   --
   -- Procedures
   --
   /**
   * This function boosts last name search as the top search attribute.
   *
   * @param search_var     Person Name
   * @param expression_var Search Attributes( e.g. First Name, Last Name, Middle Name, Id )
   *
   */
FUNCTION name_search_booster(search_var VARCHAR2, expession_var VARCHAR2) RETURN NUMBER;
--
   /**
   * This function returns the search filter for the advanced search component.
   */
FUNCTION f_get_search_filter RETURN VARCHAR2;
--
   /**
   * This procedure set the search filter for the advanced search component.
   */
PROCEDURE p_set_search_filter(search_var VARCHAR2);
--
/**
   * This function returns the page size of listbox for the advanced search component.
   */
FUNCTION f_get_page_size RETURN NUMBER;
--
   /**
   * This procedure set the page size of listbox for the advanced search component.
   */
PROCEDURE p_set_page_size(search_var NUMBER);
--
   /*
   * This function split the search string into tokens
   * @param chaine The search parameters separated by |
   * @param pos Position
   * @param sep Separator
   */
FUNCTION f_split(chaine IN VARCHAR2, pos IN PLS_INTEGER, sep IN VARCHAR2 DEFAULT ',') RETURN VARCHAR2;
--
   /*
   * This function returns 1 if match is found, 2 if match is not found.
   * @param The name search string to be matched
   */
FUNCTION f_match_name(filter_str VARCHAR2) RETURN NUMBER;
--
pragma restrict_references (f_get_search_filter, WNDS,WNPS);
--
END soknsut;
/
SHOW ERRORS
--
SET SCAN ON
WHENEVER SQLERROR EXIT ROLLBACK;
CREATE OR REPLACE PUBLIC SYNONYM soknsut FOR soknsut;
START gurgrtb soknsut
WHENEVER SQLERROR CONTINUE;
--
PROMPT
PROMPT **********************************************************************************
PROMPT * Finished running soknsut package spec. Review any Errors Encountered.    *
PROMPT **********************************************************************************
PROMPT
PROMPT
