--
-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************


REM
REM gv_goremal_ins_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 08/08/2011
REM 1. Horizon 
REM Generated trigger for Horizon API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER goremal_view_create_trg
  INSTEAD OF INSERT ON gv_goremal
DECLARE
  p_rowid_v VARCHAR2(100);
BEGIN
  gfksjpa.setId(:NEW.goremal_surrogate_id);
  gfksjpa.setVersion(:NEW.goremal_version);
  gb_email.p_create
    (p_pidm => :NEW.goremal_pidm,
     p_emal_code => :NEW.goremal_emal_code,
     p_email_address => :NEW.goremal_email_address,
     p_status_ind => :NEW.goremal_status_ind,
     p_preferred_ind => :NEW.goremal_preferred_ind,
     p_user_id => :NEW.goremal_user_id,
     p_comment => :NEW.goremal_comment,
     p_disp_web_ind => :NEW.goremal_disp_web_ind,
     p_data_origin => :NEW.goremal_data_origin,
     p_rowid_out => p_rowid_v);
END;
/
show errors