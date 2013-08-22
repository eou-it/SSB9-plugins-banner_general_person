-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
REM
REM gv_goremal_del_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 08/08/2011
REM 1. Banner XE
REM Generated trigger for Banner XE API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER goremal_view_delete_trg
  INSTEAD OF DELETE ON gv_goremal
BEGIN
  gb_email.p_delete
    (p_pidm => :OLD.goremal_pidm,
     p_emal_code => :OLD.goremal_emal_code,
     p_email_address => :OLD.goremal_email_address,
     p_rowid => :OLD.goremal_v_rowid);
END;
/
show errors