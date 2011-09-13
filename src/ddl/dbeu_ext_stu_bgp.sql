--
-- dbeu_table_extends.sql
--
-- V8.1
--
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
whenever oserror exit rollback;
whenever sqlerror exit rollback;
REM connect dbeu_owner/&&dbeu_password

execute dbeu_util.extend_table('SATURN','SPRADDR','S',TRUE);
execute dbeu_util.extend_table('SATURN','SPRHOLD','S',TRUE);
execute dbeu_util.extend_table('SATURN','SPRIDEN','S',TRUE);
execute dbeu_util.extend_table('SATURN','SPRMEDI','S',TRUE);
execute dbeu_util.extend_table('SATURN','SPRTELE','S',TRUE);
-- keeping this in this file for now; will break them up later
execute dbeu_util.extend_table('GENERAL','GOREMAL','G',TRUE);

