--/** ***************************************************************************
-- Copyright 2013 Ellucian Company L.P. and its affiliates.
-- ******************************************************************************** */

-- sv_spbpers_del_trg.sql
--
-- AUDIT TRAIL: 9.0
--
--    Generated view for Banner XE Admissions
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER spbpers_view_delete_trg
  INSTEAD OF DELETE ON sv_spbpers
BEGIN
  gb_bio.p_delete(
     p_PIDM              =>:OLD.spbpers_pidm,
     p_ROWID             =>:OLD.spbpers_v_rowid);
END;
/
show errors