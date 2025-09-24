/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SHGTO.java
 * 
 * Created on Sat Oct 15 14:06:54 IST 2011
 */
package com.see.truetransact.transferobject.termloan.groupLoan;

import com.see.truetransact.transferobject.termloan.SHG.*;
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHG_MEMBER_DETAILS.
 */
public class GroupLoanTO extends TransferObject implements Serializable {

//    private String custId = "";

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPenal() {
        return penal;
    }

    public void setPenal(String penal) {
        this.penal = penal;
    }

    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
    
    private String custId = "";
    private String transType = "";
    private String payment = "";
    private String principle = "";
    private String interest = "";
    private String penal = "";
    private String charges = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private Date transDt = null;
    private String receipt = "";
    private String grpLoanNo = "";
    private Date repayDt = null;
    private String transId = "";
    private String narration= "";
    private String branchId = "";

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    
    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }
    private String slNo = "";

    public Date getRepayDt() {
        return repayDt;
    }

    public void setRepayDt(Date repayDt) {
        this.repayDt = repayDt;
    }

    public String getGrpLoanNo() {
        return grpLoanNo;
    }

    public void setGrpLoanNo(String grpLoanNo) {
        this.grpLoanNo = grpLoanNo;
    }
    
    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }
    
    public Date getTransDt() {
        return transDt;
    }

    public void setTransDt(Date transDt) {
        this.transDt = transDt;
    }
    /**
     * Setter/Getter for SHG_ID - table Field
     */
   

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
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("payment", payment));
        strB.append(getTOString("principle", principle));
        strB.append(getTOString("interest", interest));
        strB.append(getTOString("penal", penal));
        strB.append(getTOString("charges", charges));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("receipt", receipt));
        strB.append(getTOString("grpLoanNo", grpLoanNo));
        strB.append(getTOString("repayDt", repayDt));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("narration", narration));
		strB.append(getTOString("branchId", branchId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("payment", payment));
        strB.append(getTOXml("principle", principle));
        strB.append(getTOXml("interest", interest));
        strB.append(getTOXml("penal", penal));
        strB.append(getTOXml("charges", charges));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("receipt", receipt));
        strB.append(getTOXml("grpLoanNo", grpLoanNo));
        strB.append(getTOXml("repayDt", repayDt));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("narration", narration));
		strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property groupName.
     *
     * @return Value of property groupName.
     */
   
}