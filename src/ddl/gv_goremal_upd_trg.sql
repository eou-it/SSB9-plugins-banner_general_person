-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
REM
REM gv_goremal_upd_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 08/08/2011
REM 1. Banner XE
REM Generated trigger for Banner XE API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER goremal_view_update_trg
  INSTEAD OF UPDATE ON gv_goremal
BEGIN
  gfksjpa.setId(:OLD.goremal_surrogate_id);
  gfksjpa.setVersion(:NEW.goremal_version);
  gb_email.p_update
    (p_pidm => :NEW.goremal_pidm,
     p_emal_code => :NEW.goremal_emal_code,
     p_email_address => :NEW.goremal_email_address,
     p_status_ind => :NEW.goremal_status_ind,
     p_preferred_ind => :NEW.goremal_preferred_ind,
     p_user_id => :NEW.goremal_user_id,
     p_comment => :NEW.goremal_comment,
     p_disp_web_ind => :NEW.goremal_disp_web_ind,
     p_data_origin => :NEW.goremal_data_origin,
     p_rowid => :NEW.goremal_v_rowid);
END;
/
show errors