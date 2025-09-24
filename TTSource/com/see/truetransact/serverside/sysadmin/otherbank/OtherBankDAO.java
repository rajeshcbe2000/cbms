/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OtherBankDAO.java
 *
 * Created on Thu Dec 30 17:43:45 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.otherbank;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


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

import com.see.truetransact.transferobject.sysadmin.otherbank.OtherBankTO;
import com.see.truetransact.transferobject.sysadmin.otherbank.OtherBankBranchTO;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * OtherBank DAO.
 *
 */
public class OtherBankDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private OtherBankTO objOtherBankTO;
    private OtherBankBranchTO objOtherBankBranchTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private LinkedHashMap otherBankBranchTO = null;// Contains Other Bank Branch Details which the Status is not DELETED
    private LinkedHashMap deletedOtherBankBranchTO = null;// Contains Other Bank Branch Details which the Status is DELETED
    private LinkedHashMap totalOtherBankBranchTO = null;// Contains Both Other Bank Details and Other Bank Branch Details
    private final String TO_DELETED_AT_UPDATE_MODE = "TO_DELETED_AT_UPDATE_MODE";
    private final String TO_NOT_DELETED_AT_UPDATE_MODE = "TO_NOT_DELETED_AT_UPDATE_MODE";

    /**
     * Creates a new instance of OtherBankDAO
     */
    public OtherBankDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectOtherBankTO", where);
        returnMap.put("OtherBankTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectOtherBankBranchTO", where);
        returnMap.put("OtherBankBranchTO", list);
        list = null;

        return returnMap;
    }

    private void insertData(LogDAO logDAO, LogTO logTO, String command) throws Exception {

        try {
            sqlMap.startTransaction();

            if (objOtherBankTO != null) {
                // insert Other Bank
                objOtherBankTO.setStatus(CommonConstants.STATUS_CREATED);
                sqlMap.executeUpdate("insertOtherBankTO", objOtherBankTO);
                logTO.setData(objOtherBankTO.toString());
                logTO.setPrimaryKey(objOtherBankTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
            }
            // insert Other Bank Branch
            insertOtherBankBranch(logDAO, logTO, command);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /**
     * To Insert OtherBankBranch
     */
    private void insertOtherBankBranch(LogDAO logDAO, LogTO logTO, String command) throws Exception {
        if (otherBankBranchTO != null) {
            for (int i = 1, j = otherBankBranchTO.size(); i <= j; i++) {
                OtherBankBranchTO objOtherBankBranchTO = (OtherBankBranchTO) otherBankBranchTO.get(String.valueOf(i));
                objOtherBankBranchTO.setStatus(CommonConstants.STATUS_CREATED);
                sqlMap.executeUpdate("insertOtherBankBranchTO", objOtherBankBranchTO);
                logTO.setData(objOtherBankBranchTO.toString());
                logTO.setPrimaryKey(objOtherBankBranchTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
                objOtherBankBranchTO = null;
            }
        }
    }

    /**
     * To Delete OtherBankBranch
     */
    private void deleteOtherBankBranch(LogDAO logDAO, LogTO logTO, String command) throws Exception {
        if (otherBankBranchTO != null) {
            for (int i = 1, j = otherBankBranchTO.size(); i <= j; i++) {
                OtherBankBranchTO objOtherBankBranchTO = (OtherBankBranchTO) otherBankBranchTO.get(String.valueOf(i));
                objOtherBankBranchTO.setStatus(CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("deleteOtherBankBranchTO", objOtherBankBranchTO);
                logTO.setData(objOtherBankBranchTO.toString());
                logTO.setPrimaryKey(objOtherBankBranchTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
                objOtherBankBranchTO = null;
            }
        }
    }

    /**
     * To Update OtherBankBranch
     */
    private void updateOtherBankBranch(LogDAO logDAO, LogTO logTO, String command) throws Exception {
        if (deletedOtherBankBranchTO != null) {
            for (int i = 1, j = deletedOtherBankBranchTO.size(); i <= j; i++) {
                OtherBankBranchTO objOtherBankBranchTO = (OtherBankBranchTO) deletedOtherBankBranchTO.get(String.valueOf(i));
                objOtherBankBranchTO.setStatus(CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("deleteOtherBankBranchTO", objOtherBankBranchTO);
                logTO.setData(objOtherBankBranchTO.toString());
                logTO.setPrimaryKey(objOtherBankBranchTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
                objOtherBankBranchTO = null;
            }
        }
        if (otherBankBranchTO != null) {
            for (int i = 1, j = otherBankBranchTO.size(); i <= j; i++) {
                OtherBankBranchTO objOtherBankBranchTO = (OtherBankBranchTO) otherBankBranchTO.get(String.valueOf(i));
                if ((objOtherBankBranchTO.getStatus().length() > 0) && (objOtherBankBranchTO.getStatus() != null)) {
                    objOtherBankBranchTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    sqlMap.executeUpdate("updateOtherBankBranchTO", objOtherBankBranchTO);
                    logTO.setData(objOtherBankBranchTO.toString());
                    logTO.setPrimaryKey(objOtherBankBranchTO.getKeyData());
                    logTO.setStatus(command);
                    logDAO.addToLog(logTO);
                    objOtherBankBranchTO = null;
                } else {
                    objOtherBankBranchTO.setStatus(CommonConstants.STATUS_CREATED);
                    sqlMap.executeUpdate("insertOtherBankBranchTO", objOtherBankBranchTO);
                    logTO.setData(objOtherBankBranchTO.toString());
                    logTO.setPrimaryKey(objOtherBankBranchTO.getKeyData());
                    logTO.setStatus(command);
                    logDAO.addToLog(logTO);
                    objOtherBankBranchTO = null;
                }
            }

        }
    }

    private void updateData(LogDAO logDAO, LogTO logTO, String command) throws Exception {
        try {

            sqlMap.startTransaction();

            if (objOtherBankTO != null) {
                // update Other Bank
                objOtherBankTO.setStatus(CommonConstants.STATUS_MODIFIED);
                sqlMap.executeUpdate("updateOtherBankTO", objOtherBankTO);
                logTO.setData(objOtherBankTO.toString());
                logTO.setPrimaryKey(objOtherBankTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
            }

            // update Other Bank Branch
            updateOtherBankBranch(logDAO, logTO, command);


            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO logDAO, LogTO logTO, String command) throws Exception {
        try {
            sqlMap.startTransaction();

            if (objOtherBankTO != null) {
                // delete Other Bank
                objOtherBankTO.setStatus(CommonConstants.STATUS_DELETED);
                sqlMap.executeUpdate("deleteOtherBankTO", objOtherBankTO);
                logTO.setData(objOtherBankTO.toString());
                logTO.setPrimaryKey(objOtherBankTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
            }

            // To delete Other Bank Branch
            deleteOtherBankBranch(logDAO, logTO, command);


            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    //    public static void main(String str[]) {
    //        try {
    //            OtherBankDAO dao = new OtherBankDAO();
    //        } catch (Exception ex) {
    //            ex.printStackTrace();
    //        }
    //    }
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

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        LogDAO logDAO = new LogDAO();
        setInitialValuesForLogTO(map);
        objOtherBankTO = (OtherBankTO) map.get("OtherBankTO");
        totalOtherBankBranchTO = (LinkedHashMap) map.get("OtherBankBranchTO");
        deletedOtherBankBranchTO = (LinkedHashMap) totalOtherBankBranchTO.get("TO_DELETED_AT_UPDATE_MODE");
        otherBankBranchTO = (LinkedHashMap) totalOtherBankBranchTO.get("TO_NOT_DELETED_AT_UPDATE_MODE");

        final String command = (String) map.get("MODE");

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(logDAO, logTO, command);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(logDAO, logTO, command);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(logDAO, logTO, command);
        } else {
            throw new NoCommandException();
        }
        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objOtherBankTO = null;
        objOtherBankBranchTO = null;
        logTO = null;
        logDAO = null;
        otherBankBranchTO = null;
        deletedOtherBankBranchTO = null;
        totalOtherBankBranchTO = null;


    }
}
