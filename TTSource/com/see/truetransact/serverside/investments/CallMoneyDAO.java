/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductDAO.java
 *
 * Created on Wed Nov 24 16:51:38 GMT+05:30 2004
 */
package com.see.truetransact.serverside.investments;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.investments.CallMoneyTO;
import com.see.truetransact.transferobject.investments.InvestmentsAmortizationCalculationTO;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.batchprocess.task.share.DividendCalcTask;
import com.see.truetransact.commonutil.DateUtil;

import java.util.LinkedHashMap;
import java.util.Date;

/**
 * ShareProduct DAO.
 *
 */
public class CallMoneyDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private CallMoneyTO objTO;
    //    private CallMoneyTO objAmrTO;
    //    private InvestmentsAmortizationCalculationTO objAmrCalTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private HashMap loanDataMap = new HashMap();
    private HashMap deletedLoanDataMap;
    private String where = "";
    private Date currDt;
    private TransactionTO objTransactionTO;
    private TransactionDAO transactionDAO = null;
    
    /**
     * Creates a new instance of ShareProductDAO
     */
    public CallMoneyDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * This method execute a Query and returns the resultset in HashMap object
     */
    private HashMap getCallMoneyData() throws Exception {
        HashMap returnMap = new HashMap();
        HashMap param = new HashMap();
        param.put("BATCH_ID", where);
        param.put("INIT_BRANCH", _branchCode);
        param.put("TRANS_DATE", currDt.clone());
        List list = (List) sqlMap.executeQueryForList("getSelectCallMoneyTO", param);
        returnMap.put("CallMoneyTO", list);
        list = null;
        return returnMap;
    }

    private HashMap getCallMoneyExtensionData() throws Exception {
        HashMap returnMap = new HashMap();
        HashMap param = new HashMap();
        param.put("BATCH_ID", where);
        param.put("INIT_BRANCH", _branchCode);
        param.put("TRANS_DATE", currDt.clone());
        List list = (List) sqlMap.executeQueryForList("getSelectCallMoneyExtensionTO", param);
        returnMap.put("CallMoneyTO", list);
        list = null;
        return returnMap;
    }

    private HashMap getCallMoneyExtensionOtherThanInsertData() throws Exception {
        HashMap returnMap = new HashMap();
        HashMap param = new HashMap();
        param.put("BATCH_ID", where);
        param.put("INIT_BRANCH", _branchCode);
        param.put("TRANS_DATE", currDt.clone());
        List list = (List) sqlMap.executeQueryForList("getSelectCallMoneyExtensionotherThanInsertTO", param);
        returnMap.put("CallMoneyTO", list);
        list = null;
        return returnMap;
    }

    private HashMap getInvestmentShiftingData(HashMap obj) throws Exception {
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectInvestmentShiftingTO", obj);

        returnMap.put("InvestmentsAmortizationTO", list);
        list = null;
        return returnMap;
    }

    private HashMap getInvestmentTransData() throws Exception {
        HashMap returnMap = new HashMap();
        HashMap param = new HashMap();
        param.put("INITIATED_BRANCH", _branchCode);
        param.put("BATCH_ID", where);
        param.put("TRANS_DT", currDt.clone());
        List list = (List) sqlMap.executeQueryForList("getSelectInvestmentTransTO", param);
        returnMap.put("InvestmentsTransTO", list);
        list = null;
        return returnMap;
    }

    /**
     * This method is used to insertnew datat into the Table
     */
    private void insertData(HashMap map) throws Exception {
        if (!map.containsKey("CALLMONEYEXTENSION")) {
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            logDAO.addToLog(logTO);
            doTransaction(map);
            objTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            objTO.setInitBran(_branchCode);
            sqlMap.executeUpdate("insertCallMoneyTO", objTO);
        } else {
            objTO.setInitBran(_branchCode);
            sqlMap.executeUpdate("insertCallMoneyExtensionTO", objTO);
        }
    }

    private void doUpdateTransaction(HashMap map) throws Exception {
        System.out.println("In Side The doUpdateTransaction");
        map.put("BATCHID", objTO.getBatchID());
        map.put("BATCH_ID", objTO.getBatchID());
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH", _branchCode);
        List lst = (List) sqlMap.executeQueryForList("getBatchTxTransferTOsAuthorize", map);
        if (!lst.isEmpty()) {
            TxTransferTO txTransferTO = null;
            double oldAmount = 0, total = 0;
            HashMap oldAmountMap = new HashMap();
            ArrayList transferList = new ArrayList();
            if (lst != null && lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    total = 0;
                    txTransferTO = (TxTransferTO) lst.get(j);
                    System.out.println("txTransferTO.getAuthorizeRemarks()---------->" + txTransferTO.getAuthorizeRemarks());
                    if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("IINVESTMENT_AC_HD")) {
                        total = CommonUtil.convertObjToDouble(objTO.getCallMoneyAmount()).doubleValue();
                        System.out.println("Inssid Investment" + total);
                    }
                    if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("INTEREST_PAID_AC_HD")) {
                        total = CommonUtil.convertObjToDouble(objTO.getInterestAmt()).doubleValue();
                        System.out.println("Inssid Interest" + total);
                    }
                    if (CommonUtil.convertObjToStr(txTransferTO.getAuthorizeRemarks()).equals("TRANSACTIONAMOUNT")) {
                        System.out.println("transferList total ------------->" + transferList);
                        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                double tranAmt = 0.0;
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                total = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                System.out.println("Inssid Transaction" + total);
                            }
                        }
                    }
                    oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));


                    oldAmount = txTransferTO.getAmount().doubleValue();
                    txTransferTO.setInpAmount(new Double(total));
                    txTransferTO.setAmount(new Double(total));
                    if (objTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                    } else {
                        txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    }
                    txTransferTO.setStatusDt(currDt);
                    txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                    oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                    transferList.add(txTransferTO);
                }
                TransferTrans transferTrans = new TransferTrans();
                transferTrans.setOldAmount(oldAmountMap);
                transferTrans.setInitiatedBranch(_branchCode);
                transferTrans.doDebitCredit(transferList, _branchCode, false, objTO.getCommand());
                transferTrans = null;

            }

        } else {
            throw new TTException("Transaction Not Found");
        }
    }

    private void doDebitCredit(ArrayList batchList, String branchCode, boolean isAutoAuthorize) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", objTO.getCommand());
        data.put("INITIATED_BRANCH", branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, logTO.getUserId());
        data.put(CommonConstants.MODULE, "Transaction");
        data.put(CommonConstants.SCREEN, "");
        data.put("MODE", "MODE");
        if (isAutoAuthorize) {
            HashMap authorizeMap = new HashMap();
            authorizeMap.put("BATCH_ID", null);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
            data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
        }
        loanDataMap = transferDAO.execute(data, false);
    }

    /**
     * This method is used to Edit the already existing data in the table
     */
    private void updateData(HashMap map) throws Exception {
        if (!map.containsKey("CALLMONEYEXTENSION")) {
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            logDAO.addToLog(logTO);
            doUpdateTransaction(map);
            objTransactionTO.setBranchId(_branchCode);
            sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
            objTO.setInitBran(_branchCode);
            objTO.setTransDT(currDt);
            sqlMap.executeUpdate("updateCallMoneyTO", objTO);
        } else {
            objTO.setInitBran(_branchCode);
            objTO.setTransDT(currDt);
            sqlMap.executeUpdate("updateCallMoneyExtensionTO", objTO);
        }
    }
    /*
     * This method is used to update the already existing data by making its
     * status to be deleted
     */

    private void deleteData(HashMap map) throws Exception {
        if (!map.containsKey("CALLMONEYEXTENSION")) {
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            logDAO.addToLog(logTO);
            doUpdateTransaction(map);
            objTransactionTO.setStatus(CommonConstants.STATUS_DELETED);
            objTransactionTO.setBranchId(_branchCode);
            sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
            objTO.setInitBran(_branchCode);
            objTO.setTransDT(currDt);
            sqlMap.executeUpdate("deleteCallMoneyTO", objTO);
        } else {
            objTO.setInitBran(_branchCode);
            objTO.setTransDT(currDt);
            sqlMap.executeUpdate("deleteCallMoneyExtensionTO", objTO);
        }

    }

    public static void main(String str[]) {
        try {
            InvestmentsTransDAO dao = new InvestmentsTransDAO();
        } catch (Exception ex) {
        }
    }

    /**
     * This method is called to do desired operations in the Table
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        try {
            sqlMap.startTransaction();
            System.out.println("Map in Dao ------------>" + map);
            if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                objTO = (CallMoneyTO) map.get("CallMoneyTO");
                final String command = objTO.getCommand();
                logDAO = new LogDAO();
                logTO = new LogTO();
                logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
                logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
                logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(map);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(map);
                }
            } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                System.out.println("map:" + map);
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
                System.out.println("authMap:" + authMap);

                if (authMap != null) {
                    if (!authMap.containsKey("CALLMONEYEXTENSION")) {
                        authorize(authMap, map);
                    } else {
                        String status = (String) authMap.get(CommonConstants.AUTHORIZESTATUS);
                        String user_id = (String) authMap.get(CommonConstants.USER_ID);
                        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
                        String batchid = null;
                        HashMap dataMap = new HashMap();
                        for (int i = 0; i < selectedList.size(); i++) {
                            dataMap = (HashMap) selectedList.get(i);
                            batchid = CommonUtil.convertObjToStr(dataMap.get("BATCH_ID"));
                        }

                        dataMap.put("STATUS", status);
                        dataMap.put("BATCH_ID", batchid);
                        dataMap.put("USER_ID", user_id);
                        System.out.println("dataMap------------------>" + dataMap);
                        dataMap.put("INIT_BRANCH", _branchCode);
                        dataMap.put("TRANS_DATE", currDt.clone());
                        sqlMap.executeUpdate("updateNoOfExtensionDays", dataMap);
                        sqlMap.executeUpdate("authorizeCallMoneyExtension", dataMap);
                        dataMap = null;
                    }
                }

            } else {
                throw new NoCommandException();
            }
            sqlMap.commitTransaction();
            destroyObjects();
            return null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    private void authorize(HashMap authMap, HashMap map) throws Exception {
        System.out.println("Do Authorization");
        System.out.println("Map in Dao ------------>" + authMap);
        String batchid = "";
        String status = (String) authMap.get(CommonConstants.AUTHORIZESTATUS);
        String user_id = (String) authMap.get(CommonConstants.USER_ID);
        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap = new HashMap();
        for (int i = 0; i < selectedList.size(); i++) {
            dataMap = (HashMap) selectedList.get(i);
            batchid = CommonUtil.convertObjToStr(dataMap.get("BATCH_ID"));
        }
        dotransferInvestment(batchid, status, authMap);
        dataMap.put("STATUS", status);
        dataMap.put("INIT_BRANCH", _branchCode);
        dataMap.put("TRANS_DATE", currDt.clone());
        sqlMap.executeUpdate("authorizeCallMoney", dataMap);
        System.out.println("dataMap in Dao ------------>" + dataMap);
        if (dataMap.containsKey("RECONCILE_BATCHID")) {

            sqlMap.executeUpdate("upDateReconcileStatus", dataMap);
        }
        dataMap = null;
    }

    private void dotransferInvestment(String batchid, String status, HashMap authMap) throws Exception {
        TransactionTO transactionTO;
        HashMap cashAuthMap, tempMap, transIdMap, transferTransParam;
        ArrayList cashTransList, transferTransList, transactionList;
        transactionList = new ArrayList();
        transferTransParam = new HashMap();
        transferTransParam.put(CommonConstants.BRANCH_ID, authMap.get(CommonConstants.BRANCH_ID));
        transferTransParam.put(CommonConstants.USER_ID, authMap.get(CommonConstants.USER_ID));
        transferTransParam.put("BATCHID", batchid);
        transferTransParam.put("BATCH_ID", batchid);
        transferTransParam.put(CommonConstants.AUTHORIZESTATUS, status);
        transferTransParam.put("TRANS_DT", currDt.clone());
        transferTransParam.put("INITIATED_BRANCH", _branchCode);
        transferTransList = (ArrayList) sqlMap.executeQueryForList("getBatchTxTransferTOsAuthorize", transferTransParam);
        if (transferTransList != null && (!transferTransList.isEmpty())) {
            TransferTrans objTrans = new TransferTrans();
            objTrans.doTransferAuthorize(transferTransList, transferTransParam);
            //            transferTransParam.put(CommonConstants.STATUS, status);
            //            transferTransParam.put("BATCH_ID",batchid);
            //            transferTransParam.put(CommonConstants.AUTHORIZEDT, currDt);
            //            sqlMap.executeUpdate("authorizeInvestmentTrans", transferTransParam);
        } else {
            throw new TTException("Transfer List Is Empty");
        }
        transferTransList = null;
        transferTransParam = null;
    }

    private HashMap doTransaction(HashMap map) throws Exception {
        System.out.println("In Side The DoTransaction");
        HashMap achdMap = new HashMap();

        achdMap.put("INVESTMENT_TYPE", "CALL_MONEY");
        System.out.println("achdMap before query ------------>" + achdMap);
        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
        if (achdLst != null && achdLst.size() > 0) {
            achdMap = new HashMap();
            ArrayList transferList = new ArrayList();
            achdMap = (HashMap) achdLst.get(0);
            System.out.println("achdMap--------------->" + achdMap);
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            System.out.println("objTO.getCallMoneyAmount().doubleValue()--------->" + objTO.getCallMoneyAmount().doubleValue());
            System.out.println("objTO--------->" + objTO);
            HashMap txMap = new HashMap();
            if (!objTO.getTransType().equals("") && map.containsKey("TransactionTO")) {
                LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                if (TransactionDetailsMap.size() > 0) {
                    if (objTO.getCallMoneyType().equals("Lending")) {
                        if (objTO.getCallMoneyAmount().doubleValue() != 0.0) {
                            txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_AC_HD");
                            txMap.put(TransferTrans.PARTICULARS, objTO.getParticulars());
                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getCallMoneyAmount().doubleValue()));
                        }
                        if (objTO.getInterestAmt().doubleValue() != 0.0) {
                            txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("INTEREST_PAID_AC_HD"));
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("AUTHORIZEREMARKS", "INTEREST_PAID_AC_HD");
                            txMap.put(TransferTrans.PARTICULARS, objTO.getParticulars());
                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getInterestAmt().doubleValue()));
                        }

                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                double tranAmt = 0.0;
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                txMap.put(TransferTrans.PARTICULARS, objTO.getParticulars());
                                txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                }
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                tranAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, tranAmt));
                            }
                        }

                    }
                    if (objTO.getCallMoneyType().equals("Borrowing")) {
                        if (objTO.getCallMoneyAmount().doubleValue() != 0.0) {
                            txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_AC_HD");
                            txMap.put(TransferTrans.PARTICULARS, objTO.getParticulars());
                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getCallMoneyAmount().doubleValue()));

                        }
                        if (objTO.getInterestAmt().doubleValue() != 0.0) {
                            txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("INTEREST_RECIVED_AC_HD"));
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put("AUTHORIZEREMARKS", "INTEREST_RECIVED_AC_HD");
                            txMap.put(TransferTrans.PARTICULARS, objTO.getParticulars());
                            txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getInterestAmt().doubleValue()));

                        }

                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                double tranAmt = 0.0;
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                txMap.put(TransferTrans.PARTICULARS, objTO.getParticulars());
                                txMap.put("AUTHORIZEREMARKS", "TRANSACTIONAMOUNT");
                                if (CommonUtil.convertObjToStr(objTransactionTO.getProductType()).equals("GL")) {
                                    txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                }
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                tranAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, tranAmt));
                            }
                        }
                    }
                    System.out.println("transferList total ------------->" + transferList);
                    doDebitCredit(transferList, _branchCode, false);
                    String val = (String) objTO.getParticulars();
                    if (val.length() > 15) {
                        val = (String) objTO.getParticulars().substring(0, 15);
                    }
                    objTransactionTO.setTransId(val);
                    objTransactionTO.setStatus(objTO.getStatus());
//                    Date curDt = ServerUtil.getCurrentDate(_branchCode);
                    Date prBatchDt = (Date) currDt.clone();
                    Date batchDt = objTO.getTransDT();
                    if (batchDt.getDate() > 0) {
                        prBatchDt.setDate(batchDt.getDate());
                        prBatchDt.setMonth(batchDt.getMonth());
                        prBatchDt.setYear(batchDt.getYear());
                    }
                    objTransactionTO.setBatchDt(prBatchDt);
                    objTransactionTO.setBatchId(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                    objTransactionTO.setBranchId(_branchCode);
                    System.out.println("objTransactionTO----------------->" + objTransactionTO);
                    sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);

                    transferList = null;
                } else {
                    throw new Exception("Transaction Details Not their");
                }

            } else {
                throw new Exception("investment  Transaction is not proper");
            }


        } else {
            throw new Exception("investment  Config Date is Not set...");
        }
        return null;
    }
    /*
     * This method is used to execute a query to get all the inserted datas in
     * the table and retrun the resultset as a HashMap *
     */

    public HashMap executeQuery(HashMap obj) throws Exception {
        System.out.println("obj------------------->" + obj);
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        where = (String) obj.get(CommonConstants.MAP_WHERE);
        HashMap data = new HashMap();
        where = (String) obj.get("BATCH_ID");
        if (obj.containsKey("CALLMONEYEXTENSION")) {
            HashMap callMoneyMasterMap = getCallMoneyExtensionData();

            data.put("CallMoneyTO", callMoneyMasterMap);
        } else if (obj.containsKey("OTHERTHANINSERT")) {
            HashMap callMoneyMasterMap = getCallMoneyExtensionOtherThanInsertData();

            data.put("CallMoneyTO", callMoneyMasterMap);
        } else if (obj.containsKey("TRANSACTION")) {
            Date fromDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj.get("FROM_DATE")));
            Date toDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj.get("TO_DATE")));
//            Date curDt = ServerUtil.getCurrentDate(_branchCode);
            Date prFromDt = (Date) currDt.clone();
            if (fromDate.getDate() > 0) {
                prFromDt.setDate(fromDate.getDate());
                prFromDt.setMonth(fromDate.getMonth());
                prFromDt.setYear(fromDate.getYear());
            }
            Date prToDt = (Date) currDt.clone();
            if (toDate.getDate() > 0) {
                prToDt.setDate(toDate.getDate());
                prToDt.setMonth(toDate.getMonth());
                prToDt.setYear(toDate.getYear());
            }
            data.put("FROM_DATE", prFromDt);
            data.put("TO_DATE", prToDt);
            System.out.println("data-------------------->" + data);

            List transList = (List) sqlMap.executeQueryForList("getSelectCallMoneyDetailsTO", obj);
            data.put("getSelectCallMoneyDetailsTO", transList);
        } else {
            HashMap callMoneyMasterMap = getCallMoneyData();
            data.put("CallMoneyTO", callMoneyMasterMap);
            obj.put(CommonConstants.MAP_WHERE, where);
            HashMap getRemitTransMap = new HashMap();
            getRemitTransMap.put("TRANS_ID", obj.get("BATCH_ID"));
            getRemitTransMap.put("TRANS_DT", currDt.clone());
            getRemitTransMap.put("BRANCH_CODE", _branchCode);
            System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
            List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
            data.put("TransactionTO", list);
            list = null;
        }

        return data;

    }

    /*
     * This is used to free up the memory used by SharePrductTO object
     */
    private void destroyObjects() {
        objTO = null;
        loanDataMap = null;
        deletedLoanDataMap = null;
        //        objAmrCalTO=null;
    }
}
