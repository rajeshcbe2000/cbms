/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserDAO.java
 *
 * Created on September 08, 2003, 1:00 PM
 *
 * Modified on April 16, 2004, 10:42 AM
 *
 */
package com.see.truetransact.serverside.sysadmin.fixedassets;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetsIndividualTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.CommonUtil;

import java.util.LinkedHashMap;//trans details
import com.see.truetransact.transferobject.borrowings.disbursal.BorrowingDisbursalTO; //trans details
import com.see.truetransact.serverside.borrowings.disbursal.BorrwingDisbursalDAO; //trans details
//trans details
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransaction;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.borrowings.disbursal.BorrowingDisbursalTO;
import java.util.Date;
//end...

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * This is used for User Data Access.
 *
 * @author swaroop
 *
 *
 */
public class FixedAssetsIndividualDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private FixedAssetsIndividualTO ObjFidTO;
    private List list;
    // For Maintaining Logs...
    private LogDAO logDAO;
    private LogTO logTO;
    private LinkedHashMap deletedTableValues = null;
    private LinkedHashMap tableDetails = null;
    private String command = "";
    private Date currDt = null;//trans details
    HashMap returnMap; //trans details
    private final static Logger log = Logger.getLogger(FixedAssetsIndividualDAO.class);

    /**
     * Creates a new instance of roleDAO
     */
    public FixedAssetsIndividualDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE); //trans details
        List list = (List) sqlMap.executeQueryForList("getSelectFixedIndividualTO", map);
        returnMap.put("FixedIndividualTO", list);

        //trans details
        if (where.containsKey("FIXED_INDIVIDUAL_ID")) {
            HashMap getRemitTransMap = new HashMap();
            getRemitTransMap.put("TRANS_ID", where.get("FIXED_INDIVIDUAL_ID"));
            getRemitTransMap.put("TRANS_DT", currDt.clone());
            getRemitTransMap.put("BRANCH_CODE", _branchCode);
            System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
            list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
            //            list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", where.get("REP_INT_CLS_NO"));
            if (list != null && list.size() > 0) {
                returnMap.put("TRANSACTION_LIST", list);
            }
        }
        //end..

        if (map.get("VIEW_TYPE").equals("Authorize")) {
            list = (List) sqlMap.executeQueryForList("getSelectFixedIndividualDetailsAuth", map);
        } else {
            list = (List) sqlMap.executeQueryForList("getSelectFixedIndividualDetails", map);
        }
        if (list != null && list.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@list" + list);
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                String st = ((FixedAssetsIndividualTO) list.get(j)).getSlNo();
                ParMap.put(((FixedAssetsIndividualTO) list.get(j)).getSlNo(), list.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("FixedIndividualDetailsTO", ParMap);
        }
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            ObjFidTO.setAssetIndID(getFixedIndividualID());
            doPurchaseTransactions(map); //trans details
            sqlMap.executeUpdate("insertFixedIndividualTO", ObjFidTO);
            logTO.setData(ObjFidTO.toString());
            logTO.setPrimaryKey(ObjFidTO.getKeyData());
            logTO.setStatus(ObjFidTO.getStatus());
            insertTableValues();
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private String getFixedIndividualID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "FIXED_INDIVIDUAL_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getFixedIndividualDetail() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "FIXED_INDIVIDUAL_DETAIL");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateFixedIndividualTO", ObjFidTO);
            logTO.setData(ObjFidTO.toString());
            logTO.setPrimaryKey(ObjFidTO.getKeyData());
            logTO.setStatus(ObjFidTO.getStatus());
            updateTableValues();
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    //trans details
    private void doPurchaseTransactions(HashMap map) throws Exception, Exception {


        try {
            System.out.println("mapmapmapINNNNNNNNN to :" + map);
            if (ObjFidTO.getCommand() != null) {
                if (ObjFidTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    System.out.println("!@#!@#@# !ID GENERATED:" + ObjFidTO.getAssetIndID());
                    double amtBorrowed = CommonUtil.convertObjToDouble(ObjFidTO.getAmount()).doubleValue();
                    System.out.println("@#$ amtBorrowed :" + amtBorrowed);
                    HashMap txMap;
                    HashMap whereMap = new HashMap();
                    whereMap.put("ASSET_TYPE", ObjFidTO.getAssetType());
                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getPurchaseAcHdData", whereMap);
                    String ac_head = CommonUtil.convertObjToStr(acHeads.get("PURCHASE_DEBIT"));
                    if (ac_head == null || ac_head.equals("")) {
                        throw new TTException("Account heads not set properly...");
                    }
                    TransferTrans objTransferTrans = new TransferTrans();

                    objTransferTrans.setInitiatedBranch(_branchCode);

                    objTransferTrans.setLinkBatchId(ObjFidTO.getAssetIndID());
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();

                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    System.out.println("TransactionDetailsMap---->" + TransactionDetailsMap);
                    if (TransactionDetailsMap.size() > 0) {
                        if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                            LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                            TransactionTO objTransactionTO = null;
                            System.out.println("@#$@#$#$allowedTransDetailsTO" + allowedTransDetailsTO);
                            for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                //                                   System.out.println("objTransactionTO---->"+objTransactionTO);


                                double debitAmt = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
                                if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                    //                                            txMap.put(TransferTrans.DR_AC_HD, (String)acHeads.get("SHARE_ACHD"));
                                    txMap.put(TransferTrans.PARTICULARS, "To " + ObjFidTO.getAssetIndID() + " Purchase");
                                    txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    //txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, null);
                                    txMap.put("DR_INST_TYPE", "VOUCHER");
                                    txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                    if (objTransactionTO.getProductType().equals("GL")) {
                                        txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));

                                    } else {
                                        txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                    }
                                    txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                    transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                                    //                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, shareAmt));
                                    //                                            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                    if (amtBorrowed > 0.0) {
                                        txMap.put(TransferTrans.CR_AC_HD, ac_head);
                                        txMap.put("AUTHORIZEREMARKS", "PURCHASE_DEBIT");
                                        //                                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, shareAmt));
                                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, amtBorrowed));
                                        //                                                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                    }
                                    objTransferTrans.doDebitCredit(transferList, _branchCode, false);
                                } else {
                                    double transAmt;
                                    //  TransactionTO transTO = new TransactionTO();
                                    CashTransactionTO objCashTO = new CashTransactionTO();
                                    ArrayList cashList = new ArrayList();
                                    if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                                        objCashTO.setTransId("");
                                        objCashTO.setProdType(TransactionFactory.GL);
                                        objCashTO.setTransType(CommonConstants.CREDIT);
                                        objCashTO.setInitTransId(logTO.getUserId());
                                        objCashTO.setBranchId(_branchCode);
                                        objCashTO.setStatusBy(logTO.getUserId());
                                        objCashTO.setStatusDt(currDt);
                                        //                                        objCashTO.setTokenNo(CommonUtil.convertObjToStr(paramMap.get("TOKEN_NO")));
                                        //objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, null);
                                        objCashTO.setParticulars("By " + ObjFidTO.getAssetIndID() + " Purchase");
                                        objCashTO.setInitiatedBranch(_branchCode);
                                        objCashTO.setInitChannType(CommonConstants.CASHIER);
                                        objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        objCashTO.setAcHdId(ac_head);
                                        objCashTO.setInpAmount(objTransactionTO.getTransAmt());
                                        objCashTO.setAmount(objTransactionTO.getTransAmt());
                                        objCashTO.setLinkBatchId(ObjFidTO.getAssetIndID());

                                        System.out.println("objCashTO^^^^^^^" + objCashTO);
                                        cashList.add(objCashTO);
                                        System.out.println("BRANCH_CODE---------------->" + map.get("BRANCH_CODE"));
                                        HashMap tranMap = new HashMap();
                                        System.out.println("objCashTO^^^^^^^" + objCashTO);
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
                                /*
                                 * else {
                                 *
                                 * // txMap.put(TransferTrans.DR_AC_HD,
                                 * (String)acHeads.get("SHARE_ACHD"));
                                 * txMap.put(CashTransaction.PARTICULARS, "To
                                 * "+objTO.getBorrowingNo()+" Disbursement");
                                 * txMap.put(CommonConstants.USER_ID,
                                 * logTO.getUserId());
                                 * txMap.put("DR_INSTRUMENT_2","DEPOSIT_TRANS");
                                 * txMap.put("DR_INST_TYPE","WITHDRAW_SLIP");
                                 * txMap.put(TransferTrans.DR_BRANCH,
                                 * _branchCode);
                                 * txMap.put(TransferTrans.CR_BRANCH,
                                 * _branchCode);
                                 * txMap.put(TransferTrans.CR_PROD_TYPE,
                                 * TransactionFactory.GL); if
                                 * (objTransactionTO.getProductType().equals("GL"))
                                 * {
                                 * txMap.put(TransferTrans.DR_AC_HD,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                 *
                                 * } else {
                                 * txMap.put(TransferTrans.DR_ACT_NUM,CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                 * txMap.put(TransferTrans.DR_PROD_ID,
                                 * CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                 * } txMap.put(TransferTrans.DR_PROD_TYPE,
                                 * CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                 * transferList.add(objTransferTrans.getDebitTransferTO(txMap,
                                 * debitAmt)); //
                                 * transferList.add(objTransferTrans.getCreditTransferTO(txMap,
                                 * shareAmt)); //
                                 * objTransferTrans.doDebitCredit(transferList,
                                 * _branchCode, false); if(amtBorrowed>0.0){
                                 * txMap.put(TransferTrans.CR_AC_HD,
                                 * (String)acHeads.get("PRINCIPAL_GRP_HEAD"));
                                 * txMap.put("AUTHORIZEREMARKS","PRINCIPAL_GRP_HEAD");
                                 * //
                                 * transferList.add(objTransferTrans.getDebitTransferTO(txMap,
                                 * shareAmt));
                                 * transferList.add(objTransferTrans.getCreditTransferTO(txMap,
                                 * amtBorrowed)); //
                                 * objTransferTrans.doDebitCredit(transferList,
                                 * _branchCode, false); }
                                 * objTransferTrans.doDebitCredit(transferList,
                                 * _branchCode, false);
                                }
                                 */
                                //End cash
                                objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                objTransactionTO.setBatchId(ObjFidTO.getAssetIndID());
                                objTransactionTO.setBatchDt(currDt);
                                objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                                objTransactionTO.setBranchId(_branchCode);
                                System.out.println("objTransactionTO------------------->" + objTransactionTO);
                                sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                            }
                        }
                    }
                    amtBorrowed = 0.0;
                    objTransferTrans = null;
                    transferList = null;
                    txMap = null;
                    // Code End
                    getTransDetails(ObjFidTO.getAssetIndID());
                } else {
                    HashMap shareAcctNoMap = new HashMap();
                    //                    shareAcctNoMap = (HashMap)sqlMap.executeQueryForObject("transferResolvedShare", shareAcctNoMap);
                    //                    sqlMap.executeUpdate("updateShareAcctDetailsTO", shareAcctDetailsTO);
                    double amtBorrowed = CommonUtil.convertObjToDouble(ObjFidTO.getAmount()).doubleValue();
                    if (ObjFidTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
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
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, _branchCode);
        //        System.out.println("########getTransferTransBatchID returnMap depCloseAmt:"+resultMap);
        returnMap = new HashMap();
        List transList = (List) sqlMap.executeQueryForList("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            returnMap.put("TRANSFER_TRANS_LIST", transList);
            System.out.println("########transfrretrnmap>>>>>>>>>>>///" + returnMap);
            System.out.println("########transfrlist>>>>>>>>>>>///" + transList);
        }
        List cashList = (List) sqlMap.executeQueryForList("getCashDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
            System.out.println("########cashretrnmap>>>>>>>>>>>///" + returnMap);
            System.out.println("########cashlist>>>>>>>>>>>///" + cashList);

        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    //end...
    private void deleteData() throws Exception {
        try {
            System.out.println("delete #$#$#$" + ObjFidTO);
            if (ObjFidTO != null) {
                sqlMap.startTransaction();
                String Statusby = ObjFidTO.getStatusBy();
                System.out.println("delete #$#$#$" + ObjFidTO);
                ObjFidTO.setStatusDt(currDt);
                ObjFidTO.setAssetTabStatus(CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("deleteFixedIndividualTO", ObjFidTO);
                sqlMap.executeUpdate("deleteFixedIndividualDetailsTO", ObjFidTO);
                logTO.setData(ObjFidTO.toString());
                logTO.setPrimaryKey(ObjFidTO.getKeyData());
                logTO.setStatus(ObjFidTO.getStatus());
                logDAO.addToLog(logTO);
                sqlMap.commitTransaction();
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        try {
            currDt = ServerUtil.getCurrentDate(_branchCode);
            System.out.println("@@@@@@@ExecuteMap" + map);
            //  HashMap returnMap = null;
            //  _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
            logDAO = new LogDAO();
            logTO = new LogTO();
            logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            logTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
            logDAO.addToLog(logTO);
            deletedTableValues = (LinkedHashMap) map.get("deletedFixedAssetsIndividualTableDetails");
            tableDetails = (LinkedHashMap) map.get("FixedAssetsIndividualTableDetails");
            ObjFidTO = (FixedAssetsIndividualTO) map.get("FixedAssetsIndividual");
            command = CommonUtil.convertObjToStr(map.get("COMMAND"));
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map);
                //  returnMap = new HashMap(); //trans details
                returnMap.put("FIXED_INDIVIDUAL_ID", ObjFidTO.getAssetIndID());
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData(map);
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                if (authMap != null) {
                    authorize(authMap);
                }
            }
            map = null;
        } catch (Exception ex) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(ex);
        }
        destroyObjects();
        System.out.println("returnMap in execute>>>>>" + returnMap);
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        ObjFidTO = null;
        logTO = null;
        logDAO = null;
        command = "";
    }

    private void insertTableValues() throws Exception {

        if (tableDetails != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                FixedAssetsIndividualTO objFixedAssetsIndividualTO = (FixedAssetsIndividualTO) tableDetails.get(addList.get(i));
                String fdIndId = getFixedIndividualDetail();
                System.out.println("######## fdIndId :" + fdIndId);
                objFixedAssetsIndividualTO.setFaIndId(fdIndId);
                objFixedAssetsIndividualTO.setAssetIndID(ObjFidTO.getAssetIndID());
                sqlMap.executeUpdate("insertFixedIndividualDetailsTO", objFixedAssetsIndividualTO);
                logTO.setData(objFixedAssetsIndividualTO.toString());
                logTO.setPrimaryKey(objFixedAssetsIndividualTO.getKeyData());
                logTO.setStatus(objFixedAssetsIndividualTO.getCommand());
                logDAO.addToLog(logTO);
                objFixedAssetsIndividualTO = null;
            }
        }
    }

    private void updateTableValues() throws Exception {

        if (deletedTableValues != null) {
            ArrayList addList = new ArrayList(deletedTableValues.keySet());
            for (int i = 0; i < addList.size(); i++) {
                FixedAssetsIndividualTO objFixedAssetsIndividualTO = (FixedAssetsIndividualTO) deletedTableValues.get(addList.get(i));
                objFixedAssetsIndividualTO.setAssetTabStatus(CommonConstants.STATUS_DELETED);
                objFixedAssetsIndividualTO.setAssetIndID(ObjFidTO.getAssetIndID());
                sqlMap.executeUpdate("deleteFixedIndividualDetailsSlNoTO", objFixedAssetsIndividualTO);
                logTO.setData(objFixedAssetsIndividualTO.toString());
                logTO.setPrimaryKey(objFixedAssetsIndividualTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
                objFixedAssetsIndividualTO = null;
            }
        }

        if (tableDetails != null) {
            List lst = null;
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                FixedAssetsIndividualTO objFixedAssetsIndividualTO = (FixedAssetsIndividualTO) tableDetails.get(addList.get(i));
                logTO.setData(objFixedAssetsIndividualTO.toString());
                logTO.setPrimaryKey(objFixedAssetsIndividualTO.getKeyData());
                logTO.setStatus(command);
                objFixedAssetsIndividualTO.setAssetIndID(ObjFidTO.getAssetIndID());
                lst = sqlMap.executeQueryForList("countNoOfDetails", objFixedAssetsIndividualTO);
                int a = CommonUtil.convertObjToInt(lst.get(0));
                if (a <= 0) {
                    sqlMap.executeUpdate("insertFixedIndividualDetailsTO", objFixedAssetsIndividualTO);
                } else {
                    sqlMap.executeUpdate("updateFixedIndividualDetailsTO", objFixedAssetsIndividualTO);
                }

                logDAO.addToLog(logTO);
            }
        }
    }

    private void deleteTableValues() throws Exception {

        if (tableDetails != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                FixedAssetsIndividualTO objFixedAssetsIndividualTO = (FixedAssetsIndividualTO) tableDetails.get(addList.get(i));
                objFixedAssetsIndividualTO.setAssetIndID(ObjFidTO.getAssetIndID());
                objFixedAssetsIndividualTO.setAssetTabStatus("DELETED");
                sqlMap.executeUpdate("deleteEmpDetailsTO", objFixedAssetsIndividualTO);
                logTO.setData(objFixedAssetsIndividualTO.toString());
                logTO.setPrimaryKey(objFixedAssetsIndividualTO.getKeyData());
                logTO.setStatus(objFixedAssetsIndividualTO.getCommand());
                logDAO.addToLog(logTO);
                objFixedAssetsIndividualTO = null;
            }
        }
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("authorizeFixedIndividual", AuthMap);
            if (deletedTableValues != null) {
                System.out.println("Inside Authorize Delete");
                ArrayList addList = new ArrayList(deletedTableValues.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    FixedAssetsIndividualTO objFixedAssetsIndividualTO = (FixedAssetsIndividualTO) deletedTableValues.get(addList.get(i));
                    AuthMap.put("SL_NO", objFixedAssetsIndividualTO.getSlNo());
                    sqlMap.executeUpdate("authorizeFixedIndividualDetails", AuthMap);
                    logTO.setData(objFixedAssetsIndividualTO.toString());
                    logTO.setPrimaryKey(objFixedAssetsIndividualTO.getKeyData());
                    logDAO.addToLog(logTO);
                }
            }
            if (tableDetails != null) {
                System.out.println("Inside Authorize NonDelete");
                List lst = null;
                ArrayList addList = new ArrayList(tableDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    FixedAssetsIndividualTO objFadTO = (FixedAssetsIndividualTO) tableDetails.get(addList.get(i));
                    System.out.println("DATA CHECK" + objFadTO);
                    AuthMap.put("SL_NO", objFadTO.getSlNo());
                    sqlMap.executeUpdate("authorizeFixedIndividualDetails", AuthMap);
                    logTO.setData(objFadTO.toString());
                    logTO.setPrimaryKey(objFadTO.getKeyData());
                    logDAO.addToLog(logTO);
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
}
