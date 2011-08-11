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
-- gv_goremal.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 08/08/2011
--
--    Generated view for Horizon API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE FORCE VIEW gv_goremal AS SELECT
      goremal_pidm,
      goremal_emal_code,
      goremal_email_address,
      goremal_status_ind,
      goremal_preferred_ind,
      goremal_comment,
      goremal_disp_web_ind,
      goremal_surrogate_id,
      goremal_version,
      goremal_user_id,
      goremal_data_origin,
      goremal_activity_date,
      ROWID goremal_v_rowid
  FROM goremal;
--
CREATE OR REPLACE PUBLIC SYNONYM gv_goremal FOR gv_goremal;
