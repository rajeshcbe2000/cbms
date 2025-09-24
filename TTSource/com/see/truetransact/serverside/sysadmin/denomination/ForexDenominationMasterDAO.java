/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ForexDenominationMasterDAO.java
 *
 * Created on Thu Jan 27 12:22:03 IST 2005
 */
package com.see.truetransact.serverside.sysadmin.denomination;

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

import com.see.truetransact.transferobject.sysadmin.denomination.ForexDenominationMasterTO;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * ForexDenominationMaster DAO.
 *
 */
public class ForexDenominationMasterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ForexDenominationMasterTO objForexDenominationMasterTO;
    private LogDAO logDAO;
    private LogTO logTO;

    /**
     * Creates a new instance of ForexDenominationMasterDAO
     */
    public ForexDenominationMasterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectForexDenominationMasterTO", where);
        where = null;
        returnMap.put("ForexDenominationMasterTO", list);
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            objForexDenominationMasterTO.setStatus(CommonConstants.STATUS_CREATED);
            sqlMap.executeUpdate("insertForexDenominationMasterTO", objForexDenominationMasterTO);
            logTO.setData(objForexDenominationMasterTO.toString());
            logTO.setPrimaryKey(objForexDenominationMasterTO.getKeyData());
            logTO.setStatus(objForexDenominationMasterTO.getCommand());
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
            objForexDenominationMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.executeUpdate("updateForexDenominationMasterTO", objForexDenominationMasterTO);
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
            objForexDenominationMasterTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.executeUpdate("deleteForexDenominationMasterTO", objForexDenominationMasterTO);
            logTO.setData(objForexDenominationMasterTO.toString());
            logTO.setPrimaryKey(objForexDenominationMasterTO.getKeyData());
            logTO.setStatus(objForexDenominationMasterTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
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

    public static void main(String str[]) {
        try {
            ForexDenominationMasterDAO dao = new ForexDenominationMasterDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * To set User Id, Created By, Created Dt in the TO
     */
    private void setUserIdForTO(ForexDenominationMasterTO objTO, HashMap map) {
        objTO.setCreatedBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        objTO.setStatusBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
//        objTO.setAuthorizedBy(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);

        logDAO = new LogDAO();
        setInitialValuesForLogTO(map);

        objForexDenominationMasterTO = (ForexDenominationMasterTO) map.get("ForexDenominationMasterTO");

        setUserIdForTO(objForexDenominationMasterTO, map);
        final String command = objForexDenominationMasterTO.getCommand();

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
        return getData(obj);
    }

    private void destroyObjects() {
        objForexDenominationMasterTO = null;
        logDAO = null;
        logTO = null;
    }
}
