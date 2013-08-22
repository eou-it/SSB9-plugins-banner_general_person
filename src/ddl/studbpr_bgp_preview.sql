-- *****************************************************************************************
-- * Copyright 2009-2013 Ellucian Company L.P. and its affiliates.                         *
-- *****************************************************************************************
REM
REM studbpr_bgp_preview.sql
REM
REM AUDIT TRAIL: 9.0
REM 1. Banner XE
REM AUDIT TRAIL END
REM
REM
REM Create views.
REM
whenever oserror exit rollback;
whenever sqlerror exit rollback;
REM
REM Create triggers needed for views
REM
start soknsut
start soknsut1
 