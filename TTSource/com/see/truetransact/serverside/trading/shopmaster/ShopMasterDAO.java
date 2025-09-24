/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ShopMasterDAO.java
 *
 * @author Revathi L
 */
package com.see.truetransact.serverside.trading.shopmaster;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverutil.ServerUtil;
import org.apache.log4j.Logger;

import com.see.truetransact.transferobject.trading.shopmaster.ShopMasterTO;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.mdsapplication.mdschangeofmember.MDSChangeofMemberTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ShopMasterDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ShopMaster objTO;
    private LogTO logTO;
    private LogDAO logDAO;
    private ShopMasterTO objShopMasterTO;
    private String shopMasterID = "";
    HashMap resultMap = new HashMap();
    private final static Logger log = Logger.getLogger(ShopMasterDAO.class);

    public ShopMasterDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("####### ShopMasterDAO Map in DAO: " + map);
        HashMap returnMap = new HashMap();
        logDAO = new LogDAO();
        logTO = new LogTO();
        objShopMasterTO = (ShopMasterTO) map.get("objShopMasterTO");
        final String command = CommonUtil.convertObjToStr(map.get("COMMAND"));
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData(map);
            returnMap.put("SHOP_MASTER_ID", shopMasterID);
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData(map);
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
        return getData(obj);
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("####### ShopMasterDAO getData : " + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectShopMasterTO", map);
        returnMap.put("objShopMasterTO", list);
        return returnMap;
    }

    private String getShopMasterIDGeneration() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SHOP_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public void insertData(HashMap map) throws Exception {
        try {
            shopMasterID = getShopMasterIDGeneration();
            objShopMasterTO.setTxtStoreID(shopMasterID);
            sqlMap.startTransaction();
            sqlMap.executeUpdate("insertShopMasterTO", objShopMasterTO);
            logTO.setData(objShopMasterTO.toString());
            logTO.setPrimaryKey(objShopMasterTO.getKeyData());
            logTO.setStatus(objShopMasterTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        HashMap AuthMap = new HashMap();
        AuthMap = (HashMap) selectedList.get(0);
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            sqlMap.executeUpdate("authorizeShopMaster", AuthMap);
            String status = CommonUtil.convertObjToStr(AuthMap.get("STATUS"));
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateShopeMasterTO", objShopMasterTO);
            logTO.setData(objShopMasterTO.toString());
            logTO.setPrimaryKey(objShopMasterTO.getKeyData());
            logTO.setStatus(objShopMasterTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void deleteData(HashMap map) throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteShopeMasterTO", objShopMasterTO);
            logTO.setData(objShopMasterTO.toString());
            logTO.setPrimaryKey(objShopMasterTO.getKeyData());
            logTO.setStatus(objShopMasterTO.getStatus());
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            log.error(e);
            throw new TransRollbackException(e);
        }
        map = null;
    }

    private void destroyObjects() {
        objShopMasterTO = null;
    }
}
