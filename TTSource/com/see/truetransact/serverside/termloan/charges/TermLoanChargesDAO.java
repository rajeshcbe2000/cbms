/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AgentDAO.java
 *
 * Created on Wed Feb 02 13:11:28 IST 2005
 */
package com.see.truetransact.serverside.termloan.charges;

import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.Iterator;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.termloan.chargesTo.TermLoanChargesTO;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.termloan.loansubsidy.TermLoanSubsidyTO;
import com.see.truetransact.transferobject.termloan.TermLoanFacilityTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import com.see.truetransact.transferobject.common.mobile.smsAcknoldgmentTO;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import javax.print.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Agent DAO.
 *
 */
public class TermLoanChargesDAO extends TTDAO {

    public static String fileName = "/smstemplate.xml";
    private static SqlMap sqlMap = null;
    private double paid_interest = 0;
    private double paid_penal_int = 0;
    private double paid_principal = 0;
    private Iterator smsIterator;
    private TransactionDAO transactionDAO;
    private HashMap prodMap = new HashMap();
    private Date currDt = null;
    private Date auctionDt = null;
    private String user = "";
    private Map cache;                  //used to hold references to Resources for re-use
    private String chitsNo = "";
    HashMap returnMap = new HashMap();
    HashMap retNoticeMap = new HashMap();
    HashMap retPostMap = new HashMap();
    private String batchID = "";
    private String noticeGenId = "";
    private String autoAuthorize = "N";
    /**
     * Creates a new instance of AgentDAO
     */
    public TermLoanChargesDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public static void main(String str[]) {
        try {
            TermLoanChargesDAO dao = new TermLoanChargesDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public HashMap generateID() {

        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "AUTO_BATCH_ID"); //Here u have to pass BORROW_ID or something else
            List list = null;

            //sqlMap.startTransaction();
            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) sqlMap.executeQueryForList(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }

                // Maximum Length for the ID
                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = Integer.parseInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));

                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("TermLoanCharges Map Dao : " + map);
        returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        user = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        autoAuthorize = CommonUtil.convertObjToStr(ServerUtil.getCbmsParameterMap().get("LOAN_NOTICE_AUTO_AUTHORIZE")); 
        try {
            sqlMap.startTransaction();
            if (map.containsKey("SMS")) {         // Added By Suresh
                SmsConfigDAO smsConDao = new SmsConfigDAO();
                map.put("MDS_NOTICE_REMINDER","");
                map.put(CommonConstants.BRANCH_ID,_branchCode);
                smsConDao.MdsSmsConfiguration(map);
                //readPrintXML(map);
            } else {
                actionPerform(map);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
            throw e;
        }
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        System.out.println("returnMap ===== " + returnMap);
        return returnMap;//(map);
    }

    //Added By Suresh
//    public void readPrintXML(HashMap map) {
//        String strModuleName = "";
//        HashMap smsMap = new HashMap();
//        try {
//            String applicationDir = new java.io.File("").getAbsolutePath() + "/template" + fileName;
//            System.out.println("######### applicationDir : " + applicationDir);
//            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
//            Document doc = docBuilder.parse(new File(applicationDir));
//            doc.getDocumentElement().normalize();
//            NodeList listOfMapping = doc.getElementsByTagName("message");
//            int totalMappings = listOfMapping.getLength();
//            System.out.println("Total no of message : " + totalMappings);
//            Element ModuleElement = (Element) listOfMapping.item(0);
//            NodeList textMList = ModuleElement.getChildNodes();
//            String smsString = ((Node) textMList.item(0)).getNodeValue().trim();
//            smsMap = (HashMap) map.get("SMS");
//            System.out.println("############## smsString :" + smsString);
//            System.out.println("############## smsMap :" + smsMap);
//            smsIterator = smsMap.keySet().iterator();
//            String key = "";
//            ArrayList finalData = new ArrayList();
//            if (CommonUtil.convertObjToStr(CommonConstants.MOBILE_BANKING).equals("Y")
//                    && CommonUtil.convertObjToStr(CommonConstants.MOBILE_BANKING_USERNAME).length() > 0
//                    && CommonUtil.convertObjToStr(CommonConstants.MOBILE_BANKING_PWD).length() > 0
//                    && CommonUtil.convertObjToStr(CommonConstants.MOBILE_BANKING_SENDERID).length() > 0) {
//                for (int i = 0; i < smsMap.size(); i++) {
//                    key = (String) smsIterator.next();
//                    HashMap smsSingleMap = new HashMap();
//                    HashMap smsFinalMap = new HashMap();
//                    ArrayList ackArray = new ArrayList();                    
//                    smsSingleMap = (HashMap) smsMap.get(key);
//                    System.out.println("############## SMS Single Row Data : " + smsSingleMap);
//                    String actNo = CommonUtil.convertObjToStr(smsSingleMap.get("ACT_NUM"));
//                    String dueDate = CommonUtil.convertObjToStr(smsSingleMap.get("DUE_DT"));
//                    String schemeName = CommonUtil.convertObjToStr(smsSingleMap.get("SCHEME_NAME"));
//                    double totalDue = CommonUtil.convertObjToDouble(smsSingleMap.get("TOTAL_DUE")).doubleValue();
//                    String message = "";
//                    Date due_Date = DateUtil.getDateMMDDYYYY(dueDate);
//                    message = smsString.replaceAll("%%Schemename%%", schemeName);
//                    message = message.replaceAll("%%Accountnumber%%", getAccountNo(actNo));
//                    message = message.replaceAll("%%duedt%%", getFormattedFullDate(due_Date));
//                    message = message.replaceAll("%%totaldue%%", String.valueOf(totalDue));
//                    message += " - " + CommonConstants.BANK_SMS_DESCRIPTION;
//                    message += " - " + CommonConstants.MOBILE_BANKING_SENDERID;
//                    System.out.println("####&&&&&@@@#####  Final message  (" + i + ") : " + message);
//                    HashMap smsAlertMap = new HashMap();
//                    smsAlertMap.put("ACT_NUM", actNo);
//                    List accountList = sqlMap.executeQueryForList("getActNumFromAllProducts", smsAlertMap);
//                    if (accountList != null && accountList.size() > 0) {
//                        smsAlertMap = (HashMap) accountList.get(0);
//                        List smsAccountList = sqlMap.executeQueryForList("getSelectSMSSubscriptionMap", smsAlertMap);
//                        if (smsAccountList != null && smsAccountList.size() > 0) {
//                            SMSSubscriptionTO objSMSSubscriptionTO = (SMSSubscriptionTO) smsAccountList.get(0);                      
//                            sendSMS(message, objSMSSubscriptionTO.getMobileNo(),false);                            
//                        }
//                    }
//                }                
//            }
//        } catch (SAXParseException err) {
//            System.out.println("** Parsing error" + ", line "
//                    + err.getLineNumber() + ", uri " + err.getSystemId());
//            System.out.println(" " + err.getMessage());
//        } catch (SAXException e) {
//            Exception x = e.getException();
//            ((x == null) ? e : x).printStackTrace();
//
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//    }
//    
    
    private String getFormattedFullDate(Date dueDate) throws Exception {
        java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return DATE_FORMAT.format(dueDate);
    }

    private String getAccountNo(String actNum) {
        String tempAcNo = CommonUtil.lpad(actNum.substring(7, 13), 13, '*');
        System.out.println("@#@ tempAcNo:" + tempAcNo);
        return tempAcNo;
    }

    private void actionPerform(HashMap map) throws Exception {
        if (map.get(CommonConstants.AUTHORIZEMAP) != null) {
            System.out.println("actionPerform  map    "+map);                    
            if (map.containsKey("SOURCE_SCREEN") && map.get("SOURCE_SCREEN").equals("LOAN_SUBSIDY")) {
                startSubsidyTransaction(map);
                return;
            }else if (map.containsKey("SOURCE_SCREEN") && map.get("SOURCE_SCREEN").equals("LOAN_SUBSIDY_PROVISION_IN_BETWEEN")) {
                TermLoanSubsidyTO objTermLoanSubsidyTO = (TermLoanSubsidyTO) map.get("TermLoanSubsidyTO");
                doSubsidyProvision(objTermLoanSubsidyTO);
                return;
            } else {
                if(map.containsKey("AUTHORIZEDATA")){
                    HashMap noticeAuthMap = new HashMap();
                    noticeAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                    noticeAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    noticeAuthMap.put("AGENT_COLLECTION_DETAILS_DAO", "AGENT_COLLECTION_DETAILS_DAO"); //LOANS_ACCT_CHARGE_DETAILS Authorization purpose
                    HashMap authMap = (HashMap) map.get("AUTHORIZEMAP");
                    ArrayList authData = (ArrayList) authMap.get("AUTHORIZEDATA");
                    HashMap authDataMap = (HashMap) authData.get(0);
                    String status = (String) authMap.get("AUTHORIZESTATUS");
                    transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                    if(authDataMap.containsKey("BATCH_ID") && authDataMap.get("BATCH_ID") != null){
                        transactionDAO.authorizeCashAndTransfer(CommonUtil.convertObjToStr(authDataMap.get("BATCH_ID")), status, noticeAuthMap);
                    }
                    sqlMap.executeUpdate("updateAuthorizeDetails", authDataMap);
                }else{
                    HashMap authDataMap = new HashMap();                    
                    if(map.containsKey("LOAN_NOTICE")){
                        System.out.println("map.containsKey(LOAN_NOTICE) ; "+map.containsKey("LOAN_NOTICE"));
                        HashMap noticeAuthMap = new HashMap();
                        noticeAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                        noticeAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                        noticeAuthMap.put("TRANS_DT",currDt.clone());
                        noticeAuthMap.put("SCREEN_NAME",map.get("LOAN_NOTICE"));
                        HashMap authMap = (HashMap) map.get("AUTHORIZEMAP");
                        ArrayList authData = (ArrayList) authMap.get("AUTHORIZEDATA");
                        authDataMap = (HashMap) authData.get(0);
                        String status = (String) authMap.get("AUTHORIZESTATUS");
                        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                        if(authMap.containsKey("ACT_NUM") && authMap.get("ACT_NUM") != null){
                            transactionDAO.authorizeCashAndTransfer(CommonUtil.convertObjToStr(authMap.get("ACT_NUM")), status, noticeAuthMap);
                        }
                        authDataMap.put("ACT_NUM", authMap.get("ACT_NUM"));
                    }else{
                        HashMap authMap = (HashMap) map.get("AUTHORIZEMAP");
                         System.out.println("authMapauthMap ; "+authMap);
    //                    ArrayList authData = (ArrayList) authMap.get("AUTHORIZEDATA");
                        authDataMap.put("ACT_NUM", authMap.get("ACT_NUM"));
                        authDataMap.put("AUTHORIZE_DATE", authMap.get("AUTHORIZE_DATE"));
                        authDataMap.put("AUTHORIZE_BY", authMap.get("AUTHORIZE_BY"));
                        authDataMap.put("AUTHORIZE_STATUS", authMap.get("AUTHORIZE_STATUS"));
                    }
                    sqlMap.executeUpdate("updateAuthorizeDetail", authDataMap);
                }
            }
        } else {
            if (map.containsKey("TermLoanChargesTOs")) {
                ArrayList chargeList = (ArrayList) map.get("TermLoanChargesTOs");
                ArrayList singleRecord = null;
                if (chargeList != null) {
                    for (int i = 0; i < chargeList.size(); i++) {
                        //                    singleRecord=(ArrayList)chargeList.get(i);
                        TermLoanChargesTO termLoanChargeto = (TermLoanChargesTO) chargeList.get(i);
                        termLoanChargeto.setStatus_Dt(getProperDateFormat(currDt));
                       
                        if(autoAuthorize != null && autoAuthorize.equals("N")){
                            termLoanChargeto.setAuthorize_Status(null);
                            termLoanChargeto.setAuthorize_by(null);
                            termLoanChargeto.setAuthorize_Dt(null);
                        }

                        termLoanChargeto.setChargeDt(getProperDateFormat(termLoanChargeto.getChargeDt()));//Added Bu Suresh To Avoid ClassCastException KDSA-567
                        if (termLoanChargeto.getChargeGenerateNo() != null && termLoanChargeto.getChargeGenerateNo().doubleValue() > 0) {
                            sqlMap.executeUpdate("updateTermLoanChargeTO", termLoanChargeto);
                        } else {
                            termLoanChargeto.setBranchId(_branchCode);
                            HashMap batchIddd = generateID();
                            System.out.println("batchIddd    "+batchIddd);
                            noticeGenId = CommonUtil.convertObjToStr(batchIddd.get("DATA"));
                            System.out.println("noticeGenId    "+noticeGenId);
                            termLoanChargeto.setBatchID(noticeGenId);
                            termLoanChargeto.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                            sqlMap.executeUpdate("insertTermLoanChargeTO", termLoanChargeto);
                        }
                        singleRecord = new ArrayList();
                    }
                }
            }
            if (map.containsKey("EXCESS_TRANSACTION")) {
                exccessCollectedTransaction(map);
                destroyObjects();
            }
            if (map.containsKey("NOTICE_CHARGES")) {
                HashMap batchIddd = generateID();
                System.out.println("batchIddd    "+batchIddd);
                noticeGenId = CommonUtil.convertObjToStr(batchIddd.get("DATA"));
                insertNoticeCharges(map);
            }
            if (map.containsKey("SOURCE_SCREEN") && map.get("SOURCE_SCREEN").equals("LOAN_SUBSIDY")) {
                insertUpdateSubsidyDetails(map);
            }
            if (map.containsKey("SOURCE_SCREEN") && map.get("SOURCE_SCREEN").equals("LOAN_SUBSIDY_PROVISION_IN_BETWEEN")) {
                TermLoanSubsidyTO objTermLoanSubsidyTO = (TermLoanSubsidyTO)map.get("TermLoanSubsidyTO");
                System.out.println("objTermLoanSubsidyTO here.... :: " + objTermLoanSubsidyTO);
                insertSubsidyDetails(objTermLoanSubsidyTO);
            }            
            if (map.containsKey("SOURCE_SCREEN") && map.get("SOURCE_SCREEN").equals("LOAN_AUCTION_DATA")) {
                doAuctionUpdate(map);
                return;
            } 
            }                  
        }        

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    public String insertUpdateSubsidyDetails(HashMap transMap) throws Exception {
        HashMap subsidyAcctList = (HashMap) transMap.get("INSERT_SUBSIDY");
        String newBatchId = "";
        String status = "";
        if (subsidyAcctList != null && subsidyAcctList.size() > 0) {
            Set set = (Set) subsidyAcctList.keySet();
            Object obj[] = (Object[]) set.toArray();
            if (transMap.containsKey("COMMAND") && transMap.get("COMMAND").equals("INSERT")) {
                newBatchId = generateSubsidyBatchID();
            }

            for (int j = 0; j < set.size(); j++) {
                TermLoanSubsidyTO objTermLoanSubsidyTO = (TermLoanSubsidyTO) subsidyAcctList.get(obj[j]);
                if (objTermLoanSubsidyTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    if (CommonUtil.convertObjToStr(objTermLoanSubsidyTO.getSubsidyId()).length() == 0) {
                        objTermLoanSubsidyTO.setSubsidyId(newBatchId);
                    }
                    sqlMap.executeUpdate("insertTermLoanSubsidyDetailsTO", objTermLoanSubsidyTO);
                } else {
                    sqlMap.executeUpdate("updateTermLoanSubsidyDetailsTO", objTermLoanSubsidyTO);
                }

            }
        }
//        status= CommonConstants.TOSTATUS_INSERT
        return status;
    }
    
    
    public void insertSubsidyDetails(TermLoanSubsidyTO objTermLoanSubsidyTO) throws Exception {
        String newBatchId = generateSubsidyBatchID();
        if (CommonUtil.convertObjToStr(objTermLoanSubsidyTO.getSubsidyId()).length() == 0) {
            objTermLoanSubsidyTO.setSubsidyId(newBatchId);
        }
        sqlMap.executeUpdate("insertTermLoanSubsidyDetailsTO", objTermLoanSubsidyTO);
    }
    
    
    private void doSubsidyProvision(TermLoanSubsidyTO objTermLoanSubsidyTO) throws SQLException {
        objTermLoanSubsidyTO.setAuthorizeBy(user);
        objTermLoanSubsidyTO.setAuthorizeDate(currDt);
        sqlMap.executeUpdate("updateTermLoanSubsidyAuthorizeDetailsTO", objTermLoanSubsidyTO);
        if (objTermLoanSubsidyTO.getAuthorizeStatus().equals(CommonConstants.STATUS_AUTHORIZED)) {
            HashMap updateMap = new HashMap();
            updateMap.put("ACCT_NUM", objTermLoanSubsidyTO.getAcctNum());
            updateMap.put("AVAILABLE_SUBSIDY", objTermLoanSubsidyTO.getSubsidyAmt());
            updateMap.put("SUBSIDY_DT", getProperDateFormat(objTermLoanSubsidyTO.getSubsidyDt()));
            sqlMap.executeUpdate("upadateLoansDayEndBalAvailableSubsidy", updateMap);
            sqlMap.executeUpdate("upadateLoansRebateDetailsAfterProvision", updateMap);
        }
    }
    
    private void doAuctionUpdate(HashMap dataMap) throws SQLException {
        sqlMap.executeUpdate("updateLoansAuctionDetails", dataMap);        
    }

    /*
     * method to get the batch id, will be called once for one batch
     */
    private String generateSubsidyBatchID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SUBSIDY.BATCH_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }
//     public void updateSubsidyDetails(HashMap transMap) throws Exception{
//        HashMap subsidyAcctList =(HashMap)transMap.get("UPDATE_SUBSIDY");
//        if(subsidyAcctList !=null && subsidyAcctList.size()>0){
//        Set set =(Set)subsidyAcctList.keySet();
//        Object obj[]=(Object[])set.toArray();
//        for(int j=0;j<set.size();j++){
//            TermLoanSubsidyTO objTermLoanSubsidyTO=(TermLoanSubsidyTO)subsidyAcctList.get(obj[j]);
//            sqlMap.executeUpdate("updateTermLoanSubsidyDetailsTO", objTermLoanSubsidyTO);
//    }
//    }
//     }
//    

    /**
     * DO TRANSACTION WHILE AUTHORIZE TIME
     *
     */
    public void startSubsidyTransaction(HashMap transMap) throws Exception {
//        try{
        HashMap inputMap = new HashMap();
        HashMap txMap = new HashMap();
        TxTransferTO transferTo = new TxTransferTO();
        TransferTrans transferTrans = new TransferTrans();
        HashMap authorizeMap = new HashMap();
        TermLoanFacilityTO termFacilityTo = null;
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        String command = CommonUtil.convertObjToStr(transMap.get("COMMAND"));
        HashMap subsidyAcctList = (HashMap) transMap.get("INSERT_SUBSIDY");
//            double totSubsidyDebitAmt =CommonUtil.convertObjToDouble(transMap.get("TOTAL_SUBSIDY_TRANS_AMT")).doubleValue();
        ArrayList transferList = new ArrayList(); // for local transfer
        ArrayList prodIdList = new ArrayList();
//            prodIdList=(ArrayList)transMap.get("PRODID_LIST");
//            if(prodIdList !=null && prodIdList.size()>0){
//                for(int i=0;i<prodIdList.size();i++){
//                    String prodId=CommonUtil.convertObjToStr(prodIdList.get(i));
//                    inputMap.put("PRODUCT_ID",prodId);
//                    List lst =sqlMap.executeQueryForList("getLoanAccountHeads",inputMap);
////                    if(lst !=null && lst.size()>0){
//                        inputMap=(HashMap)lst.get(0);
        Set set = (Set) subsidyAcctList.keySet();
        Object obj[] = (Object[]) set.toArray();
        for (int j = 0; j < set.size(); j++) {

            TermLoanSubsidyTO objTermLoanSubsidyTO = (TermLoanSubsidyTO) subsidyAcctList.get(obj[j]);
            objTermLoanSubsidyTO.setAuthorizeStatus(command);
            objTermLoanSubsidyTO.setAuthorizeBy(CommonUtil.convertObjToStr(transMap.get(CommonConstants.USER_ID)));
            objTermLoanSubsidyTO.setAuthorizeDate(currDt);
            if (command.equals(CommonConstants.STATUS_AUTHORIZED)) {
                HashMap dataMap = new HashMap();
                dataMap.put("ACCT_NUM", objTermLoanSubsidyTO.getAcctNum());
                List list = (List) sqlMap.executeQueryForList("SelectTermLoanSubsidyAcctHeadDeatils", dataMap);
                if (list != null && list.size() > 0) {
                    dataMap = (HashMap) list.get(0);
                }
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                if (CommonUtil.convertObjToStr(dataMap.get("SUBSIDY_ADJUST_ACHD")).length() == 0
                        || CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")).length() == 0) {
                    throw new TTException("AcHead Id is not set");
                }
                txMap.put(TransferTrans.DR_AC_HD, dataMap.get("SUBSIDY_ADJUST_ACHD")); //(String)inputMap.get("AC_DEBIT_INT")
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                txMap.put(CommonConstants.USER_ID, transMap.get(CommonConstants.USER_ID));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.PARTICULARS, "Subsidy Amt To" + objTermLoanSubsidyTO.getAcctNum());
                transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(objTermLoanSubsidyTO.getTransAmt()).doubleValue());
                transferList.add(transferTo);

                //                        Set set =(Set)subsidyAcctList.keySet();
                //                        Object obj[]=(Object[])set.toArray();

                //                        for(int j=0;j<set.size();j++){
                //                            TermLoanSubsidyTO objTermLoanSubsidyTO=(TermLoanSubsidyTO)subsidyAcctList.get(obj[j]);
                //
                //
                if (CommonUtil.convertObjToStr(inputMap.get("BEHAVES_LIKE")).equals("OD")) {
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                } else {
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                }
                //
                txMap.put(TransferTrans.CR_AC_HD, dataMap.get("ACCT_HEAD"));
                txMap.put(TransferTrans.CR_ACT_NUM, objTermLoanSubsidyTO.getAcctNum());
                txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.PARTICULARS, "Subsidy Amt " + objTermLoanSubsidyTO.getTransAmt());
                transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(objTermLoanSubsidyTO.getTransAmt()).doubleValue());
                transferList.add(transferTo);
                txMap = new HashMap();
                System.out.println("transferList:" + transferList);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setLoanDebitInt("C*");
                transactionDAO.setBreakLoanHierachy("Y");
                transactionDAO.setLinkBatchID(objTermLoanSubsidyTO.getAcctNum());
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.doTransferLocal(transferList, _branchCode);

                authorizeMap.put(CommonConstants.BRANCH_ID, _branchCode);
                authorizeMap.put(CommonConstants.USER_ID, objTermLoanSubsidyTO.getStatusBy());
                transactionDAO.authorizeCashAndTransfer(objTermLoanSubsidyTO.getAcctNum(), "AUTHORIZED", authorizeMap);

                dataMap.put("ACCT_NUM", objTermLoanSubsidyTO.getAcctNum());
                dataMap.put("CURR_DT", currDt);
                dataMap.put("TRANS_AMT", objTermLoanSubsidyTO.getTransAmt());
                sqlMap.executeUpdate("updateTermLoanFacilityDetailsTO", dataMap);
            }
            sqlMap.executeUpdate("updateTermLoanSubsidyAuthorizeDetailsTO", objTermLoanSubsidyTO);

            //
        }
        //                        transactionDAO.setCommandMode(commandMode);

//                    }
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new TTException();
//            
//        }

    }

    public void insertNoticeCharges(HashMap inputMap) throws Exception {
        ArrayList totList = new ArrayList();
        //Added BY Suresh
        String prod_type = "";
        String notice_type = "";
        String screenName = CommonUtil.convertObjToStr(inputMap.get(CommonConstants.SCREEN));
        //  Date auctionDt=null;
        if (inputMap.containsKey("PROD_TYPE")) {
            prod_type = CommonUtil.convertObjToStr(inputMap.get("PROD_TYPE"));
        }
        if (inputMap.containsKey("AUCTIONDT")) {
            auctionDt = (Date) (inputMap.get("AUCTIONDT"));
        }
        if (inputMap.containsKey("TYPE_OF_NOTICE")) {
            notice_type = CommonUtil.convertObjToStr(inputMap.get("TYPE_OF_NOTICE"));
        }
        HashMap map = (HashMap) inputMap.get("NOTICE_CHARGES");
        boolean onlyChargeDetails = ((Boolean) inputMap.get("ONLY_CHARGE_DETAILS")).booleanValue();
        if (prod_type.equals("") && !prod_type.equals("MDS")) {
            List prodList = sqlMap.executeQueryForList("getProductsForLoanNotice", new HashMap());
            HashMap tempMap = null;
            for (int i = 0; i < prodList.size(); i++) {
                tempMap = (HashMap) prodList.get(i);
                prodMap.put(tempMap.get("PROD_DESC"), tempMap.get("PROD_ID"));
            }
        }
        if (map != null && map.size() > 0) {
            Object[] accountList = map.keySet().toArray();
            ArrayList tempList = null;
            for (int i = 0; i < accountList.length; i++) {
                tempList = (ArrayList) map.get(accountList[i]);
                ArrayList rowList = null;
                for (int j = 0; j < tempList.size(); j++) {
                    rowList = (ArrayList) tempList.get(j);
                    if(inputMap.containsKey("SURITY_ONLY") && inputMap.get("SURITY_ONLY").equals("Y")){
                      if(rowList.get(rowList.size()-1).equals("GUARANTOR") ){ // changed the indexing from 9 to size-1 by nithya on 18-12-2020 for KD-2533
                            totList.add(insertNoticeChargesTO(rowList, onlyChargeDetails, prod_type, notice_type, screenName));
                      }  
                    }
                    else{
                        totList.add(insertNoticeChargesTO(rowList, onlyChargeDetails, prod_type, notice_type, screenName));
                    }
                }

            }
            stampAdvancesTransaction(totList, notice_type, prod_type);
            noticeAdvancesTransaction(totList, notice_type, prod_type);
            
//            HashMap batchMap = new HashMap();
//            batchMap.put("BATCH_ID", batchID);
//            batchMap.put("NOTICE_GEN_ID", noticeGenId);
//            sqlMap.executeUpdate("updateBatchIdTO", batchMap);
        }
    }

    private void getTransDetailsNotice(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put("INSTRUMENT","NOTICE");
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
       
        //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
            List transList = (List) sqlMap.executeQueryForList("getTransferDetailsNotice", getTransMap);
            HashMap idMap = null;
            if (transList != null && transList.size() > 0) {
            idMap = (HashMap) transList.get(0);
            batchID = CommonUtil.convertObjToStr(idMap.get("BATCH_ID"));
              }
//             HashMap idMap = (HashMap) transList.get(0);
//             batchID = CommonUtil.convertObjToStr(idMap.get("BATCH_ID"));
             System.out.println("batchID ============== " + batchID);
             HashMap batchMap = new HashMap();
             batchMap.put("BATCH_ID", batchID);
             batchMap.put("NOTICE_GEN_ID", noticeGenId);
             batchMap.put("CHARGE_TYPE", "NOTICE CHARGES");
             sqlMap.executeUpdate("updateBatchIdTO", batchMap);
      //  List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            retNoticeMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
          //  retNoticeMap.put("CASH_TRANS_LIST", cashList);
        }
        System.out.println("retNoticeMap ============== " + retNoticeMap);
        returnMap.put("TRANS_NOTICE", retNoticeMap);
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void getTransDetailsPostage(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put("INSTRUMENT","POSTAGE");
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
//        returnMap = new HashMap();
       // List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
            
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsNotice", getTransMap);
        HashMap idMap = null;
        if (transList != null && transList.size() > 0) {
            idMap = (HashMap) transList.get(0);
            batchID = CommonUtil.convertObjToStr(idMap.get("BATCH_ID"));
        }
        System.out.println("batchID ============== " + batchID);
        HashMap batchMap = new HashMap();
        batchMap.put("BATCH_ID", batchID);
        batchMap.put("NOTICE_GEN_ID", noticeGenId);
        batchMap.put("CHARGE_TYPE", "POSTAGE CHARGES");
        sqlMap.executeUpdate("updateBatchIdTO", batchMap);

        if (transList != null && transList.size() > 0) {
            retPostMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            retPostMap.put("CASH_TRANS_LIST", cashList);
        }
        System.out.println("retPostMap 11 ============== " + retPostMap);
        returnMap.put("TRANS_POSTAGE", retPostMap);
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void noticeAdvancesTransaction(ArrayList totList, String notice_type, String prod_type) throws Exception {

        if (!prod_type.equals("MDS")) {
            HashMap acHeads = new HashMap();
            HashMap noticeHead = new HashMap();
            TransferTrans transferTrans = new TransferTrans();
            HashMap txMap = new HashMap();
            String stampAdvances = "";
            ArrayList transferList = new ArrayList();
            TxTransferTO transferTo = null;
            double transAmt = 0;
            double amountTotal = 0;
            String branchID = _branchCode;
            HashMap totAmt = new HashMap();
            String strAcNum = "";
            if (totList != null && totList.size() > 0) {
                for (int i = 0; i < totList.size(); i++) {
                    HashMap acctMap = (HashMap) totList.get(i);
                    String act_num = CommonUtil.convertObjToStr(acctMap.get("ACT_NUM"));
                    acctMap.put("NOTICE_TYPE", notice_type);
                    if (i == 0) {
                        if (!prod_type.equals("MDS")) {
                            //loan part only
                            acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", act_num);
                            stampAdvances = CommonUtil.convertObjToStr(acHeads.get("CREDIT_NOTICE_ADVANCES"));
                            noticeHead = (HashMap) sqlMap.executeQueryForObject("getChargesForLoanNotices", acctMap);
                            if (stampAdvances.equals("") || stampAdvances.equals("N")) {
                                break;
                            }
                        } else {
                            //mds part only
                            acctMap.put("PROD_ID", act_num);
                            acHeads = (HashMap) sqlMap.executeQueryForObject("getMDSAccountClosingHeads", act_num);
                            noticeHead = (HashMap) sqlMap.executeQueryForObject("getChargesForMDSNotices", acctMap);
                        }
                        if (noticeHead != null && noticeHead.size() > 0) {
                            transAmt = CommonUtil.convertObjToDouble(noticeHead.get("NOTICE_CHARGE_AMT")).doubleValue();
                        }
                    }

                    // New mode
//                double transAmt=CommonUtil.convertObjToDouble(acctMap.get("AMOUNT")).doubleValue();
                    amountTotal += transAmt;
                    txMap = new HashMap();
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("NOTICE_CHARGES"));
                    txMap.put(TransferTrans.DR_BRANCH, branchID);
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    
                    txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                    transferTrans.setInitiatedBranch(_branchCode);
                    txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                    strAcNum = CommonUtil.convertObjToStr(acctMap.get("ACT_NUM"));
                    txMap.put("DR_INSTRUMENT_2", "NOTICE");
                    txMap.put("TRANS_MOD_TYPE", "TL");
                    txMap.put(CommonConstants.USER_ID, user);
                    txMap.put("SCREEN_NAME" ,"LOAN_NOTICE");
                    transferTo = transferTrans.getDebitTransferTO(txMap, transAmt);
                    if (prod_type.equals("MDS")) {
                        //  acctMap.get("CHITTAL")
                        if (acctMap.containsKey("CHITTAL") && acctMap.get("CHITTAL") != null && !acctMap.get("CHITTAL").toString().equals("")) {
                            transferTo.setLinkBatchId(acctMap.get("CHITTAL").toString());
                        } else {
                            transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                        }

                    } else {
                        transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                    }
                    transferList.add(transferTo);
                }
                txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("NOTICE_ADVANCES_HEAD"));
                txMap.put(TransferTrans.CR_BRANCH, branchID);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put("TRANS_MOD_TYPE",TransactionFactory.GL);
                txMap.put(CommonConstants.USER_ID,user);
                transferTrans.setInitiatedBranch(_branchCode);
                txMap.put(TransferTrans.PARTICULARS, "Notice Advances Reversal");
                txMap.put("SCREEN_NAME" ,"LOAN_NOTICE");
                if (amountTotal > 0) {
                    txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                    txMap.put("DR_INSTRUMENT_2", "NOTICE");
                    transferTo = transferTrans.getCreditTransferTO(txMap, amountTotal);
                }
                if (prod_type.equals("MDS")) {
                    if (!chitsNo.equals("")) {
                        transferTo.setLinkBatchId(chitsNo);
                    }
                }
//                    else
//                    {
//                         transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
//                    }
//                    
//                }else
//                {
//                
//                
//                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
//                }
                transferList.add(transferTo);
                String link_batch_id = "";
                if (transferTo != null && transferTo.getLinkBatchId() != null) {
                    link_batch_id = transferTo.getLinkBatchId();
                } else {
                    link_batch_id = strAcNum;
                }
                if (transferList != null && transferList.size() > 0 && amountTotal > 0) {
                    if (prod_type.equals("MDS")) {
                        transferTrans.doDebitCredit(transferList, branchID, true);
                    } else if (stampAdvances.equals("Y")) {
                        System.out.println("####### stampAdvances  : "+stampAdvances);
                        boolean autoAuthorizeFlag = false;
                        if(autoAuthorize != null && autoAuthorize.equals("Y")){
                            autoAuthorizeFlag = true;
                        }
                        transferTrans.doDebitCredit(transferList, branchID, autoAuthorizeFlag);
                    }
                    if (link_batch_id != null) {
                      //  getTransDetailsPostage(link_batch_id);
                        getTransDetailsNotice(link_batch_id);
                    }
                }
            }
        }
    }

    public HashMap insertNoticeChargesTO(ArrayList chargeRow, boolean onlyChargeDetails, String prod_type, String noticeType, String screenName) throws Exception {
        HashMap chargeDetMap = null;
        HashMap transMap = new HashMap();
        TermLoanChargesTO objChargeTO = null;
        //Changed By Suresh
        String tempAmount = "";
        chitsNo = "";
        if (!prod_type.equals("") && prod_type.equals("MDS")) {
            tempAmount = CommonUtil.convertObjToStr(chargeRow.get(8));
            chitsNo = CommonUtil.convertObjToStr(chargeRow.get(2));
        } else {
            tempAmount = CommonUtil.convertObjToStr(chargeRow.get(14));
        }
        if (((Boolean) chargeRow.get(0)).booleanValue() && tempAmount.lastIndexOf("+") != -1) {
            chargeDetMap = new HashMap();
            objChargeTO = new TermLoanChargesTO();
            if (!prod_type.equals("") && prod_type.equals("MDS")) {
                objChargeTO.setAct_num(CommonUtil.convertObjToStr(chargeRow.get(2)));
                chargeDetMap.put("PROD_TYPE", "MDS");
                chargeDetMap.put("PROD_ID", chargeRow.get(1));
                // String name = CommonUtil.convertObjToStr(chargeRow.get(2));
              //  name = name.length() > 128 ? name.substring(0, 127) : name;
                HashMap custMap =new HashMap();
                String chittNo=CommonUtil.convertObjToStr(chargeRow.get(2));
                chittNo = chittNo.substring(0, chittNo.length() - 2);
                custMap.put("CHITTAL_NO",chittNo);
                List custDatalst=ServerUtil.executeQuery("getStatusMDSApp", custMap);
                if(custDatalst!=null && custDatalst.size()>0){
                    custMap=(HashMap)custDatalst.get(0); 
                }
                String name="";
                if(custMap!=null && custMap.containsKey("CUST_ID")){
                   name= CommonUtil.convertObjToStr(custMap.get("CUST_ID"));
                }
                if(chargeRow.get(chargeRow.size()-1).equals("GUARANTOR")){// Added by nithya on 14-08-2018 for KD-206 : 0015301: MDS Notice- Guarantor name shown as Chittal Name                    
                    HashMap guarantorMap = new HashMap();
                    guarantorMap.put("SHARE_ACCT_NO",chargeRow.get(3));
                    List guarantorLst=ServerUtil.executeQuery("getGuarantorCustomerId", guarantorMap);
                    if(guarantorLst != null && guarantorLst.size() > 0){
                        guarantorMap = (HashMap)guarantorLst.get(0);
                    }
                    if (guarantorMap != null && guarantorMap.containsKey("CUST_ID") && guarantorMap.get("CUST_ID") != null) {
                        name = CommonUtil.convertObjToStr(guarantorMap.get("CUST_ID"));
                    }
                }
                chargeDetMap.put("CUST_ID", name);
                chargeDetMap.put("ACT_NUM", chargeRow.get(2));
                chargeDetMap.put("MEMBER_NO", chargeRow.get(3));
                chargeDetMap.put("CUST_NAME", chargeRow.get(4));
                chargeDetMap.put("CUST_TYPE", chargeRow.get(9));
            } else {
                String prod_Id = CommonUtil.convertObjToStr(chargeRow.get(5));
                chargeDetMap.put("PROD_TYPE", "TL");
                prod_Id = CommonUtil.convertObjToStr(prodMap.get(prod_Id));
                chargeDetMap.put("PROD_ID", prod_Id);
                objChargeTO.setProd_Id(prod_Id);
                objChargeTO.setProd_Type("TL");
                objChargeTO.setAct_num(CommonUtil.convertObjToStr(chargeRow.get(1)));
                chargeDetMap.put("ACT_NUM", chargeRow.get(1));
                chargeDetMap.put("MEMBER_NO", chargeRow.get(3));
                String name = CommonUtil.convertObjToStr(chargeRow.get(2));
                name = name.length() > 128 ? name.substring(0, 127) : name;
                chargeDetMap.put("CUST_ID", name);
                chargeDetMap.put("CUST_NAME", chargeRow.get(4));
                chargeDetMap.put("CUST_TYPE", chargeRow.get(15));
            }
            objChargeTO.setChargeDt(currDt);
            objChargeTO.setStatus_Dt(currDt);
            objChargeTO.setStatus_By(user);
            objChargeTO.setAuthorize_Dt(currDt);
            objChargeTO.setAuthorize_by(user);
            objChargeTO.setAuthorize_Status("AUTHORIZED");
            chargeDetMap.put("SENT_BY", user);
            chargeDetMap.put("SENT_DT", currDt);
            auctionDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(auctionDt));// Added by nithya on 09-01-2019 for KD 372 - Gold Loan Notice Processing -Charge posting Failed  
            auctionDt = setProperDtFormat(auctionDt); // Added by nithya on 03-08-2017 for 0007230: Auction Notice Processing issue.
            if (auctionDt == null) {
                chargeDetMap.put("AUCTION_DT", null);
            } else {
                chargeDetMap.put("AUCTION_DT", auctionDt);
            }
            String amount[] = tempAmount.replace("+", ",").split(",");
            Double noticeCharge = new Double(0);
            Double postageCharge = new Double(0);
            noticeCharge = CommonUtil.convertObjToDouble(amount[0]);
            postageCharge = CommonUtil.convertObjToDouble(amount[1]);

            if (!onlyChargeDetails) {
                chargeDetMap.put("NOTICE_CHARGE", noticeCharge);
                chargeDetMap.put("POSTAGE_CHARGE", postageCharge);

                if (noticeCharge.doubleValue() > 0) {
                    objChargeTO.setCharge_Type("NOTICE CHARGES");
                    objChargeTO.setAmount(noticeCharge);
                    objChargeTO.setProd_Id(CommonUtil.convertObjToStr(chargeDetMap.get("PROD_ID")));
                    objChargeTO.setProd_Type(CommonUtil.convertObjToStr(chargeDetMap.get("PROD_TYPE")));
                    List noticeChargeList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                    /*
                     * if (noticeChargeList != null && noticeChargeList.size() >
                     * 0){ HashMap noticeChargeMap = (HashMap)
                     * noticeChargeList.get(0); noticeCharge = new
                     * Double(noticeCharge.doubleValue() +
                     * CommonUtil.convertObjToDouble(noticeChargeMap.get("AMOUNT")).doubleValue());
                     * objChargeTO.setAmount(noticeCharge); String chargeNo =
                     * CommonUtil.convertObjToStr(noticeChargeMap.get("CHARGE_NO"));
                     * objChargeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                     * objChargeTO.setChargeGenerateNo(new
                     * Long(CommonUtil.convertObjToLong(chargeNo)));
                     * sqlMap.executeUpdate("updateTermLoanChargeTO",
                     * objChargeTO);
                    }else{
                     */
                    //String autoAuthorize = CommonUtil.convertObjToStr(ServerUtil.getCbmsParameterMap().get("AUTO_AUTHORIZE_ALLOWED")); 
                    if(autoAuthorize != null && autoAuthorize.equals("N")){
                            objChargeTO.setAuthorize_Status(null);
                            objChargeTO.setAuthorize_by(null);
                            objChargeTO.setAuthorize_Dt(null);
                        }
                    objChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                    objChargeTO.setBranchId(_branchCode);
                    objChargeTO.setBatchID(noticeGenId);
                    objChargeTO.setScreenName(screenName);
                    sqlMap.executeUpdate("insertTermLoanChargeTO", objChargeTO);
                    //  }
                }
                if (prod_type.equals("MDS")) {
                    transMap.put("ACT_NUM", chargeRow.get(1));
                    transMap.put("CHITTAL", chargeRow.get(2));
                } else {
                    transMap.put("ACT_NUM", objChargeTO.getAct_num());
                }
//                    transMap.put("AMOUNT",postageCharge);NOTICE_TYPE
                transMap.put("PROD_ID", objChargeTO.getProd_Id());

                if (postageCharge.doubleValue() > 0) {
                    objChargeTO.setCharge_Type("POSTAGE CHARGES");
                    objChargeTO.setAmount(postageCharge);
                    List postageChargeList = (List) sqlMap.executeQueryForList("getSelectTermLoanChargeList", objChargeTO);
                    /*
                     * if (postageChargeList != null && postageChargeList.size()
                     * > 0){ HashMap postageChargeMap = (HashMap)
                     * postageChargeList.get(0); postageCharge = new
                     * Double(postageCharge.doubleValue() +
                     * CommonUtil.convertObjToDouble(postageChargeMap.get("AMOUNT")).doubleValue());
                     * objChargeTO.setAmount(postageCharge);//noticeCharge
                     * String chargeNo =
                     * CommonUtil.convertObjToStr(postageChargeMap.get("CHARGE_NO"));
                     * objChargeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                     * objChargeTO.setChargeGenerateNo(new
                     * Long(CommonUtil.convertObjToLong(chargeNo)));
                     * sqlMap.executeUpdate("updateTermLoanChargeTO",
                     * objChargeTO);
                    }else{
                     */
                    if(autoAuthorize != null && autoAuthorize.equals("N")){
                        objChargeTO.setAuthorize_Status(null);
                        objChargeTO.setAuthorize_by(null);
                        objChargeTO.setAuthorize_Dt(null);
                    }
                    objChargeTO.setStatus(CommonConstants.STATUS_CREATED);
                    objChargeTO.setBranchId(_branchCode);
                    objChargeTO.setBatchID(noticeGenId);
                    objChargeTO.setScreenName(screenName);
                    sqlMap.executeUpdate("insertTermLoanChargeTO", objChargeTO);
                    //  }
                }
            } else {
                chargeDetMap.put("NOTICE_CHARGE", new Double(0));
                chargeDetMap.put("POSTAGE_CHARGE", new Double(0));
            }
            chargeDetMap.put("NOTICE_TYPE", noticeType);
            sqlMap.executeUpdate("insertNoticeChargeDet", chargeDetMap);
        }
        return transMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    public HashMap getData(HashMap obj) throws Exception {
        System.out.println("obj####" + obj);
        TaskHeader header = new TaskHeader();
        header.setBranchID(_branchCode);
        HashMap getDateMap = new HashMap();
        HashMap returnMap = new HashMap();
        obj.put("WHERE", obj.get("ACT_NUM"));
        List lst=null;
        HashMap principalMap = new HashMap();
        if(obj.containsKey("PROD_TYPE") && !obj.get("PROD_TYPE").equals("MDS")&& !obj.get("PROD_TYPE").equals(TransactionFactory.DEPOSITS)){
        InterestCalculationTask interestCalTask = new InterestCalculationTask(header);


        //            List lst=(List)sqlMap.executeQueryForList("getLastIntCalDate",obj);
        //            getDateMap=(HashMap)lst.get(0);
        //            prod_id=  CommonUtil.convertObjToStr(getDateMap.get("PROD_ID"));
        //            map.put("PROD_ID",prod_id);

        HashMap behaveLike = (HashMap) (sqlMap.executeQueryForList("getLoanBehaves", obj).get(0));
        Date CURR_DATE = new java.util.Date();
        CURR_DATE = ServerUtil.getCurrentDateProperFormat(_branchCode);
        System.out.println("curr_date###1" + CURR_DATE);
        CURR_DATE = (Date) currDt.clone();
        String mapNameForLastIntCalcDt = "getLastIntCalDate";
        if (CommonUtil.convertObjToStr(behaveLike.get("BEHAVES_LIKE")).equals("OD")) {
            mapNameForLastIntCalcDt = "getLastIntCalDateAD";
        }
        lst = (List) sqlMap.executeQueryForList(mapNameForLastIntCalcDt, obj);
        getDateMap = (HashMap) lst.get(0);
        Date lastPayDate = (Date) getDateMap.get("LAST_INT_CALC_DT");
        double interest = 0;
        double penalInt = 0;
        if (CommonUtil.convertObjToStr(behaveLike.get("BEHAVES_LIKE")).equals("OD")) {
            //                lst=(List)sqlMap.executeQueryForList("getLastIntCalDate",map);
            //                getDateMap=(HashMap)lst.get(0);
            //                lastPayDate=(Date)getDateMap.get("LAST_INTCALC_DTDEBIT");
            //                noOfDays=DateUtil.dateDiff(lastPayDate, currDt);
            //                Date CURR_DATE=currDt.clone();
            //                    getDateMap.put("CURR_DATE",CURR_DATE);
            obj.put("BRANCH_ID", _branchCode);
            obj.put("ACT_NUM", obj.get(CommonConstants.MAP_WHERE));
            obj.put("LAST_INT_CALC_DT", lastPayDate);
            obj.put("PROD_ID", getDateMap.get("PROD_ID"));
            obj.put("BEHAVES_LIKE", behaveLike.get("BEHAVES_LIKE"));
            obj.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
            System.out.println("before od interest calculation####" + obj);
            getDateMap = interestCalTask.interestCalcTermLoanAD(obj); // we need same used for TL also

            if (getDateMap.containsKey("LOAN_CLOSING_PENAL_INT")) {
                penalInt = CommonUtil.convertObjToDouble(getDateMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
            }
            interest = CommonUtil.convertObjToDouble(getDateMap.get("INTEREST")).doubleValue();
            //                lst=sqlMap.executeQueryForList("getSumProductOD",getDateMap);
            //                getDateMap=(HashMap)lst.get(0);
            System.out.println(lastPayDate + "OD  #####!" + getDateMap);
            //                    returnMap.put("AccountInterest",getDateMap.get("INTEREST"));
            returnMap.put("AccountInterest", new Double(interest));
            returnMap.put("AccountPenalInterest", new Double(penalInt));
        } else {
            HashMap hash = new HashMap();
            //                    map.put("CURR_DATE",CURR_DATE);
            obj.put("BRANCH_ID", obj.get("BRANCH_CODE"));
            obj.put("ACT_NUM", obj.get("WHERE"));
            obj.put("BEHAVES_LIKE", behaveLike.get("BEHAVES_LIKE"));
            obj.put("LAST_INT_CALC_DT", lastPayDate);
            obj.put("PROD_ID", getDateMap.get("PROD_ID"));
            obj.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
            System.out.println("before od interest calculation####" + obj);
            //                hash=interestCalTask.calculateInterestNonBatchAndbatch(map);  //already calculating interest now using dayend balance comment
            hash = interestCalTask.interestCalcTermLoanAD(obj);
            System.out.println("totInterest#####" + hash);
            if (hash != null) {
                //                    returnMap.put("AccountInterest",hash.get("TOT_INT"));
                if (obj.containsKey("DEPOSIT_PREMATURE_CLOSER")) {
                    returnMap.put("AccountInterest", hash.get("FINAL_INT"));
                    //                             returnMap.put("AccountPenalInterest",hash.get("LOAN_INT"));
                } else {
                    returnMap.put("AccountInterest", hash.get("INTEREST"));

                    if (hash.containsKey("LOAN_CLOSING_PENAL_INT")) {
                        penalInt = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    }
                    interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();

                    returnMap.put("AccountInterest", new Double(interest));
                    returnMap.put("AccountPenalInterest", new Double(penalInt));
                    returnMap.put("LOANBALANCE", hash.get("PRINCIPAL_BAL"));
                    returnMap.put("ASSET_STATUS", hash.get("ASSET_STATUS"));
                    returnMap.put("ROI", hash.get("ROI"));
                    returnMap.put("REPAYMENT_TYPE", hash.get("REPAYMENT_TYPE"));
                    returnMap.put("PRINCIPAL_DUE", hash.get("PRINCIPAL_DUE"));
                    returnMap.put("SHOW_INSTALLMENT_NO", hash.get("SHOW_INSTALLMENT_NO"));

                    if (hash.containsKey("UPDATE_RET_APP_DT") && hash.get("UPDATE_RET_APP_DT") != null) {
                        returnMap.put("UPDATE_RET_APP_DT", hash.get("UPDATE_RET_APP_DT"));
                    }


                }
            }
            hash = null;
        }
        returnMap.putAll(getDateMap);
        System.out.println("returnMap  ##" + returnMap);
        if (!(returnMap != null && returnMap.containsKey("REPAYMENT_TYPE") && returnMap.get("REPAYMENT_TYPE") != null && returnMap.get("REPAYMENT_TYPE").equals("EMI"))) {
            principalMap = getPrincipalDue(obj);
            returnMap.put("AccountInterest", new Double(interest - paid_interest));
            returnMap.put("AccountPenalInterest", new Double(penalInt - paid_penal_int));

        } else {
            List installLst = sqlMap.executeQueryForList("getAllLoanInstallment", obj);
            if (installLst != null && installLst.size() > 0) {
                returnMap.put("NO_OF_INSTALLMENT", new Long(installLst.size()));
            }
            HashMap dueMap = (HashMap) installLst.get(0);
            returnMap.put("INSTALLMENT AMT", dueMap.get("TOTAL_AMT"));
        }
        //        if(obj.containsKey("MODE") && obj.get("MODE")!=null && obj.get("MODE").equals("UPDATE"))
        //        {
        }
        System.out.println("obj###" + obj);
        if(obj.containsKey("PROD_TYPE") && obj.get("PROD_TYPE").equals("MDS")){
             lst = sqlMap.executeQueryForList("getSelectTermLoanChargeDetailsTO", obj.get("CHITTAL_NO")+"_"+obj.get("SUB_NO"));
        } else{
        	lst = sqlMap.executeQueryForList("getSelectTermLoanChargeDetailsTO", obj.get("ACT_NUM"));
        }
        returnMap.put("TermLoanChargesTO", lst);
        //        }
        System.out.println("returnMap####   " + returnMap);
        returnMap.putAll(principalMap);
        System.out.println("returnMap####" + returnMap);
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }
    //as an when customer get principal due

    public HashMap getPrincipalDue(HashMap map) throws Exception {
        String prodType = CommonUtil.convertObjToStr(map.get("PROD_TYPE"));
        HashMap insertPenal = null;
        HashMap returnMap = new HashMap();
        paid_interest = 0;
        paid_penal_int = 0;
        paid_principal = 0;
        Date curr_dt = (Date) currDt.clone();
        if (prodType != null && prodType.equals("TL")) {

            HashMap InstalDate = new HashMap();
            List getInstallDate = null;

            Date cDate = ServerUtil.getCurrentDateProperFormat(_branchCode);
            HashMap allInstallmentMap = null;
            double principleAmt = 0;
            double intAmt = 0;
            double clearBalance = 0;
            List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", map);
            if (facilitylst != null && facilitylst.size() > 0) {
                HashMap hash = (HashMap) facilitylst.get(0);
                clearBalance = CommonUtil.convertObjToDouble(hash.get("CLEAR_BALANCE")).doubleValue();
                clearBalance = clearBalance * -1;
            }
            List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", map);
            allInstallmentMap = (HashMap) paidAmt.get(0);
            double totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            paidAmt = sqlMap.executeQueryForList("getIntDetails", map);
            if (paidAmt != null && paidAmt.size() > 0) {
                allInstallmentMap = (HashMap) paidAmt.get(0);
            }
            double pbal = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
            //            totPrinciple+=totExcessAmt;
            List lst = sqlMap.executeQueryForList("getAllLoanInstallment", map);
            Date inst_dt = null;
            double instalAmt = 0;
            for (int i = 0; i < lst.size(); i++) {
                allInstallmentMap = (HashMap) lst.get(i);
                instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                if (instalAmt <= totPrinciple) {
                    totPrinciple -= instalAmt;
                    //                        if(lst.size()==1){
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    //                        }
                    if (DateUtil.dateDiff(curr_dt, inst_dt) >= 0) {
                        paid_principal += instalAmt;
                    }

                } else {
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    System.out.println("paid_principal" + paid_principal + "totprincipal###" + totPrinciple);
                    if (DateUtil.dateDiff(curr_dt, inst_dt) >= 0) {
                        paid_principal += totPrinciple;
                    }
                    System.out.println("paid_principal" + paid_principal + "totprincipal###" + totPrinciple);

                    totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;

                    break;
                }

            }
            returnMap.put("INSTALLMENT AMT", new Double(instalAmt));
            returnMap.put("NO_OF_INSTALLMENT", new Long(lst.size()));
            returnMap.put("PAID_PRINCIPAL", new Double(paid_principal));
            Date from_Dt = DateUtil.addDays(inst_dt, 1);
//          Date currDt = currDt.clone();
            currDt.setDate(from_Dt.getDate());
            currDt.setMonth(from_Dt.getMonth());
            currDt.setYear(from_Dt.getYear());
            if (currDt != null) {
                map.put("FROM_DT", currDt);//DateUtil.addDays(inst_dt,1));
            }
            map.put("TO_DATE", cDate);
            System.out.println("getTotalamount#####" + map);
            List lst1 = null;
            if (inst_dt != null && (totPrinciple > 0)) {
                lst1 = sqlMap.executeQueryForList("getTotalAmountOverDue", map);
                System.out.println("listsize####" + lst1);
            }
            double principle = 0;
            if (lst1 != null && lst1.size() > 0) {
                HashMap overDuemap = (HashMap) lst1.get(0);
                principle = CommonUtil.convertObjToDouble(overDuemap.get("PRINCIPAL_AMOUNT")).doubleValue();
            }
            totPrinciple += principle;
            insertPenal = new HashMap();
            if (inst_dt != null) {
                if (DateUtil.dateDiff(currDt, inst_dt) <= 0 && clearBalance >= totPrinciple) {
                    returnMap.put("PRINCIPAL_DUE", new Double(totPrinciple));
                    insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
                } else if (DateUtil.dateDiff(currDt, inst_dt) <= 0 && clearBalance < totPrinciple) {
                    returnMap.put("PRINCIPAL_DUE", new Double(clearBalance));
                    insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(clearBalance));
                } else {
                    returnMap.put("PRINCIPAL_DUE", new Double(0));
                    insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(0));
                }
            }
            insertPenal.put("INSTALL_DT", inst_dt);
            map.put("FROM_DT", map.get("LAST_INT_CALC_DT"));
            map.put("FROM_DT", DateUtil.addDays(((Date) map.get("FROM_DT")), 2));//
            map.put("TO_DATE", cDate);
            System.out.println("map#####@@@@" + map);
            List getIntDetails = sqlMap.executeQueryForList("getPaidPrinciple", map);//from_dt,to_date,act_num
            HashMap hash = null;
            if (getIntDetails != null) {
                for (int i = 0; i < getIntDetails.size(); i++) {
                    hash = (HashMap) getIntDetails.get(i);
                    returnMap.put("PAID_INTEREST", hash.get("INTEREST"));
                    returnMap.put("PAID_PENAL_INTEREST", hash.get("PENAL"));
                    paid_interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                    paid_penal_int = CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                }
            }
        } else {
            map.put("FROM_DT", map.get("LAST_INT_CALC_DT"));
            map.put("FROM_DT", DateUtil.addDays(((Date) map.get("FROM_DT")), 2));//2
            map.put("TO_DATE", ServerUtil.getCurrentDateProperFormat(_branchCode));
            System.out.println("map$$$^^^^" + map);
            List lst = sqlMap.executeQueryForList("getPaidPrincipleAD", map);
            HashMap hash = new HashMap();
            int i = 0;
            if (lst != null) {
                hash = (HashMap) lst.get(i);
            }
            returnMap.put("PAID_INTEREST", hash.get("INTEREST"));
            returnMap.put("PAID_PENAL_INTEREST", hash.get("PENAL"));
            paid_interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
            paid_penal_int = CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
        }
        System.out.println("returnMap####" + returnMap);
        return returnMap;
    }

    /*
     * more interest already collected from customer now banker want to return
     * back
     */
    private void stampAdvancesTransaction(ArrayList totList, String notice_type, String prod_type) throws Exception {

        HashMap acHeads = new HashMap();
        HashMap noticeHead = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        HashMap txMap = new HashMap();
        HashMap mdsMap = new HashMap();
        String loan_type = "";
        HashMap loan_type_map = new HashMap();
        String stampAdvances = "";
        String MDSStampAdvanceCredit = "";
        ArrayList transferList = new ArrayList();
        TxTransferTO transferTo = null;
        double transAmt = 0;
        double postamt = 0;
        double amountTotal = 0;
        String mdsAdv = "";
        String branchID = _branchCode;
        HashMap totAmt = new HashMap();
        String strActNum = "";
        if (totList != null && totList.size() > 0) {
            for (int i = 0; i < totList.size(); i++) {
                HashMap acctMap = (HashMap) totList.get(i);
                String act_num = CommonUtil.convertObjToStr(acctMap.get("ACT_NUM"));
                if (!prod_type.equals("MDS")) {
                    String prod = CommonUtil.convertObjToStr(acctMap.get("PROD_ID"));
                    loan_type_map = (HashMap) sqlMap.executeQueryForObject("getLoanType", prod);
                    loan_type = CommonUtil.convertObjToStr(loan_type_map.get("LOAN_TYPE"));
                }
                acctMap.put("NOTICE_TYPE", notice_type);
                if (i == 0) {
                    if (!prod_type.equals("MDS")) {
                        //loan part only
                        acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", act_num);
                        stampAdvances = CommonUtil.convertObjToStr(acHeads.get("CREDIT_STAMP_ADVANCES"));
                        noticeHead = (HashMap) sqlMap.executeQueryForObject("getChargesForLoanNotices", acctMap);
                        if (stampAdvances.equals("") || stampAdvances.equals("N")) {
                            break;
                        }
                    } else {
                        //mds part only
                        acctMap.put("PROD_ID", act_num);
                        acHeads = (HashMap) sqlMap.executeQueryForObject("getMDSAccountClosingHeads", act_num);
                        noticeHead = (HashMap) sqlMap.executeQueryForObject("getChargesForMDSNotices", acctMap);
                        List aList = sqlMap.executeQueryForList("getPostageRevereseMDS", acctMap);
                        HashMap opMap = new HashMap();
                        if (aList != null && aList.size() > 0) {
                            opMap = (HashMap) aList.get(0);
                        }
                        if (opMap != null && opMap.get("IS_REV_POST_ADV") != null) {
                            mdsAdv = opMap.get("IS_REV_POST_ADV").toString();
                        }
                        if(opMap != null && opMap.containsKey("CREDIT_STAMP_ADVANCES") && opMap.get("CREDIT_STAMP_ADVANCES") != null){
                            MDSStampAdvanceCredit =  CommonUtil.convertObjToStr(opMap.get("CREDIT_STAMP_ADVANCES"));
                        }
                    }
                    if (noticeHead != null && noticeHead.size() > 0) {
                        if (prod_type.equals("MDS")) {
                            transAmt = CommonUtil.convertObjToDouble(noticeHead.get("NOTICE_CHARGE_AMT")).doubleValue();
                            postamt = CommonUtil.convertObjToDouble(noticeHead.get("POSTAGE_AMT")).doubleValue();
                        } else {
                            transAmt = CommonUtil.convertObjToDouble(noticeHead.get("POSTAGE_AMT")).doubleValue();
                        }
                    }
                }

                // New mode
//                double transAmt=CommonUtil.convertObjToDouble(acctMap.get("AMOUNT")).doubleValue();
                amountTotal += transAmt;
                txMap = new HashMap();
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                if (prod_type.equals("MDS")) {
                   txMap.put("TRANS_MOD_TYPE", "MDS");
                }
                else{
                    txMap.put("TRANS_MOD_TYPE", "TL"); 
                }
                if (!prod_type.equals("MDS")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_CHARGES"));
                    txMap.put("DR_INSTRUMENT_2", "POSTAGE");
                } else {
                    txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("NOTICE_CHARGES_HEAD"));
                    txMap.put("DR_INSTRUMENT_2", "NOTICE");
                }
                txMap.put(TransferTrans.DR_BRANCH, branchID);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                txMap.put(CommonConstants.USER_ID, user);
                transferTrans.setInitiatedBranch(_branchCode);
                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                txMap.put("SCREEN_NAME", "LOAN_NOTICE");
                transferTo = transferTrans.getDebitTransferTO(txMap, transAmt);
                if (prod_type.equals("MDS")) {
                    //  acctMap.get("CHITTAL")
                    if (acctMap.containsKey("CHITTAL") && acctMap.get("CHITTAL") != null && !acctMap.get("CHITTAL").toString().equals("")) {
                        transferTo.setLinkBatchId(acctMap.get("CHITTAL").toString());
                    } else {
                        transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                    }

                } else {
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                    strActNum = CommonUtil.convertObjToStr(acctMap.get("ACT_NUM"));
                }
                transferList.add(transferTo);
            }
            if (!prod_type.equals("MDS")) {
                txMap.put("DR_INSTRUMENT_2", "POSTAGE");
            } else {
                txMap.put("DR_INSTRUMENT_2", "NOTICE");
            }
            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("STAMP_ADVANCES_HEAD"));
            txMap.put(TransferTrans.CR_BRANCH, branchID);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
            txMap.put(CommonConstants.USER_ID, user);
            transferTrans.setInitiatedBranch(_branchCode);
            if(txMap.get(TransferTrans.CR_PROD_TYPE).equals(TransactionFactory.GL)){
                txMap.remove("LINK_BATCH_ID");
            }
            txMap.put(TransferTrans.PARTICULARS, "Stamp Advances Reversal");
            txMap.put("TRANS_MOD_TYPE", "GL");
            txMap.put("SCREEN_NAME" ,"LOAN_NOTICE");
            if (amountTotal > 0) {
                txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                transferTo = transferTrans.getCreditTransferTO(txMap, amountTotal);
            }
            
//                    else
//                    {
//                         transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
//                    }
//                    
//                }else
//                {
//                
//                
//                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
//                }
            String link_batch_id = "";
            if (transferTo != null && transferTo.getLinkBatchId() != null) {
                link_batch_id = transferTo.getLinkBatchId();
            } else {
                link_batch_id = strActNum;
            }
            //else
            //  link_batch_id= strActNum;
            //else
            //  link_batch_id 
            transferList.add(transferTo);
            if (prod_type.equals("MDS")) {
                if (!chitsNo.equals("")) {
                    transferTo.setLinkBatchId(chitsNo);
                }
            }
            if (transferTo!=null&&transferTo.getLinkBatchId() != null) {
                link_batch_id = transferTo.getLinkBatchId();
            }
            if (transferList != null && transferList.size() > 0 && amountTotal > 0) {
                if (prod_type.equals("MDS")) {
                    //System.out.println("transferList here for checking :: " + transferList);
                    if(MDSStampAdvanceCredit.equals("Y")){
                    transferTrans.doDebitCredit(transferList, branchID, true);
                    }
                } else if (stampAdvances.equals("Y")) {
                    System.out.println("####### stampAdvances  : " + stampAdvances);
                    boolean autoAuthorizeFlag = false;
                    if (autoAuthorize != null && autoAuthorize.equals("Y")) {
                        autoAuthorizeFlag = true;
                    }
                    transferTrans.doDebitCredit(transferList, branchID, autoAuthorizeFlag);
                }
                if (link_batch_id != null) {
                    if (!prod_type.equals("MDS")) {
                        getTransDetailsPostage(link_batch_id);
                    }
                    {
                        getTransDetailsNotice(link_batch_id);
                    }
                }
                if (mdsAdv != null && mdsAdv.equals("Y")) {
                    transactionpartMDSPostagerev(totList, notice_type, prod_type);
                }
            }
        }
    }

    public void transactionpartMDSPostagerev(ArrayList totList, String notice_type, String prod_type) throws Exception {
        HashMap acHeads = new HashMap();
        HashMap noticeHead = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        HashMap txMap = new HashMap();
        HashMap mdsMap = new HashMap();
        String stampAdvances = "";
        ArrayList transferList = new ArrayList();
        TxTransferTO transferTo = null;
        // double transAmt =0;
        double postamt = 0;
        double amountTotal = 0;
        String branchID = _branchCode;
        HashMap totAmt = new HashMap();
        if (totList != null && totList.size() > 0) {
            for (int i = 0; i < totList.size(); i++) {
                HashMap acctMap = (HashMap) totList.get(i);
                String act_num = CommonUtil.convertObjToStr(acctMap.get("ACT_NUM"));
                acctMap.put("NOTICE_TYPE", notice_type);
                if (i == 0) {

                    //mds part only
                    acctMap.put("PROD_ID", act_num);
                    acHeads = (HashMap) sqlMap.executeQueryForObject("getMDSAccountClosingHeads", act_num);
                    noticeHead = (HashMap) sqlMap.executeQueryForObject("getChargesForMDSNotices", acctMap);
                    //    List aList=sqlMap.executeQueryForList("getPostageRevereseMDS", acctMap);

                    if (noticeHead != null && noticeHead.size() > 0) {
                        postamt = CommonUtil.convertObjToDouble(noticeHead.get("POSTAGE_AMT")).doubleValue();
                    }
                }
                amountTotal += postamt;
                txMap = new HashMap();
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put("TRANS_MOD_TYPE", "MDS");
                txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("POSTAGE_CHARGES"));
                txMap.put(TransferTrans.DR_BRANCH, branchID);
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                txMap.put("DR_INSTRUMENT_2", "POSTAGE");
                txMap.put(CommonConstants.USER_ID, user);
                transferTrans.setInitiatedBranch(_branchCode);
                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                txMap.put("SCREEN_NAME", "LOAN_NOTICE");
                transferTo = transferTrans.getDebitTransferTO(txMap, postamt);
                if (prod_type.equals("MDS")) {
                    //  acctMap.get("CHITTAL")
                    if (acctMap.containsKey("CHITTAL") && acctMap.get("CHITTAL") != null && !acctMap.get("CHITTAL").toString().equals("")) {
                        transferTo.setLinkBatchId(acctMap.get("CHITTAL").toString());
                    } else {
                        transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                    }

                } else {
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(acctMap.get("ACT_NUM")));
                }
                transferList.add(transferTo);
            }
            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("POSTAGE_ADV_HEAD"));
            txMap.put(TransferTrans.CR_BRANCH, branchID);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.GL);
            txMap.put(TransferTrans.PARTICULARS, "Postage Advances Reversal");
            txMap.put("SCREEN_NAME", "LOAN_NOTICE");
            if (amountTotal > 0) {
                txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                txMap.put("DR_INSTRUMENT_2","POSTAGE");
                transferTo = transferTrans.getCreditTransferTO(txMap, amountTotal);
            }
            if (prod_type.equals("MDS")) {
                if (!chitsNo.equals("")) {
                    transferTo.setLinkBatchId(chitsNo);
                }
            }
            transferList.add(transferTo);
            String link_batch_id = transferTo.getLinkBatchId();
            if (transferList != null && transferList.size() > 0 && amountTotal > 0) {
                if (prod_type.equals("MDS")) {
                    transferTrans.doDebitCredit(transferList, branchID, true);
                }
                if (link_batch_id != null) {
                 getTransDetailsPostage(link_batch_id);
                }
            }
        }
    }
    /*
     * more interest already collected from customer now banker want to return
     * back
     */

    private void exccessCollectedTransaction(HashMap map) throws Exception {
        ArrayList transList = null;
        HashMap notDeleted = new HashMap();
        HashMap acHeads = new HashMap();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
//        transactionDAO.setTransType(CommonConstants.DEBIT);
        String branchID = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        HashMap totAmt = new HashMap();
        if (map.containsKey("ALL_AMOUNT")) {
            totAmt = (HashMap) map.get("ALL_AMOUNT");
        }
        String act_num = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
        acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", act_num);
        HashMap txMap = new HashMap();
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setLinkBatchID(act_num);
        System.out.println("acHeads:" + acHeads);
        TxTransferTO transferTo = null;
        String behaves = "";
        Double loanTempAmt = new Double(0);
        // New mode
        double reverseInt = CommonUtil.convertObjToDouble(totAmt.get("TRANSACTION_AMT")).doubleValue();
//                    reverseInt=reverseInt*(-1);
        txMap = new HashMap();
        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("AC_DEBIT_INT"));
        txMap.put(TransferTrans.DR_BRANCH, branchID);
        txMap.put(TransferTrans.CURRENCY, "INR");
        txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");

        txMap.put(TransferTrans.CR_PROD_ID, (String) acHeads.get("PROD_ID"));
        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("ACCT_HEAD"));
        txMap.put(TransferTrans.CR_ACT_NUM, act_num);
        txMap.put(TransferTrans.CR_BRANCH, branchID);
        txMap.put(TransferTrans.CURRENCY, "INR");
//                    if(behaves.equals("OD"))
//                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
//                    else
        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
        txMap.put("AUTHORIZEREMARKS", "ACT_CLOSE");
        txMap.put(TransferTrans.PARTICULARS, "Excess Interest");
        txMap.put("SCREEN_NAME" ,"LOAN_NOTICE");
        transferTo = transactionDAO.addTransferCreditLocal(txMap, reverseInt);
        ArrayList transferList = new ArrayList();
        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        transferList.add(transferTo);
        txMap.put(TransferTrans.PARTICULARS, act_num);
        txMap.put("SCREEN_NAME" ,"LOAN_NOTICE");
        transferTo = transactionDAO.addTransferDebitLocal(txMap, reverseInt);
        transferTo.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        transferList.add(transferTo);
        // //System.out.println("transferList:" + transferList);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
        if (totAmt != null && totAmt.size() > 0) {
            transactionDAO.setLoanAmtMap(totAmt);
        }
        transactionDAO.doTransferLocal(transferList, branchID);

    }

    private void destroyObjects() {
        transactionDAO.setLoanAmtMap(new HashMap());
    }
    
    private Date setProperDtFormat(Date dt) {   // Added by nithya on 03-08-2017 for 0007230: Auction Notice Processing issue.
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
}
