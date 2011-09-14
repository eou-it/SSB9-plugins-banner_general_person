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




CREATE OR REPLACE FORCE VIEW sv_sprmedi AS SELECT
      sprmedi_pidm,
      sprmedi_medi_code,
      sprmedi_mdeq_code,
      sprmedi_comment,
      sprmedi_disa_code,
      sprmedi_spsr_code,
      sprmedi_onset_age,
      sprmedi_disb_ind,
      sprmedi_medi_code_date,
      sprmedi_surrogate_id,
      sprmedi_version,
      sprmedi_user_id,
      sprmedi_data_origin,
      sprmedi_activity_date,
      ROWID sprmedi_v_rowid
  FROM saturn.sprmedi;
CREATE OR REPLACE PUBLIC SYNONYM sv_sprmedi FOR sv_sprmedi;
