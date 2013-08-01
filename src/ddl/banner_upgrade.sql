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
REM index may have accidently been created with baninst1
connect baninst1/&&baninst1_password
start spriden_fti_teardown.sql
connect saturn/&&saturn_password
start spriden_fti_teardown.sql
connect saturn/&&saturn_password
start create_spriden_fti.sql

REM  Create package needed for view 
connect baninst1/&&baninst1_password
start studbpr_bgp_preview
REM
connect baninst1/&&baninst1_password
REM
start stuview_bgp
REM 

connect baninst1/&&baninst1_password
start studbpr_bgp
start svq_nameparams
start svq_advsrch
commit;

REM
REM Recompile invalid objects
REM
conn sys/u_pick_it as sysdba
execute utl_recomp.recomp_parallel();
start showinv
spool off;
