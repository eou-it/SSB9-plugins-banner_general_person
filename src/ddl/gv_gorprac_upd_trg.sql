-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
--
-- gv_gorprac_upd_trg.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 06/04/2013
--
--    Generated trigger for Banner XE API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER gorprac_view_update_trg
  INSTEAD OF UPDATE ON gv_gorprac
BEGIN
  gfksjpa.setId(:OLD.gorprac_surrogate_id);
  gfksjpa.setVersion(:NEW.gorprac_version);
  gb_person_race.p_update
    (p_pidm => :NEW.gorprac_pidm,
     p_race_cde => :NEW.gorprac_race_cde,
     p_user_id => :NEW.gorprac_user_id,
     p_data_origin => :NEW.gorprac_data_origin,
     p_rowid => :NEW.gorprac_v_rowid);
END;
/
show errors
