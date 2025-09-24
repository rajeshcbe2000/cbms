/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGLDAO.java
 *
 * Created on Fri Dec 31 11:43:54 IST 2004
 */
package com.see.truetransact.serverside.generalledger.branchgl;

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

import com.see.truetransact.transferobject.generalledger.branchgl.BranchGLTO;
import com.see.truetransact.transferobject.generalledger.branchgl.BranchGLGroupTO;

/**
 * BranchGL DAO.
 *
 */
public class BranchGLDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private BranchGLTO objTO;
    private BranchGLGroupTO objGroupTO;
    private ArrayList arrayListGLTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private String command;
    private String GROUP_ID;

    /**
     * Creates a new instance of BranchGLDAO
     */
    public BranchGLDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectBranchGLGroupTO", where);
        returnMap.put("BranchGLGroupTO", list);
        return returnMap;
    }

    private String getGroupId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put("WHERE", "BRANCH_GL_ID");
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
                objGroupTO.setGroupId(GROUP_ID);
                command = objGroupTO.getCommand();
                sqlMap.executeUpdate("insertBranchGLGroupTO", objGroupTO);
                objGroupTO = null;
            }
            if (objTO != null) {
                logDAO.addToLog(logTO);
                logTO.setData(objTO.toString());
                logTO.setPrimaryKey(objTO.getKeyData());
                logTO.setStatus(objTO.getCommand());
                objTO.setGroupId(GROUP_ID);
                sqlMap.executeUpdate("insertBranchGLT0", objTO);
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
                sqlMap.executeUpdate("updateBranchGLGroupTO", objGroupTO);
                objGroupTO = null;
            }
            if (objTO != null) {
                logTO.setData(objTO.toString());
                logTO.setPrimaryKey(objTO.getKeyData());
                logTO.setStatus(objTO.getCommand());
                sqlMap.executeUpdate("updateBranchGLTO", objTO);
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
                sqlMap.executeUpdate("deleteBranchGLGroupTO", objGroupTO);
                objGroupTO = null;
            }
            if (objTO != null) {
                logTO.setData(objTO.toString());
                logTO.setPrimaryKey(objTO.getKeyData());
                logTO.setStatus(objTO.getCommand());
                sqlMap.executeUpdate("deleteBranchGLT0", objTO);
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
            BranchGLDAO dao = new BranchGLDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        String command = new String();
        if (map.containsKey("BranchGLGroupTO")) {
            objGroupTO = (BranchGLGroupTO) map.get("BranchGLGroupTO");
            setLog(map);
            command = objGroupTO.getCommand();
            execute(command);
        }
        if (map.containsKey("BranchGLTO")) {
            arrayListGLTO = (ArrayList) map.get("BranchGLTO");
            for (int i = 0; i < arrayListGLTO.size(); i++) {
                objTO = (BranchGLTO) arrayListGLTO.get(i);
                setLog(map);
                command = objTO.getCommand();
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
        objTO = null;
    }
}
