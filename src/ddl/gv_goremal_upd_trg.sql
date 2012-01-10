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
REM
REM gv_goremal_upd_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 08/08/2011
REM 1. Horizon 
REM Generated trigger for Horizon API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER goremal_view_update_trg
  INSTEAD OF UPDATE ON gv_goremal
BEGIN
  gfksjpa.setId(:OLD.goremal_surrogate_id);
  gfksjpa.setVersion(:NEW.goremal_version);
  gb_email.p_update
    (p_pidm => :NEW.goremal_pidm,
     p_emal_code => :NEW.goremal_emal_code,
     p_email_address => :NEW.goremal_email_address,
     p_status_ind => :NEW.goremal_status_ind,
     p_preferred_ind => :NEW.goremal_preferred_ind,
     p_user_id => :NEW.goremal_user_id,
     p_comment => :NEW.goremal_comment,
     p_disp_web_ind => :NEW.goremal_disp_web_ind,
     p_data_origin => :NEW.goremal_data_origin,
     p_rowid => :NEW.goremal_v_rowid);
END;
/
show errors