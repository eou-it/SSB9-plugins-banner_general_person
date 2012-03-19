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
