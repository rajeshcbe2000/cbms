/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MAllowanceTO.java
 * 
 * Created on Wed Jun 09 18:50:50 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.common;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is MAllowanceTO.
 */
public class MAllowanceTO extends TransferObject implements Serializable {

    private String mAslNo = "";
    private String mAgrade = "";
    private Date mAfromDate = null;
    private Date mAtoDate = null;
    private String mAAmount = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDate = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDate = null;
    private String branchCode = "";
    private Double tempSlNo = null;

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
        strB.append(getTOString("mAslNo", mAslNo));
        strB.append(getTOString("mAgrade", mAgrade));
        strB.append(getTOString("mAfromDate", mAfromDate));
        strB.append(getTOString("mAtoDate", mAtoDate));
        strB.append(getTOString("mAAmount", mAAmount));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("tempSlNo", tempSlNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("mAslNo", mAslNo));
        strB.append(getTOXml("mAgrade", mAgrade));
        strB.append(getTOXml("mAfromDate", mAfromDate));
        strB.append(getTOXml("mAtoDate", mAtoDate));
        strB.append(getTOXml("mAAmount", mAAmount));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("tempSlNo", tempSlNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property mAslNo.
     *
     * @return Value of property mAslNo.
     */
    public java.lang.String getMAslNo() {
        return mAslNo;
    }

    /**
     * Setter for property mAslNo.
     *
     * @param mAslNo New value of property mAslNo.
     */
    public void setMAslNo(java.lang.String mAslNo) {
        this.mAslNo = mAslNo;
    }

    /**
     * Getter for property mAgrade.
     *
     * @return Value of property mAgrade.
     */
    public java.lang.String getMAgrade() {
        return mAgrade;
    }

    /**
     * Setter for property mAgrade.
     *
     * @param mAgrade New value of property mAgrade.
     */
    public void setMAgrade(java.lang.String mAgrade) {
        this.mAgrade = mAgrade;
    }

    /**
     * Getter for property mAfromDate.
     *
     * @return Value of property mAfromDate.
     */
    public java.util.Date getMAfromDate() {
        return mAfromDate;
    }

    /**
     * Setter for property mAfromDate.
     *
     * @param mAfromDate New value of property mAfromDate.
     */
    public void setMAfromDate(java.util.Date mAfromDate) {
        this.mAfromDate = mAfromDate;
    }

    /**
     * Getter for property mAtoDate.
     *
     * @return Value of property mAtoDate.
     */
    public java.util.Date getMAtoDate() {
        return mAtoDate;
    }

    /**
     * Setter for property mAtoDate.
     *
     * @param mAtoDate New value of property mAtoDate.
     */
    public void setMAtoDate(java.util.Date mAtoDate) {
        this.mAtoDate = mAtoDate;
    }

    /**
     * Getter for property mAAmount.
     *
     * @return Value of property mAAmount.
     */
    public java.lang.String getMAAmount() {
        return mAAmount;
    }

    /**
     * Setter for property mAAmount.
     *
     * @param mAAmount New value of property mAAmount.
     */
    public void setMAAmount(java.lang.String mAAmount) {
        this.mAAmount = mAAmount;
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
}