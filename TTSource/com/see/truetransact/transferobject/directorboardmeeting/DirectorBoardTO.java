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
package com.see.truetransact.transferobject.directorboardmeeting;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import com.see.truetransact.clientutil.ComboBoxModel;

/**
 * Table name for this TO is EMP_TRANSFER.
 */
public class DirectorBoardTO extends TransferObject implements Serializable {

    private String cboBoMember = "";
    private Double sittingFeeAmount = null;
    private Date meetngDate = null;
    private Date paidDate = null;
    private boolean rdoYes1 = false;
    private boolean rdoNo1 = false;
    private boolean rdoYes2 = false;
    private boolean rdoNo2 = false;
    private String applType = "";
    private String applType1 = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizedStatus = null;
    private String directorBoardNo = "";

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
    public boolean isRdoNo2() {
        return rdoNo2;
    }

    public void setRdoNo2(boolean rdoNo2) {
        this.rdoNo2 = rdoNo2;
    }

    public boolean isRdoYes2() {
        return rdoYes2;
    }

    public void setRdoYes2(boolean rdoYes2) {
        this.rdoYes2 = rdoYes2;
    }

    public boolean isRdoNo1() {
        return rdoNo1;
    }

    public void setRdoNo1(boolean rdoNo1) {
        this.rdoNo1 = rdoNo1;
    }

    public boolean isRdoYes1() {
        return rdoYes1;
    }

    public void setRdoYes1(boolean rdoYes1) {
        this.rdoYes1 = rdoYes1;
    }

    /**
     * Getter for property applType.
     *
     * @return Value of property applType.
     */
    public java.lang.String getApplType() {
        return applType;
    }

    /**
     * Setter for property applType.
     *
     * @param applType New value of property applType.
     */
    public void setApplType(java.lang.String applType) {
        this.applType = applType;
    }

    public java.lang.String getApplType1() {
        return applType1;
    }

    /**
     * Setter for property applType1.
     *
     * @param applType1 New value of property applType1.
     */
    public void setApplType1(java.lang.String applType1) {
        this.applType1 = applType1;
    }

    /**
     * Getter for property branCode.
     *
     * @return Value of property branCode.
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
     * Getter for property empName.
     *
     * @return Value of property empName.
     */
    /**
     * Getter for property cboBoMember.
     *
     * @return Value of property cboBoMember.
     */
    public java.lang.String getCboBoMember() {
        return cboBoMember;
    }

    /**
     * Setter for property cboBoMember.
     *
     * @param cboBoMember New value of property cboBoMember.
     */
    public void setCboBoMember(java.lang.String cboBoMember) {
        this.cboBoMember = cboBoMember;
    }

    /**
     * Getter for property SittingFeeAmount.
     *
     * @return Value of property SittingFeeAmount.
     */
    /**
     * Getter for property meetngDate.
     *
     * @return Value of property meetngDate.
     */
    public java.util.Date getMeetngDate() {
        return meetngDate;
    }

    /**
     * Setter for property meetngDate.
     *
     * @param meetngDate New value of property meetngDate.
     */
    public void setMeetngDate(java.util.Date meetngDate) {
        this.meetngDate = meetngDate;
    }

    /**
     * Getter for property paidDate.
     *
     * @return Value of property paidDate.
     */
    public java.util.Date getPaidDate() {
        return paidDate;
    }

    /**
     * Setter for property paidDate.
     *
     * @param paidDate New value of property paidDate.
     */
    public void setPaidDate(java.util.Date paidDate) {
        this.paidDate = paidDate;
    }

    /**
     * Getter for property sittingFeeAmount.
     *
     * @return Value of property sittingFeeAmount.
     */
    public Double getSittingFeeAmount() {
        return sittingFeeAmount;
    }

    /**
     * Setter for property sittingFeeAmount.
     *
     * @param sittingFeeAmount New value of property sittingFeeAmount.
     */
    public void setSittingFeeAmount(Double sittingFeeAmount) {
        this.sittingFeeAmount = sittingFeeAmount;
    }

    /**
     * Getter for property directorBoardNo.
     *
     * @return Value of property directorBoardNo.
     */
    public java.lang.String getDirectorBoardNo() {
        return directorBoardNo;
    }

    /**
     * Setter for property directorBoardNo.
     *
     * @param directorBoardNo New value of property directorBoardNo.
     */
    public void setDirectorBoardNo(java.lang.String directorBoardNo) {
        this.directorBoardNo = directorBoardNo;
    }
}