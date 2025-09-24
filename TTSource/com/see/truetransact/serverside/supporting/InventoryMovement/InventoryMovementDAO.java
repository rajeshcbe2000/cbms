/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryMovementDAO.java
 *
 * Created on Tue Jan 25 10:29:17 IST 2005
 */
package com.see.truetransact.serverside.supporting.InventoryMovement;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;

import com.see.truetransact.transferobject.supporting.InventoryMovement.InventoryMovementTO;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * RemitStopPayment DAO.
 *
 */
public class InventoryMovementDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private InventoryMovementTO objInventoryMovementTO;
    private String userID = "";
    private String branchID = "";
    private Date currDt = null;
    /**
     * Creates a new instance of RemitStopPaymentDAO
     */
    public InventoryMovementDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        System.out.println("@@@@WHERE" + where);
        List list = (List) sqlMap.executeQueryForList("getSelectInventoryMovementTO", where);
        returnMap.put("InventoryMovementTO", list);
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            final String MISSING_ID = getStopDDId();
            objInventoryMovementTO.setBranchId(_branchCode);
            objInventoryMovementTO.setId(MISSING_ID);
            objInventoryMovementTO.setCreatedBy(userID);
            objInventoryMovementTO.setCreatedDt(currDt);
            objInventoryMovementTO.setStatus(CommonConstants.STATUS_CREATED);

            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertInventoryMovementTO", objInventoryMovementTO);

            objLogTO.setData(objInventoryMovementTO.toString());
            objLogTO.setPrimaryKey(objInventoryMovementTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getStopDDId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "INVENTORY_MOVEMENT_ID");

        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            objInventoryMovementTO.setStatusBy(userID);
            objInventoryMovementTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objInventoryMovementTO.setStatusDt(currDt);

            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateInventoryMovementTO", objInventoryMovementTO);

            objLogTO.setData(objInventoryMovementTO.toString());
            objLogTO.setPrimaryKey(objInventoryMovementTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            objInventoryMovementTO.setStatusBy(userID);
            objInventoryMovementTO.setStatus(CommonConstants.STATUS_DELETED);
            objInventoryMovementTO.setStatusDt(currDt);

            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteInventoryMovementTO", objInventoryMovementTO);

            objLogTO.setData(objInventoryMovementTO.toString());
            objLogTO.setPrimaryKey(objInventoryMovementTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            InventoryMovementDAO dao = new InventoryMovementDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();
        HashMap returnData = null;
        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setSelectedBranchId((String) map.get(CommonConstants.SELECTED_BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        branchID = CommonUtil.convertObjToStr(map.get(CommonConstants.SELECTED_BRANCH_ID));

        objInventoryMovementTO = (InventoryMovementTO) map.get("InventoryMovementTO");
        final String command = objInventoryMovementTO.getCommand();

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(objLogDAO, objLogTO);
            returnData = new HashMap();
            returnData.put("MISSINGID", objInventoryMovementTO.getId());
            System.out.println("@@@@@@@@returnData" + returnData);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(objLogDAO, objLogTO);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(objLogDAO, objLogTO);
        } else {
            throw new NoCommandException();
        }


        objLogDAO = null;
        objLogTO = null;
        destroyObjects();
        return returnData;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objInventoryMovementTO = null;
    }
}
