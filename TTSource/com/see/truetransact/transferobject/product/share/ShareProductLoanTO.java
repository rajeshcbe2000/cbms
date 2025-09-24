/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductLoanTO.java
 * 
 * Created on Tue Mar 15 14:08:12 IST 2005
 */
package com.see.truetransact.transferobject.product.share;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHARE_PROD_LOANS.
 */
public class ShareProductLoanTO extends TransferObject implements Serializable {

    private String shareType = "";
    private String loanType = "";
    private Double maxLoanLimit = null;
    private Double shareHoldingMultiples = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private Double maxLoanAmt = null;
//	private Double surityLimit = null;

    /**
     * Setter/Getter for SHARE_TYPE - table Field
     */
    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getShareType() {
        return shareType;
    }

    /**
     * Setter/Getter for LOAN_TYPE - table Field
     */
    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanType() {
        return loanType;
    }

    /**
     * Setter/Getter for MAX_LOAN_LIMIT - table Field
     */
    public void setMaxLoanLimit(Double maxLoanLimit) {
        this.maxLoanLimit = maxLoanLimit;
    }

    public Double getMaxLoanLimit() {
        return maxLoanLimit;
    }

    /**
     * Setter/Getter for SHARE_HOLDING_MULTIPLES - table Field
     */
    public void setShareHoldingMultiples(Double shareHoldingMultiples) {
        this.shareHoldingMultiples = shareHoldingMultiples;
    }

    public Double getShareHoldingMultiples() {
        return shareHoldingMultiples;
    }

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

//	/** Setter/Getter for SURITY_LIMIT - table Field*/
//	public void setSurityLimit (Double surityLimit) {
//		this.surityLimit = surityLimit;
//	}
//	public Double getSurityLimit () {
//		return surityLimit;
//	}
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
        strB.append(getTOString("shareType", shareType));
        strB.append(getTOString("loanType", loanType));
        strB.append(getTOString("maxLoanLimit", maxLoanLimit));
        strB.append(getTOString("shareHoldingMultiples", shareHoldingMultiples));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("maxLoanAmt", maxLoanAmt));
//		strB.append(getTOString("surityLimit", surityLimit));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("shareType", shareType));
        strB.append(getTOXml("loanType", loanType));
        strB.append(getTOXml("maxLoanLimit", maxLoanLimit));
        strB.append(getTOXml("shareHoldingMultiples", shareHoldingMultiples));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("maxLoanAmt", maxLoanAmt));
//		strB.append(getTOXml("surityLimit", surityLimit));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property maxLoanAmt.
     *
     * @return Value of property maxLoanAmt.
     */
    public java.lang.Double getMaxLoanAmt() {
        return maxLoanAmt;
    }

    /**
     * Setter for property maxLoanAmt.
     *
     * @param maxLoanAmt New value of property maxLoanAmt.
     */
    public void setMaxLoanAmt(java.lang.Double maxLoanAmt) {
        this.maxLoanAmt = maxLoanAmt;
    }
}