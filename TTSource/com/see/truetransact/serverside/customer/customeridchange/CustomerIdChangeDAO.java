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
package com.see.truetransact.serverside.customer.customeridchange;

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

import com.see.truetransact.transferobject.customer.customeridchange.CustomerIdChangeTO;
import java.util.HashMap;

/**
 * TokenConfig DAO.
 *
 */
public class CustomerIdChangeDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private CustomerIdChangeTO objTO;
    private LogDAO logDAO;
    private LogTO logTO;

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public CustomerIdChangeDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@@@@@@@map" + map);
        HashMap returnMap = new HashMap();
        List list = (List) sqlMap.executeQueryForList("getSelectCustIDChangeTO", map);
        returnMap.put("CustomerIdChangeTO", list);
        return returnMap;
    }

//    private String getTokenConfigId() throws Exception{
//        final IDGenerateDAO dao = new IDGenerateDAO();
//        final HashMap where = new HashMap();
//        where.put(CommonConstants.MAP_WHERE, "TOKEN_CONFIG_ID");
//        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
//    }
    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
//            objTO.setConfigId(getTokenConfigId());
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(objTO.toString());
//            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("insertCustIDChangeTO", objTO);
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
            objTO.setStatus(CommonConstants.STATUS_MODIFIED);
            logTO.setData(objTO.toString());
//            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("updateCustIDChangeTO", objTO);
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
//            logTO.setPrimaryKey(objTO.getKeyData());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteCustIDChangeTO", objTO);
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
            CustomerIdChangeDAO dao = new CustomerIdChangeDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("@@@@@@@mapINEXECUTE" + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        HashMap returnMap = null;
        returnMap = new HashMap();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));
        if (map.containsKey("CusomerIDChange")) {
            objTO = (CustomerIdChangeTO) map.get("CusomerIDChange");
            logDAO = new LogDAO();
            if ((map.get("COMMAND")).equals(CommonConstants.TOSTATUS_INSERT)) {
                insertData();
//            returnMap.put("CONFIG_ID",objTO.getConfigId());
            } else if ((map.get("COMMAND")).equals(CommonConstants.TOSTATUS_UPDATE)) {
                updateData();
            } else if ((map.get("COMMAND")).equals(CommonConstants.TOSTATUS_DELETE)) {
                deleteData();
            } else {
                throw new NoCommandException();
            }

            destroyObjects();
        }
        if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        }
        return returnMap;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void authorize(HashMap map) throws Exception {
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        map = null;
        map = (HashMap) selectedList.get(0);
        String status = (String) map.get("STATUS");
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            sqlMap.executeUpdate("authorizeCustIDChange", map);
//            logDAO.addToLog(logTO);
            if (status.equals(CommonConstants.STATUS_AUTHORIZED)) {
                if (map.get("PROD_TYPE").equals("TD")) {
                    String accNo = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
                    if (accNo.lastIndexOf("_") != -1) {
                        accNo = accNo.substring(0, accNo.lastIndexOf("_"));
                        map.put("ACT_NUM", accNo);
                    }
                }
                sqlMap.executeUpdate("updateCustID" + CommonUtil.convertObjToStr(map.get("PROD_TYPE")), map);
                if (map.get("PROD_TYPE").equals("OA")) {
                    sqlMap.executeUpdate("updateActJointOA", map);
                    sqlMap.executeUpdate("updateActIntOA", map);
                    sqlMap.executeUpdate("updateCustHisOA", map);
                    sqlMap.executeUpdate("updatePoaOA", map);
                    sqlMap.executeUpdate("updateJointOA", map);
                    sqlMap.executeUpdate("updateNomDetailOA", map);
                }
                if (map.get("PROD_TYPE").equals("TD")) {
//                    String accNo = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
//                    if (accNo.lastIndexOf("_")!=-1){
//                        accNo = accNo.substring(0,accNo.lastIndexOf("_"));
//                        map.put("ACT_NUM",accNo);
//                    }
                    sqlMap.executeUpdate("updateDepAuthTD", map);
                    sqlMap.executeUpdate("updateDepJointActTD", map);
                    sqlMap.executeUpdate("updateDepNomDetTD", map);
                    sqlMap.executeUpdate("updateDepPoaTD", map);
                   // sqlMap.executeUpdate("updateDepPoa1TD", map);
                    sqlMap.executeUpdate("updateDepIntTD", map);
                    sqlMap.executeUpdate("updateCustHisTD", map);
                    sqlMap.executeUpdate("updateCustPanNoTD", map);
                }
                if (map.get("PROD_TYPE").equals("TL") || map.get("PROD_TYPE").equals("AD")) {
                    sqlMap.executeUpdate("updateLoanAuth", map);
                    sqlMap.executeUpdate("updateLoanClosingIntTmp", map);
                    sqlMap.executeUpdate("updateLoansGuarantor", map);
                    sqlMap.executeUpdate("updateLoanInt", map);
                    sqlMap.executeUpdate("updateLoansJoint", map);
                    sqlMap.executeUpdate("updateLoansPoa", map);
                    sqlMap.executeUpdate("updateLoanSecDetails", map);
                    sqlMap.executeUpdate("updateCustHisLoans", map);
                }
                if (map.get("PROD_TYPE").equals("ATL") || map.get("PROD_TYPE").equals("AAD")) {
                    sqlMap.executeUpdate("updateCustIDAgriTL", map);
                    sqlMap.executeUpdate("updateAgriLoanClosingIntTmp", map);
                    sqlMap.executeUpdate("updateAgriLoansGuarantor", map);
                    sqlMap.executeUpdate("updateAgriLoanInt", map);
                    sqlMap.executeUpdate("updateAgriLoansJoint", map);
                    sqlMap.executeUpdate("updateAgriLoansPoa", map);
                    sqlMap.executeUpdate("updateAgriLoanSecDetails", map);
                    sqlMap.executeUpdate("updateCustHisLoans", map);
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void destroyObjects() {
        objTO = null;
    }
}
