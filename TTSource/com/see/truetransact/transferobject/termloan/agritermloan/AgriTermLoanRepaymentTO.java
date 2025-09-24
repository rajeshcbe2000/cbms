/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanRepaymentTO.java
 * 
 * Created on Tue Feb 08 14:45:15 IST 2005
 */
package com.see.truetransact.transferobject.termloan.agritermloan;
/*
 *
 * @author shanmugavel
 *
 */

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_REPAY_SCHEDULE.
 */
public class AgriTermLoanRepaymentTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String prodId = "";
    private Double scheduleNo = null;
    private Double loanAmount = null;
    private String intType = "";
    private String installType = "";
    private Double repaymentType = null;
    private Double noInstallments = null;
    private Date firstInstallDt = null;
    private Date lastInstallDt = null;
    private Double totalBaseAmt = null;
    private Double balanceLoanAmt = null;
    private Double amtPenultimateInstall = null;
    private Double emi = null;
    private Double amtLastInstall = null;
    private Double repaymentPr = null;
    private Double totalInstallAmt = null;
    private Double repayInterest = null;
    private String addSi = "";
    private String postDateChqallowed = "";
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String acctNum = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String repayActive = "";
    private String command = "";
    private Double disbursementId = null;
    private Date disbursementDt = null;
    private String scheduleMode = "";
    private Double refScheduleNo = null;

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
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
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for SCHEDULE_NO - table Field
     */
    public void setScheduleNo(Double scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public Double getScheduleNo() {
        return scheduleNo;
    }

    /**
     * Setter/Getter for LOAN_AMOUNT - table Field
     */
    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    /**
     * Setter/Getter for INT_TYPE - table Field
     */
    public void setIntType(String intType) {
        this.intType = intType;
    }

    public String getIntType() {
        return intType;
    }

    /**
     * Setter/Getter for INSTALL_TYPE - table Field
     */
    public void setInstallType(String installType) {
        this.installType = installType;
    }

    public String getInstallType() {
        return installType;
    }

    /**
     * Setter/Getter for REPAYMENT_TYPE - table Field
     */
    public void setRepaymentType(Double repaymentType) {
        this.repaymentType = repaymentType;
    }

    public Double getRepaymentType() {
        return repaymentType;
    }

    /**
     * Setter/Getter for NO_INSTALLMENTS - table Field
     */
    public void setNoInstallments(Double noInstallments) {
        this.noInstallments = noInstallments;
    }

    public Double getNoInstallments() {
        return noInstallments;
    }

    /**
     * Setter/Getter for FIRST_INSTALL_DT - table Field
     */
    public void setFirstInstallDt(Date firstInstallDt) {
        this.firstInstallDt = firstInstallDt;
    }

    public Date getFirstInstallDt() {
        return firstInstallDt;
    }

    /**
     * Setter/Getter for LAST_INSTALL_DT - table Field
     */
    public void setLastInstallDt(Date lastInstallDt) {
        this.lastInstallDt = lastInstallDt;
    }

    public Date getLastInstallDt() {
        return lastInstallDt;
    }

    /**
     * Setter/Getter for TOTAL_BASE_AMT - table Field
     */
    public void setTotalBaseAmt(Double totalBaseAmt) {
        this.totalBaseAmt = totalBaseAmt;
    }

    public Double getTotalBaseAmt() {
        return totalBaseAmt;
    }

    /**
     * Setter/Getter for BALANCE_LOAN_AMT - table Field
     */
    public void setBalanceLoanAmt(Double balanceLoanAmt) {
        this.balanceLoanAmt = balanceLoanAmt;
    }

    public Double getBalanceLoanAmt() {
        return balanceLoanAmt;
    }

    /**
     * Setter/Getter for AMT_PENULTIMATE_INSTALL - table Field
     */
    public void setAmtPenultimateInstall(Double amtPenultimateInstall) {
        this.amtPenultimateInstall = amtPenultimateInstall;
    }

    public Double getAmtPenultimateInstall() {
        return amtPenultimateInstall;
    }

    /**
     * Setter/Getter for EMI - table Field
     */
    public void setEmi(Double emi) {
        this.emi = emi;
    }

    public Double getEmi() {
        return emi;
    }

    /**
     * Setter/Getter for AMT_LAST_INSTALL - table Field
     */
    public void setAmtLastInstall(Double amtLastInstall) {
        this.amtLastInstall = amtLastInstall;
    }

    public Double getAmtLastInstall() {
        return amtLastInstall;
    }

    /**
     * Setter/Getter for REPAYMENT_PR - table Field
     */
    public void setRepaymentPr(Double repaymentPr) {
        this.repaymentPr = repaymentPr;
    }

    public Double getRepaymentPr() {
        return repaymentPr;
    }

    /**
     * Setter/Getter for TOTAL_INSTALL_AMT - table Field
     */
    public void setTotalInstallAmt(Double totalInstallAmt) {
        this.totalInstallAmt = totalInstallAmt;
    }

    public Double getTotalInstallAmt() {
        return totalInstallAmt;
    }

    /**
     * Setter/Getter for REPAY_INTEREST - table Field
     */
    public void setRepayInterest(Double repayInterest) {
        this.repayInterest = repayInterest;
    }

    public Double getRepayInterest() {
        return repayInterest;
    }

    /**
     * Setter/Getter for ADD_SI - table Field
     */
    public void setAddSi(String addSi) {
        this.addSi = addSi;
    }

    public String getAddSi() {
        return addSi;
    }

    /**
     * Setter/Getter for POST_DATE_CHQALLOWED - table Field
     */
    public void setPostDateChqallowed(String postDateChqallowed) {
        this.postDateChqallowed = postDateChqallowed;
    }

    public String getPostDateChqallowed() {
        return postDateChqallowed;
    }

    /**
     * Setter/Getter for AUTHORIZE_REMARKS - table Field
     */
    public void setAuthorizeRemarks(String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    public String getAuthorizeRemarks() {
        return authorizeRemarks;
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
     * Setter/Getter for ACCT_NUM - table Field
     */
    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAcctNum() {
        return acctNum;
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

    /**
     * Setter/Getter for REPAY_ACTIVE - table Field
     */
    public void setRepayActive(String repayActive) {
        this.repayActive = repayActive;
    }

    public String getRepayActive() {
        return repayActive;
    }

    /**
     * Setter/Getter for DISBURSEMENT_ID - table Field
     */
    public void setDisbursementId(Double disbursementId) {
        this.disbursementId = disbursementId;
    }

    public Double getDisbursementId() {
        return disbursementId;
    }

    /**
     * Setter/Getter for DISBURSEMENT_DT - table Field
     */
    public void setDisbursementDt(Date disbursementDt) {
        this.disbursementDt = disbursementDt;
    }

    public Date getDisbursementDt() {
        return disbursementDt;
    }

    /**
     * Setter/Getter for SCHEDULE_MODE - table Field
     */
    public void setScheduleMode(String scheduleMode) {
        this.scheduleMode = scheduleMode;
    }

    public String getScheduleMode() {
        return scheduleMode;
    }

    /**
     * Setter/Getter for REF_SCHEDULE_NO - table Field
     */
    public void setRefScheduleNo(Double refScheduleNo) {
        this.refScheduleNo = refScheduleNo;
    }

    public Double getRefScheduleNo() {
        return refScheduleNo;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("acctNum" + KEY_VAL_SEPARATOR + "scheduleNo");
        return acctNum + KEY_VAL_SEPARATOR + scheduleNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("scheduleNo", scheduleNo));
        strB.append(getTOString("loanAmount", loanAmount));
        strB.append(getTOString("intType", intType));
        strB.append(getTOString("installType", installType));
        strB.append(getTOString("repaymentType", repaymentType));
        strB.append(getTOString("noInstallments", noInstallments));
        strB.append(getTOString("firstInstallDt", firstInstallDt));
        strB.append(getTOString("lastInstallDt", lastInstallDt));
        strB.append(getTOString("totalBaseAmt", totalBaseAmt));
        strB.append(getTOString("balanceLoanAmt", balanceLoanAmt));
        strB.append(getTOString("amtPenultimateInstall", amtPenultimateInstall));
        strB.append(getTOString("emi", emi));
        strB.append(getTOString("amtLastInstall", amtLastInstall));
        strB.append(getTOString("repaymentPr", repaymentPr));
        strB.append(getTOString("totalInstallAmt", totalInstallAmt));
        strB.append(getTOString("repayInterest", repayInterest));
        strB.append(getTOString("addSi", addSi));
        strB.append(getTOString("postDateChqallowed", postDateChqallowed));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("repayActive", repayActive));
        strB.append(getTOString("disbursementId", disbursementId));
        strB.append(getTOString("disbursementDt", disbursementDt));
        strB.append(getTOString("scheduleMode", scheduleMode));
        strB.append(getTOString("refScheduleNo", refScheduleNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("borrowNo", borrowNo));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("scheduleNo", scheduleNo));
        strB.append(getTOXml("loanAmount", loanAmount));
        strB.append(getTOXml("intType", intType));
        strB.append(getTOXml("installType", installType));
        strB.append(getTOXml("repaymentType", repaymentType));
        strB.append(getTOXml("noInstallments", noInstallments));
        strB.append(getTOXml("firstInstallDt", firstInstallDt));
        strB.append(getTOXml("lastInstallDt", lastInstallDt));
        strB.append(getTOXml("totalBaseAmt", totalBaseAmt));
        strB.append(getTOXml("balanceLoanAmt", balanceLoanAmt));
        strB.append(getTOXml("amtPenultimateInstall", amtPenultimateInstall));
        strB.append(getTOXml("emi", emi));
        strB.append(getTOXml("amtLastInstall", amtLastInstall));
        strB.append(getTOXml("repaymentPr", repaymentPr));
        strB.append(getTOXml("totalInstallAmt", totalInstallAmt));
        strB.append(getTOXml("repayInterest", repayInterest));
        strB.append(getTOXml("addSi", addSi));
        strB.append(getTOXml("postDateChqallowed", postDateChqallowed));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("repayActive", repayActive));
        strB.append(getTOXml("disbursementId", disbursementId));
        strB.append(getTOXml("disbursementDt", disbursementDt));
        strB.append(getTOXml("scheduleMode", scheduleMode));
        strB.append(getTOXml("refScheduleNo", refScheduleNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}