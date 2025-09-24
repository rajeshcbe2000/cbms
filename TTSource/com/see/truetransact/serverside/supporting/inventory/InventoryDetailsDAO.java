/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryDetailsDAO.java
 *
 * Created on Mon Aug 23 12:49:08 IST 2004
 */
package com.see.truetransact.serverside.supporting.inventory;

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
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;

import com.see.truetransact.transferobject.supporting.inventory.InventoryDetailsTO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * InventoryDetails DAO.
 *
 */
public class InventoryDetailsDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private InventoryDetailsTO objInventoryDetailsTO;
    private final String ADMIN = "Admin";
    private final String TRANSID = "TRANSACTION_ID";
    private String transID = "";
    private String userID = "";
    private Date currDt = null;
    /**
     * Creates a new instance of InventoryDetailsDAO
     */
    public InventoryDetailsDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectInventoryDetailsTO", where);
        returnMap.put("InventoryDetailsTO", list);
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        transID = getInventoryID();
        objInventoryDetailsTO.setTransId(transID);
        objInventoryDetailsTO.setTransDt(currDt);
        objInventoryDetailsTO.setStatus(CommonConstants.STATUS_CREATED);
        if (!userID.equalsIgnoreCase("")) {
            objInventoryDetailsTO.setStatusBy(userID);
            objInventoryDetailsTO.setCreatedBy(userID);
        }

        objInventoryDetailsTO.setStatusDt(currDt);
        objInventoryDetailsTO.setCreatedDt(currDt);

        sqlMap.executeUpdate("insertInventoryDetailsTO", objInventoryDetailsTO);
        objLogTO.setData(objInventoryDetailsTO.toString());
        objLogTO.setPrimaryKey(objInventoryDetailsTO.getKeyData());
        objLogDAO.addToLog(objLogTO);
    }

    private String getInventoryID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "INVENTORY_TRANS_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        objInventoryDetailsTO.setStatus(CommonConstants.STATUS_MODIFIED);
        objInventoryDetailsTO.setStatusBy(userID);
        objInventoryDetailsTO.setStatusDt(currDt);

        sqlMap.executeUpdate("updateInventoryDetailsTO", objInventoryDetailsTO);
        objLogTO.setData(objInventoryDetailsTO.toString());
        objLogTO.setPrimaryKey(objInventoryDetailsTO.getKeyData());
        objLogDAO.addToLog(objLogTO);
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {

        objInventoryDetailsTO.setStatus(CommonConstants.STATUS_DELETED);
        objInventoryDetailsTO.setStatusBy(userID);
        objInventoryDetailsTO.setStatusDt(currDt);

        sqlMap.executeUpdate("deleteInventoryDetailsTO", objInventoryDetailsTO);
        objLogTO.setData(objInventoryDetailsTO.toString());
        objLogTO.setPrimaryKey(objInventoryDetailsTO.getKeyData());
        objLogDAO.addToLog(objLogTO);
    }

    public static void main(String str[]) {
        try {
            InventoryDetailsDAO dao = new InventoryDetailsDAO();
            InventoryDetailsTO objInventoryDetailsTO = new InventoryDetailsTO();

            objInventoryDetailsTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objInventoryDetailsTO.setItemId("I1001");
            objInventoryDetailsTO.setTransDt(DateUtil.getDateMMDDYYYY(null));
            objInventoryDetailsTO.setTransType("");
            objInventoryDetailsTO.setBookQuantity(new Double(10));
            objInventoryDetailsTO.setBookSlnoFrom(new Double(1001));
            objInventoryDetailsTO.setBookSlnoTo(new Double(1002));
            objInventoryDetailsTO.setLeavesSlnoFrom(new Double(1000));
            objInventoryDetailsTO.setLeavesSlnoTo(new Double(1010));
            objInventoryDetailsTO.setProdType("CAGen");
            objInventoryDetailsTO.setActNum("OA001001");
            objInventoryDetailsTO.setCreatedBy("Admin");
            objInventoryDetailsTO.setCreatedDt(DateUtil.getDateMMDDYYYY(null));
            objInventoryDetailsTO.setStatus("CREATED");
            objInventoryDetailsTO.setStatusBy("ADMIN");
            objInventoryDetailsTO.setStatusDt(DateUtil.getDateMMDDYYYY(null));
            objInventoryDetailsTO.setAuthorizeStatus("");
            objInventoryDetailsTO.setAuthorizeBy("");
            objInventoryDetailsTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(null));
            objInventoryDetailsTO.setTransId("");

            HashMap hash = new HashMap();
            hash.put("InventoryDetailsTO", objInventoryDetailsTO);
            dao.execute(hash);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        return execute(map, true);
    }

    public HashMap execute(HashMap map, boolean isTransaction) throws Exception {
        HashMap dataMap = new HashMap();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId((String) map.get(CommonConstants.USER_ID));
        objLogTO.setBranchId((String) map.get(CommonConstants.BRANCH_ID));
        objLogTO.setSelectedBranchId((String) map.get(CommonConstants.SELECTED_BRANCH_ID));
        objLogTO.setIpAddr((String) map.get(CommonConstants.IP_ADDR));
        objLogTO.setModule((String) map.get(CommonConstants.MODULE));
        objLogTO.setScreen((String) map.get(CommonConstants.SCREEN));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
        try {
            if (isTransaction) {
                sqlMap.startTransaction();
            }
            _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
            currDt = ServerUtil.getCurrentDate(_branchCode);
            if (map.containsKey("InventoryDetailsTO")) {
                objInventoryDetailsTO = (InventoryDetailsTO) map.get("InventoryDetailsTO");
                final String command = objInventoryDetailsTO.getCommand();

                objLogTO.setStatus(command);

                if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                    insertData(objLogDAO, objLogTO);
                    dataMap = new HashMap();
                    dataMap.put("TRANSACTION ID", objInventoryDetailsTO.getTransId());
                } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                    updateData(objLogDAO, objLogTO);
                } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
                    deleteData(objLogDAO, objLogTO);
                } else {
                    throw new NoCommandException();
                }

                objLogDAO = null;
                objLogTO = null;
                destroyObjects();
            }
            if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
                HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
                if (authMap != null) {
                    authorize(map, objLogDAO, objLogTO);
                }
            }

            if (isTransaction) {
                sqlMap.commitTransaction();
            }

        } catch (Exception e) {
            e.printStackTrace();

            if (isTransaction) {
                sqlMap.rollbackTransaction();
            }
            throw e;
        }

        dataMap.put(TRANSID, transID);
        return dataMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objInventoryDetailsTO = null;
    }

    private void authorize(HashMap map, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
        if (authMap != null) {
            HashMap dataMap;
            String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);

            for (int i = 0, j = selectedList.size(); i < j; i++) {
                dataMap = (HashMap) selectedList.get(i);

                dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
                dataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));

                /**
                 * Update the Authorization Fields and Update the Available
                 * Balance.
                 */
                sqlMap.executeUpdate("authInventoryDetails", dataMap);


                // AuthorizeStatus is Authorized
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    sqlMap.executeUpdate("updateInventoryMasterAvailBooks", dataMap);
                } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                    // Exisiting status is Created or Modified
//                    if (!(CommonUtil.convertObjToStr(dataMap.get("STATUS")).equalsIgnoreCase(CommonConstants.STATUS_CREATED) ||
//                    CommonUtil.convertObjToStr(dataMap.get("STATUS")).equalsIgnoreCase(CommonConstants.STATUS_MODIFIED))) {

//                        dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_MODIFIED);
                    sqlMap.executeUpdate("authInventoryDetails", dataMap);
//                    }
                }
                objLogTO.setStatus(CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS)));
                objLogTO.setData(dataMap.toString());
                objLogTO.setPrimaryKey(CommonUtil.convertObjToStr(dataMap.get("TRANSACTION ID")));
                objLogDAO.addToLog(objLogTO);
            }

        }
    }
}
