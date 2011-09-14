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
-- sv_spraddr.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 07/06/2011
--
--    Generated view for Horizon API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE FORCE VIEW sv_spraddr AS SELECT
      spraddr_pidm,
      spraddr_atyp_code,
      spraddr_seqno,
      spraddr_from_date,
      spraddr_to_date,
      spraddr_street_line1,
      spraddr_street_line2,
      spraddr_street_line3,
      spraddr_city,
      spraddr_stat_code,
      spraddr_zip,
      spraddr_cnty_code,
      spraddr_natn_code,
      spraddr_phone_area,
      spraddr_phone_number,
      spraddr_phone_ext,
      spraddr_status_ind,
      spraddr_user,
      spraddr_asrc_code,
      spraddr_delivery_point,
      spraddr_correction_digit,
      spraddr_carrier_route,
      spraddr_gst_tax_id,
      spraddr_reviewed_ind,
      spraddr_reviewed_user,
      spraddr_ctry_code_phone,
      spraddr_house_number,
      spraddr_street_line4,
      spraddr_surrogate_id,
      spraddr_version,
      spraddr_user_id,
      spraddr_data_origin,
      spraddr_activity_date,
      ROWID spraddr_v_rowid
  FROM spraddr;
--
CREATE OR REPLACE PUBLIC SYNONYM sv_spraddr FOR sv_spraddr;
