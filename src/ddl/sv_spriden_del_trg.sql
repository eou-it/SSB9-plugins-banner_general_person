-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************


REM
REM sv_spriden_del_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 1. Horizon 
REM Generated trigger for Horizon API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER spriden_view_delete_trg
  INSTEAD OF DELETE ON sv_spriden
BEGIN
  gb_identification.p_delete
    (p_pidm => :OLD.spriden_pidm,
     p_id => :OLD.spriden_id,
     p_last_name => :OLD.spriden_last_name,
     p_first_name => :OLD.spriden_first_name,
     p_mi => :OLD.spriden_mi,
     p_change_ind => :OLD.spriden_change_ind,
     p_ntyp_code => :OLD.spriden_ntyp_code,
     p_surname_prefix => :OLD.spriden_surname_prefix,
     p_rowid => :OLD.spriden_v_rowid);
END;
/