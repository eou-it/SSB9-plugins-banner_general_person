--
-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************


REM
REM sv_sprtele_del_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 30/06/2011
REM 1. Horizon 
REM Generated trigger for Horizon API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER sprtele_view_delete_trg
  INSTEAD OF DELETE ON sv_sprtele
BEGIN
  gb_telephone.p_delete
    (p_pidm => :OLD.sprtele_pidm,
     p_tele_code => :OLD.sprtele_tele_code,
     p_seqno => :OLD.sprtele_seqno,
     p_rowid => :OLD.sprtele_v_rowid);
END;
/
show errors