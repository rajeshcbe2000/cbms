/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanSecurityTO.java
 *
 * Created on Wed Mar 16 16:19:00 IST 2005
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_SECURITY_DETAILS.
 */
public class TermLoanLosTypeTO extends TransferObject implements Serializable {

    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String borrowNo = "";
    private String branchCode = "";
    private String losInstitution = "";
    private String losName = "";
    private Double losAmount;
    private String losSecurityNo = "";
    private String losSecurityType = "";
    private String losRemarks = "";
    private Double losMaturityValue;
    private Date losIssueDt;
    private Date losMatDt;

    /**
     * Setter/Getter for ACCT_NUM - table Field
     */
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
     * Setter/Getter for BORROW_NO - table Field
     */
    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getBorrowNo() {
        return borrowNo;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("acctNum" + KEY_VAL_SEPARATOR + "slno");
        return KEY_VAL_SEPARATOR;
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
        strB.append(getTOString("borrowNo", borrowNo));


        strB.append(getTOString("branchCode", branchCode));

        strB.append(getTOString("losInstitution", losInstitution));
        strB.append(getTOString("losName", losName));
        strB.append(getTOString("losAmount", losAmount));
        strB.append(getTOString("losSecurityNo", losSecurityNo));
        strB.append(getTOString("losSecurityType", losSecurityType));
        strB.append(getTOString("losRemarks", losRemarks));
        strB.append(getTOString("losMaturityValue", losMaturityValue));
        strB.append(getTOString("losIssueDt", losIssueDt));
        strB.append(getTOString("losMatDt", losMatDt));
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
        strB.append(getTOXml("borrowNo", borrowNo));



        strB.append(getTOXml("branchCode", branchCode));

        strB.append(getTOXml("losInstitution", losInstitution));
        strB.append(getTOXml("losName", losName));
        strB.append(getTOXml("losAmount", losAmount));
        strB.append(getTOXml("losSecurityNo", losSecurityNo));
        strB.append(getTOXml("losSecurityType", losSecurityType));
        strB.append(getTOXml("losRemarks", losRemarks));
        strB.append(getTOXml("losMaturityValue", losMaturityValue));
        strB.append(getTOXml("losIssueDt", losIssueDt));
        strB.append(getTOXml("losMatDt", losMatDt));

        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property txtJewelleryDetails.
     *
     * @return Value of property txtJewelleryDetails.
     */
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
     * Getter for property losInstitution.
     *
     * @return Value of property losInstitution.
     */
    public java.lang.String getLosInstitution() {
        return losInstitution;
    }

    /**
     * Setter for property losInstitution.
     *
     * @param losInstitution New value of property losInstitution.
     */
    public void setLosInstitution(java.lang.String losInstitution) {
        this.losInstitution = losInstitution;
    }

    /**
     * Getter for property losName.
     *
     * @return Value of property losName.
     */
    public java.lang.String getLosName() {
        return losName;
    }

    /**
     * Setter for property losName.
     *
     * @param losName New value of property losName.
     */
    public void setLosName(java.lang.String losName) {
        this.losName = losName;
    }

    /**
     * Getter for property losAmount.
     *
     * @return Value of property losAmount.
     */
    public java.lang.Double getLosAmount() {
        return losAmount;
    }

    /**
     * Setter for property losAmount.
     *
     * @param losAmount New value of property losAmount.
     */
    public void setLosAmount(java.lang.Double losAmount) {
        this.losAmount = losAmount;
    }

    /**
     * Getter for property losSecurityNo.
     *
     * @return Value of property losSecurityNo.
     */
    public java.lang.String getLosSecurityNo() {
        return losSecurityNo;
    }

    /**
     * Setter for property losSecurityNo.
     *
     * @param losSecurityNo New value of property losSecurityNo.
     */
    public void setLosSecurityNo(java.lang.String losSecurityNo) {
        this.losSecurityNo = losSecurityNo;
    }

    /**
     * Getter for property losSecurityType.
     *
     * @return Value of property losSecurityType.
     */
    public java.lang.String getLosSecurityType() {
        return losSecurityType;
    }

    /**
     * Setter for property losSecurityType.
     *
     * @param losSecurityType New value of property losSecurityType.
     */
    public void setLosSecurityType(java.lang.String losSecurityType) {
        this.losSecurityType = losSecurityType;
    }

    /**
     * Getter for property losRemarks.
     *
     * @return Value of property losRemarks.
     */
    public java.lang.String getLosRemarks() {
        return losRemarks;
    }

    /**
     * Setter for property losRemarks.
     *
     * @param losRemarks New value of property losRemarks.
     */
    public void setLosRemarks(java.lang.String losRemarks) {
        this.losRemarks = losRemarks;
    }

    /**
     * Getter for property losMaturityValue.
     *
     * @return Value of property losMaturityValue.
     */
    public java.lang.Double getLosMaturityValue() {
        return losMaturityValue;
    }

    /**
     * Setter for property losMaturityValue.
     *
     * @param losMaturityValue New value of property losMaturityValue.
     */
    public void setLosMaturityValue(java.lang.Double losMaturityValue) {
        this.losMaturityValue = losMaturityValue;
    }

    /**
     * Getter for property losIssueDt.
     *
     * @return Value of property losIssueDt.
     */
    public java.util.Date getLosIssueDt() {
        return losIssueDt;
    }

    /**
     * Setter for property losIssueDt.
     *
     * @param losIssueDt New value of property losIssueDt.
     */
    public void setLosIssueDt(java.util.Date losIssueDt) {
        this.losIssueDt = losIssueDt;
    }

    /**
     * Getter for property losMatDt.
     *
     * @return Value of property losMatDt.
     */
    public java.util.Date getLosMatDt() {
        return losMatDt;
    }

    /**
     * Setter for property losMatDt.
     *
     * @param losMatDt New value of property losMatDt.
     */
    public void setLosMatDt(java.util.Date losMatDt) {
        this.losMatDt = losMatDt;
    }
}