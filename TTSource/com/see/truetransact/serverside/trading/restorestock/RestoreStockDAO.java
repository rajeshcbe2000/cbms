/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RestoreStockDAO.java
 * @author Revathi L
 */


package com.see.truetransact.serverside.trading.restorestock;

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
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.serverside.transaction.transfer.TransferDAO;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;

//import com.transversalnet.db.DatabaseConnection;
//import com.transversalnet.error.TENException;
//import com.transversalnet.services.Group;
//import com.transversalnet.services.ShopMaster;
//import com.transversalnet.services.Supplier;
public class RestoreStockDAO extends TTDAO {
    private static SqlMap sqlMap = null;
    private LogTO logTO;
    private LogDAO logDAO;
    private final static Logger log = Logger.getLogger(RestoreStockDAO.class);
    HashMap dataMap = new HashMap();
    HashMap restoreMap =  new HashMap();
    String restoreID = "";
    private Date currDt = null;
    HashMap returnMap = new HashMap();
    HashMap deleteMap = new HashMap();
    HashMap singleMap = new HashMap();
    private Iterator restoreIterator;
    private TransactionDAO transactionDAO = null;
    
    
    public RestoreStockDAO()throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    public HashMap execute(HashMap map) throws Exception {
        System.out.println("#@#@##@#@#@#@#@#@#@   Map in DAO :"+map);
        returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        logDAO = new LogDAO();
        logTO = new LogTO();
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
//            deleteData();
        }else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        } else {
            throw new NoCommandException();
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT) && restoreID.length()>0) {
                returnMap.put("RESTORE_ID", restoreID);
        }

//        destroyObjects();
        return returnMap;
    }
    
    public void insertData(HashMap map) throws Exception {
        try {
            restoreID = "";
            restoreID = getRestoreID();
            dataMap = new HashMap();
            Date fromDt = null;
            Date toDt = null;
            if (map.containsKey("RESTORE_LIST")) {
                List restoreList = (List) map.get("RESTORE_LIST");
                if (restoreList != null && restoreList.size() > 0) {
                    for (int i = 0; i < restoreList.size(); i++) {
                        dataMap = (HashMap) restoreList.get(i);
                        dataMap.put("RESTORE_ID", restoreID);
                        if (CommonUtil.convertObjToInt(dataMap.get("RESTORE_QTY")) > 0
                                || CommonUtil.convertObjToDouble(dataMap.get("RESTORE_AMOUNT")).doubleValue() > 0) {
                            sqlMap.executeUpdate("insertRestoreStockDetails", dataMap);
                        }
                    }
                }
                if (map.containsKey("RESTORE_DATA")) {
                    restoreMap = (HashMap) map.get("RESTORE_DATA");
                    restoreMap.put("RESTORE_ID", restoreID);
                    if (CommonUtil.convertObjToStr(restoreMap.get("FROM_DT")).length()>0) {
                        fromDt = setProperDtFormat((Date) restoreMap.get("FROM_DT"));
                        toDt = setProperDtFormat((Date) restoreMap.get("TO_DT"));
                        restoreMap.put("FROM_DT", fromDt);
                        restoreMap.put("TO_DT", toDt);
                    }else{
                        restoreMap.put("FROM_DT",null );
                        restoreMap.put("TO_DT", null);
                    }
                    
                    sqlMap.executeUpdate("insertRestoreStock", restoreMap);
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }
    
    private void updateData(HashMap map) throws Exception {
        try {
            deleteMap = new HashMap();
            HashMap restoreMap = new HashMap();
            if (map.containsKey("RESTORE_DATA")) {
                restoreMap = (HashMap) map.get("RESTORE_DATA");
            }
            if (map.containsKey("DELETE_DATA")) {
                deleteMap = (HashMap) map.get("DELETE_DATA");
                if(deleteMap!=null && deleteMap.size()>0){
                singleMap = new HashMap();
                String slNo = "";
                restoreIterator = deleteMap.keySet().iterator();
                for (int j = 0; j < deleteMap.size(); j++) {
                    slNo = (String) restoreIterator.next();
                    singleMap = (HashMap) deleteMap.get(slNo);
                    singleMap.put("RESTORE_ID", CommonUtil.convertObjToStr(restoreMap.get("RESTORE_ID")));
                    singleMap.put("STATUS", "DELETED");
                    sqlMap.executeUpdate("deleteRestoreDetails", singleMap);
                }
            }
            }
            if (map.containsKey("RESTORE_LIST")) {
                List restoreList = (List) map.get("RESTORE_LIST");
                if (restoreList != null && restoreList.size() > 0) {
                    for (int i = 0; i < restoreList.size(); i++) {
                        dataMap = (HashMap) restoreList.get(i);
                        dataMap.put("STATUS", "MODIFIED");
                        if (CommonUtil.convertObjToInt(dataMap.get("RESTORE_QTY")) > 0
                                || CommonUtil.convertObjToDouble(dataMap.get("RESTORE_AMOUNT")).doubleValue() > 0) {
                            sqlMap.executeUpdate("updateRestoreDetails", dataMap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }
    
    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        try {
                if (CommonUtil.convertObjToStr(AuthMap.get("STATUS")).equals("AUTHORIZED")) {
                    insertTransactionData(AuthMap);
                    updateTradingStock(AuthMap);
                }
                sqlMap.executeUpdate("authorizeRestoreStock", AuthMap);
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
            String restoreID = "";
            restoreID = CommonUtil.convertObjToStr(map.get("RESTORE_ID"));
            HashMap acHeadMap = new HashMap();
            HashMap damageMap = new HashMap();
            double totAmount = 0.0;
            double transAmount = 0.0;
            if (restoreID.length() > 0) {
                List headList = (List) sqlMap.executeQueryForList("getSelectTradingAcHead", acHeadMap);
                if (headList != null && headList.size() > 0) {
                    acHeadMap = (HashMap) headList.get(0);
                    totAmount = CommonUtil.convertObjToDouble(map.get("TOTAL_AMOUNT"));
                    ArrayList transferList = new ArrayList();
                    TransferTrans transferTrans = new TransferTrans();
                    transferTrans.setInitiatedBranch(BRANCH_ID);
                    TransferTrans objTransferTrans = new TransferTrans();
                    objTransferTrans.setInitiatedBranch(_branchCode);
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    transactionDAO.setInitiatedBranch(_branchCode);
                    transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                    transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                    transactionDAO.setLinkBatchID(restoreID);
                    HashMap tansactionMap = new HashMap();
                    TxTransferTO transferTo = new TxTransferTO();
                    ArrayList TxTransferTO = new ArrayList();
                    if (totAmount > 0) {
                        transAmount = totAmount;
                        txMap = new HashMap();
                        txMap.put(TransferTrans.DR_AC_HD, acHeadMap.get("STOCK"));
                        txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "Restore Stock -" + restoreID);
                        txMap.put("AUTHORIZEREMARKS", "Debit");
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(TransferTrans.DR_INST_TYPE, "VOUCHER");
                        txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                        txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                        transferList.add(objTransferTrans.getDebitTransferTO(txMap, transAmount));

                        txMap = new HashMap();
                        txMap.put(TransferTrans.CR_AC_HD, acHeadMap.get("DAMAGES"));
                        txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                        txMap.put(TransferTrans.PARTICULARS, "Restore Stock -" + restoreID);
                        txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                        txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                        transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                        if (transferList != null && transferList.size() > 0) {
                            doDebitCredit(transferList, _branchCode, map, restoreID);
                        }
                    }
                    getTransDetails(restoreID);
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
        data.put(CommonConstants.SCREEN, "Restore Stock");
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
        int balance = 0;
        stockMap.put("RESTORE_ID", CommonUtil.convertObjToStr(map.get("RESTORE_ID")));
        List stockLst = (List) sqlMap.executeQueryForList("getSelectRestoreStock", stockMap);
        if (stockLst != null && stockLst.size() > 0) {
            for (int i = 0; i < stockLst.size(); i++) {
                stockMap = new HashMap();
                stockMap = (HashMap) stockLst.get(i);
                sqlMap.executeUpdate("updateRestoreStockQuant", stockMap);
                sqlMap.executeUpdate("updatePvStockQuant", stockMap);
                sqlMap.executeUpdate("updatePvStockBalQty", stockMap);
            }
        }
    }
    
     private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
     
    private String getRestoreID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "RESTORE_ID");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }
    
    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        map.put("RESTORE_ID", map.get("RESTORE_ID"));
        List restoreList = (List) sqlMap.executeQueryForList("getRestoreDetailsForStock", map);
        if (restoreList != null && restoreList.size() > 0) {
            returnMap.put("RESTORE_LIST", restoreList);
        }
        return returnMap;
    }
}
