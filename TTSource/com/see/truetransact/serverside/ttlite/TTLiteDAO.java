package com.see.truetransact.serverside.ttlite;

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * .
 *
 * TTLiteDAO.java
 *
 * Created on May 04, 2004, 1:42 PM
 */
import com.ibatis.common.resources.Resources;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.ibatis.db.sqlmap.XmlSqlMapBuilder;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.ttlite.TransferTO;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.see.truetransact.transferobject.remittance.rtgs.RtgsRemittanceTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.sms.SmsConfigDAO;
import com.see.truetransact.serverside.remittance.rtgs.RtgsRemittanceDAO;
import com.see.truetransact.commonutil.StringEncrypter;
import com.see.truetransact.commonutil.StringEncrypter.EncryptionException;
import com.see.truetransact.commonutil.DateUtil;
import java.util.LinkedHashMap;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.transaction.electronicpayment.ElectronicPaymentDAO;
import java.util.*;

/**
 *
 * @author Pranav
 */
public class TTLiteDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    private StringEncrypter encrypt = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private java.util.Properties dataBaseProperties = new java.util.Properties();
    private String dbDriverName = "";
    private double overDueIntWaiveAmount = 0.0;
    private double postageWaiveAmount = 0.0;
    private double miscellaneousWaiveAmount = 0.0;
    private double legalWaiveAmount = 0.0;
    private double insuranceWaiveAmont = 0.0;
    private double advertiseWaiveAmount = 0.0;
    private double epCostWaiveAmount = 0.0;
    private double decreeWaiveAmount = 0.0;
    private double arbitarayWaivwAmount = 0.0;
    private double arcWaiveAmount = 0.0;
    private double principalWaiveAmount = 0.0;
    private double interestWaiveAmount = 0.0;
    private double penalWaiveAmount = 0.0;
    private double noticeWaiveAmount = 0.0;
    private boolean isPenalWaiveOff = false;
    private boolean isInterestWaiveoff = false;
    private boolean isNoticeWaiveoff = false;
    private boolean isPrincipalwaiveoff = false;
    private boolean isArcWaiveOff = false;
    private boolean isArbitraryWaiveOff = false;
    private boolean isDecreeWaiveOff = false;
    private boolean isEpCostWaiveOff = false;
    private boolean isAdvertiseWaiveOff = false;
    private boolean isInsuranceWaiveOff = false;
    private boolean isLegalWaiveOff = false;
    private boolean isMiscellaneousWaiveOff = false;
    private boolean isPostageWaiveOff = false;
    private boolean isOverDueIntWaiveOff = false;
    private HashMap recoverChrgMap = new HashMap();
    private HashMap otherChargesMap = new HashMap();

    public TTLiteDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        try {
            dataBaseProperties.load(this.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
            dbDriverName = dataBaseProperties.getProperty("driver");
        } catch (Exception ex) {
        }
    }

    /*
     * this method will get the data against any query with some condition
     */
    private Object getMiscData(String map, Object where) {
        List list = null;
        try {
            list = (List) sqlMap.executeQueryForList(map, where);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (Object) list;
    }

    /*
     * this method will get the data from database;
     */
    public HashMap executeQuery(HashMap hash) {
        HashMap returnMap = null;
        try {
            String map = (String) hash.get(CommonConstants.MAP_NAME);
            Object where = hash.get(CommonConstants.MAP_WHERE);
            System.out.println("ttliteDAO map : " + map + " where : " + where + "hash : " + hash);
            returnMap = new HashMap();

            if (map != null) {
                if (hash.containsKey("PROD_TYPE") && map.equals("get" + hash.get("PROD_TYPE") + "Transactions")) {
                    returnMap.put("Transactions", getMiscData(map, where));
                } else if (map.contains("get")) {
                    System.out.println("execute getdata query : " + map);
                    returnMap.put(map.replace("get", ""), getMiscData(map, where));
                } else {
                    System.out.println("execute : " + map);
                    returnMap = execute(hash);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnMap;
    }

    /*
     * this method will execute the INSERT/EXECUTE/UPDATE operations
     */
    public HashMap execute(HashMap hash) throws Exception {
        try {
            System.out.println("TTLiteDAO execute : " + hash);
            String map = (String) hash.get(CommonConstants.MAP_NAME);
            Object where = hash.get(CommonConstants.MAP_WHERE);
            HashMap utilityMap = new HashMap();
            ServerUtil.cbmsParmeterMap();
            if (map != null && map.equals("insertTransferTO")) {
                if (hash.containsKey("utilityMap")) {
                    utilityMap = (HashMap) hash.get("utilityMap");
//                                    //System.out.println("utilityMap : " + utilityMap);
                }
                hash = postTransaction((TransferTO[]) hash.get(CommonConstants.DATA), hash);
                //System.out.println("After tcompleting txn result : "+hash);
                if (!utilityMap.isEmpty()) {
                    sqlMap.executeUpdate("insertUtilityMasterDetails", utilityMap);
                }
                /*
                 * start the transaction, if something goes wrong that condition
                 * is handled in individual method
                 */
//            sqlMap.startTransaction();

                // execute the command
//            sqlMap.executeUpdate(map, where);
//            sqlMap.commitTransaction();
                // commit transaction
//                return hash;
            } else {
                hash = updateAppDataValues(map, where, hash);
                //System.out.println("hash : " + hash);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // if something goes wrong, rollback the transaction
                sqlMap.rollbackTransaction();

            } catch (Exception ef) {
            }
            e.printStackTrace();
            // throw the Throable object
            throw e;
        }
        //System.out.println("hash : " + hash);
        return hash;
    }


    /*
     * method to get the batch id, will be called once for one batch
     */
    private String getBatchID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TRANSFER.BATCH_ID");

        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /*
     * method to get the transaction id, will be called for each insert
     */
    private String getTransID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TRANSFER_TRANS_ID");

        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /*
     * this method will post the transactions. follwing things are to be done 1.
     * post first transaction 2. reduce amount from the first account 3. post
     * second transaction 4. add amount in the second account
     */
    private HashMap postTransaction(TransferTO[] transferTOList, HashMap hash) throws Exception {
        try {
            /*
             * start the transaction, if something goes wrong that condition is
             * handled in individual method
             */
            sqlMap.startTransaction();
            TransferTO transTO = null;
            transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
            ArrayList transferList = new ArrayList();
            TransferTrans objTransferTrans = new TransferTrans();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            TransferDAO transferDAO = new TransferDAO();
            HashMap data = new HashMap();
            for (int i = 0; i < transferTOList.length; i++) {
                // get the TransferTO object
                transTO = (TransferTO) transferTOList[i];
//                transferList.add(transTO);
                if (transTO.getTransType().equals("DEBIT")) {
                    HashMap txMap = new HashMap();
                    txMap.put(TransferTrans.DR_AC_HD, transTO.getAcHdId());
                    txMap.put(TransferTrans.DR_ACT_NUM, transTO.getActNum());
                    txMap.put(TransferTrans.DR_PROD_TYPE, transTO.getProductType());
                    txMap.put(TransferTrans.DR_PROD_ID, transTO.getProdId());
                    txMap.put(TransferTrans.DR_BRANCH, transTO.getBranchId());
                    txMap.put(TransferTrans.PARTICULARS, transTO.getParticulars());
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    txMap.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
                    txMap.put("LINK_BATCH_ID", hash.get("debitActNum"));
                    txMap.put("SingleTransId", "SingleTransId");
                    txMap.put("SCREEN_NAME", "Mobile Application");
                    txMap.put("TRANS_MOD_TYPE", transTO.getProductType());
                    txMap.put("USER", CommonConstants.TTSYSTEM);
                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, transTO.getAmount()));
//                    //System.out.println("Debit details : " + txMap);
                } else if (transTO.getTransType().equals("CREDIT")) {
                    HashMap txMap = new HashMap();
                    txMap.put(TransferTrans.CR_AC_HD, transTO.getAcHdId());
                    txMap.put(TransferTrans.CR_ACT_NUM, transTO.getActNum());
                    txMap.put(TransferTrans.CR_PROD_TYPE, transTO.getProductType());
                    txMap.put(TransferTrans.CR_PROD_ID, transTO.getProdId());
                    txMap.put(TransferTrans.CR_BRANCH, transTO.getBranchId());
                    txMap.put(TransferTrans.PARTICULARS, transTO.getParticulars());
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
                    txMap.put("LINK_BATCH_ID", hash.get("debitActNum"));
                    txMap.put("SingleTransId", "SingleTransId");
                    txMap.put("SCREEN_NAME", "Mobile Application");
                    txMap.put("TRANS_MOD_TYPE", transTO.getProductType());
                    txMap.put("USER", CommonConstants.TTSYSTEM);
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, transTO.getAmount()));
//                    //System.out.println("Credit details : " + txMap);
                }
                objTransferTrans.setInitiatedBranch(transTO.getBranchId());
                transactionDAO.setInitiatedBranch(transTO.getInitiatedBranch());
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                transactionDAO.setLinkBatchID(transTO.getLinkBatchId());
            }

            data.put("TxTransferTO", transferList);
            data.put("COMMAND", "INSERT");
            data.put("INITIATED_BRANCH", hash.get("initiatedBranch"));
            data.put(CommonConstants.BRANCH_ID, hash.get("branchId"));
            data.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
            data.put(CommonConstants.MODULE, "Mobile Application");
            data.put(CommonConstants.SCREEN, "Mobile Application");
            data.put("MODE", "MODE");
            data.put("LINK_BATCH_ID", hash.get("debitActNum"));
            HashMap transMap = new HashMap();
            transMap = transferDAO.execute(data, false);
            String authorizeStatus = "AUTHORIZED";
            HashMap transAuthMap = new HashMap();
            transAuthMap.put(CommonConstants.BRANCH_ID, hash.get("initiatedBranch"));
            transAuthMap.put("DAILY", "DAILY");
            transAuthMap.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
            TransactionDAO.authorizeCashAndTransfer(CommonUtil.convertObjToStr(hash.get("debitActNum")), authorizeStatus, transAuthMap);
            sqlMap.commitTransaction();
            return transMap;
        } catch (Exception th) {
            try {
                // if something goes wrong, rollback the transaction
                sqlMap.rollbackTransaction();
            } catch (Exception thf) {
            }

            // throw the Throable object
            throw th;
        }
    }

    private HashMap updateAppDataValues(String mapName, Object where, HashMap hash) throws Exception {
        HashMap returnMap = new HashMap();
        try {
            sqlMap.startTransaction();
            if (mapName != null && where != null) {
                if (mapName.equals("utilitySchedulerCheck")) {
                    HashMap appMap = (HashMap) hash.get(CommonConstants.MAP_WHERE);
                    try {
                        SmsConfigDAO smsDAO = new SmsConfigDAO();
                        smsDAO.getRechargeStatus(appMap);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (mapName.equals("insertQRMaster") || mapName.equals("updateRestOTP")) {
                    HashMap otpMap = (HashMap) hash.get(CommonConstants.MAP_WHERE);
                    encrypt = new StringEncrypter();
                    if (mapName.equals("insertQRMaster")) {
                        SmsConfigDAO smsDAO = new SmsConfigDAO();
                        HashMap existMap = new HashMap();
                        existMap.put("QR_ACT_NUM", otpMap.get("QR_ACT_NUM"));
                        existMap.put("QR_BANK", otpMap.get("QR_BANK"));
                        List existList = sqlMap.executeQueryForList("getRecordExistTodaysDtorNot", existMap);
                        if (existList != null && existList.size() > 0) {
                            existMap = (HashMap) existList.get(0);
                            otpMap.put("MOBILE_APP_BRANCH_ID", existMap.get("BRANCH_ID"));
                        }
                        smsDAO.sendOTP(otpMap);
                        String encryptedOTP = encrypt.encrypt(CommonUtil.convertObjToStr(otpMap.get("OTP_NUM")));
                        otpMap.put("OTP_NUM", encryptedOTP);
                        returnMap.put("returnmessage", "SUCCESS");
                    } else {
                        sqlMap.executeUpdate(mapName, otpMap);
                        returnMap.put("returnmessage", "SUCCESS");
                    }
                }else if (mapName.equals("updateRegenerateOTP")) {
                //System.out.println("updateRegenerateOTP where : "+where);
                    HashMap smsMap = new HashMap();
                    smsMap = (HashMap) where;
                    SmsConfigDAO smsDAO = new SmsConfigDAO();
                    //System.out.println("updateRegenerateOTP smsMap : "+smsMap);
                    smsDAO.sendMobileAPPOTP(smsMap);
                    //System.out.println("insertWithinBankBeneficiary where : " + where);
    //                sqlMap.executeUpdate(mapName, where);
                    returnMap.put("statusMsg", "Record inserted successfully");
                } else if (mapName.equals("ForgotPasswordOTP") || mapName.equals("TransactionOTP")) {
                    HashMap otpMap = (HashMap) hash.get(CommonConstants.MAP_WHERE);
                    System.out.println("otpMap : " + otpMap);
                    encrypt = new StringEncrypter();
                    SmsConfigDAO smsDAO = new SmsConfigDAO();
                    otpMap.put("FROM_MOBILE_APP", "FROM_MOBILE_APP");
                    if (mapName.equals("TransactionOTP")) {
                        HashMap operativeMap = new HashMap();
                        operativeMap.put("ACT_NUM", otpMap.get("DR_ACT_NUM"));
                        List lst = sqlMap.executeQueryForList("getActMasterDataOA", operativeMap);
                        if (lst != null && lst.size() > 0) {
                            operativeMap = (HashMap) lst.get(0);
                            otpMap.put("QR_ACT_NUM", otpMap.get("DR_ACT_NUM"));
                            otpMap.put("ACT_NUM", otpMap.get("DR_ACT_NUM"));
                            otpMap.put("PROD_ID", operativeMap.get("PROD_ID"));
                            otpMap.put("PROD_TYPE", "OA");
                        }
                        System.out.println("otpMap : " + otpMap);
                        String recordDetails = smsDAO.sendOTP(otpMap);
                        System.out.println("recordDetails : " + recordDetails);
                        if (mapName.equals("TransactionOTP") && recordDetails.equals("recordExists")) {
                            mapName = "insertCustomerOTPDetails";
                            sqlMap.executeUpdate(mapName, otpMap);
                            returnMap.put("returnmessage", "SUCCESS");
                            returnMap.put("OTP_NUM", otpMap.get("OTP_NUM"));
                            List phList = sqlMap.executeQueryForList("getSelectOtherBankApprovedMobileNo", otpMap);
                            if (phList != null && phList.size() > 0) {
                                HashMap mobileMap = (HashMap) phList.get(0);
                                returnMap.put("MOBILE_NO", mobileMap.get("MOBILE_NUMBER"));
                            }
                        } else if (mapName.equals("TransactionOTP") && (recordDetails.equals("recordDoesNotExist") || recordDetails.equals("serverConfigurationNotEnabled"))) {
                            returnMap.put("returnmessage", recordDetails);
                        }
                    }
                } else if (mapName.equals("othersAccountsTransaction")) {
                    HashMap othersAccountMap = (HashMap) hash.get(CommonConstants.MAP_WHERE);
                    System.out.println("othersAccountsTransaction where : " + where + "appMap : " + othersAccountMap);
                    try {
                        TransferTrans objTransferTrans = new TransferTrans();
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        TransferDAO transferDAO = new TransferDAO();
                        HashMap data = new HashMap();
                        HashMap operativeMap = new HashMap();
                        HashMap loanMap = new HashMap();
                        HashMap loanActMap = new HashMap();
                        operativeMap.put("ACT_NUM", othersAccountMap.get("otpDebitActNum"));
                        List debitList = sqlMap.executeQueryForList("getActMasterDataOA", operativeMap);
                        operativeMap.put("ACT_NUM", othersAccountMap.get("payeeAccount"));
                        List creditList = sqlMap.executeQueryForList("getActMasterDataOA", operativeMap);
                        if (debitList != null && debitList.size() > 0 && creditList != null && creditList.size() > 0) {
                            HashMap debitMap = (HashMap) debitList.get(0);
                            HashMap creditMap = (HashMap) creditList.get(0);
                            String generateSingleTransId = generateLinkID(CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                            HashMap txMap = new HashMap();
                            ArrayList transferList = new ArrayList();
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(debitMap.get(debitMap.get("AC_HD_ID"))));
                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(debitMap.get("PROD_ID")));
                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(debitMap.get("ACT_NUM")));
                            txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                            txMap.put(TransferTrans.PARTICULARS, "To CREDIT ACT NO :" + CommonUtil.convertObjToStr(creditMap.get("ACT_NUM")));
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                            txMap.put("DR_INSTRUMENT_2", "MobileApplication");
                            txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            txMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(creditMap.get("BRANCH_CODE")));
                            txMap.put("SCREEN_NAME", "MobileApplication");
//                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//                        transferTo = transactionDAO.addTransferDebitLocal(txMap, 1 * -1);
                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(othersAccountMap.get("amount"))));
                            txMap = new HashMap();
                            txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(creditMap.get("ACT_NUM")));
                            if (CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE")).equals("OA")) {
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                            } else if (CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE")).equals("TD")) {
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(creditMap.get("ACT_NUM")) + "_1");
                            } else if (CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE")).equals("TL")) {
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                                HashMap map = new HashMap();
                                HashMap whereMap = new HashMap();
                                String _branchCode = (String) creditMap.get("BRANCH_CODE");
                                String prod_id = "";
                                map.put("ACT_NUM", creditMap.get("ACT_NUM"));
                                map.put("PROD_ID", creditMap.get("PROD_ID"));
                                map.put("PROD_TYPE", creditMap.get("PROD_TYPE"));
                                map.put("CUST_ID", creditMap.get("CUST_ID"));
                                map.put("TRANS_DT", ServerUtil.getCurrentDate(_branchCode));                             //Added By Suresh
                                map.put("INITIATED_BRANCH", _branchCode);
                                whereMap.put("BRANCH_ID", _branchCode);
                                map.put(CommonConstants.MAP_WHERE, whereMap);
                                HashMap returnCalculatedMap = calculatedInterestDetails(map);
                                System.out.println("transaction returnCalculatedMap : " + returnCalculatedMap);
                                HashMap loanInterestMap = (HashMap) returnCalculatedMap.get("LOAN_DATA_MAP");
//                                if (multiple_ALL_LOAN_AMOUNT != null && multiple_ALL_LOAN_AMOUNT.size() > 0) {
                                if (isPenalWaiveOff) {
                                    loanActMap.put("PENAL_WAIVE_OFF", "Y");
                                    loanActMap.put("PENAL_WAIVE_AMT", penalWaiveAmount);
                                } else {
                                    loanActMap.put("PENAL_WAIVE_OFF", "N");
                                }
                                //added by rishad 08/04/2015 for setting waiveamount to all_loan_amount map for waive transaction
                                if (isInterestWaiveoff) {
                                    loanActMap.put("WAIVE_OFF_INTEREST", "Y");
                                    loanActMap.put("INTEREST_WAIVE_AMT", interestWaiveAmount);
                                } else {
                                    loanActMap.put("INTEREST_WAIVE_OFF", "N");
                                }
                                if (isNoticeWaiveoff) {
                                    loanActMap.put("NOTICE_WAIVE_OFF", "Y");
                                    loanActMap.put("NOTICE_WAIVE_AMT", noticeWaiveAmount);
                                } else {
                                    loanActMap.put("NOTICE_WAIVE_OFF", "N");
                                }
                                if (isPrincipalwaiveoff) {
                                    loanActMap.put("PRINCIPAL_WAIVE_OFF", "Y");
                                    loanActMap.put("PRINCIPAL_WAIVE_AMT", principalWaiveAmount);
                                } else {
                                    loanActMap.put("PRINCIPAL_WAIVE_OFF", "N");
                                }
                                if (isArcWaiveOff) {
                                    loanActMap.put("ARC_WAIVE_OFF", "Y");
                                    loanActMap.put("ARC_WAIVE_AMT", arcWaiveAmount);
                                } else {
                                    loanActMap.put("ARC_WAIVE_OFF", "N");
                                }
                                if (isArbitraryWaiveOff) {
                                    loanActMap.put("ARBITRAY_WAIVE_OFF", "Y");
                                    loanActMap.put("ARBITRAY_WAIVE_AMT", arbitarayWaivwAmount);
                                } else {
                                    loanActMap.put("ARBITRAY_WAIVE_OFF", "N");
                                }
                                if (isDecreeWaiveOff) {
                                    loanActMap.put("DECREE_WAIVE_OFF", "Y");
                                    loanActMap.put("DECREE_WAIVE_AMT", decreeWaiveAmount);
                                } else {
                                    loanActMap.put("DECREE_WAIVE_OFF", "N");
                                }
                                if (isEpCostWaiveOff) {
                                    loanActMap.put("EP_WAIVE_OFF", "Y");
                                    loanActMap.put("EP_WAIVE_AMT", epCostWaiveAmount);
                                } else {
                                    loanActMap.put("EP_WAIVE_OFF", "N");
                                }
                                if (isAdvertiseWaiveOff) {
                                    loanActMap.put("ADVERTISE_WAIVE_OFF", "Y");
                                    loanActMap.put("ADVERTISE_WAIVE_AMT", advertiseWaiveAmount);
                                } else {
                                    loanActMap.put("ADVERTISE_WAIVE_OFF", "N");
                                }
                                if (isInsuranceWaiveOff) {
                                    loanActMap.put("INSURENCE_WAIVE_OFF", "Y");
                                    loanActMap.put("INSURENCE_WAIVE_AMT", insuranceWaiveAmont);
                                } else {
                                    loanActMap.put("INSURENCE_WAIVE_OFF", "N");
                                }
                                if (isLegalWaiveOff) {
                                    loanActMap.put("LEGAL_WAIVE_OFF", "Y");
                                    loanActMap.put("LEGAL_WAIVE_AMT", legalWaiveAmount);
                                } else {
                                    loanActMap.put("LEGAL_WAIVE_OFF", "N");
                                }
                                if (isMiscellaneousWaiveOff) {
                                    loanActMap.put("MISCELLANEOUS_WAIVE_OFF", "Y");
                                    loanActMap.put("MISCELLANEOUS_WAIVE_AMT", miscellaneousWaiveAmount);
                                } else {
                                    loanActMap.put("MISCELLANEOUS_WAIVE_OFF", "N");
                                }
                                if (isPostageWaiveOff) {
                                    loanActMap.put("POSTAGE_WAIVE_OFF", "Y");
                                    loanActMap.put("POSTAGE_WAIVE_AMT", postageWaiveAmount);
                                } else {
                                    loanActMap.put("POSTAGE_WAIVE_OFF", "N");
                                }
                                // For overdue interest -- start
                                if (isOverDueIntWaiveOff) {
                                    loanActMap.put("OVERDUEINT_WAIVE_OFF", "Y");
                                    loanActMap.put("OVERDUEINT_WAIVE_AMT", overDueIntWaiveAmount);
                                } else {
                                    loanActMap.put("OVERDUEINT_WAIVE_OFF", "N");
                                }
                                // For overdue interest -- end
//                                    transaction.put("MULTIPLE_ALL_AMOUNT", multiple_ALL_LOAN_AMOUNT);
//                                }
                                loanActMap.put("INSURANCE_WAIVER", "N");
                                loanActMap.put("EP_COST_WAIVER", "N");
                                loanActMap.put("INTEREST_WAIVER", "Y");
                                loanActMap.put("PRINCIPAL_WAIVER", "Y");
                                loanActMap.put("ARC_WAIVER", "Y");
                                loanActMap.put("INTEREST", loanInterestMap.get("INTEREST"));
                                loanActMap.put("LEGAL_WAIVER", "N");
                                loanActMap.put("MISCELLANEOUS_WAIVER", "N");
                                loanActMap.put("PRINCIPLE_BAL", null);
                                loanActMap.put("REBATE_MODE", "Transfer");
                                loanActMap.put("OVER_DUE_PRINCIPAL", null);
                                loanActMap.put("REBATE_INTEREST_UPTO", null);
                                loanActMap.put("INSTALL_DT", "");
                                loanActMap.put("CURR_MONTH_PRINCEPLE", CommonUtil.convertObjToStr(creditMap.get("")));
                                loanActMap.put("POSTAGE_WAIVER", "N");
                                loanActMap.put("ARBITRARY_WAIVER", "N");
                                loanActMap.put("CLEAR_BALANCE", CommonUtil.convertObjToStr(creditMap.get("")));
                                loanActMap.put("ADVERTISE_WAIVER", "N");
                                loanActMap.put("ACCT_NUM", CommonUtil.convertObjToStr(creditMap.get("ACT_NUM")));
                                loanActMap.put("OVER_DUE_INTEREST", null);
                                loanActMap.put("NOTICE_WAIVER=", "Y");
                                loanActMap.put("REBATE_INTEREST", "0.0");
                                loanActMap.put("NO_OF_INSTALLMENT", "0");
                                loanActMap.put("LOAN_CLOSING_PENAL_INT", loanInterestMap.get("LOAN_CLOSING_PENAL_INT"));
                                loanActMap.put("DECREE_WAIVER", "N");
                                loanActMap.put("LOAN_BALANCE_PRINCIPAL", CommonUtil.convertObjToStr(creditMap.get("")));
                                loanActMap.put("CURR_MONTH_INT", loanInterestMap.get("INTEREST"));
                                loanActMap.put("PENAL_WAIVER", "Y");
                                loanActMap.put("PENAL_INT", loanInterestMap.get("LOAN_CLOSING_PENAL_INT"));

                                if (recoverChrgMap.containsKey("POSTAGE CHARGES")) {
                                    loanActMap.put("POSTAGE CHARGES", recoverChrgMap.get("POSTAGE CHARGES"));
                                }
                                if (recoverChrgMap.containsKey("ADVERTISE CHARGES")) {
                                    loanActMap.put("ADVERTISE CHARGES", recoverChrgMap.get("ADVERTISE CHARGES"));
                                }
                                if (recoverChrgMap.containsKey("MISCELLANEOUS CHARGES")) {
                                    loanActMap.put("MISCELLANEOUS CHARGES", recoverChrgMap.get("MISCELLANEOUS CHARGES"));
                                }
                                if (recoverChrgMap.containsKey("LEGAL CHARGES")) {
                                    loanActMap.put("LEGAL CHARGES", recoverChrgMap.get("LEGAL CHARGES"));
                                }
                                if (recoverChrgMap.containsKey("INSURANCE CHARGES")) {
                                    loanActMap.put("INSURANCE CHARGES", recoverChrgMap.get("INSURANCE CHARGES"));
                                }
                                if (recoverChrgMap.containsKey("EXECUTION DECREE CHARGES")) {
                                    loanActMap.put("EXECUTION DECREE CHARGES", recoverChrgMap.get("EXECUTION DECREE CHARGES"));
                                }
                                if (recoverChrgMap.containsKey("ARBITRARY CHARGES")) {
                                    loanActMap.put("ARBITRARY CHARGES", recoverChrgMap.get("ARBITRARY CHARGES"));
                                }
                                if (recoverChrgMap.containsKey("NOTICE CHARGES")) {
                                    loanActMap.put("NOTICE CHARGES", recoverChrgMap.get("NOTICE CHARGES"));
                                }
                                if (recoverChrgMap.containsKey("ARC_COST")) {
                                    loanActMap.put("ARC_COST", recoverChrgMap.get("ARC_COST"));
                                }
                                if (recoverChrgMap.containsKey("EP_COST")) {
                                    loanActMap.put("EP_COST", recoverChrgMap.get("EP_COST"));
                                }
                                loanActMap.put("OTHER_CHARGES", otherChargesMap);
                                loanMap.put(CommonUtil.convertObjToStr(creditMap.get("ACT_NUM")), loanActMap);
                                data.put("MULTIPLE_ALL_AMOUNT", loanMap);
                            }
                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(creditMap.get("AC_HD_ID")));
                            txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(creditMap.get("PROD_ID")));
                            txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(creditMap.get("BRANCH_CODE")));
                             txMap.put(TransferTrans.PARTICULARS, " BY DEBIT ACT NO :" + CommonUtil.convertObjToStr(debitMap.get("ACT_NUM")));
                          
                            txMap.put("DR_INSTRUMENT_2", "MobileApplication");
                            txMap.put("SCREEN_NAME", "MobileApplication");
                            txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            txMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(creditMap.get("BRANCH_CODE")));
                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(othersAccountMap.get("amount"))));
                            transactionDAO.setInitiatedBranch(CommonUtil.convertObjToStr(creditMap.get("BRANCH_CODE")));
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            if (CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE")).equals("TD")) {
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(creditMap.get("ACT_NUM")) + "_1");
                            } else if (CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE")).equals("TL")) {
                                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(creditMap.get("ACT_NUM")));
                            } else {
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(debitMap.get("ACT_NUM")));
                            }
                            data.put("TxTransferTO", transferList);
                            data.put("COMMAND", "INSERT");
                            data.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                            data.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                            data.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
                            data.put(CommonConstants.MODULE, "Mobile Application");
                            data.put(CommonConstants.SCREEN, "Mobile Application");
                            data.put("MODE", "MODE");
                            if (CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE")).equals("TD")) {
                                data.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(creditMap.get("ACT_NUM")) + "_1");
                            } else if (CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE")).equals("TL")) {
                                data.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(creditMap.get("ACT_NUM")));
                            } else {
                            data.put("LINK_BATCH_ID", debitMap.get("ACT_NUM"));
                            }
                            HashMap transMap = new HashMap();
                            transMap = transferDAO.execute(data, false);
                            System.out.println("transMap : " + transMap);
                            String authorizeStatus = "AUTHORIZED";
                            HashMap transAuthMap = new HashMap();
                            transAuthMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                            transAuthMap.put("DAILY", "DAILY");
                            transAuthMap.put("BATCH_ID",CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                            transAuthMap.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
                            String linkBatchId = "";
                            if (CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE")).equals("TD")) {
                                linkBatchId = CommonUtil.convertObjToStr(creditMap.get("ACT_NUM")) + "_1";
                            } else if (CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE")).equals("TL")) {
                                linkBatchId = CommonUtil.convertObjToStr(creditMap.get("ACT_NUM"));
                            } else {
                                linkBatchId = CommonUtil.convertObjToStr(debitMap.get("ACT_NUM"));
                            }
                            TransactionDAO.authorizeCashAndTransfer(linkBatchId, authorizeStatus, transAuthMap);
                            if (transMap.containsKey("TRANS_ID")) {
                                HashMap otherBankUpdateMap = new HashMap();
                                otherBankUpdateMap.put("CUST_ID", CommonUtil.convertObjToStr(debitMap.get("CUST_ID")));
                                otherBankUpdateMap.put("DR_ACT_NUM", CommonUtil.convertObjToStr(debitMap.get("ACT_NUM")));
                                otherBankUpdateMap.put("CR_ACT_NUM", CommonUtil.convertObjToStr(creditMap.get("ACT_NUM")));
                                otherBankUpdateMap.put("OTP_NUM", CommonUtil.convertObjToStr(othersAccountMap.get("otpNumber")));
                                otherBankUpdateMap.put("TRANS_ID", CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                                sqlMap.executeUpdate("updateCustomerOTPDetails", otherBankUpdateMap);
                                returnMap.put("TRANS_ID", CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (mapName.equals("otherBankTransaction")) {
                    HashMap appMap = (HashMap) hash.get(CommonConstants.MAP_WHERE);
//                    System.out.println("otherBankTransaction where : " + where + "appMap : " + appMap);
                    String fileorAPIBased = CommonUtil.convertObjToStr(ServerUtil.getCbmsParameterMap().get("TRANSACTION_FOR_FILE_BASED"));
                    if (fileorAPIBased.equals("N")) {
                        HashMap othersAccountMap = (HashMap) hash.get(CommonConstants.MAP_WHERE);
                        System.out.println("otherBankTransaction API Based : " + where + "appMap : " + othersAccountMap + " fileorAPIBased : " + fileorAPIBased);
                        try {
                            HashMap operativeMap = new HashMap();
                            HashMap electronicMap = new HashMap();
                            operativeMap.put("ACT_NUM", othersAccountMap.get("otpDebitActNum"));
                            List debitList = sqlMap.executeQueryForList("getActMasterDataOA", operativeMap);
                            if (debitList != null && debitList.size() > 0) {
                                HashMap debitMap = (HashMap) debitList.get(0);
                                List lst1 = sqlMap.executeQueryForList("getSelectNextUTRNumber", null);
                                if (lst1 != null && lst1.size() > 0) {
                                    electronicMap = (HashMap) lst1.get(0);
                                    electronicMap.put("ACT_NUM", CommonUtil.convertObjToStr(debitMap.get("ACT_NUM")));
                                    electronicMap.put("AMT", CommonUtil.convertObjToDouble(othersAccountMap.get("amount")));
                                    electronicMap.put("TRAN_DT", ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE"))));
                                    electronicMap.put("SOURCE", "Transfer Transaction");
                                    electronicMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                                    electronicMap.put("STATUS", CommonConstants.STATUS_CREATED);
                                    electronicMap.put("IFSCODE", CommonUtil.convertObjToStr(othersAccountMap.get("beneficiaryIFSCode")));
                                    electronicMap.put("BANKACTNUM", CommonUtil.convertObjToStr(othersAccountMap.get("payeeAccount")));
                                    electronicMap.put("INST_TYPE", "IMPS");
                                    electronicMap.put("MULTIPLE_ID", "");
                                    electronicMap.put("PARTICULARS", CommonUtil.convertObjToStr(othersAccountMap.get("NARRATION")));
                                    electronicMap.put("MOBILE_NO", CommonUtil.convertObjToStr(appMap.get("txtMobileNo")));
                                    System.out.println("TransferDAO Fresh Record inserting in neft_ecs_file_creation table : " + electronicMap);
                                    sqlMap.executeUpdate("insertECSNEFTData", electronicMap);
                                    ArrayList selectedTableList = new ArrayList();
                                    ArrayList rowList = new ArrayList();
                                    selectedTableList.add("");
                                    selectedTableList.add(CommonUtil.convertObjToStr(debitMap.get("ACT_NUM")));
                                    selectedTableList.add(ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE"))));
                                    selectedTableList.add(CommonUtil.convertObjToStr(othersAccountMap.get("creditCustName")));
                                    selectedTableList.add(CommonUtil.convertObjToStr("IMPS"));
                                    selectedTableList.add(CommonUtil.convertObjToStr(othersAccountMap.get("amount")));
                                    selectedTableList.add("");
                                    selectedTableList.add(CommonUtil.convertObjToStr(othersAccountMap.get("remarks")));
                                    selectedTableList.add(CommonUtil.convertObjToStr(othersAccountMap.get("beneficiaryIFSCode")));
                                    selectedTableList.add(CommonUtil.convertObjToStr(othersAccountMap.get("payeeAccount")));
                                    selectedTableList.add(CommonUtil.convertObjToStr(othersAccountMap.get("beneficiaryBankName")));
                                    selectedTableList.add(CommonUtil.convertObjToStr(othersAccountMap.get("beneficiaryBranchName")));
                                    selectedTableList.add(CommonUtil.convertObjToStr(electronicMap.get("UTR_NUMBER")));
                                    selectedTableList.add(CommonUtil.convertObjToStr(appMap.get("txtMobileNo")));
                                    rowList.add(selectedTableList);
                                    ElectronicPaymentDAO electronicPaymentDAO = new ElectronicPaymentDAO();
                                    HashMap electronicPayMap = new HashMap();
                                    electronicPayMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                                    electronicPayMap.put("FINAL_PAYMENT_LIST", rowList);
                                    electronicPayMap.put("", "MOBILE APP");
                                    //                                electronicPayMap.put("",CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                                    electronicPayMap.put("", "PUBLIC");
                                    electronicPayMap.put("", "MobileApplication");
                                    electronicPayMap.put("", "MobileApplication");
                                    HashMap electronicResultMap = electronicPaymentDAO.execute(electronicPayMap);
                                    System.out.println("electronicResultMap : " + electronicResultMap);
                                    returnMap.put("INQUIRY_REQUEST_STATUS", CommonUtil.convertObjToStr(electronicResultMap.get("INQUIRY_REQUEST_STATUS")));
                                    returnMap.put("PAYMENT_REQUEST_STATUS", CommonUtil.convertObjToStr(electronicResultMap.get("PAYMENT_REQUEST_STATUS")));
                                    returnMap.put("TRANS_ID", CommonUtil.convertObjToStr(electronicResultMap.get("FROM_BATCH_ID")));
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        System.out.println("otherBankTransaction files Based : " + where + "appMap : " + appMap + " fileorAPIBased : " + fileorAPIBased);
//                try {
                    RtgsRemittanceDAO rtgsRemittanceDAO = new RtgsRemittanceDAO();
                    LinkedHashMap otherBankMap = new LinkedHashMap();
                    allowedTransactionDetailsTO = new LinkedHashMap();
//                    final HashMap data = new HashMap();
                    HashMap operativeMap = new HashMap();
                    operativeMap.put("ACT_NUM", appMap.get("otpDebitActNum"));
                    List lst = sqlMap.executeQueryForList("getActMasterDataOA", operativeMap);
                    if (lst != null && lst.size() > 0) {
                        operativeMap = (HashMap) lst.get(0);
                        appMap.put("BRANCH_CODE", operativeMap.get("BRANCH_CODE"));
                        appMap.put("APPL_DT", operativeMap.get("APPL_DT"));
                        appMap.put("PROD_ID", operativeMap.get("PROD_ID"));
                        appMap.put("CUST_ID", operativeMap.get("CUST_ID"));
                        otherBankMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                        otherBankMap.put("USER_ID", CommonConstants.TTSYSTEM);
                        otherBankMap.put("BRANCH_CODE", operativeMap.get("BRANCH_CODE"));
                        otherBankMap.put("otherBankTransaction", "otherBankTransaction");
                        otherBankMap.put("RTGS_DATA", setOtherBankCreditDetails(appMap));
                        System.out.println("otherBankMap : " + otherBankMap);
                        allowedTransactionDetailsTO.put(String.valueOf(1), setOtherBankDebitDetails(appMap));
                        if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
                            if (transactionDetailsTO == null) {
                                transactionDetailsTO = new LinkedHashMap();
                            }
                            transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                            otherBankMap.put("TransactionTO", transactionDetailsTO);
                            allowedTransactionDetailsTO = null;
                        }
                        HashMap resultMap = rtgsRemittanceDAO.execute(otherBankMap);
                        System.out.println("resultMap : " + resultMap);
                        ArrayList arrList = new ArrayList();
                        otherBankMap.put("COMMAND", "");
//                        otherBankMap = new LinkedHashMap();
                        HashMap authorizeMap = new HashMap();
                        HashMap singleAuthorizeMap = new HashMap();
                        otherBankMap.put("USER_ID", CommonConstants.TTSYSTEM);
                        otherBankMap.put("BRANCH_CODE", operativeMap.get("BRANCH_CODE"));
                        singleAuthorizeMap.put("STATUS", "AUTHORIZED");
                        singleAuthorizeMap.put("RTGS_ID", resultMap.get("RTGS_ID"));
                        singleAuthorizeMap.put("AUTHORIZED_BY", CommonConstants.TTSYSTEM);
                        singleAuthorizeMap.put("AUTHORIZED_DT", operativeMap.get("APPL_DT"));
                        arrList.add(singleAuthorizeMap);
                        authorizeMap.put(CommonConstants.AUTHORIZESTATUS, "AUTHORIZED");
                        authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                        otherBankMap.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                        returnMap = rtgsRemittanceDAO.execute(otherBankMap);
                        if (singleAuthorizeMap.containsKey("RTGS_ID")) {
                            HashMap otherBankUpdateMap = new HashMap();
                            otherBankUpdateMap.put("CUST_ID", CommonUtil.convertObjToStr(appMap.get("CUST_ID")));
                            otherBankUpdateMap.put("DR_ACT_NUM", CommonUtil.convertObjToStr(appMap.get("otpDebitActNum")));
                            otherBankUpdateMap.put("CR_ACT_NUM", CommonUtil.convertObjToStr(appMap.get("payeeAccount")));
                            otherBankUpdateMap.put("OTP_NUM", CommonUtil.convertObjToStr(appMap.get("otpNumber")));
                            otherBankUpdateMap.put("TRANS_ID", CommonUtil.convertObjToStr(singleAuthorizeMap.get("RTGS_ID")));
                            sqlMap.executeUpdate("updateCustomerOTPDetails", otherBankUpdateMap);
                            returnMap.put("TRANS_ID", CommonUtil.convertObjToStr(singleAuthorizeMap.get("RTGS_ID")));
                        }
                        System.out.println("resultMap : " + resultMap);
                    }
                    }
                } else if (mapName.equals("utilityTransaction")) {
                    HashMap othersAccountMap = (HashMap) hash.get(CommonConstants.MAP_WHERE);
                    System.out.println("othersAccountsTransaction where : " + where + "appMap : " + othersAccountMap);
                    try {
                        TransferTrans objTransferTrans = new TransferTrans();
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        TransferDAO transferDAO = new TransferDAO();
                        HashMap data = new HashMap();
                        HashMap operativeMap = new HashMap();
                        operativeMap.put("ACT_NUM", othersAccountMap.get("otpDebitActNum"));
                        List debitList = sqlMap.executeQueryForList("getActMasterDataOA", operativeMap);
                        operativeMap.put("ACT_NUM", othersAccountMap.get("payeeAccount"));
                        List creditList = sqlMap.executeQueryForList("getActMasterDataOA", operativeMap);
                        if (debitList != null && debitList.size() > 0 && creditList != null && creditList.size() > 0) {
                            HashMap debitMap = (HashMap) debitList.get(0);
                            HashMap creditMap = (HashMap) creditList.get(0);
                            String generateSingleTransId = generateLinkID(CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                            HashMap txMap = new HashMap();
                            ArrayList transferList = new ArrayList();
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(debitMap.get(debitMap.get("AC_HD_ID"))));
                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(debitMap.get("PROD_ID")));
                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(debitMap.get("ACT_NUM")));
                            txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                            txMap.put(TransferTrans.PARTICULARS, "DEBIT ACT NO :" + CommonUtil.convertObjToStr(debitMap.get("ACT_NUM")));
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                            txMap.put("DR_INSTRUMENT_2", "MobileApplication");
                            txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            txMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                            txMap.put("SCREEN_NAME", "MobileApplication");
//                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
//                        transferTo = transactionDAO.addTransferDebitLocal(txMap, 1 * -1);
                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(othersAccountMap.get("amount"))));
                            txMap = new HashMap();
                            if (CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE")).equals("SA")
                                    || CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE")).equals("GL")) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            } else {
                                txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(creditMap.get("PROD_ID")));
                                txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE")));
                                txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(othersAccountMap.get("payeeAccount")));
                            }
                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(creditMap.get("AC_HD_ID")));
                            txMap.put(TransferTrans.CR_BRANCH, CommonUtil.convertObjToStr(creditMap.get("BRANCH_CODE")));
                            txMap.put(TransferTrans.PARTICULARS, "DEBIT ACT NO :" + CommonUtil.convertObjToStr(debitMap.get("ACT_NUM")));
                            txMap.put("TRANS_MOD_TYPE", CommonUtil.convertObjToStr(creditMap.get("PROD_TYPE")));
                            txMap.put("DR_INSTRUMENT_2", "MobileApplication");
                            txMap.put("SCREEN_NAME", "MobileApplication");
                            txMap.put("AUTHORIZE_STATUS_2", "ENTERED_AMOUNT");
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            txMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(othersAccountMap.get("amount"))));
                            transactionDAO.setInitiatedBranch(CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(debitMap.get("ACT_NUM")));

                            data.put("TxTransferTO", transferList);
                            data.put("COMMAND", "INSERT");
                            data.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                            data.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                            data.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
                            data.put(CommonConstants.MODULE, "Mobile Application");
                            data.put(CommonConstants.SCREEN, "Mobile Application");
                            data.put("MODE", "MODE");
                            data.put("LINK_BATCH_ID", debitMap.get("ACT_NUM"));
                            HashMap transMap = new HashMap();
                            transMap = transferDAO.execute(data, false);
                            System.out.println("transMap : " + transMap);
                            String authorizeStatus = "AUTHORIZED";
                            HashMap transAuthMap = new HashMap();
                            transAuthMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(debitMap.get("BRANCH_CODE")));
                            transAuthMap.put("DAILY", "DAILY");
                            transAuthMap.put("BATCH_ID",CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                            transAuthMap.put(CommonConstants.USER_ID, CommonConstants.TTSYSTEM);
                            TransactionDAO.authorizeCashAndTransfer(CommonUtil.convertObjToStr(debitMap.get("ACT_NUM")), authorizeStatus, transAuthMap);
                            if (transMap.containsKey("TRANS_ID")) {
                                HashMap otherBankUpdateMap = new HashMap();
                                otherBankUpdateMap.put("CUST_ID", CommonUtil.convertObjToStr(debitMap.get("CUST_ID")));
                                otherBankUpdateMap.put("DR_ACT_NUM", CommonUtil.convertObjToStr(debitMap.get("ACT_NUM")));
                                otherBankUpdateMap.put("CR_ACT_NUM", CommonUtil.convertObjToStr(othersAccountMap.get("payeeAccount")));
                                otherBankUpdateMap.put("OTP_NUM", CommonUtil.convertObjToStr(othersAccountMap.get("otpNumber")));
                                otherBankUpdateMap.put("TRANS_ID", CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                                sqlMap.executeUpdate("updateCustomerOTPDetails", otherBankUpdateMap);
                                returnMap.put("TRANS_ID", CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (mapName.equals("LoanCalculationDetails")) {
                    HashMap calculatioMap = (HashMap) hash.get(CommonConstants.MAP_WHERE);
                    System.out.println("calculatioMap : " + calculatioMap);
                    if (calculatioMap != null && !calculatioMap.isEmpty()) {
                        HashMap map = new HashMap();
                        try {
//                            String prod_id = "";
                            String _branchCode = (String) calculatioMap.get("BRANCH_ID");
                            hash.put("ACT_NUM", calculatioMap.get("ACT_NUM"));
                            hash.put("CUST_ID", calculatioMap.get("CUST_ID"));
                            hash.put("PROD_ID", calculatioMap.get("PROD_ID"));
                            hash.put("PROD_TYPE", calculatioMap.get("PROD_TYPE"));
                            hash.put("INITIATED_BRANCH", _branchCode);
                            returnMap = calculatedInterestDetails(hash);
                            System.out.println("Calculated returnMap : " + returnMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    sqlMap.executeUpdate(mapName, where);
                    returnMap.put("returnmessage", "SUCCESS");
                }
            }
            // commit transaction
            sqlMap.commitTransaction();
        } catch (Exception th) {
            try {
                returnMap.put("returnmessage", "FAILURE");
                th.printStackTrace();
                returnMap.put("resetPasswordMessage", "Invalid User name or password");
                // if something goes wrong, rollback the transaction
                sqlMap.rollbackTransaction();
            } catch (Exception thf) {
            }
            // throw the Throable object
            throw th;
        }
        //System.out.println("updateAppDataValues returnMap : " + returnMap);
        return returnMap;
    }

    private LinkedHashMap setOtherBankCreditDetails(HashMap otherBankMap) throws Exception {
        LinkedHashMap RTGSmap = new LinkedHashMap();
        final RtgsRemittanceTO rtgsRemittanceTO = new RtgsRemittanceTO();
        rtgsRemittanceTO.setStatus(CommonConstants.STATUS_CREATED);
        rtgsRemittanceTO.setProdId(CommonUtil.convertObjToStr("NEFT"));
        rtgsRemittanceTO.setIfsc_Code(CommonUtil.convertObjToStr(otherBankMap.get("beneficiaryIFSCode")));
        rtgsRemittanceTO.setBeneficiary_Bank(CommonUtil.convertObjToStr(otherBankMap.get("beneficiaryBankCode")));
        rtgsRemittanceTO.setBeneficiary_Bank_Name(CommonUtil.convertObjToStr(otherBankMap.get("beneficiaryBankName")));
        rtgsRemittanceTO.setBeneficiary_Branch(CommonUtil.convertObjToStr(otherBankMap.get("beneficiaryBranchCode")));
        rtgsRemittanceTO.setBeneficiary_Branch_Name(CommonUtil.convertObjToStr(otherBankMap.get("beneficiaryBranchName")));
        rtgsRemittanceTO.setBeneficiary_IFSC_Code(CommonUtil.convertObjToStr(otherBankMap.get("beneficiaryIFSCode")));
        rtgsRemittanceTO.setBeneficiary_Name(CommonUtil.convertObjToStr(otherBankMap.get("creditCustName")));
        rtgsRemittanceTO.setAmount(CommonUtil.convertObjToDouble(otherBankMap.get("amount")));
        rtgsRemittanceTO.setAccount_No(CommonUtil.convertObjToStr(otherBankMap.get("payeeAccount")));
        rtgsRemittanceTO.setRemarks(CommonUtil.convertObjToStr(otherBankMap.get("remarks")));
        rtgsRemittanceTO.setEx_Calculated(CommonUtil.convertObjToDouble(otherBankMap.get("")));
        rtgsRemittanceTO.setEx_Collected(CommonUtil.convertObjToDouble(otherBankMap.get("")));
        rtgsRemittanceTO.setService_Tax(CommonUtil.convertObjToDouble(otherBankMap.get("")));
        rtgsRemittanceTO.setCharges(CommonUtil.convertObjToDouble(otherBankMap.get("")));
        rtgsRemittanceTO.setTotalAmt(CommonUtil.convertObjToDouble(otherBankMap.get("amount")));
        rtgsRemittanceTO.setRtgs_ID("");
        rtgsRemittanceTO.setSlNo(String.valueOf(1));
        rtgsRemittanceTO.setStatusDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(otherBankMap.get("APPL_DT"))));
        rtgsRemittanceTO.setStatusBy(CommonConstants.TTSYSTEM);
        rtgsRemittanceTO.setInitiatedBranch(CommonUtil.convertObjToStr(otherBankMap.get("BRANCH_CODE")));
        RTGSmap.put(1, rtgsRemittanceTO);
        return RTGSmap;
    }

    private TransactionTO setOtherBankDebitDetails(HashMap otherBankMap) throws Exception {
        //log.info("setTransactionTO()");
        ////System.out.println("(String)cbmInstrumentType.getKeyForSelected() : " + (String)cbmInstrumentType.getKeyForSelected());
        TransactionTO objTransactionTO = new TransactionTO();
        try {
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(otherBankMap.get("")));
            objTransactionTO.setApplName(CommonUtil.convertObjToStr(otherBankMap.get("")));

//            Date BchDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getBatchDt()));
//            if (BchDt != null) {
//                Date bchDate = (Date) curDate.clone();
//                bchDate.setDate(BchDt.getDate());
//                bchDate.setMonth(BchDt.getMonth());
//                bchDate.setYear(BchDt.getYear());
//                objTransactionTO.setBatchDt(bchDate);
//            } else {
//                objTransactionTO.setBatchDt(CommonUtil.convertObjToStr(otherBankMap.get("")));
//            }
//            objTransactionTO.setBatchDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getBatchDt())));
            objTransactionTO.setChequeNo(CommonUtil.convertObjToStr(otherBankMap.get("")));
            objTransactionTO.setChequeNo2(CommonUtil.convertObjToStr(otherBankMap.get("")));
            objTransactionTO.setBranchId(CommonUtil.convertObjToStr(otherBankMap.get("")));

//            Date ChDt = DateUtil.getDateMMDDYYYY(getTdtChequeDate());
//            if (ChDt != null) {
//                Date ChDate = (Date) curDate.clone();
//                ChDate.setDate(ChDt.getDate());
//                ChDate.setMonth(ChDt.getMonth());
//                ChDate.setYear(ChDt.getYear());
//                objTransactionTO.setChequeDt(ChDate);
//            } else {
//                objTransactionTO.setChequeDt(DateUtil.getDateMMDDYYYY(getTdtChequeDate()));
//            }
//            objTransactionTO.setChequeDt(DateUtil.getDateMMDDYYYY(getTdtChequeDate()));
            //Added by sreekrishnan
//            if (CommonUtil.convertObjToStr(cbmProductType.getKeyForSelected()) != null
//                    && !CommonUtil.convertObjToStr((String) cbmProductType.getKeyForSelected()).equals("")
//                    && CommonUtil.convertObjToStr((String) cbmProductType.getKeyForSelected()).equalsIgnoreCase("SH")) {
            objTransactionTO.setProductType(CommonUtil.convertObjToStr("OA"));
//            } else {
//                objTransactionTO.setProductType(CommonUtil.convertObjToStr((String) cbmProductType.getKeyForSelected()));
//            }
//            if (CommonUtil.convertObjToStr(cbmProductType.getKeyForSelected()) != null
//                    && !CommonUtil.convertObjToStr((String) cbmProductType.getKeyForSelected()).equals("")
//                    && CommonUtil.convertObjToStr((String) cbmProductType.getKeyForSelected()).equalsIgnoreCase("SH")) {
//            objTransactionTO.setProductId(CommonUtil.convertObjToStr(otherBankMap.get("")));
//            } else {
            objTransactionTO.setProductId(CommonUtil.convertObjToStr(otherBankMap.get("PROD_ID")));
//            }
            objTransactionTO.setInstType(CommonUtil.convertObjToStr(otherBankMap.get("")));
            objTransactionTO.setDebitAcctNo(CommonUtil.convertObjToStr(otherBankMap.get("otpDebitActNum")));
            objTransactionTO.setTransAmt(CommonUtil.convertObjToDouble(otherBankMap.get("amount")));
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(otherBankMap.get("")));
            objTransactionTO.setTransType(CommonUtil.convertObjToStr(otherBankMap.get("")));
            objTransactionTO.setCommand(CommonUtil.convertObjToStr(CommonConstants.TOSTATUS_INSERT));
            objTransactionTO.setTokenNo(CommonUtil.convertObjToStr(otherBankMap.get("")));
            objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
            objTransactionTO.setParticulars(CommonUtil.convertObjToStr(otherBankMap.get("remarks")));
            ////System.out.println("objTransactionTO : " + objTransactionTO);
        } catch (Exception e) {
            e.printStackTrace();
//            parseException.logException(e, true);
        }
        //System.out.println("objTransactionTO^$^$^$^$$"+objTransactionTO);
        return objTransactionTO;
    }

    private String generateLinkID(String _branchCode) throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    public HashMap loanInterestCalculationAsAndWhen(HashMap whereMap) {
        TaskHeader header = new TaskHeader();
        HashMap returnMap = new HashMap();
        whereMap.put("DB_DRIVER_NAME", dbDriverName);
        header.setBranchID(CommonUtil.convertObjToStr(whereMap.get("BRANCH_CODE")));
        InterestCalculationTask interestcalTask;
        try {
            interestcalTask = new InterestCalculationTask(header);
            HashMap resultMap = interestcalTask.interestCalcTermLoanAD(whereMap);
            List lst = new ArrayList();
            lst.add(resultMap);
            returnMap.put(CommonConstants.DATA, lst);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return returnMap;
    }

    private HashMap calculatedInterestDetails(HashMap calculationMap) throws Exception {
        System.out.println("calculatedInterestDetails calculatioMap : " + calculationMap);
        HashMap whereMap = new HashMap();
        whereMap = (HashMap) calculationMap.get(CommonConstants.MAP_WHERE);
        String mapNameForCalcInt = "IntCalculationDetail";
        if (calculationMap.get("PROD_TYPE").equals("AD")) {
            mapNameForCalcInt = "IntCalculationDetailAD";
        }
        HashMap loanHash = new HashMap();
        HashMap loanDataMap = new HashMap();
        HashMap calculatedDetails = new HashMap();
        List lst = sqlMap.executeQueryForList(mapNameForCalcInt, calculationMap);
        if (lst != null && lst.size() > 0) {
            loanHash = (HashMap) lst.get(0);
            if (loanHash.get("AS_CUSTOMER_COMES") != null && loanHash.get("AS_CUSTOMER_COMES").equals("N")) {
                loanHash = new HashMap();
                return loanHash;
            }
            String _branchCode = (String) whereMap.get("BRANCH_ID");
            calculationMap.put("BRANCH_ID", _branchCode);  // Changed by Rajesh to get interest for Other branch a/cs
//            calculatioMap.put("BRANCH_CODE", _branchCode);
            calculationMap.putAll(loanHash);
            System.err.println("branchCode : " + _branchCode);
            calculationMap.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
            calculationMap.put("BRANCH_CODE", _branchCode);
            calculationMap.put("CURR_DATE", ServerUtil.getCurrentDate(_branchCode));
            Date lastCalcIntDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(loanHash.get("LAST_INT_CALC_DT")));
            loanHash = loanInterestCalculationAsAndWhen(calculationMap);
            if (loanHash == null) {
                loanHash = new HashMap();
            }
            loanHash.putAll(calculationMap);
            loanHash.put("AS_CUSTOMER_COMES", calculationMap.get("AS_CUSTOMER_COMES"));
            System.out.println("loanHash : " + loanHash);
            HashMap customerMap = new HashMap();
            customerMap.put("CUST_ID", loanHash.get("CUST_ID"));
            ArrayList dataList = new ArrayList();
            dataList = (ArrayList) loanHash.get("DATA");
            HashMap hash = new HashMap();
            System.out.println("dataList : " + dataList + " size : " + dataList.size());
            if (dataList != null && dataList.size() > 0) {
                for (int j = 0; j < dataList.size(); j++) {
                    loanDataMap = (HashMap) dataList.get(j);
                    loanDataMap.put("CURR_MONTH_INT", loanDataMap.get("INTEREST"));
                    loanDataMap.put("ACT_NUM", loanDataMap.get("ACT_NUM"));
                    hash.put("FROM_DT", DateUtil.addDays((lastCalcIntDt), 2));
                    hash.put("TO_DATE", ServerUtil.getCurrentDate(_branchCode));
                    hash.put("ACT_NUM", calculationMap.get("ACT_NUM"));
                    List lst1 = sqlMap.executeQueryForList("getPaidPrinciple", hash);
                    if (lst1 != null && lst1.size() > 0) {
                        loanHash = (HashMap) lst1.get(0);
                        double penal = CommonUtil.convertObjToDouble(loanHash.get("PENAL")).doubleValue();
                        double interest = CommonUtil.convertObjToDouble(loanHash.get("INTEREST")).doubleValue();
                        double principal = CommonUtil.convertObjToDouble(loanHash.get("PRINCIPLE")).doubleValue();
                        double actualPenal = CommonUtil.convertObjToDouble(loanDataMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                        double actualinterest = CommonUtil.convertObjToDouble(loanDataMap.get("INTEREST")).doubleValue();
                        double actualCharges = CommonUtil.convertObjToDouble(loanDataMap.get("CHARGES")).doubleValue();
                        actualPenal = actualPenal - penal;
                        actualinterest = actualinterest - interest;
                        if (actualPenal < 0) {
                            actualPenal = 0;
                        }
                        if (actualinterest < 0) {
                            actualinterest = 0;
                        }
                        loanDataMap.put("INTEREST", actualinterest);
                        loanDataMap.put("LOAN_CLOSING_PENAL_INT", actualPenal);
                        loanDataMap.put("INTEREST", actualinterest);
//                        loanDataMap.put("ROI", loanHash.get("ROI"));
                        if (loanDataMap.containsKey("LAST_INT_CALC_DT") && loanDataMap.get("LAST_INT_CALC_DT") != null) {
                            Date lastintdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(loanDataMap.get("LAST_INT_CALC_DT")));
                            long noOfintDays = DateUtil.dateDiff(lastintdt, ServerUtil.getCurrentDate(_branchCode)) - 1;
                            loanDataMap.put("INTEREST_PERIOD", noOfintDays);
                        }
                    }
                    System.out.println("dataMap : " + loanDataMap);
                }
                customerMap.put("SELF_CREDIT_SINGLE", "SELF_CREDIT_SINGLE");
                customerMap.put("ACT_NUM", calculationMap.get("ACT_NUM"));
                List lst2 = sqlMap.executeQueryForList("getSelfAccountList", customerMap);
                if (lst2 != null && lst2.size() > 0) {
                    customerMap = (HashMap) lst2.get(0);
                    loanDataMap.put("AVAILABLE_BALANCE", customerMap.get("AVAILABLE_BALANCE"));
                    loanDataMap.put("PROD_DESC", customerMap.get("PROD_DESC"));
                    loanDataMap.put("NAME", customerMap.get("NAME"));
                    loanDataMap.put("LAST_INT_CALC_DT", customerMap.get("LAST_INT_CALC_DT"));
                }
                loanDataMap.put("CHARGE_AMT", getChargeAmount(loanDataMap, CommonUtil.convertObjToStr(calculationMap.get("PROD_TYPE"))));
                calculatedDetails.put("LOAN_DATA_MAP", loanDataMap);
            }
        }
        return calculatedDetails;
    }

    private double getChargeAmount(HashMap whereMap, String prodType) {   //Charges
        double chargeAmount = 0.0;
        try {
            List chargeList = null;
            String actNo = "";
            recoverChrgMap = new HashMap();
            recoverChrgMap.put("CHARGE_AMT", "0.0");
            actNo = CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
            chargeList = ServerUtil.executeQuery("getChargeDetails", whereMap);
            if (chargeList != null && chargeList.size() > 0) {
                for (int i = 0; i < chargeList.size(); i++) {
                    whereMap = (HashMap) chargeList.get(i);
                    chargeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                    double chargeAmt = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                    if (whereMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
                        recoverChrgMap.put("POSTAGE CHARGES", whereMap.get("CHARGE_AMT"));
                    } else if (whereMap.get("CHARGE_TYPE").equals("ADVERTISE CHARGES") && chargeAmt > 0) {
                        recoverChrgMap.put("ADVERTISE CHARGES", whereMap.get("CHARGE_AMT"));
                    } else if (whereMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
                        recoverChrgMap.put("MISCELLANEOUS CHARGES", whereMap.get("CHARGE_AMT"));
                    } else if (whereMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                        recoverChrgMap.put("LEGAL CHARGES", whereMap.get("CHARGE_AMT"));
                    } else if (whereMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
                        recoverChrgMap.put("INSURANCE CHARGES", whereMap.get("CHARGE_AMT"));
                    } else if (whereMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                        recoverChrgMap.put("EXECUTION DECREE CHARGES", whereMap.get("CHARGE_AMT"));
                    } else if (whereMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
                        recoverChrgMap.put("ARBITRARY CHARGES", whereMap.get("CHARGE_AMT"));
                    } else if (whereMap.get("CHARGE_TYPE").equals("NOTICE CHARGES") && chargeAmt > 0) {
                        recoverChrgMap.put("NOTICE CHARGES", whereMap.get("CHARGE_AMT"));
                    } else if (whereMap.get("CHARGE_TYPE").equals("ARC_COST") && chargeAmt > 0) {
                        recoverChrgMap.put("ARC_COST", whereMap.get("CHARGE_AMT"));
                    } else if (whereMap.get("CHARGE_TYPE").equals("EP_COST") && chargeAmt > 0) {
                        recoverChrgMap.put("EP_COST", whereMap.get("CHARGE_AMT"));
                    } else {
                        otherChargesMap.put(whereMap.get("CHARGE_TYPE"), whereMap.get("CHARGE_AMT"));
                    }
                }
            }
            chargeList = null;
//            recoverChrgMap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeAmount;
    }
}
