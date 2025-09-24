/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLLimitDAO.java
 *
 * Created on Wed Jun 08 12:14:52 GMT+05:30 2005
 */
package com.see.truetransact.serverside.generalledger.gllimit;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.HashMap;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.generalledger.gllimit.GLLimitTO;

/**
 * GLLimit DAO.
 *
 */
public class GLLimitDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private GLLimitTO objTO;

    /**
     * Creates a new instance of GLLimitDAO
     */
    public GLLimitDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String mapName = (String) map.get(CommonConstants.MAP_NAME);
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList(mapName, where);
        returnMap.put(CommonConstants.MAP_NAME, list);
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            sqlMap.executeUpdate("insertGLLimitTO", objTO);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            sqlMap.executeUpdate("updateGLLimitTO", objTO);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            sqlMap.executeUpdate("deleteGLLimitTO", objTO);
            sqlMap.commitTransaction();
        } catch (SQLException e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            GLLimitDAO dao = new GLLimitDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("map is" + map);
        ArrayList toList = (ArrayList) map.get("GLLimitTO");
        ArrayList delList = (ArrayList) map.get("DELETED_LIST");
        int size = 0;
        if (toList.size() > 0) {
            size = toList.size();
        }
        int delSize = 0;
        if (delList != null) {
            if (delList.size() > 0) {
                delSize = delList.size();
            }
        }
        String achead = "";
        String user = "";
        for (int i = 0; i < size; i++) {
            objTO = (GLLimitTO) toList.get(i);
            final String command = objTO.getCommand();
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData();
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                throw new NoCommandException();
            }
            objTO = null;
        }
        if (delSize > 0) {
            for (int i = 0; i < delSize; i++) {
                HashMap hmap = new HashMap();
                HashMap delMap = (HashMap) delList.get(i);
                sqlMap.executeUpdate("DeleteGlLimit", delMap);
            }
        }
        destroyObjects();

        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
