/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * LoanRepaymentDAO.java
 * 
 * Created on Fri Jun 21 11:33:46 IST 2013
 */
package com.see.truetransact.serverside.termloan.kcctopacs;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;

/**
 * This is used for LoanRepaymentDAO Data Access.
 *
 * @author Suresh R
 *
 */
public class LoanRepaymentDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    private Date currDt = null;
    HashMap returnMap = new HashMap();
    private final static Logger log = Logger.getLogger(LoanRepaymentDAO.class);
    private int yearTobeAdded = 1900;
    TransactionTO transactionTO = new TransactionTO();

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public LoanRepaymentDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("#### GetData map : " + map);
        returnMap = new HashMap();
        List releaseList = (List) sqlMap.executeQueryForList("getLoanRepaymentDetails", map);
        if (releaseList != null && releaseList.size() > 0) {
            returnMap.put("RELEASE_LIST_DATA", releaseList);
            releaseList = null;
        }
        String where = (String) map.get("LOAN_REPAY_NO");
        List transList = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTO", where);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSACTION_LIST", transList);
            transList = null;
        }
        getTransDetails(where);
        System.out.println("########## returnMap : " + returnMap);
        return returnMap;
    }

    private String getLoanRepaymentNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "KCC_REPAYMENT_ID");
        String loanRepayNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return loanRepayNo;
    }

    public HashMap execute(HashMap map) throws Exception {

        System.out.println("######## LoanRepayment Map DAO : " + map);
        returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
//            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(map);
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap, map);
            }
        }
        map = null;
        destroyObjects();
        System.out.println("######## returnMap DAO : " + returnMap);
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if (map.containsKey("RELEASE_LIST_DATA") && map.containsKey("APPROPRIATE_LIST_DATA") && map.containsKey("TransactionTO")) {
                String loanRepayNo = "";
                loanRepayNo = getLoanRepaymentNo();
                returnMap.put("LOAN_REPAY_NO", loanRepayNo);
                //Transaction Start
                transactionPart(map, loanRepayNo);
                HashMap releaseMap = new HashMap();
                List releaseList = (List) map.get("RELEASE_LIST_DATA");
                for (int i = 0; i < releaseList.size(); i++) {
                    releaseMap = new HashMap();
                    releaseMap = (HashMap) releaseList.get(i);
                    releaseMap.put("LOAN_REPAY_NO", loanRepayNo);
                    releaseMap.put("NCL_SANCTION_NO", map.get("NCL_SANCTION_NO"));
                    releaseMap.put("REPAY_DATE", currDt);
                    releaseMap.put("PENAL", "");
                    releaseMap.put("CHARGES", "");
                    releaseMap.put("REPAY_AMOUNT", map.get("REPAYMENT_AMOUNT"));
                    releaseMap.put("STATUS", map.get("STATUS"));
                    releaseMap.put("STATUS_BY", map.get("STATUS_BY"));
                    releaseMap.put("STATUS_DT", currDt);
                    System.out.println("######## releaseMap : " + releaseMap);
                    sqlMap.executeUpdate("insertLoanRepaymentDetails", releaseMap);
                }

                HashMap appropriateMap = new HashMap();
                ArrayList rowList = new ArrayList();
                List appropriateList = (List) map.get("APPROPRIATE_LIST_DATA");
                for (int i = 0; i < appropriateList.size(); i++) {
                    appropriateMap = new HashMap();
                    rowList = (ArrayList) appropriateList.get(i);
                    appropriateMap.put("LOAN_REPAY_NO", loanRepayNo);
                    appropriateMap.put("RELEASE_NO", CommonUtil.convertObjToStr(rowList.get(1)));
                    appropriateMap.put("PRINCIPAL_TO_PAY", CommonUtil.convertObjToStr(rowList.get(4)));
                    appropriateMap.put("INT_UP_TO_DATE_AMT", CommonUtil.convertObjToStr(rowList.get(5)));
                    appropriateMap.put("INT_AFTER_DUE_DATE_AMT", CommonUtil.convertObjToStr(rowList.get(6)));
                    appropriateMap.put("PENAL", CommonUtil.convertObjToStr(rowList.get(7)));
                    appropriateMap.put("CHARGES", CommonUtil.convertObjToStr(rowList.get(8)));
                    System.out.println("######## appropriateMap : " + appropriateMap);
                    sqlMap.executeUpdate("insertAppropriateLoanRepaymentDetails", appropriateMap);
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    public void transactionPart(HashMap map, String loanRepayNo) throws Exception {
        System.out.println("########### TRANSACTION PART####### ");
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        HashMap txMap = new HashMap();
        HashMap transMap = new HashMap();
        HashMap debitMap = new HashMap();
        double transAmt = 0.0;
        if (map.containsKey("TransactionTO")) {
            HashMap creditMap = new HashMap();
            creditMap.put("PROD_ID", map.get("KCC_PROD_ID"));
            List creditList = (List) sqlMap.executeQueryForList("getAllLoanAccountHeads", creditMap);
            if (creditList != null && creditList.size() > 0) {
                creditMap = (HashMap) creditList.get(0);
            }
            double principal = 0.0;
            double intUptoDueAmt = 0.0;
            double intAfterDueAmt = 0.0;
            double penal = 0.0;
            double charges = 0.0;
            ArrayList rowList = new ArrayList();
            List appropriateList = (List) map.get("APPROPRIATE_LIST_DATA");
            for (int i = 0; i < appropriateList.size(); i++) {
                rowList = (ArrayList) appropriateList.get(i);
                principal += CommonUtil.convertObjToDouble(rowList.get(4));
                intUptoDueAmt += CommonUtil.convertObjToDouble(rowList.get(5));
                intAfterDueAmt += CommonUtil.convertObjToDouble(rowList.get(6));
                penal += CommonUtil.convertObjToDouble(rowList.get(7));
                charges += CommonUtil.convertObjToDouble(rowList.get(8));
            }
            System.out.println("########### principal       : " + principal);
            System.out.println("########### intUptoDueAmt   : " + intUptoDueAmt);
            System.out.println("########### intAfterDueAmt  : " + intAfterDueAmt);
            System.out.println("########### penal           : " + penal);
            System.out.println("########### charges         : " + charges);


            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            transAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();

            //Transafer
            if (transactionTO.getTransType().equals("TRANSFER") && transAmt > 0) {
                //DEBIT PART
                ArrayList transferList = new ArrayList();
                TransferTrans transferTrans = new TransferTrans();
                transferTrans.setInitiatedBranch(BRANCH_ID);
                TransferTrans objTransferTrans = new TransferTrans();
                objTransferTrans.setInitiatedBranch(_branchCode);
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                HashMap tansactionMap = new HashMap();
                TxTransferTO transferTo = new TxTransferTO();
                ArrayList TxTransferTO = new ArrayList();
                if (!transactionTO.getProductType().equals("") && transactionTO.getProductType().equals("GL") && transactionTO.getProductId().equals("")) {
                    txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                } else {
                    txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(transactionTO.getProductType()));
                    txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                    txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(transactionTO.getProductId()));
                }
                txMap.put(TransferTrans.PARTICULARS, "KCC_CURRENT_ACCOUNT");
                txMap.put("AUTHORIZEREMARKS", "KCC_CURRENT_ACCOUNT");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmt));
//                //CREDIT PART
//                HashMap creditMap = new HashMap();
//                creditMap.put("PROD_ID", map.get("KCC_PROD_ID"));
//                List creditList = (List) sqlMap.executeQueryForList("getAllLoanAccountHeads", creditMap);
//                if (creditList != null && creditList.size() > 0) {
//                    creditMap = (HashMap) creditList.get(0);
//                }
//                double principal = 0.0;
//                double intUptoDueAmt = 0.0;
//                double intAfterDueAmt = 0.0;
//                double penal = 0.0;
//                double charges = 0.0;
//                ArrayList rowList = new ArrayList();
//                List appropriateList = (List) map.get("APPROPRIATE_LIST_DATA");
//                for (int i = 0; i < appropriateList.size(); i++) {
//                    rowList = (ArrayList) appropriateList.get(i);
//                    principal += CommonUtil.convertObjToDouble(rowList.get(4));
//                    intUptoDueAmt += CommonUtil.convertObjToDouble(rowList.get(5));
//                    intAfterDueAmt += CommonUtil.convertObjToDouble(rowList.get(6));
//                    penal += CommonUtil.convertObjToDouble(rowList.get(7));
//                    charges += CommonUtil.convertObjToDouble(rowList.get(8));
//                }
//                System.out.println("########### principal       : " + principal);
//                System.out.println("########### intUptoDueAmt   : " + intUptoDueAmt);
//                System.out.println("########### intAfterDueAmt  : " + intAfterDueAmt);
//                System.out.println("########### penal           : " + penal);
//                System.out.println("########### charges         : " + charges);

                //INTEREST
                if (intUptoDueAmt > 0 || intAfterDueAmt > 0) {
                    double totalIntAmt = 0.0;
                    totalIntAmt = intUptoDueAmt + intAfterDueAmt;
                    txMap.put(TransferTrans.CR_AC_HD, creditMap.get("AC_CREDIT_INT"));
                    txMap.put("AUTHORIZEREMARKS", "KCC_REPAYMENT_INTEREST");
                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, "INTEREST");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, totalIntAmt));
                }
                //PENAL
                if (penal > 0) {
                    txMap.put(TransferTrans.CR_AC_HD, creditMap.get("PENAL_INT"));
                    txMap.put("AUTHORIZEREMARKS", "KCC_REPAYMENT_PENAL_INT");
                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, "PENAL_INT");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, penal));
                }
                //CHARGES
                if (charges > 0) {
                    txMap.put(TransferTrans.CR_AC_HD, creditMap.get("ARBITRARY_CHARGES"));
                    txMap.put("AUTHORIZEREMARKS", "KCC_REPAYMENT_CHARGES");
                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, "CHARGES");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, charges));
                }
                //PRINCIPAL
                if (principal > 0) {
                    txMap.put(TransferTrans.CR_AC_HD, creditMap.get("ACCT_HEAD"));
                    txMap.put("AUTHORIZEREMARKS", "KCC_REPAYMENT_PRINCIPAL");
                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.ADVANCES);
                    txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(map.get("KCC_PROD_ID")));
                    txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(map.get("KCC_ACC_NO")));
                    txMap.put(TransferTrans.PARTICULARS, "PRINCIPAL");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, principal));
                }

                if (transferList != null && transferList.size() > 0) {
                    System.out.println("########### transferList         : " + transferList);
                    doDebitCredit(transferList, _branchCode, false, map, loanRepayNo);
                }
            } else if (transactionTO.getTransType().equals("CASH") && transAmt > 0) {
                ArrayList cashList = new ArrayList();
                HashMap cashTransMap = new HashMap();
                cashTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                cashTransMap.put("BRANCH_CODE", _branchCode);
                cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                cashTransMap.put("USER_ID", map.get("USER_ID"));
//                cashTransMap.put("INVESTMENT_NO", objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());

                //INTEREST
                if (intUptoDueAmt > 0 || intAfterDueAmt > 0) {
                    double totalIntAmt = 0.0;
                    totalIntAmt = intUptoDueAmt + intAfterDueAmt;
                    cashTransMap.put("ACCT_HEAD", creditMap.get("AC_CREDIT_INT"));
                    cashTransMap.put("TRANS_AMOUNT", String.valueOf(totalIntAmt));
                    cashTransMap.put("AUTHORIZEREMARKS", "KCC_REPAYMENT_INTEREST");
                    cashTransMap.put("PARTICULARS", "INTEREST");
                    cashTransMap.put("PROD_TYPE", TransactionFactory.GL);
                    cashList.add(createCashTransactionTO(cashTransMap, loanRepayNo));
                }

                //PENAL
                if (penal > 0) {
                    cashTransMap.put("ACCT_HEAD", creditMap.get("PENAL_INT"));
                    cashTransMap.put("TRANS_AMOUNT", String.valueOf(penal));
                    cashTransMap.put("AUTHORIZEREMARKS", "KCC_REPAYMENT_PENAL_INT");
                    cashTransMap.put("PARTICULARS", "PENAL_INT");
                    cashTransMap.put("PROD_TYPE", TransactionFactory.GL);
                    cashList.add(createCashTransactionTO(cashTransMap, loanRepayNo));
                }

                //CHARGES
                if (charges > 0) {
                    cashTransMap.put("ACCT_HEAD", creditMap.get("ARBITRARY_CHARGES"));
                    cashTransMap.put("TRANS_AMOUNT", String.valueOf(charges));
                    cashTransMap.put("AUTHORIZEREMARKS", "KCC_REPAYMENT_CHARGES");
                    cashTransMap.put("PARTICULARS", "CHARGES");
                    cashTransMap.put("PROD_TYPE", TransactionFactory.GL);
                    cashList.add(createCashTransactionTO(cashTransMap, loanRepayNo));
                }

                //PRINCIPAL
                if (principal > 0) {
                    cashTransMap.put("ACCT_HEAD", creditMap.get("ACCT_HEAD"));
                    cashTransMap.put("TRANS_AMOUNT", String.valueOf(principal));
                    cashTransMap.put("AUTHORIZEREMARKS", "KCC_REPAYMENT_PRINCIPAL");
                    cashTransMap.put("PARTICULARS", "PRINCIPAL");
                    cashTransMap.put("PROD_TYPE", TransactionFactory.ADVANCES);
                    cashTransMap.put("PROD_ID", CommonUtil.convertObjToStr(map.get("KCC_PROD_ID")));
                    cashTransMap.put("ACT_NUM", CommonUtil.convertObjToStr(map.get("KCC_ACC_NO")));
                    cashList.add(createCashTransactionTO(cashTransMap, loanRepayNo));
                }
                System.out.print("###### cashList: " + cashList);
                if (cashList != null && cashList.size() > 0) {
                    doCashTrans(cashList, _branchCode, map, false, loanRepayNo);
                }
            }
            getTransDetails(loanRepayNo);
        }
    }

    private void doCashTrans(ArrayList batchList, String branchCode, HashMap map, boolean isAutoAuthorize, String loanRepayNo) throws Exception {
        CashTransactionDAO cashDAO = new CashTransactionDAO();
        HashMap data = new HashMap();
        data.put("DAILYDEPOSITTRANSTO", batchList);
        data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, map.get("USER_ID"));
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "Loan Repayment");
        data.put("MODE", "MODE");
        HashMap loanDataMap = new HashMap();
        loanDataMap = cashDAO.execute(data, false);
        System.out.println("############ LoanDataMap : " + loanDataMap);
        //Insert Remit Issue Trans Table
        if (transactionTO == null) {
            transactionTO = new TransactionTO();
        }
        transactionTO.setBatchId(loanRepayNo);
        transactionTO.setBatchDt(currDt);
        transactionTO.setTransId(loanRepayNo);
        transactionTO.setStatus(CommonUtil.convertObjToStr(map.get("COMMAND")));
        transactionTO.setBranchId(_branchCode);
        System.out.println("objTransactionTO------------------->" + transactionTO);
        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
    }

    private CashTransactionTO createCashTransactionTO(HashMap dataMap, String loanRepayNo) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
        if (dataMap.get("PROD_TYPE").equals("GL")) {
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
        } else {
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.ADVANCES));
            objCashTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
            objCashTO.setActNum(CommonUtil.convertObjToStr(dataMap.get("ACT_NUM")));
        }
        objCashTO.setTransType(CommonConstants.CREDIT);
        objCashTO.setParticulars("By Cash : " + dataMap.get("PARTICULARS"));
        objCashTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(dataMap.get("AUTHORIZEREMARKS")));
        objCashTO.setInpAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
        objCashTO.setAmount(CommonUtil.convertObjToDouble(dataMap.get("TRANS_AMOUNT")));
        objCashTO.setInitTransId(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
        objCashTO.setBranchId(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        objCashTO.setStatusBy(CommonUtil.convertObjToStr(dataMap.get("USER_ID")));
        objCashTO.setStatusDt(currDt);
        objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
        objCashTO.setInitiatedBranch(CommonUtil.convertObjToStr(dataMap.get("BRANCH_CODE")));
        objCashTO.setInitChannType(CommonConstants.CASHIER);
        objCashTO.setCommand("INSERT");
        objCashTO.setLinkBatchId(loanRepayNo);
        System.out.println("########### objCashTO : " + objCashTO);
        return objCashTO;
    }

    private void doDebitCredit(ArrayList batchList, String branchCode, boolean isAutoAuthorize, HashMap map, String loanRepayNo) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", map.get("COMMAND"));
        data.put("INITIATED_BRANCH", _branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, map.get("USER_ID"));
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        data.put("LINK_BATCH_ID", loanRepayNo);
        System.out.println("########### data         : " + data);
        HashMap loanDataMap = new HashMap();
        loanDataMap = transferDAO.execute(data, false);

        //Insert Remit Issue Trans Table
        if (transactionTO == null) {
            transactionTO = new TransactionTO();
        }
        transactionTO.setBatchId(loanRepayNo);
        transactionTO.setBatchDt(currDt);
        transactionTO.setTransId(loanRepayNo);
        transactionTO.setStatus(CommonUtil.convertObjToStr(map.get("COMMAND")));
        transactionTO.setBranchId(_branchCode);
        System.out.println("objTransactionTO------------------->" + transactionTO);
        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        List transList = (List) sqlMap.executeQueryForList("getTransferDetailsWithAccHeadDesc", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetailsWithAccHeadDesc", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            deleteTransactionData(map);
            sqlMap.executeUpdate("updateLoanRepaymentStatus", map);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteTransactionData(HashMap map) throws Exception {
        TransferTrans transferTrans = new TransferTrans();
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        transferTrans.setInitiatedBranch(BRANCH_ID);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        transactionDAO.setInitiatedBranch(_branchCode);
        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_DELETE);
        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
        HashMap tempMap = new HashMap();
        tempMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(map.get("LOAN_REPAY_NO")));
        tempMap.put("TRANS_DT", currDt);
        tempMap.put("INITIATED_BRANCH", BRANCH_ID);
        ArrayList batchList = new ArrayList();
        TxTransferTO txTransferTO = null;
        HashMap oldAmountMap = new HashMap();
        List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", tempMap);
        if (lst != null) {
            for (int i = 0; i < lst.size(); i++) {
                txTransferTO = (TxTransferTO) lst.get(i);
                txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                txTransferTO.setStatusDt(currDt);
                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                oldAmountMap.put(txTransferTO.getTransId(), CommonUtil.convertObjToDouble(txTransferTO.getAmount()));
                batchList.add(txTransferTO);
            }
        }
        transferTrans = new TransferTrans();
        transferTrans.setOldAmount(oldAmountMap);
        transferTrans.setInitiatedBranch(BRANCH_ID);
        transferTrans.doDebitCredit(batchList, BRANCH_ID, false, CommonConstants.TOSTATUS_DELETE);
        lst = null;
        transferTrans = null;
        txTransferTO = null;
        batchList = null;
        oldAmountMap = null;
        tempMap = null;
    }

    private void authorize(HashMap map, HashMap repayMap) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap : " + AuthMap);
        try {
            sqlMap.startTransaction();
            authorizeTransaction(AuthMap, map, repayMap);
            sqlMap.executeUpdate("authorizeLoanRepayment", AuthMap);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public void authorizeTransaction(HashMap AuthMap, HashMap map, HashMap repayMap) throws Exception {
        try {
            System.out.println("############### repayMap : " + repayMap);
            if (AuthMap != null && AuthMap.get("LOAN_REPAY_NO") != null && !AuthMap.get("LOAN_REPAY_NO").equals("")) {
                String authorizeStatus = CommonUtil.convertObjToStr(AuthMap.get("STATUS"));
                String linkBatchId = CommonUtil.convertObjToStr(AuthMap.get("LOAN_REPAY_NO"));
                HashMap transAuthMap = new HashMap();
                transAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
                transAuthMap.put(CommonConstants.USER_ID, repayMap.get(CommonConstants.USER_ID));
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, transAuthMap);
                transAuthMap = null;

                //Insert Loan Trans Details Table && Updating Last Interest Calculation Date And Clear Balance  
                if (authorizeStatus.equals("AUTHORIZED")) {
                    HashMap loanTransMap = new HashMap();
                    HashMap appropriateMap = new HashMap();
                    appropriateMap.put("LOAN_REPAY_NO", linkBatchId);
                    List appropriateList = (List) sqlMap.executeQueryForList("getAppropriateRepaymentDetails", appropriateMap);

                    //get CustCategory
                    HashMap whereMap = new HashMap();
                    String category = "";
                    whereMap.put("ACT_NUM", repayMap.get("KCC_ACC_NO"));
                    whereMap.put("PROD_ID", repayMap.get("KCC_PROD_ID"));
                    List loanCategoryList = (List) sqlMap.executeQueryForList("getCategoryForIntSubsidy", whereMap);
                    if (loanCategoryList != null && loanCategoryList.size() > 0) {
                        whereMap = (HashMap) loanCategoryList.get(0);
                        category = CommonUtil.convertObjToStr(whereMap.get("CATEGORY"));
                    }
                    if (appropriateList != null && appropriateList.size() > 0) {
                        for (int i = 0; i < appropriateList.size(); i++) {
                            appropriateMap = (HashMap) appropriateList.get(i);
                            loanTransMap = new HashMap();
                            HashMap releaseMap = new HashMap();
                            releaseMap.put("LOAN_REPAY_NO", linkBatchId);
                            releaseMap.put("RELEASE_NO", appropriateMap.get("RELEASE_NO"));
                            List releaseList = (List) sqlMap.executeQueryForList("getLoanRepayDetails", releaseMap);
                            if (releaseList != null && releaseList.size() > 0) {
                                releaseMap = (HashMap) releaseList.get(0);
                                loanTransMap.put("ACCOUNTNO", appropriateMap.get("RELEASE_NO"));
//                                loanTransMap.put("PRINCIPLE", CommonUtil.convertObjToDouble(appropriateMap.get("PRINCIPAL")));
                                loanTransMap.put("PRINCIPLE", String.valueOf(new Double(CommonUtil.convertObjToDouble(appropriateMap.get("PRINCIPAL")).doubleValue())));
//                                loanTransMap.put("INTEREST", CommonUtil.convertObjToStr(appropriateMap.get("INT_UPTO_DUE_DT")));
                                loanTransMap.put("INTEREST", String.valueOf(new Double(CommonUtil.convertObjToDouble(appropriateMap.get("INT_UPTO_DUE_DT")).doubleValue())));
//                                loanTransMap.put("PENAL", CommonUtil.convertObjToDouble(appropriateMap.get("PENAL")));
                                loanTransMap.put("PENAL", String.valueOf(new Double(CommonUtil.convertObjToDouble(appropriateMap.get("PENAL")).doubleValue())));
                                loanTransMap.put("PROD_ID", "");
                                loanTransMap.put("TRANSTYPE", "CREDIT");
                                loanTransMap.put("BRANCH_CODE", _branchCode);
                                loanTransMap.put("TRN_CODE", "C*");
                                loanTransMap.put("TODAY_DT", currDt);
                                loanTransMap.put("EFFECTIVE_DT", currDt);
                                loanTransMap.put("IBAL", "");
                                loanTransMap.put("PIBAL", "");
                                if (CommonUtil.convertObjToDouble(appropriateMap.get("PRINCIPAL")).doubleValue() > 0) {
                                    loanTransMap.put("PBAL", String.valueOf(CommonUtil.convertObjToDouble(releaseMap.get("CLEAR_BALANCE"))
                                            - CommonUtil.convertObjToDouble(appropriateMap.get("PRINCIPAL"))));
                                } else {
                                    loanTransMap.put("PBAL", String.valueOf(new Double(CommonUtil.convertObjToDouble(releaseMap.get("CLEAR_BALANCE")).doubleValue())));
                                }
                                loanTransMap.put("TRANS_ID", linkBatchId);
                                loanTransMap.put("AUTHORIZE_STATUS", authorizeStatus);
                                loanTransMap.put("AUTHORIZE_BY", repayMap.get("USER_ID"));
                                loanTransMap.put("TRANS_SLNO", new Long(1));
                                loanTransMap.put("EXPENSE", "");
                                loanTransMap.put("EBAL", "");
                                loanTransMap.put("NPA_INTEREST", "");
                                loanTransMap.put("NPA_INT_BAL", "");
                                loanTransMap.put("NPA_PENAL", "");
                                loanTransMap.put("NPA_PENAL_BAL", "");
                                loanTransMap.put("EXCESS_AMT", "");
                                loanTransMap.put("TRANS_MODE", "");
                                sqlMap.executeUpdate("insertintoloanTransAuthDetails", loanTransMap);
                                sqlMap.executeUpdate("updateRepaymentClearBalance", loanTransMap);

                                //Last Interest Calculation Date Updating
                                if (CommonUtil.convertObjToDouble(releaseMap.get("INT_UPTO_DUE_DT"))
                                        <= CommonUtil.convertObjToDouble(appropriateMap.get("INT_UPTO_DUE_DT"))) {
                                    releaseMap.put("LAST_INT_CALC_DT", currDt);
                                    sqlMap.executeUpdate("updateReleaseLastIntCalcDate", releaseMap);
                                }

                                //INSERT INTO SUB_SIDY TABLE
                                HashMap categoryMap = new HashMap();
                                double custROI = 0.0;
                                double subsidyROI = 0.0;
                                double intAmount = 0.0;
                                double interestSubsidyAmt = 0.0;
                                Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(releaseMap.get("DUE_DATE")));
                                intAmount = CommonUtil.convertObjToDouble(appropriateMap.get("INT_UPTO_DUE_DT"));
                                categoryMap.put("RELEASE_NO", appropriateMap.get("RELEASE_NO"));
                                List releaseDateList = (List) sqlMap.executeQueryForList("NCLAmountReleaseDetails", categoryMap);
                                if (releaseDateList != null && releaseDateList.size() > 0 && intAmount > 0 && !category.equals("")) {
                                    System.out.println("############# currDt  : " + currDt);
                                    System.out.println("############# dueDate : " + dueDate);
                                    if (DateUtil.dateDiff(currDt, dueDate) > 0) {
                                        categoryMap = (HashMap) releaseDateList.get(0);
                                        categoryMap.put("CUR_DT", categoryMap.get("RELEASE_DATE"));
                                        categoryMap.put("PRODUCT_TYPE", "AD");
                                        categoryMap.put("PROD_ID", repayMap.get("KCC_PROD_ID"));
                                        categoryMap.put("CATEGORY_ID", category);
                                        System.out.println("####### categoryMap : " + categoryMap);
                                        List intCategoryList = (List) sqlMap.executeQueryForList("getIntSubsidyRateOfInt", categoryMap);
                                        if (intCategoryList != null && intCategoryList.size() > 0) {
                                            for (int j = 0; j < intCategoryList.size(); j++) {
                                                HashMap subsidyMap = new HashMap();
                                                interestSubsidyAmt = 0.0;
                                                subsidyMap = (HashMap) intCategoryList.get(j);
                                                custROI = CommonUtil.convertObjToDouble(subsidyMap.get("CUST_ROI"));
                                                subsidyROI = CommonUtil.convertObjToDouble(subsidyMap.get("ROI_INTEREST"));
                                                interestSubsidyAmt = (intAmount * subsidyROI) / custROI;
                                                Rounding rod = new Rounding();
                                                interestSubsidyAmt = (double) rod.getNearest((long) (interestSubsidyAmt * 100), 100) / 100;
                                                subsidyMap.put("ACCOUNTNO", appropriateMap.get("RELEASE_NO"));
                                                subsidyMap.put("RATE", String.valueOf(subsidyROI));
                                                subsidyMap.put("DATE", currDt);
                                                subsidyMap.put("AMOUNT", String.valueOf(interestSubsidyAmt));
                                                System.out.println("############### subsidyMap : " + subsidyMap);
                                                sqlMap.executeUpdate("insertIndividualSubsidy", subsidyMap);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String str[]) {
        try {
            ReleaseDetailsDAO dao = new ReleaseDetailsDAO();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void destroyObjects() {
    }
}