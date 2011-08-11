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
