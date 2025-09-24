/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.servicetax.servicetaxsettings;

import java.util.List;
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
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.servicetax.servicetaxsettings.ServiceTaxSettingsTO;

import com.see.truetransact.transferobject.transaction.token.tokenconfig.TokenConfigTO;
import java.util.Date;
import java.util.HashMap;

/**
 * TokenConfig DAO.
 *
 */
public class ServiceTaxSettingsDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ServiceTaxSettingsTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public ServiceTaxSettingsDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getServiceTaxSettingsTO", where);
        returnMap.put("ServiceTaxSettingsTO", list);
        return returnMap;
    }

    private String getTokenConfigId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SERVICETAX_GEN_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
 private String getserviceTaxID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "SERVICETAX_GEN_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }
    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setServiceTaxId(getserviceTaxID());
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getServiceTaxId());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("insertServiceTaxSettingsTO", objTO);
            logDAO.addToLog(logTO);
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
            Date currDt = ServerUtil.getCurrentDate(objTO.getBranchId());    
            String userId = CommonUtil.convertObjToStr(map.get("USER_ID"));
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getServiceTaxId());
            logTO.setStatus(objTO.getCommand());
            if (map.containsKey("NEW_TAX_SETTINGS")) {
                HashMap updatemap = new HashMap();
                updatemap.put("END_DATE", currDt);
                updatemap.put("BRANCH_ID", objTO.getBranchId());
                updatemap.put("SERVICETAX_GEN_ID", objTO.getServiceTaxId());
                //ClientUtil.execute("updateEndDate", updatemap);
                sqlMap.executeUpdate("closeCurrentTaxSettings", updatemap);
                objTO.setCreatedDt(currDt);
                objTO.setCreatedBy(userId);
                objTO.setStatus("CREATED");
                sqlMap.executeUpdate("insertServiceTaxSettingsTO", objTO);
            }else{
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getServiceTaxId());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateServiceTaxSettingsTO", objTO);
            }            
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
            logTO.setPrimaryKey(objTO.getServiceTaxId());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteServiceTaxSettingsTO", objTO);
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
            ServiceTaxSettingsDAO dao = new ServiceTaxSettingsDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        objTO = (ServiceTaxSettingsTO) map.get("ServiceTaxSettingsTO");
         System.out.println("Map in DAO: " + map);
        if (map.containsKey("ACCT_HD")) {
            ServerUtil.verifyAccountHead(map);
        }
        System.out.println("objTO============================================="+objTO);
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
            returnMap.put("SERVICETAX_GEN_ID", objTO.getServiceTaxId());
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData(map);
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
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
