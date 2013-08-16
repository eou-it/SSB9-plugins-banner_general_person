-- *****************************************************************************************
-- * Copyright 2013 Ellucian Company L.P. and its affiliates.                              *
-- *****************************************************************************************


REM AUDIT TRAIL: 5.3.0.1
REM 1. JWM 10/12/01
REM    GURGRTB.SQL
REM    New script, introduced as part of the Oct. 12 Banner Security Patch. 
REM    The purpose of this script is to grant EXECUTE privilege on the
REM    BANINST1-owned stored procedure passed as the first argument to each of
REM    the Banner schema owners and roles. Clients may modify this file to add
REM    additional schema owners or roles, or to remove any that are not
REM    applicable to the local environment.
REM
REM AUDIT TRAIL END   
REM
SET SCAN ON VERIFY OFF
REM
REM Following are Banner schema owners; if you have additional owners which 
REM need to have execute privilege on BANINST1 objects (e.g., in order to
REM create table triggers) then add them here.
REM
grant execute on &&1 to ALUMNI;
grant execute on &&1 to BANIMGR;
grant execute on &&1 to FAISMGR;
grant execute on &&1 to FIMSMGR;
grant execute on &&1 to GENERAL;
grant execute on &&1 to PAYROLL;
grant execute on &&1 to POSNCTL;
grant execute on &&1 to SATURN;
grant execute on &&1 to TAISMGR;
grant execute on &&1 to WFAUTO;
grant execute on &&1 to WTAILOR;
REM
REM Following are Banner roles; if you have additional Oracle roles which 
REM need to be able to execute BANINST1 objects, add them here.
REM
grant execute on &&1 to BAN_DEFAULT_M;
grant execute on &&1 to BAN_DEFAULT_Q;
