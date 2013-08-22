-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
REM
REM sv_spraddr_upd_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 07/06/2011
REM 1. Banner XE
REM Generated trigger for Banner XE API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER spraddr_view_update_trg
  INSTEAD OF UPDATE ON sv_spraddr
BEGIN
  gfksjpa.setId(:OLD.spraddr_surrogate_id);
  gfksjpa.setVersion(:NEW.spraddr_version);
  gb_address.p_update
    (p_pidm => :NEW.spraddr_pidm,
     p_atyp_code => :NEW.spraddr_atyp_code,
     p_seqno => :NEW.spraddr_seqno,
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
     p_rowid => :NEW.spraddr_v_rowid);
END;
/
show errors