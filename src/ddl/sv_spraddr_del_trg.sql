--
-- *****************************************************************************
-- *                                                                           *
-- * Copyright 2010 SunGard. All rights reserved.                              *
-- *                                                                           *
-- * SunGard or its subsidiaries in the U.S. and other countries is the owner  *
-- * of numerous marks, including "SunGard," the SunGard logo, "Banner,"       *
-- * "PowerCAMPUS," "Advance," "Luminis," "UDC," and "Unified Digital Campus." *
-- * Other names and marks used in this material are owned by third parties.   *
-- *                                                                           *
-- * This [site/software] contains confidential and proprietary information of *
-- * SunGard and its subsidiaries. Use of this [site/software] is limited to   *
-- * SunGard Higher Education licensees, and is subject to the terms and       *
-- * conditions of one or more written license agreements between SunGard      *
-- * Higher Education and the licensee in question.                            *
-- *                                                                           *
-- *****************************************************************************
--
-- sv_spraddr_del_trg.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 07/06/2011
--
--    Generated trigger for Horizon API support
--
-- AUDIT TRAIL END
--
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
