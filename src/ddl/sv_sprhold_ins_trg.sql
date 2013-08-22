-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
REM
REM sv_sprhold_ins_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 1. Banner XE
REM Generated trigger for Banner XE API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER sprhold_view_create_trg
  INSTEAD OF INSERT ON sv_sprhold
DECLARE
  p_rowid_v VARCHAR2(100);
BEGIN
  gfksjpa.setId(:NEW.sprhold_surrogate_id);
  gfksjpa.setVersion(:NEW.sprhold_version);
  gb_hold.p_create
    (p_pidm => :NEW.sprhold_pidm,
     p_hldd_code => :NEW.sprhold_hldd_code,
     p_user => :NEW.sprhold_user,
     p_from_date => :NEW.sprhold_from_date,
     p_to_date => :NEW.sprhold_to_date,
     p_release_ind => :NEW.sprhold_release_ind,
     p_reason => :NEW.sprhold_reason,
     p_amount_owed => :NEW.sprhold_amount_owed,
     p_orig_code => :NEW.sprhold_orig_code,
     p_data_origin => :NEW.sprhold_data_origin,
     p_rowid_out => p_rowid_v);
END;
/