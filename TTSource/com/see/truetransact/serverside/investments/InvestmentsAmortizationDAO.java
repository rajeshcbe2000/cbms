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

import com.see.truetransact.transferobject.investments.InvestmentsAmortizationTO;
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
public class InvestmentsAmortizationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private InvestmentsTransTO objTO;
    private InvestmentsAmortizationTO objAmrTO;
    private InvestmentsAmortizationCalculationTO objAmrCalTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private HashMap loanDataMap = new HashMap();
    private HashMap deletedLoanDataMap;
    private String where = "";
    private TransactionTO objTransactionTO;
    private TransactionDAO transactionDAO = null;
    private Date currDt = null;
    /**
     * Creates a new instance of ShareProductDAO
     */
    public InvestmentsAmortizationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * This method execute a Query and returns the resultset in HashMap object
     */
    private HashMap getInvestmentMasterData() throws Exception {
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectInvestmentMasterTO", where);
        returnMap.put("InvestmentsMasterTO", list);
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
    //    private HashMap getShareProductLoanData()throws Exception{
    //        HashMap dataMap = new HashMap();
    //        List list = (List) sqlMap.executeQueryForList("getSelectShareProductLoan", where);
    //        if(list.size() != 0){
    //            for(int i=0;i<list.size();i++){
    //                dataMap.put(((ShareProductLoanTO)list.get(i)).getLoanType(),(ShareProductLoanTO)list.get(i));
    //            }
    //        }
    //        return dataMap;
    //    }

    /**
     * This method is used to insertnew datat into the Table
     */
    private void insertData(HashMap map) throws Exception {
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        logDAO.addToLog(logTO);
        System.out.println("objTO.getPremiumAmount().doubleValue()---------->" + objTO.getPremiumAmount().doubleValue());
        if (map.containsKey("SHIFTING")) {
            if (objTO.getPremiumAmount().doubleValue() > 0.0) {
                doTransaction(map);
                objTO.setTransType(CommonConstants.CREDIT);
                objTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
                objTO.setInitiatedBranch(_branchCode);
                sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                objAmrTO.setBatchID(objTO.getBatchID());
            }
            objAmrTO.setTransDT(currDt);
            objAmrTO.setInitiatedBranch(_branchCode);
            sqlMap.executeUpdate("insertInvestmentShiftingTO", objAmrTO);
        } else if (map.containsKey("AMORTIZATION")) {

            ArrayList AmrtList = new ArrayList();
            AmrtList = (ArrayList) map.get("InvestmentsAmortizationCalculationTO");
            if (AmrtList != null && AmrtList.size() > 0) {
                doTransaction(map);
                doAmortization(AmrtList);
            } else {
                throw new Exception("Transaction is not proper");
            }

        } else {
            throw new Exception("Transaction is not proper");
        }
        //        addLoanData();
    }

    private void doAmortization(ArrayList AmrtList) throws Exception {
        objTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
        objTO.setTransType("CREDIT");
        InvestmentsAmortizationCalculationTO objInvestmentsAmortizationCalculationTO = new InvestmentsAmortizationCalculationTO();
        for (int i = 0; i < AmrtList.size(); i++) {
            objInvestmentsAmortizationCalculationTO = new InvestmentsAmortizationCalculationTO();
            objInvestmentsAmortizationCalculationTO = (InvestmentsAmortizationCalculationTO) AmrtList.get(i);
            objInvestmentsAmortizationCalculationTO.setBatchID(objTO.getBatchID());
            sqlMap.executeUpdate("insertInvestmentAmrotizationTO", objInvestmentsAmortizationCalculationTO);
            objTO.setInvestmentBehaves(objInvestmentsAmortizationCalculationTO.getInvestmentBehaves());
            objTO.setInvestmentID(objInvestmentsAmortizationCalculationTO.getInvestmentID());
            objTO.setInvestmentName(objInvestmentsAmortizationCalculationTO.getInvestmentName());
            objTO.setPremiumAmount(objInvestmentsAmortizationCalculationTO.getAmortizationAmount());
            objTO.setInitiatedBranch(_branchCode);
            sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
            System.out.println("objAmrTO----------------------->" + objAmrTO);
            HashMap dataMap = new HashMap();
            dataMap.put("NO_OF_UNITS", "0");
            dataMap.put("AMORTIZATION_AMOUNT", objTO.getPremiumAmount());
            dataMap.put("INTEREST_PAID", "0");
            dataMap.put("INVESTMENT_TYPE", objTO.getInvestmentBehaves());
            dataMap.put("INVESTMENT_PROD_ID", objTO.getInvestmentID());
            dataMap.put("INVESTMENT_NAME", objTO.getInvestmentName());
            dataMap.put("INVESTMENT_OUTSTANDING_AMOUNT", "0");
            dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_AUTHORIZED);
            sqlMap.executeUpdate("updateInvestmentAmortzation", dataMap);
            dataMap.put("BATCH_ID", objTO.getBatchID());
            dataMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            dataMap.put("TRANS_DT", currDt.clone());
            dataMap.put("INITIATED_BRANCH", _branchCode);
            sqlMap.executeUpdate("authorizeInvestmentTrans", dataMap);
            dataMap = null;
        }
    }

    private HashMap doTransaction(HashMap map) throws Exception {
        System.out.println("In Side The DoTransaction");
        HashMap achdMap = new HashMap();

        achdMap.put("INVESTMENT_TYPE", CommonUtil.convertObjToStr(objTO.getInvestmentBehaves()));
        achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTO.getInvestmentID()));
        System.out.println("achdMap before query ------------>" + achdMap);
        List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
        if (achdLst != null && achdLst.size() > 0) {
            achdMap = new HashMap();
            ArrayList transferList = new ArrayList();
            achdMap = (HashMap) achdLst.get(0);
            System.out.println("achdMap--------------->" + achdMap);
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            objTransferTrans.setLinkBatchId(objTO.getInvestmentName());
            HashMap txMap = new HashMap();
            if (!objTO.getTrnCode().equals("")) {
                if (objTO.getTrnCode().equals("Shifting") || objTO.getTrnCode().equals("Amortization")) {
                    if (objTO.getPremiumAmount().doubleValue() != 0.0) {
                        txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("PREMIUM_DEPR_AC_HD"));
                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("AUTHORIZEREMARKS", "PREMIUM_PAID_AC_HD");
                        txMap.put(TransferTrans.PARTICULARS, objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());
                        if (map.containsKey("AMORTIZATION")) {
                            txMap.put(TransferTrans.PARTICULARS, "Amortization" + "_" + objTO.getInvestmentName());
                        }
                        txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());
                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, objTO.getPremiumAmount().doubleValue()));
                        txMap.put(TransferTrans.CR_AC_HD, (String) achdMap.get("PREMIUM_RECEIVED_AC_HD"));
                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put("AUTHORIZEREMARKS", "PREMIUM_RECEIVED_AC_HD");
                        txMap.put(TransferTrans.PARTICULARS, objTO.getInvestmentBehaves() + "_" + objTO.getInvestmentName());
                        if (map.containsKey("AMORTIZATION")) {
                            txMap.put(TransferTrans.PARTICULARS, "Amortization" + "_" + objTO.getInvestmentName());
                        }
                        txMap.put(CommonConstants.USER_ID, objTO.getStatusBy());

                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, objTO.getPremiumAmount().doubleValue()));

                        System.out.println("transferList total ------------->" + transferList);
                    }

                }

                if (map.containsKey("AMORTIZATION")) {
                    doDebitCredit(transferList, _branchCode, true);
                } else {
                    doDebitCredit(transferList, _branchCode, false);
                }

                transferList = null;
            } else {
                throw new Exception("investment  Transaction is not proper");
            }


        } else {
            throw new Exception("investment  Config Date is Not set...");
        }
        return null;
    }

    private void doUpdateTransaction(HashMap map) throws Exception {
        System.out.println("In Side The doUpdateTransaction");
        map.put("BATCHID", objTO.getBatchID());
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
                    total = CommonUtil.convertObjToDouble(objTO.getPremiumAmount()).doubleValue();
                    System.out.println("total-------------------->" + total);
                    oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                    oldAmount = txTransferTO.getAmount().doubleValue();
                    txTransferTO.setInpAmount(new Double(total));
                    txTransferTO.setAmount(new Double(total));
                    if (objTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                        txTransferTO.setAmount(new Double(oldAmount));
                        System.out.println("In Side Delete ");
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

        System.out.println("loanDataMap---------------->" + loanDataMap);
    }

    /**
     * This method is used to Edit the already existing data in the table
     */
    private void updateData(HashMap map) throws Exception {
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        logDAO.addToLog(logTO);
        //        objTransactionTO.setStatus(objTO.getCommand());
        if (CommonUtil.convertObjToDouble(objTO.getPremiumAmount()).doubleValue() == 0.0) {
            objTO.setCommand(CommonConstants.TOSTATUS_DELETE);
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            objAmrTO.setBatchID("");
            doUpdateTransaction(map);
        } else if (CommonUtil.convertObjToDouble(objTO.getPremiumAmount()).doubleValue() > 0.0 && CommonUtil.convertObjToStr(objTO.getBatchID()).equals("")) {
            objTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            doTransaction(map);
            objTO.setBatchID(CommonUtil.convertObjToStr(loanDataMap.get("TRANS_ID")));
            objTO.setInitiatedBranch(_branchCode);
            sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
            objAmrTO.setBatchID(objTO.getBatchID());
        } else {
            doUpdateTransaction(map);
        }
        //sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
        objTO.setTransDT(currDt);
        objTO.setInitiatedBranch(_branchCode);
        sqlMap.executeUpdate("updateInvestmentTransTO", objTO);
        sqlMap.executeUpdate("updateInvestmentShiftingTO", objAmrTO);

        //        addLoanData();
    }
    /*
     * This method is used to update the already existing data by making its
     * status to be deleted
     */

    private void deleteData(HashMap map) throws Exception {
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        logDAO.addToLog(logTO);
        if (CommonUtil.convertObjToDouble(objTO.getPremiumAmount()).doubleValue() > 0.0) {
            doUpdateTransaction(map);
            objTO.setTransDT(currDt);
            objTO.setInitiatedBranch(_branchCode);
            sqlMap.executeUpdate("deleteInvestmentTransTO", objTO);
        }
        //        objTransactionTO.setStatus(objTO.getCommand());
        //        sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);
        sqlMap.executeUpdate("deleteInvestmentShiftingTO", objAmrTO);

        //        addLoanData();
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

                objTO = (InvestmentsTransTO) map.get("InvestmentsTransTO");

                if (map.containsKey("SHIFTING")) {
                    objAmrTO = (InvestmentsAmortizationTO) map.get("InvestmentsAmortizationTO");
                }

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
                    objTO = (InvestmentsTransTO) authMap.get("InvestmentsTransTO");
                    objAmrTO = (InvestmentsAmortizationTO) authMap.get("InvestmentsAmortizationTO");
                    objTO.setTrnCode("Shifting");
                    authorize(authMap, map);
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
        String batchid;
        String status = (String) authMap.get(CommonConstants.AUTHORIZESTATUS);
        String user_id = (String) authMap.get(CommonConstants.USER_ID);
        ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap = new HashMap();
        for (int i = 0; i < selectedList.size(); i++) {
            dataMap = (HashMap) selectedList.get(i);
            batchid = CommonUtil.convertObjToStr(dataMap.get("BATCH_ID"));
            if (!batchid.equals("") && batchid.length() > 0 && CommonUtil.convertObjToDouble(objTO.getPremiumAmount()).doubleValue() > 0) {
                dotransferInvestment(batchid, status, authMap);
            }
        }

        System.out.println("objTO----------------------->" + objTO);
        System.out.println("objAmrTO----------------------->" + objAmrTO);
        dataMap.put("NO_OF_UNITS", "0");
        dataMap.put("PREMIUM_PAID", objTO.getPremiumAmount());
        dataMap.put("INTEREST_PAID", "0");
        dataMap.put("INVESTMENT_TYPE", objTO.getInvestmentBehaves());
        dataMap.put("INVESTMENT_PROD_ID", objTO.getInvestmentID());
        dataMap.put("INVESTMENT_NAME", objTO.getInvestmentName());
        dataMap.put("SHFTING_DATE", objAmrTO.getShiftingDate());
        dataMap.put("INVESTMENT_OUTSTANDING_AMOUNT", "0");
        dataMap.put("CLASSIFICATION", objAmrTO.getNewClassfication());
        dataMap.put(CommonConstants.STATUS, status);

        System.out.println("dataMap----------------------->" + dataMap);
        if (!status.equals(CommonConstants.STATUS_REJECTED)) {
            sqlMap.executeUpdate("updateInvestmentMasteSaleValues", dataMap);
            sqlMap.executeUpdate("shiftingInvestmentClassification", dataMap);


        }

        sqlMap.executeUpdate("authorizeInvestmentShifting", dataMap);
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
        transferTransParam.put("TRANS_DT", currDt);
        transferTransParam.put("INITIATED_BRANCH", _branchCode);
        transferTransList = (ArrayList) sqlMap.executeQueryForList("getBatchTxTransferTOsAuthorize", transferTransParam);
        if (transferTransList != null && (!transferTransList.isEmpty())) {
            TransferTrans objTrans = new TransferTrans();
            objTrans.doTransferAuthorize(transferTransList, transferTransParam);
            transferTransParam.put(CommonConstants.STATUS, status);
            transferTransParam.put("BATCH_ID", batchid);
            transferTransParam.put(CommonConstants.AUTHORIZEDT, currDt);
            transferTransParam.put("TRANS_DT", currDt.clone());
            transferTransParam.put("INITIATED_BRANCH", _branchCode);
            sqlMap.executeUpdate("authorizeInvestmentTrans", transferTransParam);
        } else {
            throw new TTException("Transfer List Is Empty");
        }
        transferTransList = null;
        transferTransParam = null;
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
        if (!obj.containsKey("AMORTIZATIONCALC")) {
            HashMap investmentMasterMap = getInvestmentMasterData();
            data.put("InvestmentsMasterTO", investmentMasterMap);
            if (!obj.containsKey("MASTER")) {
                where = (String) obj.get("BATCH_ID");
                HashMap investmenttransMap = getInvestmentTransData();
                data.put("InvestmentsTransTO", investmenttransMap);
                obj.put(CommonConstants.MAP_WHERE, where);
                HashMap getRemitTransMap = new HashMap();
                getRemitTransMap.put("TRANS_ID", obj.get(CommonConstants.MAP_WHERE));
                getRemitTransMap.put("TRANS_DT", currDt.clone());
                getRemitTransMap.put("BRANCH_CODE", _branchCode);
                System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
                List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
                data.put("TransactionTO", list);
                list = null;
                obj.put("BATCHID", obj.get("BATCH_ID"));
                HashMap investmentShiftMap = getInvestmentShiftingData(obj);
                data.put("InvestmentsAmortizationTO", investmentShiftMap);
                list = null;
            }
        } else {
            data.put("InvestmentsAmortizationCalculationTO", callAmortizationCalc(obj));

        }

        return data;
    }

    private ArrayList callAmortizationCalc(HashMap obj) throws Exception {
        try {
            List lst = ServerUtil.executeQuery("getLastFinancialYearend", null);
            ArrayList investmentAmrotizationList = new ArrayList();
            if (lst != null && lst.size() > 0) {
                HashMap lstFinDateMap = new HashMap();
                lstFinDateMap = (HashMap) lst.get(0);
                Date lstFinDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lstFinDateMap.get("LAST_FINANCIAL_YEAR_END")));
                lstFinDate = DateUtil.addDays(lstFinDate, 365);
                obj.put("UPTO_DATE", lstFinDate);
                lst = null;
                lstFinDateMap = null;
                List investmentList = ServerUtil.executeQuery("getSelectInvestmentForAmortization", obj);
                if (investmentList != null && investmentList.size() > 0) {
                    HashMap invMap = new HashMap();
                    for (int i = 0; i < investmentList.size(); i++) {
                        invMap = new HashMap();
                        invMap = (HashMap) investmentList.get(i);
                        String investmentName = CommonUtil.convertObjToStr(invMap.get("INVESTMENT_NAME"));
                        String investmentProdId = CommonUtil.convertObjToStr(invMap.get("INVESTMENT_PROD_ID"));
                        String investmentType = CommonUtil.convertObjToStr(invMap.get("INVESTMENT_TYPE"));
                        Date issueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(invMap.get("INVESTMENT_ISSUE_DT")));
                        Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(invMap.get("INVESTMENT_MATURITY_DT")));
                        double premiumCollected = CommonUtil.convertObjToDouble(invMap.get("TOTAL_PREMIUM_COLLECTED")).doubleValue();
                        double premiumPaid = CommonUtil.convertObjToDouble(invMap.get("TOTAL_PREMIUM_PAID")).doubleValue();
                        double pendingPremium = CommonUtil.convertObjToDouble(invMap.get("PENDING_PREMIUM")).doubleValue();
                        double faceValue = CommonUtil.convertObjToDouble(invMap.get("INVESTMENT_FACE_VALUE,")).doubleValue();
                        double avlNoOfUnits = CommonUtil.convertObjToDouble(invMap.get("AVILABLE_NO_OF_UNITS,")).doubleValue();
                        double outStandingBalance = CommonUtil.convertObjToDouble(invMap.get("INVESTMENT_OUTSTANDING_AMOUNT,")).doubleValue();
                        System.out.println("matDate----------------->" + matDate);
                        Date applnDt = (Date) lstFinDate.clone();
                        if (matDate.getDate() > 0) {
                            applnDt.setYear(matDate.getYear());
                        }
                        matDate = applnDt;
                        System.out.println("invMap----------------------->" + invMap);
                        System.out.println("matDate----------------->" + matDate);
                        System.out.println("lstFinDate----------------->" + lstFinDate);

                        int a = 0;
                        for (Date stDt = lstFinDate; DateUtil.dateDiff(stDt, matDate) > 0; stDt = DateUtil.addDays(stDt, 365)) {
                            a = a + 1;
                            System.out.println("a------------------->" + a);
                        }
                        double amortizationAmount = 0.0;
                        amortizationAmount = pendingPremium / a;
                        System.out.println("amortizationAmount--------------->" + amortizationAmount);
                        DividendCalcTask divCal = new DividendCalcTask();

                        amortizationAmount = (double) divCal.getNearest((long) (amortizationAmount * 100), 100) / 100;
                        double pendingAmrAmt = premiumPaid - premiumCollected;

                        System.out.println("pendingAmrAmt----------------->" + pendingAmrAmt);
                        if (pendingAmrAmt < amortizationAmount) {
                            amortizationAmount = pendingAmrAmt;
                        }
                        System.out.println("amortizationAmount after rounding--------------->" + amortizationAmount);
                        InvestmentsAmortizationCalculationTO objInvestmentsAmortizationCalculationTO = new InvestmentsAmortizationCalculationTO();
                        objInvestmentsAmortizationCalculationTO.setInvestmentBehaves(investmentType);
                        objInvestmentsAmortizationCalculationTO.setInvestmentID(investmentProdId);
                        objInvestmentsAmortizationCalculationTO.setInvestmentName(investmentName);
                        objInvestmentsAmortizationCalculationTO.setPremium(new Double(pendingPremium));
                        //                        objInvestmentsAmortizationCalculationTO.setTransDate(currDt);
                        objInvestmentsAmortizationCalculationTO.setUptoDate(lstFinDate);
                        objInvestmentsAmortizationCalculationTO.setAmortizationAmount(new Double(amortizationAmount));
                        investmentAmrotizationList.add(objInvestmentsAmortizationCalculationTO);

                    }
                }

            }
            System.out.println("investmentAmrotizationList---------------->" + investmentAmrotizationList);

            return investmentAmrotizationList;

        } catch (Exception e) {
            e.printStackTrace();
            sqlMap.rollbackTransaction();
            throw e;
        }
    }
    /*
     * This is used to free up the memory used by SharePrductTO object
     */

    private void destroyObjects() {
        objTO = null;
        loanDataMap = null;
        deletedLoanDataMap = null;
        objAmrCalTO = null;
    }
}
