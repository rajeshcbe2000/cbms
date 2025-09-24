/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DisplayDueProductsOB.java
 */

package com.see.truetransact.ui.dueproducts;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.uicomponent.CObservable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class DisplayDueProductsOB extends CObservable {

    Date curDate = null;
    private static DisplayDueProductsOB displayDueProductsOB;
    SMSSubscriptionTO objSmsSubscriptionTO;
    Date currDt = null;
    private ProxyFactory proxy;
    private HashMap map;
    private final static ClientParseException parseException = ClientParseException.getInstance();

    static {
        try {
            displayDueProductsOB = new DisplayDueProductsOB();
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    public static DisplayDueProductsOB getInstance() {
        return displayDueProductsOB;

    }
    
    public DisplayDueProductsOB() {
        try {
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "SmsConfigJNDI");
            map.put(CommonConstants.HOME, "sms.SmsConfigHome");
            map.put(CommonConstants.REMOTE, "sms.SmsConfig");
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    public void setSMSForMaturedDeposits(List smsDetailList){
        try {
            if (smsDetailList != null && smsDetailList.size() > 0) {
                //System.out.println("smsDetailList :: " + smsDetailList);
                currDt = ClientUtil.getCurrentDate();
                String displayDt = DateUtil.getStringDate(getProperDateFormat(currDt));
                HashMap where = new HashMap();
                for (int i = 0; i < smsDetailList.size(); i++) {
                    HashMap smsMap = (HashMap) smsDetailList.get(i);
                    if (smsMap.containsKey(("CUST_PHONE"))) {
                        if (smsMap.get("CUST_PHONE") != null) {
                            String message = "Your " + smsMap.get("DEPOSIT_TYPE") + " Acct No : " + smsMap.get("DEPO_NO") + " Amount : " + smsMap.get("DEPO_AMT") + " - is matured on " + displayDt + " - " + CommonConstants.BANK_SMS_DESCRIPTION + " - " + CommonConstants.MOBILE_BANKING_SENDERID;
                            SmsConfigDAO smsDAO = new SmsConfigDAO();
                            smsDAO.sendSMS(message, smsMap.get("CUST_PHONE").toString(), "", ProxyParameters.BRANCH_ID,CommonUtil.convertObjToStr(smsMap.get("DEPO_NO")),"MaturedDeposit");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in setSMSSubscriptionTO");
            parseException.logException(e, true);
        }
    }

    public void setSMSSubscriptionTO(List smsDetailList, String selectedNode) {
        try {
            if (selectedNode.equals("Interest due")) {
                currDt = ClientUtil.getCurrentDate();
                String displayDt = DateUtil.getStringDate(getProperDateFormat(currDt));
                HashMap where = new HashMap();
                objSmsSubscriptionTO = (SMSSubscriptionTO) (smsDetailList.get(0));
                //System.out.println("inside setSMSSubscriptionTO :: objSmsSubscriptionTO :: " + objSmsSubscriptionTO);
                where.put("DUE_DT", currDt.clone());
                where.put("ACCT_NUM", objSmsSubscriptionTO.getActNum());
                List lst = ClientUtil.executeQuery("getSMSDateForIntDueCustomers", where);
                HashMap smsMap = (HashMap) lst.get(0);
                //System.out.println("lst :::: " + smsMap);
                String message = "Your term loan Acct No  " + objSmsSubscriptionTO.getActNum() + " - Installment  no : " + smsMap.get("INSTALLMENT_SLNO") + " is due on " + displayDt + " Amount is : " + smsMap.get("TOTAL_AMT") + " - " + CommonConstants.BANK_SMS_DESCRIPTION + " - " + CommonConstants.MOBILE_BANKING_SENDERID + " Please ignore if paid!! ";
                //System.out.println("message :: " + message);
                SmsConfigDAO smsDAO = new SmsConfigDAO();
                smsDAO.sendSMS(message, CommonUtil.convertObjToStr(objSmsSubscriptionTO.getMobileNo()), "", ProxyParameters.BRANCH_ID,CommonUtil.convertObjToStr(objSmsSubscriptionTO.getActNum()),"InterestDue");
                objSmsSubscriptionTO = null;
            }
        } catch (Exception e) {
            System.out.println("Error in setSMSSubscriptionTO");
            parseException.logException(e, true);
        }
    }

    public Date getProperDateFormat(Date obj) {
        Date curr_Dt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            curr_Dt = (Date) currDt.clone();
            curr_Dt.setDate(tempDt.getDate());
            curr_Dt.setMonth(tempDt.getMonth());
            curr_Dt.setYear(tempDt.getYear());
        }
        return curr_Dt;
    }
    
     public void SendSMS(HashMap smsMap) {// Added by nithya on 22-04-2019 for KD 468 - DueProducts screen SmsConfigDAO Called in OB Side, Needs to be removed and it has to use the standard way
        try {
            smsMap = proxy.execute(smsMap, map) ;
           setProxyReturnMap(smsMap);
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e,true);           
        }
    }
}
