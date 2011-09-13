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


CREATE OR REPLACE TRIGGER spriden_view_delete_trg
  INSTEAD OF DELETE ON sv_spriden
BEGIN
  gb_identification.p_delete
    (p_pidm => :OLD.spriden_pidm,
     p_id => :OLD.spriden_id,
     p_last_name => :OLD.spriden_last_name,
     p_first_name => :OLD.spriden_first_name,
     p_mi => :OLD.spriden_mi,
     p_change_ind => :OLD.spriden_change_ind,
     p_ntyp_code => :OLD.spriden_ntyp_code,
     p_surname_prefix => :OLD.spriden_surname_prefix,
     p_rowid => :OLD.spriden_v_rowid);
END;
/
