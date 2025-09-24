/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryStructureTO.java
 * 
 * Created on Wed Jun 09 18:50:50 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.common;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SalaryStructure.
 */
public class HRAllowanceTO extends TransferObject implements Serializable {

    private String hRAslNo = "";
    private String hRAgrade = "";
    private String hRACityType = "";
    private Date hRAfromDate = null;
    private Date hRAtoDate = null;
    private String hRAstartingScaleAmt = "";
    private String hRAincrementAmt = "";
    private String hRAPayable = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDate = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDate = null;
    private String branchCode = "";
    private Double tempSlNo = null;
    private Date lastEffectiveDt = null;

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
//    public String getKeyData() {
//        setKeyColumns("lienNo");
//        return slNo;
//    }
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("hRAslNo", hRAslNo));
        strB.append(getTOString("hRAgrade", hRAgrade));
        strB.append(getTOString("hRAfromDate", hRAfromDate));
        strB.append(getTOString("hRAtoDate", hRAtoDate));
        strB.append(getTOString("hRAstartingScaleAmt", hRAstartingScaleAmt));
        strB.append(getTOString("hRAincrementAmt", hRAincrementAmt));
        strB.append(getTOString("hRAPayable", hRAPayable));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("tempSlNo", tempSlNo));
        strB.append(getTOString("lastEffectiveDt", lastEffectiveDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("HRAslNo", hRAslNo));
        strB.append(getTOXml("HRAgrade", hRAgrade));
        strB.append(getTOXml("HRAfromDate", hRAfromDate));
        strB.append(getTOXml("HRAtoDate", hRAtoDate));
        strB.append(getTOXml("HRAstartingScaleAmt", hRAstartingScaleAmt));
        strB.append(getTOXml("HRAincrementAmt", hRAincrementAmt));
        strB.append(getTOXml("HRAPayable", hRAPayable));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("tempSlNo", tempSlNo));
        strB.append(getTOXml("lastEffectiveDt", lastEffectiveDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property hRAslNo.
     *
     * @return Value of property hRAslNo.
     */
    public java.lang.String getHRAslNo() {
        return hRAslNo;
    }

    /**
     * Setter for property hRAslNo.
     *
     * @param hRAslNo New value of property hRAslNo.
     */
    public void setHRAslNo(java.lang.String hRAslNo) {
        this.hRAslNo = hRAslNo;
    }

    /**
     * Getter for property hRAgrade.
     *
     * @return Value of property hRAgrade.
     */
    public java.lang.String getHRAgrade() {
        return hRAgrade;
    }

    /**
     * Setter for property hRAgrade.
     *
     * @param hRAgrade New value of property hRAgrade.
     */
    public void setHRAgrade(java.lang.String hRAgrade) {
        this.hRAgrade = hRAgrade;
    }

    /**
     * Getter for property hRACityType.
     *
     * @return Value of property hRACityType.
     */
    public java.lang.String getHRACityType() {
        return hRACityType;
    }

    /**
     * Setter for property hRACityType.
     *
     * @param hRACityType New value of property hRACityType.
     */
    public void setHRACityType(java.lang.String hRACityType) {
        this.hRACityType = hRACityType;
    }

    /**
     * Getter for property hRAfromDate.
     *
     * @return Value of property hRAfromDate.
     */
    public java.util.Date getHRAfromDate() {
        return hRAfromDate;
    }

    /**
     * Setter for property hRAfromDate.
     *
     * @param hRAfromDate New value of property hRAfromDate.
     */
    public void setHRAfromDate(java.util.Date hRAfromDate) {
        this.hRAfromDate = hRAfromDate;
    }

    /**
     * Getter for property hRAtoDate.
     *
     * @return Value of property hRAtoDate.
     */
    public java.util.Date getHRAtoDate() {
        return hRAtoDate;
    }

    /**
     * Setter for property hRAtoDate.
     *
     * @param hRAtoDate New value of property hRAtoDate.
     */
    public void setHRAtoDate(java.util.Date hRAtoDate) {
        this.hRAtoDate = hRAtoDate;
    }

    /**
     * Getter for property hRAstartingScaleAmt.
     *
     * @return Value of property hRAstartingScaleAmt.
     */
    public java.lang.String getHRAstartingScaleAmt() {
        return hRAstartingScaleAmt;
    }

    /**
     * Setter for property hRAstartingScaleAmt.
     *
     * @param hRAstartingScaleAmt New value of property hRAstartingScaleAmt.
     */
    public void setHRAstartingScaleAmt(java.lang.String hRAstartingScaleAmt) {
        this.hRAstartingScaleAmt = hRAstartingScaleAmt;
    }

    /**
     * Getter for property hRAincrementAmt.
     *
     * @return Value of property hRAincrementAmt.
     */
    public java.lang.String getHRAincrementAmt() {
        return hRAincrementAmt;
    }

    /**
     * Setter for property hRAincrementAmt.
     *
     * @param hRAincrementAmt New value of property hRAincrementAmt.
     */
    public void setHRAincrementAmt(java.lang.String hRAincrementAmt) {
        this.hRAincrementAmt = hRAincrementAmt;
    }

    /**
     * Getter for property hRAPayable.
     *
     * @return Value of property hRAPayable.
     */
    public java.lang.String getHRAPayable() {
        return hRAPayable;
    }

    /**
     * Setter for property hRAPayable.
     *
     * @param hRAPayable New value of property hRAPayable.
     */
    public void setHRAPayable(java.lang.String hRAPayable) {
        this.hRAPayable = hRAPayable;
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
     * Getter for property statusDate.
     *
     * @return Value of property statusDate.
     */
    public java.util.Date getStatusDate() {
        return statusDate;
    }

    /**
     * Setter for property statusDate.
     *
     * @param statusDate New value of property statusDate.
     */
    public void setStatusDate(java.util.Date statusDate) {
        this.statusDate = statusDate;
    }

    /**
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
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
     * Getter for property authorizeDate.
     *
     * @return Value of property authorizeDate.
     */
    public java.util.Date getAuthorizeDate() {
        return authorizeDate;
    }

    /**
     * Setter for property authorizeDate.
     *
     * @param authorizeDate New value of property authorizeDate.
     */
    public void setAuthorizeDate(java.util.Date authorizeDate) {
        this.authorizeDate = authorizeDate;
    }

    /**
     * Getter for property branchCode.
     *
     * @return Value of property branchCode.
     */
    public java.lang.String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter for property branchCode.
     *
     * @param branchCode New value of property branchCode.
     */
    public void setBranchCode(java.lang.String branchCode) {
        this.branchCode = branchCode;
    }

    /**
     * Getter for property tempSlNo.
     *
     * @return Value of property tempSlNo.
     */
    public java.lang.Double getTempSlNo() {
        return tempSlNo;
    }

    /**
     * Setter for property tempSlNo.
     *
     * @param tempSlNo New value of property tempSlNo.
     */
    public void setTempSlNo(java.lang.Double tempSlNo) {
        this.tempSlNo = tempSlNo;
    }

    /**
     * Getter for property lastEffectiveDt.
     *
     * @return Value of property lastEffectiveDt.
     */
    public java.util.Date getLastEffectiveDt() {
        return lastEffectiveDt;
    }

    /**
     * Setter for property lastEffectiveDt.
     *
     * @param lastEffectiveDt New value of property lastEffectiveDt.
     */
    public void setLastEffectiveDt(java.util.Date lastEffectiveDt) {
        this.lastEffectiveDt = lastEffectiveDt;
    }
}