--
-- *****************************************************************************
-- *                                                                           *
-- * Copyright 2010 SunGard. All rights reserved.                              *
-- *                                                                           *
-- * SunGard or its subsidiaries in the U.S. and other countries is the owner  *
-- * of numerous marks, including "SunGard," the SunGard logo, "Banner,"       *
-- * "PowerCAMPUS," "Advance," "Luminis," "UDC," and "Unified Digital Campus." *
-- * Other names and marks used in this material are owned by third parties.   *
-- *                                                                           *
-- * This [site/software] contains confidential and proprietary information of *
-- * SunGard and its subsidiaries. Use of this [site/software] is limited to   *
-- * SunGard Higher Education licensees, and is subject to the terms and       *
-- * conditions of one or more written license agreements between SunGard      *
-- * Higher Education and the licensee in question.                            *
-- *                                                                           *
-- *****************************************************************************
--
-- gv_gorprac_upd_trg.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 06/04/2013
--
--    Generated trigger for Horizon API support
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
