/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MultipleCustomerIdChangeDAO.java
 *Created On 20 Feb 2017 
 * Created By Kannan AR
 */
package com.see.truetransact.serverside.customer.multipleCustomerIdChange;

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
import com.see.truetransact.transferobject.customer.CustomerSuspensionTO;

import com.see.truetransact.transferobject.customer.customeridchange.CustomerIdChangeTO;
import com.see.truetransact.transferobject.customer.multipleCustomerIdChange.MultipleCustomerIdChangeTO;
import java.util.HashMap;

/**
 * MultipleCustomerIdChange DAO.
 *
 */
public class MultipleCustomerIdChangeDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private CustomerIdChangeTO objTO;
    private MultipleCustomerIdChangeTO multiplObjTO;
    private LogDAO logDAO;
    private LogTO logTO;
    private CustomerSuspensionTO objCustomerSuspensionTO;
    private ArrayList allAccountsList = new ArrayList();
    private String multipleCustBatchId = "";

    /**
     * Creates a new instance of TokenConfigDAO
     */
    public MultipleCustomerIdChangeDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        System.out.println("@#@#@#  Multiple Change Cust ID Execute Query " + map);
        HashMap returnMap = new HashMap();
        List filterList = (List) sqlMap.executeQueryForList("getMultipleCustIDChangeTOResult", map);
        returnMap.put("MultipleCustomerIdChangeTO", filterList);
        List accountList = (List) sqlMap.executeQueryForList("getMultipleAccoutDetails", map);
        returnMap.put("ALL_ACCOUNTS_LIST", accountList);
        return returnMap;
    }

    private void insertData() throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_CREATED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getActNum());
            logTO.setStatus(objTO.getCommand());
            objTO.setMultipleBatchId(multipleCustBatchId);
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
            logTO.setPrimaryKey(objTO.getActNum());
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

    private void deleteData(CustomerIdChangeTO objTO) throws Exception {
        try {
            sqlMap.startTransaction();
            objTO.setStatus(CommonConstants.STATUS_DELETED);
            logTO.setData(objTO.toString());
            logTO.setPrimaryKey(objTO.getActNum());
            logTO.setStatus(objTO.getCommand());
            sqlMap.executeUpdate("deleteCustIDTO", objTO);//Sub Table Deletion code
            logDAO.addToLog(logTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private String getBatchId() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "MULTI_CUST_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    public static void main(String str[]) {
        try {
            MultipleCustomerIdChangeDAO dao = new MultipleCustomerIdChangeDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        System.out.println("########## Multiple Customer ID Change DAO : " + map);
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        HashMap returnMap = null;
        returnMap = new HashMap();
        logTO = new LogTO();
        logTO.setUserId(CommonUtil.convertObjToStr(map.get(CommonConstants.USER_ID)));
        logTO.setBranchId(CommonUtil.convertObjToStr(map.get(CommonConstants.BRANCH_ID)));
        logTO.setIpAddr(CommonUtil.convertObjToStr(map.get(CommonConstants.IP_ADDR)));
        logTO.setModule(CommonUtil.convertObjToStr(map.get(CommonConstants.MODULE)));
        logTO.setScreen(CommonUtil.convertObjToStr(map.get(CommonConstants.SCREEN)));

        if (map.get("COMMAND") != null && (map.get("COMMAND").equals(CommonConstants.TOSTATUS_INSERT) || map.get("COMMAND").equals(CommonConstants.TOSTATUS_UPDATE) || map.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE))) {
            if (map.containsKey("ALL_ACCOUNTS_LIST") && map.get("ALL_ACCOUNTS_LIST") != null) {
                allAccountsList = (ArrayList) map.get("ALL_ACCOUNTS_LIST");
                logDAO = new LogDAO();
                if (map.containsKey("MULTIPLE_MERGE_DATA") && map.get("MULTIPLE_MERGE_DATA") != null) {
                    multiplObjTO = new MultipleCustomerIdChangeTO();
                    multiplObjTO = (MultipleCustomerIdChangeTO) map.get("MULTIPLE_MERGE_DATA");
                    if (CommonUtil.convertObjToStr(multiplObjTO.getMultipleBatchId()).equals("")) {
                        multipleCustBatchId = getBatchId();
                    } else {
                        multipleCustBatchId = CommonUtil.convertObjToStr(multiplObjTO.getMultipleBatchId());
                    }
                    multiplObjTO.setMultipleBatchId(multipleCustBatchId);
                    if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_UPDATE) || map.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE)) {
                        objTO = new CustomerIdChangeTO();
                        objTO.setMultipleBatchId(multipleCustBatchId);
                        objTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
                        objTO.setStatusDt(ServerUtil.getCurrentDateProperFormat(_branchCode));
                        logTO.setPrimaryKey(objTO.getActNum());
                        logTO.setStatus(objTO.getCommand());
                        deleteData(objTO);
                        if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_DELETE)) {
                            sqlMap.executeUpdate("deleteMultipleCustChangeTO", objTO);//Main Table Deletion code
                        }
                    }
                    if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_INSERT)) {
                        sqlMap.executeUpdate("insertMultipleCustTO", multiplObjTO);
                    } else if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_UPDATE)) {
                        sqlMap.executeUpdate("updateMultipleCustChangeTO", multiplObjTO);
                    }
                }
                if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_INSERT) || map.get("COMMAND").equals(CommonConstants.TOSTATUS_UPDATE)) {
                    for (int i = 0; i < allAccountsList.size(); i++) {
                        ArrayList accts = (ArrayList) allAccountsList.get(i);
                        System.out.println("accts : " + accts);
                        objTO = (CustomerIdChangeTO) setCusomerIDChangeTransaction(accts, map);
                        insertData();
                    }
                    returnMap.put("MULTIPLE_MERGE_ID", multipleCustBatchId);
                }
            }
        } else if (map.containsKey(CommonConstants.AUTHORIZEMAP)) {
            HashMap authMap = (HashMap) map.get(CommonConstants.AUTHORIZEMAP);
            if (authMap != null) {
                authorize(authMap);
            }
        } else {
            throw new NoCommandException();
        }
        destroyObjects();

        return returnMap;
    }

    /**
     * To populate data into the screen
     */
    public CustomerIdChangeTO setCusomerIDChangeTransaction(ArrayList accountList, HashMap map) {
        final CustomerIdChangeTO objCustomerIdChangeTO = new CustomerIdChangeTO();
        try {
            objCustomerIdChangeTO.setProdType(CommonUtil.convertObjToStr(accountList.get(0)));
            objCustomerIdChangeTO.setProdId(CommonUtil.convertObjToStr(accountList.get(1)));
            objCustomerIdChangeTO.setActNum(CommonUtil.convertObjToStr(accountList.get(2)));
            objCustomerIdChangeTO.setActStatus(CommonUtil.convertObjToStr(accountList.get(3)));
            objCustomerIdChangeTO.setOldCustId(CommonUtil.convertObjToStr(accountList.get(4)));
            if (map.get("COMMAND").equals(CommonConstants.TOSTATUS_INSERT)){
                objCustomerIdChangeTO.setActName(CommonUtil.convertObjToStr(accountList.get(5)));
            }
            objCustomerIdChangeTO.setStatusBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            objCustomerIdChangeTO.setBranchCode(_branchCode);
            objCustomerIdChangeTO.setNewCustId(CommonUtil.convertObjToStr(map.get("NEW_CUST_ID")));
            objCustomerIdChangeTO.setCommand(CommonUtil.convertObjToStr(map.get("COMMAND")));
            objCustomerIdChangeTO.setStatusDt(ServerUtil.getCurrentDateProperFormat(_branchCode));
            objCustomerIdChangeTO.setCreatedBy(CommonUtil.convertObjToStr(map.get("USER_ID")));
            objCustomerIdChangeTO.setCreatedDate(ServerUtil.getCurrentDateWithTime(_branchCode));
            objCustomerIdChangeTO.setNewActName(CommonUtil.convertObjToStr(map.get("NEW_CUST_ID_NAME")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objCustomerIdChangeTO;
    }

    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        return getData(obj);
    }

    private void authorize(HashMap map) throws Exception {
        System.out.println("@#@#@#  Multiple Change Cust ID Auth map " + map);
        logDAO = new LogDAO();
        ArrayList selectedList = (ArrayList) map.get(CommonConstants.AUTHORIZEDATA);
        map = null;
        map = (HashMap) selectedList.get(0);
        String status = (String) map.get("STATUS");
        try {
            sqlMap.startTransaction();
            logTO.setData(map.toString());
            logTO.setPrimaryKey(CommonUtil.convertObjToStr(map.get("MULTIPLE_BATCH_ID")));
            logTO.setStatus(status);
            logTO.setInitiatedBranch(_branchCode);
            sqlMap.executeUpdate("authorizeMultipleCustIDChange", map);//Main Table Authorization
            sqlMap.executeUpdate("authCustIDChangeDetails", map);     //Sub Table Authorization       
            logDAO.addToLog(logTO);
            if (status.equals(CommonConstants.STATUS_AUTHORIZED)) {
                HashMap acctsMap = new HashMap();
                acctsMap.put("BATCH_ID", CommonUtil.convertObjToStr(map.get("MULTIPLE_BATCH_ID")));
                List acctsLst = sqlMap.executeQueryForList("getMultipleAccoutDetails", acctsMap);
                if (acctsLst != null && acctsLst.size() > 0) {
                    for (int i = 0; i < acctsLst.size(); i++) {
                        acctsMap = (HashMap) acctsLst.get(i);
                        if (acctsMap.get("PROD_TYPE").equals("TD")) {
                            String accNo = CommonUtil.convertObjToStr(acctsMap.get("ACT_NUM"));
                            if (accNo.lastIndexOf("_") != -1) {
                                accNo = accNo.substring(0, accNo.lastIndexOf("_"));
                                acctsMap.put("ACT_NUM", accNo);
                            }
                        }
                        sqlMap.executeUpdate("updateCustID" + CommonUtil.convertObjToStr(acctsMap.get("PROD_TYPE")), acctsMap);
                        if (acctsMap.get("PROD_TYPE").equals("OA")) {
                            sqlMap.executeUpdate("updateActJointOA", acctsMap);
                            sqlMap.executeUpdate("updateActIntOA", acctsMap);
                            sqlMap.executeUpdate("updateCustHisOA", acctsMap);
                            sqlMap.executeUpdate("updatePoaOA", acctsMap);
                            sqlMap.executeUpdate("updateJointOA", acctsMap);
                            sqlMap.executeUpdate("updateNomDetailOA", acctsMap);
                        }
                        if (acctsMap.get("PROD_TYPE").equals("TD")) {
//                    String accNo = CommonUtil.convertObjToStr(map.get("ACT_NUM"));
//                    if (accNo.lastIndexOf("_")!=-1){
//                        accNo = accNo.substring(0,accNo.lastIndexOf("_"));
//                        map.put("ACT_NUM",accNo);
//                    }
                            sqlMap.executeUpdate("updateDepAuthTD", acctsMap);
                            sqlMap.executeUpdate("updateDepJointActTD", acctsMap);
                            sqlMap.executeUpdate("updateDepNomDetTD", acctsMap);
                            sqlMap.executeUpdate("updateDepPoaTD", acctsMap);
                            sqlMap.executeUpdate("updateDepIntTD", acctsMap);
                            sqlMap.executeUpdate("updateCustHisTD", acctsMap);
                            sqlMap.executeUpdate("updateCustPanNoTD", acctsMap);
                        }
                        if (acctsMap.get("PROD_TYPE").equals("TL") || acctsMap.get("PROD_TYPE").equals("AD")) {
                            sqlMap.executeUpdate("updateLoanAuth", acctsMap);
                            sqlMap.executeUpdate("updateLoanClosingIntTmp", acctsMap);
                            sqlMap.executeUpdate("updateLoansGuarantor", acctsMap);
                            sqlMap.executeUpdate("updateLoanInt", acctsMap);
                            sqlMap.executeUpdate("updateLoansJoint", acctsMap);
                            sqlMap.executeUpdate("updateLoansPoa", acctsMap);
                            sqlMap.executeUpdate("updateLoanSecDetails", acctsMap);
                            sqlMap.executeUpdate("updateCustHisLoans", acctsMap);
                        }
                        if (acctsMap.get("PROD_TYPE").equals("ATL") || acctsMap.get("PROD_TYPE").equals("AAD")) {
                            sqlMap.executeUpdate("updateCustIDAgriTL", acctsMap);
                            sqlMap.executeUpdate("updateAgriLoanClosingIntTmp", acctsMap);
                            sqlMap.executeUpdate("updateAgriLoansGuarantor", acctsMap);
                            sqlMap.executeUpdate("updateAgriLoanInt", acctsMap);
                            sqlMap.executeUpdate("updateAgriLoansJoint", acctsMap);
                            sqlMap.executeUpdate("updateAgriLoansPoa", acctsMap);
                            sqlMap.executeUpdate("updateAgriLoanSecDetails", acctsMap);
                            sqlMap.executeUpdate("updateCustHisLoans", acctsMap);
                        }
                        //sqlMap.executeUpdate("updateLockerCust", acctsMap); //Locker updation
                        suspendCustomer(acctsMap);
                    }
                }
            }
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    private void suspendCustomer(HashMap authMap) {
        try {
            HashMap suspMap = new HashMap();
            suspMap.put("ACT_NUM", authMap.get("ACT_NUM"));
            suspMap.put("CUST_ID", authMap.get("OLD_CUST_ID"));
            List suspLst = sqlMap.executeQueryForList("checkAllProductsAccount", suspMap);
            if (suspLst != null && suspLst.size() > 0) {
                suspMap = (HashMap) suspLst.get(0);
                if (CommonUtil.convertObjToInt(suspMap.get("COUNT")) == 0) {
                    objCustomerSuspensionTO = new CustomerSuspensionTO();
                    objCustomerSuspensionTO.setCustId(CommonUtil.convertObjToStr(authMap.get("OLD_CUST_ID")));
                    objCustomerSuspensionTO.setStatus("SUSPENDED");
                    objCustomerSuspensionTO.setSuspendedFromDate(ServerUtil.getCurrentDate(_branchCode));
                    objCustomerSuspensionTO.setSuspendedBy(CommonUtil.convertObjToStr(authMap.get("USER_ID")));
                    objCustomerSuspensionTO.setStatusDate(ServerUtil.getCurrentDate(_branchCode));
                    objCustomerSuspensionTO.setRemarks("Merged To " + authMap.get("NEW_CUST_ID"));
                    sqlMap.executeUpdate("insertCustomerSuspensionTO", objCustomerSuspensionTO);
                }
            }
        } catch (Exception e) {
        }
    }

    private void destroyObjects() {
        objTO = null;
    }
}
