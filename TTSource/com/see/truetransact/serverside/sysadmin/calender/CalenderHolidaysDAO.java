/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CalenderHolidaysDAO.java
 *
 * Created on Fri Jan 23 16:33:11 IST 2004
 */
package com.see.truetransact.serverside.sysadmin.calender;

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
import com.see.truetransact.transferobject.sysadmin.calender.CalenderHolidaysTO;
import com.see.truetransact.transferobject.sysadmin.calender.WeeklyOffTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.TOHeader;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * CalenderHolidaysTO DAO.
 *
 */
public class CalenderHolidaysDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private CalenderHolidaysTO objCalenderHolidaysTO;
    private WeeklyOffTO objWeeklyOffTO;
    private List list;
    private IDGenerateDAO dao;
    private HashMap where;
    private String Holiday_ID;
    private String userID = "";
    private Date currDt = null;
    /**
     * Creates a new instance of CalenderHolidaysTODAO
     */
    public CalenderHolidaysDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        map.put("HOLIDAY_ID", where);
        list = (List) sqlMap.executeQueryForList("getSelectCalenderHolidaysTO", map);
        returnMap.put("CalenderHolidaysTO", list);
        map = null;
        where = null;
        list = null;
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            if (objCalenderHolidaysTO != null) {
                Holiday_ID = getHolidayID();
                objCalenderHolidaysTO.setHolidayId(Holiday_ID);
                objCalenderHolidaysTO.setStatus(CommonConstants.STATUS_CREATED);
                objCalenderHolidaysTO.setStatusBy(userID);
                objCalenderHolidaysTO.setStatusDt(currDt);
                objCalenderHolidaysTO.setCreatedBy(userID);
                objCalenderHolidaysTO.setCreatedDt(currDt);

                sqlMap.executeUpdate("insertCalenderHolidaysTO", objCalenderHolidaysTO);

                objLogTO.setData(objCalenderHolidaysTO.toString());
                objLogTO.setPrimaryKey(objCalenderHolidaysTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
            if (objWeeklyOffTO != null) {
                executeWeeklyOffTO(objLogDAO, objLogTO);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    private String getHolidayID() throws Exception {
        dao = new IDGenerateDAO();
        where = new HashMap();
        where.put("WHERE", "HOLIDAY_ID");
        String str = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        where = null;
        dao = null;
        return str;
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            if (objCalenderHolidaysTO != null) {
                objCalenderHolidaysTO.setStatus(CommonConstants.STATUS_MODIFIED);
                objCalenderHolidaysTO.setStatusBy(userID);
                objCalenderHolidaysTO.setStatusDt(currDt);

                sqlMap.executeUpdate("updateCalenderHolidaysTO", objCalenderHolidaysTO);

                objLogTO.setData(objCalenderHolidaysTO.toString());
                objLogTO.setPrimaryKey(objCalenderHolidaysTO.getKeyData());
                objLogDAO.addToLog(objLogTO);

                Holiday_ID = null;
            }
            if (objWeeklyOffTO != null) {
                executeWeeklyOffTO(objLogDAO, objLogTO);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objCalenderHolidaysTO.setStatus(CommonConstants.STATUS_DELETED);
            objCalenderHolidaysTO.setStatusBy(userID);
            objCalenderHolidaysTO.setStatusDt(currDt);

            sqlMap.executeUpdate("deleteCalenderHolidaysTO", objCalenderHolidaysTO);

            objLogTO.setData(objCalenderHolidaysTO.toString());
            objLogTO.setPrimaryKey(objCalenderHolidaysTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    private void executeWeeklyOffTO(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            if (objWeeklyOffTO != null) {
                HashMap mapData = new HashMap();
                int cnt = 0;
                mapData.put("BRANCH_ID", _branchCode);
                List lstData = (List) sqlMap.executeQueryForList("getWeekoffRecordCnt", mapData);
                if (lstData.size() > 0 && lstData != null) {
                    mapData = (HashMap) lstData.get(0);
                    System.out.println("@@@@@@@@@@@@@@@@@mapData" + mapData);
                    cnt = CommonUtil.convertObjToInt(mapData.get("REC_COUNT"));
                    System.out.println("@@@@@@@@@@@@@@@@@cnt" + cnt);
                }
                System.out.println("@@@@@@@@@@@@@@@@@^^%%cnt" + cnt);
                if (objWeeklyOffTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    if (cnt == 0) {
                        objWeeklyOffTO.setStatus(CommonConstants.STATUS_CREATED);
                        objWeeklyOffTO.setStatusBy(userID);
                        objWeeklyOffTO.setStatusDt(currDt);
                        objWeeklyOffTO.setCreatedBy(userID);
                        objWeeklyOffTO.setCreatedDt(currDt);
                        weeklyOffTOQuery("insertWeeklyOffTO", objWeeklyOffTO);
                    } else {
                        objWeeklyOffTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        objWeeklyOffTO.setStatusBy(userID);
                        objWeeklyOffTO.setStatusDt(currDt);
                        weeklyOffTOQuery("updateWeeklyOffTO", objWeeklyOffTO);
                    }

                } else if (objWeeklyOffTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                    objWeeklyOffTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    objWeeklyOffTO.setStatusBy(userID);
                    objWeeklyOffTO.setStatusDt(currDt);

                    weeklyOffTOQuery("updateWeeklyOffTO", objWeeklyOffTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void weeklyOffTOQuery(String map, WeeklyOffTO objWeeklyOffTO) throws Exception {
        try {
            sqlMap.executeUpdate(map, objWeeklyOffTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * public static void main(String str[]) { try { CalenderHolidaysDAO
     * calenderHolidaysDAO = new CalenderHolidaysDAO(); WeeklyOffTO
     * objWeeklyOffTO = new WeeklyOffTO(); TOHeader toHeader = new TOHeader();
     * objWeeklyOffTO.setTOHeader(toHeader);
     * toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);
     *
     * objWeeklyOffTO.setWeeklyOff("Y"); objWeeklyOffTO.setWeeklyOff1("TT");
     * objWeeklyOffTO.setWeeklyOff2("TT"); objWeeklyOffTO.setHalfDay1("TT");
     * objWeeklyOffTO.setHalfDay2("TT");
     *
     * HashMap hash = new HashMap(); hash.put("WeeklyOffTO",objWeeklyOffTO);
     * System.out.println("hash --> "+hash); calenderHolidaysDAO.execute(hash);
     * System.out.println("execute --> "+calenderHolidaysDAO); } catch
     * (Exception ex) { ex.printStackTrace(); } }
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
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
        if (map.get("WeeklyOffTO") != null) {
            objWeeklyOffTO = (WeeklyOffTO) map.get("WeeklyOffTO");
            command = objWeeklyOffTO.getCommand();
        }

        if (map.containsKey("CalenderHolidaysTO") && map.get("CalenderHolidaysTO") != null) {
            objCalenderHolidaysTO = (CalenderHolidaysTO) map.get("CalenderHolidaysTO");
            command = objCalenderHolidaysTO.getCommand();
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(objLogDAO, objLogTO);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(objLogDAO, objLogTO);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(objLogDAO, objLogTO);
        } else {
            throw new NoCommandException();
        }
//        }
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
//        list = (List) sqlMap.executeQueryForList("getSelectCalenderHolidaysTO", DateUtil.getDateMMDDYYYY(where));
//        returnMap.put("CalenderHolidaysTO", list);
//        map = null;
//        where = null;
//        list = null;
//        return returnMap;
//    }
    private void destroyObjects() {
        objCalenderHolidaysTO = null;
        objWeeklyOffTO = null;
    }
}