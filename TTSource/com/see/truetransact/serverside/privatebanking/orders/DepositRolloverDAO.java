/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositRolloverDAO.java
 *
 * Created on Thu Jun 17 10:24:49 GMT+05:30 2004
 */
package com.see.truetransact.serverside.privatebanking.orders;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.privatebanking.orders.DepositRolloverTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.CommonUtil;

/**
 * DepositRollover DAO.
 *
 */
public class DepositRolloverDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private DepositRolloverTO objDepositRolloverTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private List list;
    private IDGenerateDAO dao;
    private HashMap where;

    /**
     * Creates a new instance of DepositRolloverDAO
     */
    public DepositRolloverDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        list = (List) sqlMap.executeQueryForList("getSelectDepositRolloverTO", where);
        returnMap.put("DepositRolloverTO", list);
        map = null;
        where = null;
        list = null;
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            final String order_ID = getOrderID();
            objDepositRolloverTO.setOrdId(order_ID);
            objDepositRolloverTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(objDepositRolloverTO.toString());
            logTO.setPrimaryKey(objDepositRolloverTO.getKeyData());
            logTO.setStatus(objDepositRolloverTO.getCommand());
            sqlMap.executeUpdate("insertDepositRolloverTO", objDepositRolloverTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getOrderID() throws Exception {
        dao = new IDGenerateDAO();
        where = new HashMap();
        where.put("WHERE", "PVT_ORDER_ID");
        String str = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        where = null;
        dao = null;
        return str;
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objDepositRolloverTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objDepositRolloverTO.toString());
            logTO.setPrimaryKey(objDepositRolloverTO.getKeyData());
            logTO.setStatus(objDepositRolloverTO.getCommand());
            sqlMap.executeUpdate("updateDepositRolloverTO", objDepositRolloverTO);
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
            objDepositRolloverTO.setStatus(CommonConstants.STATUS_DELETED);
            logTO.setData(objDepositRolloverTO.toString());
            logTO.setPrimaryKey(objDepositRolloverTO.getKeyData());
            logTO.setStatus(objDepositRolloverTO.getCommand());
            sqlMap.executeUpdate("deleteDepositRolloverTO", objDepositRolloverTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /*
     * public static void main(String str[]) { try { DepositRolloverDAO
     * objDepositRolloverDAO = new DepositRolloverDAO(); DepositRolloverTO
     * objDepositRolloverTO = new DepositRolloverTO(); TOHeader toHeader = new
     * TOHeader(); toHeader.setCommand(CommonConstants.TOSTATUS_DELETE);
     * objDepositRolloverTO.setTOHeader(toHeader);
     *
     * objDepositRolloverTO.setOrdId("OI001001");
     * objDepositRolloverTO.setMember("TT");
     * objDepositRolloverTO.setRelationship("tt");
     * objDepositRolloverTO.setOrderType("tt");
     * objDepositRolloverTO.setContactMode("tt");
     * objDepositRolloverTO.setContactDt(DateUtil.getDateMMDDYYYY(null));
     * objDepositRolloverTO.setContactHr("tt");
     * objDepositRolloverTO.setContactMins("TT");
     * objDepositRolloverTO.setClientContact("TT");
     * objDepositRolloverTO.setPhoneExt("TT");
     * objDepositRolloverTO.setInstructionFrom("tt");
     * objDepositRolloverTO.setSolicited("tt");
     * objDepositRolloverTO.setSourDocDt(DateUtil.getDateMMDDYYYY(null));
     * objDepositRolloverTO.setDescription("TT");
     * objDepositRolloverTO.setAuthSourDoc("TT");
     * objDepositRolloverTO.setSourDocDetails("tt");
     * objDepositRolloverTO.setViewVisual("TT");
     *
     * HashMap hash = new HashMap();
     * hash.put("DepositRolloverTO",objDepositRolloverTO);
     * objDepositRolloverDAO.execute(hash); } catch (Exception ex) {
     * ex.printStackTrace(); } }
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        logDAO = new LogDAO();
        logTO = new LogTO();
        objDepositRolloverTO = (DepositRolloverTO) map.get("DepositRolloverTO");
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        final String command = objDepositRolloverTO.getCommand();
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
        objDepositRolloverTO = null;
        logDAO = null;
        logTO = null;
    }
}