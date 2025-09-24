/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 *  CustomerTO.java
 *
 * Created on Wed Jul 27 16:13:11 GMT+05:30 2005
 */
package com.see.truetransact.transferobject.employee;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUSTOMER.
 */
public class EmployeeMasterLanguageTO extends TransferObject implements Serializable {

    private String txtEmpId = "";
    private String cboLanguageType = "";
    private String rdoWrite = "";
    private String rdoRead = "";
    private String rdoSpeak = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;

    public String getKeyData() {
        setKeyColumns(txtEmpId);
        return txtEmpId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));

        strB.append(getTOString("txtEmpId", txtEmpId));
        strB.append(getTOString("cboLanguageType", cboLanguageType));
        strB.append(getTOString("rdoWrite", rdoWrite));
        strB.append(getTOString("rdoRead", rdoRead));
        strB.append(getTOString("rdoSpeak", rdoSpeak));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("txtEmpId", txtEmpId));
        strB.append(getTOXml("cboLanguageType", cboLanguageType));
        strB.append(getTOXml("rdoWrite", rdoWrite));
        strB.append(getTOXml("rdoRead", rdoRead));
        strB.append(getTOXml("rdoSpeak", rdoSpeak));;
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property txtEmpId.
     *
     * @return Value of property txtEmpId.
     */
    public java.lang.String getTxtEmpId() {
        return txtEmpId;
    }

    /**
     * Setter for property txtEmpId.
     *
     * @param txtEmpId New value of property txtEmpId.
     */
    public void setTxtEmpId(java.lang.String txtEmpId) {
        this.txtEmpId = txtEmpId;
    }

    /**
     * Getter for property cboLanguageType.
     *
     * @return Value of property cboLanguageType.
     */
    public java.lang.String getCboLanguageType() {
        return cboLanguageType;
    }

    /**
     * Setter for property cboLanguageType.
     *
     * @param cboLanguageType New value of property cboLanguageType.
     */
    public void setCboLanguageType(java.lang.String cboLanguageType) {
        this.cboLanguageType = cboLanguageType;
    }

    /**
     * Getter for property rdoWrite.
     *
     * @return Value of property rdoWrite.
     */
    public java.lang.String getRdoWrite() {
        return rdoWrite;
    }

    /**
     * Setter for property rdoWrite.
     *
     * @param rdoWrite New value of property rdoWrite.
     */
    public void setRdoWrite(java.lang.String rdoWrite) {
        this.rdoWrite = rdoWrite;
    }

    /**
     * Getter for property rdoRead.
     *
     * @return Value of property rdoRead.
     */
    public java.lang.String getRdoRead() {
        return rdoRead;
    }

    /**
     * Setter for property rdoRead.
     *
     * @param rdoRead New value of property rdoRead.
     */
    public void setRdoRead(java.lang.String rdoRead) {
        this.rdoRead = rdoRead;
    }

    /**
     * Getter for property rdoSpeak.
     *
     * @return Value of property rdoSpeak.
     */
    public java.lang.String getRdoSpeak() {
        return rdoSpeak;
    }

    /**
     * Setter for property rdoSpeak.
     *
     * @param rdoSpeak New value of property rdoSpeak.
     */
    public void setRdoSpeak(java.lang.String rdoSpeak) {
        this.rdoSpeak = rdoSpeak;
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
}