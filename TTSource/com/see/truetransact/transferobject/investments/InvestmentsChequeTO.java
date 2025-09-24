/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InvestmentsChequeTO.java
 * 
 * Created on Mon Feb 27 15:31:15 IST 2012
 */
package com.see.truetransact.transferobject.investments;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is INVESTMENT_CHEQUE_DETAILS.
 */
public class InvestmentsChequeTO extends TransferObject implements Serializable {

    private String investmentId = "";
    private String slNo = "";
    private Date issueDt = null;
    private String fromNo = "";
    private String toNo = "";
    private String noOfCheques = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizedStatus = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String txtBranchCode = "";

    /**
     * Setter/Getter for INVESTMENT_ID - table Field
     */
    public void setInvestmentId(String investmentId) {
        this.investmentId = investmentId;
    }

    public String getInvestmentId() {
        return investmentId;
    }

    /**
     * Setter/Getter for SL_NO - table Field
     */
    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getSlNo() {
        return slNo;
    }

    /**
     * Setter/Getter for ISSUE_DT - table Field
     */
    public void setIssueDt(Date issueDt) {
        this.issueDt = issueDt;
    }

    public Date getIssueDt() {
        return issueDt;
    }

    /**
     * Setter/Getter for FROM_NO - table Field
     */
    public void setFromNo(String fromNo) {
        this.fromNo = fromNo;
    }

    public String getFromNo() {
        return fromNo;
    }

    /**
     * Setter/Getter for TO_NO - table Field
     */
    public void setToNo(String toNo) {
        this.toNo = toNo;
    }

    public String getToNo() {
        return toNo;
    }

    /**
     * Setter/Getter for NO_OF_CHEQUES - table Field
     */
    public void setNoOfCheques(String noOfCheques) {
        this.noOfCheques = noOfCheques;
    }

    public String getNoOfCheques() {
        return noOfCheques;
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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    public String getTxtBranchCode() {
        return txtBranchCode;
    }

    public void setTxtBranchCode(String txtBranchCode) {
        this.txtBranchCode = txtBranchCode;
    }

    
    
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
        strB.append(getTOString("investmentId", investmentId));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("issueDt", issueDt));
        strB.append(getTOString("fromNo", fromNo));
        strB.append(getTOString("toNo", toNo));
        strB.append(getTOString("noOfCheques", noOfCheques));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("txtBranchCode", txtBranchCode));        
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("investmentId", investmentId));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("issueDt", issueDt));
        strB.append(getTOXml("fromNo", fromNo));
        strB.append(getTOXml("toNo", toNo));
        strB.append(getTOXml("noOfCheques", noOfCheques));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("txtBranchCode", txtBranchCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}