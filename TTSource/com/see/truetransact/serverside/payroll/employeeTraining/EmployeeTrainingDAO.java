/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmployeeTrainingDAO.java
 *
 * Created on September 08, 2003, 1:00 PM
 *
 * Modified on April 16, 2004, 10:42 AM
 *
 */
package com.see.truetransact.serverside.payroll.employeeTraining;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.payroll.employeeTraining.EmployeeTrainingTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * EmployeeTrainingDAO
 *
 * @author Karthik
 *
 * @modified Pinky
 * 
 * Modified by anjuanand on 08/12/2014
 */
public class EmployeeTrainingDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private EmployeeTrainingTO ObjEmpTO;
    private List list;
    // For Maintaining Logs...
    private LogDAO logDAO;
    private LogTO logTO;
    private LinkedHashMap deletedTableValues = null;
    private LinkedHashMap tableDetails = null;
    private String command = "";
    private final static Logger log = Logger.getLogger(EmployeeTrainingDAO.class);

    /**
     * Creates a new instance of EmployeeTrainingDAO
     */
    public EmployeeTrainingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap dataMap = new HashMap();
        dataMap.put("TRAINING_ID", map.get("TRAINING_ID"));
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectEmployeeTrainingTO", dataMap);
        returnMap.put("EmpTrainingTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectEmployeeList", dataMap);
        if (list != null && list.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                int st = ((EmployeeTrainingTO) list.get(j)).getSlNo();
                ParMap.put(((EmployeeTrainingTO) list.get(j)).getSlNo(), list.get(j));
            }
            returnMap.put("EmpDetailsTO", ParMap);
        }
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            ObjEmpTO.setEmpTrainingID(getEmpTrainingID());
            sqlMap.executeUpdate("insertEmployeeTrainingTO", ObjEmpTO);
            logTO.setData(ObjEmpTO.toString());
            logTO.setPrimaryKey(ObjEmpTO.getKeyData());
            logTO.setStatus(ObjEmpTO.getStatus());
            insertTableValues(map);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private String getEmpTrainingID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "EMP_TRAINING");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateEmployeeTrainingTO", ObjEmpTO);
            logTO.setData(ObjEmpTO.toString());
            logTO.setPrimaryKey(ObjEmpTO.getKeyData());
            logTO.setStatus(ObjEmpTO.getStatus());
            updateEmpTableValues();
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteEmployeeTrainingDetailsTO", ObjEmpTO);
            sqlMap.executeUpdate("deleteEmployeeTrainingTO", ObjEmpTO);
            logTO.setData(ObjEmpTO.toString());
            logTO.setPrimaryKey(ObjEmpTO.getKeyData());
            deleteTableValues();
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        logDAO = new LogDAO();
        logTO = new LogTO();
        deletedTableValues = (LinkedHashMap) map.get("deletedEmpTableDetails");
        tableDetails = (LinkedHashMap) map.get("EmpTableDetails");
        ObjEmpTO = (EmployeeTrainingTO) map.get("EmpTraining");
        command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
            returnMap.put("TRAINING_ID", ObjEmpTO.getEmpTrainingID());
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        }
        map = null;
        destroyObjects();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        ObjEmpTO = null;
        logTO = null;
        logDAO = null;
        command = "";
    }

    private void insertTableValues(HashMap map) throws Exception {
        if (tableDetails != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                EmployeeTrainingTO objEmpDetailsTO = (EmployeeTrainingTO) tableDetails.get(addList.get(i));
                objEmpDetailsTO.setEmpTrainingID(ObjEmpTO.getEmpTrainingID());
                sqlMap.executeUpdate("insertEmployeeDetailsTO", objEmpDetailsTO);
                logTO.setData(objEmpDetailsTO.toString());
                logTO.setPrimaryKey(objEmpDetailsTO.getKeyData());
                logTO.setStatus(objEmpDetailsTO.getCommand());
                logDAO.addToLog(logTO);
                objEmpDetailsTO = null;
            }
            updateEmpBranchCode();
        }
    }

    private void updateEmpBranchCode() throws Exception {
        try {
            HashMap trainIdMap = new HashMap();
            List empList = null;
            trainIdMap.put("TRAININGID", ObjEmpTO.getEmpTrainingID());
            empList = sqlMap.executeQueryForList("getEmpBranchCode", trainIdMap);
            if (empList != null && empList.size() > 0) {
                HashMap empMap = new HashMap();
                String empId = "";
                String empBranch = "";
                for (int i = 0; i < empList.size(); i++) {
                    empMap = (HashMap) empList.get(i);
                    empId = CommonUtil.convertObjToStr(empMap.get("EMPLOYEEID"));
                    empBranch = CommonUtil.convertObjToStr(empMap.get("BRANCH_CODE"));
                    HashMap empDetailsMap = new HashMap();
                    empDetailsMap.put("EMPID", empId);
                    empDetailsMap.put("EMP_BRANCH", empBranch);
                    empDetailsMap.put("EMP_TRAININGID", ObjEmpTO.getEmpTrainingID());
                    sqlMap.executeUpdate("updateEmpBranch", empDetailsMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateEmpTableValues() throws Exception {
        if (deletedTableValues != null) {
            ArrayList addList = new ArrayList(deletedTableValues.keySet());
            for (int i = 0; i < addList.size(); i++) {
                EmployeeTrainingTO objEmpTrainingTO = (EmployeeTrainingTO) deletedTableValues.get(addList.get(i));
                objEmpTrainingTO.setEmpTrainingID(ObjEmpTO.getEmpTrainingID());
                sqlMap.executeUpdate("deleteEmployeeDetailsTO", objEmpTrainingTO);
                logTO.setData(objEmpTrainingTO.toString());
                logTO.setPrimaryKey(objEmpTrainingTO.getKeyData());
                logTO.setStatus(command);
                logDAO.addToLog(logTO);
                objEmpTrainingTO = null;
            }
        }
        if (tableDetails != null) {
            List lst = null;
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                EmployeeTrainingTO objEmpDetailsTO = (EmployeeTrainingTO) tableDetails.get(addList.get(i));
                logTO.setData(objEmpDetailsTO.toString());
                logTO.setPrimaryKey(objEmpDetailsTO.getKeyData());
                logTO.setStatus(command);
                objEmpDetailsTO.setEmpTrainingID(ObjEmpTO.getEmpTrainingID());
                lst = sqlMap.executeQueryForList("countNoOfEmployee", objEmpDetailsTO);
                int a = CommonUtil.convertObjToInt(lst.get(0));
                if (a <= 0) {
                    sqlMap.executeUpdate("insertEmployeeDetailsTO", objEmpDetailsTO);
                } else {
                    sqlMap.executeUpdate("updateEmployeeDetailsTO", objEmpDetailsTO);
                }
                logDAO.addToLog(logTO);
            }
            updateEmpBranchCode();
        }
    }

    private void deleteTableValues() throws Exception {

        if (tableDetails != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                EmployeeTrainingTO objEmpDetailsTO = (EmployeeTrainingTO) tableDetails.get(addList.get(i));
                objEmpDetailsTO.setEmpTrainingID(ObjEmpTO.getEmpTrainingID());
                objEmpDetailsTO.setEmpStatus("DELETED");
                sqlMap.executeUpdate("deleteEmployeeDetailsTO", objEmpDetailsTO);
                logTO.setData(objEmpDetailsTO.toString());
                logTO.setPrimaryKey(objEmpDetailsTO.getKeyData());
                logTO.setStatus(objEmpDetailsTO.getCommand());
                logDAO.addToLog(logTO);
                objEmpDetailsTO = null;
            }
        }
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            sqlMap.executeUpdate("authorizeEmpTransfer", AuthMap);
            if (AuthMap.get("STATUS").equals("AUTHORIZED")) {
                sqlMap.executeUpdate("updatePresentBranch", AuthMap);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
}
