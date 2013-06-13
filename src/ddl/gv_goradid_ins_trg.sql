--/** ***************************************************************************
-- Copyright 2013 Ellucian Company L.P. and its affiliates.
-- ******************************************************************************** */

--
-- gv_goradid_ins_trg.sql
--
-- AUDIT TRAIL: 9.0
--
--    Generated view for Banner XE Admissions
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER goradid_view_insert_trg
  INSTEAD OF INSERT ON gv_goradid
DECLARE
  p_rowid_v VARCHAR2(100);
BEGIN
  gfksjpa.setId(:NEW.goradid_surrogate_id);
  gfksjpa.setVersion(:NEW.goradid_version);
    GB_ADDITIONAL_IDENT.P_CREATE(p_PIDM          =>:NEW.GORADID_PIDM,
                                 p_ADDITIONAL_ID =>:NEW.GORADID_ADDITIONAL_ID,
                                 p_ADID_CODE     =>:NEW.GORADID_ADID_CODE,
                                 p_USER_ID       =>:NEW.GORADID_USER_ID,
                                 p_DATA_ORIGIN   =>:NEW.GORADID_DATA_ORIGIN,
                                 p_ROWID_OUT     =>p_rowid_v);

END;
/
show errors