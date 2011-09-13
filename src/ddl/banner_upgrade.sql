-- **************************************************************************************
-- * Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.                 *
-- * This copyrighted software contains confidential and proprietary information of     *
-- * SunGard Higher Education and its subsidiaries. Any use of this software is limited *
-- * solely to SunGard Higher Education licensees, and is further subject to the terms  *
-- * and conditions of one or more written license agreements between SunGard Higher    *
-- * Education and the licensee in question. SunGard, Banner and Luminis are either     *
-- * registered trademarks or trademarks of SunGard Higher Education in the U.S.A.      *
-- * and/or other regions and/or countries.                                             *
-- **************************************************************************************


--
--  Main common project schema maintenance script.
--

set scan on echo on termout on;

REM spool horizon_upgrade.lis


connect dbeu_owner/&&dbeu_password

start dbeu_ext_stu_bgp


connect baninst1/&&baninst1_password

start stuview_bgp


connect baninst1/&&baninst1_password
start studbpr_bgp

spool off;



select * from all_objects where status = 'INVALID'

conn sys/u_pick_it as sysdba
execute utl_recomp.recomp_parallel();
