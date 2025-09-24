/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserDAO.java
 *
 * Created on September 08, 2003, 1:00 PM
 *
 * Modified on April 16, 2004, 10:42 AM
 *
 */
package com.see.truetransact.serverside.sysadmin.emptraining;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.sysadmin.emptraining.EmpTrainingTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.CommonUtil;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * This is used for User Data Access.
 *
 * @author Karthik
 *
 * @modified Pinky
 */
public class EmpTrainingDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private EmpTrainingTO ObjEmpTO;
    private List list;
    // For Maintaining Logs...
    private LogDAO logDAO;
    private LogTO logTO;
    private LinkedHashMap deletedTableValues = null;
    private LinkedHashMap tableDetails = null;
    private String command = "";
    private final static Logger log = Logger.getLogger(EmpTrainingDAO.class);
    private Date currDt = null;
    /**
     * Creates a new instance of roleDAO
     */
    public EmpTrainingDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectEmpTrainingTO", map);
        returnMap.put("EmpTrainingTO", list);
        list = (List) sqlMap.executeQueryForList("getSelectEmpList", map);
        if (list != null && list.size() > 0) {
            LinkedHashMap ParMap = new LinkedHashMap();
            System.out.println("@@@list" + list);
            for (int i = list.size(), j = 0; i > 0; i--, j++) {
                String st = ((EmpTrainingTO) list.get(j)).getSlNo();
                ParMap.put(((EmpTrainingTO) list.get(j)).getSlNo(), list.get(j));
            }
            System.out.println("@@@ParMap" + ParMap);
            returnMap.put("EmpDetailsTO", ParMap);
        }
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            ObjEmpTO.setEmpTrainingID(getEmpTrainingID());
            sqlMap.executeUpdate("insertEmpTrainingTO", ObjEmpTO);
            logTO.setData(ObjEmpTO.toString());
            logTO.setPrimaryKey(ObjEmpTO.getKeyData());
            logTO.setStatus(ObjEmpTO.getStatus());
            insertTableValues();
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
            sqlMap.executeUpdate("updateEmpTrainingTO", ObjEmpTO);
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
            String Statusby = ObjEmpTO.getStatusBy();
            ObjEmpTO.setStatusDt(currDt);
            sqlMap.executeUpdate("deleteEmpTrainingTO", ObjEmpTO);
            logTO.setData(ObjEmpTO.toString());
            logTO.setPrimaryKey(ObjEmpTO.getKeyData());
            logTO.setStatus(ObjEmpTO.getStatus());
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
        System.out.println("@@@@@@@ExecuteMap" + map);
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        deletedTableValues = (LinkedHashMap) map.get("deletedEmpTableDetails");
        tableDetails = (LinkedHashMap) map.get("EmpTableDetails");
        ObjEmpTO = (EmpTrainingTO) map.get("EmpTraining");
        command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
            returnMap.put("EMP_TRAINING_ID", ObjEmpTO.getEmpTrainingID());
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
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        ObjEmpTO = null;
        logTO = null;
        logDAO = null;
        command = "";
    }

    private void insertTableValues() throws Exception {

        if (tableDetails != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                EmpTrainingTO objEmpDetailsTO = (EmpTrainingTO) tableDetails.get(addList.get(i));
                objEmpDetailsTO.setEmpTrainingID(ObjEmpTO.getEmpTrainingID());
                sqlMap.executeUpdate("insertEmpDetailsTO", objEmpDetailsTO);
                logTO.setData(objEmpDetailsTO.toString());
                logTO.setPrimaryKey(objEmpDetailsTO.getKeyData());
                logTO.setStatus(objEmpDetailsTO.getCommand());
                logDAO.addToLog(logTO);
                objEmpDetailsTO = null;
            }
        }
    }

    private void updateEmpTableValues() throws Exception {

        if (deletedTableValues != null) {
            ArrayList addList = new ArrayList(deletedTableValues.keySet());
            for (int i = 0; i < addList.size(); i++) {
                EmpTrainingTO objEmpTrainingTO = (EmpTrainingTO) deletedTableValues.get(addList.get(i));
                objEmpTrainingTO.setEmpStatus(CommonConstants.STATUS_DELETED);
                objEmpTrainingTO.setEmpTrainingID(ObjEmpTO.getEmpTrainingID());
                sqlMap.executeUpdate("deleteEmpDetailsTO", objEmpTrainingTO);
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
                EmpTrainingTO objEmpDetailsTO = (EmpTrainingTO) tableDetails.get(addList.get(i));
                logTO.setData(objEmpDetailsTO.toString());
                logTO.setPrimaryKey(objEmpDetailsTO.getKeyData());
                logTO.setStatus(command);
                objEmpDetailsTO.setEmpTrainingID(ObjEmpTO.getEmpTrainingID());
                lst = sqlMap.executeQueryForList("countNoOfEmp", objEmpDetailsTO);
                int a = CommonUtil.convertObjToInt(lst.get(0));
                if (a <= 0) {
                    sqlMap.executeUpdate("insertEmpDetailsTO", objEmpDetailsTO);
                } else {
                    sqlMap.executeUpdate("updateEmpDetailsTO", objEmpDetailsTO);
                }

                logDAO.addToLog(logTO);
            }
        }
    }

    private void deleteTableValues() throws Exception {

        if (tableDetails != null) {
            ArrayList addList = new ArrayList(tableDetails.keySet());
            for (int i = 0; i < addList.size(); i++) {
                EmpTrainingTO objEmpDetailsTO = (EmpTrainingTO) tableDetails.get(addList.get(i));
                objEmpDetailsTO.setEmpTrainingID(ObjEmpTO.getEmpTrainingID());
                objEmpDetailsTO.setEmpStatus("DELETED");
                sqlMap.executeUpdate("deleteEmpDetailsTO", objEmpDetailsTO);
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
        System.out.println("@@@@@@@AuthMap" + AuthMap);
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
