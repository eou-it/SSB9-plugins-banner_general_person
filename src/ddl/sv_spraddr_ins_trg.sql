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
-- sv_spraddr_ins_trg.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 07/06/2011
--
--    Generated trigger for Horizon API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER spraddr_view_create_trg
  INSTEAD OF INSERT ON sv_spraddr
DECLARE
  p_seqno_inout_v spraddr.spraddr_seqno%TYPE;
  p_rowid_v VARCHAR2(100);
BEGIN
  gfksjpa.setId(:NEW.spraddr_surrogate_id);
  gfksjpa.setVersion(:NEW.spraddr_version);
  p_seqno_inout_v := :NEW.spraddr_seqno;
  gb_address.p_create
    (p_pidm => :NEW.spraddr_pidm,
     p_atyp_code => :NEW.spraddr_atyp_code,
     p_from_date => :NEW.spraddr_from_date,
     p_to_date => :NEW.spraddr_to_date,
     p_street_line1 => :NEW.spraddr_street_line1,
     p_street_line2 => :NEW.spraddr_street_line2,
     p_street_line3 => :NEW.spraddr_street_line3,
     p_city => :NEW.spraddr_city,
     p_stat_code => :NEW.spraddr_stat_code,
     p_zip => :NEW.spraddr_zip,
     p_cnty_code => :NEW.spraddr_cnty_code,
     p_natn_code => :NEW.spraddr_natn_code,
     p_status_ind => :NEW.spraddr_status_ind,
     p_user => :NEW.spraddr_user,
     p_asrc_code => :NEW.spraddr_asrc_code,
     p_delivery_point => :NEW.spraddr_delivery_point,
     p_correction_digit => :NEW.spraddr_correction_digit,
     p_carrier_route => :NEW.spraddr_carrier_route,
     p_gst_tax_id => :NEW.spraddr_gst_tax_id,
     p_reviewed_ind => :NEW.spraddr_reviewed_ind,
     p_reviewed_user => :NEW.spraddr_reviewed_user,
     p_data_origin => :NEW.spraddr_data_origin,
     p_ctry_code_phone => :NEW.spraddr_ctry_code_phone,
     p_house_number => :NEW.spraddr_house_number,
     p_street_line4 => :NEW.spraddr_street_line4,
     p_seqno_inout => p_seqno_inout_v,
     p_rowid_out => p_rowid_v);
END;
/
show errors
