/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanFacilityTO.java
 * 
 * Created on Wed Apr 13 17:21:29 IST 2005
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_FACILITY_DETAILS.
 */
public class TermLoanCourtDetailsTO extends TransferObject implements Serializable {

    private String courtOrderNo = "";
    private Date courtOrderDate = null;
    private Date oTSDate = null;
    private Double oTSRate = null;
    private Double totAmountDue = null;
    private Double settlementAmt = null;
    private Double principalAmount = null;
    private Double interestAmount = null;
    private Double penalInterestAmount = null;
    private Double chargeAmount = null;
    private Double installmentAmt = null;
    private String installmentNo = null;
    private Date firstInstallmentDt = null;
    private Date lastInstallmentDt = null;
    private String freq = "";
    private String repaySingle_YES = "";
    private Double penal = null;
    private String courtRemarks = "";
    private String status = "";
    private Date status_dt = null;
    private String authStatus = "";
    private Date authDate = null;
    private String slno = "";
    private String acctNum = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
//	 public String getKeyData() {
////		setKeyColumns("borrowNo"+KEY_VAL_SEPARATOR+"sanctionNo"+KEY_VAL_SEPARATOR+"slNo");
////		return borrowNo+KEY_VAL_SEPARATOR+sanctionNo+KEY_VAL_SEPARATOR+slNo;
//	}
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//		strB.append (getTOStringKey(getKeyData()));
        strB.append(getTOString("courtOrderNo", courtOrderNo));
        strB.append(getTOString("courtOrderDate", courtOrderDate));
        strB.append(getTOString("oTSDate", oTSDate));
        strB.append(getTOString("oTSRate", oTSRate));
        strB.append(getTOString("totAmountDue", totAmountDue));
        strB.append(getTOString("settlementAmt", settlementAmt));
        strB.append(getTOString("principalAmount", principalAmount));
        strB.append(getTOString("interestAmount", interestAmount));
        strB.append(getTOString("penalInterestAmount", penalInterestAmount));
        strB.append(getTOString("chargeAmount", chargeAmount));
        strB.append(getTOString("installmentAmt", installmentAmt));
        strB.append(getTOString("installmentNo", installmentNo));
        strB.append(getTOString("firstInstallmentDt", firstInstallmentDt));
        strB.append(getTOString("lastInstallmentDt", lastInstallmentDt));
        strB.append(getTOString("freq", freq));
        strB.append(getTOString("repaySingle_YES", repaySingle_YES));
        strB.append(getTOString("penal", penal));
        strB.append(getTOString("courtRemarks", courtRemarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("status_dt", status_dt));
        strB.append(getTOString("authStatus", authStatus));
        strB.append(getTOString("authDate", authDate));
        strB.append(getTOString("slno", slno));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//		strB.append (getTOXmlKey(getKeyData()));
        strB.append(getTOXml("courtOrderNo", courtOrderNo));
        strB.append(getTOXml("courtOrderDate", courtOrderDate));
        strB.append(getTOXml("oTSDate", oTSDate));
        strB.append(getTOXml("oTSRate", oTSRate));
        strB.append(getTOXml("totAmountDue", totAmountDue));
        strB.append(getTOXml("settlementAmt", settlementAmt));
        strB.append(getTOXml("principalAmount", principalAmount));
        strB.append(getTOXml("interestAmount", interestAmount));
        strB.append(getTOXml("penalInterestAmount", penalInterestAmount));
        strB.append(getTOXml("chargeAmount", chargeAmount));
        strB.append(getTOXml("installmentAmt", installmentAmt));
        strB.append(getTOXml("installmentNo", installmentNo));
        strB.append(getTOXml("firstInstallmentDt", firstInstallmentDt));
        strB.append(getTOXml("lastInstallmentDt", lastInstallmentDt));
        strB.append(getTOXml("freq", freq));
        strB.append(getTOXml("repaySingle_YES", repaySingle_YES));
        strB.append(getTOXml("penal", penal));
        strB.append(getTOXml("courtRemarks", courtRemarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("status_dt", status_dt));
        strB.append(getTOXml("authStatus", authStatus));
        strB.append(getTOXml("authDate", authDate));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property courtOrderNo.
     *
     * @return Value of property courtOrderNo.
     */
    public java.lang.String getCourtOrderNo() {
        return courtOrderNo;
    }

    /**
     * Setter for property courtOrderNo.
     *
     * @param courtOrderNo New value of property courtOrderNo.
     */
    public void setCourtOrderNo(java.lang.String courtOrderNo) {
        this.courtOrderNo = courtOrderNo;
    }

    /**
     * Getter for property oTSDate.
     *
     * @return Value of property oTSDate.
     */
    public java.util.Date getOTSDate() {
        return oTSDate;
    }

    /**
     * Setter for property oTSDate.
     *
     * @param oTSDate New value of property oTSDate.
     */
    public void setOTSDate(java.util.Date oTSDate) {
        this.oTSDate = oTSDate;
    }

    /**
     * Getter for property oTSRate.
     *
     * @return Value of property oTSRate.
     */
    public java.lang.Double getOTSRate() {
        return oTSRate;
    }

    /**
     * Setter for property oTSRate.
     *
     * @param oTSRate New value of property oTSRate.
     */
    public void setOTSRate(java.lang.Double oTSRate) {
        this.oTSRate = oTSRate;
    }

    /**
     * Getter for property totAmountDue.
     *
     * @return Value of property totAmountDue.
     */
    public java.lang.Double getTotAmountDue() {
        return totAmountDue;
    }

    /**
     * Setter for property totAmountDue.
     *
     * @param totAmountDue New value of property totAmountDue.
     */
    public void setTotAmountDue(java.lang.Double totAmountDue) {
        this.totAmountDue = totAmountDue;
    }

    /**
     * Getter for property settlementAmt.
     *
     * @return Value of property settlementAmt.
     */
    public java.lang.Double getSettlementAmt() {
        return settlementAmt;
    }

    /**
     * Setter for property settlementAmt.
     *
     * @param settlementAmt New value of property settlementAmt.
     */
    public void setSettlementAmt(java.lang.Double settlementAmt) {
        this.settlementAmt = settlementAmt;
    }

    /**
     * Getter for property principalAmount.
     *
     * @return Value of property principalAmount.
     */
    public java.lang.Double getPrincipalAmount() {
        return principalAmount;
    }

    /**
     * Setter for property principalAmount.
     *
     * @param principalAmount New value of property principalAmount.
     */
    public void setPrincipalAmount(java.lang.Double principalAmount) {
        this.principalAmount = principalAmount;
    }

    /**
     * Getter for property interestAmount.
     *
     * @return Value of property interestAmount.
     */
    public java.lang.Double getInterestAmount() {
        return interestAmount;
    }

    /**
     * Setter for property interestAmount.
     *
     * @param interestAmount New value of property interestAmount.
     */
    public void setInterestAmount(java.lang.Double interestAmount) {
        this.interestAmount = interestAmount;
    }

    /**
     * Getter for property penalInterestAmount.
     *
     * @return Value of property penalInterestAmount.
     */
    public java.lang.Double getPenalInterestAmount() {
        return penalInterestAmount;
    }

    /**
     * Setter for property penalInterestAmount.
     *
     * @param penalInterestAmount New value of property penalInterestAmount.
     */
    public void setPenalInterestAmount(java.lang.Double penalInterestAmount) {
        this.penalInterestAmount = penalInterestAmount;
    }

    /**
     * Getter for property chargeAmount.
     *
     * @return Value of property chargeAmount.
     */
    public java.lang.Double getChargeAmount() {
        return chargeAmount;
    }

    /**
     * Setter for property chargeAmount.
     *
     * @param chargeAmount New value of property chargeAmount.
     */
    public void setChargeAmount(java.lang.Double chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    /**
     * Getter for property installmentAmt.
     *
     * @return Value of property installmentAmt.
     */
    public java.lang.Double getInstallmentAmt() {
        return installmentAmt;
    }

    /**
     * Setter for property installmentAmt.
     *
     * @param installmentAmt New value of property installmentAmt.
     */
    public void setInstallmentAmt(java.lang.Double installmentAmt) {
        this.installmentAmt = installmentAmt;
    }

    /**
     * Getter for property installmentNo.
     *
     * @return Value of property installmentNo.
     */
    public java.lang.String getInstallmentNo() {
        return installmentNo;
    }

    /**
     * Setter for property installmentNo.
     *
     * @param installmentNo New value of property installmentNo.
     */
    public void setInstallmentNo(java.lang.String installmentNo) {
        this.installmentNo = installmentNo;
    }

    /**
     * Getter for property firstInstallmentDt.
     *
     * @return Value of property firstInstallmentDt.
     */
    public java.util.Date getFirstInstallmentDt() {
        return firstInstallmentDt;
    }

    /**
     * Setter for property firstInstallmentDt.
     *
     * @param firstInstallmentDt New value of property firstInstallmentDt.
     */
    public void setFirstInstallmentDt(java.util.Date firstInstallmentDt) {
        this.firstInstallmentDt = firstInstallmentDt;
    }

    /**
     * Getter for property lastInstallmentDt.
     *
     * @return Value of property lastInstallmentDt.
     */
    public java.util.Date getLastInstallmentDt() {
        return lastInstallmentDt;
    }

    /**
     * Setter for property lastInstallmentDt.
     *
     * @param lastInstallmentDt New value of property lastInstallmentDt.
     */
    public void setLastInstallmentDt(java.util.Date lastInstallmentDt) {
        this.lastInstallmentDt = lastInstallmentDt;
    }

    /**
     * Getter for property freq.
     *
     * @return Value of property freq.
     */
    public java.lang.String getFreq() {
        return freq;
    }

    /**
     * Setter for property freq.
     *
     * @param freq New value of property freq.
     */
    public void setFreq(java.lang.String freq) {
        this.freq = freq;
    }

    /**
     * Getter for property repaySingle_YES.
     *
     * @return Value of property repaySingle_YES.
     */
    public java.lang.String getRepaySingle_YES() {
        return repaySingle_YES;
    }

    /**
     * Setter for property repaySingle_YES.
     *
     * @param repaySingle_YES New value of property repaySingle_YES.
     */
    public void setRepaySingle_YES(java.lang.String repaySingle_YES) {
        this.repaySingle_YES = repaySingle_YES;
    }

    /**
     * Getter for property penal.
     *
     * @return Value of property penal.
     */
    public java.lang.Double getPenal() {
        return penal;
    }

    /**
     * Setter for property penal.
     *
     * @param penal New value of property penal.
     */
    public void setPenal(java.lang.Double penal) {
        this.penal = penal;
    }

    /**
     * Getter for property courtRemarks.
     *
     * @return Value of property courtRemarks.
     */
    public java.lang.String getCourtRemarks() {
        return courtRemarks;
    }

    /**
     * Setter for property courtRemarks.
     *
     * @param courtRemarks New value of property courtRemarks.
     */
    public void setCourtRemarks(java.lang.String courtRemarks) {
        this.courtRemarks = courtRemarks;
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
     * Getter for property status_dt.
     *
     * @return Value of property status_dt.
     */
    public java.util.Date getStatus_dt() {
        return status_dt;
    }

    /**
     * Setter for property status_dt.
     *
     * @param status_dt New value of property status_dt.
     */
    public void setStatus_dt(java.util.Date status_dt) {
        this.status_dt = status_dt;
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
     * Setter for property courtOrderDate.
     *
     * @param courtOrderDate New value of property courtOrderDate.
     */
    public void setCourtOrderDate(java.util.Date courtOrderDate) {
        this.courtOrderDate = courtOrderDate;
    }

    /**
     * Getter for property courtOrderDate.
     *
     * @return Value of property courtOrderDate.
     */
    public java.util.Date getCourtOrderDate() {
        return courtOrderDate;
    }

    /**
     * Getter for property slno.
     *
     * @return Value of property slno.
     */
    public java.lang.String getSlno() {
        return slno;
    }

    /**
     * Setter for property slno.
     *
     * @param slno New value of property slno.
     */
    public void setSlno(java.lang.String slno) {
        this.slno = slno;
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
}
