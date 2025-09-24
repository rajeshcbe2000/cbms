/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AdvancesProductAccParameterTO.java
 * 
 * Created on Mon Apr 11 18:01:48 IST 2005
 */
package com.see.truetransact.transferobject.product.advances;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ADVANCES_PROD_ACPARAM.
 */
public class AdvancesProductAccParameterTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String chkAllowed = "";
    private Double discEolPerc = null;
    private String numberPattern = "";
    private String lastAcNo = "";
    private Double statFreq = null;
    private Double noFreeChqLeaves = null;
    private Double freeChqPeriod = null;
    private Date freeChqStart = null;
    private String limitDefAllowed = "";
    private String tempOdAllowed = "";
    private String staffAcOpened = "";
    private String debitUnclearAppl = "";
    private String creditUnclearAppl = "";
    private String issueToken = "";
    private String allowWdSlip = "";
    private Double maxAmtAllowed = null;

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
     * Setter/Getter for CHK_ALLOWED - table Field
     */
    public void setChkAllowed(String chkAllowed) {
        this.chkAllowed = chkAllowed;
    }

    public String getChkAllowed() {
        return chkAllowed;
    }

    /**
     * Setter/Getter for DISC_EOL_PERC - table Field
     */
    public void setDiscEolPerc(Double discEolPerc) {
        this.discEolPerc = discEolPerc;
    }

    public Double getDiscEolPerc() {
        return discEolPerc;
    }

    /**
     * Setter/Getter for NUMBER_PATTERN - table Field
     */
    public void setNumberPattern(String numberPattern) {
        this.numberPattern = numberPattern;
    }

    public String getNumberPattern() {
        return numberPattern;
    }

    /**
     * Setter/Getter for LAST_AC_NO - table Field
     */
    public void setLastAcNo(String lastAcNo) {
        this.lastAcNo = lastAcNo;
    }

    public String getLastAcNo() {
        return lastAcNo;
    }

    /**
     * Setter/Getter for STAT_FREQ - table Field
     */
    public void setStatFreq(Double statFreq) {
        this.statFreq = statFreq;
    }

    public Double getStatFreq() {
        return statFreq;
    }

    /**
     * Setter/Getter for NO_FREE_CHQ_LEAVES - table Field
     */
    public void setNoFreeChqLeaves(Double noFreeChqLeaves) {
        this.noFreeChqLeaves = noFreeChqLeaves;
    }

    public Double getNoFreeChqLeaves() {
        return noFreeChqLeaves;
    }

    /**
     * Setter/Getter for FREE_CHQ_PERIOD - table Field
     */
    public void setFreeChqPeriod(Double freeChqPeriod) {
        this.freeChqPeriod = freeChqPeriod;
    }

    public Double getFreeChqPeriod() {
        return freeChqPeriod;
    }

    /**
     * Setter/Getter for FREE_CHQ_START - table Field
     */
    public void setFreeChqStart(Date freeChqStart) {
        this.freeChqStart = freeChqStart;
    }

    public Date getFreeChqStart() {
        return freeChqStart;
    }

    /**
     * Setter/Getter for LIMIT_DEF_ALLOWED - table Field
     */
    public void setLimitDefAllowed(String limitDefAllowed) {
        this.limitDefAllowed = limitDefAllowed;
    }

    public String getLimitDefAllowed() {
        return limitDefAllowed;
    }

    /**
     * Setter/Getter for TEMP_OD_ALLOWED - table Field
     */
    public void setTempOdAllowed(String tempOdAllowed) {
        this.tempOdAllowed = tempOdAllowed;
    }

    public String getTempOdAllowed() {
        return tempOdAllowed;
    }

    /**
     * Setter/Getter for STAFF_AC_OPENED - table Field
     */
    public void setStaffAcOpened(String staffAcOpened) {
        this.staffAcOpened = staffAcOpened;
    }

    public String getStaffAcOpened() {
        return staffAcOpened;
    }

    /**
     * Setter/Getter for DEBIT_UNCLEAR_APPL - table Field
     */
    public void setDebitUnclearAppl(String debitUnclearAppl) {
        this.debitUnclearAppl = debitUnclearAppl;
    }

    public String getDebitUnclearAppl() {
        return debitUnclearAppl;
    }

    /**
     * Setter/Getter for CREDIT_UNCLEAR_APPL - table Field
     */
    public void setCreditUnclearAppl(String creditUnclearAppl) {
        this.creditUnclearAppl = creditUnclearAppl;
    }

    public String getCreditUnclearAppl() {
        return creditUnclearAppl;
    }

    /**
     * Setter/Getter for ISSUE_TOKEN - table Field
     */
    public void setIssueToken(String issueToken) {
        this.issueToken = issueToken;
    }

    public String getIssueToken() {
        return issueToken;
    }

    /**
     * Setter/Getter for ALLOW_WD_SLIP - table Field
     */
    public void setAllowWdSlip(String allowWdSlip) {
        this.allowWdSlip = allowWdSlip;
    }

    public String getAllowWdSlip() {
        return allowWdSlip;
    }

    /**
     * Setter/Getter for MAX_AMT_ALLOWED - table Field
     */
    public void setMaxAmtAllowed(Double maxAmtAllowed) {
        this.maxAmtAllowed = maxAmtAllowed;
    }

    public Double getMaxAmtAllowed() {
        return maxAmtAllowed;
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
        strB.append(getTOString("chkAllowed", chkAllowed));
        strB.append(getTOString("discEolPerc", discEolPerc));
        strB.append(getTOString("numberPattern", numberPattern));
        strB.append(getTOString("lastAcNo", lastAcNo));
        strB.append(getTOString("statFreq", statFreq));
        strB.append(getTOString("noFreeChqLeaves", noFreeChqLeaves));
        strB.append(getTOString("freeChqPeriod", freeChqPeriod));
        strB.append(getTOString("freeChqStart", freeChqStart));
        strB.append(getTOString("limitDefAllowed", limitDefAllowed));
        strB.append(getTOString("tempOdAllowed", tempOdAllowed));
        strB.append(getTOString("staffAcOpened", staffAcOpened));
        strB.append(getTOString("debitUnclearAppl", debitUnclearAppl));
        strB.append(getTOString("creditUnclearAppl", creditUnclearAppl));
        strB.append(getTOString("issueToken", issueToken));
        strB.append(getTOString("allowWdSlip", allowWdSlip));
        strB.append(getTOString("maxAmtAllowed", maxAmtAllowed));
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
        strB.append(getTOXml("chkAllowed", chkAllowed));
        strB.append(getTOXml("discEolPerc", discEolPerc));
        strB.append(getTOXml("numberPattern", numberPattern));
        strB.append(getTOXml("lastAcNo", lastAcNo));
        strB.append(getTOXml("statFreq", statFreq));
        strB.append(getTOXml("noFreeChqLeaves", noFreeChqLeaves));
        strB.append(getTOXml("freeChqPeriod", freeChqPeriod));
        strB.append(getTOXml("freeChqStart", freeChqStart));
        strB.append(getTOXml("limitDefAllowed", limitDefAllowed));
        strB.append(getTOXml("tempOdAllowed", tempOdAllowed));
        strB.append(getTOXml("staffAcOpened", staffAcOpened));
        strB.append(getTOXml("debitUnclearAppl", debitUnclearAppl));
        strB.append(getTOXml("creditUnclearAppl", creditUnclearAppl));
        strB.append(getTOXml("issueToken", issueToken));
        strB.append(getTOXml("allowWdSlip", allowWdSlip));
        strB.append(getTOXml("maxAmtAllowed", maxAmtAllowed));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}