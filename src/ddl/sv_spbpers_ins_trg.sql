--/** ***************************************************************************
-- Copyright 2013 Ellucian Company L.P. and its affiliates.
-- ******************************************************************************** */

--
-- sv_spbpers_ins_trg.sql
--
-- AUDIT TRAIL: 9.0
--
--    Generated view for Banner XE Admissions
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER spbpers_view_create_trg
  INSTEAD OF INSERT ON sv_spbpers
DECLARE
  p_rowid_v VARCHAR2(100);
BEGIN
  gfksjpa.setId(:NEW.spbpers_surrogate_id);
  gfksjpa.setVersion(:NEW.spbpers_version);
  gb_bio.p_create
    (p_pidm => :NEW.spbpers_pidm,
     p_ssn => :NEW.spbpers_ssn,
     p_birth_date => :NEW.spbpers_birth_date,
     p_lgcy_code => :NEW.spbpers_lgcy_code,
     p_ethn_code => :NEW.spbpers_ethn_code,
     p_mrtl_code => :NEW.spbpers_mrtl_code,
     p_relg_code => :NEW.spbpers_relg_code,
     p_sex => :NEW.spbpers_sex,
     p_confid_ind => :NEW.spbpers_confid_ind,
     p_dead_ind => :NEW.spbpers_dead_ind,
     p_vetc_file_number => :NEW.spbpers_vetc_file_number,
     p_legal_name => :NEW.spbpers_legal_name,
     p_pref_first_name => :NEW.spbpers_pref_first_name,
     p_name_prefix => :NEW.spbpers_name_prefix,
     p_name_suffix => :NEW.spbpers_name_suffix,
     p_vera_ind => :NEW.spbpers_vera_ind,
     p_citz_ind => :NEW.spbpers_citz_ind,
     p_dead_date => :NEW.spbpers_dead_date,
     p_citz_code => :NEW.spbpers_citz_code,
     p_hair_code => :NEW.spbpers_hair_code,
     p_eyes_code => :NEW.spbpers_eyes_code,
     p_city_birth => :NEW.spbpers_city_birth,
     p_stat_code_birth => :NEW.spbpers_stat_code_birth,
     p_driver_license => :NEW.spbpers_driver_license,
     p_stat_code_driver => :NEW.spbpers_stat_code_driver,
     p_natn_code_driver => :NEW.spbpers_natn_code_driver,
     p_uoms_code_height => :NEW.spbpers_uoms_code_height,
     p_height => :NEW.spbpers_height,
     p_uoms_code_weight => :NEW.spbpers_uoms_code_weight,
     p_weight => :NEW.spbpers_weight,
     p_sdvet_ind => :NEW.spbpers_sdvet_ind,
     p_license_issued_date => :NEW.spbpers_license_issued_date,
     p_license_expires_date => :NEW.spbpers_license_expires_date,
     p_incar_ind => :NEW.spbpers_incar_ind,
     p_itin => :NEW.spbpers_itin,
     p_active_duty_sepr_date => :NEW.spbpers_active_duty_sepr_date,
     p_data_origin => :NEW.spbpers_data_origin,
     p_user_id => :NEW.spbpers_user_id,
     p_ethn_cde => :NEW.spbpers_ethn_cde,
     p_confirmed_re_cde => :NEW.spbpers_confirmed_re_cde,
     p_confirmed_re_date => :NEW.spbpers_confirmed_re_date,
     p_armed_serv_med_vet_ind => :NEW.spbpers_armed_serv_med_vet_ind,
     p_rowid_out => p_rowid_v);
END;
/
show errors