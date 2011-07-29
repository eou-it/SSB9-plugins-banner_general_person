--
--  Create views.
--
whenever oserror exit rollback;
whenever sqlerror exit rollback;
REM connect baninst1/&&baninst1_password
REM
REM Create triggers
REM

start sv_spraddr_ins_trg
start sv_spraddr_upd_trg
start sv_spraddr_del_trg

start sv_sprhold_del_trg
start sv_sprhold_ins_trg
start sv_sprhold_upd_trg

start sv_spriden_del_trg
start sv_spriden_ins_trg
start sv_spriden_upd_trg

start sv_sprmedi_del_trg
start sv_sprmedi_ins_trg
start sv_sprmedi_upd_trg

start sv_sprtele_ins_trg
start sv_sprtele_upd_trg
start sv_sprtele_del_trg
