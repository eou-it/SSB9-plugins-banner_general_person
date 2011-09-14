--
-- *****************************************************************************************
-- * Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.                    *
-- * This copyrighted software contains confidential and proprietary information of        *
-- * SunGard Higher Education and its subsidiaries. Any use of this software is limited    *
-- * solely to SunGard Higher Education licensees, and is further subject to the terms     *
-- * and conditions of one or more written license agreements between SunGard Higher       *
-- * Education and the licensee in question. SunGard is either a registered trademark or   *
-- * trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.*
-- * Banner and Luminis are either registered trademarks or trademarks of SunGard Higher   *
-- * Education in the U.S.A. and/or other regions and/or countries.                        *
-- *****************************************************************************************


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
