/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * InterestSubsidyAdjustmentDAO.java
 * 
 * Created on Fri Apr 19 11:33:46 IST 2013
 */
package com.see.truetransact.serverside.termloan.kcctopacs;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.GregorianCalendar;
import java.util.Date;
import org.apache.log4j.Logger;
import java.util.LinkedHashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.transferobject.product.operativeacct.*;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.termloan.kcctopacs.InterestSubsidyAdjustmentTO;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclClassificationTO;
import com.see.truetransact.transferobject.termloan.kcctopacs.NclAmtSlabWiseDetTO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.interestcalc.Rounding;

/**
 * This is used for InterestSubsidyAdjustmentDAO Data Access.
 *
 * @author Suresh
 *
 */
public class InterestSubsidyAdjustmentDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private InterestSubsidyAdjustmentTO objInterestSubsidyAdjustmentTO;
    private TransactionDAO transactionDAO = null;
    TransferDAO transferDAO = new TransferDAO();
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt = null;
    HashMap returnMap = new HashMap();
    private LinkedHashMap membertableDetails = null;
    private LinkedHashMap loantableDetails = null;
    private NclClassificationTO objClasficationDetailsTO;
    private NclAmtSlabWiseDetTO objLoanSlabDetailsTO;
    private final static Logger log = Logger.getLogger(InterestSubsidyAdjustmentDAO.class);
    private int yearTobeAdded = 1900;
    TransactionTO transactionTO = new TransactionTO();

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public InterestSubsidyAdjustmentDAO() throws ServiceLocatorException {
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
        List subsidyList = (List) sqlMap.executeQueryForList("getSelectInterestSubsidyAdjustmentTO", map);
        if (subsidyList != null && subsidyList.size() > 0) {
            returnMap.put("SubsidyInterestData", subsidyList);
            subsidyList = null;
        }
        //Get Subsidy Due Details
        List dueList = (List) sqlMap.executeQueryForList("getSelectInterestSubsidyDueDetails", map);
        if (dueList != null && dueList.size() > 0) {
            returnMap.put("SUBSIDY_DUE_LIST", dueList);
            dueList = null;
        }
        String where = (String) map.get("ADJUSTMENT_NO");
        List transList = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTO", where);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSACTION_LIST", transList);
            transList = null;
        }

        getTransDetails(CommonUtil.convertObjToStr(map.get("ADJUSTMENT_NO")));
        System.out.println("########## returnMap : " + returnMap);
        return returnMap;
    }

    private String getAdjustmentNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SUBSIDY_NO");
        String adjustmentNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return adjustmentNo;
    }

    public HashMap execute(HashMap map) throws Exception {

        System.out.println("######## SubsidyInterest Map DAO : " + map);
        returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        objInterestSubsidyAdjustmentTO = (InterestSubsidyAdjustmentTO) map.get("SubsidyInterestData");
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
                authorize(authMap);
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
            if (map.containsKey("SUBSIDY_DUE_LIST")) {
                String adjustmentNo = "";
                adjustmentNo = getAdjustmentNo();
                objInterestSubsidyAdjustmentTO.setAdjustmentNo(adjustmentNo);

                if (map.containsKey("PRINCIPAL_SUBSIDY") && map.get("PRINCIPAL_SUBSIDY").equals("Y")) {
                    principalTransactionPart(map, adjustmentNo);
                } else {
                    transactionPart(map, adjustmentNo);
                }
                HashMap releaseMap = new HashMap();
                List releaseList = (List) map.get("SUBSIDY_DUE_LIST");
                if (releaseList != null && releaseList.size() > 0) {
                    for (int i = 0; i < releaseList.size(); i++) {
                        releaseMap = new HashMap();
                        releaseMap = (HashMap) releaseList.get(i);
                        releaseMap.put("ADJUSTMENT_NO", adjustmentNo);
                        System.out.println("######## releaseMap : " + releaseMap);
                        if (CommonUtil.convertObjToDouble(releaseMap.get("TODAYS_ADJUSTMENT_AMOUNT")).doubleValue() > 0
                                || CommonUtil.convertObjToDouble(releaseMap.get("RECOVERY_FROM_CUST_AMOUNT")).doubleValue() > 0
                                || CommonUtil.convertObjToDouble(releaseMap.get("WRITE_OFF_AMOUNT")).doubleValue() > 0
                                || CommonUtil.convertObjToDouble(releaseMap.get("OTS_AMOUNT")).doubleValue() > 0) {
                            sqlMap.executeUpdate("insertSubsidyIntDueDetails", releaseMap);
                        }
                    }
                }
                sqlMap.executeUpdate("insertInterestSubsidyAdjustmentTO", objInterestSubsidyAdjustmentTO);
                logTO.setData(objInterestSubsidyAdjustmentTO.toString());
                logTO.setPrimaryKey(objInterestSubsidyAdjustmentTO.getKeyData());
                logTO.setStatus(objInterestSubsidyAdjustmentTO.getStatus());
                logDAO.addToLog(logTO);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    public void transactionPart(HashMap map, String adjustmentNo) throws Exception {
        System.out.println("########### TRANSACTION PART####### ");
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        HashMap txMap = new HashMap();
        HashMap transMap = new HashMap();
        HashMap debitMap = new HashMap();
        double transAmt = 0.0;
        if (map.containsKey("TransactionTO")) {
            HashMap transactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
            LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
            if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
            }
            transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
            transAmt = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
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
                transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(BRANCH_ID);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("DP");
                transactionDAO.setLinkBatchID(adjustmentNo);
                TxTransferTO.add(transferTo);

                //CREDIT PART
                HashMap creditMap = new HashMap();
                creditMap.put("PROD_ID", objInterestSubsidyAdjustmentTO.getProdId());
                List creditList = (List) sqlMap.executeQueryForList("getAllLoanAccountHeads", creditMap);
                if (creditList != null && creditList.size() > 0) {
                    creditMap = (HashMap) creditList.get(0);
                }
                txMap = new HashMap();
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.CR_AC_HD, (String) creditMap.get("AC_CREDIT_INT"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.PARTICULARS, creditMap.get("AC_CREDIT_INT") + "-" + ":Interest Subsidy");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setLinkBatchId(adjustmentNo);
                transactionDAO.setLinkBatchID(adjustmentNo);
                transactionTO.setChequeNo("SERVICE_TAX");
                TxTransferTO.add(transferTo);

                transferDAO = new TransferDAO();
                tansactionMap.put("TxTransferTO", TxTransferTO);
                tansactionMap.put("MODE", map.get("COMMAND"));
                tansactionMap.put("COMMAND", map.get("COMMAND"));
                tansactionMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                tansactionMap.put("LINK_BATCH_ID", adjustmentNo);
                System.out.println("################ tansactionMap :" + tansactionMap);
                transMap = transferDAO.execute(tansactionMap, false);
                //Insert 
                transactionTO.setBatchId(adjustmentNo);
                transactionTO.setBatchDt(currDt);
                transactionTO.setStatus(CommonConstants.STATUS_CREATED);
                transactionTO.setTransId(adjustmentNo);
                System.out.println("transactionTO------------------->" + transactionTO);
                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
                getTransDetails(adjustmentNo);
            }
        }
    }

    public void principalTransactionPart(HashMap map, String adjustmentNo) throws Exception {
        System.out.println("########### PRINCIPAL TRANSACTION PART ####### : ");
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        HashMap txMap = new HashMap();
        HashMap transMap = new HashMap();
        double transAmt = 0.0;
        HashMap releaseMap = new HashMap();
        HashMap tansactionMap = new HashMap();
        ArrayList TxTransferTO = new ArrayList();
        TxTransferTO transferTo = new TxTransferTO();
        List releaseList = (List) map.get("SUBSIDY_DUE_LIST");
        if (releaseList != null && releaseList.size() > 0) {
            //CREDIT PART
            HashMap creditMap = new HashMap();
            creditMap.put("PROD_ID", objInterestSubsidyAdjustmentTO.getProdId());
            List creditList = (List) sqlMap.executeQueryForList("getAllLoanAccountHeads", creditMap);
            if (creditList != null && creditList.size() > 0) {
                creditMap = (HashMap) creditList.get(0);
            }
            for (int i = 0; i < releaseList.size(); i++) {
                releaseMap = new HashMap();
                releaseMap = (HashMap) releaseList.get(i);
                System.out.println("######## releaseMap : " + releaseMap);
                transAmt = 0.0;
                transAmt = CommonUtil.convertObjToDouble(releaseMap.get("TODAYS_ADJUSTMENT_AMOUNT")).doubleValue()
                        + CommonUtil.convertObjToDouble(releaseMap.get("RECOVERY_FROM_CUST_AMOUNT")).doubleValue()
                        + CommonUtil.convertObjToDouble(releaseMap.get("WRITE_OFF_AMOUNT")).doubleValue()
                        + CommonUtil.convertObjToDouble(releaseMap.get("OTS_AMOUNT")).doubleValue();
                System.out.println("######## transAmt : " + transAmt);
                if (transAmt > 0) {
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
                    transferTo = new TxTransferTO();
                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(releaseMap.get("SUBSIDY_ADJUST_ACHD")));
                    txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                    txMap.put(TransferTrans.PARTICULARS, "SUBSIDY_ADJUST_ACHD");
                    txMap.put("AUTHORIZEREMARKS", "SUBSIDY_ADJUST_ACHD");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                    transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setTransDt(currDt);
                    transferTo.setBranchId(BRANCH_ID);
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setAuthorizeRemarks("DP");
                    transactionDAO.setLinkBatchID(adjustmentNo);
                    TxTransferTO.add(transferTo);


                    //CREDIT PART
                    txMap = new HashMap();
                    transferTo = new TxTransferTO();
                    txMap = new HashMap();
                    transferTo.setInstrumentNo2("APPL_GL_TRANS");
                    txMap.put(TransferTrans.CR_AC_HD, (String) creditMap.get("ACCT_HEAD"));
                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.LOANS);
                    txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getProdId()));
                    txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(releaseMap.get("ACT_NUM")));
                    txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                    txMap.put(TransferTrans.PARTICULARS, creditMap.get("ACCT_HEAD") + "-" + ":Principal Subsidy");
                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                    txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                    txMap.put("AUTHORIZEREMARKS", "PRINCIPAL SUBSIDY");
                    transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                    transferTo.setTransId("-");
                    transferTo.setBatchId("-");
                    transferTo.setTransDt(currDt);
                    transferTo.setInitiatedBranch(BRANCH_ID);
                    transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                    transferTo.setLinkBatchId(adjustmentNo);
                    transactionDAO.setLinkBatchID(adjustmentNo);
                    transactionTO.setChequeNo("SERVICE_TAX");
                    TxTransferTO.add(transferTo);
                }
            }
            transferDAO = new TransferDAO();
            tansactionMap.put("TxTransferTO", TxTransferTO);
            tansactionMap.put("MODE", map.get("COMMAND"));
            tansactionMap.put("COMMAND", map.get("COMMAND"));
            tansactionMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
            tansactionMap.put("LINK_BATCH_ID", adjustmentNo);
            System.out.println("################ tansactionMap :" + tansactionMap);
            transMap = transferDAO.execute(tansactionMap, false);
            getTransDetails(adjustmentNo);
        }
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
        getTransMap.clear();
        getTransMap = null;
        transList = null;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateInterestSubsidyAdjustmentTO", objInterestSubsidyAdjustmentTO);
            logTO.setData(objInterestSubsidyAdjustmentTO.toString());
            logTO.setPrimaryKey(objInterestSubsidyAdjustmentTO.getKeyData());
            logTO.setStatus(objInterestSubsidyAdjustmentTO.getStatus());
            logDAO.addToLog(logTO);
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
            sqlMap.executeUpdate("deleteIntSubsidyTO", objInterestSubsidyAdjustmentTO);
            logTO.setData(objInterestSubsidyAdjustmentTO.toString());
            logTO.setPrimaryKey(objInterestSubsidyAdjustmentTO.getKeyData());
            logTO.setStatus(objInterestSubsidyAdjustmentTO.getStatus());
            logDAO.addToLog(logTO);
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
        tempMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(objInterestSubsidyAdjustmentTO.getAdjustmentNo()));
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

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap : " + AuthMap);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            authorizeTransaction(AuthMap, map);
            sqlMap.executeUpdate("authorizeInterestSubsidy", AuthMap);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public void authorizeTransaction(HashMap AuthMap, HashMap map) throws Exception {
        try {
            if (AuthMap != null && AuthMap.get("ADJUSTMENT_NO") != null && !AuthMap.get("ADJUSTMENT_NO").equals("")) {
                String authorizeStatus = CommonUtil.convertObjToStr(AuthMap.get("STATUS"));
                String linkBatchId = CommonUtil.convertObjToStr(AuthMap.get("ADJUSTMENT_NO"));
                HashMap transAuthMap = new HashMap();
                transAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
                transAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                TransactionDAO.authorizeCashAndTransferWithoutRemitIssueDet(linkBatchId, authorizeStatus, transAuthMap);
                transAuthMap = null;
                // Update Principal Subsidy Amount
                if (authorizeStatus.equals("AUTHORIZED") && AuthMap.containsKey("PRINCIPAL_SUBSIDY")) {
                    System.out.println("########### Update principal Subsidy Amount : ");
                    List dueList = (List) sqlMap.executeQueryForList("getSelectInterestSubsidyDueDetails", AuthMap);
                    if (dueList != null && dueList.size() > 0) {
                        double transAmt = 0.0;
                        for (int i = 0; i < dueList.size(); i++) {
                            HashMap subsidyListMap = new HashMap();
                            HashMap updateMap = new HashMap();
                            subsidyListMap = (HashMap) dueList.get(i);
                            transAmt = 0.0;
                            transAmt = CommonUtil.convertObjToDouble(subsidyListMap.get("TODAYS_ADJUSTMENT_AMOUNT")).doubleValue()
                                    + CommonUtil.convertObjToDouble(subsidyListMap.get("RECOVERY_FROM_CUST_AMOUNT")).doubleValue()
                                    + CommonUtil.convertObjToDouble(subsidyListMap.get("WRITE_OFF_AMOUNT")).doubleValue()
                                    + CommonUtil.convertObjToDouble(subsidyListMap.get("OTS_AMOUNT")).doubleValue();
                            updateMap.put("ACCT_NUM", subsidyListMap.get("ACCOUNT_NO"));
                            updateMap.put("CURR_DT", currDt);
                            updateMap.put("TRANS_AMT", String.valueOf(transAmt));
                            sqlMap.executeUpdate("updateTermLoanFacilityDetailsTO", updateMap);
                        }
                        dueList = null;
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
        objInterestSubsidyAdjustmentTO = null;
    }
}