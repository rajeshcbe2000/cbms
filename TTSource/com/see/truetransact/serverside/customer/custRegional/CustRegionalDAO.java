/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustRegionalDAO.java
 *
 * Created on Thu Jan 20 17:19:05 IST 2005
 */
package com.see.truetransact.serverside.customer.custRegional;
/**
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 14-05-2015
 */
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
import com.see.truetransact.transferobject.customer.CustRegionalTo;

import com.see.truetransact.transferobject.transaction.token.tokenconfig.TokenConfigTO;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

/**
 * CustRegional DAO.
 *
 */
public class CustRegionalDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private CustRegionalTo objTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private Date currDt =null;
    private String userId =null;

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public CustRegionalDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap where = (HashMap) map.get(CommonConstants.MAP_WHERE);
        List list = (List) sqlMap.executeQueryForList("getCustRegionalDetailsForView", where);
        if (list != null && list.size() > 0) {
            returnMap.put("CUST_REGIONAL_DATA_OBJ", list);
        }
        List list1 = (List) sqlMap.executeQueryForList("getCustomerDetailsForView", where);
        if (list1 != null && list1.size() > 0) {
            returnMap.put("CUST_REGIONAL_DATA", list1);
        }
        
        System.out.println("returnMap   :"+returnMap);
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
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(objTO.toString());
            logTO.setStatus(objTO.getCommand());
            storeRegLanguageDet();
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }
 private void storeRegLanguageDet() throws SQLException {
        if (objTO != null) {
            objTO.setStatus(objTO.getCommand());
            objTO.setBranch_code(_branchCode);
            objTO.setStatusBy(userId);
            objTO.setStatusDt((Date)currDt.clone());
            sqlMap.executeUpdate("insertCustRegionalTO", objTO);
        }
    }
    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objTO.toString());
            logTO.setStatus(objTO.getCommand());
            objTO.setStatus(objTO.getCommand());
            objTO.setBranch_code(_branchCode);
            objTO.setStatusBy(userId);
            objTO.setStatusDt((Date) currDt.clone());
            sqlMap.executeUpdate("updateCustRegionalTO", objTO);
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
           // logTO.setPrimaryKey(objTO.getServiceTaxId());
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
            CustRegionalDAO dao = new CustRegionalDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        objTO = (CustRegionalTo) map.get("CustRegionalTo");
         System.out.println("Map in DAO: " + map);
       
        System.out.println("objTO============================================="+objTO);
         String command = objTO.getCommand();
        HashMap returnMap = null;
        logDAO = new LogDAO();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        userId=CommonUtil.convertObjToStr(map.get("USER_ID"));
        if (objTO != null) {
            HashMap whMap = new HashMap();
            whMap.put("CUST_ID", objTO.getCustId());
            List passCnt = sqlMap.executeQueryForList("getDataFromCustRegional", whMap);
            if (passCnt != null && passCnt.size() > 0) {
               command = CommonConstants.TOSTATUS_UPDATE ;
            } else {
                command = CommonConstants.TOSTATUS_INSERT;
            }
        }
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            insertData();
            userId="";
            returnMap = new HashMap();
            returnMap.put("MEM_NUM", objTO.getMemNo());
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
            userId="";
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
        System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhh");
        return getData(obj);
    }

    private void destroyObjects() {
        objTO = null;
    }
}
