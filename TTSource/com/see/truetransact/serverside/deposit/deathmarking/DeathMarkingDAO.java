/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathMarkingDAO.java
 *
 * Created on Thu May 27 10:22:24 GMT+05:30 2004
 */
package com.see.truetransact.serverside.deposit.deathmarking;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import org.apache.log4j.Logger;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.CommonUtil;

import com.see.truetransact.transferobject.deposit.deathmarking.DeathMarkingTO;

/**
 * DeathMarking DAO.
 *
 */
public class DeathMarkingDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private DeathMarkingTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger _log = Logger.getLogger(DeathMarkingDAO.class);

    /**
     * Creates a new instance of DeathMarkingDAO
     */
    public DeathMarkingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * This methods returns a HashMap with to object
     */
    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        ArrayList head = null;
        ArrayList data = null;
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectDeathMarkingTO", where);
        returnMap.put("DeathMarkingTO", list);
        list = null;
        where = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    /**
     * Call the mappedstatement related to insertion
     */
    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("insertDeathMarkingTO", objTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            _log.error(e);
            throw e;
        }
    }

    /**
     * Calls the query related to updation
     */
    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateDeathMarkingTO", objTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            _log.error(e);
            throw e;
        }
    }

    /**
     * Calls the query related to deletion
     */
    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteDeathMarkingTO", objTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            _log.error(e);
            throw e;
        }
    }

    public static void main(String str[]) {
        try {
            DeathMarkingDAO dao = new DeathMarkingDAO();
        } catch (Exception ex) {
            _log.error(ex);
        }
    }

    /**
     * This method is called either to do insert,update,delete operation
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        if (map.containsKey("DeathMarkingTO")) {
            objTO = (DeathMarkingTO) map.get("DeathMarkingTO");
            final String command = objTO.getCommand();

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
        } else {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        }
        map = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return null;
    }

    /**
     * This method is used to do Authoirization
     */
    private void authorize(HashMap map) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap;
        String custId;
        DeathMarkingTO objDeathMarkingTO = null;
        try {
            sqlMap.startTransaction();
            for (int i = 0; i < selectedList.size(); i++) {
                dataMap = (HashMap) selectedList.get(i);
                custId = CommonUtil.convertObjToStr(dataMap.get("CUST_ID"));
                dataMap.put(CommonConstants.STATUS, status);
                dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                sqlMap.executeUpdate("authorizeDeathMarking", dataMap);
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    objDeathMarkingTO = getDeathMarkingData(custId);
                    if (objDeathMarkingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED) || objDeathMarkingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                        authorizeUpdate(objDeathMarkingTO);
                    } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                        objDeathMarkingTO = getDeathMarkingData(custId);
                        if (objDeathMarkingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                                || objDeathMarkingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                            sqlMap.executeUpdate("rejectDeathMarking", dataMap);
                        }
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * This is used to Update the customer table's Customer_Status as
     * 'DeathMarked'
     */
    private void authorizeUpdate(DeathMarkingTO objDeathMarkingTO) throws Exception {
        try {
            HashMap updateMap = new HashMap();
            updateMap.put("CUST_ID", objDeathMarkingTO.getCustId());
            sqlMap.executeUpdate("updateCustomerMaster", updateMap);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    /**
     * Returns all the Data of the DeathMarking table
     */
    private DeathMarkingTO getDeathMarkingData(String custId) throws Exception {
        List list = (List) sqlMap.executeQueryForList("getSelectDeathMarkingTO", custId);
        return ((DeathMarkingTO) list.get(0));
    }

    /**
     * This methood is used to retrieve the data from Death_marking Table
     */
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    /**
     * called to freeup the to object
     */
    private void destroyObjects() {
        objTO = null;
    }
}
