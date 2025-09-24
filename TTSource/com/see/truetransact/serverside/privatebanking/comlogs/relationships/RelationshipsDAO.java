/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RelationshipsDAO.java
 *
 * Created on Fri Jul 16 16:21:05 GMT+05:30 2004
 */
package com.see.truetransact.serverside.privatebanking.comlogs.relationships;

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
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.privatebanking.comlogs.relationships.RelationshipsTO;

/**
 * @author Ashok
 *
 */
public class RelationshipsDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private RelationshipsTO objTO;

    /**
     * Creates a new instance of RelationshipsDAO
     */
    public RelationshipsDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectRelationshipsTO", where);
        returnMap.put("RelationshipsTO", list);
        return returnMap;
    }

    /**
     * Returns an Autogenearated Id using IDGeneratorDAO
     */
    private String getRelateId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "PVT_RELATION_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            String id = getRelateId();
            objTO.setRelateId(id);
            sqlMap.executeUpdate("insertRelationshipsTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateRelationshipsTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    private void deleteData() throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteRelationshipsTO", objTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw e;
        }
    }

    public static void main(String str[]) {
        try {
            RelationshipsDAO dao = new RelationshipsDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        objTO = (RelationshipsTO) map.get("RelationshipsTO");
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

        destroyObjects();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
