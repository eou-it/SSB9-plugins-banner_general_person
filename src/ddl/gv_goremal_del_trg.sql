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
-- gv_goremal_del_trg.sql
--
-- AUDIT TRAIL: 8.x
-- DBEU 08/08/2011
--
--    Generated trigger for Horizon API support
--
-- AUDIT TRAIL END
--
CREATE OR REPLACE TRIGGER goremal_view_delete_trg
  INSTEAD OF DELETE ON gv_goremal
BEGIN
  gb_email.p_delete
    (p_pidm => :OLD.goremal_pidm,
     p_emal_code => :OLD.goremal_emal_code,
     p_email_address => :OLD.goremal_email_address,
     p_rowid => :OLD.goremal_v_rowid);
END;
/
show errors
