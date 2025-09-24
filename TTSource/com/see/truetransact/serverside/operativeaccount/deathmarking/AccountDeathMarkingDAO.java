/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathMarkingDAO.java
 *
 * Created on Thu Jun 03 15:22:11 GMT+05:30 2004
 */
package com.see.truetransact.serverside.operativeaccount.deathmarking;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.operativeaccount.deathmarking.AccountDeathMarkingTO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverside.common.log.LogDAO;

/**
 * @author Ashok
 *
 */
public class AccountDeathMarkingDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private AccountDeathMarkingTO objTO;
    private LogTO logTO;
    private LogDAO logDAO;

    /**
     * Creates a new instance of DeathMarkingDAO
     */
    public AccountDeathMarkingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectAccountDeathMarkingTO", where);
        returnMap.put("AccountDeathMarkingTO", list);
        list = null;
        where = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("insertAccountDeathMarkingTO", objTO);
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
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateAccountDeathMarkingTO", objTO);
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
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteAccountDeathMarkingTO", objTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        if (map.containsKey("AccountDeathMarkingTO")) {
            objTO = (AccountDeathMarkingTO) map.get("AccountDeathMarkingTO");
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

    private AccountDeathMarkingTO getAccountDeathMarkingData(String actNum) throws Exception {
        List list = (List) sqlMap.executeQueryForList("getSelectAccountDeathMarkingTO", actNum);
        return ((AccountDeathMarkingTO) list.get(0));
    }

    private void authorizeUpdate(AccountDeathMarkingTO objAccountDeathMarkingTO) throws Exception {
        try {
            HashMap updateMap = new HashMap();
            updateMap.put("ACT_NUM", objAccountDeathMarkingTO.getActNum());
            sqlMap.executeUpdate("updateActMaster", updateMap);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    private void authorize(HashMap map) throws Exception {
        String status = (String) map.get(CommonConstants.AUTHORIZESTATUS);
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap dataMap;
        String actNum;
        AccountDeathMarkingTO objAccountDeathMarkingTO = null;
        try {
            sqlMap.startTransaction();
            for (int i = 0; i < selectedList.size(); i++) {
                dataMap = (HashMap) selectedList.get(i);
                actNum = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                dataMap.put(CommonConstants.STATUS, status);
                dataMap.put(CommonConstants.USER_ID, map.get(CommonConstants.USER_ID));
                sqlMap.executeUpdate("authorizeAccountDeathMarking", dataMap);
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    objAccountDeathMarkingTO = getAccountDeathMarkingData(actNum);
                    if (objAccountDeathMarkingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED) || objAccountDeathMarkingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                        authorizeUpdate(objAccountDeathMarkingTO);
                    } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                        objAccountDeathMarkingTO = getAccountDeathMarkingData(actNum);
                        if (objAccountDeathMarkingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                                || objAccountDeathMarkingTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_MODIFIED)) {
                            sqlMap.executeUpdate("rejectAccountDeathMarking", dataMap);
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

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
