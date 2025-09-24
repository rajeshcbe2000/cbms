/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchManagementDAO.java
 *
 * Created on September 08, 2003, 1:00 PM
 * Modified on Feb, 09,2004
 */
package com.see.truetransact.serverside.sysadmin.branch;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.sysadmin.branch.*;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.commonutil.CommonConstants;
// For Maintaining Logs...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

/**
 * This is used for BranchManagement Data Access.
 *
 * @author Karthik @ Modified by Hemant.
 */
public class BranchManagementDAO extends TTDAO {

    private SqlMap sqlMap;
    private BranchMasterTO objBranchMasterTO;
    private BranchPhoneTO objBranchPhoneTO;
    private List phoneList;
    private Iterator phoneNos;
    private LogTO logTO;
    private LogDAO logDAO;
    private Date currDt = null;
    /**
     * Creates a new instance of BranchManagementDAO
     */
    public BranchManagementDAO() throws ServiceLocatorException {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    /**
     * To insert data
     */
    private void insertData() throws Exception {
        HashMap data = new HashMap();
        List lst = (List) sqlMap.executeQueryForList("getBank_CodeFromBank", data);
        if (lst != null) {
            if (lst.size() > 0) {
                data = (HashMap) lst.get(0);
                objBranchMasterTO.setBankCode(CommonUtil.convertObjToStr(data.get("BANK_CODE")));
            }
        }

        sqlMap.executeUpdate("insertBranchMasterTO", objBranchMasterTO);
        logTO.setData(objBranchMasterTO.toString());
        logTO.setPrimaryKey(objBranchMasterTO.getKeyData());
        logTO.setStatus(objBranchMasterTO.getStatus());
        logDAO.addToLog(logTO);
        if (phoneList != null && phoneList.size() > 0) {
            int size = phoneList.size();
            int i = 0;
            while (i < size) {
                sqlMap.executeUpdate("insertBranchPhoneTO", (BranchPhoneTO) phoneList.get(i));
                logTO.setData(((BranchPhoneTO) phoneList.get(i)).toString());
                logDAO.addToLog(logTO);
                i++;
            }
        }
        //insertPhoneData();
    }

    /**
     * To insert phone data
     */
    /*
     * private void insertPhoneData() throws Exception { try { phoneNos =
     * phoneList.values().iterator(); final int phoneListSize =
     * phoneList.size(); for(int i = 0; i < phoneListSize; i++){
     * objBranchPhoneTO = (BranchPhoneTO)phoneNos.next();
     * sqlMap.executeUpdate("insertBranchPhoneTO", objBranchPhoneTO); } } catch
     * (Exception e) { // if something goes wrong, rollback the transaction
     * sqlMap.rollbackTransaction(); throw new TransRollbackException(e); }
    }/*
     */
    /**
     * To update data
     */
    private void updateData() throws Exception {
        sqlMap.executeUpdate("updateBranchMasterTO", objBranchMasterTO);
        logTO.setData(objBranchMasterTO.toString());
        logTO.setPrimaryKey(objBranchMasterTO.getKeyData());
        logTO.setStatus(objBranchMasterTO.getStatus());
        logDAO.addToLog(logTO);

        if (phoneList != null && phoneList.size() > 0) {
            sqlMap.executeUpdate("deleteBranchPhoneTO", (BranchPhoneTO) phoneList.get(0));
            logTO.setData(((BranchPhoneTO) phoneList.get(0)).toString());
            logTO.setPrimaryKey(((BranchPhoneTO) phoneList.get(0)).getKeyData());
            logTO.setStatus(CommonConstants.TOSTATUS_DELETE);
            logDAO.addToLog(logTO);
            int size = phoneList.size();
            int i = 0;
            while (i < size) {
                sqlMap.executeUpdate("insertBranchPhoneTO", (BranchPhoneTO) phoneList.get(i));
                logTO.setData(((BranchPhoneTO) phoneList.get(i)).toString());
                logTO.setPrimaryKey(((BranchPhoneTO) phoneList.get(i)).getKeyData());
                logTO.setStatus(CommonConstants.TOSTATUS_INSERT);
                i++;
            }
        } else {
            BranchPhoneTO objBPTO = new BranchPhoneTO();
            objBPTO.setBranchCode(objBranchMasterTO.getBranchCode());
            sqlMap.executeUpdate("deleteBranchPhoneTO", objBPTO);
            logTO.setData(objBPTO.toString());
            logTO.setPrimaryKey(objBPTO.getKeyData());
            logTO.setStatus(CommonConstants.TOSTATUS_DELETE);
        }

        //sqlMap.executeUpdate("deleteBranchPhoneTO", objBranchMasterTO);
        //insertPhoneData();
    }

    /**
     * To delete data
     */
    private void deleteData() throws Exception {
        sqlMap.executeUpdate("deleteBranchMasterTO", objBranchMasterTO);
        logTO.setData(objBranchMasterTO.toString());
        logTO.setPrimaryKey(objBranchMasterTO.getKeyData());
        logTO.setStatus(objBranchMasterTO.getStatus());
        logDAO.addToLog(logTO);
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
    }

    /**
     * Standard method to perform necessary insert,update & delete tasks
     */
    public HashMap execute(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        logDAO = new LogDAO();
        setInitialValuesForLogTO(obj);
        objBranchMasterTO = (BranchMasterTO) obj.get("BranchMasterTO");
        phoneList = (List) obj.get("BranchPhoneTO");
        // start the transaction
        sqlMap.startTransaction();
        //To perform corresponding actions based on command object
        if (objBranchMasterTO.getCommand().equals(CommonConstants.TOSTATUS_INSERT)) {
            objBranchMasterTO.setCreatedDt(currDt);
            insertData();
        } else if (objBranchMasterTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (objBranchMasterTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
            deleteData();
        } else {
            sqlMap.rollbackTransaction();
            throw new NoCommandException();
        }

        if (!isDayEndExist(objBranchMasterTO.getBranchCode())) {
            HashMap dayEndMap = new HashMap();
            dayEndMap.put("CURR_APPL_DT", DateUtil.addDays(objBranchMasterTO.getOpeningDt(), -1));
            dayEndMap.put("BRANCH_CODE", objBranchMasterTO.getBranchCode());
            dayEndMap.put("END_DAY_STATUS", "COMPLETED");
            sqlMap.executeUpdate("insertBranchDayEnd", dayEndMap);
        }
        //TODO : If it is not authorized it can update activation date
//        } else {
//            if (objBranchMasterTO.getCommand().equals(CommonConstants.TOSTATUS_UPDATE)) {
//                HashMap dayEndMap = new HashMap();
//                dayEndMap.put("CURR_APPL_DT", objBranchMasterTO.getOpeningDt());
//                dayEndMap.put("BRANCH_CODE", objBranchMasterTO.getBranchCode());
//                sqlMap.executeUpdate("updateBranchDayEnd", dayEndMap);
//            } else if ( objBranchMasterTO.getCommand().equals(CommonConstants.TOSTATUS_DELETE)){
//                HashMap dayEndMap = new HashMap();
//                dayEndMap.put("BRANCH_CODE", objBranchMasterTO.getBranchCode());
//                sqlMap.executeUpdate("deleteBranchDayEnd", dayEndMap);
//            }
//        }

        // commit transaction
        sqlMap.commitTransaction();
        makeNull();
        return null;
    }

    private boolean isDayEndExist(String branchID) throws Exception {
        int count = 0;
        count = CommonUtil.convertObjToInt(sqlMap.executeQueryForObject("getBranchDayEndExist", branchID));
        return count > 0 ? true : false;
    }

    /**
     * Standard method to get Branch data for the given Branch code
     */
    public HashMap executeQuery(HashMap obj) throws Exception {
        _branchCode = (String) obj.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        List list = (List) sqlMap.executeQueryForList("getSelectBranchMasterTO", obj);
        final HashMap branchMap = new HashMap();
        objBranchMasterTO = (BranchMasterTO) list.get(0);
        branchMap.put("BranchMasterTO", objBranchMasterTO);

        list = (List) sqlMap.executeQueryForList("getSelectBranchPhoneTO", obj);
        /*
         * phoneList = new HashMap(); final int listSize = list.size(); for(int
         * i = 0; i < listSize; i++){ objBranchPhoneTO =
         * (BranchPhoneTO)list.get(i); objBranchPhoneTO.setPhoneId(i+1);
         * phoneList.put(new Integer(i+1), objBranchPhoneTO);
        }/*
         */
        branchMap.put("BranchPhoneTO", list);
        makeNull();
        return branchMap;
    }

    /**
     * To nullify objects
     */
    private void makeNull() {
        objBranchMasterTO = null;
        objBranchPhoneTO = null;
        phoneList = null;
        phoneNos = null;
        logTO = null;
        logDAO = null;
    }

    public static void main(String[] arg) {
        try {
            BranchManagementDAO dao = new BranchManagementDAO();
            BranchMasterTO objBM = new BranchMasterTO();
            BranchPhoneTO objBP = new BranchPhoneTO();

            objBM.setArea("a");
            objBM.setAreaCode("a");
            objBM.setAvgCashStock(new Double("10"));
            objBM.setBranchCode("B001");
            objBM.setBranchDbIp("10.10.10.10");
            objBM.setBranchDbName("Domlur");
            objBM.setBranchDbPort(new Double("10"));
            objBM.setBranchName("a");
            objBM.setChkBalanceLimit("Y");
            objBM.setCity("a");
            objBM.setCountryCode("a");
            objBM.setDbDriver("a");
            objBM.setDbPassword("a");
            objBM.setDbUrl("a");
            objBM.setDbUserId("a");
            objBM.setIpAddr("10.10.10.10");
            objBM.setMaxCashStock(new Double("10"));
            objBM.setPinCode("a");
            objBM.setPort(new Double("1021"));
            objBM.setState("a");
            objBM.setStreet("a");
            objBM.setStatus("MODIFIED");

            objBP.setBranchCode("B001");
            objBP.setContactNo("11111111");
            objBP.setContactType("LandLine");
            //            objBP.setPhoneId(10);

            List phoneList = new ArrayList();
            phoneList.add(objBP);

            objBP = new BranchPhoneTO();
            objBP.setBranchCode("B001");
            objBP.setContactNo("11111111");
            objBP.setContactType("Mobile");
            //            objBP.setPhoneId(10);
            phoneList.add(objBP);

            HashMap hash = new HashMap();
            objBM.setCommand(CommonConstants.TOSTATUS_UPDATE);
            hash.put("BranchMasterTO", objBM);
            hash.put("BranchPhoneTO", phoneList);
            dao.execute(hash);
            phoneList = null;

            hash = new HashMap();
            hash.put("BRANCHCODE", "B001");
            hash = dao.executeQuery(hash);
            objBM = (BranchMasterTO) hash.get("BranchMasterTO");
            phoneList = (List) hash.get("BranchPhoneTO");

            System.out.println(objBM.getArea());
            System.out.println(objBM.getAreaCode());
            System.out.println(objBM.getAvgCashStock().toString());
            System.out.println(objBM.getBranchCode());
            System.out.println(objBM.getBranchDbIp());
            System.out.println(objBM.getBranchDbName());
            System.out.println(objBM.getBranchDbPort().toString());
            System.out.println(objBM.getBranchName());
            System.out.println(objBM.getChkBalanceLimit());
            System.out.println(objBM.getCity());
            System.out.println(objBM.getCountryCode());
            System.out.println(objBM.getDbDriver());
            System.out.println(objBM.getDbPassword());
            System.out.println(objBM.getDbUrl());
            System.out.println(objBM.getDbUserId());
            System.out.println(objBM.getIpAddr());
            System.out.println(objBM.getMaxCashStock().toString());
            System.out.println(objBM.getPinCode());
            System.out.println(objBM.getPort().toString());
            System.out.println(objBM.getState());
            System.out.println(objBM.getStreet());
            int i = 0;
            while (i < phoneList.size()) {
                objBP = (BranchPhoneTO) phoneList.get(i);
                System.out.println(objBP.getBranchCode());
                System.out.println(objBP.getContactNo());
                System.out.println(objBP.getContactType());
                //                System.out.println(objBP.getPhoneId());
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
