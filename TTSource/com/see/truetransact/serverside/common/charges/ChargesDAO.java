/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ChargesDAO.java
 *
 * Created on Fri Dec 24 10:41:17 IST 2004
 */
package com.see.truetransact.serverside.common.charges;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.common.charges.ChargesTO;
import java.util.Date;
/**
 * Charges DAO.
 *
 */
public class ChargesDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ChargesTO objTO;
    private Date currDt = null;
    /**
     * Creates a new instance of ChargesDAO
     */
    public ChargesDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = null;
        if (map.containsKey(CommonConstants.MAP_NAME)) {
            list = (List) sqlMap.executeQueryForList((String) map.get(CommonConstants.MAP_NAME), where);
        } else {
            list = (List) sqlMap.executeQueryForList("getSelectChargesTO", where);
        }
        returnMap.put("ChargesTO", list);
        return returnMap;
    }

    private void insertData() throws Exception {
        // int returnCount = Integer.parseInt((String)sqlMap.executeQueryForObject("verifyData", objTO));
        //verify if the data inserted is not an overlapping rule
        // if(returnCount > 0)
        //    throw new TTException("Value Entered already exists");
        // else{
        objTO.setStatusDate(currDt);
        sqlMap.executeUpdate("insertChargesTO", objTO);
        // }
    }

    private void updateData() throws Exception {
        sqlMap.executeUpdate("updateChargesTO", objTO);
    }

    private void deleteData() throws Exception {
        objTO.setStatusDate(currDt);
        sqlMap.executeUpdate("deleteChargesTO", objTO);
    }

    public static void main(String str[]) {
        try {
            ChargesDAO dao = new ChargesDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("chargesconfigurationdao#####" + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        ArrayList dataList = (ArrayList) map.get("ChargesTO");
        int listSize = dataList.size();
        currDt = ServerUtil.getCurrentDate(_branchCode);
        sqlMap.startTransaction();
        try {
            for (int i = 0; i < listSize; i++) {
                objTO = (ChargesTO) dataList.get(i);
                System.out.println("objTO : " + objTO.toString());
                if (objTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                    insertData();
                } else if (objTO.getStatus().equals(CommonConstants.STATUS_MODIFIED)) {
                    updateData();
                } else if (objTO.getStatus().equals(CommonConstants.STATUS_DELETED)) {
                    deleteData();
                } else {
                    throw new NoCommandException();
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }

        destroyObjects();
        return null;
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
