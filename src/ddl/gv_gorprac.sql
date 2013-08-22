-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
--
-- gv_gorprac.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 06/04/2013
--
--    Generated view for Banner XE API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE FORCE VIEW gv_gorprac AS SELECT
      gorprac_pidm,
      gorprac_race_cde,
      gorprac_surrogate_id,
      gorprac_version,
      gorprac_user_id,
      gorprac_data_origin,
      gorprac_activity_date,
      ROWID gorprac_v_rowid
  FROM gorprac;
--
CREATE OR REPLACE PUBLIC SYNONYM gv_gorprac FOR gv_gorprac;
