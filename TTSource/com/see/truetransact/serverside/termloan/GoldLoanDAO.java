/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TermLoanDAO.java
 *
 * Created on Fri Jan 09 18:03:55 CST 2004
 */
package com.see.truetransact.serverside.termloan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
//import com.ibatis.db.sqlmap.cache.CacheModel;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.businessrule.RuleContext;
import com.see.truetransact.businessrule.RuleEngine;
import com.see.truetransact.commonutil.*;
//import com.see.truetransact.businessrule.termloan.InterestDetailsValidationRule;
//import com.see.truetransact.businessrule.termloan.SecurityDetailsValidationRule;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
//import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
//import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
//import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAO;
//import com.see.truetransact.serverutil.ServerConstants;
//import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
//import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.authorizedsignatory.AuthorizedSignatoryDAO;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.common.powerofattorney.PowerOfAttorneyDAO;
import com.see.truetransact.serverside.termloan.settlement.SettlementDAO;
//import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.termloan.*;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
import java.util.GregorianCalendar;
import com.see.truetransact.transferobject.deposit.lien.DepositLienTO;
import com.see.truetransact.serverside.deposit.lien.DepositLienDAO;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
//import com.see.truetransact.serverside.deposit.lien.DepositLien;
import com.see.truetransact.transferobject.operativeaccount.TodAllowedTO;
import com.see.truetransact.serverside.operativeaccount.TodAllowedDAO;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.serverside.customer.CustomerHistoryDAO;
import com.see.truetransact.transferobject.customer.CustomerHistoryTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.common.nominee.NomineeDAO;
import com.see.truetransact.transferobject.common.mobile.SMSSubscriptionTO;
import com.see.truetransact.transferobject.operativeaccount.AccountClosingTO;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.io.*;
import java.sql.*;
import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;

/**
 * TermLoan DAO.
 *
 * @author shanmugavel
 *
 */
public class GoldLoanDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private TermLoanBorrowerTO objTermLoanBorrowerTO;
//    private TermLoanCompanyTO objTermLoanCompanyTO;
    private TermLoanClassificationTO objTermLoanClassificationTO;
    private GoldLoanSecurityTO objTermLoanSecurityTO;
//    private TermLoanOtherDetailsTO objTermLoanOtherDetailsTO;
    private AuthorizedSignatoryDAO objAuthorizedSignatoryDAO;
    private PowerOfAttorneyDAO objPowerOfAttorneyDAO;
//    private TermLoanExtenFacilityDetailsTO objTermLoanExtenFacilityDetailsTO;
    private HashMap jointAcctMap;
    // To update the networth details in Customer Table
//    private HashMap netWorthDetailsMap;
    private HashMap authorizedMap;
    private HashMap poaMap;
    private HashMap sanctionMap;
    private HashMap sanctionFacilityMap;
    private HashMap facilityMap;
    private HashMap securityMap;
    private HashMap repaymentMap;
    private HashMap installmentMap;
    private LinkedHashMap installmentAllMap;
    private HashMap installmentSingleMap;
    private HashMap installmentMultIntMap;
//    private HashMap guarantorMap;
//    private HashMap guaranInstitMap;
    private HashMap documentMap;
    private HashMap interestMap;
    private HashMap additionalSanMap;
    private HashMap NPA;
    private HashMap DisbursementMap;
    private HashMap depositCustDetMap;
    private HashMap termLoanExtenFacilityMap = new HashMap();
    private String borrower_No;
    private String acct_No;
    private String lienNo = "";
    public String prod_id = "";
    public List chargeLst = null;
    private final String ACCT_NUM = "ACCT_NUM";
    private final String ACT = "ACT";
    private final String AMOUNT = "AMOUNT";
    private final String AUTHORIZEDT = "AUTHORIZEDT";
    private final String BEHAVES_LIKE = "BEHAVES_LIKE";
    private final String CATEGORY_ID = "CATEGORY_ID";
    private final String FROM_DATE = "FROM_DATE";
    private final String FULLY_SECURED = "FULLY_SECURED";
    private final String INT_GET_FROM = "INT_GET_FROM";
    private final String LIMIT = "LIMIT";
    private final String PROD_ID = "PROD_ID";
    private final String PROD = "PROD";
    private final String RULE_MAP_PATH = "com.see.truetransact.clientutil.exceptionhashmap.termloan.TermLoanRuleHashMap";
    private final String SECURITY_DETAILS = "SECURITY_DETAILS";
    private final String TO_DATE = "TO_DATE";
    private final String VALUE = "value";
    final String SCREEN = "TL";
    private final String CASH_TO = "CashTransactionTO";
    private HashMap delRefMap = null;
    private Date curr_dt = null;
    private String loanType = "";
    private boolean newLoan = false;
    private String appraiserId = "";
    private String ProductId = "";
    String userID = "";
    boolean isRenewal = false;
    HashMap paramMapRenewal = new HashMap();
    private SMSSubscriptionTO objSMSSubscriptionTO = null;
    ArrayList renewaltransList = new ArrayList();
    private HashMap totAmt = new HashMap();
    String generateSingleTransId = "";
    String generateSingleCashIdForDebit = "";
    String generateSingleCashIdForCredit = "";
    String generateSingleTranIdForRenOpCharge = "";
    private int ibrHierarchy = 0;
    private String old_prod_id ="";
    private String oldAccNumRnew = "";
    private byte[] photoByteArray;
    private String driverName;
    private Connection conn;
    private Statement stmt;
    private ResultSet rset;
    private String cmd;
    private String dataBaseURL;
    private String userName;
    private String passWord;
    private String SERVER_ADDRESS;
    private String tableName;
    private String tableCondition;
    private File photoFile;
    private FileOutputStream writer = null;
    private HashMap mapPhotoSign;
    /**
     * Creates a new instance of TermLoanDAO
     */
    public GoldLoanDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap acHeads = new HashMap();
        HashMap renewalResultMap = new HashMap();
        TransactionDAO transactionDAO = null;
        TxTransferTO transferDao = null;
        ArrayList transList = new ArrayList();
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        NomineeDAO objNomineeDAO = new NomineeDAO();
        System.out.println("getdata####" + map);
        if (map.containsKey("INT_TRANSACTION_REPAYMENT")) {
            String branchId = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            try {
                acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", map.get("ACT_NUM"));
                transactionDAO.setLinkBatchID((String) map.get("ACT_NUM"));
                transactionDAO.setInitiatedBranch(branchId);

                txMap.put(TransferTrans.DR_ACT_NUM, map.get("ACT_NUM"));
                txMap.put(TransferTrans.DR_AC_HD, acHeads.get("ACCT_HEAD"));
                txMap.put(TransferTrans.DR_PROD_ID, map.get("PROD_ID"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                txMap.put(TransferTrans.DR_BRANCH, map.get("BRANCH_CODE"));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "VOUCHER");
                double interestAmt = CommonUtil.convertObjToDouble(map.get("INTEREST_AMT")).doubleValue();
                transferDao = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                transferDao.setScreenName("Gold Loan Account Opening");
                //                transferDao.setAuthorizeRemarks("DI");
                transList.add(transferDao);
                txMap.put(TransferTrans.CR_AC_HD, acHeads.get("INT_PAYABLE_ACHD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, map.get("BRANCH_CODE"));
                System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);
                //DELETEING RECORD
                map.put("LINK_BATCH_ID", map.get("ACT_NUM"));
                map.put("TRANS_DT", curr_dt.clone());
                map.put("INITIATED_BRANCH", _branchCode);
                List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", map);
                TxTransferTO txtransferTO = new TxTransferTO();
                ArrayList batchList = new ArrayList();
                if (lst != null && lst.size() > 0) {
                    for (int i = 0; i < lst.size(); i++) {
                        txtransferTO = new TxTransferTO();
                        TxTransferTO singletxtransferTO = new TxTransferTO();
                        singletxtransferTO = (TxTransferTO) lst.get(i);
                        txtransferTO.setActNum(singletxtransferTO.getActNum());
                        txtransferTO.setAcHdId(singletxtransferTO.getAcHdId());
                        txtransferTO.setAmount(singletxtransferTO.getAmount());
                        txtransferTO.setBatchId(singletxtransferTO.getBatchId());
                        txtransferTO.setBranchId(singletxtransferTO.getBranchId());
                        txtransferTO.setInitiatedBranch(singletxtransferTO.getInitiatedBranch());
                        txtransferTO.setLinkBatchId(singletxtransferTO.getLinkBatchId());
                        txtransferTO.setProdId(singletxtransferTO.getProdId());
                        txtransferTO.setProdType(singletxtransferTO.getProdType());
                        txtransferTO.setStatus("DELETED");
                        txtransferTO.setTransDt(singletxtransferTO.getTransDt());
                        txtransferTO.setTransId(singletxtransferTO.getTransId());
                        txtransferTO.setTransType(singletxtransferTO.getTransType());
                        txtransferTO.setTransMode(singletxtransferTO.getTransMode());
                        batchList.add(txtransferTO);
                    }
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    TransferTrans transfer = new TransferTrans();
                    transfer.setInitiatedBranch((String) map.get("BRANCH_CODE"));
                    transfer.doDebitCredit(batchList, (String) map.get("BRANCH_CODE"), false, "INSERT");


                }
                //
                transferDao = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                transferDao.setScreenName("Gold Loan Account Opening");
                transList.add(transferDao);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                System.out.println("transList  ###" + transList + "transferDao   :" + transferDao);
                transferTrans.setLoanDebitInt("DI");
                transferTrans.doDebitCredit(transList, branchId, false);
                HashMap hash = new HashMap();
                //                hash.put("ACCOUNTNO",map.get("ACT_NUM"));
                //                Date curr_dt=(Date)map.get("CURR_DATE");
                //                hash.put("LAST_CALC_DT",DateUtil.addDays(curr_dt,-1));
                //                sqlMap.executeUpdate("updateclearBal", hash);

                //                transactionDAO.doTransferLocal(transList, branchId);
                //            transactionDAO.setCommandMode(commandMode);
            } catch (Exception e) {
                //                throw new TTException(" A/c Head not set.");
                e.printStackTrace();
            }
            transList = null;
            transactionDAO = null;
            transferDao = null;
            txMap = null;
            acHeads = null;
            ServiceLocator.flushCache(sqlMap);
            System.gc();
            return returnMap;
        }
        if (map.containsKey("NPA")) {
            //System.out.println("mapNPA####@@@@@" + map);
            List lst = (List) sqlMap.executeQueryForList(CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_NAME)), map);
            //System.out.println("######od" + lst);
            if (lst.size() > 0) {
                returnMap.put("NPAProductList", lst);
            }
            return returnMap;
        } else {
            String where = (String) map.get(CommonConstants.MAP_WHERE);
            if (map.containsKey("KEY_VALUE")) {
                if (map.containsKey("BORROW_NO")) {
                    where = CommonUtil.convertObjToStr(map.get("BORROW_NO"));
                    // To retrieve the values based on Borrower Number
//                    returnMap = executeQueryForBorrNo(returnMap, where);
//                    where = CommonUtil.convertObjToStr(map.get("ACCT_NUM"));  
//                    returnMap.putAll(executeQueryForAcctNo(returnMap, where));
                    if (map.containsKey("RENEWED_LOAN_NO") && CommonUtil.convertObjToStr(map.get("RENEWED_LOAN_NO")).length() > 0) {
                        String renewalAcctNo = CommonUtil.convertObjToStr(map.get("RENEWED_LOAN_NO"));
                        List lst = (List) sqlMap.executeQueryForList("getSelectTermLoanFacilityTO.AUTHORIZE", renewalAcctNo);
                        if (lst != null && lst.size() > 0) {
                            TermLoanFacilityTO obj = (TermLoanFacilityTO) lst.get(0);


                            //OLD ACCOUNT NO
                            // To retrieve the values based on Borrower Number
                            returnMap = executeQueryForBorrNo(returnMap, obj.getBorrowNo());
                            where = CommonUtil.convertObjToStr(map.get("RENEWED_LOAN_NO"));
                            returnMap.putAll(executeQueryForAcctNo(returnMap, where));


                            ///NEW ACCOUNT NO
                            where = CommonUtil.convertObjToStr(map.get("BORROW_NO"));
                            renewalResultMap = (HashMap) executeQueryForBorrNo(renewalResultMap, where);
                            returnMap.put("NEW_BORROWER_DETAILS", renewalResultMap);
                            //System.out.println("renewalResultMap     " + renewalResultMap);
                            where = CommonUtil.convertObjToStr(map.get("ACCT_NUM"));
                            renewalResultMap = (HashMap) executeQueryForAcctNo(renewalResultMap, where);
                            System.out.println("renewalResultMap  2  " + renewalResultMap);
                            returnMap.put("NEW_FACILITY_DETAILS", executeQueryForAcctNo(renewalResultMap, where));
                            System.out.println("returnMap ########     " + returnMap);



                        }
                    } else {
                        where = CommonUtil.convertObjToStr(map.get("BORROW_NO"));
                        // To retrieve the values based on Borrower Number
                        returnMap = executeQueryForBorrNo(returnMap, where);
                        where = CommonUtil.convertObjToStr(map.get("ACCT_NUM"));
                        returnMap.putAll(executeQueryForAcctNo(returnMap, where));
                    }

                } else if (returnMap.get("KEY_VALUE").equals("ACCOUNT_NUMBER")) {
                    // To retrieve the values based on Account Number
                    returnMap = executeQueryForAcctNo(returnMap, where);
                }

            } else if (returnMap.get("KEY_VALUE").equals("AUTHORIZE")) {
                // To retrieve the values based on Account Number(To populate UI
                // at the time of Authorization)
                returnMap = executeQueryForAuthorize(returnMap, where, CommonUtil.convertObjToStr(map.get("BORROWER_NO")));
            }

            HashMap checkMap = new HashMap();

            checkMap.put("PROD_TYPE", "TL");
            checkMap.put("PROD_ID", map.get("PROD_ID"));
            checkMap.put("ACT_NUM", where);
            List list = sqlMap.executeQueryForList("getSelectSMSSubscriptionMap", checkMap);
            if (list != null && list.size() > 0) {
                returnMap.put("SMSSubscriptionTO", list);
            }
            returnMap.put("AccountNomineeList", (List) objNomineeDAO.getDataList(where, SCREEN));
            // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
            String goldStockPhotoRequired = "N";
            HashMap photoMap = new HashMap();
            photoMap.put("PROD_ID", map.get("PROD_ID"));
            List photoLst = sqlMap.executeQueryForList("getSelectGoldStockPhotoRequired", photoMap);
            if (photoLst != null && photoLst.size() > 0) {
                photoMap = (HashMap) photoLst.get(0);
                goldStockPhotoRequired = CommonUtil.convertObjToStr(photoMap.get("GOLD_STOCK_PHOTO_REQUIRED"));
            }
            if (goldStockPhotoRequired.equalsIgnoreCase("Y")) {
                getStockPhoto(where);
                returnMap.put("STOCK_PHOTO_FILE", mapPhotoSign);
                mapPhotoSign = null;
            }            
            map = null;
            where = null;
            ServiceLocator.flushCache(sqlMap);
            objAuthorizedSignatoryDAO = null;
            objPowerOfAttorneyDAO = null;
            System.out.println("returnmap#######" + returnMap);
            return returnMap;
        }
    }

    private HashMap executeQueryForBorrNo(HashMap returnMap, String where) throws Exception {
        System.out.println(where + "borrownoreturnMap#####" + returnMap);
        List list = (List) sqlMap.executeQueryForList("getSelectTermLoanJointAcctTO", where);
        returnMap.put("TermLoanJointAcctTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanBorrowerTO", where);
        returnMap.put("TermLoanBorrowerTO", list);
        list = null;

//        list = (List) sqlMap.executeQueryForList("getSelectTermLoanCompanyTO", where);
//        returnMap.put("TermLoanCompanyTO", list);
//        list = null;

        //        objAuthorizedSignatoryDAO.getData(returnMap, where, sqlMap);

        //        objPowerOfAttorneyDAO.getData(returnMap, where, sqlMap);

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanSanctionTO", where);
        returnMap.put("TermLoanSanctionTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanSanctionFacilityTO", where);
        returnMap.put("TermLoanSanctionFacilityTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanFacilityTO", where);
        returnMap.put("TermLoanFacilityTO", list);
        list = null;

        return returnMap;
    }

    private HashMap executeQueryForAcctNo(HashMap returnMap, String where) throws Exception {
        System.out.println(where + "acctnumberreturnMap#####" + returnMap);

        List list = (List) sqlMap.executeQueryForList("getSelectGoldLoanSecurityTO", where);
        returnMap.put("TermLoanSecurityTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanRepaymentTO", where);
        returnMap.put("TermLoanRepaymentTO", list);
        list = null;

//        list = (List) sqlMap.executeQueryForList("getSelectTermLoanGuarantorTO", where);
//        returnMap.put("TermLoanGuarantorTO", list);
//        list = null;
//        
//        list = (List) sqlMap.executeQueryForList("getSelectTermLoanInstitGuarantorTO", where);
//        returnMap.put("TermLoanInstitGuarantorTO", list);
//        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanDocumentTO", where);
        returnMap.put("TermLoanDocumentTO", list);
        list = null;

        list = getInterestDetails(where, returnMap);
        returnMap.put("TermLoanInterestTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanClassificationTO", where);
        returnMap.put("TermLoanClassificationTO", list);
        list = null;

//        list = (List) sqlMap.executeQueryForList("getSelectTermLoanOtherDetailsTO", where);
//        returnMap.put("TermLoanOtherDetailsTO", list);
//        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanAdditionalSanctionTO", where);
        returnMap.put("TermLoanAdditionalSanctionTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanDisburstTO", where);
        returnMap.put("TermLoanDisburstTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanFacilityExtnTO", where);
        returnMap.put("TermLoanFacilityExtnTO", list);
        list = null;

        //                     System.out.println("obj^^^^^^^^^^^^"+obj);
        //        HashMap dataMap = new HashMap();
        //        dataMap.put("ACCT_NUM",where);
        //        SettlementDAO objSetDao = new SettlementDAO("common");
        //
        //        HashMap tempReturnMap = objSetDao.getDataSet(dataMap, sqlMap);
        //        returnMap.putAll(tempReturnMap);
        //
        //        tempReturnMap = objSetDao.getDataActTrans(dataMap, sqlMap);
        //        returnMap.putAll(tempReturnMap);


        return returnMap;
    }

    private HashMap executeQueryForAuthorize(HashMap returnMap, String where, String borrow_no) throws Exception {

        List list = (List) sqlMap.executeQueryForList("getSelectTermLoanSanctionTO.AUTHORIZE", where);
        returnMap.put("TermLoanSanctionTO.AUTHORIZE", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanSanctionFacilityTO.AUTHORIZE", where);
        returnMap.put("TermLoanSanctionFacilityTO.AUTHORIZE", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanFacilityTO.AUTHORIZE", where);
        returnMap.put("TermLoanFacilityTO.AUTHORIZE", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectGoldLoanSecurityTO", where);
        returnMap.put("TermLoanSecurityTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanRepaymentTO", where);
        returnMap.put("TermLoanRepaymentTO", list);
        list = null;

//        list = (List) sqlMap.executeQueryForList("getSelectTermLoanGuarantorTO", where);
//        returnMap.put("TermLoanGuarantorTO", list);
//        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanDocumentTO", where);
        returnMap.put("TermLoanDocumentTO", list);
        list = null;

        list = getInterestDetails(where, returnMap);
        returnMap.put("TermLoanInterestTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanClassificationTO", where);
        returnMap.put("TermLoanClassificationTO", list);
        list = null;

//        list = (List) sqlMap.executeQueryForList("getSelectTermLoanOtherDetailsTO", where);
//        returnMap.put("TermLoanOtherDetailsTO", list);
//        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanAdditionalSanctionTO", where);
        returnMap.put("TermLoanAdditionalSanctionTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanDisburstTO", where);
        returnMap.put("TermLoanDisburstTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanFacilityExtnTO", where);
        returnMap.put("TermLoanFacilityExtnTO", list);
        list = null;

//        list = (List) sqlMap.executeQueryForList("getSelectTermLoanInstitGuarantorTO", where);
//        returnMap.put("TermLoanInstitGuarantorTO", list);
//        list = null;

        HashMap dataMap = new HashMap();
        dataMap.put("ACCT_NUM", where);
        SettlementDAO objSetDao = new SettlementDAO("common");

        HashMap tempReturnMap = objSetDao.getDataSet(dataMap, sqlMap);
        returnMap.putAll(tempReturnMap);
        tempReturnMap = objSetDao.getDataActTrans(dataMap, sqlMap);
        returnMap.putAll(tempReturnMap);


        // set the Borrower number to where
        where = borrow_no;
        list = (List) sqlMap.executeQueryForList("getSelectTermLoanJointAcctTO", where);
        returnMap.put("TermLoanJointAcctTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanBorrowerTO", where);
        returnMap.put("TermLoanBorrowerTO", list);
        list = null;

//        list = (List) sqlMap.executeQueryForList("getSelectTermLoanCompanyTO", where);
//        returnMap.put("TermLoanCompanyTO", list);
//        list = null;

        //        objAuthorizedSignatoryDAO.getData(returnMap, where, sqlMap);
        //
        //        objPowerOfAttorneyDAO.getData(returnMap, where, sqlMap);

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanSanctionTO", where);
        returnMap.put("TermLoanSanctionTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanSanctionFacilityTO", where);
        returnMap.put("TermLoanSanctionFacilityTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectTermLoanFacilityTO", where);
        returnMap.put("TermLoanFacilityTO", list);
        list = null;

        return returnMap;
    }

    private List getInterestDetails(String where, HashMap returnMap) throws Exception {
        //        List list = (List) sqlMap.executeQueryForList("getInterestDetailsWhereConditions", where);
        //        HashMap whereConditionMap = new HashMap();
        //        HashMap finalMap = new HashMap();
        //        String strIntGetFrom = "";
        //        String interstType="";
        //        whereConditionMap.put(CATEGORY_ID, "");
        //        whereConditionMap.put(AMOUNT, "");
        //        whereConditionMap.put(PROD_ID, "");
        //        whereConditionMap.put(FROM_DATE, null);
        //        whereConditionMap.put(TO_DATE, null);
        //        if (list != null && list.size() > 0){
        //            finalMap = (HashMap) list.get(0);
        //            whereConditionMap.put(CATEGORY_ID, CommonUtil.convertObjToStr(finalMap.get("CATEGORY")));
        //            whereConditionMap.put(AMOUNT, new java.math.BigDecimal(CommonUtil.convertObjToDouble(finalMap.get("LIMIT")).doubleValue()));
        //            whereConditionMap.put(PROD_ID, CommonUtil.convertObjToStr(finalMap.get("PROD_ID")));
        //            whereConditionMap.put(FROM_DATE, finalMap.get("FROM_DATE"));
        //            whereConditionMap.put(TO_DATE, finalMap.get("TO_DATE"));
        //            strIntGetFrom = CommonUtil.convertObjToStr(finalMap.get("INT_GET_FROM"));
        //            interstType=CommonUtil.convertObjToStr(finalMap.get("INTEREST_TYPE"));
        //        }
        //        list = null;
        //        list = new ArrayList();
        //        if (strIntGetFrom.equals(PROD)){
        //            if(interstType.equals("FLOATING_RATE"))
        //                list = (List) sqlMap.executeQueryForList("getSelectProductTermLoanInterestFloatTO", whereConditionMap);
        //            else
        //                list = (List) sqlMap.executeQueryForList("getSelectProductTermLoanInterestTO", whereConditionMap);
        //        }else if (strIntGetFrom.equals(ACT)){
        List list = (List) sqlMap.executeQueryForList("getSelectTermLoanInterestTO", where);
        //        }
        //        whereConditionMap = null;
        //        finalMap = null;
        //        strIntGetFrom = null;
        return list;
    }

    private void executeAllTabQuery() throws Exception {
        System.out.println("$#$# jointAcctMap : " + jointAcctMap);
        executeJointAcctTabQuery();
        //        objAuthorizedSignatoryDAO.executeAuthorizedTabQuery(logTO, logDAO, sqlMap);
        //        objPowerOfAttorneyDAO.executePoATabQuery(logTO, logDAO, sqlMap);
        executeSanctionTabQuery();
        executeSanctionDetailsTabQuery();
       	executeFacilityTabQuery();
        executeSecurityTabQuery();
        executeRepaymentQuery();
        executeInstallmentTabQuery();
        //        executeInstallmentMultIntTabQuery();
        //        executeGuarantorTabQuery();
        //        executeInsititGuarantorTabQuery();
        executeDocumentTabQuery();
        executeInterestTabQuery();
        executeClassificationTabQuery();
        //        executeOtherDetailsTabQuery();
        //        executeAdditionalSanctionTabQuery();
        //        executeDisbursementTabQuery();
        //        executeExtenFacilityDetailsTabQuery();
    }

    private void insertData(HashMap map, HashMap transDetailMap) throws Exception {
        try {
            borrower_No = getBorrower_No();
            objTermLoanBorrowerTO.setBorrowNo(borrower_No);
//            objTermLoanCompanyTO.setBorrowNo(borrower_No);
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_CREATED);
//            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_CREATED);
            NomineeDAO objNomineeDAO = new NomineeDAO();
            sqlMap.startTransaction();

            logTO.setData(objTermLoanBorrowerTO.toString());
            logTO.setPrimaryKey(objTermLoanBorrowerTO.getKeyData());
            logTO.setStatus(objTermLoanBorrowerTO.getCommand());
            executeUpdate("insertTermLoanBorrowerTO", objTermLoanBorrowerTO);
            logDAO.addToLog(logTO);

//            logTO.setData(objTermLoanCompanyTO.toString());
//            logTO.setPrimaryKey(objTermLoanCompanyTO.getKeyData());
//            logTO.setStatus(objTermLoanCompanyTO.getCommand());
//            executeUpdate("insertTermLoanCompanyTO", objTermLoanCompanyTO);
//            logDAO.addToLog(logTO);
           	if (map.containsKey("RENEW_OLD_PROD_ID") && old_prod_id.length() < 0) {
                old_prod_id = CommonUtil.convertObjToStr(map.get("RENEW_OLD_PROD_ID"));
            } else {
                if (map.containsKey("PRODUCT_ID")) {
                    old_prod_id = CommonUtil.convertObjToStr(map.get("PRODUCT_ID"));
                }

            }
            if (map.containsKey("OLD_RENEW_ACT_NO")) {
                oldAccNumRnew = CommonUtil.convertObjToStr(map.get("OLD_RENEW_ACT_NO"));
            }
            String apprId = "";
            apprId = CommonUtil.convertObjToStr(map.get("APPRAISER_ID"));
            objTermLoanSecurityTO.setAppraiserId(apprId);
            executeAllTabQuery();
            System.out.println("map 88888888888888888888888 " + map + " transDetailMap====" + transDetailMap);
            List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
            if (listData != null && listData.size() > 0) {
                HashMap map1 = (HashMap) listData.get(0);
                if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")){
                    HashMap actMap = new HashMap();
                    actMap=(HashMap) map.get("Transaction Details Data");
                    if(isRenewal && actMap==null && actMap.size()<=0){
                        actMap.put("ACCT_NUM",oldAccNumRnew);
                    }
                    insertAccountClosingToForCashier(actMap);
                }
            }
            executeTransactionPart(map, transDetailMap);
           
            System.out.println("#$%#$%#$%#$%acct_No:" + acct_No);

            if (isRenewal) {
                paramMapRenewal = new HashMap();
                paramMapRenewal = (HashMap) map.get("RENEWAL_PARAM");
                paramMapRenewal.put("ACCT_NUM", acct_No);
                if (paramMapRenewal != null) {
                    if (paramMapRenewal.get("RENEW_WITH_OLDAMT").equals("Y")) {
                        //update
                        sqlMap.executeUpdate("updateGldshadow", paramMapRenewal);
                    }
                }
            }
            //__ To Add the Data Related to Nomine(s)...
            ArrayList nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
            objNomineeDAO.insertData(nomineeTOList, acct_No, false, userID, SCREEN, logTO, logDAO);
            // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
            if (map.containsKey("serviceTaxDetailsTO")) {
                ServiceTaxDetailsTO objserviceTaxDetailsTO = (ServiceTaxDetailsTO) map.get("serviceTaxDetailsTO");
                insertServiceTaxDetails(objserviceTaxDetailsTO);
            }
            // End
            if(isRenewal){
                HashMap renewalUpdateMap = new HashMap();
                renewalUpdateMap.put("ACCT_NUM",oldAccNumRnew);
                sqlMap.executeUpdate("updateGoldLoanRenewalStatusAsRenewal", renewalUpdateMap);
            }
            sqlMap.commitTransaction();
            nomineeTOList = null;
            objNomineeDAO = null;
             if (photoByteArray != null) { // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
                storePhotoSign();
                photoByteArray = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
    private void insertAccountClosingToForCashier(HashMap map)throws Exception {
        try {
            System.out.println("map for########"+map);
            System.out.println("INT########"+totAmt);
            AccountClosingTO acTo = new AccountClosingTO();
            //HashMap actMap = new HashMap();
            //actMap=(HashMap) map.get("Transaction Details Data");
            System.out.println("act num########"+map.get("ACCT_NUM"));
            //actMap.put("", map.get("Transaction Details Data"));
            acTo.setStatus("CREATED");
            acTo.setStatusBy(userID);
            acTo.setStatusDt(curr_dt);
            acTo.setActNum(CommonUtil.convertObjToStr(map.get("ACCT_NUM")));
            //acTo.setIntPayable(CommonUtil.convertObjToDouble(intMap.get("interestAmt")));
            //acTo.setPayableBal(CommonUtil.convertObjToDouble(intMap.get("totalAmt")));
            //System.out.println("closing To##############3"+acTo);
            sqlMap.executeUpdate("insertAccountClosingTO", acTo);
        }catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
    private void executeTransactionPart(HashMap map, HashMap transDetailMap) throws Exception {
        try {
            if(transDetailMap!=null && transDetailMap.size()>0){
            oldAccNumRnew = CommonUtil.convertObjToStr(transDetailMap.get("ACCT_NUM"));
            transDetailMap.put("ACCT_NUM", acct_No);
            transDetailMap.put("BRANCH_CODE", _branchCode);
            }
            System.out.println("@##$#$% map #### :" + map);
            System.out.println("@##$#$% transDetailMap #### :" + transDetailMap);
            if (transDetailMap != null && transDetailMap.size() > 0) {
                if (transDetailMap.containsKey("TRANSACTION_PART")) {
                    transDetailMap = getProdBehavesLike(transDetailMap);
                    insertTimeTransaction(map, transDetailMap);

                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void authorize(HashMap map) throws Exception {
        try {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            HashMap dataMap;
            String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
            RuleEngine engine;
            RuleContext context;
            ArrayList list;
            String partillyReject = "";
            if (map.containsKey("PARTIALLY_REJECT")) {
                partillyReject = CommonUtil.convertObjToStr(map.get("PARTIALLY_REJECT"));
            }

            sqlMap.startTransaction();

            for (int i = selectedList.size() - 1, j = 0; i >= 0; --i, ++j) {
                dataMap = (HashMap) selectedList.get(j);

                // Get the Produt's Behaves Like
                dataMap = getProdBehavesLike(dataMap);

                engine = new RuleEngine();
                context = new RuleContext();
                list = new ArrayList();
                ////                if(status !=null && status.equals("AUTHORIZED"))
                ////                    context.addRule(new InterestDetailsValidationRule());
                //                if (dataMap.get(SECURITY_DETAILS).equals(FULLY_SECURED) && status.equals("AUTHORIZED")){
                //                    context.addRule(new SecurityDetailsValidationRule());
                //                }

                /*
                 * LOANS INTEREST DETAILS AUTHORIZATION STATUS UPDATION
                 */
                if (status != null && status.length() > 0) {
                    authorizeLoanInterestDetails(status);
                }

                dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
                dataMap.put(AUTHORIZEDT, curr_dt.clone());//curr_dt);
                dataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                dataMap.put(CommonConstants.BRANCH_ID, _branchCode);
                /**
                 * LTD AUTHORZE LIEN *
                 */
                lienAuthorize(dataMap);
                /**
                 * validating all rule map *
                 */
                list = (ArrayList) engine.validateAll(context, dataMap);

                if (list != null) {
                    HashMap exception = new HashMap();
                    exception.put(CommonConstants.EXCEPTION_LIST, list);
                    exception.put(CommonConstants.CONSTANT_CLASS, RULE_MAP_PATH);
                    System.out.println("Exception List : " + list);

                    throw new TTException(exception);
                }
                /**
                 * Update the Authorization Fields and Update the Available
                 * Balance.
                 */
                if (additionalSanctionLTD()) {
                    dataMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                }
                System.out.println("dataMap  ###" + dataMap);
                if (status.length() > 0) {
                    if (dataMap.containsKey("RENEWAL_NEW_NO") && CommonUtil.convertObjToStr(dataMap.get("RENEWAL_NEW_NO")).length() == 0) {
                        if (rejectLiveAccount(dataMap, partillyReject)) {
                            dataMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                        }
                    }
                    //Added By Suresh
                    if (status.equals(CommonConstants.STATUS_REJECTED)) {
                        dataMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_REJECTED);
                    }
                    if (status.equals(CommonConstants.STATUS_EXCEPTION)) {
                        dataMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_EXCEPTION);
                    }
                }
                //dataMap.put("ACCT_NUM",dataMap.get("RENEWAL_NEW_NO").toString());
                if (dataMap.get("RENEWAL_NEW_NO").toString() == null || dataMap.get("RENEWAL_NEW_NO").equals("")) {
                    sqlMap.executeUpdate("authorizeTermLoan", dataMap);                    
                } else {
                    sqlMap.executeUpdate("authorizeGldLoan", dataMap);
                }
                getAuthorizeNPA(dataMap);
                //                if(status !=null){
                //                    authorizeAdditionalSanction(status);
                //                }
                // AuthorizeStatus is Authorized
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    getAuthorizeTransaction(map, status, dataMap);
                    sqlMap.executeUpdate("updateRestructringInstallment", dataMap);
                    dataMap.put("LINK_BATCH_ID", dataMap.get("ACCT_NUM"));
                    sqlMap.executeUpdate("updateInstrumentNO1Transfer", dataMap);
                    sqlMap.executeUpdate("updateInstrumentNO1Cash", dataMap);
                } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                    getAuthorizeTransaction(map, status, dataMap);
                    System.out.println("dataMap.get(ENEWAL_NEW_NO)).length()  ###" + dataMap);
                    if (dataMap.containsKey("RENEWAL_NEW_NO") && CommonUtil.convertObjToStr(dataMap.get("RENEWAL_NEW_NO")).length() == 0) {
                        sqlMap.executeUpdate("updateReschduleStatus", dataMap);
                        sqlMap.executeUpdate("updateInstallmentStatus", dataMap);
                    }
                    // Exisiting status is Created or Modified
                    if (!(CommonUtil.convertObjToStr(dataMap.get("STATUS")).equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                            || CommonUtil.convertObjToStr(dataMap.get("STATUS")).equalsIgnoreCase(CommonConstants.STATUS_MODIFIED))) {

                        dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_MODIFIED);
                        //                            sqlMap.executeUpdate("rejectInventoryDetails", dataMap);
                    }
                }
                updateMobilebanking(dataMap);
                // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
                if (authMap.containsKey("SER_TAX_AUTH")) {
                    HashMap serMapAuth = (HashMap) authMap.get("SER_TAX_AUTH");
                    sqlMap.executeUpdate("authorizeServiceTaxDetails", serMapAuth);
                }
                // END
                logTO.setStatus(CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS)));
                logTO.setData(dataMap.toString());
                logTO.setPrimaryKey(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                logDAO.addToLog(logTO);

                context = null;
                engine = null;

            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void updateMobilebanking(HashMap dataMap) throws Exception {
        dataMap.put("PROD_TYPE", "TL");
        System.out.println("dataMap#########" + dataMap);
        dataMap.put("ACCOUNTNO", dataMap.get("LOAN_NO"));
        dataMap.put("STATUS", dataMap.get("AUTHORIZESTATUS"));
        sqlMap.executeUpdate("authorizeSMSSubscriptionMap", dataMap);
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

    private void insertTimeTransaction(HashMap map, HashMap dataMap) throws Exception {
        double serchargeAmt = 0.0;// Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
        generateSingleTransId = generateLinkID();
        generateSingleCashIdForDebit = generateLinkID();
        generateSingleCashIdForCredit = generateLinkID();
        generateSingleTranIdForRenOpCharge = generateLinkID();
        HashMap returnMap = new HashMap();
        HashMap acHeads = new HashMap();
         HashMap acHeads_new = new HashMap();

        TransactionDAO transactionDAO = null;
        TxTransferTO transferDao = null;
        ArrayList transList = new ArrayList();
        HashMap txMap = new HashMap();
        double loanAmt = CommonUtil.convertObjToDouble(dataMap.get("LIMIT")).doubleValue();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        System.out.println("mapTRANSACTION_PART " + map + "dataMap :" + dataMap);
        if (dataMap.containsKey("TRANSACTION_PART")) {
            HashMap reserchMap = new HashMap();
            //            reserchMap = (HashMap)map.get("INT_TRANSACTION_REPAYMENT");
            String branchId = CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE"));
            try {
                dataMap.put("ACT_NUM", dataMap.get("ACCT_NUM"));
                dataMap.put("USER_ID", userID);
                String acc_numbr = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                String acc_numbr_new = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                if( isRenewal && oldAccNumRnew!=null && oldAccNumRnew.length()>0){
                    acc_numbr_new = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                    acHeads_new =(HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", /*dataMap.get("ACT_NUM")*/acc_numbr_new);
                }
                 if(map!=null && map.containsKey("ACCOUNT_NUMBER")){
                  acc_numbr = CommonUtil.convertObjToStr(map.get("ACCOUNT_NUMBER"));  
                }
                acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", /*dataMap.get("ACT_NUM")*/acc_numbr);
                //System.out.println("acHeads " + acHeads);
                double appraiserAmt = CommonUtil.convertObjToDouble(dataMap.get("APPRAISER_AMT")).doubleValue();
                double serviceTaxAmt = CommonUtil.convertObjToDouble(dataMap.get("SERVICE_TAX_AMT")).doubleValue();
                if (dataMap.get("TRANS_TYPE") != null && dataMap.get("TRANS_TYPE").equals("CASH")) {
                    HashMap loanAuthTransMap = new HashMap();
                    System.out.println("isRenewal --------------------" + isRenewal);
                    if (isRenewal) {
                        if (map.containsKey("RENEWAL_TRANS") && map.get("RENEWAL_TRANS") != null) {
                            HashMap renewalMap = (HashMap) map.get("RENEWAL_TRANS");
                            loanAuthTransMap.put("OLD_ACCT_NUM", renewalMap.get("OLD_ACCT_NUM"));
                        }
                        paramMapRenewal = new HashMap();
                        paramMapRenewal = (HashMap) (map.get("RENEWAL_PARAM"));
                        if (paramMapRenewal != null && paramMapRenewal.get("RENEW_THROUGH_CASH").equals("Y")) {
//                        if (dataMap.get("TRANS_TYPE") != null && dataMap.get("TRANS_TYPE").equals("CASH")) {
                            loanAmt = debitNewAccountAdjustOldAccountthroughCash(map, dataMap, acHeads, acHeads_new);
                        } else {
//                            loanAmt = debitNewAccountAdjustOldAccount(map, dataMap, acHeads, acHeads,acHeads_new);
                            loanAmt = debitNewAccountAdjustOldAccountCashTransfer(map, dataMap, acHeads, acHeads, acHeads_new);
                        }
                       // System.out.println("loanAmt --------------------" + loanAmt);
                    } else {
                        loanAmt = CommonUtil.convertObjToDouble(dataMap.get("LIMIT")).doubleValue();
                    }
                    System.out.println("loanAmt1111 --------------------" + loanAmt);
                    loanAuthTransMap.put("SELECTED_BRANCH_ID", dataMap.get("BRANCH_CODE"));
                    loanAuthTransMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
                    loanAuthTransMap.put("PROD_ID", dataMap.get("PROD_ID"));
                    loanAuthTransMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                    if (isRenewal && oldAccNumRnew != null && oldAccNumRnew.length() > 0) {
                        loanAuthTransMap.put("ACCT_HEAD", acHeads_new.get("ACCT_HEAD"));
                    } else {
                        loanAuthTransMap.put("ACCT_HEAD", acHeads.get("ACCT_HEAD"));
                    }
                    loanAuthTransMap.put("TOKEN_NO", dataMap.get("TOKEN_NO"));
                    loanAuthTransMap.put("LIMIT", new Double(loanAmt));//dataMap.get("LIMIT"));
                    loanAuthTransMap.put("LOANDEBIT", "LOANDEBIT");
                    loanAuthTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                    loanAuthTransMap.put("TRANS_MOD_TYPE", "TL");
                    // loanAuthTransMap.put("SINGLE_TRANS_ID", generateSingleCashId);//20-03-2014
                    ArrayList loanAuthTransList = loanAuthorizeTimeTransaction(loanAuthTransMap);
                    loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                    loanAuthTransMap.put("DEBIT_LOAN_TYPE", "DP");
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    HashMap cashMap = new HashMap();
                    if (loanAmt > 0) {
                        cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                    }
                    System.out.println("cashMap :111=====" + cashMap);
                    cashMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                    String authorizeStatus = CommonConstants.STATUS_AUTHORIZED;
                    ArrayList arrList = new ArrayList();
                    HashMap authDataMap = new HashMap();
                    HashMap authStatusMap = new HashMap();

                    System.out.println("chargeLst  :111=====" + chargeLst);
                    //uncommented by rish
                    if (!isRenewal && chargeLst != null && chargeLst.size() > 0) {   //Charge Details Transaction
                        System.out.println("@##$#$% chargeLst #### :" + chargeLst);
                        for (int i = 0; i < chargeLst.size(); i++) {
                            HashMap chargeMap = new HashMap();
                            String accHead = "";
                            double chargeAmt = 0;
                            chargeMap = (HashMap) chargeLst.get(i);
                            accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                            chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                            if(chargeAmt>0 || chargeAmt>0.0){
                                System.out.println("$#@@$ accHead" + accHead);
                                System.out.println("$#@@$ chargeAmt" + chargeAmt);
                                loanAuthTransMap.remove("LOANDEBIT");
                                loanAuthTransMap.remove("PROD_ID");
                                loanAuthTransMap.remove("DEBIT_LOAN_TYPE");
                                loanAuthTransMap.put("ACCT_HEAD", accHead);
                                loanAuthTransMap.put("LIMIT", String.valueOf(chargeAmt));
                                loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                                loanAuthTransMap.put("TRANS_MOD_TYPE", "TL");
                                loanAuthTransMap.put("PARTICULARS", CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC")));
                                loanAuthTransMap.put("DEDUCTION_ACCU", chargeMap.get("DEDUCTION_ACCU"));
                                // loanAuthTransMap.put("SINGLE_TRANS_ID", generateSingleTransId);20-03-2014
                                cashTransactionDAO = new CashTransactionDAO();
                                loanAuthTransList = loanAuthorizeTimeTransaction(loanAuthTransMap);
                                loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                                cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                                System.out.println("cashMap :" + cashMap);
                                cashMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                            }
                        }
                         if (map.containsKey("serviceTaxDetails") && map.get("serviceTaxDetails") != null) {
                            HashMap taxdetails = (HashMap) map.get("serviceTaxDetails");
                            double serviceTax = 0.0;
                            double swachhCess = 0.0;
                            double krishikalyanCess = 0.0;
                            double taxAmt = 0.0;
                            double normalServiceTax = 0.0;
                            if (taxdetails.containsKey("TOT_TAX_AMT") && taxdetails.get("TOT_TAX_AMT") != null) {
                                taxAmt = CommonUtil.convertObjToDouble(taxdetails.get("TOT_TAX_AMT"));
                            }
                            if (taxdetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && taxdetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                swachhCess = CommonUtil.convertObjToDouble(taxdetails.get(ServiceTaxCalculation.SWACHH_CESS));
                            }
                            if (taxdetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                krishikalyanCess = CommonUtil.convertObjToDouble(taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                            }
                            if (taxdetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && taxdetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(taxdetails.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                            if(swachhCess > 0){
                                loanAuthTransMap.put("ACCT_HEAD", CommonUtil.convertObjToStr(taxdetails.get(ServiceTaxCalculation.SWACHH_HEAD_ID)));
                                loanAuthTransMap.put("LIMIT", String.valueOf(swachhCess));
                                loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                                loanAuthTransMap.put("TRANS_MOD_TYPE", "GL");
                                loanAuthTransMap.put("PARTICULARS", "CGST");
                                cashTransactionDAO = new CashTransactionDAO();
                                loanAuthTransList = loanAuthorizeTimeTransaction(loanAuthTransMap);
                                loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                                cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                                System.out.println("cashMap :" + cashMap);
                                cashMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                            }  
                            if(krishikalyanCess > 0){
                                loanAuthTransMap.put("ACCT_HEAD", CommonUtil.convertObjToStr(taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_HEAD_ID)));
                                loanAuthTransMap.put("LIMIT", String.valueOf(krishikalyanCess));
                                loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                                loanAuthTransMap.put("TRANS_MOD_TYPE", "GL");
                                loanAuthTransMap.put("PARTICULARS", "SGST");
                                cashTransactionDAO = new CashTransactionDAO();
                                loanAuthTransList = loanAuthorizeTimeTransaction(loanAuthTransMap);
                                loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                                cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                                System.out.println("cashMap :" + cashMap);
                                cashMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                            }
                             if(normalServiceTax > 0){
                                loanAuthTransMap.put("ACCT_HEAD", CommonUtil.convertObjToStr(taxdetails.get("TAX_HEAD_ID")));
                                loanAuthTransMap.put("LIMIT", String.valueOf(normalServiceTax));
                                loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                                loanAuthTransMap.put("TRANS_MOD_TYPE", "GL");
                                loanAuthTransMap.put("PARTICULARS", "Service Tax");
                                cashTransactionDAO = new CashTransactionDAO();
                                loanAuthTransList = loanAuthorizeTimeTransaction(loanAuthTransMap);
                                loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                                cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                                System.out.println("cashMap :" + cashMap);
                                cashMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                            } 
                          }    
                    }

                    arrList = null;
                    returnMap = null;
                    acHeads = null;
                    txMap = null;
                    reserchMap = null;
                    loanAuthTransMap = null;
                    authDataMap = null;
                    authStatusMap = null;
                    chargeLst=null;
                    dataMap=null;
                    map = null;
                } else if (dataMap.get("TRANS_TYPE") != null && dataMap.get("TRANS_TYPE").equals("TRANSFER")) {
                    if(!isRenewal){
                     oldAccNumRnew = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));   
                    }
                  	transactionDAO.setLinkBatchID(/*(String) dataMap.get("ACT_NUM")*/oldAccNumRnew);
                    transactionDAO.setInitiatedBranch(branchId);
                    HashMap crMap = new HashMap();
                    String oldAcctNo = "";
                    crMap.put("ACT_NUM", dataMap.get("CR_ACT_NUM"));
                   if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.OPERATIVE)) {
                        List oaAcctHead = sqlMap.executeQueryForList("getAccNoProdIdDet", crMap);
                        if (oaAcctHead != null && oaAcctHead.size() > 0) {
                            crMap = (HashMap) oaAcctHead.get(0);
                        }
                    } else if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.SUSPENSE)) {
                        List oaAcctHead = sqlMap.executeQueryForList("getSAAccNoProdIdDet", crMap);
                        if (oaAcctHead != null && oaAcctHead.size() > 0) {
                            crMap = (HashMap) oaAcctHead.get(0);
                        }
                    }else if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.OTHERBANKACTS)) {
                        crMap.put("ACT_MASTER_ID",dataMap.get("CR_ACT_NUM"));
                        List oaAcctHead = sqlMap.executeQueryForList("getOtherAccNoHeaddDet", crMap);
                        if (oaAcctHead != null && oaAcctHead.size() > 0) {
                            crMap = (HashMap) oaAcctHead.get(0);
                        }
                    }
                    txMap = new HashMap();

                    HashMap interBranchCodeMap = new HashMap();
                    interBranchCodeMap.put("ACT_NUM", dataMap.get("CR_ACT_NUM"));
                    List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
                    if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
                        interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                        //System.out.println("interBranchCodeMap : " + interBranchCodeMap);
                    }
 					double diffAmt = 0;
                    TxTransferTO transferTo = new TxTransferTO();
                    boolean openingChrg = false; // Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                    System.out.println("isRenewal  :22222222222222=====" + isRenewal);
                    double amount = 0, newAmt = 0;
                    HashMap ammAm = null;
                    if (isRenewal) {

                        if (map.containsKey("RENEWAL_TRANS") && map.get("RENEWAL_TRANS") != null) {
                            HashMap renewalMap = (HashMap) map.get("RENEWAL_TRANS");
                            oldAcctNo = CommonUtil.convertObjToStr(renewalMap.get("OLD_ACCT_NUM"));
//                             transactionDAO.setLinkBatchID(acct_No);
                            ammAm = (HashMap) renewalMap.get("ALL_AMOUNT");
                            double renewalSan = CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_SAN_AMT")).doubleValue();
                            double tot_rec_amt =CommonUtil.convertObjToDouble(renewalMap.get("TOTAL_RECEVABLE"));
                            amount = CommonUtil.convertObjToDouble(renewalMap.get("ACT_CLOSING_CHARGE"))
                                    + CommonUtil.convertObjToDouble(ammAm.get("INTEREST"))
                                    + CommonUtil.convertObjToDouble(ammAm.get("INTEREST"))
                                    + CommonUtil.convertObjToDouble(ammAm.get("PENAL_INT"))
                                    + CommonUtil.convertObjToDouble(ammAm.get("POSTAGE CHARGES"))
                                    + CommonUtil.convertObjToDouble(ammAm.get("POSTAGE CHARGES"));
                            newAmt = renewalSan - amount;
                             diffAmt = tot_rec_amt- renewalSan;
                            
                        }
                        loanAmt = debitNewAccountAdjustOldAccount(map, dataMap, acHeads, crMap, acHeads_new);
                        System.out.println("loanAmt  :3333333333333333=====" + loanAmt);
                        
                        // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
                        if (map.containsKey("serviceTaxDetails") && map.get("serviceTaxDetails") != null) {
                            HashMap taxdetails = (HashMap) map.get("serviceTaxDetails");
                            if (taxdetails != null && taxdetails.containsKey("TOT_TAX_AMT") && taxdetails.get("TOT_TAX_AMT") != null) {
                                serchargeAmt = CommonUtil.convertObjToDouble(taxdetails.get("TOT_TAX_AMT")).doubleValue();
                            }
                        }
                        // END
                       

                    } else {
                        loanAmt = CommonUtil.convertObjToDouble(dataMap.get("LIMIT")).doubleValue();
                    }
                    System.out.println("loanAmt  :22222222222222=====" + loanAmt);
                      txMap = new HashMap();
//                    double interestAmt = CommonUtil.convertObjToDouble(dataMap.get("LIMIT")).doubleValue();
                    txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("ACT_NUM"));
                    txMap.put(TransferTrans.DR_AC_HD, acHeads.get("ACCT_HEAD"));
                    txMap.put(TransferTrans.DR_PROD_ID, dataMap.get("PROD_ID"));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                    txMap.put(TransferTrans.DR_BRANCH, dataMap.get("BRANCH_CODE"));
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("DR_INST_TYPE", "VOUCHER");
                    txMap.put(TransferTrans.PARTICULARS, "disbursement - " + dataMap.get("CR_ACT_NUM"));
                    txMap.put("AUTHORIZEREMARKS", "DP");
                    txMap.put("DEBIT_LOAN_TYPE", "DP");

                    txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("CR_ACT_NUM"));
                    txMap.put(TransferTrans.CR_AC_HD, crMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));
                   // txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    System.out.println("dataMap KKK K:"+dataMap);
                    if (dataMap != null && dataMap.size() > 0 && dataMap.containsKey("CR_PROD_TYPE")) {
                        if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.OPERATIVE)) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        } else if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.SUSPENSE)) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                        } else if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.OTHERBANKACTS)) {
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                        }
                    }
                    if(interBranchCodeMap != null && !interBranchCodeMap.isEmpty()){
                        txMap.put(TransferTrans.CR_BRANCH, interBranchCodeMap.get("BRANCH_CODE"));   
                    }else{
                        txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                    }
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                    System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    if (!isRenewal) {
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, loanAmt);//bb
                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                        transferTo.setLinkBatchId(/*(String) dataMap.get("ACT_NUM")*/oldAccNumRnew);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName("Gold Loan Account Opening");
                        renewaltransList.add(transferTo);
                    }
                    txMap.put(TransferTrans.PARTICULARS, dataMap.get("ACT_NUM"));
                    txMap.put("generateSingleTransId", generateSingleTransId);
                    //txMap.put("TRANS_MOD_TYPE", "OA");
                     if (dataMap != null && dataMap.size() > 0 && dataMap.containsKey("CR_PROD_TYPE")) {
                        if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.OPERATIVE)) {
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                        } else if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.SUSPENSE)) {
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                        }else if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.OTHERBANKACTS)) {
                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.OTHERBANKACTS);
                        }
                    }
                    if (isRenewal) {// Added by nithya on 27-01-2017 for 0005736: LOANS_DISBURSEMENT TABLE ,DATA INSERTION ISSUE
                        txMap.put("DEBIT_GOLDLOAN_TYPE", "DP");
                    }  
                    // Added by nithya on 03-10-2018 for KD 270 - Gold Loan Renewal - Now it is not possible to do the interbranch transaction Credit(Savings Bank of Other Branch)
                    if(!((CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"))).equalsIgnoreCase(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE"))))){                        
                        txMap.put("GOLD_LOAN_INTER_BRANCH_TRANS", "GOLD_LOAN_INTER_BRANCH_TRANS");  
                        txMap.put("INITIATED_BRANCH",_branchCode);
                    }
                    // End
                    if(!isRenewal){
                           //calculate total charge amount and total servicetax
                        double totalTaxAmt = 0.0;
                        if (map.containsKey("serviceTaxDetails") && map.get("serviceTaxDetails") != null) {
                            HashMap taxdetails = (HashMap) map.get("serviceTaxDetails");
                            if (taxdetails != null && taxdetails.containsKey("TOT_TAX_AMT") && taxdetails.get("TOT_TAX_AMT") != null) {
                                totalTaxAmt = CommonUtil.convertObjToDouble(taxdetails.get("TOT_TAX_AMT")).doubleValue();
                            }
                        }                       
                        double totOpeningChrgAmt = 0;
                        if (chargeLst != null && chargeLst.size() > 0) {
                            HashMap chargeMap = new HashMap();
                            for (int i = 0; i < chargeLst.size(); i++) {                                
                                chargeMap = (HashMap)chargeLst.get(i);                                
                                double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                                totOpeningChrgAmt = totOpeningChrgAmt + chargeAmt;
                            }
                        }
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, loanAmt- totalTaxAmt - totOpeningChrgAmt);
                    }else{
                       transferTo = transactionDAO.addTransferCreditLocal(txMap, loanAmt);
                    }
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setLinkBatchId(/*(String) dataMap.get("ACT_NUM")*/oldAccNumRnew);
                    transferTo.setGlTransActNum(CommonUtil.convertObjToStr(dataMap.get("CR_ACT_NUM")));
                    transferTo.setScreenName("Gold Loan Account Renewal");
                    if (!isRenewal) {// Added by nithya on 08-09-2017 
                       transferTo.setScreenName("Gold Loan Account Opening");
                    } 
                    renewaltransList.add(transferTo);
                     System.out.println("renewaltransList 2=="+renewaltransList);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);

                    //  if (loanAmt > 0) {
                    //    transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
                    //  }
                    System.out.println("chargeLst  :22222222222222=====" + chargeLst);
                    double totChrgAmt =0;
                    if (chargeLst != null && chargeLst.size() > 0) {   //Charge Details Transaction
                       // System.out.println("@##$#$% chargeLst #### :" + chargeLst);
                        for (int i = 0; i < chargeLst.size(); i++) {
                            HashMap chargeMap = new HashMap();
                            String accHead = "";
                            double chargeAmt = 0;
                            String chargeType = "";
                            chargeMap = (HashMap) chargeLst.get(i);
                            accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                            chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                            if(chargeAmt>0 || chargeAmt>0.0){
                               if (isRenewal) {
                                  totChrgAmt = totChrgAmt + chargeAmt;
                                }
                            chargeType = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
                            //System.out.println("$#@@$ accHead" + accHead);
                            //System.out.println("$#@@$ chargeAmt" + chargeAmt);
                            if (!isRenewal) {        
                                txMap = new HashMap();
                                // transferList = new ArrayList();
                                transferTo = new TxTransferTO();
                                txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("CR_ACT_NUM"));
                                txMap.put(TransferTrans.DR_AC_HD, crMap.get("AC_HD_ID"));
                                txMap.put(TransferTrans.DR_PROD_ID, crMap.get("PROD_ID"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                if(interBranchCodeMap != null && !interBranchCodeMap.isEmpty()){
                                    txMap.put(TransferTrans.DR_BRANCH, interBranchCodeMap.get("BRANCH_CODE"));
                                }else{
                                    txMap.put(TransferTrans.DR_BRANCH, dataMap.get("BRANCH_CODE"));
                                }
                                if (isRenewal) {// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "LOAN_ACT_CLOSING_CHARGE");
                                } else {
                                    txMap.put(TransferTrans.DR_INSTRUMENT_2, "LOAN_ACT_OPENING_CHARGE");
                                }
                                txMap.put(TransferTrans.PARTICULARS, dataMap.get("ACT_NUM"));
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put("DR_INST_TYPE", "VOUCHER");
                              ///  txMap.put("TRANS_MOD_TYPE", "OA");
                                if (dataMap != null && dataMap.size() > 0 && dataMap.containsKey("CR_PROD_TYPE")) {
                                    if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.OPERATIVE)) {
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE);
                                    } else if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.SUSPENSE)) {
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE);
                                    } else if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.OTHERBANKACTS)) {
                                        txMap.put("TRANS_MOD_TYPE", TransactionFactory.OTHERBANKACTS);
                                    }
                                }
                                //System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);

                                txMap.put(TransferTrans.CR_AC_HD, accHead);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                                txMap.put(TransferTrans.PARTICULARS, dataMap.get("ACT_NUM"));
                                System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);
                                //System.out.println("CC---" + chargeMap.get("DEDUCTION_ACCU") + "isRenewal val---" + isRenewal);
                                if (isRenewal && chargeMap.containsKey("DEDUCTION_ACCU")
                                        && chargeMap.get("DEDUCTION_ACCU").equals("O")) {
                                    txMap.put("generateSingleTransId", generateSingleTranIdForRenOpCharge);
                                } else {
                                    txMap.put("generateSingleTransId", generateSingleTransId);
                                }
                                //For KD-3434 - Gold loan payment charge
                                if (interBranchCodeMap.containsKey("BRANCH_CODE") && !((CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"))).equalsIgnoreCase(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE"))))) {
                                    //System.out.println("interBranchCodeMap :: " + interBranchCodeMap);
                                    txMap.put("GOLD_LOAN_INTER_BRANCH_TRANS", "GOLD_LOAN_INTER_BRANCH_TRANS");
                                    txMap.put("INITIATED_BRANCH", _branchCode);
                                }
                                //transferTo = transactionDAO.addTransferDebitLocal(txMap, chargeAmt);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setLinkBatchId(/*(String) dataMap.get("ACT_NUM")*/oldAccNumRnew);
                                if (isRenewal && chargeMap.containsKey("DEDUCTION_ACCU")
                                        && chargeMap.get("DEDUCTION_ACCU").equals("O")) {
                                    transferTo.setSingleTransId(generateSingleTranIdForRenOpCharge);
                                    transferTo.setScreenName("Gold Loan Account Renewal");
                                } else {
                                    transferTo.setSingleTransId(generateSingleTransId);
                                    transferTo.setScreenName("Gold Loan Account Opening");
                                }
                               // renewaltransList.add(transferTo);
                            }
                            txMap = new HashMap();
                            transferTo = new TxTransferTO();
                            txMap.put(TransferTrans.CR_AC_HD, accHead);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                            txMap.put(TransferTrans.PARTICULARS, dataMap.get("ACT_NUM"));
                            txMap.put("TRANS_MOD_TYPE", "GL");
                            System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);
                            System.out.println("CC---" + chargeMap.get("DEDUCTION_ACCU") + "isRenewal val---" + isRenewal);
                            if (isRenewal && chargeMap.containsKey("DEDUCTION_ACCU")
                                    && chargeMap.get("DEDUCTION_ACCU").equals("O")) {
                                txMap.put("generateSingleTransId", generateSingleTranIdForRenOpCharge);
                            } else {
                                txMap.put("generateSingleTransId", generateSingleTransId);
                            }
                            txMap.put("generateSingleTransId", generateSingleTransId);
                            txMap.put(TransferTrans.PARTICULARS, chargeType + " - : " + dataMap.get("ACT_NUM"));
                            System.out.println("CC-11--" + chargeMap.get("DEDUCTION_ACCU") + "isRenewal val---" + isRenewal);
                            if (isRenewal && chargeMap.containsKey("DEDUCTION_ACCU")
                                    && chargeMap.get("DEDUCTION_ACCU").equals("O")) {
                                txMap.put("generateSingleTransId", generateSingleTranIdForRenOpCharge);
                            } else {
                                txMap.put("generateSingleTransId", generateSingleTransId);
                            }
                            txMap.put("TRANS_MOD_TYPE", "TL");
                            if (isRenewal){ // Added by nithya on 27-01-2017 for 0005736: LOANS_DISBURSEMENT TABLE ,DATA INSERTION ISSUE
                               txMap.put("DEBIT_GOLDLOAN_TYPE", "DP"); 
                            }                            
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmt);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                            transferTo.setLinkBatchId(/*(String) dataMap.get("ACT_NUM")*/oldAccNumRnew);
                            if (isRenewal && chargeMap.containsKey("DEDUCTION_ACCU")
                                    && chargeMap.get("DEDUCTION_ACCU").equals("O")) {
                                //transferTo.setSingleTransId(generateSingleTranIdForRenOpCharge);
                                transferTo.setScreenName("Gold Loan Account Renewal");
                                transferTo.setSingleTransId(generateSingleTransId);//KD-3774
                                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                                openingChrg = true;
                            } else {
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Gold Loan Account Opening");
                                transferTo.setLinkBatchId(oldAccNumRnew); // Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                            }
                            renewaltransList.add(transferTo);
                            }
                            // transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            //  transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
                            //				transferTo.setSingleTransId(generateSingleTransId);
                            //   renewaltransList.add(transferTo);
                            // transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            //  transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")))
                        }
                    }
                    // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
                    //if(isRenewal){
                        if (map.containsKey("serviceTaxDetails") && map.get("serviceTaxDetails") != null) {
                            HashMap taxdetails = (HashMap) map.get("serviceTaxDetails");
                            double serviceTax = 0.0;
                            double swachhCess = 0.0;
                            double krishikalyanCess = 0.0;
                            double taxAmt = 0.0;
                            double normalServiceTax = 0.0;
                            if (taxdetails.containsKey("TOT_TAX_AMT") && taxdetails.get("TOT_TAX_AMT") != null) {
                                taxAmt = CommonUtil.convertObjToDouble(taxdetails.get("TOT_TAX_AMT"));
                            }
                            if (taxdetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && taxdetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                swachhCess = CommonUtil.convertObjToDouble(taxdetails.get(ServiceTaxCalculation.SWACHH_CESS));
                            }
                            if (taxdetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                krishikalyanCess = CommonUtil.convertObjToDouble(taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                            }
                            if (taxdetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && taxdetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(taxdetails.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                            taxAmt -= (swachhCess + krishikalyanCess);
                            String seraccHead = "";
                            seraccHead = CommonUtil.convertObjToStr(taxdetails.get("TAX_HEAD_ID"));
                            double chargeAmt = CommonUtil.convertObjToDouble(taxdetails.get("TOT_TAX_AMT")).doubleValue();
                            if (normalServiceTax > 0) {
                                txMap = new HashMap();
                                transferTo = new TxTransferTO();
                                txMap.put(TransferTrans.CR_AC_HD, seraccHead);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                                txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                                txMap.put(TransferTrans.PARTICULARS, "Service Tax" + " - : " + dataMap.get("ACT_NUM"));
                                txMap.put(TransferTrans.DR_INSTRUMENT_2,CommonConstants.SERVICE_TAX_CHRG);// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                                if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {// Added by nithya on 26-06-2018 for servicetax transmode issue                                
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                                }
                                txMap.put("AUTHORIZEREMARKS", "MISCELLANEOUS CHARGES"); // Added by nithya on 27-07-2021 for KD-2951
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                //transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                if(openingChrg){// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                                  transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                }else{
                                   transferTo.setLinkBatchId(oldAccNumRnew); 
                                }
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Gold Loan Account Renewal");
                                renewaltransList.add(transferTo);
                            }
                            if (swachhCess > 0) {
                                txMap = new HashMap();
                                transferTo = new TxTransferTO();
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(taxdetails.get(ServiceTaxCalculation.SWACHH_HEAD_ID)));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                                txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                                txMap.put(TransferTrans.PARTICULARS, "CGST" + " - : " + dataMap.get("ACT_NUM"));
                                txMap.put(TransferTrans.DR_INSTRUMENT_2,CommonConstants.SERVICE_TAX_CHRG); // Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                                if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {// Added by nithya on 26-06-2018 for servicetax transmode issue                                
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                                }
                                txMap.put("AUTHORIZEREMARKS", "MISCELLANEOUS CHARGES"); // Added by nithya on 27-07-2021 for KD-2951
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                //transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                if(openingChrg){ // Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                                  transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                }else{
                                   transferTo.setLinkBatchId(oldAccNumRnew); 
                                }
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Gold Loan Account Renewal");
                                renewaltransList.add(transferTo);
                            }
                            if (krishikalyanCess > 0) {
                                txMap = new HashMap();
                                transferTo = new TxTransferTO();
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_HEAD_ID)));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                                txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                                txMap.put(TransferTrans.PARTICULARS, "SGST" + " - : " + dataMap.get("ACT_NUM"));
                                txMap.put(TransferTrans.DR_INSTRUMENT_2,CommonConstants.SERVICE_TAX_CHRG);// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                                if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {// Added by nithya on 26-06-2018 for servicetax transmode issue                                
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                                }
                                txMap.put("AUTHORIZEREMARKS", "MISCELLANEOUS CHARGES"); // Added by nithya on 27-07-2021 for KD-2951
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                //transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                if(openingChrg){// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                                  transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                }else{
                                   transferTo.setLinkBatchId(oldAccNumRnew); 
                                }
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Gold Loan Account Renewal");
                                renewaltransList.add(transferTo);
                            }
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        }
                   // }
                    // end
                     System.out.println("renewaltransList 3=="+renewaltransList);                     
                    if (isRenewal) {
                        diffAmt = diffAmt + totChrgAmt + serchargeAmt;
                    }    
                    System.out.println("diffAmt :: " + diffAmt);
                    if (isRenewal && diffAmt > 0) {
                        txMap = new HashMap();
                        txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("CR_ACT_NUM"));
                        txMap.put(TransferTrans.DR_BRANCH, dataMap.get("BRANCH_CODE"));
                        if (dataMap != null && dataMap.size() > 0 && dataMap.containsKey("CR_PROD_TYPE")) {
                            if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.OPERATIVE)) {
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.OPERATIVE); // Added by nithya on 02-12-2016 for 5481
                            } else if (CommonUtil.convertObjToStr(dataMap.get("CR_PROD_TYPE")).equals(TransactionFactory.SUSPENSE)) {
                                txMap.put(TransferTrans.DR_AC_HD, TransactionFactory.SUSPENSE);
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.SUSPENSE); // Added by nithya on 02-12-2016 for 5481
                            } else if (CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equals(TransactionFactory.OTHERBANKACTS)) {
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                                txMap.put("TRANS_MOD_TYPE", TransactionFactory.OTHERBANKACTS);
                            }
                        }
                        txMap.put(TransferTrans.DR_AC_HD, crMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_PROD_ID, crMap.get("PROD_ID"));
                        txMap.put(TransferTrans.PARTICULARS, "disbursement - " + dataMap.get("CR_ACT_NUM"));
                        txMap.put("AUTHORIZEREMARKS", "DP");
                        txMap.put("DEBIT_LOAN_TYPE", "DP");
                        txMap.put("generateSingleTransId", generateSingleTransId);
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, diffAmt);//bb
                        System.out.println("transferTo--------- ::" + transferTo);
                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                        transferTo.setLinkBatchId(/*(String) dataMap.get("ACT_NUM")*/oldAccNumRnew);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferTo.setScreenName("Gold Loan Account Renewal");
                        renewaltransList.add(transferTo);
                    }
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    if (isRenewal) {
                        transactionDAO.setInitiatedBranch(_branchCode);
                        transactionDAO.setLoanAmtMap(totAmt);//(HashMap) renewalMap.get("ALL_AMOUNT"));//totAmt);//
                        transactionDAO.setLinkBatchID(/*acct_No*/oldAccNumRnew);
                        System.out.println("acct_No checking :: " + acct_No);
                    }  
                    System.out.println("renewaltransList :: " + renewaltransList);
                    if(isRenewal){
                       renewaltransList.add("GOLD_LOAN_RENEWAL"); 
                    }
                    transactionDAO.doTransferLocal(renewaltransList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
                    renewaltransList = null;
                    totAmt = null;

                    transList = null;
                    transactionDAO = null;
                    transferDao = null;
                    txMap = null;
                    acHeads = null;
                    dataMap = null;//19-03-2014
                    map = null;
                    if (chargeLst != null) {
                        chargeLst.clear();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            transList = null;
            transactionDAO = null;
            transferDao = null;
            txMap = null;
            acHeads = null;
        }
    }

    private double debitNewAccountAdjustOldAccount(HashMap map, HashMap dataMap, HashMap acHeads, HashMap crMap,HashMap acHeads_new) throws Exception {
        TransactionDAO transactionDAO = null;
        TxTransferTO transferDao = null;
        double serchargeAmt = 0.0;
        System.out.println("dataMap@@@@@@@" + dataMap);
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        double debitAmt = 0;
        HashMap renewalMap = new HashMap();

        // HashMap totAmt = new HashMap();
        if (map.containsKey("RENEWAL_TRANS") && map.get("RENEWAL_TRANS") != null) {
            renewalMap = (HashMap) map.get("RENEWAL_TRANS");
            totAmt = (HashMap) renewalMap.get("ALL_AMOUNT");
           
            // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
            if (map.containsKey("serviceTaxDetails") && map.get("serviceTaxDetails") != null) {
                HashMap taxdetails = (HashMap) map.get("serviceTaxDetails");
                if(taxdetails != null && taxdetails.containsKey("TOT_TAX_AMT") && taxdetails.get("TOT_TAX_AMT") != null){
                   serchargeAmt = CommonUtil.convertObjToDouble(taxdetails.get("TOT_TAX_AMT")).doubleValue(); 
                }                
            }
            // END

            //   ArrayList transferList = new ArrayList();
            TxTransferTO transferTo = new TxTransferTO();
            double chrge = CommonUtil.convertObjToDouble(renewalMap.get("ACT_CLOSING_CHARGE")).doubleValue();
            //  if (CommonUtil.convertObjToDouble(chrge).doubleValue() > 0) {/20-03-2014
            //      totAmt.put("ACT_CLOSING_CHARGE", chrge);
            // }
            //   if (chargeLst != null && chargeLst.size() > 0) {   // Account Closing Charges
            //       totAmt.put("ACT_CLOSING_CHARGES", chargeLst);
            //   }
            double totalRecivable = CommonUtil.convertObjToDouble(renewalMap.get("TOTAL_RECEVABLE")).doubleValue();
            double renewalSanction = CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_SAN_AMT")).doubleValue();
            System.out.println("chrge ==" + chrge + "totalRecivable==" + totalRecivable + "renewalSanction=" + renewalSanction);
            //  renewalSanction=renewalSanction+chrge;
            //  totalRecivable=totalRecivable+chrge;
            System.out.println("chrge111 ==" + chrge + "totalRecivable111==" + totalRecivable + "renewalSanction111=" + renewalSanction);
            String oldAcctNo = CommonUtil.convertObjToStr(renewalMap.get("OLD_ACCT_NUM"));
            String oldPrd_id = CommonUtil.convertObjToStr(renewalMap.get("OLD_PROD_ID"));
            if (totalRecivable < renewalSanction) {
                debitAmt = renewalSanction - (totalRecivable+chrge+serchargeAmt);
            }
            // totalRecivable=totalRecivable+chrge;
            txMap.put(TransferTrans.DR_ACT_NUM, acct_No);
            txMap.put(TransferTrans.DR_AC_HD, acHeads_new.get("ACCT_HEAD"));
            txMap.put(TransferTrans.DR_PROD_ID, dataMap.get("PROD_ID"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
            txMap.put(TransferTrans.DR_BRANCH, dataMap.get("BRANCH_CODE"));
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put("DR_INST_TYPE", "VOUCHER");
            txMap.put(TransferTrans.PARTICULARS, "disbursement - " + oldAcctNo);
            txMap.put("AUTHORIZEREMARKS", "DP");
            txMap.put("DEBIT_LOAN_TYPE", "DP");

            txMap.put(TransferTrans.CR_ACT_NUM, oldAcctNo);
            txMap.put(TransferTrans.CR_AC_HD, acHeads.get("ACCT_HEAD"));
            txMap.put(TransferTrans.CR_PROD_ID, /*dataMap.get("PROD_ID")*/oldPrd_id);
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
            txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
            txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            txMap.put("generateSingleTransId", generateSingleTransId);
            System.out.println("txMap  ###" + txMap);
            transferTo.setInitiatedBranch(_branchCode);
            transferTo.setAmount(new Double(totalRecivable));
            transferTo.setInpAmount(new Double(totalRecivable));
            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
            transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));

            transferTo.setLinkBatchId(/*acct_No*/oldAccNumRnew);//To be checked for mamala
            transferTo.setSingleTransId(generateSingleTransId);
            txMap.put("TRANS_MOD_TYPE", "TL");
            transferTo = transactionDAO.addTransferDebitLocal(txMap, renewalSanction);// totalRecivable);// );babuu
            transferTo.setScreenName("Gold Loan Account Renewal");
            System.out.println("nithya checking acct_No :: " + acct_No + "isRenewal :: " + isRenewal);
            transferTo.setGlTransActNum(acct_No);// Added by nithya on 15-10-2018 for (KD-287) Gold Loan renewal - New Loan A/C Number's GL Trans Act Num is old Number. Needed to be the GL Trans act num should be New A/C No
            if(isRenewal){// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
              transferTo.setLinkBatchId(/*acct_No*/oldAccNumRnew);//change to be verified...
            }else{
              transferTo.setLinkBatchId(/*acct_No*/oldAccNumRnew);
            }
            renewaltransList.add(transferTo);
            System.out.println("renewaltransList 0== ::" + renewaltransList);
            txMap.put(TransferTrans.PARTICULARS, oldAcctNo);
            transferTo.setInitiatedBranch(_branchCode);
            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
            transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            txMap.put(TransferTrans.PARTICULARS, "disbursement - " + acct_No);
            txMap.put("generateSingleTransId", generateSingleTransId);
            //transferTo.setLinkBatchId(/*acct_No*/oldAccNumRnew);// Commented by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
            transferTo.setSingleTransId(generateSingleTransId);
            txMap.put("TRANS_MOD_TYPE", "TL");
            if (isRenewal) { // Added by nithya on 27-01-2017 for 0005736: LOANS_DISBURSEMENT TABLE ,DATA INSERTION ISSUE
                txMap.put("DEBIT_GOLDLOAN_TYPE", "DP");
            }
            transferTo = transactionDAO.addTransferCreditLocal(txMap, totalRecivable);
            transferTo.setScreenName("Gold Loan Account Renewal");
            transferTo.setLinkBatchId(/*acct_No*/oldAccNumRnew);// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
            renewaltransList.add(transferTo);
            System.out.println("renewaltransList 1=="+renewaltransList);
            /*  transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setLoanAmtMap(totAmt);//(HashMap) renewalMap.get("ALL_AMOUNT"));//totAmt);//
            System.out.println("acct_No#####" + acct_No);
            transactionDAO.setLinkBatchID(acct_No);*/
            /**
             * **********************************
             */
            /*
             * double renewalSan =
             * CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_SAN_AMT")).doubleValue();
             * double
             * amount=CommonUtil.convertObjToDouble(renewalMap.get("ACT_CLOSING_CHARGE"))+
             * CommonUtil.convertObjToDouble(totAmt.get("INTEREST")) +
             * CommonUtil.convertObjToDouble(totAmt.get("PENAL_INT")) +
             * CommonUtil.convertObjToDouble(totAmt.get("POSTAGE CHARGES")) +
             * CommonUtil.convertObjToDouble(totAmt.get("LOAN_BALANCE_PRINCIPAL"))
             * ; System.out.println("renewalSan11 ====== "+renewalSan);
             * System.out.println("amount11 ====== "+amount); double
             * newAmt=renewalSan-amount; if(newAmt>0) { // debitAmt=newAmt; // }
             * System.out.println("debitAmt111 ====== "+newAmt);
             * txMap.put(TransferTrans.DR_ACT_NUM, dataMap.get("ACT_NUM"));
             * txMap.put(TransferTrans.DR_AC_HD, acHeads.get("ACCT_HEAD"));
             * txMap.put(TransferTrans.DR_PROD_ID, dataMap.get("PROD_ID"));
             * txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
             * txMap.put(TransferTrans.DR_BRANCH, dataMap.get("BRANCH_CODE"));
             * txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
             * txMap.put("DR_INST_TYPE", "VOUCHER");
             * txMap.put(TransferTrans.PARTICULARS, "disbursement - " +
             * dataMap.get("CR_ACT_NUM")); txMap.put("AUTHORIZEREMARKS", "DP");
             * txMap.put("DEBIT_LOAN_TYPE", "DP");
             *
             * txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("CR_ACT_NUM"));
             * txMap.put(TransferTrans.CR_AC_HD, crMap.get("AC_HD_ID"));
             * txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));
             * txMap.put(TransferTrans.CR_PROD_TYPE,
             * TransactionFactory.OPERATIVE); txMap.put(TransferTrans.CR_BRANCH,
             * dataMap.get("BRANCH_CODE")); System.out.println("txMap ###" +
             * txMap + "transferDao :" + transferDao);
             *
             * // transferTo = transactionDAO.addTransferDebitLocal(txMap,
             * loanAmt);//bb //
             * transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT"); //
             * transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
             * //
             * transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
             * // transferTo.setLinkBatchId((String) dataMap.get("ACT_NUM")); //
             * transferList.add(transferTo);
             * txMap.put(TransferTrans.PARTICULARS, dataMap.get("ACT_NUM"));
             * transferTo = transactionDAO.addTransferCreditLocal(txMap,
             * newAmt);//debitAmt);
             * transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
             * transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
             * transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
             * transferTo.setLinkBatchId((String) dataMap.get("ACT_NUM"));
             * transferList.add(transferTo);
             * transactionDAO.setTransactionType(transactionDAO.TRANSFER); } //
             * if (loanAmt > 0) { //
             * transactionDAO.doTransferLocal(transferList,
             * CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE"))); // }
            /********************************************
             */
            // transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            // System.out.println("transferList  ==" + transferList + "debitAmt ===" + debitAmt + "totAmt === " + totAmt);
        }

        return debitAmt;
    }

    private double debitNewAccountAdjustOldAccountthroughCash(HashMap map, HashMap dataMap, HashMap acHeads,HashMap acHeads_new) throws Exception {
        double serchargeAmt = 0.0;
        TransactionDAO transactionDAO = null;
        boolean openingChrgCash = false;
        TxTransferTO transferDao = null;
        System.out.println("dataMap@@@@@@@Cash" + dataMap);
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        double debitAmt = 0;
        CashTransactionDAO cashDao;
        CashTransactionTO cashTO;
        HashMap cashMap;
        HashMap loanAuthTransMap;
        double balPrin = 0;
        //  HashMap totAmt = new HashMap();
        HashMap renewalMap = new HashMap();
        if (map.containsKey("RENEWAL_TRANS") && map.get("RENEWAL_TRANS") != null) {
            renewalMap = (HashMap) map.get("RENEWAL_TRANS");
            // totAmt = (HashMap) renewalMap.get("ALL_AMOUNT");//20-03-2014
            ArrayList transferList = new ArrayList();
            TransactionTO transferTo = new TransactionTO();
            // double chrge = CommonUtil.convertObjToDouble(renewalMap.get("ACT_CLOSING_CHARGE")).doubleValue();//20-03-2014
            // if (CommonUtil.convertObjToDouble(chrge).doubleValue() > 0) {
            //     totAmt.put("ACT_CLOSING_CHARGE", chrge);
            // }
            balPrin = CommonUtil.convertObjToDouble(totAmt.get("LOAN_BALANCE_PRINCIPAL"));
            //  if (chargeLst != null && chargeLst.size() > 0) {   // Account Closing Charges //20-03-2014
            //      totAmt.put("ACT_CLOSING_CHARGES", chargeLst);
            //  }
            double totalRecivable = CommonUtil.convertObjToDouble(renewalMap.get("TOTAL_RECEVABLE")).doubleValue();
            double renewalSanction = CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_SAN_AMT")).doubleValue();
            System.out.println("totalRecivable11 ==" + totalRecivable + "renewalSanction11 =" + renewalSanction);
            // renewalSanction=renewalSanction+chrge;
            // totalRecivable=totalRecivable+chrge;
            String oldAcctNo = CommonUtil.convertObjToStr(renewalMap.get("OLD_ACCT_NUM"));
             String oldPrd_id = CommonUtil.convertObjToStr(renewalMap.get("OLD_PROD_ID"));
            if (totalRecivable < renewalSanction) {
                debitAmt = renewalSanction - totalRecivable;
            }
             // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
            if (map.containsKey("serviceTaxDetails") && map.get("serviceTaxDetails") != null) {
                HashMap taxdetails = (HashMap) map.get("serviceTaxDetails");
                if(taxdetails != null && taxdetails.containsKey("TOT_TAX_AMT") && taxdetails.get("TOT_TAX_AMT") != null){
                   serchargeAmt = CommonUtil.convertObjToDouble(taxdetails.get("TOT_TAX_AMT")).doubleValue(); 
                   if(serchargeAmt > 0){
                       totalRecivable = totalRecivable + serchargeAmt;
                   }
                }                
            }
            // END
            if (chargeLst != null && chargeLst.size() > 0) {   //Charge Details Transaction
                ArrayList chargeCahToList = new ArrayList();
                for (int i = 0; i < chargeLst.size(); i++) {
                    HashMap chargeMap = new HashMap();
                    String accHead = "";
                    double chargeAmt = 0;
                    chargeMap = (HashMap) chargeLst.get(i);
                    loanAuthTransMap = new HashMap();
                    accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                    chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                    if (chargeAmt > 0 || chargeAmt > 0.0) {
                       	loanAuthTransMap.put("SELECTED_BRANCH_ID", dataMap.get("BRANCH_CODE"));
                        loanAuthTransMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                        loanAuthTransMap.remove("LOANDEBIT");
                        loanAuthTransMap.remove("PROD_ID");
                        loanAuthTransMap.remove("DEBIT_LOAN_TYPE");
                        loanAuthTransMap.put("ACCT_HEAD", accHead);
                        loanAuthTransMap.put("LIMIT", String.valueOf(chargeAmt));
                        loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                        loanAuthTransMap.put("TRANS_MOD_TYPE", "TL");
                        loanAuthTransMap.put("PARTICULARS", CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC")));
                        loanAuthTransMap.put("DEDUCTION_ACCU", chargeMap.get("DEDUCTION_ACCU"));
                        loanAuthTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                        loanAuthTransMap.put("OLD_ACCT_NUM", oldAcctNo);
                        loanAuthTransMap.put("ACCT_NUM", oldAcctNo);
                        loanAuthTransMap.put("TOKEN_NO", dataMap.get("TOKEN_NO"));
                        loanAuthTransMap.put("DEBIT_LOAN_TYPE", "DP");
                        //Adding on 21-05-2024 - // Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                        loanAuthTransMap.put("NEW_ACT_NUM", dataMap.get("ACT_NUM"));
                        if (chargeMap.get("DEDUCTION_ACCU").equals("O")) {
                            openingChrgCash = true;
                        }
                        ArrayList loanAuthTransList = loanAuthorizeTimeTransaction(loanAuthTransMap);
                        cashTO = new CashTransactionTO();
                        cashTO = (CashTransactionTO) loanAuthTransList.get(0);
                        chargeCahToList.add(cashTO);
                    }
                }
                HashMap renwMaptemp = (HashMap) renewalMap.get("ALL_AMOUNT");
                renwMaptemp.put("CHARGEDATA_GOLD", chargeCahToList);
                renewalMap.put("ALL_AMOUNT", renwMaptemp);
            }
            
            HashMap serviceTaxMap = new HashMap();
            if (map.containsKey("serviceTaxDetails") && map.get("serviceTaxDetails") != null) {
                serviceTaxMap = (HashMap) map.get("serviceTaxDetails");
                if (serviceTaxMap.containsKey("TOT_TAX_AMT") && serviceTaxMap.get("TOT_TAX_AMT") != null) {
                    HashMap renwMaptemp = (HashMap) renewalMap.get("ALL_AMOUNT");
                    // Added by nithya on 12-10-2018 for KD 286 - Need service tax option in (gold renewal screen)
                    renwMaptemp.put("TOT_SER_TAX_AMT", serviceTaxMap.get("TOT_TAX_AMT"));
                    renwMaptemp.put("SER_TAX_HEAD", serviceTaxMap.get("TAX_HEAD_ID"));
                    // End
                    renwMaptemp.put("SER_TAX_MAP", serviceTaxMap);
                    renewalMap.put("ALL_AMOUNT", renwMaptemp);
                }
            }            
            if (totalRecivable > 0) {
                cashDao = new CashTransactionDAO();
                cashTO = new CashTransactionTO();
                loanAuthTransMap = new HashMap();
               	loanAuthTransMap.put("SELECTED_BRANCH_ID", dataMap.get("BRANCH_CODE"));
                loanAuthTransMap.put("OLD_ACCT_NUM", oldAcctNo);
                loanAuthTransMap.put("ACCT_NUM", oldAcctNo);
                loanAuthTransMap.put("PROD_ID", oldPrd_id/*dataMap.get("PROD_ID")*/);
                loanAuthTransMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                loanAuthTransMap.put("ACCT_HEAD", acHeads.get("ACCT_HEAD"));
                loanAuthTransMap.put("TOKEN_NO", dataMap.get("TOKEN_NO"));
                loanAuthTransMap.put("LIMIT", new Double(totalRecivable));//dataMap.get("LIMIT"));
                loanAuthTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                loanAuthTransMap.put("ACCOUNT_CLOSING", "ACCOUNT_CLOSING");
                loanAuthTransMap.put("ALL_AMOUNT", renewalMap.get("ALL_AMOUNT"));//bbbb
                loanAuthTransMap.put("TRANS_MOD_TYPE", "TL");
                //loanAuthTransMap.put("SINGLE_TRANS_ID", generateSingleCashId);//20-03-2014
                //loanAuthTransMap.put("ALL_AMOUNT", totAmt);
                ArrayList loanAuthTransList = loanAuthorizeTimeTransactionCash(loanAuthTransMap);
                loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                cashTO = (CashTransactionTO) loanAuthTransList.get(0);
                //cashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                loanAuthTransMap.put(CASH_TO, cashTO);
                CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                cashMap = new HashMap();
                if (totalRecivable > 0) {
                    if(openingChrgCash){// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                       loanAuthTransMap.put("GST_LINK_BATCH_ID", dataMap.get("ACT_NUM")); 
                    }
                    cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                }
            }
            if (renewalSanction > 0) {
                System.out.println("renewalSanction debit loan nithya ::" + renewalSanction);
                loanAuthTransMap = new HashMap();
                loanAuthTransMap.put("SELECTED_BRANCH_ID", dataMap.get("BRANCH_CODE"));
                loanAuthTransMap.put("OLD_ACCT_NUM", oldAcctNo);
                loanAuthTransMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
                loanAuthTransMap.put("PROD_ID", dataMap.get("PROD_ID"));
                loanAuthTransMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                loanAuthTransMap.put("ACCT_HEAD", acHeads_new.get("ACCT_HEAD"));
                loanAuthTransMap.put("TOKEN_NO", dataMap.get("TOKEN_NO"));
                loanAuthTransMap.put("LIMIT", new Double(renewalSanction));//dataMap.get("LIMIT"));
                loanAuthTransMap.put("LOANDEBIT", "LOANDEBIT");
                loanAuthTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                loanAuthTransMap.put("TRANS_MOD_TYPE", "TL");
                //loanAuthTransMap.put("SINGLE_TRANS_ID", generateSingleCashId);//20-03-2014
                if (openingChrgCash) {// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]s
                    loanAuthTransMap.put("GST_LINK_BATCH_ID", dataMap.get("ACT_NUM"));
                    loanAuthTransMap.put("LOAN_DEBIT_THROUGH_CASH", "LOAN_DEBIT_THROUGH_CASH");
                    loanAuthTransMap.put("OPENING_CHARGE_CASH", "OPENING_CHARGE_CASH");//change to be verified...
                }
                loanAuthTransMap.put("NEW_ACT_NUM", dataMap.get("ACT_NUM"));
                ArrayList loanAuthTransList = loanAuthorizeTimeTransactionCash(loanAuthTransMap);
                loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                loanAuthTransMap.put("DEBIT_LOAN_TYPE", "DP");
                CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                cashMap = new HashMap();
                cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
            }

        }

        return 0;
    }
    // added by rishad for gold loan renewal with cash and transfer(01/may 2015)
    private double debitNewAccountAdjustOldAccountCashTransfer(HashMap map, HashMap dataMap, HashMap acHeads, HashMap crMap, HashMap acHeads_new) throws Exception {
        TransactionDAO transactionDAO = null;
        boolean openingChrg = false;
        boolean openingChrgCash = false;
        TxTransferTO transferDao = null;
        double serchargeAmt = 0.0;
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        double debitAmt = 0;
        HashMap renewalMap = new HashMap();
        HashMap loanAuthTransMap;
        CashTransactionDAO cashDao;
        CashTransactionTO cashTO;
        HashMap cashMap;
        double totalChargeAmount = 0;
        if (map.containsKey("RENEWAL_TRANS") && map.get("RENEWAL_TRANS") != null) {
            renewalMap = (HashMap) map.get("RENEWAL_TRANS");
            totAmt = (HashMap) ((HashMap) renewalMap.get("ALL_AMOUNT")).clone();
            if (map.containsKey("serviceTaxDetails") && map.get("serviceTaxDetails") != null) {
                HashMap taxdetails = (HashMap) map.get("serviceTaxDetails");
                if (taxdetails != null && taxdetails.containsKey("TOT_TAX_AMT") && taxdetails.get("TOT_TAX_AMT") != null) {
                    serchargeAmt = CommonUtil.convertObjToDouble(taxdetails.get("TOT_TAX_AMT")).doubleValue();
                }
            }

            // END
            TxTransferTO transferTo = new TxTransferTO();
            double chrge = CommonUtil.convertObjToDouble(renewalMap.get("ACT_CLOSING_CHARGE")).doubleValue();
            double totalRecivable = CommonUtil.convertObjToDouble(renewalMap.get("TOTAL_RECEVABLE")).doubleValue();
            double renewalSanction = CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_SAN_AMT")).doubleValue();
            String oldAcctNo = CommonUtil.convertObjToStr(renewalMap.get("OLD_ACCT_NUM"));
            String oldPrd_id = CommonUtil.convertObjToStr(renewalMap.get("OLD_PROD_ID"));
            double cashAmount = 0.0;
            if (totalRecivable < renewalSanction) {
                debitAmt = renewalSanction - (totalRecivable + chrge + serchargeAmt);
            }
            
            if (totalRecivable >= renewalSanction) {
                cashAmount = (totalRecivable + chrge + serchargeAmt) - renewalSanction;
                double tempCashAmount = cashAmount;
                //transaction amount for cash transaction
                if (tempCashAmount >= serchargeAmt) {
                    HashMap serviceTaxMap = new HashMap();
                    if (map.containsKey("serviceTaxDetails") && map.get("serviceTaxDetails") != null) {
                        serviceTaxMap = (HashMap) map.get("serviceTaxDetails");
                        if (serviceTaxMap.containsKey("TOT_TAX_AMT") && serviceTaxMap.get("TOT_TAX_AMT") != null) {
                            HashMap renwMaptempTax = (HashMap) renewalMap.get("ALL_AMOUNT");
                            renwMaptempTax.put("TOT_SER_TAX_AMT", serviceTaxMap.get("TOT_TAX_AMT"));
                            renwMaptempTax.put("SER_TAX_HEAD", serviceTaxMap.get("TAX_HEAD_ID"));
                            renwMaptempTax.put("SER_TAX_MAP", serviceTaxMap);
                            renewalMap.put("ALL_AMOUNT", renwMaptempTax);
                        }
                    }
                    tempCashAmount = tempCashAmount - serchargeAmt;
                    serchargeAmt=0;
                }
                if (chargeLst != null && chargeLst.size() > 0) {   //Charge Details Transaction
                    ArrayList chargeCahToList = new ArrayList();
                    for (int i = 0; i < chargeLst.size(); i++) {
                        HashMap chargeMap = new HashMap();
                        String accHead = "";
                        double chargeAmt = 0;
                        chargeMap = (HashMap) chargeLst.get(i);
                        loanAuthTransMap = new HashMap();
                        accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                        chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                        if (chargeAmt > 0 || chargeAmt > 0.0) {
                            totalChargeAmount += chargeAmt;
                            loanAuthTransMap.put("SELECTED_BRANCH_ID", dataMap.get("BRANCH_CODE"));
                            loanAuthTransMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                            loanAuthTransMap.remove("LOANDEBIT");
                            loanAuthTransMap.remove("PROD_ID");
                            loanAuthTransMap.remove("DEBIT_LOAN_TYPE");
                            loanAuthTransMap.put("ACCT_HEAD", accHead);
                            loanAuthTransMap.put("LIMIT", String.valueOf(chargeAmt));
                            loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                            loanAuthTransMap.put("TRANS_MOD_TYPE", "TL");
                            loanAuthTransMap.put("PARTICULARS", CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC")));
                            loanAuthTransMap.put("DEDUCTION_ACCU", chargeMap.get("DEDUCTION_ACCU"));
                            loanAuthTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            loanAuthTransMap.put("OLD_ACCT_NUM", oldAcctNo);
                            loanAuthTransMap.put("ACCT_NUM", oldAcctNo);
                            loanAuthTransMap.put("TOKEN_NO", dataMap.get("TOKEN_NO"));
                            loanAuthTransMap.put("DEBIT_LOAN_TYPE", "DP");
                            loanAuthTransMap.put("NEW_ACT_NUM", dataMap.get("ACT_NUM"));
                            if(chargeMap.get("DEDUCTION_ACCU").equals("O")){// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                                openingChrgCash = true;
                            }
                            ArrayList loanAuthTransList = loanAuthorizeTimeTransaction(loanAuthTransMap);
                            cashTO = new CashTransactionTO();
                            cashTO = (CashTransactionTO) loanAuthTransList.get(0);
                            cashTO.setScreenName("Gold Loan Account Renewal");
                            chargeCahToList.add(cashTO);
                        }
                    }
                    if (tempCashAmount >= totalChargeAmount) {
                        HashMap renwMapCharge = (HashMap) map.get("RENEWAL_TRANS");
                        HashMap renewChargeMap = (HashMap) renwMapCharge.get("ALL_AMOUNT");
                        renewChargeMap.put("CHARGEDATA_GOLD", chargeCahToList);
                        renewalMap.put("ALL_AMOUNT", renewChargeMap);
                        tempCashAmount = tempCashAmount - totalChargeAmount;
                        chargeLst = null;

                    }
                }
                // Start
//                HashMap renwMapChargetemp =  (HashMap) map.get("RENEWAL_TRANS");
                HashMap renwMaptemp = (HashMap) renewalMap.get("ALL_AMOUNT");
                if (renwMaptemp.containsKey("POSTAGE CHARGES")) {
                    double chargeAmt = CommonUtil.convertObjToDouble(renwMaptemp.get("POSTAGE CHARGES"));
                    if (chargeAmt > 0 && tempCashAmount >= chargeAmt) {                      
                          tempCashAmount -= chargeAmt;
//                        renwMaptemp.put("POSTAGE CHARGES", tempCashAmount);
                          totAmt.remove("POSTAGE CHARGES");
                    } else {
                        renwMaptemp.put("POSTAGE CHARGES", tempCashAmount);
                        totAmt.put("POSTAGE CHARGES", chargeAmt - tempCashAmount);
                        tempCashAmount = 0;
                    }
                }
                if (renwMaptemp.containsKey("MISCELLANEOUS CHARGES")) {
                    double chargeAmt = CommonUtil.convertObjToDouble(renwMaptemp.get("MISCELLANEOUS CHARGES"));
                    if (chargeAmt > 0 && tempCashAmount >= chargeAmt) {
                        totAmt.remove("MISCELLANEOUS CHARGES");
                        tempCashAmount -= chargeAmt;
                    } else {
                        renwMaptemp.put("MISCELLANEOUS CHARGES", tempCashAmount);
                        totAmt.put("MISCELLANEOUS CHARGES", chargeAmt - tempCashAmount);
                        tempCashAmount = 0;
                    }
                }
                if (renwMaptemp.containsKey("LEGAL CHARGES")) {
                    double chargeAmt = CommonUtil.convertObjToDouble(renwMaptemp.get("LEGAL CHARGES"));
                    if (chargeAmt > 0 && tempCashAmount >= chargeAmt) {
                        totAmt.remove("LEGAL CHARGES");
                        tempCashAmount -=chargeAmt;
                    } else {
                        renwMaptemp.put("LEGAL CHARGES", tempCashAmount);
                        totAmt.put("LEGAL CHARGES", chargeAmt - tempCashAmount);
                        tempCashAmount = 0;
                    }
                }
                if (renwMaptemp.containsKey("INSURANCE CHARGES")) {
                    double chargeAmt = CommonUtil.convertObjToDouble(renwMaptemp.get("INSURANCE CHARGES"));
                    if (chargeAmt > 0 && tempCashAmount >= chargeAmt) {
                        totAmt.remove("INSURANCE CHARGES");
                        tempCashAmount -= chargeAmt;
                    } else {
                        renwMaptemp.put("INSURANCE CHARGES", tempCashAmount);
                        totAmt.put("INSURANCE CHARGES", chargeAmt - tempCashAmount);
                        tempCashAmount = 0;
                    }
                }
                if (renwMaptemp.containsKey("EXECUTION DECREE CHARGES")) {
                    double chargeAmt = CommonUtil.convertObjToDouble(renwMaptemp.get("EXECUTION DECREE CHARGES"));
                    if (chargeAmt > 0 && tempCashAmount >= chargeAmt) {
                        totAmt.remove("EXECUTION DECREE CHARGES");
                        tempCashAmount -= chargeAmt;
                    } else {
                        renwMaptemp.put("EXECUTION DECREE CHARGES", tempCashAmount);
                        totAmt.put("EXECUTION DECREE CHARGES", chargeAmt - tempCashAmount);
                        tempCashAmount = 0;
                    }
                }
                if (renwMaptemp.containsKey("ARBITRARY CHARGES")) {
                    double chargeAmt = CommonUtil.convertObjToDouble(renwMaptemp.get("ARBITRARY CHARGES"));
                    if (chargeAmt > 0 && tempCashAmount >= chargeAmt) {
                        totAmt.remove("ARBITRARY CHARGES");
                        tempCashAmount -= chargeAmt;
                    } else {
                        renwMaptemp.put("ARBITRARY CHARGES", tempCashAmount);
                        totAmt.put("ARBITRARY CHARGES", chargeAmt - tempCashAmount);
                        tempCashAmount = 0;
                    }
                }
                if (renwMaptemp.containsKey("NOTICE CHARGES")) {
                    double chargeAmt = CommonUtil.convertObjToDouble(renwMaptemp.get("NOTICE CHARGES"));
                    if (chargeAmt > 0 && tempCashAmount >= chargeAmt) {
                        totAmt.remove("NOTICE CHARGES");
                        tempCashAmount -= chargeAmt;
                    } else {
                        renwMaptemp.put("NOTICE CHARGES", tempCashAmount);
                        totAmt.put("NOTICE CHARGES", chargeAmt - tempCashAmount);
                        tempCashAmount = 0;
                    }
                }
                if (renwMaptemp.containsKey("EP_COST")) {
                    double chargeAmt = CommonUtil.convertObjToDouble(renwMaptemp.get("EP_COST"));
                    if (chargeAmt > 0 && tempCashAmount >= chargeAmt) {
                        totAmt.remove("EP_COST");
                        tempCashAmount -= chargeAmt;
                    } else {
                        renwMaptemp.put("EP_COST", tempCashAmount);
                        totAmt.put("EP_COST", chargeAmt - tempCashAmount);
                        tempCashAmount = 0;
                    }
                }
                if (renwMaptemp.containsKey("LEGAL CHARGES")) {
                    double chargeAmt = CommonUtil.convertObjToDouble(renwMaptemp.get("LEGAL CHARGES"));
                    if (chargeAmt > 0 && tempCashAmount >= chargeAmt) {
                        totAmt.remove("LEGAL CHARGES");
                        tempCashAmount -= chargeAmt;
                    } else {
                        renwMaptemp.put("LEGAL CHARGES", tempCashAmount);
                        totAmt.put("LEGAL CHARGES", chargeAmt - tempCashAmount);
                        tempCashAmount = 0;
                    }
                }
                if (renwMaptemp.containsKey("ARC_COST") ) {
                    double chargeAmt = CommonUtil.convertObjToDouble(renwMaptemp.get("ARC_COST"));
                    if (chargeAmt > 0 && tempCashAmount >= chargeAmt) {
                        totAmt.remove("ARC_COST");
                        tempCashAmount -= chargeAmt;
                    } else {
                        renwMaptemp.put("ARC_COST", tempCashAmount);
                        totAmt.put("ARC_COST", chargeAmt - tempCashAmount);
                        tempCashAmount = 0;
                    }
                }
                
                 if (renwMaptemp.containsKey("ADVERTISE CHARGES")) {
                    double chargeAmt = CommonUtil.convertObjToDouble(renwMaptemp.get("ADVERTISE CHARGES"));
                    if (chargeAmt > 0 && tempCashAmount >= chargeAmt) {
                        totAmt.remove("ADVERTISE CHARGES");
                        tempCashAmount -= chargeAmt;
                    } else {
                        renwMaptemp.put("ADVERTISE CHARGES", tempCashAmount);
                        totAmt.put("ADVERTISE CHARGES", chargeAmt - tempCashAmount);
                        tempCashAmount = 0;
                    }
                }
                if (renwMaptemp.containsKey("OTHER_CHARGES")) {
                    HashMap otherChargesMap = new HashMap();
                    otherChargesMap = (HashMap) renwMaptemp.get("OTHER_CHARGES");
                    if (otherChargesMap.containsKey("OTHER CHARGES") && otherChargesMap.get("OTHER CHARGES") != null) {
                        double chargeAmt = CommonUtil.convertObjToDouble(otherChargesMap.get("OTHER CHARGES"));
                        if (chargeAmt > 0 && tempCashAmount >= chargeAmt) {
                             totAmt.remove("OTHER_CHARGES");
                          tempCashAmount -= chargeAmt;
                        }
                        else{
                              
                        otherChargesMap.put("OTHER CHARGES",chargeAmt - tempCashAmount);
                        totAmt.put("OTHER_CHARGES",otherChargesMap);
                        tempCashAmount = 0;
                        }
                    }
                }
                renewalMap.put("ALL_AMOUNT", renwMaptemp);
                //end
                if (cashAmount > 0) {
                    cashDao = new CashTransactionDAO();
                    cashTO = new CashTransactionTO();
                    loanAuthTransMap = new HashMap();
                    loanAuthTransMap.put("SELECTED_BRANCH_ID", dataMap.get("BRANCH_CODE"));
                    loanAuthTransMap.put("OLD_ACCT_NUM", oldAcctNo);
                    loanAuthTransMap.put("ACCT_NUM", oldAcctNo);
                    loanAuthTransMap.put("PROD_ID", oldPrd_id/*
                             * dataMap.get("PROD_ID")
                             */);
                    loanAuthTransMap.put("BRANCH_CODE", dataMap.get("BRANCH_CODE"));
                    loanAuthTransMap.put("ACCT_HEAD", acHeads.get("ACCT_HEAD"));
                    loanAuthTransMap.put("TOKEN_NO", dataMap.get("TOKEN_NO"));
                    if (cashAmount > 0) {
                        loanAuthTransMap.put("LIMIT", new Double(cashAmount-chrge));//dataMap.get("LIMIT")); 
                    }
                    loanAuthTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                    loanAuthTransMap.put("ACCOUNT_CLOSING", "ACCOUNT_CLOSING");
                    loanAuthTransMap.put("ALL_AMOUNT", renewalMap.get("ALL_AMOUNT"));//bbbb
                    loanAuthTransMap.put("TRANS_MOD_TYPE", "TL");
                    loanAuthTransMap.put("SCREEN_NAME", "Gold Loan Account Renewal");
                    ArrayList loanAuthTransList = loanAuthorizeTimeTransactionCash(loanAuthTransMap);
                    loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                    cashTO = (CashTransactionTO) loanAuthTransList.get(0);
                    loanAuthTransMap.put(CASH_TO, cashTO);
                     if(openingChrgCash){// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                       loanAuthTransMap.put("GST_LINK_BATCH_ID", dataMap.get("ACT_NUM")); 
                    }
                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    cashMap = new HashMap();
                    if (totalRecivable > 0) {
                        cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                        double interest = CommonUtil.convertObjToDouble(totAmt.get("INTEREST"));
                        double penal_int = CommonUtil.convertObjToDouble(totAmt.get("PENAL_INT"));
                        if (tempCashAmount >= penal_int) {
                            totAmt.put("PENAL_INT", 0);
                            tempCashAmount = tempCashAmount - penal_int;
                        } else {
                            totAmt.put("PENAL_INT", penal_int - tempCashAmount);
                            tempCashAmount = 0;
                        }
                        if (tempCashAmount >= interest) {
                            totAmt.put("INTEREST", 0);
                            totAmt.put("CURR_MONTH_INT", 0);
                            tempCashAmount = tempCashAmount - interest;
                        } else {
                            
                            totAmt.put("INTEREST", interest - tempCashAmount);
                            totAmt.put("CURR_MONTH_INT", interest - tempCashAmount);
                            tempCashAmount = 0;
                        }
                    }
                }
                //end
                totalRecivable = renewalSanction;
            }
               double totChrgAmt = 0;
            if (chargeLst != null && chargeLst.size() > 0) {   //Charge Details Transaction
                for (int i = 0; i < chargeLst.size(); i++) {
                    HashMap chargeMap = new HashMap();
                    String accHead = "";
                    double chargeAmt = 0;
                    String chargeType = "";
                    chargeMap = (HashMap) chargeLst.get(i);
                    accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                    chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                    if (chargeAmt > 0 || chargeAmt > 0.0) {
                        if (isRenewal) {
                            totChrgAmt = totChrgAmt + chargeAmt;
                        }
                        chargeType = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
                        txMap = new HashMap();
                        transferTo = new TxTransferTO();
                        txMap.put(TransferTrans.CR_AC_HD, accHead);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                        txMap.put(TransferTrans.PARTICULARS, dataMap.get("ACT_NUM"));
                        
                        if (isRenewal) {// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "LOAN_ACT_CLOSING_CHARGE");
                        } else {
                            txMap.put(TransferTrans.DR_INSTRUMENT_2, "LOAN_ACT_OPENING_CHARGE");
                        }
                        
                        txMap.put("TRANS_MOD_TYPE", "GL");
                        if (isRenewal && chargeMap.containsKey("DEDUCTION_ACCU")
                                && chargeMap.get("DEDUCTION_ACCU").equals("O")) {
                            txMap.put("generateSingleTransId", generateSingleTranIdForRenOpCharge);
                        } else {
                            txMap.put("generateSingleTransId", generateSingleTransId);
                        }
                        txMap.put("generateSingleTransId", generateSingleTransId);
                        txMap.put(TransferTrans.PARTICULARS, chargeType + " - : " + dataMap.get("ACT_NUM"));
                        if (isRenewal && chargeMap.containsKey("DEDUCTION_ACCU")
                                && chargeMap.get("DEDUCTION_ACCU").equals("O")) {
                            txMap.put("generateSingleTransId", generateSingleTranIdForRenOpCharge);
                        } else {
                            txMap.put("generateSingleTransId", generateSingleTransId);
                        }
                        txMap.put("TRANS_MOD_TYPE", "TL");
                        if (isRenewal) {
                            txMap.put("DEBIT_GOLDLOAN_TYPE", "DP");
                        }
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmt);
                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                        if (isRenewal && chargeMap.containsKey("DEDUCTION_ACCU")
                                && chargeMap.get("DEDUCTION_ACCU").equals("O")) {// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                            transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
                            openingChrg = true;
                        }else{
                            transferTo.setLinkBatchId(oldAccNumRnew);
                        } 
                        transferTo.setScreenName("Gold Loan Account Renewal");
                        transferTo.setSingleTransId(generateSingleTransId);
                        renewaltransList.add(transferTo);
                    }
                }
            }
            
                           if(isRenewal && serchargeAmt>0){
                        if (map.containsKey("serviceTaxDetails") && map.get("serviceTaxDetails") != null) {
                            HashMap taxdetails = (HashMap) map.get("serviceTaxDetails");
                            double serviceTax = 0.0;
                            double swachhCess = 0.0;
                            double krishikalyanCess = 0.0;
                            double taxAmt = 0.0;
                            double normalServiceTax = 0.0;
                            if (taxdetails.containsKey("TOT_TAX_AMT") && taxdetails.get("TOT_TAX_AMT") != null) {
                                taxAmt = CommonUtil.convertObjToDouble(taxdetails.get("TOT_TAX_AMT"));
                            }
                            if (taxdetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && taxdetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                swachhCess = CommonUtil.convertObjToDouble(taxdetails.get(ServiceTaxCalculation.SWACHH_CESS));
                            }
                            if (taxdetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                krishikalyanCess = CommonUtil.convertObjToDouble(taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                            }
                            if (taxdetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && taxdetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(taxdetails.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                            taxAmt -= (swachhCess + krishikalyanCess);
                            String seraccHead = "";
                            seraccHead = CommonUtil.convertObjToStr(taxdetails.get("TAX_HEAD_ID"));
                            double chargeAmt = CommonUtil.convertObjToDouble(taxdetails.get("TOT_TAX_AMT")).doubleValue();
                            if (normalServiceTax > 0) {
                                txMap = new HashMap();
                                transferTo = new TxTransferTO();
                                txMap.put(TransferTrans.CR_AC_HD, seraccHead);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                                txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                                txMap.put(TransferTrans.PARTICULARS, "Service Tax" + " - : " + dataMap.get("ACT_NUM"));
                                txMap.put(TransferTrans.DR_INSTRUMENT_2,CommonConstants.SERVICE_TAX_CHRG);// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                                if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {// Added by nithya on 26-06-2018 for servicetax transmode issue                                
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                                }
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                if(openingChrg){// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                                  transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                }else{
                                   transferTo.setLinkBatchId(oldAccNumRnew); 
                                }
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Gold Loan Account Renewal");
                                renewaltransList.add(transferTo);
                            }
                            if (swachhCess > 0) {
                                txMap = new HashMap();
                                transferTo = new TxTransferTO();
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(taxdetails.get(ServiceTaxCalculation.SWACHH_HEAD_ID)));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                                txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                                txMap.put(TransferTrans.PARTICULARS, "CGST" + " - : " + dataMap.get("ACT_NUM"));
                                txMap.put(TransferTrans.DR_INSTRUMENT_2,CommonConstants.SERVICE_TAX_CHRG);
                                if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {// Added by nithya on 26-06-2018 for servicetax transmode issue                                
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                                }
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                //transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                if(openingChrg){// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                                  transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                }else{
                                   transferTo.setLinkBatchId(oldAccNumRnew); 
                                }
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Gold Loan Account Renewal");
                                renewaltransList.add(transferTo);
                            }
                            if (krishikalyanCess > 0) {
                                txMap = new HashMap();
                                transferTo = new TxTransferTO();
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_HEAD_ID)));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                                txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                                txMap.put(TransferTrans.PARTICULARS, "SGST" + " - : " + dataMap.get("ACT_NUM"));
                                txMap.put(TransferTrans.DR_INSTRUMENT_2,CommonConstants.SERVICE_TAX_CHRG);
                                if (dataMap.get("BEHAVES_LIKE") != null && dataMap.get("BEHAVES_LIKE").equals("OD")) {// Added by nithya on 26-06-2018 for servicetax transmode issue                                
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                                } else {
                                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                                }
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                //transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                if(openingChrg){// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                                  transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                }else{
                                   transferTo.setLinkBatchId(oldAccNumRnew); 
                                }
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName("Gold Loan Account Renewal");
                                renewaltransList.add(transferTo);
                            }
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        }
                    }
                    // end

            txMap = new HashMap();
            transferTo = new TxTransferTO();
            txMap.put(TransferTrans.DR_ACT_NUM, acct_No);
            txMap.put(TransferTrans.DR_AC_HD, acHeads_new.get("ACCT_HEAD"));
            txMap.put(TransferTrans.DR_PROD_ID, dataMap.get("PROD_ID"));
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
            txMap.put(TransferTrans.DR_BRANCH, dataMap.get("BRANCH_CODE"));
            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
            txMap.put("DR_INST_TYPE", "VOUCHER");
            txMap.put(TransferTrans.PARTICULARS, "disbursement - " + oldAcctNo);
            txMap.put("AUTHORIZEREMARKS", "DP");
            txMap.put("DEBIT_LOAN_TYPE", "DP");

            txMap.put(TransferTrans.CR_ACT_NUM, oldAcctNo);
            txMap.put(TransferTrans.CR_AC_HD, acHeads.get("ACCT_HEAD"));
            txMap.put(TransferTrans.CR_PROD_ID, oldPrd_id);
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
            txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
            txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            txMap.put("generateSingleTransId", generateSingleTransId);
            transferTo.setInitiatedBranch(_branchCode);
            transferTo.setAmount(new Double(totalRecivable));
            transferTo.setInpAmount(new Double(totalRecivable));
            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
            transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));

            //transferTo.setLinkBatchId(oldAcctNo);
            transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
            transferTo.setSingleTransId(generateSingleTransId);
            txMap.put("TRANS_MOD_TYPE", "TL");
            if (debitAmt > 0) {
                transferTo = transactionDAO.addTransferDebitLocal(txMap, totalRecivable + chrge + serchargeAmt);
            } else {
                transferTo = transactionDAO.addTransferDebitLocal(txMap, renewalSanction);
            }
            transferTo.setScreenName("Gold Loan Account Renewal");
            transferTo.setGlTransActNum(acct_No);
            //transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
            transferTo.setLinkBatchId(oldAcctNo);//change to be verified...
            renewaltransList.add(transferTo);
            txMap.put(TransferTrans.PARTICULARS, oldAcctNo);
            transferTo.setInitiatedBranch(_branchCode);
            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
            transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            txMap.put(TransferTrans.PARTICULARS, "disbursement - " + acct_No);
            txMap.put("generateSingleTransId", generateSingleTransId);
            //transferTo.setLinkBatchId(oldAcctNo);// Commented by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
            transferTo.setSingleTransId(generateSingleTransId);
            txMap.put("TRANS_MOD_TYPE", "TL");
            if (isRenewal) {
                txMap.put("DEBIT_GOLDLOAN_TYPE", "DP");
            }
            transferTo = transactionDAO.addTransferCreditLocal(txMap, totalRecivable);
            transferTo.setLinkBatchId(oldAcctNo);// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
            transferTo.setScreenName("Gold Loan Account Renewal");
            renewaltransList.add(transferTo);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setLoanAmtMap(totAmt);
            transactionDAO.setLinkBatchID(oldAcctNo);
            renewaltransList.add("GOLD_LOAN_RENEWAL");// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
            System.out.println("renewaltransList checking nithya:: " + renewaltransList);
            transactionDAO.doTransferLocal(renewaltransList, CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        }

        return debitAmt;
    }

    private void setBorrower_No(String borrower_No) {
        //        objAuthorizedSignatoryDAO.setBorrower_No(borrower_No);
        //        objPowerOfAttorneyDAO.setBorrower_No(borrower_No);
        this.borrower_No = borrower_No;
        //        objAuthorizedSignatoryDAO.setCommand(objTermLoanBorrowerTO.getCommand());
        //        objPowerOfAttorneyDAO.setCommand(objTermLoanBorrowerTO.getCommand());
    }

    private String getBorrower_No() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "BORROWER_NO");
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");
        String strBorrower_No = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        where = null;
        dao = null;
        setBorrower_No(strBorrower_No);
        return strBorrower_No;
    }

    private void setAccount_No(String acct_No) {
        this.acct_No = acct_No;
    }

    private String getAccount_No() throws Exception {
        //        IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        //        where.put(CommonConstants.MAP_WHERE, "LOAN.ACCOUNT_NO");
        //        String strAcct_No = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        //        where = null;
        //        dao = null;
        //        return strAcct_No;
        //        HashMap map = new HashMap();
        //        int len=10;
        //        map.put("PROD_ID", prod_id);
        //        List list = (List) sqlMap.executeQueryForList("getNextActNumForLoan", map);
        //        System.out.println("@@@@list"+list);
        //        if(list!=null && list.size()>0)
        //        {
        //            where = (HashMap)list.get(0);
        //        }
        //        String prefix = CommonUtil.convertObjToStr( where.get("NUMBER_PATTERN"));
        //        String lastacno = CommonUtil.convertObjToStr( where.get("LAST_AC_NO"));
        //         int numFrom = prefix.trim().length();
        //         String id = "";
        //        if(lastacno.equals("0")){
        //          id = CommonUtil.convertObjToStr( where.get("NUMBER_PATTERN_SUFFIX"));
        //        }else{
        //          id = String.valueOf(Integer.parseInt(String.valueOf( where.get("LAST_AC_NO")))+1);
        //        }
        //                String genID = prefix.toUpperCase() + CommonUtil.lpad(id, len - numFrom, '0');
        //                sqlMap.executeUpdate("updateNextIdForLoan", map);
        //                return genID;

        HashMap mapData = new HashMap();
        String strPrefix = "";
        String strNum = "";
        //        int len=10;
        //        where.put("PROD_ID", prod_id);
        //        List lst = (List) sqlMap.executeQueryForList("getNextActNumForLoan", where);
        //        if(lst != null && lst.size() > 0){
        //            mapData = (HashMap)lst.get(0);
        //        }
        //        if (mapData.containsKey("PREFIX")) {
        //            strPrefix = (String) mapData.get("PREFIX");
        //        }
        //        int numFrom = strPrefix.trim().length();
        //        String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
        //        System.out.println("@@@@@@@@"+newID);
        //        String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE")))+1);
        //        System.out.println("@@@@@@@@"+nxtID);
        //        String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
        //        where.put("VALUE", nxtID);
        //        sqlMap.executeUpdate("updateNextIdForLoan", where);
        //        newLoan = true;
        String genID = null;
        int len = 13;
        where.put("PROD_ID", prod_id);
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        //        List lst = (List) sqlMap.executeQueryForList("getNextActNumForLoan", where);
        List lst = (List) sqlMap.executeQueryForList("getCoreBankNextActNum", where);
        if (lst != null && lst.size() > 0) {
            mapData = (HashMap) lst.get(0);
            if (mapData.containsKey("PREFIX")) {
                strPrefix = (String) mapData.get("PREFIX");
            }
            int numFrom = strPrefix.trim().length();
            String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
            System.out.println("@@@@@@@@" + newID);
            String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
            System.out.println("@@@@@@@@" + nxtID);
            genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
            where.put("VALUE", nxtID);
            //        sqlMap.executeUpdate("updateNextId", where);
            sqlMap.executeUpdate("updateCoreBankNextActNum", where);
        } else {
            len = 10;
            lst = (List) sqlMap.executeQueryForList("getNextActNumForLoan", where);
            if (lst != null && lst.size() > 0) {
                mapData = (HashMap) lst.get(0);
            }
            if (mapData.containsKey("PREFIX")) {
                strPrefix = (String) mapData.get("PREFIX");
            }
            int numFrom = strPrefix.trim().length();
            String newID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))));
            System.out.println("@@@@@@@@" + newID);
            String nxtID = String.valueOf(Integer.parseInt(String.valueOf(mapData.get("LAST_VALUE"))) + 1);
            System.out.println("@@@@@@@@" + nxtID);
            genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
            where.put("VALUE", nxtID);
            sqlMap.executeUpdate("updateNextIdForLoan", where);
        }
        newLoan = true;
        return genID;

    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            NomineeDAO objNomineeDAO = new NomineeDAO();
            System.out.println("update #%$#$%sas#$%map :" + map);
            setBorrower_No(objTermLoanBorrowerTO.getBorrowNo());
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_MODIFIED);
//            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_MODIFIED);

            logTO.setData(objTermLoanBorrowerTO.toString());
            logTO.setPrimaryKey(objTermLoanBorrowerTO.getKeyData());
            logTO.setStatus(objTermLoanBorrowerTO.getCommand());
            executeUpdate("updateTermLoanBorrowerTO", objTermLoanBorrowerTO);
            logDAO.addToLog(logTO);

//            logTO.setData(objTermLoanCompanyTO.toString());
//            logTO.setPrimaryKey(objTermLoanCompanyTO.getKeyData());
//            logTO.setStatus(objTermLoanCompanyTO.getCommand());
//            executeUpdate("updateTermLoanCompanyTO", objTermLoanCompanyTO);
//            logDAO.addToLog(logTO);

//            executeAllTabQuery();
            if(map.containsKey("RENEW_OLD_PROD_ID") && old_prod_id.length()<0){
                old_prod_id = CommonUtil.convertObjToStr(map.get("RENEW_OLD_PROD_ID"));
            }
            acct_No = CommonUtil.convertObjToStr(map.get("ACCOUNT_NUMBER"));//Added By Kannan AR
            executeFacilityTabQuery();
            executeSecurityUpdateQuery(map);//Added By Kannan AR
               
            /*
             * add for npa status changes
             */
//            if( NPA !=null && NPA .size()>0 && CommonUtil.convertObjToStr(NPA.get("MODE")).equals("UPDATE") )
////                updateAssetChanges();
            /*
             * npa finish
             */
//            if (objSMSSubscriptionTO!=null) {
//                updateSMSSubscription();
//            }
            ArrayList nomineeTOList = (ArrayList) map.get("AccountNomineeTO");
            ArrayList nomineeDeleteList = (ArrayList) map.get("AccountNomineeDeleteTO");
            acct_No = CommonUtil.convertObjToStr(map.get("ACCOUNT_NUMBER"));
            System.out.println("@#$@#$#$acct_No:" + acct_No);
            // Update the data regarding the NomineeTable...
            //__ Data for Nominee...
            if (nomineeDeleteList != null) {
                if (nomineeDeleteList.size() > 0) {
                    System.out.println("Deleted Nominee List " + nomineeDeleteList);
                    objNomineeDAO.deleteData(acct_No, SCREEN);
                    objNomineeDAO.insertData(nomineeDeleteList, acct_No, true, userID, SCREEN, logTO, logDAO);
                }
            }

            if (nomineeTOList != null) {
                if (nomineeTOList.size() > 0) {
                    System.out.println("Nominee List " + nomineeTOList);
                    objNomineeDAO.deleteData(acct_No, SCREEN);
                    objNomineeDAO.insertData(nomineeTOList, acct_No, false, userID, SCREEN, logTO, logDAO);
                }
            }
            //Commented by Kannan AR due to duplicate code it already availble inside of executeFacilityTabQuery method
            /*if (objSMSSubscriptionTO != null) {
                objSMSSubscriptionTO.setActNum(acct_No);
                updateSMSSubscription();
            }*/
            sqlMap.commitTransaction();
            nomineeTOList = null;
            nomineeDeleteList = null;
            objNomineeDAO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    void updateAssetChanges() {
        if (NPA != null) {

            System.out.println("check npa@@@@###" + NPA);
            try {
                sqlMap.executeUpdate("NPAHISTORY_DAO", NPA);
            } catch (Exception e) {
                e.printStackTrace();
            }
            NPA = null;
        }

    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            setBorrower_No(objTermLoanBorrowerTO.getBorrowNo());
            objTermLoanBorrowerTO.setStatus(CommonConstants.STATUS_DELETED);
//            objTermLoanCompanyTO.setStatus(CommonConstants.STATUS_DELETED);

            logTO.setData(objTermLoanBorrowerTO.toString());
            logTO.setPrimaryKey(objTermLoanBorrowerTO.getKeyData());
            logTO.setStatus(objTermLoanBorrowerTO.getCommand());
            executeUpdate("deleteTermLoanBorrowerTO", objTermLoanBorrowerTO);
            logDAO.addToLog(logTO);

//            logTO.setData(objTermLoanCompanyTO.toString());
//            logTO.setPrimaryKey(objTermLoanCompanyTO.getKeyData());
//            logTO.setStatus(objTermLoanCompanyTO.getCommand());
//            executeUpdate("deleteTermLoanCompanyTO", objTermLoanCompanyTO);
            logDAO.addToLog(logTO);

            executeAllTabQuery();

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void executeJointAcctTabQuery() throws Exception {
        TermLoanJointAcctTO objTermLoanJointAcctTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = jointAcctMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To retrieve the TermLoanJointAcctTO from the jointAcctMap
            for (int i = jointAcctMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanJointAcctTO = (TermLoanJointAcctTO) jointAcctMap.get(objKeySet[j]);
                objTermLoanJointAcctTO.setBorrowNo(borrower_No);
                if (objTermLoanBorrowerTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    objTermLoanJointAcctTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                }
                if (objTermLoanJointAcctTO.getCommand().equals(CommonConstants.STATUS_CREATED)) {
                    objTermLoanJointAcctTO.setStatus(CommonConstants.STATUS_CREATED);
                    logTO.setData(objTermLoanJointAcctTO.toString());
                    logTO.setPrimaryKey(objTermLoanJointAcctTO.getKeyData());
                    logTO.setStatus(CommonConstants.TOSTATUS_INSERT);
                    executeUpdate("insertTermLoanJointAcctTO", objTermLoanJointAcctTO);
                    logDAO.addToLog(logTO);
                } else if (objTermLoanJointAcctTO.getCommand().equals(CommonConstants.STATUS_MODIFIED)) {
                    objTermLoanJointAcctTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    logTO.setData(objTermLoanJointAcctTO.toString());
                    logTO.setPrimaryKey(objTermLoanJointAcctTO.getKeyData());
                    logTO.setStatus(CommonConstants.TOSTATUS_UPDATE);
                    executeUpdate("updateTermLoanJointAcctTO", objTermLoanJointAcctTO);
                    logDAO.addToLog(logTO);
                } else if (objTermLoanJointAcctTO.getCommand().equals(CommonConstants.STATUS_DELETED)) {
                    objTermLoanJointAcctTO.setStatus(CommonConstants.STATUS_DELETED);
                    logTO.setData(objTermLoanJointAcctTO.toString());
                    logTO.setPrimaryKey(objTermLoanJointAcctTO.getKeyData());
                    logTO.setStatus(CommonConstants.TOSTATUS_DELETE);
                    executeUpdate("deleteTermLoanJointAcctTO", objTermLoanJointAcctTO);
                    logDAO.addToLog(logTO);
                }
                objTermLoanJointAcctTO = null;
            }
            keySet = null;
            objKeySet = null;
            objTermLoanJointAcctTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
    //for remove LTD Lien

    private void lienCancel(HashMap lienCancel) throws Exception {
        HashMap hash = new HashMap();
        System.out.println("lienCancelmap#####" + lienCancel);
        HashMap behaveMap = (HashMap) ((List) sqlMap.executeQueryForList("getLoansProduct", lienCancel)).get(0);
        if (CommonUtil.convertObjToStr(behaveMap.get("BEHAVES_LIKE")).equals("LOANS_AGAINST_DEPOSITS")) {
            HashMap lienCancelMap = new HashMap();
            String authStatus = "";

            List lst = sqlMap.executeQueryForList("getDepositLienUnlienLoanTO", lienCancel);
            //System.out.println("getDepositLienUnlienLoanTO##" + lst);
            if (lst != null && lst.size() > 0) {
                DepositLienTO depLienTO = (DepositLienTO) lst.get(0);
                lienCancelMap.put("DEPOSIT_ACT_NUM", depLienTO.getDepositNo());
                lienCancelMap.put("SUBNO", CommonUtil.convertObjToInt(depLienTO.getDepositSubNo()));
                lienCancelMap.put("SHADOWLIEN", depLienTO.getLienAmount());
                lienCancelMap.put("BALANCE", depLienTO.getLienAmount());
                lienCancelMap.put("AMOUNT", depLienTO.getLienAmount());
                lienCancelMap.put("LIENNO", depLienTO.getLienNo());
                lienCancelMap.put("AUTHORIZEDT", curr_dt.clone());//curr_dt);
                lienCancelMap.put("AUTHORIZE_DATE", curr_dt.clone());//curr_dt);
                lienCancelMap.put("COMMAND_STATUS", "CREATED");
                lienCancelMap.put("STATUS", "CREATED");
                DepositLienDAO depositLiendao = new DepositLienDAO();
                lienCancelMap.put(CommonConstants.BRANCH_ID, _branchCode);
                lienCancelMap.put("ACTION", CommonConstants.STATUS_AUTHORIZED);
                lienCancelMap.put("AUTHORIZE_STATUS", CommonConstants.STATUS_AUTHORIZED);
                lienCancelMap.put("LIENAMOUNT", new Double(0));//depLienTO.getLienAmount()
                lienCancelMap.put("STATUS", "UNLIENED");
                lienCancelMap.put("COMMAND", CommonConstants.AUTHORIZEDATA);
                depositLiendao.setCallFromOtherDAO(true);
                System.out.println("lienCancelbefore dao" + lienCancelMap);
                depositLiendao.execute(lienCancelMap);

            }
        }
    }

    private void executeSanctionTabQuery() throws Exception {
        TermLoanSanctionTO objTermLoanSanctionTO;
        Set keySet;
        Object[] objKeySet;
        try {
            if (sanctionMap != null) {
                keySet = sanctionMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                // To retrieve the TermLoanSanctionTO from the sanctionMap
                for (int i = sanctionMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanSanctionTO = (TermLoanSanctionTO) sanctionMap.get(objKeySet[j]);
                    objTermLoanSanctionTO.setBorrowNo(borrower_No);
                    logTO.setData(objTermLoanSanctionTO.toString());
                    logTO.setPrimaryKey(objTermLoanSanctionTO.getKeyData());
                    logTO.setStatus(objTermLoanSanctionTO.getCommand());
                    executeOneTabQueries("TermLoanSanctionTO", objTermLoanSanctionTO);
                    logDAO.addToLog(logTO);
                    objTermLoanSanctionTO = null;
                }
                keySet = null;
                objKeySet = null;
                objTermLoanSanctionTO = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void executeSanctionDetailsTabQuery() throws Exception {
        TermLoanSanctionFacilityTO objTermLoanSanctionFacilityTO;
        Set keySet;
        Object[] objKeySet;
        try {
            if (sanctionFacilityMap != null) {
                keySet = sanctionFacilityMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                // To retrieve the TermLoanSanctionFacilityTO from the sanctionFacilityMap
                for (int i = sanctionFacilityMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanSanctionFacilityTO = (TermLoanSanctionFacilityTO) sanctionFacilityMap.get(objKeySet[j]);
                    objTermLoanSanctionFacilityTO.setBorrowNo(borrower_No);
                    objTermLoanSanctionFacilityTO.setProductId(ProductId);
                    if (objTermLoanSanctionFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        delRefMap.put(objTermLoanSanctionFacilityTO.getSanctionNo() + objTermLoanSanctionFacilityTO.getSlNo(), "");
                    }
                    logTO.setData(objTermLoanSanctionFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanSanctionFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanSanctionFacilityTO.getCommand());
                    executeOneTabQueries("TermLoanSanctionFacilityTO", objTermLoanSanctionFacilityTO);
                    logDAO.addToLog(logTO);
                    objTermLoanSanctionFacilityTO = null;
                }
                System.out.println("#$#$# inside executeSanctionDetailsTabQuery() delRefMap : " + delRefMap);
                keySet = null;
                objKeySet = null;
                objTermLoanSanctionFacilityTO = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void executeFacilityTabQuery() throws Exception {
        TermLoanFacilityTO objTermLoanFacilityTO;
        Set keySet;
        Object[] objKeySet;
        try {
            if (facilityMap != null) {
                keySet = facilityMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                // To retrieve the TermLoanFacilityTO from the facilityMap
                for (int i = facilityMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanFacilityTO = (TermLoanFacilityTO) facilityMap.get(objKeySet[j]);
                    objTermLoanFacilityTO.setBorrowNo(borrower_No);
                    if (delRefMap.containsKey(objTermLoanFacilityTO.getSanctionNo() + objTermLoanFacilityTO.getSlNo())) {
                        delRefMap.put(objTermLoanFacilityTO.getAcctNum(), "");
                        objTermLoanFacilityTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    }
                    if (objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                        prod_id = objTermLoanFacilityTO.getProdId();
                        acct_No = getAccount_No();
                        objTermLoanFacilityTO.setAcctNum(acct_No);
                        objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_CREATED);
                        Date tempDt = (Date) curr_dt.clone();
                        Date lastIntCalcDt = DateUtil.addDays(objTermLoanFacilityTO.getAccOpenDt(), -1);
                        tempDt.setDate(lastIntCalcDt.getDate());
                        tempDt.setMonth(lastIntCalcDt.getMonth());
                        tempDt.setYear(lastIntCalcDt.getYear());
                        objTermLoanFacilityTO.setLastIntCalcDt(tempDt);
                        logTO.setData(objTermLoanFacilityTO.toString());
                        logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                        logTO.setStatus(objTermLoanFacilityTO.getCommand());
                        HashMap chckMap = new HashMap();
                        chckMap.put("PROD_ID",objTermLoanFacilityTO.getProdId());
                        List intCalcMethodLst = (List) sqlMap.executeQueryForList("checkIfIntCalcPeriodSlabWiseOrNot", chckMap);
                        if (intCalcMethodLst != null && intCalcMethodLst.size() > 0) {
                            HashMap intCalcMethodMap = (HashMap) intCalcMethodLst.get(0);
                            if (intCalcMethodMap.containsKey("IS_INT_PERIOD_SLAB_CALC") && intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC") != null && !"".equals(intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC"))) {
                                if (CommonUtil.convertObjToStr(intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC")).equalsIgnoreCase("Y")) {
                                    objTermLoanFacilityTO.setIntGetFrom("PROD");
                                }
                            }
                        }
                        executeUpdate("insertTermLoanFacilityTO", objTermLoanFacilityTO);
                        insertCustomerHistory(objTermLoanFacilityTO);
//                        logDAO.addToLog(logTO);
                    } else if (objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                        //                    setAccount_No(objTermLoanFacilityTO.getAcctNum());
//                        acct_No = objTermLoanFacilityTO.getAcctNum();
                        objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        if (old_prod_id != null && old_prod_id.length() > 0) {
                           objTermLoanFacilityTO.setProdId(old_prod_id);
                        }
                        logTO.setData(objTermLoanFacilityTO.toString());
                        logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                        logTO.setStatus(objTermLoanFacilityTO.getCommand());
                        System.out.println("objTermLoanFacilityTO#####" + objTermLoanFacilityTO);
                        if (objTermLoanFacilityTO.getAuthorizeStatus1() != null && objTermLoanFacilityTO.getAuthorizeStatus1().length() > 0) {
                            executeUpdate("updateTermLoanFacilityTOMaterializedView", objTermLoanFacilityTO);//dont want to updte available balance
                        } else {
                              //Commented for KD-3846 - Gold Loan Renewal Issue - INT_GET_FROM changes from ACT to PROD
//                            HashMap chckMap = new HashMap();
//                            chckMap.put("PROD_ID", objTermLoanFacilityTO.getProdId());
//                            List intCalcMethodLst = (List) sqlMap.executeQueryForList("checkIfIntCalcPeriodSlabWiseOrNot", chckMap);
//                            if (intCalcMethodLst != null && intCalcMethodLst.size() > 0) {
//                                HashMap intCalcMethodMap = (HashMap) intCalcMethodLst.get(0);
//                                if (intCalcMethodMap.containsKey("IS_INT_PERIOD_SLAB_CALC") && intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC") != null && !"".equals(intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC"))) {
//                                    if (CommonUtil.convertObjToStr(intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC")).equalsIgnoreCase("Y")) {
//                                        objTermLoanFacilityTO.setIntGetFrom("PROD");
//                                    }
//                                }
//                            }
                            if(objTermLoanFacilityTO.getAcctStatus().equalsIgnoreCase("RENEWAL")){
                                objTermLoanFacilityTO.setAcctStatus("NEW"); 
                           }
                            executeUpdate("updateTermLoanFacilityTO", objTermLoanFacilityTO);
                        }
                        insertCustomerHistory(objTermLoanFacilityTO);
//                        logDAO.addToLog(logTO);
                    } else if (objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        //                    setAccount_No(objTermLoanFacilityTO.getAcctNum());
                        objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                        logTO.setData(objTermLoanFacilityTO.toString());
                        logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                        logTO.setStatus(objTermLoanFacilityTO.getCommand());
                        executeUpdate("deleteTermLoanFacilityTO", objTermLoanFacilityTO);
//                        logDAO.addToLog(logTO);
                        HashMap lienCancelMap = new HashMap();
                        lienCancelMap.put("LOAN_NO", objTermLoanFacilityTO.getAcctNum());
                        lienCancelMap.put("PROD_ID", objTermLoanFacilityTO.getProdId());
                        lienCancel(lienCancelMap);
                        insertCustomerHistory(objTermLoanFacilityTO);

                    }
                    if (objSMSSubscriptionTO != null) {
                        objSMSSubscriptionTO.setActNum(acct_No);
                        updateSMSSubscription();
                    }
                    // To make Lien Marking for LTD loans

                    if (depositCustDetMap != null && objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                        DepositLienTO obj = new DepositLienTO();
                        HashMap prodMap = new HashMap();
                        String depositLienRoundOff = "";
                        prodMap.put("prodId", objTermLoanFacilityTO.getProdId());
                        List lst = sqlMap.executeQueryForList("TermLoan.getProdHead", prodMap);
                        if (lst.size() > 0) {
                            prodMap = (HashMap) lst.get(0);
                            obj.setLienAcHd(CommonUtil.convertObjToStr(prodMap.get("AC_HEAD")));
                            depositLienRoundOff = CommonUtil.convertObjToStr(prodMap.get("DEPOSIT_ROUNDOFF"));
                        }
                        obj.setLienAcNo(objTermLoanFacilityTO.getAcctNum());
                        double eligibleMargin = CommonUtil.convertObjToDouble(prodMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
                        prodMap = null;
                        double availBal = objTermLoanFacilityTO.getAvailableBalance().doubleValue();
                        availBal = (availBal / eligibleMargin * 100.0);  //85.0
                        availBal = roundOffDepositLien(availBal, depositLienRoundOff);
                        obj.setLienAmount(new Double(availBal));
                        obj.setLienDt(curr_dt);//curr_dt);
                        obj.setRemarks("Lien for LTD");
                        obj.setCreditLienAcct("NA");
                        obj.setDepositNo(CommonUtil.convertObjToStr(depositCustDetMap.get("DEPOSIT_NO")));
                        obj.setDepositSubNo(CommonUtil.convertObjToInt(depositCustDetMap.get("DEPOSIT_SUB_NO")));
                        obj.setLienProdId(objTermLoanFacilityTO.getProdId());
                        obj.setLienNo("-");
                        obj.setStatus(objTermLoanFacilityTO.getStatus());
                        obj.setStatusBy(objTermLoanFacilityTO.getStatusBy());
                        obj.setStatusDt(objTermLoanFacilityTO.getStatusDt());

                        ArrayList lienTOs = new ArrayList();
                        lienTOs.add(obj);
                        HashMap objHashMap = new HashMap();
                        objHashMap.put("lienTOs", lienTOs);
                        objHashMap.put("SHADOWLIEN", new Double(availBal));
                        objHashMap.put(CommonConstants.BRANCH_ID, _branchCode);
                        objHashMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                        System.out.println("### objHashMap : " + objHashMap);
                        DepositLienDAO depLienDAO = new DepositLienDAO();
                        depLienDAO.setCallFromOtherDAO(true);
                        objHashMap = depLienDAO.execute(objHashMap);
                        if (objHashMap != null) {
                            lienNo = CommonUtil.convertObjToStr(objHashMap.get("LIENNO"));
                        }
                        obj = null;
                        lienTOs = null;
                        depLienDAO = null;
                        objHashMap = null;
                    } else if (loanType.equals("LTD") && objTermLoanFacilityTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                        ArrayList lienTOs = new ArrayList();
                        HashMap lienCancelMap = new HashMap();
                        lienCancelMap.put("LOAN_NO", objTermLoanFacilityTO.getAcctNum());
                        HashMap prodMap = new HashMap();
                        String depositLienRoundOff = "";
                        double eligibleMargin = 0;

                        prodMap.put("prodId", objTermLoanFacilityTO.getProdId());
                        List lst = sqlMap.executeQueryForList("TermLoan.getProdHead", prodMap);
                        if (lst.size() > 0) {
                            prodMap = (HashMap) lst.get(0);
                            //                            obj.setLienAcHd(CommonUtil.convertObjToStr(prodMap.get("AC_HEAD")));
                            depositLienRoundOff = CommonUtil.convertObjToStr(prodMap.get("DEPOSIT_ROUNDOFF"));
                            eligibleMargin = CommonUtil.convertObjToDouble(prodMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
                        }

                        List list = sqlMap.executeQueryForList("getDepositLienUnlienLoanTO", lienCancelMap);
                       // System.out.println(additionalSanMap + "getdepositliento#####" + list);
                        if (list != null && list.size() > 0 && additionalSanMap.isEmpty()) {

                            double availBal = objTermLoanFacilityTO.getAvailableBalance().doubleValue();
                            availBal = (availBal / eligibleMargin * 100.0);  //85.0
                            availBal = roundOffDepositLien(availBal, depositLienRoundOff);
                            DepositLienTO depLienTO = (DepositLienTO) list.get(0);
                            depLienTO.setLienAmount(new Double(availBal));
                            depLienTO.setStatus("MODIFIED");
                            depLienTO.setStatusDt(curr_dt);
                            depLienTO.setRemarks("Lien for LTD");
                            depLienTO.setAuthorizeBy(depLienTO.getAuthorizeBy());
                            depLienTO.setAuthorizeDt(depLienTO.getAuthorizeDt());
                            depLienTO.setAuthorizeStatus(depLienTO.getAuthorizeStatus());
                            lienTOs.add(depLienTO);
                            DepositLienDAO depositLiendao = new DepositLienDAO();
                            lienCancelMap.put(CommonConstants.BRANCH_ID, _branchCode);
                            lienCancelMap.put("COMMAND", CommonConstants.TOSTATUS_UPDATE);
                            lienCancelMap.put("lienTOs", lienTOs);
                            lienCancelMap.put("SHADOWLIEN", depLienTO.getLienAmount());
                            depositLiendao.setCallFromOtherDAO(true);
                            System.out.println("lienCancelbefore UPDATE ******" + lienCancelMap);
                            depositLiendao.execute(lienCancelMap);
                        }

                    }
                    objTermLoanFacilityTO = null;
                }
                System.out.println("#$#$# inside executeFacilityTabQuery() delRefMap : " + delRefMap);
                depositCustDetMap = null;
                keySet = null;
                objKeySet = null;
                objTermLoanFacilityTO = null;
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
            //            throw e;
        }
    }

    //--- Inserts the data Record in SMS_SUBSCRIPTION Table
    private void updateSMSSubscription() throws Exception {
//        objSMSSubscriptionTO.setStatusBy(objTermLoanFacilityTO.getStatusBy());
//        objSMSSubscriptionTO.setStatusDt(objTermLoanFacilityTO.getStatusDt());
        objSMSSubscriptionTO.setCustId(objTermLoanBorrowerTO.getCustId());
        if (CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()) != null && CommonUtil.convertObjToStr(objSMSSubscriptionTO.getMobileNo()).length() > 0 && objSMSSubscriptionTO.getSubscriptionDt() != null) {
            List list = (List) sqlMap.executeQueryForList("getRecordExistorNotinSMSSub", objSMSSubscriptionTO);
            if (list != null && list.size() > 0) {
                sqlMap.executeUpdate("updateSMSSubscriptionMap", objSMSSubscriptionTO);
            } else {
                sqlMap.executeUpdate("insertSMSSubscriptionMap", objSMSSubscriptionTO);
            }
        }
    }

    //we should update customer history
    private void insertCustomerHistory(TermLoanFacilityTO termLoanFacilityTO) throws Exception {
        // Inserting into Customer History Table
        HashMap map = new HashMap();
        String behaves_like = null;
        map.put("PROD_ID", termLoanFacilityTO.getProdId());
        List lst = sqlMap.executeQueryForList("getLoansProduct", map);
        if (lst != null && lst.size() > 0) {
            map = (HashMap) lst.get(0);
            behaves_like = CommonUtil.convertObjToStr(map.get("BEHAVES_LIKE"));
        }
        CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO();
        objCustomerHistoryTO.setAcctNo(termLoanFacilityTO.getAcctNum());
        objCustomerHistoryTO.setCustId(objTermLoanBorrowerTO.getCustId());
        if (behaves_like.equals("OD") || behaves_like.equals("CC")) {
            objCustomerHistoryTO.setProductType("AD");
        } else {
            objCustomerHistoryTO.setProductType("TL");
        }
        objCustomerHistoryTO.setProdId(termLoanFacilityTO.getProdId());
        objCustomerHistoryTO.setRelationship("ACCT_HOLDER");
        map = setFromToDate(termLoanFacilityTO);
        if (map != null && map.size() > 0) {
            objCustomerHistoryTO.setFromDt((Date) map.get("FROM_DT"));
            objCustomerHistoryTO.setToDt((Date) map.get("TO_DT"));

        }
        objCustomerHistoryTO.setCommand(termLoanFacilityTO.getCommand());
        CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
        objCustomerHistoryTO = null;

    }

    //        private void executeDisbursementTabQuery()throws Exception{
    //        Set set=DisbursementMap.keySet();
    //        TermLoanDisburstTO agriSubLimitTO=new TermLoanDisburstTO();
    //        Object objKeySet[]=(Object[])set.toArray();
    //        try{
    //            System.out.println("DisbursementMap$$$$"+DisbursementMap);
    //            for(int i=0;i<DisbursementMap.size();i++){
    //                agriSubLimitTO=(TermLoanDisburstTO)DisbursementMap.get(objKeySet[i]);
    //                agriSubLimitTO.setAcctNum(acct_No);
    //                if(agriSubLimitTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
    //                    logTO.setData(agriSubLimitTO.toString());
    //                    //                    logTO.setPrimaryKey(agriSubLimitTO.getKeyData());
    //                    logTO.setStatus(CommonConstants.TOSTATUS_INSERT);
    //                    sqlMap.executeUpdate("insertTermLoanDisburstTO", agriSubLimitTO);
    //                    logDAO.addToLog(logTO);
    //                }else if(agriSubLimitTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)){
    //                    logTO.setData(agriSubLimitTO.toString());
    //                    //                    logTO.setPrimaryKey(objTermLoanJointAcctTO.getKeyData());
    //                    logTO.setStatus(CommonConstants.TOSTATUS_UPDATE);
    //                    sqlMap.executeUpdate("updateTermLoanDisburstTO", agriSubLimitTO);
    //                    logDAO.addToLog(logTO);
    //                }else if (agriSubLimitTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
    //                    logTO.setData(agriSubLimitTO.toString());
    //                    //                    logTO.setPrimaryKey(agriSubLimitTO.getKeyData());
    //                    logTO.setStatus(CommonConstants.TOSTATUS_DELETE);
    //                    sqlMap.executeUpdate("updateTermLoanDisburstTO", agriSubLimitTO);
    //                    logDAO.addToLog(logTO);
    //                }
    //            }
    //        }catch(Exception e){
    //            sqlMap.rollbackTransaction();
    //            e.printStackTrace();
    //            throw new TransRollbackException(e);
    //        }
    //    }
    private void executeExtenFacilityDetailsTabQuery() throws Exception {
        Set set = termLoanExtenFacilityMap.keySet();
        TermLoanExtenFacilityDetailsTO agriSubLimitTO = new TermLoanExtenFacilityDetailsTO();
        Object objKeySet[] = (Object[]) set.toArray();
        try {
            if (set.size() > 0) {
                System.out.println("termLoanExtenFacilityMap#####" + termLoanExtenFacilityMap);
                agriSubLimitTO = (TermLoanExtenFacilityDetailsTO) termLoanExtenFacilityMap.get("TermLoanExtenFacilityDetailsTO");
                if (agriSubLimitTO != null) {
                    if (agriSubLimitTO.getCommand() == null) {
                        sqlMap.executeUpdate("insertTermLoanFacilityExtnTO", agriSubLimitTO);
                    }
                    if (agriSubLimitTO.getCommand() != null && agriSubLimitTO.getCommand().equals("UPDATE")) {
                        sqlMap.executeUpdate("updateTermLoanFacilityExtnTO", agriSubLimitTO);
                    }
                }
            }
            //            for(int i=0;i<DisbursementMap.size();i++){
            //                agriSubLimitTO=(TermLoanDisburstTO)DisbursementMap.get(objKeySet[i]);
            //                if(agriSubLimitTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
            //                    logTO.setData(agriSubLimitTO.toString());
            //                    //                    logTO.setPrimaryKey(agriSubLimitTO.getKeyData());
            //                    logTO.setStatus(CommonConstants.TOSTATUS_INSERT);
            //                    sqlMap.executeUpdate("insertTermLoanDisburstTO", agriSubLimitTO);
            //                    logDAO.addToLog(logTO);
            //                }else if(agriSubLimitTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)){
            //                    logTO.setData(agriSubLimitTO.toString());
            //                    //                    logTO.setPrimaryKey(objTermLoanJointAcctTO.getKeyData());
            //                    logTO.setStatus(CommonConstants.TOSTATUS_UPDATE);
            //                    sqlMap.executeUpdate("updateTermLoanDisburstTO", agriSubLimitTO);
            //                    logDAO.addToLog(logTO);
            //                }else if (agriSubLimitTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
            //                    logTO.setData(agriSubLimitTO.toString());
            //                    //                    logTO.setPrimaryKey(agriSubLimitTO.getKeyData());
            //                    logTO.setStatus(CommonConstants.TOSTATUS_DELETE);
            //                    sqlMap.executeUpdate("updateTermLoanDisburstTO", agriSubLimitTO);
            //                    logDAO.addToLog(logTO);
            //                }
            //            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    //    //we should update customer history
    //    private void insertGuarantorCustomerHistory(TermLoanGuarantorTO termLoanGuarantorTO) throws Exception{
    //                    // Inserting into Customer History Table
    //        HashMap map=new HashMap();
    //        String behaves_like=null;
    //        map.put("PROD_ID",termLoanFacilityTO.getProdId());
    //        List lst =sqlMap.executeQueryForList("getLoansProduct",map);
    //        if(lst !=null &&lst.size()>0){
    //            map=(HashMap)lst.get(0);
    //            behaves_like=CommonUtil.convertObjToStr(map.get("BEHAVES_LIKE"));
    //        }
    //            CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO();
    //            objCustomerHistoryTO.setAcctNo(termLoanGuarantorTO.getAcctNum());
    //            objCustomerHistoryTO.setCustId(termLoanGuarantorTO.getCustId());
    //            if(behaves_like.equals("OD") || behaves_like.equals("CC"))
    //                objCustomerHistoryTO.setProductType("AD");
    //            else
    //                objCustomerHistoryTO.setProductType("TL");
    ////            objCustomerHistoryTO.setProdId(termLoanFacilityTO.getProdId());
    //            objCustomerHistoryTO.setRelationship(AcctStatusConstants.GUARANTOR);
    ////             map=setFromToDate(termLoanFacilityTO);
    ////            if(map !=null &&  map.size()>0){
    ////            objCustomerHistoryTO.setFromDt((Date)map.get("FROM_DT"));
    ////            objCustomerHistoryTO.setToDt((Date)map.get("TO_DT"));
    ////
    ////            }
    //            objCustomerHistoryTO.setCommand(termLoanGuarantorTO.getCommand());
    //            CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
    //            objCustomerHistoryTO = null;
    //
    //    }
    //
    //
    //        //we should update customer history
    //    private void insertSecurityCustomerHistory(TermLoanFacilityTO termLoanFacilityTO) throws Exception{
    //                    // Inserting into Customer History Table
    //        HashMap map=new HashMap();
    //        String behaves_like=null;
    //        map.put("PROD_ID",termLoanFacilityTO.getProdId());
    //        List lst =sqlMap.executeQueryForList("getLoansProduct",map);
    //        if(lst !=null &&lst.size()>0){
    //            map=(HashMap)lst.get(0);
    //            behaves_like=CommonUtil.convertObjToStr(map.get("BEHAVES_LIKE"));
    //        }
    //            CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO();
    //            objCustomerHistoryTO.setAcctNo(termLoanFacilityTO.getAcctNum());
    //            objCustomerHistoryTO.setCustId(objTermLoanBorrowerTO.getCustId());
    //            if(behaves_like.equals("AOD") || behaves_like.equals("CC"))
    //                objCustomerHistoryTO.setProductType("AD");
    //            else
    //                objCustomerHistoryTO.setProductType("TL");
    //            objCustomerHistoryTO.setProdId(termLoanFacilityTO.getProdId());
    //            objCustomerHistoryTO.setRelationship("ACCT_HOLDER");
    //             map=setFromToDate(termLoanFacilityTO);
    //            if(map !=null &&  map.size()>0){
    //            objCustomerHistoryTO.setFromDt((Date)map.get("FROM_DT"));
    //            objCustomerHistoryTO.setToDt((Date)map.get("TO_DT"));
    //
    //            }
    //            objCustomerHistoryTO.setCommand(termLoanFacilityTO.getCommand());
    //            CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
    //            objCustomerHistoryTO = null;
    //
    //    }
    private HashMap setFromToDate(TermLoanFacilityTO termLoanFacilityTO) {
        HashMap resultMap = new HashMap();
        if (sanctionFacilityMap != null) {
            Set set = sanctionFacilityMap.keySet();
            Object objKeySet[] = (Object[]) set.toArray();
            if (sanctionFacilityMap != null) {
                for (int i = 0; i < sanctionFacilityMap.size(); i++) {
                    TermLoanSanctionFacilityTO termLoanSanctionFacilityTO = (TermLoanSanctionFacilityTO) sanctionFacilityMap.get(objKeySet[i]);
                    if (termLoanFacilityTO.getSanctionNo() == termLoanSanctionFacilityTO.getSanctionNo()
                            && (termLoanSanctionFacilityTO.getSlNo() == termLoanFacilityTO.getSlNo())) {
                        resultMap.put("FROM_DT", termLoanSanctionFacilityTO.getFromDt());
                        resultMap.put("TO_DT", termLoanSanctionFacilityTO.getToDt());
                        return resultMap;
                    }
                }
            }
        }
        return resultMap;
    }

    private double roundOffDepositLien(double lienAmt, String roundOff) throws Exception {
        //        roundOff=CommonUtil.convertObjToStr(resultMap.get("DEPOSIT_ROUNDOFF"));
        long roundOffValue = 0;
        double finalLien = 0.0;
        //        long finalLien=0;
        //        long longLien=(long)lienAmt;

        //            if(roundOff.length()>0){
        //                if(roundOff .equals("NEAREST_TENS")){
        //                    roundOffValue=10;
        //                }
        //                else   if(roundOff .equals("NEAREST_HUNDREDS")){
        //                    roundOffValue=100;
        //                }
        //                else  if(roundOff .equals("NEAREST_VALUE")){
        roundOffValue = 1;
        //                } else  if(roundOff .equals("NO_ROUND_OFF")){
        //                    return finalLien;
        //                }
        //            }

        if (roundOff.length() == 0) {
            sqlMap.rollbackTransaction();
            throw new TTException("Eligible Deposit Rounding of  Parameter Value Not Set In product Level");
        }

        //         long lienAmt=(long)(enterAmt/eligibleMargin);
        Rounding rd = new Rounding();
        finalLien = rd.getNearestHigher(lienAmt, roundOffValue);
        return finalLien;
    }

    private String getEligibleLoanAmtKey(String strCustID, String strSecurityNo) throws Exception {
        return strCustID + "#" + strSecurityNo;
    }

    private void executeRepaymentQuery() throws Exception {
        TermLoanRepaymentTO objTermLoanRepaymentTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = repaymentMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            HashMap repayDetailsMap;
            HashMap updateMap = new HashMap();
            // To retrieve the TermLoanRepaymentTO from the repaymentMap
            for (int i = repaymentMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanRepaymentTO = (TermLoanRepaymentTO) repaymentMap.get(objKeySet[j]);
                if (CommonUtil.convertObjToStr(objTermLoanRepaymentTO.getAcctNum()).length() < 1) {
                    objTermLoanRepaymentTO.setAcctNum(acct_No);
                }
                if (delRefMap.containsKey(objTermLoanRepaymentTO.getAcctNum())) {
                    objTermLoanRepaymentTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    objTermLoanRepaymentTO.setStatus(CommonConstants.STATUS_DELETED);
                    delRefMap.put("DELACTNUM", objTermLoanRepaymentTO.getAcctNum());
                    delRefMap.put("DELSCHNUM", objTermLoanRepaymentTO.getScheduleNo());
                }

                updateMap.put("ACCT_NUM", objTermLoanRepaymentTO.getAcctNum());
                updateMap.put("CURR_DT", curr_dt);
                if (objTermLoanRepaymentTO.getRepayActive().equals("N")) {
                    updateMap.put("SCHEDULE_ID", objTermLoanRepaymentTO.getScheduleNo());
                    System.out.println("updateMap#####" + updateMap);
                    sqlMap.executeUpdate("updateLoansInstallmentTL", updateMap);
                }
                logTO.setData(objTermLoanRepaymentTO.toString());
                logTO.setPrimaryKey(objTermLoanRepaymentTO.getKeyData());
                logTO.setStatus(objTermLoanRepaymentTO.getCommand());
                executeOneTabQueries("TermLoanRepaymentTO", objTermLoanRepaymentTO);
                logDAO.addToLog(logTO);
                System.out.println("repaymentmap" + repaymentMap);
                //                if (objTermLoanRepaymentTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
                //                    repayDetailsMap = new HashMap();
                //                    repayDetailsMap.put("DISBURSEMENT_ID", objTermLoanRepaymentTO.getDisbursementId());
                //                    repayDetailsMap.put("ACT_NUM", objTermLoanRepaymentTO.getAcctNum());
                //                    logTO.setPrimaryKey(repayDetailsMap.toString());
                //                    repayDetailsMap.put("REPAYMENT_SCHEDULE_NO", objTermLoanRepaymentTO.getScheduleNo());
                //                    logTO.setData(repayDetailsMap.toString());
                //                    logTO.setStatus(CommonConstants.TOSTATUS_UPDATE);
                //                    sqlMap.executeUpdate("updateRepayScheduleNoInLoansDisbursement", repayDetailsMap);
                //                }
                //                for (i=0; i<installmentAllMap.size(); i++) {
                //                    installmentSingleMap = (HashMap) installmentAllMap.get(String.valueOf(i));
                //                    System.out.println("allrecords"+installmentSingleMap);
                //                    installmentSingleMap.put("ACT_NUM", objTermLoanRepaymentTO.getAcctNum());
                //                    installmentSingleMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                //                    System.out.println("singlerecor"+installmentSingleMap);
                //                    executeUpdate("TermRepaymentInstallmentAllTO", installmentSingleMap);
                //                    installmentSingleMap = null;
                //                }
                installmentAllMap = null;
                objTermLoanRepaymentTO = null;
                repayDetailsMap = null;
            }
            keySet = null;
            objKeySet = null;
            objTermLoanRepaymentTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;

        }

    }

    private void executeInstallmentTabQuery() throws Exception {
        TermLoanInstallmentTO objTermLoanInstallmentTO;
        try {
            if (installmentMap != null && installmentMap.size() > 0) {
                Set keySet;
                Object[] objKeySet;
                keySet = installmentMap.keySet();
                objKeySet = (Object[]) keySet.toArray();
                HashMap updateMap = new HashMap();
                // To retrieve the TermLoanInstallmentTO from the installmentMap
                System.out.println("jitinstallmentMap" + installmentMap);
                System.out.println("jitdelRefMap" + delRefMap);
                //System.out.println("objTermLoanInstallmentTO"+objTermLoanInstallmentTO);
                for (int i = installmentMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                    objTermLoanInstallmentTO = (TermLoanInstallmentTO) installmentMap.get(objKeySet[j]);
                    if (CommonUtil.convertObjToStr(objTermLoanInstallmentTO.getAcctNum()).length() < 1) {
                        objTermLoanInstallmentTO.setAcctNum(acct_No);
                    }
                    if (delRefMap.containsKey(objTermLoanInstallmentTO.getAcctNum())) {
                        objTermLoanInstallmentTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                        objTermLoanInstallmentTO.setStatus(CommonConstants.STATUS_DELETED);
                    }
                    logTO.setData(objTermLoanInstallmentTO.toString());
                    logTO.setPrimaryKey(objTermLoanInstallmentTO.getKeyData());
                    logTO.setStatus(objTermLoanInstallmentTO.getCommand());
//                    System.out.println("objTermLoanOtherDetailsTO"+objTermLoanOtherDetailsTO);
                    executeOneTabQueries("TermLoanInstallmentTO", objTermLoanInstallmentTO);



                    logDAO.addToLog(logTO);
                    objTermLoanInstallmentTO = null;
                }

                keySet = null;
                objKeySet = null;
            } else {
                if (delRefMap != null && delRefMap.size() > 0 && delRefMap.containsKey("DELACTNUM")) {
                    List lst = sqlMap.executeQueryForList("getSelectTermLoanInstallmentTO", delRefMap);
                    if (lst != null && lst.size() > 0) {
                        for (int i = 0; i < lst.size(); i++) {
                            objTermLoanInstallmentTO = (TermLoanInstallmentTO) lst.get(i);
                            objTermLoanInstallmentTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                            objTermLoanInstallmentTO.setStatus(CommonConstants.STATUS_DELETED);
                            logTO.setData(objTermLoanInstallmentTO.toString());
                            logTO.setPrimaryKey(objTermLoanInstallmentTO.getKeyData());
                            logTO.setStatus(objTermLoanInstallmentTO.getCommand());
//                            System.out.println("objTermLoanOtherDetailsTO"+objTermLoanOtherDetailsTO);
                            executeOneTabQueries("TermLoanInstallmentTO", objTermLoanInstallmentTO);
                            logDAO.addToLog(logTO);
                            objTermLoanInstallmentTO = null;
                        }
                    }
                    lst = null;
                }
            }
            objTermLoanInstallmentTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    //    private void executeInstallmentMultIntTabQuery() throws Exception{
    //        TermLoanInstallMultIntTO objTermLoanInstallMultIntTO;
    //        Set keySetMultInt;
    //        Object[] objKeySetMultInt;
    //        try{
    //            keySetMultInt =  installmentMultIntMap.keySet();
    //            objKeySetMultInt = (Object[]) keySetMultInt.toArray();
    //            // To retrieve the TermLoanInstallMultIntTO from the installmentMultIntMap
    //            for (int i = installmentMultIntMap.size() - 1, j = 0;i >= 0;--i,++j){
    //                objTermLoanInstallMultIntTO = (TermLoanInstallMultIntTO) installmentMultIntMap.get(objKeySetMultInt[j]);
    //                //                if (delRefMap.containsKey(objTermLoanInstallMultIntTO.getAcctNum())) {
    //                //                    objTermLoanInstallMultIntTO.setCommand(CommonConstants.TOSTATUS_DELETE);
    //                //                    objTermLoanInstallMultIntTO.setStatus(CommonConstants.STATUS_DELETED);
    //                //                }
    //                if (CommonUtil.convertObjToStr(objTermLoanInstallMultIntTO.getAcctNum()).length()<1) {
    //                    objTermLoanInstallMultIntTO.setAcctNum(acct_No);
    //                }
    //                logTO.setData(objTermLoanInstallMultIntTO.toString());
    //                logTO.setPrimaryKey(objTermLoanInstallMultIntTO.getKeyData());
    //                logTO.setStatus(objTermLoanInstallMultIntTO.getCommand());
    //                executeOneTabQueries("TermLoanInstallMultIntTO", objTermLoanInstallMultIntTO);
    //                logDAO.addToLog(logTO);
    //                objTermLoanInstallMultIntTO = null;
    //            }
    //            objTermLoanInstallMultIntTO = null;
    //            keySetMultInt = null;
    //            objKeySetMultInt = null;
    //        } catch (Exception e) {
    //            sqlMap.rollbackTransaction();
    //            e.printStackTrace();
    //            throw e;
    //        }
    //    }
//    private void executeGuarantorTabQuery() throws Exception{
//        TermLoanGuarantorTO objTermLoanGuarantorTO;
//        Set keySet =  guarantorMap.keySet();
//        Object[] objKeySet = (Object[]) keySet.toArray();
//        try{
//            keySet =  guarantorMap.keySet();
//            objKeySet = (Object[]) keySet.toArray();
//            
//            // To retrieve the TermLoanGuarantorTO from the guarantorMap
//            for (int i = guarantorMap.size() - 1, j = 0;i >= 0;--i,++j){
//                objTermLoanGuarantorTO = (TermLoanGuarantorTO) guarantorMap.get(objKeySet[j]);
//                if (CommonUtil.convertObjToStr(objTermLoanGuarantorTO.getAcctNum()).length()<1) {
//                    objTermLoanGuarantorTO.setAcctNum(acct_No);
//                }
//                if (delRefMap.containsKey(objTermLoanGuarantorTO.getAcctNum())) {
//                    objTermLoanGuarantorTO.setCommand(CommonConstants.TOSTATUS_DELETE);
//                    objTermLoanGuarantorTO.setStatus(CommonConstants.STATUS_DELETED);
//                }
//                logTO.setData(objTermLoanGuarantorTO.toString());
//                logTO.setPrimaryKey(objTermLoanGuarantorTO.getKeyData());
//                logTO.setStatus(objTermLoanGuarantorTO.getCommand());
//                executeOneTabQueries("TermLoanGuarantorTO", objTermLoanGuarantorTO);
//                logDAO.addToLog(logTO);
//                objTermLoanGuarantorTO = null;
//            }
//            objTermLoanGuarantorTO = null;
//            keySet = null;
//            objKeySet = null;
//        } catch (Exception e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw e;
//        }
//    }
//    private void executeInsititGuarantorTabQuery() throws Exception{
//        TermLoanInstitGuarantorTO objTermLoanInstitGuarantorTO;
//        Set keySet =  guaranInstitMap.keySet();
//        Object[] objKeySet = (Object[]) keySet.toArray();
//        try{
//            keySet =  guaranInstitMap.keySet();
//            objKeySet = (Object[]) keySet.toArray();
//            
//            // To retrieve the TermLoanGuarantorTO from the guarantorMap
//            for (int i = guaranInstitMap.size() - 1, j = 0;i >= 0;--i,++j){
//                objTermLoanInstitGuarantorTO = (TermLoanInstitGuarantorTO) guaranInstitMap.get(objKeySet[j]);
//                if (delRefMap.containsKey(objTermLoanInstitGuarantorTO.getAcctNum())) {
//                    objTermLoanInstitGuarantorTO.setCommand(CommonConstants.TOSTATUS_DELETE);
//                    objTermLoanInstitGuarantorTO.setStatus(CommonConstants.STATUS_DELETED);
//                }
//                logTO.setData(objTermLoanInstitGuarantorTO.toString());
//                logTO.setPrimaryKey(objTermLoanInstitGuarantorTO.getKeyData());
//                logTO.setStatus(objTermLoanInstitGuarantorTO.getCommand());
//                executeOneTabQueries("TermLoanInstitGuarantorTO", objTermLoanInstitGuarantorTO);
//                logDAO.addToLog(logTO);
//                objTermLoanInstitGuarantorTO = null;
//            }
//            objTermLoanInstitGuarantorTO = null;
//            keySet = null;
//            objKeySet = null;
//        } catch (Exception e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw e;
//        }
//    }
    private void executeDocumentTabQuery() throws Exception {
        TermLoanDocumentTO objTermLoanDocumentTO;
        Set keySet = documentMap.keySet();
        Object[] objKeySet = (Object[]) keySet.toArray();
        try {
            keySet = documentMap.keySet();
            objKeySet = (Object[]) keySet.toArray();

            // To retrieve the TermLoanDocumentTO from the documentMap
            for (int i = documentMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanDocumentTO = (TermLoanDocumentTO) documentMap.get(objKeySet[j]);
                //                if (delRefMap.containsKey(objTermLoanDocumentTO.getAcctNo())) {
                //                    objTermLoanDocumentTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                //                    objTermLoanDocumentTO.setStatus(CommonConstants.STATUS_DELETED);
                //                }
                if (CommonUtil.convertObjToStr(objTermLoanDocumentTO.getAcctNo()).length() < 1) {
                    objTermLoanDocumentTO.setAcctNo(acct_No);
                }
                logTO.setData(objTermLoanDocumentTO.toString());
                logTO.setPrimaryKey(objTermLoanDocumentTO.getKeyData());
                logTO.setStatus(objTermLoanDocumentTO.getCommand());
                executeOneTabQueries("TermLoanDocumentTO", objTermLoanDocumentTO);
                logDAO.addToLog(logTO);
                objTermLoanDocumentTO = null;
            }
            objTermLoanDocumentTO = null;
            keySet = null;
            objKeySet = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void executeInterestTabQuery() throws Exception {
        TermLoanInterestTO objTermLoanInterestTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = interestMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To retrieve the TermLoanInterestTO from the interestMap
            for (int i = interestMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanInterestTO = (TermLoanInterestTO) interestMap.get(objKeySet[j]);
                if (CommonUtil.convertObjToStr(objTermLoanInterestTO.getAcctNum()).length() < 1) {
                    objTermLoanInterestTO.setAcctNum(acct_No);
                }
                if (delRefMap.containsKey(objTermLoanInterestTO.getAcctNum())) {
                    objTermLoanInterestTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    objTermLoanInterestTO.setStatus(CommonConstants.STATUS_DELETED);
                    sqlMap.executeUpdate("updateIntMaintenanceTL", objTermLoanInterestTO);

                }
                logTO.setData(objTermLoanInterestTO.toString());
                logTO.setPrimaryKey(objTermLoanInterestTO.getKeyData());
                logTO.setStatus(objTermLoanInterestTO.getCommand());
                executeOneTabQueries("TermLoanInterestTO", objTermLoanInterestTO);
                logDAO.addToLog(logTO);
                objTermLoanInterestTO = null;
            }
            objTermLoanInterestTO = null;
            keySet = null;
            objKeySet = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void executeAdditionalSanctionTabQuery() throws Exception {
        TermLoanAdditionalSanctionTO objTermLoanAdditionalSanctionTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = additionalSanMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To retrieve the TermLoanInterestTO from the interestMap
            for (int i = additionalSanMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanAdditionalSanctionTO = (TermLoanAdditionalSanctionTO) additionalSanMap.get(objKeySet[j]);
                if (CommonUtil.convertObjToStr(objTermLoanAdditionalSanctionTO.getAcctNum()).length() < 1) {
                    objTermLoanAdditionalSanctionTO.setAcctNum(acct_No);
                }
                if (delRefMap.containsKey(objTermLoanAdditionalSanctionTO.getAcctNum())) {
                    objTermLoanAdditionalSanctionTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    objTermLoanAdditionalSanctionTO.setStatus(CommonConstants.STATUS_DELETED);
                }

                logTO.setData(objTermLoanAdditionalSanctionTO.toString());
                logTO.setPrimaryKey(objTermLoanAdditionalSanctionTO.getKeyData());
                logTO.setStatus(objTermLoanAdditionalSanctionTO.getCommand());
                executeOneTabQueries("TermLoanAdditionalSanctionTO", objTermLoanAdditionalSanctionTO);
                System.out.println("loanType   ####" + loanType);
                if (loanType.equals("LTD")) {
                    additionalSanctionLienMark(objTermLoanAdditionalSanctionTO);
                }

                logDAO.addToLog(logTO);
                objTermLoanAdditionalSanctionTO = null;
            }
            objTermLoanAdditionalSanctionTO = null;
            keySet = null;
            objKeySet = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void authorizeAdditionalSanction(String authStatus) throws Exception {
        TermLoanAdditionalSanctionTO objTermLoanAdditionalSanctionTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = additionalSanMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To retrieve the TermLoanInterestTO from the interestMap
            for (int i = additionalSanMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanAdditionalSanctionTO = (TermLoanAdditionalSanctionTO) additionalSanMap.get(objKeySet[j]);
                System.out.println("objTermLoanAdditionalSanctionTO ####" + objTermLoanAdditionalSanctionTO);
                if (objTermLoanAdditionalSanctionTO.getAuthorizeStatus().length() == 0) {
                    objTermLoanAdditionalSanctionTO.setAuthorizeStatus(authStatus);
                    sqlMap.executeUpdate("updateTermLoanAdditionalSanctionTO", objTermLoanAdditionalSanctionTO);
                    if (authStatus.equals(CommonConstants.STATUS_AUTHORIZED)) {
                        sqlMap.executeUpdate("updateAvailableBalanceLTD", objTermLoanAdditionalSanctionTO);
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void executeClassificationTabQuery() throws Exception {
        try {
            if (objTermLoanClassificationTO != null && ((this.acct_No != null && this.acct_No.length() > 0) || objTermLoanClassificationTO.getAcctNum().length() > 0)) {
                if (objTermLoanClassificationTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    objTermLoanClassificationTO.setAcctNum(this.acct_No);
                }
                if (delRefMap.containsKey(objTermLoanClassificationTO.getAcctNum())) {
                    objTermLoanClassificationTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    objTermLoanClassificationTO.setStatus(CommonConstants.STATUS_DELETED);
                }
                logTO.setData(objTermLoanClassificationTO.toString());
                logTO.setPrimaryKey(objTermLoanClassificationTO.getKeyData());
                logTO.setStatus(objTermLoanClassificationTO.getCommand());
                executeOneTabQueries("TermLoanClassificationTO", objTermLoanClassificationTO);
                logDAO.addToLog(logTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    private void executeSecurityTabQuery() throws Exception {
        try {
            if (objTermLoanSecurityTO != null && ((this.acct_No != null && this.acct_No.length() > 0) || objTermLoanSecurityTO.getAcctNum().length() > 0)) {
                if (objTermLoanSecurityTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    objTermLoanSecurityTO.setAcctNum(this.acct_No);
                    objTermLoanSecurityTO.setSlNo(CommonUtil.convertObjToInt("0"));
                }
                if (delRefMap.containsKey(objTermLoanSecurityTO.getAcctNum())) {
                    objTermLoanSecurityTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                    objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_DELETED);
                }
                logTO.setData(objTermLoanSecurityTO.toString());
                //                logTO.setPrimaryKey(objTermLoanSecurityTO.getKeyData());
                logTO.setStatus(objTermLoanSecurityTO.getCommand());
                System.out.print("insertGoldLoanSecurityTO :" + objTermLoanSecurityTO);
                executeOneTabQueries("GoldLoanSecurityTO", objTermLoanSecurityTO);
                logDAO.addToLog(logTO);              
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
    
     //Added By Kannan AR EDIT mode only security details updation required EDIT and RENEWAL time only insertion with NEW account number Ref. abi on 16-Jun-2017
      private void executeSecurityUpdateQuery(HashMap updateMap) throws Exception {
        try {            
            if (objTermLoanSecurityTO != null && objTermLoanSecurityTO.getAcctNum().length() > 0 &&  objTermLoanSecurityTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {                         
                if (updateMap.containsKey("RENEWAL_PARAM") && ((HashMap) updateMap.get("RENEWAL_PARAM")).size() == 0) {
                    HashMap val = new HashMap();
                    val.put("ACT_NUM", objTermLoanSecurityTO.getAcctNum());
                    val.put("PARTICULARS", objTermLoanSecurityTO.getParticulars());
                    sqlMap.executeUpdate("updateSecurityDetails", val);
                }                                                         
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    //    private void executeSecurityTabQuery() throws Exception{
    //        GoldLoanSecurityTO objTermLoanSecurityTO;
    //        Set keySet;
    //        Object[] objKeySet;
    //        String strCustSecurityKey = "";
    //        try{
    //            HashMap oldAvailSecAmtMap = (HashMap)securityMap.remove("OLD_ELIGIBLE_LOAN_AMT");
    //            HashMap whereAvailSecAmtMap;
    //            keySet =  securityMap.keySet();
    //            objKeySet = (Object[]) keySet.toArray();
    //            double eligibleAmt = 0.0;
    //
    //            // To retrieve the TermLoanSecurityTO from the securityMap
    //            for (int i = securityMap.size() - 1, j = 0;i >= 0;--i,++j){
    //                whereAvailSecAmtMap = new HashMap();
    //                objTermLoanSecurityTO = (GoldLoanSecurityTO) securityMap.get(objKeySet[j]);
    //                if (CommonUtil.convertObjToStr(objTermLoanSecurityTO.getAcctNum()).length()<1) {
    //                    objTermLoanSecurityTO.setAcctNum(acct_No);
    //                }
    //                if (delRefMap.containsKey(objTermLoanSecurityTO.getAcctNum())) {
    //                    objTermLoanSecurityTO.setCommand(CommonConstants.TOSTATUS_DELETE);
    //                    objTermLoanSecurityTO.setStatus(CommonConstants.STATUS_DELETED);
    //                }
    //                logTO.setData(objTermLoanSecurityTO.toString());
    //                //                logTO.setPrimaryKey(objTermLoanSecurityTO.getKeyData());
    //                logTO.setStatus(objTermLoanSecurityTO.getCommand());
    //                objTermLoanSecurityTO.setAppraiserId(CommonUtil.convertObjToStr(appraiserId));
    //                System.out.print("insertGoldLoanSecurityTO :" +objTermLoanSecurityTO);
    //                executeOneTabQueries("GoldLoanSecurityTO", objTermLoanSecurityTO);
    //
    //                //                whereAvailSecAmtMap.put("CUST_ID", objTermLoanSecurityTO.getCustId());
    //                //                whereAvailSecAmtMap.put("SECURITY_NO", objTermLoanSecurityTO.getSecurityNo());
    //                //
    //                //                strCustSecurityKey = getEligibleLoanAmtKey(objTermLoanSecurityTO.getCustId(), CommonUtil.convertObjToStr(objTermLoanSecurityTO.getSecurityNo()));
    //                //                eligibleAmt = 0.0;
    //                //                if (oldAvailSecAmtMap.containsKey(strCustSecurityKey)){
    //                //                    if (objTermLoanSecurityTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)){
    //                //                        double eligibleOldLoanAmt = CommonUtil.convertObjToDouble(oldAvailSecAmtMap.get(strCustSecurityKey)).doubleValue();
    //                //                        double currentEligibleAmt = objTermLoanSecurityTO.getEligibleLoanAmt().doubleValue();
    //                //                        eligibleAmt = currentEligibleAmt - eligibleOldLoanAmt;
    //                //                    }else if (objTermLoanSecurityTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
    //                //                        eligibleAmt = (-1 * CommonUtil.convertObjToDouble(oldAvailSecAmtMap.get(strCustSecurityKey)).doubleValue());
    //                //                    }
    //                //                }else if (objTermLoanSecurityTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
    //                //                    eligibleAmt = objTermLoanSecurityTO.getEligibleLoanAmt().doubleValue();
    //                //                }
    //                //
    //                //                whereAvailSecAmtMap.put("AVAILABLE_SECURITY_VALUE", new java.math.BigDecimal(eligibleAmt));
    //                //
    //                //                if (eligibleAmt != 0.0){
    //                //                    sqlMap.executeUpdate("updateCustSecurityAvailableAmt", whereAvailSecAmtMap);
    //                //                }
    //
    //                logDAO.addToLog(logTO);
    //                objTermLoanSecurityTO = null;
    //                strCustSecurityKey = null;
    //                whereAvailSecAmtMap = null;
    //            }
    //            keySet = null;
    //            objKeySet = null;
    //            objTermLoanSecurityTO = null;
    //            oldAvailSecAmtMap = null;
    //        } catch (Exception e) {
    //            sqlMap.rollbackTransaction();
    //            e.printStackTrace();
    //            throw e;
    //        }
    //    }
//    private void executeOtherDetailsTabQuery() throws Exception{
//        if (objTermLoanOtherDetailsTO != null && ((this.acct_No != null && this.acct_No.length() > 0) || objTermLoanOtherDetailsTO.getActNum().length() > 0)){
//            if (objTermLoanOtherDetailsTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)){
//                objTermLoanOtherDetailsTO.setActNum(this.acct_No);
//            }
//            if (delRefMap.containsKey(objTermLoanOtherDetailsTO.getActNum())) {
//                objTermLoanOtherDetailsTO.setCommand(CommonConstants.TOSTATUS_DELETE);
//                objTermLoanOtherDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
//            }
//            logTO.setData(objTermLoanOtherDetailsTO.toString());
//            logTO.setPrimaryKey(objTermLoanOtherDetailsTO.getKeyData());
//            logTO.setStatus(objTermLoanOtherDetailsTO.getCommand());
//            
//            executeOneTabQueries("TermLoanOtherDetailsTO", objTermLoanOtherDetailsTO);
//            logDAO.addToLog(logTO);
//        }
//    }
    private void executeOneTabQueries(String strTOName, TransferObject termLoanTO) throws Exception {
        try {
            StringBuffer sbMapName = new StringBuffer();
            if (termLoanTO.getCommand() != null) {
                sbMapName.append(termLoanTO.getCommand().toLowerCase());
                System.out.println("insertcheck" + sbMapName);
                sbMapName.append(strTOName);
                System.out.println("tostring" + sbMapName.toString());
                if (termLoanTO.getCommand().equals("INSERT")) {
                    //                    HashMap hash=new HashMap();
                    //                   TermLoanInstallmentTO actNum=(TermLoanInstallmentTO)termLoanTO;
                    //                    hash.put("ACT_NUM",actNum.getAcctNum());
                    //                    lst=sqlMap.executeQueryForList("getAllLoanInstallment",hash);
                    //                    if(lst !=null && lst.size()>0)
                    //                        executeUpdate("delLoanInstallment",hash);
                    ////                    actNum=null;
                    //                    hash=null;
                    //                    lst=null;
                }
                executeUpdate(sbMapName.toString(), termLoanTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
    //Additional sanction details

    private void additionalSanctionLienMark(TermLoanAdditionalSanctionTO termLoanTO) throws Exception {
        // To make Lien Marking for LTD loans
        double eligibleMargin = 0;
        String depositLienRoundOff = "";
        System.out.println("termLoanT   O" + termLoanTO);
        HashMap prodMap = new HashMap();
        prodMap.put("ACCT_NUM", termLoanTO.getAcctNum());
        String authStatus = CommonUtil.convertObjToStr(termLoanTO.getAuthorizeStatus());

        List lst = sqlMap.executeQueryForList("TermLoan.getBehavesLike", prodMap);
        if (lst != null && lst.size() > 0) {
            prodMap = (HashMap) lst.get(0);
        }
        String prodId = CommonUtil.convertObjToStr(prodMap.get("PROD_ID"));
        prodMap.put("prodId", prodMap.get("PROD_ID"));

        lst = sqlMap.executeQueryForList("TermLoan.getProdHead", prodMap);
        if (lst.size() > 0) {
            prodMap = (HashMap) lst.get(0);
            eligibleMargin = CommonUtil.convertObjToDouble(prodMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
            depositLienRoundOff = CommonUtil.convertObjToStr(prodMap.get("DEPOSIT_ROUNDOFF"));
        }

        if (termLoanTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            DepositLienTO obj = new DepositLienTO();

            //                        List lst = sqlMap.executeQueryForList("TermLoan.getBehavesLike", prodMap);
            //                        if(lst !=null && lst.size()>0)
            //                            prodMap=(HashMap)lst.get(0);
            //                        String prodId=CommonUtil.convertObjToStr(prodMap.get("PROD_ID"));
            //            prodMap.put("prodId", prodMap.get("PROD_ID"));
            //            lst = sqlMap.executeQueryForList("TermLoan.getProdHead", prodMap);
            //            if (lst.size()>0) {
            //                prodMap = (HashMap)lst.get(0);
            obj.setLienAcHd(CommonUtil.convertObjToStr(prodMap.get("AC_HEAD")));
            //                eligibleMargin=CommonUtil.convertObjToDouble(prodMap.get("DEP_ELIGIBLE_LOAN_AMT")).doubleValue();
            //                depositLienRoundOff=CommonUtil.convertObjToStr(prodMap.get("DEPOSIT_ROUNDOFF"));
            //            }
            prodMap.put("ACCT_NUM", termLoanTO.getAcctNum());
            lst = sqlMap.executeQueryForList("getDepositbeforeAuthDetails", prodMap);//getDepositDetails
            if (lst.size() > 0) {
                prodMap = (HashMap) lst.get(0);
                //                            obj.setLienAcHd(CommonUtil.convertObjToStr(prodMap.get("AC_HEAD")));
            }


            obj.setLienAcNo(termLoanTO.getAcctNum());
            double availBal = termLoanTO.getAdditionalLimit().doubleValue();
            availBal = (availBal / eligibleMargin * 100.0);  //85.0
            availBal = roundOffDepositLien(availBal, depositLienRoundOff);
            obj.setLienAmount(new Double(availBal));
            obj.setLienDt(curr_dt);//curr_dt);
            obj.setRemarks("Lien for LTD");
            obj.setCreditLienAcct("NA");
            obj.setDepositNo(CommonUtil.convertObjToStr(prodMap.get("DEPOSIT_NO")));
            obj.setDepositSubNo(CommonUtil.convertObjToInt(prodMap.get("DEPOSIT_SUB_NO")));
            obj.setLienProdId(prodId);
            obj.setLienNo("-");
            obj.setStatus(termLoanTO.getStatus());
            //                        obj.setStatusBy(termLoanTO.getStatusBy());
            obj.setStatusDt(curr_dt);

            ArrayList lienTOs = new ArrayList();
            lienTOs.add(obj);
            HashMap objHashMap = new HashMap();
            objHashMap.put("lienTOs", lienTOs);
            objHashMap.put("SHADOWLIEN", new Double(availBal));
            objHashMap.put(CommonConstants.BRANCH_ID, _branchCode);
            objHashMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
            //System.out.println("### objHashMap : " + objHashMap);
            DepositLienDAO depLienDAO = new DepositLienDAO();
            depLienDAO.setCallFromOtherDAO(true);
            System.out.println("objHashMap before  ####" + objHashMap);
            objHashMap = depLienDAO.execute(objHashMap);
            System.out.println("objHashMap   ####" + objHashMap);
            if (objHashMap != null) {
                HashMap updateMap = new HashMap();
                lienNo = CommonUtil.convertObjToStr(((ArrayList) objHashMap.get("LIENNO")).get(0));
                updateMap.put("ACCT_NUM", termLoanTO.getAcctNum());
                updateMap.put("SLNO", termLoanTO.getSlno());
                updateMap.put("LIEN_NO", lienNo);
                //System.out.println("updateMap####" + updateMap);
                sqlMap.executeUpdate("additionalSanctionLien", updateMap);

            }
        } else if (termLoanTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE) && authStatus.length() == 0) {
            ArrayList lienTOs = new ArrayList();
            HashMap lienCancelMap = new HashMap();
            lienCancelMap.put("LOAN_NO", termLoanTO.getAcctNum());
            List list = sqlMap.executeQueryForList("getDepositLienUnlienLoanTO", lienCancelMap);
            //System.out.println("getdepositliento#####" + lst);
            if (list != null && list.size() > 0) {

                double availBal = termLoanTO.getAdditionalLimit().doubleValue();
                availBal = (availBal / eligibleMargin * 100.0);  //85.0
                availBal = roundOffDepositLien(availBal, depositLienRoundOff);


                //                lienCancelMap=(HashMap)lst.get(0);
                DepositLienTO depLienTO = (DepositLienTO) list.get(0);
                depLienTO.setLienAmount(new Double(availBal));
                depLienTO.setStatus("MODIFIED");
                depLienTO.setStatusDt(curr_dt);
                depLienTO.setRemarks("Lien for LTD");
                //                    depLienTO.setUnlienDt(curr_dt);
                depLienTO.setAuthorizeBy(depLienTO.getAuthorizeBy());
                depLienTO.setAuthorizeDt(depLienTO.getAuthorizeDt());
                depLienTO.setAuthorizeStatus(depLienTO.getAuthorizeStatus());
                lienTOs.add(depLienTO);
                DepositLienDAO depositLiendao = new DepositLienDAO();
                lienCancelMap.put(CommonConstants.BRANCH_ID, _branchCode);
                lienCancelMap.put("COMMAND", CommonConstants.TOSTATUS_UPDATE);
                lienCancelMap.put("lienTOs", lienTOs);
                lienCancelMap.put("SHADOWLIEN", depLienTO.getLienAmount());
                depositLiendao.setCallFromOtherDAO(true);
                System.out.println("lienCancelbefore UPDATE ******" + lienCancelMap);
                depositLiendao.execute(lienCancelMap);
            }

        } else if (termLoanTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
            HashMap lienCancelMap = new HashMap();
            lienCancelMap.put("LOAN_NO", termLoanTO.getAcctNum());
            lienCancelMap.put("PROD_ID", prodId);
            lienCancel(lienCancelMap);

        }
    }
    //regarding LTD

    private void lienAuthorize(HashMap authMap) throws Exception {
        HashMap lienAuthMap = new HashMap();
        if (authMap != null && authMap.size() > 0) {
            authMap.put("LOAN_NO", authMap.get("ACCT_NUM"));
            List list = sqlMap.executeQueryForList("getDepositLienUnlienLoanTO", authMap);
            //System.out.println("getdepositliento#####" + list);
            if (list != null && list.size() > 0) {
                DepositLienTO depositLienTO = (DepositLienTO) list.get(0);
                lienAuthMap.put("DEPOSIT_ACT_NUM", depositLienTO.getDepositNo());
                lienAuthMap.put("ACCOUNT_NO", depositLienTO.getDepositNo());
                list = sqlMap.executeQueryForList("getIntRateforDeposit", lienAuthMap);
                if (list != null && list.size() > 0) {
                    HashMap singleMap = (HashMap) list.get(0);
                    lienAuthMap.put("BALANCE", singleMap.get("AVAILABLE_BALANCE"));
                }
                lienAuthMap.put("LIENNO", depositLienTO.getLienNo());
                lienAuthMap.put("SUBNO", CommonUtil.convertObjToInt(depositLienTO.getDepositSubNo()));
                lienAuthMap.put("AMOUNT", depositLienTO.getLienAmount());
                if (authMap.get(CommonConstants.AUTHORIZESTATUS).equals(CommonConstants.STATUS_AUTHORIZED)) {
                    lienAuthMap.put("LIENAMOUNT", new Double(depositLienTO.getLienAmount().doubleValue()));
                }
                if (authMap.get(CommonConstants.AUTHORIZESTATUS).equals(CommonConstants.STATUS_REJECTED)) {
                    lienAuthMap.put("LIENAMOUNT", new Double(0));
                }
                lienAuthMap.put("ACTION", authMap.get(CommonConstants.AUTHORIZESTATUS));
                lienAuthMap.put("AUTHORIZE_DATE", (Date) curr_dt.clone());
                //                lienAuthMap.put("HIERARCHY_ID",)
                lienAuthMap.put("STATUS", depositLienTO.getStatus());
                lienAuthMap.put("BRANCH_CODE", _branchCode);
                lienAuthMap.put("AUTHORIZEDT", (Date) curr_dt.clone());
                lienAuthMap.put("SHADOWLIEN", depositLienTO.getLienAmount());
                lienAuthMap.put("USER_ID", authMap.get(CommonConstants.USER_ID));
                lienAuthMap.put("AUTHORIZE_STATUS", authMap.get(CommonConstants.AUTHORIZESTATUS));
                lienAuthMap.put("COMMAND", CommonConstants.AUTHORIZEDATA);
                lienAuthMap.put("COMMAND_STATUS", depositLienTO.getStatus());

                DepositLienDAO depositLiendao = new DepositLienDAO();
                depositLiendao.setCallFromOtherDAO(true);
                System.out.println("lienAuthMap  ###" + lienAuthMap);
                depositLiendao.execute(lienAuthMap);


            }
        }
    }

    /*
     * authorize time need not reject wole live account only reject additional
     * sanction only
     */
    private boolean additionalSanctionLTD() throws Exception {
        if (additionalSanMap != null && additionalSanMap.size() > 0) {
            System.out.println("additionalSanMap###" + additionalSanMap);
            Iterator iterator = additionalSanMap.keySet().iterator();
            for (int i = 0; i < additionalSanMap.size(); i++) {
                if (iterator.hasNext()) {
                    TermLoanAdditionalSanctionTO termLoanAdditionalSanctionTO = (TermLoanAdditionalSanctionTO) additionalSanMap.get(CommonUtil.convertObjToStr(iterator.next()));
                    System.out.println("termLoanAdditionalSanctionTO###" + termLoanAdditionalSanctionTO);
                    if (termLoanAdditionalSanctionTO.getAuthorizeStatus() == null || termLoanAdditionalSanctionTO.getAuthorizeStatus().length() == 0) {
                        return true;
                    }
                } else {
                    break;
                }
            }
        }
        return false;
    }

    private void executeUpdate(String str, Object objTermLoanTO) throws Exception {
        try {
            sqlMap.executeUpdate(str, objTermLoanTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

//    private void updateNetworthDetails() throws Exception{
//        try{
//            sqlMap.executeUpdate("updateCustNetworthDetailsTL", netWorthDetailsMap);
//        }catch(Exception e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw e;
//        }
//    }
    private HashMap getProdBehavesLike(HashMap dataMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList("TermLoan.getBehavesLike", dataMap);
        if (list.size() > 0) {
            HashMap retrieveMap = (HashMap) list.get(0);
            dataMap.put(PROD_ID, retrieveMap.get(PROD_ID));
            dataMap.put("prodId", retrieveMap.get(PROD_ID));
            dataMap.put(LIMIT, retrieveMap.get(LIMIT));
            dataMap.put(BEHAVES_LIKE, retrieveMap.get(BEHAVES_LIKE));
            dataMap.put(INT_GET_FROM, retrieveMap.get(INT_GET_FROM));
            dataMap.put(SECURITY_DETAILS, retrieveMap.get(SECURITY_DETAILS));
            System.out.println("dataMap  ##" + dataMap);
            retrieveMap = null;
        }
        list = null;
        return dataMap;
    }

    /*
     * live account going to making rejection means it s called partial
     * rejection that time loan authorize status should be authorized
     */
    private boolean rejectLiveAccount(HashMap dataMap, String partial) throws Exception {
        HashMap updateMap = new HashMap();
        //        System.out.println("dataMap#####"+dataMap);
        updateMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
        updateMap.put("CURR_DT", curr_dt);
        if (partial.equals("PARTIALLY_REJECT")) {
            sqlMap.executeUpdate("updateLoansInstallmentRejectTL", updateMap);
            sqlMap.executeUpdate("updateRepaymentRejectTL", updateMap);
            sqlMap.executeUpdate("updateLoansInstallmentNewRejectTL", updateMap);
            sqlMap.executeUpdate("updateRepaymentNewRejectTL", updateMap);
            return true;
        }
        return false;

    }

    private void getAuthorizeTransaction(HashMap map, String status, HashMap dataMap) throws Exception {
        HashMap cashAuthMap = new HashMap();
        cashAuthMap.put(CommonConstants.BRANCH_ID, dataMap.get(CommonConstants.BRANCH_ID));
        cashAuthMap.put(CommonConstants.USER_ID, dataMap.get(CommonConstants.USER_ID));
        //cashAuthMap.put("LOAN_TRANS_OUT", "LOAN_TRANS_OUT"); //// Commented by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
        String actNum = CommonUtil.convertObjToStr(dataMap.get("RENEWAL_NEW_NO"));//ACCT_NUM
        String oldAcctNo = CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM"));
        //System.out.println("oldAcctNooldAcctNo" + oldAcctNo);
        System.out.println("dataMap auth===" + dataMap);
        System.out.println("map auth===" + map);
        String linkBatchId = oldAcctNo;
        String linkBatchId1 = actNum; //bb1
        cashAuthMap.put("DEBIT_LOAN_TYPE", "DP");
        System.out.println(cashAuthMap + "  linkBatchId####" + linkBatchId);
        cashAuthMap.put("INTERBRANCH_CREATION_SCREEN", "INTERBRANCH_CREATION_SCREEN");
        if (CommonUtil.convertObjToStr(status).length() > 0) {
            HashMap insertMap = (HashMap) dataMap.clone();


            if (CommonUtil.convertObjToStr(insertMap.get("RENEWAL_NEW_NO")).length() > 0) {
                if (status.equals("REJECTED")) {
                    //insertMap.put("ACCT_NUM",insertMap.get("RENEWAL_NEW_NO"));
                    insertMap.put("ACCT_NUM", linkBatchId);
                    sqlMap.executeUpdate("authorizeTermLoan", insertMap);
                    insertMap.put("ACCT_NUM", oldAcctNo);
                    insertMap.put("AUTHORIZESTATUS", "AUTHORIZED");
                    sqlMap.executeUpdate("authorizeTermLoan", insertMap);
                } else {
                    //insertMap.put("ACCT_NUM",insertMap.get("RENEWAL_NEW_NO"));

                    insertMap.put("ACCT_NUM", linkBatchId);
                    sqlMap.executeUpdate("authorizeTermLoan", insertMap);
                    insertMap.put("ACCT_NUM", oldAcctNo);
//                    insertMap.put("AUTHORIZESTATUS","REJECTED");
                    sqlMap.executeUpdate("authorizeTermLoan", insertMap);

                }
                String renewedLoanNo = CommonUtil.convertObjToStr(insertMap.get("RENEWAL_NEW_NO"));
                cashAuthMap.put("RENEWED_LOAN_NO",renewedLoanNo);// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                authoirzeGoldLoanRenewal(oldAcctNo, status, linkBatchId, actNum);
                authorizeCashTransferFromGoldLoan(linkBatchId, status, cashAuthMap, /*linkBatchId1*/linkBatchId);//linkBatchId1
            } else {
                linkBatchId = CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM"));
                GoldLoanSecurityTO goldTO = new GoldLoanSecurityTO();
                goldTO.setAcctNum(linkBatchId);
                goldTO.setAuthorizeStatus(status);
                goldTO.setAuthorizeBy(userID);
                goldTO.setAuthorizeDt(curr_dt);
                sqlMap.executeUpdate("authorizeGoldLoanSecurityTO", goldTO);
                TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
            }
        }

        cashAuthMap = null;

    }

    private void authorizeCashTransferFromGoldLoan(String linkBatchId, String status, HashMap map, String transferLinkId) throws Exception {
        ArrayList transferTransList = new ArrayList();
        ArrayList renewTransferList =  new ArrayList();
        HashMap transferTransParam = new HashMap();
        HashMap cashAuthMap = new HashMap();
        HashMap tempMap = new HashMap();
        boolean flag = false;
        CashTransactionTO cashTOchk = null;
        HashMap getCashForAuthMap = new HashMap();
        Date currDate = ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        System.out.println("@@@@linkBatchId" + linkBatchId);
        getCashForAuthMap.put("LINK_BATCH_ID", linkBatchId);
        getCashForAuthMap.put("TRANS_DT", currDate);
        getCashForAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
        List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
        if (listData != null && listData.size() > 0) {
            HashMap map1 = (HashMap) listData.get(0);
            if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")){
                getCashForAuthMap.put("TRANS_TYPE",CommonConstants.DEBIT);
            }
        }
        List lst = (List) sqlMap.executeQueryForList("getCashTransactionTOForAuthorzation", getCashForAuthMap);
        
         //Gold loan renewal insurance charge issue 
        if(map.containsKey("RENEWED_LOAN_NO") && map.get("RENEWED_LOAN_NO") != null){
            cashAuthMap.put("GOLD_LOAN_RENEWAL_AUTH","GOLD_LOAN_RENEWAL_AUTH");
            getCashForAuthMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(map.get("RENEWED_LOAN_NO")));// linkBatch
            List renewalList = (ArrayList) sqlMap.executeQueryForList("getCashTransactionTOForAuthorzation", getCashForAuthMap);
            if(renewalList != null && renewalList.size() >0){
               lst.addAll(renewalList);
            }
        }
        getCashForAuthMap.put("LINK_BATCH_ID", linkBatchId);
        // End
        
        if (lst != null) {
            if (lst.size() > 0) {
                tempMap.put(CommonConstants.AUTHORIZESTATUS, status);
                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                cashAuthMap.put(TransactionDAOConstants.OLDAMT, CommonUtil.convertObjToDouble("0.0"));
//                if(map.containsKey("DAILY"))
                tempMap.put("DAILY", "DAILY");
                for (int i = 0; i < lst.size(); i++) {
                    CashTransactionTO cashTO = (CashTransactionTO) lst.get(i);
                    if (i > 0) {
                        cashTOchk = (CashTransactionTO) lst.get(i - 1);
                    }
                    if (cashTO != null) {
                        HashMap transIdMap = new HashMap();
                        System.out.println("flag in do" + flag);
                        if (!(flag == true && cashTO.getTransId().equals(cashTOchk.getTransId()))) {
                            transIdMap.put("TRANS_ID", cashTO.getTransId());
                            transIdMap.put("LINK_BATCH_ID", linkBatchId);
                            ArrayList cashTransList = new ArrayList();
                            cashTransList.add(transIdMap);
                            tempMap.put(CommonConstants.AUTHORIZEDATA, cashTransList);
//                        if(map.containsKey("LOAN_TRANS_OUT")){
//                            tempMap.put("LOAN_TRANS_OUT", "LOAN_TRANS_OUT");
//                        }
                            cashAuthMap.put(CommonConstants.AUTHORIZEMAP, tempMap);
                            cashAuthMap.put("PRODUCTTYPE", cashTO.getProdType());
                            if (map.containsKey("PRODUCT")) {
                                cashAuthMap.put("PRODUCT", "SHARE");
                            }
                            if (cashTO.getAuthorizeRemarks() != null && cashTO.getAuthorizeRemarks().length() > 0) {
                                cashAuthMap.put("DEBIT_LOAN_TYPE", cashTO.getAuthorizeRemarks());
                            }
                            if (cashTO.getTransType().equals(CommonConstants.CREDIT)) {
                                flag = true;
                            } else {
                                flag = false;
                            }
                            System.out.println("cash transaction ###" + cashAuthMap);
                            TransactionDAO.doCashAuthorize(cashAuthMap);
                        }

                    }
                }
            }
        }





        transferTransParam = new HashMap();
        transferTransParam.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
        transferTransParam.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
        transferTransParam.put("LINK_BATCH_ID", transferLinkId);// linkBatchId);


        //transfer
        transferTransList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
        
         //Gold loan renewal insurance charge issue 
        if(map.containsKey("RENEWED_LOAN_NO") && map.get("RENEWED_LOAN_NO") != null){
            transferTransParam.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(map.get("RENEWED_LOAN_NO")));// linkBatch
            renewTransferList = (ArrayList) sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", transferTransParam);
            if(renewTransferList != null && renewTransferList.size() > 0){
              transferTransList.addAll(renewTransferList);
            }
        }
        transferTransParam.put("LINK_BATCH_ID", transferLinkId);
        
        
        
        TransferTrans objTrans = new TransferTrans();
        //System.out.println("transferTransListSize ###" + transferTransList.size());
        if (transferTransList != null) {
            for (int x = 0; x < transferTransList.size(); x++) {
                String batchId = ((TxTransferTO) transferTransList.get(x)).getBatchId();
                transferTransParam.put("BATCH_ID", batchId);
                System.out.println("batchId ###" + batchId);
                transferTransParam.put(CommonConstants.AUTHORIZESTATUS, status);

                if (map.containsKey("DEBIT_LOAN_TYPE"))//if (debitInt.equals("DP"))
                {
                    objTrans.setLoanDebitInt(CommonUtil.convertObjToStr(map.get("DEBIT_LOAN_TYPE")));
                }
                transferTransParam.put("DEBIT_LOAN_TYPE", CommonUtil.convertObjToStr(map.get("DEBIT_LOAN_TYPE")));


            }
            System.out.println("transferTransList###" + transferTransList.size() + "transferTransParam###" + transferTransParam);
            if (transferTransList.size() > 0) {
                objTrans.doTransferAuthorize(transferTransList, transferTransParam);
            }
        }






    }

    private void authoirzeGoldLoanRenewal(String linkBatchId, String status, String newAcctNo, String actNum) throws Exception {
        HashMap facilitymap = new HashMap();
        AccountClosingTO actTo = new AccountClosingTO();
        GoldLoanSecurityTO goldTO = new GoldLoanSecurityTO();
        GoldLoanSecurityTO renewalGoldTO = new GoldLoanSecurityTO();
        actTo.setStatus("CREATED");
        actTo.setStatusBy(userID);
        actTo.setStatusDt(curr_dt);
        facilitymap.put("STATUS", status);
        facilitymap.put("USER_ID", userID);
        facilitymap.put("AUTHORIZEDT", curr_dt);
        facilitymap.put("ACCT_STATUS", "NEW");
        if (status.equals("AUTHORIZED")) {
            facilitymap.put("ACCOUNTNO", linkBatchId);
            goldTO.setAcctNum(newAcctNo);
            goldTO.setAuthorizeStatus(status);
            goldTO.setAuthorizeBy(userID);
            goldTO.setAuthorizeDt(curr_dt);
            renewalGoldTO.setAcctNum(actNum);
            renewalGoldTO.setAuthorizeStatus(status);
            renewalGoldTO.setAuthorizeBy(userID);
            renewalGoldTO.setAuthorizeDt(curr_dt);
            actTo.setActNum(linkBatchId);
            List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
            if (listData != null && listData.size() > 0) {
                HashMap map1 = (HashMap) listData.get(0);
                if (map1.get("CASHIER_AUTH_ALLOWED") == null || map1.get("CASHIER_AUTH_ALLOWED").toString().equals("N")){
                    sqlMap.executeUpdate("insertAccountClosingTO", actTo);
                    sqlMap.executeUpdate("authorizeUpdateAccountCloseTO", facilitymap);
                }
            }
            facilitymap.put("ACCOUNT_STATUS", "CLOSED");
            facilitymap.put("AUTHORIZEDT", curr_dt);
            sqlMap.executeUpdate("authorizeAcctStatusTL", facilitymap);
            
            //Added by nithya for Gold loan renewal security release
            HashMap updatemap = new HashMap();
            updatemap.put("IS_RELEASE", "Y");
            updatemap.put("RELEASE_DT", curr_dt);
            updatemap.put("ACCT_NUM", linkBatchId);
            sqlMap.executeUpdate("updateGoldReleaseStatusForKCC", updatemap);

        } else if (status.equals("REJECTED")) {
            facilitymap.put("ACCT_NUM", linkBatchId);//OLDACCTNO AS LINKBATCHID

            sqlMap.executeUpdate("updateStatusForAccountTL", facilitymap);
            facilitymap.put("ACCOUNTNO", newAcctNo);
            actTo.setActNum(newAcctNo);

        }


//        sqlMap.executeUpdate("insertAccountClosingTO",actTo);
//        sqlMap.executeUpdate("authorizeUpdateAccountCloseTO",facilitymap);
        if (status.equals("AUTHORIZED")) {
            facilitymap.put("ACCOUNTNO", actNum);

            // sqlMap.executeUpdate("updateTermLoanFacilityRenewal",facilitymap);
            sqlMap.executeUpdate("authorizeGoldLoanSecurityTO", goldTO);
            sqlMap.executeUpdate("authorizeGoldLoanSecurityTO", renewalGoldTO);
        }else{   
            renewalGoldTO.setAcctNum(actNum);
            renewalGoldTO.setAuthorizeStatus(status);
            renewalGoldTO.setAuthorizeBy(userID);
            renewalGoldTO.setAuthorizeDt(curr_dt);
            sqlMap.executeUpdate("authorizeGoldLoanSecurityTO", renewalGoldTO);
        }



    }

    void getAuthorizeNPA(HashMap dataMap) throws Exception {
        System.out.println("authorize data###" + dataMap);
        List lst = sqlMap.executeQueryForList("SELECTNPA_HISTORY", dataMap);
        if (lst.size() > 0) {
            sqlMap.executeUpdate("updateNPA_HISTORY", dataMap);
        }
    }

    private void authorizeLoanInterestDetails(String status) throws Exception {
        if (interestMap != null) {
            Set set = interestMap.keySet();
            Object obj[] = (Object[]) set.toArray();
            for (int i = 0; i < obj.length; i++) {
                TermLoanInterestTO objTermLoanInterestTO = (TermLoanInterestTO) interestMap.get(obj[i]);
                if (objTermLoanInterestTO.getAuthorizeStatus().equals("")) {
                    objTermLoanInterestTO.setAuthorizeStatus(status);
                    if (status.equals(CommonConstants.STATUS_REJECTED)) {
                        sqlMap.executeUpdate("updateIntMaintenanceTL", objTermLoanInterestTO);
                    }

                    System.out.println("objTermLoanInterestTO   " + objTermLoanInterestTO);
                    sqlMap.executeUpdate("deleteTermLoanInterestTO", objTermLoanInterestTO);
                }

            }
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        curr_dt = ServerUtil.getCurrentDate(_branchCode);
        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        newLoan = false;
        HashMap hash;
        HashMap resultMap = new HashMap();
        System.out.print("termloandao#######" + map);
        if(map.containsKey("RENEWAL_GOLDLOAN")){ // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
            photoByteArray = (byte[]) map.get("RENEWAL_PHOTO");
        }else{
            photoByteArray = (byte[]) map.get("PHOTO");
        }
        if (map.containsKey("LOAN_TYPE")) {
            loanType = CommonUtil.convertObjToStr(map.get("LOAN_TYPE"));
        } else {
            loanType = "";
        }
        if (map.containsKey("NPAHISTORY")) {
            if (map.get("NPAHISTORY") != null) {
                NPA = (HashMap) map.get("NPAHISTORY");
                System.out.println("NPAHISTORY####" + NPA);
            }
        }
        if (map.containsKey("APPRAISER_ID")) {
            appraiserId = CommonUtil.convertObjToStr(map.get("APPRAISER_ID"));
        }
        if (map.containsKey("NPA")) {
            HashMap paramMap = new HashMap();
            //System.out.println("#####@@daomap" + map);
            paramMap.put("BRANCH_CODE", _branchCode);
            paramMap.put("BRANCH_ID", _branchCode);
            paramMap.put("TODAY_DT", ServerUtil.getCurrentDate(super._branchCode));
            if (map.containsKey("PROD_ID") && map.get("PROD_ID") != null) {
                paramMap.put("PROD_ID", map.get("PROD_ID"));
            }


            ibrHierarchy = 1;
            hash = new HashMap();
            int i = 0;
            HashMap loans_product = (HashMap) sqlMap.executeQueryForList("getLoansProduct", paramMap).get(0);
            //            Date curr_dt=DateUtil.getDateWithoutMinitues(ServerUtil.getCurrentDate(_branchCode));
            String prod_behaves = CommonUtil.convertObjToStr(loans_product.get("BEHAVES_LIKE"));
            List actLst = null;
            if (loans_product != null && prod_behaves.equals("OD")) {
                actLst = sqlMap.executeQueryForList("getAllLoanRecordOD", paramMap);
            } else {
                actLst = sqlMap.executeQueryForList("getAllLoanRecord", paramMap);
            }
            for (int s = 0; s < actLst.size(); s++) {
                HashMap hashMap = (HashMap) actLst.get(s);
                String behaveLike = CommonUtil.convertObjToStr(hashMap.get("BEHAVES_LIKE"));
                Date instalDt = new Date();
                paramMap.put("ACT_NUM", hashMap.get("ACCT_NUM"));
                paramMap.putAll(hashMap);
                instalDt = getInstallmentDate(paramMap);
                //System.out.println("instalDt###" + instalDt);
                decalreAssetStatus(instalDt, paramMap, hashMap, curr_dt, behaveLike);
                System.out.println("instalDt###" + instalDt);
                instalDt = new Date();
                if (hashMap.containsKey("LAST_INT_CALC_DT") && hashMap.get("LAST_INT_CALC_DT") != null) {
                    instalDt = (Date) hashMap.get("LAST_INT_CALC_DT");
                    System.out.println("instalDt###" + instalDt);

                    decalreAssetStatus(instalDt, paramMap, hashMap, curr_dt, behaveLike);
                }

            }
            return null;
        } else if (true) { 
            resultMap = dataManipulation(map);
            if (map.containsKey("RENEWAL_GOLDLOAN")) {
                isRenewal = true;
                map = (HashMap) map.get("RENEWAL_GOLDLOAN");
                System.out.println("map @@@" + map);
                resultMap = dataManipulation(map);
            }
            return resultMap;
        }

        ServiceLocator.flushCache(sqlMap);
        System.gc();

//        throw new TTException();  //testing purpose
        return null;
    }

    private HashMap dataManipulation(HashMap map) throws Exception {

        logDAO = new LogDAO();
        logTO = new LogTO();
        //            objAuthorizedSignatoryDAO = new AuthorizedSignatoryDAO(CommonUtil.convertObjToStr(map.get("UI_PRODUCT_TYPE")));
        //            objPowerOfAttorneyDAO = new PowerOfAttorneyDAO(CommonUtil.convertObjToStr(map.get("UI_PRODUCT_TYPE")));

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setSelectedBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.SELECTED_BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        jointAcctMap = (HashMap) map.get("TermLoanJointAcctTO");
        // To update the networth details in Customer Table
//            netWorthDetailsMap = (HashMap) map.get("NETWORTH_DETAILS");
        objTermLoanBorrowerTO = (TermLoanBorrowerTO) map.get("TermLoanBorrowerTO");
//            objTermLoanCompanyTO = (TermLoanCompanyTO) map.get("TermLoanCompanyTO");
        //            objAuthorizedSignatoryDAO.setAuthorizeMap((HashMap) map.get("AuthorizedSignatoryTO"));
        //            objAuthorizedSignatoryDAO.setAuthorizedInstructionMap((HashMap) map.get("AuthorizedSignatoryInstructionTO"));
        //            objPowerOfAttorneyDAO.setPoAMap((HashMap) map.get("PowerAttorneyTO"));
        if (map.get("TermLoanSanctionTO") != null) {
            sanctionMap = (HashMap) map.get("TermLoanSanctionTO");
        }
        sanctionFacilityMap = (HashMap) map.get("TermLoanSanctionFacilityTO");
        facilityMap = (HashMap) map.get("TermLoanFacilityTO");
        //            securityMap = (HashMap) map.get("TermLoanSecurityTO");
        repaymentMap = (HashMap) map.get("TermLoanRepaymentTO");
        installmentMap = (HashMap) map.get("TermLoanInstallmentTO");
        System.out.println("installmentMap in manipulate" + installmentMap);
        DisbursementMap = (HashMap) map.get("TermLoanDisbursementTO");
        installmentAllMap = (LinkedHashMap) map.get("TermRepaymentInstallmentAllTO");
        System.out.println("installmentAllmap" + installmentAllMap);
        installmentMultIntMap = (HashMap) map.get("TermLoanInstallMultIntTO");
//            guarantorMap = (HashMap) map.get("TermLoanGuarantorTO");
//            guaranInstitMap= (HashMap) map.get("TermLoanInstitGuarantorTO");
        documentMap = (HashMap) map.get("TermLoanDocumentTO");
        interestMap = (HashMap) map.get("TermLoanInterestTO");
        additionalSanMap = (HashMap) map.get("TermLoanAdditionalSanctionTO");        
        objSMSSubscriptionTO = null;
        if (map.containsKey("SMSSubscriptionTO")) {
            objSMSSubscriptionTO = (SMSSubscriptionTO) map.get("SMSSubscriptionTO");
        }

        System.out.println("map^^^^^^^^^^^^" + map);
        SettlementDAO objSetDao = new SettlementDAO("common");
        if (map.containsKey("SettlementTO")) {
            objSetDao.setSetMap((HashMap) map.get("SettlementTO"));
        }
        if (map.containsKey("SettlementBankTO")) {
            objSetDao.setSetBankMap((HashMap) map.get("SettlementBankTO"));
        }
        if (map.containsKey("ActTransTO")) {
            objSetDao.setSetActTrans((HashMap) map.get("ActTransTO"));
        }

        ////            if(objSetDao.getSetActTrans() != null){
        //                objSetDao.executeActTransTabQuery(sqlMap);
        ////            }else{
        //               objSetDao.executeSetTabQuery(sqlMap);
        //            }

        if (map.containsKey("PRODUCT_ID")) {
            ProductId = CommonUtil.convertObjToStr(map.get("PRODUCT_ID"));
        }
        if (map.containsKey("OLD_PRODUCT_ID")) {
            old_prod_id= CommonUtil.convertObjToStr(map.get("OLD_PRODUCT_ID"));
        }
        if (map.containsKey("TermLoanExtenFacilityDetailsTO") && map.get("TermLoanExtenFacilityDetailsTO") != null) {
            termLoanExtenFacilityMap = (HashMap) map.get("TermLoanExtenFacilityDetailsTO");
            //            objTermLoanExtenFacilityDetailsTO=(TermLoanExtenFacilityDetailsTO)insideMap.get("TermLoanExtenFacilityDetailsTO");
        }
        if (map.containsKey("AdvancesLiablityExceedLimit")) {
            todCreation((HashMap) map.get("AdvancesLiablityExceedLimit"));
        }
        if (map.containsKey("INT_TRANSACTION_REPAYMENT")) {
            repayschduleTransaction(map);
        }

        if (map.containsKey("LTD")) {
            depositCustDetMap = (HashMap) map.get("LTD");
        }
        
        if (map.containsKey("TermLoanClassificationTO")) {
            objTermLoanClassificationTO = (TermLoanClassificationTO) map.get("TermLoanClassificationTO");
        }else{
             objTermLoanClassificationTO = null;
        }     

        if (map.containsKey("TermLoanSecurityTO")) {
            objTermLoanSecurityTO = (GoldLoanSecurityTO) map.get("TermLoanSecurityTO");
        } else {
            objTermLoanSecurityTO = null;
        }

//            Object objOtherDetailsTO = map.get("TermLoanOtherDetailsTO");
//            if (objOtherDetailsTO != null){
//                objTermLoanOtherDetailsTO = (TermLoanOtherDetailsTO) objOtherDetailsTO;
//            }else{
//                objTermLoanOtherDetailsTO = null;
//            }

        final String command = objTermLoanBorrowerTO.getCommand();
        System.out.println("welcometocommand" + command);

//            if (netWorthDetailsMap != null && netWorthDetailsMap.keySet().size() > 0){
//                if (netWorthDetailsMap.containsKey("NETWORTH_AS_ON"))
//                    if (netWorthDetailsMap.get("NETWORTH_AS_ON")!=null)
//                        updateNetworthDetails();
//            }

        delRefMap = new HashMap();

        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(map);
            }
        } else {
            HashMap transDetailMap = new HashMap();
            if (map.containsKey("Transaction Details Data")) {
                transDetailMap = (HashMap) map.get("Transaction Details Data");
                System.out.println("@##$#$% transDetailMap #### :" + transDetailMap);
                chargeLst = (List) map.get("Charge List Data");
                System.out.println("@##$#$% chargeLst #### :" + chargeLst);
            }
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map, transDetailMap);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                throw new NoCommandException();
            }
            chargeLst=null;
        }
        if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            //            objSetDao.setLoanAcctNumber(acct_No);
            //            objSetDao.executeActTransTabQuery(sqlMap);
            //            objSetDao.executeSetTabQuery(sqlMap);
        }
        destroyObjects();
        HashMap returnMap = new HashMap();
        if (lienNo.length() > 0) {
            returnMap.put("LIENNO", lienNo);
        }
        if (newLoan) {
            returnMap.put("ACCTNO", acct_No);
        }
        
        if (returnMap.size() > 0) {
            return returnMap;
        }

        ServiceLocator.flushCache(sqlMap);
        System.gc();

        return null;
    }

    /**
     * upto date interest calculate and to the outstanding amount and create new
     * repay schdule existing schdule become inactive newly declared only active
     * *
     */
    private void repayschduleTransaction(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap acHeads = new HashMap();
        TransactionDAO transactionDAO = null;
        TxTransferTO transferDao = null;
        ArrayList transList = new ArrayList();
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        System.out.println("map######" + map);
        if (map.containsKey("INT_TRANSACTION_REPAYMENT")) {
            HashMap reserchMap = new HashMap();
            reserchMap = (HashMap) map.get("INT_TRANSACTION_REPAYMENT");
            String branchId = CommonUtil.convertObjToStr(reserchMap.get("BRANCH_CODE"));
            try {
                acHeads = (HashMap) sqlMap.executeQueryForObject("getLoanAccountClosingHeads", reserchMap.get("ACT_NUM"));
                transactionDAO.setLinkBatchID((String) reserchMap.get("ACT_NUM"));
                transactionDAO.setInitiatedBranch(branchId);
                transferTrans.setLinkBatchId((String) reserchMap.get("ACT_NUM"));
                txMap.put(TransferTrans.DR_ACT_NUM, reserchMap.get("ACT_NUM"));
                txMap.put(TransferTrans.DR_AC_HD, acHeads.get("ACCT_HEAD"));
                txMap.put(TransferTrans.DR_PROD_ID, reserchMap.get("PROD_ID"));
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                txMap.put(TransferTrans.DR_BRANCH, reserchMap.get("BRANCH_CODE"));
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put("DR_INST_TYPE", "VOUCHER");
                txMap.put("AUTHORIZEREMARKS", "DI");
                double interestAmt = CommonUtil.convertObjToDouble(reserchMap.get("INTEREST_AMT")).doubleValue();

                transferDao = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                //                transferDao.setAuthorizeRemarks("DI");
                transferDao.setScreenName("Gold Loan Account Opening");
                transList.add(transferDao);
                txMap.put(TransferTrans.CR_AC_HD, acHeads.get("INT_PAYABLE_ACHD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, reserchMap.get("BRANCH_CODE"));
                System.out.println("txMap  ###" + txMap + "transferDao   :" + transferDao);
                //DELETEING RECORD
                reserchMap.put("LINK_BATCH_ID", reserchMap.get("ACT_NUM"));
                reserchMap.put("TRANS_DT", ServerUtil.getCurrentDate(_branchCode));
                reserchMap.put("INITIATED_BRANCH", _branchCode);
                List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", reserchMap);
                TxTransferTO txtransferTO = new TxTransferTO();
                ArrayList batchList = new ArrayList();
                if (lst != null && lst.size() > 0) {
                    for (int i = 0; i < lst.size(); i++) {
                        txtransferTO = new TxTransferTO();
                        TxTransferTO singletxtransferTO = new TxTransferTO();
                        singletxtransferTO = (TxTransferTO) lst.get(i);
                        txtransferTO.setActNum(singletxtransferTO.getActNum());
                        txtransferTO.setAcHdId(singletxtransferTO.getAcHdId());
                        txtransferTO.setAmount(singletxtransferTO.getAmount());
                        txtransferTO.setBatchId(singletxtransferTO.getBatchId());
                        txtransferTO.setBranchId(singletxtransferTO.getBranchId());
                        txtransferTO.setInitiatedBranch(singletxtransferTO.getInitiatedBranch());
                        txtransferTO.setLinkBatchId(singletxtransferTO.getLinkBatchId());
                        txtransferTO.setProdId(singletxtransferTO.getProdId());
                        txtransferTO.setProdType(singletxtransferTO.getProdType());
                        txtransferTO.setStatus("DELETED");
                        txtransferTO.setTransDt(singletxtransferTO.getTransDt());
                        txtransferTO.setTransId(singletxtransferTO.getTransId());
                        txtransferTO.setTransType(singletxtransferTO.getTransType());
                        txtransferTO.setTransMode(singletxtransferTO.getTransMode());
                        batchList.add(txtransferTO);
                    }
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    TransferTrans transfer = new TransferTrans();
                    transfer.setInitiatedBranch((String) reserchMap.get("BRANCH_CODE"));
                    transfer.doDebitCredit(batchList, (String) reserchMap.get("BRANCH_CODE"), false, "INSERT");


                }
                //
                transferDao = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                transferDao.setScreenName("Gold Loan Account Opening");
                transList.add(transferDao);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                System.out.println("transList  ###" + transList + "transferDao   :" + transferDao);
                transferTrans.setLoanDebitInt(new String("DI"));
                transferTrans.setInitiatedBranch((String) reserchMap.get("BRANCH_CODE"));
                transferTrans.doDebitCredit(transList, branchId, false);
                HashMap hash = new HashMap();
                //                hash.put("ACCOUNTNO",map.get("ACT_NUM"));
                //                Date curr_dt=(Date)map.get("CURR_DATE");
                //                hash.put("LAST_CALC_DT",DateUtil.addDays(curr_dt,-1));
                //                sqlMap.executeUpdate("updateclearBal", hash);

                //                transactionDAO.doTransferLocal(transList, branchId);
                //            transactionDAO.setCommandMode(commandMode);
            } catch (Exception e) {
                //                throw new TTException(" A/c Head not set.");
                e.printStackTrace();
            }
            transList = null;
            transactionDAO = null;
            transferDao = null;
            txMap = null;
            acHeads = null;
            //            return returnMap;
        }

    }

    private void todCreation(HashMap AdvancesLiablityExceedLimit) throws Exception {
        TodAllowedTO todAllowed = new TodAllowedTO();
        TodAllowedTO todAllowedExist = new TodAllowedTO();
        TodAllowedDAO todallowedDao = new TodAllowedDAO();
        HashMap todMap = new HashMap();
        String mode = null;
        System.out.println("AdvancesLiablityExceedLimit@@@@" + AdvancesLiablityExceedLimit);
        AdvancesLiablityExceedLimit.put("CURR_DT", curr_dt);
        List lst = sqlMap.executeQueryForList("getSelectExistAccountList", AdvancesLiablityExceedLimit);
        todAllowed.setAccountNumber(CommonUtil.convertObjToStr(AdvancesLiablityExceedLimit.get("ACCT_NUM")));
        if (!(AdvancesLiablityExceedLimit.containsKey("MODE") && AdvancesLiablityExceedLimit.get("MODE") == null)) {
            if (lst != null && lst.size() > 0) {
                todAllowedExist = (TodAllowedTO) lst.get(0);
                mode = "UPDATE";
                todAllowed.setTrans_id(todAllowedExist.getTrans_id());
                //           todAllowed.setAcctName(CommonUtil.convertObjToStr(getTxtAcctName()));
                todAllowed.setFromDate(curr_dt);
                todAllowed.setToDate(curr_dt);
                todAllowed.setPermittedDt(curr_dt);
                //           todAllowed.setStatusBy(ProxyParameters.USER_ID);
                //           todAllowed.setRemarks("Liablity Exceed Limit");
                todAllowed.setRemarks(todAllowed.getRemarks());
                todAllowed.setProductType(todAllowedExist.getProductType());
                todAllowed.setProductId(todAllowedExist.getProductId());
                todAllowed.setProductId(todAllowedExist.getProductId());
                todAllowed.setPermitedBy(todAllowedExist.getPermitedBy());
                todAllowed.setTodAllowed(CommonUtil.convertObjToStr(AdvancesLiablityExceedLimit.get("DIFFERENT_AMT")));
                todAllowed.setPermissionRefNo(todAllowedExist.getPermissionRefNo());
                todAllowed.setBranchCode(_branchCode);
                todAllowed.setTypeOfTOD("SINGLE");
                todMap.put("MODE", mode);
                todMap.put("BRANCH_CODE", _branchCode);
                todMap.put("TodAllowed", todAllowed);
                todMap.put("AUTHORIZEMAP", null);
                System.out.println("todMap#######" + todMap);
                todallowedDao.execute(todMap);

            } else {
                mode = "INSERT";
                todAllowed.setTrans_id(todAllowedExist.getTrans_id());
                //           todAllowed.setAcctName(CommonUtil.convertObjToStr(getTxtAcctName()));
                todAllowed.setFromDate(curr_dt);
                todAllowed.setToDate(curr_dt);
                todAllowed.setPermittedDt(curr_dt);
                //           todAllowed.setStatusBy(ProxyParameters.USER_ID);
                //           todAllowed.setRemarks("Liablity Exceed Limit");
                todAllowed.setRemarks("Liablity Exceed Limit");
                todAllowed.setProductType("AD");
                todAllowed.setProductId(CommonUtil.convertObjToStr(AdvancesLiablityExceedLimit.get("PROD_ID")));
                todAllowed.setPermitedBy(todAllowedExist.getPermitedBy());
                todAllowed.setTodAllowed(CommonUtil.convertObjToStr(AdvancesLiablityExceedLimit.get("DIFFERENT_AMT")));
                todAllowed.setPermissionRefNo("");
                todAllowed.setBranchCode(_branchCode);
                todAllowed.setTypeOfTOD("SINGLE");
                todMap.put("MODE", mode);
                todMap.put("BRANCH_CODE", _branchCode);
                todMap.put("TodAllowed", todAllowed);
                todMap.put("AUTHORIZEMAP", null);
                System.out.println("todMap#######" + todMap);
                todallowedDao.execute(todMap);

            }
        } else {
            AdvancesLiablityExceedLimit.put(CommonConstants.BRANCH_ID, _branchCode);
            System.out.println("AdvancesLiablityExceedLimit#######" + AdvancesLiablityExceedLimit);
            todallowedDao.execute(AdvancesLiablityExceedLimit);
        }
    }

    public void decalreAssetStatus(Date instalDt, HashMap paramMap, HashMap hashMap, Date curr_dt, String behaves_like) throws Exception {
        String ASSET_STATUS = null;
        try {
            List lst = sqlMap.executeQueryForList("getAssetStatusNo", paramMap);
            if (lst != null && lst.size() > 0) {
                HashMap resultMap = (HashMap) lst.get(0);
                hashMap.put("ASSET_STATUS", resultMap.get("CURR_STATUS"));
            }
            if (instalDt != null) {
                paramMap.put("INSTALLMENT_DT", instalDt);
                int asset_status = CommonUtil.convertObjToInt(hashMap.get("ASSET_STATUS"));

                //                System.out.println(asset_status.equals("STANDARD_ASSETS")+"parammap#####"+paramMap);
                int period = 0;

                period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_LOSS"));
                if (period > 0) {
                    if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status < 6) {
                        ASSET_STATUS = "LOSS_ASSETS";
                        asset_status = 6;
                    }
                }

                period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_DOUBTFUL_3"));
                if (period > 0) {
                    if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status < 5) {
                        ASSET_STATUS = "DOUBTFUL_ASSETS_3";
                        asset_status = 5;
                    }
                }

                period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_DOUBTFUL_2"));
                if (period > 0) {
                    if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status < 4) {
                        ASSET_STATUS = "DOUBTFUL_ASSETS_2";
                        asset_status = 4;
                    }
                }

                period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_DOUBTFUL"));
                if (period > 0) {
                    if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status < 3) {
                        ASSET_STATUS = "DOUBTFUL_ASSETS_1";
                        asset_status = 3;
                    }
                }

                period = CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_SUBSTANDARD"));
                System.out.println(period + "datediff#####" + DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt));
                if (period > 0) {
                    if (DateUtil.dateDiff(DateUtil.addDays(instalDt, period), curr_dt) > 0 && asset_status < 2) {
                        //note when the account status changed std to substd upto date int shoude be calculated
                        TaskHeader header = new TaskHeader();
                        HashMap assetMap = new HashMap();
                        header.setBranchID(_branchCode);
                        assetMap.put("PROD_ID", hashMap.get("PROD_ID"));
                        assetMap.put("PRODUCT_ID", hashMap.get("PROD_ID"));
                        assetMap.put("ACT_TO", hashMap.get("ACCT_NUM"));
                        assetMap.put("ACT_FROM", hashMap.get("ACCT_NUM"));
                        assetMap.put("DATE_FROM", hashMap.get("LAST_INT_CALC_DT"));
                        assetMap.put("DATE_TO", curr_dt);
                        assetMap.put("CHARGESUI", "CHARGESUI");
                        header.setTaskParam(assetMap);
                        System.out.println("assetMap#######" + assetMap);
                        if (hashMap.containsKey("CALENDAR_FREQ") && hashMap.get("CALENDAR_FREQ") != null && hashMap.get("CALENDAR_FREQ").equals("Y")) {
                            InterestCalculationTask intcalTask = new InterestCalculationTask(header);
                            intcalTask.executeTask();
                        }
                        ASSET_STATUS = "SUB_STANDARD_ASSETS";
                        asset_status = 2;
                    }
                }
                //                              period=CommonUtil.convertObjToInt(hashMap.get("PERIOD_TRANS_SUBSTANDARD"));
                //                           if(DateUtil.dateDiff(DateUtil.addDays(instalDt,period),curr_dt)<0 && asset_status.equals("LOSS_ASSETS")){
                //                               ASSET_STATUS="SUB_STANDARD_ASSETS";
                //                           }


                //                              List lst=sqlMap.executeQueryForList("NPA_SELECT_STD_TO_SUBSTD",paramMap);


                //                        for(  i=0;i<lst.size();i++){
                //                            hash=(HashMap)lst.get(i);


                if (ASSET_STATUS != null && ASSET_STATUS.length() > 0) {
                    hashMap.put("CURR_STATUS", ASSET_STATUS);
                    hashMap.put("TO_DATE", paramMap.get("TODAY_DT"));
                    hashMap.put("TODAY_DT", paramMap.get("TODAY_DT"));
                    //                        hash.put("STATUS_BY","TTSYSTEM");
                    hashMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
                    sqlMap.executeUpdate("INSERT_NPA_HISTORY", hashMap);
                    System.out.println("finalmap####" + hashMap);
                    hashMap.put("ACT_NUM", hashMap.get("ACCT_NUM"));
                    sqlMap.executeUpdate("NPA_std_to_substdtest", hashMap);
                }

                //                        }


                /*
                 * sqlMap.executeUpdate("NPA_std_to_substd", map); //
                 * lst=sqlMap.executeQueryForList("NPA_SELECT_SUBSTD_TO_DOUBT_1",paramMap);
                 * sqlMap.executeUpdate("NPA_substd_to_doubt_1", map); hash=new
                 * HashMap(); for( i=0;i<lst.size();i++){
                 * hash=(HashMap)lst.get(i);
                 * hash.put("CURR_STATUS",ASSET_STATUS);
                 * hash.put("TO_DATE",paramMap.get("TODAY_DT")); //
                 * hash.put("STATUS_BY","TTSYSTEM");
                 * hash.put("AUTHORIZE_STATUS","AUTHORIZED");
                 * sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash); }
                 *
                 * lst=sqlMap.executeQueryForList("NPA_SELECT_SUBSTD_TO_DOUBT_2",paramMap);
                 * sqlMap.executeUpdate("NPA_substd_to_doubt_2",map); hash=new
                 * HashMap(); for(i=0;i<lst.size();i++){
                 * hash=(HashMap)lst.get(i);
                 * hash.put("CURR_STATUS","DOUBTFUL_ASSETS_2");
                 * hash.put("TO_DATE",paramMap.get("TODAY_DT")); //
                 * hash.put("STATUS_BY","TTSYSTEM");
                 * hash.put("AUTHORIZE_STATUS","AUTHORIZED");
                 * sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash); }
                 *
                 * lst=sqlMap.executeQueryForList("NPA_SELECT_SUBSTD_TO_DOUBT_3",paramMap);
                 * sqlMap.executeUpdate("NPA_substd_to_doubt_3",map); hash=new
                 * HashMap(); for(i=0;i<lst.size();i++){
                 * hash=(HashMap)lst.get(i);
                 * hash.put("CURR_STATUS","DOUBTFUL_ASSETS_3");
                 * hash.put("TO_DATE",paramMap.get("TODAY_DT")); //
                 * hash.put("STATUS_BY","TTSYSTEM");
                 * hash.put("AUTHORIZE_STATUS","AUTHORIZED");
                 * sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash); }
                 *
                 * lst=sqlMap.executeQueryForList("NPA_SELECT_DOUBT_3_TO_LOSS",paramMap);
                 * sqlMap.executeUpdate("NPA_doubt_3_to_loss", paramMap);
                 * hash=new HashMap(); for( i=0;i<lst.size();i++){
                 * hash=(HashMap)lst.get(i);
                 * hash.put("CURR_STATUS","LOSS_ASSETS");
                 * hash.put("TO_DATE",paramMap.get("TODAY_DT")); //
                 * hash.put("STATUS_BY","TTSYSTEM");
                 * hash.put("AUTHORIZE_STATUS","AUTHORIZED");
                 * sqlMap.executeUpdate("INSERT_NPA_HISTORY",hash); }
                 */
            }
            //            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
        curr_dt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    public Date getInstallmentDate(HashMap paramMap) throws Exception {
        HashMap allInstallmentMap = new HashMap();
        Date inst_dt = null;
        System.out.println("paramMap#####" + paramMap);
        String behaveLike = CommonUtil.convertObjToStr(paramMap.get("BEHAVES_LIKE"));
        //        paramMap.put("ACT_NUM",hash.get("ACCT_NUM"));
        if (behaveLike != null && (!behaveLike.equals("OD"))) {
            List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", paramMap);

            allInstallmentMap = (HashMap) paidAmt.get(0);
            double totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            paidAmt = sqlMap.executeQueryForList("getIntDetails", paramMap);
            if (paidAmt != null && paidAmt.size() > 0) {
                allInstallmentMap = (HashMap) paidAmt.get(0);
            }
            double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
            //            if(totPrinciple >0){
            totPrinciple += totExcessAmt;
            List allLst = sqlMap.executeQueryForList("getAllLoanInstallment", paramMap);
            inst_dt = null;
            for (int i = 0; i < allLst.size(); i++) {
                allInstallmentMap = (HashMap) allLst.get(i);
                double instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                if (instalAmt <= totPrinciple) {
                    totPrinciple -= instalAmt;

                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);

                } else {
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    totPrinciple += CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                    break;
                }

            }

            //            return inst_dt;
            //            }
        } else {
            String asset_status = CommonUtil.convertObjToStr(paramMap.get("ASSET_STATUS"));
            Date curr_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("TODAY_DT")));
            Date previus_dt = null;
            List transDetails = sqlMap.executeQueryForList("getFirstTranDetails", paramMap);
            //System.out.println("getFirstTranDetails####" + transDetails);
            if (asset_status.equals("STANDARD_ASSETS")) {
                if (paramMap.get("PERIOD_TRANS_SUBSTANDARD") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_SUBSTANDARD")));
                }
            }
            //                                 else
            //                                     previus_dt=DateUtil.addDays(curr_dt,0);

            if (asset_status.equals("SUB_STANDARD_ASSETS")) {
                if (paramMap.get("PERIOD_TRANS_DOUBTFUL") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL")));
                }
            }
            //                                 else
            //                                     previus_dt=DateUtil.addDays(curr_dt,0);

            if (asset_status.equals("DOUBTFUL_ASSETS")) {
                if (paramMap.get("PERIOD_TRANS_DOUBTFUL_1") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL_1")));
                }
            }
            //                                 else
            //                                     previus_dt=DateUtil.addDays(curr_dt,0);
            if (asset_status.equals("DOUBTFUL_ASSETS_1")) {
                if (paramMap.get("PERIOD_TRANS_DOUBTFUL_2") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL_2")));
                }
            }
            //                                 else
            //                                     previus_dt=DateUtil.addDays(curr_dt,0);

            if (asset_status.equals("DOUBTFUL_ASSETS_2")) {
                if (paramMap.get("PERIOD_TRANS_DOUBTFUL_3") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL_3")));
                }
            }
            //                                 else
            //                                     previus_dt=DateUtil.addDays(curr_dt,0);

            if (asset_status.equals("DOUBTFUL_ASSETS_3")) {
                if (paramMap.get("PERIOD_TRANS_LOSS") != null) {
                    previus_dt = DateUtil.addDays(curr_dt, -CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_LOSS")));
                }
            }
            //                                 else
            //                                     previus_dt=DateUtil.addDays(curr_dt,0);

            Date transdt = null;
            if (transDetails != null && transDetails.size() > 0) {
                HashMap transDetailsMap = (HashMap) transDetails.get(0);
                transdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(transDetailsMap.get("TRANS_DT")));
            }
            System.out.println("previusdate" + previus_dt + "transdt" + transdt);
            System.out.println("DateUtil.dateDiff(previus_dt,transdt)<0" + DateUtil.dateDiff(previus_dt, transdt));
            if (transdt != null && previus_dt != null && DateUtil.dateDiff(previus_dt, transdt) < 0) {

                //                         if(asset_status.equals("STANDARD_ASSETS"))
                //                         {
                GregorianCalendar firstdaymonth = new GregorianCalendar(1, previus_dt.getMonth() + 1, previus_dt.getYear() + 1900);
                int noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                GregorianCalendar lastdaymonth = new GregorianCalendar(previus_dt.getYear() + 1900, previus_dt.getMonth() + 1, noOfDays);
                paramMap.put("FROM_DT", firstdaymonth.getTime());
                paramMap.put("TO_DATE", lastdaymonth.getTime());
                System.out.println("paramMap###" + paramMap);
                List gettotCredit = sqlMap.executeQueryForList("getTotTranAmt", paramMap);
                System.out.println("getTotTranAmt####" + transDetails);
                if (gettotCredit != null && gettotCredit.size() > 0) {
                    GregorianCalendar debitIntMonth = new GregorianCalendar(previus_dt.getYear() + 1900, previus_dt.getMonth(), 1);
                    noOfDays = debitIntMonth.getActualMaximum(debitIntMonth.DAY_OF_MONTH);
                    GregorianCalendar debitlastdaymonth = new GregorianCalendar(previus_dt.getYear() + 1900, previus_dt.getMonth(), noOfDays);
                    paramMap.put("FROM_DT", debitIntMonth.getTime());
                    paramMap.put("TO_DATE", debitlastdaymonth.getTime());
                    System.out.println("getTotTranAmt####2paramMap" + paramMap);
                    List lst = sqlMap.executeQueryForList("getDebitTranAmt", paramMap);
                    if (lst != null && lst.size() > 0) {
                        HashMap hash = (HashMap) lst.get(0);
                        HashMap totCredit = (HashMap) gettotCredit.get(0);
                        double ibal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                        double totCreditamt = CommonUtil.convertObjToDouble(totCredit.get("AMOUNT")).doubleValue();
                        if (totCreditamt >= ibal) {
                            inst_dt = null;
                        } else {
                            inst_dt = DateUtil.getDateWithoutMinitues(ServerUtil.getCurrentDate(_branchCode));
                        }
                    } else {
                        inst_dt = null;
                    }
                }

                //                         }else if(asset_status.equals("SUB_STANDARD_ASSETS")){
                //                             previus_dt=DateUtil.addDays(curr_dt,CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL")));
                //
                //                         }else if(asset_status.equals("DOUBTFUL_ASSETS_1")){
                //                             previus_dt=DateUtil.addDays(curr_dt,CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL_2")));
                //                         }else if(asset_status.equals("DOUBTFUL_ASSETS_2")){
                //                             previus_dt=DateUtil.addDays(curr_dt,CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_DOUBTFUL_3")));
                //                         }else if(asset_status.equals("DOUBTFUL_ASSETS_3")){
                //                             previus_dt=DateUtil.addDays(curr_dt,CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_LOSS")));
                //                         }else if(asset_status.equals("LOSS_ASSETS")){
                //                             previus_dt=DateUtil.addDays(curr_dt,CommonUtil.convertObjToInt(paramMap.get("PERIOD_TRANS_SUBSTANDARD")));
                //                         }
            } else {
                inst_dt = null;
            }
        }
        return inst_dt;
    }

    private void destroyObjects() {
        logDAO = null;
        logTO = null;
        objTermLoanBorrowerTO = null;
//        objTermLoanCompanyTO = null;
        jointAcctMap = null;
//        netWorthDetailsMap = null;
        authorizedMap = null;
        poaMap = null;
        sanctionMap = null;
        sanctionFacilityMap = null;
        facilityMap = null;
        securityMap = null;
        repaymentMap = null;
        installmentMap = null;
        installmentMultIntMap = null;
//        guarantorMap = null;
//        guaranInstitMap=null;
        documentMap = null;
        interestMap = null;
        additionalSanMap = null;
        objTermLoanClassificationTO = null;
        objTermLoanSecurityTO = null;
//        objTermLoanOtherDetailsTO = null;
        objAuthorizedSignatoryDAO = null;
        objPowerOfAttorneyDAO = null;
        appraiserId = "";        
    }

    private ArrayList loanAuthorizeTimeTransaction(HashMap dataMap) throws Exception {
        ArrayList cashList = new ArrayList();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            String particulars = "";
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            if (dataMap.containsKey("LOANDEBIT")) {
                objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
                objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.LOANS));
                objCashTO.setTokenNo(CommonUtil.convertObjToStr(dataMap.get("TOKEN_NO")));
                objCashTO.setTransType(CommonConstants.DEBIT);
                objCashTO.setSingleTransId(generateSingleCashIdForDebit);//20-03-2014
                //akhil@
                objCashTO.setScreenName("Gold Loan Account Opening");
//                  if(dataMap.containsKey("OLD_ACCT_NUM") && dataMap.get("OLD_ACCT_NUM") !=null){
//                    objCashTO.setParticulars("To Cash : "+dataMap.get("OLD_ACCT_NUM"));
//                }else{
                objCashTO.setParticulars("To Disbursement Cash : ");//+dataMap.get("ACCT_NUM")
//                }
//                objCashTO.setParticulars("To Cash : "+dataMap.get("ACCT_NUM"));
                objCashTO.setAuthorizeRemarks("DP");
                objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            } else {
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
                objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
                objCashTO.setTransType(CommonConstants.CREDIT);
                
                objCashTO.setSingleTransId(generateSingleCashIdForCredit);
                
//                if (isRenewal && dataMap.containsKey("DEDUCTION_ACCU")
//                        && dataMap.get("DEDUCTION_ACCU").equals("O")) {
//                    objCashTO.setSingleTransId(generateSingleTransId);
//                } else {
//                    objCashTO.setSingleTransId(generateSingleCashIdForCredit);//20-03-
//                }
//                if(dataMap.containsKey("OLD_ACCT_NUM") && dataMap.get("OLD_ACCT_NUM") !=null){
//                    objCashTO.setParticulars("By Cash : "+dataMap.get("OLD_ACCT_NUM"));
//                }else{
                if (dataMap.containsKey("PARTICULARS") && dataMap.get("PARTICULARS") != null) {
                    particulars = CommonUtil.convertObjToStr(dataMap.get("PARTICULARS"));
                }
                if (isRenewal) {
                    System.out.println("OLD ACC No: "+dataMap.get("OLD_ACCT_NUM") +" NEW acc No:"+dataMap.get("ACCT_NUM"));
                    if(dataMap.containsKey("DEDUCTION_ACCU")
                        && dataMap.get("DEDUCTION_ACCU").equals("C")){
                        objCashTO.setParticulars("By Cash : "+dataMap.get("OLD_ACCT_NUM")+":"+particulars);
                    }
                    else {
                        if(dataMap.containsKey("NEW_ACT_NUM") && dataMap.get("NEW_ACT_NUM")!= null){
                           objCashTO.setParticulars(CommonUtil.convertObjToStr(dataMap.get("NEW_ACT_NUM")) + ":" + particulars);
                        }else{
                           objCashTO.setParticulars(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")) + ":" + particulars); 
                        }
                    }
                }
                else{
                      objCashTO.setParticulars(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")) + " : " + particulars);
                }
                objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            }
            //objCashTO.setSingleTransId(generateSingleTransId);//20-03-2014
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setStatusDt(ServerUtil.getCurrentDate(_branchCode));
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            if (dataMap.containsKey("OLD_ACCT_NUM") && dataMap.get("OLD_ACCT_NUM") != null) {//20-03-2014
                objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("OLD_ACCT_NUM")));
            } else {
                objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
            }
            
            if(isRenewal){// Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                if(dataMap.containsKey("DEDUCTION_ACCU")
                        && dataMap.get("DEDUCTION_ACCU").equals("O")){
                    if(dataMap.containsKey("NEW_ACT_NUM") && dataMap.get("NEW_ACT_NUM")!= null){
                      objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("NEW_ACT_NUM")));
                    }else{
                      objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("OLD_ACCT_NUM")));   
                    }
                }else{
                   objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("OLD_ACCT_NUM"))); 
                }
            }
            
            if (isRenewal) {
                objCashTO.setInstrumentNo2("LOAN_ACT_CLOSING_CHARGE");
            } else {
                objCashTO.setInstrumentNo2("LOAN_ACT_OPENING_CHARGE");
            }
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            // objCashTO.setSingleTransId(CommonUtil.convertObjToStr(dataMap.get("SINGLE_TRANS_ID")));//20-03-2014
            objCashTO.setCommand("INSERT");
            objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
            System.out.println("objCashTO 1st one:" + objCashTO);
            cashList.add(objCashTO);
        }
        return cashList;
    }

    private ArrayList loanAuthorizeTimeTransactionCash(HashMap dataMap) throws Exception {
        ArrayList cashList = new ArrayList();
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            if (dataMap.containsKey("LOANDEBIT")) {
                objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
                objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.LOANS));
                objCashTO.setTokenNo(CommonUtil.convertObjToStr(dataMap.get("TOKEN_NO")));
                objCashTO.setTransType(CommonConstants.DEBIT);
                objCashTO.setSingleTransId(generateSingleCashIdForDebit);//20-03-2014//UnCommented by nithya for Slip print issue
                //objCashTO.setSingleTransId(generateSingleTransId); //To be checked - mamala - 22-05-2024
                //akhil@
                objCashTO.setScreenName("Gold Loan Account Opening");
//                  if(dataMap.containsKey("OLD_ACCT_NUM") && dataMap.get("OLD_ACCT_NUM") !=null){
//                    objCashTO.setParticulars("To Cash : "+dataMap.get("OLD_ACCT_NUM"));
//                }else{
                objCashTO.setParticulars("To Disbursement Cash : ");//+dataMap.get("ACCT_NUM")
//                }
//                objCashTO.setParticulars("To Cash : "+dataMap.get("ACCT_NUM"));
                objCashTO.setAuthorizeRemarks("DP");
                objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
                // Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
                if(isRenewal && dataMap.containsKey("LOAN_DEBIT_THROUGH_CASH") && dataMap.get("LOAN_DEBIT_THROUGH_CASH") != null && dataMap.get("LOAN_DEBIT_THROUGH_CASH").equals("LOAN_DEBIT_THROUGH_CASH")){
                    if(dataMap.containsKey("NEW_ACT_NUM") && dataMap.get("NEW_ACT_NUM")!= null){
                      objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("NEW_ACT_NUM")));
                    }else{
                      objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("OLD_ACCT_NUM")));   
                    }
                }
                objCashTO.setGlTransActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));// Added by nithya on 15-10-2018 for (KD-287) Gold Loan renewal - New Loan A/C Number's GL Trans Act Num is old Number. Needed to be the GL Trans act num should be New A/C No
            } else {
                if(dataMap.containsKey("SCREEN_NAME"))
                {
                     objCashTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get("SCREEN_NAME")));
                }
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
                objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.LOANS));
                objCashTO.setTransType(CommonConstants.CREDIT);
                objCashTO.setSingleTransId(generateSingleCashIdForCredit);//20-03-2014 //Uncommented by nithya for slip print issue
                // objCashTO.setSingleTransId(generateSingleTransId);// To be checked - mamala - 22-05-2024
                objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
//                if(dataMap.containsKey("OLD_ACCT_NUM") && dataMap.get("OLD_ACCT_NUM") !=null){
//                    objCashTO.setParticulars("By Cash : "+dataMap.get("OLD_ACCT_NUM"));
//                }else{
                objCashTO.setParticulars("By Cash : " + dataMap.get("ACCT_NUM"));
                objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
//                }
            }
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setStatusDt(ServerUtil.getCurrentDate(_branchCode));
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
//            if(dataMap.containsKey("OLD_ACCT_NUM") && dataMap.get("OLD_ACCT_NUM") !=null){
//                objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("OLD_ACCT_NUM")));
//            }else{
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("OLD_ACCT_NUM")));
            // Added by nithya for KD-3775 [ Gold loan insurance charge - opening time ]
            if (isRenewal && dataMap.containsKey("LOAN_DEBIT_THROUGH_CASH") && dataMap.get("LOAN_DEBIT_THROUGH_CASH") != null && dataMap.get("LOAN_DEBIT_THROUGH_CASH").equals("LOAN_DEBIT_THROUGH_CASH")) {
                if (dataMap.containsKey("OPENING_CHARGE_CASH") && dataMap.containsKey("NEW_ACT_NUM") && dataMap.get("NEW_ACT_NUM") != null) {//change to be verified...
                    objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("NEW_ACT_NUM")));
                }
            }
            // objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
//            }
            objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setTransModType(CommonUtil.convertObjToStr(dataMap.get("TRANS_MOD_TYPE")));
            //objCashTO.setSingleTransId(CommonUtil.convertObjToStr(dataMap.get("SINGLE_TRANS_ID")));//20-03-2014
            objCashTO.setCommand("INSERT");
            objCashTO.setIbrHierarchy(CommonUtil.convertObjToStr(ibrHierarchy++));
            System.out.println("objCashTO 1st one:" + objCashTO);
            cashList.add(objCashTO);
        }
        return cashList;
    }

    public static void main(String[] arg) {
        try {
            HashMap data = new HashMap();
            data.put("WHERE", "LA00000000001061");
            data.put("KEY_VALUE", "ACCOUNT_NUMBER");
            new GoldLoanDAO().executeQuery(data);
            data = null;
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private void deleteSMSSubscription(SMSSubscriptionTO objSMSSubscriptionTO) throws Exception {
        HashMap checkMap = new HashMap();
        checkMap.put("PROD_TYPE", "TL");
        checkMap.put("PROD_ID", objSMSSubscriptionTO.getProdId());
        checkMap.put("ACT_NUM", objSMSSubscriptionTO.getActNum());
        checkMap.put(CommonConstants.USER_ID, objSMSSubscriptionTO.getStatusBy());
        sqlMap.executeUpdate("deleteSMSSubscriptionMap", checkMap);
        checkMap.clear();
        checkMap = null;
    }
    
    // Added by nithya on 09-10-2018 for KD 263 GST - Needed to implement the GST in GOld Loan renewal
    private void insertServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        try {
            HashMap mapDel = new HashMap();
            mapDel.put("ACCT_NUM", objserviceTaxDetailsTO.getAcct_Num());
            mapDel.put("CREATED_DT", curr_dt);
            sqlMap.executeUpdate("deleteServiceTaxDetails", mapDel);
            objserviceTaxDetailsTO.setServiceTaxDet_Id(getServiceTaxNo());            
            objserviceTaxDetailsTO.setParticulars("Gold Loan Renewal");          
            sqlMap.executeUpdate("insertServiceTaxDetailsTO", objserviceTaxDetailsTO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }    
    private String getServiceTaxNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SERVICETAX_DET_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    
    private void setDriver() throws Exception {
        java.util.Properties serverProperties = new java.util.Properties();
        try {
            Dummy cons = new Dummy();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/serverutil/SqlMapConfig.properties"));
            dataBaseURL = serverProperties.getProperty("url");
            userName = serverProperties.getProperty("username");
            passWord = serverProperties.getProperty("password");
            driverName = serverProperties.getProperty("driver");
            Class.forName(serverProperties.getProperty("driver"));
            serverProperties = new java.util.Properties();
            serverProperties.load(cons.getClass().getResourceAsStream("/com/see/truetransact/commonutil/TT.properties"));
            SERVER_ADDRESS = "http://" + CommonConstants.SERVER_IP_ADDR + ":" + serverProperties.getProperty("HTTP_PORT") + "/" + serverProperties.getProperty("HTTP_CONTEXT");
            System.out.println("#### SERVER_ADDRESS : " + SERVER_ADDRESS);
            serverProperties = null;
            cons = null;
        } catch (Exception ex) {
        }
    }
    
    // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
     private void storePhotoSignFilesInServer() throws Exception {
        photoFile = new File(ServerConstants.SERVER_PATH + "/goldloan/" + acct_No + "_stock.jpg");
        writer = new FileOutputStream(photoFile);
//        FileOutputStream writer = new FileOutputStream(file);
        System.out.println("#### inside storePhotoSignFilesInServer() After FileOutputStream creation...");
//        System.out.println("##### byteArray : "+byteArray);
        if (photoByteArray != null) {
            writer.write(photoByteArray);
            System.out.println("##### inside writer.write(photoByteArray) :" + photoByteArray.toString());
        } else {
            photoFile = null;
        }
        writer.flush();
        writer.close();       
    }

    
   // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option  
   private void storePhotoSign() throws Exception {
//        DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
        setDriver();
        DriverManager.registerDriver(((java.sql.Driver) Class.forName(driverName).newInstance()));

        storePhotoSignFilesInServer();
        conn = DriverManager.getConnection(dataBaseURL, userName, passWord);
        // @machineName:port:SID,   userid,  password
        conn.setAutoCommit(false);
     
        if (driverName.equals("com.ibm.db2.jcc.DB2Driver")) {
            
        } else if (driverName.equals("oracle.jdbc.OracleDriver") || driverName.equals("oracle.jdbc.driver.OracleDriver")) {
            try {
                stmt = conn.createStatement();
                boolean b = false;
                String st;
             
                    st = "UPDATE " + "SECURITY_DETAILS" + " SET STOCK_PHOTO_FILE = empty_blob() WHERE ACCT_NUM ='" + acct_No + "'";                    
                    //System.out.println("Update Statement executed : \n\t" + st);
                    b = stmt.execute(st);
                    //System.out.println("##### cmd.equals(CommonConstants.TOSTATUS_INSERT) so, update Statement executed..."); 
                    st = "SELECT STOCK_PHOTO_FILE, STOCK_PHOTO_FILE FROM " + "SECURITY_DETAILS" + " WHERE ACCT_NUM ='" + acct_No + "'";                
                    st = st + " FOR UPDATE";
                //System.out.println("Statement execute query : \n\t" + st);
                rset = stmt.executeQuery(st);
                //System.out.println("Statement execute query : " + rset);
                rset.next();
                //System.out.println("rset.next()... Photo.... ");
                BLOB oracleBlob = ((OracleResultSet) rset).getBLOB(1);
                //System.out.println("#$#$ photoFile : " + photoFile);
                //System.out.println("selected file length = " + (photoFile == null ? "null" : photoFile.length()));
                //System.out.println("cust_id = " + rset.getString(2));
                FileInputStream reader = null;
                OutputStream outstream = null;
                int size = 0;
                byte[] buffer;
                int length = 0;
//                int oldLength = 0;
                if (photoFile != null) {
                    reader = new FileInputStream(photoFile);
                    //System.out.println("reader initialized ");
                    outstream = oracleBlob.getBinaryOutputStream();
                    //System.out.println("outstream initialized ");
                    size = oracleBlob.getBufferSize();
                    buffer = new byte[size];
                    length = -1;
//                    oldLength = 0;
                    while ((length = reader.read(buffer)) != -1) {
                        //System.out.println("length : " + length);
                        outstream.write(buffer, 0, length);
//                        oldLength = length;
                    }
                    //System.out.println("outstream written ");
                    reader.close();
                    outstream.close();
                }
                //System.out.println("rset.next()... Signature.... ");           
                //System.out.println("outstream closed ");
                stmt.close();
                //System.out.println("stmt closed ");
                conn.commit();
                conn.close();
                conn = null;
                rset.close();
                if (photoFile != null) {
                    reader.close();
                }
                outstream = null;
                reader = null;
                oracleBlob = null;
                photoFile = null;              
            } catch (Exception se) {
                se.printStackTrace();
                System.out.println("SQL Exception : " + se);
                conn.close();
                stmt.close();
                conn = null;
                rset.close();
                photoFile = null;               
            }
        }else if (driverName.equals("org.postgresql.Driver")) {
           try {
               tableName = "SECURITY_DETAILS";
               tableCondition = "ACCT_NUM";
               String photoString = "";
               if (photoByteArray != null) {
                   System.out.println("#$#$ photoByteArray : " + photoByteArray);
                   photoFile = new File(ServerConstants.SERVER_PATH + "/goldloan/" + acct_No + "_stock.jpg");
               } else {
                   photoFile = null;
               }
               System.out.println("#$#$ photoFile : " + photoFile);
               if (photoFile != null) {
                   photoString = "?";
               } else {
                   if (driverName.equals("org.postgresql.Driver")) {//Added By Revathi 23-08-2024 to avoid empty_blob() does not exist issue.
                       photoString = "''";
                   } else {
                       photoString = "empty_blob()";
                   }
               }
               String st = "UPDATE " + tableName + " SET STOCK_PHOTO_FILE = " + photoString
                       + " WHERE " + tableCondition + "='" + acct_No + "'";
               PreparedStatement ps = conn.prepareStatement(st);
               FileInputStream photoFis = null;
               if (photoFile != null) {
                   int fileLength = (int) photoFile.length();
                   photoFis = new FileInputStream(photoFile);
                   ps.setBinaryStream(1, photoFis, fileLength);
                   System.out.println("#$#$ fileLength..." + fileLength);
               }
               System.out.println("#$#$ Update statement is : " + st);
               ps.executeUpdate();
               ps.close();
               conn.commit();
               conn.close();
               conn = null;
               photoByteArray = null;
               if (photoFile != null) {
                   photoFis.close();
               }
           } catch (Exception se) {
               se.printStackTrace();
               conn.close();
               conn = null;
               photoFile = null;
           }
       }
   }
   
   
   // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
   private void getStockPhoto(String actNo) throws Exception {
        try {
            mapPhotoSign = new HashMap();           
            setDriver();
            conn = DriverManager.getConnection(dataBaseURL, userName, passWord);            
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String st = "select ACCT_NUM,STOCK_PHOTO_FILE from SECURITY_DETAILS where " + "ACCT_NUM = '" + actNo + "'";            
            //System.out.println("#### statement to be executed : " + st);
            rset = stmt.executeQuery(st);
            //System.out.println("#### rset.getRow() " + rset.getRow());
            if (rset != null) {
                //System.out.println("#### rset not null");
                if (driverName.equals("org.postgresql.Driver")) {
                    while (rset.next()) {
                        System.out.println("#### rset not null" + rset);
                        File f1 = null;
                        f1 = new File(ServerConstants.SERVER_PATH + "\\goldloan\\" + actNo + "_photo.jpg");
                        f1.createNewFile();
                        FileOutputStream proofPhotoFis = new FileOutputStream(f1);
                        byte[] buf = rset.getBytes("STOCK_PHOTO_FILE");
                        proofPhotoFis.write(buf, 0, buf.length);            
                        mapPhotoSign.put("PHOTO", buf);
                    }
                } 
                else{
                while (rset.next()) {
                   // System.out.println("#### rset in while");
                    java.sql.Blob oracleBlob = null;
                    File f1 = null;
                    FileOutputStream out = null;
                    InputStream in = null;
                    java.net.URI serverURI = null;
                    int fileLength = 0;
                    byte[] blobBytes;
                    int len = 0;
                    if (rset.getBlob(2) != null) {
                        //System.out.println("#### Cust ID : " + rset.getString(1));   // Print col 1
                        oracleBlob = rset.getBlob(2);                       
                        long length = oracleBlob.length();                       
                        //System.out.println("photo blob length " + length);
                        if (length != 0) {
                            //System.out.println("#### " + SERVER_ADDRESS + "goldloan/" + actNo + "_photo.jpg");
                            serverURI = new java.net.URI(SERVER_ADDRESS + "goldloan/" + actNo + "_photo.jpg");
                            f1 = new File(ServerConstants.SERVER_PATH + "\\goldloan\\" + actNo + "_photo.jpg");
                            f1.createNewFile();
                            out = new FileOutputStream(f1);
                            in = oracleBlob.getBinaryStream();                          
                            //System.out.println("fileLength " + fileLength);
                            fileLength = (int) length;
                            blobBytes = new byte[fileLength];
                            //System.out.println("blobBytes.length " + blobBytes.length);
                            len = -1;
                            in.read(blobBytes);
                            //System.out.println("inside while len " + len);
                            out.write(blobBytes);
                            out.close();
                            in.close();                         
                            mapPhotoSign.put("PHOTO", blobBytes);
                        }
                    }                                     
                    f1 = null;
                    out = null;
                    in = null;
                    oracleBlob = null;
                }
              }
            }           
            rset.close();
            stmt.close();
        } catch (Exception se) {
            se.printStackTrace();         
            rset.close();
            stmt.close();
            System.out.println("SQL Exception : " + se);
        }
    }
    
    // END
    
}
