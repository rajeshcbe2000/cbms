/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveManagementDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.sysadmin.leavemanagement;

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

import com.see.truetransact.transferobject.sysadmin.leavemanagement.LeaveManagementTO;
import java.util.HashMap;

/**
 * TokenConfig DAO.
 *
 */
public class LeaveManagementDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LeaveManagementTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public LeaveManagementDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectLeaveManagementTO", map);
        returnMap.put("LeaveManagementTO", list);
        return returnMap;
    }

    private String getLeaveId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LEAVE_MANGEMENT_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setLeaveID(getLeaveId());
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(objTO.toString());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("insertLeave", objTO);
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
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objTO.toString());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateLeave", objTO);
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
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            logTO.setData(objTO.toString());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteLeave", objTO);
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
            LeaveManagementDAO dao = new LeaveManagementDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@mapINEXECUTE" + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        HashMap returnMap = null;
        returnMap = new HashMap();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        if (map.containsKey("LeaveManagementTO")) {
            objTO = (LeaveManagementTO) map.get("LeaveManagementTO");
            final String command = objTO.getCommand();
            logDAO = new LogDAO();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData();
                returnMap = new HashMap();
                returnMap.put("LEAVE_ID", objTO.getLeaveID());
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                throw new NoCommandException();
            }

            destroyObjects();
        }
        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        }
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        map = null;
        map = (HashMap) selectedList.get(0);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            sqlMap.executeUpdate("authorizeNotPer", map);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void destroyObjects() {
        objTO = null;
    }
}
