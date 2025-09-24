/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SupplierMasterDAO.java
 *
 * Created on Fri Jun 10 15:52:50 IST 2011
 */
package com.see.truetransact.serverside.deposit.rdStandingInstruction;

import com.see.truetransact.serverside.inventory.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;

import com.see.truetransact.transferobject.inventory.SupplierMasterTO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;

/**
 * SupplierMaster DAO.
 *
 */
public class RdStandingInstructionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private SupplierMasterTO objTO;
    private String userID = "";
    private Date currDt = null;
    public List rdList = null;
    public HashMap returnMap; 

    /**
     * Creates a new instance of SupplierMasterDAO
     */
    public RdStandingInstructionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectSupplierMasterTO", where);
        returnMap.put("SupplierMasterTO", list);
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            HashMap proxyResultMap = new HashMap();
            HashMap data ;
            HashMap rdMap;
            String singleTransID = "";
            int size = rdList.size();
            singleTransID = generateLinkID();
            CashTransactionDAO CashDAO;            
            for(int i=0;i<size;i++){
                rdMap = new HashMap();
                data = new HashMap();
                rdMap = (HashMap)rdList.get(i);
                CashDAO = new CashTransactionDAO();
                System.out.println("rdMap%#@%@#%#@"+rdMap);
                rdMap.put("SINGLE_TRANS_ID", singleTransID);
                data.put("CashTransactionTO", setCashTransaction(rdMap,map));
                data.put("DEPOSIT_PENAL_AMT", CommonUtil.convertObjToDouble(rdMap.get("DEPOSIT_PENAL_AMT")));                
                data.put(CommonConstants.MODULE, "Rd Standing Instruction");
                data.put(CommonConstants.SCREEN, "Rd Standing Instruction");
                data.put(CommonConstants.INITIATED_BRANCH, CommonUtil.convertObjToStr(map.get(CommonConstants.INITIATED_BRANCH)));
                data.put("BRANCH_CODE", CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                data.put("SELECTED_BRANCH_ID", CommonUtil.convertObjToStr(map.get("SELECTED_BRANCH_ID")));
                data.put("CASH_TRANSACTION", "TD");
                data.put("MODE", CommonConstants.TOSTATUS_INSERT); 
                data.put("PRODUCTTYPE","TD");             
                data.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
                System.out.println("%#%#%#%#%data%#%#%#%%" + data);
                CashDAO.execute(data, false);
            }  
            returnMap.put("SINGLE_TRANS_ID", singleTransID);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String generateLinkID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, _branchCode);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }
    
    public CashTransactionTO setCashTransaction(HashMap rdMap,HashMap Map) {
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(rdMap.get("FIXED_DEPOSIT_ACHD")));
            objCashTransactionTO.setScreenName("Rd Standing Instruction");
            objCashTransactionTO.setProdId(CommonUtil.convertObjToStr(rdMap.get("PROD_ID")));
            objCashTransactionTO.setProdType("TD");
            objCashTransactionTO.setActNum(CommonUtil.convertObjToStr(rdMap.get("DEPOSIT_NO")));
            //objCashTransactionTO.setInpAmount(CommonUtil.convertObjToDouble(rdMap.get("TOTAL_AMOUNT")));            
            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(rdMap.get("TOTAL_AMOUNT")));
            objCashTransactionTO.setTransType("CREDIT");
            objCashTransactionTO.setBranchId(CommonUtil.convertObjToStr(Map.get(CommonConstants.BRANCH_ID)));
            objCashTransactionTO.setStatusBy(CommonUtil.convertObjToStr(Map.get(CommonConstants.USER_ID)));
            objCashTransactionTO.setInitTransId(CommonUtil.convertObjToStr(Map.get(CommonConstants.USER_ID)));
            //objCashTransactionTO.setParticulars("");
            objCashTransactionTO.setInitiatedBranch(CommonUtil.convertObjToStr(Map.get(CommonConstants.INITIATED_BRANCH)));
            objCashTransactionTO.setLinkBatchId(objCashTransactionTO.getActNum());
            //objCashTransactionTO.setAuthorizeRemarks(CommonUtil.convertObjToStr(getCbmAgentType().getKeyForSelected()));
            objCashTransactionTO.setSingleTransId(CommonUtil.convertObjToStr(rdMap.get("SINGLE_TRANS_ID")));            
            //objCashTransactionTO.setAuthorizeStatus_2("ENTERED_AMOUNT");
            System.out.println("#@@%@@#%@#objCashTransactionTO" + objCashTransactionTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }
    
    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objTO.setStatusBy(userID);
            objTO.setStatusDt(currDt);
            sqlMap.executeUpdate("updateSupplierMasterTO", objTO);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            objTO.setStatusBy(userID);
            objTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deleteSupplierMasterTO", objTO);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            RdStandingInstructionDAO dao = new RdStandingInstructionDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("In RdStandingInstructionDAO %#%#%#"+map);
        //objTO = (SupplierMasterTO) map.get("SupplierMasterTO");
        _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);

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
        if (map.containsKey("RD_FINAL_LIST")) {
            rdList = (List) map.get("RD_FINAL_LIST");
            //  System.out.println("@##$#$% chargeLst #### :" + chargeLst);
        }
        returnMap = new HashMap();
        insertData(map);
//        final String command = objTO.getCommand();
//
////        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
////            insertData(map);
////        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
////            updateData();
////        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
////            deleteData();
////        } else {
////            throw new NoCommandException();
////        }

        //objLogTO.setData(rdList.toString());
        //objLogTO.setPrimaryKey(objTO.getKeyData());
        //objLogDAO.addToLog(objLogTO);

        objLogTO = null;
        objLogDAO = null;
        map.clear();
        map = null;
        rdList = null;
        return returnMap;        
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        returnMap = null;
    }
}
