--/** ***************************************************************************
-- Copyright 2013 Ellucian Company L.P. and its affiliates.
-- ******************************************************************************** */

--
-- sv_spbpers_upd_trg.sql
--
-- AUDIT TRAIL: 9.0
--
--    Generated view for Banner XE Admissions
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER spbpers_view_update_trg
  INSTEAD OF UPDATE ON sv_spbpers
BEGIN
  gfksjpa.setId(:OLD.spbpers_surrogate_id);
  gfksjpa.setVersion(:NEW.spbpers_version);
  GB_BIO.P_UPDATE(p_PIDM                   =>:NEW.SPBPERS_PIDM,
                  p_SSN                    =>:NEW.SPBPERS_SSN,
                  p_BIRTH_DATE             =>:NEW.SPBPERS_BIRTH_DATE,
                  p_LGCY_CODE              =>:NEW.SPBPERS_LGCY_CODE,
                  p_ETHN_CODE              =>:NEW.SPBPERS_ETHN_CODE,
                  p_MRTL_CODE              =>:NEW.SPBPERS_MRTL_CODE,
                  p_RELG_CODE              =>:NEW.SPBPERS_RELG_CODE,
                  p_SEX                    =>:NEW.SPBPERS_SEX,
                  p_CONFID_IND             =>:NEW.SPBPERS_CONFID_IND,
                  p_DEAD_IND               =>:NEW.SPBPERS_DEAD_IND,
                  p_VETC_FILE_NUMBER       =>:NEW.SPBPERS_VETC_FILE_NUMBER,
                  p_LEGAL_NAME             =>:NEW.SPBPERS_LEGAL_NAME,
                  p_PREF_FIRST_NAME        =>:NEW.SPBPERS_PREF_FIRST_NAME,
                  p_NAME_PREFIX            =>:NEW.SPBPERS_NAME_PREFIX,
                  p_NAME_SUFFIX            =>:NEW.SPBPERS_NAME_SUFFIX,
                  p_VERA_IND               =>:NEW.SPBPERS_VERA_IND,
                  p_DEAD_DATE              =>:NEW.SPBPERS_DEAD_DATE,
                  p_CITZ_CODE              =>:NEW.SPBPERS_CITZ_CODE,
                  p_ACTIVE_DUTY_SEPR_DATE  =>:NEW.SPBPERS_ACTIVE_DUTY_SEPR_DATE,
                  p_SDVET_IND              =>:NEW.SPBPERS_SDVET_IND,
                  p_DATA_ORIGIN            =>:NEW.SPBPERS_DATA_ORIGIN,
                  p_USER_ID                =>:NEW.SPBPERS_USER_ID,
                  P_ETHN_CDE               =>:NEW.SPBPERS_ETHN_CDE,
                  P_CONFIRMED_RE_CDE       =>:NEW.SPBPERS_CONFIRMED_RE_CDE,
                  P_CONFIRMED_RE_DATE      =>:NEW.SPBPERS_CONFIRMED_RE_DATE,
                  p_ARMED_SERV_MED_VET_IND =>:NEW.SPBPERS_ARMED_SERV_MED_VET_IND,
                  p_rowid                  =>:NEW.spbpers_v_rowid);
END;
/
show errors