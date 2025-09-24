/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigTO.java
 *
 * Created on Thu Jan 20 16:44:08 IST 2005
 */
package com.see.truetransact.transferobject.servicetax.servicetaxdetails;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class ServiceTaxDetailsTO extends TransferObject implements Serializable {

    private String serviceTaxDet_Id = "";
    private String acct_Num = "";
    private String particulars = "";
    private Double serviceTaxAmt=0.0;
    private Double educationCess=0.0;
    private Double higherCess=0.0;
    private Double totalTaxAmt=0.0;
    private String roundVal= "";
    private String branchID = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String authorizedBy = "";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String trans_type = "C";
    private Double swachhCess = 0.0;
    private Double krishiKalyan = 0.0;
    
    
    public String getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(String trans_type) {
        this.trans_type = trans_type;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
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
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("serviceTaxDet_Id", serviceTaxDet_Id));
        strB.append(getTOString("acct_Num", acct_Num));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("branchID", branchID));
        strB.append(getTOString("serviceTaxAmt", serviceTaxAmt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("educationCess", educationCess));
        strB.append(getTOString("higherCess", higherCess));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("totalTaxAmt", totalTaxAmt));
        strB.append(getTOString("roundVal", roundVal));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("trans_type", trans_type));
        strB.append(getTOString("swachhCess", swachhCess));
        strB.append(getTOString("krishiKalyan", krishiKalyan));   
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("serviceTaxDet_Id", serviceTaxDet_Id));
        strB.append(getTOXml("acct_Num", acct_Num));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("branchID", branchID));
        strB.append(getTOXml("serviceTaxAmt", serviceTaxAmt));
        strB.append(getTOXml("educationCess", educationCess));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("higherCess", higherCess));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("totalTaxAmt", totalTaxAmt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("roundVal", roundVal));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("trans_type", trans_type));
        strB.append(getTOXml("swachhCess", swachhCess));
        strB.append(getTOXml("krishiKalyan", krishiKalyan));               
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getAcct_Num() {
        return acct_Num;
    }

    public void setAcct_Num(String acct_Num) {
        this.acct_Num = acct_Num;
    }

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

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Double getEducationCess() {
        return educationCess;
    }

    public void setEducationCess(Double educationCess) {
        this.educationCess = educationCess;
    }

    public Double getHigherCess() {
        return higherCess;
    }

    public void setHigherCess(Double higherCess) {
        this.higherCess = higherCess;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getRoundVal() {
        return roundVal;
    }

    public void setRoundVal(String roundVal) {
        this.roundVal = roundVal;
    }

    public Double getServiceTaxAmt() {
        return serviceTaxAmt;
    }

    public void setServiceTaxAmt(Double serviceTaxAmt) {
        this.serviceTaxAmt = serviceTaxAmt;
    }

    public String getServiceTaxDet_Id() {
        return serviceTaxDet_Id;
    }

    public void setServiceTaxDet_Id(String serviceTaxDet_Id) {
        this.serviceTaxDet_Id = serviceTaxDet_Id;
    }

    public Double getTotalTaxAmt() {
        return totalTaxAmt;
    }

    public void setTotalTaxAmt(Double totalTaxAmt) {
        this.totalTaxAmt = totalTaxAmt;
    }

    public Double getKrishiKalyan() {
        return krishiKalyan;
    }

    public void setKrishiKalyan(Double krishiKalyan) {
        this.krishiKalyan = krishiKalyan;
    }

    public Double getSwachhCess() {
        return swachhCess;
    }

    public void setSwachhCess(Double swachhCess) {
        this.swachhCess = swachhCess;
    }
    
    
}