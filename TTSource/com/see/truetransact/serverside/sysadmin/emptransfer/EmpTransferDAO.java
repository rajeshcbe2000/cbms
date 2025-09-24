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
package com.see.truetransact.serverside.sysadmin.emptransfer;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.sysadmin.emptransfer.EmpTransferTO;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.CommonUtil;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class EmpTransferDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private EmpTransferTO ObjEmpTO;
    private List list;
    // For Maintaining Logs...
    private LogDAO logDAO;
    private LogTO logTO;
    private final static Logger log = Logger.getLogger(EmpTransferDAO.class);
    private Date currDt = null;
    /**
     * Creates a new instance of roleDAO
     */
    public EmpTransferDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectEmpTransferTO", map);
        returnMap.put("EmpTransferTO", list);
        return returnMap;
    }

    private void insertData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            ObjEmpTO.setEmpTransferID(getEmpTransferID());
            sqlMap.executeUpdate("insertEmpTransferTO", ObjEmpTO);
            logTO.setData(ObjEmpTO.toString());
            logTO.setPrimaryKey(ObjEmpTO.getKeyData());
            logTO.setStatus(ObjEmpTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private String getEmpTransferID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "EMP_TRANSFER");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateEmpTransferTO", ObjEmpTO);
            logTO.setData(ObjEmpTO.toString());
            logTO.setPrimaryKey(ObjEmpTO.getKeyData());
            logTO.setStatus(ObjEmpTO.getStatus());
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
            sqlMap.executeUpdate("deleteEmpTransferTO", ObjEmpTO);
            logTO.setData(ObjEmpTO.toString());
            logTO.setPrimaryKey(ObjEmpTO.getKeyData());
            logTO.setStatus(ObjEmpTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
    }

    /*
     * public static void main(String str[]) { try { UserDAO dao = new
     * UserDAO(); HashMap map=new HashMap();
     * map.put(CommonConstants.MAP_NAME,"getSelectTerminalMasterTO");
     * map.put(CommonConstants.MAP_WHERE,"T0001042"); map=dao.executeQuery(map);
     * } catch (Exception ex) { ex.printStackTrace(); } }
     */
    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@ExecuteMap" + map);
        HashMap returnMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        logTO = new LogTO();
        ObjEmpTO = (EmpTransferTO) map.get("EmpTransfer");
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
            returnMap.put("LEAVE_ID", ObjEmpTO.getEmpTransferID());
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
