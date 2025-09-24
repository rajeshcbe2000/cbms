/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ConfigurationDAO.java
 *
 * Created on Fri Feb 11 11:44:57 IST 2005
 */
package com.see.truetransact.serverside.sysadmin.config;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.sysadmin.config.ConfigPasswordTO;
import com.see.truetransact.transferobject.sysadmin.config.SIChargesHeadTO;

// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import java.util.Date;
/**
 * Configuration DAO.
 *
 */
public class ConfigurationDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private ConfigPasswordTO objConfigPasswordTO;
    private SIChargesHeadTO objSIChargesHeadTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private String userID = "";
    private Date currDt = null;
    /**
     * Creates a new instance of ConfigurationDAO
     */
    public ConfigurationDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("##### Configuration DAO : HO : " + ServerConstants.HO);
        HashMap returnMap = new HashMap();
        List list = null;
        HashMap whereMap = (HashMap) map.get(CommonConstants.MAP_WHERE);
//        if(map.get(CommonConstants.MAP_NAME).equals("getSelectConfigPasswordTO")){
//            String mapName = (String) map.get(CommonConstants.MAP_NAME);
        list = (List) sqlMap.executeQueryForList("getSelectConfigPasswordTO", whereMap);
        returnMap.put("ConfigPasswordTO", list);

//        }
//         if(map.get(CommonConstants.MAP_NAME).equals("getSelectSIChargesHeadTO")){
//            String mapName = (String) map.get(CommonConstants.MAP_NAME);
        list = (List) sqlMap.executeQueryForList("getSelectSIChargesHeadTO", whereMap);
        returnMap.put("SIChargesHeadTO", list);
//        }
//        List list = (List) sqlMap.executeQueryForList(mapName, null);
//        returnMap.put("ConfigPasswordTO", list);

        whereMap = null;

        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();

            objConfigPasswordTO.setStatusBy(userID);
            objConfigPasswordTO.setStatusDt(currDt);

            sqlMap.executeUpdate("insertConfigPasswordTO", objConfigPasswordTO);
            sqlMap.executeUpdate("insertSIChargesHeadTO", objSIChargesHeadTO);
            if (!objConfigPasswordTO.getServicePeriod().equals("")) {
                ConfigPasswordTO objConfigPasswordTOForService = new ConfigPasswordTO();
                objConfigPasswordTOForService.setServicePeriod(objConfigPasswordTO.getServicePeriod());
                objConfigPasswordTOForService.setEffectiveFrom(objConfigPasswordTO.getEffectiveFrom());
                sqlMap.executeUpdate("insertServicePeriod", objConfigPasswordTOForService);
            }
            logTO.setData(objConfigPasswordTO.toString());
            logTO.setPrimaryKey(objConfigPasswordTO.getKeyData());
            logTO.setStatus(CommonConstants.TOSTATUS_INSERT);
            logDAO.addToLog(logTO);

            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();

            objConfigPasswordTO.setStatusBy(userID);
            objConfigPasswordTO.setStatusDt(currDt);

            sqlMap.executeUpdate("updateConfigPasswordTO", objConfigPasswordTO);
            sqlMap.executeUpdate("updateSIChargesHeadTO", objSIChargesHeadTO);

            if (!objConfigPasswordTO.getServicePeriod().equals("")) {
                ConfigPasswordTO objConfigPasswordTOForService = new ConfigPasswordTO();
                objConfigPasswordTOForService.setServicePeriod(objConfigPasswordTO.getServicePeriod());
                objConfigPasswordTOForService.setEffectiveFrom(objConfigPasswordTO.getEffectiveFrom());
                sqlMap.executeUpdate("UpdateServicePeriod", objConfigPasswordTOForService);
            }


            logTO.setData(objConfigPasswordTO.toString());
            logTO.setPrimaryKey(objConfigPasswordTO.getKeyData());
            logTO.setStatus(CommonConstants.TOSTATUS_UPDATE);
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
            ConfigurationDAO dao = new ConfigurationDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sets the values for logTO Object
     */
    private void setInitialValuesForLogTO(HashMap map) throws Exception {
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        userID = CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID));
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        setInitialValuesForLogTO(map);

        if ((map.containsKey("ConfigPasswordTO")) || (map.containsKey("SIChargesHeadTO"))) {
            objConfigPasswordTO = (ConfigPasswordTO) map.get("ConfigPasswordTO");
            final String command = (String) map.get("TOSTATUS");
            if (map.containsKey("SIChargesHeadTO")) {
                objSIChargesHeadTO = (SIChargesHeadTO) map.get("SIChargesHeadTO");
            }
            if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData();
            } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else {
                throw new NoCommandException();
            }
        }

        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
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
        objConfigPasswordTO = null;
        logTO = null;
        logDAO = null;
    }

    private void authorize(HashMap authMap) throws Exception {
        //__ To Authorize the Data in the PARAMETERS...
        sqlMap.executeUpdate("authorizeConfigPasswordData", authMap);


        logTO.setData(authMap.toString());
        //__ No Primary Key Defined...
        logTO.setPrimaryKey("");
        logTO.setStatus(CommonUtil.convertObjToStr(authMap.get("STATUS")));
        logDAO.addToLog(logTO);
    }
}
