-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************
REM
REM sv_spriden_cur.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 1. Horizon 
REM    This view returns only the current spriden identification row where the
REM    spriden_change_indicator IS null.
REM AUDIT TRAIL END
REM
CREATE OR REPLACE FORCE VIEW SV_SPRIDEN_CUR AS SELECT
      spriden_pidm,
      spriden_id,
      spriden_last_name,
      spriden_first_name,
      spriden_mi,
      spriden_change_ind,
      spriden_entity_ind,
      spriden_user,
      spriden_origin,
      spriden_search_last_name,
      spriden_search_first_name,
      spriden_search_mi,
      spriden_soundex_last_name,
      spriden_soundex_first_name,
      spriden_ntyp_code,
      spriden_create_user,
      spriden_create_date,
      spriden_create_fdmn_code,
      spriden_surname_prefix,
      spriden_surrogate_id,
      spriden_version,
      spriden_user_id,
      spriden_data_origin,
      spriden_activity_date,
      DECODE(spriden_pidm, NULL, NULL,
             DECODE(spriden_entity_ind, 'C', F_Format_Name(spriden_pidm,'L'),
                    f_format_name(spriden_pidm,'FML'))) as FULL_NAME,
      ROWID spriden_v_rowid
  FROM spriden
  WHERE spriden_change_ind IS NULL;
REM
CREATE OR REPLACE PUBLIC SYNONYM SV_SPRIDEN_CUR FOR SV_SPRIDEN_CUR;