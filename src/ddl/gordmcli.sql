REM
REM gordmcli.sql
REM
REM AUDIT TRAIL: 8.5
REM AK 09/01/2009
REM 1) New column surname prefix is added to few query
REM forms in banner 9 and then are available for masking
REM by inserting into GORDMCL
REM AUDIT TRAIL END
REM
set echo on
set scan off
REM

insert into GORDMCL
(GORDMCL_OBJS_CODE,
GORDMCL_BLOCK_NAME,
GORDMCL_COLUMN_NAME,
GORDMCL_SYS_REQ_IND,
GORDMCL_DATA_TYPE_CDE,
GORDMCL_DATA_LENGTH,
GORDMCL_ACTIVITY_DATE,
GORDMCL_USER_ID,
GORDMCL_QUERY_COLUMN,
GORDMCL_NUMERIC_PRECISION)
SELECT 'SOAIDEN',
'SPRIDEN',
'SURNAME_PREFIX',
'N',
'C',
60,
sysdate,
'BASELINE',
NULL,
NULL FROM DUAL
     WHERE NOT EXISTS
    (SELECT 1 FROM GORDMCL WHERE GORDMCL_OBJS_CODE = 'SOAIDEN' and GORDMCL_BLOCK_NAME='SPRIDEN' and  GORDMCL_COLUMN_NAME='SURNAME_PREFIX');


insert into GORDMCL
(GORDMCL_OBJS_CODE,
GORDMCL_BLOCK_NAME,
GORDMCL_COLUMN_NAME,
GORDMCL_SYS_REQ_IND,
GORDMCL_DATA_TYPE_CDE,
GORDMCL_DATA_LENGTH,
GORDMCL_ACTIVITY_DATE,
GORDMCL_USER_ID,
GORDMCL_QUERY_COLUMN,
GORDMCL_NUMERIC_PRECISION)
SELECT 'GUIALTI',
'GUVALTI',
'SURNAME_PREFIX',
'N',
'C',
60,
sysdate,
'BASELINE',
NULL,
NULL FROM DUAL
     WHERE NOT EXISTS
    (SELECT 1 FROM GORDMCL WHERE GORDMCL_OBJS_CODE = 'GUIALTI' and GORDMCL_BLOCK_NAME='GUVALTI' and  GORDMCL_COLUMN_NAME='SURNAME_PREFIX');


insert into GORDMCL
(GORDMCL_OBJS_CODE,
GORDMCL_BLOCK_NAME,
GORDMCL_COLUMN_NAME,
GORDMCL_SYS_REQ_IND,
GORDMCL_DATA_TYPE_CDE,
GORDMCL_DATA_LENGTH,
GORDMCL_ACTIVITY_DATE,
GORDMCL_USER_ID,
GORDMCL_QUERY_COLUMN,
GORDMCL_NUMERIC_PRECISION)
SELECT 'GUISRCH',
'GUISRCH',
'SURNAME_PREFIX',
'N',
'C',
60,
sysdate,
'BASELINE',
NULL,
NULL  FROM DUAL
     WHERE NOT EXISTS
    (SELECT 1 FROM GORDMCL WHERE GORDMCL_OBJS_CODE = 'GUISRCH' and GORDMCL_BLOCK_NAME='GUISRCH' and  GORDMCL_COLUMN_NAME='SURNAME_PREFIX');
REM
SET echo off
SET scan on

