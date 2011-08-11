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
