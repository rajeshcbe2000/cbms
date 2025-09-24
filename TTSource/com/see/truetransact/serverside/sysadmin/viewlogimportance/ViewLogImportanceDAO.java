/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewLogImportanceDAO.java
 *
 * Created on January 7, 2005, 6:42 PM
 */
package com.see.truetransact.serverside.sysadmin.viewlogimportance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.ibatis.db.sqlmap.cache.CacheModel;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.*;
import com.see.truetransact.transferobject.TOHeader;

import com.see.truetransact.transferobject.common.log.*;
import com.see.truetransact.transferobject.sysadmin.viewlogimportance.*;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author 152713
 */
public class ViewLogImportanceDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private HashMap viewLogImportanceMap;

    /**
     * Creates a new instance of ViewLogImportanceDAO
     */
    public ViewLogImportanceDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);

        List list = (List) sqlMap.executeQueryForList("getSelectViewLogImportanceTO", where);
        returnMap.put("ViewLogImportanceTO", list);
        list = null;

        map = null;
        where = null;
        ServiceLocator.flushCache(sqlMap);

        return returnMap;
    }

    private void executeViewLogImportance() throws Exception {
        ViewLogImportanceTO objViewLogImportanceTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = viewLogImportanceMap.keySet();
            objKeySet = (Object[]) keySet.toArray();

            sqlMap.startTransaction();

            // To retrieve the TermLoanJointAcctTO from the jointAcctMap
            for (int i = viewLogImportanceMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objViewLogImportanceTO = (ViewLogImportanceTO) viewLogImportanceMap.get(objKeySet[j]);
                if (objViewLogImportanceTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
                    objViewLogImportanceTO.setStatus(CommonConstants.STATUS_CREATED);
                    logTO.setData(objViewLogImportanceTO.toString());
                    logTO.setPrimaryKey(objViewLogImportanceTO.getKeyData());
                    logTO.setStatus(CommonConstants.TOSTATUS_INSERT);
                    executeUpdate("insertViewLogImportanceTO", objViewLogImportanceTO);
                    logDAO.addToLog(logTO);
                } else if (objViewLogImportanceTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
                    objViewLogImportanceTO.setStatus(CommonConstants.STATUS_MODIFIED);
                    logTO.setData(objViewLogImportanceTO.toString());
                    logTO.setPrimaryKey(objViewLogImportanceTO.getKeyData());
                    logTO.setStatus(CommonConstants.TOSTATUS_UPDATE);
                    executeUpdate("updateViewLogImportanceTO", objViewLogImportanceTO);
                    logDAO.addToLog(logTO);
                } else if (objViewLogImportanceTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                    objViewLogImportanceTO.setStatus(CommonConstants.STATUS_DELETED);
                    logTO.setData(objViewLogImportanceTO.toString());
                    logTO.setPrimaryKey(objViewLogImportanceTO.getKeyData());
                    logTO.setStatus(CommonConstants.TOSTATUS_DELETE);
                    executeUpdate("deleteViewLogImportanceTO", objViewLogImportanceTO);
                    logDAO.addToLog(logTO);
                }
                objViewLogImportanceTO = null;
            }

            sqlMap.commitTransaction();

            keySet = null;
            objKeySet = null;
            objViewLogImportanceTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeUpdate(String str, Object objTO) throws Exception {
        try {
            sqlMap.executeUpdate(str, objTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        logDAO = new LogDAO();
        logTO = new LogTO();

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));


        viewLogImportanceMap = (HashMap) map.get("ViewLogImportanceTO");

        executeViewLogImportance();

        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        logDAO = null;
        logTO = null;
        viewLogImportanceMap = null;
    }

    public static void main(String[] arg) {
        try {
            HashMap data = new HashMap();
            HashMap whereMap = new HashMap();
            ViewLogImportanceTO viewLogImportanceTO = new ViewLogImportanceTO();
            viewLogImportanceTO.setImpId("1");
            viewLogImportanceTO.setModule("TermLoan");
            viewLogImportanceTO.setScreen("TermLoan");
            viewLogImportanceTO.setActivity("INSERT");
            viewLogImportanceTO.setImportance("NORMAL");
            viewLogImportanceTO.setCommand(CommonConstants.TOSTATUS_DELETE);
            viewLogImportanceTO.setStatus(CommonConstants.STATUS_DELETED);

            data.put(String.valueOf(1), viewLogImportanceTO);
            whereMap.put("ViewLogImportanceTO", data);
            new ViewLogImportanceDAO().execute(whereMap);
            data = null;
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
