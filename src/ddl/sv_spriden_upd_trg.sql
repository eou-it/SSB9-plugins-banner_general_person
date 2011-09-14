-- *****************************************************************************************
-- * Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.                    *
-- * This copyrighted software contains confidential and proprietary information of        *
-- * SunGard Higher Education and its subsidiaries. Any use of this software is limited    *
-- * solely to SunGard Higher Education licensees, and is further subject to the terms     *
-- * and conditions of one or more written license agreements between SunGard Higher       *
-- * Education and the licensee in question. SunGard is either a registered trademark or   *
-- * trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.*
-- * Banner and Luminis are either registered trademarks or trademarks of SunGard Higher   *
-- * Education in the U.S.A. and/or other regions and/or countries.                        *
-- *****************************************************************************************




CREATE OR REPLACE TRIGGER spriden_view_update_trg
  INSTEAD OF UPDATE ON sv_spriden
BEGIN
  gfksjpa.setId(:OLD.spriden_surrogate_id);
  gfksjpa.setVersion(:NEW.spriden_version);
  gb_identification.p_update
    (p_pidm => :NEW.spriden_pidm,
     p_id => :NEW.spriden_id,
     p_last_name => :NEW.spriden_last_name,
     p_first_name => :NEW.spriden_first_name,
     p_mi => :NEW.spriden_mi,
     p_change_ind => :NEW.spriden_change_ind,
     p_entity_ind => :NEW.spriden_entity_ind,
     p_user => :NEW.spriden_user,
     p_origin => :NEW.spriden_origin,
     p_ntyp_code => :NEW.spriden_ntyp_code,
     p_data_origin => :NEW.spriden_data_origin,
     p_surname_prefix => :NEW.spriden_surname_prefix,
     p_rowid => :NEW.spriden_v_rowid);
END;
/
