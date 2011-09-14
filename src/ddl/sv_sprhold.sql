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
