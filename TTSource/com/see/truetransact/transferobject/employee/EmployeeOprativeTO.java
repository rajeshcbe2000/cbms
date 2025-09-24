/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerPhoneTO.java
 *
 * Created on Wed Feb 16 09:38:12 IST 2005
 */
package com.see.truetransact.transferobject.employee;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUST_PHONE.
 */
public class EmployeeOprativeTO extends TransferObject implements Serializable {

    private String sysId = "";
    private String status = "";
    private String statusBy = "";
    private String cboOprativePordId = "";
    private String txtOPAcNo = "";
    private String cboOpACBranch = "";
    private String operativeId = "";
    private Date statusDt = null;

    /**
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(sysId);
        return sysId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));

        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("cboOprativePordId", cboOprativePordId));
        strB.append(getTOString("txtOPAcNo", txtOPAcNo));
        strB.append(getTOString("cboOpACBranch", cboOpACBranch));
        strB.append(getTOString("sysId", sysId));
        strB.append(getTOString("operativeId", operativeId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));

        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("cboOprativePordId", cboOprativePordId));
        strB.append(getTOXml("txtOPAcNo", txtOPAcNo));
        strB.append(getTOXml("cboOpACBranch", cboOpACBranch));
        strB.append(getTOXml("sysId", sysId));
        strB.append(getTOXml("operativeId", operativeId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property sysId.
     *
     * @return Value of property sysId.
     */
    public java.lang.String getSysId() {
        return sysId;
    }

    /**
     * Setter for property sysId.
     *
     * @param sysId New value of property sysId.
     */
    public void setSysId(java.lang.String sysId) {
        this.sysId = sysId;
    }

    /**
     * Getter for property cboOprativePordId.
     *
     * @return Value of property cboOprativePordId.
     */
    public java.lang.String getCboOprativePordId() {
        return cboOprativePordId;
    }

    /**
     * Setter for property cboOprativePordId.
     *
     * @param cboOprativePordId New value of property cboOprativePordId.
     */
    public void setCboOprativePordId(java.lang.String cboOprativePordId) {
        this.cboOprativePordId = cboOprativePordId;
    }

    /**
     * Getter for property txtOPAcNo.
     *
     * @return Value of property txtOPAcNo.
     */
    public java.lang.String getTxtOPAcNo() {
        return txtOPAcNo;
    }

    /**
     * Setter for property txtOPAcNo.
     *
     * @param txtOPAcNo New value of property txtOPAcNo.
     */
    public void setTxtOPAcNo(java.lang.String txtOPAcNo) {
        this.txtOPAcNo = txtOPAcNo;
    }

    /**
     * Getter for property cboOpACBranch.
     *
     * @return Value of property cboOpACBranch.
     */
    public java.lang.String getCboOpACBranch() {
        return cboOpACBranch;
    }

    /**
     * Setter for property cboOpACBranch.
     *
     * @param cboOpACBranch New value of property cboOpACBranch.
     */
    public void setCboOpACBranch(java.lang.String cboOpACBranch) {
        this.cboOpACBranch = cboOpACBranch;
    }

    /**
     * Getter for property operativeId.
     *
     * @return Value of property operativeId.
     */
    public java.lang.String getOperativeId() {
        return operativeId;
    }

    /**
     * Setter for property operativeId.
     *
     * @param operativeId New value of property operativeId.
     */
    public void setOperativeId(java.lang.String operativeId) {
        this.operativeId = operativeId;
    }
}