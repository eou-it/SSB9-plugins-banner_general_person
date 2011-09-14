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
-- sv_sprtele.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 06/30/2011
--
--    Generated view for Horizon API support
--
-- AUDIT TRAIL END
--
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
CREATE OR REPLACE PUBLIC SYNONYM sv_sprtele FOR sv_sprtele;