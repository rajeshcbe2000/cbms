/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * CommonConstants.java
 *
 * Created on June 18, 2003, 4:27 PM
 */
package com.see.truetransact.commonutil;

import com.see.truetransact.transferobject.sysadmin.config.ConfigPasswordTO;

/**
 * Common Constants which is used in Serverside as well as Clientside.
 *
 * @author balachandar
 */
public class CommonConstants {

    private static java.util.Properties serverProperties = new java.util.Properties();
    public static String SERVER_IP_ADDR; // Server IP Address or Machine Name
    public static String PROVIDER_URL; // JNP ejb provider URL
    public static String TIME_OUT; // Time Out parameter
    //The following Parameter has to be set at the time of New Branch Implementation
    public static String OPERATE_MODE;
    public static String TOLERANCE_AMT;
    // The following Parameters has to be set properly for Mobile Banking
    public static String MOBILE_BANKING;
    public static String MOBILE_BANKING_USERNAME;
    public static String MOBILE_BANKING_PWD;
    public static String MOBILE_BANKING_SENDERID;
    public static String SAL_REC_MODULE;
    public static String SMS_SERVER;
    public static String BANK_TYPE;
    public static String DB_DRIVER_NAME = "DB_DRIVER_NAME";
    public static String SESSION_ID = "SESSION_ID";
    public static String VENDOR;
    public static String SERVER_PATH;
    public static String HEAD_OFFICE = "HEAD_OFFICE";
    public static String BANK_SMS_DESCRIPTION;
    public static String REPORT_TEMPLATE;
    public static String PACS_ID;
    public static String SFTP_IP_ADDRESS;
    public static String SFTP_USER_ID;
    public static String SFTP_PASSWORD;
    public static String SFTP_PORT;
    public static String NEFT_OUTWARD_N06_LOCAL;
    public static String NEFT_OUTWARD_N06;
    public static String NEFT_INWARD_N02_LOCAL;
    public static String NEFT_INWARD_N02;
    public static String NEFT_OUTWARD_N27_LOCAL;
    public static String NEFT_OUTWARD_N27;
    //RTGS
    public static String RTGS_OUTWARD_R41_R42_LOCAL;
    public static String RTGS_OUTWARD_R41_R42;
    public static String RTGS_BANK_NAME;
    public static String RTGS_NEFT_CLIENT_CODE;
    public static String RTGS_NEFT_FORMAT_CODE;
    public static String RTGS_NEFT_INWARD_PATH;
    public static String RTGS_NEFT_INWARD_BACKUP_PATH;
    public static String RTGS_NEFT_OUTWARD_PATH;
    public static String RTGS_NEFT_OUTWARD_BACKUP_PATH;
    public static String RTGS_NEFT_PAYMENT_CODE;
    public static String RTGS_NEFT_ECOLL_CODE;
    public static String RTGS_NEFT_DR_ACT_NUM;
    public static String RTGS_NEFT_OUTWARD_MOVED_NW_PATH;
    
    //AXIS BANK DETAILS
    public static String AXIS_RTGS_NEFT_INWARD_PATH;
    public static String AXIS_RTGS_NEFT_INWARD_BACKUP_PATH;
    public static String AXIS_RTGS_NEFT_INWARD_RETURN_PATH;
    public static String AXIS_RTGS_NEFT_INWARD_BACKUP_RETURN_PATH;
    public static String AXIS_RTGS_NEFT_OUTWARD_PATH;
    public static String AXIS_CHANNEL_IDENTIFIER;
    public static String AXIS_CORPORATE_IDENTIFIER;
    public static String AXIS_USER_ID_UPLOADER;
    public static String AXIS_USER_ID_AUTHORIZER_1;
    public static String AXIS_USER_ID_AUTHORIZER_2;
    public static String AXIS_USER_ID_AUTHORIZER_3;
    public static String AXIS_USER_ID_AUTHORIZER_4;
    public static String AXIS_USER_ID_AUTHORIZER_5;
    public static String AXIS_CORPORATE_PRODUCT_CODE;
    public static String ELECTRONIC_TYPE_ATM;
    public static String ELECTRONIC_TYPE_RTGSNEFT;
    public static String AXIS_RTGS_NEFT_DR_ACT_NUM;
    //ATM FOR KERALA
    public static String ATM_OUR_INWARD_STOP_ACK;
    public static String ATM_OUR_INWARD_TOPUP_ACK;
    public static String ATM_OUR_INWARD_TRANSACTION;
    public static String ATM_OUR_OUTWARD_STOPAC;
    public static String ATM_OUR_OUTWARD_TOPUP;
    public static String ATM_OUR_OUTWARD_TRANSACTION_ACK;
    public static String ATM_OUR_OUTWARD_TRANSACTION_REPORT;
    
    public static String ATM_THEIR_INWARD_STOP_ACK;
    public static String ATM_THEIR_INWARD_TOPUP_ACK;
    public static String ATM_THEIR_INWARD_TRANSACTION;
    public static String ATM_THEIR_OUTWARD_STOPAC;
    public static String ATM_THEIR_OUTWARD_TOPUP;
    public static String ATM_THEIR_OUTWARD_TRANSACTION_ACK;
    public static String ATM_THEIR_INWARD_TRANSACTION_ARCHIVE;
    public static String ATM_OUR_BACKUP_INWARD_STOP_ACK;
    public static String ATM_OUR_BACKUP_INWARD_TOPUP_ACK;
    public static String ATM_OUR_BACKUP_INWARD_TRANSACTION;
    public static String ATM_OUR_BACKUP_OUTWARD_STOPAC;
    public static String ATM_OUR_BACKUP_OUTWARD_TOPUP;
    public static String ATM_OUR_BACKUP_OUTWARD_TRANSACTION_ACK;

//    public static String ATM_THEIR_BACKUP_INWARD_STOP_ACK;
    public static String ATM_THEIR_BACKUP_INWARD_TOPUP_ACK;
//    public static String ATM_THEIR_BACKUP_INWARD_TRANSACTION;
//    public static String ATM_THEIR_BACKUP_OUTWARD_STOPAC;
//    public static String ATM_THEIR_BACKUP_OUTWARD_TOPUP;
//    public static String ATM_THEIR_BACKUP_OUTWARD_TRANSACTION_ACK;
    
    public static String ICICI_RTGS_NEFT_OUTWARD_PATH;
    public static String ICICI_RTGS_NEFT_OUTWARD_MOVED_NW_PATH;
    public static String ICICI_RTGS_NEFT_OUTWARD_BACKUP_PATH;
    public static String ICICI_RTGS_NEFT_INWARD_PATH;
    public static String ICICI_RTGS_NEFT_INWARD_MOVED_NW_PATH;
    public static String ICICI_RTGS_NEFT_INWARD_BACKUP_PATH;
    
    //For JBoss 7.1.1
    public static String SECURITY_PRINCIPAL;
    public static String SECURITY_CREDENTIALS;

    static {
        try {
            Dummy cons = new Dummy();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/commonutil/TT.properties"));
            SERVER_IP_ADDR = serverProperties.getProperty("APPL_URL");
            TIME_OUT = serverProperties.getProperty("TIME_OUT");
//            PROVIDER_URL = "jnp://" + SERVER_IP_ADDR + ":" + serverProperties.getProperty("JNP_PORT") + "/";
//            Naming URL for JBoss 7.1.1 should be as follows
            //PROVIDER_URL = "remote://" + SERVER_IP_ADDR + ":" + serverProperties.getProperty("JNP_PORT");
            PROVIDER_URL = "http-remoting://" + CommonConstants.SERVER_IP_ADDR + ":" + CommonConstants.serverProperties.getProperty("HTTP_PORT");
//            PROVIDER_URL = "http-remoting://" + SERVER_IP_ADDR + ":" + serverProperties.getProperty("HTTP_PORT");
            SECURITY_PRINCIPAL = serverProperties.getProperty("SECURITY_PRINCIPAL");
            SECURITY_CREDENTIALS = serverProperties.getProperty("SECURITY_CREDENTIALS");
            
            /* The following Constant has been set for identifying whether 
             * New Branch Implementation or Not. For this OPERATE_MODE property
             * has to be set in TT.properties file 
             * if live mode set -> OPERATE_MODE = LIVE
             * if implementation mode set -> OPERATE_MODE = IMPLEMENTATION
             */
            OPERATE_MODE = serverProperties.getProperty("OPERATE_MODE");
            TOLERANCE_AMT = serverProperties.getProperty("TOLERANCE_AMT");
            MOBILE_BANKING = serverProperties.getProperty("MOBILE_BANKING");
            MOBILE_BANKING_USERNAME = serverProperties.getProperty("MOBILE_BANKING_USERNAME");
            MOBILE_BANKING_PWD = serverProperties.getProperty("MOBILE_BANKING_PWD");
            MOBILE_BANKING_SENDERID = serverProperties.getProperty("MOBILE_BANKING_SENDERID");
            SAL_REC_MODULE = CommonUtil.convertObjToStr(serverProperties.getProperty("SAL_REC_MODULE"));
            BANK_TYPE = CommonUtil.convertObjToStr(serverProperties.getProperty("BANK_TYPE"));
            SMS_SERVER = CommonUtil.convertObjToStr(serverProperties.getProperty("SMS_SERVER"));
            VENDOR = CommonUtil.convertObjToStr(serverProperties.getProperty("VENDOR"));

            SERVER_PATH = CommonUtil.convertObjToStr(serverProperties.getProperty("SERVER_PATH"));
            BANK_SMS_DESCRIPTION = CommonUtil.convertObjToStr(serverProperties.getProperty("BANK_SMS_DESCRIPTION"));
            REPORT_TEMPLATE = CommonUtil.convertObjToStr(serverProperties.getProperty("REPORT_TEMPLATE"));
            PACS_ID = CommonUtil.convertObjToStr(serverProperties.getProperty("PACS_ID"));
            SFTP_IP_ADDRESS = CommonUtil.convertObjToStr(serverProperties.getProperty("SFTP_SERVER_IP_ADDRESS"));
            SFTP_USER_ID = CommonUtil.convertObjToStr(serverProperties.getProperty("SFTP_SERVER_USER_NAME"));
            SFTP_PASSWORD = CommonUtil.convertObjToStr(serverProperties.getProperty("SFTP_SERVER_PASSWORD"));
            SFTP_PORT = CommonUtil.convertObjToStr(serverProperties.getProperty("SFTP_SERVER_PORT"));
            NEFT_OUTWARD_N06_LOCAL = CommonUtil.convertObjToStr(serverProperties.getProperty("NEFT_OUTWARD_N06_LOCAL"));
            NEFT_OUTWARD_N06 = CommonUtil.convertObjToStr(serverProperties.getProperty("NEFT_OUTWARD_N06"));
            NEFT_INWARD_N02_LOCAL = CommonUtil.convertObjToStr(serverProperties.getProperty("NEFT_INWARD_N02_LOCAL"));
            NEFT_INWARD_N02 = CommonUtil.convertObjToStr(serverProperties.getProperty("NEFT_INWARD_N02"));
            NEFT_OUTWARD_N27_LOCAL = CommonUtil.convertObjToStr(serverProperties.getProperty("NEFT_OUTWARD_N027_LOCAL"));
            NEFT_OUTWARD_N27 = CommonUtil.convertObjToStr(serverProperties.getProperty("NEFT_OUTWARD_N027"));

            RTGS_OUTWARD_R41_R42_LOCAL = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_OUTWARD_R41_R42_LOCAL"));
            RTGS_OUTWARD_R41_R42 = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_OUTWARD_R41_R42"));
            RTGS_BANK_NAME = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_BANK_NAME"));
            RTGS_NEFT_CLIENT_CODE = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_NEFT_CLIENT_CODE"));
            RTGS_NEFT_FORMAT_CODE = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_NEFT_FORMAT_CODE"));

            RTGS_OUTWARD_R41_R42_LOCAL = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_OUTWARD_R41_R42_LOCAL"));
            RTGS_OUTWARD_R41_R42 = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_OUTWARD_R41_R42"));
            RTGS_BANK_NAME = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_BANK_NAME"));
            RTGS_NEFT_CLIENT_CODE = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_NEFT_CLIENT_CODE"));
            RTGS_NEFT_FORMAT_CODE = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_NEFT_FORMAT_CODE"));
            RTGS_NEFT_INWARD_PATH = serverProperties.getProperty("RTGS_NEFT_INWARD_PATH");
            RTGS_NEFT_INWARD_BACKUP_PATH = serverProperties.getProperty("RTGS_NEFT_INWARD_BACKUP_PATH");
            RTGS_NEFT_OUTWARD_PATH = serverProperties.getProperty("RTGS_NEFT_OUTWARD_PATH");
            RTGS_NEFT_OUTWARD_BACKUP_PATH = serverProperties.getProperty("RTGS_NEFT_OUTWARD_BACKUP_PATH");
            RTGS_NEFT_PAYMENT_CODE = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_NEFT_PAYMENT_CODE"));
            RTGS_NEFT_ECOLL_CODE = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_NEFT_ECOLL_CODE"));
            RTGS_NEFT_DR_ACT_NUM = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_NEFT_DR_ACT_NUM"));
            RTGS_NEFT_OUTWARD_MOVED_NW_PATH = CommonUtil.convertObjToStr(serverProperties.getProperty("RTGS_NEFT_OUTWARD_MOVED_NW_PATH"));

            //AXIS BANK DETAILS
            AXIS_CHANNEL_IDENTIFIER = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_CHANNEL_IDENTIFIER"));
            AXIS_RTGS_NEFT_INWARD_PATH = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_RTGS_NEFT_INWARD_PATH"));
            AXIS_RTGS_NEFT_INWARD_RETURN_PATH = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_RTGS_NEFT_INWARD_RETURN_PATH"));
            AXIS_RTGS_NEFT_INWARD_BACKUP_RETURN_PATH = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_RTGS_NEFT_INWARD_BACKUP_RETURN_PATH"));
            AXIS_RTGS_NEFT_INWARD_BACKUP_PATH = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_RTGS_NEFT_INWARD_BACKUP_PATH"));
            AXIS_RTGS_NEFT_OUTWARD_PATH = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_RTGS_NEFT_OUTWARD_PATH"));
            AXIS_CORPORATE_IDENTIFIER = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_CORPORATE_IDENTIFIER"));
            AXIS_USER_ID_UPLOADER = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_USER_ID_UPLOADER"));
            AXIS_USER_ID_AUTHORIZER_1 = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_USER_ID_AUTHORIZER_1"));
            AXIS_USER_ID_AUTHORIZER_2 = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_USER_ID_AUTHORIZER_2"));
            AXIS_USER_ID_AUTHORIZER_3 = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_USER_ID_AUTHORIZER_3"));
            AXIS_USER_ID_AUTHORIZER_4 = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_USER_ID_AUTHORIZER_4"));
            AXIS_USER_ID_AUTHORIZER_5 = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_USER_ID_AUTHORIZER_5"));
            AXIS_CORPORATE_PRODUCT_CODE = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_CORPORATE_PRODUCT_CODE"));
            ELECTRONIC_TYPE_RTGSNEFT = CommonUtil.convertObjToStr(serverProperties.getProperty("ELECTRONIC_TYPE_RTGSNEFT"));
            ELECTRONIC_TYPE_ATM = CommonUtil.convertObjToStr(serverProperties.getProperty("ELECTRONIC_TYPE_ATM"));
            AXIS_RTGS_NEFT_DR_ACT_NUM = CommonUtil.convertObjToStr(serverProperties.getProperty("AXIS_RTGS_NEFT_DR_ACT_NUM"));
            
            //ATM FOR KERALA
            ATM_OUR_INWARD_STOP_ACK = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_OUR_INWARD_STOP_ACK"));
            ATM_OUR_INWARD_TOPUP_ACK = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_OUR_INWARD_TOPUP_ACK"));
            ATM_OUR_INWARD_TRANSACTION = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_OUR_INWARD_TRANSACTION"));
            ATM_OUR_OUTWARD_STOPAC = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_OUR_OUTWARD_STOPAC"));
            ATM_OUR_OUTWARD_TOPUP = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_OUR_OUTWARD_TOPUP"));
            ATM_OUR_OUTWARD_TRANSACTION_ACK = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_OUR_OUTWARD_TRANSACTION_ACK"));
            ATM_OUR_OUTWARD_TRANSACTION_REPORT = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_OUR_OUTWARD_TRANSACTION_REPORT"));
            ATM_THEIR_INWARD_STOP_ACK = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_THEIR_INWARD_STOP_ACK"));
            ATM_THEIR_INWARD_TOPUP_ACK = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_THEIR_INWARD_TOPUP_ACK"));
            ATM_THEIR_INWARD_TRANSACTION = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_THEIR_INWARD_TRANSACTION"));
            ATM_THEIR_OUTWARD_STOPAC = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_THEIR_OUTWARD_STOPAC"));
            ATM_THEIR_OUTWARD_TOPUP = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_THEIR_OUTWARD_TOPUP"));
            ATM_THEIR_OUTWARD_TRANSACTION_ACK = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_THEIR_OUTWARD_TRANSACTION_ACK"));
            ATM_THEIR_INWARD_TRANSACTION_ARCHIVE = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_THEIR_INWARD_TRANSACTION_ARCHIVE"));

            ATM_OUR_BACKUP_INWARD_STOP_ACK = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_OUR_BACKUP_INWARD_STOP_ACK"));
            ATM_OUR_BACKUP_INWARD_TOPUP_ACK = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_OUR_BACKUP_INWARD_TOPUP_ACK"));
            ATM_OUR_BACKUP_INWARD_TRANSACTION = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_OUR_BACKUP_INWARD_TRANSACTION"));
            ATM_OUR_BACKUP_OUTWARD_STOPAC = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_OUR_BACKUP_OUTWARD_STOPAC"));
            ATM_OUR_BACKUP_OUTWARD_TOPUP = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_OUR_BACKUP_OUTWARD_TOPUP"));
            ATM_OUR_BACKUP_OUTWARD_TRANSACTION_ACK = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_OUR_BACKUP_OUTWARD_TRANSACTION_ACK"));

//            ATM_THEIR_BACKUP_INWARD_STOP_ACK = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_THEIR_BACKUP_INWARD_STOP_ACK"));
            ATM_THEIR_BACKUP_INWARD_TOPUP_ACK = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_THEIR_BACKUP_INWARD_TOPUP_ACK"));
//            ATM_THEIR_BACKUP_INWARD_TRANSACTION = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_THEIR_BACKUP_INWARD_TRANSACTION"));
//            ATM_THEIR_BACKUP_OUTWARD_STOPAC = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_THEIR_BACKUP_OUTWARD_STOPAC"));
//            ATM_THEIR_BACKUP_OUTWARD_TOPUP = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_THEIR_BACKUP_OUTWARD_TOPUP"));
//            ATM_THEIR_BACKUP_OUTWARD_TRANSACTION_ACK = CommonUtil.convertObjToStr(serverProperties.getProperty("ATM_THEIR_BACKUP_OUTWARD_TRANSACTION_ACK"));
            
            
            ICICI_RTGS_NEFT_OUTWARD_PATH = CommonUtil.convertObjToStr(serverProperties.getProperty("ICICI_RTGS_NEFT_OUTWARD_PATH"));
            ICICI_RTGS_NEFT_OUTWARD_MOVED_NW_PATH = CommonUtil.convertObjToStr(serverProperties.getProperty("ICICI_RTGS_NEFT_OUTWARD_MOVED_NW_PATH"));
            ICICI_RTGS_NEFT_OUTWARD_BACKUP_PATH = CommonUtil.convertObjToStr(serverProperties.getProperty("ICICI_RTGS_NEFT_OUTWARD_BACKUP_PATH"));
            ICICI_RTGS_NEFT_INWARD_PATH = CommonUtil.convertObjToStr(serverProperties.getProperty("ICICI_RTGS_NEFT_INWARD_PATH"));
            ICICI_RTGS_NEFT_INWARD_MOVED_NW_PATH = CommonUtil.convertObjToStr(serverProperties.getProperty("ICICI_RTGS_NEFT_INWARD_MOVED_NW_PATH"));
            ICICI_RTGS_NEFT_INWARD_BACKUP_PATH = CommonUtil.convertObjToStr(serverProperties.getProperty("ICICI_RTGS_NEFT_INWARD_BACKUP_PATH"));
            cons = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // HashMap Key Values
    public static final String JNDI = "JNDI";      // Hash Map Key for JNDI
    public static final String HOME = "Home";      // Hash Map Key for Home Interface
    public static final String REMOTE = "Remote";  // Hash Map Key for Remote Interface
    public static final String METHOD = "METHOD";  // Hash Map Key for Remote Interface
    public static final String OBJ = "OBJ";        // Hash Map Key for Remote Interface
    public static final String PARAM = "PARAM";    // Hash Map Key for Remote Interface
    public static final String EXECUTE = "execute";// Hash Map Key for Remote Interface
    public static final String EXECUTE_QUERY = "executeQuery";// Hash Map Key for Remote Interface
    public static final String HIERARCHY_ID = "HIERARCHY_ID";
//    public static final String SERVER_IP_ADDR = "tcs041571";
//    public static final String SERVER_IP_ADDR = "applserver";
    // JNDI - Initial Context Values - JBoss
//    public static final String INITIAL_CONTEXT_FACTORY = "org.jnp.interfaces.NamingContextFactory";
//    INITIAL_CONTEXT_FACTORY for JBoss 7.1.1 should be as follows
    public static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    
    public static final String URL_PKG_PREFIXES = "org.jboss.naming:org.jnp.interfaces";
//    // Clustering with JBoss - Run it thru run.bat -c all
//    public static final String PROVIDER_URL = "jnp://" + SERVER_IP_ADDR + ":1100/,jnp://see-dev-31:1100/";
//    // JNDI - WebLogic Initial Context Values
//    public static final String INITIAL_CONTEXT_FACTORY = "weblogic.jndi.WLInitialContextFactory";
//    public static final String URL_PKG_PREFIXES = ""; 
//    public static final String PROVIDER_URL = "t3://" + SERVER_IP_ADDR + ":7001/";
//    public static final String LOCAL_PROVIDER_URL = "jnp://172.26.200.237:1099/";
    public static final String TTPACKAGE = "com.see.truetransact";
    public static final String OTHEREXCEPTION = "java";
    public static final String OK = "Ok";
    public static final String WARNINGTITLE = "Warning";
    // Database Updation
    public static final String STATUS_CREATED = "CREATED";
    public static final String STATUS_MODIFIED = "MODIFIED";
    public static final String STATUS_DELETED = "DELETED";
    public static final String STATUS_AUTHORIZED = "AUTHORIZED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_EXCEPTION = "EXCEPTION";
    public static final String STATUS_REALIZED = "REALIZED";
    //For Freeze of SB/CA
    public static final String STATUS_UNFREEZE = "UNFREEZED";
    public static final String STATUS_FREEZE = "FREEZED";
    //For Lien of SB/CA
    public static final String STATUS_UNLIEN = "UNLIENED";
    public static final String STATUS_LIEN = "LIENED";
    // Account Status
    public static final String OPEN = "OPEN";
    public static final String CLOSED = "CLOSED";
    public static final String NEW = "NEW";
    // Cheque status STOP
    public static final String STOPPED = "STOPPED";
    public static final String REVOKED = "REVOKED";
    // Transfer Object Status
    public static final String TOSTATUS_INSERT = "INSERT";
    public static final String TOSTATUS_UPDATE = "UPDATE";
    public static final String TOSTATUS_DELETE = "DELETE";
    public static final String TOSTATUS_RENEW = "RENEW";
    public static final String TOSTATUS_EXTENSION = "EXTENSION";
    // Lookup Strings
    public static final String MAP_NAME = "MAPNAME";
    public static final String MAP_WHERE = "WHERE";
    public static final String UPDATE_MAP_NAME = "UPDATEMAPNAME";
    public static final String UPDATE_MAP_WHERE = "UPDATEWHERE";
    public static final String DATA = "DATA";
    public static final String KEY = "KEY";
    public static final String VALUE = "VALUE";
    public static final String TABLEHEAD = "TABLEHEAD";
    public static final String TABLEDATA = "TABLEDATA";
    public static final String PARAMFORQUERY = "paramforquery";
    public static final String INFORMATIONTITLE = "Information";
    public static final String AUTHORIZEMAP = "AUTHORIZEMAP";
    public static final String AUTHORIZESTATUS = "AUTHORIZESTATUS";
    public static final String AUTHORIZEDATA = "AUTHORIZEDATA";
    public static final String REALIZEMAP = "REALIZEMAP";
    public static final String AUTHORIZEUSERLIST = "AUL";
    public static final String TRANSACTION_TYPE = "TT";
    // BusinessRule Exception handling constants
    public static final String EXCEPTION_LIST = "EXCEPTIONLIST";
    public static final String CONFIRMATION_LIST = "CONFIRMATIONLIST";
    public static final String CONSTANT_CLASS = "CONSTANTCLASS";
    public static final String EXCEPTION_TYPE = "EXCEPTION_TYPE";
    public static final String SERVER_VALUE = "SERVER_VALUE";
    // Type of Exceptions
    public static final String CONCAT_EXCEPTION = "CONCAT_EXCEPTION";
    // Used for Passing parameters from Client to Server
    public static final String IP_ADDR = "IP_ADDR";
    public static final String USER_ID = "USER_ID";
    public static final String BRANCH_ID = "BRANCH_CODE";
    public static final String INITIATED_BRANCH = "INITIATED_BRANCH";
    public static final String BANK_ID = "BANK_CODE";
    public static final String SELECTED_BRANCH_ID = "SELECTED_BRANCH_ID";
    public static final String OPENED_FOR_EDIT = "OPENED_FOR_EDIT";
    public static final String MODULE = "MODULE";
    public static final String SCREEN = "SCREEN";
    public static final String STATUS = "STATUS";
    // Debit & Credit
    public static final String CREDIT = "CREDIT";
    public static final String DEBIT = "DEBIT";
    public static final String TTSYSTEM = "TTSYSTEM";
    // TRANSACTION TYPES
    public static final int CASH = 1;
    public static final int TRANSFER = 2;
    public static final int CLEARING = 3;
    // TRANSACTION TYPES
    public static final String TX_CASH = "CASH";
    public static final String TX_TRANSFER = "TRANSFER";
    public static final String TX_CLEARING = "CLEARING";
    //For ShareAccount
    public static final String STATUS_SEND_TO_RESOLUTION = "SENDTORESOLUTION";
    public static final String SENDTORESOLUTIONSTATUS = "SENDTORESOLUTION";
//For Share resolution
    public static final String STATUS_RESOLUTION_ACCEPT = "RES_ACCEPT";
    public static final String STATUS_RESOLUTION_REJECT = "RES_REJECT";
    public static final String STATUS_RESOLUTION_DEFFERED = "RES_DEFFERED";
    public static final String TOSTATUS_RESOLUTION_ACCEPT = "RES_ACCEPT";
    public static final String TOSTATUS_RESOLUTION_REJECT = "RES_REJECT";
    public static final String TOSTATUS_RESOLUTION_DEFFERED = "RES_DEFFERED";
    public static final String RESOLUTIONMAP = "RESOLUTIONMAP";
    // INITIATOR TYPES
    public static final String CASHIER = "CASHIER";
    public static final String ATM = "ATM";
    //CHARGES INPUT MAP KEYS
    public static final String ACT_AMT = "ACT_AMT";
    public static final String ACT_COUNT = "ACT_COUNT";
    public static final String ACT_NUM = "ACT_NUM";
    public static final String CHRG_TYPE = "CHRG_TYPE";
    public static final String CHRG_AMT = "CHRG_AMT";
    public static final String AC_HD_ID = "AC_HD_ID";
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String PRODUCT_TYPE = "PRODUCT_TYPE";
    // Interest calculation constants
    public static final String INT_TYPE = "INT_TYPE";
    public static final String BRANCH = "BRANCH";
    public static final String PAYABLE = "PAYABLE";
    public static final String RECEIVABLE = "RECEIVABLE";
    // Remittance Issue Operations
    public static final String REMIT_ISSUE = "ISSUE";
    public static final String REMIT_REVALIDATE = "REVALIDATE";
    public static final String REMIT_DUPLICATE = "DUPLICATE";
    // DEPOSIT INTEREST CALCULATION
    public static final String AT_MATURITY_INT = "AT_MATURITY_INT";
    public static final String BEFORE_MATURITY_INT = "BEFORE_MATURITY_INT";
    public static final String AFTER_MATURITY_INT = "AFTER_MATURITY_INT";
    public static final String TOTAL_INTEREST_INT = "TOTAL_INTEREST_INT";
    public static final String AT_MATURITY_ROI = "AT_MATURITY_ROI";
    public static final String BEFORE_MATURITY_ROI = "BEFORE_MATURITY_ROI";
    public static final String AFTER_MATURITY_ROI = "AFTER_MATURITY_ROI";
    public static final String PARTIAL_WITHDRAWL = "PARTIAL_WITHDRAWAL";
    public static final String NORMAL_CLOSURE = "NORMAL";
    public static final String PREMATURE_CLOSURE = "PREMATURE";
    public static final String TRANSFER_OUT_CLOSURE = "TRANSFEROUT";
    public static final String DAY_BEGIN = "DAY_BEGIN";
    public static final String DAY_END = "DAY_END";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";
    public static final String GENERAL = "GENERAL";
    public static final String ACCOUNT = "ACCOUNT";
    public static final String CUSTOMER = "CUSTOMER";
    public static final String CUSTOMER_CORPORATE = "CORPORATE";
    public static final String IS_COOPERATIVE = "YES";
    public static final String TRANS_ID = "TRANS_ID";
    public static final String AUTHORIZEDT = "AUTHORIZEDT";
    public static final String ERR_MESSAGE = "EM";
    public static final String ERR_DATA = "ED";
    public static final String REPAY_SCHEDULE_MODE_REGULAR = "REGULAR";
    public static final String REPAY_SCHEDULE_MODE_REPHASE = "REPHASE";
    public static final String REPAY_SCHEDULE_MODE_MERGED = "MERGED";
    public static final String REPAY_SCHEDULE_MODE_INACTIVE = "INACTIVE";
    //loans
    public static final String DEBIT_LOAN_TYPE = "DEBIT_LOAN_TYPE";
    public static final String PARTICULARS = "PARTICULARS";
    /* The following Constant has been set for identifying whether 
     New Branch Implementation or Not
     */
    public static final String IMPLEMENTATION = "IMPLEMENTATION";
    public static final String BREAK_LOAN_HIERARCHY = "BREAK_LOAN_HIERARCHY";
    public static final String COMPLETE_FREEZE = "COMP_FREEZE";
    public static final String PARTIAL_FREEZE = "PART_FREEZE";
    public static final String SHARE = "1";
    public static final String OPERATIVE_ACCOUNTS = "2";
    public static final String DEPOSITS = "3";
    public static final String LOANS = "4";
    public static final String BILLS = "5";
    public static final String MMBS = "6";
    public static final String INVESTMENTS = "7";
    public static final String BORROWINGS = "8";
    public static final String OTHER_BANK_ACCOUNTS = "9";
    public static final String LOCKER = "10";
    public static final String RENT = "11";
    public static final String INDEND = "12";
    public static final String SUSPENSE = "13";
    public static final String ADVANCES = "14";
    public static final String GL = "15";
    //locker trans mode type for locker transaction , added by shihad on 15/05/2014
    public static final String LOCKER_TRANSMODE_TYPE = "LK";
    public static final String DEPOSIT_TRANSMODE_TYPE = "TD";
    public static final String LOAN_TRANSMODE_TYPE = "TL";
    public static final String OPERATIVE_TRANSMODE_TYPE = "OA";
    public static final String GL_TRANSMODE_TYPE = "GL";
    public static final String MDS_TRANSMODE_TYPE = "MDS";
    public static final String OTHER_BANK_TRANSMODE_TYPE = "AB";
    public static final String ADVANCE_TRANSMODE_TYPE = "AD";
    public static final String SUSPENSE_TRANSMODE_TYPE = "SA";
    public static final String ROLLED_BACK_NEW = "ROLLED_BACK_NEW";
    public static final String ROLLED_BACK_CLOSED = "ROLLED_BACK_CLOSED";
    public static final String ROLLED_BACK_RENEWED = "ROLLED_BACK_RENEWED";
    public static final String TXN_PROD_TYPE_OPERATIVE = "OA";
    public static final String TXN_PROD_TYPE_DEPOSITS = "TD";
    public static final String TXN_PROD_TYPE_GL = "GL";
    public static final String TXN_PROD_TYPE_LOANS = "TL";
    public static final String TXN_PROD_TYPE_ADVANCES = "AD";
    public static final String TXN_PROD_TYPE_AGRILOANS = "ATL";
    public static final String TXN_PROD_TYPE_AGRIADVANCES = "AAD";
    public static final String TXN_PROD_TYPE_SUSPENSE = "SA";
    public static final String TXN_PROD_TYPE_OTHERBANKACTS = "AB";
    public static final String TXN_PROD_TYPE_INDEND = "TR";
    public static final String SERVICE_TAX_CHRG = "SERVICETAX_CHARGE";
}
