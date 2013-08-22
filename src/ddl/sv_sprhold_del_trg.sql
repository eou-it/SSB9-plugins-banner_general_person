-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
REM
REM sv_sprhold_del_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 1. Banner XE
REM Generated trigger for Banner XE API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER sprhold_view_delete_trg
  INSTEAD OF DELETE ON sv_sprhold
BEGIN
  gb_hold.p_delete
    (p_user => :OLD.sprhold_user,
     p_rowid => :OLD.sprhold_v_rowid);
END;
/