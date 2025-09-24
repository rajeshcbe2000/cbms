/*
 * Copyright 2004 SeE Consulting (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of SeE Consulting (P) Ltd..
 * Use is subject to license terms.
 *
 * ElectronicPaymentDAO.java
 *
 * Created on Wed Nov 13 13:59:17 IST 2019
 * 
 * Created by Sathiya
 */
package com.see.truetransact.serverside.transaction.electronicpayment;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.*;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
//import static com.see.truetransact.serverutil.ServerUtil.isMac;
//import static com.see.truetransact.serverutil.ServerUtil.isSolaris;
//import static com.see.truetransact.serverutil.ServerUtil.isUnix;
//import static com.see.truetransact.serverutil.ServerUtil.isWindows;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 * ElectronicPayment DAO.
 *
 */
public class ElectronicPaymentDAO extends TTDAO {

    private SqlMap sqlMap = null;
    private String command = "";
    private LogDAO logDAO;
    private LogTO logTO;
    private TransactionDAO transactionDAO = null;
    private String branchID = "";
    TransferDAO transferDAO = new TransferDAO();
    private Date currDt = null;
    String MultipleBatchId = "";
    TransactionTO transactionTO = null;
    private HashMap acctHeadMap;
    HashMap returnMap = new HashMap();
    int processedCount = 0;
    double porcessedAmount = 0;
    boolean callFromOtherDAO = false;

    /**
     * Creates a new instance of ElectronicPaymentDAO
     */
    public ElectronicPaymentDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public ElectronicPaymentDAO(SqlMap sqlMap) throws ServiceLocatorException {
        this.sqlMap = sqlMap;
        callFromOtherDAO = true;
        System.out.println("@#@# ElectronicPaymentDAO constructor sqlMap:" + sqlMap);
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        return returnMap;
    }

    public static void main(String str[]) {
        try {
            ElectronicPaymentDAO dao = new ElectronicPaymentDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        HashMap returnData = null;
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("ElectronicPaymentDAO###### " + map);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setInitialValuesForLogTO(map);
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        branchID = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        String myIPAddress = "";
        String jbossIPAddress = CommonUtil.convertObjToStr(ServerUtil.getCbmsParameterMap().get("JBOSS_IP_ADDRESS"));
//        if (isWindows()) {
//            InetAddress myIP = InetAddress.getLocalHost();
//            myIPAddress = myIP.getHostAddress();
//            System.out.println("My Windows IP Address is : " + myIPAddress);
//        } else if (isMac()) {
//            System.out.println("This is Mac");
//        } else if (isUnix()) {
//            Process p = Runtime.getRuntime().exec("curl http://checkip.amazonaws.com");
//            int returnCode = p.waitFor();
//            if (returnCode == 0) {
//                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                myIPAddress = r.readLine();
//                System.out.println("My Linux/Unix IP Address is  : " + myIPAddress);
//                r.close();
//            }
//            System.out.println("This is Unix or Linux");
//        } else if (isSolaris()) {
//            System.out.println("This is Solaris");
//        } else {
//            System.out.println("Your OS is not support!!");
//        }

//        if (isUnix() && myIPAddress != null && myIPAddress.equals(jbossIPAddress)) {
        if (map.containsKey("FINAL_PAYMENT_LIST") && map.get("FINAL_PAYMENT_LIST") != null) {
            returnData = doCallElectronicPayment(map);
        } else {
            throw new NoCommandException();
        }
//        } else {
//            returnData = new HashMap();
//            returnData.put("NOT_REGISTERED", "MobileApplication cannot be processed, Because this not production environment...");
//        }
        destroyObjects();
        System.out.println("execute returnData : " + returnData);
        return returnData;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    public Date getProperFormatDate(Object obj) {
        Date dt = null;
        dt = currDt;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            dt = (Date) currDt.clone();
            dt.setDate(tempDt.getDate());
            dt.setMonth(tempDt.getMonth());
            dt.setYear(tempDt.getYear());
        }
        return dt;
    }

    public class ElectronicPaymentCallingActivity implements Runnable {

        List beneficieryist;
        CountDownLatch latch;
        HashMap finalMap;
        SqlMap sqlMap;
        SmsConfigDAO sms;

        public ElectronicPaymentCallingActivity(List beneficieryist, CountDownLatch latch, HashMap finalMap, SqlMap sqlMap) throws Exception {
            this.beneficieryist = beneficieryist;
            this.latch = latch;
            this.finalMap = finalMap;
            this.sqlMap = sqlMap;
            sms = new SmsConfigDAO();
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " : Started Task...");
            System.out.println("@#@# 1 ElectronicPaymentCallingActivity beneficieryist : " + beneficieryist);
            for (int i = 0; i < beneficieryist.size(); i++) {
                try {
                    sqlMap.startTransaction();
                    ArrayList eachList = (ArrayList) beneficieryist.get(i);
                    String actNum = CommonUtil.convertObjToStr(eachList.get(1));
                    Date batchDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(eachList.get(2)));
                    String name = CommonUtil.convertObjToStr(eachList.get(3));
                    String transType = CommonUtil.convertObjToStr(eachList.get(4));
                    String amount = CommonUtil.convertObjToStr(eachList.get(5));
                    String utrNumber = CommonUtil.convertObjToStr(eachList.get(6));
                    String particulars = CommonUtil.convertObjToStr(eachList.get(7));
                    String ifsccode = CommonUtil.convertObjToStr(eachList.get(8));
                    String senderActNum = CommonUtil.convertObjToStr(eachList.get(9));
                    String senderBankName = CommonUtil.convertObjToStr(eachList.get(10));
                    String senderBranchName = CommonUtil.convertObjToStr(eachList.get(11));
                    String utrNum = CommonUtil.convertObjToStr(eachList.get(12));
                    String mobileNo = CommonUtil.convertObjToStr(eachList.get(13));
//                    System.out.println("doCallElectronicPayment utrNum : " + utrNum + " amount : " + amount);
//                    String utrNumber = CommonUtil.convertObjToStr(eachList.get(12));
//                    System.out.println("doCallElectronicPayment amount : " + utrNumber + " amount : " + utrNumber);
                    HashMap paymentRequestMap = getPaymentRequestFromURL(actNum, batchDt, "", "AUTHORIZED", transType, name,
                            ifsccode, senderActNum, senderBankName, senderBranchName, particulars, amount, utrNum, mobileNo);
//                    paymentRequestMap.put("PAYMENT_REQUEST_STATUS", "ACCEPTED");
                    if (CommonUtil.convertObjToStr(paymentRequestMap.get("INQUIRY_REQUEST_STATUS")).equals("Transaction Successful")) {
//                        String smsAllowed = CommonUtil.convertObjToStr(ServerUtil.getCbmsParameterMap().get("ELECTRONIC_PAYMENT_SMS_ALLOWED"));
                        System.out.println("doCallElectronicPayment AFTER accepted paymentRequestMap : " + paymentRequestMap);
//                        if (smsAllowed.equals("Y")) {
//                            HashMap paymentSMSMap = new HashMap();
//                            if (paymentRequestMap.containsKey("SCREEN_NAME")
//                                    && CommonUtil.convertObjToStr(paymentRequestMap.get("SCREEN_NAME")).equals("INTEREST_APPLICATION")) {
//                                paymentSMSMap.put("ACTION_TYPE", "InterestPayoutNotification");
//                                HashMap tdsMap = new HashMap();
//                                tdsMap.put("ACT_NUM", CommonUtil.convertObjToStr(paymentRequestMap.get("ACCT_NUM")) + "_1");
//                                tdsMap.put("APPL_DT", getProperFormatDate(paymentRequestMap.get("TRANS_DT")));
//                                List tdsList = (List) sqlMap.executeQueryForList("getInterestRunTDSAmount", tdsMap);
//                                if (tdsList != null && tdsList.size() > 0) {
//                                    tdsMap = (HashMap) tdsList.get(0);
//                                    paymentSMSMap.put("TDS_AMT", CommonUtil.convertObjToStr(tdsMap.get("TDS_AMT")));
//                                } else {
//                                    paymentSMSMap.put("TDS_AMT", "0");
//                                }
//                                paymentSMSMap.put("TRANS_DT", CommonUtil.convertObjToStr(paymentRequestMap.get("TRANS_DT")));
//                            } else {
//                                paymentSMSMap.put("ACTION_TYPE", "MobileApplication");
//                            }
//                            paymentSMSMap.put("REQUEST_ID", "");
//                            paymentSMSMap.put("DEPOSIT_AMT", amount);
//                            paymentSMSMap.put("DEPOSIT_NO", actNum);
//                            paymentSMSMap.put("SENDER_ACT_NUM", senderActNum);
//                            System.out.println("paymentSMSMap : " + paymentSMSMap);
////                            sms.callSMS(paymentSMSMap);
//                        }
//                        String typeOfAPICalling = CommonUtil.convertObjToStr(paymentRequestMap.get("PAYMENT_TYPE"));
                        String xmlStatus = "";
                        paymentRequestMap.put("ACT_NUM", actNum);
                        paymentRequestMap.put("USER_ID", CommonUtil.convertObjToStr(finalMap.get("USER_ID")));
                        paymentRequestMap.put("PARTICULARS", particulars);
                        HashMap transactionMap = electronicPaymentTransaction(paymentRequestMap);
//                        System.out.println("transactionMap actNum : " + transactionMap);
                        processedCount = processedCount + 1;
                        porcessedAmount = porcessedAmount + CommonUtil.convertObjToDouble(amount);
                        returnMap.put("TO_BATCH_ID", transactionMap.get("TRANS_ID"));
                        if (i == 0) {
                            returnMap.put("FROM_BATCH_ID", transactionMap.get("TRANS_ID"));
                        } else if (i == beneficieryist.size()) {
                            returnMap.put("FROM_BATCH_ID", transactionMap.get("TRANS_ID"));
                        }
                        returnMap.put("PROCESSED_COUNT", processedCount);
                        returnMap.put("PROCESSED_AMT", porcessedAmount);
                        returnMap.put("PAYMENT_REQUEST_STATUS", CommonUtil.convertObjToStr(paymentRequestMap.get("PAYMENT_REQUEST_STATUS")));
                        returnMap.put("INQUIRY_REQUEST_STATUS", CommonUtil.convertObjToStr(paymentRequestMap.get("INQUIRY_REQUEST_STATUS")));
                        returnMap.put("INQUIRY_STATUS", xmlStatus);
                    } else {
                        returnMap.put("PROCESSED_COUNT", processedCount);
                        returnMap.put("PROCESSED_AMT", porcessedAmount);
                        returnMap.put("PAYMENT_REQUEST_STATUS", CommonUtil.convertObjToStr(paymentRequestMap.get("PAYMENT_REQUEST_STATUS")));
                        returnMap.put("INQUIRY_REQUEST_STATUS", CommonUtil.convertObjToStr(paymentRequestMap.get("INQUIRY_REQUEST_STATUS")));
//                        returnMap.put("INQUIRY_STATUS", xmlStatus);
                    }
                    System.out.println("inside run doCallElectronicPayment returnMap : " + returnMap);
                    sqlMap.commitTransaction();
                } catch (Exception e) {
                    try {
                        if (!callFromOtherDAO) {
                            sqlMap.rollbackTransaction();
                        }
                        e.printStackTrace();
                    } catch (SQLException ex) {
                        Logger.getLogger(ElectronicPaymentDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            System.out.println(threadName + " : Completed Task...");
            latch.countDown();
        }
    }

    private HashMap doCallElectronicPayment(HashMap map) throws Exception {
//        System.out.println("doCallElectronicPayment map : " + map);
//        HashMap transMap = new HashMap();
        ArrayList finalList = (ArrayList) map.get("FINAL_PAYMENT_LIST");
        System.out.println("doCallElectronicPayment finalList : " + finalList);
//        int processedCount = 0;
//        double porcessedAmount = 0;
        int threadCount = CommonUtil.convertObjToInt(ServerUtil.getCbmsParameterMap().get("ELECTRONIC_PAYMENT_THREAD_COUNT"));
        int latchCount = Math.round(finalList.size() / threadCount);
        latchCount += finalList.size() % threadCount > 0 ? 1 : 0;
        CountDownLatch latch = new CountDownLatch(latchCount);
        if (finalList != null && finalList.size() > 0) {
            List tempList = new ArrayList();
            int threadNo = 0;
            for (int i = 0; i < finalList.size(); i++) {
                if ((i + 1) % threadCount == 0) {
                    System.out.println("#$#$ " + (i + 1) + " tempList:" + tempList);
                    new Thread(new ElectronicPaymentCallingActivity(tempList, latch, map, sqlMap), "T" + (threadNo++)).start();
                    tempList = new ArrayList();
                }
                tempList.add(finalList.get(i));
            }
            if (tempList != null && tempList.size() > 0) {
                System.out.println("#$#$ last broken tempList:" + tempList);
                new Thread(new ElectronicPaymentCallingActivity(tempList, latch, map, sqlMap), "T" + (threadNo++)).start();
            }
            latch.await();
        }
//    }
        System.out.println("doCallElectronicPayment returnMap : " + returnMap);
        return returnMap;
    }

    private void destroyObjects() {
        transactionTO = null;
        processedCount = 0;
        porcessedAmount = 0;
    }

    public HashMap getPaymentRequestFromURL(String actNum, Date batchDt, String transId, String authorizeStatus, String productId,
            String name, String ifsccode, String senderActNum, String senderBankName, String senderBranchName, String particulars, String amount, String utrNumber, String mobileNumber) throws Exception {
//        System.out.println("getPaymentRequestFromURL : " + actNum + " batchDt : " + batchDt + " transId : " + transId + "senderBankName : " + senderBankName + " senderBranchName : " + senderBranchName);
//        System.out.println("productId : " + productId + " name : " + name + " ifsccode : " + ifsccode + " senderActNum : " + senderActNum);
//        System.out.println("@#@# Thread :  / calling after some time 20 * 1000 : ");
        String xmlStatus = "";
        GregorianCalendar cal = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        format.setCalendar(cal);
//        String transDtWithTimeStamp = format.format(curDate.clone());
        String transDtWithTimeStamp = format.format(cal.getTime());
        format = new SimpleDateFormat("yyyy-MM-dd");
        format.setCalendar(cal);
//        String transDt = format.format(curDate.clone());
        String transDt = format.format(cal.getTime());
//        System.out.println("result : " + transDtWithTimeStamp + " transDt : " + transDt);
        String typeOfAPICalling = "";
        HashMap rtgsNeftMap = new HashMap();
        StringBuffer stringBuffer = new StringBuffer();
        rtgsNeftMap.put("ACT_NUM", actNum);
        rtgsNeftMap.put("UTR_NUMBER", utrNumber);
        rtgsNeftMap.put("BATCH_DT", getProperFormatDate(batchDt));
        rtgsNeftMap.put("SENDER_ACT_NUM", senderActNum);
        rtgsNeftMap.put("IFSC_CODE", ifsccode);
        rtgsNeftMap.put("AMOUNT", amount);
        System.out.println("@#@# 2 getPaymentRequestFromURL before calling query sqlMap : " + rtgsNeftMap);
//        List lst = sqlMap.executeQueryForList("getElectronicPaymentDetails", rtgsNeftMap);
//        System.out.println("@#@# 2 getPaymentRequestFromURL before calling query sqlMap : " + lst.size() + " lst OutPut : " + lst);
//        if (lst != null && lst.size() > 0) {
//            rtgsNeftMap = (HashMap) lst.get(0);
//            xmlStatus = CommonUtil.convertObjToStr(rtgsNeftMap.get("PAYMENT_REQUEST_STATUS"));
//            if (xmlStatus.length() > 0 && xmlStatus.equals("ACCEPTED")) {
//                System.out.println("Already accepted the request : " + CommonUtil.convertObjToStr(rtgsNeftMap.get("REQUEST_PAYMENT_REF_NO")));
//            } else if (CommonUtil.convertObjToStr(authorizeStatus).equals("AUTHORIZED")) {
        System.out.println("before calling api rtgsNeftMap : " + rtgsNeftMap);
        HashMap utrMap = new HashMap();
        utrMap.put("API_TYPE", "PAYMENT");
        utrMap.put("BATCH_NUMBER", "");
        utrMap.put("API_REFERENCE_NUM", "");
        utrMap.put("UTR_NUMBER", rtgsNeftMap.get("UTR_NUMBER"));
        utrMap.put("BANK_REF_NUM", "");
        utrMap.put("API_RESPONSE_STATUS", "");
        utrMap.put("BANK_INQUIRY_REF_NO", "");
        utrMap.put("BANK_INQUIRY_RES_DT", "");
        System.out.println("before calling api utrMap : " + utrMap);
        sqlMap.executeUpdate("insertElectronicPaymentHistory", utrMap);
        String otherBankIFSCode = ifsccode;
//        String amount = CommonUtil.convertObjToStr(rtgsNeftMap.get("AMOUNT"));
        String custdetadd1 = senderBranchName;
        String custdetadd2 = senderBranchName;
        String custdet = "testdet";
        String beneficiaryName = name;
        String batchnumext = getElecronicBatchNum();
        String paymentrefno = "";//check it
        String remarks = particulars;
        String paymentrefnoDR = "";
        String paymentrefnoCR = "";
        String mobileNo = "";
        paymentrefno = getElecronicPaymentRequestRefNo();
        String narration = "";
        if (rtgsNeftMap.containsKey("PARTICUALRS") && CommonUtil.convertObjToStr(rtgsNeftMap.get("PARTICUALRS")).length() > 0) {
            narration = CommonUtil.convertObjToStr(rtgsNeftMap.get("PARTICUALRS"));
        } else {
            narration = "transaction";
        }
        String emailId = "";
        if (rtgsNeftMap.containsKey("EMAIL_ID") && CommonUtil.convertObjToStr(rtgsNeftMap.get("EMAIL_ID")).length() > 0) {
            emailId = CommonUtil.convertObjToStr(rtgsNeftMap.get("EMAIL_ID"));
        } else {
            emailId = "a@a.com";
        }
        if (CommonUtil.convertObjToStr(productId).equals("RTGS") || CommonUtil.convertObjToStr(productId).equals("NEFT")) {
            if (CommonUtil.convertObjToStr(productId).equals("RTGS")) {
                typeOfAPICalling = "rtgs";
            } else {
                typeOfAPICalling = "neft";
            }
            if (beneficiaryName.length() > 35) {
                beneficiaryName = beneficiaryName.substring(0, 35);
                beneficiaryName = beneficiaryName.replaceAll("[^a-zA-Z0-9]", " ");
            }
            if (custdetadd1.length() > 35) {
                custdetadd1 = custdetadd1.substring(0, 35);
                custdetadd1 = custdetadd1.replaceAll("[^a-zA-Z0-9]", " ");
            }
            if (custdetadd2.length() > 35) {
                custdetadd2 = custdetadd2.substring(0, 35);
                custdetadd2 = custdetadd2.replaceAll("[^a-zA-Z0-9]", " ");
            }
            if (narration.length() > 35) {
                narration = narration.substring(0, 35);
                narration = narration.replaceAll("[^a-zA-Z0-9]", " ");
            }
            System.out.println("neft beneficiaryName : " + beneficiaryName + " custdetadd1 : " + custdetadd1 + " custdetadd2 : " + custdetadd2);
        } else if (CommonUtil.convertObjToStr(productId).equals("IMPS")) {
            typeOfAPICalling = "imps";
//                    paymentrefnoCR = getElecronicPaymentRefNo();//12 digit number debit transaction
            paymentrefno = getElecronicPaymentRefNo();//12 digit number credit transaction
            paymentrefnoCR = "&paymentrefnoCR=" + paymentrefno;
//                    System.out.println("imps beneficiaryName : " + beneficiaryName + " custdetadd1 : " + custdetadd1 + " custdetadd2 : " + custdetadd2 + " remarks : " + remarks);
        } else if (CommonUtil.convertObjToStr(productId).equals("DIRECT")) {
            typeOfAPICalling = "a2a";
            if (custdetadd1.length() > 30) {
                custdetadd1 = custdetadd1.substring(0, 30);
                custdetadd1 = custdetadd1.replaceAll("[^a-zA-Z0-9]", " ");
            }
            if (custdetadd2.length() > 30) {
                custdetadd2 = custdetadd2.substring(0, 30);
                custdetadd2 = custdetadd2.replaceAll("[^a-zA-Z0-9]", " ");
            }
            if (remarks.length() > 40) {
                remarks = remarks.substring(0, 40);
                remarks = remarks.replaceAll("[^a-zA-Z0-9]", " ");
            }
            paymentrefnoDR = getElecronicPaymentRefNo();//12 digit number debit transaction
            paymentrefno = getElecronicPaymentRefNo();//12 digit number credit transaction
            paymentrefnoDR = "&paymentrefnoDR=" + paymentrefnoDR;
//                    System.out.println("a2a beneficiaryName : " + beneficiaryName + " custdetadd1 : " + custdetadd1 + " custdetadd2 : " + custdetadd2 + " remarks : " + remarks);
        }
        rtgsNeftMap.put("PAYMENT_BATCH_NUMBER", batchnumext);
        rtgsNeftMap.put("PAYMENT_REF_NUM", paymentrefno);
        String uniqueId = paymentrefno;
        String bankName = senderBankName;
        String branchName = senderBranchName;
        String sourceScreen = "";
        actNum = "actNum=" + actNum;
        String senderActNo = "&senderActNo=" + senderActNum;
        otherBankIFSCode = "&otherBankIFSCode=" + ifsccode;
        amount = "&amount=" + amount;
        String prodId = "&prodId=" + productId;
        beneficiaryName = "&beneficiaryName=" + beneficiaryName;
        transDt = "&transDt=" + transDt;
        transDtWithTimeStamp = "&transDtWithTimeStamp=" + transDtWithTimeStamp;
        uniqueId = "&uniqueId=" + uniqueId;
        bankName = "&bankName=" + bankName;
        branchName = "&branchName=" + branchName;
        remarks = "&remarks=" + remarks;
        batchnumext = "&batchnumext=" + batchnumext;
        paymentrefno = "&paymentrefno=" + paymentrefno;
        custdetadd1 = "&custdetadd1=" + custdetadd1;
        custdetadd2 = "&custdetadd2=" + custdetadd2;
        custdet = "&custdet=" + custdet;
        narration = "&narration=" + narration;
        emailId = "&emailId=" + emailId;
        utrNumber = "&utrNumber=" + utrNumber;
        sourceScreen = "&sourceScreen=" + sourceScreen;
        mobileNo = "&mobileNo=" + mobileNumber;
        actNum = actNum.replace(" ", "%20");
        senderActNo = senderActNo.replace(" ", "%20");
        otherBankIFSCode = otherBankIFSCode.replace(" ", "%20");
        amount = amount.replace(" ", "%20");
        prodId = prodId.replace(" ", "%20");
        uniqueId = uniqueId.replace(" ", "%20");
        bankName = bankName.replace(" ", "%20");
        branchName = branchName.replace(" ", "%20");
        beneficiaryName = beneficiaryName.replace(" ", "%20");
        remarks = remarks.replace(" ", "%20");
        System.out.println("typeOfAPICalling : " + typeOfAPICalling);
        String productionIPAddress = "";//ServerUtil.getElectronicPaymentProductionIP();
//                String jbossIPAddress = CommonUtil.convertObjToStr(ServerUtil.getCbmsParameterMap().get("JBOSS_IP_ADDRESS"));
        URL url = null;
//                if (isUnix() && productionIPAddress != null && productionIPAddress.equals(jbossIPAddress)) {
//                    System.out.println("Calling to production environment productionIPAddress : " + productionIPAddress+" jbossIPAddress : "+jbossIPAddress);
        url = new URL("http://202.21.32.28:8090/rtgsneftapi/" + typeOfAPICalling + "/doPayment?");
//                }else{
//                System.out.println("Calling to UAT environment : " + productionIPAddress + " jbossIPAddress : " + jbossIPAddress);
//                url = new URL("http://" + jbossIPAddress + ":8090/rtgsneftapi/" + typeOfAPICalling + "/doPayment?");
//                }
        if (url != null) {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String data = "";
            if (CommonUtil.convertObjToStr(productId).equals("DIRECT")) {
                data = actNum + senderActNo + otherBankIFSCode
                        + amount + prodId + beneficiaryName + transDt
                        + uniqueId + bankName + branchName + remarks + batchnumext + paymentrefno
                        + custdetadd1 + custdetadd2 + custdet + transDtWithTimeStamp + paymentrefnoDR + utrNumber + sourceScreen + mobileNo;
            } else if (CommonUtil.convertObjToStr(productId).equals("IMPS")) {
                data = actNum + senderActNo + otherBankIFSCode
                        + amount + prodId + beneficiaryName + transDt
                        + uniqueId + bankName + branchName + remarks + batchnumext + paymentrefno
                        + custdetadd1 + custdetadd2 + custdet + transDtWithTimeStamp + paymentrefnoCR + emailId + utrNumber + sourceScreen + mobileNo;
            } else {
                data = actNum + senderActNo + otherBankIFSCode
                        + amount + prodId + beneficiaryName + transDt
                        + uniqueId + bankName + branchName + remarks + batchnumext + paymentrefno
                        + custdetadd1 + custdetadd2 + custdet + transDtWithTimeStamp + narration + emailId + utrNumber + sourceScreen + mobileNo;
            }
//            data = data;
            System.out.println("data : " + data);
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
//                System.out.println("conn : " + conn + "request method : " + conn.getRequestMethod());
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String reply = "";
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();
            rtgsNeftMap.put("API_TYPE", "PAYMENT");
            rtgsNeftMap.put("BANK_INQUIRY_RES_DT", "");
            rtgsNeftMap.put("BANK_INQUIRY_REF_NO", "");
            rtgsNeftMap.put("API_REFERENCE_NUM", CommonUtil.convertObjToStr(rtgsNeftMap.get("PAYMENT_REF_NUM")));
            rtgsNeftMap.put("BATCH_NUMBER", CommonUtil.convertObjToStr(rtgsNeftMap.get("PAYMENT_BATCH_NUMBER")));
            rtgsNeftMap.put("PAYMENT_TYPE", typeOfAPICalling);
            rtgsNeftMap.put("RTGS_ID", CommonUtil.convertObjToStr(rtgsNeftMap.get("RTGS_ID")));
            rtgsNeftMap.put("ACT_NUM", CommonUtil.convertObjToStr(rtgsNeftMap.get("ACT_NUM")));
            System.out.println("stringBuffer : " + stringBuffer.toString());
            JSONObject jsonObj = new JSONObject(stringBuffer.toString());
            String Status = jsonObj.getString("success");
            int actCode = jsonObj.getInt("ActCode");
            int transRefNo = jsonObj.getInt("TranRefNo");
            int bankRRN = jsonObj.getInt("BankRRN");
            String apiResponse = jsonObj.getString("Response");
            System.out.println("mobileNumber : " + mobileNumber + " Response : " + apiResponse);
            System.out.println("TranRefNo : " + transRefNo);
            System.out.println("ActCode : " + actCode);
            System.out.println("BankRRN : " + bankRRN);
            System.out.println("success : " + Status);
            rtgsNeftMap.put("BANK_REF_NUM", String.valueOf(transRefNo));
            rtgsNeftMap.put("PAYMENT_REQUEST_STATUS", "ACCEPTED");
            rtgsNeftMap.put("INQUIRY_REQUEST_STATUS", apiResponse);
            rtgsNeftMap.put("API_RESPONSE_STATUS", apiResponse);
            rtgsNeftMap.put("BANK_INQUIRY_REF_NO", String.valueOf(bankRRN));
            rtgsNeftMap.put("PAYMENT_REQUEST_NO", String.valueOf(bankRRN));
//            if (Status.equals("true") && actCode == 0) {
//                rtgsNeftMap.put("PAYMENT_REQUEST_STATUS", apiResponse);
//                rtgsNeftMap.put("PAYMENT_REQUEST_NO", bankRRN);
////                    returnMap.put("REQUEST_STATUS", xmlStatus);
//                returnMap.put("INST_TYPE", productId);
//                returnMap.put("API_REFERENCE_NUM", CommonUtil.convertObjToStr(rtgsNeftMap.get("PAYMENT_REF_NUM")));
//                rtgsNeftMap.put("BANK_REF_NUM", bankRRN);
//            } else {
//                System.out.println("connection timed out");
//            }
            rtgsNeftMap.put("INST_TYPE", productId);
            rtgsNeftMap.put("PAYMENT_ACCEPTED_DT", currDt.clone());

            sqlMap.executeUpdate("updatePaymentRequestStatus", rtgsNeftMap);
            sqlMap.executeUpdate("insertElectronicPaymentHistory", rtgsNeftMap);
        }
//            } else {
//                xmlStatus = "";
//                rtgsNeftMap.put("PAYMENT_REQUEST_STATUS", xmlStatus);
//            }
//        }
        return rtgsNeftMap;
    }

    private String getElecronicBatchNum() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "ELECTRONIC_BATCH_NUM");
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return batchID;
    }

    private String getElecronicPaymentRefNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "ELECTRONIC_PAYMENT_REF_NO");
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return batchID;
    }

    private String getElecronicPaymentRequestRefNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "ELECTRONIC_PAYMENT_REQ_REF_NO");
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return batchID;
    }

    private HashMap electronicPaymentTransaction(HashMap paymentRequestMap) throws Exception {
        System.out.println("ElectronicPaymentTransaction paymentRequestMap : " + paymentRequestMap);
        HashMap transMap = new HashMap();
        TransferTrans trans = new TransferTrans();
        String actsWithOtherBankGL = CommonUtil.convertObjToStr(ServerUtil.getCbmsParameterMap().get("ELECTRONIC_PAYMENT_CREDIT_GL"));
        trans.setInitiatedBranch(branchID);
        HashMap txMap = new HashMap();
        _branchCode = branchID;
        String BRANCH_ID = _branchCode;
        double transAmt = 0.0;
        transAmt = CommonUtil.convertObjToDouble(paymentRequestMap.get("AMOUNT"));
        ArrayList transferList = new ArrayList();
        TransferTrans transferTrans = new TransferTrans();
        transferTrans.setInitiatedBranch(BRANCH_ID);
        TransferTrans objTransferTrans = new TransferTrans();
        objTransferTrans.setInitiatedBranch(_branchCode);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(actsWithOtherBankGL));
        transactionDAO.setScreenName("MobileApplication");
        HashMap otherBankMap = new HashMap();
        otherBankMap.put("ACT_MASTER_ID", actsWithOtherBankGL);
        List otherBankList = (List) sqlMap.executeQueryForList("getOtherAccNoHeaddDet", otherBankMap);
        if (otherBankList != null && otherBankList.size() > 0) {
            otherBankMap = (HashMap) otherBankList.get(0);
        }
        HashMap debitActMap = new HashMap();
        debitActMap.put("ACT_NUM", CommonUtil.convertObjToStr(paymentRequestMap.get("ACT_NUM")));
        List debitActList = (List) sqlMap.executeQueryForList("getSelectRespectiveAcBranch", debitActMap);
        if (debitActList != null && debitActList.size() > 0) {
            debitActMap = (HashMap) debitActList.get(0);
        }
        if (transAmt > 0 && debitActList != null && otherBankList != null) {
            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(debitActMap.get("AC_HD_ID")));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(debitActMap.get("PROD_ID")));
            txMap.put(TransferTrans.PARTICULARS, paymentRequestMap.get("PARTICULARS"));
            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(paymentRequestMap.get("ACT_NUM")));
            txMap.put("AUTHORIZEREMARKS", "MobileApplication");
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            txMap.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
            txMap.put(CommonConstants.SCREEN, "MobileApplication");
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
            txMap.put("NEFT_ECS_UTR_NUMBER", paymentRequestMap.get("UTR_NUMBER"));
            txMap.put("SCREEN_NAME", "MobileApplication");
            txMap.put("GL_TRANS_ACT_NUM", CommonUtil.convertObjToStr(paymentRequestMap.get("ACT_NUM")));
            txMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(paymentRequestMap.get("ACT_NUM")));
            transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmt));

            txMap = new HashMap();
            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(otherBankMap.get("PRINCIPAL_AC_HD")));
            txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(otherBankMap.get("PROD_ID")));
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
            txMap.put(TransferTrans.CR_ACT_NUM, actsWithOtherBankGL);
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.OTHERBANKACTS);
            txMap.put(TransferTrans.PARTICULARS, paymentRequestMap.get("PARTICULARS"));
            txMap.put(TransferTrans.DR_INSTRUMENT_2, "AB_PRINCIPAL");
            txMap.put("NARRATION", paymentRequestMap.get("PARTICULARS"));
            txMap.put("AUTHORIZEREMARKS", "MobileApplication");
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
            txMap.put(CommonConstants.SCREEN, "MobileApplication");
            txMap.put(TransferTrans.PARTICULARS, paymentRequestMap.get("PARTICULARS"));
            txMap.put("NARRATION", paymentRequestMap.get("PARTICULARS"));
            txMap.put("NEFT_ECS_UTR_NUMBER", paymentRequestMap.get("UTR_NUMBER"));
            txMap.put("SCREEN_NAME", "MobileApplication");
            txMap.put("GL_TRANS_ACT_NUM", actsWithOtherBankGL);
            txMap.put("LINK_BATCH_ID", actsWithOtherBankGL);
            transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmt));
            if (transferList != null && transferList.size() > 0) {
                transMap = doDebitCredit(transferList, _branchCode, paymentRequestMap, CommonUtil.convertObjToStr(actsWithOtherBankGL));
            }
        } else {
            System.out.println("Please check account details : " + paymentRequestMap);
        }
        return transMap;
    }

    private HashMap doDebitCredit(ArrayList batchList, String branchCode, HashMap map, String bulkID) throws Exception {
        HashMap transMap = new HashMap();
        try {
            TransferDAO transferDAO = new TransferDAO();
            HashMap data = new HashMap();
            data.put("TxTransferTO", batchList);
            data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
            data.put("INITIATED_BRANCH", _branchCode);
            data.put(CommonConstants.BRANCH_ID, branchCode);
            data.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
            data.put(CommonConstants.MODULE, map.get(CommonConstants.MODULE));
            data.put(CommonConstants.SCREEN, "MobileApplication");
            data.put("MODE", "MODE");
            data.put("LINK_BATCH_ID", bulkID);
            transMap = transferDAO.execute(data, false);
            String authorizeStatus = "AUTHORIZED";
            HashMap transAuthMap = new HashMap();
            transAuthMap.put(CommonConstants.BRANCH_ID, branchCode);
            transAuthMap.put("DAILY", "DAILY");
            transAuthMap.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
            transAuthMap.put("BATCH_ID", CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
            String actsWithOtherBankGL = CommonUtil.convertObjToStr(ServerUtil.getCbmsParameterMap().get("ELECTRONIC_PAYMENT_CREDIT_GL"));
            TransactionDAO.authorizeCashAndTransfer(CommonUtil.convertObjToStr(actsWithOtherBankGL), authorizeStatus, transAuthMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return transMap;
    }
}
