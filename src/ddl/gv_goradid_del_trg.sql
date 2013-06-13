--/** ***************************************************************************
-- Copyright 2013 Ellucian Company L.P. and its affiliates.
-- ******************************************************************************** */

-- gv_goradid_del_trg.sql
--
-- AUDIT TRAIL: 9.0
--
--    Generated view for Banner XE Admissions
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER goradid_view_delete_trg
  INSTEAD OF DELETE ON gv_goradid
BEGIN
  GB_ADDITIONAL_IDENT.P_DELETE(p_PIDM          =>:OLD.GORADID_PIDM,
                               p_ADDITIONAL_ID =>:OLD.GORADID_ADDITIONAL_ID,
                               p_ADID_CODE     =>:OLD.GORADID_ADID_CODE,
                               p_ROWID         =>:OLD.goradid_v_rowid);

END;
/
show errors