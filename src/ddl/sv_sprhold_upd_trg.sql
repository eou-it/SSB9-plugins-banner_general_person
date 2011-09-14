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




CREATE OR REPLACE TRIGGER sprhold_view_update_trg
  INSTEAD OF UPDATE ON sv_sprhold
BEGIN
  gfksjpa.setId(:OLD.sprhold_surrogate_id);
  gfksjpa.setVersion(:NEW.sprhold_version);
  gb_hold.p_update
    (p_pidm => :NEW.sprhold_pidm,
     p_hldd_code => :NEW.sprhold_hldd_code,
     p_user => :NEW.sprhold_user,
     p_from_date => :NEW.sprhold_from_date,
     p_to_date => :NEW.sprhold_to_date,
     p_release_ind => :NEW.sprhold_release_ind,
     p_reason => :NEW.sprhold_reason,
     p_amount_owed => :NEW.sprhold_amount_owed,
     p_orig_code => :NEW.sprhold_orig_code,
     p_data_origin => :NEW.sprhold_data_origin,
     p_rowid => :NEW.sprhold_v_rowid);
END;
/
