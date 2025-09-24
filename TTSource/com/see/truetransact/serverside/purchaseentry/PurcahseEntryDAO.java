/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BorrwingDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.purchaseentry;

import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import java.util.LinkedHashMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransaction;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.purchaseentry.PurchaseEntryTO;
import java.util.HashMap;
import java.util.Date;

public class PurcahseEntryDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private PurchaseEntryTO objTO;
    private TransactionDAO transactionDAO = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt;
    HashMap returnMap = null;

    public PurcahseEntryDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        where.put("TRADEEXEPENSE_ID", map.get("TRADEEXEPENSE_ID"));
        if(where.get("PURCHASE_ENTRY_ID")!=null){
	        List list = (List) sqlMap.executeQueryForList("PurchaseEntry.getSelectPurchaseEntry", where);
	        returnMap.put("PurchaseEntryTO", list);
	
	        if (where.containsKey("PURCHASE_ENTRY_ID")) {
	            getTransDetails(where.get("PURCHASE_ENTRY_ID").toString());
	        }
        }else {
            List list = (List) sqlMap.executeQueryForList("PurchaseEntry.getSelectTradeExpense", where);
        	returnMap.put("PurchaseEntryTO", list);

	        if (where.containsKey("TRADEEXEPENSE_ID")) {
	            getTransDetails(where.get("TRADEEXEPENSE_ID").toString());
	        }
        }
        return returnMap;
    }
    private String getTradeExpenseID()   throws Exception {              
        final HashMap where = new HashMap();
        HashMap map = generateID();
        return (String) map.get(CommonConstants.DATA);

        
    }
    private String getPurchaseEntryID() throws Exception {
        final HashMap where = new HashMap();
        HashMap map = generateID();
        return (String) map.get(CommonConstants.DATA);

    }

    public HashMap generateID() {

        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();


            if (objTO.getTransMode().equals("Purchase")) {
                where.put("ID_KEY", "PURCHASE_ENTRY_ID");

            } else if (objTO.getTransMode().equals("Sundry Creditors")) {
                where.put("ID_KEY", "SUNDRY_ENTRY_ID");
            } else if (objTO.getGrandTot() > 0) {
                where.put("ID_KEY", "TRADE_EXPENSE_ID");
            }
            List list = null;


            sqlMap.executeUpdate("updateIDGenerated", where);
            list = (List) sqlMap.executeQueryForList(mapName, where);


            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";


                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }


                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = Integer.parseInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));


                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            //HashMap tabMap = (HashMap) map.get("tab_no");
            List balance = (List) map.get("dataList");
            int tabNo = (CommonUtil.convertObjToInt(map.get("tab_no")));
            double totAmount = (CommonUtil.convertObjToDouble(map.get("totAmount")));
            String tradeNarration = (CommonUtil.convertObjToStr(map.get("tradeNarration")));
            if (tabNo == 1) {
                for (int i = 0; i < balance.size(); i++) {
                List tabList = (List) balance.get(i);
                objTO.setFromDate(DateUtil.getDateMMDDYYYY(tabList.get(0).toString()));
                objTO.setFromWeight(CommonUtil.convertObjToDouble(tabList.get(1)));
                objTO.setToWeight(CommonUtil.convertObjToDouble(tabList.get(2)));
                objTO.setAmount(CommonUtil.convertObjToDouble(tabList.get(3)));
                sqlMap.executeUpdate("insertTradeExpenseTO", objTO);
                }
                return;
            } else if (totAmount > 0) {
                 System.out.println("in tradeexpenseto");
                sqlMap.startTransaction();
                objTO.setTradeId(getTradeExpenseID());
//                System.out.println("getPurchaseEntryID()"+getTradeExpenseID());
                objTO.setGrandTot(totAmount);
                objTO.setTradeNarration(tradeNarration);
                objTO.setTrans_mode(CommonConstants.GL_TRANSMODE_TYPE);
                objTO.setStatus(CommonConstants.STATUS_CREATED);
                objTO.setStatus_By(logTO.getUserId());
                objTO.setStatusDt((Date)currDt.clone());
                objTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
                objTO.setAuthorizeBy(logTO.getUserId());
                doPurchaseEntryTransactions(map);
                sqlMap.executeUpdate("insertTradeExpenseentryTO", objTO);
                sqlMap.commitTransaction();
                return;
            }


            sqlMap.startTransaction();
            objTO.setPurId(getPurchaseEntryID());
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            objTO.setStatus_By(logTO.getUserId());
            doPurchaseEntryTransactions(map);
            sqlMap.executeUpdate("insertPurchaseEntryTO", objTO);
            sqlMap.commitTransaction();

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
         returnMap.put("purid", objTO.getPurId());
         returnMap.put("tradeid", objTO.getTradeId());
    }

    private void transactionInd(HashMap txMap, String acHead, double amt, String purHead) {
        try {
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            objTransferTrans.setLinkBatchId(objTO.getPurId());
            ArrayList transferList = new ArrayList();
            String partic = "";
            if (objTO.getChequeNo() != null) {
                partic = objTO.getNarration() + " " + objTO.getChequeNo();
            } else {
                partic = objTO.getNarration();
            }
            txMap.put(TransferTrans.PARTICULARS, partic);
            System.out.println("objTO.getPurId()--" + objTO.getPurId());
            System.out.println("logTO.getUserId()-" + logTO.getUserId());
            txMap.put(CommonConstants.USER_ID, logTO.getUserId());

            txMap.put("DR_INSTRUMENT_2", "DEPOSIT_TRANS");
            txMap.put("DR_INST_TYPE", "WITHDRAW_SLIP");
            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
            if (!txMap.containsKey("CR_PROD_TYPE")) {
                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            }
            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(purHead));
           //Added by Anju
           if (!txMap.containsKey("DR_PROD_TYPE")) {
            //txMap.put(TransferTrans.DR_PROD_TYPE, CommonUtil.convertObjToStr(TransactionFactory.GL));
            txMap.put(TransferTrans.DR_PROD_TYPE,TransactionFactory.SUSPENSE);
            txMap.put(TransferTrans.DR_ACT_NUM, objTO.getSupActnum());
            }
            transferList.add(objTransferTrans.getDebitTransferTO(txMap, amt));
            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acHead));
            txMap.put("AUTHORIZEREMARKS", "PURCHASE_AC_HEAD");
            transferList.add(objTransferTrans.getCreditTransferTO(txMap, amt));
            objTransferTrans.doDebitCredit(transferList, _branchCode, false);
            objTransferTrans = null;
            transferList = null;
           // txMap = null;
        } catch (Exception e) {
            System.out.println("Exe_---" + e);
        }
    }

    private void doPurchaseEntryTransactions(HashMap map) throws Exception, Exception {
         try {
            System.out.println("!@#$@# Borrowings to :" + objTO);
            if (objTO.getCommand() != null) {
                if (objTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    System.out.println("!@#!@#@# !ID GENERATED:" + objTO.getPurId());
                    double purAmt = CommonUtil.convertObjToDouble(objTO.getPurAmount());
                    double purComm = CommonUtil.convertObjToDouble(objTO.getPurComm());
                    double purRet = CommonUtil.convertObjToDouble(objTO.getPurchaseRet());
                    double sundry = CommonUtil.convertObjToDouble(objTO.getSundry());
                    double investAchd = CommonUtil.convertObjToDouble(objTO.getInvestAcHead());
                    double balAmt = CommonUtil.convertObjToDouble(objTO.getCashAmount());
                    double grandTot = CommonUtil.convertObjToDouble(objTO.getGrandTot());
                    String tradeNarration = CommonUtil.convertObjToStr(objTO.getTradeNarration());
                    HashMap txMap = new HashMap();
                    HashMap whereMap = new HashMap();
                    HashMap acHeads = null;
                    acHeads = (HashMap) sqlMap.executeQueryForObject("PurchaseEntry.getAcHeads", whereMap);
                    System.out.println("acHeads acHeads acHeads " + acHeads);
                    if (acHeads == null || acHeads.isEmpty()) {
                        throw new TTException("Account heads not set properly...");
                    }
                    if (purComm > 0) {
                       // txMap = new HashMap();
                        if (objTO.getTransMode() != null && objTO.getTransMode().equals("Purchase")) {
                            transactionInd(txMap, (String) acHeads.get("PURCHASE_COMMISSION_AC_HEAD"), purComm, (String) acHeads.get("PURCHASE_AC_HEAD"));
                        } else if (objTO.getTransMode() != null && objTO.getTransMode().equals("Sundry Creditors")) {
                            transactionInd(txMap, (String) acHeads.get("PURCHASE_COMMISSION_AC_HEAD"), purComm, (String) acHeads.get("SUNDRY_CREDITORS_AC_HEAD"));
                        }
                    }
                    if (purRet > 0) {
                       // txMap = new HashMap();
                        if (objTO.getTransMode() != null && objTO.getTransMode().equals("Purchase")) {
                            transactionInd(txMap, (String) acHeads.get("PURCHASE_RETURN_AC_HEAD"), purRet, (String) acHeads.get("PURCHASE_AC_HEAD"));
                        } else if (objTO.getTransMode() != null && objTO.getTransMode().equals("Sundry Creditors")) {
                            transactionInd(txMap, (String) acHeads.get("PURCHASE_RETURN_AC_HEAD"), purRet, (String) acHeads.get("SUNDRY_CREDITORS_AC_HEAD"));
                        }
                    }

                    if (sundry > 0) {
                        if (objTO.getTransType().equals("Transfer") && objTO.getIsSundry().equals("Y")) {
                            objTO.setTrans_mode("Sundry Credit");
                            txMap = new HashMap();
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.SUSPENSE);
                            txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTO.getActnum()));
                            System.out.println("sdgfdfgrgfgrf grfgfugg rgfreanjuuuuuuu"+CommonUtil.convertObjToStr(objTO.getActnum()));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                            txMap.put(TransferTrans.DR_ACT_NUM, objTO.getSupActnum());
                            transactionInd(txMap, (String) acHeads.get("SUNDRY_CREDITORS_AC_HEAD"), sundry, (String) acHeads.get("PURCHASE_AC_HEAD"));
                        }
                    }
                    if (investAchd > 0) {
                        if (objTO.getTransType().equals("Transfer") && objTO.getIsInvestment().equals("Y")) {
                            objTO.setTrans_mode("Investment");
                            txMap = new HashMap();
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.OTHERBANKACTS);
                            txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(objTO.getActnum()));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                            txMap.put(TransferTrans.DR_ACT_NUM, objTO.getSupActnum());
                            System.out.println(" objTO.getSupActnum() objTO.getSupActnum()"+ objTO.getSupActnum());
                            HashMap acHeads1 = new HashMap();
                            acHeads1.put("INVESTMENT_ACC_NO", objTO.getActnum());
                            acHeads1 = (HashMap) sqlMap.executeQueryForObject("getSelectOthrBankAcHd", acHeads1);
                             if (acHeads1 == null || acHeads1.isEmpty()) {
                                throw new TTException("Account heads not set properly...");
                             }
                            //transactionInd(txMap, (String) acHeads1.get("PRINCIPAL_AC_HD"), investAchd, (String) acHeads.get("SUNDRY_CREDITORS_AC_HEAD"));
                            if (objTO.getTransMode() != null && objTO.getTransMode().equals("Purchase")) {
                            transactionInd(txMap, (String) acHeads1.get("PRINCIPAL_AC_HD"), investAchd, (String) acHeads.get("PURCHASE_AC_HEAD"));
                            } else if (objTO.getTransMode() != null && objTO.getTransMode().equals("Sundry Creditors")) {
                              transactionInd(txMap, (String) acHeads1.get("PRINCIPAL_AC_HD"), investAchd, (String) acHeads.get("SUNDRY_CREDITORS_AC_HEAD"));
                            }
                        }
                    }
                    if (balAmt > 0) {
                        CashTransactionTO objCashTO = new CashTransactionTO();
                        ArrayList cashList = new ArrayList();
                        String grpHead = "";
                        if (objTO.getTransMode().equals("Purchase")) {
                            grpHead = (String) acHeads.get("PURCHASE_AC_HEAD");
                        } else {
                            grpHead = (String) acHeads.get("SUNDRY_CREDITORS_AC_HEAD");
                        }
                        objCashTO.setTransId("");
                        objCashTO.setProdType(TransactionFactory.SUSPENSE);
                        objCashTO.setActNum(objTO.getSupActnum());
                        objCashTO.setTransType(CommonConstants.DEBIT);
                        objCashTO.setInitTransId(logTO.getUserId());
                        objCashTO.setBranchId(_branchCode);
                        objCashTO.setStatusBy(logTO.getUserId());
                        objCashTO.setStatusDt(currDt);
                        objCashTO.setInstrumentNo1("ENTERED_AMOUNT");
                        objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        String partic = "";
                        if (objTO.getChequeNo() != null) {
                            partic = objTO.getNarration() + " " + objTO.getChequeNo();
                        } else {
                            partic = objTO.getNarration();
                        }
                        objCashTO.setParticulars(partic);
                        objCashTO.setInitiatedBranch(_branchCode);
                        objCashTO.setInitChannType(CommonConstants.CASHIER);
                        objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        objCashTO.setAcHdId(grpHead);
                        objCashTO.setInpAmount(balAmt);
                        objCashTO.setAmount(balAmt);
                        objCashTO.setLinkBatchId(objTO.getPurId());
                        txMap = new HashMap();
                        txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                        txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.SUSPENSE);
                        txMap.put(CommonConstants.ACT_NUM, objTO.getSupActnum());
                        txMap.put("TRANS_TYPE", CommonConstants.DEBIT);
                        txMap.put(CommonConstants.AC_HD_ID, grpHead);
                        txMap.put("AUTHORIZEREMARKS", "PURCHASE_AC_HEAD");
                        txMap.put("AMOUNT", new Double(objTO.getCashAmount()));
                        txMap.put("LINK_BATCH_ID", objTO.getPurId());
                        cashList.add(/*
                                 * setCashTransaction(txMap)
                                 */objCashTO);
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
                        System.out.println("tranMap BEFOREE EXECUTE---------------->" + tranMap);
                        tranMap = cashDao.execute(tranMap, false);
                        System.out.println("tranMap AFTER EXECUTE---------------->" + tranMap);
                        cashDao = null;
                        tranMap = null;
                    }
                    if (grandTot > 0) {
                        CashTransactionTO objCashTO = new CashTransactionTO();
                        ArrayList cashList = new ArrayList();
                        String grpHead = "";
                        grpHead = (String) acHeads.get("TRADE_EXPENSE_ACHD");
                        System.out.println("grpHead  ------" + grpHead);
                        objCashTO.setTransId("");
                        objCashTO.setProdType(TransactionFactory.GL);
                        objCashTO.setActNum("");
                        objCashTO.setTransType(CommonConstants.DEBIT);
                        objCashTO.setInitTransId(logTO.getUserId());
                        objCashTO.setBranchId(_branchCode);
                        objCashTO.setStatusBy(logTO.getUserId());
                        objCashTO.setStatusDt(currDt);
                        objCashTO.setAuthorizeDt(currDt);
                       // objCashTO.setInstrumentNo1("ENTERED_AMOUNT");
                        //objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                        String partic = "TRADE EXPENCE";
                        objCashTO.setNarration(tradeNarration);
                        objCashTO.setParticulars(partic);
                        objCashTO.setInitiatedBranch(_branchCode);
                        objCashTO.setInitChannType(CommonConstants.CASHIER);
                        objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                        objCashTO.setAcHdId(grpHead);
                        objCashTO.setInpAmount(grandTot);
                        objCashTO.setAmount(grandTot);
                        //objCashTO.setAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
                        objCashTO.setAuthorizeBy(logTO.getUserId());
                        objCashTO.setTransModType(TransactionFactory.GL);
                        objCashTO.setLinkBatchId(objTO.getTradeId());
                        txMap = new HashMap();
                        txMap.put(CommonConstants.BRANCH_ID, _branchCode);
                        txMap.put(CommonConstants.PRODUCT_ID, TransactionFactory.GL);
                        txMap.put(CommonConstants.ACT_NUM, "");
                        txMap.put("TRANS_TYPE", CommonConstants.DEBIT);
                        txMap.put(CommonConstants.AC_HD_ID, grpHead);
                        txMap.put("AUTHORIZEREMARKS", "TRADE_EXPENSE_ACHD");
                        txMap.put("AMOUNT", new Double(objTO.getGrandTot()));
                        txMap.put("NARRATION", objTO.getTradeNarration());
                        txMap.put("LINK_BATCH_ID", objTO.getTradeId());
                        cashList.add(/*
                                 * setCashTransaction(txMap)
                                 */objCashTO);
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
                        System.out.println("tranMap BEFOREE EXECUTE---------------->" + tranMap);
                        tranMap = cashDao.execute(tranMap, false);
                        System.out.println("tranMap AFTER EXECUTE---------------->" + tranMap);
                        cashDao = null;
                        tranMap = null;
                    }
                    getTransDetails(objTO.getTradeId());
                    getTransDetails(objTO.getPurId());
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
//        returnMap = new HashMap();
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
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.executeUpdate("updatePurchaseEntryTO", objTO);
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
            sqlMap.executeUpdate("deletePurchaseEntryTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            PurcahseEntryDAO dao = new PurcahseEntryDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        System.out.println("*&$@#$@#$map:" + map);

        if (!map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            objTO = (PurchaseEntryTO) map.get("PurchaseEntryTO");
            objTO.setStatus_By(logTO.getUserId());
            final String command = objTO.getCommand();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData(map);
                //            returnMap = new HashMap();
                System.out.println("objTO.getStrIRNo()objTO.getStrIRNo()objTO.getStrIRNo()====:" + objTO);
                returnMap.put("PURCHASE_ENTRY_ID", objTO.getPurId());
                returnMap.put("TRADEEXEPENSE_ID", objTO.getTradeId());
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                throw new NoCommandException();
            }
        } else {
          
            System.out.println("map:" + map);
              objTO = (PurchaseEntryTO) map.get("PurchaseEntryTO");
             System.out.println("objTO.getStrIRNo()objTO.getStrIRNo()objTO.getStrIRNo()====:" + objTO);
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            authMap.put(CommonConstants.BRANCH_ID, (String) map.get(CommonConstants.BRANCH_ID));
            authMap.put(CommonConstants.USER_ID, (String) map.get(CommonConstants.USER_ID));
            if(map.containsKey("PURCHASE_ENTRY_ID")){
                 authMap.put("PURCHASE_ENTRY_ID",objTO.getPurId());
            }
            else{
               authMap.put("TRADEEXEPENSE_ID",objTO.getTradeId());
            }
            System.out.println("authMap:" + authMap);
            if (authMap != null) {
                authorize(authMap);
            }
        }

        destroyObjects();
        System.out.println("@#@#@# returnMap:" + returnMap);
        return returnMap;
    }

    private void authorize(HashMap map) throws Exception {
        String status = (String) map.get("STATUS");
        String linkBatchId = null;
        String disbursalNo = null;
        HashMap cashAuthMap;
        TransactionTO objTransactionTO = null;
        try {
            sqlMap.startTransaction();
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            System.out.println("map:" + map);
           
            map.put(CommonConstants.STATUS, status);
            map.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            map.put("CURR_DATE", currDt);
            System.out.println("status------------>" + status);
            if(CommonUtil.convertObjToDouble(objTO.getGrandTot())>0){
	            disbursalNo = CommonUtil.convertObjToStr(map.get("TRADEEXEPENSE_ID"));
	            sqlMap.executeUpdate("authorizePurchaseEntry", map);  //authorizeIndendDisbursal not there in map
	            linkBatchId = CommonUtil.convertObjToStr(map.get("TRADE_ID"));//Transaction Batch Id
            }else{
	            disbursalNo = CommonUtil.convertObjToStr(map.get("PURCHASE_ENTRY_ID"));
	            sqlMap.executeUpdate("authorizePurchaseEntry", map);  //authorizeIndendDisbursal not there in map
	            linkBatchId = CommonUtil.convertObjToStr(map.get("PURCHASE_ENTRY_ID"));//Transaction Batch Id  
            }
            //Separation of Authorization for Cash and Transfer
            //Call this in all places that need Authorization for Transaction
            cashAuthMap = new HashMap();
            System.out.println("@#$@zvbvxcvzx#$map:" + map);
            cashAuthMap.put(CommonConstants.BRANCH_ID, map.get(CommonConstants.BRANCH_ID));
            cashAuthMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
            cashAuthMap.put("DAILY", "DAILY");
            System.out.println("map:" + map);
            System.out.println("cashAuthMap:" + cashAuthMap);
            System.out.println("#$%#$%#$%xcvlinkBatchId" + linkBatchId);
            TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
            HashMap transMap = new HashMap();
            transMap.put("LINK_BATCH_ID", linkBatchId);
            System.out.println("transMap----------------->" + transMap);
            sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
            sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
            transMap = null;
            System.out.println("disbursalNo----------------->" + disbursalNo);
            objTransactionTO = new TransactionTO();
            objTransactionTO.setBatchId(CommonUtil.convertObjToStr(disbursalNo));
            objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
            objTransactionTO.setBranchId(_branchCode);
            System.out.println("objTransactionTO----------------->" + objTransactionTO);
            sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
            if (!status.equals("REJECTED")) {
            }
            map = null;
            sqlMap.commitTransaction();
            getTransDetails(objTO.getTradeId());
            getTransDetails(objTO.getPurId());
            returnMap.put("purid", objTO.getPurId());
            returnMap.put("tradeid",objTO.getTradeId());
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
