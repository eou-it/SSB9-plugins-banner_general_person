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
-- sv_sprtele_ins_trg.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 06/30/2011
--
--    Generated trigger for Horizon API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER sprtele_view_create_trg
  INSTEAD OF INSERT ON sv_sprtele
DECLARE
  p_seqno_out_v sprtele.sprtele_seqno%TYPE;
  p_rowid_v VARCHAR2(100);
BEGIN
  gfksjpa.setId(:NEW.sprtele_surrogate_id);
  gfksjpa.setVersion(:NEW.sprtele_version);
  p_seqno_out_v := :NEW.sprtele_seqno;
  gb_telephone.p_create
    (p_pidm => :NEW.sprtele_pidm,
     p_tele_code => :NEW.sprtele_tele_code,
     p_phone_area => :NEW.sprtele_phone_area,
     p_phone_number => :NEW.sprtele_phone_number,
     p_phone_ext => :NEW.sprtele_phone_ext,
     p_status_ind => :NEW.sprtele_status_ind,
     p_atyp_code => :NEW.sprtele_atyp_code,
     p_addr_seqno => :NEW.sprtele_addr_seqno,
     p_primary_ind => :NEW.sprtele_primary_ind,
     p_unlist_ind => :NEW.sprtele_unlist_ind,
     p_comment => :NEW.sprtele_comment,
     p_intl_access => :NEW.sprtele_intl_access,
     p_data_origin => :NEW.sprtele_data_origin,
     p_user_id => :NEW.sprtele_user_id,
     p_ctry_code_phone => :NEW.sprtele_ctry_code_phone,
     p_seqno_out => p_seqno_out_v,
     p_rowid_out => p_rowid_v);
END;
/
show errors
