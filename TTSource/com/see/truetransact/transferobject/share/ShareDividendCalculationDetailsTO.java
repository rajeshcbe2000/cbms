/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryMasterDetailsTO.java
 * 
 * Created on Mon Feb 21 13:11:45 IST 2011
 */
package com.see.truetransact.transferobject.share;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SALARY_MASTER_DETAILS.
 */
public class ShareDividendCalculationDetailsTO extends TransferObject implements Serializable {

    private String dividendCalcID = "";
    private String memberNO = "";
    private String name = "";
    private String dividendAmt = "";
    private String siAcctNo = "";
    private String prodType = "";
    private String prodID = "";
    private String dividendPaidStatus = "";
    private String authStatus = "";

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
        strB.append(getTOString("dividendCalcID", dividendCalcID));
        strB.append(getTOString("memberNO", memberNO));
        strB.append(getTOString("name", name));
        strB.append(getTOString("dividendAmt", dividendAmt));
        strB.append(getTOString("siAcctNo", siAcctNo));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("prodID", prodID));
        strB.append(getTOString("dividendPaidStatus", dividendPaidStatus));
        strB.append(getTOString("authStatus", authStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("dividendCalcID", dividendCalcID));
        strB.append(getTOXml("memberNO", memberNO));
        strB.append(getTOXml("name", name));
        strB.append(getTOXml("dividendAmt", dividendAmt));
        strB.append(getTOXml("siAcctNo", siAcctNo));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodID", prodID));
        strB.append(getTOXml("dividendPaidStatus", dividendPaidStatus));
        strB.append(getTOXml("authStatus", authStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property dividendCalcID.
     *
     * @return Value of property dividendCalcID.
     */
    public java.lang.String getDividendCalcID() {
        return dividendCalcID;
    }

    /**
     * Setter for property dividendCalcID.
     *
     * @param dividendCalcID New value of property dividendCalcID.
     */
    public void setDividendCalcID(java.lang.String dividendCalcID) {
        this.dividendCalcID = dividendCalcID;
    }

    /**
     * Getter for property memberNO.
     *
     * @return Value of property memberNO.
     */
    public java.lang.String getMemberNO() {
        return memberNO;
    }

    /**
     * Setter for property memberNO.
     *
     * @param memberNO New value of property memberNO.
     */
    public void setMemberNO(java.lang.String memberNO) {
        this.memberNO = memberNO;
    }

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * Getter for property dividendAmt.
     *
     * @return Value of property dividendAmt.
     */
    public java.lang.String getDividendAmt() {
        return dividendAmt;
    }

    /**
     * Setter for property dividendAmt.
     *
     * @param dividendAmt New value of property dividendAmt.
     */
    public void setDividendAmt(java.lang.String dividendAmt) {
        this.dividendAmt = dividendAmt;
    }

    /**
     * Getter for property siAcctNo.
     *
     * @return Value of property siAcctNo.
     */
    public java.lang.String getSiAcctNo() {
        return siAcctNo;
    }

    /**
     * Setter for property siAcctNo.
     *
     * @param siAcctNo New value of property siAcctNo.
     */
    public void setSiAcctNo(java.lang.String siAcctNo) {
        this.siAcctNo = siAcctNo;
    }

    /**
     * Getter for property prodType.
     *
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }

    /**
     * Setter for property prodType.
     *
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }

    /**
     * Getter for property prodID.
     *
     * @return Value of property prodID.
     */
    public java.lang.String getProdID() {
        return prodID;
    }

    /**
     * Setter for property prodID.
     *
     * @param prodID New value of property prodID.
     */
    public void setProdID(java.lang.String prodID) {
        this.prodID = prodID;
    }

    /**
     * Getter for property dividendPaidStatus.
     *
     * @return Value of property dividendPaidStatus.
     */
    public java.lang.String getDividendPaidStatus() {
        return dividendPaidStatus;
    }

    /**
     * Setter for property dividendPaidStatus.
     *
     * @param dividendPaidStatus New value of property dividendPaidStatus.
     */
    public void setDividendPaidStatus(java.lang.String dividendPaidStatus) {
        this.dividendPaidStatus = dividendPaidStatus;
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
}