/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingTransferDAO.java
 *
 * Created on 02 March, 2015, 4:50 PM
 */
package com.see.truetransact.serverside.trading.tradingtransfer;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.trading.tradingstock.TradingStockDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.tds.tdsconfig.TDSConfigTO;
import com.see.truetransact.transferobject.trading.tradingtransfer.TradingTransferTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;
/**
 *
 * @author Revathi L
 */
public class TradingTransferDAO extends TTDAO {
    private static SqlMap sqlMap = null;
    private LogTO logTO;
    private LogDAO logDAO;
    private Date currDt = null;
    private TradingTransferTO objTradingTransferTO;
    private LinkedHashMap transTblDetails = null;
    private LinkedHashMap deleteTransMap = null;
    HashMap returnMap = new HashMap();
    private final static Logger log = Logger.getLogger(TradingStockDAO.class);
    String transferID = "";
    private TransactionDAO transactionDAO = null;
    
    public TradingTransferDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@#@@@@@#@#@#@#@#@#@#@    Map In Dao :"+map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        if(map.containsKey("TRANSFER_DATA")){
            transTblDetails = (LinkedHashMap) map.get("TRANSFER_DATA");
        }
        if (map.containsKey("DELETE_DATA")) {
            deleteTransMap = (LinkedHashMap) map.get("DELETE_DATA");
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        }else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap,map);
            }
        } else {
            throw new NoCommandException();
        }
        
         if (command.equals(CommonConstants.TOSTATUS_INSERT) && transferID.length()>0) {
                returnMap.put("TRANSFER_ID", transferID);
        }

//        destroyObjects();
        return returnMap;
    }
    
    public void insertData(HashMap map) throws Exception {
        try {
            transferID = "";
            transferID = getTransID();
            if (transTblDetails != null) {
                ArrayList addList = new ArrayList(transTblDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    TradingTransferTO objTradingTransferTO = (TradingTransferTO) transTblDetails.get(addList.get(i));
                    objTradingTransferTO.setTransferID(transferID);
                    sqlMap.executeUpdate("insertTradingTransferTO", objTradingTransferTO);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }
    
    private String getTransID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TRANSFER_ID");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }
    
    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            if (deleteTransMap != null && deleteTransMap.size()>0) {
                ArrayList addList = new ArrayList(deleteTransMap.keySet());
                TradingTransferTO objTradingTransferTO = null;
                for (int i = 0; i < deleteTransMap.size(); i++) {
                    objTradingTransferTO = new TradingTransferTO();
                    objTradingTransferTO = (TradingTransferTO) deleteTransMap.get(addList.get(i));
                    sqlMap.executeUpdate("deleteTradingTransferTO", objTradingTransferTO);
                }
            }
            if (transTblDetails != null && transTblDetails.size()>0) {
                ArrayList addList = new ArrayList(transTblDetails.keySet());
                TradingTransferTO objTradingTransferTO = null;
                for (int i = 0; i < transTblDetails.size(); i++) {
                    objTradingTransferTO = new TradingTransferTO();
                    objTradingTransferTO = (TradingTransferTO) transTblDetails.get(addList.get(i));
                    if (objTradingTransferTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        sqlMap.executeUpdate("insertTradingTransferTO", objTradingTransferTO);
                    } else {
                        sqlMap.executeUpdate("updateTradingTransferTO", objTradingTransferTO);
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }
    
    private void authorize(HashMap map, HashMap returnDetMap) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        try {
            if (transTblDetails != null) {
                ArrayList addList = new ArrayList(transTblDetails.keySet());
                TradingTransferTO objTradingTransferTO = (TradingTransferTO) transTblDetails.get(addList.get(0));
                if (objTradingTransferTO != null) {
                    if (CommonUtil.convertObjToStr(objTradingTransferTO.getFromBranch()).length() > 0
                            && CommonUtil.convertObjToStr(objTradingTransferTO.getToBranch()).length() > 0) {
                        String fromBranch = CommonUtil.convertObjToStr(objTradingTransferTO.getFromBranch());
                        String toBranch = CommonUtil.convertObjToStr(objTradingTransferTO.getToBranch());
                        if (CommonUtil.convertObjToStr(AuthMap.get("STATUS")).equals("AUTHORIZED") && transTblDetails != null && transTblDetails.size() > 0) {
                            insertTransactionData(AuthMap,fromBranch,toBranch);
                            updateTradingStock(map);
                        }
                        sqlMap.executeUpdate("authorizeTradingTransferTO", AuthMap);
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    
    private void insertTransactionData(HashMap map,String fromBranch,String toBranch) throws Exception {
        try {
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            HashMap txMap = new HashMap();
            HashMap transMap = new HashMap();
            String transferID = "";
            transferID = CommonUtil.convertObjToStr(map.get("TRANSFER_ID"));
            HashMap acHeadMap = new HashMap();
            HashMap transStock = new HashMap();
            double totAmount = 0.0;
            double transAmount = 0.0;
            String achd = "";
            if (transferID.length() > 0) {
                List headList = (List) sqlMap.executeQueryForList("getSelectTradingAcHead", acHeadMap);
                if (headList != null && headList.size() > 0) {
                    acHeadMap = (HashMap) headList.get(0);
                    transStock.put("TRANSFER_ID", transferID);
                    totAmount = CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT"));
                    achd = CommonUtil.convertObjToStr(map.get("AC_HD"));
                    ArrayList transferList = new ArrayList();
                    TransferTrans transferTrans = new TransferTrans();
                    transferTrans.setInitiatedBranch(BRANCH_ID);
                    TransferTrans objTransferTrans = new TransferTrans();
                    objTransferTrans.setInitiatedBranch(_branchCode);
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    transactionDAO.setInitiatedBranch(_branchCode);
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.setLinkBatchID(transferID);
                    HashMap tansactionMap = new HashMap();
                    TxTransferTO transferTo = new TxTransferTO();
                    ArrayList TxTransferTO = new ArrayList();
                    if (totAmount > 0) {
                        if (fromBranch.equals(_branchCode)) {
                            transAmount = totAmount;
                            txMap = new HashMap();
                            txMap.put(TransferTrans.DR_AC_HD,achd);
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "Trading Transfer -" + transferID);
                            txMap.put("AUTHORIZEREMARKS", "Debit");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmount));

                            txMap = new HashMap();
                            txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("STOCK"));
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "Trading Transfer -" + transferID);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                            if (transferList != null && transferList.size() > 0) {
                                doDebitCredit(transferList, _branchCode, map, transferID);
                            }
                        } else if (toBranch.equals(_branchCode)) {
                           transAmount = totAmount;
                            txMap = new HashMap();
                            txMap.put(TransferTrans.DR_AC_HD, acHeadMap.get("STOCK"));
                            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "Trading Transfer -" + transferID);
                            txMap.put("AUTHORIZEREMARKS", "Debit");
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                            txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                            txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                            transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmount));

                            txMap = new HashMap();
                            txMap.put(TransferTrans.CR_AC_HD, achd);
                            txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                            txMap.put(TransferTrans.PARTICULARS, "Trading Transfer -" + transferID);
                            txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                            txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                            transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                            if (transferList != null && transferList.size() > 0) {
                                doDebitCredit(transferList, _branchCode, map, transferID);
                            } 
                        }
                    }

                    getTransDetails(transferID);
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
        data.put(CommonConstants.SCREEN, "Trading Stock");
        data.put("MODE", "MODE");
        data.put("LINK_BATCH_ID", salesNo);
        HashMap transMap = new HashMap();
        transMap = transferDAO.execute(data, false);
        String authorizeStatus = CommonUtil.convertObjToStr(map.get("STATUS"));
        HashMap transAuthMap = new HashMap();
        transAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
        transAuthMap.put("DAILY", "DAILY");
        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
        TransactionDAO.authorizeCashAndTransfer(salesNo, authorizeStatus, transAuthMap);
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
    

    
    private void updateTradingStock(HashMap map) throws Exception {
        HashMap stockMap = new HashMap();
        ArrayList addList = new ArrayList(transTblDetails.keySet());
        for (int i = 0; i < addList.size(); i++) {
            stockMap = new HashMap();
            TradingTransferTO objTradingTransferTO = (TradingTransferTO) transTblDetails.get(addList.get(i));
            stockMap.put("RETURN_QTY", CommonUtil.convertObjToStr(objTradingTransferTO.getTransferQty()));
            stockMap.put("STOCK_ID", CommonUtil.convertObjToStr(objTradingTransferTO.getStockID()));
            stockMap.put("PRODUCT_ID", CommonUtil.convertObjToStr(objTradingTransferTO.getProdID()));
            sqlMap.executeUpdate("updateReturnStockQuant", stockMap);
        } 
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }
    
    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
           map.put("TRANSFER_ID", map.get("TRANSFER_ID")); 
           List pvLst = (List) sqlMap.executeQueryForList("getTradingTransferList", map);
            if (pvLst != null && pvLst.size() > 0) {
                LinkedHashMap ParMap = new LinkedHashMap();
                for (int i = pvLst.size(), j = 0; i > 0; i--, j++) {
                    String st = ((TradingTransferTO) pvLst.get(j)).getTransferID();
                    ParMap.put(j + 1, pvLst.get(j));
                }
                returnMap.put("TRANSFER_DATA", ParMap);
            }
        return returnMap;
    }
}
