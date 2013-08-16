--
-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************


REM
REM sv_sprtele.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 30/06/2011
REM 1. Horizon 
REM Generated view for Horizon API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE FORCE VIEW sv_sprtele AS SELECT
      sprtele_pidm,
      sprtele_seqno,
      sprtele_tele_code,
      sprtele_phone_area,
      sprtele_phone_number,
      sprtele_phone_ext,
      sprtele_status_ind,
      sprtele_atyp_code,
      sprtele_addr_seqno,
      sprtele_primary_ind,
      sprtele_unlist_ind,
      sprtele_comment,
      sprtele_intl_access,
      sprtele_ctry_code_phone,
      sprtele_surrogate_id,
      sprtele_version,
      sprtele_user_id,
      sprtele_data_origin,
      sprtele_activity_date,
      ROWID sprtele_v_rowid
  FROM sprtele;
REM
CREATE OR REPLACE PUBLIC SYNONYM sv_sprtele FOR sv_sprtele;