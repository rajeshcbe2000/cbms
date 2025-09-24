/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryMasterDAO.java
 *
 * Created on Fri Aug 20 14:36:56 IST 2004
 */
package com.see.truetransact.serverside.supporting.inventory;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.commonutil.DateUtil;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.transferobject.supporting.inventory.InventoryMasterTO;
import java.util.Date;
/**
 * InventoryMaster DAO.
 *
 */
public class InventoryMasterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private InventoryMasterTO objInventoryMasterTO;
    private String userID = "";
    private Date currDt = null;
    /**
     * Creates a new instance of InventoryMasterDAO
     */
    public InventoryMasterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = (String) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getSelectInventoryMasterTO", where);
        returnMap.put("InventoryMasterTO", list);
        return returnMap;
    }

    private void insertData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            //__ To Restrick the Duplication of the Record...
            // getDuplicateInventoryMaster
            HashMap dataMap = new HashMap();
            dataMap.put("ITEM_TYPE", CommonUtil.convertObjToStr(objInventoryMasterTO.getItemType()));
            dataMap.put("ITEM_SUB_TYPE", CommonUtil.convertObjToStr(objInventoryMasterTO.getItemSubType()));
            dataMap.put("LEAVES_PER_BOOK", CommonUtil.convertObjToInt(objInventoryMasterTO.getLeavesPerBook()));
            dataMap.put(CommonConstants.BRANCH_ID, _branchCode);

            List list = (List) sqlMap.executeQueryForList("getDuplicateInventoryMaster", dataMap);
            System.out.println("List: " + list);
            if (list.size() > 0) {
                throw new TTException();
            } else {
                objInventoryMasterTO.setItemId(getInventoryID());
                objInventoryMasterTO.setStatus(CommonConstants.STATUS_CREATED);
                objInventoryMasterTO.setStatusBy(userID);
                objInventoryMasterTO.setStatusDt(currDt);

                objInventoryMasterTO.setCreatedBy(userID);
                objInventoryMasterTO.setCreatedDt(currDt);

                sqlMap.executeUpdate("insertInventoryMasterTO", objInventoryMasterTO);

                objLogTO.setData(objInventoryMasterTO.toString());
                objLogTO.setPrimaryKey(objInventoryMasterTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
//            e.printStackTrace();
            if (e instanceof TTException) {
                throw new TTException("This Combination of Item-Type, Sub-Item-Type and No. Of Leaves Already Exists");
            } else {
                throw new TransRollbackException(e);
            }
        }
    }

    private String getInventoryID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "INVENTORY_ITEM_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    private void updateData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objInventoryMasterTO.setStatus(CommonConstants.STATUS_MODIFIED);
            objInventoryMasterTO.setStatusBy(userID);
            objInventoryMasterTO.setStatusDt(currDt);

            sqlMap.executeUpdate("updateInventoryMasterTO", objInventoryMasterTO);

            objLogTO.setData(objInventoryMasterTO.toString());
            objLogTO.setPrimaryKey(objInventoryMasterTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void deleteData(LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objInventoryMasterTO.setStatus(CommonConstants.STATUS_DELETED);
            objInventoryMasterTO.setStatusBy(userID);
            objInventoryMasterTO.setStatusDt(currDt);

            sqlMap.executeUpdate("deleteInventoryMasterTO", objInventoryMasterTO);

            objLogTO.setData(objInventoryMasterTO.toString());
            objLogTO.setPrimaryKey(objInventoryMasterTO.getKeyData());
            objLogDAO.addToLog(objLogTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            InventoryMasterDAO dao = new InventoryMasterDAO();
            InventoryMasterTO objInventoryMasterTO = new InventoryMasterTO();

            objInventoryMasterTO.setCommand(CommonConstants.TOSTATUS_UPDATE);
            objInventoryMasterTO.setItemId("I1001");
            objInventoryMasterTO.setItemType("CHEQUE");
            objInventoryMasterTO.setItemSubType("CURRENT ACCOUNT CHEQUES");
            objInventoryMasterTO.setAvailableBooks(new Double("10"));
            objInventoryMasterTO.setLeavesPerBook(new Double("100"));
            objInventoryMasterTO.setBooksReorderLevel(new Double("100"));
            objInventoryMasterTO.setBooksDangerLevel(new Double("50"));
            objInventoryMasterTO.setCreatedBy("Admin");
            objInventoryMasterTO.setCreatedDt(DateUtil.getDateMMDDYYYY(null));
            objInventoryMasterTO.setStatus("DELETED");
            objInventoryMasterTO.setStatusBy("Admin");
            objInventoryMasterTO.setStatusDt(DateUtil.getDateMMDDYYYY(null));
            objInventoryMasterTO.setAuthorizeStatus("");
            objInventoryMasterTO.setAuthorizeBy("");
            objInventoryMasterTO.setAuthorizeDt(DateUtil.getDateMMDDYYYY(null));
            objInventoryMasterTO.setBranchId("Bran");
            objInventoryMasterTO.setInstrumentPrefix("ABC");

            HashMap hash = new HashMap();
            hash.put("InventoryMasterTO", objInventoryMasterTO);
            dao.execute(hash);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        // Log DAO
        HashMap returnMap = null;
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

        objInventoryMasterTO = (InventoryMasterTO) map.get("InventoryMasterTO");
        final String command = objInventoryMasterTO.getCommand();

        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(objLogDAO, objLogTO);
            returnMap = new HashMap();
            returnMap.put("ITEM ID", objInventoryMasterTO.getItemId());
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
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        objInventoryMasterTO = null;
    }
}
