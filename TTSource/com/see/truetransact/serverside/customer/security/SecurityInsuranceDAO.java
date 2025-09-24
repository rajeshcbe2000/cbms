/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * SecurityInsuranceDAO.java
 *
 * Created on January 13, 2005, 4:16 PM
 */
package com.see.truetransact.serverside.customer.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.ibatis.db.sqlmap.cache.CacheModel;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.transferobject.customer.security.InsuranceTO;
import com.see.truetransact.transferobject.customer.security.SecurityTO;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
/**
 *
 * @author 152713
 */
public class SecurityInsuranceDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private LogDAO logDAO;
    private LogTO logTO;
    private HashMap securityMap;
    private HashMap insuranceMap;
    private Date currDt = null;
    /**
     * Creates a new instance of SecurityInsuranceDAO
     */
    public SecurityInsuranceDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        String where = CommonUtil.convertObjToStr(map.get(CommonConstants.MAP_WHERE));

        List list = (List) sqlMap.executeQueryForList("getSelectSecurityTO", where);
        returnMap.put("SecurityTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList("getSelectInsuranceTO", where);
        returnMap.put("InsuranceTO", list);
        list = null;

        map = null;
        where = null;
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return returnMap;
    }

    private void executeAllTabQuery() throws Exception {
        executeSecurityTabQuery();
        executeInsuranceQuery();
    }

    private void executeSecurityTabQuery() throws Exception {
        SecurityTO objSecurityTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = securityMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To retrieve the SecurityTO from the securityMap
            for (int i = securityMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objSecurityTO = (SecurityTO) securityMap.get(objKeySet[j]);
                logTO.setData(objSecurityTO.toString());
                logTO.setPrimaryKey(objSecurityTO.getKeyData());
                logTO.setStatus(objSecurityTO.getCommand());
                executeOneTabQueries("SecurityTO", objSecurityTO);
                logDAO.addToLog(logTO);
                objSecurityTO = null;
            }
            keySet = null;
            objKeySet = null;
            objSecurityTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeInsuranceQuery() throws Exception {
        InsuranceTO objInsuranceTO;
        Set keySet;
        Object[] objKeySet;
        try {
            keySet = insuranceMap.keySet();
            objKeySet = (Object[]) keySet.toArray();
            // To retrieve the InsuranceTO from the insuranceMap
            for (int i = insuranceMap.size() - 1, j = 0; i >= 0; --i, ++j) {
                objInsuranceTO = (InsuranceTO) insuranceMap.get(objKeySet[j]);
                logTO.setData(objInsuranceTO.toString());
                logTO.setPrimaryKey(objInsuranceTO.getKeyData());
                logTO.setStatus(objInsuranceTO.getCommand());
                executeOneTabQueries("InsuranceTO", objInsuranceTO);
                logDAO.addToLog(logTO);
                objInsuranceTO = null;
            }
            keySet = null;
            objKeySet = null;
            objInsuranceTO = null;
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeOneTabQueries(String strTOName, TransferObject TO) throws Exception {
        try {
            StringBuffer sbMapName = new StringBuffer();
            if (TO.getCommand() != null) {
                sbMapName.append(TO.getCommand().toLowerCase());
                sbMapName.append(strTOName);
                executeUpdate(sbMapName.toString(), TO);
            }
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void executeUpdate(String str, Object objTO) throws Exception {
        try {
            sqlMap.executeUpdate(str, objTO);
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void authorize(HashMap map) throws Exception {
        String strCustID;
        SecurityTO objTO = null;
        try {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            HashMap dataMap;
            String status = CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS));
            ArrayList selectedList = (ArrayList) authMap.get(CommonConstants.AUTHORIZEDATA);
            sqlMap.startTransaction();
            for (int i = selectedList.size() - 1, j = 0; i >= 0; --i, ++j) {
                dataMap = (HashMap) selectedList.get(j);

                dataMap.put(CommonConstants.AUTHORIZESTATUS, status);
                dataMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
                dataMap.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));

                /**
                 * Update the Authorization Fields and Update the Available
                 * Balance.
                 */
                sqlMap.executeUpdate("authorizeCust_Security", dataMap);
                sqlMap.executeUpdate("authorizeCust_Insurance", dataMap);

                // AuthorizeStatus is Authorized
                if (status.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    //                        sqlMap.executeUpdate("updateInventoryMasterAvailBooks", dataMap);
                } else if (status.equalsIgnoreCase(CommonConstants.STATUS_REJECTED)) {
                    // Exisiting status is Created or Modified
                    if (!(CommonUtil.convertObjToStr(dataMap.get("STATUS")).equalsIgnoreCase(CommonConstants.STATUS_CREATED)
                            || CommonUtil.convertObjToStr(dataMap.get("STATUS")).equalsIgnoreCase(CommonConstants.STATUS_MODIFIED))) {

                        dataMap.put(CommonConstants.STATUS, CommonConstants.STATUS_MODIFIED);
                        //                            sqlMap.executeUpdate("rejectInventoryDetails", dataMap);
                    }
                }
                logTO.setStatus(CommonUtil.convertObjToStr(authMap.get(CommonConstants.AUTHORIZESTATUS)));
                logTO.setData(dataMap.toString());
                logTO.setPrimaryKey(CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
                logDAO.addToLog(logTO);
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        logDAO = new LogDAO();
        logTO = new LogTO();

        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setSelectedBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.SELECTED_BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        _branchCode = CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID));
        currDt = ServerUtil.getCurrentDate(_branchCode);
        securityMap = (HashMap) map.get("SecurityTO");
        insuranceMap = (HashMap) map.get("InsuranceTO");

        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(map);
            }
        } else {
            executeAllTabQuery();
        }

        destroyObjects();
        ServiceLocator.flushCache(sqlMap);
        System.gc();
        return null;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        return getData(obj);
    }

    private void destroyObjects() {
        logDAO = null;
        logTO = null;
        securityMap = null;
        insuranceMap = null;
    }
}
