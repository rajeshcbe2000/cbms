/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TAMMaintenanceCreateDAO.java
 *
 * Created on Tue Jul 13 10:15:56 GMT+05:30 2004
 */
package com.see.truetransact.serverside.privatebanking.tammaintenance;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.CommonUtil;

import com.see.truetransact.transferobject.privatebanking.tammaintenance.TAMMaintenanceCreateTO;

/**
 * TAMMaintenanceCreate DAO.
 *
 */
public class TAMMaintenanceCreateDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private TAMMaintenanceCreateTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private List list;
    private String PVT_TAM_ID;
    private IDGenerateDAO dao;
    private HashMap where;

    /**
     * Creates a new instance of TAMMaintenanceCreateDAO
     */
    public TAMMaintenanceCreateDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        list = (List) sqlMap.executeQueryForList("getSelectTAMMaintenanceCreateTO", where);
        returnMap.put("TAMMaintenanceCreateTO", list);
        map = null;
        where = null;
        list = null;
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            PVT_TAM_ID = getPvtTamID();
            objTO.setTamId(PVT_TAM_ID);
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("insertTAMMaintenanceCreateTO", objTO);
            PVT_TAM_ID = null;
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getPvtTamID() throws Exception {
        dao = new IDGenerateDAO();
        where = new HashMap();
        where.put("WHERE", "PVT_TAM_ID");
        String str = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        where = null;
        dao = null;
        return str;
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateTAMMaintenanceCreateTO", objTO);
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
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteTAMMaintenanceCreateTO", objTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /*
     * public static void main(String str[]) { try { TAMMaintenanceCreateDAO
     * objTAMMaintenanceCreateDAO = new TAMMaintenanceCreateDAO();
     * TAMMaintenanceCreateTO objTAMMaintenanceCreateTO = new
     * TAMMaintenanceCreateTO(); TOHeader toHeader = new TOHeader();
     * toHeader.setCommand(CommonConstants.TOSTATUS_DELETE);
     * objTAMMaintenanceCreateTO.setTOHeader(toHeader);
     *
     * objTAMMaintenanceCreateTO.setTamId("PTI01106");
     * objTAMMaintenanceCreateTO.setAssetClsId("TT");
     * objTAMMaintenanceCreateTO.setAssetSubClsId("TT");
     * objTAMMaintenanceCreateTO.setTamOrderType("tt");
     * objTAMMaintenanceCreateTO.setTamDefType("y");
     * objTAMMaintenanceCreateTO.setTamStatus("TT");
     * objTAMMaintenanceCreateTO.setStatus("tt");
     * objTAMMaintenanceCreateTO.setStatusBy("TT");
     * objTAMMaintenanceCreateTO.setStatusDt(DateUtil.getDateMMDDYYYY(null));
     * objTAMMaintenanceCreateTO.setAuthorizeStatus("TT");
     * objTAMMaintenanceCreateTO.setAuthorizeBy("TT");
     * objTAMMaintenanceCreateTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(null));
     *
     * HashMap hash = new HashMap();
     * hash.put("TAMMaintenanceCreateTO",objTAMMaintenanceCreateTO);
     * objTAMMaintenanceCreateDAO.execute(hash);
     *
     * } catch (Exception ex) { ex.printStackTrace(); } }
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        logDAO = new LogDAO();
        logTO = new LogTO();
        objTO = (TAMMaintenanceCreateTO) map.get("TAMMaintenanceCreateTO");
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        final String command = objTO.getCommand();
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
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
        objTO = null;
        logDAO = null;
        logTO = null;
    }
}
