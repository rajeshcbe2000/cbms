/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTransferTO.java
 * 
 * Created on Wed Jun 28  2010 swaroop
 */
package com.see.truetransact.transferobject.directoryboardsetting;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import com.see.truetransact.clientutil.ComboBoxModel;

/**
 * Table name for this TO is EMP_TRANSFER.
 */
public class DirectoryBoardTO extends TransferObject implements Serializable {

    private String id = "";
    private String memno = "";
    private String desig = "";
    private Integer priority = new Integer(0);
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizedStatus = null;
    private String directorBoardNo = "";
    private String BranCode = " ";
    private Date selDate = null;
    private String accHead = " ";
    private String prodType = "";
    private String prodId = "";
    private String actNo = "";

    public String getAccHead() {
        return accHead;
    }

    public void setAccHead(String accHead) {
        this.accHead = accHead;
    }

    public Date getSelDate() {
        return selDate;
    }

    public void setSelDate(Date selDate) {
        this.selDate = selDate;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    /*
     * public String getKeyData() { setKeyColumns("empTransferID"); return
     * empTransferID; }
     *
     * /** toString method which returns this TO as a String.
     */
    /*
     * public String toString() { StringBuffer strB = new
     * StringBuffer(getTOStringStart(this.getClass().getName())); strB.append
     * (getTOStringKey(getKeyData())); strB.append(getTOString("empTransferID",
     * empTransferID)); strB.append(getTOString("empID", empID));
     * strB.append(getTOString("currBran", currBran));
     * strB.append(getTOString("transferBran", transferBran));
     * strB.append(getTOString("lastWorkingDay", lastWorkingDay));
     * strB.append(getTOString("doj", doj));
     * strB.append(getTOString("roleInCurrBran", roleInCurrBran));
     * strB.append(getTOString("roleInTransferBran", roleInTransferBran));
     * strB.append(getTOString("applType", applType));
     * strB.append(getTOString("branCode", branCode));
     * strB.append(getTOString("status", status));
     * strB.append(getTOString("statusDt", statusDt));
     * strB.append(getTOString("statusBy", statusBy));
     * strB.append(getTOString("createdDt", createdDt));
     * strB.append(getTOString("createdBy", createdBy));
     * strB.append(getTOString("authorizeBy", authorizeBy));
     * strB.append(getTOString("authorizeDt", authorizeDt));
     * strB.append(getTOString("authorizedStatus", authorizedStatus));
     * strB.append(getTOString("empName", empName));
     * strB.append(getTOString("currBranName", currBranName));
     * strB.append(getTOStringEnd()); return strB.toString(); }
     *
     * /** toXML method which returns this TO as a XML output.
     */
    /*
     * public String toXML() { StringBuffer strB = new
     * StringBuffer(getTOXmlStart(this.getClass().getName())); strB.append
     * (getTOXmlKey(getKeyData())); strB.append(getTOXml("empTransferID",
     * empTransferID)); strB.append(getTOXml("empID", empID));
     * strB.append(getTOXml("currBran", currBran));
     * strB.append(getTOXml("transferBran", transferBran));
     * strB.append(getTOXml("lastWorkingDay", lastWorkingDay));
     * strB.append(getTOXml("doj", doj)); strB.append(getTOXml("roleInCurrBran",
     * roleInCurrBran)); strB.append(getTOXml("roleInTransferBran",
     * roleInTransferBran)); strB.append(getTOXml("applType", applType));
     * strB.append(getTOXml("branCode", branCode));
     * strB.append(getTOXml("status", status)); strB.append(getTOXml("statusDt",
     * statusDt)); strB.append(getTOXml("statusBy", statusBy));
     * strB.append(getTOXml("createdDt", createdDt));
     * strB.append(getTOXml("createdBy", createdBy));
     * strB.append(getTOXml("authorizeBy", authorizeBy));
     * strB.append(getTOXml("authorizeDt", authorizeDt));
     * strB.append(getTOXml("authorizedStatus", authorizedStatus));
     * strB.append(getTOXml("empName", empName));
     * strB.append(getTOXml("currBranName", currBranName));
     * strB.append(getTOXmlEnd()); return strB.toString(); }
     *
     */
    
    
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

    /**
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property createdDt.
     *
     * @param createdDt New value of property createdDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
    }

    /**
     * Getter for property createdBy.
     *
     * @return Value of property createdBy.
     */
    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for property createdBy.
     *
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property authorizeDt.
     *
     * @return Value of property authorizeDt.
     */
    public java.util.Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter for property authorizeDt.
     *
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.util.Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    /**
     * Getter for property authorizedStatus.
     *
     * @return Value of property authorizedStatus.
     */
    public java.lang.String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter for property authorizedStatus.
     *
     * @param authorizedStatus New value of property authorizedStatus.
     */
    public void setAuthorizedStatus(java.lang.String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    /**
     * Getter for property memno.
     *
     * @return Value of property memno.
     */
    public java.lang.String getMemno() {
        return memno;
    }

    /**
     * Setter for property memno.
     *
     * @param memno New value of property memno.
     */
    public void setMemno(java.lang.String memno) {
        this.memno = memno;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * Getter for property id.
     *
     * @return Value of property id.
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Setter for property id.
     *
     * @param id New value of property id.
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /**
     * Getter for property BranCode.
     *
     * @return Value of property BranCode.
     */
    public String getBranCode() {
        return BranCode;
    }

    /**
     * Setter for property BranCode.
     *
     * @param BranCode New value of property BranCode.
     */
    public void setBranCode(String BranCode) {
        this.BranCode = BranCode;
    }

    /**
     * Getter for property desig.
     *
     * @return Value of property desig.
     */
    public java.lang.String getDesig() {
        return desig;
    }

    /**
     * Setter for property desig.
     *
     * @param desig New value of property desig.
     */
    public void setDesig(java.lang.String desig) {
        this.desig = desig;
    }

    public String getActNo() {
        return actNo;
    }

    public void setActNo(String actNo) {
        this.actNo = actNo;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }    
    
}