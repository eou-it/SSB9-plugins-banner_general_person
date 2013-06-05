--
-- *****************************************************************************
-- *                                                                           *
-- * Copyright 2010 SunGard. All rights reserved.                              *
-- *                                                                           *
-- * SunGard or its subsidiaries in the U.S. and other countries is the owner  *
-- * of numerous marks, including "SunGard," the SunGard logo, "Banner,"       *
-- * "PowerCAMPUS," "Advance," "Luminis," "UDC," and "Unified Digital Campus." *
-- * Other names and marks used in this material are owned by third parties.   *
-- *                                                                           *
-- * This [site/software] contains confidential and proprietary information of *
-- * SunGard and its subsidiaries. Use of this [site/software] is limited to   *
-- * SunGard Higher Education licensees, and is subject to the terms and       *
-- * conditions of one or more written license agreements between SunGard      *
-- * Higher Education and the licensee in question.                            *
-- *                                                                           *
-- *****************************************************************************
--
-- gv_gorprac.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 06/04/2013
--
--    Generated view for Horizon API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE FORCE VIEW gv_gorprac AS SELECT
      gorprac_pidm,
      gorprac_race_cde,
      gorprac_surrogate_id,
      gorprac_version,
      gorprac_user_id,
      gorprac_data_origin,
      gorprac_activity_date,
      ROWID gorprac_v_rowid
  FROM gorprac;
--
CREATE OR REPLACE PUBLIC SYNONYM gv_gorprac FOR gv_gorprac;
