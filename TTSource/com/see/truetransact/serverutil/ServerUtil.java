/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ServerUtil.java
 *
 * Created on July 15, 2004, 11:36 AM
 */
package com.see.truetransact.serverutil;

import java.util.HashMap;
import java.util.ArrayList;

import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.businessrule.generalledger.AccountMaintenanceRule;
import com.see.truetransact.businessrule.login.LoginValidationRule;
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import com.see.truetransact.servicelocator.ServiceLocator;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rahul
 * @author JK
 */
public class ServerUtil {

    public static HashMap cbmsParameterMap = new HashMap();
    public static String callingSMSBranchCode;
    private static boolean smsScheduleFlag = true;
    private static long ONE_MIN_VALUE = 1000 * 60;

    public static HashMap getCbmsParameterMap() {
        return cbmsParameterMap;
    }

    public static void setCbmsParameterMap(HashMap cbmsParameterMap) {
        cbmsParameterMap = cbmsParameterMap;
    }

    /**
     * Creates a new instance of ServerUtil
     */
    public ServerUtil() {
    }

    public static List executeQuery(String mapName, HashMap whereMap) {
        List list = null;
//        HashMap where = null;
        try {

            ServiceLocator locator = ServiceLocator.getInstance();
            list = (List) locator.getDAOSqlMap().executeQueryForList(mapName, whereMap);
        } catch (Exception cnF) {
        }
//        where = new HashMap();
//        where.put(CommonConstants.DATA, list);
        return list;
    }

    public static Date getCurrentDate(String branchCode) {
//////////////        Date applDate = null;
//////////////        try {
//////////////            if (CommonUtil.convertObjToStr(branchCode).equals("")) 
//////////////                System.out.println("ServerUtil:BranchCode is null");
//////////////            
//////////////            ServiceLocator locator = ServiceLocator.getInstance();
//////////////            HashMap whereMap = new HashMap();
//////////////            whereMap.put("BRANCH_CODE", branchCode);
//////////////            
//////////////            applDate = (Date) locator.getDAOSqlMap().executeQueryForObject("getApplDate", whereMap);
//////////////            
////////////////            System.out.println("****** applDate "+applDate);
//////////////        } catch (Exception cnF) {
//////////////        }
//////////////        return applDate;
        Date applDate = null;
        try {
            if (CommonUtil.convertObjToStr(branchCode).equals("")) {
                System.out.println("ServerUtil:BranchCode is null");
            }

            ServiceLocator locator = ServiceLocator.getInstance();
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", branchCode);

            whereMap = (HashMap) locator.getDAOSqlMap().executeQueryForObject("getApplDateHashMap", whereMap);
            applDate = (Date) whereMap.get("CURR_APPL_DT");
//            System.out.println("****** applDate "+applDate);
        } catch (Exception cnF) {
        }
        return applDate;

    }

    public static Date getCurrentDateProperFormat(String branchCode) {
        Date applDate = null;
        try {
            if (CommonUtil.convertObjToStr(branchCode).equals("")) {
                System.out.println("ServerUtil:BranchCode is null");
            }

            ServiceLocator locator = ServiceLocator.getInstance();
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", branchCode);

            whereMap = (HashMap) locator.getDAOSqlMap().executeQueryForObject("getApplDateHashMap", whereMap);
            applDate = (Date) whereMap.get("CURR_APPL_DT");
//            System.out.println("****** applDate "+applDate);
        } catch (Exception cnF) {
        }
        return applDate;
    }

    public static Date getCurrentDateWithTime(String branchCode) {
        Date applDate = null;
        try {
            if (CommonUtil.convertObjToStr(branchCode).equals("")) {
                System.out.println("ServerUtil:BranchCode is null");
            }

            ServiceLocator locator = ServiceLocator.getInstance();
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", branchCode);

            applDate = (Date) locator.getDAOSqlMap().executeQueryForObject("getApplDate", whereMap);

            GregorianCalendar c1 = new GregorianCalendar();
            applDate.setHours(c1.getTime().getHours());
            applDate.setMinutes(c1.getTime().getMinutes());
            applDate.setSeconds(c1.getTime().getSeconds());
//            System.out.println("****** applDate "+applDate);
        } catch (Exception cnF) {
        }
        return applDate;
    }

    public static void verifyAccountHead(HashMap inputMap) throws Exception {
        System.out.println("In verifyAccountHead");
        RuleEngine engine = new RuleEngine();
        RuleContext context = new RuleContext();
        context.addRule(new AccountMaintenanceRule());

        ArrayList list = (ArrayList) engine.validateAll(context, inputMap);
        if (list != null) {
            System.out.println("list in DAO: " + list);
            HashMap exception = new HashMap();
            exception.put(CommonConstants.EXCEPTION_LIST, list);
            exception.put(CommonConstants.CONSTANT_CLASS,
                    "com.see.truetransact.clientutil.exceptionhashmap.generalledger.GeneralLedgerRuleHashMap");
            throw new TTException(exception);
            //sqlMap.rollbackTransaction();
        }

    }

    public static void verifyLogin(HashMap inputMap) throws Exception {
        System.out.println("In verifyLogin");
        RuleEngine engine = new RuleEngine();
        RuleContext context = new RuleContext();
        context.addRule(new LoginValidationRule());
        cbmsParmeterMap();
        ArrayList list = (ArrayList) engine.validateAll(context, inputMap);
        if (list != null) {
            System.out.println("list in DAO: " + list);
            HashMap exception = new HashMap();
            exception.put(CommonConstants.EXCEPTION_LIST, list);
            exception.put(CommonConstants.CONSTANT_CLASS,
                    "com.see.truetransact.clientutil.exceptionhashmap.login.LoginValidationRuleHashMap");
            throw new TTException(exception);
        }

    }

    public static void cbmsParmeterMap() throws Exception {
        ServiceLocator locator = ServiceLocator.getInstance();
        List list = (List) locator.getDAOSqlMap().executeQueryForList("getSelectCbmsParameterValues", null);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                HashMap cbmsMap = (HashMap) list.get(i);
                cbmsParameterMap.put(CommonUtil.convertObjToStr(cbmsMap.get("CBMS_KEY")), CommonUtil.convertObjToStr(cbmsMap.get("CBMS_VALUE")));
            }
            System.out.println("serverUtil cbmsParameterMap : " + cbmsParameterMap);
        }
    }

    public static void callingSMSSchedule(String _branchCode) throws Exception {
        int smsSchedulePeriodInterval = CommonUtil.convertObjToInt(ServerUtil.getCbmsParameterMap().get("SMS_SCHEDULE_PERIOD_INTERVAL_MIN"));
        System.out.println("callingSMSSchedule...smsScheduleFlag : " + smsScheduleFlag + " smsSchedulePeriodInterval : " + smsSchedulePeriodInterval);
        if (smsScheduleFlag && smsSchedulePeriodInterval > 0) {
            smsScheduleFlag = false;
            int smsStartSchedule = CommonUtil.convertObjToInt(ServerUtil.getCbmsParameterMap().get("SMS_SCHEDULE_START_HRS"));
            Date currActualDate = new Date();
            long hours = currActualDate.getHours();
            long currentTimeSec = ONE_MIN_VALUE * (60 * hours);
            long configuredStartTime = ONE_MIN_VALUE * (60 * smsStartSchedule);
            System.out.println("callingSMSSchedule currentTimeSec : " + currentTimeSec + " hours : " + hours + " smsStartSchedule : " + smsStartSchedule
                    + " configuredStartTime : " + configuredStartTime);
            if (currentTimeSec >= configuredStartTime) {
                System.out.println("Starting schedule task...");
                callingSMSBranchCode = _branchCode;
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new SMSTimer(), 1000 * 60, 1000 * 60 * smsSchedulePeriodInterval);
            } else {
                System.out.println("Scheduled task not started...");
                smsScheduleFlag = true;
            }
        }
    }

    static class SMSTimer extends TimerTask {

        HashMap smsMap = new HashMap();
        Date currActualDate;
        int smsStopSchedule = CommonUtil.convertObjToInt(ServerUtil.getCbmsParameterMap().get("SMS_SCHEDULE_END_HRS"));

        public void run() {
            try {
                currActualDate = new Date();
                long hours = currActualDate.getHours();
                long currentTimeSec = ONE_MIN_VALUE * (60 * hours);
                long configuredStopTime = ONE_MIN_VALUE * (60 * smsStopSchedule);
                System.out.println("run currentTimeSec : " + currentTimeSec + " hours : " + hours + " smsStopSchedule : " + smsStopSchedule + " configuredStartTime : " + configuredStopTime);
                SmsConfigDAO smsConfigDAO = new SmsConfigDAO();
                smsMap.put("SMS_SCHEDULE", "SMS_SCHEDULE");
                smsMap.put(CommonConstants.BRANCH_ID, callingSMSBranchCode);
                smsConfigDAO.execute(smsMap);
                if (currentTimeSec >= configuredStopTime) {
                    System.out.println("Inside time is equal StopsSmsTask : ");
                    this.cancel();
                    smsScheduleFlag = true;
                }
                System.out.println("SMSTimer...smsScheduleFlag : " + smsScheduleFlag);
            } catch (Exception ex) {
                Logger.getLogger(ServerUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
