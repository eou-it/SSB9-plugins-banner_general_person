-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
REM
REM sv_sprmedi_ins_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 1. Banner XE
REM Generated trigger for Banner XE API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER sprmedi_view_create_trg
  INSTEAD OF INSERT ON sv_sprmedi
DECLARE
  p_rowid_v VARCHAR2(100);
BEGIN
  gfksjpa.setId(:NEW.sprmedi_surrogate_id);
  gfksjpa.setVersion(:NEW.sprmedi_version);
  gb_medical.p_create
    (p_pidm => :NEW.sprmedi_pidm,
     p_medi_code => :NEW.sprmedi_medi_code,
     p_mdeq_code => :NEW.sprmedi_mdeq_code,
     p_comment => :NEW.sprmedi_comment,
     p_disa_code => :NEW.sprmedi_disa_code,
     p_spsr_code => :NEW.sprmedi_spsr_code,
     p_onset_age => :NEW.sprmedi_onset_age,
     p_disb_ind => :NEW.sprmedi_disb_ind,
     p_user_id => :NEW.sprmedi_user_id,
     p_medi_code_date => :NEW.sprmedi_medi_code_date,
     p_data_origin => :NEW.sprmedi_data_origin,
     p_rowid_out => p_rowid_v);
END;
/