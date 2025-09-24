/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositRolloverDetailsDAO.java
 *
 * Created on Wed Jul 07 17:46:29 GMT+05:30 2004
 */
package com.see.truetransact.serverside.privatebanking.orders.details;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;

import com.see.truetransact.transferobject.privatebanking.orders.details.DepositRolloverDetailsTO;

/**
 * DepositRolloverDetails DAO.
 *
 */
public class DepositRolloverDetailsDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private DepositRolloverDetailsTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private List list;

    /**
     * Creates a new instance of DepositRolloverDetailsDAO
     */
    public DepositRolloverDetailsDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        list = (List) sqlMap.executeQueryForList("getSelectDepositRolloverDetailsTO", where);
        returnMap.put("DepositRolloverDetailsTO", list);
        map = null;
        where = null;
        list = null;
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("insertDepositRolloverDetailsTO", objTO);
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
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateDepositRolloverDetailsTO", objTO);
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
            sqlMap.executeUpdate("deleteDepositRolloverDetailsTO", objTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    /*
     * public static void main(String str[]) { try { DepositRolloverDetailsDAO
     * objDepositRolloverDetailsDAO = new DepositRolloverDetailsDAO();
     * DepositRolloverDetailsTO objDepositRolloverDetailsTO = new
     * DepositRolloverDetailsTO(); TOHeader toHeader = new TOHeader();
     * toHeader.setCommand(CommonConstants.TOSTATUS_DELETE);
     * objDepositRolloverDetailsTO.setTOHeader(toHeader);
     *
     * objDepositRolloverDetailsTO.setOrdId("OI001002");
     * objDepositRolloverDetailsTO.setPortfolioLoc("TT");
     * objDepositRolloverDetailsTO.setAssetSubClass("TT");
     * objDepositRolloverDetailsTO.setAccount("tt");
     * objDepositRolloverDetailsTO.setPrincipal(new Double(7));
     * objDepositRolloverDetailsTO.setRollover("TT");
     * objDepositRolloverDetailsTO.setIntEarned(new Double(7));
     * objDepositRolloverDetailsTO.setCspMemoBalance(new Double(5));
     * objDepositRolloverDetailsTO.setStartDt(DateUtil.getDateMMDDYYYY(null));
     * objDepositRolloverDetailsTO.setMaturityDate(DateUtil.getDateMMDDYYYY(null));
     * objDepositRolloverDetailsTO.setRolloverAmount(new Double(5));
     * objDepositRolloverDetailsTO.setPhoneOrder("t");
     * objDepositRolloverDetailsTO.setTenorPeriodType("TT");
     * objDepositRolloverDetailsTO.setTenorDays(new Double(7));
     * objDepositRolloverDetailsTO.setRolloverType("tt");
     * objDepositRolloverDetailsTO.setCurrency("TT");
     * objDepositRolloverDetailsTO.setSpread(new Double(5));
     * objDepositRolloverDetailsTO.setStatusBy("tt");
     * objDepositRolloverDetailsTO.setStatusDt(DateUtil.getDateMMDDYYYY(null));
     * objDepositRolloverDetailsTO.setAuthorizeStatus("TT");
     * objDepositRolloverDetailsTO.setAuthorizeBy("TT");
     * objDepositRolloverDetailsTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY
     * (null));
     *
     * HashMap hash = new HashMap();
     * hash.put("DepositRolloverDetailsTO",objDepositRolloverDetailsTO);
     * objDepositRolloverDetailsDAO.execute(hash);
     *
     * } catch (Exception ex) { ex.printStackTrace(); } }
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        logDAO = new LogDAO();
        logTO = new LogTO();
        objTO = (DepositRolloverDetailsTO) map.get("DepositRolloverDetailsTO");
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
