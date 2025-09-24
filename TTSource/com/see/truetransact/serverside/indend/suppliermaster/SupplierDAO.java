/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * BorrwingDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.indend.suppliermaster;

import com.see.truetransact.serverside.inwardregister.*;
import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import org.apache.log4j.Logger;

import com.see.truetransact.transferobject.indend.suppliermaster.SupplierTO;
import java.util.HashMap;
import java.util.Date;
/**
 * TokenConfig DAO.
 *
 */
public class SupplierDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private SupplierTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(SupplierDAO.class);
    private Date currDt = null;
    /**
     * Creates a new instance of TokenConfigDAO
     */
    public SupplierDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectSupplyTO", map);
        System.out.println("#########" + list);
        returnMap.put("SupplyTO", list);
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setSupplierNo(getSupplierNo());
            objTO.setBranchId(_branchCode);
            sqlMap.executeUpdate("insertSupplierTO", objTO);
//            logTO.setData(objTO.toString());
//            logTO.setPrimaryKey(objTO.getKeyData());
//            logTO.setStatus(objTO.getStatus());
//            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private String getSupplierNo() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SUPPLIER_NO");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateSupplyTO", objTO);
//            logTO.setData(objTO.toString());
//            logTO.setPrimaryKey(objTO.getKeyData());
//            logTO.setStatus(objTO.getStatus());
//            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            String Statusby = objTO.getStatusBy();
            //objTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deleteSupplyTO", objTO);
//            logTO.setData(objTO.toString());
//            logTO.setPrimaryKey(objTO.getKeyData());
//            logTO.setStatus(objTO.getStatus());
//            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    /*
     * public static void main(String str[]) { try { UserDAO dao = new
     * UserDAO(); HashMap map=new HashMap();
     * map.put(CommonConstants.MAP_NAME,"getSelectTerminalMasterTO");
     * map.put(CommonConstants.MAP_WHERE,"T0001042"); map=dao.executeQuery(map);
     * } catch (Exception ex) { ex.printStackTrace(); } }
     */
    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@ExecuteMap" + map);
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        objTO = (SupplierTO) map.get("SupplierMaster");
        //objTO.setBranCode(_branchCode);
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
            returnMap.put("SUPPLIER_NO", objTO.getSupplierNo());
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            // if(authMap!=null)
            //authorize(authMap);
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
        objTO = null;
        logTO = null;
        logDAO = null;
    }
//    private void authorize(HashMap map) throws Exception {
//        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
//        HashMap AuthMap= new HashMap();
//        AuthMap= (HashMap) selectedList.get(0);
//        System.out.println("@@@@@@@AuthMap"+AuthMap);
//        try {
//            sqlMap.startTransaction();
//            logTO.setData(map.toString());
//            sqlMap.executeUpdate("authorizeEmpTransfer", AuthMap);
//            if(AuthMap.get("STATUS").equals("AUTHORIZED")){
//              sqlMap.executeUpdate("updatePresentBranch", AuthMap);  
//            }
//            sqlMap.commitTransaction();
//        } catch (Exception e) {
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            throw new TransRollbackException(e);
//        }
//    }
}
