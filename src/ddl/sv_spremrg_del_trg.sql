-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************
--
-- sv_spremrg_del_trg.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 06/03/2013
--
--    Generated trigger for Banner XE API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER spremrg_view_delete_trg
  INSTEAD OF DELETE ON sv_spremrg
BEGIN
  gb_emergency_contact.p_delete
    (p_pidm => :OLD.spremrg_pidm,
     p_priority => :OLD.spremrg_priority,
     p_rowid => :OLD.spremrg_v_rowid);
END;
/
show errors
