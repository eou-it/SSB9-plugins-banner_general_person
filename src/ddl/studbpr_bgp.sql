-- *****************************************************************************************
-- * Copyright 2009-2013 Ellucian Company L.P. and its affiliates.                         *
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
REM   SV_SPRIDEN SHOULD NOT HAVE AN "INSTEAD OF UPDATE" TRIGGER
REM
start sv_spriden_cur_del_trg
start sv_spriden_cur_ins_trg
REM   SV_SPRIDEN_CURRENT SHOULD NOT HAVE AN "INSTEAD OF UPDATE" TRIGGER
REM
start sv_spriden_alt_del_trg
start sv_spriden_alt_ins_trg
REM   SV_SPRIDEN_ALTERNATE SHOULD NOT HAVE AN "INSTEAD OF UPDATE" TRIGGER
REM
start sv_sprmedi_del_trg
start sv_sprmedi_ins_trg
start sv_sprmedi_upd_trg
REM
start sv_sprtele_ins_trg
start sv_sprtele_upd_trg
start sv_sprtele_del_trg
REM
start gv_goremal_ins_trg
start gv_goremal_upd_trg
start gv_goremal_del_trg
REM
start gv_gorprac_ins_trg
start gv_gorprac_upd_trg
start gv_gorprac_del_trg
REM
start sv_spbpers_ins_trg
start sv_spbpers_upd_trg
start sv_spbpers_del_trg
REM
start gv_goradid_ins_trg
start gv_goradid_upd_trg
start gv_goradid_del_trg
REM
start sv_spremrg_ins_trg
start sv_spremrg_upd_trg
start sv_spremrg_del_trg
REM
REM Create procedures
REM
start soknsut
start soknsut1
REM