/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingSalesDAO.java
 */

package com.see.truetransact.serverside.trading.tradingsales;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.trading.shopmaster.ShopMasterTO;
import com.see.truetransact.transferobject.trading.tradingsales.TradingSalesMasterTO;
import com.see.truetransact.transferobject.trading.tradingsales.TradingSalesTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import org.apache.log4j.Logger;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import java.util.LinkedHashMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.trading.tradingsales.TradingSalesRetTO;
import com.see.truetransact.transferobject.trading.tradingsales.TradingSalesRetDetailsTO;
import com.see.truetransact.transferobject.trading.tradingstock.TradingStockTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

/**
 *
 * @author Revathi L
 */
public class TradingSalesDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogTO logTO;
    private LogDAO logDAO;
    private TradingSalesMasterTO objTradingSalesMasterTO;
    private TradingSalesRetTO objTradingSalesRetTO;
    private TradingSalesRetDetailsTO objTradingSalesRetDetailsTO;
    private TradingSalesTO objTradingSalesTO;
    private TradingStockTO objTradingStockTO;
    private final static Logger log = Logger.getLogger(TradingSalesDAO.class);
    private LinkedHashMap salesTblDetails = null;
    private Date currDt = null;
    private TransactionDAO transactionDAO = null;
    HashMap returnMap = new HashMap();

    public TradingSalesDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("Map in DAO: " + map);
        logDAO = new LogDAO();
        logTO = new LogTO();
        returnMap = new HashMap();
        if (map.containsKey("objTradingSalesMasterTO")) {
            objTradingSalesMasterTO = (TradingSalesMasterTO) map.get("objTradingSalesMasterTO");
        } else if (map.containsKey("objTradingSalesReturnTO")) { //for Sales Return
            objTradingSalesRetTO = (TradingSalesRetTO) map.get("objTradingSalesReturnTO");
        }
        if (map.containsKey("SALES_DATA")) {
            salesTblDetails = (LinkedHashMap) map.get("SALES_DATA");
        }

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
                authorize(authMap, map);
            }
        }

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            if (objTradingSalesMasterTO != null) {
                returnMap.put("SALES_NO", objTradingSalesMasterTO.getSalesNo());
            } else if (objTradingSalesRetTO != null) {
                returnMap.put("SALES_RETURN_NO", objTradingSalesRetTO.getSalesRetNo());
            }
        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            objTradingSalesMasterTO.setStatus(CommonUtil.convertObjToStr("DELETED"));
            sqlMap.executeUpdate("deleteSalesMasterTO", objTradingSalesMasterTO);
            sqlMap.executeUpdate("deleteSalesMasterDetailsTO", objTradingSalesMasterTO);
            logTO.setData(objTradingSalesMasterTO.toString());
            logTO.setPrimaryKey(objTradingSalesMasterTO.getKeyData());
            logTO.setStatus(objTradingSalesMasterTO.getStatus());
            logDAO.addToLog(logTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            HashMap wheremap = new HashMap();
            if (objTradingSalesMasterTO != null) {
                wheremap.put("SALES_NO", objTradingSalesMasterTO.getSalesNo());
                updateSalesData(map, wheremap);
                sqlMap.executeUpdate("updateTradingSalesMasterTO", objTradingSalesMasterTO);
                logTO.setData(objTradingSalesMasterTO.toString());
                logTO.setPrimaryKey(objTradingSalesMasterTO.getKeyData());
                logTO.setStatus(objTradingSalesMasterTO.getStatus());
                logDAO.addToLog(logTO);
                sqlMap.commitTransaction();
            } else if (objTradingSalesRetTO != null) {
                List salesRetList = (List) map.get("RETURN_DATA");
                String salesRetNo = "";
                String salesNo = "";
                salesRetNo = CommonUtil.convertObjToStr(objTradingSalesRetTO.getSalesRetNo());
                salesNo = CommonUtil.convertObjToStr(objTradingSalesRetTO.getSalesNo());
                sqlMap.executeUpdate("deleteSalesRetTableData", objTradingSalesRetTO);
                insertSalesReturnDetails(salesRetNo, salesRetList, salesNo);
                logTO.setData(objTradingSalesRetTO.toString());
                logTO.setPrimaryKey(objTradingSalesRetTO.getKeyData());
                logTO.setStatus(objTradingSalesRetTO.getStatus());
                logDAO.addToLog(logTO);
                sqlMap.commitTransaction();
            }

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateSalesData(HashMap map, HashMap wheremap) throws Exception {
        try {
            sqlMap.executeUpdate("deleteSalesTableData", wheremap);
            if (salesTblDetails != null) {
                ArrayList addList = new ArrayList(salesTblDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    TradingSalesTO objTradingSalesTO = (TradingSalesTO) salesTblDetails.get(addList.get(i));
                    sqlMap.executeUpdate("insertTradingSalesMasterDetailsTO", objTradingSalesTO);
                    logTO.setData(objTradingSalesTO.toString());
                    logTO.setPrimaryKey(objTradingSalesTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objTradingSalesTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void updateSalesRetData(HashMap map, HashMap wheremap) throws Exception {
        try {
            sqlMap.executeUpdate("deleteSalesRetTableData", wheremap);
            if (salesTblDetails != null) {
                ArrayList addList = new ArrayList(salesTblDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    TradingSalesRetDetailsTO objTradingSalesRetDetailsTO = (TradingSalesRetDetailsTO) salesTblDetails.get(addList.get(i));
                    sqlMap.executeUpdate("insertTradingSalesRetDetailsTO", objTradingSalesTO);
                    logTO.setData(objTradingSalesRetDetailsTO.toString());
                    logTO.setPrimaryKey(objTradingSalesRetDetailsTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objTradingSalesRetDetailsTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void authorize(HashMap map, HashMap oldMap) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        try {
            if (AuthMap.containsKey("SALES")) {
                sqlMap.executeUpdate("authorizeTradingSales", AuthMap);
                List purchRetList = (List) salesTblDetails.get("SALES_DATA");
                if (CommonUtil.convertObjToStr(AuthMap.get("STATUS")).equals("AUTHORIZED")) {
                    insertTransactionData(AuthMap);
                    updateTradingStockQty(map);
                }
            } else if (AuthMap.containsKey("SALES_RETURN")) {
                sqlMap.executeUpdate("authorizeTradingSalesReturn", AuthMap);
                List salesRetList = (List) oldMap.get("RETURN_DATA");
                if (CommonUtil.convertObjToStr(AuthMap.get("STATUS")).equals("AUTHORIZED")) {
                    insertReturnTransactionData(AuthMap);
                    updateTradingRetStockQty(salesRetList);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void insertTransactionData(HashMap map) throws Exception {
        try {
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            HashMap txMap = new HashMap();
            HashMap transMap = new HashMap();
            String salesNo = "";
            salesNo = CommonUtil.convertObjToStr(map.get("SALES_NO"));
            HashMap acHeadMap = new HashMap();
            HashMap salesMap = new HashMap();
            double totAmount = 0.0;
            double transAmount = 0.0;
            if (salesNo.length() > 0) {
                List headList = (List) sqlMap.executeQueryForList("getSelectTradingAcHead", acHeadMap);
                if (headList != null && headList.size() > 0) {
                    acHeadMap = (HashMap) headList.get(0);
                    salesMap.put("SALES_NO", salesNo);
                    List salesLst = (List) sqlMap.executeQueryForList("getSelectTradingSalesForTrans", salesMap);
                    if (salesLst != null && salesLst.size() > 0) {
                        salesMap = (HashMap) salesLst.get(0);
                        totAmount = CommonUtil.convertObjToDouble(salesMap.get("GRAND_TOTAL"));
                        ArrayList transferList = new ArrayList();
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setInitiatedBranch(BRANCH_ID);
                        TransferTrans objTransferTrans = new TransferTrans();
                        objTransferTrans.setInitiatedBranch(_branchCode);
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        transactionDAO.setInitiatedBranch(_branchCode);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.setLinkBatchID(salesNo);
                        HashMap tansactionMap = new HashMap();
                        TxTransferTO transferTo = new TxTransferTO();
                        ArrayList TxTransferTO = new ArrayList();
                        if (CommonUtil.convertObjToStr(salesMap.get("SALES_TYPE")).equals("Cash Sale")) {
                            if (totAmount > 0) {
                                transAmount = totAmount;
                                txMap = new HashMap();
                                txMap.put(TransferTrans.DR_AC_HD, acHeadMap.get("CASH_ON_HAND"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading Sales -" + salesNo);
                                txMap.put("AUTHORIZEREMARKS", "Debit");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmount));

                                txMap = new HashMap();
                                txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("SALES"));
                                //txMap.put("AUTHORIZEREMARKS", "Insurance - " + insuranceProd_ID);
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading Sales -" + salesNo);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                                if (transferList != null && transferList.size() > 0) {
                                    doDebitCredit(transferList, _branchCode, map, salesNo);
                                }
                            }
                        } else if (CommonUtil.convertObjToStr(salesMap.get("SALES_TYPE")).equals("Credit Sale")) {
                            if (totAmount > 0) {
                                transAmount = totAmount;
                                txMap = new HashMap();
                                txMap.put(TransferTrans.DR_ACT_NUM, salesMap.get("ACC_NO"));
                                txMap.put(TransferTrans.DR_PROD_ID, salesMap.get("PROD_ID"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.SUSPENSE);
                                txMap.put(TransferTrans.PARTICULARS, "Trading Sales -" + salesNo);
                                txMap.put("AUTHORIZEREMARKS", "Debit");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmount));

                                txMap = new HashMap();
                                txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("SALES"));
                                //txMap.put("AUTHORIZEREMARKS", "Insurance - " + insuranceProd_ID);
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading Sales -" + salesNo);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                                if (transferList != null && transferList.size() > 0) {
                                    doDebitCredit(transferList, _branchCode, map, salesNo);
                                }
                            }
                        }
                    }
                    getTransDetails(CommonUtil.convertObjToStr(salesMap.get("SALES_NO")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void insertReturnTransactionData(HashMap map) throws Exception {
        try {
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            HashMap txMap = new HashMap();
            HashMap transMap = new HashMap();
            String salesRetNo = "";
            salesRetNo = CommonUtil.convertObjToStr(map.get("SALES_RET_NO"));
            HashMap acHeadMap = new HashMap();
            HashMap salesMap = new HashMap();
            HashMap getProdIDMap = new HashMap();
            double totAmount = 0.0;
            double transAmount = 0.0;
            if (salesRetNo.length() > 0) {
                List headList = (List) sqlMap.executeQueryForList("getSelectTradingAcHead", acHeadMap);
                if (headList != null && headList.size() > 0) {
                    acHeadMap = (HashMap) headList.get(0);
                    salesMap.put("SALES_RET_NO", salesRetNo);
                    List salesLst = (List) sqlMap.executeQueryForList("getSelectTradingSalRetForTrans", salesMap);
                    if (salesLst != null && salesLst.size() > 0) {
                        salesMap = (HashMap) salesLst.get(0);
                        totAmount = CommonUtil.convertObjToDouble(salesMap.get("RET_TOTAL"));
                        ArrayList transferList = new ArrayList();
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setInitiatedBranch(_branchCode);
                        TransferTrans objTransferTrans = new TransferTrans();
                        objTransferTrans.setInitiatedBranch(_branchCode);
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        transactionDAO.setInitiatedBranch(_branchCode);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.setLinkBatchID(salesRetNo);
                        HashMap tansactionMap = new HashMap();
                        TxTransferTO transferTo = new TxTransferTO();
                        ArrayList TxTransferTO = new ArrayList();
                        if (CommonUtil.convertObjToStr(salesMap.get("SALES_TYPE")).equals("Cash Sale")) {
                            if (totAmount > 0) {
                                transAmount = totAmount;
                                txMap = new HashMap();
                                txMap.put(TransferTrans.DR_AC_HD, acHeadMap.get("SALES_RETURN"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading Sales -" + salesRetNo);
                                txMap.put("AUTHORIZEREMARKS", "Debit");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmount));

                                txMap = new HashMap();
                                txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("CASH_ON_HAND"));
                                //txMap.put("AUTHORIZEREMARKS", "Insurance - " + insuranceProd_ID);
                                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading Sales -" + salesRetNo);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                                if (transferList != null && transferList.size() > 0) {
                                    doDebitCredit(transferList, _branchCode, map, salesRetNo);
                                }
                            }
                        } else if (CommonUtil.convertObjToStr(salesMap.get("SALES_TYPE")).equals("Credit Sale")) {
                            if (totAmount > 0) {
                                transAmount = totAmount;
                                txMap = new HashMap();
                                txMap.put(TransferTrans.DR_AC_HD, acHeadMap.get("SALES_RETURN"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Trading Sales -" + salesRetNo);
                                txMap.put("AUTHORIZEREMARKS", "Debit");
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmount));

                                getProdIDMap = new HashMap();
                                getProdIDMap.put("ACT_NUM", CommonUtil.convertObjToStr(salesMap.get("BANK_AC_HEAD")));
                                List creditProdActList = (List) sqlMap.executeQueryForList("getCrAccountDetails", getProdIDMap);
                                if (creditProdActList != null && creditProdActList.size() > 0) {
                                    getProdIDMap = (HashMap) creditProdActList.get(0);
                                    txMap = new HashMap();
                                    txMap.put(TransferTrans.CR_ACT_NUM, getProdIDMap.get("ACT_NUM"));
                                    txMap.put(TransferTrans.CR_AC_HD, getProdIDMap.get("AC_HD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_ID, getProdIDMap.get("PROD_ID"));
                                    txMap.put(TransferTrans.CR_PROD_TYPE, getProdIDMap.get("PROD_TYPE"));
                                    txMap.put(TransferTrans.CR_BRANCH, getProdIDMap.get("BRANCH_ID"));
                                    txMap.put("AUTHORIZEREMARKS", "Culture");
                                    txMap.put(TransferTrans.PARTICULARS, "Culture " + salesRetNo);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                    txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                    transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                                }
                                if (transferList != null && transferList.size() > 0) {
                                    doDebitCredit(transferList, _branchCode, map, salesRetNo);
                                }
                            }
                        }
                    }
                    getTransDetails(CommonUtil.convertObjToStr(salesMap.get("SALES_RET_NO")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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

    private void updateTradingStockQty(HashMap map) throws Exception {
        HashMap stockMap = new HashMap();
        ArrayList addList = new ArrayList(salesTblDetails.keySet());
        for (int i = 0; i < addList.size(); i++) {
            stockMap = new HashMap();
            TradingSalesTO objTradingSalesTO = (TradingSalesTO) salesTblDetails.get(addList.get(i));
            TradingStockTO objTradingStockTO = new TradingStockTO();
            stockMap.put("STOCK_QUANT", CommonUtil.convertObjToStr(objTradingSalesTO.getQty()));
            stockMap.put("STOCK_ID", CommonUtil.convertObjToStr(objTradingSalesTO.getStockID()));
            sqlMap.executeUpdate("updateSalesStockQuant", stockMap);
        }
    }

    private void updateTradingRetStockQty(List salesRetList) throws Exception {
        try {
            HashMap salesRetMap = new HashMap();
            if (salesRetList != null && salesRetList.size() > 0) {
                for (int i = 0; i < salesRetList.size(); i++) {
                    HashMap purchRetMap = new HashMap();
                    salesRetMap = (HashMap) salesRetList.get(i);
                    sqlMap.executeUpdate("updateSalesRetStockQuant", salesRetMap);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    public void insertData(HashMap map) throws Exception {
        try {
            String salesNo = "";
            //sqlMap.startTransaction();
            if (objTradingSalesMasterTO != null) {
                salesNo = getSalesNo();
                insertSalesDetails(salesNo);
                objTradingSalesMasterTO.setSalesNo(salesNo);
                sqlMap.executeUpdate("insertTradingSalesMasterTO", objTradingSalesMasterTO);
                logTO.setData(objTradingSalesMasterTO.toString());
                logTO.setPrimaryKey(objTradingSalesMasterTO.getKeyData());
                logTO.setStatus(objTradingSalesMasterTO.getStatus());
                logDAO.addToLog(logTO);
                //sqlMap.commitTransaction();
            } else if (objTradingSalesRetTO != null) {
                if (map.containsKey("RETURN_DATA")) {
                    List salesRetList = (List) map.get("RETURN_DATA");
                    salesNo = CommonUtil.convertObjToStr(objTradingSalesRetTO.getSalesNo());
                    String salesRetNo = "";
                    salesRetNo = getSalesReturnNo();
                    insertSalesReturnDetails(salesRetNo, salesRetList, salesNo);
                    objTradingSalesRetTO.setSalesRetNo(salesRetNo);
                    sqlMap.executeUpdate("insertTradingSalesReturnTO", objTradingSalesRetTO);
                    logTO.setData(objTradingSalesRetTO.toString());
                    logTO.setPrimaryKey(objTradingSalesRetTO.getKeyData());
                    logTO.setStatus(objTradingSalesRetTO.getStatus());
                    logDAO.addToLog(logTO);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private String getSalesReturnNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SALES_RET_ID");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }

    private void insertSalesDetails(String salesNo) throws Exception {
        try {
            if (salesTblDetails != null) {
                ArrayList addList = new ArrayList(salesTblDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    TradingSalesTO objTradingSalesTO = (TradingSalesTO) salesTblDetails.get(addList.get(i));
                    objTradingSalesTO.setSalesNo(salesNo);
                    sqlMap.executeUpdate("insertTradingSalesMasterDetailsTO", objTradingSalesTO);
                    logTO.setData(objTradingSalesTO.toString());
                    logTO.setPrimaryKey(objTradingSalesTO.getKeyData());
                    logDAO.addToLog(logTO);
                    objTradingSalesTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    private void insertSalesReturnDetails(String salesRetNo, List salesRetList, String salesNo) throws Exception {
        try {
            if (salesRetList != null && salesRetList.size() > 0) {
                int retQty = 0;
                for (int i = 0; i < salesRetList.size(); i++) {
                    HashMap salesRetMap = new HashMap();
                    salesRetMap = (HashMap) salesRetList.get(i);
                    retQty = CommonUtil.convertObjToInt(salesRetMap.get("RETURN_QTY"));
                    if (retQty > 0) {
                        objTradingSalesRetDetailsTO = new TradingSalesRetDetailsTO();
                        objTradingSalesRetDetailsTO.setSalesRetNo(salesRetNo);
                        objTradingSalesRetDetailsTO.setSlNo(CommonUtil.convertObjToStr(salesRetMap.get("SL_NO")));
                        objTradingSalesRetDetailsTO.setSalesNo(salesNo);
                        objTradingSalesRetDetailsTO.setProdName(CommonUtil.convertObjToStr(salesRetMap.get("PRODUCT_NAME")));
                        objTradingSalesRetDetailsTO.setUnitType(CommonUtil.convertObjToStr(salesRetMap.get("UNIT_TYPE")));
                        objTradingSalesRetDetailsTO.setRate(CommonUtil.convertObjToStr(salesRetMap.get("RATE")));
                        objTradingSalesRetDetailsTO.setQty(CommonUtil.convertObjToStr(salesRetMap.get("QTY")));
                        objTradingSalesRetDetailsTO.setTax(CommonUtil.convertObjToStr(salesRetMap.get("TAX")));
                        objTradingSalesRetDetailsTO.setTaxAmt(CommonUtil.convertObjToStr(salesRetMap.get("TAXABLE_AMOUNT")));
                        objTradingSalesRetDetailsTO.setStockID(CommonUtil.convertObjToStr(salesRetMap.get("STOCK_ID")));
                        objTradingSalesRetDetailsTO.setSalesTot(CommonUtil.convertObjToStr(salesRetMap.get("SALES_TOTAL")));
                        objTradingSalesRetDetailsTO.setRetQty(CommonUtil.convertObjToStr(salesRetMap.get("RETURN_QTY")));
                        objTradingSalesRetDetailsTO.setRetTot(CommonUtil.convertObjToStr(salesRetMap.get("RETURN_TOTAL")));
                        objTradingSalesRetDetailsTO.setStatus("CREATED");
                        sqlMap.executeUpdate("insertTradingSalesRetDetailsTO", objTradingSalesRetDetailsTO);
                    }
                    objTradingSalesRetDetailsTO = null;
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    private String getSalesNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SALES_ID");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
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

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        System.out.println("#### GetData map : " + map);
        returnMap = new HashMap();
        if (map.containsKey("SALES")) {
            List list = (List) sqlMap.executeQueryForList("getSelectTradingSalesMasterTO", map);
            if (list != null && list.size() > 0) {
                returnMap.put("objTradingSalesMasterTO", list);
            }
            map.put("SALES_NO", map.get("SALES_NO"));
            List salesList = (List) sqlMap.executeQueryForList("getTradingSalesMasterDetailsTO", map);
            if (salesList != null && salesList.size() > 0) {
                LinkedHashMap ParMap = new LinkedHashMap();
                for (int i = salesList.size(), j = 0; i > 0; i--, j++) {
                    String st = ((TradingSalesTO) salesList.get(j)).getSalesNo();
                    ParMap.put(j + 1, salesList.get(j));
                }
                returnMap.put("SALES_DATA", ParMap);
            }
        } else if (map.containsKey("SALESRETURN")) {
            List returnLst = (List) sqlMap.executeQueryForList("getSelectTradingSalesReturnTO", map);
            if (returnLst != null && returnLst.size() > 0) {
                returnMap.put("objTradingSalesReturnTO", returnLst);
            }
            map.put("SALES_RET_NO", map.get("SALES_RET_NO"));
            List subRetList = (List) sqlMap.executeQueryForList("getTradingSalesReturnDetailsTO", map);
            if (subRetList != null && subRetList.size() > 0) {
                LinkedHashMap subMap = new LinkedHashMap();
                for (int i = subRetList.size(), j = 0; i > 0; i--, j++) {
                    //String st = ((TradingPurchaseReturnDetailsTO) subRetList.get(j)).getPurchaseRetNo();
                    //subMap.put(j + 1, subRetList.get(j));
                }
                returnMap.put("RETURN_DATA", subRetList);
                subRetList = null;
            }
        }
        return returnMap;
    }

    private void destroyObjects() {
        objTradingSalesMasterTO = null;
        objTradingSalesTO = null;
    }
}
