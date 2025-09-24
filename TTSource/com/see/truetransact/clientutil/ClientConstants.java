
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ClientConstants.java
 *
 * Created on June 18, 2003, 4:20 PM
 */

package com.see.truetransact.clientutil;

import com.see.truetransact.transferobject.sysadmin.config.ConfigPasswordTO;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.HashMap;
import java.util.List;
/**
 * @author  balachandar
 */

public class ClientConstants {
    private static java.util.Properties serverProperties = new java.util.Properties();
    public static String SERVER_ROOT;
    public static String HTTPS_SERVER_ROOT;
    public static String REPORT_TEMPLATE;
    public static String REPORT_USER;
    public static String REPORT_PWD;
    static {
        try {
            Dummy cons = new Dummy();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/commonutil/TT.properties"));
//            CLIENT_PROXY = serverProperties.getProperty("CLIENT_PROXY");
            SERVER_ROOT = "http://" + CommonConstants.SERVER_IP_ADDR + ":" + serverProperties.getProperty("HTTP_PORT") + "/" + serverProperties.getProperty("HTTP_CONTEXT");
            HTTPS_SERVER_ROOT = "https://" + CommonConstants.SERVER_IP_ADDR + ":" + serverProperties.getProperty("HTTPS_PORT") + "/" + serverProperties.getProperty("HTTPS_CONTEXT");
            REPORT_TEMPLATE = serverProperties.getProperty("REPORT_TEMPLATE");
            REPORT_USER = serverProperties.getProperty("REPORT_USER");
            REPORT_PWD = serverProperties.getProperty("REPORT_PWD");
            cons = null;
        } catch (Exception ex) {
            
        }
    }
    
    // Cheque/Instrument Maximum Length
    public static final int INSTRUMENT_NO1 = 16;
    public static final int INSTRUMENT_NO2 = 16;
    public static final int CHEQUE_SERIES = 16;    
    
    public static final String ACCOUNTHEAD_JNDI = "AccountHeadJNDI";  // Account Head JNDI
    public static final String ACCOUNTHEAD_REMOTE = "Acct"; // Account Head Remote Interface
    public static final String ACCOUNTHEAD_HOME = "AcctHome"; // Account Head Home Interface
    
    public static final String HEADOB_JNDI = "HeadOBJNDI";  // Head JNDI
    public static final String HEADOB_REMOTE = "Head"; // Head Remote Interface
    public static final String HEADOB_HOME = "HeadHome"; // Head Home Interface;
    
    public static final String ACCOUNTCREATION_JNDI = "AccountCreationOBJNDI";  // Account Creation JNDI
    public static final String ACCOUNTCREATION_REMOTE = "AcctCreation"; // Account Creation Remote Interface
    public static final String ACCOUNTCREATION_HOME = "AcctCreationHome"; // Account Creation Home Interface;
    
//    public static final String CLIENT_PROXY = ProxyFactory.EJBAPPL_CLIENT; // Proxy Configuration
    public static final String CLIENT_PROXY = ProxyFactory.WEBAPPL_CLIENT; // Proxy Configuration
//    public static final String CLIENT_PROXY = ProxyFactory.WEBHTTPS_CLIENT; // Proxy Configuration
    
    //Status message display
    
   /* public static final String STATUS_EMPTY = "                      ";
    public static final String STATUS_NEW = "New";
    public static final String STATUS_INSERTED = "Inserted";
    public static final String STATUS_EDIT = "Edit";
    public static final String STATUS_UPDATED = "Updated";
    public static final String STATUS_DELETE = "Delete";
    public static final String STATUS_DELETED = "Deleted";
    public static final String STATUS_FAILED = "Failed";*/
    
    public static final String VIEW_TYPE_NEW = "NEW";
    public static final String VIEW_TYPE_DELETE = "DELETE";
    public static final String VIEW_TYPE_EDIT  = "EDIT";
    public static final String VIEW_TYPE_AUTHORIZE = "AUTHORIZE";
    public static final String VIEW_TYPE_CANCEL = "";
    public static final String VIEW_TYPE_RENEW = "RENEW";    
    public static final String VIEW_TYPE_EXTENSION = "EXTENSION_OF_DEPOSIT";
    
    public static final String RECORD_KEY_COL = "RECORD_KEY_COL";
    
    //In ShareAccount
    public static final String VIEW_TYPE_SEND_TO_RESOLUTION = "SENDTORESOLUTION";
    
    public static final int ACTIONTYPE_NEW = 1;
    public static final int ACTIONTYPE_EDIT = 2;
    public static final int ACTIONTYPE_DELETE = 3;
    public static final int ACTIONTYPE_FAILED = 4;
    public static final int ACTIONTYPE_CANCEL = 0;
    public static final int ACTIONTYPE_VIEW = 17;
    public static final int ACTIONTYPE_VIEW_MODE = 10;
    
    //for Operative Account Opening
    public static final int ACTIONTYPE_NEWTI = 5;
    public static final int ACTIONTYPE_EDITTI = 6;
    
    // for Transfer transaction
    public static final int ACTIONTYPE_EXCEPTION = 7;
    public static final int ACTIONTYPE_AUTHORIZE = 8;
    
    public static final int ACTIONTYPE_IMPORT = 9;
    public static final int ACTIONTYPE_REJECT = 10;
    public static final int ACTIONTYPE_REALIZE = 11;
    
    //for Renewal of the account
    public static final int ACTIONTYPE_RENEW = 12;    
    public static final int ACTIONTYPE_SEND_TO_RESOLUTION = 13;
    
//--- For share resolution
    public static final int ACTIONTYPE_RESOLUTION_ACCEPT = 14;
    public static final int ACTIONTYPE_RESOLUTION_REJECT = 15;
    public static final int ACTIONTYPE_RESOLUTION_DEFFERED = 16;
    public static final int ACTIONTYPE_REVALIDATE = 18;
    public static final int ACTIONTYPE_DUPLICATE = 19;
    public static final int ACTIONTYPE_COPY = 20;
    public static final int ACTIONTYPE_EXTENSION = 21;
    
    public static final String[] ACTION_STATUS = {"                      ","New","Edit","Delete","","New TransferIn","Edit TransferIn","Exception","Authorize", "Import", "Reject", "Realize","Renew", "Send to Resolution", "Accept Resolution", "Reject Resolution", "Deffer Resolution","Enquiry", "Revalidate", "Duplicate", "Copy","Extension of Deposit"};
    public static final String[] RESULT_STATUS = {"                      ","Inserted","Updated","Deleted","Failed","Inserted","Updated", "Exception Marked", "Authorized", "Imported", "Rejected", "Realized","Renewed", "Sent to Resolution", "Resolution accepted", "Resolution rejected", "Resolution deffered","","Revalidated","Duplicated","Copied","Extended"};

//    public static final String[] ACTION_STATUS = {"                      ","New","Edit","Delete","","New TransferIn","Edit TransferIn","Exception","Authorize", "Import", "Reject", "Realize","Renew", "Send to Resolution"};
//    public static final String[] RESULT_STATUS = {"                      ","Inserted","Updated","Deleted","Failed","Inserted","Updated", "Exception Marked", "Authorized", "Imported", "Rejected", "Realized","Renewed", "Sent to Resolution"};

    public static final String NO_DATA_INFO = "No relevant data found.";
    public static final String COMMUNICATION_EXCEPTION = "Application/DB Server is not running. Pls. Check DB and " + CommonConstants.PROVIDER_URL;
    private static HashMap test = new HashMap();
    private static List lst = ((List)ClientUtil.executeQuery("getSelectConfigPasswordTO", test));
    public static final String HO =  lst!=null ? ((ConfigPasswordTO)lst.get(0)).getCboBranches() : "";
}
