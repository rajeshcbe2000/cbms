/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ParameterDAO.java
 *
 * Created on Wed Mar 17 11:06:22 PST 2004
 */
package com.see.truetransact.serverside.clearing;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.clearing.ParameterTO;

/**
 * Parameter DAO. This is used for Parameter Data Access.
 *
 * @author Prasath.T
 *
 */
public class ParameterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ParameterTO parameterTO;

    /**
     * Creates a new instance of ParameterDAO
     */
    public ParameterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    // To get the data from the database 
    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
//        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectParameterTO", map);
        returnMap.put("ParameterTO", list);
        return returnMap;
    }

    // To insert the data from the database
    private void insertData() throws Exception {
        try {
            parameterTO.setStatus(CommonConstants.STATUS_CREATED);
            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertParameterTO", parameterTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    // To update the data from the database
    private void updateData() throws Exception {
        try {
            parameterTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateParameterTO", parameterTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    // To delete the data from the database
    private void deleteData() throws Exception {
        try {
            parameterTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteParameterTO", parameterTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    // To insert or update or delete the data in the database
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        parameterTO = (ParameterTO) map.get("ParameterTO");
        final String command = parameterTO.getCommand();
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

    // To retrive data from the database
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    // To nulify the objects 
    private void destroyObjects() {
        parameterTO = null;
    }

    public static void main(String str[]) {
        try {
            ParameterDAO dao = new ParameterDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
