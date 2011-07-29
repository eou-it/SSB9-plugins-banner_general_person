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
-- sv_sprtele_del_trg.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 06/30/2011
--
--    Generated trigger for Horizon API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER sprtele_view_delete_trg
  INSTEAD OF DELETE ON sv_sprtele
BEGIN
  gb_telephone.p_delete
    (p_pidm => :OLD.sprtele_pidm,
     p_tele_code => :OLD.sprtele_tele_code,
     p_seqno => :OLD.sprtele_seqno,
     p_rowid => :OLD.sprtele_v_rowid);
END;
/
show errors
