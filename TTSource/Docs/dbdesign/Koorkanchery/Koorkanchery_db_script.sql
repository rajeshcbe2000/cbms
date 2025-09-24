--Start Version  [Script Version - 0.0.01] [ReleaseVersion - 9.2.2.4][18-Dec-2015] -- by  Suresh R
CREATE TABLE CARD_ACCT_STATUS
(
  CARD_ACCT_NUM      VARCHAR2(36 BYTE),
  SL_NO              NUMBER(5),
  ACTION             VARCHAR2(6 BYTE),
  ACTION_DT          DATE,
  REMARKS            VARCHAR2(250 BYTE),
  STATUS             VARCHAR2(16 BYTE),
  STATUS_BY          VARCHAR2(32 BYTE),
  STATUS_DT          DATE,
  AUTHORIZED_STATUS  VARCHAR2(32 BYTE),
  AUTHORIZED_BY      VARCHAR2(64 BYTE),
  AUTHORIZED_DT      DATE
);
ALTER TABLE ACT_MASTER ADD CARD_ACCT_NUM VARCHAR2(36 BYTE);
--End Version  [Script Version - 0.0.01] [ReleaseVersion - 9.2.2.4][18-Dec-2015] -- by  Suresh R
