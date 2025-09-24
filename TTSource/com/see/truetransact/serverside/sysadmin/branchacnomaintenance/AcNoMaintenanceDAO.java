/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AcNoMaintenanceDAO.java
 *
 * Created on Fri Jan 23 16:33:11 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.branchacnomaintenance;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.sysadmin.branchacnomaintenance.AcNoMaintenanceTO;
import com.see.truetransact.transferobject.sysadmin.calender.WeeklyOffTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.TOHeader;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;

/**
 * AcNoMaintenanceTO DAO.
 *
 */
public class AcNoMaintenanceDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private AcNoMaintenanceTO objAcNoMaintenanceTO;
    private WeeklyOffTO objWeeklyOffTO;
    private List list;
    private IDGenerateDAO dao;
    private HashMap where;
    private String userID = "";
    private java.util.Date currDt = null;

    /**
     * Creates a new instance of AcNoMaintenanceTODAO
     */
    public AcNoMaintenanceDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        map.putAll((HashMap) map.get(CommonConstants.MAP_WHERE));
        list = (List) sqlMap.executeQueryForList("getSelectAllBranchAcNoMaintenanceTO", map);
        returnMap.put("AcNoMaintenanceTO", list);
        map = null;
        list = null;
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (objAcNoMaintenanceTO != null) {
                objAcNoMaintenanceTO.setStatus(CommonConstants.STATUS_CREATED);
                objAcNoMaintenanceTO.setStatusBy(userID);
                objAcNoMaintenanceTO.setStatusDt(currDt);
                objAcNoMaintenanceTO.setCreatedBy(userID);
                objAcNoMaintenanceTO.setCreatedDt(currDt);
                objAcNoMaintenanceTO.setAuthorizedBy(userID);
                objAcNoMaintenanceTO.setAuthorizedDt(currDt);
                objAcNoMaintenanceTO.setAuthorizedStatus(CommonConstants.STATUS_AUTHORIZED);
                sqlMap.executeUpdate("insertBranchAcNoMaintenanceTO", objAcNoMaintenanceTO);

                objLogTO.setData(objAcNoMaintenanceTO.toString());
                //            objLogTO.setPrimaryKey(objAcNoMaintenanceTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (objAcNoMaintenanceTO != null) {
                objAcNoMaintenanceTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objAcNoMaintenanceTO.setStatusBy(userID);
                objAcNoMaintenanceTO.setStatusDt(currDt);

                sqlMap.executeUpdate("updateBranchAcNoMaintenanceTO", objAcNoMaintenanceTO);

                objLogTO.setData(objAcNoMaintenanceTO.toString());
                //            objLogTO.setPrimaryKey(objAcNoMaintenanceTO.getKeyData());
                objLogDAO.addToLog(objLogTO);

            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            objAcNoMaintenanceTO.setStatus(CommonConstants.STATUS_DELETED);
            objAcNoMaintenanceTO.setStatusBy(userID);
            objAcNoMaintenanceTO.setStatusDt(currDt);

            sqlMap.executeUpdate("deleteAcNoMaintenanceTO", objAcNoMaintenanceTO);

            objLogTO.setData(objAcNoMaintenanceTO.toString());
//            objLogTO.setPrimaryKey(objAcNoMaintenanceTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    /*
     * public static void main(String str[]) { try { AcNoMaintenanceDAO
     * AcNoMaintenanceDAO = new AcNoMaintenanceDAO(); WeeklyOffTO objWeeklyOffTO
     * = new WeeklyOffTO(); TOHeader toHeader = new TOHeader();
     * objWeeklyOffTO.setTOHeader(toHeader);
     * toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);
     *
     * objWeeklyOffTO.setWeeklyOff("Y"); objWeeklyOffTO.setWeeklyOff1("TT");
     * objWeeklyOffTO.setWeeklyOff2("TT"); objWeeklyOffTO.setHalfDay1("TT");
     * objWeeklyOffTO.setHalfDay2("TT");
     *
     * HashMap hash = new HashMap(); hash.put("WeeklyOffTO",objWeeklyOffTO);
     * System.out.println("hash --> "+hash); AcNoMaintenanceDAO.execute(hash);
     * System.out.println("execute --> "+AcNoMaintenanceDAO); } catch (Exception
     * ex) { ex.printStackTrace(); } }
     */
    public HashMap execute(HashMap map) throws Exception {
        System.out.println("#$#$#$# map in AcNoMaintenanceDAO : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        ArrayList arrayAcNoMaintenanceTOs = null;
        currDt = ServerUtil.getCurrentDate(_branchCode);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        objLogTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        objLogTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        objLogTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        objLogTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        String command = null;

        if (map.containsKey("AcNoMaintenanceTO") && map.get("AcNoMaintenanceTO") != null) {
            arrayAcNoMaintenanceTOs = (ArrayList) map.get("AcNoMaintenanceTO");
        }
        try {
            if (arrayAcNoMaintenanceTOs != null && arrayAcNoMaintenanceTOs.size() > 0) {
                sqlMap.startTransaction();
                for (int i = 0; i < arrayAcNoMaintenanceTOs.size(); i++) {
                    objAcNoMaintenanceTO = (AcNoMaintenanceTO) arrayAcNoMaintenanceTOs.get(i);
                    command = objAcNoMaintenanceTO.getCommand();
                    if (command != null && command.length() > 0) {
                        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                            insertData(objLogDAO, objLogTO);
                        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                            updateData(objLogDAO, objLogTO);
                        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                            deleteData(objLogDAO, objLogTO);
                        } else {
                            throw new NoCommandException();
                        }
                    }
                }
                sqlMap.commitTransaction();
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
        map = null;
        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

//    private HashMap getDataTable(HashMap map) throws Exception {
//        HashMap returnMap = new HashMap();
//        String where = (String) map.get(CommonConstants.MAP_WHERE);
//        list = (List) sqlMap.executeQueryForList("getSelectAcNoMaintenanceTO", DateUtil.getDateMMDDYYYY(where));
//        returnMap.put("AcNoMaintenanceTO", list);
//        map = null;
//        where = null;
//        list = null;
//        return returnMap;
//    }
    private void destroyObjects() {
        objAcNoMaintenanceTO = null;
        objWeeklyOffTO = null;
    }
}