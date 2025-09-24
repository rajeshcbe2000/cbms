/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SettlementDAO.java
 *
 * Created on December 29, 2004, 4:40 PM
 */
package com.see.truetransact.serverside.termloan.settlement;

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
import com.see.truetransact.transferobject.termloan.settlement.SettlementBankTO;
import com.see.truetransact.transferobject.termloan.settlement.SettlementTO;
import com.see.truetransact.transferobject.termloan.accounttransfer.ActTransTO;

/**
 *
 * @author 152713
 */
public class SettlementDAO {

    private HashMap setMap;
    private HashMap setBankMap;
    private HashMap setActTrans;
    private String borrower_No;
    private String updateSetMapName = "";
    private String getDataSetMapName = "";
    private String updateSetBnkMapName = "";
    private String getDataSetBnkMapName = "";
    private String updateActTranMapName = "";
    private String getDataActTranMapName = "";
    private String module;
    private String command = "";
    private String loanAcctNumber = "";

    /**
     * Creates a new instance of PowerOfAttorneyDAO
     */
    public SettlementDAO(String module) {
        this.module = module;
        setUpdateSetMap(module);
        setGetDataSetMap(module);
        setUpdateSetBnkMap(module);
        setGetDataSetBnkMap(module);
        setUpdateActTranMapName(module);
        setGetDataActTranMapName(module);
    }

    private void setUpdateActTranMapName(String module) {
        this.updateActTranMapName = "ActTransTO" + module;
    }

    private String getUpdateActTranMapName() {
        return this.updateActTranMapName;
    }

    private void setGetDataActTranMapName(String module) {
        this.getDataActTranMapName = "getSelectActTransTO" + module;
    }

    private String getGetDataActTranMapName() {
        return this.getDataActTranMapName;
    }

    private void setUpdateSetMap(String module) {
        this.updateSetMapName = "SettlementTO" + module;
    }

    private String getUpdateSetMap() {
        return this.updateSetMapName;
    }

    private void setGetDataSetMap(String module) {
        this.getDataSetMapName = "getSelectSettlementTO" + module;
    }

    private String getGetDataMap() {
        return this.getDataSetMapName;
    }

    private void setUpdateSetBnkMap(String module) {
        this.updateSetBnkMapName = "SettlementBankTO" + module;
    }

    private String getUpdateSetBnkMap() {
        return this.updateSetBnkMapName;
    }

    private void setGetDataSetBnkMap(String module) {
        this.getDataSetBnkMapName = "getSelectSettlementBankTO" + module;
    }

    private String getGetDataBnkMap() {
        return this.getDataSetBnkMapName;
    }

    public void setSetMap(HashMap setMap) {
        this.setMap = setMap;
    }

    private HashMap getSetMap() {
        return this.setMap;
    }

    public void setBorrower_No(String strBorrowNo) {
        this.borrower_No = strBorrowNo;
    }

    private String getBorrower_No() {
        return this.borrower_No;
    }

    public HashMap getDataActTrans(HashMap returnMap, SqlMap sqlMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList(getGetDataActTranMapName(), returnMap);
        returnMap.put("ActTransTO", list);
        list = null;
        return returnMap;
    }

    public HashMap getDataSet(HashMap returnMap, SqlMap sqlMap) throws Exception {
        List list = (List) sqlMap.executeQueryForList(getGetDataMap(), returnMap);
        returnMap.put("SettlementTO", list);
        list = null;
        list = (List) sqlMap.executeQueryForList(getGetDataBnkMap(), returnMap);
        returnMap.put("SettlementBankTO", list);
        return returnMap;
    }

    public void executeSetTabQuery(SqlMap sqlMap) throws Exception {
        SettlementTO objSettlementTO;
        SettlementBankTO objSettlementBankTO;
        Set keySet;
        Object[] objKeySet;
        keySet = setMap.keySet();
        objKeySet = (Object[]) keySet.toArray();
        System.out.println("getLoanAcctNumber" + getLoanAcctNumber());
        // To retrieve the TermLoanAuthorizedSignatoryTO from the authorizedMap
        for (int i = setMap.size() - 1, j = 0; i >= 0; --i, ++j) {
            objSettlementTO = (SettlementTO) setMap.get(objKeySet[j]);
            objSettlementTO.setAcctNum(getLoanAcctNumber());
            System.out.println("objTermLoanPowerAttorneyTO $$$$" + objSettlementTO);
            if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                objSettlementTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                objSettlementTO.setStatus(CommonConstants.STATUS_DELETED);
            }
            executeUpdate(getUpdateSetMap(), objSettlementTO, sqlMap);
            objSettlementTO = null;
        }


        System.out.println("setBankMap$$$$" + setBankMap);
        objSettlementBankTO = (SettlementBankTO) setBankMap.get(String.valueOf(0));
        objSettlementBankTO.setLoanAcctNum(getLoanAcctNumber());
        System.out.println("objSettlementBankTO $$$$" + objSettlementBankTO);
        if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
            objSettlementBankTO.setCommand(CommonConstants.TOSTATUS_DELETE);
        }
        executeUpdate(getUpdateSetBnkMap(), objSettlementBankTO, sqlMap);

        objSettlementTO = null;
        objSettlementBankTO = null;
        setCommand("");
        keySet = null;
        objKeySet = null;
        objSettlementTO = null;
    }

    public void executeActTransTabQuery(SqlMap sqlMap) throws Exception {
        ActTransTO objActTransTO;
        System.out.println("setActTrans" + setActTrans);
        if (setActTrans != null && setActTrans.size() > 0) {
            objActTransTO = (ActTransTO) setActTrans.get(String.valueOf(0));
            objActTransTO.setAcctNum(getLoanAcctNumber());
            System.out.println("objActTransTO $$$$" + objActTransTO);
            if (getCommand().equals(CommonConstants.TOSTATUS_DELETE)) {
                objActTransTO.setCommand(CommonConstants.TOSTATUS_DELETE);
                objActTransTO.setStatus(CommonConstants.STATUS_DELETED);
            } else {
                objActTransTO.setStatus(CommonConstants.STATUS_CREATED);
            }
            executeUpdate(getUpdateActTranMapName(), objActTransTO, sqlMap);
        }
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
     * Getter for property setBankMap.
     *
     * @return Value of property setBankMap.
     */
    public java.util.HashMap getSetBankMap() {
        return setBankMap;
    }

    /**
     * Setter for property setBankMap.
     *
     * @param setBankMap New value of property setBankMap.
     */
    public void setSetBankMap(java.util.HashMap setBankMap) {
        this.setBankMap = setBankMap;
    }

    /**
     * Getter for property setActTrans.
     *
     * @return Value of property setActTrans.
     */
    public java.util.HashMap getSetActTrans() {
        return setActTrans;
    }

    /**
     * Setter for property setActTrans.
     *
     * @param setActTrans New value of property setActTrans.
     */
    public void setSetActTrans(java.util.HashMap setActTrans) {
        this.setActTrans = setActTrans;
    }

    /**
     * Getter for property loanAcctNumber.
     *
     * @return Value of property loanAcctNumber.
     */
    public java.lang.String getLoanAcctNumber() {
        return loanAcctNumber;
    }

    /**
     * Setter for property loanAcctNumber.
     *
     * @param loanAcctNumber New value of property loanAcctNumber.
     */
    public void setLoanAcctNumber(java.lang.String loanAcctNumber) {
        this.loanAcctNumber = loanAcctNumber;
    }
}
