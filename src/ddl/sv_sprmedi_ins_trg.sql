-- *****************************************************************************************
-- * Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.                    *
-- * This copyrighted software contains confidential and proprietary information of        *
-- * SunGard Higher Education and its subsidiaries. Any use of this software is limited    *
-- * solely to SunGard Higher Education licensees, and is further subject to the terms     *
-- * and conditions of one or more written license agreements between SunGard Higher       *
-- * Education and the licensee in question. SunGard is either a registered trademark or   *
-- * trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.*
-- * Banner and Luminis are either registered trademarks or trademarks of SunGard Higher   *
-- * Education in the U.S.A. and/or other regions and/or countries.                        *
-- *****************************************************************************************
REM
REM sv_sprmedi_ins_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 1. Horizon 
REM Generated trigger for Horizon API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER sprmedi_view_create_trg
  INSTEAD OF INSERT ON sv_sprmedi
DECLARE
  p_rowid_v VARCHAR2(100);
BEGIN
  gfksjpa.setId(:NEW.sprmedi_surrogate_id);
  gfksjpa.setVersion(:NEW.sprmedi_version);
  gb_medical.p_create
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
     p_rowid_out => p_rowid_v);
END;
/