/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanCaseDetailTO.java
 * 
 * Created on Tue Aug 02 10:05:04 IST 2011
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TERM_LOAN_CASE_DETAILS.
 */
public class TermLoanCaseDetailTO extends TransferObject implements Serializable {

    private String actNum = "";
    private String caseStatus = "";
    private String caseNo = "";
    private Date fillingDt = null;
    private String fillingFees = null;
    private String miscCharges = null;
    private String status = null;
    private String authStatus = "";
    private Date authDate = null;
    private String authBy = "";

    /**
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    /**
     * Setter/Getter for CASE_STATUS - table Field
     */
    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    /**
     * Setter/Getter for CASE_NO - table Field
     */
    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getCaseNo() {
        return caseNo;
    }

    /**
     * Setter/Getter for FILLING_DT - table Field
     */
    public void setFillingDt(Date fillingDt) {
        this.fillingDt = fillingDt;
    }

    public Date getFillingDt() {
        return fillingDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("caseStatus", caseStatus));
        strB.append(getTOString("caseNo", caseNo));
        strB.append(getTOString("fillingDt", fillingDt));
        strB.append(getTOString("fillingFees", fillingFees));
        strB.append(getTOString("miscCharges", miscCharges));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authStatus", authStatus));
        strB.append(getTOString("authDate", authDate));
        strB.append(getTOString("authBy", authBy));

        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("caseStatus", caseStatus));
        strB.append(getTOXml("caseNo", caseNo));
        strB.append(getTOXml("fillingDt", fillingDt));
        strB.append(getTOXml("fillingFees", fillingFees));
        strB.append(getTOXml("miscCharges", miscCharges));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authStatus", authStatus));
        strB.append(getTOXml("authDate", authDate));
        strB.append(getTOXml("authBy", authBy));

        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property fillingFees.
     *
     * @return Value of property fillingFees.
     */
    public java.lang.String getFillingFees() {
        return fillingFees;
    }

    /**
     * Setter for property fillingFees.
     *
     * @param fillingFees New value of property fillingFees.
     */
    public void setFillingFees(java.lang.String fillingFees) {
        this.fillingFees = fillingFees;
    }

    /**
     * Getter for property miscCharges.
     *
     * @return Value of property miscCharges.
     */
    public java.lang.String getMiscCharges() {
        return miscCharges;
    }

    /**
     * Setter for property miscCharges.
     *
     * @param miscCharges New value of property miscCharges.
     */
    public void setMiscCharges(java.lang.String miscCharges) {
        this.miscCharges = miscCharges;
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
     * Getter for property authStatus.
     *
     * @return Value of property authStatus.
     */
    public java.lang.String getAuthStatus() {
        return authStatus;
    }

    /**
     * Setter for property authStatus.
     *
     * @param authStatus New value of property authStatus.
     */
    public void setAuthStatus(java.lang.String authStatus) {
        this.authStatus = authStatus;
    }

    /**
     * Getter for property authDate.
     *
     * @return Value of property authDate.
     */
    public java.util.Date getAuthDate() {
        return authDate;
    }

    /**
     * Setter for property authDate.
     *
     * @param authDate New value of property authDate.
     */
    public void setAuthDate(java.util.Date authDate) {
        this.authDate = authDate;
    }

    /**
     * Getter for property authBy.
     *
     * @return Value of property authBy.
     */
    public java.lang.String getAuthBy() {
        return authBy;
    }

    /**
     * Setter for property authBy.
     *
     * @param authBy New value of property authBy.
     */
    public void setAuthBy(java.lang.String authBy) {
        this.authBy = authBy;
    }
}
