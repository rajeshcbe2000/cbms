/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountsDAO.java
 *
 * Created on September 08, 2003, 1:00 PM
 */
package com.see.truetransact.serverside.operativeaccount;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.operativeaccount.*;
import com.see.truetransact.transferobject.operativeaccount.CardAccountTO;


import com.see.truetransact.serverside.common.nominee.NomineeDAO;
import com.see.truetransact.serverside.common.powerofattorney.PowerOfAttorneyDAO;
import com.see.truetransact.serverside.common.authorizedsignatory.AuthorizedSignatoryDAO;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.see.truetransact.serverside.customer.CustomerHistoryDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.customer.CustomerHistoryTO;
import com.see.truetransact.commonutil.AcctStatusConstants;


import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
//import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
//import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;
import com.see.truetransact.serverside.remittance.RemittanceIssueDAO;

import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;

import com.see.truetransact.serverside.common.transaction.TransactionDAO;
//import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.remittance.RemittanceIssueDAO;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;

import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.transaction.ATMTrans.ATMAcknowledgementTO;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * This is used for Accounts Data Access.
 *
 * @author 152721
 */
public class AccountDAO extends TTDAO {

    static SqlMap sqlMap = null;
    final String SCREEN = "OA";
    String userID = "";
    String transID = "";
//    for transaction during account opening
    String newTransactionID = "";
    private final static Logger log = Logger.getLogger(AccountDAO.class);
    private LinkedHashMap mapJointAccntTO;
    private String accountNo;
    public String prod_id;
    private SMSSubscriptionTO objSMSSubscriptionTO = null;
    private int ibrHierarchy = 0;
    private Date currDt = null;
    private LinkedHashMap cardDetailsMap;
    private HashMap suretyDetailsMap; // Added by nithya
    private HashMap deletedSuretyTableValues; // Added by nithya
    private Date properFormatDate; // Added by nithya
    private CardAccountTO objCardAccountTO;
    static {
        try {
            ServiceLocator locate = ServiceLocator.getInstance();
            sqlMap = (SqlMap) locate.getDAOSqlMap();
        } catch (ServiceLocatorException se) {
            System.err.println(se);
            log.error(se);
        }
    }

    /**
     * Creates a new instance of AccountsDAO
     */
    public AccountDAO() {
    }

    /*
     * this method will get the data against any query with some comdition
     */
    public Object getMiscData(String map, String where) {
        List list = null;
        try {
            list = (List) sqlMap.executeQueryForList(map, where);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException("Error initializing SqlConfig class. Cause: " + e);
        }
        return (Object) list;
    }
    /*
     * this method is used to generate an account number on a fly when creating
     * a new account
     */

    private String getAccountNumber() throws Exception {
        HashMap where = new HashMap();
        String genID = null;
//                return genID;


        HashMap mapData = new HashMap();
        String strPrefix = "";
        String strNum = "";
        int len = 13;
        where.put("PROD_ID", prod_id);
        where.put(CommonConstants.BRANCH_ID, _branchCode);
//        List lst = (List) sqlMap.executeQueryForList("getNextActNum", where);
        List lst = (List) sqlMap.executeQueryForList("getCoreBankNextActNum", where);
        if (lst != null && lst.size() > 0) {
            mapData = (HashMap) lst.get(0);
            if (mapData.containsKey("PREFIX")) {
                strPrefix = (String) mapData.get("PREFIX");
            }
            int numFrom = strPrefix.trim().length();
            String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
            //System.out.println("@@@@@@@@" + newID);
            String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
            //System.out.println("@@@@@@@@" + nxtID);
            genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
            where.put("VALUE", nxtID);
            //        sqlMap.executeUpdate("updateNextId", where);   
            sqlMap.executeUpdate("updateCoreBankNextActNum", where);
        } else {
            len = 10;
            lst = (List) sqlMap.executeQueryForList("getNextActNum", where);
            if (lst != null && lst.size() > 0) {
                mapData = (HashMap) lst.get(0);
            }
            if (mapData.containsKey("PREFIX") && mapData.get("PREFIX")!=null) {
                strPrefix = (String) mapData.get("PREFIX");
            }
            int numFrom = strPrefix.trim().length();
            String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
            //System.out.println("@@@@@@@@" + newID);
            String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
            //System.out.println("@@@@@@@@" + nxtID);
            genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
            where.put("VALUE", nxtID);
            sqlMap.executeUpdate("updateNextId", where);
        }
        return genID;
    }

    private void executeTransactionPart(HashMap map, HashMap transDetailMap, String accountNumber) throws Exception {
        try {
            transDetailMap.put("OPERATIVE_NO", accountNumber);
            transDetailMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
            //System.out.println("@##$#$% map #### :" + map);
            //System.out.println("@##$#$% transDetailMap #### :" + transDetailMap);
            if (transDetailMap != null && transDetailMap.size() > 0) {
                if (transDetailMap.containsKey("TRANSACTION_PART")) {
//                    transDetailMap = getProdBehavesLike(transDetailMap);
                    if(map.containsKey("FROM_MOBILE_APP_CUST_CREATION") && map.get("FROM_MOBILE_APP_CUST_CREATION") != null && map.get("FROM_MOBILE_APP_CUST_CREATION").equals("FROM_MOBILE_APP_CUST_CREATION")){
                        //System.out.println("for FROM_MOBILE_APP_CUST_CREATION transaction");
                        doSBOpeningProcessFromMobileData(map, transDetailMap, accountNumber);
                    }else{
                    insertTimeTransaction(map, transDetailMap, accountNumber);
                    }

                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private String generateLinkID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }

    private void insertTimeTransaction(HashMap map, HashMap dataMap, String accountNumber) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap acHeads = new HashMap();
        TransactionDAO transactionDAO = null;
        TxTransferTO transferDao = null;
        ArrayList transList = new ArrayList();
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        //System.out.println("mapTRANSACTION_PART " + map + "dataMap :" + dataMap);
        if (dataMap.containsKey("TRANSACTION_PART")) {
            HashMap reserchMap = new HashMap();
            //            reserchMap = (HashMap)map.get("INT_TRANSACTION_REPAYMENT");
            String branchId = CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE"));
            try {
                dataMap.put("OPERATIVE_NO", accountNumber);
                newTransactionID = CommonUtil.convertObjToStr(accountNumber);
//                acHeads = (HashMap)sqlMap.executeQueryForObject("getLoanAccountClosingHeads", dataMap.get("ACT_NUM"));
//                System.out.println("acHeads "+acHeads);
                if (dataMap.get("TRANS_TYPE") != null && dataMap.get("TRANS_TYPE").equals("CASH")) {
                    HashMap operativeAuthTransMap = new HashMap();
                    operativeAuthTransMap.put("SELECTED_BRANCH_ID", dataMap.get("BRANCH_CODE"));
                    operativeAuthTransMap.put("OPERATIVE_NO", dataMap.get("OPERATIVE_NO"));
                    operativeAuthTransMap.put("PROD_ID", dataMap.get("PROD_ID"));
                    operativeAuthTransMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                    operativeAuthTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    operativeAuthTransMap.put("ACCT_HEAD", dataMap.get("ACCT_HEAD"));
                    operativeAuthTransMap.put("OPERATIVE_AMOUNT", dataMap.get("OPERATIVE_AMOUNT"));
                    operativeAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                    operativeAuthTransMap.put("TRANS_MOD_TYPE", "OA");
                    ArrayList depositAuthTransList = operativeAuthorizeTimeTransaction(operativeAuthTransMap);
                    operativeAuthTransMap.put("DAILYDEPOSITTRANSTO", depositAuthTransList);
                    operativeAuthTransMap.put("DEBIT_LOAN_TYPE", "DP");
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    HashMap cashMap = cashTransactionDAO.execute(operativeAuthTransMap, false);
                    //System.out.println("cashMap :" + cashMap);
                    cashMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                    String authorizeStatus = CommonConstants.STATUS_AUTHORIZED;
                    ArrayList arrList = new ArrayList();
                    HashMap authDataMap = new HashMap();
                    HashMap authStatusMap = new HashMap();
                    arrList = null;
                    returnMap = null;
                    acHeads = null;
                    txMap = null;
                    reserchMap = null;
                    operativeAuthTransMap = null;
                    authDataMap = null;
                    authStatusMap = null;
                } else if (dataMap.get("TRANS_TYPE") != null && dataMap.get("TRANS_TYPE").equals("TRANSFER")) {
                    transactionDAO.setLinkBatchID((String) dataMap.get("OPERATIVE_NO"));
                    transactionDAO.setInitiatedBranch(branchId);
                    HashMap crMap = new HashMap();
                    crMap.put("ACT_NUM", dataMap.get("CR_ACT_NUM"));
                    List oaAcctHead = sqlMap.executeQueryForList("getAccNoProdIdDet", crMap);
                    if (oaAcctHead != null && oaAcctHead.size() > 0) {
                        crMap = (HashMap) oaAcctHead.get(0);
                    }
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    TxTransferTO transferTo = new TxTransferTO();
                    double interestAmt = CommonUtil.convertObjToDouble(dataMap.get("OPERATIVE_AMOUNT")).doubleValue();
                    txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("OPERATIVE_NO"));
                    txMap.put(TransferTrans.CR_AC_HD, dataMap.get("ACCT_HEAD"));
                    txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                    txMap.put(TransferTrans.PARTICULARS, "AccountOpening - " + dataMap.get("OPERATIVE_NO"));
                    txMap.put("AUTHORIZEREMARKS", "DP");
//                    txMap.put("DEBIT_LOAN_TYPE", "DP");

                    txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("CR_ACT_NUM"));
                    txMap.put(TransferTrans.DR_AC_HD, crMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.DR_PROD_ID, crMap.get("PROD_ID"));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    txMap.put(TransferTrans.DR_BRANCH, dataMap.get("BRANCH_CODE"));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    //System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);

                    transferTo = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                    transferTo.setInstrumentNo2("OPERATIVE_TRANS");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setLinkBatchId((String) dataMap.get("OPERATIVE_NO"));
                    transferTo.setTransModType("OA");
                    transferList.add(transferTo);
                    txMap.put(TransferTrans.PARTICULARS, dataMap.get("OPERATIVE_NO"));
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                    transferTo.setInstrumentNo2("OPERATIVE_TRANS");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setLinkBatchId((String) dataMap.get("OPERATIVE_NO"));
                    transferTo.setTransModType("OA");
                    transferList.add(transferTo);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));

                    transList = null;
                    transactionDAO = null;
                    transferDao = null;
                    txMap = null;
                    acHeads = null;
                }
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
            }
            transList = null;
            transactionDAO = null;
            transferDao = null;
            txMap = null;
            acHeads = null;
        }
    }

    private ArrayList operativeAuthorizeTimeTransaction(HashMap dataMap) throws Exception {
        ArrayList cashList = new ArrayList();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("OPERATIVE_NO")));
            objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.OPERATIVE));
            objCashTO.setTransType(CommonConstants.CREDIT);
            objCashTO.setParticulars("AccountOpening - " + dataMap.get("OPERATIVE_NO"));
            objCashTO.setAuthorizeRemarks("DP");
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("OPERATIVE_AMOUNT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("OPERATIVE_AMOUNT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setStatusDt(currDt);
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("OPERATIVE_NO")));
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            objCashTO.setCommand("INSERT");
            objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
            //akhil@
            objCashTO.setScreenName("SB/Current Account Opening");
            //System.out.println("objCashTO 1st one:" + objCashTO);
            cashList.add(objCashTO);
        }
        return cashList;
    }
    /*
     * this method will insert the data in the database against the Accounts
     * screen, it will insert the record for AccountTO, *IntroTO,
     * AccountParamTO, nomineeTOList and poaTOList
     */

    private void insertData(HashMap map, LogDAO objLogDAO, LogTO objLogTO, HashMap transDetailMap) throws Exception {
        try {
            /**
             * Using the PowerOfAttorneyDAO, IntroducerDAO and NomineeDAO to
             * insert the Data in the Inroducer and Nominee Table...
             */
            PowerOfAttorneyDAO objPowerOfAttorneyDAO = new PowerOfAttorneyDAO(SCREEN);
            NomineeDAO objNomineeDAO = new NomineeDAO();
            AuthorizedSignatoryDAO objAuthSignDAO = new AuthorizedSignatoryDAO(SCREEN);

            AccountTO accountTO = (AccountTO) map.get("AccountTO");
            prod_id = accountTO.getProdId();
            // this is used to generate the account number on the server side
            final String accountNumber = getAccountNumber();
            //__ To return to the OB...
            transID = accountNumber;

            accountTO.setActNum(accountNumber);
            accountTO.setCreatedBy(userID);
            accountTO.setCreateDt(currDt);
            accountTO.setStatusBy(userID);
            accountTO.setStatus(CommonConstants.STATUS_CREATED);
            accountTO.setStatusDt(currDt);
            // insert AccountTO
            sqlMap.executeUpdate("insertAccountTO", accountTO);
            //System.out.println("_branchCode: " + _branchCode);
            // Inserting into Customer History Table
            CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO();
            objCustomerHistoryTO.setAcctNo(accountNumber);
            objCustomerHistoryTO.setCustId(accountTO.getCustId());
            objCustomerHistoryTO.setProductType(TransactionFactory.OPERATIVE);
            objCustomerHistoryTO.setProdId(accountTO.getProdId());
            objCustomerHistoryTO.setRelationship(AcctStatusConstants.ACCT_HOLDER);
            objCustomerHistoryTO.setFromDt(currDt);
            CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
            objCustomerHistoryTO = null;

            objLogTO.setData(accountTO.toString());
            objLogTO.setPrimaryKey(accountTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            //__ Global Variable...
            accountNo = accountNumber;

            AccountParamTO paramTO = (AccountParamTO) map.get("AccountParamTO");
            paramTO.setActNum(accountNumber);
            paramTO.setStatus(CommonConstants.STATUS_CREATED);
            paramTO.setStatusBy(userID);
            paramTO.setStatusDt(currDt);

//            for new account opening transactionHashMap 
            HashMap debitMap = new HashMap();
            debitMap.put("ACT_NUM", accountNumber);
            List lst = sqlMap.executeQueryForList("getAccNoProdIdDet", debitMap);
            if (lst != null && lst.size() > 0) {
                debitMap = (HashMap) lst.get(0);
                //System.out.println("$#%$%#%$#$% debitMap $%#%## : " + debitMap);
                transDetailMap.put("ACCT_HEAD", CommonUtil.convertObjToStr(debitMap.get("AC_HD_ID")));
            }

            //--- Inserts the Joint Account Holder  Details data into the database if the Constitution is Joint_Account.
            if (accountTO.getActCatId().equals("JOINT")) {
                insertJointAccntDetails(objLogDAO, objLogTO, accountTO.getProdId());
            }

            // insert AccountParamTO
            sqlMap.executeUpdate("insertAccountParamTO", paramTO);
            objLogTO.setData(paramTO.toString());
            objLogTO.setPrimaryKey(paramTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            //__ To Add the Data Related to Nomine(s)...
            ArrayList nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
            objNomineeDAO.insertData(nomineeTOList, accountNumber, false, userID, SCREEN, objLogTO, objLogDAO);

            //__ To enter the Data in the POA...
            objPowerOfAttorneyDAO.setPoAMap((HashMap) map.get("PowerAttorneyTO"));
            objPowerOfAttorneyDAO.setBorrower_No(accountNumber);
            objPowerOfAttorneyDAO.executePoATabQuery(objLogTO, objLogDAO, sqlMap);

            objAuthSignDAO.setAuthorizeMap((HashMap) map.get("AuthorizedSignatoryTO"));
            objAuthSignDAO.setAuthorizedInstructionMap((HashMap) map.get("AuthorizedSignatoryInstructionTO"));
            objAuthSignDAO.setBorrower_No(accountNumber);
            objAuthSignDAO.executeAuthorizedTabQuery(objLogTO, objLogDAO, sqlMap);
            if (transDetailMap != null && transDetailMap.size() > 0) {
                executeTransactionPart(map, transDetailMap, accountNumber);
            }

            if (objSMSSubscriptionTO != null) {
                updateSMSSubscription(accountTO);
            }
            if (cardDetailsMap != null && cardDetailsMap.size() > 0) { //Added By Suresh   02-Dec-2015
                insertCardData();
            }
            // Added by nithya
            if(suretyDetailsMap !=null && suretyDetailsMap.size() > 0){
                insertSuretyData(accountNumber);
            }            
            // End
            nomineeTOList = null;
            paramTO = null;
            accountTO = null;

            objNomineeDAO = null;
            objPowerOfAttorneyDAO = null;
            objAuthSignDAO = null;
            sqlMap.commitTransaction();
        } catch (Exception e) {
            // if something goes wrong, rollback the transaction
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }
    
    private void insertCardData() throws Exception {        //Added By Suresh   02-Dec-2015 Refered By Abi
        try {
            ArrayList addList = new ArrayList(cardDetailsMap.keySet());
            for (int i = 0; i < addList.size(); i++) {
                CardAccountTO objCardAccountTO = (CardAccountTO) cardDetailsMap.get(addList.get(i));
                Date actionDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objCardAccountTO.getActionDt()));
                actionDt = setProperDtFormat(actionDt);
                objCardAccountTO.setActionDt(actionDt);
                objCardAccountTO.setStatus(CommonConstants.STATUS_CREATED);
                objCardAccountTO.setStatusBy(userID);
                sqlMap.executeUpdate("insertCardDetailsTO", objCardAccountTO);
                objCardAccountTO = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

//    //--- Inserts the data Record in SMS_SUBSCRIPTION Table
//    private void updateSMSSubscription(AccountTO accountTO) throws Exception {
//        HashMap checkMap = new HashMap();
//        checkMap.put("PROD_TYPE", "OA");
//        checkMap.put("PROD_ID", accountTO.getProdId());
//        checkMap.put("ACT_NUM", accountTO.getActNum());
//        List lst = sqlMap.executeQueryForList("getSelectSMSSubscriptionMap",checkMap);
//        objSMSSubscriptionTO.setStatusBy(accountTO.getStatusBy());
//        objSMSSubscriptionTO.setStatusDt(accountTO.getStatusDt());
//        if (lst!=null && lst.size()>0) {
//            objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_MODIFIED);
//            sqlMap.executeUpdate("updateSMSSubscriptionMap", objSMSSubscriptionTO);
//            lst.clear();
//        } else {
//            objSMSSubscriptionTO.setStatus(CommonConstants.STATUS_CREATED);
//            sqlMap.executeUpdate("insertSMSSubscriptionMap", objSMSSubscriptionTO);
//        }
//        lst = null;
//        checkMap.clear();
//        checkMap = null;
//    }
    private void updateSMSSubscription(AccountTO accountTO) throws Exception {
        objSMSSubscriptionTO.setStatusBy(accountTO.getStatusBy());
        objSMSSubscriptionTO.setStatusDt(accountTO.getStatusDt());
        objSMSSubscriptionTO.setCreatedBy(accountTO.getStatusBy());
        objSMSSubscriptionTO.setActNum(accountTO.getActNum());
        objSMSSubscriptionTO.setCustId(accountTO.getCustId());
        if(CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()) != null && CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()).length()>0 && objSMSSubscriptionTO.getSubscriptionDt() != null){
        	List list = (List) sqlMap.executeQueryForList("getRecordExistorNotinSMSSub", objSMSSubscriptionTO);
        	if(list!=null && list.size()>0){
            	sqlMap.executeUpdate("updateSMSSubscriptionMap", objSMSSubscriptionTO);
        	}else{
            	sqlMap.executeUpdate("insertSMSSubscriptionMap", objSMSSubscriptionTO);
        	}
        }
    }
    //--- Inserts the data Record in SMS_SUBSCRIPTION Table

    private void deleteSMSSubscription(AccountTO accountTO) throws Exception {
        HashMap checkMap = new HashMap();
        checkMap.put("PROD_TYPE", "OA");
        checkMap.put("PROD_ID", accountTO.getProdId());
        checkMap.put("ACT_NUM", accountTO.getActNum());
        checkMap.put(CommonConstants.USER_ID, accountTO.getStatusBy());
        sqlMap.executeUpdate("deleteSMSSubscriptionMap", checkMap);
        checkMap.clear();
        checkMap = null;
    }

    //--- Inserts the data Record by record in AccountJointTO Table
    private void insertJointAccntDetails(LogDAO objLogDAO, LogTO objLogTO, String prodID) throws Exception {
        AccountJointTO accountJointTO;
        CustomerHistoryTO objCustomerHistoryTO;
        int jointAccntSize = mapJointAccntTO.size();
        for (int i = 0; i < jointAccntSize; i++) {
            try {
                accountJointTO = (AccountJointTO) mapJointAccntTO.get(String.valueOf(i));
                accountJointTO.setActNum(accountNo);
                accountJointTO.setStatus(CommonConstants.STATUS_CREATED);
                sqlMap.executeUpdate("insertAccountJointTO", accountJointTO);

                // Inserting into Customer History Table
                objCustomerHistoryTO = new CustomerHistoryTO();
                objCustomerHistoryTO.setAcctNo(accountNo);
                objCustomerHistoryTO.setCustId(accountJointTO.getCustId());
                objCustomerHistoryTO.setProductType(TransactionFactory.OPERATIVE);
                objCustomerHistoryTO.setProdId(prodID);
                objCustomerHistoryTO.setRelationship(AcctStatusConstants.JOINT);
                objCustomerHistoryTO.setFromDt(currDt);
                CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
                objCustomerHistoryTO = null;

                objLogTO.setData(accountJointTO.toString());
                objLogTO.setPrimaryKey(accountJointTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                throw new TransRollbackException(e);
            }
        }
    }
    /*
     * this method will update the data on screen for a particular account
     * number
     */

    private void updateData(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            NomineeDAO objNomineeDAO = new NomineeDAO();
            PowerOfAttorneyDAO objPowerOfAttorneyDAO = new PowerOfAttorneyDAO(SCREEN);
            AuthorizedSignatoryDAO objAuthSignDAO = new AuthorizedSignatoryDAO(SCREEN);

            /*
             * get the varoius objects, in this scenario, there are 6 objects
             * which are to be gathered from the "obj".
             */
            AccountTO accountTO = (AccountTO) map.get("AccountTO");
            accountNo = CommonUtil.convertObjToStr(accountTO.getActNum());

            accountTO.setStatus(CommonConstants.STATUS_MODIFIED);
            accountTO.setStatusBy(userID);
            accountTO.setStatusDt(currDt);

            sqlMap.executeUpdate("updateAccountTO", accountTO);
            objLogTO.setData(accountTO.toString());
            objLogTO.setPrimaryKey(accountTO.getKeyData());
            objLogDAO.addToLog(objLogTO);


            //--- Updates the Joint Account Holder  Details data into the database if the Constitution is Joint.
            if (accountTO.getActCatId().equals("JOINT")) {
                updateJointAccountDetails(objLogDAO, objLogTO,accountTO);
                // Added by nithya on 05-09-2018 for KD 231 - cust history table updation is wrong  
                 if (map.containsKey("deletedJointDetails")) {
                     List delList = (ArrayList) map.get("deletedJointDetails");
                     if (delList != null && delList.size() > 0) {
                         for (int i = 0; i < delList.size(); i++) {
                             HashMap checkMap = new HashMap();
                             checkMap.put("CUST_ID", delList.get(i));
                             checkMap.put("TO_DT", currDt);
                             checkMap.put("ACCT_NO", accountNo);
                             sqlMap.executeUpdate("updateCustomerHistoryToDate", checkMap);
                         }
                     }
                 }
                 // End KD 231
            }

            AccountParamTO paramTO = (AccountParamTO) map.get("AccountParamTO");
            paramTO.setStatus(CommonConstants.STATUS_MODIFIED);
            paramTO.setStatusBy(userID);
            paramTO.setStatusDt(currDt);

            sqlMap.executeUpdate("updateAccountParamTO", paramTO);
            objLogTO.setData(paramTO.toString());
            objLogTO.setPrimaryKey(paramTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            ArrayList nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
            ArrayList nomineeDeleteList = (ArrayList) map.get("AccountNomineeDeleteTO");

            //__ Data for Nominee...
            if (nomineeDeleteList != null) {
                if (nomineeDeleteList.size() > 0) {
                    //System.out.println("Deleted Nominee List " + nomineeDeleteList);
                    objNomineeDAO.deleteData(accountNo, SCREEN);
                    objNomineeDAO.insertData(nomineeDeleteList, accountNo, true, userID, SCREEN, objLogTO, objLogDAO);
                }
            }
            // Update the data regarding the NomineeTable...
            if (nomineeTOList != null) {
                if (nomineeTOList.size() > 0) {
                    //System.out.println("Nominee List " + nomineeTOList);
                    objNomineeDAO.deleteData(accountNo, SCREEN);
                    objNomineeDAO.insertData(nomineeTOList, accountNo, false, userID, SCREEN, objLogTO, objLogDAO);
                }
            }
            //__ date for the POA...
            objPowerOfAttorneyDAO.setPoAMap((HashMap) map.get("PowerAttorneyTO"));
            objPowerOfAttorneyDAO.setBorrower_No(accountNo);
            objPowerOfAttorneyDAO.executePoATabQuery(objLogTO, objLogDAO, sqlMap);

            objAuthSignDAO.setAuthorizeMap((HashMap) map.get("AuthorizedSignatoryTO"));
            objAuthSignDAO.setAuthorizedInstructionMap((HashMap) map.get("AuthorizedSignatoryInstructionTO"));
            objAuthSignDAO.setBorrower_No(accountNo);
            objAuthSignDAO.executeAuthorizedTabQuery(objLogTO, objLogDAO, sqlMap);

            if (objSMSSubscriptionTO != null) {
                updateSMSSubscription(accountTO);
            }
            if(cardDetailsMap!=null && cardDetailsMap.size()>0){ //Added By Suresh   02-Dec-2015
                updateCardData(accountTO);
            }
            // Added by nithya
            if(suretyDetailsMap !=null && suretyDetailsMap.size() > 0){
                updateSuretyData(accountTO);
            }
            
            
            nomineeTOList = null;
            nomineeDeleteList = null;
            paramTO = null;
            accountTO = null;
            objNomineeDAO = null;
            objPowerOfAttorneyDAO = null;
            objAuthSignDAO = null;
            sqlMap.commitTransaction();
        } catch (Exception e) {
            // if something goes wrong, rollback the transaction
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }

    }
    
    private void updateCardData(AccountTO accountTO) throws Exception { //Added By Suresh   02-Dec-2015 Refered By Abi
        try {
            HashMap cardMap = new HashMap();
            cardMap.put("ACT_NUM",accountTO.getActNum());
            sqlMap.executeUpdate("deleteCardAccountData", cardMap);
            if (cardDetailsMap != null && cardDetailsMap.size() > 0) {
                ArrayList addList = new ArrayList(cardDetailsMap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    CardAccountTO objCardAccountTO = (CardAccountTO) cardDetailsMap.get(addList.get(i));
                    Date actionDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(objCardAccountTO.getActionDt()));
                    actionDt = setProperDtFormat(actionDt);
                    objCardAccountTO.setActionDt(actionDt);
                    objCardAccountTO.setStatus(CommonConstants.STATUS_CREATED);
                    objCardAccountTO.setStatusBy(userID);
                    if (CommonUtil.convertObjToStr(objCardAccountTO.getAuthorizedStatus()).equals("")) {
                        sqlMap.executeUpdate("insertCardDetailsTO", objCardAccountTO);
                    }
                    objCardAccountTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    private void updateJointAccountDetails(LogDAO objLogDAO, LogTO objLogTO, AccountTO accountTO) throws Exception {
        AccountJointTO accountJointTO;
        CustomerHistoryTO objCustomerHistoryTO;
        HashMap acctNo = new HashMap();
        acctNo.put("ACT_NUM", accountNo);
        List list = (List) sqlMap.executeQueryForList("getCountAccntJointHolders", acctNo);
        HashMap jntMapData = new HashMap();
        jntMapData.put("JointAccountHoldersData", list);
        HashMap JointAccntMap;
        JointAccntMap = (HashMap) ((List) jntMapData.get("JointAccountHoldersData")).get(0);
        int isMore = (int) Integer.parseInt(JointAccntMap.get("COUNT").toString());
        int jntAccntHolderSize = mapJointAccntTO.size();
        for (int i = 0; i < jntAccntHolderSize; i++) {
            try {
                accountJointTO = (AccountJointTO) mapJointAccntTO.get(String.valueOf(i));
                accountJointTO.setActNum(accountNo);
                if (i < isMore) { //--- If the data is exisiting , update the data
                    //accountJointTO.setStatus(CommonConstants.STATUS_MODIFIED);//Commented by nithya on 17-07-2018 for KD169 - A Joint A/C needed to change Main Account holder in to another.Now its not possible
                    sqlMap.executeUpdate("updateAccountJointTO", accountJointTO);
                    // Added by nithya on 05-09-2018 for KD 231 - cust history table updation is wrong
                    if (accountJointTO.getStatus().equalsIgnoreCase("DELETED") && !(accountJointTO.getCustId().equalsIgnoreCase(accountTO.getCustId()))) {
                        HashMap checkMap = new HashMap();
                        checkMap.put("CUST_ID", accountJointTO.getCustId());
                        checkMap.put("TO_DT", currDt);
                        checkMap.put("ACCT_NO", accountNo);
                        sqlMap.executeUpdate("updateCustomerHistoryToDate", checkMap);
                    }if (!(accountJointTO.getStatus().equalsIgnoreCase("DELETED")) && !(accountJointTO.getCustId().equalsIgnoreCase(accountTO.getCustId()))) {
                        HashMap checkMap = new HashMap();
                        checkMap.put("CUST_ID", accountJointTO.getCustId());
                        checkMap.put("RELATIONSHIP", AcctStatusConstants.JOINT);
                        checkMap.put("ACCT_NO", accountNo);
                        sqlMap.executeUpdate("updateCustomerHistoryCustRelationship", checkMap);
                    }
                    // END
                } else { //--- Else insert the data.
                    //accountJointTO.setStatus(CommonConstants.STATUS_CREATED);//Commented by nithya on 17-07-2018 for KD169 - A Joint A/C needed to change Main Account holder in to another.Now its not possible
                    sqlMap.executeUpdate("insertAccountJointTO", accountJointTO);
                    // Added by nithya on 05-09-2018 for KD 231 - cust history table updation is wrong
                    HashMap checkMap = new HashMap();                  
                    checkMap.put("CUST_ID",accountJointTO.getCustId());
                    checkMap.put("TO_DT",currDt);
                    checkMap.put("ACCT_NO",accountNo);
                    sqlMap.executeUpdate("updateCustomerHistoryToDate", checkMap);
                     // Inserting into Customer History Table
                    objCustomerHistoryTO = new CustomerHistoryTO();
                    objCustomerHistoryTO.setAcctNo(accountNo);
                    objCustomerHistoryTO.setCustId(accountJointTO.getCustId());
                    objCustomerHistoryTO.setProductType(TransactionFactory.OPERATIVE);
                    objCustomerHistoryTO.setProdId(accountNo.substring(4, 7));
                    objCustomerHistoryTO.setRelationship(AcctStatusConstants.JOINT);
                    objCustomerHistoryTO.setFromDt(currDt);
                    CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);   
                    // END
                }
                objLogTO.setData(accountJointTO.toString());
                objLogTO.setPrimaryKey(accountJointTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                throw new TransRollbackException(e);
            }
        }
        // Added by nithya on 05-09-2018 for KD 231 - cust history table updation is wrong
        HashMap checkMap = new HashMap();
        String custRelationShip = "";
        checkMap.put("CUST_ID", accountTO.getCustId());
        checkMap.put("TO_DT", currDt);
        checkMap.put("ACCT_NO", accountNo);
        List statuslist = (List) sqlMap.executeQueryForList("getCustHistoryRelationShipStatus", checkMap);
        if(statuslist != null && statuslist.size() > 0){
            HashMap statusMap = (HashMap)statuslist.get(0);
            if(statusMap.containsKey("RELATIONSHIP") && statusMap.get("RELATIONSHIP") != null){
               custRelationShip = CommonUtil.convertObjToStr(statusMap.get("RELATIONSHIP"));
            }            
        }
        if(!(custRelationShip.equalsIgnoreCase("ACCT_HOLDER"))){
            sqlMap.executeUpdate("updateCustomerHistoryToDate", checkMap);
            // Inserting into Customer History Table
            objCustomerHistoryTO = new CustomerHistoryTO();
            objCustomerHistoryTO.setAcctNo(accountNo);
            objCustomerHistoryTO.setCustId(accountTO.getCustId());
            objCustomerHistoryTO.setProductType(TransactionFactory.OPERATIVE);
            objCustomerHistoryTO.setProdId(accountTO.getProdId());
            objCustomerHistoryTO.setRelationship(AcctStatusConstants.ACCT_HOLDER);
            objCustomerHistoryTO.setFromDt(currDt);
            CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
            objCustomerHistoryTO = null;
        } 
        //END
    }

    private void deleteData(AccountTO accountTO, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            accountTO.setStatus(CommonConstants.STATUS_DELETED);
            accountTO.setStatusBy(userID);
            accountTO.setStatusDt(currDt);

            sqlMap.executeUpdate("deleteAccountTO", accountTO);

            // To delete SMSSubscription details
            deleteSMSSubscription(accountTO);

            objLogTO.setData(accountTO.toString());
            objLogTO.setPrimaryKey(accountTO.getKeyData());
            objLogDAO.addToLog(objLogTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            // if something goes wrong, rollback the transaction
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    private void getAuthorizeTransaction(HashMap map, String status) throws Exception {
        HashMap cashAuthMap = new HashMap();
        TransactionDAO transactionDAO = null;
        cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
        cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
        cashAuthMap.put("LOAN_TRANS_OUT", "LOAN_TRANS_OUT");
        String linkBatchId = CommonUtil.convertObjToStr(map.get("ACCOUNTNO"));
//        String linkBatchId=CommonUtil.convertObjToStr(dataMap.get(DEPOSITNO));
        //System.out.println(cashAuthMap + "  linkBatchId####" + linkBatchId);
        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
        cashAuthMap = null;

    }

    private String authorize(HashMap map) throws Exception {
        String b = "";
        try {
            sqlMap.startTransaction();
            String status = CommonUtil.convertObjToStr(map.get(CommonConstants.STATUS));
            b = status;
            int authVal = sqlMap.executeUpdate("authorizeAccountMaster", map);
            //System.out.println("#@$@#$@#$@#$authVal:" + authVal);
            sqlMap.executeUpdate("authorizeSMSSubscriptionMap", map);
            if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {// added by abi 
                 //System.out.println("#@$@#$@#$@#  map:" + map);
                writeATMFile(map);
            }
            sqlMap.executeUpdate("authorizeCardAccountData", map);
            sqlMap.executeUpdate("authorizeSuretyDetailsForSBOD", map); // Added by nithya            
            if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                sqlMap.executeUpdate("removeJointActDetails", map);
            }
            getAuthorizeTransaction(map, status);
            if(map.containsKey("SB_OD_PROD")){
               updateAvailableBalanceForSBODProd(map);
            }else if(map.containsKey("SB_OD_PROD_CLOSE")){    
               updateBalancesWhileSBODClosing(map);
            }   
            sqlMap.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
            throw new TransRollbackException();
        }
        return b;
    }
    
    private void writeATMFile(HashMap datamap) throws Exception {
        ATMAcknowledgementTO acknowledgementTO = new ATMAcknowledgementTO();
        HashMap cardMap = new HashMap();
        DateFormat years2digit = new SimpleDateFormat("yy");
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmm");
        DateFormat dtFormtWithOutMin = new SimpleDateFormat("yyyyMMdd");
        DateFormat dtMiddleWithOutMin = new SimpleDateFormat("yyyyddMM");
        DateFormat minOnly = new SimpleDateFormat("hhmm");
        Calendar calender = Calendar.getInstance();
        dateFormat.format(calender.getTime());

        Date dt = new Date();
        String path = "";
        String transAmt = "";
        String pipeLine = "|";//{:
        String closeBrace = "}";//}:
        String mandatoryField = "^M";
        String colan = ":";
        int daysInYear = 0, amtcount = 16, actualamtcnt = 0;
        String neftUniqueId = "", neftgenId = "", sequnceNum = "";
        StringBuffer buffer = new StringBuffer();
        StringBuffer dateBuffer = new StringBuffer();
        HashMap authMap = new HashMap();
        String cardAcctNum = "", cardOrgNo = "", remarks = "", action = "", acct_status = "";
        // StringBuffer buffer=new StringBuffer();
        //System.out.println("map#####$" + "dtFormtWithMin" + dateFormat.format(calender.getTime()));

//{ACCOUNTNO=0002505003195, USER_ID=B, PROD_TYPE=OA, PROD_ID=505, BRANCH_CODE=0002, AUTHORIZEDT=2015-11-24 00:00:00.0, STATUS=AUTHORIZED}
//2015-12-24 12:12:35,277 INFO  [STDOUT] {LOAN_TRANS_OUT=LOAN_TRANS_OUT, USER_ID=B, BRANCH_CODE=0002}  
        authMap.put("ACCT_NUM", datamap.get("ACCOUNTNO"));
        List lst = sqlMap.executeQueryForList("getSelectCardAcctNum", authMap);
        if (lst != null && lst.size() > 0) {
            try {
                HashMap map = (HashMap) lst.get(0);
                cardAcctNum = CommonUtil.convertObjToStr(map.get("CARD_ACCT_NUM"));
                cardOrgNo = CommonUtil.convertObjToStr(map.get("CARD_ORG_NO"));
                lst = sqlMap.executeQueryForList("getSelectCardActStatus", authMap);
                if (lst != null && lst.size() > 0) {
                    cardMap = (HashMap) lst.get(0);
                    remarks = CommonUtil.convertObjToStr(cardMap.get("REMARKS"));
                    action = CommonUtil.convertObjToStr(cardMap.get("ACTION"));
                }
                if (action.length() == 0) {
                    return;
                }
                neftgenId = getCEDGEuNIQUE_ATM_ID();
                if (action.equals("STOP")) {
                    acct_status = "STOP";
                    //  path = "D:/ELECTRONIC_TRANS/Outward/NEFT/N06/"+"NEFT"+dateFormat.format(calender.getTime())+".txt";
                    //    path =CommonConstants.NEFT_OUTWARD_N06_LOCAL+"NEFT"+dateFormat.format(calender.getTime())+".TXT";
                    path = CommonConstants.ATM_OUR_OUTWARD_STOPAC + CommonConstants.PACS_ID + "_stop_" + dateFormat.format(calender.getTime()) + "_" + neftgenId.substring(3, neftgenId.length()) + "_seq_" + "req" + ".txt";
                } else {
                    acct_status = "NEW";
                    //  path = "D:/ELECTRONIC_TRANS/Outward/RTGS/R41_R42/"+"RTGS"+dateFormat.format(calender.getTime())+".txt";
                    path = CommonConstants.ATM_OUR_OUTWARD_STOPAC + CommonConstants.PACS_ID + "_stoprev_" + dateFormat.format(calender.getTime()) + "_" + neftgenId.substring(3, neftgenId.length()) + "_seq_" + "req" + ".txt";
                }
                java.io.FileWriter write = new java.io.FileWriter(path, false);
                java.io.PrintWriter print_line = new java.io.PrintWriter(write);
                //System.out.println(" neft initialted years2digit" + years2digit.format(calender.getTime()));
                Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
                daysInYear = localCalendar.get(Calendar.DAY_OF_YEAR);
                //   String amt=CommonUtil.convertObjToStr(objCashTransactionTO.getAmount().doubleValue()*100);
                //   System.out.println(" neft started +neftUniqueId" + neftgenId + "neftgenId.substring(2, 8)" + neftgenId.substring(2, 8)
                //         +"amt######$#$#$"+objCashTransactionTO.getAmount().toString().length());
                if (neftgenId != null && neftgenId.length() > 0) {
                    //  actualamtcnt=amtcount-amt.toString().length();
                    // for(int i=0;i<actualamtcnt;i++){
                    //      buffer.append("0");
                    //   }
                    //   buffer.append(amt);
                    if (action.equals("STOP")) {
                        print_line.print(cardAcctNum);
                        print_line.print(pipeLine);
                        print_line.print(remarks);//bank application registed in RTGS server 123
                        //System.out.println(" debit completed  buffer " + buffer);
                    } else {
                        print_line.print(cardAcctNum);
//                        print_line.print(pipeLine);
//                        print_line.print(buffer);//bank application registed in RTGS server 123
                        //System.out.println(" credit completed buffer" + buffer);
                    }
                    acknowledgementTO.setAcctNum(CommonUtil.convertObjToStr(datamap.get("ACCOUNTNO")));
                    acknowledgementTO.setAmount(new Double(0));
                    acknowledgementTO.setCardAcctNum(cardAcctNum);
                    acknowledgementTO.setInitiatedBranch(_branchCode);
                    acknowledgementTO.setSequenceNo(neftgenId.substring(3, neftgenId.length()));
                    acknowledgementTO.setStatus("CREATED");
                    acknowledgementTO.setTransDt((Date)currDt.clone());
                    acknowledgementTO.setTransId("");
                    acknowledgementTO.setTransType("");
                    acknowledgementTO.setTransId("");
                    sqlMap.executeUpdate("insertATMAcknowledgementTO", acknowledgementTO);
                    if (acct_status.length() > 0) {
                        datamap.put("ACT_STATUS_ID", acct_status);
                        datamap.put("ACT_NUM", authMap.get("ACCT_NUM"));
                        if (action.equals("STOP")) {
                            datamap.put("SPONSOR_BANK_STATUS", acct_status);
                        }else {
                            datamap.put("SPONSOR_BANK_STATUS", "");
                        }
                        sqlMap.executeUpdate("updateSposnerBankStatusForAccount", datamap);
                    }
                }
                if (print_line != null) {
                    print_line.close();
                }
                if (write != null) {
                    write.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new TTException(e);
            }
            //displayAlert("Export completed...\nFile path is :" + path);
        } else {
            //ClientUtil.showMessageWindow("List is Empty !!! ");
        }
    }
    
	private String getCEDGEuNIQUE_ATM_ID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "CEDGE_ATMID");
        String rtgs_ID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return rtgs_ID;
    }


    /*
     * performing all operations based on the command parameter
     */
    public HashMap execute(HashMap hash) throws Exception {
        System.out.println(" ############# INSIDE OF EXECUTE METHOD ############# : " + hash);
        HashMap execReturnMap = new HashMap();
        //System.out.println("@@@@@hash" + hash);
        _branchCode = CommonUtil.convertObjToStr(hash.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) hash.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) hash.get(CommonConstants.BRANCH_ID));
        objLogTO.setSelectedBranchId((String) hash.get(CommonConstants.SELECTED_BRANCH_ID));
        objLogTO.setIpAddr((String) hash.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) hash.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) hash.get(CommonConstants.SCREEN));
        userID = CommonUtil.convertObjToStr(hash.get(CommonConstants.USER_ID));
        ibrHierarchy = 1;
        if (hash.containsKey("OPERATIVE_AUTHDATA")) {
            HashMap singleAuthMap = new HashMap();
            singleAuthMap = (HashMap) hash.get("OPERATIVE_AUTHDATA");
            //System.out.println("#@$@#$@$@#$singleAuthMap:" + singleAuthMap);
            String resultStatus = authorize(singleAuthMap);
            execReturnMap.put("AUTHDATA", resultStatus);
        } else {
            AccountTO accountTO = (AccountTO) hash.get("AccountTO");
            mapJointAccntTO = (LinkedHashMap) hash.get("JointAccntTO");

            final String COMMAND = accountTO.getCommand();

            /*
             * start the transaction, if something goes wrong that condition is
             * handled in individual method
             */
            sqlMap.startTransaction();
            HashMap transDetailMap = new HashMap();
            if (hash.containsKey("Transaction Details Data")) {
                transDetailMap = (HashMap) hash.get("Transaction Details Data");
                //System.out.println("@##$#$% transDetailMap #### :" + transDetailMap);
            }
            objSMSSubscriptionTO = null;
            if (hash.containsKey("SMSSubscriptionTO")) {
                objSMSSubscriptionTO = (SMSSubscriptionTO) hash.get("SMSSubscriptionTO");
            }
            if (hash.containsKey("CARD_ACT_DEATILS")) {
                cardDetailsMap = (LinkedHashMap) hash.get("CARD_ACT_DEATILS");
            }
            // Added by nithya
            if (hash.containsKey("MemberTableDetails") || hash.containsKey("deletedMemberTypeData")) {
                suretyDetailsMap = (HashMap) hash.get("MemberTableDetails");
                deletedSuretyTableValues = (HashMap) hash.get("deletedMemberTypeData");
            }
            // End  suretyDetailsMap
            //To perform corresponding actions based on command object
            if (COMMAND.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(hash, objLogDAO, objLogTO, transDetailMap);
                execReturnMap.put(CommonConstants.TRANS_ID, transID);
                if (newTransactionID != null && newTransactionID.length() > 0) {
                    execReturnMap.put("ACCOUNT_OPENING_TRANSACTION", newTransactionID);
                    newTransactionID = "";
                }
                //from mobile app 
                if (hash.containsKey("FROM_MOBILE_APP_CUST_CREATION") && hash.get("FROM_MOBILE_APP_CUST_CREATION") != null && hash.get("FROM_MOBILE_APP_CUST_CREATION").equals("FROM_MOBILE_APP_CUST_CREATION")) {
                    HashMap authMap = new HashMap();
                    authMap.put("ACCOUNTNO", transID);
                    authMap.put("PROD_TYPE", "OA");
                    authMap.put("PROD_ID", accountTO.getProdId());
                    authMap.put(CommonConstants.AUTHORIZEDT, currDt);
                    authMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(hash.get(CommonConstants.USER_ID)));
                    authMap.put(CommonConstants.STATUS, CommonConstants.STATUS_AUTHORIZED);
                    authMap.put(CommonConstants.BRANCH_ID, _branchCode);                    
                    String resultStatus = authorize(authMap);
                    execReturnMap.put("AUTHDATA", resultStatus);
                    System.out.println("execReturnMap final :: " + execReturnMap);
                }
                // End
                ///System.out.println("execReturnMap:" + execReturnMap);

            } else if (COMMAND.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(hash, objLogDAO, objLogTO);
//                if (hash.containsKey("deletedJointDetails")) {// Commented the block by nithya on 17-07-2018 for Savings Bank Account - KD169 - A Joint A/C needed to change Main Account holder in to another.Now its not possible
//                    List delList = (ArrayList) hash.get("deletedJointDetails");
//                    if (delList != null && delList.size() > 0) {
//                        for (int i = 0; i < delList.size(); i++) {
//                            HashMap delListMap = new HashMap();
//                            delListMap.put("CUST_ID", delList.get(i));
//                            delListMap.put("ACCOUNTNO", accountTO.getActNum());
//                            sqlMap.executeUpdate("deleteJointActDetails", delListMap);
//                        }
//                    }
//                }
            } else if (COMMAND.equals(CommonConstants.TOSTATUS_DELETE)) {

                // only the account number is required so pass that only
                deleteData(accountTO, objLogDAO, objLogTO);
            } else {
                sqlMap.rollbackTransaction();
                throw new NoCommandException();
            }
            accountTO = null;
            cardDetailsMap = null;
            suretyDetailsMap = null; // Added by nithya
            deletedSuretyTableValues = null; // Added by nithya
        }
        hash = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return execReturnMap;
    }

    public static void main(String[] arg) {
        try {
            HashMap data = new HashMap();
            data.put("WHERE", "OA060893");
            new AccountDAO().executeQuery(data);
            data = null;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public synchronized HashMap executeQuery(HashMap hash) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(hash.get(CommonConstants.BRANCH_ID));
        HashMap returnMap = null;
        NomineeDAO objNomineeDAO = new NomineeDAO();
        PowerOfAttorneyDAO objPowerOfAttorneyDAO = new PowerOfAttorneyDAO(SCREEN);
        AuthorizedSignatoryDAO objAuthSignDAO = new AuthorizedSignatoryDAO(SCREEN);
        currDt = ServerUtil.getCurrentDate(_branchCode);
//        objPowerOfAttorneyDAO.setPoAMap((HashMap) hash.get("PowerAttorneyTO"));
//        objAuthSignDAO.setAuthorizeMap((HashMap) hash.get("AuthorizedSignatoryTO"));

        try {
            String map = (String) hash.get("MAPNAME");
            String where = (String) hash.get("WHERE");

            returnMap = new HashMap();

            if (map == null) {

                List list = (List) sqlMap.executeQueryForList("getSelectAccountJointTO", where);
                returnMap.put("JointAcctDetails", list);

                list = (List) sqlMap.executeQueryForList("getSelectAccountTO", where);
                returnMap.put("AccountTO", list);
                AccountTO accountTO = (AccountTO) list.get(0);

                list = (List) sqlMap.executeQueryForList("getSelectAccountParamTO", where);
                returnMap.put("AccountParamTO", list);

                HashMap checkMap = new HashMap();
                checkMap.put("PROD_TYPE", "OA");
                checkMap.put("PROD_ID", accountTO.getProdId());
                checkMap.put("ACT_NUM", accountTO.getActNum());
                list = sqlMap.executeQueryForList("getSelectSMSSubscriptionMap", checkMap);
                if (list != null && list.size() > 0) {
                    returnMap.put("SMSSubscriptionTO", list);
                }
                //Added By Suresh   02-Dec-2015 Refered By Abi
                checkMap = new HashMap();
                checkMap.put("ACT_NUM", accountTO.getActNum());
                List cardList = (List) sqlMap.executeQueryForList("getSelectCardActTO", checkMap);
                if (cardList != null && cardList.size() > 0) {
                    LinkedHashMap ParMap = new LinkedHashMap();
                    for (int i = cardList.size(), j = 0; i > 0; i--, j++) {
                        ParMap.put(CommonUtil.convertObjToInt(((CardAccountTO) cardList.get(j)).getSlNo()), cardList.get(j));
                    }
                    returnMap.put("CARD_ACT_DEATILS", ParMap);
                }
                // Added by nithya 
                
                List memberList = (List) sqlMap.executeQueryForList("getSelectSBODSecurityTO", where);
                if (memberList != null && memberList.size() > 0) {
                     LinkedHashMap ParMap = new LinkedHashMap();
                     //System.out.println("@@@memberList" + memberList);
                     for (int i = memberList.size(), j = 0; i > 0; i--, j++) {
                       String st = ((SbODSecurityTO) memberList.get(j)).getMemberNo();
                       ParMap.put(((SbODSecurityTO) memberList.get(j)).getMemberNo(), memberList.get(j));
                 }
                 returnMap.put("memberListTO", ParMap);
               }else if(hash.containsKey("SB_OD_SET")){                 
                 List sbODStatusLst = (List) sqlMap.executeQueryForList("getSBODStatusForAcct", where);   
                 //ACT_STATUS
                 if(sbODStatusLst != null && sbODStatusLst.size() > 0){
                     HashMap sbODActStatusMap = (HashMap)sbODStatusLst.get(0);
                     String sbODAcctStatus = CommonUtil.convertObjToStr(sbODActStatusMap.get("ACT_STATUS"));
                     if(sbODAcctStatus != null && sbODAcctStatus.equalsIgnoreCase("CLOSED")){
                        returnMap.put("SB_OD_ACT_STATUS", "CLOSED"); 
                     }
                 }
               }                
                // End                
                
                accountTO = null;
                list = null;
                
                //                list = (List) sqlMap.executeQueryForList("getSelectAccountNomineeTO", where);
                returnMap.put("AccountNomineeList", (List) objNomineeDAO.getDataList(where, SCREEN));

                objPowerOfAttorneyDAO.getData(returnMap, where, sqlMap);

                objAuthSignDAO.getData(returnMap, where, sqlMap);

            } else if (map.equals("getCustomerDetails")) {
                returnMap.put("CustomerDetails", getMiscData(map, where));
            } else if (map.equals("getAccountDetails")) {
                returnMap.put("AccountDetails", getMiscData(map, where));
            } else if (map.equals("getProductInterests")) {
                returnMap.put("ProductInterests", getMiscData(map, where));
            } else if (map.equals("getBranchDetails")) {
                returnMap.put("BranchDetails", getMiscData(map, where));
            } else if (map.equals("getTransActDetails")) {
                returnMap.put("TransActDetails", getMiscData(map, where));
            } else if (map.equals("getPrevoisAccountDetails")) {
                returnMap.put("getPrevoisAccountDetails", getMiscData(map, where));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            throw new RuntimeException("Error initializing SqlConfig class. Cause: " + e);
        }
        hash = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }
    
    private Date setProperDtFormat(Date dt) {   //Added By Suresh
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    
    // Added by nithya
    private void insertSuretyData(String acctNo) throws Exception {        
        try {
            LinkedHashMap suretyFinalMap = new LinkedHashMap();
            Date sbODDueDt = null;
            suretyFinalMap =(LinkedHashMap)suretyDetailsMap.get("MemberTableDetails");
            double tosSanctioned =  CommonUtil.convertObjToDouble(suretyDetailsMap.get("TOD_SANCTIONED"));
            double salary = CommonUtil.convertObjToDouble(suretyDetailsMap.get("TOTAL_SALARY"));
            String networth = CommonUtil.convertObjToStr(suretyDetailsMap.get("BORROWER_NETWORTH"));
            //String acctStatus = CommonUtil.convertObjToStr(suretyDetailsMap.get("ACCT_STATUS"));
            String borrowerMemberNo = CommonUtil.convertObjToStr(suretyDetailsMap.get("BORROWER_MEMBER_NO"));   
            HashMap dueDateMap = new HashMap();
            dueDateMap.put("MEMBER_NO",borrowerMemberNo);                  
            dueDateMap.put("FRM_DT",DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(currDt.clone())));
            List lst = sqlMap.executeQueryForList("getSBODDueDt",dueDateMap);
            if(lst != null && lst.size() > 0){                
                HashMap dueDtMap = (HashMap)lst.get(0);
                if(dueDtMap.containsKey("DUE_DT")){
                   sbODDueDt = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dueDtMap.get("DUE_DT"))));
                }                
            }
            //Least Due date taken code added by Kannan AR
            ArrayList surietyList = new ArrayList(suretyFinalMap.keySet());
            for (int i = 0; i < surietyList.size(); i++) {
                SbODSecurityTO objSbODSecurityTO = (SbODSecurityTO) suretyFinalMap.get(surietyList.get(i));
                dueDateMap = new HashMap();
                dueDateMap.put("MEMBER_NO", CommonUtil.convertObjToStr(objSbODSecurityTO.getMemberNo()));
                dueDateMap.put("FRM_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(currDt.clone())));                
                List dueDatelst = sqlMap.executeQueryForList("getSBODDueDt", dueDateMap);
                if (dueDatelst != null && dueDatelst.size() > 0) {
                    HashMap dueDtMap = (HashMap) dueDatelst.get(0);
                    if (dueDtMap.containsKey("DUE_DT")) {
                        if(DateUtil.dateDiff(sbODDueDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dueDtMap.get("DUE_DT"))))<0){
                            sbODDueDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dueDtMap.get("DUE_DT")));
                        }                       
                    }
                }
            }
            System.out.println("Final sbODDueDt "+sbODDueDt);
            ArrayList addList = new ArrayList(suretyFinalMap.keySet());
            for (int i = 0; i < addList.size(); i++) {
                  SbODSecurityTO objSbODSecurityTO = (SbODSecurityTO) suretyFinalMap.get(addList.get(i));
                  objSbODSecurityTO.setAcctNum(acctNo);
                  objSbODSecurityTO.setBorrowerSalary(salary);
                  objSbODSecurityTO.setBorrowerNetworth(networth);
                  objSbODSecurityTO.setAcctstatus("NEW");
                  objSbODSecurityTO.setFromDt(currDt);
                  objSbODSecurityTO.setToDt(sbODDueDt);
                  objSbODSecurityTO.setBorrowerMemberNo(borrowerMemberNo);                  
                  objSbODSecurityTO.setStatus(CommonConstants.STATUS_CREATED);
                  objSbODSecurityTO.setStatusBy(userID);
                  objSbODSecurityTO.setTodSanctioned(tosSanctioned);
                  sqlMap.executeUpdate("insertSuretyDetailsForSBOD",objSbODSecurityTO);
                  objSbODSecurityTO = null;
            }
            suretyFinalMap = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }
  
    private void updateSuretyData(AccountTO accountTO) throws Exception {
        SbODSecurityTO objMemberTypeTO = null;
        if (deletedSuretyTableValues != null && deletedSuretyTableValues.size() > 0) {
            //updateOldSureryRecords(accountTO.getActNum());
            LinkedHashMap removedSuretyFinalMap = new LinkedHashMap();
            removedSuretyFinalMap = (LinkedHashMap) deletedSuretyTableValues.get("deletedMemberTypeData");
            ArrayList addList = new ArrayList(removedSuretyFinalMap.keySet());
            for (int i = 0; i < removedSuretyFinalMap.size(); i++) {
                objMemberTypeTO = new SbODSecurityTO();
                if (suretyDetailsMap.containsKey("FROM_DT")) {
                    Date from_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(suretyDetailsMap.get("FROM_DT")));
                    from_dt = setProperDtFormat(from_dt);
                    objMemberTypeTO.setFromDt(from_dt); //doubt
                }
                if (suretyDetailsMap.containsKey("TO_DT")) {
                    Date to_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(suretyDetailsMap.get("TO_DT")));
                    to_dt = setProperDtFormat(to_dt);
                    objMemberTypeTO.setToDt(to_dt); // doubt
                }
                objMemberTypeTO = (SbODSecurityTO) removedSuretyFinalMap.get(addList.get(i));
                objMemberTypeTO.setStatusDt(setProperDtFormat(objMemberTypeTO.getStatusDt()));
                sqlMap.executeUpdate("deleteSuretyDetailsForSBOD", objMemberTypeTO);
                objMemberTypeTO = null;
            }
        }
        if (suretyDetailsMap != null && suretyDetailsMap.size() > 0) {
            Date sbODDueDt = null;
            String isODEnhanced = "N";
            LinkedHashMap suretyFinalMap = new LinkedHashMap();
            suretyFinalMap = (LinkedHashMap) suretyDetailsMap.get("MemberTableDetails");
            if (suretyDetailsMap.containsKey("OD_ENHANCEMENT") && suretyDetailsMap.get("OD_ENHANCEMENT").equals("OD_ENHANCEMENT")) {
                isODEnhanced = "Y";
            }
            double tosSanctioned = CommonUtil.convertObjToDouble(suretyDetailsMap.get("TOD_SANCTIONED"));
            double salary = CommonUtil.convertObjToDouble(suretyDetailsMap.get("TOTAL_SALARY"));
            String networth = CommonUtil.convertObjToStr(suretyDetailsMap.get("BORROWER_NETWORTH"));
            String acctStatus = CommonUtil.convertObjToStr(suretyDetailsMap.get("ACCT_STATUS"));
            String borrowerMemberNo = CommonUtil.convertObjToStr(suretyDetailsMap.get("BORROWER_MEMBER_NO"));
            String isRenewed = CommonUtil.convertObjToStr(suretyDetailsMap.get("IS_RENEWED"));
            HashMap dueDateMap = new HashMap();
            dueDateMap.put("MEMBER_NO", borrowerMemberNo);
            dueDateMap.put("FRM_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(currDt.clone())));
            List lst = sqlMap.executeQueryForList("getSBODDueDt", dueDateMap);
            if (lst != null && lst.size() > 0) {
                HashMap dueDtMap = (HashMap) lst.get(0);
                if (dueDtMap.containsKey("DUE_DT")) {
                    sbODDueDt = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dueDtMap.get("DUE_DT"))));
                }
            }
            //Least Due date taken code added by Kannan AR
            ArrayList surietyList = new ArrayList(suretyFinalMap.keySet());
            for (int i = 0; i < surietyList.size(); i++) {
                SbODSecurityTO objSbODSecurityTO = (SbODSecurityTO) suretyFinalMap.get(surietyList.get(i));
                dueDateMap = new HashMap();
                dueDateMap.put("MEMBER_NO", CommonUtil.convertObjToStr(objSbODSecurityTO.getMemberNo()));
                dueDateMap.put("FRM_DT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(currDt.clone())));
                List dueDatelst = sqlMap.executeQueryForList("getSBODDueDt", dueDateMap);
                if (dueDatelst != null && dueDatelst.size() > 0) {
                    HashMap dueDtMap = (HashMap) dueDatelst.get(0);
                    if (dueDtMap.containsKey("DUE_DT")) {
                        if (DateUtil.dateDiff(sbODDueDt, DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dueDtMap.get("DUE_DT")))) < 0) {
                            sbODDueDt = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dueDtMap.get("DUE_DT"))));
                        }
                    }
                }
            }
            if (acctStatus.equalsIgnoreCase("CLOSEOD")) {// Added by nithya on 16-02-2017 for 5433
                // Update op_ac_security_member
                updateOldSureryRecords(accountTO.getActNum());
                //insert into SB_OD_ACCT_STATUS
                objMemberTypeTO = new SbODSecurityTO();
                objMemberTypeTO.setAcctNum(accountTO.getActNum());
                objMemberTypeTO.setAcctstatus("CLOSED");
                sqlMap.executeUpdate("insertSBODAcctStatus", objMemberTypeTO);
            } else if (acctStatus.equalsIgnoreCase("RENEW")) {
                // update all fields that are in status ( created, modified)
                // Insert all records as status created			
                updateOldSureryRecords(accountTO.getActNum());
                ArrayList addList = new ArrayList(suretyFinalMap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    objMemberTypeTO = new SbODSecurityTO();
                    objMemberTypeTO = (SbODSecurityTO) suretyFinalMap.get(addList.get(i));
                    objMemberTypeTO.setBorrowerSalary(salary);
                    objMemberTypeTO.setBorrowerNetworth(networth);
                    objMemberTypeTO.setAcctstatus(acctStatus);
                    objMemberTypeTO.setStatusDt(currDt);
                    objMemberTypeTO.setStatus(CommonConstants.STATUS_CREATED);
                    objMemberTypeTO.setFromDt(currDt);
                    objMemberTypeTO.setToDt(sbODDueDt);
                    objMemberTypeTO.setAcctNum(accountTO.getActNum());
                    objMemberTypeTO.setBranchCode(_branchCode);
                    objMemberTypeTO.setBorrowerMemberNo(borrowerMemberNo);
                    objMemberTypeTO.setStatusBy(userID);
                    objMemberTypeTO.setTodSanctioned(tosSanctioned);
                    if (isODEnhanced.equalsIgnoreCase("Y")) {
                        objMemberTypeTO.setAuthorizedStatus(null);
                        objMemberTypeTO.setAuthorizedBy(null);
                        objMemberTypeTO.setAuthorizedDt(null);
                    }
                    sqlMap.executeUpdate("insertSuretyDetailsForSBOD", objMemberTypeTO);
                }
                objMemberTypeTO = null;
            } else {
                // Normal updation query
                // Do not update from to dates
                ArrayList addList = new ArrayList(suretyFinalMap.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    objMemberTypeTO = new SbODSecurityTO();
                    objMemberTypeTO = (SbODSecurityTO) suretyFinalMap.get(addList.get(i));
                    objMemberTypeTO.setBorrowerSalary(salary);
                    objMemberTypeTO.setBorrowerNetworth(networth);
                    if (isRenewed.equalsIgnoreCase("Y")) {
                        acctStatus = "RENEW";
                    } else {
                        acctStatus = "NEW";
                    }
                    objMemberTypeTO.setAcctstatus(acctStatus);
                    objMemberTypeTO.setStatusDt(currDt);
                    objMemberTypeTO.setStatusBy(userID);
                    objMemberTypeTO.setBorrowerMemberNo(borrowerMemberNo);
                    objMemberTypeTO.setTodSanctioned(tosSanctioned);
                    //System.out.println("suretyDetailsMap :: " + suretyDetailsMap);
                    if (objMemberTypeTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        objMemberTypeTO.setFromDt(currDt);
                        objMemberTypeTO.setToDt(sbODDueDt);
                        objMemberTypeTO.setAcctNum(accountTO.getActNum());
                        objMemberTypeTO.setBranchCode(_branchCode);
                        if (isODEnhanced.equalsIgnoreCase("Y")) {
                        objMemberTypeTO.setAuthorizedStatus(null);
                        objMemberTypeTO.setAuthorizedBy(null);
                        objMemberTypeTO.setAuthorizedDt(null);
                        }
                        sqlMap.executeUpdate("insertSuretyDetailsForSBOD", objMemberTypeTO);
                    } else {
                        objMemberTypeTO.setAcctNum(accountTO.getActNum());
                        objMemberTypeTO.setBranchCode(_branchCode);
                        objMemberTypeTO.setFromDt(currDt);
                        objMemberTypeTO.setToDt(sbODDueDt);
                        if (isODEnhanced.equalsIgnoreCase("Y")) {
                        objMemberTypeTO.setAuthorizedStatus(null);
                        objMemberTypeTO.setAuthorizedBy(null);
                        objMemberTypeTO.setAuthorizedDt(null);
                        }
                        sqlMap.executeUpdate("updateSuretyDetailsForSBOD", objMemberTypeTO);
                    }
                    objMemberTypeTO = null;
                }
            }
            suretyFinalMap = null;
        }
    }
    
  private void updateOldSureryRecords(String accntNo) throws Exception{
     try {
            SbODSecurityTO objMemberTypeTO = null;
            //sqlMap.startTransaction();
            objMemberTypeTO = new SbODSecurityTO();
            objMemberTypeTO.setAcctNum(accntNo);
            objMemberTypeTO.setAcctstatus("CLOSED"); // Updating to old
            String acctStatus = CommonUtil.convertObjToStr(suretyDetailsMap.get("ACCT_STATUS"));
            //System.out.println("inside updateOldSureryRecords :: acctStatus :: " + acctStatus);
            // Modified by nithya on 08-06-2018 for 0010455: Savings bank a/c - while closing OD,after authorization needed to release sureties.
            if(acctStatus.equalsIgnoreCase("CLOSEOD")){                
              sqlMap.executeUpdate("updateOldSureryRecordsForClosedOD", objMemberTypeTO);  
            }else{
              sqlMap.executeUpdate("updateOldSureryRecords", objMemberTypeTO);  
            }            
            //sqlMap.commitTransaction();
            objMemberTypeTO = null;
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
            throw new TransRollbackException();
        } 
  }

  // Added by nithya on 19.08.2016
  
    private void updateAvailableBalanceForSBODProd(HashMap map) throws Exception {
        try {  
            HashMap odWhrMap = new HashMap();
            odWhrMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("ACCOUNTNO")));
            List oldODLimitDetailsLst = sqlMap.executeQueryForList("getLatestSBODLimitForAccNum", odWhrMap);
            if (oldODLimitDetailsLst != null && oldODLimitDetailsLst.size() > 0) {
                HashMap oldODLimitDetailsMap = (HashMap) oldODLimitDetailsLst.get(0);
                double clearBal = CommonUtil.convertObjToDouble(oldODLimitDetailsMap.get("CLEAR_BALANCE"));
                if(clearBal < 0){
                    map.put("OD_ENHANCE","");
                }else{
                    map.put("NORMAL","");
                }
            }
            sqlMap.executeUpdate("updateAvailableBalanceForSBODProduct", map);           
        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
            throw new TransRollbackException();
        }

    }
 
  // End
    
  // Added by nithya on 16-02-2017
    private void updateBalancesWhileSBODClosing(HashMap map) throws Exception {       
        //System.out.println("nithya testing :: " + map);
        HashMap chkMap = new HashMap();
        chkMap.put("value",map.get("ACCOUNTNO"));        
        List sbODStatusLst = (List) sqlMap.executeQueryForList("getSBODStatusForAcct", chkMap);
        //ACT_STATUS
        if (sbODStatusLst != null && sbODStatusLst.size() > 0) {
            HashMap sbODActStatusMap = (HashMap) sbODStatusLst.get(0);
            String sbODAcctStatus = CommonUtil.convertObjToStr(sbODActStatusMap.get("ACT_STATUS"));
            if (sbODAcctStatus != null && sbODAcctStatus.equalsIgnoreCase("CLOSED")) {
               sqlMap.executeUpdate("updateBalancesWhileSBODClosing", map);   
            }
        }
    }
  // End  
    
    
    private void doSBOpeningProcessFromMobileData(HashMap map, HashMap dataMap, String accountNumber) throws ServiceLocatorException, SQLException {
        HashMap returnMap = new HashMap();
        HashMap acHeads = new HashMap();
        TransactionDAO transactionDAO = null;
        TxTransferTO transferDao = null;
        ArrayList transList = new ArrayList();
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        //System.out.println("mapTRANSACTION_PART " + map + "dataMap :" + dataMap);
        if (dataMap.containsKey("TRANSACTION_PART")) {
            HashMap reserchMap = new HashMap();
            //            reserchMap = (HashMap)map.get("INT_TRANSACTION_REPAYMENT");
            String branchId = CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE"));
            try {
                dataMap.put("OPERATIVE_NO", accountNumber);
                newTransactionID = CommonUtil.convertObjToStr(accountNumber);
                transactionDAO.setLinkBatchID((String) dataMap.get("OPERATIVE_NO"));
                transactionDAO.setInitiatedBranch(branchId);
                HashMap crMap = new HashMap();
                crMap.put("ACT_NUM", dataMap.get("CR_ACT_NUM"));
                List oaAcctHead = sqlMap.executeQueryForList("getAccNoProdIdDet", crMap);
                if (oaAcctHead != null && oaAcctHead.size() > 0) {
                    crMap = (HashMap) oaAcctHead.get(0);
                }
                //TRANS_TYPE=TRANSFER, ACCT_HEAD=2014001014, CR_ACT_NUM=0001703000007, PROD_TYPE=SA, USER_ID=admin, PROD_ID=703, BRANCH_CODE=0001, 
                //TRANSACTION_PART=TRANSACTION_PART, OPERATIVE_NO=0001101001195, OPERATIVE_AMOUNT=1000.0}
                 if (dataMap.get("PROD_TYPE").equals("OA")) {
                        crMap.put("ACT_NUM", dataMap.get("CR_ACT_NUM"));
                        oaAcctHead = sqlMap.executeQueryForList("getAccNoProdIdDet", crMap);
                        if (oaAcctHead != null && oaAcctHead.size() > 0) {
                            crMap = (HashMap) oaAcctHead.get(0);
                        }
                    }
                    if (dataMap.get("PROD_TYPE").equals("SA")) {
                        crMap.put("prodId", dataMap.get("PROD_ID"));
                        oaAcctHead = sqlMap.executeQueryForList("getAccountHeadProdSAHead", crMap);
                        if (oaAcctHead != null && oaAcctHead.size() > 0) {
                            crMap = (HashMap) oaAcctHead.get(0);
                            //transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                        }

                    }                
                txMap = new HashMap();
                ArrayList transferList = new ArrayList();
                TxTransferTO transferTo = new TxTransferTO();
                double interestAmt = CommonUtil.convertObjToDouble(dataMap.get("OPERATIVE_AMOUNT")).doubleValue();
                txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("OPERATIVE_NO"));
                txMap.put(TransferTrans.CR_AC_HD, dataMap.get("ACCT_HEAD"));
                txMap.put(TransferTrans.CR_PROD_ID, dataMap.get("PROD_ID"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                txMap.put(TransferTrans.PARTICULARS, "AccountOpening - " + dataMap.get("OPERATIVE_NO"));
                txMap.put("SCREEN_NAME", "Deposit Auto Creation");
                txMap.put("NARRATION","MOBILE_APP");
                txMap.put("AUTHORIZEREMARKS", "DP");
                
                
                /*
                {TRANS_TYPE=TRANSFER, ACCT_HEAD=2002001007, CR_ACT_NUM=2011001007, PROD_TYPE=GL, 
                USER_ID=admin, PROD_ID=101, BRANCH_CODE=0001, 
                TRANSACTION_PART=TRANSACTION_PART, OPERATIVE_NO=0001104000001, OPERATIVE_AMOUNT=500.0}
                */                
                
                
                txMap.put(TransferTrans.DR_PROD_TYPE, dataMap.get("PROD_TYPE"));
                if(dataMap.get("PROD_TYPE").equals("GL")){
                  txMap.put(TransferTrans.DR_PROD_ID, null);  
                  txMap.put(TransferTrans.DR_ACT_NUM, null);
                  txMap.put(TransferTrans.DR_AC_HD, dataMap.get("CR_ACT_NUM"));
                }else{
                  txMap.put(TransferTrans.DR_PROD_ID, crMap.get("PROD_ID")); 
                  txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("CR_ACT_NUM"));
                  txMap.put(TransferTrans.DR_AC_HD, crMap.get("AC_HD_ID"));
                }                               
                txMap.put(TransferTrans.DR_BRANCH, dataMap.get("BRANCH_CODE"));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                txMap.put("SCREEN_NAME", "Deposit Auto Creation");
                txMap.put("NARRATION","MOBILE_APP");
                //System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);

                transferTo = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                transferTo.setInstrumentNo2("OPERATIVE_TRANS");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                transferTo.setLinkBatchId((String) dataMap.get("OPERATIVE_NO"));
                transferTo.setTransModType(CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")));//KD-3554
                transferList.add(transferTo);
                txMap.put(TransferTrans.PARTICULARS, dataMap.get("OPERATIVE_NO"));
                transferTo = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                transferTo.setInstrumentNo2("OPERATIVE_TRANS");
                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                transferTo.setLinkBatchId((String) dataMap.get("OPERATIVE_NO"));
                transferTo.setTransModType("OA");
                transferList.add(transferTo);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));

                transList = null;
                transactionDAO = null;
                transferDao = null;
                txMap = null;
                acHeads = null;

            } catch (Exception e) {
                sqlMap.rollbackTransaction();
                e.printStackTrace();
            }
            transList = null;
            transactionDAO = null;
            transferDao = null;
            txMap = null;
            acHeads = null;
        }
    }
    
     
}
