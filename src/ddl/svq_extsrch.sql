-- /*********************************************************************************
--  Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
--  ********************************************************************************* */
--  SVQ_EXTSRCH.sql
--
-- AUDIT TRAIL: 9.0
-- View for ExtendedWindow ID search Component - ID Search    shrridevis 16-JUL-2013
-- AUDIT TRAIL END
--

SET SCAN OFF;


CREATE OR REPLACE FORCE VIEW SVQ_EXTSRCH
   (ROW_COUNT,
   SURROGATE_ID,
   ID,
   BIRTH_DATE,
   DATA_ORIGIN,
   FIRST_NAME,
   ACTIVITY_DATE,
   USER_ID,
   LAST_NAME,
   MI,
   NAME_PREFIX,
   NAME_SUFFIX,
   PIDM,
   PREFERRED_FIRST_NAME,
   SURNAME_PREFIX,
   VERSION,
   CHANGE_INDICATOR,
   CURRENT_ID,
   CURRENT_FIRST_NAME,
   CURRENT_LAST_NAME,
   CURRENT_MI,
   CURRENT_SURNAME_PREFIX,
   ENTITY_INDICATOR,
   NAME_TYPE,
   SEARCH_FIRST_NAME,
   SEARCH_LAST_NAME,
   SEARCH_MI,
   SEX,
   SSN
)
AS
select /*+ RULE */  t.* from (select count(*) over() as Row_Count, personadv.surrogate_id,
         personadv.id, personadv.birth_date,
         personadv.data_origin, personadv.first_name, personadv.activity_date,
         personadv.user_id, personadv.last_name, personadv.mi, personadv.name_prefix, personadv.name_suffix,
         personadv.pidm, personadv.preferred_first_name, personadv.surname_prefix,
         personadv.version, personadv.change_indicator, personadv.current_id,
         personadv.current_first_name,
         personadv.current_last_name,
         personadv.current_mi,
         personadv.current_surname_prefix,
         personadv.entity_indicator,
         personadv.name_type,personadv.search_first_name
         , personadv.search_last_name,
         personadv.search_mi, personadv.sex,
         personadv.ssn from svq_sadvsrc personadv where
         (exists (select personidsrch.surrogate_id from svq_idsrch
         personidsrch where personidsrch.pidm=personadv.pidm)) order by
         personadv.current_last_name asc, personadv.current_first_name
         asc, personadv.current_surname_prefix asc, personadv.current_mi
         asc, personadv.current_id asc, personadv.change_indicator desc,
         personadv.pidm, personadv.change_indicator desc )t where rownum
         <= soknsut.f_get_page_size;

COMMENT ON TABLE SVQ_EXTSRCH IS 'View on Id search component';
COMMENT ON COLUMN SVQ_EXTSRCH.ROW_COUNT IS 'ROW COUNT: Total count of person which matches the search filter';
COMMENT ON COLUMN SVQ_EXTSRCH.SURROGATE_ID IS 'SURROGATE ID: Immutable unique key';
COMMENT ON COLUMN SVQ_EXTSRCH.ID IS 'ID: This field defines the identification number used to access person on-line.';
COMMENT ON COLUMN SVQ_EXTSRCH.BIRTH_DATE IS 'BIRTH DATE:This field maintains the birth date of the Person.';
COMMENT ON COLUMN SVQ_EXTSRCH.DATA_ORIGIN IS 'DATA ORIGIN: Source system that created or updated the data.';
COMMENT ON COLUMN SVQ_EXTSRCH.FIRST_NAME IS 'FIRST NAME: This field identifies the first name of person.';
COMMENT ON COLUMN SVQ_EXTSRCH.ACTIVITY_DATE IS 'ACTIVITY DATE: Date information was last created or modified.';
COMMENT ON COLUMN SVQ_EXTSRCH.USER_ID IS 'USER ID: The user ID of the person who inserted or last updated this record.';
COMMENT ON COLUMN SVQ_EXTSRCH.LAST_NAME IS 'LAST NAME: This field identifies the last name of person.';
COMMENT ON COLUMN SVQ_EXTSRCH.MI IS 'MI: This field identifies the middle name of person.';
COMMENT ON COLUMN SVQ_EXTSRCH.NAME_PREFIX IS 'NAME PREFIX: This field maintains the prefix (Mr, Mrs, etc) used before person name.';
COMMENT ON COLUMN SVQ_EXTSRCH.NAME_SUFFIX IS 'NAME SUFFIX:This field maintains the suffix (Jr, Sr, etc) used after person name.';
COMMENT ON COLUMN SVQ_EXTSRCH.PIDM IS 'PIDM:Internal identification number of the person.';
COMMENT ON COLUMN SVQ_EXTSRCH.PREFERRED_FIRST_NAME IS 'PREFERRED FIRST NAME: This field maintains the preferred first name associated with person.';
COMMENT ON COLUMN SVQ_EXTSRCH.SURNAME_PREFIX IS 'SURNAME PREFIX: This field maintains name tag preceding the last name or surname.(Van, Von, Mac, etc.).';
COMMENT ON COLUMN SVQ_EXTSRCH.VERSION IS 'VERSION: Optimistic lock token.';
COMMENT ON COLUMN SVQ_EXTSRCH.CHANGE_INDICATOR IS 'CHANGE INDICATOR: This field identifies whether type of change made to the record was an ID number change or a name change. Valid values: I - ID change, N - name change.';
COMMENT ON COLUMN SVQ_EXTSRCH.CURRENT_ID IS 'CURRENT ID: This field defines the current identification number used to access person on-line.';
COMMENT ON COLUMN SVQ_EXTSRCH.CURRENT_FIRST_NAME IS 'CURRENT_FIRST_NAME: This field identifies current the first name of person.';
COMMENT ON COLUMN SVQ_EXTSRCH.CURRENT_LAST_NAME IS 'CURRENT_LAST_NAME:  This field identifies the current last name of person.';
COMMENT ON COLUMN SVQ_EXTSRCH.CURRENT_MI IS 'CURRENT MI:  This field identifies current the middle name of person.';
COMMENT ON COLUMN SVQ_EXTSRCH.CURRENT_SURNAME_PREFIX IS 'CURRENT SURNAME PREFIX MI: This field maintains the name tag preceding the current last name or surname.(Van, Von, Mac, etc.).';
COMMENT ON COLUMN SVQ_EXTSRCH.ENTITY_INDICATOR IS 'ENTITY INDICATOR: This field identifies whether record is person or non-person record. Valid values:  P - person, C - non-person.';
COMMENT ON COLUMN SVQ_EXTSRCH.NAME_TYPE IS 'NAME_TYPE: This field is used to store the code that represents the name type associated with a person name.';
COMMENT ON COLUMN SVQ_EXTSRCH.SEARCH_FIRST_NAME IS 'SEARCH_FIRST_NAME: The First Name field with all spaces and punctuation removed and all letters capitalized.';
COMMENT ON COLUMN SVQ_EXTSRCH.SEARCH_LAST_NAME IS 'SEARCH_LAST_NAME: The Last Name field with all spaces and punctuation removed and all letters capitalized.';
COMMENT ON COLUMN SVQ_EXTSRCH.SEARCH_MI IS 'SEARCH_MI:The MI (Middle Initial) field with all spaces and punctuation removed and all letters capitalized.';
COMMENT ON COLUMN SVQ_EXTSRCH.SEX IS 'SEX: This field maintains the sex code.';
COMMENT ON COLUMN SVQ_EXTSRCH.SSN IS 'SSN:Internal identification number of the person in.';

CREATE OR REPLACE PUBLIC SYNONYM SVQ_EXTSRCH FOR SVQ_EXTSRCH;
SHOW ERRORS VIEW SVQ_EXTSRCH;
SET SCAN ON;
