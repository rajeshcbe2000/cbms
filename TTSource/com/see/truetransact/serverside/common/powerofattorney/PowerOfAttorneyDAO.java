/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * PowerOfAttorneyDAO.java
 *
 * Created on December 29, 2004, 4:40 PM
 */
package com.see.truetransact.serverside.common.powerofattorney;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.serverside.common.log.*;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.transferobject.common.log.*;
import com.see.truetransact.transferobject.common.powerofattorney.*;
import com.see.truetransact.transferobject.TransferObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.see.truetransact.serverside.customer.CustomerHistoryDAO;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.customer.CustomerHistoryTO;
import com.see.truetransact.commonutil.AcctStatusConstants;
import com.see.truetransact.commonutil.DateUtil;

/**
 *
 * @author 152713
 */
public class PowerOfAttorneyDAO {

    private HashMap poaMap;
    private String borrower_No;
    private String updateMapName = "";
    private String getDataMapName = "";
    private String module;
    private String command = "";
    private String screen = "";

    /**
     * Creates a new instance of PowerOfAttorneyDAO
     */
    public PowerOfAttorneyDAO(String module) {
        this.module = module;
        setUpdateMap(module);
        setGetDataMap(module);
    }

    private void setUpdateMap(String module) {
        this.updateMapName = "PowerAttorneyTO" + module;
    }

    private String getUpdateMap() {
        return this.updateMapName;
    }

    private void setGetDataMap(String module) {
        this.getDataMapName = "getSelectPowerAttorneyTO" + module;
    }

    private String getGetDataMap() {
        return this.getDataMapName;
    }

    public void setPoAMap(HashMap poaMap) {
        this.poaMap = poaMap;
    }

    private HashMap getPoAMap() {
        return this.poaMap;
    }

    public void setBorrower_No(String strBorrowNo) {
        this.borrower_No = strBorrowNo;
    }

    private String getBorrower_No() {
        return this.borrower_No;
    }

    public void getData(HashMap returnMap, String where, SqlMap sqlMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList(getGetDataMap(), where);
        returnMap.put("PowerAttorneyTO", list);
        list = null;
    }

    public void executePoATabQuery(LogTO logTO, LogDAO logDAO, SqlMap sqlMap) throws Exception {
        PowerAttorneyTO objTermLoanPowerAttorneyTO;
        Set keySet;
        Object[] objKeySet;
        keySet = poaMap.keySet();
        objKeySet = (Object[]) keySet.toArray();
        // To retrieve the TermLoanAuthorizedSignatoryTO from the authorizedMap
        for (int i = poaMap.size() - 1, j = 0; i >= 0; --i, ++j) {
            objTermLoanPowerAttorneyTO = (PowerAttorneyTO) poaMap.get(objKeySet[j]);
//            objTermLoanPowerAttorneyTO.setBorrowNo(getBorrower_No());
            System.out.println("objTermLoanPowerAttorneyTO $$$$" + objTermLoanPowerAttorneyTO);
            if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                objTermLoanPowerAttorneyTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                objTermLoanPowerAttorneyTO.setState(CommonConstants.STATUS_DELETED);
            }
            logTO.setData(objTermLoanPowerAttorneyTO.toString());
            logTO.setPrimaryKey(objTermLoanPowerAttorneyTO.getKeyData());
            logTO.setStatus(objTermLoanPowerAttorneyTO.getCommand());
            if (CommonUtil.convertObjToStr(getScreen()).equals("LOCKER_ISSUE")) {
                objTermLoanPowerAttorneyTO.setBorrowNo(getBorrower_No());
            }
            executeUpdate(getUpdateMap(), objTermLoanPowerAttorneyTO, sqlMap);

            // Inserting into Customer History Table
            if (objTermLoanPowerAttorneyTO.getCustId() != null) {
                CustomerHistoryTO objCustomerHistoryTO = new CustomerHistoryTO();
                objCustomerHistoryTO.setAcctNo(objTermLoanPowerAttorneyTO.getBorrowNo());//getBorrower_No());
//                if(CommonUtil.convertObjToStr(getScreen()).equals("LOCKER_ISSUE"))
//                    objCustomerHistoryTO.setAcctNo(getBorrower_No());
                objCustomerHistoryTO.setCustId(objTermLoanPowerAttorneyTO.getCustId());
                objCustomerHistoryTO.setProductType(module);
                objCustomerHistoryTO.setProdId("");
                objCustomerHistoryTO.setRelationship(AcctStatusConstants.POA);
                objCustomerHistoryTO.setFromDt(ServerUtil.getCurrentDate(logTO.getBranchId()));
                objCustomerHistoryTO.setCommand(objTermLoanPowerAttorneyTO.getCommand());
                CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
                objCustomerHistoryTO = null;
            }

            logDAO.addToLog(logTO);
            objTermLoanPowerAttorneyTO = null;
        }
        setCommand("");
        keySet = null;
        objKeySet = null;
        objTermLoanPowerAttorneyTO = null;
    }

    private void executeUpdate(String strTOName, TransferObject termLoanTO, SqlMap sqlMap) throws Exception {
        StringBuffer sbMapName = new StringBuffer();
        if (termLoanTO.getCommand() != null) {
            sbMapName.append(termLoanTO.getCommand().toLowerCase());
            sbMapName.append(strTOName);
            sqlMap.executeUpdate(sbMapName.toString(), termLoanTO);
        }
    }

    /**
     * Getter for property command.
     *
     * @return Value of property command.
     */
    public java.lang.String getCommand() {
        return command;
    }

    /**
     * Setter for property command.
     *
     * @param command New value of property command.
     */
    public void setCommand(java.lang.String command) {
        this.command = command;
    }

    /**
     * Getter for property screen.
     *
     * @return Value of property screen.
     */
    public java.lang.String getScreen() {
        return screen;
    }

    /**
     * Setter for property screen.
     *
     * @param screen New value of property screen.
     */
    public void setScreen(java.lang.String screen) {
        this.screen = screen;
    }
}
