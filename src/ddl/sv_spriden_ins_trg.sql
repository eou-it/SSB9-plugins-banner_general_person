CREATE OR REPLACE TRIGGER spriden_view_create_trg
  INSTEAD OF INSERT ON sv_spriden
DECLARE
  p_id_inout_v spriden.spriden_id%TYPE;
  p_pidm_inout_v spriden.spriden_pidm%TYPE;
  p_rowid_v VARCHAR2(100);
BEGIN
  gfksjpa.setId(:NEW.spriden_surrogate_id);
  gfksjpa.setVersion(:NEW.spriden_version);
  p_id_inout_v := :NEW.spriden_id;
  p_pidm_inout_v := :NEW.spriden_pidm;
  gb_identification.p_create
    (p_id_inout => p_id_inout_v,
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
     p_pidm_inout => p_pidm_inout_v,
     p_rowid_out => p_rowid_v);
END;
/
