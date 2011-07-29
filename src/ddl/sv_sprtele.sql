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