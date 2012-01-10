--
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
REM sv_spraddr_del_trg.sql
REM 
REM AUDIT TRAIL: 9.0 
REM 07/06/2011
REM 1. Horizon 
REM Generated trigger for Horizon API support
REM AUDIT TRAIL END 
REM
CREATE OR REPLACE TRIGGER spraddr_view_delete_trg
  INSTEAD OF DELETE ON sv_spraddr
BEGIN
  gb_address.p_delete
    (p_pidm => :OLD.spraddr_pidm,
     p_atyp_code => :OLD.spraddr_atyp_code,
     p_seqno => :OLD.spraddr_seqno,
     p_status_ind => :OLD.spraddr_status_ind,
     p_rowid => :OLD.spraddr_v_rowid);
END;
/
show errors