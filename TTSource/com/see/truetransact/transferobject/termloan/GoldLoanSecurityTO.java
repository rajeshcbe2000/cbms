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
public class GoldLoanSecurityTO extends TransferObject implements Serializable {
//	private String acctNum = "";
//	private String custId = "";
//	private Double securityNo = null;
//	private Date fromDt = null;
//	private Date toDt = null;
//	private Double securityAmt = null;
//	private String status = "";
//	private String statusBy = "";
//	private Date statusDt = null;
//	private String borrowNo = "";
//	private Double slno = null;
//	private Double margin = null;
//	private Double eligibleLoanAmt = null;

    private Integer slNo = 0;
    private String acctNum = "";
    private Date asOn = null;
    private Double grossWeight = 0.0;
    private Double netWeight = 0.0;
    private String purity = "";
    private String marketRate = "";
    private String securityValue = "";
    private String totalSecurityValue = "";
    private String margin = "";
    private String marginAmt = "";
    private String eligibleLoanAmt = "";
    private String appraiserId = "";
    private String particulars = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizeStatus = null;  //AJITH
    private String authorizeBy = null;  //AJITH
    private Date authorizeDt = null;
    private Integer noofPacket = 0;
    private String isRelease = "";
    private Date releaseDt = null;

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
//	 public String getKeyData() {
//		setKeyColumns("acctNum"+KEY_VAL_SEPARATOR+"slno");
//		return acctNum+KEY_VAL_SEPARATOR+slno;
//	}
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//		strB.append (getTOStringKey(getKeyData()));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("asOn", asOn));
        strB.append(getTOString("grossWeight", grossWeight));
        strB.append(getTOString("netWeight", netWeight));
        strB.append(getTOString("purity", purity));
        strB.append(getTOString("marketRate", marketRate));
        strB.append(getTOString("securityValue", securityValue));
        strB.append(getTOString("totalSecurityValue", totalSecurityValue));
        strB.append(getTOString("margin", margin));
        strB.append(getTOString("marginAmt", marginAmt));
        strB.append(getTOString("eligibleLoanAmt", eligibleLoanAmt));
        strB.append(getTOString("appraiserId", appraiserId));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("noofPacket", noofPacket));
        strB.append(getTOString("isRelease", isRelease));
        strB.append(getTOString("releaseDt", releaseDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//		strB.append (getTOXmlKey(getKeyData()));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("asOn", asOn));
        strB.append(getTOXml("grossWeight", grossWeight));
        strB.append(getTOXml("netWeight", netWeight));
        strB.append(getTOXml("purity", purity));
        strB.append(getTOXml("marketRate", marketRate));
        strB.append(getTOXml("securityValue", securityValue));
        strB.append(getTOXml("totalSecurityValue", totalSecurityValue));
        strB.append(getTOXml("margin", margin));
        strB.append(getTOXml("marginAmt", marginAmt));
        strB.append(getTOXml("eligibleLoanAmt", eligibleLoanAmt));
        strB.append(getTOXml("appraiserId", appraiserId));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("noofPacket", noofPacket));
        strB.append(getTOXml("isRelease", isRelease));
        strB.append(getTOXml("releaseDt", releaseDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(Integer slNo) {
        this.slNo = slNo;
    }

    

    /**
     * Getter for property acctNum.
     *
     * @return Value of property acctNum.
     */
    public java.lang.String getAcctNum() {
        return acctNum;
    }

    /**
     * Setter for property acctNum.
     *
     * @param acctNum New value of property acctNum.
     */
    public void setAcctNum(java.lang.String acctNum) {
        this.acctNum = acctNum;
    }

    /**
     * Getter for property asOn.
     *
     * @return Value of property asOn.
     */
    public java.util.Date getAsOn() {
        return asOn;
    }

    /**
     * Setter for property asOn.
     *
     * @param asOn New value of property asOn.
     */
    public void setAsOn(java.util.Date asOn) {
        this.asOn = asOn;
    }

    

    

    /**
     * Getter for property purity.
     *
     * @return Value of property purity.
     */
    public java.lang.String getPurity() {
        return purity;
    }

    /**
     * Setter for property purity.
     *
     * @param purity New value of property purity.
     */
    public void setPurity(java.lang.String purity) {
        this.purity = purity;
    }

    /**
     * Getter for property marketRate.
     *
     * @return Value of property marketRate.
     */
    public java.lang.String getMarketRate() {
        return marketRate;
    }

    /**
     * Setter for property marketRate.
     *
     * @param marketRate New value of property marketRate.
     */
    public void setMarketRate(java.lang.String marketRate) {
        this.marketRate = marketRate;
    }

    /**
     * Getter for property securityValue.
     *
     * @return Value of property securityValue.
     */
    public java.lang.String getSecurityValue() {
        return securityValue;
    }

    /**
     * Setter for property securityValue.
     *
     * @param securityValue New value of property securityValue.
     */
    public void setSecurityValue(java.lang.String securityValue) {
        this.securityValue = securityValue;
    }

    /**
     * Getter for property totalSecurityValue.
     *
     * @return Value of property totalSecurityValue.
     */
    public java.lang.String getTotalSecurityValue() {
        return totalSecurityValue;
    }

    /**
     * Setter for property totalSecurityValue.
     *
     * @param totalSecurityValue New value of property totalSecurityValue.
     */
    public void setTotalSecurityValue(java.lang.String totalSecurityValue) {
        this.totalSecurityValue = totalSecurityValue;
    }

    /**
     * Getter for property margin.
     *
     * @return Value of property margin.
     */
    public java.lang.String getMargin() {
        return margin;
    }

    /**
     * Setter for property margin.
     *
     * @param margin New value of property margin.
     */
    public void setMargin(java.lang.String margin) {
        this.margin = margin;
    }

    /**
     * Getter for property marginAmt.
     *
     * @return Value of property marginAmt.
     */
    public java.lang.String getMarginAmt() {
        return marginAmt;
    }

    /**
     * Setter for property marginAmt.
     *
     * @param marginAmt New value of property marginAmt.
     */
    public void setMarginAmt(java.lang.String marginAmt) {
        this.marginAmt = marginAmt;
    }

    /**
     * Getter for property eligibleLoanAmt.
     *
     * @return Value of property eligibleLoanAmt.
     */
    public java.lang.String getEligibleLoanAmt() {
        return eligibleLoanAmt;
    }

    /**
     * Setter for property eligibleLoanAmt.
     *
     * @param eligibleLoanAmt New value of property eligibleLoanAmt.
     */
    public void setEligibleLoanAmt(java.lang.String eligibleLoanAmt) {
        this.eligibleLoanAmt = eligibleLoanAmt;
    }

    /**
     * Getter for property appraiserId.
     *
     * @return Value of property appraiserId.
     */
    public java.lang.String getAppraiserId() {
        return appraiserId;
    }

    /**
     * Setter for property appraiserId.
     *
     * @param appraiserId New value of property appraiserId.
     */
    public void setAppraiserId(java.lang.String appraiserId) {
        this.appraiserId = appraiserId;
    }

    /**
     * Getter for property particulars.
     *
     * @return Value of property particulars.
     */
    public java.lang.String getParticulars() {
        return particulars;
    }

    /**
     * Setter for property particulars.
     *
     * @param particulars New value of property particulars.
     */
    public void setParticulars(java.lang.String particulars) {
        this.particulars = particulars;
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
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    /**
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property authorizeDt.
     *
     * @return Value of property authorizeDt.
     */
    public java.util.Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter for property authorizeDt.
     *
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.util.Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    public Integer getNoofPacket() {
        return noofPacket;
    }

    public void setNoofPacket(Integer noofPacket) {
        this.noofPacket = noofPacket;
    }

    

    public String getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(String isRelease) {
        this.isRelease = isRelease;
    }

    public Date getReleaseDt() {
        return releaseDt;
    }

    public void setReleaseDt(Date releaseDt) {
        this.releaseDt = releaseDt;
    }
    
}