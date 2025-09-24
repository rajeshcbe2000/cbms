/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ParameterTO.java
 * 
 * Created on Wed Jan 19 15:58:31 IST 2005
 */
package com.see.truetransact.transferobject.clearing.clearingData;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CLEARING_PARAM.
 */
public class ClearingDataImportTO extends TransferObject implements Serializable {

    private Double transCode = null;
    private Double userNumber = null;
    private String userName = "";
    private String userCreditRef = "";
    private Double tapeInputNo = null;
    private Double sponsor = null;
    private String usersAcNo = null;
    private String ledgerNo = null;
    private Double userLimit = null;
    private Double totalAmt = null;
    private Date setellementDt = null;
    private Double sequenceNo = null;
    private Double checkSum = null;
    private String filter = "";

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
        strB.append(getTOString("transCode", transCode));
        strB.append(getTOString("userNumber", userNumber));
        strB.append(getTOString("userName", userName));
        strB.append(getTOString("userCreditRef", userCreditRef));
        strB.append(getTOString("tapeInputNo", tapeInputNo));
        strB.append(getTOString("sponsor", sponsor));
        strB.append(getTOString("usersAcNo", usersAcNo));
        strB.append(getTOString("ledgerNo", ledgerNo));
        strB.append(getTOString("userLimit", userLimit));
        strB.append(getTOString("totalAmt", totalAmt));
        strB.append(getTOString("setellementDt", setellementDt));
        strB.append(getTOString("sequenceNo", sequenceNo));
        strB.append(getTOString("checkSum", checkSum));
        strB.append(getTOString("filter", filter));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("transCode", transCode));
        strB.append(getTOXml("userNumber", userNumber));
        strB.append(getTOXml("userName", userName));
        strB.append(getTOXml("userCreditRef", userCreditRef));
        strB.append(getTOXml("tapeInputNo", tapeInputNo));
        strB.append(getTOXml("sponsor", sponsor));
        strB.append(getTOXml("usersAcNo", usersAcNo));
        strB.append(getTOXml("ledgerNo", ledgerNo));
        strB.append(getTOXml("userNumber", userNumber));
        strB.append(getTOXml("userLimit", userLimit));
        strB.append(getTOXml("totalAmt", totalAmt));
        strB.append(getTOXml("setellementDt", setellementDt));
        strB.append(getTOXml("sequenceNo", sequenceNo));
        strB.append(getTOXml("checkSum", checkSum));
        strB.append(getTOXml("filter", filter));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property transCode.
     *
     * @return Value of property transCode.
     */
    public java.lang.Double getTransCode() {
        return transCode;
    }

    /**
     * Setter for property transCode.
     *
     * @param transCode New value of property transCode.
     */
    public void setTransCode(java.lang.Double transCode) {
        this.transCode = transCode;
    }

    /**
     * Getter for property userNumber.
     *
     * @return Value of property userNumber.
     */
    public java.lang.Double getUserNumber() {
        return userNumber;
    }

    /**
     * Setter for property userNumber.
     *
     * @param userNumber New value of property userNumber.
     */
    public void setUserNumber(java.lang.Double userNumber) {
        this.userNumber = userNumber;
    }

    /**
     * Getter for property userName.
     *
     * @return Value of property userName.
     */
    public java.lang.String getUserName() {
        return userName;
    }

    /**
     * Setter for property userName.
     *
     * @param userName New value of property userName.
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    /**
     * Getter for property userCreditRef.
     *
     * @return Value of property userCreditRef.
     */
    public java.lang.String getUserCreditRef() {
        return userCreditRef;
    }

    /**
     * Setter for property userCreditRef.
     *
     * @param userCreditRef New value of property userCreditRef.
     */
    public void setUserCreditRef(java.lang.String userCreditRef) {
        this.userCreditRef = userCreditRef;
    }

    /**
     * Getter for property tapeInputNo.
     *
     * @return Value of property tapeInputNo.
     */
    public java.lang.Double getTapeInputNo() {
        return tapeInputNo;
    }

    /**
     * Setter for property tapeInputNo.
     *
     * @param tapeInputNo New value of property tapeInputNo.
     */
    public void setTapeInputNo(java.lang.Double tapeInputNo) {
        this.tapeInputNo = tapeInputNo;
    }

    /**
     * Getter for property sponsor.
     *
     * @return Value of property sponsor.
     */
    public java.lang.Double getSponsor() {
        return sponsor;
    }

    /**
     * Setter for property sponsor.
     *
     * @param sponsor New value of property sponsor.
     */
    public void setSponsor(java.lang.Double sponsor) {
        this.sponsor = sponsor;
    }

    /**
     * Getter for property usersAcNo.
     *
     * @return Value of property usersAcNo.
     */
    public java.lang.String getUsersAcNo() {
        return usersAcNo;
    }

    /**
     * Setter for property usersAcNo.
     *
     * @param usersAcNo New value of property usersAcNo.
     */
    public void setUsersAcNo(java.lang.String usersAcNo) {
        this.usersAcNo = usersAcNo;
    }

    /**
     * Getter for property ledgerNo.
     *
     * @return Value of property ledgerNo.
     */
    public java.lang.String getLedgerNo() {
        return ledgerNo;
    }

    /**
     * Setter for property ledgerNo.
     *
     * @param ledgerNo New value of property ledgerNo.
     */
    public void setLedgerNo(java.lang.String ledgerNo) {
        this.ledgerNo = ledgerNo;
    }

    /**
     * Getter for property userLimit.
     *
     * @return Value of property userLimit.
     */
    public java.lang.Double getUserLimit() {
        return userLimit;
    }

    /**
     * Setter for property userLimit.
     *
     * @param userLimit New value of property userLimit.
     */
    public void setUserLimit(java.lang.Double userLimit) {
        this.userLimit = userLimit;
    }

    /**
     * Getter for property totalAmt.
     *
     * @return Value of property totalAmt.
     */
    public java.lang.Double getTotalAmt() {
        return totalAmt;
    }

    /**
     * Setter for property totalAmt.
     *
     * @param totalAmt New value of property totalAmt.
     */
    public void setTotalAmt(java.lang.Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    /**
     * Getter for property setellementDt.
     *
     * @return Value of property setellementDt.
     */
    public java.util.Date getSetellementDt() {
        return setellementDt;
    }

    /**
     * Setter for property setellementDt.
     *
     * @param setellementDt New value of property setellementDt.
     */
    public void setSetellementDt(java.util.Date setellementDt) {
        this.setellementDt = setellementDt;
    }

    /**
     * Getter for property sequenceNo.
     *
     * @return Value of property sequenceNo.
     */
    public java.lang.Double getSequenceNo() {
        return sequenceNo;
    }

    /**
     * Setter for property sequenceNo.
     *
     * @param sequenceNo New value of property sequenceNo.
     */
    public void setSequenceNo(java.lang.Double sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    /**
     * Getter for property checkSum.
     *
     * @return Value of property checkSum.
     */
    public java.lang.Double getCheckSum() {
        return checkSum;
    }

    /**
     * Setter for property checkSum.
     *
     * @param checkSum New value of property checkSum.
     */
    public void setCheckSum(java.lang.Double checkSum) {
        this.checkSum = checkSum;
    }

    /**
     * Getter for property filter.
     *
     * @return Value of property filter.
     */
    public java.lang.String getFilter() {
        return filter;
    }

    /**
     * Setter for property filter.
     *
     * @param filter New value of property filter.
     */
    public void setFilter(java.lang.String filter) {
        this.filter = filter;
    }
}