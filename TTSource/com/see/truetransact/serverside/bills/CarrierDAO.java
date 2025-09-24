/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CarrierDAO.java
 *
 * Created on Wed Jan 05 14:59:17 IST 2005
 */
package com.see.truetransact.serverside.bills;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.bills.CarrierTO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * Carrier DAO.
 *
 */
public class CarrierDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private CarrierTO objCarrierTO;
    private String command;
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt = null;
    /**
     * Creates a new instance of CarrierDAO
     */
    public CarrierDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectCarrierTO", where);
        returnMap.put("CarrierTO", list);
        list = null;
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();

            objCarrierTO.setStatus(CommonConstants.STATUS_CREATED);
            objCarrierTO.setStatusDt(currDt);
            sqlMap.executeUpdate("insertCarrierTO", objCarrierTO);
            logTO.setData(objCarrierTO.toString());
            logTO.setPrimaryKey(objCarrierTO.getKeyData());
            logTO.setStatus(command);
            System.out.println("logTO" + logTO);
            logDAO.addToLog(logTO);

            sqlMap.commitTransaction();

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();

            objCarrierTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objCarrierTO.setStatusDt(currDt);
            sqlMap.executeUpdate("updateCarrierTO", objCarrierTO);
            logTO.setData(objCarrierTO.toString());
            logTO.setPrimaryKey(objCarrierTO.getKeyData());
            logTO.setStatus(command);
            logDAO.addToLog(logTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();

            objCarrierTO.setStatus(CommonConstants.STATUS_DELETED);
            objCarrierTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deleteCarrierTO", objCarrierTO);
            logTO.setData(objCarrierTO.toString());
            logTO.setPrimaryKey(objCarrierTO.getKeyData());
            logTO.setStatus(command);
            logDAO.addToLog(logTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            CarrierDAO dao = new CarrierDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        logDAO = new LogDAO();
        setInitialValuesForLogTO(map);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        objCarrierTO = (CarrierTO) map.get("CarrierTO");
        objCarrierTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        command = (String) map.get("MODE");
        logTO.setStatus(command);
        objCarrierTO.setCommand(command);

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }

        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objCarrierTO = null;
        logTO = null;
        logDAO = null;
        command = null;
    }

    /**
     * Sets the values for logTO Object
     */
    private void setInitialValuesForLogTO(HashMap map) throws Exception {
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
    }
}
