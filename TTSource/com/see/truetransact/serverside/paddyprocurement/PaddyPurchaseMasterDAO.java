/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PaddyPurchaseMasterDAO.java
 *
 * Created on Fri Jun 10 15:52:50 IST 2011
 */
package com.see.truetransact.serverside.paddyprocurement;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.SQLException;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.sql.*;
import oracle.sql.*;
import org.apache.log4j.Logger;

import com.see.truetransact.serverside.customer.CustomerHistoryDAO;
import com.see.truetransact.transferobject.customer.CustomerHistoryTO;
import com.see.truetransact.commonutil.AcctStatusConstants;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;

import com.see.truetransact.transferobject.paddyprocurement.PaddyPurchaseMasterTO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * PaddyPurchaseMaster DAO.
 *
 */
public class PaddyPurchaseMasterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private PaddyPurchaseMasterTO objPaddyPurchaseMasterTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private TransactionDAO transactionDAO = null;
    private String userID = "";
    private Date currDt = null;
    private HashMap whereConditions;
    private String whereCondition;
    private final static Logger log = Logger.getLogger(PaddyPurchaseMasterDAO.class);
    private HashMap data;
    private String key;
    private LinkedHashMap purchaseMap = new LinkedHashMap();
    private LinkedHashMap deletedPurchase = new LinkedHashMap();
    private Iterator addressIterator;
    private String addressKey = new String();
    private String command = "";
    private String paddyTransID = "";
    private TransactionTO objTransactionTO;

    /**
     * Creates a new instance of PaddyPurchaseMasterDAO
     */
    public PaddyPurchaseMasterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectPaddyPurchaseMasterTO", where);
        returnMap.put("PaddyPurchaseMasterTO", list);
        return returnMap;
    }

    private void insertData(String command) throws Exception {
        try {
            sqlMap.startTransaction();
            getAllTOs();
            processPurchaseData(command);
            insertPaddyPurchaseTransDetails(data);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getPaddyPurchaseTrans_No() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "PADDY_PURCHASE");
        String purchaseId = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return purchaseId;
    }

    private void insertPaddyPurchaseTransDetails(HashMap map) throws Exception, Exception {


        try {
            System.out.println("!@#$@#$objDrfTransactionTO:" + objPaddyPurchaseMasterTO);
            if (command != null && command.length() > 0) {
                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    String purchaseActHead = "";
                    double paddyPurchaseAmt = CommonUtil.convertObjToDouble(map.get("TOTALAMOUNT")).doubleValue();
                    System.out.println("@#$@paddyPurchaseAmt:" + paddyPurchaseAmt);
                    System.out.println("@#$@paddyTransID:" + paddyTransID);
                    HashMap txMap;
                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getPaddyPurchaseHead", paddyTransID);
                    if (acHeads != null && acHeads.size() > 0) {
                        purchaseActHead = CommonUtil.convertObjToStr(acHeads.get("PURCHASE_ACT_HEAD"));
                    }
                    TransferTrans objTransferTrans = new TransferTrans();
                    objTransferTrans.setInitiatedBranch(_branchCode);

                    objTransferTrans.setLinkBatchId(paddyTransID);
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();
                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    System.out.println("TransactionDetailsMap---->" + TransactionDetailsMap);
                    if (TransactionDetailsMap.size() > 0) {
                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            System.out.println("@#$@#$#$allowedTransDetailsTO" + allowedTransDetailsTO);
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                    if (paddyPurchaseAmt > 0.0) {
                                        txMap.put(TransferTrans.PARTICULARS, paddyTransID);
                                        txMap.put(CommonConstants.USER_ID, userID);
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);

                                        //                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                        //                                        if (objTransactionTO.getProductType().equals("GL")) {
                                        //                                            txMap.put(TransferTrans.DR_AC_HD,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        //                                        } else {
                                        //                                            txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        //                                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        //                                        }
                                        //                                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        //                                        txMap.put(TransferTrans.CR_AC_HD, purchaseActHead);

                                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                        if (objTransactionTO.getProductType().equals("GL")) {
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        } else {
                                            txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        }
                                        txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        txMap.put(TransferTrans.DR_AC_HD, purchaseActHead);

                                        txMap.put("AUTHORIZEREMARKS", "PURCHASE_ACT_HEAD");
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));

                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, paddyPurchaseAmt));
                                        //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);

                                        objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                    }
                                } else {
                                    if (paddyPurchaseAmt > 0.0) {
                                        double transAmt;
                                        TransactionTO transTO = new TransactionTO();
                                        ArrayList cashList = new ArrayList();
                                        if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                                            System.out.println("line no 465^^^^^^^");
                                            txMap = new HashMap();
                                            txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                                            txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                                            txMap.put(CommonConstants.USER_ID, userID);

                                            //                                            txMap.put("TRANS_TYPE", CommonConstants.CREDIT);

                                            txMap.put("TRANS_TYPE", CommonConstants.DEBIT);

                                            txMap.put(CommonConstants.AC_HD_ID, purchaseActHead);
                                            txMap.put("AUTHORIZEREMARKS", "PURCHASE_ACT_HEAD");
                                            txMap.put("AMOUNT", new Double(paddyPurchaseAmt));
                                            txMap.put("LINK_BATCH_ID", objTransferTrans.getLinkBatchId());

                                            System.out.println("@#$@#$@#$txMap" + txMap);
                                            cashList.add(setCashTransaction(txMap));
                                            System.out.println("cashList---------------->" + cashList);
                                            HashMap tranMap = new HashMap();
                                            tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                                            tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                                            tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                                            tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                                            tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                            tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                                            CashTransactionDAO cashDao;
                                            cashDao = new CashTransactionDAO();
                                            tranMap = cashDao.execute(tranMap, false);
                                            cashDao = null;
                                            tranMap = null;
                                        }
                                    }
                                }
                                objTransactionTO.setBatchId(paddyTransID);
                                objTransactionTO.setBatchDt(currDt);
                                objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                                objTransactionTO.setProductType(TransactionFactory.GL);
                                objTransactionTO.setBranchId(_branchCode);
                                System.out.println("objTransactionTO------------------->" + objTransactionTO);
                                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                            }
                        }
                    }
                    paddyPurchaseAmt = 0.0;
                    objTransferTrans = null;
                    transferList = null;
                    txMap = null;
                    // Code End
                } else {

                    HashMap shareAcctNoMap = new HashMap();
                    String purchaseActHead = "";
                    double paddyPurchaseAmt = CommonUtil.convertObjToDouble(map.get("TOTALAMOUNT")).doubleValue();
                    System.out.println("@#$@paddyPurchaseAmt:" + paddyPurchaseAmt);
                    System.out.println("@#$@paddyTransID:" + paddyTransID);
                    if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                        shareAcctNoMap = new HashMap();
                        shareAcctNoMap.put("LINK_BATCH_ID", paddyTransID);
                        List lst = sqlMap.executeQueryForList("getAuthBatchTxTransferTOs", shareAcctNoMap);
                        TxTransferTO txTransferTO = null;
                        double oldAmount = 0;
                        HashMap oldAmountMap = new HashMap();
                        ArrayList transferList = new ArrayList();
                        if (lst != null && lst.size() > 0) {
                            for (int j = 0; j < lst.size(); j++) {
                                txTransferTO = (TxTransferTO) lst.get(j);
                                System.out.println("#@$@#$@#$lst:" + lst);
                            }

                        } else {
                            System.out.println("In Cash Edit");
                            LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                if (allowedTransDetailsTO != null && allowedTransDetailsTO.size() > 0) {
                                    for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                        objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));

                                        HashMap tempMap = new HashMap();
                                        List cLst1 = sqlMap.executeQueryForList("getSelectShareCashTransactionTO", CommonUtil.convertObjToStr(objTransactionTO.getTransId()));
                                        if (cLst1 != null && cLst1.size() > 0) {
                                            CashTransactionTO txTransferTO1 = null;
                                            txTransferTO1 = (CashTransactionTO) cLst1.get(0);
                                            oldAmount = CommonUtil.convertObjToDouble(txTransferTO1.getAmount()).doubleValue();
                                            double newAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                            txTransferTO1.setInpAmount(new Double(newAmount));
                                            txTransferTO1.setAmount(new Double(newAmount));
                                            txTransferTO1.setCommand(command);
                                            txTransferTO1.setStatus(CommonConstants.STATUS_DELETED);
                                            txTransferTO1.setStatusDt(currDt);

                                            map.put("PRODUCTTYPE", TransactionFactory.GL);
                                            map.put("OLDAMOUNT", new Double(oldAmount));
                                            map.put("CashTransactionTO", txTransferTO1);
                                            CashTransactionDAO cashTransDAO = new CashTransactionDAO();
                                            cashTransDAO.execute(map, false);
                                        }
                                        cLst1 = null;
                                        objTransactionTO.setStatus(CommonConstants.STATUS_DELETED);
                                        objTransactionTO.setTransId(paddyTransID);
                                        objTransactionTO.setBatchId(paddyTransID);
                                        objTransactionTO.setBranchId(_branchCode);
                                        sqlMap.executeUpdate("updateRemittanceIssueTransactionTO", objTransactionTO);

                                    }

                                }
                            }
                            lst = null;
                            oldAmountMap = null;
                            transferList = null;
                            shareAcctNoMap = null;
                            txTransferTO = null;
                        }
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            //                throw new TransRollbackException(e);
            throw e;
        }
    }

    public CashTransactionTO setCashTransaction(HashMap cashMap) {
        log.info("In setCashTransaction()");
        Date curDate = (Date) currDt.clone();
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.AC_HD_ID)));
            objCashTransactionTO.setProdType(TransactionFactory.GL);
            objCashTransactionTO.setInpAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(cashMap.get("AMOUNT")));
            objCashTransactionTO.setTransType(CommonUtil.convertObjToStr(cashMap.get("TRANS_TYPE")));
            objCashTransactionTO.setBranchId(_branchCode);
            objCashTransactionTO.setStatusBy(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            //            objCashTransactionTO.setInstrumentNo1(cashTo.getInstrumentNo1());
            objCashTransactionTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            objCashTransactionTO.setInitTransId(CommonUtil.convertObjToStr(cashMap.get(CommonConstants.USER_ID)));
            objCashTransactionTO.setInitChannType("CASHIER");
            objCashTransactionTO.setParticulars("By " + CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            objCashTransactionTO.setInitiatedBranch(_branchCode);
            objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(cashMap.get("LINK_BATCH_ID")));
            objCashTransactionTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(cashMap.get("AUTHORIZEREMARKS")));
            objCashTransactionTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            System.out.println("objCashTransactionTO:" + objCashTransactionTO);
        } catch (Exception e) {
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }

    private void processPurchaseData(String command) throws Exception {

        if (deletedPurchase != null) {
            addressIterator = deletedPurchase.keySet().iterator();
            for (int i = deletedPurchase.size(); i > 0; i--) {
                key = (String) addressIterator.next();
                System.out.println("entering deleted ACADEMIC map!!" + deletedPurchase);
                objPaddyPurchaseMasterTO = (PaddyPurchaseMasterTO) deletedPurchase.get(key);
                sqlMap.executeUpdate("deletePaddyPurchaseTO", objPaddyPurchaseMasterTO);
                //                logDAO.addToLog(logTO);
            }
        }
        if (purchaseMap != null) {
            addressIterator = purchaseMap.keySet().iterator();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                paddyTransID = getPaddyPurchaseTrans_No();
            } else {
                if (data.containsKey("PURCHASEID")) {
                    paddyTransID = CommonUtil.convertObjToStr(data.get("PURCHASEID"));
                }
            }
            System.out.println("@#$@#$@#$paddyTransID" + paddyTransID);
            for (int i = purchaseMap.size(); i > 0; i--) {
                addressKey = (String) addressIterator.next();
                objPaddyPurchaseMasterTO = (PaddyPurchaseMasterTO) purchaseMap.get(addressKey);
                //if customer Id exists, set to customerAddressTO objectobj
                System.out.println("paddyPurchaseTO" + objPaddyPurchaseMasterTO);
                objPaddyPurchaseMasterTO.setStatusBy(userID);
                objPaddyPurchaseMasterTO.setStatusDt(currDt);
                if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    if (objPaddyPurchaseMasterTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        objPaddyPurchaseMasterTO.setTxtTotalAmount(CommonUtil.convertObjToStr(data.get("TOTALAMOUNT")));
                        sqlMap.executeUpdate("insertPaddyPurchaseMasterTO", objPaddyPurchaseMasterTO);
                    } else {;
                        sqlMap.executeUpdate("updatePaddyPurchaseMasterTO", objPaddyPurchaseMasterTO);
                    }

                } else if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    System.out.println("@#$#$%#$%#$%Inside Insert : " + objPaddyPurchaseMasterTO);
                    objPaddyPurchaseMasterTO.setTxtTotalAmount(CommonUtil.convertObjToStr(data.get("TOTALAMOUNT")));
                    objPaddyPurchaseMasterTO.setPurchaseId(paddyTransID);
                    sqlMap.executeUpdate("insertPaddyPurchaseMasterTO", objPaddyPurchaseMasterTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    objPaddyPurchaseMasterTO.setStatus(CommonConstants.STATUS_DELETED);
                    objPaddyPurchaseMasterTO.setStatusBy(userID);
                    objPaddyPurchaseMasterTO.setPurchaseId(paddyTransID);
                    System.out.println("!@!#$@#$@#$objPaddyPurchaseMasterTO" + objPaddyPurchaseMasterTO);
                    sqlMap.executeUpdate("deletePaddyPurchaseTO", objPaddyPurchaseMasterTO);

                }

            }

        }

    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objPaddyPurchaseMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objPaddyPurchaseMasterTO.setStatusBy(userID);
            objPaddyPurchaseMasterTO.setStatusDt(currDt);
            sqlMap.executeUpdate("updatePaddyPurchaseMasterTO", objPaddyPurchaseMasterTO);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(String command) throws Exception {
        try {
            sqlMap.startTransaction();
            getAllTOs();
            processPurchaseData(command);
            insertPaddyPurchaseTransDetails(data);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            PaddyPurchaseMasterDAO dao = new PaddyPurchaseMasterDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        data = new HashMap();
        data = map;
        System.out.println("#$%#$%#$%data" + data);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId(userID);
        objLogTO.setBranchId(_branchCode);
        objLogTO.setSelectedBranchId(_branchCode);
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(command);
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                //                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData(command);
            } else {
                throw new NoCommandException();
            }
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            System.out.println("map:" + map);
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
            authMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
            System.out.println("authMap:" + authMap);
            if (authMap != null) {
                authorize(authMap);
            }
        }


        objLogTO = null;
        objLogDAO = null;
        map.clear();
        map = null;

        destroyObjects();
        return null;
    }

    private void authorize(HashMap map) throws Exception {

        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap;
        paddyTransID = "";
        PaddyPurchaseMasterTO objTO = null;
        String linkBatchId = null;
        HashMap cashAuthMap;

        try {
            sqlMap.startTransaction();
            for (int i = 0; i < selectedList.size(); i++) {
                dataMap = (HashMap) selectedList.get(i);
                System.out.println("dataMap:" + dataMap);
                paddyTransID = CommonUtil.convertObjToStr(dataMap.get("PURCHASE_ID"));
                dataMap.put(CommonConstants.STATUS, status);
                dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                dataMap.put("CURR_DATE", currDt);
                System.out.println("status------------>" + status);
                sqlMap.executeUpdate("authorizePaddyPurchaseTransInfo", dataMap);
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    List purchaseItemList = sqlMap.executeQueryForList("getSelectPurchaseItems", dataMap);
                    if (purchaseItemList != null && purchaseItemList.size() > 0) {
                        for (int j = 0; j < purchaseItemList.size(); j++) {
                            HashMap purchaseItemMap = new HashMap();
                            purchaseItemMap = (HashMap) purchaseItemList.get(j);
                            System.out.println("#@$@#$@#$purchaseItemMap" + purchaseItemMap);
                            double kiloGrams = 0.0;
                            double bags = 0.0;
                            if (CommonUtil.convertObjToStr(purchaseItemMap.get("KILO_GRAMS")).length() > 0) {
                                kiloGrams = CommonUtil.convertObjToDouble(purchaseItemMap.get("KILO_GRAMS")).doubleValue();
                                System.out.println("@#$@#$inside KiloGrams:" + kiloGrams);
                                HashMap updateKiloGrams = new HashMap();
                                updateKiloGrams.put("QUANTITY", purchaseItemMap.get("KILO_GRAMS"));
                                updateKiloGrams.put("ITEM_CODE", purchaseItemMap.get("PRODUCT_CODE"));
                                System.out.println("@#$@#$updateKiloGrams:" + updateKiloGrams);
                                sqlMap.executeUpdate("updatePaddyItemAfterPurchaseAuth", updateKiloGrams);
                            } else if (CommonUtil.convertObjToStr(purchaseItemMap.get("BAGS")).length() > 0) {
                                bags = CommonUtil.convertObjToDouble(purchaseItemMap.get("BAGS")).doubleValue();
                                System.out.println("@#$@#$inside bags:" + bags);
                                HashMap updateKiloGrams = new HashMap();
                                updateKiloGrams.put("QUANTITY", purchaseItemMap.get("BAGS"));
                                updateKiloGrams.put("ITEM_CODE", purchaseItemMap.get("PRODUCT_CODE"));
                                System.out.println("@#$@#$updateKiloGrams:" + updateKiloGrams);
                                sqlMap.executeUpdate("updatePaddyItemAfterPurchaseAuth", updateKiloGrams);

                            }
                        }
                    }
                }

                linkBatchId = CommonUtil.convertObjToStr(dataMap.get("PURCHASE_ID"));//Transaction Batch Id
                //Separation of Authorization for Cash and Transfer
                //Call this in all places that need Authorization for Transaction
                cashAuthMap = new HashMap();
                System.out.println("@#$@zxcvzx#$dataMap:" + dataMap);
                cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                cashAuthMap.put("DAILY", "DAILY");
                System.out.println("map:" + map);
                System.out.println("cashAuthMap:" + cashAuthMap);
                TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                HashMap transMap = new HashMap();
                transMap.put("LINK_BATCH_ID", linkBatchId);
                System.out.println("transMap----------------->" + transMap);
                sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                transMap = null;
                System.out.println("paddyTransID----------------->" + paddyTransID);
                objTransactionTO = new TransactionTO();
                objTransactionTO.setBatchId(CommonUtil.convertObjToStr(paddyTransID));
                objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
                objTransactionTO.setBranchId(_branchCode);
                System.out.println("objTransactionTO----------------->" + objTransactionTO);
                sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
            }
            if (!status.equals("REJECTED")) {
            }
            selectedList = null;
            dataMap = null;
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }

    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        HashMap transMap = new HashMap();
        String where = (String) obj.get("PURCHASE_ID");
        HashMap getRemitTransMap = new HashMap();
        getRemitTransMap.put("TRANS_ID", obj.get("PURCHASE_ID"));
        getRemitTransMap.put("TRANS_DT", currDt.clone());
        getRemitTransMap.put("BRANCH_CODE", _branchCode);
        System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
        List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
//        List list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTO", where);
        if (list != null && list.size() > 0) {
            transMap.put("TRANSACTION_LIST", list);
        }
        purchaseMap = new LinkedHashMap();
        purchaseMap = getPaddyPurchaseDeatails(obj);
        if (purchaseMap != null && purchaseMap.size() > 0) {
            transMap.put("PURCHASE", purchaseMap);
        }
        return transMap;
    }

    private LinkedHashMap getPaddyPurchaseDeatails(HashMap purchaseAuthMap) throws Exception {
        List purchaseList = (List) sqlMap.executeQueryForList("getSelectPaddyPurchaseMasterTO", purchaseAuthMap);
        if (purchaseList.size() > 0) {
            purchaseMap = new LinkedHashMap();
            for (int i = purchaseList.size(), j = 0; i > 0; i--, j++) {
                purchaseMap.put(((PaddyPurchaseMasterTO) purchaseList.get(j)).getPurchaseSlNo(), purchaseList.get(j));
            }
        }
        return purchaseMap;
    }

    private void getAllTOs() throws Exception {
        if (data.containsKey("PURCHASE")) {
            purchaseMap = (LinkedHashMap) data.get("PURCHASE");
        }

        if (data.containsKey("DELETEDPURCHASE")) {
            deletedPurchase = (LinkedHashMap) data.get("DELETEDPURCHASE");
        }

    }

    private void makeDataNull() {
        data = null;
    }

    private void makeNull() {
        objPaddyPurchaseMasterTO = null;
    }

    private void getLocalityMasterData() throws Exception {
        if (data == null) {
            data = new HashMap();
        }
        whereConditions.put("CURRENT_DT", currDt);
        System.out.println("#@$@#$whereConditions:" + whereConditions);
        List list = (List) sqlMap.executeQueryForList("getSelectPaddyPurchaseMasterTO", whereConditions);
        System.out.println("@#$@#$@#4list :" + list);
        PaddyPurchaseMasterTO objPaddyPurchaseMasterTO = new PaddyPurchaseMasterTO();
        if (list.size() > 0) {
            objPaddyPurchaseMasterTO = (PaddyPurchaseMasterTO) list.get(0);
            data.put("PaddyPurchaseMasterTO", objPaddyPurchaseMasterTO);
        }

        System.out.println("#@!$@#$@#$data:" + data);

    }

    private void destroyObjects() {
        objPaddyPurchaseMasterTO = null;
    }
}
