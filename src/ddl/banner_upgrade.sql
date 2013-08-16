-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************


REM
REM banner_upgrade.sql
REM
REM AUDIT TRAIL: 9.0
REM 1. Horizon
REM Main common project schema maintenance script.
REM AUDIT TRAIL END
REM
set scan on echo on termout on;
REM
spool horizon_upgrade.lis
REM
connect dbeu_owner/&&dbeu_password
REM
start dbeu_ext_stu_bgp
REM
connect baninst1/&&baninst1_password
REM
start stuview_bgp
REM
connect baninst1/&&baninst1_password
start studbpr_bgp
commit;
REM
REM Recompile invalid objects
REM
conn sys/u_pick_it as sysdba
execute utl_recomp.recomp_parallel();
start showinv
spool off;
