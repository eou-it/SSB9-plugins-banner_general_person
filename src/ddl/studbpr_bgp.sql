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
REM studbpr_bgp.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 1. Horizon 
REM AUDIT TRAIL END 
REM
REM
REM Create views.
REM
whenever oserror exit rollback;
whenever sqlerror exit rollback;
REM connect baninst1/&&baninst1_password
REM
REM Create triggers
REM
start sv_spraddr_ins_trg
start sv_spraddr_upd_trg
start sv_spraddr_del_trg
REM
start sv_sprhold_del_trg
start sv_sprhold_ins_trg
start sv_sprhold_upd_trg
REM
start sv_spriden_del_trg
start sv_spriden_ins_trg
start sv_spriden_upd_trg
REM
start sv_sprmedi_del_trg
start sv_sprmedi_ins_trg
start sv_sprmedi_upd_trg
REM
start sv_sprtele_ins_trg
start sv_sprtele_upd_trg
start sv_sprtele_del_trg
REM
REM Create procedures
REM
start soknsut
start soknsut1
REM