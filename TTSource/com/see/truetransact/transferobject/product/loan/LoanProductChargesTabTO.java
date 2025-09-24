/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductChargesTabTO.java
 * 
 * Created on Fri May 14 16:03:15 IST 2004
 */
package com.see.truetransact.transferobject.product.loan;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_CHQCHRG. AND NOTICE_TYPE CHARGES
 */
public class LoanProductChargesTabTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String issueAfter = "";
    private String noticeType = "";
    private Double noticeChargeAmt = null;
    private Double postageAmt = null;
    private Double chqReturnChrgRate = null;
    private Double chqReturnChrgFrom = null;
    private Double chqReturnChrgTo = null;

    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for CHQ_RETURN_CHRG_RATE - table Field
     */
    public void setChqReturnChrgRate(Double chqReturnChrgRate) {
        this.chqReturnChrgRate = chqReturnChrgRate;
    }

    public Double getChqReturnChrgRate() {
        return chqReturnChrgRate;
    }

    /**
     * Setter/Getter for CHQ_RETURN_CHRG_FROM - table Field
     */
    public void setChqReturnChrgFrom(Double chqReturnChrgFrom) {
        this.chqReturnChrgFrom = chqReturnChrgFrom;
    }

    public Double getChqReturnChrgFrom() {
        return chqReturnChrgFrom;
    }

    /**
     * Setter/Getter for CHQ_RETURN_CHRG_TO - table Field
     */
    public void setChqReturnChrgTo(Double chqReturnChrgTo) {
        this.chqReturnChrgTo = chqReturnChrgTo;
    }

    public Double getChqReturnChrgTo() {
        return chqReturnChrgTo;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("prodId");
        return prodId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("chqReturnChrgRate", chqReturnChrgRate));
        strB.append(getTOString("chqReturnChrgFrom", chqReturnChrgFrom));
        strB.append(getTOString("chqReturnChrgTo", chqReturnChrgTo));
        strB.append(getTOString("noticeType", noticeType));
        strB.append(getTOString("noticeChargeAmt", noticeChargeAmt));
        strB.append(getTOString("postageAmt", postageAmt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("chqReturnChrgRate", chqReturnChrgRate));
        strB.append(getTOXml("chqReturnChrgFrom", chqReturnChrgFrom));
        strB.append(getTOXml("chqReturnChrgTo", chqReturnChrgTo));
        strB.append(getTOXml("noticeType", noticeType));
        strB.append(getTOXml("noticeChargeAmt", noticeChargeAmt));
        strB.append(getTOXml("postageAmt", postageAmt));

        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property issueAfter.
     *
     * @return Value of property issueAfter.
     */
    public String getIssueAfter() {
        return issueAfter;
    }

    /**
     * Setter for property issueAfter.
     *
     * @param issueAfter New value of property issueAfter.
     */
    public void setIssueAfter(String issueAfter) {
        this.issueAfter = issueAfter;
    }

    /**
     * Getter for property noticeChargeAmt.
     *
     * @return Value of property noticeChargeAmt.
     */
    public Double getNoticeChargeAmt() {
        return noticeChargeAmt;
    }

    /**
     * Setter for property noticeChargeAmt.
     *
     * @param noticeChargeAmt New value of property noticeChargeAmt.
     */
    public void setNoticeChargeAmt(Double noticeChargeAmt) {
        this.noticeChargeAmt = noticeChargeAmt;
    }

    /**
     * Getter for property postageAmt.
     *
     * @return Value of property postageAmt.
     */
    public Double getPostageAmt() {
        return postageAmt;
    }

    /**
     * Setter for property postageAmt.
     *
     * @param postageAmt New value of property postageAmt.
     */
    public void setPostageAmt(Double postageAmt) {
        this.postageAmt = postageAmt;
    }

    /**
     * Getter for property noticeType.
     *
     * @return Value of property noticeType.
     */
    public String getNoticeType() {
        return noticeType;
    }

    /**
     * Setter for property noticeType.
     *
     * @param noticeType New value of property noticeType.
     */
    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }
}