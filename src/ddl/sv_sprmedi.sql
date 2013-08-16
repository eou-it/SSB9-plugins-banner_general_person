-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************


REM
REM sv_sprmedi.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 1. Horizon 
REM Generated view for Horizon API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE FORCE VIEW sv_sprmedi AS SELECT
      sprmedi_pidm,
      sprmedi_medi_code,
      sprmedi_mdeq_code,
      sprmedi_comment,
      sprmedi_disa_code,
      sprmedi_spsr_code,
      sprmedi_onset_age,
      sprmedi_disb_ind,
      sprmedi_medi_code_date,
      sprmedi_surrogate_id,
      sprmedi_version,
      sprmedi_user_id,
      sprmedi_data_origin,
      sprmedi_activity_date,
      ROWID sprmedi_v_rowid
  FROM sprmedi;
REM
CREATE OR REPLACE PUBLIC SYNONYM sv_sprmedi FOR sv_sprmedi;