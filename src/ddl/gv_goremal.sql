--
-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************


REM
REM gv_goremal.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 08/08/2011
REM 1. Horizon 
REM Generated view for Horizon API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE FORCE VIEW gv_goremal AS SELECT
      goremal_pidm,
      goremal_emal_code,
      goremal_email_address,
      goremal_status_ind,
      goremal_preferred_ind,
      goremal_comment,
      goremal_disp_web_ind,
      goremal_surrogate_id,
      goremal_version,
      goremal_user_id,
      goremal_data_origin,
      goremal_activity_date,
      ROWID goremal_v_rowid
  FROM goremal;
REM
CREATE OR REPLACE PUBLIC SYNONYM gv_goremal FOR gv_goremal;