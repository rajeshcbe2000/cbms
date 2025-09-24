/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.borrowings.disbursal;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.investments.InvestmentsTransDAO;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.borrowings.disbursal.BorrowingDisbursalTO;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * TokenConfig DAO.
 *
 */
public class BorrwingDisbursalDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private BorrowingDisbursalTO objTO;
    private TransactionDAO transactionDAO = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt;
    HashMap returnMap;
    private String authBy = "";
    private InvestmentsTransTO objInv = null;
    private String invProdType = "";
    private String invAccNo = "";
    private Double dividendAmount = 0.0;
    private Iterator processLstIterator;

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public BorrwingDisbursalDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        // HashMap data=(HashMap) map.get("DATA");
        System.out.println("where 00========" + where);
        // System.out.println("data 00========"+data.containsValue("BORROWING_DATA"));
        System.out.println("data 00" + where.containsKey("DISBURSAL_NO"));
        List list = null;
        if (where.containsKey("DISBURSAL_NO")) {
            list = (List) sqlMap.executeQueryForList("BorrwingDisbursal.getSelectBorrowingDisbursal", where);
        } else {
            list = (List) sqlMap.executeQueryForList("BorrwingDisbursal.getSelectBorrowingsList", where);
        }

        returnMap.put("BorrowingDisbursalTO", list);

        if (where.containsKey("DISBURSAL_NO")) {
            HashMap getRemitTransMap = new HashMap();
            getRemitTransMap.put("TRANS_ID", where.get("DISBURSAL_NO"));
            getRemitTransMap.put("TRANS_DT", currDt.clone());
            getRemitTransMap.put("BRANCH_CODE", _branchCode);
            System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
            list = (List) sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
            //            list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTO", where.get("DISBURSAL_NO"));
            if (list != null && list.size() > 0) {
                returnMap.put("TRANSACTION_LIST", list);
            }
        }
        System.out.println("returnMap 00========" + returnMap);
        return returnMap;
    }

    private String getDisbursalNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "BORROWING_DISBURSAL_ID");
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//reseting branch code
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            doBorrowingTransactions(map);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            HashMap finalMap = (HashMap) map.get("finalMap");
            processLstIterator = finalMap.keySet().iterator();
            String key1 = "";
            for (int i = 0; i < finalMap.size(); i++) {
                key1 = (String) processLstIterator.next();
                objTO = (BorrowingDisbursalTO) finalMap.get(key1);
                sqlMap.executeUpdate("insertBorrwingDisbursalTO", objTO);
//                System.out.println("amtBorrowedMaster in DAPO===" + objTO.getAmtBorrowedMaster());
//                System.out.println("avalbalBorrowedMaster in DAPO===" + objTO.getAvalbalBorrowedMaster());
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void doBorrowingTransactions(HashMap map) throws Exception, Exception {
        try {
            String batchIdnew = getDisbursalNo();
            ArrayList transferList = new ArrayList();
            TransferTrans objTransferTrans = null;
            ArrayList cashList = new ArrayList();
            HashMap finalMap = (HashMap) map.get("finalMap");
            processLstIterator = finalMap.keySet().iterator();
            String key1 = "";
            HashMap tranMap = new HashMap();
            String trans_type = "";
            for (int k = 0; k < finalMap.size(); k++) {
                key1 = (String) processLstIterator.next();
                objTO = (BorrowingDisbursalTO) finalMap.get(key1);
                if (objTO.getCommand() != null) {
                    if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                        objTO.setDisbursalNo(batchIdnew);
                        double amtBorrowed = CommonUtil.convertObjToDouble(objTO.getAmtBorrowed()).doubleValue();
                        HashMap txMap;
                        HashMap whereMap = new HashMap();
                        whereMap.put("BORROWING_NO", objTO.getBorrowingNo());
                        HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("Borrowings.getAcHeads", whereMap);
                        if (acHeads == null || acHeads.size() == 0) {
                            throw new TTException("Account heads not set properly...");
                        }
                        objTransferTrans = new TransferTrans();
                        objTransferTrans.setInitiatedBranch(_branchCode);
                        objTransferTrans.setLinkBatchId(objTO.getDisbursalNo());
                        txMap = new HashMap();
                        //    ArrayList transferList = new ArrayList();
                        LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                        if (TransactionDetailsMap.size() > 0) {
                            if (TransactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                                LinkedHashMap allowedTransDetailsTO = (LinkedHashMap) TransactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                                TransactionTO objTransactionTO = null;
                                for (int J = 1; J <= allowedTransDetailsTO.size(); J++) {
                                    objTransactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(J));
                                    double debitAmt = amtBorrowed;
                                    trans_type = objTransactionTO.getTransType().toString();
                                    if (objTransactionTO.getTransType().equals("TRANSFER")) {
                                        txMap.put(TransferTrans.PARTICULARS, "To " + objTO.getBorrowingNo() + " Disbursement");
                                        txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                        txMap.put("DR_INST_TYPE", "VOUCHER");
                                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                        //txMap.put(TransferTrans.CR_PROD_TYPE, objTransactionTO.getProductType()); // Commented by nithya on 14-03-2017 for 5972
                                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL); // Added by nithya for 5972
                                        if (objTransactionTO.getProductType().equals("GL")) {
                                            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                        } else {
                                            txMap.put(TransferTrans.DR_ACT_NUM, CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
                                            txMap.put(TransferTrans.DR_PROD_ID, CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                        }
                                        txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(objTransactionTO.getProductType()));
                                        //Added By Suresh
                                        if (!objTransactionTO.getProductType().equals("") && objTransactionTO.getProductType().equals("INV")) {
                                            HashMap achdMap = new HashMap();
                                            achdMap.put("INVESTMENT_PROD_ID", CommonUtil.convertObjToStr(objTransactionTO.getProductId()));
                                            List achdLst = ServerUtil.executeQuery("getSelectinvestmentAccountHead", achdMap);
                                            if (achdLst != null && achdLst.size() > 0) {
                                                achdMap = new HashMap();
                                                achdMap = (HashMap) achdLst.get(0);
                                                txMap.put(TransferTrans.DR_AC_HD, (String) achdMap.get("IINVESTMENT_AC_HD"));
                                                txMap.put("AUTHORIZEREMARKS", "IINVESTMENT_WITHDRAWAL");
                                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                                txMap.put("TRANS_MOD_TYPE", "INV");
                                                //insert Investment Details in INVESTMENT_TRANS_DETAILS Table
                                                dividendAmount = amtBorrowed;
                                                invAccNo = CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo());
//                                            double dividendAmount=0.0;
//                                            whereMap=new HashMap();
//                                            dividendAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//                                            whereMap.put("INVESTMENT_ACC_NO",CommonUtil.convertObjToStr(objTransactionTO.getDebitAcctNo()));
//                                            InvestmentsTransTO objTO= new InvestmentsTransTO();
//                                            objTO = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, dividendAmount, whereMap, map);
//                                            sqlMap.executeUpdate("insertInvestmentTransTO", objTO);
                                            } else {
                                                throw new TTException("Account heads not set properly...");
                                            }
                                        }
                                        if (objTransactionTO.getProductType().equals("OA")){
                                            txMap.put("TRANS_MOD_TYPE", "OA");
                                        }else if(objTransactionTO.getProductType().equals("AB")){
                                            txMap.put("TRANS_MOD_TYPE", "AB");
                                        }else if(objTransactionTO.getProductType().equals("SA")){
                                            txMap.put("TRANS_MOD_TYPE", "SA");
                                        }else if(objTransactionTO.getProductType().equals("TL")){
                                            txMap.put("TRANS_MOD_TYPE", "TL");
                                        }else if(objTransactionTO.getProductType().equals("AD")){
                                            txMap.put("TRANS_MOD_TYPE", "AD");
                                        }else
                                            txMap.put("TRANS_MOD_TYPE", "GL");
                                        txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, debitAmt));
                                    //    System.out.println("hhhhhaaa" + amtBorrowed);
                                        if (amtBorrowed > 0.0) {
                                            System.out.println("jjjjjjjjjyyy" + amtBorrowed);
                                            txMap.put(TransferTrans.CR_AC_HD, (String) acHeads.get("PRINCIPAL_GRP_HEAD"));
                                            txMap.put("AUTHORIZEREMARKS", "PRINCIPAL_GRP_HEAD");
                                            txMap.put("TRANS_MOD_TYPE", "BR");
                                            txMap.put("SCREEN_NAME",CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, amtBorrowed));
                                        }


                                        //vivek
                                        if ((invProdType.equals("INV"))) {
                                            HashMap batchListMap = new HashMap();
                                            HashMap batchListMap1 = new HashMap();
                                            // double dividendAmount=0.0;
                                            String batchIdval = "";
                                            whereMap = new HashMap();
                                            // dividendAmount = CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue();
//                                            System.out.println("dividendAmount()--------------->" + dividendAmount);
//                                            System.out.println("invAccNo--------------->" + invAccNo);
                                            whereMap.put("INVESTMENT_ACC_NO", invAccNo);
                                            // System.out.println("objTransactionTO.getBatchId()123--------------->"+objTransactionTO.getBatchId());
                                        //    System.out.println("objTO.getDisbursalNo(--------------->" + objTO.getDisbursalNo());

                                            batchListMap.put("BORROW", objTO.getDisbursalNo());
                                            List batchIdList = sqlMap.executeQueryForList("getBatchIdInvForBorrow", batchListMap);
                                            for (int i = 0; i < batchIdList.size(); i++) {
                                                batchListMap1 = (HashMap) batchIdList.get(0);
                                            }
                                            batchIdval = batchListMap1.get("BATCH_ID").toString();
                                          //  System.out.println("batchIdval)--------------->" + batchIdval);
                                            whereMap.put("BATCH_ID", batchIdval);
                                            InvestmentsTransTO objInv = new InvestmentsTransTO();
                                            objInv = getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, dividendAmount, whereMap, map);
                                            sqlMap.executeUpdate("insertInvestmentTransTO", objInv);
                                        }
                                        //end
                                    } else {
                                        double transAmt;
                                        //  TransactionTO transTO = new TransactionTO();
                                        CashTransactionTO objCashTO = new CashTransactionTO();
                                        // cashList = new ArrayList();
                                        if (CommonUtil.convertObjToDouble(objTransactionTO.getTransAmt()).doubleValue() > 0) {
                                            objTransactionTO.setTransAmt(amtBorrowed);
                                            objCashTO.setTransId("");
                                            objCashTO.setProdType(TransactionFactory.GL);
                                            objCashTO.setTransType(CommonConstants.CREDIT);
                                            objCashTO.setInitTransId(logTO.getUserId());
                                            objCashTO.setBranchId(_branchCode);
                                            objCashTO.setStatusBy(logTO.getUserId());
                                            objCashTO.setStatusDt(currDt);
                                            objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                            objCashTO.setParticulars("By " + objTO.getBorrowingNo() + " Disbursement");
                                            objCashTO.setInitiatedBranch(_branchCode);
                                            objCashTO.setInitChannType(CommonConstants.CASHIER);
                                            objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                            objCashTO.setAcHdId((String) acHeads.get("PRINCIPAL_GRP_HEAD"));
                                            objCashTO.setInpAmount(objTransactionTO.getTransAmt());
                                            objCashTO.setAmount(objTransactionTO.getTransAmt());
                                            objCashTO.setLinkBatchId(objTO.getDisbursalNo());
                                            objCashTO.setTransModType("BR");
                                            objCashTO.setScreenName(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
                                           // System.out.println("objCashTO^^^^^^^" + objCashTO);
                                            cashList.add(objCashTO);
                                            System.out.println("cashList---------------->" + cashList);
                                            //   HashMap tranMap=new HashMap();
                                            tranMap.put(CommonConstants.BRANCH_ID, CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                                            tranMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get("USER_ID")));
                                            tranMap.put(CommonConstants.IP_ADDR, CommonUtil.convertObjToStr(map.get("IP_ADDR")));
                                            tranMap.put(CommonConstants.MODULE, CommonUtil.convertObjToStr(map.get("MODULE")));
                                            tranMap.put(CommonConstants.SCREEN, CommonUtil.convertObjToStr(map.get("SCREEN")));
                                            tranMap.put("MODE", CommonConstants.TOSTATUS_INSERT);
                                            tranMap.put("DAILYDEPOSITTRANSTO", cashList);
                                            // CashTransactionDAO cashDao ;
                                            // cashDao = new CashTransactionDAO() ;
                                            //  tranMap=cashDao.execute(tranMap, false);
                                            //  cashDao = null ;
                                            //  tranMap = null ;
                                        }
                                    }
                                    System.out.println("sssssssssssssssssss" + objTO.getDisbursalNo());
                                    objTransactionTO.setStatus(CommonConstants.STATUS_CREATED);
                                    objTransactionTO.setBatchId(objTO.getDisbursalNo());
                                    objTransactionTO.setBatchDt(currDt);
                                    objTransactionTO.setTransId(objTransferTrans.getLinkBatchId());
                                    objTransactionTO.setBranchId(_branchCode);
                                    System.out.println("objTransactionTO------------------->" + objTransactionTO);
                                    sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", objTransactionTO);
                                }
                            }
                        }
                        amtBorrowed = 0.0;


                    } else {
                        HashMap shareAcctNoMap = new HashMap();
                        double amtBorrowed = CommonUtil.convertObjToDouble(objTO.getAmtBorrowed()).doubleValue();
                        if (objTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                        }
                    }
                }
            }
            if (trans_type.equals("CASH")) {
                CashTransactionDAO cashDao;
                cashDao = new CashTransactionDAO();
                tranMap = cashDao.execute(tranMap, false);
                cashDao = null;
                tranMap = null;
            }
            // Code End
            if (transferList.size() > 0) {
                objTransferTrans.doDebitCredit(transferList, _branchCode, false);
            }
            getTransDetails(objTO.getDisbursalNo());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    //Added By Suresh
    public InvestmentsTransTO getInvestmentsTransTO(String cmd, double intTrfAm, HashMap acctDtlMap, HashMap map) {
        HashMap whereMap = new HashMap();
        InvestmentsTransTO objgetInvestmentsTransTO = new InvestmentsTransTO();
        acctDtlMap.put("INT_PAY_ACC_NO", CommonUtil.convertObjToStr(acctDtlMap.get("INVESTMENT_ACC_NO")));
        List invList = ServerUtil.executeQuery("getInvestmentDetails", acctDtlMap);
        if (invList != null && invList.size() > 0) {
            whereMap = (HashMap) invList.get(0);
//            System.out.println("cmd>>>>>4444" + cmd);
//            System.out.println("intTrfAm>>>>>4444" + intTrfAm);
//            System.out.println("acctDtlMap>>>>>4444" + acctDtlMap);
//            System.out.println("map>>>>>4444" + map);
//            System.out.println("whereMap>>>>>4444" + whereMap);
//            System.out.println("CommonConstants.STATUS_CREATED>>>>>4444" + CommonConstants.STATUS_CREATED);
            objgetInvestmentsTransTO.setCommand(cmd);
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_CREATED);
            objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_TYPE")));
            objgetInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_ID")));
            objgetInvestmentsTransTO.setInvestment_internal_Id(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_ID")));
            objgetInvestmentsTransTO.setInvestment_Ref_No(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_REF_NO")));
            objgetInvestmentsTransTO.setInvestmentName(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_DESC")));
            objgetInvestmentsTransTO.setTransDT(currDt);
            objgetInvestmentsTransTO.setTransType("DEBIT");
            objgetInvestmentsTransTO.setTrnCode("Deposit");
            objgetInvestmentsTransTO.setAmount(new Double(0.0));
            objgetInvestmentsTransTO.setPurchaseDt(currDt);
            objgetInvestmentsTransTO.setInvestmentAmount(new Double(intTrfAm));
            objgetInvestmentsTransTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            objgetInvestmentsTransTO.setStatusDt(currDt);
            objgetInvestmentsTransTO.setDividendAmount(new Double(0));
            objgetInvestmentsTransTO.setLastIntPaidDate(currDt);
            objgetInvestmentsTransTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
            objgetInvestmentsTransTO.setPurchaseMode("SHARE_PAYMENT");
            // System.out.println("objTO.getAuthorizeBy(>>>>>>9999"+objTO.getAuthorizeBy());
            //  System.out.println("objTO.getAuthorizeStatus()>>>>>>9999"+objTO.getAuthorizeStatus());
//            System.out.println("currDt>>>>>newwwww" + currDt);
//            System.out.println("acctDtlMap111@@@sdfsd>>>>>>9999" + acctDtlMap);
//            System.out.println("acctDtlMap.get(BATCH_ID)>>>>>>99999" + acctDtlMap.get("BATCH_ID"));
//            System.out.println("cmd>>>>>>99999" + cmd);
//            System.out.println("CommonConstants.STATUS_AUTHORIZED>>>>>>99999" + CommonConstants.STATUS_AUTHORIZED);
//            System.out.println("authBy>>>>>>99999" + authBy);
//            System.out.println("objgetInvestmentsTransTO.getCommand()>>>>>4444" + objgetInvestmentsTransTO.getCommand());
            //System.out.println("objTO.getAvailBalance()>>>>>newwww>9999"+objTO.get);
            if (objgetInvestmentsTransTO.getCommand() != null && objgetInvestmentsTransTO.getCommand().equals(CommonConstants.STATUS_AUTHORIZED)) {
                //  System.out.println("CommonConstants.USER_ID(>>>>>>4444"+CommonConstants.USER_ID);
               // System.out.println("currDt>>>>>4444" + currDt);
                //  System.out.println("objTO.getAuthorizeBy(>>>>>>4444"+CommonConstants.USER_ID);
                objgetInvestmentsTransTO.setAuthorizeBy(authBy);
                // objgetInvestmentsTransTO.setInvAvailableBal(objTO.getAvailBalance());
                objgetInvestmentsTransTO.setAuthorizeDt(currDt);
                objgetInvestmentsTransTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            }
            // System.out.println("objTO.getAvailBalance()>>>>>>9999"+objTO.getAvalbalBorrowedMaster());
            // objgetInvestmentsTransTO.setInvAvailableBal(objTO.getAvalbalBorrowedMaster());
            //  objgetInvestmentsTransTO.setAuthorizeBy(objTO.getAuthorizeBy());
            // objgetInvestmentsTransTO.setAuthorizeDt(currDt);
            // objgetInvestmentsTransTO.setAuthorizeStatus(objTO.getAuthorizeStatus());
            // objgetInvestmentsTransTO.setInvAvailableBal(objTO.);
           // System.out.println("acctDtlMap.get(BATCH_ID))>>>>>4444" + acctDtlMap.get("BATCH_ID"));
            if (acctDtlMap.containsKey("BATCH_ID")) {
                objgetInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(acctDtlMap.get("BATCH_ID")));
            }
        }
        return objgetInvestmentsTransTO;
    }

    private void getTransDetails(String batchId) throws Exception {
        HashMap getTransMap = new HashMap();
        System.out.println("batch_id" + batchId);
        getTransMap.put("BATCH_ID", batchId);
        getTransMap.put("TRANS_DT", currDt);
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

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
//            System.out.println("CommonConstants.STATUS_MODIFIED ======" + CommonConstants.STATUS_MODIFIED + " objTO==" + objTO);
//            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%=============" + objTO.getDisbursalNo());
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.executeUpdate("updateBorrwingDisbursalTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
         //   System.out.println("CommonConstants.STATUS_DELETED--------" + CommonConstants.STATUS_DELETED);
            sqlMap.executeUpdate("deleteBorrwingDisbursalTO", objTO);
           // System.out.println("CommonConsobjTOobjTO-------" + objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            BorrwingDisbursalDAO dao = new BorrwingDisbursalDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = null;
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        System.out.println("*&$@#$@#$map:" + map);
        if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            objTO = (BorrowingDisbursalTO) map.get("BorrowingDisbursalTO");
            final String command = (String) map.get("COMMAND");
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map);
                returnMap.put("DISBURSAL_NO", objTO.getDisbursalNo());
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                throw new NoCommandException();
            }
        } else {
           // System.out.println("map:" + map);
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
            authMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
           // System.out.println("authMap:" + authMap);
            if (authMap != null) {
                authorize(authMap, map);
            }
        }
        destroyObjects();
        System.out.println("@#@#@# returnMap:" + returnMap);
        return returnMap;
    }

    private void authorize(HashMap map, HashMap borrowMap) throws Exception {
        String status = (String) map.get("STATUS");
        String linkBatchId = null;
        String disbursalNo = null;
        HashMap cashAuthMap;
        TransactionTO objTransactionTO = null;
        try {
            sqlMap.startTransaction();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            //System.out.println("map:" + map);
            disbursalNo = CommonUtil.convertObjToStr(map.get("DISBURSAL_NO"));
            map.put(CommonConstants.STATUS, status);
            map.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            map.put("CURR_DATE", currDt);
            //System.out.println("status------------>" + status);
            sqlMap.executeUpdate("authorizeBorrowingDisbursal", map);
            linkBatchId = CommonUtil.convertObjToStr(map.get("DISBURSAL_NO"));//Transaction Batch Id
            //Separation of Authorization for Cash and Transfer

            //Added By Suresh
            if (borrowMap.containsKey("TransactionTO")) {
                HashMap transactionDetailsMap = (LinkedHashMap) borrowMap.get("TransactionTO");
                TransactionTO transactionTO = new TransactionTO();
                LinkedHashMap allowedTransDetailsTO = new LinkedHashMap();
                if (transactionDetailsMap.size() > 0 && transactionDetailsMap.containsKey("NOT_DELETED_TRANS_TOs")) {
                    allowedTransDetailsTO = (LinkedHashMap) transactionDetailsMap.get("NOT_DELETED_TRANS_TOs");
                }
                transactionTO = (TransactionTO) allowedTransDetailsTO.get(String.valueOf(1));
                if (!CommonUtil.convertObjToStr(transactionTO.getProductType()).equals("") && transactionTO.getProductType().equals("INV")) {
                    HashMap whereMap = new HashMap();
                    String investmentBatchId = "";
                    whereMap.put("BATCH_ID", CommonUtil.convertObjToStr(transactionTO.getBatchId()));
                    whereMap.put("TRANS_DT", currDt);
                    whereMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    List transList = (List) sqlMap.executeQueryForList("getTransferDetails", whereMap);
                    if (transList != null && transList.size() > 0) {
                        whereMap = (HashMap) transList.get(0);
                        investmentBatchId = CommonUtil.convertObjToStr(whereMap.get("BATCH_ID"));
                        double dividendAmount = 0.0;
                        whereMap = new HashMap();
                        dividendAmount = CommonUtil.convertObjToDouble(transactionTO.getTransAmt()).doubleValue();
              //          System.out.println("####Investment Transaction");
                        //Authorization
                        whereMap.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(transactionTO.getDebitAcctNo()));
                        whereMap.put("BATCH_ID", investmentBatchId);
                        borrowMap.put("FROM_INTEREST_TASK", "FROM_INTEREST_TASK");
                        ArrayList arrList = new ArrayList();
                        HashMap authDataMap = new HashMap();
                        HashMap singleAuthorizeMap = new HashMap();
                        authDataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(borrowMap.get("USER_ID")));
                        authDataMap.put("BATCH_ID", investmentBatchId);
                        authBy = (CommonUtil.convertObjToStr(borrowMap.get("USER_ID")));
                        arrList.add(authDataMap);
                        singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, status);
                        singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                        singleAuthorizeMap.put("InvestmentsTransTO", getInvestmentsTransTO(status, dividendAmount, whereMap, map));
                        singleAuthorizeMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(borrowMap.get("USER_ID")));
                        borrowMap.put(CommonConstants.AUTHORIZEMAP, singleAuthorizeMap);
                        InvestmentsTransDAO investmentDAO = new InvestmentsTransDAO();
                        whereMap = investmentDAO.execute(borrowMap);
                    }
                } else {
                    cashAuthMap = new HashMap();
                    cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
                    cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    cashAuthMap.put("DAILY", "DAILY");
                    TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                }
                HashMap transMap = new HashMap();
                transMap.put("LINK_BATCH_ID", linkBatchId);
                sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                transMap = null;
            }
            objTransactionTO = new TransactionTO();
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(disbursalNo));
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
            objTransactionTO.setBranchId(_branchCode);
            sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
            String key1 = "";

            if (borrowMap.containsKey("finalMap")) {
                HashMap finalMap = (HashMap) borrowMap.get("finalMap");
                processLstIterator = finalMap.keySet().iterator();
                //System.out.println("finalMap0" + finalMap);
                for (int k = 0; k < finalMap.size(); k++) {
                    key1 = (String) processLstIterator.next();
                    objTO = (BorrowingDisbursalTO) finalMap.get(key1);
                    if(CommonUtil.convertObjToStr(status).equals("AUTHORIZED")){
                    	sqlMap.executeUpdate("upateMultidisbursalBorrowerMaster", objTO);
                    }
                }
            }
            map = null;
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
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
