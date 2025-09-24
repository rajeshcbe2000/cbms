/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SupplierMasterDAO.java
 *
 * Created on Fri Jun 10 15:52:50 IST 2011
 */
package com.see.truetransact.serverside.inventory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

import com.ibatis.db.sqlmap.SqlMap;

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

import com.see.truetransact.transferobject.inventory.SupplierMasterTO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * SupplierMaster DAO.
 *
 */
public class SupplierMasterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private SupplierMasterTO objTO;
    private String userID = "";
    private Date currDt = null;

    /**
     * Creates a new instance of SupplierMasterDAO
     */
    public SupplierMasterDAO() throws ServiceLocatorException {
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

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            objTO.setStatusBy(userID);
            objTO.setStatusDt(currDt);
            objTO.setCreatedBy(userID);
            objTO.setCreatedDt(currDt);
            sqlMap.executeUpdate("insertSupplierMasterTO", objTO);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
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
            SupplierMasterDAO dao = new SupplierMasterDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        objTO = (SupplierMasterTO) map.get("SupplierMasterTO");
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

        final String command = objTO.getCommand();

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }

        objLogTO.setData(objTO.toString());
        objLogTO.setPrimaryKey(objTO.getKeyData());
        objLogDAO.addToLog(objLogTO);

        objLogTO = null;
        objLogDAO = null;
        map.clear();
        map = null;

        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = CommonUtil.convertObjToStr(obj.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
