/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TradingStockDAO.java
 *
 * Created on 02 March, 2015, 4:50 PM
 */
package com.see.truetransact.serverside.trading.tradingstock;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
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
import com.see.truetransact.transferobject.trading.tradingstock.PhysicalVerificationTO;
import com.see.truetransact.transferobject.trading.tradingstock.TradingStockTO;
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

//import com.transversalnet.db.DatabaseConnection;
//import com.transversalnet.error.TENException;
//import com.transversalnet.services.Group;
//import com.transversalnet.services.ShopMaster;
//import com.transversalnet.services.Supplier;

/**
 *
 * @author Revathi L
 */

public class TradingStockDAO extends TTDAO {
    private static SqlMap sqlMap = null;
    private TradingStockTO objTradingStockTO;
    private LogTO logTO;
    private LogDAO logDAO;
    private Date currDt = null;
    private LinkedHashMap PVTblDetails = null;
    private LinkedHashMap deletePVMap = null;
    HashMap returnMap = new HashMap();
    private final static Logger log = Logger.getLogger(TradingStockDAO.class);
    String pvID = "";
    private TransactionDAO transactionDAO = null;
    
    public TradingStockDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@#@@@@@#@#@#@#@#@#@#@    Map In Dao :"+map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        if (map.containsKey("PV_DATA")) {
            PVTblDetails = (LinkedHashMap) map.get("PV_DATA");
        }
        if (map.containsKey("DELETE_DATA")) {
            deletePVMap = (LinkedHashMap) map.get("DELETE_DATA");
        }
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        }else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap,map);
            }
        } 
        //else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
////            deleteData();
//        } else {
//            throw new NoCommandException();
//        }
        
        if (command.equals(CommonConstants.TOSTATUS_INSERT) && pvID.length()>0) {
                returnMap.put("PV_ID", pvID);
        }

//        destroyObjects();
        return returnMap;
    }
    
    public void insertData(HashMap map) throws Exception {
        try {
            pvID = "";
            pvID = getpvID();
            if (PVTblDetails != null) {
                ArrayList addList = new ArrayList(PVTblDetails.keySet());
                for (int i = 0; i < addList.size(); i++) {
                    PhysicalVerificationTO objPhysicalVerificationTO = (PhysicalVerificationTO) PVTblDetails.get(addList.get(i));
                    objPhysicalVerificationTO.setPhy_ID(pvID);
                    sqlMap.executeUpdate("insertPhysicalVerificationTO", objPhysicalVerificationTO);
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
            sqlMap.startTransaction();
            if (deletePVMap != null && deletePVMap.size()>0) {
                ArrayList addList = new ArrayList(deletePVMap.keySet());
                PhysicalVerificationTO objPhysicalVerificationTO = null;
                for (int i = 0; i < deletePVMap.size(); i++) {
                    objPhysicalVerificationTO = new PhysicalVerificationTO();
                    objPhysicalVerificationTO = (PhysicalVerificationTO) deletePVMap.get(addList.get(i));
                    sqlMap.executeUpdate("deletePhysicalVerificationTO", objPhysicalVerificationTO);
                }
            }
            if (PVTblDetails != null && PVTblDetails.size()>0) {
                ArrayList addList = new ArrayList(PVTblDetails.keySet());
                PhysicalVerificationTO objPhysicalVerificationTO = null;
                for (int i = 0; i < PVTblDetails.size(); i++) {
                    objPhysicalVerificationTO = new PhysicalVerificationTO();
                    objPhysicalVerificationTO = (PhysicalVerificationTO) PVTblDetails.get(addList.get(i));
                    if (objPhysicalVerificationTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        sqlMap.executeUpdate("insertPhysicalVerificationTO", objPhysicalVerificationTO);
                    } else {
                        sqlMap.executeUpdate("updatePhysicalVerificationTO", objPhysicalVerificationTO);
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
    
    private void authorize(HashMap map,HashMap returnDetMap) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        try {
            if (CommonUtil.convertObjToStr(AuthMap.get("STATUS")).equals("AUTHORIZED") && PVTblDetails!=null && PVTblDetails.size()>0) {
                insertTransactionData(AuthMap);
                updateTradingStock(map);
            }
            sqlMap.executeUpdate("authorizePhysicalVerificationTO", AuthMap);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    

    
    private void updateTradingStock(HashMap map) throws Exception {
        HashMap stockMap = new HashMap();
        ArrayList addList = new ArrayList(PVTblDetails.keySet());
        for (int i = 0; i < addList.size(); i++) {
            stockMap = new HashMap();
            PhysicalVerificationTO objPhysicalVerificationTO = (PhysicalVerificationTO) PVTblDetails.get(addList.get(i));
            stockMap.put("RETURN_QTY", CommonUtil.convertObjToStr(objPhysicalVerificationTO.getStock_Diff()));
            stockMap.put("STOCK_ID", CommonUtil.convertObjToStr(objPhysicalVerificationTO.getStockID()));
            stockMap.put("PRODUCT_ID", CommonUtil.convertObjToStr(objPhysicalVerificationTO.getProduct_ID()));
            sqlMap.executeUpdate("updateReturnStockQuant", stockMap);
        } 
    }
    
    private void insertTransactionData(HashMap map) throws Exception {
        try {
            String BRANCH_ID = CommonUtil.convertObjToStr(map.get("BRANCH_CODE"));
            HashMap txMap = new HashMap();
            HashMap transMap = new HashMap();
            String pvID = "";
            pvID = CommonUtil.convertObjToStr(map.get("PV_ID"));
            HashMap acHeadMap = new HashMap();
            HashMap pvMap = new HashMap();
            double totAmount = 0.0;
            double transAmount = 0.0;
            if (pvID.length() > 0) {
                List headList = (List) sqlMap.executeQueryForList("getSelectTradingAcHead", acHeadMap);
                if (headList != null && headList.size() > 0) {
                    acHeadMap = (HashMap) headList.get(0);
                    pvMap.put("PV_ID", pvID);
                        totAmount = CommonUtil.convertObjToDouble(map.get("TOT_AMT"));
                        ArrayList transferList = new ArrayList();
                        TransferTrans transferTrans = new TransferTrans();
                        transferTrans.setInitiatedBranch(BRANCH_ID);
                        TransferTrans objTransferTrans = new TransferTrans();
                        objTransferTrans.setInitiatedBranch(_branchCode);
                        transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                        transactionDAO.setInitiatedBranch(_branchCode);
                        transactionDAO.setCommandMode(CommonConstants.TOSTATUS_INSERT);
                        transactionDAO.setTransactionType(transactionDAO.TRANSFER);
                        transactionDAO.setLinkBatchID(pvID);
                        HashMap tansactionMap = new HashMap();
                        TxTransferTO transferTo = new TxTransferTO();
                        ArrayList TxTransferTO = new ArrayList();
                            if (totAmount > 0) {
                                transAmount = totAmount;
                                txMap = new HashMap();
                                txMap.put(TransferTrans.DR_AC_HD, acHeadMap.get("DAMAGES"));
                                txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                                txMap.put(TransferTrans.PARTICULARS, "Physical Verification -" + pvID);
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
                                txMap.put(TransferTrans.PARTICULARS, "Physical Verification -" + pvID);
                                txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
                                txMap.put(CommonConstants.USER_ID, map.get("USER_ID"));
                                transferList.add(objTransferTrans.getCreditTransferTO(txMap, transAmount));
                                if (transferList != null && transferList.size() > 0) {
                                    doDebitCredit(transferList, _branchCode, map, pvID);
                                }
                            }
                    
                    getTransDetails(pvID);
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
    
     private String getpvID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "PV_ID");
        String releaseNo = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return releaseNo;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }
    
    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@#@#@#@@@#@@ Get Data :"+map);
        HashMap returnMap = new HashMap();
        if (map.containsKey("STOCK")) {
         map.put("PRODUCT_ID", map.get("PRODUCT_ID"));
            List salesList = (List) sqlMap.executeQueryForList("getTradingStockList", map);
            if (salesList != null && salesList.size() > 0) {
                LinkedHashMap ParMap = new LinkedHashMap();
                for (int i = salesList.size(), j = 0; i > 0; i--, j++) {
                    String st = ((TradingStockTO) salesList.get(j)).getStockID();
                    ParMap.put(j + 1, salesList.get(j));
                }
                returnMap.put("STOCK_DETAILS", ParMap);
            }
        }else if (map.containsKey("PVSTOCK")) {
           map.put("PV_ID", map.get("PV_ID")); 
           List pvLst = (List) sqlMap.executeQueryForList("getTradingPVStockDetailsTO", map);
            if (pvLst != null && pvLst.size() > 0) {
                LinkedHashMap ParMap = new LinkedHashMap();
                for (int i = pvLst.size(), j = 0; i > 0; i--, j++) {
                    String st = ((PhysicalVerificationTO) pvLst.get(j)).getPhy_ID();
                    ParMap.put(j + 1, pvLst.get(j));
                }
                returnMap.put("PV_DATA", ParMap);
            }
        }else if (map.containsKey("DEFICITSTOCK")) {
            Date fromDt = null;
            Date toDt = null;
            fromDt = setProperDtFormat((Date) map.get("FROM_DT"));
            toDt = setProperDtFormat((Date) map.get("TO_DT"));
            map.put("FROM_DT", fromDt);
            map.put("TO_DT", toDt);
            HashMap deficitStockMap = new HashMap();
            List pvLst = (List) sqlMap.executeQueryForList("getTradingPVStock", map);
            if (pvLst != null && pvLst.size() > 0) {
                returnMap.put("PV_STOCK_DATA", pvLst);
                //Deficit Stock Details
                HashMap stockMap = new HashMap();
                for (int i = 0; i < pvLst.size(); i++) {
                    stockMap = (HashMap) pvLst.get(i);
                    List deficitLst = (List) sqlMap.executeQueryForList("getDefStockDetails", stockMap);
                    if (deficitLst != null && deficitLst.size() > 0) {
                        deficitStockMap.put(stockMap.get("PV_ID"), deficitLst);
                    }
                }
                if(deficitStockMap!=null && deficitStockMap.size()>0){
                    returnMap.put("DEFICIT_DATA", deficitStockMap);
                }
            }
        }else if (map.containsKey("RESTORESTOCK")) {
            Date fromDt = null;
            Date toDt = null;
            fromDt = setProperDtFormat((Date) map.get("FROM_DT"));
            toDt = setProperDtFormat((Date) map.get("TO_DT"));
            map.put("FROM_DT", fromDt);
            map.put("TO_DT", toDt);
            map.put("PROD_ID", map.get("PROD_ID"));
            HashMap deficitStockMap = new HashMap();
            List pvLst = (List) sqlMap.executeQueryForList("getRestorePVStock", map);
            if (pvLst != null && pvLst.size() > 0) {
                returnMap.put("RESTORE_PV_STOCK_DATA", pvLst);
                //Deficit Stock Details
                HashMap stockMap = new HashMap();
                for (int i = 0; i < pvLst.size(); i++) {
                    stockMap = (HashMap) pvLst.get(i);
                    List restoreLst = (List) sqlMap.executeQueryForList("getRestoreStockDetails", stockMap);
                    if (restoreLst != null && restoreLst.size() > 0) {
                        deficitStockMap.put(stockMap.get("PV_ID"), restoreLst);
                    }
                }
                if(deficitStockMap!=null && deficitStockMap.size()>0){
                    returnMap.put("RESTORE_DATA", deficitStockMap);
                }
            }
        }
        return returnMap;
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
}


