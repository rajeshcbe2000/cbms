/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DateUtil.java
 *
 * Created on August 21, 2003, 4:18 PM
 */

package com.see.truetransact.commonutil;
import com.see.truetransact.clientutil.ClientUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author  administrator
 */
public class SuspiciousAccountValidation {
    private int addDaysEach = 0;
    private int addMonthsEach = 0;
    private static int DAYS = 1;
    private static int MONTHS = 2;
    private static int YEARS = 3;
    
    /** Creates a new instance of DateUtil */
    public SuspiciousAccountValidation() {
        
    }
    
    
    public static String checkForSuspiciousActivity(String acctNum,String transType) {
        String msg = "";
        String crCash = "";
        String crTransfer = "";
        String drCash = "";
        String drTransfer = "";
        HashMap mapData = new HashMap();
        mapData.put("CONF_FOR", "ACCOUNT");
        mapData.put("ACCOUNTNO", acctNum);
        List mapDataList = ClientUtil.executeQuery("getSuspeciousConfigData", mapData);
        if(mapDataList != null && mapDataList.size() > 0){
            mapData = (HashMap) mapDataList.get(0);
            //CR_CASH CR_TRANS DR_TRANS DR_CASH
            
            if(mapData.containsKey("CR_CASH") && mapData.get("CR_CASH") != null && mapData.get("CR_CASH") .equals("Y")){
                crCash = "Y";
            }if(mapData.containsKey("CR_TRANS") && mapData.get("CR_TRANS") != null && mapData.get("CR_TRANS") .equals("Y")){
                crTransfer = "Y";
            }if(mapData.containsKey("DR_CASH") && mapData.get("DR_CASH") != null && mapData.get("DR_CASH") .equals("Y")){
                drCash = "Y";
            }if(mapData.containsKey("DR_TRANS") && mapData.get("DR_TRANS") != null && mapData.get("DR_TRANS") .equals("Y")){
                drTransfer = "Y";
            }
            
            if(transType.equals("CREDIT") && crCash.equals("Y")){
                msg += "Account is Suspicious !!! Cash Credit Blocked\n";
            }if(transType.equals("CREDIT") && crTransfer.equals("Y")){
                msg += "Account is Suspicious !!! Transfer Credit Blocked\n";
            }if(transType.equals("DEBIT") && drCash.equals("Y")){
                msg += "Account is Suspicious !!! Cash Debit Blocked\n";
            }if(transType.equals("DEBIT") && drTransfer.equals("Y")){
                msg += "Account is Suspicious !!! Transfer Debit Blocked\n";
            }            
        }
        return msg;
    }
    
    
     public static String checkForSuspiciousActivity(String acctNum,String transType,String transMode) {
        String msg = "";
        String crCash = "";
        String crTransfer = "";
        String drCash = "";
        String drTransfer = "";
        HashMap mapData = new HashMap();
        mapData.put("CONF_FOR", "ACCOUNT");
        mapData.put("ACCOUNTNO", acctNum);
        List mapDataList = ClientUtil.executeQuery("getSuspeciousConfigData", mapData);
        if(mapDataList != null && mapDataList.size() > 0){
            mapData = (HashMap) mapDataList.get(0);
            //CR_CASH CR_TRANS DR_TRANS DR_CASH
            
            if(mapData.containsKey("CR_CASH") && mapData.get("CR_CASH") != null && mapData.get("CR_CASH") .equals("Y")){
                crCash = "Y";
            }if(mapData.containsKey("CR_TRANS") && mapData.get("CR_TRANS") != null && mapData.get("CR_TRANS") .equals("Y")){
                crTransfer = "Y";
            }if(mapData.containsKey("DR_CASH") && mapData.get("DR_CASH") != null && mapData.get("DR_CASH") .equals("Y")){
                drCash = "Y";
            }if(mapData.containsKey("DR_TRANS") && mapData.get("DR_TRANS") != null && mapData.get("DR_TRANS") .equals("Y")){
                drTransfer = "Y";
            }
            
            if(transMode.equals("CASH") && transType.equals("CREDIT") && crCash.equals("Y")){
                msg += "Account is Suspicious !!! Cash Credit Blocked\n";
            }if(transMode.equals("TRANSFER") && transType.equals("CREDIT") && crTransfer.equals("Y")){
                msg += "Account is Suspicious !!! Transfer Credit Blocked\n";
            }if(transMode.equals("CASH") && transType.equals("DEBIT") && drCash.equals("Y")){
                msg += "Account is Suspicious !!! Cash Debit Blocked\n";
            }if(transMode.equals("TRANSFER") && transType.equals("DEBIT") && drTransfer.equals("Y")){
                msg += "Account is Suspicious !!! Transfer Debit Blocked\n";
            }            
        }
        return msg;
    }
    
 
}
