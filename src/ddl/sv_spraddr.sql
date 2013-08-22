-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
REM
REM sv_spraddr.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 07/06/2011
REM 1. Banner XE
REM Generated view for Banner XE API support
REM AUDIT TRAIL END 
REM
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
REM
CREATE OR REPLACE PUBLIC SYNONYM sv_spraddr FOR sv_spraddr;