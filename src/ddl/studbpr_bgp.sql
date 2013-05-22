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
start gv_goremal_ins_trg
start gv_goremal_upd_trg
start gv_goremal_del_trg
REM
start sv_sorpcol_ins_trg
start sv_sorpcol_upd_trg
start sv_sorpcol_del_trg
REM
start sv_sorconc_ins_trg
start sv_sorconc_upd_trg
start sv_sorconc_del_trg
REM
start sv_sordegr_ins_trg
start sv_sordegr_upd_trg
start sv_sordegr_del_trg
REM
start sv_sormajr_ins_trg
start sv_sormajr_upd_trg
start sv_sormajr_del_trg
REM
start sv_sorminr_ins_trg
start sv_sorminr_upd_trg
start sv_sorminr_del_trg
REM
REM Create procedures
REM
start soknsut
start soknsut1
REM