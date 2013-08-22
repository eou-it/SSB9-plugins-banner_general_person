-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
REM
REM sv_spraddr_del_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 07/06/2011
REM 1. Banner XE
REM Generated trigger for Banner XE API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER spraddr_view_delete_trg
  INSTEAD OF DELETE ON sv_spraddr
BEGIN
  gb_address.p_delete
    (p_pidm => :OLD.spraddr_pidm,
     p_atyp_code => :OLD.spraddr_atyp_code,
     p_seqno => :OLD.spraddr_seqno,
     p_status_ind => :OLD.spraddr_status_ind,
     p_rowid => :OLD.spraddr_v_rowid);
END;
/
show errors