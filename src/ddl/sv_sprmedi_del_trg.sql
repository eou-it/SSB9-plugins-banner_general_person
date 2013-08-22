-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
REM
REM sv_sprmedi_del_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 1. Banner XE
REM Generated trigger for Banner XE API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER sprmedi_view_delete_trg
  INSTEAD OF DELETE ON sv_sprmedi
BEGIN
  gb_medical.p_delete
    (p_pidm => :OLD.sprmedi_pidm,
     p_medi_code => :OLD.sprmedi_medi_code,
     p_rowid => :OLD.sprmedi_v_rowid);
END;
/