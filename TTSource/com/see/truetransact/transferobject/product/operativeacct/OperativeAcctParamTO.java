/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctParamTO.java
 * 
 * Created on Mon Jul 12 15:46:20 IST 2004
 */
package com.see.truetransact.transferobject.product.operativeacct;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OP_AC_ACCOUNT_PARAM.
 */
public class OperativeAcctParamTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String chkAllowed = "";
    private Double minBalWChk = null;
    private Double minBalWtChk = null;
    private String intReq = "";
    private String nomineeReq = "";
    private Double noOfNominee = null;
    private String acOpReq = "";
    private Double minPdNew = null;
    private Double minPdDormant = null;
    private Double minPdInop = null;
    private Double minPdCls = null;
    private Double statFrequency = null;
    private Double freeWithdrawals = null;
    private Double freeWithdrawalsPd = null;
    private Date freeWithdrawalFrom = null;
    private Double noFreeChkLeaves = null;
    private Double freeChkLeavesPd = null;
    private Date freeChkLeavesFrom = null;
    private String taxIntApplicable = "";
    private Double rateOfInt = null;
    private String lmtDefinitionAllowed = "";
    private String tempOdAllowed = "";
    private String staffAcctOpened = "";
    private String collectIntCredit = "";
    private String debitIntClearing = "";
    private String creditIntUnclear = "";
    private String issueToken = "";
    private String allowWithdrawalSlip = "";
    private Double maxAllowedWdSlip = null;
    private String freeWithdrawalType = "";
    private String freeChkLeavesType = "";
    private String numPatternFollowedPrefix = "";
    private Long numPatternFollowedSuffix = null;
    private Long lastAccNum = null;

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
     * Setter/Getter for MIN_BAL_W_CHK - table Field
     */
    public void setMinBalWChk(Double minBalWChk) {
        this.minBalWChk = minBalWChk;
    }

    public Double getMinBalWChk() {
        return minBalWChk;
    }

    /**
     * Setter/Getter for MIN_BAL_WT_CHK - table Field
     */
    public void setMinBalWtChk(Double minBalWtChk) {
        this.minBalWtChk = minBalWtChk;
    }

    public Double getMinBalWtChk() {
        return minBalWtChk;
    }

    /**
     * Setter/Getter for INT_REQ - table Field
     */
    public void setIntReq(String intReq) {
        this.intReq = intReq;
    }

    public String getIntReq() {
        return intReq;
    }

    /**
     * Setter/Getter for NOMINEE_REQ - table Field
     */
    public void setNomineeReq(String nomineeReq) {
        this.nomineeReq = nomineeReq;
    }

    public String getNomineeReq() {
        return nomineeReq;
    }

    /**
     * Setter/Getter for NO_OF_NOMINEE - table Field
     */
    public void setNoOfNominee(Double noOfNominee) {
        this.noOfNominee = noOfNominee;
    }

    public Double getNoOfNominee() {
        return noOfNominee;
    }

    /**
     * Setter/Getter for AC_OP_REQ - table Field
     */
    public void setAcOpReq(String acOpReq) {
        this.acOpReq = acOpReq;
    }

    public String getAcOpReq() {
        return acOpReq;
    }

    /**
     * Setter/Getter for MIN_PD_NEW - table Field
     */
    public void setMinPdNew(Double minPdNew) {
        this.minPdNew = minPdNew;
    }

    public Double getMinPdNew() {
        return minPdNew;
    }

    /**
     * Setter/Getter for MIN_PD_DORMANT - table Field
     */
    public void setMinPdDormant(Double minPdDormant) {
        this.minPdDormant = minPdDormant;
    }

    public Double getMinPdDormant() {
        return minPdDormant;
    }

    /**
     * Setter/Getter for MIN_PD_INOP - table Field
     */
    public void setMinPdInop(Double minPdInop) {
        this.minPdInop = minPdInop;
    }

    public Double getMinPdInop() {
        return minPdInop;
    }

    /**
     * Setter/Getter for MIN_PD_CLS - table Field
     */
    public void setMinPdCls(Double minPdCls) {
        this.minPdCls = minPdCls;
    }

    public Double getMinPdCls() {
        return minPdCls;
    }

    /**
     * Setter/Getter for STAT_FREQUENCY - table Field
     */
    public void setStatFrequency(Double statFrequency) {
        this.statFrequency = statFrequency;
    }

    public Double getStatFrequency() {
        return statFrequency;
    }

    /**
     * Setter/Getter for FREE_WITHDRAWALS - table Field
     */
    public void setFreeWithdrawals(Double freeWithdrawals) {
        this.freeWithdrawals = freeWithdrawals;
    }

    public Double getFreeWithdrawals() {
        return freeWithdrawals;
    }

    /**
     * Setter/Getter for FREE_WITHDRAWALS_PD - table Field
     */
    public void setFreeWithdrawalsPd(Double freeWithdrawalsPd) {
        this.freeWithdrawalsPd = freeWithdrawalsPd;
    }

    public Double getFreeWithdrawalsPd() {
        return freeWithdrawalsPd;
    }

    /**
     * Setter/Getter for FREE_WITHDRAWAL_FROM - table Field
     */
    public void setFreeWithdrawalFrom(Date freeWithdrawalFrom) {
        this.freeWithdrawalFrom = freeWithdrawalFrom;
    }

    public Date getFreeWithdrawalFrom() {
        return freeWithdrawalFrom;
    }

    /**
     * Setter/Getter for NO_FREE_CHK_LEAVES - table Field
     */
    public void setNoFreeChkLeaves(Double noFreeChkLeaves) {
        this.noFreeChkLeaves = noFreeChkLeaves;
    }

    public Double getNoFreeChkLeaves() {
        return noFreeChkLeaves;
    }

    /**
     * Setter/Getter for FREE_CHK_LEAVES_PD - table Field
     */
    public void setFreeChkLeavesPd(Double freeChkLeavesPd) {
        this.freeChkLeavesPd = freeChkLeavesPd;
    }

    public Double getFreeChkLeavesPd() {
        return freeChkLeavesPd;
    }

    /**
     * Setter/Getter for FREE_CHK_LEAVES_FROM - table Field
     */
    public void setFreeChkLeavesFrom(Date freeChkLeavesFrom) {
        this.freeChkLeavesFrom = freeChkLeavesFrom;
    }

    public Date getFreeChkLeavesFrom() {
        return freeChkLeavesFrom;
    }

    /**
     * Setter/Getter for TAX_INT_APPLICABLE - table Field
     */
    public void setTaxIntApplicable(String taxIntApplicable) {
        this.taxIntApplicable = taxIntApplicable;
    }

    public String getTaxIntApplicable() {
        return taxIntApplicable;
    }

    /**
     * Setter/Getter for RATE_OF_INT - table Field
     */
    public void setRateOfInt(Double rateOfInt) {
        this.rateOfInt = rateOfInt;
    }

    public Double getRateOfInt() {
        return rateOfInt;
    }

    /**
     * Setter/Getter for LMT_DEFINITION_ALLOWED - table Field
     */
    public void setLmtDefinitionAllowed(String lmtDefinitionAllowed) {
        this.lmtDefinitionAllowed = lmtDefinitionAllowed;
    }

    public String getLmtDefinitionAllowed() {
        return lmtDefinitionAllowed;
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
     * Setter/Getter for STAFF_ACCT_OPENED - table Field
     */
    public void setStaffAcctOpened(String staffAcctOpened) {
        this.staffAcctOpened = staffAcctOpened;
    }

    public String getStaffAcctOpened() {
        return staffAcctOpened;
    }

    /**
     * Setter/Getter for COLLECT_INT_CREDIT - table Field
     */
    public void setCollectIntCredit(String collectIntCredit) {
        this.collectIntCredit = collectIntCredit;
    }

    public String getCollectIntCredit() {
        return collectIntCredit;
    }

    /**
     * Setter/Getter for DEBIT_INT_CLEARING - table Field
     */
    public void setDebitIntClearing(String debitIntClearing) {
        this.debitIntClearing = debitIntClearing;
    }

    public String getDebitIntClearing() {
        return debitIntClearing;
    }

    /**
     * Setter/Getter for CREDIT_INT_UNCLEAR - table Field
     */
    public void setCreditIntUnclear(String creditIntUnclear) {
        this.creditIntUnclear = creditIntUnclear;
    }

    public String getCreditIntUnclear() {
        return creditIntUnclear;
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
     * Setter/Getter for ALLOW_WITHDRAWAL_SLIP - table Field
     */
    public void setAllowWithdrawalSlip(String allowWithdrawalSlip) {
        this.allowWithdrawalSlip = allowWithdrawalSlip;
    }

    public String getAllowWithdrawalSlip() {
        return allowWithdrawalSlip;
    }

    /**
     * Setter/Getter for MAX_ALLOWED_WD_SLIP - table Field
     */
    public void setMaxAllowedWdSlip(Double maxAllowedWdSlip) {
        this.maxAllowedWdSlip = maxAllowedWdSlip;
    }

    public Double getMaxAllowedWdSlip() {
        return maxAllowedWdSlip;
    }

    /**
     * Setter/Getter for FREE_WITHDRAWAL_TYPE - table Field
     */
    public void setFreeWithdrawalType(String freeWithdrawalType) {
        this.freeWithdrawalType = freeWithdrawalType;
    }

    public String getFreeWithdrawalType() {
        return freeWithdrawalType;
    }

    /**
     * Setter/Getter for FREE_CHK_LEAVES_TYPE - table Field
     */
    public void setFreeChkLeavesType(String freeChkLeavesType) {
        this.freeChkLeavesType = freeChkLeavesType;
    }

    public String getFreeChkLeavesType() {
        return freeChkLeavesType;
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
        strB.append(getTOString("minBalWChk", minBalWChk));
        strB.append(getTOString("minBalWtChk", minBalWtChk));
        strB.append(getTOString("intReq", intReq));
        strB.append(getTOString("nomineeReq", nomineeReq));
        strB.append(getTOString("noOfNominee", noOfNominee));
        strB.append(getTOString("acOpReq", acOpReq));
        strB.append(getTOString("minPdNew", minPdNew));
        strB.append(getTOString("minPdDormant", minPdDormant));
        strB.append(getTOString("minPdInop", minPdInop));
        strB.append(getTOString("minPdCls", minPdCls));
        strB.append(getTOString("statFrequency", statFrequency));
        strB.append(getTOString("freeWithdrawals", freeWithdrawals));
        strB.append(getTOString("freeWithdrawalsPd", freeWithdrawalsPd));
        strB.append(getTOString("freeWithdrawalFrom", freeWithdrawalFrom));
        strB.append(getTOString("noFreeChkLeaves", noFreeChkLeaves));
        strB.append(getTOString("freeChkLeavesPd", freeChkLeavesPd));
        strB.append(getTOString("freeChkLeavesFrom", freeChkLeavesFrom));
        strB.append(getTOString("taxIntApplicable", taxIntApplicable));
        strB.append(getTOString("rateOfInt", rateOfInt));
        strB.append(getTOString("lmtDefinitionAllowed", lmtDefinitionAllowed));
        strB.append(getTOString("tempOdAllowed", tempOdAllowed));
        strB.append(getTOString("staffAcctOpened", staffAcctOpened));
        strB.append(getTOString("collectIntCredit", collectIntCredit));
        strB.append(getTOString("debitIntClearing", debitIntClearing));
        strB.append(getTOString("creditIntUnclear", creditIntUnclear));
        strB.append(getTOString("issueToken", issueToken));
        strB.append(getTOString("allowWithdrawalSlip", allowWithdrawalSlip));
        strB.append(getTOString("maxAllowedWdSlip", maxAllowedWdSlip));
        strB.append(getTOString("freeWithdrawalType", freeWithdrawalType));
        strB.append(getTOString("freeChkLeavesType", freeChkLeavesType));
        strB.append(getTOString("numPatternFollowedPrefix", numPatternFollowedPrefix));
        strB.append(getTOString("numPatternFollowedSuffix", numPatternFollowedSuffix));
        strB.append(getTOString("lastAccNum", lastAccNum));
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
        strB.append(getTOXml("minBalWChk", minBalWChk));
        strB.append(getTOXml("minBalWtChk", minBalWtChk));
        strB.append(getTOXml("intReq", intReq));
        strB.append(getTOXml("nomineeReq", nomineeReq));
        strB.append(getTOXml("noOfNominee", noOfNominee));
        strB.append(getTOXml("acOpReq", acOpReq));
        strB.append(getTOXml("minPdNew", minPdNew));
        strB.append(getTOXml("minPdDormant", minPdDormant));
        strB.append(getTOXml("minPdInop", minPdInop));
        strB.append(getTOXml("minPdCls", minPdCls));
        strB.append(getTOXml("statFrequency", statFrequency));
        strB.append(getTOXml("freeWithdrawals", freeWithdrawals));
        strB.append(getTOXml("freeWithdrawalsPd", freeWithdrawalsPd));
        strB.append(getTOXml("freeWithdrawalFrom", freeWithdrawalFrom));
        strB.append(getTOXml("noFreeChkLeaves", noFreeChkLeaves));
        strB.append(getTOXml("freeChkLeavesPd", freeChkLeavesPd));
        strB.append(getTOXml("freeChkLeavesFrom", freeChkLeavesFrom));
        strB.append(getTOXml("taxIntApplicable", taxIntApplicable));
        strB.append(getTOXml("rateOfInt", rateOfInt));
        strB.append(getTOXml("lmtDefinitionAllowed", lmtDefinitionAllowed));
        strB.append(getTOXml("tempOdAllowed", tempOdAllowed));
        strB.append(getTOXml("staffAcctOpened", staffAcctOpened));
        strB.append(getTOXml("collectIntCredit", collectIntCredit));
        strB.append(getTOXml("debitIntClearing", debitIntClearing));
        strB.append(getTOXml("creditIntUnclear", creditIntUnclear));
        strB.append(getTOXml("issueToken", issueToken));
        strB.append(getTOXml("allowWithdrawalSlip", allowWithdrawalSlip));
        strB.append(getTOXml("maxAllowedWdSlip", maxAllowedWdSlip));
        strB.append(getTOXml("freeWithdrawalType", freeWithdrawalType));
        strB.append(getTOXml("freeChkLeavesType", freeChkLeavesType));
        strB.append(getTOXml("numPatternFollowedPrefix", numPatternFollowedPrefix));
        strB.append(getTOXml("numPatternFollowedSuffix", numPatternFollowedSuffix));
        strB.append(getTOXml("lastAccNum", lastAccNum));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property numPatternFollowedPrefix.
     *
     * @return Value of property numPatternFollowedPrefix.
     */
    public java.lang.String getNumPatternFollowedPrefix() {
        return numPatternFollowedPrefix;
    }

    /**
     * Setter for property numPatternFollowedPrefix.
     *
     * @param numPatternFollowedPrefix New value of property
     * numPatternFollowedPrefix.
     */
    public void setNumPatternFollowedPrefix(java.lang.String numPatternFollowedPrefix) {
        this.numPatternFollowedPrefix = numPatternFollowedPrefix;
    }

    /**
     * Getter for property numPatternFollowedSuffix.
     *
     * @return Value of property numPatternFollowedSuffix.
     */
    public Long getNumPatternFollowedSuffix() {
        return numPatternFollowedSuffix;
    }

    /**
     * Setter for property numPatternFollowedSuffix.
     *
     * @param numPatternFollowedSuffix New value of property
     * numPatternFollowedSuffix.
     */
    public void setNumPatternFollowedSuffix(Long numPatternFollowedSuffix) {
        this.numPatternFollowedSuffix = numPatternFollowedSuffix;
    }

    /**
     * Getter for property lastAccNum.
     *
     * @return Value of property lastAccNum.
     */
    public Long getLastAccNum() {
        return lastAccNum;
    }

    /**
     * Setter for property lastAccNum.
     *
     * @param lastAccNum New value of property lastAccNum.
     */
    public void setLastAccNum(Long lastAccNum) {
        this.lastAccNum = lastAccNum;
    }
}