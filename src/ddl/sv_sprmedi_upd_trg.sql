CREATE OR REPLACE TRIGGER sprmedi_view_update_trg
  INSTEAD OF UPDATE ON sv_sprmedi
BEGIN
  gfksjpa.setId(:OLD.sprmedi_surrogate_id);
  gfksjpa.setVersion(:NEW.sprmedi_version);
  gb_medical.p_update
    (p_pidm => :NEW.sprmedi_pidm,
     p_medi_code => :NEW.sprmedi_medi_code,
     p_mdeq_code => :NEW.sprmedi_mdeq_code,
     p_comment => :NEW.sprmedi_comment,
     p_disa_code => :NEW.sprmedi_disa_code,
     p_spsr_code => :NEW.sprmedi_spsr_code,
     p_onset_age => :NEW.sprmedi_onset_age,
     p_disb_ind => :NEW.sprmedi_disb_ind,
     p_user_id => :NEW.sprmedi_user_id,
     p_medi_code_date => :NEW.sprmedi_medi_code_date,
     p_data_origin => :NEW.sprmedi_data_origin,
     p_rowid => :NEW.sprmedi_v_rowid);
END;
/
