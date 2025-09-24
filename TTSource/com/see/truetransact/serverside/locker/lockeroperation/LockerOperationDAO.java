/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TokenConfigDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.locker.lockeroperation;

import java.util.List;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.transferobject.locker.lockeroperation.LockerOperationTO;
import java.util.HashMap;
import java.util.Date;

/**
 * TokenConfig DAO.
 *
 */
public class LockerOperationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LockerOperationTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private ArrayList operationList = null;
    private Date currDt = null;
    /**
     * Creates a new instance of TokenConfigDAO
     */
    public LockerOperationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectLockerOptTO", where);
        returnMap.put("LockerOptTO", list);
//        list = (List) sqlMap.executeQueryForList("getSelectLockerOptDetails", where);
//        returnMap.put("LockerOptDetails", list);
        list = (List) sqlMap.executeQueryForList("getSelectLockerTO", map.get("ISSUE_ID"));
        returnMap.put("LockerTO", list);

        return returnMap;
    }

    private String getLockerOptId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "LOCKER_OPT_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setOptId(getLockerOptId());
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            objTO.setOptDt(currDt);
            objTO.setAuthorizeStatus(null);
            objTO.setAuthorizeBy(null);
            sqlMap.executeUpdate("insertLockerOptTO", objTO);
            ArrayList tempList = null;
            if (operationList != null && operationList.size() > 0) {
                for (int i = 0; i < operationList.size(); i++) {
                    tempList = (ArrayList) operationList.get(i);
                    System.out.println("tempList : " + tempList);
                    //if (tempList.get(2)!=null && CommonUtil.convertObjToStr(tempList.get(2)).length()>0) {
                    HashMap operatedMap = new HashMap();
                    operatedMap.put("OPERATION_ID", objTO.getOptId());
                    operatedMap.put("CUST_ID", tempList.get(0));
                    operatedMap.put("CUST_NAME", tempList.get(1));
                    sqlMap.executeUpdate("insertLockerOptDetailsTO", operatedMap);
                    operatedMap = null;
                    //}
                }
            }
            HashMap where = new HashMap();
            where.put("OPERATION_ID", objTO.getOptId());
            where.put("BRANCH_CODE", objTO.getBranchID());
            List list = (List) sqlMap.executeQueryForList("getSelectLockerOptTO", where);
            if (list != null && list.size() > 0) {
                objTO = (LockerOperationTO) list.get(0);
            }
            list = null;
            where = null;
            tempList = null;
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
            objTO.setLockerOutDt(ServerUtil.getCurrentDateWithTime(_branchCode));
            sqlMap.executeUpdate("updateLockerOptTO", objTO);
            HashMap where = new HashMap();
            where.put("OPERATION_ID", objTO.getOptId());
            where.put("BRANCH_CODE", objTO.getBranchID());
            List list = (List) sqlMap.executeQueryForList("getSelectLockerOptTO", where);
            if (list != null && list.size() > 0) {
                objTO = (LockerOperationTO) list.get(0);
            }
            list = null;
            where = null;
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
            sqlMap.executeUpdate("deleteLockerOptTO", objTO);
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            LockerOperationDAO dao = new LockerOperationDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@#$!! Map in OperationDAO : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        objTO = (LockerOperationTO) map.get("LockerOperationTO");
        operationList = (ArrayList) map.get("OperationList");
        final String command = objTO.getCommand();
        HashMap returnMap = null;
        logDAO = new LogDAO();
        logTO = new LogTO();

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
            returnMap = new HashMap();
            returnMap.put("LOCKER_OPT_ID", objTO.getOptId());
            returnMap.put("LockerOperationTO", objTO);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
            returnMap = new HashMap();
            returnMap.put("LockerOperationTO", objTO);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            throw new NoCommandException();
        }

        destroyObjects();
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
