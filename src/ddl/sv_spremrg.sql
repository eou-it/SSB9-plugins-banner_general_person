-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************
--
-- sv_spremrg.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 06/03/2013
--
--    Generated view for Horizon API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE FORCE VIEW sv_spremrg AS SELECT
      spremrg_pidm,
      spremrg_priority,
      spremrg_last_name,
      spremrg_first_name,
      spremrg_mi,
      spremrg_street_line1,
      spremrg_street_line2,
      spremrg_street_line3,
      spremrg_city,
      spremrg_stat_code,
      spremrg_natn_code,
      spremrg_zip,
      spremrg_phone_area,
      spremrg_phone_number,
      spremrg_phone_ext,
      spremrg_relt_code,
      spremrg_atyp_code,
      spremrg_surname_prefix,
      spremrg_ctry_code_phone,
      spremrg_house_number,
      spremrg_street_line4,
      spremrg_surrogate_id,
      spremrg_version,
      spremrg_user_id,
      spremrg_data_origin,
      spremrg_activity_date,
      ROWID spremrg_v_rowid
  FROM spremrg;
--
CREATE OR REPLACE PUBLIC SYNONYM sv_spremrg FOR sv_spremrg;
