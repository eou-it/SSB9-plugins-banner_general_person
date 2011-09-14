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




CREATE OR REPLACE FORCE VIEW SV_SPRIDEN AS SELECT
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
      decode(spriden_pidm,null, null,f_format_name(spriden_pidm,'FML')) as FULL_NAME,
      ROWID spriden_v_rowid
  FROM saturn.spriden;
CREATE OR REPLACE PUBLIC SYNONYM SV_SPRIDEN FOR SV_SPRIDEN;
