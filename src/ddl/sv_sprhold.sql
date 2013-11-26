-- *****************************************************************************************
-- * Copyright 2009-2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************

REM
REM sv_sprhold.sql
REM
REM AUDIT TRAIL: 9.0
REM 1. Horizon
REM Generated view for Horizon API support
REM AUDIT TRAIL END
REM
CREATE OR REPLACE FORCE VIEW sv_sprhold AS SELECT
      sprhold_pidm,
      sprhold_hldd_code,
      sprhold_user,
      sprhold_from_date,
      sprhold_to_date,
      sprhold_release_ind,
      sprhold_reason,
      sprhold_amount_owed,
      sprhold_orig_code,
      sprhold_surrogate_id,
      sprhold_version,
      sprhold_data_origin,
      sprhold_activity_date,
      ROWID sprhold_v_rowid
  FROM sprhold;
REM
CREATE OR REPLACE PUBLIC SYNONYM sv_sprhold FOR sv_sprhold;
