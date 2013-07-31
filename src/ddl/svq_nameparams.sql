-- *****************************************************************************************
-- * Copyright 2009-2012 SunGard Higher Education. All Rights Reserved.                    *
-- * This copyrighted software contains confidential and proprietary information of        *
-- * SunGard Higher Education and its subsidiaries. Any use of this software is limited    *
-- * solely to SunGard Higher Education licensees, and is further subject to the terms     *
-- * and conditions of one or more written license agreements between SunGard Higher       *
-- * Education and the licensee in question. SunGard is either a registered trademark or   *
-- * trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.*
-- * Banner and Luminis are either registered trademarks or trademarks of SunGard Higher   *
-- * Education in the U.S.A. and/or other regions and/or countries.                        *
-- *****************************************************************************************
--  SVQ_NAMEPARAMS.sql
--
-- AUDIT TRAIL: 9.0
-- Read only view for Advanced Search Filter UI Component - ID Search    rahulb 31-Jul-2012
-- AUDIT TRAIL END
--

SET SCAN OFF;
CREATE OR REPLACE FORCE VIEW SVQ_NAME_PARAMS(NAME1,NAME2,
NAME3) as
Select
Soknsut.F_Get_Name1,
Soknsut.F_Get_Name2,
Soknsut.F_Get_Name3
from dual;

COMMENT ON TABLE SVQ_NAME_PARAMS IS 'Read only view for Name Search';
COMMENT ON COLUMN SVQ_NAME_PARAMS.NAME1 IS 'First word in the search text ';
COMMENT ON COLUMN SVQ_NAME_PARAMS.NAME2 IS 'Second word in the search text ';
COMMENT ON COLUMN SVQ_NAME_PARAMS.NAME3 IS 'Third word in the search text ';
CREATE OR REPLACE PUBLIC SYNONYM SVQ_NAME_PARAMS FOR SVQ_NAME_PARAMS;
SHOW ERRORS VIEW SVQ_NAME_PARAMS;
SET SCAN ON;

