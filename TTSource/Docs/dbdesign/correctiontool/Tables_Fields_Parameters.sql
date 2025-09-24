-- Added by nithya on 19-06-2024 -- Version 1.0 - Correction Tool - Start

/* Tables and fields */

-- Log table for data corrections

CREATE TABLE data_correction_log (
	branch_code varchar(4) NULL,
	correction_dt timestamp(6) NULL,
	trans_dt date NULL,
	trans_id varchar(20) NULL,
	batch_id varchar(20) NULL,
	act_num varchar(20) NULL,
	modification_type varchar(40) NULL,
	old_field_vale varchar(100) NULL,
	new_field_vlaue varchar(100) NULL,
	old_status_by varchar(20) NULL,
	old_authorized_by varchar(20) NULL,
	correction_user_id varchar(20) NULL,
	currection_authorize_staff varchar(20) NULL,
	currection_authorize_date timestamp(6) NULL,
	remarks varchar(200) NULL
);

-- Screen updations & group screen insertion
Insert into SCREEN_MASTER
   (SCREEN_ID, MENU_ID, SCREEN_NAME, APP_ID, WF_STATUS, 
    MODULE_ID, SCREEN_CLASS, SL_NO, STATUS, SCREEN_TYPE, 
    RECORD_KEY_COL, SCREEN_DESC)
 Values
   ('SCR03043', '3042', 'Data Correction', 'APP01', 'DONE', 
    '5', NULL, 5, 'CREATED', NULL, 
    NULL, NULL);
    
call INSERT_REPORT_GROUP('SCR03043')

  
-- Parametrizing the screen usage restrictions and user restrictions

alter table PARAMETERS ADD CORRECTION_DURATION_MONTHS integer default 0;

alter table user_master add CORRECTION_ALLOWED VARCHAR DEFAULT 'N';


-- Tags for data correction -- For drop down

Insert into LOOKUP_MASTER
   (LOOKUP_ID, LOOKUP_REF_ID, LOOKUP_DESC, STATUS, EDITABLE, 
    AUTHORIZED)
 Values
   ('DATA.CORRECTION_TYPE', 'NARRATION_CHANGE', 'Particulars Change', 'CREATED', NULL, 
    'Y');
Insert into LOOKUP_MASTER
   (LOOKUP_ID, LOOKUP_REF_ID, LOOKUP_DESC, STATUS, EDITABLE, 
    AUTHORIZED)
 Values
   ('DATA.CORRECTION_TYPE', 'HEAD_CHANGE', 'Account Head Change', 'CREATED', NULL, 
    'Y');
Insert into LOOKUP_MASTER
   (LOOKUP_ID, LOOKUP_REF_ID, LOOKUP_DESC, STATUS, EDITABLE, 
    AUTHORIZED)
 Values
   ('DATA.CORRECTION_TYPE', 'GOLD_ITEM_CHANGE', 'Gold Item Change', 'CREATED', NULL, 
    'Y');
Insert into LOOKUP_MASTER
   (LOOKUP_ID, LOOKUP_REF_ID, LOOKUP_DESC, STATUS, EDITABLE, 
    AUTHORIZED)
 Values
   ('DATA.CORRECTION_TYPE', 'GOLD_WEIGHT_CHANGE', 'Gold Weight Change', 'CREATED', NULL, 
    'Y');
Insert into LOOKUP_MASTER
   (LOOKUP_ID, LOOKUP_REF_ID, LOOKUP_DESC, STATUS, EDITABLE, 
    AUTHORIZED)
 Values
   ('DATA.CORRECTION_TYPE', 'OA_ACTNO_CHANGE', 'Operative Account Number Change', 'CREATED', NULL, 
    'Y');
Insert into LOOKUP_MASTER
   (LOOKUP_ID, LOOKUP_REF_ID, LOOKUP_DESC, STATUS, EDITABLE, 
    AUTHORIZED)
 Values
   ('DATA.CORRECTION_TYPE', 'TRANS_TYPE_INTERCHANGE', 'Transaction Type Inter Change', 'CREATED', NULL, 
    'Y');
Insert into LOOKUP_MASTER
   (LOOKUP_ID, LOOKUP_REF_ID, LOOKUP_DESC, STATUS, EDITABLE, 
    AUTHORIZED)
 Values
   ('DATA.CORRECTION_TYPE', 'OTHERBANK_ACTNO_CHANGE', 'Other Bank Account Number Change', 'CREATED', NULL, 
    'Y');
Insert into LOOKUP_MASTER
   (LOOKUP_ID, LOOKUP_REF_ID, LOOKUP_DESC, STATUS, EDITABLE, 
    AUTHORIZED)
 Values
   ('DATA.CORRECTION_TYPE', 'SUSPENSE_ACTNO_CHANGE', 'Suspense Account Number Change', 'CREATED', NULL, 
    'Y');
Insert into LOOKUP_MASTER
   (LOOKUP_ID, LOOKUP_REF_ID, LOOKUP_DESC, STATUS, EDITABLE, 
    AUTHORIZED)
 Values
   ('DATA.CORRECTION_TYPE', 'TRANS_AMT_CHANGE', 'Transaction Amount Change', 'CREATED', NULL, 
    'Y');
	
-- Added by nithya on 19-06-2024 -- Version 1.0 - Correction Tool - End	