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
      sprhold_user_id,
      sprhold_data_origin,
      sprhold_activity_date,
      ROWID sprhold_v_rowid
  FROM saturn.sprhold;
CREATE OR REPLACE PUBLIC SYNONYM sv_sprhold FOR sv_sprhold;
