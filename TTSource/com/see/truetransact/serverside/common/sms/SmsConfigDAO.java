/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SMSParameterDAO.java
 *
 * Created on Fri May 04 13:31:47 IST 2012
 */
package com.see.truetransact.serverside.common.sms;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.SQLException;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.commonutil.EngToMalTransliterator;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.common.mobile.smsAcknoldgmentTO;
import com.see.truetransact.transferobject.sms.SMSParameterTO;
import com.see.truetransact.commonutil.CommonUtil;
import java.util.Date;
import java.util.Map;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.InputStreamReader;
import java.io.File;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.net.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * SMSParameter DAO.
 *
 */
public class SmsConfigDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private SMSParameterTO objTO;
    private Map cache;
    private Date curDt = null;
    public static String fileName = "\\template\\smstemplate.xml";
    private Iterator smsIterator;
    private TransactionDAO transactionDAO = null;
    private String reply = "";
    private boolean taskRunning = false;

    /**
     * Creates a new instance of SmsConfigDAO
     */
    public SmsConfigDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        if (map.containsKey("TRUE_TRANSACT_MAIN_DBBACKUP")) {
            HashMap dataBaseMap = callingFromTrueTransactMainDBBackup();
            returnMap.put("dataBaseMap", dataBaseMap);
            System.out.println("dataBaseMap : " + dataBaseMap);
        }
        return returnMap;
    }

    private void insertData() throws Exception {
    }

    private void updateData() throws Exception {
    }

    private void deleteData() throws Exception {
    }

    public static void main(String str[]) {
        try {
            SmsConfigDAO dao = new SmsConfigDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("In smsCongigDAO for execute : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        curDt = ServerUtil.getCurrentDate(_branchCode);
        String mobieBanking = readFromTemplate("MOBILE_BANKING");
        if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
            String mobileBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
            String mobileBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
            String mobileBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
            String smsServer = readFromTemplate("SMS_SERVER");
            if (mobileBankingUserName != null && mobileBankingPwd != null && mobileBankingSenderId != null
                    && CommonUtil.convertObjToStr(mobileBankingUserName).length() > 0
                    && CommonUtil.convertObjToStr(mobileBankingPwd).length() > 0
                    && CommonUtil.convertObjToStr(mobileBankingSenderId).length() > 0) {
                if (map != null && map.containsKey("SMS_SCHEDULE")) {
                    scheduledSMSTask();
                } else if (map != null && map.containsKey("SMS_NOTICES")) {
                    MdsSmsConfiguration(map);
                } else if (map != null && map.containsKey("LOAN_MDS_INSTALLMENT")) {
                    LoanMdsInstallmentRemider(map);
                } else if (map != null && map.containsKey("SEND_COMMON_SMS")) {
                    commomSms(map);
                } else if (map != null && map.containsKey("LOAN_OVER_DUE")) {
                    HashMap smsMap = new HashMap();
                    smsMap = (HashMap) map.get("SMS");
                    smsIterator = smsMap.keySet().iterator();
                    String key = "";
                    for (int i = 0; i < smsMap.size(); i++) {
                        key = (String) smsIterator.next();
                        HashMap smsSingleMap = new HashMap();
                        HashMap smsFinalMap = new HashMap();
                        ArrayList ackArray = new ArrayList();
                        smsSingleMap = (HashMap) smsMap.get(key);
                        ShareCustomerSendingSMS(smsSingleMap);
                    }
                } else if (map != null && map.containsKey("DISPLAY_DUE_PRODUCT_SCREEN")) { // Added by nithya on 22-04-2019 for KD 468 - DueProducts screen SmsConfigDAO Called in OB Side, Needs to be removed and it has to use the standard way
                    HashMap smsMap = new HashMap();
                    smsMap = (HashMap) map.get("SMS");
                    smsIterator = smsMap.keySet().iterator();
                    String key = "";
                    for (int i = 0; i < smsMap.size(); i++) {
                        key = (String) smsIterator.next();
                        HashMap smsSingleMap = new HashMap();
                        HashMap smsFinalMap = new HashMap();
                        ArrayList ackArray = new ArrayList();
                        smsSingleMap = (HashMap) smsMap.get(key);
                        dueAccountSMS(smsSingleMap);
                    }
                }
            }
        }
        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return getData(obj);
    }

    public void sendMobileAPPOTP(HashMap phMap) {
        System.out.println("sendMobileAPPOTP phMap : " + phMap);
        try {
            String mobieBanking = readFromTemplate("MOBILE_BANKING");
            if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
                String smsString = "";
                if (phMap.containsKey("OTP")) {
					smsString = readFromTemplate("appOTP");                    
                } else if (phMap.containsKey("CUST_PWD")) {
                    smsString = readFromTemplate("appLoginPassword");
                }
                System.out.println("smsString$#$@$OTP" + smsString);
                if (smsString != null && smsString.length() > 0) {
                    List phList = sqlMap.executeQueryForList("getCustomerPhForWelcome", phMap);
                    if (phList != null && phList.size() > 0) {
                        HashMap phResultMap = (HashMap) phList.get(0);
                        String message = "";
                        if (phMap.containsKey("OTP")) {
                            message = smsString.replaceAll("%%otpPin%%", CommonUtil.convertObjToStr(phMap.get("OTP")));
                        } else if (phMap.containsKey("CUST_PWD")) {
                            message = smsString.replaceAll("%%otpPin%%", CommonUtil.convertObjToStr(phMap.get("CUST_PWD")));
                        }
                        // Commented for KD-2442
                        /*message = message + " (" + getFormattedFullDate() + ") - ";
                        message += " - " + CommonConstants.BANK_SMS_DESCRIPTION;
                        message += " - " + CommonConstants.MOBILE_BANKING_SENDERID;*/
                        if (message != null && message.length() > 0) {
                            phMap.put("BRANCH_ID", phResultMap.get("BRANCH_ID"));
                            if (phMap.containsKey("MOBILE_NO")) {
                                sendSMS(message, CommonUtil.convertObjToStr(phMap.get("MOBILE_NO")),"",CommonUtil.convertObjToStr(phResultMap.get("BRANCH_ID")),"",smsString );
                            } else {
                                sendSMS(message, CommonUtil.convertObjToStr(phResultMap.get("PHONE_NUMBER")),"",CommonUtil.convertObjToStr(phResultMap.get("BRANCH_ID")),"",smsString );
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
        
    public void scheduledSMSTask() throws Exception {
        try {
//            InetAddress inet = InetAddress.getByName(smsServer);
//            if (inet != null && inet.isReachable(1000)) {
            String mobieBanking = readFromTemplate("MOBILE_BANKING");
            if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
                String mobileBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
                String mobileBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
                String mobileBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
                String smsServer = readFromTemplate("SMS_SERVER");
                String smsVendorName = readFromTemplate("SMS_VENDOR_NAME");
                if (mobileBankingUserName != null && mobileBankingPwd != null && mobileBankingSenderId != null
                        && CommonUtil.convertObjToStr(mobileBankingUserName).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingPwd).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingSenderId).length() > 0
                        && CommonUtil.convertObjToStr(smsVendorName).length() > 0) {
                    HashMap updateMap = new HashMap();
                    int maxRun = CommonUtil.convertObjToInt(ServerUtil.getCbmsParameterMap().get("SMS_PROCESSING_COUNT"));
                    HashMap checkMap = new HashMap();
                    checkMap.put("APPLN_DT",curDt.clone());
                    //List smsAckList = sqlMap.executeQueryForList("getSelectSMSUndeliverdList", "");
                    List smsAckList = sqlMap.executeQueryForList("getSelectSMSUndeliverdList", checkMap);//for KD-2442
                    if (smsAckList != null && smsAckList.size() > 0) {
                        if (!taskRunning) {
                            taskRunning = true;
//                            System.out.println("scheduledSMSTask smsAckList : " + smsAckList.size());
                            for (int i = 0; i < smsAckList.size(); i++) {
                                smsAcknoldgmentTO objSmsAcknoldgmentTO = (smsAcknoldgmentTO) smsAckList.get(i);
                                System.out.println("scheduledSMSTask smsAckList : " + objSmsAcknoldgmentTO.getAcknowledgementId() + "smsVendorName : " + smsVendorName);
                                System.out.println("scheduledSMSTask getProcessedCount : " + objSmsAcknoldgmentTO.getProcessedCount() + "maxRun : " + maxRun);
                                if (objSmsAcknoldgmentTO.getProcessedCount() < maxRun) {
                                    System.out.println("scheduledSMSTask inside this objSmsAcknoldgmentTO.getProcessedCount() < maxRun : ");
                                    if (objSmsAcknoldgmentTO.getAcknowledgementId() != null && objSmsAcknoldgmentTO.getAcknowledgementId().length() > 0) {
                                        int processedCount = CommonUtil.convertObjToInt(objSmsAcknoldgmentTO.getProcessedCount());
                                        String replyAck = "";
                                        Map apiResult = new HashMap();
                                        if (smsVendorName.equals("EA polu") || smsVendorName.equals("alert.in") || smsVendorName.equals("greenadsglobal")) {
                                            String acknowledgementURL = "acknowledgementurl";
                                            String acknowledgementURLOutput = readFromTemplate(acknowledgementURL);
                                            String apiRequestURL = "";
                                            if(smsVendorName.equals("EA polu")){
                                                apiRequestURL = acknowledgementURLOutput.replaceAll("%%messageid%%", CommonUtil.convertObjToStr(objSmsAcknoldgmentTO.getAcknowledgementId()));                                                
                                            }else if(smsVendorName.equals("alert.in") || smsVendorName.equals("greenadsglobal")){
                                                apiRequestURL = acknowledgementURLOutput.replaceAll("%%mobileBankingUserName%%", mobileBankingUserName);
                                                apiRequestURL = apiRequestURL.replaceAll("%%mobileBankingPwd%%", mobileBankingPwd);
                                                apiRequestURL = apiRequestURL.replaceAll("%%acknowledgementID%%", objSmsAcknoldgmentTO.getAcknowledgementId());
                                            }
                                            System.out.println("scheduledSMSTask apiRequestURL : " + apiRequestURL);
//                                            String apiRequestURL = "http://eapoluenterprise.in/httpapi/httpdlr?token=f084c3778108c8dd29ecd0c0fe5bc9af&messageid=" + objSmsAcknoldgmentTO.getAcknowledgementId();
////                                            String apiRequestURL = "http://alertin.co.in/getdelivery/" + mobileBankingUserName + "/" + mobileBankingPwd + "/" + objSmsAcknoldgmentTO.getAcknowledgementId();
////                                            String apiRequestURL = "http://bulksms.greenadsglobal.com/getdelivery/" + mobileBankingUserName + "/" + mobileBankingPwd + "/" + objSmsAcknoldgmentTO.getAcknowledgementId();
                                            HashMap smsMap1 = new HashMap();
                                            smsMap1.put("URL", apiRequestURL);
                                            apiResult = getSMSStatusFromURL(smsMap1, smsServer);
                                            replyAck = CommonUtil.convertObjToStr(apiResult.get("REPLY"));
                                            if (smsVendorName.equals("EA polu") || smsVendorName.equals("alert.in")){
                                                String reply = apiResult.get("REPLY").toString();
                                                if (reply.contains("Delivered")) {
                                                    replyAck = "Delivered";
                                                } 
                                            }else if(smsVendorName.equals("greenadsglobal")){
                                                String reply1[] = apiResult.get("REPLY").toString().split("~");
                                                replyAck = reply1[2];
                                            }
                                            System.out.println("@#@#reply : " + reply + " !@!@!replyAck : " + replyAck);
                                        } else if (smsVendorName.equals("GUPSHUP")) {
                                            reply = "DELIVERD";
                                        }
                                        System.out.println("@#@#replyAck : " + replyAck + "!@!@!apiResult : " + apiResult);
                                        if ((smsVendorName.equals("EA polu") && replyAck != null && replyAck.equals("Delivered"))
                                                || (smsVendorName.equals("alert.in") && replyAck != null && replyAck.equals("Delivered"))
                                                || (smsVendorName.equals("GUPSHUP") && replyAck != null && replyAck.equals("Delivered"))
                                                || (smsVendorName.equals("greenadsglobal") && replyAck != null && CommonUtil.convertObjToInt(replyAck) == 1)) {
                                            reply = "DELIVERD";
                                            processedCount  = processedCount + 1;
                                        } else {
                                            reply = "UNDELIVERD";
                                        }
                                        updateMap.put("ACKNOWLEDGMENT", reply);
                                        updateMap.put("SMS_ID", objSmsAcknoldgmentTO.getSmsID());
                                        updateMap.put("SEND_DT", ServerUtil.getCurrentDate(_branchCode));
                                        updateMap.put("ACK_ID", objSmsAcknoldgmentTO.getAcknowledgementId());
                                        updateMap.put("PROCESSED_COUNT", processedCount);
                                        sqlMap.executeUpdate("updateSmsAcknolegment", updateMap);
                                    } else {
                                        System.out.println("scheduledSMSTask acknowledgement id is null smsAckList : " + objSmsAcknoldgmentTO.getActNum() + " SMS ID : " + objSmsAcknoldgmentTO.getSmsID());
//                                    final String message, final String phoneNo, final boolean isKeepSms, final String brachCode, final String actNum, final String smsModule
                                        sendSMS(objSmsAcknoldgmentTO.getMessage(), objSmsAcknoldgmentTO.getPhoneNo(), objSmsAcknoldgmentTO.getSmsID(), _branchCode, objSmsAcknoldgmentTO.getActNum(), objSmsAcknoldgmentTO.getSmsModule());
                                    }
                                    if (i + 1 == smsAckList.size()) {
                                        taskRunning = false;
                                    }
                                } else {
                                    System.out.println("Already processed max count : ");
                                }
                            }
                        }
                        smsAckList.clear();
                    }
                } else {
                    System.out.println("Mobile Banking settings not configured properly, please enable from smstemplate server jboss bin folder : ");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sqlMap.rollbackTransaction();
        }
    }

    public void MdsSmsConfiguration(HashMap map) {
        String messageTag = "";
        HashMap smsMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        curDt = ServerUtil.getCurrentDate(_branchCode);
        try {
            System.out.println("MdsSmsConfiguration map : " + map);
            String mobieBanking = readFromTemplate("MOBILE_BANKING");
            if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
                String mobileBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
                String mobileBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
                String mobileBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
                String smsServer = readFromTemplate("SMS_SERVER");
                String bankSMSDescription = readFromTemplate("BANK_SMS_DESCRIPTION");
                if (mobileBankingUserName != null && mobileBankingPwd != null && mobileBankingSenderId != null && bankSMSDescription != null
                        && CommonUtil.convertObjToStr(mobileBankingUserName).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingPwd).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingSenderId).length() > 0
                        && CommonUtil.convertObjToStr(bankSMSDescription).length() > 0) {
                    if (map.containsKey("MDS_PRIZED_REMIDER")) {
                        messageTag = "MdsAuctionRemider";
                    } else if (map.containsKey("MDS_NOTICE_REMINDER")) {
                        messageTag = "MdsNoticeRemider";
                    }
                    String smsString = readFromTemplate(messageTag);
                    smsMap = (HashMap) map.get("SMS");
                    smsIterator = smsMap.keySet().iterator();
                    String key = "";
                    ArrayList finalData = new ArrayList();
                    if (smsString != null && smsString.length() > 0) {
                        for (int i = 0; i < smsMap.size(); i++) {
                            key = (String) smsIterator.next();
                            HashMap smsSingleMap = new HashMap();
                            HashMap smsFinalMap = new HashMap();
                            ArrayList ackArray = new ArrayList();
                            smsSingleMap = (HashMap) smsMap.get(key);
                            System.out.println("############## SMS Single Row Data : " + smsSingleMap);
                            String actNo = "";
                            String dueDate = "";
                            String schemeName = "";
                            double totalDue = 0.0;
                            String chittalNo = "";
                            String installmentDate = "";
                            String schemeDesc = "";
                            double instamount = 0.0;
                            double instNo = 0.0;
                            Date due_Date = null;
                            Date installment_Date = null;
                            if (smsSingleMap.containsKey("ACT_NUM")) {
                                actNo = CommonUtil.convertObjToStr(smsSingleMap.get("ACT_NUM"));
                            }
                            if (smsSingleMap.containsKey("DUE_DT")) {
                                due_Date = getProperDateFormat(smsSingleMap.get("DUE_DT"));
                            }
                            if (smsSingleMap.containsKey("SCHEME_NAME")) {
                                schemeName = CommonUtil.convertObjToStr(smsSingleMap.get("SCHEME_NAME"));
                            }
                            if (smsSingleMap.containsKey("TOTAL_DUE")) {
                                totalDue = CommonUtil.convertObjToDouble(smsSingleMap.get("TOTAL_DUE"));
                            }
                            if (smsSingleMap.containsKey("CHITTAL_NO")) {
                                chittalNo = CommonUtil.convertObjToStr(smsSingleMap.get("CHITTAL_NO"));
                            }
                            if (smsSingleMap.containsKey("NEXT_INSTALLMENT_DATE")) {
                                installment_Date = (Date) smsSingleMap.get("NEXT_INSTALLMENT_DATE");
                            }
                            if (smsSingleMap.containsKey("SCHEME_DESC")) {
                                schemeDesc = CommonUtil.convertObjToStr(smsSingleMap.get("SCHEME_DESC"));
                            }
                            if (smsSingleMap.containsKey("INSTALLMENT")) {
                                instamount = CommonUtil.convertObjToDouble(smsSingleMap.get("INSTALLMENT"));
                            }
                            if (smsSingleMap.containsKey("INSTALLMENT_NO")) {
                                instNo = CommonUtil.convertObjToDouble(smsSingleMap.get("INSTALLMENT_NO"));
                            }
                            String message = "";
                            //Date due_Date = DateUtil.getDateMMDDYYYY(dueDate);
                            //Date installment_Date = DateUtil.getp(installmentDate);
                            message = smsString.replaceAll("%%Schemename%%", schemeName);
                            message = message.replaceAll("%%Accountnumber%%", actNo);
                            if (map.containsKey("MDS_NOTICE_REMINDER")) {
                                message = message.replaceAll("%%duedt%%", getFormattedFullDate(due_Date));
                            }
                            message = message.replaceAll("%%totaldue%%", String.valueOf(totalDue));
                            message = message.replaceAll("%%Accountnumber%%", actNo);
                            message = message.replaceAll("%%chittalNo%%", chittalNo);
                            if (smsSingleMap.containsKey("NEXT_INSTALLMENT_DATE")) {
                                message = message.replaceAll("%%installmentDate%%", getFormattedFullDate(installment_Date));
                            }
                            message = message.replaceAll("%%instamount%%", String.valueOf(instamount));
                            message = message.replaceAll("%%instNo%%", String.valueOf(instNo));
                            // Commented for KD-2442
                            /*message = message + " (" + getFormattedFullDate() + ") - ";
                            message += " - " + bankSMSDescription;
                            message += " - " + mobileBankingSenderId;*/
                            System.out.println("####&&&&&@@@#####  Final message  (" + i + ") : " + message);
                            HashMap smsAlertMap = new HashMap();
                            if (chittalNo != null && !chittalNo.equals("") && chittalNo.length() > 0) {
                                smsAlertMap.put("ACT_NUM", chittalNo);
                            } else {
                                smsAlertMap.put("ACT_NUM", actNo);
                            }
                            List accountList = sqlMap.executeQueryForList("getActNumFromAllProducts", smsAlertMap);
                            if (accountList != null && accountList.size() > 0) {
                                smsAlertMap = (HashMap) accountList.get(0);
                                List smsAccountList = sqlMap.executeQueryForList("getSelectSMSSubscriptionMap", smsAlertMap);
                                if (smsAccountList != null && smsAccountList.size() > 0) {
                                    SMSSubscriptionTO objSMSSubscriptionTO = (SMSSubscriptionTO) smsAccountList.get(0);
                                    sendSMS(message, CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()), "", _branchCode, CommonUtil.convertObjToStr(smsAlertMap.get("ACT_NUM")), messageTag);
                                    smsAccountList.clear();
                                }
                                accountList.clear();
                            }
                        }
//                    }
                    }
                } else {
                    System.out.println("Mobile Banking settings not configured properly, please enable from smstemplate server jboss bin folder : ");
                }
            }
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line "
                    + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public Date getProperDateFormat(Object obj) {
        Date currDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt = (Date) curDt.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }

    public void MdsReceiptsSmsConfiguration(HashMap smsSingleMap) {
        String messageTag = "";
        HashMap smsMap = new HashMap();
        _branchCode = (String) smsSingleMap.get(CommonConstants.BRANCH_ID);
        try {
            System.out.println("MdsReceiptsSmsConfiguration smsSingleMap : " + smsSingleMap);
            String mobieBanking = readFromTemplate("MOBILE_BANKING");
            if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
                String mobileBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
                String mobileBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
                String mobileBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
                String smsServer = readFromTemplate("SMS_SERVER");
                String bankSMSDescription = readFromTemplate("BANK_SMS_DESCRIPTION");
                if (mobileBankingUserName != null && mobileBankingPwd != null && mobileBankingSenderId != null && bankSMSDescription != null && CommonUtil.convertObjToStr(mobileBankingUserName).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingPwd).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingSenderId).length() > 0
                        && CommonUtil.convertObjToStr(bankSMSDescription).length() > 0) {
                    // applicationDir = new java.io.File("").getAbsolutePath() + "/template" + fileName;
                    //System.out.println("######### applicationDir : " + applicationDir);   
                    if (smsSingleMap.containsKey("MDS_PRIZED_SMS")) {
                        messageTag = "MdsPrizedRemider";
                    } else {
                        messageTag = "MdsReceiptRemider";
                    }
                    String smsString = readFromTemplate(messageTag);
                    ArrayList finalData = new ArrayList();
                    if (smsString != null && smsString.length() > 0) {
                        String transDate = "";
                        String schemeName = "";
                        String chittalNo = "";
                        String installemtDesc = "";
                        double instamount = 0.0;
                        if (smsSingleMap.containsKey("SCHEME_NAME")) {
                            schemeName = CommonUtil.convertObjToStr(smsSingleMap.get("SCHEME_NAME"));
                        }
                        if (smsSingleMap.containsKey("CHITTAL_NO")) {
                            chittalNo = CommonUtil.convertObjToStr(smsSingleMap.get("CHITTAL_NO"));
                        }
                        if (smsSingleMap.containsKey("INSTALLMENT")) {
                            instamount = CommonUtil.convertObjToDouble(smsSingleMap.get("INSTALLMENT"));
                        }
                        if (smsSingleMap.containsKey("NARRATION")) {
                            installemtDesc = CommonUtil.convertObjToStr(smsSingleMap.get("NARRATION"));
                            if(installemtDesc.contains("#")){
                                installemtDesc = installemtDesc.replaceAll("#", " No : ");
//                                installemtDesc = "Inst No : "+installemtDesc.substring(0,4)+" for the month of "+installemtDesc.substring(4,installemtDesc.length());
                            }
                        }
                        if (smsSingleMap.containsKey("TRANS_DT")) {
                            transDate = CommonUtil.convertObjToStr(smsSingleMap.get("TRANS_DT"));
                        }
                        String message = "";
                        //Date transDt = DateUtil.getDateMMDDYYYY(transDate);
                        message = smsString.replaceAll("%%Schemename%%", schemeName);
                        message = message.replaceAll("%%chittalNo%%", chittalNo);
                        message = message.replaceAll("%%narration%%", installemtDesc.replaceAll("/", " "));
                        message = message.replaceAll("%%instamount%%", String.valueOf(instamount));
                        //message = message.replaceAll("%%transDt%%", getFormattedFullDate(transDt)); 
                        //Commented for KD-2442
                        /*message = message + " (" + getFormattedFullDate() + ") - ";
                        message += " - " + bankSMSDescription;
                        message += " - " + mobileBankingSenderId;*/
                        System.out.println("####&&&&&@@@#####  Final message  : " + message);
                        HashMap smsAlertMap = new HashMap();
                        smsAlertMap.put("ACT_NUM", chittalNo);
                        System.out.println("smsAlertMap %#%#%#%%##%#%#%" + smsAlertMap);
                        List accountList = sqlMap.executeQueryForList("getActNumFromAllProducts", smsAlertMap);
                        if (accountList != null && accountList.size() > 0) {
                            smsAlertMap = (HashMap) accountList.get(0);
                            List smsAccountList = sqlMap.executeQueryForList("getSelectSMSSubscriptionMap", smsAlertMap);
                            if (smsAccountList != null && smsAccountList.size() > 0) {
                                SMSSubscriptionTO objSMSSubscriptionTO = (SMSSubscriptionTO) smsAccountList.get(0);
                                sendSMS(message, CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()), "", _branchCode, chittalNo, messageTag);
                                smsAccountList.clear();
                            }
                            accountList.clear();
                        }
                    }
                } else {
                    System.out.println("Mobile Banking settings not configured properly, please enable from smstemplate server jboss bin folder : ");
                }
            }
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line "
                    + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String readFromTemplate(String smsTag) throws SAXException {
        String smsString = "";
        try {
            //String hostDir = "D:\\TTCBS\\jboss-3.2.7\\bin\\template\\" + fileName;
            //System.out.println("fileName@$#$@#"+fileName);
            //File file = new File(fileName);
            //String absoluteFilePath = file.getAbsolutePath();
            String smsTemplatePath = CommonUtil.convertObjToStr(ServerUtil.getCbmsParameterMap().get("SMS_TEMPLATE_PATH"));
            //System.out.println("readfromtemplate : " + smsTag + " smsTemplatePath : " + smsTemplatePath);
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(smsTemplatePath));
            if (doc != null) {
                doc.getDocumentElement().normalize();
                NodeList listOfMapping = doc.getElementsByTagName(smsTag);
                int totalMappings = listOfMapping.getLength();
                //            System.out.println("readFromTemplate Total no of message : " + totalMappings);
                Element ModuleElement = (Element) listOfMapping.item(0);
                NodeList textMList = ModuleElement.getChildNodes();
                smsString = ((Node) textMList.item(0)).getNodeValue().trim();
                //            System.out.println("readFromTemplate smsString : " + smsString);
            } else {
                System.out.println("Template path not configured,Please configure it : ");
            }
        } catch (FileNotFoundException exception) {
            // Output expected FileNotFoundExceptions.
            System.out.println("File not found exception..." + exception.getMessage());
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line "
                    + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return smsString;
    }

    private String getFormattedFullDate(Date dueDate) throws Exception {
        java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return DATE_FORMAT.format(dueDate);
    }

    private String getFormattedFullDateWithoutTimeStamp(Date dueDate) throws Exception {
        java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy");
        return DATE_FORMAT.format(dueDate);
    }

    public String sendSMS(final String message, final String phoneNo, final String smsID, final String brachCode, final String actNum, final String smsModule) throws Exception {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                _branchCode = brachCode;
                String hostag = "";
                String url = "";
                HashMap smsMap = new HashMap();
                HashMap ackMap = new HashMap();
                System.out.println("inside sendSMS function phoneNo : " + phoneNo + "smsID : " + smsID + "brachCode : " + brachCode + " actNum : " + actNum + "smsModule : " + smsModule);
                try {
                    String mobieBanking = readFromTemplate("MOBILE_BANKING");
                    if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
                        String mobileBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
                        String mobileBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
                        String mobileBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
                        String smsServer = readFromTemplate("SMS_SERVER");
                        String smsVendorName = readFromTemplate("SMS_VENDOR_NAME");
                        if (mobileBankingUserName != null && mobileBankingPwd != null && mobileBankingSenderId != null && mobileBankingSenderId != null && smsServer != null
                                && CommonUtil.convertObjToStr(mobileBankingUserName).length() > 0
                                && CommonUtil.convertObjToStr(mobileBankingPwd).length() > 0
                                && CommonUtil.convertObjToStr(mobileBankingSenderId).length() > 0
                                && CommonUtil.convertObjToStr(smsServer).length() > 0
                                && CommonUtil.convertObjToStr(smsVendorName).length() > 0) {
                            ackMap.put("MESSAGE", message);
                            ackMap.put("PHONE_NO", phoneNo);
                            ackMap.put("ACT_NUM", actNum);
                            ackMap.put("SMS_MODULE", smsModule);
                            ackMap.put("SMS_ACK_ID", smsID);
                            String hostDir = "/template" + fileName;
                            hostag = "host";
                            String smsString = readFromTemplate(hostag);
                            if (smsString != null && smsString.length() > 0) {
                                url = smsString.replaceAll("%%MOBILE_BANKING_USERNAME%%", mobileBankingUserName);
                                url = url.replaceAll("%%MOBILE_BANKING_PWD%%", mobileBankingPwd);
                                url = url.replaceAll("%%MOBILE_BANKING_SENDERID%%", mobileBankingSenderId);
                                url = url.replaceAll("%%phoneNo%%", phoneNo);
//                                if (message.contains("&templateid=")) {
//                                    int sub = message.indexOf("&templateid=");
                                 String seperator="templateAppender:";
                                if (message.contains(seperator)) {
                                    int sub = message.indexOf(seperator);
                                    String messageFirstPart;
                                    if (sub != -1) {
                                        messageFirstPart = message.substring(0, sub);
                                        String encodeMessage = URLEncoder.encode(messageFirstPart, "UTF-8");
                                        String messgaeSecondart=message.substring(sub+seperator.length(), message.length());
                                        String smsMessage = encodeMessage +messgaeSecondart;
                                        url = url.replaceAll("%%message%%", smsMessage);
                                    } else {
                                url = url.replaceAll("%%message%%", URLEncoder.encode(message, "UTF-8"));
                                    }

                                } else {
                                    url = url.replaceAll("%%message%%", URLEncoder.encode(message, "UTF-8"));
                                }
                                url = url.replaceAll("\\s+", "");
                                // url=url.replaceAll(" ","%20");
                                System.out.println("url#$%#%#%#%#%#%#%#%%   " + url);
                                //String url = "http://easyhops.co.in/sendsms/" + CommonConstants.MOBILE_BANKING_USERNAME
                                //         + "/" + CommonConstants.MOBILE_BANKING_PWD + "/" + CommonConstants.MOBILE_BANKING_SENDERID + "/" + phoneNo + "/"
                                //        + URLEncoder.encode(message) + "/T";

                                List list = sqlMap.executeQueryForList("getServerSystemTime", new HashMap());
                                if (list != null && list.size() > 0) {
                                    HashMap serverDt = (HashMap) list.get(0);
                                    curDt = ServerUtil.getCurrentDate(_branchCode);
                                    System.out.println("serverDt : " + serverDt.get("SYSTEM_DATE") + " Appln Date : " + ServerUtil.getCurrentDate(_branchCode));
                                    System.out.println("count : " + DateUtil.dateDiff((Date) serverDt.get("SYSTEM_DATE"), ServerUtil.getCurrentDate(_branchCode)));
                                   // if (DateUtil.dateDiff((Date) serverDt.get("SYSTEM_DATE"), ServerUtil.getCurrentDate(_branchCode)) >= 0) { // commented for KD-2442
                                        smsMap.put("URL", url);
                                        HashMap returnSMSMap = getSMSStatusFromURL(smsMap, smsServer);
                                        String outPutFromServer = CommonUtil.convertObjToStr(returnSMSMap.get("REPLY"));
                                        System.out.println("argument reply from outPutFromServer : " + outPutFromServer);
                                        if (outPutFromServer != null && outPutFromServer.length() > 0 && !CommonUtil.convertObjToStr(outPutFromServer).equals("EXCEPTION")) {
                                            JSONArray jsonarray = new JSONArray(outPutFromServer);
                                            if (jsonarray != null && jsonarray.length() > 0) {
                                                String statusCodeResult = "";
                                                String replyStatus = "";
                                                String acknowledgementID = "";
                                                JSONObject statusCode = jsonarray.getJSONObject(0);
                                                System.out.println("sendSMS inside vendor status : " + statusCode.optString("statusCode") + " returnMsg : " + statusCode.optString("returnMsg"));
                                                String acknowledgementURL = "acknowledgementurl";
                                                String acknowledgementURLOutput = "";
                                                if (smsVendorName.equals("EA polu") || smsVendorName.equals("alert.in") || smsVendorName.equals("greenadsglobal")) {
                                                    acknowledgementURLOutput = readFromTemplate(acknowledgementURL);
                                                }
                                                if (smsVendorName.equals("greenadsglobal")) {
                                                    statusCodeResult = statusCode.optString("returnMsg");
                                                    String reply[] = statusCodeResult.toString().split(";");
                                                    replyStatus = reply[0].replaceAll("Status:", "");;
                                                    acknowledgementID = reply[3].replaceAll("GUID: ", "");
//                                                    String apiRequestURL = "http://bulksms.greenadsglobal.com/getdelivery/"+mobileBankingUserName+"/"+mobileBankingPwd+"/"+acknowledgementID;
//                                                    HashMap smsMap1 = new HashMap();
//                                                    smsMap1.put("URL", apiRequestURL);
//                                                    HashMap apiResult = getSMSStatusFromURL(smsMap1, smsServer);
//                                                    String reply1[] = apiResult.get("REPLY").toString().split("~");
//                                                    System.out.println("apiresult : "+apiResult.get("REPLY")+"@#@#reply1[0] : " + reply1[0]+"@#@#reply1[1] : " + reply1[1]+"@#@#reply1[2] : " + reply1[2]);
//                                                    statusCodeResult = reply1[2];
                                                } else if (smsVendorName.equals("EA polu") || smsVendorName.equals("alert.in")) {
                                                    String apiRequestURL = "";
                                                    if(smsVendorName.equals("EA polu")){
                                                        apiRequestURL = acknowledgementURLOutput.replaceAll("%%messageid%%", CommonUtil.convertObjToStr(statusCode.optString("returnMsg")));
                                                    }else if(smsVendorName.equals("alert.in")){
                                                        apiRequestURL = acknowledgementURLOutput.replaceAll("%%mobileBankingUserName%%", mobileBankingUserName);
                                                        apiRequestURL = apiRequestURL.replaceAll("%%mobileBankingPwd%%", mobileBankingPwd);
                                                        apiRequestURL = apiRequestURL.replaceAll("%%acknowledgementID%%", CommonUtil.convertObjToStr(statusCode.optString("returnMsg")));
//                                                        System.out.println("alert.in apiRequestURL : " + apiRequestURL);
                                                    }
                                                    System.out.println("sendSMS apiRequestURL : "+apiRequestURL);
//                                                    String apiRequestURL = "http://eapoluenterprise.in/httpapi/httpdlr?token=f084c3778108c8dd29ecd0c0fe5bc9af&messageid=" + statusCode.optString("returnMsg");
                                                    HashMap smsMap1 = new HashMap();
                                                    smsMap1.put("URL", apiRequestURL);
                                                    HashMap apiResult = getSMSStatusFromURL(smsMap1, smsServer);
//                                                    String reply[] = apiResult.get("REPLY").toString().split(",");
//                                                    replyStatus = reply[5].replaceAll("\"", "");
                                                    String reply = apiResult.get("REPLY").toString();
                                                    if (reply.contains("Delivered")) {
                                                        replyStatus = "Delivered";
                                                    } 
                                                    System.out.println("@#@#reply : " + reply + " !@!@!replyAck : " + replyStatus);
                                                    acknowledgementID = statusCode.optString("returnMsg");
                                                    statusCodeResult = statusCode.optString("statusCode");
                                                    ackMap.put("ACKNOWLEDGEMENT_ID", acknowledgementID);
                                                    System.out.println("EA polu apiResult : "+apiResult+" replyStatus : "+replyStatus+" statusCodeResult : "+statusCodeResult);
                                                } else if (smsVendorName.equals("GUPSHUP")) {
                                                    statusCodeResult = statusCode.optString("returnMsg");
                                                    String reply1[] = statusCodeResult.split("\\|");
                                                    replyStatus = reply1[0];
                                                    acknowledgementID = reply1[2];
                                                }
                                                ackMap.put("ACKNOWLEDGEMENT_ID", acknowledgementID);
                                                System.out.println("sendSMS @#@#replyStatus : " + replyStatus + "!@!@!acknowledgementID : " + acknowledgementID + " statusCodeResult : " + statusCodeResult);
                                                if ((smsVendorName.equals("EA polu") && CommonUtil.convertObjToInt(statusCodeResult) == 200 && (replyStatus != null && replyStatus.equals("Delivered")))
                                                        || (smsVendorName.equals("greenadsglobal") && replyStatus != null && CommonUtil.convertObjToInt(replyStatus) == 1)
                                                        || (smsVendorName.equals("GUPSHUP") && replyStatus != null && replyStatus.trim().equals("success"))
                                                        || (smsVendorName.equals("alert.in") && statusCodeResult != null && CommonUtil.convertObjToInt(statusCodeResult) == 200 && replyStatus != null && replyStatus.equals("Delivered"))) {
                                                    reply = "DELIVERD";
                                                } else {
                                                    reply = "UNDELIVERD";
                                                }
                                            } else {
                                                reply = "UNDELIVERD";
                                            }
                                        } else {
                                            reply = "UNDELIVERD";
                                        }
                                        ackMap.put("REPLY", reply);
                                        smsAcknowledgemnt(ackMap);
//                                    } else {
//                                        System.out.println("Application date is future message not delivered");
//                                        ackMap.put("REPLY", "UNDELIVERD");
//                                        smsAcknowledgemnt(ackMap);
//                                    }
                                }
                            }
                            smsMap.clear();
                            smsMap = null;
                            ackMap = null;
                        } else {
                            System.out.println("Mobile Banking settings not configured properly, please enable from smstemplate server jboss bin folder : ");
                        }
                    }
                } catch (Exception e) {
                    ackMap.put("REPLY", "UNDELIVERD");
                    try {
                        smsAcknowledgemnt(ackMap);
                    } catch (SQLException ex) {
                        Logger.getLogger(SmsConfigDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return reply;
    }

    public void smsAcknowledgemnt(HashMap dataMap) throws SQLException {
        smsAcknoldgmentTO smsAcknoldgmentTO = new smsAcknoldgmentTO();
        try {
            System.out.println("smsAcknowledgemnt dataMap : " + dataMap + " _branchCode : " + _branchCode);
            if (dataMap != null && dataMap.size() > 0) {
                HashMap existMap = new HashMap();
                existMap.put("ACT_NUM", dataMap.get("ACT_NUM"));
                if (dataMap.containsKey("SMS_ACK_ID")) {
                    existMap.put("SMS_ID", dataMap.get("SMS_ACK_ID"));
                } else {
                    existMap.put("SMS_ID", "");
                }
                smsAcknoldgmentTO.setMessage(CommonUtil.convertObjToStr(dataMap.get("MESSAGE")));
                smsAcknoldgmentTO.setAcknoldgment(CommonUtil.convertObjToStr(dataMap.get("REPLY")));
                smsAcknoldgmentTO.setPhoneNo(CommonUtil.convertObjToStr(dataMap.get("PHONE_NO")));
                smsAcknoldgmentTO.setSendDt(ServerUtil.getCurrentDate(_branchCode));
                smsAcknoldgmentTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
                smsAcknoldgmentTO.setSmsModule(CommonUtil.convertObjToStr(dataMap.get("SMS_MODULE")));
                smsAcknoldgmentTO.setAcknowledgementId(CommonUtil.convertObjToStr(dataMap.get("ACKNOWLEDGEMENT_ID")));
                System.out.println("smsAcknowledgemnt existMap : " + existMap);
                List existList = sqlMap.executeQueryForList("getRecordExistTodaysDtorNot", existMap);
                if (existList != null && existList.size() > 0) {
                    existMap = (HashMap) existList.get(0);
                    smsAcknoldgmentTO.setSmsID(CommonUtil.convertObjToStr(existMap.get("SMS_ID")));
                    smsAcknoldgmentTO.setProcessedCount(CommonUtil.convertObjToInt(existMap.get("PROCESSED_COUNT")));
                    System.out.println("Record inserted todays date with this AcNo : " + existMap.get("ACT_NUM") + " Module : " + dataMap.get("SMS_MODULE") + "ACK ID : " + smsAcknoldgmentTO.getAcknowledgementId());
                } else {
                    smsAcknoldgmentTO.setSmsID(generateSmsAckID());
                }
                sqlMap.executeUpdate("insertSmsAcknolegmentTo", smsAcknoldgmentTO);//Merge function used in this query,incase record exist will update if not then will insert 
                dataMap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
        }
    }

    public void LoanMdsInstallmentRemider(HashMap instMap) {
        try {
            System.out.println("LoanMdsInstallmentRemider instMap#$@$" + instMap);
            String mobieBanking = readFromTemplate("MOBILE_BANKING");
            if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
                String mobileBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
                String mobileBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
                String mobileBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
                String smsServer = readFromTemplate("SMS_SERVER");
                String bankSMSDescription = readFromTemplate("BANK_SMS_DESCRIPTION");
                String smsAllowed = CommonUtil.convertObjToStr(ServerUtil.getCbmsParameterMap().get("SMS_ALLOWED"));
                if (mobileBankingUserName != null && mobileBankingPwd != null && mobileBankingSenderId != null && bankSMSDescription != null && CommonUtil.convertObjToStr(mobileBankingUserName).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingPwd).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingSenderId).length() > 0
                        && CommonUtil.convertObjToStr(bankSMSDescription).length() > 0 && smsAllowed != null && smsAllowed.equals("Y")) {
                    if (instMap != null && instMap.containsKey("LOAN_MDS_INSTALLMENT")) {
                        HashMap MdsInstMap = new HashMap();
                        HashMap LoanInstMap = new HashMap();
                        String message = "";
                        String messageTag = "";
                        String DuCount = readFromTemplate("InstDueCount");
                        String inOperativeCountTD = readFromTemplate("TdInoperativeCount");
                        if (DuCount != null && DuCount.length() > 0) {
                            MdsInstMap.put("COUNT", CommonUtil.convertObjToDouble(DuCount));
                            MdsInstMap.put("TD_INOPERATIVE_COUNT", CommonUtil.convertObjToDouble(inOperativeCountTD));
                            MdsInstMap.put("CURR_DT", curDt.clone());
                            List InstList = sqlMap.executeQueryForList("getDetailsForLoanMdsInstallments", MdsInstMap);
                            if (InstList != null && InstList.size() > 0) {
                                for (int i = 0; i < InstList.size(); i++) {
                                    instMap = (HashMap) InstList.get(i);
                                    if (instMap.get("PRODUCT") != null && CommonUtil.convertObjToStr(instMap.get("PRODUCT")).equalsIgnoreCase("MDS")) {
                                        messageTag = "MdsInstallmentRemider";
                                        String smsString = readFromTemplate(messageTag);
                                        if (smsString != null && smsString.length() > 0) {
                                            message = "";
                                            message = smsString.replaceAll("%%Schemename%%", CommonUtil.convertObjToStr(instMap.get("PROD_DESC")));
                                            message = message.replaceAll("%%chittalNo%%", CommonUtil.convertObjToStr(instMap.get("ACT_NUM")));
                                            message = message.replaceAll("%%instamount%%", CommonUtil.convertObjToStr(instMap.get("INSTALLMENT")));
                                            message = message.replaceAll("%%installmentDate%%", getFormattedFullDate((Date) instMap.get("INSTALLMENT_DT")));
                                            message = message.replaceAll("%%instNo%%", CommonUtil.convertObjToStr(instMap.get("INSTALLMENT_NO")));
                                            // Commented for KD-2442
                                            /*message = message + " (" + getFormattedFullDate() + ") - ";
                                            message += " - " + bankSMSDescription;
                                            message += " - " + mobileBankingSenderId;*/
                                        }
                                    } else if (instMap.get("PRODUCT") != null && CommonUtil.convertObjToStr(instMap.get("PRODUCT")).equalsIgnoreCase("LOAN")) {
                                        messageTag = "LoanInstallmentRemider";
                                        String smsString = readFromTemplate(messageTag);
                                        if (smsString != null && smsString.length() > 0) {
                                            HashMap whereMap=new HashMap();
                                            whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(instMap.get("ACT_NUM")));
                                            whereMap.put("FROM_DATE", getProperDateFormat(instMap.get("INSTALLMENT_DT")));
                                            double totalDue = 0.0;
                                            List lis = sqlMap.executeQueryForList("getLoanStatus", whereMap);
                                            if (lis != null && lis.size() > 0) {
                                                    HashMap tempResult = new HashMap();
                                                    for(int j=0;j<lis.size();j++){
                                                    tempResult = (HashMap) lis.get(j);
                                                    if (tempResult.containsKey("CHARGE_TYPE") && tempResult.get("CHARGE_TYPE").equals("PENAL INTEREST")) {
                                                        totalDue+=CommonUtil.convertObjToDouble(tempResult.get("BALANCE"));
                                                    }
                                                   if (tempResult.containsKey("CHARGE_TYPE") && tempResult.get("CHARGE_TYPE").equals("PRINCIPAL OVERDUE")) {
                                                        totalDue+=CommonUtil.convertObjToDouble(tempResult.get("BALANCE"));
                                                    }
                                                    if (tempResult.containsKey("CHARGE_TYPE") && tempResult.get("CHARGE_TYPE").equals("INTEREST")) {
                                                        totalDue+=CommonUtil.convertObjToDouble(tempResult.get("BALANCE"));
                                                    }
                                                    
//                                                    if (tempResult.containsKey("CHARGE_TYPE") && tempResult.get("CHARGE_TYPE").equals("PRINCIPAL")) {
//                                                        totalDue+=CommonUtil.convertObjToDouble(tempResult.get("BALANCE"));
//                                                    }
                                                }
                                            }
                                            message = "";
                                            message = smsString.replaceAll("%%Schemename%%", CommonUtil.convertObjToStr(instMap.get("PROD_DESC")));
                                            message = message.replaceAll("%%chittalNo%%", CommonUtil.convertObjToStr(instMap.get("ACT_NUM")));
                                            message = message.replaceAll("%%instamount%%", CommonUtil.convertObjToStr(totalDue));
                                            message = message.replaceAll("%%installmentDate%%", getFormattedFullDate((Date) instMap.get("INSTALLMENT_DT")));
                                            message = message.replaceAll("%%instNo%%", CommonUtil.convertObjToStr(instMap.get("INSTALLMENT_NO")));
                                            // Commented for KD-2442
                                            /*message = message + " (" + getFormattedFullDate() + ") - ";
                                            message += " - " + bankSMSDescription;
                                            message += " - " + mobileBankingSenderId;*/
                                        }
                                    } else if (instMap.get("PRODUCT") != null && CommonUtil.convertObjToStr(instMap.get("PRODUCT")).equalsIgnoreCase("DEPOSIT")) {
                                        if (CommonUtil.convertObjToDouble(instMap.get("TOTAL_COUNT")).doubleValue() > 1) {
                                            messageTag = "MaturedCustDepositRemider";
                                        } else {
                                            messageTag = "MaturedDepositRemider";
                                        }
                                        String smsString = readFromTemplate(messageTag);
                                        if (smsString != null && smsString.length() > 0) {
                                            message = "";
                                            HashMap depMap = new HashMap();
                                            MdsInstMap.put("CUST_ID", CommonUtil.convertObjToStr(instMap.get("ACT_NUM")));
                                            List DepList = sqlMap.executeQueryForList("getSingleCustomerDepositDetails", MdsInstMap);
                                            if (DepList != null && DepList.size() > 0) {
                                                depMap = (HashMap) DepList.get(0);
                                                System.out.println("depMap : " + depMap);
                                                instMap.put("MOBILE_NO", CommonUtil.convertObjToStr(depMap.get("MOBILE_NO")));
                                                instMap.put("INSTALLMENT_DT", depMap.get("INSTALLMENT_DT"));
                                                if (CommonUtil.convertObjToDouble(instMap.get("TOTAL_COUNT")) == 1) {
                                                    message = smsString.replaceAll("%%depositNo%%", CommonUtil.convertObjToStr(depMap.get("ACT_NUM")));
                                                    message = message.replaceAll("%%maturityDate%%", getFormattedFullDateWithoutTimeStamp((Date) depMap.get("INSTALLMENT_DT")));
                                                } else {
                                                    message = smsString;
                                                }
                                            }
                                        }
                                        // Commented fpr KD-2442
                                        /*message = message + " (" + getFormattedFullDate() + ") - ";
                                        message += " - " + bankSMSDescription;
                                        message += " - " + mobileBankingSenderId;*/
//                                    System.out.println("instMap : "+instMap+" message : "+message);
                                    } else if (instMap.get("PRODUCT") != null && CommonUtil.convertObjToStr(instMap.get("PRODUCT")).equalsIgnoreCase("RECURRING")) {
                                        messageTag = "RdInstallmentReminder";
                                        String smsString = readFromTemplate(messageTag);
                                        if (smsString != null && smsString.length() > 0) {
                                            message = "";
                                            message = smsString.replaceAll("%%depositNo%%", CommonUtil.convertObjToStr(instMap.get("ACT_NUM")));
                                            message = message.replaceAll("%%installmentDate%%", getFormattedFullDate((Date) instMap.get("INSTALLMENT_DT")));
                                            // Commented for Kd-2442
                                            /*message = message + " (" + getFormattedFullDate() + ") - ";
                                            message += " - " + bankSMSDescription;
                                            message += " - " + mobileBankingSenderId;*/
                                        }
                                    }
                                    //Chekcing for ready send sms
                                    if (message != null && message.length() > 0 && messageTag != null && messageTag.length() > 0) {
                                        System.out.println("LoanMdsInstallmentRemider message : " + message + " messageTag : " + messageTag);
                                        if (!checkSendSms(message, CommonUtil.convertObjToStr(instMap.get("ACT_NUM")), messageTag)) {
                                            System.out.println("LoanMdsInstallmentRemider message inside : " + message);
                                            sendSMS(message, CommonUtil.convertObjToStr(instMap.get("MOBILE_NO")), "", _branchCode, CommonUtil.convertObjToStr(instMap.get("ACT_NUM")), messageTag);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Mobile Banking settings not configured properly, please enable from smstemplate server jboss bin folder : ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void welcomeCustomer(HashMap phMap) {
        System.out.println("welcomeCustomer phMap#$@$" + phMap);
        try {
            String mobieBanking = readFromTemplate("MOBILE_BANKING");
            if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
                String mobileBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
                String mobileBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
                String mobileBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
                String smsServer = readFromTemplate("SMS_SERVER");
                String bankSMSDescription = readFromTemplate("BANK_SMS_DESCRIPTION");
                if (mobileBankingUserName != null && mobileBankingPwd != null && mobileBankingSenderId != null && bankSMSDescription != null
                        && CommonUtil.convertObjToStr(mobileBankingUserName).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingPwd).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingSenderId).length() > 0
                        && CommonUtil.convertObjToStr(bankSMSDescription).length() > 0) {
                    if (phMap != null && phMap.containsKey("CUSTOMER ID")) {
                        _branchCode = CommonUtil.convertObjToStr(phMap.get("BRANCH_ID"));
                        String smsString = readFromTemplate("WelcomeCustomer");
                        System.out.println("smsString$#$@$" + smsString);
                        if (smsString != null && smsString.length() > 0) {
                            List phList = sqlMap.executeQueryForList("getCustomerPhForWelcome", phMap);
                            if (phList != null && phList.size() > 0) {
                                phMap = (HashMap) phList.get(0);
                                String message = "";
                                message = smsString.replaceAll("%%custName%%", CommonUtil.convertObjToStr(phMap.get("CUST_NAME")));
                                // Commented for KD-2442
                                /*message = message + " (" + getFormattedFullDate() + ") - ";
                                message += " - " + bankSMSDescription;
                                message += " - " + mobileBankingSenderId;*/
                                if (message != null && message.length() > 0) {
                                    sendSMS(message, CommonUtil.convertObjToStr(phMap.get("PHONE_NUMBER")), "", _branchCode, CommonUtil.convertObjToStr(phMap.get("CUST_ID")), "WelcomeCustomer");
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Mobile Banking settings not configured properly, please enable from smstemplate server jboss bin folder : ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getwordFromDict(String word) {
        String malWord = "";
        ///  getMalWord(word);
        if (word != null && word.length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("ENG_WORD", word.toUpperCase().replace(",", "").trim());
            List lst = null;
            try {
                lst = (List) sqlMap.executeQueryForList("getMalayalamWord", whereMap);
            } catch (SQLException ex) {
                Logger.getLogger(SmsConfigDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (lst != null && lst.size() > 0) {
                HashMap one = (HashMap) lst.get(0);
                if (one != null && one.containsKey("M")) {
                    String mWord = CommonUtil.convertObjToStr(one.get("M"));
                    malWord += " " + mWord;
                } else {
                    malWord += " " + EngToMalTransliterator.get_ml(word);
                }
            } else {
                String arr[] = word.split(" ");
                for (int i = 0; i < arr.length; i++) {
                    whereMap = new HashMap();
                    whereMap.put("ENG_WORD", arr[i].toUpperCase().replace(",", "").trim());
                    try {
                        lst = (List) sqlMap.executeQueryForList("getMalayalamWord", whereMap);
                    } catch (SQLException ex) {
                        Logger.getLogger(SmsConfigDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (lst != null && lst.size() > 0) {
                        HashMap one = (HashMap) lst.get(0);
                        if (one != null && one.containsKey("M")) {
                            String mWord = CommonUtil.convertObjToStr(one.get("M"));
                            malWord += " " + mWord;
                        } else {
                            malWord += " " + EngToMalTransliterator.get_ml(arr[i]);
                        }

                    } else {
                        malWord += " " + EngToMalTransliterator.get_ml(arr[i]);
                    }
                }
            }
        }
        return malWord.trim();
    }

    public void commomSms(HashMap phMap) {
        System.out.println("commomSms phMap#$@$ IN commomSms" + phMap);
        try {
            String mobieBanking = readFromTemplate("MOBILE_BANKING");
            if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
                String mobileBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
                String mobileBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
                String mobileBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
                String smsServer = readFromTemplate("SMS_SERVER");
                String bankSMSDescription = readFromTemplate("BANK_SMS_DESCRIPTION");
                if (mobileBankingUserName != null && mobileBankingPwd != null && mobileBankingSenderId != null && bankSMSDescription != null
                        && CommonUtil.convertObjToStr(mobileBankingUserName).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingPwd).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingSenderId).length() > 0
                        && CommonUtil.convertObjToStr(bankSMSDescription).length() > 0) {
                    if (phMap != null && phMap.containsKey("CUST_CLASS")) {
                        _branchCode = CommonUtil.convertObjToStr(phMap.get("BRANCH_ID"));
                        String message = getwordFromDict(CommonUtil.convertObjToStr(phMap.get("SMS_CONTENT")));
                        System.out.println("smsString$#$@$" + URLEncoder.encode(message, "UTF-8"));
                        if (message != null && message.length() > 0) {
                            List phList = sqlMap.executeQueryForList("getDetaislCommonSMS", phMap);
                            if (phList != null && phList.size() > 0) {
                                for (int i = 0; i < phList.size(); i++) {
                                    phMap = (HashMap) phList.get(i);
                                    //Commented for KD-2442
                                    /*message = message + " (" + getFormattedFullDate() + ") - ";
                                    message += " - " + bankSMSDescription;
                                    message += " - " + mobileBankingSenderId;*/
                                    if (message != null && message.length() > 0) {
                                        //sendSMS(message, CommonUtil.convertObjToStr(phMap.get("PHONE_NUMBER")), true,CommonUtil.convertObjToStr(phMap.get("CUST_ID")),"CommomSMS");
                                    }
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Mobile Banking settings not configured properly, please enable from smstemplate server jboss bin folder : ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkSendSms(String message, String actNum, String smsModule) throws Exception {
        boolean checkFlag = false;
        String smsAckId = "";
        try {
            String mobieBanking = readFromTemplate("MOBILE_BANKING");
            if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
                String mobileBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
                String mobileBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
                String mobileBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
                String smsServer = readFromTemplate("SMS_SERVER");
                if (mobileBankingUserName != null && mobileBankingPwd != null && mobileBankingSenderId != null
                        && CommonUtil.convertObjToStr(mobileBankingUserName).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingPwd).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingSenderId).length() > 0) {
                    HashMap sendMap = new HashMap();
                    sendMap.put("MESSAGE", message);
                    sendMap.put("SEND_DT", curDt.clone());
                    sendMap.put("ACT_NUM", actNum);
                    sendMap.put("SMS_MODULE", smsModule);
                    List smsSendList = sqlMap.executeQueryForList("getSelectSendsms", sendMap);
                    System.out.println("smsSendList#$%$#%#$%#%" + smsSendList);
                    if (smsSendList != null && smsSendList.size() > 0) {
                        sendMap = (HashMap) smsSendList.get(0);
                        checkFlag = true;
                        smsAckId = CommonUtil.convertObjToStr(sendMap.get("SMS_ID"));
                    }
                } else {
                    System.out.println("Mobile Banking settings not configured properly, please enable from smstemplate server jboss bin folder : ");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return checkFlag;
    }

    private String generateSmsAckID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SMS_ACKNOLEDGMENT_ID");
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    private String getFormattedFullDate() throws Exception {
        java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return DATE_FORMAT.format(new Date());
    }

    private void destroyObjects() {
        objTO = null;
    }

    private HashMap callingFromTrueTransactMainDBBackup() throws Exception {
        java.util.Properties serverProperties = new java.util.Properties();
        HashMap dataBaseMap = new HashMap();
        try {
            Dummy cons = new Dummy();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
            dataBaseMap.put("URL_ADDRESS", serverProperties.getProperty("url"));
            dataBaseMap.put("USER_NAME", serverProperties.getProperty("username"));
            dataBaseMap.put("PASSWORD", serverProperties.getProperty("password"));
        } catch (Exception e) {
        }
        return dataBaseMap;
    }

    public String sendOTP(HashMap phMap) {
        System.out.println("sendOTP phMap : " + phMap);
        String recordAvailable = "";
        try {
            String mobieBanking = readFromTemplate("MOBILE_BANKING");
            if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
                String mobileBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
                String mobileBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
                String mobileBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
                String smsServer = readFromTemplate("SMS_SERVER");
                String bankSMSDescription = readFromTemplate("BANK_SMS_DESCRIPTION");
                if (mobileBankingUserName != null && mobileBankingPwd != null && mobileBankingSenderId != null && bankSMSDescription != null
                        && CommonUtil.convertObjToStr(mobileBankingUserName).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingPwd).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingSenderId).length() > 0
                        && CommonUtil.convertObjToStr(bankSMSDescription).length() > 0) {
                    //            if (phMap != null && phMap.containsKey("OTP_KEY")) {
                    if(phMap.containsKey("BRANCH_ID")){
                    	_branchCode = CommonUtil.convertObjToStr(phMap.get("BRANCH_ID"));
                    }else if(phMap.containsKey("BRANCH_CODE")){
                        _branchCode = CommonUtil.convertObjToStr(phMap.get("BRANCH_CODE"));
                    }else if(phMap.containsKey("QR_ACT_NUM")){
                        _branchCode = CommonUtil.convertObjToStr(phMap.get("QR_ACT_NUM")).substring(0, 4);
                    }
                    String smsString = readFromTemplate("otpMessage");
                    System.out.println("smsString$#$@$" + smsString);
                    if (smsString != null && smsString.length() > 0) {
                        List phList = sqlMap.executeQueryForList("getDetailsExistorNotinSMSSub", phMap);
                        if (phList != null && phList.size() > 0) {
                            HashMap phResultMap = (HashMap) phList.get(0);
                            String message = "";
                            message = smsString.replaceAll("%%otpPin%%", CommonUtil.convertObjToStr(phMap.get("OTP_NUM")));
                            // Commented for KD-2442
                            /*message = message + " (" + getFormattedFullDate() + ") - ";
                            message += " - " + bankSMSDescription;
                            message += " - " + mobileBankingSenderId;*/
                            if (message != null && message.length() > 0) {
                                recordAvailable = "recordExists";
                                sendSMS(message, CommonUtil.convertObjToStr(phResultMap.get("MOBILE_NO")), "", _branchCode, CommonUtil.convertObjToStr(phMap.get("QR_ACT_NUM")), "otpMessage");
                            }
                        }
                    } else {
                        recordAvailable = "recordDoesNotExist";
                        System.out.println("Record does not exist in sms subscription table,Please enter data on to that table ");
                    }
                } else {
                    recordAvailable = "serverConfigruationNotEnabled";
                    System.out.println("Mobile Banking settings not configured properly, please enable from smstemplate server jboss bin folder : ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recordAvailable;
    }

    public void getRechargeStatus(HashMap utilityMap) throws Exception {
        String url = "";
        HashMap resultMap = new HashMap();
        URLConnection urlConn = null;
        InputStreamReader inputReader = null;
        try {
            System.out.println("getRechargeStatus utilityMap : " + utilityMap);
            List list = sqlMap.executeQueryForList("getUtilityRechargeList", utilityMap);
            String utilityType = "";
            String debitActNum = "";
            String creditActNum = "";
            String statusResult = "";
            if (list != null && list.size() > 0) {
//                for (int i = 0; i < list.size(); i++) {
//                    resultMap = (HashMap) list.get(i);
                resultMap = (HashMap) list.get(0);
                statusResult = CommonUtil.convertObjToStr(utilityMap.get("STATUS"));
                if (utilityMap != null && utilityMap.containsKey("STATUS") && (statusResult.toUpperCase().equals("REVERSED") || statusResult.toUpperCase().equals("SUCCESS"))) {
                    double txnAmount = CommonUtil.convertObjToDouble(resultMap.get("RECHARGE_AMOUNT")).doubleValue();
                    //System.out.println("resultMap : " + resultMap);
                    utilityType = CommonUtil.convertObjToStr(resultMap.get("UTILITY_TYPE"));
                    debitActNum = CommonUtil.convertObjToStr(resultMap.get("APP_CREDIT_ACT_NUM"));//reversing from credited act and debiting now
                    creditActNum = CommonUtil.convertObjToStr(resultMap.get("APP_DEBIT_ACT_NUM"));//crediting into customer act num
//                    if(utilityType != null && utilityType.equals("Electricity")){
//                        url = "http://2.ponnusonline.com/webApi/ksebStatus.php?uid=" + resultMap.get("UTILITY_ID") + "&pin=" + resultMap.get("UTILITY_PIN") + "&txid=" + resultMap.get("TRANS_ID") + "&version=4";
//                    }else{
//                        url = "http://2.ponnusonline.com/webApi/rechargeStatus.php?uid=" + resultMap.get("UTILITY_ID") + "&pin=" + resultMap.get("UTILITY_PIN") + "&txid=" + resultMap.get("TRANS_ID") + "&version=4";
//                    }
//                    System.out.println("url : " + url);
//                    StringBuilder sb = new StringBuilder();
//                    URL urlOpen = new URL(url);
//                    urlConn = urlOpen.openConnection();
//                    HttpURLConnection conn = (HttpURLConnection) urlOpen.openConnection();
//                    conn.setRequestMethod("GET");
//                    conn.setRequestProperty("Accept", "application/json");
//                    if (urlConn != null) {
//                        urlConn.setReadTimeout(60 * 1800000);
//                    }
//                    if (urlConn != null && urlConn.getInputStream() != null) {                         
//                        inputReader = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
//                        BufferedReader bufferedReader = new BufferedReader(inputReader);
//                        if (bufferedReader != null) {
//                            int cp;
//                            while ((cp = bufferedReader.read()) != -1) {
//                                sb.append((char) cp);
//                            }
//                            bufferedReader.close();
//                        }
//                        inputReader.close();
//                    }
//                    System.out.println("sb output is : " + sb);
//                    int firstComma = nthsearch(sb.toString(), ',', 2);
//////                System.out.println("Position is:" + firstComma);
//                    int secondComma = nthsearch(sb.toString(), ',', 3);
////                System.out.println("Position is:" + secondComma);
//                    statusResult = sb.substring(firstComma, secondComma - 1);
//                    System.out.println("statusResult : " + statusResult + " txnId : " + resultMap.get("TRANS_ID"));
//                System.out.println("sb : " + sb);
                    if (statusResult != null && statusResult.length() > 0 && statusResult.toUpperCase().equals("REVERSED") && (debitActNum != null && debitActNum.length() > 0 && creditActNum != null && creditActNum.length() > 0)) {
//                    sqlMap.startTransaction();
                        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                        ArrayList transferList = new ArrayList();
                        TransferTrans objTransferTrans = new TransferTrans();
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        TransferDAO transferDAO = new TransferDAO();
                        HashMap data = new HashMap();
                        HashMap actResultMap = new HashMap();
                        actResultMap.put("ACT_NUM", debitActNum);
                        List list1 = sqlMap.executeQueryForList("getSelectRespectiveAcBranch", actResultMap);
                        if (list1 != null && list1.size() > 0) {
                            actResultMap = (HashMap) list1.get(0);
                            //System.out.println("resultMap : " + resultMap);
                            HashMap txMap = new HashMap();
                            if (debitActNum != null && debitActNum.length() > 0) {
                                HashMap acctHeadMap = new HashMap();
                                acctHeadMap.put("ACCOUNT_NO", debitActNum);
                                List list2 = sqlMap.executeQueryForList("getAcctHeadUsingActNum", acctHeadMap);
                                if (list2 != null && list2.size() > 0) {
                                    acctHeadMap = (HashMap) list2.get(0);
                                    txMap.put(TransferTrans.DR_AC_HD, acctHeadMap.get("ACCT_HEAD"));
                                }
                            } else {
//                                txMap.put(TransferTrans.DR_AC_HD, acctHeadMap.get("ACCT_HEAD"));
                            }
                            txMap.put(TransferTrans.DR_ACT_NUM, actResultMap.get("ACT_NUM"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, actResultMap.get("PROD_TYPE"));
                            txMap.put(TransferTrans.DR_PROD_ID, actResultMap.get("PROD_ID"));
                            txMap.put(TransferTrans.DR_BRANCH, actResultMap.get("BRANCH_ID"));
                            txMap.put(TransferTrans.PARTICULARS, "Reversed from " + utilityType + " Payment");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
                            txMap.put("LINK_BATCH_ID", creditActNum);
                            txMap.put("SingleTransId", "SingleTransId");
                            txMap.put("SCREEN_NAME", "Mobile Application");
                            txMap.put("TRANS_MOD_TYPE", actResultMap.get("PROD_TYPE"));
                            txMap.put("USER", CommonConstants.TTSYSTEM);
                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, txnAmount));
                            //System.out.println("Debit details : " + txMap);
                        }
                        actResultMap = new HashMap();
                        actResultMap.put("ACT_NUM", creditActNum);
                        list1 = sqlMap.executeQueryForList("getSelectRespectiveAcBranch", actResultMap);
                        if (list1 != null && list1.size() > 0) {
                            actResultMap = (HashMap) list1.get(0);
                            //System.out.println("resultMap : " + resultMap);
                            HashMap txMap = new HashMap();
                            HashMap acctHeadMap = new HashMap();
                            acctHeadMap.put("ACCOUNT_NO", creditActNum);
                            List list2 = sqlMap.executeQueryForList("getAcctHeadUsingActNum", acctHeadMap);
                            if (list2 != null && list2.size() > 0) {
                                acctHeadMap = (HashMap) list2.get(0);
                                txMap.put(TransferTrans.CR_AC_HD, acctHeadMap.get("ACCT_HEAD"));
                            }
                            txMap.put(TransferTrans.CR_ACT_NUM, actResultMap.get("ACT_NUM"));
                            txMap.put(TransferTrans.CR_PROD_TYPE, actResultMap.get("PROD_TYPE"));
                            txMap.put(TransferTrans.CR_PROD_ID, actResultMap.get("PROD_ID"));
                            txMap.put(TransferTrans.CR_BRANCH, actResultMap.get("BRANCH_ID"));
                            txMap.put(TransferTrans.PARTICULARS, "Reversed from " + utilityType + " Payment");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
                            txMap.put("LINK_BATCH_ID", creditActNum);
                            txMap.put("SingleTransId", "SingleTransId");
                            txMap.put("SCREEN_NAME", "Mobile Application");
                            txMap.put("TRANS_MOD_TYPE", actResultMap.get("PROD_TYPE"));
                            txMap.put("USER", CommonConstants.TTSYSTEM);
                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, txnAmount));
                            //System.out.println("Credit details : " + txMap);
                        }
                        objTransferTrans.setInitiatedBranch(CommonUtil.convertObjToStr(actResultMap.get("BRANCH_ID")));
                        transactionDAO.setInitiatedBranch(CommonUtil.convertObjToStr(actResultMap.get("BRANCH_ID")));//initiatedBranch
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(actResultMap.get("ACT_NUM")));
                        data.put("TxTransferTO", transferList);
                        data.put("COMMAND", "INSERT");
                        data.put("INITIATED_BRANCH", actResultMap.get("BRANCH_ID"));//initiatedBranch
                        data.put(CommonConstants.BRANCH_ID, actResultMap.get("BRANCH_ID"));
                        data.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
                        data.put(CommonConstants.MODULE, "Mobile Applicaiton");
                        data.put(CommonConstants.SCREEN, "Mobile Applicaiton");
                        data.put("MODE", "MODE");
                        data.put("LINK_BATCH_ID", creditActNum);
                        //System.out.println("data : " + data);
                        HashMap transMap = new HashMap();
                        transMap = transferDAO.execute(data, false);
                        //System.out.println("transMap : " + transMap);
                        String authorizeStatus = "AUTHORIZED";
                        HashMap transAuthMap = new HashMap();
                        transAuthMap.put(CommonConstants.BRANCH_ID, actResultMap.get("BRANCH_ID"));//initiatedBranch
                        transAuthMap.put("DAILY", "DAILY");
                        transAuthMap.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
                        TransactionDAO.authorizeCashAndTransfer(CommonUtil.convertObjToStr(creditActNum), authorizeStatus, transAuthMap);
                        resultMap.put("UTILITY_STATUS", statusResult);
                        sqlMap.executeUpdate("updateUtilityMasterStatus", resultMap);
                    } else if (statusResult != null && statusResult.length() > 0 && statusResult.toUpperCase().equals("SUCCESS")) {
                        resultMap.put("UTILITY_STATUS", statusResult.toUpperCase());
                        sqlMap.executeUpdate("updateUtilityMasterStatus", resultMap);
                    } else {
                        System.out.println("Not processed this transaction id : " + resultMap.get("TRANS_ID") + " statusResult : " + statusResult + " utilityType : " + utilityType);
                    }
                }
            } else {
                System.out.println("Already processed or record does not exist our DB this transaction id : " + utilityMap.get("TXN_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ShareCustomerSendingSMS(HashMap phMap) {
        System.out.println("ShareCustomerSendingSMS phMap : " + phMap);
        try {
            String mobieBanking = readFromTemplate("MOBILE_BANKING");
            if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
                String mobileBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
                String mobileBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
                String mobileBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
                String smsServer = readFromTemplate("SMS_SERVER");
                String bankSMSDescription = readFromTemplate("BANK_SMS_DESCRIPTION");
                if (mobileBankingUserName != null && mobileBankingPwd != null && mobileBankingSenderId != null && bankSMSDescription != null
                        && CommonUtil.convertObjToStr(mobileBankingUserName).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingPwd).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingSenderId).length() > 0
                        && CommonUtil.convertObjToStr(bankSMSDescription).length() > 0) {
                    _branchCode = CommonUtil.convertObjToStr(phMap.get("BRANCH_ID"));
                    curDt = ServerUtil.getCurrentDate(_branchCode);
                    String smsString = CommonUtil.convertObjToStr(phMap.get("MESSAGE"));
                    System.out.println("smsString$#$@$" + smsString);
                    if (smsString != null && smsString.length() > 0 && !phMap.isEmpty()) {
                        String message = "";
                        message = smsString;
                        // Commented for KD-2442
                        /*message = message + " (" + getFormattedFullDate() + ") - ";
                        message += " - " + bankSMSDescription;
                        message += " - " + mobileBankingSenderId;*/
                        if (message != null && message.length() > 0) {
                            sendSMS(message, CommonUtil.convertObjToStr(phMap.get("PHONE_NUMBER")),  "", _branchCode, CommonUtil.convertObjToStr(phMap.get("ACT_NUM")), CommonUtil.convertObjToStr(phMap.get("SMS_MODULE")));
                        }
                    }
                } else {
                    System.out.println("Mobile Banking settings not configured properly, please enable from smstemplate server jboss bin folder : ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void MultipletoSingleMSGForCustomerWise(HashMap phMap) {
        System.out.println("MultipletoSingleMSGForCustomerWise phMap : " + phMap);
        try {
            String mobieBanking = readFromTemplate("MOBILE_BANKING");
            if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
                String mobileBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
                String mobileBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
                String mobileBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
                String smsServer = readFromTemplate("SMS_SERVER");
                String bankSMSDescription = readFromTemplate("BANK_SMS_DESCRIPTION");
                if (mobileBankingUserName != null && mobileBankingPwd != null && mobileBankingSenderId != null && bankSMSDescription != null
                        && CommonUtil.convertObjToStr(mobileBankingUserName).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingPwd).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingSenderId).length() > 0
                        && CommonUtil.convertObjToStr(bankSMSDescription).length() > 0) {
                    if (phMap != null && phMap.containsKey("CUST_ID")) {
                        _branchCode = CommonUtil.convertObjToStr(phMap.get("BRANCH_ID"));
                        curDt = ServerUtil.getCurrentDate(_branchCode);
                        String smsString = readFromTemplate(CommonUtil.convertObjToStr(phMap.get("SMS_TEMPLATE")));
                        System.out.println("smsString$#$@$" + smsString);
                        if (smsString != null && smsString.length() > 0 && !phMap.isEmpty()) {
                            String message = "";
                            message = smsString;
                            if (phMap.containsKey("INT_AMT")) {
                                message = message.replaceAll("%%intAmt%%", CommonUtil.convertObjToStr(phMap.get("INT_AMT")));
                            }
                            if (phMap.containsKey("OA_BALANCE")) {
                                message = message.replaceAll("%%OAbalance%%", CommonUtil.convertObjToStr(phMap.get("OA_BALANCE")));
                            }
                            //Commented for KD-2442
                            /*message = message + " (" + getFormattedFullDate() + ") - ";
                            message += " - " + bankSMSDescription;
                            message += " - " + mobileBankingSenderId;*/
                            if (message != null && message.length() > 0) {
                                sendSMS(message, CommonUtil.convertObjToStr(phMap.get("PHONE_NUMBER")), "", _branchCode, CommonUtil.convertObjToStr(phMap.get("CUST_ID")), CommonUtil.convertObjToStr(phMap.get("SMS_MODULE")));
                            }
                        }
                    }
                } else {
                    System.out.println("Mobile Banking settings not configured properly, please enable from smstemplate server jboss bin folder : ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap getSMSStatusFromURL(HashMap map, String smsServer) throws Exception {
        String urlLink = (String) map.get("URL");
        urlLink = urlLink.replace("&", "~");
        String urlCombinedLink = "http://" + smsServer + ":3000/sendSMS/urlLink?key=" + urlLink;
        HashMap returnMap = new HashMap();
        String reply = "";
        java.io.BufferedReader in = null;
        try {
            //System.out.println("-->> getSMSStatusFromURL ... : " + urlLink + " urlCombinedLink : " + urlCombinedLink);
            java.net.URL myURL = new java.net.URL(urlCombinedLink);
            HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
            in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                reply = inputLine;
                returnMap.put("REPLY", reply);
            }
            if (in != null) {
                myURL = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("REPLY", "EXCEPTION");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return returnMap;
    }

    public void dueAccountSMS(HashMap phMap) {
        System.out.println("inside dueAccountSMS phMap : " + phMap);
        try {
            String mobieBanking = readFromTemplate("MOBILE_BANKING");
            if (mobieBanking != null && CommonUtil.convertObjToStr(mobieBanking).length() > 0 && CommonUtil.convertObjToStr(mobieBanking).equals("Y")) {
                String mobileBankingUserName = readFromTemplate("MOBILE_BANKING_USERNAME");
                String mobileBankingPwd = readFromTemplate("MOBILE_BANKING_PWD");
                String mobileBankingSenderId = readFromTemplate("MOBILE_BANKING_SENDERID");
                String smsServer = readFromTemplate("SMS_SERVER");
                String bankSMSDescription = readFromTemplate("BANK_SMS_DESCRIPTION");
                if (mobileBankingUserName != null && mobileBankingPwd != null && mobileBankingSenderId != null && bankSMSDescription != null
                        && CommonUtil.convertObjToStr(mobileBankingUserName).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingPwd).length() > 0
                        && CommonUtil.convertObjToStr(mobileBankingSenderId).length() > 0
                        && CommonUtil.convertObjToStr(bankSMSDescription).length() > 0) {
                    _branchCode = CommonUtil.convertObjToStr(phMap.get("BRANCH_ID"));
                    curDt = ServerUtil.getCurrentDate(_branchCode);
                    String smsString = CommonUtil.convertObjToStr(phMap.get("MESSAGE"));
                    System.out.println("smsString$#$@$" + smsString);
                    if (smsString != null && smsString.length() > 0 && !phMap.isEmpty()) {
                        String message = "";
                        message = smsString;
                        // Commented for KD-2442
                        /*message = message + " (" + getFormattedFullDate() + ") - ";
                        message += " - " + bankSMSDescription;
                        message += " - " + mobileBankingSenderId;*/
                        if (message != null && message.length() > 0) {
                            sendSMS(message, CommonUtil.convertObjToStr(phMap.get("PHONE_NUMBER")), "", _branchCode, CommonUtil.convertObjToStr(phMap.get("ACT_NUM")), CommonUtil.convertObjToStr(phMap.get("SMS_MODULE")));
                        }
                    }
                } else {
                    System.out.println("Mobile Banking settings not configured properly, please enable from smstemplate server jboss bin folder : ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
