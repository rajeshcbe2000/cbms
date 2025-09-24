/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MultipleCashTransactionTO.java
 *
 * Created on 16 September, 2011, 2:24 PM
 */
package com.see.truetransact.transferobject.transaction.multipleCash;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
import java.util.*;

/**
 *
 * @author aravind
 */
public class MultipleCashTransactionTO extends TransferObject implements Serializable {

    private String prodType = "";
    private String acHed = "";
    private String amount = "";
    private List list = new ArrayList();
    private String screenName = "";

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
    

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getAcHed() {
        return acHed;
    }

    public void setAcHed(String acHed) {
        this.acHed = acHed;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }

    public void setAuthorizeMap(HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }
    private String statusBy = "";
    private Date statusDt = null;
    private String status = "";
    private Date createdDt = null;
    private String currBranName = "";
    private String branCode = "";
    private String createdBy = "";
    private HashMap _authorizeMap;
    private Integer result;
    private String command = "";

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append(getTOStringKey(getKeyData()));
//        strB.append(getTOString("complaintid", complaintid));
//        strB.append(getTOString("dateofComplaint", dateofComplaint));
//        strB.append(getTOString("nameAddress", nameAddress));
//        strB.append(getTOString("employeeid",employeeid));
//        strB.append(getTOString("comments",comments));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("currBranName", currBranName));
        strB.append(getTOString("branCode", branCode));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        //strB.append(getTOXmlKey(getKeyData()));
//        strB.append(getTOXml("complaintid", complaintid));
//        strB.append(getTOXml("dateofComplaint", dateofComplaint));
//        strB.append(getTOXml("nameAddress", nameAddress));
//        strB.append(getTOXml("employeeid",employeeid));
//        strB.append(getTOXml("comments",comments));
//        strB.append(getTOXml("statusBy",statusBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("currBranName", currBranName));
        strB.append(getTOXml("branCode", branCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property nameAddress.
     *
     * @return Value of property nameAddress.
     */
    /**
     * Getter for property instNameAddress.
     *
     * @return Value of property instNameAddress.
     */
    /**
     * Getter for property statusBy.
     *
     * @return Value of property statusBy.
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
    }

    public java.lang.String getCommand() {
        return command;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     */
    public void setCommand(java.lang.String command) {
        this.command = command;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    /**
     * Getter for property currBranName.
     *
     * @return Value of property currBranName.
     */
    public java.lang.String getCurrBranName() {
        return currBranName;
    }

    /**
     * Setter for property currBranName.
     *
     * @param currBranName New value of property currBranName.
     */
    public void setCurrBranName(java.lang.String currBranName) {
        this.currBranName = currBranName;
    }

    /**
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public Integer getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(Integer result) {
        this.result = result;
    }

    /**
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public java.util.Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
    }

    /**
     * Getter for property cratedDt.
     *
     * @return Value of property cratedDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property cratedDt.
     *
     * @param cratedDt New value of property cratedDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
    }

    /**
     * Getter for property createdBy.
     *
     * @return Value of property createdBy.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for property createdBy.
     *
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Getter for property branCode.
     *
     * @return Value of property branCode.
     */
    public java.lang.String getBranCode() {
        return branCode;
    }

    /**
     * Setter for property branCode.
     *
     * @param branCode New value of property branCode.
     */
    public void setBranCode(java.lang.String branCode) {
        this.branCode = branCode;
    }
    /**
     * Getter for property complaintid.
     *
     * @return Value of property complaintid.
     */
}
