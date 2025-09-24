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
public class LoansSecurityGoldStockTO extends TransferObject implements Serializable {  
    private String acctNum = "";
    private Date asOnDt = null;    
    private Double pledgeAmount = 0.0;   
    private String remarks = "";
    private String branchCode = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String goldStockId = "";
    private String prodId = "";
    private String prodType = "";
    

    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("asOnDt", asOnDt));
        strB.append(getTOString("pledgeAmount", pledgeAmount));
        strB.append(getTOString("remarks", remarks));        
        strB.append(getTOString("branchCode", branchCode));       
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizedStatus));
        strB.append(getTOString("authorizeBy", authorizedBy));
        strB.append(getTOString("authorizeDt", authorizedDt));
        strB.append(getTOString("goldStockId", goldStockId)); 
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("prodType", prodType)); 
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("asOnDt", asOnDt));
        strB.append(getTOXml("pledgeAmount", pledgeAmount));
        strB.append(getTOXml("remarks", remarks));        
        strB.append(getTOXml("branchCode", branchCode));       
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizedStatus));
        strB.append(getTOXml("authorizeBy", authorizedBy));
        strB.append(getTOXml("authorizeDt", authorizedDt));
        strB.append(getTOXml("goldStockId", goldStockId));    
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("prodType", prodType)); 
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public Date getAsOnDt() {
        return asOnDt;
    }

    public void setAsOnDt(Date asOnDt) {
        this.asOnDt = asOnDt;
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

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getGoldStockId() {
        return goldStockId;
    }

    public void setGoldStockId(String goldStockId) {
        this.goldStockId = goldStockId;
    }

    public Double getPledgeAmount() {
        return pledgeAmount;
    }

    public void setPledgeAmount(Double pledgeAmount) {
        this.pledgeAmount = pledgeAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }
  
    
}