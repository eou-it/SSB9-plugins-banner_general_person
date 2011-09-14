--
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


--
-- gv_goremal_ins_trg.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 08/08/2011
--
--    Generated trigger for Horizon API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER goremal_view_create_trg
  INSTEAD OF INSERT ON gv_goremal
DECLARE
  p_rowid_v VARCHAR2(100);
BEGIN
  gfksjpa.setId(:NEW.goremal_surrogate_id);
  gfksjpa.setVersion(:NEW.goremal_version);
  gb_email.p_create
    (p_pidm => :NEW.goremal_pidm,
     p_emal_code => :NEW.goremal_emal_code,
     p_email_address => :NEW.goremal_email_address,
     p_status_ind => :NEW.goremal_status_ind,
     p_preferred_ind => :NEW.goremal_preferred_ind,
     p_user_id => :NEW.goremal_user_id,
     p_comment => :NEW.goremal_comment,
     p_disp_web_ind => :NEW.goremal_disp_web_ind,
     p_data_origin => :NEW.goremal_data_origin,
     p_rowid_out => p_rowid_v);
END;
/
show errors
