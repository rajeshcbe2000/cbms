/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * BorrwingDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.termloan.loanapplicationregister;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.customer.CustomerHistoryDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.customer.CustomerHistoryTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.transferobject.termloan.*;
import com.see.truetransact.transferobject.termloan.loanapplicationregister.LoanApplicationTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * LoanApplication DAO
 *
 */
public class LoanApplicationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LoanApplicationTO objTO;
    //security details
    private HashMap facilityMap;
    private HashMap securityMap;
    private HashMap delRefMap = null;
    public String prod_id = "";
    private String acct_No;
    private HashMap depositCustDetMap;
    private String behaves_like = null;
    String userID = "";
    //  private TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO;
    // private LinkedHashMap memberTableDetails = null;
    private TermLoanSecurityMemberTO objMemberTypeTO;
    private TermLoanSecurityLandTO objTermLoanSecurityLandTO;
    private LinkedHashMap deletedMemberTableValues = null;
    private LinkedHashMap memberTableDetails = null;
    private LinkedHashMap deletedVehicleTableValues = null;
    private LinkedHashMap vehicleTableDetails = null;
    private LinkedHashMap deletedCollateralTableValues = null;
    private LinkedHashMap collateralTableDetails = null;
    private LinkedHashMap termLoanSecuritySalaryTOMap = null;
    private LinkedHashMap termLoanSecuritySalaryTODeletedMap = null;
    private TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO;
    private LinkedHashMap depositTableDetails = null;
    private LinkedHashMap deletedDepositTableValues = null;
    private LinkedHashMap losTableDetails = null;
    private LinkedHashMap deletedLosTableValues = null;
    //security end..
    private Date currDt = null;//trans details
    HashMap returnMap;//trans details
    private LogDAO logDAO;
    private LogTO logTO;
    public List chargeLst = null; //charge details
    private String loanType = "";//charge details
    private String appNo = "";//charge details
    private final String PROD_ID = "PROD_ID";//charge details
    private final String LIMIT = "LIMIT";//charge details
    private final String BEHAVES_LIKE = "BEHAVES_LIKE";//charge details
    private final String INT_GET_FROM = "INT_GET_FROM";//charge details
    private final String SECURITY_DETAILS = "SECURITY_DETAILS";//charge details
    private final static Logger log = Logger.getLogger(LoanApplicationDAO.class);
    private TransactionDAO transactionDAO = null; //trans details

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public LoanApplicationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();

    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        //charge details
        HashMap acHeads = new HashMap();
        TransactionDAO transactionDAO = null;
        TxTransferTO transferDao = null;
        ArrayList transList = new ArrayList();
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        if (map.containsKey("LOAN_TYPE")) {
            loanType = CommonUtil.convertObjToStr(map.get("LOAN_TYPE"));
//            productCategory = CommonUtil.convertObjToStr(map.get("PRODUCT_CATEGORY"));
        } else {
            loanType = "";
        }
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
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                double interestAmt = CommonUtil.convertObjToDouble(map.get("INTEREST_AMT")).doubleValue();
                transferDao = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                //                transferDao.setAuthorizeRemarks("DI");
                transList.add(transferDao);
                txMap.put(TransferTrans.CR_AC_HD, acHeads.get("INT_PAYABLE_ACHD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, map.get("BRANCH_CODE"));
                //DELETEING RECORD
                map.put("LINK_BATCH_ID", map.get("ACT_NUM"));
                map.put("TRANS_DT", currDt.clone());
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
                transList.add(transferDao);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transferTrans.setLoanDebitInt("DI");
                transferTrans.doDebitCredit(transList, branchId, false);
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
        //security details
        String whereMap = (String) map.get(CommonConstants.MAP_WHERE);
        returnMap.put("SECURITY_DETAILS", executeQueryForAuthorize(returnMap, whereMap, whereMap)); //security details
        //security end..
        HashMap where = new HashMap();//;(HashMap)map.get(CommonConstants.MAP_WHERE); //trans details
        where.put("APPLICATION_NO", map.get(CommonConstants.MAP_WHERE));
        List list = (List) sqlMap.executeQueryForList("getSelectLoanApplicationTO", where);
        returnMap.put("LoanApplicationTO", list);
        //trans details
        if (where.containsKey("APPLICATION_NO")) {
            HashMap getRemitTransMap = new HashMap();
            getRemitTransMap.put("TRANS_ID", where.get("APPLICATION_NO"));
            getRemitTransMap.put("TRANS_DT", currDt.clone());
            getRemitTransMap.put("BRANCH_CODE", _branchCode);
            list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
//            list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", where.get("REP_INT_CLS_NO"));
            if (list != null && list.size() > 0) {
                returnMap.put("TRANSACTION_LIST", list);
            }
        }
        //end.. 
        return returnMap;
    }

    //security details
    private HashMap executeQueryForAuthorize(HashMap returnMap, String where, String borrow_no) throws Exception {

        List list = (List) sqlMap.executeQueryForList("getSelectTermLoanSecuritySalaryTO", where);
        if (list != null && list.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                String st = CommonUtil.convertObjToStr(((TermLoanSecuritySalaryTO) list.get(j)).getSlNo());
                ParMap.put(((TermLoanSecuritySalaryTO) list.get(j)).getSlNo(), list.get(j));
            }
            returnMap.put("TermLoanSecuritySalaryTO", ParMap);

        }
        list = null;

        List memberList = (List) sqlMap.executeQueryForList("getSelectTermLoanSecurityMemberTO", where);
        if (memberList != null && memberList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = memberList.size(), j = 0; i > 0; i--, j++) {
                String st = ((TermLoanSecurityMemberTO) memberList.get(j)).getMemberNo();
                ParMap.put(((TermLoanSecurityMemberTO) memberList.get(j)).getMemberNo(), memberList.get(j));
            }
            returnMap.put("memberListTO", ParMap);
        }
        memberList = null;
        List vehicleList = (List) sqlMap.executeQueryForList("getSelectTermLoanSecurityVehicleTO", where);
        if (vehicleList != null && vehicleList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = vehicleList.size(), j = 0; i > 0; i--, j++) {
                String st = ((TermLoanSecurityVehicleTO) vehicleList.get(j)).getMemberNo();
                ParMap.put(((TermLoanSecurityVehicleTO) vehicleList.get(j)).getMemberNo(), vehicleList.get(j));
            }
            returnMap.put("TermLoanSecurityvehicleListTO", ParMap);
        }
        vehicleList = null;
        List collateralList = (List) sqlMap.executeQueryForList("getSelectTermLoanSecurityLandTO", where);
        if (collateralList != null && collateralList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = collateralList.size(), j = 0; i > 0; i--, j++) {
                String st = ((TermLoanSecurityLandTO) collateralList.get(j)).getMemberNo();
                ParMap.put(((TermLoanSecurityLandTO) collateralList.get(j)).getMemberNo() + "_" + (j + 1), collateralList.get(j));
            }
            returnMap.put("CollateralListTO", ParMap);
        }
        collateralList = null;

        List depositTypeList = (List) sqlMap.executeQueryForList("EditTermLoanDepositTypeDetaisTO", borrow_no);

        if (depositTypeList != null && depositTypeList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = depositTypeList.size(), j = 0; i > 0; i--, j++) {
                String st = ((TermLoanDepositTypeTO) depositTypeList.get(j)).getTxtDepNo();
                ParMap.put(((TermLoanDepositTypeTO) depositTypeList.get(j)).getTxtDepNo(), depositTypeList.get(j));
            }
            returnMap.put("DeositTypeDetails", ParMap);
        }
        depositTypeList = null;
        List losTypeList = (List) sqlMap.executeQueryForList("EditTermLoanLosTypeDetaisTO", borrow_no);
        if (losTypeList != null && losTypeList.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = losTypeList.size(), j = 0; i > 0; i--, j++) {
                String st = ((TermLoanLosTypeTO) losTypeList.get(j)).getLosSecurityNo();
                ParMap.put(((TermLoanLosTypeTO) losTypeList.get(j)).getLosSecurityNo(), losTypeList.get(j));
            }
            returnMap.put("LosTypeDetails", ParMap);
        }
        losTypeList = null;
        list = (List) sqlMap.executeQueryForList("getSelectEditTermLoanFacilityTO", where);
        returnMap.put("TermLoanFacilityTO", list);
        list = null;
        //        returnMap.put("ACCT_NUM",objTO.getApplNo());
        System.out.println("returnMap>>>executeedittt>>>>" + returnMap);
        return returnMap;
    }
    //security end..

    //charge details
    private void executeTransactionPart(HashMap map, HashMap transDetailMap, List chargeLst) throws Exception {
        try {
            appNo = objTO.getApplNo();
            transDetailMap.put("ACCT_NUM", appNo);
            transDetailMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
            transDetailMap = getProdBehavesLike(transDetailMap);
            insertTimeTransaction(map, transDetailMap, chargeLst);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

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

    private void editTransaction(HashMap transMap) throws Exception {
        transMap.put(CommonConstants.BRANCH_ID, _branchCode);
        if (transMap.containsKey("CashTransactionTO")) {
            CashTransactionDAO objCashTransactionDAO = new CashTransactionDAO();
            transMap.put("DEBIT_LOAN_TYPE", "DP");
            objCashTransactionDAO.execute(transMap, false);
        } else if (transMap.containsKey("TxTransferTO")) {
            TransferDAO objTransferDAO = new TransferDAO();
            objTransferDAO.execute(transMap, false);

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

    private void insertTimeTransaction(HashMap map, HashMap dataMap, List chargeLst) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap acHeads = new HashMap();
        TransactionDAO transactionDAO = null;
        TxTransferTO transferDao = null;
        ArrayList transList = new ArrayList();
        HashMap txMap = new HashMap();
        TransferTrans transferTrans = new TransferTrans();
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        TransactionTO objTransactionTO = null;
        // if(dataMap.containsKey("TRANSACTION_PART")){
        HashMap reserchMap = new HashMap();
        String generateSingleCashId = "";
        String generateSingleTransId = generateLinkID();
        try {
            ///TransactionTO objTransactionTO = (TransactionTO) dataMap.get("TransactionTO");
            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            if (TransactionDetailsMap.size() > 0) {
                if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                    //  for (int J = 1;J <= allowedTransDetailsTO.size();J++){
                    if (allowedTransDetailsTO != null) {
                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                    }
                }
            }
            if (objTransactionTO != null) {
                String branchId = CommonUtil.convertObjToStr(objTransactionTO.getBranchId());
                dataMap.put("ACT_NUM", objTransactionTO.getDebitAcctNo());
                if (objTransactionTO.getTransType() != null && objTransactionTO.getTransType().equals("CASH")) {
                    generateSingleCashId = generateLinkID();
                    HashMap loanAuthTransMap = new HashMap();

                    // ArrayList loanAuthTransList = loanAuthorizeTimeTransaction(loanAuthTransMap);
                    ArrayList loanAuthTransList = new ArrayList();
                    loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                    loanAuthTransMap.put("BRANCH_CODE", objTransactionTO.getBranchId());

                    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
                    HashMap cashMap = new HashMap();
                    cashMap.put("BRANCH_CODE", objTransactionTO.getBranchId());
                    String authorizeStatus = CommonConstants.STATUS_AUTHORIZED;
                    ArrayList arrList = new ArrayList();
                    HashMap authDataMap = new HashMap();
                    HashMap authStatusMap = new HashMap();;
                    //Added By Suresh
                    if (chargeLst != null && chargeLst.size() > 0) {   //Charge Details Transaction
                        for (int i = 0; i < chargeLst.size(); i++) {
                            HashMap chargeMap = new HashMap();
                            String accHead = "";
                            double chargeAmt = 0;
                            chargeMap = (HashMap) chargeLst.get(i);
                            accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                            chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                            loanAuthTransMap.remove("LOANDEBIT");
                            loanAuthTransMap.remove("PROD_ID");
                            loanAuthTransMap.remove("DEBIT_LOAN_TYPE");
                            loanAuthTransMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
                            loanAuthTransMap.put("ACCT_HEAD", accHead);
                            loanAuthTransMap.put("LIMIT", String.valueOf(chargeAmt));
                            loanAuthTransMap.put("USER_ID", userID);
                            loanAuthTransMap.put("TODAY_DT", currDt.clone());
                            loanAuthTransMap.put("SINGLE_TRANS_ID", generateSingleCashId);
                            loanAuthTransList.add(loanAuthorizeTimeTransaction1(loanAuthTransMap));
                        }
                        if (map.containsKey("serviceTaxDetails")) {
                            HashMap Taxdetails = (HashMap) map.get("serviceTaxDetails");

                            // Revised code for GST - Added by nithya
                            double serviceTax = 0.0;
                            double swachhCess = 0.0;
                            double krishikalyanCess = 0.0;
                            double taxAmt = 0.0;
                            double normalServiceTax = 0.0;
                            if (Taxdetails.containsKey("TOT_TAX_AMT") && Taxdetails.get("TOT_TAX_AMT") != null) {
                                taxAmt = CommonUtil.convertObjToDouble(Taxdetails.get("TOT_TAX_AMT"));
                            }
                            if (Taxdetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && Taxdetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                swachhCess = CommonUtil.convertObjToDouble(Taxdetails.get(ServiceTaxCalculation.SWACHH_CESS));
                            }
                            if (Taxdetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && Taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                krishikalyanCess = CommonUtil.convertObjToDouble(Taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                            }
                            if (Taxdetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && Taxdetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(Taxdetails.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                            //taxAmt-=(swachhCess+krishikalyanCess);                            
                            if (swachhCess > 0) {
                                loanAuthTransMap.remove("LOANDEBIT");
                                loanAuthTransMap.remove("PROD_ID");
                                loanAuthTransMap.remove("DEBIT_LOAN_TYPE");
                                loanAuthTransMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
                                loanAuthTransMap.put("ACCT_HEAD", Taxdetails.get(ServiceTaxCalculation.SWACHH_HEAD_ID));
                                loanAuthTransMap.put("LIMIT", swachhCess);
                                loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                                loanAuthTransMap.put("TODAY_DT", currDt.clone());
                                loanAuthTransMap.put("INSTRUMENT_NO2", "CGST");
                                loanAuthTransMap.put("PARTICULARS", "CGST");
                                loanAuthTransMap.put("SINGLE_TRANS_ID", generateSingleCashId);
                                loanAuthTransList.add(loanAuthorizeTimeTransaction(loanAuthTransMap));
                                //taxAmt -= swachhCess;
                            }
                            if (krishikalyanCess > 0) {
                                loanAuthTransMap.remove("LOANDEBIT");
                                loanAuthTransMap.remove("PROD_ID");
                                loanAuthTransMap.remove("DEBIT_LOAN_TYPE");
                                loanAuthTransMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
                                loanAuthTransMap.put("ACCT_HEAD", Taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_HEAD_ID));
                                loanAuthTransMap.put("LIMIT", krishikalyanCess);
                                loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                                loanAuthTransMap.put("TODAY_DT", currDt.clone());
                                loanAuthTransMap.put("SINGLE_TRANS_ID", generateSingleCashId);
                                loanAuthTransMap.put("INSTRUMENT_NO2", "SGST");
                                loanAuthTransMap.put("PARTICULARS", "SGST");
                                loanAuthTransList.add(loanAuthorizeTimeTransaction(loanAuthTransMap));
                                //taxAmt -= krishikalyanCess;
                            }
                            if (normalServiceTax > 0) {
                                loanAuthTransMap.remove("LOANDEBIT");
                                loanAuthTransMap.remove("PROD_ID");
                                loanAuthTransMap.remove("DEBIT_LOAN_TYPE");
                                loanAuthTransMap.put("ACCT_NUM", dataMap.get("ACCT_NUM"));
                                loanAuthTransMap.put("ACCT_HEAD", Taxdetails.get("TAX_HEAD_ID"));
                                loanAuthTransMap.put("LIMIT", normalServiceTax);
                                loanAuthTransMap.put("USER_ID", dataMap.get("USER_ID"));
                                loanAuthTransMap.put("TODAY_DT", currDt.clone());
                                loanAuthTransMap.put("SINGLE_TRANS_ID", generateSingleCashId);
                                loanAuthTransMap.put("INSTRUMENT_NO2", "SERVICETAX_CHARGE");
                                loanAuthTransMap.put("PARTICULARS", "SERVICETAX CHARGE");
                                loanAuthTransList.add(loanAuthorizeTimeTransaction(loanAuthTransMap));
                            }
                            // End
                        }
                        cashTransactionDAO = new CashTransactionDAO();
                        loanAuthTransMap.put("BRANCH_CODE", _branchCode);
                        cashMap.put("BRANCH_CODE", objTransactionTO.getBranchId());
                        loanAuthTransMap.put("DAILYDEPOSITTRANSTO", loanAuthTransList);
                        cashMap = cashTransactionDAO.execute(loanAuthTransMap, false);
                    }
                    arrList = null;
                    returnMap = null;
                    acHeads = null;
                    txMap = null;
                    reserchMap = null;
                    loanAuthTransMap = null;
                    authDataMap = null;
                    authStatusMap = null;
                } else if (objTransactionTO.getTransType() != null && objTransactionTO.getTransType().equals("TRANSFER")) {
                    transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                    transactionDAO.setInitiatedBranch(branchId);
                    //  TransactionTO objTransactionTO = null;

                    HashMap crMap = new HashMap();
                    crMap.put("ACT_NUM", objTransactionTO.getDebitAcctNo());//"ACCT_NUM
                    List oaAcctHead = sqlMap.executeQueryForList("getAccNoProdIdDet", crMap);
                    if (oaAcctHead != null && oaAcctHead.size() > 0) {
                        crMap = (HashMap) oaAcctHead.get(0);
                    }
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    TxTransferTO transferTo = new TxTransferTO();
                    double interestAmt = CommonUtil.convertObjToDouble(dataMap.get("LIMIT")).doubleValue();
                    txMap.put(TransferTrans.DR_ACT_NUM, objTransactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_AC_HD, acHeads.get("ACCT_HEAD"));
                    //  txMap.put(TransferTrans.DR_PROD_ID, dataMap.get("PROD_ID"));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
                    txMap.put(TransferTrans.DR_BRANCH, objTransactionTO.getBranchId());
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put("DR_INST_TYPE", "VOUCHER");
                    txMap.put("AUTHORIZEREMARKS", "DP");
                    txMap.put("DEBIT_LOAN_TYPE", "DP");

                    txMap.put(TransferTrans.CR_ACT_NUM, dataMap.get("ACCT_NUM"));
                    txMap.put(TransferTrans.CR_AC_HD, crMap.get("AC_HD_ID"));
                    txMap.put(TransferTrans.CR_PROD_ID, crMap.get("PROD_ID"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OPERATIVE);
                    txMap.put(TransferTrans.CR_BRANCH, objTransactionTO.getBranchId());
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                    transferTo.setSingleTransId(generateSingleTransId);
                    // transferList.add(transferTo);
                    txMap.put(TransferTrans.PARTICULARS, dataMap.get("ACT_NUM"));
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, interestAmt);
                    transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                    transferTo.setStatusBy(CommonUtil.convertObjToStr(userID));
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(userID));
                    transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    //Added By Suresh
                    if (chargeLst != null && chargeLst.size() > 0) {   //Charge Details Transaction
                        transferList = new ArrayList();
                        double totchargeAmt = 0;
                        for (int i = 0; i < chargeLst.size(); i++) {
                            HashMap chargeMap = new HashMap();
                            String accHead = "";
                            double chargeAmt = 0;
                            String chargeType = "";
                            chargeMap = (HashMap) chargeLst.get(i);
                            accHead = CommonUtil.convertObjToStr(chargeMap.get("ACC_HEAD"));
                            chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT")).doubleValue();
                            chargeType = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_DESC"));
                            totchargeAmt = totchargeAmt + chargeAmt;
                            txMap = new HashMap();
                            transferTo = new TxTransferTO();
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put("DR_INST_TYPE", "VOUCHER");
                            txMap.put(TransferTrans.CR_AC_HD, accHead);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.CR_BRANCH, objTransactionTO.getBranchId());
                            txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                            txMap.put(TransferTrans.PARTICULARS, chargeType + " - : " + dataMap.get("ACT_NUM"));
                            transferTo = transactionDAO.addTransferCreditLocal(txMap, chargeAmt);
                            transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                            transferTo.setStatusBy(CommonUtil.convertObjToStr(userID));
                            transferTo.setInitTransId(CommonUtil.convertObjToStr(userID));
                            transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                            transferTo.setSingleTransId(generateSingleTransId);
                            transferTo.setTransModType(TransactionFactory.GL);
                            transferList.add(transferTo);
                        }
                        if (map.containsKey("serviceTaxDetails")) {
                            HashMap Taxdetails = (HashMap) map.get("serviceTaxDetails");
                            // Revised code for Service tax to GST
                            String accHead = "";
                            double chargeAmt = 0;
                            accHead = CommonUtil.convertObjToStr(Taxdetails.get("TAX_HEAD_ID"));
                            chargeAmt = CommonUtil.convertObjToDouble(Taxdetails.get("TOT_TAX_AMT")).doubleValue();
                            totchargeAmt = totchargeAmt + chargeAmt;
                            double serviceTax = 0.0;
                            double swachhCess = 0.0;
                            double krishikalyanCess = 0.0;
                            double taxAmt = 0.0;
                            double normalServiceTax = 0.0;
                            if (Taxdetails.containsKey("TOT_TAX_AMT") && Taxdetails.get("TOT_TAX_AMT") != null) {
                                taxAmt = CommonUtil.convertObjToDouble(Taxdetails.get("TOT_TAX_AMT"));
                            }
                            if (Taxdetails.containsKey(ServiceTaxCalculation.SWACHH_CESS) && Taxdetails.get(ServiceTaxCalculation.SWACHH_CESS) != null) {
                                swachhCess = CommonUtil.convertObjToDouble(Taxdetails.get(ServiceTaxCalculation.SWACHH_CESS));
                            }
                            if (Taxdetails.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS) && Taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS) != null) {
                                krishikalyanCess = CommonUtil.convertObjToDouble(Taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_CESS));
                            }
                            if (Taxdetails.containsKey(ServiceTaxCalculation.SERVICE_TAX) && Taxdetails.get(ServiceTaxCalculation.SERVICE_TAX) != null) {
                                normalServiceTax = CommonUtil.convertObjToDouble(Taxdetails.get(ServiceTaxCalculation.SERVICE_TAX));
                            }
                            //  taxAmt-=(swachhCess+krishikalyanCess);                            
                            if (swachhCess > 0) {
                                txMap = new HashMap();
                                transferTo = new TxTransferTO();
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(Taxdetails.get(ServiceTaxCalculation.SWACHH_HEAD_ID)));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                                txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                                txMap.put(TransferTrans.PARTICULARS, "CGST" + " - : " + dataMap.get("ACT_NUM"));
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, swachhCess);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                transferTo.setTransModType(TransactionFactory.GL);
                                transferList.add(transferTo);
                                // taxAmt -= swachhCess;
                            }
                            if (krishikalyanCess > 0) {
                                txMap = new HashMap();
                                transferTo = new TxTransferTO();
                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(Taxdetails.get(ServiceTaxCalculation.KRISHIKALYAN_HEAD_ID)));
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                                txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                                txMap.put(TransferTrans.PARTICULARS, "SGST" + " - : " + dataMap.get("ACT_NUM"));
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, krishikalyanCess);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                transferTo.setTransModType(TransactionFactory.GL);
                                transferList.add(transferTo);
                                // taxAmt -= krishikalyanCess;
                            }
                            if (normalServiceTax > 0) {
                                txMap = new HashMap();
                                transferTo = new TxTransferTO();
                                txMap.put(TransferTrans.CR_AC_HD, accHead);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.CR_BRANCH, dataMap.get("BRANCH_CODE"));
                                txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                                txMap.put(TransferTrans.PARTICULARS, "SERVICE_TAX");
                                transferTo = transactionDAO.addTransferCreditLocal(txMap, normalServiceTax);
                                transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                transferTo.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
                                transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                                transferTo.setSingleTransId(generateSingleTransId);
                                transferTo.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                transferTo.setTransModType(TransactionFactory.GL);
                                transferList.add(transferTo);
                            }
                            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                            // End
                        }
                        transferTo = new TxTransferTO();
                        txMap.put(TransferTrans.DR_ACT_NUM, objTransactionTO.getDebitAcctNo());//dataMap.get("CR_ACT_NUM"));
                        txMap.put(TransferTrans.DR_AC_HD, crMap.get("AC_HD_ID"));
                        txMap.put(TransferTrans.DR_PROD_ID, crMap.get("PROD_ID"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.OPERATIVE);
                        txMap.put(TransferTrans.DR_BRANCH, objTransactionTO.getBranchId());
                        txMap.put(TransferTrans.PARTICULARS, "");// dataMap.get("ACT_NUM")
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put("DR_INST_TYPE", "VOUCHER");
                        // txMap.put(TransferTrans.CR_AC_HD, accHead);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_BRANCH, objTransactionTO.getBranchId());
                        txMap.put(TransferTrans.PARTICULARS, "");//dataMap.get("ACT_NUM")
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, totchargeAmt);
                        transferTo.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        transferTo.setStatusBy(CommonUtil.convertObjToStr(userID));
                        transferTo.setInitTransId(CommonUtil.convertObjToStr(userID));//dataMap.get("USER_ID")
                        transferTo.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                        transferTo.setTransModType(TransactionFactory.OPERATIVE);
                        transferTo.setSingleTransId(generateSingleTransId);
                        transferList.add(transferTo);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.doTransferLocal(transferList, CommonUtil.convertObjToStr(objTransactionTO.getBranchId()));
                    }
                    transList = null;
                    transactionDAO = null;
                    transferDao = null;
                    txMap = null;
                    acHeads = null;
                }
                getTransDetails(objTO.getApplNo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        transList = null;
        transactionDAO = null;
        transferDao = null;
        txMap = null;
        acHeads = null;
        // }
    }

    private CashTransactionTO loanAuthorizeTimeTransaction(HashMap dataMap) throws Exception {
        CashTransactionTO objCashTO = null;
        dataMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO = new CashTransactionTO();
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            if (dataMap.containsKey("LOANDEBIT")) {
                objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
                objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.LOANS));
                objCashTO.setTransModType("TL");
                objCashTO.setTokenNo(CommonUtil.convertObjToStr(dataMap.get("TOKEN_NO")));
                objCashTO.setTransType(CommonConstants.DEBIT);
                objCashTO.setParticulars("To Cash : " + dataMap.get("ACCT_NUM"));
                objCashTO.setAuthorizeRemarks("DP");
                objCashTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
            } else {
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
                objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
                objCashTO.setTransModType("GL");
                objCashTO.setTransType(CommonConstants.CREDIT);
                objCashTO.setParticulars("By Cash : " + dataMap.get("ACCT_NUM"));
                objCashTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
            }
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setBranchId(_branchCode);
            objCashTO.setTransDt((Date) currDt.clone());
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setStatusDt((Date) currDt.clone());
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
            objCashTO.setInitiatedBranch(_branchCode);
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand("INSERT");
            objCashTO.setScreenName(CommonUtil.convertObjToStr(dataMap.get(CommonConstants.SCREEN)));
            System.out.println("objCashTO 1st one:" + objCashTO);
        }
        return objCashTO;
    }

    private CashTransactionTO loanAuthorizeTimeTransaction1(HashMap dataMap) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        dataMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
        if (dataMap.get("STATUS").equals(CommonConstants.TOSTATUS_INSERT)) {
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
            if (dataMap.containsKey("LOANDEBIT")) {
                objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
                objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
                objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.LOANS));
                objCashTO.setTransModType("TL");
                objCashTO.setTokenNo(CommonUtil.convertObjToStr(dataMap.get("TOKEN_NO")));
                objCashTO.setTransType(CommonConstants.DEBIT);
                objCashTO.setParticulars("To Cash : " + dataMap.get("ACCT_NUM"));
                objCashTO.setAuthorizeRemarks("DP");
            } else {
                objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
                objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
                objCashTO.setTransModType("GL");
                objCashTO.setTransType(CommonConstants.CREDIT);
                objCashTO.setParticulars("By Cash : " + dataMap.get("ACCT_NUM"));
            }
            objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setBranchId(_branchCode);
            objCashTO.setTransDt((Date) currDt.clone());
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
            objCashTO.setStatusDt((Date) currDt.clone());
            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM")));
            objCashTO.setInitiatedBranch(_branchCode);
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand("INSERT");
            System.out.println("objCashTO 1st one:" + objCashTO);
            // cashList.add(objCashTO);
        }
        return objCashTO;
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
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                txMap.put("AUTHORIZEREMARKS", "DI");
                double interestAmt = CommonUtil.convertObjToDouble(reserchMap.get("INTEREST_AMT")).doubleValue();
                transferDao = transactionDAO.addTransferDebitLocal(txMap, interestAmt);
                //                transferDao.setAuthorizeRemarks("DI");
                transList.add(transferDao);
                txMap.put(TransferTrans.CR_AC_HD, acHeads.get("INT_PAYABLE_ACHD"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, reserchMap.get("BRANCH_CODE"));
                //DELETEING RECORD
                reserchMap.put("LINK_BATCH_ID", reserchMap.get("ACT_NUM"));
                reserchMap.put("TRANS_DT", currDt.clone());
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
                transList.add(transferDao);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                System.out.println("transList  ###" + transList + "transferDao   :" + transferDao);
                transferTrans.setLoanDebitInt(new String("DI"));
                transferTrans.setInitiatedBranch((String) reserchMap.get("BRANCH_CODE"));
                transferTrans.doDebitCredit(transList, branchId, false);
                HashMap hash = new HashMap();
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
    // charge end..
    private void insertServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        try {
            objserviceTaxDetailsTO.setServiceTaxDet_Id(getServiceTaxNo());
            objserviceTaxDetailsTO.setAcct_Num(objTO.getApplNo());
            sqlMap.executeUpdate("insertServiceTaxDetailsTO", objserviceTaxDetailsTO);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LoanApplicationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertData(HashMap map, HashMap transDetailMap, List chargeLst) throws Exception {
        try {
            //    sqlMap.startTransaction();
            if (map.containsKey("Charge List Data")) {
                objTO.setIsTransaction("Y");
            } else {
                objTO.setIsTransaction("N");
            }
            objTO.setApplNo(getApplNo(map));
            objTO.setBranCode(_branchCode);
            //  doBorrowingTransactions(map); //trans details
            sqlMap.executeUpdate("insertLoanApplicationTO", objTO);
            doLoanApplTransactions(map); //trans details
            //security details
            executeFacilityTabQuery();
            executeSalaryDetailsQuery();
            insertMemberTableDetails();
            if (vehicleTableDetails != null) {
                insertVehicleTableDetails();
            }
            insertCollateralTableDetails();
            insertDepositTableDetails();
            insertLosTableDetails();
            if (map.containsKey("serviceTaxDetailsTO")) {
                ServiceTaxDetailsTO objserviceTaxDetailsTO = (ServiceTaxDetailsTO) map.get("serviceTaxDetailsTO");
                insertServiceTaxDetails(objserviceTaxDetailsTO);
            }
            if (objTermLoanSecuritySalaryTO != null) {
                logTO.setData(objTermLoanSecuritySalaryTO.toString());
                logTO.setPrimaryKey(objTermLoanSecuritySalaryTO.getKeyData());
                logTO.setStatus(objTermLoanSecuritySalaryTO.getCommand());
                objTermLoanSecuritySalaryTO.setAcctNum(objTO.getApplNo());
                //                executeUpdate("insertTermLoanSecuritySalaryTO", objTermLoanSecuritySalaryTO);
                logDAO.addToLog(logTO);
            }
            //security end..
            executeTransactionPart(map, transDetailMap, chargeLst);//trans charge details
            //     sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    //security details
    private void executeFacilityTabQuery() throws Exception {
        TermLoanFacilityTO objTermLoanFacilityTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = facilityMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To retrieve the TermLoanFacilityTO from the facilityMap
            for (int i = facilityMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objTermLoanFacilityTO = (TermLoanFacilityTO) facilityMap.get(objKeySet[j]);
                objTermLoanFacilityTO.setBorrowNo(null);
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    prod_id = objTermLoanFacilityTO.getProdId();
                    acct_No = objTO.getApplNo();
                    objTermLoanFacilityTO.setAcctNum(objTO.getApplNo());
                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_CREATED);
                    logTO.setData(objTermLoanFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanFacilityTO.getCommand());
                    executeUpdate("insertTermLoanFacilityTO", objTermLoanFacilityTO);
                    insertCustomerHistory(objTermLoanFacilityTO);
                    //                    logDAO.addToLog(logTO);
                } else if (objTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                    //                    setAccount_No(objTermLoanFacilityTO.getAcctNum());
                    //curr_dt = currDt.clone();
                    acct_No = objTO.getApplNo();
                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objTermLoanFacilityTO.setAcctNum(objTO.getApplNo());
                    if (objTermLoanFacilityTO.getAuthorizeStatus2().equals("ENHANCE") || objTermLoanFacilityTO.getAuthorizeStatus2().equals("ODRENEWAL")) {
                        insertRenewalRecords(objTermLoanFacilityTO);
                    }
                    logTO.setData(objTermLoanFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanFacilityTO.getCommand());
                    if (objTermLoanFacilityTO.getAuthorizeStatus1() != null && objTermLoanFacilityTO.getAuthorizeStatus1().length() > 0) {
                        executeUpdate("updateSecurityTermLoanFacilityTOMaterializedView", objTermLoanFacilityTO);//dont want to updte available balance
                    } else {
                        executeUpdate("updateSecurityTermLoanFacilityTO", objTermLoanFacilityTO);
                    }
                    insertCustomerHistory(objTermLoanFacilityTO);
                    //                    logDAO.addToLog(logTO);
                } else if (objTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    //                    setAccount_No(objTermLoanFacilityTO.getAcctNum());
                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                    objTermLoanFacilityTO.setAcctNum(objTO.getApplNo());
                    logTO.setData(objTermLoanFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanFacilityTO.getCommand());
                    executeUpdate("deleteTermLoanSecurityDepositDetails", objTermLoanFacilityTO);

                    executeUpdate("deleteTermLoanSecurityLosDetails", objTermLoanFacilityTO);
                    executeUpdate("deleteTermLoanFacilityTO", objTermLoanFacilityTO);

                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                    logTO.setData(objTermLoanFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanFacilityTO.getCommand());
                    executeUpdate("deleteTermLoanSecurityMemberDetails", objTermLoanFacilityTO);

                    objTermLoanFacilityTO.setStatus(CommonConstants.STATUS_DELETED);
                    logTO.setData(objTermLoanFacilityTO.toString());
                    logTO.setPrimaryKey(objTermLoanFacilityTO.getKeyData());
                    logTO.setStatus(objTermLoanFacilityTO.getCommand());
                    executeUpdate("deleteTermLoanSecurityCollateralDetails", objTermLoanFacilityTO);
                    logDAO.addToLog(logTO);
                    HashMap lienCancelMap = new HashMap();
                    lienCancelMap.put("LOAN_NO", objTO.getApplNo());
                    lienCancelMap.put("PROD_ID", objTO.getProdId());
                    //          lienCancel(lienCancelMap);
                    insertCustomerHistory(objTermLoanFacilityTO);
                }
                objTermLoanFacilityTO = null;
            }
            depositCustDetMap = null;
            keySet = null;
            objKeySet = null;
            objTermLoanFacilityTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
            //            throw e;
        }
    }
    //we should update customer history

    private void insertCustomerHistory(TermLoanFacilityTO termLoanFacilityTO) throws Exception {
        // Inserting into Customer History Table
        HashMap map = new HashMap();
        behaves_like = null;
        map.put("PROD_ID", objTO.getProdId());
        List lst = sqlMap.executeQueryForList("getLoansProduct", map);
        if (lst != null && lst.size() > 0) {
            map = (HashMap) lst.get(0);
            behaves_like = CommonUtil.convertObjToStr(map.get("BEHAVES_LIKE"));
        }
        CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO();
        objCustomerHistoryTO.setAcctNo(objTO.getApplNo());
        objCustomerHistoryTO.setCustId(objTO.getCustId());
        if (behaves_like.equals("OD") || behaves_like.equals("CC")) {
            objCustomerHistoryTO.setProductType("AD");
        } else {
            objCustomerHistoryTO.setProductType("TL");
        }
        objCustomerHistoryTO.setProdId(objTO.getProdId());
        objCustomerHistoryTO.setRelationship("ACCT_HOLDER");
        objCustomerHistoryTO.setFromDt((Date)currDt.clone());
        objCustomerHistoryTO.setToDt((Date)currDt.clone());
        objCustomerHistoryTO.setCommand(objTO.getCommand());
        CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
        objCustomerHistoryTO = null;

    }

    private void insertRenewalRecords(TermLoanFacilityTO objTermLoanFacilityTO) throws Exception {
        HashMap map = new HashMap();
        map.put("ACCT_NUM", objTO.getApplNo());
        sqlMap.executeUpdate("insertLoansRenewalFacilityDetails", map);
    }

    private void insertMemberTableDetails() throws Exception {
        if (memberTableDetails != null && memberTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(memberTableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                TermLoanSecurityMemberTO objMemberTypeTO = (TermLoanSecurityMemberTO) memberTableDetails.get(addList.get(i));
                objMemberTypeTO.setAcctNum(objTO.getApplNo());
                objMemberTypeTO.setBranchCode(_branchCode);
                objMemberTypeTO.setStatusDt((Date)currDt.clone());//added to avoid class cast Exception
                sqlMap.executeUpdate("insertTermLoanSecurityMemberTO", objMemberTypeTO);
                logTO.setData(objMemberTypeTO.toString());
                logTO.setPrimaryKey(objMemberTypeTO.getKeyData());
                logDAO.addToLog(logTO);
                objMemberTypeTO = null;
            }
        }
    }
    private void insertVehicleTableDetails() throws Exception {
        if (vehicleTableDetails != null && vehicleTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(vehicleTableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                TermLoanSecurityVehicleTO objVehicleTypeTO = (TermLoanSecurityVehicleTO) vehicleTableDetails.get(addList.get(i));
                objVehicleTypeTO.setAcctNum(objTO.getApplNo());
                objVehicleTypeTO.setBranchCode(_branchCode);
                objVehicleTypeTO.setStatusDt((Date) currDt.clone());//added to avoid class cast Exception
                sqlMap.executeUpdate("insertTermLoanSecurityVehicleTO", objVehicleTypeTO);
                logTO.setData(objVehicleTypeTO.toString());
                logTO.setPrimaryKey(objVehicleTypeTO.getKeyData());
                logDAO.addToLog(logTO);
                objVehicleTypeTO = null;
            }
        }
    }

    private void executeSalaryDetailsQuery() throws Exception {
        
        if (objTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
            if (termLoanSecuritySalaryTODeletedMap != null && termLoanSecuritySalaryTODeletedMap.size() > 0) {
                Set keyset = termLoanSecuritySalaryTODeletedMap.keySet();
                Object obj[] = (Object[]) keyset.toArray();
                for (int i = 0; i < keyset.size(); i++) {
                    TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) termLoanSecuritySalaryTODeletedMap.get(obj[i]);
                    objTermLoanSecuritySalaryTO.setStatus("DELETED");
                    objTermLoanSecuritySalaryTO.setAcctNum(objTO.getApplNo());
                    objTermLoanSecuritySalaryTO.setStatusBy(userID);
                    objTermLoanSecuritySalaryTO.setStatusDt(currDt);
                    executeUpdate("deleteTermLoanSecuritySalaryTO", objTermLoanSecuritySalaryTO);
                }
            }
        }
        
        if (termLoanSecuritySalaryTOMap != null && termLoanSecuritySalaryTOMap.size() > 0) {
            Set keyset = termLoanSecuritySalaryTOMap.keySet();
            Object obj[] = (Object[]) keyset.toArray();
            for (int i = 0; i < keyset.size(); i++) {
                TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) termLoanSecuritySalaryTOMap.get(obj[i]);
                objTermLoanSecuritySalaryTO.setStatusBy(userID);
                objTermLoanSecuritySalaryTO.setStatusDt(currDt);
//                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
//                    if (objTermLoanSecuritySalaryTO != null && objTermLoanSecuritySalaryTO.getStatus().equals("CREATED")) {
//                        objTermLoanSecuritySalaryTO.setAcctNum(objTO.getApplNo());
//                        executeUpdate("insertTermLoanSecuritySalaryTO", objTermLoanSecuritySalaryTO);
//                    }
//                } else if (objTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
//                    objTermLoanSecuritySalaryTO.setAcctNum(objTO.getApplNo());
//                    objTermLoanSecuritySalaryTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                    //executeUpdate("updateEditTermLoanSecuritySalaryTO", objTermLoanSecuritySalaryTO);
//                }
                if (objTermLoanSecuritySalaryTO != null && objTermLoanSecuritySalaryTO.getStatus().equals("CREATED")) {
                    objTermLoanSecuritySalaryTO.setAcctNum(objTO.getApplNo());
                    objTermLoanSecuritySalaryTO.setRetirementDt(setProperDtFormat(objTermLoanSecuritySalaryTO.getRetirementDt()));
                    executeUpdate("insertTermLoanSecuritySalaryTO", objTermLoanSecuritySalaryTO);
                } else {
                    objTermLoanSecuritySalaryTO.setAcctNum(objTO.getApplNo());
                    objTermLoanSecuritySalaryTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objTermLoanSecuritySalaryTO.setStatusBy(userID);
                    objTermLoanSecuritySalaryTO.setStatusDt(currDt);
                    executeUpdate("updateTermLoanSecuritySalaryTO", objTermLoanSecuritySalaryTO);
                }
            }
        }
//        if (objTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
//            if (termLoanSecuritySalaryTODeletedMap != null && termLoanSecuritySalaryTODeletedMap.size() > 0) {
//                Set keyset = termLoanSecuritySalaryTODeletedMap.keySet();
//                Object obj[] = (Object[]) keyset.toArray();
//                for (int i = 0; i < keyset.size(); i++) {
//                    TermLoanSecuritySalaryTO objTermLoanSecuritySalaryTO = (TermLoanSecuritySalaryTO) termLoanSecuritySalaryTODeletedMap.get(obj[i]);
//                    objTermLoanSecuritySalaryTO.setStatus(CommonConstants.STATUS_MODIFIED);
//                    objTermLoanSecuritySalaryTO.setAcctNum(objTO.getApplNo());
//                    executeUpdate("updateEditTermLoanSecuritySalaryTO", objTermLoanSecuritySalaryTO);
//                }
//            }
//        }
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

    private void insertCollateralTableDetails() throws Exception {
        if (collateralTableDetails != null && collateralTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(collateralTableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                TermLoanSecurityLandTO objTermLoanSecurityLandTO = (TermLoanSecurityLandTO) collateralTableDetails.get(addList.get(i));
                objTermLoanSecurityLandTO.setAcctNum(objTO.getApplNo());
                objTermLoanSecurityLandTO.setBranchCode(_branchCode);
                sqlMap.executeUpdate("insertTermLoanSecurityLandTO", objTermLoanSecurityLandTO);
                logTO.setData(objTermLoanSecurityLandTO.toString());
                logTO.setPrimaryKey(objTermLoanSecurityLandTO.getKeyData());
                logDAO.addToLog(logTO);
                objTermLoanSecurityLandTO = null;
            }
        }
    }

    private void insertDepositTableDetails() throws Exception {

        if (depositTableDetails != null) {
            ArrayList addList = new ArrayList(depositTableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                TermLoanDepositTypeTO objTermLoanDepositTypeTO = (TermLoanDepositTypeTO) depositTableDetails.get(addList.get(i));
                objTermLoanDepositTypeTO.setBorrowNo(objTO.getApplNo());
                objTermLoanDepositTypeTO.setStatusDt((Date)currDt.clone());
                objTermLoanDepositTypeTO.setBranchCode(_branchCode);
                sqlMap.executeUpdate("insertDepositSecurityTableDetails", objTermLoanDepositTypeTO);
                objTermLoanDepositTypeTO = null;
            }
        }
    }

    private void insertLosTableDetails() throws Exception {
        if (losTableDetails != null) {
            ArrayList addList = new ArrayList(losTableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                TermLoanLosTypeTO objTermLoanLosTypeTO = (TermLoanLosTypeTO) losTableDetails.get(addList.get(i));
                objTermLoanLosTypeTO.setBorrowNo(objTO.getApplNo());
                objTermLoanLosTypeTO.setStatusDt((Date)currDt.clone());
                objTermLoanLosTypeTO.setBranchCode(_branchCode);
                sqlMap.executeUpdate("insertLosSecurityTableDetails", objTermLoanLosTypeTO);
                objTermLoanLosTypeTO = null;
            }
        }
    }
    //security end..

    private String getApplNo(HashMap map) throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "APPLICATION_NO");
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//reseting branch code
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getServiceTaxNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SERVICETAX_DET_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(HashMap map, HashMap transDetailMap, List chargeLst) throws Exception {
        try {
            //  sqlMap.startTransaction();
            if (map.containsKey("Charge List Data")) {
                objTO.setIsTransaction("Y");
            } else {
                objTO.setIsTransaction("N");
            }
            objTO.setAuthorizeStatus(null); //AUTH
            objTO.setBranCode(_branchCode);
            sqlMap.executeUpdate("updateLoanApplicationTO", objTO);
            //security details
            if (map.containsKey("serviceTaxDetailsTO")) {
                ServiceTaxDetailsTO objserviceTaxDetailsTO = (ServiceTaxDetailsTO) map.get("serviceTaxDetailsTO");
                updateServiceTaxDetails(objserviceTaxDetailsTO);
            }
            executeTransactionPart(map, transDetailMap, chargeLst);//trans charge details
            executeFacilityTabQuery();
            executeSalaryDetailsQuery();
            updateMemberTableDetails();
            updateVehicleTableDetails();
            updateCollateralTableDetails();
            updateDepositTypeDetails();
            updateLosTypeDetails();
            //security end..
            //    sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }
    public void updateServiceTaxDetails(ServiceTaxDetailsTO objserviceTaxDetailsTO) {
        HashMap wherMap = new HashMap();
        wherMap.put("ACCT_NUM", objserviceTaxDetailsTO.getAcct_Num());
        List rsltList;
        try {
            rsltList = sqlMap.executeQueryForList("getServiceTaxDetailsForUpdation", wherMap);
            if (rsltList != null && rsltList.size() > 0) {
                HashMap each = (HashMap) rsltList.get(0);
                double seramt = CommonUtil.convertObjToDouble(each.get("SERVICE_TAX_AMT")) + objserviceTaxDetailsTO.getServiceTaxAmt();
                double hCess = CommonUtil.convertObjToDouble(each.get("HIGHER_EDU_CESS")) + objserviceTaxDetailsTO.getHigherCess();
                double eCess = CommonUtil.convertObjToDouble(each.get("EDUCATION_CESS")) + objserviceTaxDetailsTO.getEducationCess();
                double roundval = CommonUtil.convertObjToDouble(each.get("ROUND_VAL")) + CommonUtil.convertObjToDouble(objserviceTaxDetailsTO.getRoundVal());
                double totAmt = CommonUtil.convertObjToDouble(each.get("TOTAL_TAX_AMOUNT")) + objserviceTaxDetailsTO.getTotalTaxAmt();
                HashMap serMapUpdate = new HashMap();
                serMapUpdate.put("SERVICE_TAX_AMT", seramt);
                serMapUpdate.put("HIGHER_EDU_CESS", hCess);
                serMapUpdate.put("EDUCATION_CESS", eCess);
                serMapUpdate.put("TOTAL_TAX_AMOUNT", totAmt);
                serMapUpdate.put("ROUND_VAL", roundval);
                serMapUpdate.put("STATUS", "MODIFIED");
                serMapUpdate.put("ACCT_NUM", objserviceTaxDetailsTO.getAcct_Num());
                sqlMap.executeUpdate("updateServiceTaxDetails", serMapUpdate);
            } else {
                objserviceTaxDetailsTO.setAcct_Num(objserviceTaxDetailsTO.getAcct_Num());
                insertServiceTaxDetails(objserviceTaxDetailsTO);
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
    }
    //security details
    private void updateMemberTableDetails() throws Exception {
        if (memberTableDetails != null && memberTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(memberTableDetails.keySet());
            TermLoanSecurityMemberTO objMemberTypeTO = null;
            for (int i = 0; i < memberTableDetails.size(); i++) {
                objMemberTypeTO = new TermLoanSecurityMemberTO();
                objMemberTypeTO = (TermLoanSecurityMemberTO) memberTableDetails.get(addList.get(i));
                //objMemberTypeTO.setStatusDt(new java.sql.Date(curr_dt.getTime()));
                if (objMemberTypeTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    objMemberTypeTO.setAcctNum(objTO.getApplNo());
                    objMemberTypeTO.setBranchCode(_branchCode);
                    objMemberTypeTO.setStatusDt((Date) currDt.clone());
                    sqlMap.executeUpdate("insertTermLoanSecurityMemberTO", objMemberTypeTO);
                } else {
                    objMemberTypeTO.setBranchCode(_branchCode);
                    objMemberTypeTO.setAcctNum(objTO.getApplNo());
                    sqlMap.executeUpdate("updateTermLoanSecurityMemberTO", objMemberTypeTO);
                }
            }
        }
        if (deletedMemberTableValues != null && deletedMemberTableValues.size() > 0) {
            ArrayList addList = new ArrayList(deletedMemberTableValues.keySet());
            TermLoanSecurityMemberTO objMemberTypeTO = null;
            for (int i = 0; i < deletedMemberTableValues.size(); i++) {
                objMemberTypeTO = new TermLoanSecurityMemberTO();
                objMemberTypeTO = (TermLoanSecurityMemberTO) deletedMemberTableValues.get(addList.get(i));
                objMemberTypeTO.setAcctNum(objTO.getApplNo());
                objMemberTypeTO.setBranchCode(_branchCode);
                objMemberTypeTO.setStatusBy(userID);
                objMemberTypeTO.setStatusDt(currDt);
                sqlMap.executeUpdate("deleteTermLoanSecurityMemberTO", objMemberTypeTO);
            }
        }
    }
    private void updateVehicleTableDetails() throws Exception {
        if (vehicleTableDetails != null && vehicleTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(vehicleTableDetails.keySet());
            TermLoanSecurityVehicleTO objVehicleTypeTO = null;
            for (int i = 0; i < vehicleTableDetails.size(); i++) {
                objVehicleTypeTO = new TermLoanSecurityVehicleTO();
                objVehicleTypeTO = (TermLoanSecurityVehicleTO) vehicleTableDetails.get(addList.get(i));
                //objMemberTypeTO.setStatusDt(new java.sql.Date(curr_dt.getTime()));
                if (objVehicleTypeTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    objVehicleTypeTO.setAcctNum(objTO.getApplNo());
                    objVehicleTypeTO.setBranchCode(_branchCode);
                    objVehicleTypeTO.setStatusDt((Date) currDt.clone());
                    sqlMap.executeUpdate("insertTermLoanSecurityVehicleTO", objVehicleTypeTO);
                } else {
                    objVehicleTypeTO.setBranchCode(_branchCode);
                    objVehicleTypeTO.setAcctNum(objTO.getApplNo());
                    sqlMap.executeUpdate("updateTermLoanSecurityVehicleTO", objVehicleTypeTO);
                }
            }
        }
        if (deletedVehicleTableValues != null && deletedVehicleTableValues.size() > 0) {
            ArrayList addList = new ArrayList(deletedVehicleTableValues.keySet());
            TermLoanSecurityVehicleTO objVehicleTO = null;
            for (int i = 0; i < deletedVehicleTableValues.size(); i++) {
                objVehicleTO = new TermLoanSecurityVehicleTO();
                objVehicleTO = (TermLoanSecurityVehicleTO) deletedVehicleTableValues.get(addList.get(i));
                objVehicleTO.setAcctNum(objTO.getApplNo());
                objVehicleTO.setBranchCode(_branchCode);
                objVehicleTO.setStatusBy(userID);
                objVehicleTO.setStatusDt(currDt);
                sqlMap.executeUpdate("deleteTermLoanSecurityVehicleTO", objVehicleTO);
            }
        }
    }

    private void updateCollateralTableDetails() throws Exception {
        if (collateralTableDetails != null && collateralTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(collateralTableDetails.keySet());
            TermLoanSecurityLandTO objTermLoanSecurityLandTO = null;
            for (int i = 0; i < collateralTableDetails.size(); i++) {
                objTermLoanSecurityLandTO = new TermLoanSecurityLandTO();
                objTermLoanSecurityLandTO = (TermLoanSecurityLandTO) collateralTableDetails.get(addList.get(i));
                objTermLoanSecurityLandTO.setStatusDt((Date)currDt.clone());
                if (objTermLoanSecurityLandTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    objTermLoanSecurityLandTO.setAcctNum(objTO.getApplNo());
                    objTermLoanSecurityLandTO.setBranchCode(_branchCode);
                    sqlMap.executeUpdate("insertTermLoanSecurityLandTO", objTermLoanSecurityLandTO);
                } else {
                    objTermLoanSecurityLandTO.setBranchCode(_branchCode);
                    objTermLoanSecurityLandTO.setAcctNum(objTO.getApplNo());
                    objTermLoanSecurityLandTO.setDocumentDt(setProperDtFormat(objTermLoanSecurityLandTO.getDocumentDt()));
                    sqlMap.executeUpdate("updateTermLoanSecurityLandTO1", objTermLoanSecurityLandTO);
                }
            }
        }
        if (deletedCollateralTableValues != null && deletedCollateralTableValues.size() > 0) {
            ArrayList addList = new ArrayList(deletedCollateralTableValues.keySet());
            TermLoanSecurityLandTO objTermLoanSecurityLandTO = null;
            for (int i = 0; i < deletedCollateralTableValues.size(); i++) {
                objTermLoanSecurityLandTO = new TermLoanSecurityLandTO();
                objTermLoanSecurityLandTO = (TermLoanSecurityLandTO) deletedCollateralTableValues.get(addList.get(i));
                objTermLoanSecurityLandTO.setAcctNum(objTO.getApplNo());
                objTermLoanSecurityLandTO.setBranchCode(_branchCode);
                objTermLoanSecurityLandTO.setStatusDt(currDt);
                objTermLoanSecurityLandTO.setStatusBy(userID);
                sqlMap.executeUpdate("deleteTermLoanSecurityLandTO1", objTermLoanSecurityLandTO);
            }
        }
    }

    private void updateDepositTypeDetails() throws Exception {
        if (depositTableDetails != null && depositTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(depositTableDetails.keySet());
            TermLoanDepositTypeTO objTermLoanDepositTypeTO = null;
            for (int i = 0; i < depositTableDetails.size(); i++) {
                objTermLoanDepositTypeTO = new TermLoanDepositTypeTO();
                objTermLoanDepositTypeTO = (TermLoanDepositTypeTO) depositTableDetails.get(addList.get(i));
                objTermLoanDepositTypeTO.setStatusDt((Date) currDt.clone());
                if (objTermLoanDepositTypeTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    //                    objTermLoanDepositTypeTO.setAcctNum(acct_No);
                    objTermLoanDepositTypeTO.setBorrowNo(objTO.getApplNo());
                    objTermLoanDepositTypeTO.setBranchCode(_branchCode);
                    sqlMap.executeUpdate("insertDepositSecurityTableDetails", objTermLoanDepositTypeTO);
                } else {
                    objTermLoanDepositTypeTO.setBorrowNo(objTO.getApplNo());
                    sqlMap.executeUpdate("updateSecurityTermLoanDepositTypeTO", objTermLoanDepositTypeTO);
                }
            }
        }
        if (deletedDepositTableValues != null && deletedDepositTableValues.size() > 0) {
            ArrayList addList = new ArrayList(deletedDepositTableValues.keySet());
            TermLoanDepositTypeTO objDepositTypeTO = null;
            for (int i = 0; i < deletedDepositTableValues.size(); i++) {
                objDepositTypeTO = new TermLoanDepositTypeTO();
                objDepositTypeTO = (TermLoanDepositTypeTO) deletedDepositTableValues.get(addList.get(i));
                objDepositTypeTO.setStatusDt(currDt);
                objDepositTypeTO.setStatusBy(userID);
                sqlMap.executeUpdate("deleteTermLoanSecurityDepositTO", objDepositTypeTO);
            }
        }
    }

    private void updateLosTypeDetails() throws Exception {
        if (losTableDetails != null && losTableDetails.size() > 0) {
            ArrayList addList = new ArrayList(losTableDetails.keySet());
            TermLoanLosTypeTO objTermLoanLosTypeTO = null;
            for (int i = 0; i < losTableDetails.size(); i++) {
                objTermLoanLosTypeTO = new TermLoanLosTypeTO();
                objTermLoanLosTypeTO = (TermLoanLosTypeTO) losTableDetails.get(addList.get(i));
                objTermLoanLosTypeTO.setStatusDt((Date)currDt.clone());
                if (objTermLoanLosTypeTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    //                    objTermLoanDepositTypeTO.setAcctNum(acct_No);
                    objTermLoanLosTypeTO.setBranchCode(_branchCode);
                    sqlMap.executeUpdate("insertLosSecurityTableDetails", objTermLoanLosTypeTO);
                } else {
                    objTermLoanLosTypeTO.setBorrowNo(objTO.getApplNo());
                    sqlMap.executeUpdate("updateSecurityTermLoanLosTypeTO", objTermLoanLosTypeTO);
                }
            }
        }
        if (deletedLosTableValues != null && deletedLosTableValues.size() > 0) {
            ArrayList addList = new ArrayList(deletedLosTableValues.keySet());
            TermLoanLosTypeTO objLosTypeTO = null;
            for (int i = 0; i < deletedLosTableValues.size(); i++) {
                objLosTypeTO = new TermLoanLosTypeTO();
                objLosTypeTO = (TermLoanLosTypeTO) deletedLosTableValues.get(addList.get(i));
                objLosTypeTO.setStatusBy(userID);
                objLosTypeTO.setStatusDt(currDt);
                sqlMap.executeUpdate("deleteTermLoanSecurityLosTO", objLosTypeTO);
            }
        }
    }
    //security end..

    //trans details
    private void doLoanApplTransactions(HashMap map) throws Exception, Exception {
        try {
            System.out.println("####doLoanApplTransactions :" + map);
            if (objTO.getCommand() != null) {
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    HashMap txMap;
                    HashMap whereMap = new HashMap();
                    whereMap.put("APPLICATION_NO", objTO.getApplNo());
                    HashMap m2 = new HashMap();
                    m2.put("SCHEME_NAME", objTO.getSchemName());
                    List chgDetails = (List) map.get("Charge List Data");
                    if (chgDetails != null) {
                        HashMap newMap = (HashMap) chgDetails.get(0);
                        m2.put("CHARGE_DESC", newMap.get("CHARGE_DESC").toString());
                    }
                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getacHdData", m2);
                    String achd = "";
                    if (acHeads != null) {
                        achd = acHeads.get("ACC_HEAD").toString();
                    }
                    TransferTrans objTransferTrans = new TransferTrans();
                    objTransferTrans.setInitiatedBranch(_branchCode);
                    objTransferTrans.setLinkBatchId(objTO.getApplNo());
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    if (TransactionDetailsMap.size() > 0) {
                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            TransactionTO objTransactionTO = null;
                            if (allowedTransDetailsTO != null) {
                                System.out.println("@#$@#$#$allowedTransDetailsTO11111" + allowedTransDetailsTO);
                            }
                            if (allowedTransDetailsTO != null) {
                                for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                    double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                        //                                            txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("SHARE_ACHD"));
                                        txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getApplNo() + " Disbursement");
                                        txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("DR_INST_TYPE", "VOUCHER");
                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        if (objTransactionTO.getProductType().equals("GL")) {
                                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        } else {
                                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        }
                                        if (objTransactionTO.getProductType().equals("OA")) {
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        } else if (objTransactionTO.getProductType().equals("AB")) {
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        } else if (objTransactionTO.getProductType().equals("SA")) {
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        } else if (objTransactionTO.getProductType().equals("TL")) {
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        } else if (objTransactionTO.getProductType().equals("AD")) {
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        } else {
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        }
                                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        txMap.put("SCREEN_NAME", CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                                        //                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareAmt));
                                        //                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                        if (debitAmt > 0.0) {
                                            txMap.put(TransferTrans.CR_AC_HD, achd);
                                            txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_GRP_HEAD");
                                            //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                            txMap.put("SCREEN_NAME", CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, debitAmt));
                                            //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                        }
                                        if (objTransactionTO.getProductType().equals("GL")) {
                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                        }
                                    } else {
                                    }
                                    //End cash
                                    objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                    objTransactionTO.setBatchId(objTO.getApplNo());;
                                    objTransactionTO.setBatchDt((Date) currDt.clone());
                                    objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                                    objTransactionTO.setBranchId(_branchCode);
                                    sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                                }
                            }
                        }
                    }
                    //2a  amtBorrowed=0.0;
                    objTransferTrans = null;
                    transferList = null;
                    txMap = null;
                    // Code End
                    getTransDetails(objTO.getApplNo());
                } else {
                    HashMap shareAcctNoMap = new HashMap();
                    if (objTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
//                        shareAcctNoMap = new HashMap();
//                        shareAcctNoMap.put("LINK_BATCH_ID",objTO.getBorrowingNo());
//                        List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", shareAcctNoMap);
////                        TxTransferTO txTransferTO = null;
//                        double oldAmount = 0;
//                        HashMap oldAmountMap = new HashMap();
//                        ArrayList transferList = new ArrayList();
//                        if (lst!=null && lst.size()>0) {
//                            for (int j=0; j<lst.size(); j++) {
//                                txTransferTO = (TxTransferTO) lst.get(j);
//                                System.out.println("#@$@#$@#$lst:"+lst);
//                            }
//                            
//                        }else{
//                            System.out.println("In Cash Edit");
//                            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
//                            if(TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
//                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap)TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
//                                TransactionTO objTransactionTO = null;
//                                if(allowedTransDetailsTO!=null && allowedTransDetailsTO.size()>0){
//                                    for (int J = 1;J <= allowedTransDetailsTO.size();J++)  {
//                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
//                                        
//                                        //                                if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
//                                        HashMap tempMap=new HashMap();
//                                        //                                        if(!CommonUtil.convertObjToStr(shareAcctDetailsTO.getShareNoFrom()).equals("ADD")){
//                                        List cLst1 = sqlMap.executeQueryForList("getSelectShareCashTransactionTO",   CommonUtil.convertObjToStr(objTransactionTO.getTransId()));
//                                        if (cLst1!=null && cLst1.size()>0) {
//                                            CashTransactionTO txTransferTO1 = null;
//                                            txTransferTO1 = (CashTransactionTO) cLst1.get(0);
//                                            oldAmount= CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
//                                            double newAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//                                            txTransferTO1.setInpAmount(new Double(newAmount));
//                                            txTransferTO1.setAmount(new Double(newAmount));
//                                            txTransferTO1.setCommand(command);
//                                            txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
//                                            txTransferTO1.setStatusDt(currDt);
//                                            
//                                            map.put("PRODUCTTYPE", TransactionFactory.GL);
//                                            map.put("OLDAMOUNT", new Double(oldAmount));
//                                            map.put("CashTransactionTO", txTransferTO1);
//                                            CashTransactionDAO cashTransDAO = new CashTransactionDAO();
//                                            cashTransDAO.execute(map,false);
//                                        }
//                                        cLst1 = null;
//                                        //                                        }
//                                        
//                                        //                                                         for (int J = 1;J <= allowedTransDetailsTO.size();J++){
//                                        
//                                        objTransactionTO.setStatus(CommonConstants.STATUS_DELETED);
//                                        objTransactionTO.setTransId(objDrfTransactionTO.getDrfTransID());
//                                        objTransactionTO.setBatchId(objDrfTransactionTO.getDrfTransID());
//                                        objTransactionTO.setBranchId(_branchCode);
//                                        sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
//                                        
//                                    }
//                                    
//                                }
//                                
//                                //                                }
//                                
//                                
//                            }
//                            lst = null;
//                            oldAmountMap = null;
//                            transferList = null;
//                            shareAcctNoMap = null;
//                            txTransferTO = null;
//                        }
                    }
                }
            }
        } catch (Exception e) {
            //                sqlMap.rollbackTransaction();
            e.printStackTrace();
            //                throw new TransRollbackException(e);
            throw e;
        }
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        // String branchId=CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt.clone());
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        returnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    //end...
    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            String Statusby = objTO.getStatusBy();
            //objTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deleteLoanApplicationTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    /*
     * public static void main(String str[]) { try { UserDAO dao = new
     * UserDAO(); HashMap map=new HashMap();
     * map.put(CommonConstants.MAP_NAME,"getSelectTerminalMasterTO");
     * map.put(CommonConstants.MAP_WHERE,"T0001042"); map=dao.executeQuery(map);
     * } catch (Exception ex) { ex.printStackTrace(); } }
     */
    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@ExecuteMap" + map);
        //  HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        try {
            sqlMap.startTransaction();
            logDAO = new LogDAO();
            logTO = new LogTO();
            objTO = (LoanApplicationTO) map.get("LoanApplication");
            final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            if (map.containsKey("Charge List Data")) { //charge details
                chargeLst = (List) map.get("Charge List Data");
            }
            HashMap transDetailMap = new HashMap();//charge details
            if (map.containsKey("Transaction Details Data")) {
                transDetailMap = (HashMap) map.get("Transaction Details Data");
                if (map.containsKey("Charge List Data")) {
                    chargeLst = (List) map.get("Charge List Data");
                }
            }
            //end..
            //security details

            facilityMap = (HashMap) map.get("TermLoanFacilityTO");
            securityMap = (HashMap) map.get("TermLoanSecurityTO");
            if (map.containsKey("VehicleTableDetails")) {
                vehicleTableDetails = (LinkedHashMap) map.get("VehicleTableDetails");
            }
            if (map.containsKey("deletedVehicleTypeData")) {
                deletedVehicleTableValues = (LinkedHashMap) map.get("deletedVehicleTypeData");
            }
            if (map.containsKey("MemberTableDetails") || map.containsKey("deletedMemberTypeData")) {
                memberTableDetails = (LinkedHashMap) map.get("MemberTableDetails");
                deletedMemberTableValues = (LinkedHashMap) map.get("deletedMemberTypeData");
            }

            if (map.containsKey("CollateralTableDetails") || map.containsKey("deletedCollateralTypeData")) {
                collateralTableDetails = (LinkedHashMap) map.get("CollateralTableDetails");
                deletedCollateralTableValues = (LinkedHashMap) map.get("deletedCollateralTypeData");
            }
            if (map.containsKey("TermLoanSecuritySalaryTOData")) {

                termLoanSecuritySalaryTOMap = (LinkedHashMap) map.get("TermLoanSecuritySalaryTOData");
            }
            if (map.containsKey("TermLoanSecuritySalaryTODeletedData")) {

                termLoanSecuritySalaryTODeletedMap = (LinkedHashMap) map.get("TermLoanSecuritySalaryTODeletedData");
            }
            if (map.containsKey("DepositSecurityTableDetails")) {
                depositTableDetails = (LinkedHashMap) map.get("DepositSecurityTableDetails");
                deletedDepositTableValues = (LinkedHashMap) map.get("deletedDepositTypeData");
            }
            if (map.containsKey("LosSecurityTableDetails")) {
                losTableDetails = (LinkedHashMap) map.get("LosSecurityTableDetails");
                deletedLosTableValues = (LinkedHashMap) map.get("deletedLosTypeData");
            }
            //security end..
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map, transDetailMap, chargeLst);
                returnMap.put("APPLICATION_NO", objTO.getApplNo());
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(map, transDetailMap, chargeLst);
                if (returnMap == null) {
                    returnMap = new HashMap();
                }
                returnMap.put("APPLICATION_NO", objTO.getApplNo());
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
                returnMap.put("APPLICATION_NO", objTO.getApplNo());
            } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
                authMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
                System.out.println("authMap:" + authMap);
                if (authMap != null) {
                    authorize(authMap); //trans details
                }
            }
            map = null;
            sqlMap.commitTransaction();
        } catch (Exception ex) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(ex);
        }
        destroyObjects();
        System.out.println("returnmap in dao >>>>>" + returnMap);
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
        logTO = null;
        logDAO = null;
        facilityMap = null;
        securityMap = null;
        vehicleTableDetails=null;
        deletedVehicleTableValues=null;
        memberTableDetails = null;
        deletedMemberTableValues = null;
        collateralTableDetails = null;
        deletedCollateralTableValues = null;
        chargeLst = null;
        behaves_like = null;
        termLoanSecuritySalaryTOMap = null;
        termLoanSecuritySalaryTODeletedMap = null;
        depositTableDetails = null;
        deletedDepositTableValues = null;
        losTableDetails = null;
        deletedLosTableValues = null;
    }
    //trans details
    
    private void authorize(HashMap map) throws Exception {
        String status = (String) map.get("STATUS");
        String linkBatchId = null;
        String appNo = null;
        HashMap cashAuthMap;
        TransactionTO objTransactionTO = null;
        try {
            //sqlMap.startTransaction();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            System.out.println("map:" + map);
            appNo = CommonUtil.convertObjToStr(map.get("APPLICATION_NO"));
            sqlMap.executeUpdate("authorizeBorrowingRepIntCls", map);
            linkBatchId = CommonUtil.convertObjToStr(map.get("APPLICATION_NO"));//Transaction Batch Id
            //Separation of Authorization for Cash and Transfer
            //Call this in all places that need Authorization for Transaction
            if (map != null && map.containsKey("SERVICE_TAX_AUTH")) {
                sqlMap.executeUpdate("authorizeServiceTaxDetails", map);
            }
            cashAuthMap = new HashMap();
            cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            // cashAuthMap.put("DAILY", "DAILY");
            cashAuthMap.put("DAILY", "DAILY");
            TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
            HashMap transMap = new HashMap();
            transMap.put("LINK_BATCH_ID", linkBatchId);
            sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
            sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
            transMap = null;
            objTransactionTO = new TransactionTO();
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(appNo));
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
            objTransactionTO.setBranchId(_branchCode);
            //   sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
            if (!status.equals("REJECTED")) {
            }
            map = null;
            //sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
        //trans end..
    private Date setProperDtFormat(Date dt) {
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
