/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PurcahseEntryDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */

/**
 *
 * @author Revathi L
 */
package com.see.truetransact.serverside.trading.purchaseentry;

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
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransaction;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.trading.purchaseentry.PurchaseEntryTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.util.HashMap;
import java.util.Date;

public class PurcahseEntryDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TransactionDAO transactionDAO = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt;
    HashMap returnMap = null;
    private PurchaseEntryTO objPurchaseEntryTO;
    TransactionTO transactionTO = new TransactionTO();

    public PurcahseEntryDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        returnMap = new HashMap();
        System.out.println("#### GetData map : " + map);
        returnMap = new HashMap();
        List purchaseLst = (List) sqlMap.executeQueryForList("getPurchaseEntryDetails", map);
        if (purchaseLst != null && purchaseLst.size() > 0) {
            returnMap.put("objPurchaseEntryTO", purchaseLst);
        }
        if (map.containsKey("PURCHASE_ENTRY_ID")) {
            getTransDetails(map.get("PURCHASE_ENTRY_ID").toString());
        }
        return returnMap;
    }

    private String getTradeExpenseID() throws Exception {
        final HashMap where = new HashMap();
        HashMap map = generateID();
        return (String) map.get(CommonConstants.DATA);


    }

    public HashMap generateID() {

        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            if (objPurchaseEntryTO.getTransMode().equals("Purchase")) {
                where.put("ID_KEY", "PURCHASE_ENTRY_ID");
            } else if (objPurchaseEntryTO.getTransMode().equals("Purchase Return")) {
                where.put("ID_KEY", "PURCHASE_RETURN");
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

    private String getPurchaseEntryID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        if (objPurchaseEntryTO.getTransMode().equals("Purchase")) {
            where.put(CommonConstants.MAP_WHERE, "PURCHASE_ENTRY_ID");
        } else if (objPurchaseEntryTO.getTransMode().equals("Purchase Return")) {
            where.put(CommonConstants.MAP_WHERE, "PURCHASE_RETURN_ENTRY_ID");
        }
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            String purchaseEntryID = "";
            purchaseEntryID = getPurchaseEntryID();
            objPurchaseEntryTO.setPurchaseEntryID(purchaseEntryID);
            sqlMap.executeUpdate("insertPurchaseEntryTO", objPurchaseEntryTO);
            logTO.setData(objPurchaseEntryTO.toString());
            logTO.setPrimaryKey(objPurchaseEntryTO.getKeyData());
            logTO.setStatus(objPurchaseEntryTO.getStatus());
            logDAO.addToLog(logTO);
            getTransDetails(purchaseEntryID);
            returnMap.put("PURCHASE_ENTRY_ID", purchaseEntryID);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(HashMap map) throws Exception {
        try {
            HashMap wheremap = new HashMap();
            sqlMap.executeUpdate("updatePurchaseEntryTO", objPurchaseEntryTO);
            logTO.setData(objPurchaseEntryTO.toString());
            logTO.setPrimaryKey(objPurchaseEntryTO.getKeyData());
            logTO.setStatus(objPurchaseEntryTO.getStatus());
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void insertTransactionData(HashMap map) throws Exception {
        try {
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            HashMap data = new HashMap();
            HashMap transDataMap = new HashMap();
            HashMap creditMap = new HashMap();
            ArrayList transferList = new ArrayList();
            TransferTrans transferTrans = new TransferTrans();
            transferTrans.setInitiatedBranch(_branchCode);
            TransferTrans objTransferTrans = new TransferTrans();
            objTransferTrans.setInitiatedBranch(_branchCode);
            transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
            transactionDAO.setInitiatedBranch(_branchCode);
            transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
            transactionDAO.setTransactionType(transactionDAO.TRANSFER);
            transactionDAO.setLinkBatchID(CommonUtil.convertObjToStr(map.get("PURCHASE_ENTRY_ID")));
            HashMap txMap = new HashMap();
            HashMap tansactionMap = new HashMap();
            TxTransferTO transferTo = new TxTransferTO();
            ArrayList TxTransferTO = new ArrayList();
            double transAmount = 0.0;
            HashMap acHeadMap = new HashMap();
            List headList = (List) sqlMap.executeQueryForList("getTradingAcHeads", acHeadMap);
            if (headList != null && headList.size() > 0) {
                acHeadMap = (HashMap) headList.get(0);
                if (CommonUtil.convertObjToStr(objPurchaseEntryTO.getTransMode()).equals("Purchase")) {
                    transAmount = CommonUtil.convertObjToDouble(objPurchaseEntryTO.getTotalAmt());
                    if (transAmount > 0) {
                        if (CommonUtil.convertObjToStr(objPurchaseEntryTO.getTransType()).equals("Cash")) {
                            ArrayList cashList = new ArrayList();
                            HashMap cashTransMap = new HashMap();
                            cashTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                            cashTransMap.put("BRANCH_CODE", _branchCode);
                            cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            cashTransMap.put("USER_ID", map.get("USER_ID"));
                            cashTransMap.put("ACCT_HEAD", acHeadMap.get("PURCHASE"));
                            cashTransMap.put("TRANS_AMOUNT", String.valueOf(transAmount));
                            cashTransMap.put("AUTHORIZEREMARKS", "PURCHASE_ENTRY");
                            txMap.put(TransferTrans.PARTICULARS, "Trading -" + objPurchaseEntryTO.getSupplierID());
                            cashTransMap.put("PROD_TYPE", TransactionFactory.GL);
                            cashList.add(createCashTransactionTO(cashTransMap, CommonUtil.convertObjToStr(map.get("PURCHASE_ENTRY_ID"))));
                            if (cashList != null && cashList.size() > 0) {
                                doCashTrans(cashList, _branchCode, map, false, CommonUtil.convertObjToStr(map.get("PURCHASE_ENTRY_ID")));
                            }
                        } else if (CommonUtil.convertObjToStr(objPurchaseEntryTO.getTransType()).equals("Transfer")) {
                            if (CommonUtil.convertObjToStr(objPurchaseEntryTO.getCreditFrom()).equals("S")) {
                                txMap.put(TransferTrans.DR_AC_HD, acHeadMap.get("PURCHASE"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading -" + objPurchaseEntryTO.getSupplierID());
                                txMap.put("AUTHORIZEREMARKS", "Debit");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmount));

                                creditMap.put("ACT_NUM", CommonUtil.convertObjToStr(objPurchaseEntryTO.getAcHd()));
                                List creditAccountDetailsList = (List) sqlMap.executeQueryForList("getCrAccountDetails", creditMap); //IF SALARY ACCOUNT STATUS LIVE/CLOSED
                                if (creditAccountDetailsList != null && creditAccountDetailsList.size() > 0) {
                                    creditMap = (HashMap) creditAccountDetailsList.get(0);
                                    txMap.put(TransferTrans.CR_ACT_NUM, creditMap.get("ACT_NUM"));
                                    txMap.put(TransferTrans.CR_AC_HD, creditMap.get("AC_HD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_ID, creditMap.get("PROD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, creditMap.get("PROD_TYPE"));
                                    txMap.put(TransferTrans.CR_BRANCH, creditMap.get("BRANCH_ID"));
                                    txMap.put("AUTHORIZEREMARKS", "Salary");
                                    txMap.put(TransferTrans.PARTICULARS, "Trading - " + objPurchaseEntryTO.getSupplierID());
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                                }
                            } else if (CommonUtil.convertObjToStr(objPurchaseEntryTO.getCreditFrom()).equals("O")) {
                                txMap.put(TransferTrans.DR_AC_HD, acHeadMap.get("PURCHASE"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading - " + objPurchaseEntryTO.getSupplierID());
                                txMap.put("AUTHORIZEREMARKS", "Debit");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmount));

                                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(objPurchaseEntryTO.getAcHd()));
                                txMap.put("AUTHORIZEREMARKS", "Credit");
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading - " + objPurchaseEntryTO.getSupplierID());
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                            }
                            if (transferList != null && transferList.size() > 0) {
                                TransferDAO transferDAO = new TransferDAO();
                                data = new HashMap();
                                transDataMap = new HashMap();
                                data.put("TxTransferTO", transferList);
                                data.put("COMMAND", map.get("COMMAND"));
                                data.put("INITIATED_BRANCH", _branchCode);
                                data.put(CommonConstants.BRANCH_ID, _branchCode);

                                data.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                data.put(CommonConstants.MODULE, "Pay Roll");
                                data.put(CommonConstants.SCREEN, "Salary Calculation");
                                data.put("MODE", "MODE");
                                data.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(map.get("PURCHASE_ENTRY_ID")));
                                transDataMap = transferDAO.execute(data, false);
                            }
                        }
                    }
                }
                if (CommonUtil.convertObjToStr(objPurchaseEntryTO.getTransMode()).equals("Purchase Return")) {
                    transAmount = CommonUtil.convertObjToDouble(objPurchaseEntryTO.getTotalAmt());
                    if (transAmount > 0) {
                        if (CommonUtil.convertObjToStr(objPurchaseEntryTO.getTransType()).equals("Cash")) {
                            txMap.put(TransferTrans.DR_AC_HD, acHeadMap.get("SA_RECEIVABLE"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "Trading - " + objPurchaseEntryTO.getSupplierID());
                            txMap.put("AUTHORIZEREMARKS", "Debit");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmount));

                            txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("PURCHASE_RETURN"));
                            txMap.put("AUTHORIZEREMARKS", "Credit");
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "Trading - " + objPurchaseEntryTO.getSupplierID());
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));

                            if (transferList != null && transferList.size() > 0) {
                                TransferDAO transferDAO = new TransferDAO();
                                data = new HashMap();
                                transDataMap = new HashMap();
                                data.put("TxTransferTO", transferList);
                                data.put("COMMAND", map.get("COMMAND"));
                                data.put("INITIATED_BRANCH", _branchCode);
                                data.put(CommonConstants.BRANCH_ID, _branchCode);

                                data.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                data.put(CommonConstants.MODULE, "Pay Roll");
                                data.put(CommonConstants.SCREEN, "Salary Calculation");
                                data.put("MODE", "MODE");
                                data.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(map.get("PURCHASE_ENTRY_ID")));
                                transDataMap = transferDAO.execute(data, false);
                            }

                            ArrayList cashList = new ArrayList();
                            HashMap cashTransMap = new HashMap();
                            cashTransMap.put("SELECTED_BRANCH_ID", _branchCode);
                            cashTransMap.put("BRANCH_CODE", _branchCode);
                            cashTransMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                            cashTransMap.put("USER_ID", map.get("USER_ID"));
                            cashTransMap.put("ACCT_HEAD", acHeadMap.get("SA_RECEIVABLE"));
                            cashTransMap.put("TRANS_AMOUNT", String.valueOf(transAmount));
                            cashTransMap.put("AUTHORIZEREMARKS", "PURCHASE_ENTRY");
                            txMap.put(TransferTrans.PARTICULARS, "Trading -" + objPurchaseEntryTO.getSupplierID());
                            cashTransMap.put("PROD_TYPE", TransactionFactory.GL);
                            cashList.add(createCreditCashTransactionTO(cashTransMap, CommonUtil.convertObjToStr(map.get("PURCHASE_ENTRY_ID"))));
                            if (cashList != null && cashList.size() > 0) {
                                doCashTrans(cashList, _branchCode, map, false, CommonUtil.convertObjToStr(map.get("PURCHASE_ENTRY_ID")));
                            }
                        } else if (CommonUtil.convertObjToStr(objPurchaseEntryTO.getTransType()).equals("Transfer")) {
                            if (CommonUtil.convertObjToStr(objPurchaseEntryTO.getCreditFrom()).equals("S")) {
                                creditMap.put("ACT_NUM", CommonUtil.convertObjToStr(objPurchaseEntryTO.getAcHd()));
                                List creditAccountDetailsList = (List) sqlMap.executeQueryForList("getCrAccountDetails", creditMap);
                                if (creditAccountDetailsList != null && creditAccountDetailsList.size() > 0) {
                                    creditMap = (HashMap) creditAccountDetailsList.get(0);
                                    txMap.put(TransferTrans.DR_ACT_NUM, creditMap.get("ACT_NUM"));
                                    txMap.put(TransferTrans.DR_AC_HD, creditMap.get("AC_HD_ID"));
                                    txMap.put(TransferTrans.DR_PROD_ID, creditMap.get("PROD_ID"));
                                    txMap.put(TransferTrans.DR_PROD_TYPE, creditMap.get("PROD_TYPE"));
                                    txMap.put(TransferTrans.DR_BRANCH, creditMap.get("BRANCH_ID"));
                                    txMap.put("AUTHORIZEREMARKS", "Salary");
                                    txMap.put(TransferTrans.PARTICULARS, "Trading - " + objPurchaseEntryTO.getSupplierID());
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                                }

                                txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("PURCHASE_RETURN"));
                                txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("PURCHASE_RETURN"));
                                txMap.put("AUTHORIZEREMARKS", "Credit");
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading - " + objPurchaseEntryTO.getSupplierID());
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                            } else if (CommonUtil.convertObjToStr(objPurchaseEntryTO.getCreditFrom()).equals("O")) {
                                txMap.put(TransferTrans.DR_AC_HD, acHeadMap.get("SA_RECEIVABLE"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading - " + objPurchaseEntryTO.getSupplierID());
                                txMap.put("AUTHORIZEREMARKS", "Debit");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmount));

                                txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("PURCHASE_RETURN"));
                                txMap.put("AUTHORIZEREMARKS", "Credit");
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading - " + objPurchaseEntryTO.getSupplierID());
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                            }
                            if (transferList != null && transferList.size() > 0) {
                                TransferDAO transferDAO = new TransferDAO();
                                data = new HashMap();
                                transDataMap = new HashMap();
                                data.put("TxTransferTO", transferList);
                                data.put("COMMAND", map.get("COMMAND"));
                                data.put("INITIATED_BRANCH", _branchCode);
                                data.put(CommonConstants.BRANCH_ID, _branchCode);

                                data.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                data.put(CommonConstants.MODULE, "Pay Roll");
                                data.put(CommonConstants.SCREEN, "Salary Calculation");
                                data.put("MODE", "MODE");
                                data.put("LINK_BATCH_ID", CommonUtil.convertObjToStr(map.get("PURCHASE_ENTRY_ID")));
                                transDataMap = transferDAO.execute(data, false);
                            }
                        }
                    }
                }
                getTransDetails(map.get("PURCHASE_ENTRY_ID").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
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
        if (transactionTO == null) {
            transactionTO = new TransactionTO();
        }
        transactionTO.setBatchId(loanRepayNo);
        transactionTO.setBatchDt(currDt);
        transactionTO.setTransId(loanRepayNo);
        transactionTO.setStatus(CommonUtil.convertObjToStr(map.get("COMMAND")));
        transactionTO.setBranchId(_branchCode);
        sqlMap.executeUpdate("insertRemittanceIssueTransactionTO", transactionTO);
    }

    private CashTransactionTO createCashTransactionTO(HashMap dataMap, String loanRepayNo) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
        if (dataMap.get("PROD_TYPE").equals("GL")) {
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
        }
        objCashTO.setTransType(CommonConstants.DEBIT);
        objCashTO.setParticulars("TO Cash : " + dataMap.get("PARTICULARS"));
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
        return objCashTO;
    }

    private CashTransactionTO createCreditCashTransactionTO(HashMap dataMap, String loanRepayNo) throws Exception {
        CashTransactionTO objCashTO = new CashTransactionTO();
        objCashTO.setAcHdId(CommonUtil.convertObjToStr(dataMap.get("ACCT_HEAD")));
        if (dataMap.get("PROD_TYPE").equals("GL")) {
            objCashTO.setProdType(CommonUtil.convertObjToStr(TransactionFactory.GL));
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
        return objCashTO;
    }

    private void doDebitCredit(ArrayList batchList, String branchCode, HashMap map, String salesNo) throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        HashMap data = new HashMap();
        data.put("TxTransferTO", batchList);
        data.put("COMMAND", map.get("COMMAND"));
        data.put("INITIATED_BRANCH", _branchCode);
        data.put(CommonConstants.BRANCH_ID, branchCode);
        data.put(CommonConstants.USER_ID, map.get("USER_ID"));
        data.put(CommonConstants.MODULE, "Trading");
        data.put(CommonConstants.SCREEN, "Trading Sales");
        data.put("MODE", "MODE");
        data.put("LINK_BATCH_ID", salesNo);
        HashMap transMap = new HashMap();
        transMap = transferDAO.execute(data, false);
        String authorizeStatus = CommonUtil.convertObjToStr(map.get("STATUS"));
        HashMap transAuthMap = new HashMap();
        transAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
        //transAuthMap.put(CommonConstants.USER_ID, insuranceMap.get(CommonConstants.USER_ID));
        transAuthMap.put("DAILY", "DAILY");
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        TransactionDAO.authorizeCashAndTransfer(salesNo, authorizeStatus, transAuthMap);
    }

    private void authorize(HashMap map, HashMap oldMap) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        try {
            sqlMap.executeUpdate("authorizePurchaseEntry", AuthMap);
            if (CommonUtil.convertObjToStr(AuthMap.get("STATUS")).equals("AUTHORIZED")) {
                insertTransactionData(AuthMap);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
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
        List cashList = (List) sqlMap.executeQueryForList("getCashDetailsWithAccHeadDesc", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            returnMap.put("CASH_TRANS_LIST", cashList);
        }
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }

    private void deleteData() throws Exception {
//        try {
//            sqlMap.startTransaction();
//            objTO.setStatus(CommonConstants.STATUS_DELETED);
//            sqlMap.executeUpdate("deletePurchaseEntryTO", objTO);
//            sqlMap.commitTransaction();
//        } catch (Exception e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw new TransRollbackException(e);
//        }
    }

    public static void main(String str[]) {
        try {
            PurcahseEntryDAO dao = new PurcahseEntryDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("################# TradingPurchase DAO : " + map);
        returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        if (map.containsKey("objPurchaseEntryTO")) {      // for Purchase Screen
            objPurchaseEntryTO = (PurchaseEntryTO) map.get("objPurchaseEntryTO");
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } //else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
        //             deleteData(map);
        //        }
        else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap, map);
            }
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
//            if (objTradingPurchaseTO != null) {
//                returnMap.put("PURCHASE_NO", objTradingPurchaseTO.getPurchaseNo());
//            } else if (objTradingPurchaseReturnTO != null) {
//                returnMap.put("PURCHASE_Return_NO", objTradingPurchaseReturnTO.getPurchaseRetNo());
//            }
        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objPurchaseEntryTO = null;
    }
}
