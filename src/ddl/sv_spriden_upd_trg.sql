-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************


REM
REM sv_spriden_upd_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 1. Horizon 
REM Generated trigger for Horizon API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER spriden_view_update_trg
  INSTEAD OF UPDATE ON sv_spriden
BEGIN
  gfksjpa.setId(:OLD.spriden_surrogate_id);
  gfksjpa.setVersion(:NEW.spriden_version);
  gb_identification.p_update
    (p_pidm => :NEW.spriden_pidm,
     p_id => :NEW.spriden_id,
     p_last_name => :NEW.spriden_last_name,
     p_first_name => :NEW.spriden_first_name,
     p_mi => :NEW.spriden_mi,
     p_change_ind => :NEW.spriden_change_ind,
     p_entity_ind => :NEW.spriden_entity_ind,
     p_user => :NEW.spriden_user,
     p_origin => :NEW.spriden_origin,
     p_ntyp_code => :NEW.spriden_ntyp_code,
     p_data_origin => :NEW.spriden_data_origin,
     p_surname_prefix => :NEW.spriden_surname_prefix,
     p_rowid => :NEW.spriden_v_rowid);
END;
/