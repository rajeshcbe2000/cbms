/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountClosing.java
 * 
 * Created on Mon May 10 11:10:46 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.operativeaccount;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ACT_CLOSING.
 */
public class AccountClosingTO extends TransferObject implements Serializable {

    private String actNum = "";
    private Double unusedChk = null;
    private Double actClosingChrg = null;
    private Double intPayable = null;
    private Double chrgDetails = null;
    private Double payableBal = null;
    private String authorizeStatus = null;;
    private String authorizeUser = "";
    private Date authorizeDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String prodId = "";
    private String VariableNo = "";
    private Double taxPayable = null;
    private String remarks = null;
    private Double creditIntAD = null;
    private Double susbsidyAmt = null;
    private Double rebateInterest = null;
    private Double insuranceCharges = null;
    private String drAccHead = null;
    private Double principalWaiveAmt = 0.0;

    public String getDrAccHead() {
        return drAccHead;
    }

    public void setDrAccHead(String drAccHead) {
        this.drAccHead = drAccHead;
    }
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
     * Setter/Getter for UNUSED_CHK - table Field
     */
    public void setUnusedChk(Double unusedChk) {
        this.unusedChk = unusedChk;
    }

    public Double getUnusedChk() {
        return unusedChk;
    }

    /**
     * Setter/Getter for ACT_CLOSING_CHRG - table Field
     */
    public void setActClosingChrg(Double actClosingChrg) {
        this.actClosingChrg = actClosingChrg;
    }

    public Double getActClosingChrg() {
        return actClosingChrg;
    }

    /**
     * Setter/Getter for INT_PAYABLE - table Field
     */
    public void setIntPayable(Double intPayable) {
        this.intPayable = intPayable;
    }

    public Double getIntPayable() {
        return intPayable;
    }

    /**
     * Setter/Getter for CHRG_DETAILS - table Field
     */
    public void setChrgDetails(Double chrgDetails) {
        this.chrgDetails = chrgDetails;
    }

    public Double getChrgDetails() {
        return chrgDetails;
    }

    /**
     * Setter/Getter for PAYABLE_BAL - table Field
     */
    public void setPayableBal(Double payableBal) {
        this.payableBal = payableBal;
    }

    public Double getPayableBal() {
        return payableBal;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for AUTHORIZE_USER - table Field
     */
    public void setAuthorizeUser(String authorizeUser) {
        this.authorizeUser = authorizeUser;
    }

    public String getAuthorizeUser() {
        return authorizeUser;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("actNum");
        return "actNum";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("unusedChk", unusedChk));
        strB.append(getTOString("actClosingChrg", actClosingChrg));
        strB.append(getTOString("intPayable", intPayable));
        strB.append(getTOString("chrgDetails", chrgDetails));
        strB.append(getTOString("payableBal", payableBal));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeUser", authorizeUser));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("VariableNo", VariableNo));
        strB.append(getTOString("taxPayable", taxPayable));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("creditIntAD", creditIntAD));
        strB.append(getTOString("susbsidyAmt", susbsidyAmt));
        strB.append(getTOString("rebateInterest", rebateInterest));
        strB.append(getTOString("insuranceCharges", insuranceCharges));
        strB.append(getTOString("drAccHead", drAccHead));
        strB.append(getTOString("principalWaiveAmt", principalWaiveAmt));
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
        strB.append(getTOXml("unusedChk", unusedChk));
        strB.append(getTOXml("actClosingChrg", actClosingChrg));
        strB.append(getTOXml("intPayable", intPayable));
        strB.append(getTOXml("chrgDetails", chrgDetails));
        strB.append(getTOXml("payableBal", payableBal));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeUser", authorizeUser));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("VariableNo", VariableNo));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("taxPayable", taxPayable));
        strB.append(getTOXml("creditIntAD", creditIntAD));
        strB.append(getTOXml("susbsidyAmt", susbsidyAmt));
        strB.append(getTOXml("rebateInterest", rebateInterest));
        strB.append(getTOXml("insuranceCharges", insuranceCharges));
        strB.append(getTOXml("drAccHead", drAccHead));
        strB.append(getTOXml("principalWaiveAmt", principalWaiveAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
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

    /**
     * Getter for property prodId.
     *
     * @return Value of property prodId.
     */
    public java.lang.String getProdId() {
        return prodId;
    }

    /**
     * Setter for property prodId.
     *
     * @param prodId New value of property prodId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }

    /**
     * Getter for property VariableNo.
     *
     * @return Value of property VariableNo.
     */
    public java.lang.String getVariableNo() {
        return VariableNo;
    }

    /**
     * Setter for property VariableNo.
     *
     * @param VariableNo New value of property VariableNo.
     */
    public void setVariableNo(java.lang.String VariableNo) {
        this.VariableNo = VariableNo;
    }

    /**
     * Getter for property taxPayable.
     *
     * @return Value of property taxPayable.
     */
    public java.lang.Double getTaxPayable() {
        return taxPayable;
    }

    /**
     * Setter for property taxPayable.
     *
     * @param taxPayable New value of property taxPayable.
     */
    public void setTaxPayable(java.lang.Double taxPayable) {
        this.taxPayable = taxPayable;
    }

    /**
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }

    /**
     * Setter for property remarks.
     *
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }

    /**
     * Getter for property creditIntAD.
     *
     * @return Value of property creditIntAD.
     */
    public java.lang.Double getCreditIntAD() {
        return creditIntAD;
    }

    /**
     * Setter for property creditIntAD.
     *
     * @param creditIntAD New value of property creditIntAD.
     */
    public void setCreditIntAD(java.lang.Double creditIntAD) {
        this.creditIntAD = creditIntAD;
    }

    /**
     * Getter for property susbsidyAmt.
     *
     * @return Value of property susbsidyAmt.
     */
    public java.lang.Double getSusbsidyAmt() {
        return susbsidyAmt;
    }

    /**
     * Setter for property susbsidyAmt.
     *
     * @param susbsidyAmt New value of property susbsidyAmt.
     */
    public void setSusbsidyAmt(java.lang.Double susbsidyAmt) {
        this.susbsidyAmt = susbsidyAmt;
    }

    /**
     * Getter for property rebateInterest.
     *
     * @return Value of property rebateInterest.
     */
    public java.lang.Double getRebateInterest() {
        return rebateInterest;
    }

    /**
     * Setter for property rebateInterest.
     *
     * @param rebateInterest New value of property rebateInterest.
     */
    public void setRebateInterest(java.lang.Double rebateInterest) {
        this.rebateInterest = rebateInterest;
    }

    /**
     * Getter for property insuranceCharges.
     *
     * @return Value of property insuranceCharges.
     */
    public java.lang.Double getInsuranceCharges() {
        return insuranceCharges;
    }

    /**
     * Setter for property insuranceCharges.
     *
     * @param insuranceCharges New value of property insuranceCharges.
     */
    public void setInsuranceCharges(java.lang.Double insuranceCharges) {
        this.insuranceCharges = insuranceCharges;
    }

    public Double getPrincipalWaiveAmt() {
        return principalWaiveAmt;
    }

    public void setPrincipalWaiveAmt(Double principalWaiveAmt) {
        this.principalWaiveAmt = principalWaiveAmt;
    }
    
    
    
    
}