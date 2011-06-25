CREATE OR REPLACE TRIGGER sprhold_view_delete_trg
  INSTEAD OF DELETE ON sv_sprhold
BEGIN
  gb_hold.p_delete
    (p_user => :OLD.sprhold_user,
     p_rowid => :OLD.sprhold_v_rowid);
END;
/
