-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************

CREATE OR REPLACE FORCE VIEW GV_GORADID AS SELECT
      GORADID_PIDM,
      GORADID_ADDITIONAL_ID,
      GORADID_ADID_CODE,
      GORADID_USER_ID,
      GORADID_ACTIVITY_DATE,
      GORADID_DATA_ORIGIN,
      GORADID_SURROGATE_ID,
      GORADID_VERSION,
      GORADID_VPDI_CODE,
      ROWID GORADID_V_ROWID
  FROM GORADID;
--
CREATE OR REPLACE PUBLIC SYNONYM GV_GORADID FOR GV_GORADID;