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
