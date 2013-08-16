--
-- dbeu_table_extends.sql
--
-- V8.1
--
-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************


REM
REM dbeu_ext_stu_bgp.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 1. Horizon 
REM AUDIT TRAIL END 
REM
REM
whenever oserror exit rollback;
whenever sqlerror exit rollback;
REM connect dbeu_owner/&&dbeu_password
REM
execute dbeu_util.extend_table('SATURN','SPRADDR','S',TRUE);
execute dbeu_util.extend_table('SATURN','SPRHOLD','S',TRUE);
execute dbeu_util.extend_table('SATURN','SPRIDEN','S',TRUE);
execute dbeu_util.extend_table('SATURN','SPRMEDI','S',TRUE);
execute dbeu_util.extend_table('SATURN','SPRTELE','S',TRUE);
execute dbeu_util.extend_table('GENERAL','GOREMAL','G',TRUE);