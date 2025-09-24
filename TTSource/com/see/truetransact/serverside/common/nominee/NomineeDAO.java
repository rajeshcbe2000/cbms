/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * NomineeDAO.java
 *
 * Created on Fri Dec 31 08:37:24 IST 2004
 */
package com.see.truetransact.serverside.common.nominee;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.NoCommandException;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.transferobject.common.nominee.NomineeTO;
//__ To Log the Data...
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;

import com.see.truetransact.serverside.customer.CustomerHistoryDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.customer.CustomerHistoryTO;
import com.see.truetransact.commonutil.AcctStatusConstants;
import java.util.Date;
/**
 * Nominee DAO.
 *
 */
public class NomineeDAO extends TTDAO {

    private static SqlMap sqlMap = null;
    private NomineeTO objNomineeTO;
    private Date currDt = null;
    /**
     * Creates a new instance of NomineeDAO
     */
    public NomineeDAO() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    private HashMap getData(HashMap map) throws Exception {
        HashMap returnMap = new HashMap();
        //String where = (String) map.get(CommonConstants.MAP_WHERE);
        //List list = (List) sqlMap.executeQueryForList("select query map", where);
        //returnMap.put("return key ", list);
        return returnMap;
    }

    public List getDataList(String where, String screen) throws Exception {
        List list = (List) sqlMap.executeQueryForList("getSelectNomineeTO" + screen, where);
        return list;
    }

    public void insertData(ArrayList nomineeList, String accountNo, boolean del, String userID, String screen, LogTO objLogTO, LogDAO objLogDAO) throws Exception {
        try {
            //            sqlMap.startTransaction();
            int count = nomineeList.size();
            NomineeTO objNomineeTO;
            try {
                for (int i = 0; i < count; i++) {
                    objNomineeTO = (NomineeTO) nomineeList.get(i);
                    objNomineeTO.setActNum(accountNo);

                    // If Deleted Record...
                    if (del) {
                        objNomineeTO.setStatus(CommonConstants.STATUS_DELETED);
                    } else { //__ If Insert of Modidied Record
                        /**
                         * If the record Exists, Modify it, else add the New
                         * record...
                         */
                        if (CommonUtil.convertObjToStr(objNomineeTO.getStatus()).equalsIgnoreCase("")) {
                            objNomineeTO.setStatus(CommonConstants.STATUS_CREATED);
                        } else {
                            objNomineeTO.setStatus(CommonConstants.STATUS_MODIFIED);
                        }
                    }
                    // Added by nithya on 17-09-2018 for KD 240 - Nominee details updation issue in deposit renewal screen 
                    if (!(objNomineeTO.getStatus().equalsIgnoreCase(CommonConstants.STATUS_DELETED))) {
                        objNomineeTO.setNomineeCurrStatus("EXISTING");
                    } else {
                        objNomineeTO.setNomineeCurrStatus("PREVIOUS");
                    }
                    // END - KD 240 - Nominee details updation issue in deposit renewal screen 
                    objNomineeTO.setStatusBy(userID);
                    objNomineeTO.setStatusDt(currDt);
                    sqlMap.executeUpdate("insertNomineeTO" + screen, objNomineeTO);


                    // Inserting into Customer History Table
                    if (CommonUtil.convertObjToStr(objNomineeTO.getCustId()).length() > 1) {
                        CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO();
                        objCustomerHistoryTO.setAcctNo(accountNo);
                        objCustomerHistoryTO.setCustId(objNomineeTO.getCustId());
                        objCustomerHistoryTO.setProductType(screen);
                        objCustomerHistoryTO.setProdId("");
                        objCustomerHistoryTO.setRelationship(AcctStatusConstants.NOMINEE);
                        objCustomerHistoryTO.setFromDt(currDt);
                        CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
                        objCustomerHistoryTO = null;
                    }
                    objLogTO.setData(objNomineeTO.toString());
                    objLogTO.setPrimaryKey(objNomineeTO.getKeyData());
                    objLogDAO.addToLog(objLogTO);
                    objNomineeTO = null;
                }

            } catch (Exception e) {
                System.out.println("Error in enterNomineeData in DAO");
                e.printStackTrace();
                sqlMap.rollbackTransaction();
            }
            //            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            //            throw new TransRollbackException(e);
        }
    }

    private void updateData() throws Exception {
        try {
            sqlMap.startTransaction();
            sqlMap.executeUpdate("updateNomineeTO", objNomineeTO);
            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public void deleteData(String accountNo, String screen) throws Exception {
        try {
            //            sqlMap.startTransaction();
            sqlMap.executeUpdate("deleteNomineeTO" + screen, accountNo);
            //            sqlMap.commitTransaction();
        } catch (Exception e) {
            sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    public static void main(String str[]) {
        try {
            NomineeDAO dao = new NomineeDAO();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap execute(HashMap map) throws Exception {
        objNomineeTO = (NomineeTO) map.get("NomineeTO");
        final String command = objNomineeTO.getCommand();
        _branchCode = (String) map.get(CommonConstants.BRANCH_ID);
        currDt = ServerUtil.getCurrentDate(_branchCode);
        if (command.equals(CommonConstants.TOSTATUS_INSERT)) {
            //            insertData();
        } else if (command.equals(CommonConstants.TOSTATUS_UPDATE)) {
            updateData();
        } else if (command.equals(CommonConstants.TOSTATUS_DELETE)) {
            //            deleteData();
        } else {
            throw new NoCommandException();
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
        objNomineeTO = null;
    }
}
