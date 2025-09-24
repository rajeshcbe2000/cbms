/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductDAO.java
 *
 * Created on Wed Nov 24 16:51:38 GMT+05:30 2004
 */
package com.see.truetransact.serverside.product.share;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.transferobject.product.share.ShareProductTO;
import com.see.truetransact.transferobject.product.share.ShareProductLoanTO;

/**
 * ShareProduct DAO.
 *
 */
public class ShareProductDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ShareProductTO objTO;
    private ShareProductLoanTO objLoanTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private HashMap loanDataMap = new HashMap();
    private HashMap deletedLoanDataMap;
    private String where = "";

    /**
     * Creates a new instance of ShareProductDAO
     */
    public ShareProductDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * This method execute a Query and returns the resultset in HashMap object
     */
    private HashMap getShareProductData() throws Exception {
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectShareProductTO", where);
        returnMap.put("ShareProductTO", list);
        list = null;
        return returnMap;
    }

    private HashMap getShareProductLoanData() throws Exception {
        HashMap dataMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectShareProductLoan", where);
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                dataMap.put(((ShareProductLoanTO) list.get(i)).getLoanType(), (ShareProductLoanTO) list.get(i));
            }
        }
        return dataMap;
    }

    /**
     * This method is used to insertnew datat into the Table
     */
    private void insertData() throws Exception {
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        logDAO.addToLog(logTO);
        sqlMap.executeUpdate("insertShareProductTO", objTO);
        addLoanData();
    }

    /**
     * This method is used to Edit the already existing data in the table
     */
    private void updateData() throws Exception {
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        logDAO.addToLog(logTO);
        sqlMap.executeUpdate("updateShareProductTO", objTO);
        addLoanData();
    }

    /*
     * This method is used to update the already existing data by making its
     * status to be deleted
     */
    private void deleteData() throws Exception {
        logTO.setData(objTO.toString());
        logTO.setPrimaryKey(objTO.getKeyData());
        logTO.setStatus(objTO.getCommand());
        logDAO.addToLog(logTO);
        sqlMap.executeUpdate("deleteShareProductTO", objTO);
        addLoanData();
    }

    public static void main(String str[]) {
        try {
            ShareProductDAO dao = new ShareProductDAO();
        } catch (Exception ex) {
        }
    }

    /**
     * This method is called to do desired operations in the Table
     */
    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        try {
            sqlMap.startTransaction();

            objTO = (ShareProductTO) map.get("ShareProductTO");
            if (map.containsKey("ShareProductLoanTO")) {
                loanDataMap = (HashMap) map.get("ShareProductLoanTO");
            }
            if (map.containsKey("DeletedShareProductLoanTO")) {
                deletedLoanDataMap = (HashMap) map.get("DeletedShareProductLoanTO");
            }
            final String command = objTO.getCommand();

            logDAO = new LogDAO();
            logTO = new LogTO();

            logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
            logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
            logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
            logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
            logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData();
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                throw new NoCommandException();
            }
            sqlMap.commitTransaction();
            destroyObjects();
            return null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            throw new TransRollbackException(e);
        }
    }

    /*
     * This method is used to execute a query to get all the inserted datas in
     * the table and retrun the resultset as a HashMap *
     */
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        where = (String) obj.get(CommonConstants.MAP_WHERE);
        HashMap shareMap = getShareProductData();
        HashMap shareLoanMap = getShareProductLoanData();
        HashMap data = new HashMap();
        data.put("ShareProductTO", shareMap);
        data.put("ShareProductLoanTO", shareLoanMap);
        return data;
    }

    /*
     * This is used to free up the memory used by SharePrductTO object
     */
    private void destroyObjects() {
        objTO = null;
        loanDataMap = null;
        deletedLoanDataMap = null;
    }

    /**
     * This is used to Insert,Update,Delete the loan related detials in the
     * table *
     */
    private void addLoanData() throws Exception {
        if (loanDataMap != null) {
            ArrayList list = new ArrayList(loanDataMap.keySet());
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    objLoanTO = (ShareProductLoanTO) loanDataMap.get(CommonUtil.convertObjToStr(list.get(i)));
                    if (objLoanTO.getStatus().equals(CommonConstants.STATUS_CREATED)) {
                        sqlMap.executeUpdate("insertShareProductLoanTO", objLoanTO);
                    } else if (objLoanTO.getStatus().equals(CommonConstants.STATUS_MODIFIED)) {
                        sqlMap.executeUpdate("updateShareProductLoanTO", objLoanTO);
                    }
                }
            }
        }
        if (deletedLoanDataMap != null) {
            ArrayList deletedList = new ArrayList(deletedLoanDataMap.keySet());
            if (deletedList.size() != 0) {
                for (int j = 0; j < deletedList.size(); j++) {
                    objLoanTO = (ShareProductLoanTO) deletedLoanDataMap.get(CommonUtil.convertObjToStr(deletedList.get(j)));
                    sqlMap.executeUpdate("deleteShareProductLoanTO", objLoanTO);
                }
            }
        }
    }
}
