-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
REM
REM sv_sprtele_upd_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 30/06/2011
REM 1. Banner XE
REM Generated trigger for Banner XE API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER sprtele_view_update_trg
  INSTEAD OF UPDATE ON sv_sprtele
BEGIN
  gfksjpa.setId(:OLD.sprtele_surrogate_id);
  gfksjpa.setVersion(:NEW.sprtele_version);
  gb_telephone.p_update
    (p_pidm => :NEW.sprtele_pidm,
     p_seqno => :NEW.sprtele_seqno,
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
     p_rowid => :NEW.sprtele_v_rowid);
END;
/
show errors