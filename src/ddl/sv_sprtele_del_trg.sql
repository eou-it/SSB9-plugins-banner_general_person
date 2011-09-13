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
