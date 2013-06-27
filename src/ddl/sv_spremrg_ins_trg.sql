-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************
--
-- sv_spremrg_ins_trg.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 06/03/2013
--
--    Generated trigger for Horizon API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER spremrg_view_create_trg
  INSTEAD OF INSERT ON sv_spremrg
DECLARE
  p_rowid_v VARCHAR2(100);
BEGIN
  gfksjpa.setId(:NEW.spremrg_surrogate_id);
  gfksjpa.setVersion(:NEW.spremrg_version);
  gb_emergency_contact.p_create
    (p_pidm => :NEW.spremrg_pidm,
     p_priority => :NEW.spremrg_priority,
     p_last_name => :NEW.spremrg_last_name,
     p_first_name => :NEW.spremrg_first_name,
     p_mi => :NEW.spremrg_mi,
     p_street_line1 => :NEW.spremrg_street_line1,
     p_street_line2 => :NEW.spremrg_street_line2,
     p_street_line3 => :NEW.spremrg_street_line3,
     p_city => :NEW.spremrg_city,
     p_stat_code => :NEW.spremrg_stat_code,
     p_natn_code => :NEW.spremrg_natn_code,
     p_zip => :NEW.spremrg_zip,
     p_phone_area => :NEW.spremrg_phone_area,
     p_phone_number => :NEW.spremrg_phone_number,
     p_phone_ext => :NEW.spremrg_phone_ext,
     p_relt_code => :NEW.spremrg_relt_code,
     p_atyp_code => :NEW.spremrg_atyp_code,
     p_data_origin => :NEW.spremrg_data_origin,
     p_user_id => :NEW.spremrg_user_id,
     p_surname_prefix => :NEW.spremrg_surname_prefix,
     p_ctry_code_phone => :NEW.spremrg_ctry_code_phone,
     p_house_number => :NEW.spremrg_house_number,
     p_street_line4 => :NEW.spremrg_street_line4,
     p_rowid_out => p_rowid_v);
END;
/
show errors
