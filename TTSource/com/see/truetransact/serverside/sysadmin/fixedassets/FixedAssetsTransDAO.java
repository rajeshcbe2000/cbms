/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FixedAssetsTransDAO.java
 *
 * Created on Tue Jan 18 17:58:40 IST 2011
 */
package com.see.truetransact.serverside.sysadmin.fixedassets;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Date;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;//trans details
//trans details
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.cash.CashTransaction;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.commonutil.TTException;
//end..

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetDepreciationTO;
import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetSaleTO;
import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetMovementTO;
import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetBreakageTO;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.sysadmin.fixedassets.FixedAssetMovementTO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import oracle.sql.*;
import oracle.jdbc.driver.*;
import com.see.truetransact.commonutil.Dummy;
import com.see.truetransact.clientutil.ClientConstants;

import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.introducer.IntroducerDAO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.deposit.JointAccntTO;
import java.util.List;
import java.util.Date;

/**
 * FixedAssetsTrans DAO.
 *
 */
public class FixedAssetsTransDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private HashMap data;
    private TransactionDAO transactionDAO = null;
    private LinkedHashMap saleMap;
    private LinkedHashMap deleteSaleTableMap;
    private LinkedHashMap depreciationMap;
    private LinkedHashMap deleteDepreciationTableMap;
    private LinkedHashMap movementMap;
    private LinkedHashMap deleteMoveTableMap;
    private LinkedHashMap breakageMap;
    private LinkedHashMap deleteBreakTableMap;
    private FixedAssetMovementTO objFixedAssetMovementTO;
    private FixedAssetDepreciationTO objFixedAssetDepreciationTO;
    private FixedAssetBreakageTO objFixedAssetBreakageTO;
    private FixedAssetSaleTO objFixedAssetSaleTO = null;
    private Iterator depreIterator;
    private String depreKey = new String();
    private ArrayList depriciaionList = new ArrayList();
    private ArrayList saleList = new ArrayList();
    private ArrayList breakageList = new ArrayList();
    private ArrayList movementList = new ArrayList();
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt = null;//trans details
    String batchid = "";
    String saleBatchId = "";
    String deprBatchId = "";
    String movBatchId = "";
    String brkBatchId = "";
    String fdid = "";
    String fdSid = "";
    String fdMid = "";
    String fdBid = "";
    // FixedAssetSaleTO objFixedAssetSaleTO = null; //trans details
    HashMap returnMap; //trans details

    /**
     * Creates a new instance of FixedAssetsTransDAO
     */
    public FixedAssetsTransDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();

    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE); //trans details
        where.put("DEPR_BATCH_ID", map.get("DEPR_BATCH_ID"));
        List depList = (List) sqlMap.executeQueryForList("FixedAssetDepreciation.getSelectFixedAssetDepreciation", where);
        returnMap.put("FixedAssetsDepreciationTO", depList);
        where.put("MOVE_BATCH_ID", map.get("MOVE_BATCH_ID"));
        List movList = (List) sqlMap.executeQueryForList("FixedAssetMovement.getSelectFixedAssetMovement", where);
        returnMap.put("FixedAssetsMovementTO", movList);
        where.put("BREAK_BATCH_ID", map.get("BREAK_BATCH_ID"));
        List brkList = (List) sqlMap.executeQueryForList("FixedAssetBreakage.getSelectFixedAssetBreakage", where);
        returnMap.put("FixedAssetsBreakageTO", brkList);
        //trans details
        if (where.containsKey("FIXED_ASSETS_SALE_ID")) {
            HashMap getRemitTransMap = new HashMap();
            getRemitTransMap.put("TRANS_ID", where.get("FIXED_ASSETS_SALE_ID"));
            getRemitTransMap.put("TRANS_DT", currDt.clone());
            getRemitTransMap.put("BRANCH_CODE", _branchCode);
            System.out.println("@#%$#@%#$%getRemitTransMap:" + getRemitTransMap);
            List list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", getRemitTransMap);
            //            list = sqlMap.executeQueryForList("getSelectRemittanceIssueTransactionTODate", where.get("REP_INT_CLS_NO"));
            if (list != null && list.size() > 0) {
                returnMap.put("TRANSACTION_LIST", list);
            }
        }
        //end..
//            list = (List) sqlMap.executeQueryForList("getSelectFixedAssetDescAuthorize", map);
//        
//        if(list!=null && list.size() > 0){
//            LinkedHashMap ParMap = new LinkedHashMap();
//            System.out.println("@@@list"+list);
//            for(int i = list.size(), j = 0; i > 0; i--,j++){
//                String st= ((FixedAssetsDescriptionTO)list.get(j)).getSlNo();
//                ParMap.put( ((FixedAssetsDescriptionTO)list.get(j)).getSlNo(),list.get(j));
//            }
//            System.out.println("@@@ParMap"+ParMap);
//            returnMap.put("FixedAssetsDescriptionTO",ParMap);
//        }
        return returnMap;
    }

    private void insertData() throws Exception {
        getAllTOs();
        processDepreciationData();
        processSaleData();
        processBreakageData();
        processMovementData();

    }

    private void getAllTOs() throws Exception {
//        objFixedAssetDepreciationTO = (FixedAssetDepreciationTO)data.get("DEPRECIATION_LIST");
//         if(data.containsKey("DEPRECIATION_LIST")){
//            depreciationMap = (LinkedHashMap) data.get("DEPRECIATION_LIST");
//        }
    }

    private String getFixedDepreBatchIdDetails() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "FIXED_ASSETS_DEPRECIATION_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getFixedSaleBatchIdDetails() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "FIXED_ASSETS_SALE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getFixedBreakageBatchIdDetails() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "FIXED_ASSETS_BREAKAGE_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private String getFixedMovementBatchIdDetails() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "FIXED_ASSETS_MOVEMENT_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /**
     * To insert address data
     */
    private void processDepreciationData() throws Exception {
//        if(deleteDepreciationTableMap != null  && deleteDepreciationTableMap.size()>0){
//            addressIterator = deleteDepreciationTableMap.keySet().iterator();
//            for(int i = deleteDepreciationTableMap.size(); i > 0; i--){
//                key = (String)addressIterator.next();
//                objEmployeeMasterAddressTO = (EmployeeMasterAddressTO)deleteDepreciationTableMap.get(key);
//                logTO.setData(objEmployeeMasterAddressTO.toString());
//                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
//                logTO.setStatus(objEmployeeMasterTO.getCommand());
//                sqlMap.executeUpdate("deleteFixedAssetDepreciationTO", objEmployeeMasterAddressTO);
//                logDAO.addToLog(logTO);
//            }
//        }
        if (depriciaionList != null) {
            System.out.println("######## depriciaionList :" + depriciaionList);
            FixedAssetDepreciationTO objFixedAssetDepreciationTO = null;
            String fdBatchId = getFixedDepreBatchIdDetails();
            fdid = fdBatchId;
            System.out.println("######## fdIndId :" + fdBatchId + "kjdsnbfjsn" + fdid);
            for (int i = 0; i < depriciaionList.size(); i++) {
                objFixedAssetDepreciationTO = new FixedAssetDepreciationTO();
                objFixedAssetDepreciationTO = (FixedAssetDepreciationTO) depriciaionList.get(i);
                objFixedAssetDepreciationTO.setDeprBatchId(fdBatchId);
                System.out.println("depreciationTO" + objFixedAssetDepreciationTO);
                sqlMap.executeUpdate("insertFixedAssetDepreciationTO", objFixedAssetDepreciationTO);

            }
        }

    }

    private void processSaleData() throws Exception {
        if (saleList != null) {
            System.out.println("######## saleList :" + saleList);
            //  FixedAssetSaleTO objFixedAssetSaleTO = null;
            String fdBatchId = getFixedSaleBatchIdDetails();
            fdSid = fdBatchId;
            System.out.println("######## fdIndId :" + fdBatchId);
            for (int i = 0; i < saleList.size(); i++) {
                objFixedAssetSaleTO = new FixedAssetSaleTO();
                objFixedAssetSaleTO = (FixedAssetSaleTO) saleList.get(i);
                objFixedAssetSaleTO.setSaleBatchId(fdBatchId);
                objFixedAssetSaleTO.setStatus("CREATED");
                System.out.println("objFixedAssetSaleTO>>>" + objFixedAssetSaleTO);
                sqlMap.executeUpdate("insertFixedAssetSaleTO", objFixedAssetSaleTO);
            }
        }
    }

    private void processBreakageData() throws Exception {
//        if(deleteDepreciationTableMap != null  && deleteDepreciationTableMap.size()>0){
//            addressIterator = deleteDepreciationTableMap.keySet().iterator();
//            for(int i = deleteDepreciationTableMap.size(); i > 0; i--){
//                key = (String)addressIterator.next();
//                objEmployeeMasterAddressTO = (EmployeeMasterAddressTO)deleteDepreciationTableMap.get(key);
//                logTO.setData(objEmployeeMasterAddressTO.toString());
//                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
//                logTO.setStatus(objEmployeeMasterTO.getCommand());
//                sqlMap.executeUpdate("deleteFixedAssetDepreciationTO", objEmployeeMasterAddressTO);
//                logDAO.addToLog(logTO);
//            }
//        }
        if (breakageList != null) {
            System.out.println("######## breakageList :" + breakageList);
            FixedAssetBreakageTO objFixedAssetBreakageTO = null;
            String fdBreakBatchId = getFixedBreakageBatchIdDetails();
            fdBid = fdBreakBatchId;
            System.out.println("######## fdIndId :" + fdBreakBatchId);
            for (int i = 0; i < breakageList.size(); i++) {
                objFixedAssetBreakageTO = new FixedAssetBreakageTO();
                objFixedAssetBreakageTO = (FixedAssetBreakageTO) breakageList.get(i);
                objFixedAssetBreakageTO.setBreakBatchId(fdBreakBatchId);
                System.out.println("breakageTO" + objFixedAssetBreakageTO);
                sqlMap.executeUpdate("insertFixedAssetBreakageTO", objFixedAssetBreakageTO);
            }
        }

    }

    private void processMovementData() throws Exception {
//        if(deleteDepreciationTableMap != null  && deleteDepreciationTableMap.size()>0){
//            addressIterator = deleteDepreciationTableMap.keySet().iterator();
//            for(int i = deleteDepreciationTableMap.size(); i > 0; i--){
//                key = (String)addressIterator.next();
//                objEmployeeMasterAddressTO = (EmployeeMasterAddressTO)deleteDepreciationTableMap.get(key);
//                logTO.setData(objEmployeeMasterAddressTO.toString());
//                logTO.setPrimaryKey(objEmployeeMasterTO.getKeyData());
//                logTO.setStatus(objEmployeeMasterTO.getCommand());
//                sqlMap.executeUpdate("deleteFixedAssetDepreciationTO", objEmployeeMasterAddressTO);
//                logDAO.addToLog(logTO);
//            }
//        }
        if (movementList != null) {
            System.out.println("######## movementList :" + movementList);
            FixedAssetMovementTO objFixedAssetMovementTO = null;
            String fdMoveBatchId = getFixedMovementBatchIdDetails();
            fdMid = fdMoveBatchId;
            System.out.println("######## fdMoveBatchId :" + fdMoveBatchId);
            for (int i = 0; i < movementList.size(); i++) {
                objFixedAssetMovementTO = new FixedAssetMovementTO();
                objFixedAssetMovementTO = (FixedAssetMovementTO) movementList.get(i);
                objFixedAssetMovementTO.setMoveBatchId(fdMoveBatchId);
                System.out.println("breakageTO" + objFixedAssetMovementTO);
                sqlMap.executeUpdate("insertFixedAssetsMovement", objFixedAssetMovementTO);
            }
        }

    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("update map", objFixedAssetDepreciationTO);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            //sqlMap.startTransaction();
            if (saleBatchId != null && (!saleBatchId.equals(""))) {
                objFixedAssetSaleTO = new FixedAssetSaleTO();
                System.out.println("nmn bhjvbhjyy>>>>" + objFixedAssetSaleTO);
                objFixedAssetSaleTO.setStatus("DELETED");
                objFixedAssetSaleTO.setSaleBatchId(saleBatchId);
                sqlMap.executeUpdate("updateFixedAssetSaleDeleteStatus", objFixedAssetSaleTO);
                // sqlMap.commitTransaction();
            }

            if (deprBatchId != null && (!deprBatchId.equals(""))) {
                objFixedAssetDepreciationTO = new FixedAssetDepreciationTO();
                System.out.println("nmn bhjvbhjyy>>>>deprr" + objFixedAssetDepreciationTO);
                objFixedAssetDepreciationTO.setStatus("DELETED");
                objFixedAssetDepreciationTO.setDeprBatchId(deprBatchId);
                sqlMap.executeUpdate("updateFixedAssetDepreciationDeleteStatus", objFixedAssetDepreciationTO);
            }

            if (movBatchId != null && (!movBatchId.equals(""))) {
                objFixedAssetMovementTO = new FixedAssetMovementTO();
                System.out.println("nmn bhjvbhjyy>>>>movvv" + objFixedAssetMovementTO);
                objFixedAssetMovementTO.setStatus("DELETED");
                objFixedAssetMovementTO.setMoveBatchId(movBatchId);
                sqlMap.executeUpdate("updateFixedAssetMovementDeleteStatus", objFixedAssetMovementTO);
            }

            if (brkBatchId != null && (!brkBatchId.equals(""))) {
                objFixedAssetBreakageTO = new FixedAssetBreakageTO();
                System.out.println("nmn bhjvbhjyy>>>>brkkk" + objFixedAssetBreakageTO);
                objFixedAssetBreakageTO.setStatus("DELETED");
                objFixedAssetBreakageTO.setBreakBatchId(brkBatchId);
                sqlMap.executeUpdate("updateFixedAssetBreakageDeleteStatus", objFixedAssetBreakageTO);
            }
            sqlMap.commitTransaction();
            saleBatchId = null;
            deprBatchId = null;
            movBatchId = null;
            System.out.println("nmn bhjvbhjyy>>>>brkkkbatch id... >" + brkBatchId);
            brkBatchId = null;

        } catch (SQLException e) {
            //  sqlMap.rollbackTransaction();
            e.printStackTrace();
            // throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            FixedAssetsTransDAO dao = new FixedAssetsTransDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap obj) throws Exception {
//        objFixedAssetDepreciationTO = new FixedAssetDepreciationTO();
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        try {
            sqlMap.startTransaction();
            System.out.println("braaanchcodeee...jhdf>>>>" + _branchCode);
            currDt = ServerUtil.getCurrentDate(_branchCode);
            System.out.println("######## obj :" + obj);
            returnMap = new HashMap(); //trans details
            final String command = CommonUtil.convertObjToStr(obj.get("COMMAND"));
            if (obj.get("SALE_BATCH_ID") != null) {
                System.out.println("kjhlhfkjwgfkwjij453634534676" + obj.get("SALE_BATCH_ID").toString());
                saleBatchId = obj.get("SALE_BATCH_ID").toString();
            }

            if (obj.get("DEPR_BATCH_ID") != null) {
                System.out.println("kjhlhfkjwgfkwjij453634534676deprrrr" + obj.get("DEPR_BATCH_ID").toString());
                deprBatchId = obj.get("DEPR_BATCH_ID").toString();
            }

            if (obj.get("MOVE_BATCH_ID") != null) {
                System.out.println("kjhlhfkjwgfkwjij453634534676movvvvv" + obj.get("MOVE_BATCH_ID").toString());
                movBatchId = obj.get("MOVE_BATCH_ID").toString();
            }

            if (obj.get("BREAK_BATCH_ID") != null) {
                System.out.println("kjhlhfkjwgfkwjij453634534676brkkkkk" + obj.get("BREAK_BATCH_ID").toString());
                brkBatchId = obj.get("BREAK_BATCH_ID").toString();
            }
            //   System.out.println("kjhlhfkjwgfkwjij453634534676jnjbubyuu44444"+obj.get("BREAK_BATCH_ID").toString());
            System.out.println("kjhlhfkjwgfkwjij" + objFixedAssetSaleTO);
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                depriciaionList = (ArrayList) obj.get("DEPRECIATION_LIST");
                saleList = (ArrayList) obj.get("SALE_LIST");
                breakageList = (ArrayList) obj.get("BREAKAGE_LIST");
                movementList = (ArrayList) obj.get("MOVEMENT_LIST");
                insertData();
                if (saleList != null) {
                    doSaleTransactions(obj); //trans details
                    returnMap.put("FIXED_ASSETS_SALE_ID", objFixedAssetSaleTO.getSaleBatchId()); //trans details
                    System.out.println("saletransac>>>>>>retrnmap" + returnMap);
                }

                if (saleList != null) {
                    returnMap.put("FIXED_ASSETS_SAL_ID", fdSid);
                }
                if (depriciaionList != null) {
                    System.out.println("fdid>>>>>>retrnmap" + fdid);
                    returnMap.put("FIXED_ASSETS_DEPR_ID", fdid);
                }
                if (movementList != null) {
                    returnMap.put("FIXED_ASSETS_MOVE_ID", fdMid);
                }
                if (breakageList != null) {
                    returnMap.put("FIXED_ASSETS_BRK_ID", fdBid);
                }
                //   returnMap = new HashMap(); //trans details


                // insertData();
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                returnMap = new HashMap();
                deleteData();
                return returnMap;
            } else if (obj.containsKey(CommonConstants.AUTHORIZEMAP)) {
                HashMap authMap = (HashMap) obj.get(CommonConstants.AUTHORIZEMAP);
                if (authMap != null) {
                    authorize(authMap);
                }
            }
            System.out.println("saletransac>>>>>>retrnmap222" + returnMap);
            logDAO = new LogDAO();
            logTO = new LogTO();
            logTO.setUserId(CommonUtil.convertObjToStr(obj.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID)));
            logTO.setInitiatedBranch(CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(obj.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(obj.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(obj.get(CommonConstants.SCREEN)));
            logDAO.addToLog(logTO);
            System.out.println("saletransac>>>>>>retrnmap333" + returnMap);
            sqlMap.commitTransaction();
            System.out.println("saletransac>>>>>>retrnmap444" + returnMap);
        } catch (Exception ex) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(ex);
        }
        System.out.println("saletransac>>>>>>retrnmap555" + returnMap);
        destroyObjects();
        // return obj;
        System.out.println("bbbbbbbbbbb" + returnMap);
        return returnMap; //trans details
    }

    //trans details
    private void doSaleTransactions(HashMap map) throws Exception, Exception {


        try {
            logTO = new LogTO();
            logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            System.out.println("command>>ihjkogfw" + objFixedAssetSaleTO);
            System.out.println("mapmapmapINNNNNNNNN to :" + map);
            _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
            if (objFixedAssetSaleTO.getCommand() != null) {
                if (objFixedAssetSaleTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    System.out.println("!@#!@#@# !ID GENERATED:" + objFixedAssetSaleTO.getSaleBatchId());
                    double amtBorrowed = CommonUtil.convertObjToDouble(objFixedAssetSaleTO.getSaleAmount()).doubleValue();
                    System.out.println("@#$ amtBorrowed :" + amtBorrowed);
                    HashMap txMap;
                    HashMap whereMap = new HashMap();
                    whereMap.put("SALE_BATCH_ID", objFixedAssetSaleTO.getSaleBatchId());
                    HashMap acHeads = (HashMap) sqlMap.executeQueryForObject("getFixedAssetsSaleAcHeads", whereMap);
                    String ac_head = CommonUtil.convertObjToStr(acHeads.get("SELLING_AC_ID"));
                    if (ac_head == null || ac_head.equals("")) {
                        throw new TTException("Account heads not set properly...");
                    }
                    TransferTrans objTransferTrans = new TransferTrans();

                    objTransferTrans.setInitiatedBranch(_branchCode);

                    objTransferTrans.setLinkBatchId(objFixedAssetSaleTO.getSaleBatchId());
                    txMap = new HashMap();
                    ArrayList transferList = new ArrayList();

                    LinkedHashMap TransactionDetailsMap = (LinkedHashMap) map.get("TransactionTO");
                    //                            System.out.println("TransactionDetailsMap---->"+TransactionDetailsMap);
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
                                    txMap.put(TransferTrans.PARTICULARS, "To " + objFixedAssetSaleTO.getSaleBatchId() + " Sale");
                                    txMap.put(CommonConstants.USER_ID, logTO.getUserId());
                                    txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                                    txMap.put(TransferTrans.AUTHORIZE_STATUS_2, "ENTERED_AMOUNT");
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
                                        txMap.put("AUTHORIZEREMARKS", "SELLING_AC_ID");
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
                                        //    System.out.println("userid>>>>>hgwsdg"+logTO.getUserId());
                                        System.out.println("branchcode>>>>jhjkqwd" + _branchCode);
                                        objCashTO.setInitTransId(logTO.getUserId());
                                        objCashTO.setBranchId(_branchCode);

                                        objCashTO.setStatusBy(logTO.getUserId());
                                        objCashTO.setStatusDt(currDt);
                                        //                                        objCashTO.setTokenNo(CommonUtil.convertObjToStr(paramMap.get("TOKEN_NO")));
                                        objCashTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
                                        objCashTO.setParticulars("By " + objFixedAssetSaleTO.getSaleBatchId() + " Sale");
                                        objCashTO.setInitiatedBranch(_branchCode);
                                        objCashTO.setInitChannType(CommonConstants.CASHIER);
                                        objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                                        objCashTO.setAcHdId(ac_head);
                                        objCashTO.setInpAmount(objTransactionTO.getTransAmt());
                                        objCashTO.setAmount(objTransactionTO.getTransAmt());
                                        objCashTO.setLinkBatchId(objFixedAssetSaleTO.getSaleBatchId());

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
                                objTransactionTO.setBatchId(objFixedAssetSaleTO.getSaleBatchId());
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
                    System.out.println("objFixedAssetSaleTO.getSaleBatchId------------------->" + objFixedAssetSaleTO.getSaleBatchId());
                    getTransDetails(objFixedAssetSaleTO.getSaleBatchId());
                } else {

                    HashMap shareAcctNoMap = new HashMap();
                    //                    shareAcctNoMap = (HashMap)sqlMap.executeQueryForObject("transferResolvedShare", shareAcctNoMap);
                    //                    sqlMap.executeUpdate("updateShareAcctDetailsTO", shareAcctDetailsTO);
                    double amtBorrowed = CommonUtil.convertObjToDouble(objFixedAssetSaleTO.getSaleAmount()).doubleValue();
                    if (objFixedAssetSaleTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
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
        System.out.println("mjbggyugyu....>>>" + currDt);
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
    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        System.out.println("@@@@@@@AuthMap" + AuthMap);
        try {
//            if(deleteDepreciationTableMap!=null){
//                System.out.println("Inside Authorize Delete");
//                ArrayList addList =new ArrayList(deleteDepreciationTableMap.keySet());
//                for(int i=0;i<addList.size();i++){
//                    FixedAssetDepreciationTO objFixedAssetDepreciationTO = (FixedAssetDepreciationTO) deleteDepreciationTableMap.get(addList.get(i));
//                    sqlMap.executeUpdate("authorizeFixedDepreciation", AuthMap);
//                    logTO.setData(objFixedAssetDepreciationTO.toString());
//                    logTO.setPrimaryKey(objFixedAssetDepreciationTO.getKeyData());
//                    logDAO.addToLog(logTO);
//                }
//            }
            if (AuthMap.get("PAN").toString().equals("DEPR")) {
                sqlMap.executeUpdate("authorizeFixedDepreciation", AuthMap);
            } else if (AuthMap.get("PAN").toString().equals("SALE")) {
                sqlMap.executeUpdate("authorizeFixedSale", AuthMap);
                String status = (String) AuthMap.get("STATUS");
                String linkBatchId = null;
                String appNo = null;
                HashMap cashAuthMap;
                TransactionTO objTransactionTO = null;
                try {
                    //  sqlMap.startTransaction();
                    transactionDAO = new TransactionDAO(CommonConstants.DEBIT);
                    System.out.println("map:" + map);
                    appNo = CommonUtil.convertObjToStr(AuthMap.get("SALE_BATCH_ID"));
                    //   map.put(CommonConstants.STATUS, status);
                    //  map.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                    //  map.put("CURR_DATE", currDt);
                    // System.out.println("status--56---------->"+CommonConstants.USER_ID);
                    //  sqlMap.executeUpdate("authorizeBorrowingRepIntCls", map);
                    linkBatchId = CommonUtil.convertObjToStr(AuthMap.get("SALE_BATCH_ID"));//Transaction Batch Id

                    //Separation of Authorization for Cash and Transfer
                    //Call this in all places that need Authorization for Transaction
                    cashAuthMap = new HashMap();
                    System.out.println("@#$@zxcvzx#$map:" + map);
                    cashAuthMap.put(CommonConstants.BRANCH_ID, _branchCode);
                    cashAuthMap.put(CommonConstants.USER_ID, AuthMap.get("AUTH_BY"));
                    cashAuthMap.put("DAILY", "DAILY");
                    System.out.println("map:" + map);
                    System.out.println("cashAuthMap:" + cashAuthMap);
                    System.out.println("#$%#$%#$%linkBatchId" + linkBatchId);
                    System.out.println("#$%#$%#$%status" + status);
                    TransactionDAO.authorizeCashAndTransfer(linkBatchId, status, cashAuthMap);
                    HashMap transMap = new HashMap();
                    transMap.put("LINK_BATCH_ID", linkBatchId);
                    System.out.println("transMap----------------->" + transMap);
                    sqlMap.executeUpdate("updateInstrumentNO1Transfer", transMap);
                    sqlMap.executeUpdate("updateInstrumentNO1Cash", transMap);
                    transMap = null;
                    System.out.println("appNo----------------->" + appNo);
                    objTransactionTO = new TransactionTO();
                    objTransactionTO.setBatchId(CommonUtil.convertObjToStr(appNo));
                    objTransactionTO.setTransId(CommonUtil.convertObjToStr(linkBatchId));
                    objTransactionTO.setBranchId(_branchCode);
                    System.out.println("objTransactionTO----------------->" + objTransactionTO);
                    sqlMap.executeUpdate("deleteRemittanceIssueTransactionTO", objTransactionTO);
                    if (!status.equals("REJECTED")) {
                    }
                    map = null;
                    //  sqlMap.commitTransaction();
                } catch (Exception e) {
                    //  sqlMap.rollbackTransaction();
                    e.printStackTrace();
                    throw e;
                }
            } else if (AuthMap.get("PAN").toString().equals("MOVEMENT")) {
                sqlMap.executeUpdate("authorizeFixedMovement", AuthMap);
            } else if (AuthMap.get("PAN").toString().equals("BROKAGE")) {
                sqlMap.executeUpdate("authorizeFixedBreakage", AuthMap);
            }

//            if (depreciationMap != null) {
//                System.out.println("Inside Authorize NonDelete");
//                List lst= null;
//                ArrayList addList =new ArrayList(depreciationMap.keySet());
//                for(int i=0;i<addList.size();i++){
//                    FixedAssetDepreciationTO objFixedAssetDepreciationTO = (FixedAssetDepreciationTO) depreciationMap.get(addList.get(i));
//                    System.out.println("DATA CHECK"+objFixedAssetDepreciationTO);
//                    sqlMap.executeUpdate("authorizeFixedDepreciation", AuthMap);
//                    logTO.setData(objFixedAssetDepreciationTO.toString());
//                    logTO.setPrimaryKey(objFixedAssetDepreciationTO.getKeyData());
//                    logDAO.addToLog(logTO);
//                }
//            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objFixedAssetDepreciationTO = null;
    }
//    public java.util.HashMap execute(HashMap obj) throws Exception {
//    }
//    
//    public java.util.HashMap executeQuery(HashMap obj) throws Exception {
//    }
}
