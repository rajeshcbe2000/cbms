/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ParameterDAO.java
 *
 * Created on Wed Mar 17 11:06:22 PST 2004
 */
package com.see.truetransact.serverside.clearing.banklevel;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.clearing.banklevel.BankClearingParameterTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * Parameter DAO. This is used for Parameter Data Access.
 *
 * @author Prasath.T
 *
 */
public class BankClearingParameterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private BankClearingParameterTO parameterTO;
    private LogDAO logDAO;
    private LogTO logTO;

    /**
     * Creates a new instance of ParameterDAO
     */
    public BankClearingParameterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    // To get the data from the database

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectBankClearingParameterTO", where);
        returnMap.put("BankClearingParameterTO", list);
        return returnMap;
    }
    // To insert the data from the database

    private void insertData() throws Exception {
        try {
            parameterTO.setStatus(CommonConstants.STATUS_CREATED);
            sqlMap.startTransaction();
            logTO.setData(parameterTO.toString());
            logTO.setPrimaryKey(parameterTO.getKeyData());
            logTO.setStatus(parameterTO.getCommand());
            sqlMap.executeUpdate("insertBankClearingParameterTO", parameterTO);
            sqlMap.commitTransaction();
            logDAO.addToLog(logTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    // To update the data from the database

    private void updateData() throws Exception {
        try {
            parameterTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.startTransaction();
            logTO.setData(parameterTO.toString());
            logTO.setPrimaryKey(parameterTO.getKeyData());
            logTO.setStatus(parameterTO.getCommand());
            sqlMap.executeUpdate("updateBankClearingParameterTO", parameterTO);
            sqlMap.commitTransaction();
            logDAO.addToLog(logTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
    // To delete the data from the database

    private void deleteData() throws Exception {
        try {
            parameterTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.startTransaction();
            logTO.setData(parameterTO.toString());
            logTO.setPrimaryKey(parameterTO.getKeyData());
            logTO.setStatus(parameterTO.getCommand());
            sqlMap.executeUpdate("deleteBankClearingParameterTO", parameterTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    // To insert or update or delete the data in the database
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        parameterTO = (BankClearingParameterTO) map.get("BankClearingParameterTO");
        final String command = parameterTO.getCommand();
        logDAO = new LogDAO();
        logTO = new LogTO();

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
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
    // To retrive data from the database

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }
    // To nulify the objects

    private void destroyObjects() {
        parameterTO = null;
    }

    public static void main(String str[]) {
        try {
            BankClearingParameterDAO dao = new BankClearingParameterDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
