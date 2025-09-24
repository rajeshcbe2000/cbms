/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGroupDAO.java
 *
 * Created on Thu Aug 25 11:43:54 IST 2005
 */
package com.see.truetransact.serverside.sysadmin.branchgroup;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.transferobject.sysadmin.branchgroup.BranchGroupTO;
import com.see.truetransact.transferobject.sysadmin.branchgroup.BranchGroupDetailsTO;

/**
 * BranchGL DAO.
 *
 */
public class BranchGroupDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private BranchGroupTO objGroupTO;
    private BranchGroupDetailsTO objGroupDetailsTO;
    private ArrayList arrayListBGDetailsTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private String command;
    private String GROUP_ID;

    /**
     * Creates a new instance of BranchGroupDAO
     */
    public BranchGroupDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectBranchGroupTO", where);
        returnMap.put("BranchGroupTO", list);
        return returnMap;
    }

    private String getGroupId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "BRANCH_GROUP_ID");
        String str = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        return str;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            if (objGroupTO != null) {
                logTO.setData(objGroupTO.toString());
                logTO.setPrimaryKey(objGroupTO.getKeyData());
                logTO.setStatus(objGroupTO.getCommand());
                GROUP_ID = getGroupId();
                objGroupTO.setBranchGroupId(GROUP_ID);
                command = objGroupTO.getCommand();
                sqlMap.executeUpdate("insertBranchGroupTO", objGroupTO);
                objGroupTO = null;
            }
            if (objGroupDetailsTO != null) {
                logDAO.addToLog(logTO);
                logTO.setData(objGroupDetailsTO.toString());
                logTO.setPrimaryKey(objGroupDetailsTO.getKeyData());
                logTO.setStatus(objGroupDetailsTO.getCommand());
                objGroupDetailsTO.setBranchGroupId(GROUP_ID);
                sqlMap.executeUpdate("insertBranchGroupDetailsTO", objGroupDetailsTO);
                logDAO.addToLog(logTO);
            }
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
            if (objGroupTO != null) {
                logTO.setData(objGroupTO.toString());
                logTO.setPrimaryKey(objGroupTO.getKeyData());
                logTO.setStatus(objGroupTO.getCommand());
                sqlMap.executeUpdate("updateBranchGroupTO", objGroupTO);
                objGroupTO = null;
            }
            if (objGroupDetailsTO != null) {
                logTO.setData(objGroupDetailsTO.toString());
                logTO.setPrimaryKey(objGroupDetailsTO.getKeyData());
                logTO.setStatus(objGroupDetailsTO.getCommand());
                sqlMap.executeUpdate("updateBranchGroupDetailsTO", objGroupDetailsTO);
                logDAO.addToLog(logTO);
            }
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
            if (objGroupTO != null) {
                logTO.setData(objGroupTO.toString());
                logTO.setPrimaryKey(objGroupTO.getKeyData());
                logTO.setStatus(objGroupTO.getCommand());
                if (objGroupTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                    objGroupTO.setStatus(CommonConstants.STATUS_MODIFIED);
                } else {
                    objGroupTO.setStatus(CommonConstants.STATUS_DELETED);
                }
                sqlMap.executeUpdate("deleteBranchGroupTO", objGroupTO);
                objGroupTO = null;
            }
            if (objGroupDetailsTO != null) {
                logTO.setData(objGroupDetailsTO.toString());
                logTO.setPrimaryKey(objGroupDetailsTO.getKeyData());
                logTO.setStatus(objGroupDetailsTO.getCommand());
                sqlMap.executeUpdate("deleteBranchGroupDetailsTO", objGroupDetailsTO);
                logDAO.addToLog(logTO);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            BranchGroupDAO dao = new BranchGroupDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        String command = new String();
        if (map.containsKey("BranchGroupTO")) {
            objGroupTO = (BranchGroupTO) map.get("BranchGroupTO");
            setLog(map);
            command = objGroupTO.getCommand();
            execute(command);
        }
        if (map.containsKey("BranchGroupDetailsTO")) {
            arrayListBGDetailsTO = (ArrayList) map.get("BranchGroupDetailsTO");
            for (int i = 0; i < arrayListBGDetailsTO.size(); i++) {
                objGroupDetailsTO = (BranchGroupDetailsTO) arrayListBGDetailsTO.get(i);
                setLog(map);
                command = objGroupDetailsTO.getCommand();
                execute(command);

            }
        }
        destroyObjects();
        return null;
    }

    private void setLog(HashMap map) throws Exception {
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
    }

    private void execute(String command) throws Exception {
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objGroupDetailsTO = null;
    }
}
