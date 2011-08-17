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
