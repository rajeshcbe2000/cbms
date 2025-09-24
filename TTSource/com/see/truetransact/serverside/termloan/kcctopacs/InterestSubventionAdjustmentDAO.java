/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * InterestSubventionAdjustmentDAO.java
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
import com.see.truetransact.transferobject.termloan.kcctopacs.InterestSubventionAdjustmentTO;
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
 * This is used for InterestSubventionAdjustmentDAO Data Access.
 *
 * @author Suresh
 *
 */
public class InterestSubventionAdjustmentDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private InterestSubventionAdjustmentTO objInterestSubventionAdjustmentTO;
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
    private final static Logger log = Logger.getLogger(InterestSubventionAdjustmentDAO.class);
    private int yearTobeAdded = 1900;

    /**
     * Creates a new instance of OperativeAcctProductDAO
     */
    public InterestSubventionAdjustmentDAO() throws ServiceLocatorException {
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
        List list = (List) sqlMap.executeQueryForList("getSelectInterestSubventionAdjustmentTO", map);
        if (list != null && list.size() > 0) {
            returnMap.put("SubventionInterestData", list);
        }
        String where = (String) map.get("ADJUSTMENT_NO");
        List transList = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTO", where);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSACTION_LIST", transList);
            list = null;
        }
        if (map.containsKey("DISPLAY_TRANS_DETAILS")) {
            getTransDetails(CommonUtil.convertObjToStr(map.get("ADJUSTMENT_NO")));
        }
        System.out.println("########## returnMap : " + returnMap);
        return returnMap;
    }

    private String getAdjustmentNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SUBVENTION_NO");
        String adjustmentNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return adjustmentNo;
    }

    public HashMap execute(HashMap map) throws Exception {

        System.out.println("######## Release Map DAO : " + map);
        returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        objInterestSubventionAdjustmentTO = (InterestSubventionAdjustmentTO) map.get("SubventionInterestData");
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
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
            String adjustmentNo = "";
            adjustmentNo = getAdjustmentNo();
            objInterestSubventionAdjustmentTO.setAdjustmentNo(adjustmentNo);
            transactionPart(map);
            sqlMap.executeUpdate("insertInterestSubventionAdjustmentTO", objInterestSubventionAdjustmentTO);
            logTO.setData(objInterestSubventionAdjustmentTO.toString());
            logTO.setPrimaryKey(objInterestSubventionAdjustmentTO.getKeyData());
            logTO.setStatus(objInterestSubventionAdjustmentTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    public void transactionPart(HashMap map) throws Exception {
        String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
        TransactionTO transactionTO = new TransactionTO();
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
            if (transactionTO.getTransType().equals("TRANSFER") && transAmt > 0 && transactionTO.getProductType().equals("GL")) {
                TransferTrans transferTrans = new TransferTrans();
                transferTrans.setInitiatedBranch(BRANCH_ID);
                transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                HashMap tansactionMap = new HashMap();
                TxTransferTO transferTo = new TxTransferTO();
                ArrayList TxTransferTO = new ArrayList();
                HashMap dataMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                // Debit Insert Start
                txMap = new HashMap();
                System.out.println("Transfer Started debit : ");
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.DR_AC_HD, (String) transactionTO.getDebitAcctNo());
                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.PARTICULARS, transactionTO.getDebitAcctNo() + "-" + ":Interest Subvention");
                txMap.put(TransferTrans.DR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo = transactionDAO.addTransferDebitLocal(txMap, transAmt);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setBranchId(BRANCH_ID);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setAuthorizeRemarks("DP");
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(objInterestSubventionAdjustmentTO.getAdjustmentNo()));
                TxTransferTO.add(transferTo);
                // Debit Acc INSERT End

                // Credit Insert Start
                HashMap creditMap = new HashMap();                // Crdit Acc INSERT Start
                creditMap.put("CUST_ID", objInterestSubventionAdjustmentTO.getCustId());
                List lst = sqlMap.executeQueryForList("getSubventionIntCreditLoanAccHead", creditMap);    // Acc Head
                if (lst != null && lst.size() > 0) {
                    creditMap = (HashMap) lst.get(0);
                }
                txMap = new HashMap();
                transferTo = new TxTransferTO();
                txMap = new HashMap();
                transferTo.setInstrumentNo2("APPL_GL_TRANS");
                txMap.put(TransferTrans.CR_AC_HD, (String) creditMap.get("AC_CREDIT_INT"));
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                txMap.put(TransferTrans.CR_BRANCH, BRANCH_ID);
                txMap.put(TransferTrans.PARTICULARS, creditMap.get("AC_CREDIT_INT") + "-" + ":Interest Subvention");
                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                txMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo = transactionDAO.addTransferCreditLocal(txMap, transAmt);
                transferTo.setTransId("-");
                transferTo.setBatchId("-");
                transferTo.setTransDt(currDt);
                transferTo.setInitiatedBranch(BRANCH_ID);
                transferTo.setInitTransId(CommonUtil.convertObjToStr(map.get("USER_ID")));
                transferTo.setLinkBatchId(objInterestSubventionAdjustmentTO.getAdjustmentNo());
                transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(objInterestSubventionAdjustmentTO.getAdjustmentNo()));
                transactionTO.setChequeNo("SERVICE_TAX");
                TxTransferTO.add(transferTo);

                transferDAO = new TransferDAO();
                tansactionMap.put("TxTransferTO", TxTransferTO);
                tansactionMap.put("MODE", map.get("COMMAND"));
                tansactionMap.put("COMMAND", map.get("COMMAND"));
                tansactionMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
                tansactionMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(objInterestSubventionAdjustmentTO.getAdjustmentNo()));
                System.out.println("################ tansactionMap :" + tansactionMap);
                transMap = transferDAO.execute(tansactionMap, false);
                //Insert 
                transactionTO.setBatchId(objInterestSubventionAdjustmentTO.getAdjustmentNo());
                transactionTO.setBatchDt(currDt);
                transactionTO.setStatus(CommonConstants.STATUS_CREATED);
                transactionTO.setTransId(objInterestSubventionAdjustmentTO.getAdjustmentNo());
                System.out.println("transactionTO------------------->" + transactionTO);
                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
                getTransDetails(objInterestSubventionAdjustmentTO.getAdjustmentNo());
            }
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
            sqlMap.executeUpdate("updateInterestSubventionAdjustmentTO", objInterestSubventionAdjustmentTO);
            logTO.setData(objInterestSubventionAdjustmentTO.toString());
            logTO.setPrimaryKey(objInterestSubventionAdjustmentTO.getKeyData());
            logTO.setStatus(objInterestSubventionAdjustmentTO.getStatus());
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
            sqlMap.executeUpdate("deleteIntSubventionTO", objInterestSubventionAdjustmentTO);
            logTO.setData(objInterestSubventionAdjustmentTO.toString());
            logTO.setPrimaryKey(objInterestSubventionAdjustmentTO.getKeyData());
            logTO.setStatus(objInterestSubventionAdjustmentTO.getStatus());
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
        tempMap.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(objInterestSubventionAdjustmentTO.getAdjustmentNo()));
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
            sqlMap.executeUpdate("authorizeInterestSubvention", AuthMap);
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
        objInterestSubventionAdjustmentTO = null;
    }
}