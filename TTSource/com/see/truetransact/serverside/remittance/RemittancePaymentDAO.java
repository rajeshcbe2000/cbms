/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittancePaymentTODAO.java
 *
 * Created on Sun Jan 18 11:32:21 IST 2004
 */
package com.see.truetransact.serverside.remittance;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
//import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import org.apache.log4j.Logger;

import com.see.truetransact.transferobject.remittance.RemittancePaymentTO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.transaction.chargesServiceTax.ChargesServiceTaxTO;

import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import java.util.LinkedHashMap;
import java.util.Date;
/**
 * RemittancePaymentTO DAO.
 *
 */
public class RemittancePaymentDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private RemittancePaymentTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private final String REMIT_PAY_ID = "REMIT_PAY_ID";
    private TransferDAO transferDAO = new TransferDAO();
    private TransactionDAO transactionDAO = null;
    private final static Logger _log = Logger.getLogger(RemittancePaymentDAO.class);
    private TransactionTO objTransactionTO;
    private LinkedHashMap transactionDetailsMap;// Contains all the Transaction Details TOs
    private LinkedHashMap allowedTransDetailsTO;
    private LinkedHashMap deletedTransDetailsTO;// Contains deleted trans details
    private String operationMode = null;
    private RemittancePaymentTO objRemittancePaymentTO;
    private boolean callingFromOtherDAO = false;
    private Date currDt = null;
    /**
     * Creates a new instance of RemittancePaymentTODAO
     */
    public RemittancePaymentDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private void insertData(HashMap obj, String command) throws Exception {
        objTO.setStatus(CommonConstants.STATUS_CREATED);
        objTO.setRemitPayDt(currDt);
        String Remit_Pay_ID = getRemitPayID();
        objTO.setRemitPayId(Remit_Pay_ID);
        Remit_Pay_ID = null;
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        objTO.setCreatedDt(currDt);
        sqlMap.executeUpdate("insertRemittancePaymentTO", objTO);
        transactionDAO.setBatchId(objTO.getRemitPayId());
        transactionDAO.setBatchDate(currDt);
        transactionDAO.setLinkBatchID(objTO.getRemitPayId());
        insertAccHead(obj, command);
        logDAO.addToLog(logTO);
    }

    private String getRemitPayID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", REMIT_PAY_ID);
        String str = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return str;
    }

    private void updateData(HashMap obj, String command) throws Exception {

        objTO.setStatus(CommonConstants.STATUS_MODIFIED);
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        objTO.setStatusDt(currDt);
        sqlMap.executeUpdate("updateRemittancePaymentTO", objTO);
        transactionDAO.setLinkBatchID(objTO.getRemitPayId());
        insertAccHead(obj, command);
        logDAO.addToLog(logTO);
    }

    /*
     * public void doUpdate(String command) throws Exception{
     *
     * deletedTransDetailsTO = (LinkedHashMap)
     * transactionDetailsMap.remove("DELETED_TRANS_TOs"); if(
     * (deletedTransDetailsTO != null) ){ for (int i = 1,j =
     * deletedTransDetailsTO.size();i<=j;i++){ objRemittancePaymentTO =
     * (RemittancePaymentTO)deletedTransDetailsTO.get(String.valueOf(i));
     * deleteRemitPaymentDetails(command); } } if( allowedTransDetailsTO != null
     * ) { for (int i = 1,j = allowedTransDetailsTO.size();i<=j;i++){
     * objRemittancePaymentTO =
     * (RemittancePaymentTO)allowedTransDetailsTO.get(String.valueOf(i));
     *
     * if((objRemittancePaymentTO.getStatus() != null) &&
     * (objRemittancePaymentTO.getStatus().length() >0 )) {
     * updateRemitPaymentDetails(command);
     *
     * } else {
     *
     * insertRemitPaymentDetails(command); } } }
    }
     */
    private void deleteData(HashMap obj, String command) throws Exception {
        objTO.setStatus(CommonConstants.STATUS_DELETED);
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        objTO.setStatusDt(currDt);
        insertAccHead(obj, command);
        sqlMap.executeUpdate("deleteRemittancePaymentTO", objTO);
        logDAO.addToLog(logTO);
    }

    public HashMap execute(HashMap map) throws Exception {
//	CommonUtil.serializeObjWrite("D:\\myFile2.txt", map) ;
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        System.out.println("Map in payment DAO : " + map);
        if (!isCallingFromOtherDAO()) {
            sqlMap.startTransaction();
        }
        HashMap returnData = null;
        try {

            if (map.containsKey("RemittancePaymentTO")) {
                transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
                transactionDAO.setInitiatedBranch(_branchCode);
                objTO = (RemittancePaymentTO) map.get("RemittancePaymentTO");
                objTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                logDAO = new LogDAO();
                logTO = new LogTO();

                logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
                logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
                logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
                logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                if (objTO != null) {
                    final String command = objTO.getCommand();
                    if (command != null) {
                        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                            insertData(map, command);
                            transactionDAO.execute(map);
                            returnData = new HashMap();
                            returnData.put(REMIT_PAY_ID, objTO.getRemitPayId());
                            returnData.put("PAY_STATUS", objTO.getPayStatus());
//                            System.out.println("@@@@@@@@returnData"+returnData);
                            if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                                transactionDAO.doTransfer();
                            }
                        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                            updateData(map, command);
                            transactionDAO.execute(map);
                        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            deleteData(map, command);
                            transactionDAO.execute(map);
                        } else {
                            throw new NoCommandException();
                        }
                        destroyObjects();
                    }
                }
            } else {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                if (authMap != null) {
                    authorize(authMap);
                }
            }
            if (!isCallingFromOtherDAO()) {
                sqlMap.commitTransaction();
            }
        } catch (Exception e) {
            if (!isCallingFromOtherDAO()) {
                sqlMap.rollbackTransaction();
            }
            e.printStackTrace();
            _log.error(e);
            throw new TransRollbackException();
        }
        map = null;
        return returnData;
    }

    /**
     * To insert data for each account head
     */
    public void insertAccHead(HashMap obj, String command) throws Exception {
        System.out.println("######iside insertAccHead");
        String branchID = objTO.getBranchId();
        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getRemitAccountHeads", objTO.getInstrumentType());
        HashMap txMap = new HashMap();
        //TransactionTO objTransactionTO;
        TxTransferTO transferTo = null;
        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) obj.get("TransactionTO");
        if (TransactionDetailsMap.size() > 0) {
            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                for (int i = 1, j = allowedTransDetailsTO.size(); i <= j; i++) {
                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(i));

                    Double tempAmt = objTO.getCharges();

                    //edit mode
                    if (obj.containsKey("CHARGES_TRANSFER_TRANS_DETAILS")) {
                        System.out.println("######iside 1st edit");
                        HashMap tempMap = (HashMap) obj.get("CHARGES_TRANSFER_TRANS_DETAILS");
                        tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                        tempMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                        tempMap.put("TRANS_DT", currDt.clone());
                        tempMap.put("INITIATED_BRANCH", _branchCode);
                        double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                        double newAmount = CommonUtil.convertObjToDouble(objTO.getCharges()).doubleValue();
                        ArrayList batchList = new ArrayList();
                        TxTransferTO txTransferTO = null;
                        HashMap oldAmountMap = new HashMap();
//                        if(newAmount == 0)
//                            command = CommonConstants.TOSTATUS_DELETE;
                        if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                            if (lst != null) {
                                txTransferTO = (TxTransferTO) lst.get(0);
                                txTransferTO.setInpAmount(new Double(newAmount));
                                txTransferTO.setAmount(new Double(newAmount));
                                if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                } else {
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                }
                                txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                                TransferTrans transferTrans = new TransferTrans();
                                transferTrans.setOldAmount(oldAmountMap);
                                transferTrans.setInitiatedBranch(branchID);
                                transferTrans.doDebitCredit(batchList, branchID, false, command);
                                lst = null;
                                transferTrans = null;
                            }
                        }
                        txTransferTO = null;
                        batchList = null;
                        oldAmountMap = null;
                        tempMap = null;
                        // objTO.setCharges(new Double(0));
                    }

                    //edit mode
                    if (obj.containsKey("TRANSCHRG_TRANSFER_TRANS_DETAILS")) {
                        System.out.println("######iside 2nd edit");
                        HashMap tempMap = (HashMap) obj.get("TRANSCHRG_TRANSFER_TRANS_DETAILS");
                        tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                        tempMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                        tempMap.put("TRANS_DT", currDt.clone());
                        tempMap.put("INITIATED_BRANCH", _branchCode);
                        double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                        double newAmount = CommonUtil.convertObjToDouble(objTO.getCharges()).doubleValue();
                        ArrayList batchList = new ArrayList();
                        TxTransferTO txTransferTO = null;
                        HashMap oldAmountMap = new HashMap();
//                         if(newAmount == 0)
//                            command = CommonConstants.TOSTATUS_DELETE;
                        if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                            if (lst != null) {
                                txTransferTO = (TxTransferTO) lst.get(0);
                                txTransferTO.setInpAmount(new Double(newAmount));
                                txTransferTO.setAmount(new Double(newAmount));
                                if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                } else {
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                }
                                txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                                TransferTrans transferTrans = new TransferTrans();
                                transferTrans.setOldAmount(oldAmountMap);
                                transferTrans.setInitiatedBranch(branchID);
                                transferTrans.doDebitCredit(batchList, branchID, false, command);
                                lst = null;
                                transferTrans = null;
                            }
                        }
                        txTransferTO = null;
                        batchList = null;
                        oldAmountMap = null;
                        tempMap = null;
                        objTO.setCharges(new Double(0));
                    }

                    //edit mode
                    if (obj.containsKey("SERVICE_TRANSFER_TRANS_DETAILS")) {
                        System.out.println("######iside 2nd edit");
                        HashMap tempMap = (HashMap) obj.get("SERVICE_TRANSFER_TRANS_DETAILS");
                        tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                        tempMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                        tempMap.put("TRANS_DT", currDt.clone());
                        tempMap.put("INITIATED_BRANCH", _branchCode);
                        double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                        double newAmount = CommonUtil.convertObjToDouble(objTO.getServiceTax()).doubleValue();
                        ArrayList batchList = new ArrayList();
                        TxTransferTO txTransferTO = null;
                        HashMap oldAmountMap = new HashMap();
//                         if(newAmount == 0)
//                            command = CommonConstants.TOSTATUS_DELETE;
                        if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                            if (lst != null) {
                                txTransferTO = (TxTransferTO) lst.get(0);
                                txTransferTO.setInpAmount(new Double(newAmount));
                                txTransferTO.setAmount(new Double(newAmount));
                                if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                } else {
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                }
                                txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                                TransferTrans transferTrans = new TransferTrans();
                                transferTrans.setOldAmount(oldAmountMap);
                                transferTrans.setInitiatedBranch(branchID);
                                transferTrans.doDebitCredit(batchList, branchID, false, command);
                                lst = null;
                                transferTrans = null;
                            }
                        }
                        txTransferTO = null;
                        batchList = null;
                        oldAmountMap = null;
                        tempMap = null;
//                        objTO.setServiceTax(new Double(0));
                    }

                    //edit mode
                    if (obj.containsKey("TRANSERVICE_TRANSFER_TRANS_DETAILS")) {
                        System.out.println("######iside 2nd edit");
                        HashMap tempMap = (HashMap) obj.get("TRANSERVICE_TRANSFER_TRANS_DETAILS");
                        tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                        tempMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                        tempMap.put("TRANS_DT", currDt.clone());
                        tempMap.put("INITIATED_BRANCH", _branchCode);
                        double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                        double newAmount = CommonUtil.convertObjToDouble(objTO.getServiceTax()).doubleValue();
                        ArrayList batchList = new ArrayList();
                        TxTransferTO txTransferTO = null;
                        HashMap oldAmountMap = new HashMap();
//                         if(newAmount == 0)
//                            command = CommonConstants.TOSTATUS_DELETE;
                        if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                            if (lst != null) {
                                txTransferTO = (TxTransferTO) lst.get(0);
                                txTransferTO.setInpAmount(new Double(newAmount));
                                txTransferTO.setAmount(new Double(newAmount));
                                if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                    txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                } else {
                                    txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                }
                                txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                batchList.add(txTransferTO);
                                TransferTrans transferTrans = new TransferTrans();
                                transferTrans.setOldAmount(oldAmountMap);
                                transferTrans.setInitiatedBranch(branchID);
                                transferTrans.doDebitCredit(batchList, branchID, false, command);
                                lst = null;
                                transferTrans = null;
                            }
                        }
                        txTransferTO = null;
                        batchList = null;
                        oldAmountMap = null;
                        tempMap = null;
                        objTO.setServiceTax(new Double(0));
                    }

                    //new mode
                    if (objTO.getCharges() != null && CommonUtil.convertObjToDouble(objTO.getCharges()).doubleValue() != 0) {
                        System.out.println("######getchargenewmode");
                        System.out.println("######iside new mode");
                        TxTransferTO txTransferTO = null;
                        txMap = new HashMap();
                        ArrayList transferList = new ArrayList(); // for local transfer
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("PAY_HD"));
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("CANCELL_CHRG_HD"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.PARTICULARS, objTO.getInstrumentType() + " Cancellation Chrg" + " " + objTO.getInstrumentNo1() + "" + objTO.getInstrumentNo2());
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(objTO.getCharges()).doubleValue());
                        transferList.add(transferTo);
//                         txMap.put(TransferTrans.PARTICULARS, objTO.getInstrumentType());
                        txMap.put(TransferTrans.PARTICULARS, objTO.getInstrumentType() + " Cancellation Chrg" + " " + objTO.getInstrumentNo1() + "" + objTO.getInstrumentNo2());
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(objTO.getCharges()).doubleValue());
                        transferList.add(transferTo);
                        transactionDAO.doTransferLocal(transferList, branchID);
                    }
                    objTO.setCharges(tempAmt);
                    //new mode
                    if (objTO.getServiceTax() != null && CommonUtil.convertObjToDouble(objTO.getServiceTax()).doubleValue() != 0) {
                        System.out.println("######ServiceTax");
                        System.out.println("######ServiceTax new mode");
                        TxTransferTO txTransferTO = null;
                        txMap = new HashMap();
                        ArrayList transferList = new ArrayList(); // for local transfer
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("PAY_HD"));
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("OTHER_CHRG_HD"));
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.CR_BRANCH, branchID);
                        txMap.put(TransferTrans.PARTICULARS, objTO.getInstrumentType() + " Cancellation Chrg Service Tax" + " " + objTO.getInstrumentNo1() + "" + objTO.getInstrumentNo2());
                        transferTo = transactionDAO.addTransferDebitLocal(txMap, CommonUtil.convertObjToDouble(objTO.getServiceTax()).doubleValue());
                        transferList.add(transferTo);
//                         txMap.put(TransferTrans.PARTICULARS, objTO.getInstrumentType());
                        txMap.put(TransferTrans.PARTICULARS, objTO.getInstrumentType() + " Cancellation Chrg Service Tax" + " " + objTO.getInstrumentNo1() + "" + objTO.getInstrumentNo2());
                        transferTo = transactionDAO.addTransferCreditLocal(txMap, CommonUtil.convertObjToDouble(objTO.getServiceTax()).doubleValue());
                        transferList.add(transferTo);
                        transactionDAO.doTransferLocal(transferList, branchID);
                    }
                    //edit mode
                    if (objTransactionTO.getTransType().equalsIgnoreCase("CASH")) {
                        System.out.println("######iside 3rd edit");
                        if (obj.containsKey("AMT_CASH_TRANS_DETAILS")) {
                            HashMap tempMap = (HashMap) obj.get("AMT_CASH_TRANS_DETAILS");
                            tempMap.put("TRANS_DT", currDt.clone());
                            tempMap.put("INITIATED_BRANCH", _branchCode);
                            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                            double newAmount = CommonUtil.convertObjToDouble(objTO.getPayAmt()).doubleValue();
                            CashTransactionTO txTransferTO = null;
                            HashMap oldAmountMap = new HashMap();
                            if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                List lst = sqlMap.executeQueryForList("getSelectCashTransactionTO", tempMap);
                                if (lst != null) {
                                    txTransferTO = (CashTransactionTO) lst.get(0);
                                    txTransferTO.setInpAmount(new Double(newAmount));
                                    txTransferTO.setAmount(new Double(newAmount));
                                    txTransferTO.setCommand(command);
                                    if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                        txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                    } else {
                                        txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                    }
                                    txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                    txTransferTO.setProdId(objTO.getInstrumentType());
                                    obj.put("PRODUCTTYPE", TransactionFactory.GL);
                                    obj.put(TransactionDAOConstants.OLDAMT, new Double(oldAmount));
                                    obj.put("CashTransactionTO", txTransferTO);
                                    CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                                    cashTransDAO.execute(obj, false);
                                    txMap = null;
                                }
                                lst = null;
                            }
                            txTransferTO = null;
                            oldAmountMap = null;
                            tempMap = null;
                            objTO.setPayAmt(new Double(0));
                        }
                    }

                    if (objTransactionTO.getTransType().equalsIgnoreCase("TRANSFER")) {
                        //System.out.println("##### TRANSFER MODE.........");
                        System.out.println("######iside TRANSFER");
                        double transAmt;
                        ArrayList transferList = new ArrayList(); // for local transfer
                        tempAmt = objTO.getPayAmt();

                        //edit mode
                        if (obj.containsKey("AMT_TRANSFER_TRANS_DETAILS")) {
                            System.out.println("######iside AMT_TRANSFER_TRANS_DETAILS");
                            HashMap tempMap = (HashMap) obj.get("AMT_TRANSFER_TRANS_DETAILS");
                            tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                            tempMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                            tempMap.put("TRANS_DT", currDt.clone());
                            tempMap.put("INITIATED_BRANCH", _branchCode);
                            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();//TOTAL_AMT
                            double newAmount = CommonUtil.convertObjToDouble(objTO.getPayAmt()).doubleValue();
                            ArrayList batchList = new ArrayList();
                            TxTransferTO txTransferTO = null;
                            HashMap oldAmountMap = new HashMap();
//                             if(newAmount == 0)
//                            command = CommonConstants.TOSTATUS_DELETE;
                            if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                                if (lst != null) {
                                    txTransferTO = (TxTransferTO) lst.get(0);
                                    txTransferTO.setInpAmount(new Double(newAmount));
                                    txTransferTO.setAmount(new Double(newAmount));
                                    if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                        txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                    } else {
                                        txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                    }
                                    txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                    txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                    oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                    batchList.add(txTransferTO);
                                    TransferTrans transferTrans = new TransferTrans();
                                    transferTrans.setOldAmount(oldAmountMap);
                                    transferTrans.setInitiatedBranch(branchID);
                                    transferTrans.doDebitCredit(batchList, branchID, false, command);
                                    lst = null;
                                    transferTrans = null;
                                }
                            }
                            txTransferTO = null;
                            batchList = null;
                            oldAmountMap = null;
                            tempMap = null;
                            //objTO.setPayAmt(new Double(0));
                        }
                        //edit mode
                        if (obj.containsKey("AMT_TRANSFER_TRANS")) {
                            System.out.println("######iside AMT_TRANSFER_TRANS");
                            HashMap tempMap = (HashMap) obj.get("AMT_TRANSFER_TRANS");
                            tempMap.put("BATCHID", tempMap.get("BATCH_ID"));
                            tempMap.put("TRANS_ID", tempMap.get("TRANS_ID"));
                            tempMap.put("TRANS_DT", currDt.clone());
                            tempMap.put("INITIATED_BRANCH", _branchCode);
                            double oldAmount = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                            double newAmount = CommonUtil.convertObjToDouble(objTO.getPayAmt()).doubleValue();
                            System.out.println("########AMT" + newAmount + "%%%AMT" + oldAmount);
                            ArrayList batchList = new ArrayList();
                            TxTransferTO txTransferTO = null;
                            HashMap oldAmountMap = new HashMap();
//                             if(newAmount == 0)
//                            command = CommonConstants.TOSTATUS_DELETE;
                            if ((oldAmount != newAmount) || command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                List lst = sqlMap.executeQueryForList("getBatchTxTransferTOs", tempMap);
                                if (lst != null) {
                                    txTransferTO = (TxTransferTO) lst.get(0);
                                    txTransferTO.setInpAmount(new Double(newAmount));
                                    txTransferTO.setAmount(new Double(newAmount));
                                    if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                                        txTransferTO.setStatus(CommonConstants.STATUS_DELETED);
                                    } else {
                                        txTransferTO.setStatus(CommonConstants.STATUS_MODIFIED);
                                    }
                                    txTransferTO.setStatusDt(ServerUtil.getCurrentDate(branchID));
                                    txTransferTO.setTransMode(CommonConstants.TX_TRANSFER);
                                    oldAmountMap.put(txTransferTO.getTransId(), new Double(oldAmount));
                                    batchList.add(txTransferTO);
                                    TransferTrans transferTrans = new TransferTrans();
                                    transferTrans.setOldAmount(oldAmountMap);
                                    transferTrans.setInitiatedBranch(branchID);
                                    transferTrans.doDebitCredit(batchList, branchID, false, command);
                                    lst = null;
                                    transferTrans = null;
                                }
                            }
                            txTransferTO = null;
                            batchList = null;
                            oldAmountMap = null;
                            tempMap = null;
                            objTO.setPayAmt(new Double(0));
                        }

                    }

                    //new mode
                    if (objTO.getPayAmt() != null) {
                        System.out.println("######iside las new mode");
                        txMap = new HashMap();
                        txMap.put(TransferTrans.DR_AC_HD, (String) acHeads.get("PAY_HD"));
                        txMap.put(TransferTrans.DR_BRANCH, branchID);
                        txMap.put(TransferTrans.CURRENCY, "INR");
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        if (objTransactionTO.getTransType().equalsIgnoreCase("CASH")) {
                            txMap.put(TransferTrans.PARTICULARS, objTO.getInstrumentType() + " " + objTO.getPayStatus() + " " + objTO.getInstrumentNo1() + "" + objTO.getInstrumentNo2());
                        } else {
                            txMap.put(TransferTrans.PARTICULARS, objTO.getInstrumentType() + " " + objTO.getPayStatus() + " " + objTO.getInstrumentNo1() + "" + objTO.getInstrumentNo2());
                        }
                        transactionDAO.addTransferDebit(txMap, CommonUtil.convertObjToDouble(objTO.getPayAmt()).doubleValue());
                    }
                    objTO.setPayAmt(tempAmt);
                }
            }
        }
    }

    private void authorizeUpdate(RemittancePaymentTO objRemittancePaymentTO) throws Exception {
        HashMap updateMap = new HashMap();
        updateMap.put("VARIABLE_NO", objRemittancePaymentTO.getSerialNo());
        updateMap.put("REMIT_STATUS", objRemittancePaymentTO.getPayStatus());
        updateMap.put("TODAY_DT", currDt.clone());
        sqlMap.executeUpdate("updateRemittanceIssue", updateMap);
    }

    private RemittancePaymentTO getRemittancePaymentData(String remitPayId) throws Exception {
        List list = (List) sqlMap.executeQueryForList("getSelectRemittancePaymentTO", remitPayId);
        return ((RemittancePaymentTO) list.get(0));
    }

    private void authorize(HashMap map) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap;
        String remitPayId;
        RemittancePaymentTO objRemittancePaymentTO = null;
        for (int i = 0; i < selectedList.size(); i++) {
            dataMap = (HashMap) selectedList.get(i);
            //System.out.println("dataMap###"+dataMap);
            remitPayId = CommonUtil.convertObjToStr(dataMap.get(REMIT_PAY_ID));
            //System.out.println("remitPayId###"+remitPayId);
            dataMap.put(CommonConstants.STATUS, status);
            dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            dataMap.put(CommonConstants.BRANCH_ID, _branchCode);
            dataMap.put("TODAY_DT", currDt.clone());
            //System.out.println("datamap %%^^^^^^"+dataMap);
            TransactionDAO dao = new TransactionDAO(CommonConstants.DEBIT);
            TransactionDAO.authorizeCashAndTransfer(remitPayId, status, dataMap);
            dao = null;
            sqlMap.executeUpdate("authorizeRemitPayment", dataMap);
            if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                objRemittancePaymentTO = getRemittancePaymentData(remitPayId);
                if (objRemittancePaymentTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED) || objRemittancePaymentTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                    authorizeUpdate(objRemittancePaymentTO);
                } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                    objRemittancePaymentTO = getRemittancePaymentData(remitPayId);
                    if (objRemittancePaymentTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                            || objRemittancePaymentTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                        sqlMap.executeUpdate("rejectRemitPayment", dataMap);
                    }
                }
            }
            if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                ChargesServiceTaxTO objChargesServiceTaxTO = new ChargesServiceTaxTO();
                HashMap whereMap = new HashMap();
                whereMap.put("VARIABLE_NO", CommonUtil.convertObjToStr(dataMap.get(REMIT_PAY_ID)));
                List exgStLst = (List) sqlMap.executeQueryForList("getSelectExgSTPayment", whereMap);
                whereMap = null;
                if (exgStLst != null && exgStLst.size() > 0) {
                    whereMap = (HashMap) exgStLst.get(0);
                    objChargesServiceTaxTO.setAmount(CommonUtil.convertObjToDouble(whereMap.get("CHARGES")));
                    objChargesServiceTaxTO.setService_tax_amt(CommonUtil.convertObjToDouble(whereMap.get("SERVICE_TAX")));
                    objChargesServiceTaxTO.setTotal_amt(CommonUtil.convertObjToDouble(whereMap.get("TOT_AMT")));
                }
                exgStLst = null;
                exgStLst = (List) sqlMap.executeQueryForList("getSelectSTHeadRemittancePayment", whereMap);
                whereMap = null;
                if (exgStLst != null && exgStLst.size() > 0) {
                    whereMap = (HashMap) exgStLst.get(0);
                    whereMap.put("VARIABLE_NO", CommonUtil.convertObjToStr(dataMap.get(REMIT_PAY_ID)));
                    whereMap.put("SERVICE_TAX", objChargesServiceTaxTO.getService_tax_amt());
                    String exgHd = CommonUtil.convertObjToStr(whereMap.get("EXCHANGE_HD"));
                    exgStLst = null;
                    exgStLst = (List) sqlMap.executeQueryForList("getSelectTransCashRemitSTDetails", whereMap);
                    whereMap = null;
                    if (exgStLst != null && exgStLst.size() > 0) {
                        whereMap = (HashMap) exgStLst.get(0);
                        Date curDate = ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(_branchCode));
                        objChargesServiceTaxTO.setAc_Head(exgHd);
                        objChargesServiceTaxTO.setAcct_num(CommonUtil.convertObjToStr(whereMap.get("ACT_NUM")));
                        objChargesServiceTaxTO.setAuthorize_by(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                        objChargesServiceTaxTO.setAuthorize_dt(curDate);
                        objChargesServiceTaxTO.setAuthorize_status(status);
                        objChargesServiceTaxTO.setCreated_by(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                        objChargesServiceTaxTO.setProd_id(CommonUtil.convertObjToStr(whereMap.get("PROD_ID")));
                        objChargesServiceTaxTO.setProd_type(CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE")));
                        objChargesServiceTaxTO.setStatus(CommonConstants.STATUS_CREATED);
                        objChargesServiceTaxTO.setTrans_id(CommonUtil.convertObjToStr(whereMap.get("TRAN_ID")));
                        objChargesServiceTaxTO.setCreated_dt(curDate);
                        objChargesServiceTaxTO.setTrans_dt(curDate);
                        objChargesServiceTaxTO.setParticulars(CommonUtil.convertObjToStr(whereMap.get("PARTICULARS")));
                        objChargesServiceTaxTO.setBranchCode(CommonUtil.convertObjToStr(whereMap.get("BRANCH_ID")));
                        sqlMap.executeUpdate("insertChargesServiceTaxTO", objChargesServiceTaxTO);
                        whereMap = null;
                        exgStLst = null;
                    }

                }
            }

        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {

        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        transactionDAO = new TransactionDAO(CommonConstants.CREDIT);
        TransactionTO dataTO = new TransactionTO();
        RemittancePaymentTO objRemitPaymentTO;
        HashMap returnMap = new HashMap();
        HashMap dataMap = new HashMap();
        String where = (String) obj.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectRemittancePaymentTO", where);
        objRemitPaymentTO = (RemittancePaymentTO) list.get(0);
        returnMap.put("RemittancePaymentTO", list);
        list = null;

        list = transactionDAO.getData(obj);
        dataTO = (TransactionTO) list.get(0);
        if (dataTO.getTransType().equals("CASH")) {
            if (returnMap != null) {
                if (returnMap.size() > 0) {
                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getRemitAccountHeads", objRemitPaymentTO.getInstrumentType());
                    HashMap getTransMap = new HashMap();
                    dataMap.put("AMOUNT", objRemitPaymentTO.getPayAmt());
                    double amount = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
                    //System.out.println("@@@@@@@amount"+amount);
                    getTransMap.put("LINK_BATCH_ID", objRemitPaymentTO.getRemitPayId());
                    getTransMap.put("TODAY_DT", currDt.clone());
                    getTransMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(_branchCode));
                    if (amount > 0) {
                        getTransMap.put("AC_HD_ID", acHeads.get("PAY_HD"));
                        getTransMap.put("AMOUNT", dataMap.get("AMOUNT"));
                        List lst = (List) sqlMap.executeQueryForList("getCashTransBatchID", getTransMap);
                        if (lst != null) {
                            if (lst.size() > 0) {
                                returnMap.put("AMT_CASH_TRANS_DETAILS", lst.get(0));
                            }
                        }
                        lst = null;
                    }
                    dataMap.put("CHARGES", objRemitPaymentTO.getCharges());
                    //System.out.println("@@@@@@***@dataMap"+dataMap);
                    double charge = CommonUtil.convertObjToDouble(dataMap.get("CHARGES")).doubleValue();
                    if (charge > 0) {
                        getTransMap.put("AC_HD_ID", acHeads.get("CANCELL_CHRG_HD"));
                        getTransMap.put("AMOUNT", dataMap.get("CHARGES"));
                        List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                        if (lst != null) {
                            if (lst.size() > 0) {
                                returnMap.put("CHARGES_TRANSFER_TRANS_DETAILS", lst.get(0));
                            }
                        }
                        lst = null;
                    }
                    dataMap.put("TRANSCHRG", objRemitPaymentTO.getCharges());
                    //System.out.println("@@@@@@***@dataMap"+dataMap);
                    double transamt = CommonUtil.convertObjToDouble(dataMap.get("TRANSCHRG")).doubleValue();
                    if (charge > 0) {
                        getTransMap.put("AC_HD_ID", acHeads.get("PAY_HD"));
                        getTransMap.put("AMOUNT", dataMap.get("TRANSCHRG"));

                        List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                        if (lst != null) {
                            if (lst.size() > 0) {
                                returnMap.put("TRANSCHRG_TRANSFER_TRANS_DETAILS", lst.get(0));
                            }
                        }
                        lst = null;
                    }
                    dataMap.put("SERVICETAX", objRemitPaymentTO.getServiceTax());
                    //System.out.println("@@@@@@***@dataMap"+dataMap);
                    double serviceTax = CommonUtil.convertObjToDouble(dataMap.get("SERVICETAX")).doubleValue();
                    if (serviceTax > 0) {
                        getTransMap.put("AC_HD_ID", acHeads.get("OTHER_CHRG_HD"));
                        getTransMap.put("AMOUNT", dataMap.get("SERVICETAX"));
                        List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                        if (lst != null) {
                            if (lst.size() > 0) {
                                returnMap.put("SERVICE_TRANSFER_TRANS_DETAILS", lst.get(0));
                            }
                        }
                        lst = null;
                    }
                }
            }
        }
        if (dataTO.getTransType().equals("TRANSFER")) {
            if (returnMap != null) {
                if (returnMap.size() > 0) {
                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getRemitAccountHeads", objRemitPaymentTO.getInstrumentType());
                    HashMap getTransMap = new HashMap();
                    dataMap.put("AMOUNT", objRemitPaymentTO.getPayAmt());
                    double amount = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
                    //System.out.println("@@@@@@@amount"+amount);
                    getTransMap.put("LINK_BATCH_ID", objRemitPaymentTO.getRemitPayId());
                    getTransMap.put("TODAY_DT", ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(_branchCode)));
                    getTransMap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(_branchCode));
                    if (amount > 0) {
                        getTransMap.put("AC_HD_ID", acHeads.get("PAY_HD"));
                        getTransMap.put("AMOUNT", dataMap.get("AMOUNT"));
                        List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                        if (lst != null) {
                            if (lst.size() > 0) {

                                returnMap.put("AMT_TRANSFER_TRANS_DETAILS", lst.get(0));
                            }
                        }
                        lst = null;
                    }
                    dataMap.put("TRANSAMOUNT", objRemitPaymentTO.getPayAmt());
                    double transAmt = CommonUtil.convertObjToDouble(dataMap.get("AMOUNT")).doubleValue();
                    //System.out.println("@@@@@@@amount"+amount);
                    getTransMap.put("LINK_BATCH_ID", objRemitPaymentTO.getRemitPayId());
                    getTransMap.put("TODAY_DT", ServerUtil.getCurrentDate(CommonUtil.convertObjToStr(_branchCode)));
                    if (transAmt > 0) {
                        //getTransMap.put("AC_HD_ID",acHeads.get("PAY_HD"));
                        HashMap newData = new HashMap();
                        if (returnMap.containsKey("AMT_TRANSFER_TRANS_DETAILS")) {
                            newData = (HashMap) returnMap.get("AMT_TRANSFER_TRANS_DETAILS");
                            getTransMap.put("TRANS_ID", newData.get("TRANS_ID"));
                        }
                        getTransMap.put("AMOUNT", dataMap.get("TRANSAMOUNT"));
                        getTransMap.remove("AC_HD_ID");
                        List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                        if (lst != null) {
                            if (lst.size() > 0) {

                                returnMap.put("AMT_TRANSFER_TRANS", lst.get(0));
                            }
                        }
                        lst = null;
                    }
                    dataMap.put("CHARGES", objRemitPaymentTO.getCharges());
                    //System.out.println("@@@@@@***@dataMap"+dataMap);
                    double charge = CommonUtil.convertObjToDouble(dataMap.get("CHARGES")).doubleValue();
                    if (charge > 0) {
                        getTransMap.put("AC_HD_ID", acHeads.get("CANCELL_CHRG_HD"));
                        getTransMap.put("AMOUNT", dataMap.get("CHARGES"));

                        List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                        if (lst != null) {
                            if (lst.size() > 0) {
                                returnMap.put("CHARGES_TRANSFER_TRANS_DETAILS", lst.get(0));
                            }
                        }
                        lst = null;
                    }
                    dataMap.put("TRANSCHRG", objRemitPaymentTO.getCharges());
                    //System.out.println("@@@@@@***@dataMap"+dataMap);
                    double transamt = CommonUtil.convertObjToDouble(dataMap.get("TRANSCHRG")).doubleValue();
                    if (charge > 0) {
                        getTransMap.put("AC_HD_ID", acHeads.get("PAY_HD"));
                        getTransMap.put("AMOUNT", dataMap.get("TRANSCHRG"));

                        List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                        if (lst != null) {
                            if (lst.size() > 0) {
                                returnMap.put("TRANSCHRG_TRANSFER_TRANS_DETAILS", lst.get(0));
                            }
                        }
                        lst = null;
                    }
                    dataMap.put("SERVICETAX", objRemitPaymentTO.getServiceTax());
                    //System.out.println("@@@@@@***@dataMap"+dataMap);
                    double serviceTax = CommonUtil.convertObjToDouble(dataMap.get("SERVICETAX")).doubleValue();
                    if (serviceTax > 0) {
                        getTransMap.put("AC_HD_ID", acHeads.get("OTHER_CHRG_HD"));
                        getTransMap.put("AMOUNT", dataMap.get("SERVICETAX"));
                        List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                        if (lst != null) {
                            if (lst.size() > 0) {
                                returnMap.put("SERVICE_TRANSFER_TRANS_DETAILS", lst.get(0));
                            }
                        }
                        lst = null;
                    }
                    dataMap.put("TRANSERVICETAX", objRemitPaymentTO.getServiceTax());
                    //System.out.println("@@@@@@***@dataMap"+dataMap);
                    double tranServiceTax = CommonUtil.convertObjToDouble(dataMap.get("SERVICETAX")).doubleValue();
                    if (serviceTax > 0) {
                        getTransMap.put("AC_HD_ID", acHeads.get("PAY_HD"));
                        getTransMap.put("AMOUNT", dataMap.get("TRANSERVICETAX"));
                        List lst = (List) sqlMap.executeQueryForList("getTransferTransBatchID", getTransMap);
                        if (lst != null) {
                            if (lst.size() > 0) {
                                returnMap.put("TRANSERVICE_TRANSFER_TRANS_DETAILS", lst.get(0));
                            }
                        }
                        lst = null;
                    }
                }
            }
        }

        System.out.println("@@@@@@returnMap" + returnMap);
        returnMap.put("TransactionTO", list);
        where = null;
        return returnMap;
    }

    private void destroyObjects() {
        objTO = null;
    }

    public static void main(String args[]) throws Exception {
        HashMap inputMap = CommonUtil.serializeObjRead("D:\\myfile2.txt");
        RemittancePaymentDAO acobj = new RemittancePaymentDAO();
        acobj.execute(inputMap);
        //acobj.doAuthorize(inputMap);
    }

    /**
     * Getter for property callingFromOtherDAO.
     *
     * @return Value of property callingFromOtherDAO.
     */
    public boolean isCallingFromOtherDAO() {
        return callingFromOtherDAO;
    }

    /**
     * Setter for property callingFromOtherDAO.
     *
     * @param callingFromOtherDAO New value of property callingFromOtherDAO.
     */
    public void setCallingFromOtherDAO(boolean callingFromOtherDAO) {
        this.callingFromOtherDAO = callingFromOtherDAO;
    }
}
