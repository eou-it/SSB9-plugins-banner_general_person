-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
--
-- gv_gorprac_del_trg.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 06/04/2013
--
--    Generated trigger for Banner XE API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER gorprac_view_delete_trg
  INSTEAD OF DELETE ON gv_gorprac
BEGIN
  gb_person_race.p_delete
    (p_pidm => :OLD.gorprac_pidm,
     p_race_cde => :OLD.gorprac_race_cde,
     p_rowid => :OLD.gorprac_v_rowid);
END;
/
show errors
