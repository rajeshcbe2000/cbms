/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OutwardClearingTallyDAO.java
 *
 * Created on Tue Mar 30 12:31:31 PST 2004
 */
package com.see.truetransact.serverside.clearing.outwardtally;

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

import com.see.truetransact.transferobject.clearing.outwardtally.OutwardClearingTallyTO;
import com.see.truetransact.transferobject.clearing.outwardtally.OutwardTallyDetailsTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.transaction.clearing.outward.OutwardClearingDAO;
import java.util.Date;
/**
 * OutwardClearingTally DAO.
 *
 */
public class OutwardClearingTallyDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private OutwardClearingTallyTO objOutwardClearingTallyTO;
    private List objList;
    private String scheduleNo = "";
    private Date currDt = null;
    /**
     * Creates a new instance of OutwardClearingTallyDAO
     */
    public OutwardClearingTallyDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    // To get the data from the table Outward Tally
    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectOutwardClearingTally", where);
        returnMap.put("OutwardClearingTallyTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectOutwardTallyDetailsTO", where);
        returnMap.put("OutwardTallyDetailsTO", list);
        return returnMap;
    }

    /* To Generate the auto generated field ScheduleNumber at the time of insertion */
    private String getScheduleNumber() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "OUTWARD.SCHEDULE_NO");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    /* To set the schedule number */

    private void setScheduleNumber(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }
    /* To set the schedule number in the TO */

    private void setScheduleNumberTO(String scheduleNo) throws Exception {
        objOutwardClearingTallyTO.setScheduleNo(scheduleNo);
    }
    /* To set the schedule no from the TO */

    private void setScheduleNumberFromTO() throws Exception {
        setScheduleNumber(objOutwardClearingTallyTO.getScheduleNo());
    }
    // To insert the data into the Table Outward Tally

    private void insertData() throws Exception {
        objOutwardClearingTallyTO.setStatus(CommonConstants.STATUS_CREATED);
        setScheduleNumber(getScheduleNumber());
        setScheduleNumberTO(scheduleNo);
        sqlMap.executeUpdate("insertOutwardClearingTally", objOutwardClearingTallyTO);
        int size = objList.size();
        for (int i = 0; i < size; i++) {
            sqlMap.executeUpdate("insertOutwardTallyDetailsTO", (OutwardTallyDetailsTO) objList.get(i));
        }
    }

    // To update the data into the Table Outward Tally
    private void updateData() throws Exception {
        objOutwardClearingTallyTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objOutwardClearingTallyTO.setStatusDt(currDt);

        sqlMap.executeUpdate("updateOutwardClearingTally", objOutwardClearingTallyTO);
        OutwardTallyDetailsTO otdTO = new OutwardTallyDetailsTO();
        otdTO.setScheduleNo(objOutwardClearingTallyTO.getScheduleNo());
        sqlMap.executeUpdate("deleteOutwardTallyDetailsTO", objOutwardClearingTallyTO);
        int size = objList.size();
        for (int i = 0; i < size; i++) {
            sqlMap.executeUpdate("insertOutwardTallyDetailsTO", (OutwardTallyDetailsTO) objList.get(i));
        }
    }

    // To delete  the data into the Table Outward Tally ie set the status as Deleted
    private void deleteData() throws Exception {
        objOutwardClearingTallyTO.setStatus(CommonConstants.STATUS_DELETED);
        sqlMap.executeUpdate("deleteOutwardClearingTally", objOutwardClearingTallyTO);
    }

    public static void main(String str[]) {
        try {
            OutwardClearingTallyDAO dao = new OutwardClearingTallyDAO();
            HashMap map = new HashMap();
            map.put(CommonConstants.MAP_WHERE, "OS001019");

            map = dao.getData(map);
            System.out.println(map.get("OutwardClearingTallyTO").toString());
            System.out.println(map.get("OutwardTallyDetailsTO").toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // To perform insert or update or delete based on the status of TO
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap execReturnMap = new HashMap();

        try {
            sqlMap.startTransaction();

            objOutwardClearingTallyTO = (OutwardClearingTallyTO) map.get("OutwardClearingTallyTO");
            objList = (List) map.get("OutwardTallyDetailsTO");

            String command = objOutwardClearingTallyTO.getStatus();

            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData();
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                throw new NoCommandException();
            }

            if (map.containsKey("SCHEDULE_NO")) {
                doCloser(map);
            }

            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                execReturnMap.put(CommonConstants.TRANS_ID, objOutwardClearingTallyTO.getScheduleNo());
            }
            destroyObjects();

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
        return execReturnMap;
    }

    public void doCloser(HashMap map) throws Exception {
        sqlMap.executeUpdate("closeOutwardClearingTallyTO", map);

        OutwardClearingDAO owClearing = new OutwardClearingDAO();
        map.put("CLOSE_SCHEDULE", "CLOSE");
        HashMap returnMap = owClearing.execute(map, false);
    }

    // To retrieve data from the table
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }
    // To nullify the created Objects

    private void destroyObjects() {
        objOutwardClearingTallyTO = null;
        objList = null;
    }
}
