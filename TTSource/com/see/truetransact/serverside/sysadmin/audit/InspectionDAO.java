/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InspectionDAO.java
 *
 * Created on Wed Jun 09 16:13:30 GMT+05:30 2004
 */
package com.see.truetransact.serverside.sysadmin.audit;

/**
 *
 * @author Ashok
 */
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
import com.see.truetransact.transferobject.sysadmin.audit.BranchInspectionTO;
import com.see.truetransact.transferobject.sysadmin.audit.PerformanceInspectionTO;
import com.see.truetransact.transferobject.sysadmin.audit.JobInspectionTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.CommonUtil;

/**
 * Inspection DAO.
 *
 */
public class InspectionDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private PerformanceInspectionTO objPerformanceInspectionTO;
    private BranchInspectionTO objBranchInspectionTO;
    private JobInspectionTO objJobInspectionTO;
    private ArrayList arrayAuditJobMasterRow;
    private LogDAO logDAO;
    private LogTO logTO;
    private String auditId = "";
    private String command = "";

    /**
     * Creates a new instance of InspectionDAO
     */
    public InspectionDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List performanceList = (List) sqlMap.executeQueryForList("getSelectPerformanceInspectionTO", where);
        List auditBranchList = (List) sqlMap.executeQueryForList("getSelectBranchInspectionTO", where);
        List auditJobList = (List) sqlMap.executeQueryForList("getSelectJobInspectionTO", where);
        returnMap.put("PerformanceInspectionTO", performanceList);
        returnMap.put("BranchInspectionTO", auditBranchList);
        returnMap.put("JobInspectionTO", auditJobList);
        performanceList = null;
        auditBranchList = null;
        auditJobList = null;
        where = null;
        return returnMap;
    }

    /**
     * To get auto generated TerminalID from table
     */
    private String getAuditId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "AUDIT_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    /**
     * Does the insert Operation in the Database
     */
    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            auditId = getAuditId();
            objPerformanceInspectionTO.setAuditId(auditId);
            objBranchInspectionTO.setAuditId(auditId);
            logTO.setData(objPerformanceInspectionTO.toString());
            logTO.setPrimaryKey(objPerformanceInspectionTO.getKeyData());
            logTO.setStatus(objPerformanceInspectionTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("insertPerformanceInspectionTO", objPerformanceInspectionTO);
            logTO.setData(objBranchInspectionTO.toString());
            logTO.setPrimaryKey(objBranchInspectionTO.getKeyData());
            logTO.setStatus(objBranchInspectionTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("insertBranchInspectionTO", objBranchInspectionTO);
            addJobInspectionTO();
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    /**
     * Does the updation in the database
     */
    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            logTO.setData(objPerformanceInspectionTO.toString());
            logTO.setPrimaryKey(objPerformanceInspectionTO.getKeyData());
            logTO.setStatus(objPerformanceInspectionTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("updatePerformanceInspectionTO", objPerformanceInspectionTO);
            logTO.setData(objBranchInspectionTO.toString());
            logTO.setPrimaryKey(objBranchInspectionTO.getKeyData());
            logTO.setStatus(objBranchInspectionTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("updateBranchInspectionTO", objBranchInspectionTO);
            addJobInspectionTO();
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    /**
     * Does the deletion in the DataBase
     */
    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            logTO.setData(objPerformanceInspectionTO.toString());
            logTO.setPrimaryKey(objPerformanceInspectionTO.getKeyData());
            logTO.setStatus(objPerformanceInspectionTO.getCommand());
            logDAO.addToLog(logTO);
            sqlMap.executeUpdate("deletePerformanceInspectionTO", objPerformanceInspectionTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    public static void main(String str[]) {
        try {
            InspectionDAO dao = new InspectionDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method does the appropriate operation either insert,update or delete
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        objPerformanceInspectionTO = (PerformanceInspectionTO) map.get("PerformanceInspectionTO");
        objBranchInspectionTO = (BranchInspectionTO) map.get("BranchInspectionTO");
        arrayAuditJobMasterRow = (ArrayList) map.get("JobInspectionTO");
        logDAO = new LogDAO();
        logTO = new LogTO();

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        command = objPerformanceInspectionTO.getCommand();
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }

        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objPerformanceInspectionTO = null;
        objBranchInspectionTO = null;
        objJobInspectionTO = null;
    }

    private void addJobInspectionTO() throws Exception {
        if (arrayAuditJobMasterRow != null) {
            for (int i = 0; i < arrayAuditJobMasterRow.size(); i++) {
                JobInspectionTO objJobInspectionTO = (JobInspectionTO) arrayAuditJobMasterRow.get(i);
                if (objJobInspectionTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                        objJobInspectionTO.setAuditId(auditId);
                    }
                    sqlMap.executeUpdate("insertJobInspectionTO", objJobInspectionTO);
                } else if (objJobInspectionTO.getStatus().equals(CommonConstants.STATUS_MODIFIED)) {
                    sqlMap.executeUpdate("updateJobInspectionTO", objJobInspectionTO);
                } else if (objJobInspectionTO.getStatus().equals(CommonConstants.STATUS_DELETED)) {
                    sqlMap.executeUpdate("deleteJobInspectionTO", objJobInspectionTO);
                }
            }
        }
    }
}
