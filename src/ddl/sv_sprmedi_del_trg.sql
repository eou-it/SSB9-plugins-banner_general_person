CREATE OR REPLACE TRIGGER sprmedi_view_delete_trg
  INSTEAD OF DELETE ON sv_sprmedi
BEGIN
  gb_medical.p_delete
    (p_pidm => :OLD.sprmedi_pidm,
     p_medi_code => :OLD.sprmedi_medi_code,
     p_rowid => :OLD.sprmedi_v_rowid);
END;
/
