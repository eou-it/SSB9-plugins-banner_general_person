
-- *****************************************************************************************
-- * Copyright 2010-2013 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************
REM
REM spriden_080602_06.sql
REM
REM This script creates index for idname searching
REM
REM Project: Student id/name search for Banner General
REM AUDIT TRAIL: 8.6.2
REM 8/11/2013
REM 1. Added indexes for name and id searching.
REM function index
REM AUDIT TRAIL END
 --
-- Create the function based index on SPRIDEN_CHANGE_IND.
--
whenever sqlerror continue;
CREATE INDEX SPRIDEN_CHANGE_IND ON SPRIDEN (nvl(SPRIDEN_CHANGE_IND,'null'));
whenever sqlerror exit rollback;
