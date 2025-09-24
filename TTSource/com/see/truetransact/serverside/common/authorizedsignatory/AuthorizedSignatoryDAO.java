/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AuthorizedSignatoryDAO.java
 *
 * Created on December 29, 2004, 2:39 PM
 */
package com.see.truetransact.serverside.common.authorizedsignatory;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.serverutil.ServerUtil;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.TTDAO;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.serverside.common.log.*;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.transferobject.common.log.*;
import com.see.truetransact.transferobject.common.authorizedsignatory.*;
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
public class AuthorizedSignatoryDAO {

    private HashMap authorizedMap;
    private HashMap authorizedInstructionMap;
    private String borrower_No;
    private String updateAuthorizeMapName = "";
    private String getAuthorizeDataMapName = "";
    private String updateAuthorizeInstructionMapName = "";
    private String getAuthorizeInstructionDataMapName = "";
    private String module;
    private String command = "";
    private String screen = "";

    /**
     * Creates a new instance of AuthorizedSignatoryDAO
     */
    public AuthorizedSignatoryDAO(String module) {
        this.module = module;
        setUpdateMap(module);
        setGetDataMap(module);
    }

    private void setUpdateMap(String module) {
        this.updateAuthorizeMapName = "AuthorizedSignatoryTO" + module;
        this.updateAuthorizeInstructionMapName = "AuthorizedSignatoryInstructionTO" + module;
    }

    private String getUpdateAuthorizeMap() {
        return this.updateAuthorizeMapName;
    }

    private String getUpdateAuthorizeInstructionMap() {
        return this.updateAuthorizeInstructionMapName;
    }

    private void setGetDataMap(String module) {
        this.getAuthorizeDataMapName = "getSelectAuthorizedSignatoryTO" + module;
        this.getAuthorizeInstructionDataMapName = "getSelectAuthorizedSignatoryInstructionTO" + module;
    }

    private String getAuthorizeDataMap() {
        return this.getAuthorizeDataMapName;
    }

    private String getAuthorizeInstructionDataMap() {
        return this.getAuthorizeInstructionDataMapName;
    }

    public void setAuthorizeMap(HashMap authorizedMap) {
        this.authorizedMap = authorizedMap;
    }

    private HashMap getAuthorizeMap() {
        return this.authorizedMap;
    }

    public void setBorrower_No(String strBorrowNo) {
        this.borrower_No = strBorrowNo;
    }

    private String getBorrower_No() {
        return this.borrower_No;
    }

    public void getData(HashMap returnMap, String where, SqlMap sqlMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList(getAuthorizeDataMap(), where);
        returnMap.put("AuthorizedSignatoryTO", list);
        list = null;

        list = (List) sqlMap.executeQueryForList(getAuthorizeInstructionDataMap(), where);
        returnMap.put("AuthorizedSignatoryInstructionTO", list);
        list = null;
    }

    public void executeAuthorizedTabQuery(LogTO logTO, LogDAO logDAO, SqlMap sqlMap) throws Exception {
        executeAuthorizeQuery(logTO, logDAO, sqlMap);
        executeAuthorizeInstructionQuery(logTO, logDAO, sqlMap);
    }

    private void executeAuthorizeQuery(LogTO logTO, LogDAO logDAO, SqlMap sqlMap) throws Exception {
        AuthorizedSignatoryTO objTermLoanAuthorizedSignatoryTO;
        Set keySet;
        Object[] objKeySet;
        keySet = authorizedMap.keySet();
        objKeySet = (Object[]) keySet.toArray();

        CustomerHistoryTO objCustomerHistoryTO;

        // To retrieve the AuthorizedSignatoryTO from the authorizedMap
        for (int i = authorizedMap.size() - 1, j = 0; i >= 0; --i, ++j) {
            objTermLoanAuthorizedSignatoryTO = (AuthorizedSignatoryTO) authorizedMap.get(objKeySet[j]);
            objTermLoanAuthorizedSignatoryTO.setBorrowNo(getBorrower_No());
            objTermLoanAuthorizedSignatoryTO.setSlNo(CommonUtil.convertObjToStr(objTermLoanAuthorizedSignatoryTO.getSlNo()));
            System.out.println("objTermLoanAuthorizedSignatoryTO###" + objTermLoanAuthorizedSignatoryTO);
            if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                objTermLoanAuthorizedSignatoryTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                objTermLoanAuthorizedSignatoryTO.setState(CommonConstants.STATUS_DELETED);
            }
            logTO.setData(objTermLoanAuthorizedSignatoryTO.toString());
            logTO.setPrimaryKey(objTermLoanAuthorizedSignatoryTO.getKeyData());
            logTO.setStatus(objTermLoanAuthorizedSignatoryTO.getCommand());
            if (CommonUtil.convertObjToStr(getScreen()).equals("LOCKER_ISSUE")) {
                objTermLoanAuthorizedSignatoryTO.setBorrowNo(getBorrower_No());
            }
            executeUpdate(getUpdateAuthorizeMap(), objTermLoanAuthorizedSignatoryTO, sqlMap);

            if (objTermLoanAuthorizedSignatoryTO.getCustId() != null) {
                // Inserting into Customer History Table
                objCustomerHistoryTO = new CustomerHistoryTO();
                objCustomerHistoryTO.setAcctNo(getBorrower_No());
                objCustomerHistoryTO.setCustId(objTermLoanAuthorizedSignatoryTO.getCustId());
                objCustomerHistoryTO.setProductType(module);
                objCustomerHistoryTO.setProdId("");
                objCustomerHistoryTO.setRelationship(AcctStatusConstants.AUTHORIZED_SIGNATORY);
                objCustomerHistoryTO.setFromDt(ServerUtil.getCurrentDate(logTO.getBranchId()));
                objCustomerHistoryTO.setCommand(objTermLoanAuthorizedSignatoryTO.getCommand());
                CustomerHistoryDAO.insertToHistory(objCustomerHistoryTO);
                objCustomerHistoryTO = null;
            }

            logDAO.addToLog(logTO);
            objTermLoanAuthorizedSignatoryTO = null;
        }
        setCommand("");
        keySet = null;
        objKeySet = null;
        objTermLoanAuthorizedSignatoryTO = null;
    }

    private void executeAuthorizeInstructionQuery(LogTO logTO, LogDAO logDAO, SqlMap sqlMap) throws Exception {
        AuthorizedSignatoryInstructionTO objAuthorizedSignatoryInstructionTO;
        Set keySet;
        Object[] objKeySet;
        keySet = authorizedInstructionMap.keySet();
        objKeySet = (Object[]) keySet.toArray();

        // To retrieve the AuthorizedSignatoryInstructionTO from the authorizedMap
        for (int i = authorizedInstructionMap.size() - 1, j = 0; i >= 0; --i, ++j) {
            objAuthorizedSignatoryInstructionTO = (AuthorizedSignatoryInstructionTO) authorizedInstructionMap.get(objKeySet[j]);
            objAuthorizedSignatoryInstructionTO.setBorrowNo(getBorrower_No());
            logTO.setData(objAuthorizedSignatoryInstructionTO.toString());
            logTO.setPrimaryKey(objAuthorizedSignatoryInstructionTO.getKeyData());
            logTO.setStatus(objAuthorizedSignatoryInstructionTO.getCommand());

            executeUpdate(getUpdateAuthorizeInstructionMap(), objAuthorizedSignatoryInstructionTO, sqlMap);
            logDAO.addToLog(logTO);
            objAuthorizedSignatoryInstructionTO = null;
        }
        keySet = null;
        objKeySet = null;
        objAuthorizedSignatoryInstructionTO = null;
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
     * Getter for property authorizeInstructionMap.
     *
     * @return Value of property authorizeInstructionMap.
     */
    public java.util.HashMap getAuthorizedInstructionMap() {
        return authorizedInstructionMap;
    }

    /**
     * Setter for property authorizeInstructionMap.
     *
     * @param authorizeInstructionMap New value of property
     * authorizeInstructionMap.
     */
    public void setAuthorizedInstructionMap(java.util.HashMap authorizedInstructionMap) {
        this.authorizedInstructionMap = authorizedInstructionMap;
    }

    /**
     * Getter for property getAuthorizeInstructionDataMapName.
     *
     * @return Value of property getAuthorizeInstructionDataMapName.
     */
    public java.lang.String getGetAuthorizeInstructionDataMapName() {
        return getAuthorizeInstructionDataMapName;
    }

    /**
     * Setter for property getAuthorizeInstructionDataMapName.
     *
     * @param getAuthorizeInstructionDataMapName New value of property
     * getAuthorizeInstructionDataMapName.
     */
    public void setGetAuthorizeInstructionDataMapName(java.lang.String getAuthorizeInstructionDataMapName) {
        this.getAuthorizeInstructionDataMapName = getAuthorizeInstructionDataMapName;
    }

    /**
     * Getter for property updateAuthorizeInstructionMapName.
     *
     * @return Value of property updateAuthorizeInstructionMapName.
     */
    public java.lang.String getUpdateAuthorizeInstructionMapName() {
        return updateAuthorizeInstructionMapName;
    }

    /**
     * Setter for property updateAuthorizeInstructionMapName.
     *
     * @param updateAuthorizeInstructionMapName New value of property
     * updateAuthorizeInstructionMapName.
     */
    public void setUpdateAuthorizeInstructionMapName(java.lang.String updateAuthorizeInstructionMapName) {
        this.updateAuthorizeInstructionMapName = updateAuthorizeInstructionMapName;
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
