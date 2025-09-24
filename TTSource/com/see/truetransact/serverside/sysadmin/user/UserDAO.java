/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserDAO.java
 *
 * Created on September 08, 2003, 1:00 PM
 *
 * Modified on April 16, 2004, 10:42 AM
 *
 */
package com.see.truetransact.serverside.sysadmin.user;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.sysadmin.user.UserTO;
import com.see.truetransact.transferobject.sysadmin.user.UserTerminalTO;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * This is used for User Data Access.
 *
 * @author Karthik
 *
 * @modified Pinky
 */
public class UserDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private UserTO userObj;
    private List list;
    // For Maintaining Logs...
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(UserDAO.class);

    /**
     * Creates a new instance of roleDAO
     */
    public UserDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String mapStr = (String) map.get(CommonConstants.MAP_NAME);
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        list = (List) sqlMap.executeQueryForList(mapStr, where);
        returnMap.put(CommonConstants.MAP_NAME, list);
        mapStr = null;
        map = null;
        where = null;
        list = null;
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertUserTO", userObj);
            logTO.setData(userObj.toString());
            logTO.setPrimaryKey(userObj.getKeyData());
            logTO.setStatus(userObj.getStatus());
            logDAO.addToLog(logTO);
            insertUserTerminalTOs(map);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void insertUserTerminalTOs(HashMap map) throws Exception {
        UserTerminalTO userTerminalObj;
        ArrayList userTerminalTOs = (ArrayList) map.get("UserTerminalTOs");
        int size = userTerminalTOs.size();

        //Insert terminals assigned to a particular user.

        try {
            for (int i = 0; i < size; i++) {
                userTerminalObj = (UserTerminalTO) userTerminalTOs.get(i);
                sqlMap.executeUpdate("insertUserTerminalTO", userTerminalObj);
                logTO.setData(userTerminalObj.toString());
                logTO.setPrimaryKey(userTerminalObj.getKeyData());
                logTO.setStatus(userTerminalObj.getStatus());
                logDAO.addToLog(logTO);

            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
        userTerminalObj = null;
        userTerminalTOs = null;
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateUserTO", userObj);
            sqlMap.executeUpdate("deleteUserTerminalTO", userObj.getUserId());
            logTO.setData(userObj.toString());
            logTO.setPrimaryKey(userObj.getKeyData());
            logTO.setStatus(userObj.getStatus());
            logDAO.addToLog(logTO);
            insertUserTerminalTOs(map);
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
            String Statusby = userObj.getStatusBy();
            userObj.setStatusDt(ServerUtil.getCurrentDate(_branchCode));
            userObj.setAuthorizedStatus("REJECTED");
            sqlMap.executeUpdate("deleteUserTO", userObj);
            sqlMap.executeUpdate("updateStatusUserTerminalTO", userObj.getUserId());
            logTO.setData(userObj.toString());
            logTO.setPrimaryKey(userObj.getKeyData());
            logTO.setStatus(userObj.getStatus());
            logDAO.addToLog(logTO);
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
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        logDAO = new LogDAO();
        logTO = new LogTO();
        userObj = (UserTO) map.get("UserTO");
        final String command = userObj.getCommand();
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }
        map = null;
        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        userObj = null;
        logTO = null;
        logDAO = null;
    }
}
