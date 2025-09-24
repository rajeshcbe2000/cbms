/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IncrementTO.java
 * 
 * Created on Fri Nov 14 10:00:00 IST 2014
 */
package com.see.truetransact.transferobject.payroll.increment;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 */
public class IncrementTO extends TransferObject implements Serializable {

    private String branchId = "";
    private String remarks = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String initiatedBranch = "";
    private String employeeId = "";
    private String name = "";
    private String designation = "";
    private String scaleId = "";
    private Double presentBasic = 0.0;
    private Date lastIncrDate = null;
    private String newDesig = "";
    private Double newBasicSal = 0.0;
    private Date nextIncrDate = null;
    private Integer numOfIncr = 0;
    private String incrType = "";
    private Date newIncrDate = null;
    private String incrID = "";
    private String payCode = "";
    private String prodType = "";
    private String prodId = "";
    private String accNo = "";
    private Double principal = 0.0;
    private Double penalInterest = 0.0; 
    private Double interest = 0.0;
    private String active = "";
    private Integer serialNo = 0;
    private Double amount = 0.0; 
    private Date calcUpto = null;
    private Date fromDate = null;
    private Date toDate = null;  
    private String incrementCount = "";
    private String versionNo = "";
    private Integer recoveryMonths = 0;
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    /**
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter/Getter for CREATED_BY - table Field
     */
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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
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
     * Setter/Getter for INITIATED_BRANCH - table Field
     */
    public void setInitiatedBranch(String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }

    public String getInitiatedBranch() {
        return initiatedBranch;
    }
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("presentBasic", presentBasic));
        strB.append(getTOString("lastIncrDate", lastIncrDate));
        strB.append(getTOString("newDesig", newDesig));
        strB.append(getTOString("newBasicSal", newBasicSal));
        strB.append(getTOString("nextIncrDate", nextIncrDate));
        strB.append(getTOString("numOfIncr", numOfIncr));
        strB.append(getTOString("incrID", incrID)); 
        strB.append(getTOString("employeeId", employeeId));
        strB.append(getTOString("name", name));
        strB.append(getTOString("designation", designation));
        strB.append(getTOString("incrType", incrType)); 
        strB.append(getTOString("newIncrDate", newIncrDate));
        strB.append(getTOString("payCode", payCode));   
        strB.append(getTOString("prodType", prodType)); 
        strB.append(getTOString("prodId", prodId)); 
        strB.append(getTOString("accNo", accNo)); 
        strB.append(getTOString("principal", principal)); 
        strB.append(getTOString("penalInterest", penalInterest)); 
        strB.append(getTOString("active", active)); 
        strB.append(getTOString("serialNo", serialNo));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("calcUpto", calcUpto));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOString("interest", interest));
         strB.append(getTOString("incrementCount", incrementCount));
        strB.append(getTOString("versionNo", versionNo)); 
        strB.append(getTOString("recoveryMonths", recoveryMonths));
        strB.append(getTOStringEnd());
        return strB.toString();
    }
   
    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXml("presentBasic", presentBasic));
        strB.append(getTOXml("lastIncrDate", lastIncrDate));
        strB.append(getTOXml("newDesig", newDesig));
        strB.append(getTOXml("newBasicSal", newBasicSal));
        strB.append(getTOXml("nextIncrDate", nextIncrDate));
        strB.append(getTOXml("numOfIncr", numOfIncr));
        strB.append(getTOXml("incrID", incrID));
        strB.append(getTOXml("employeeId", employeeId));
        strB.append(getTOXml("name", name));
        strB.append(getTOXml("designation", designation));
        strB.append(getTOXml("incrType", incrType));
        strB.append(getTOXml("newIncrDate", newIncrDate));
        strB.append(getTOXml("payCode", payCode));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("accNo", accNo));
        strB.append(getTOXml("principal", principal));
        strB.append(getTOXml("penalInterest", penalInterest));
        strB.append(getTOXml("active", active));
        strB.append(getTOXml("serialNo", serialNo));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("calcUpto", calcUpto));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXml("interest", interest));
        strB.append(getTOXml("incrementCount", incrementCount));
        strB.append(getTOXml("versionNo", versionNo));  
        strB.append(getTOXml("recoveryMonths", recoveryMonths));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
 
    /**
     * Setter for property agentMachineId.
     *
     * @param agentMachineId New value of property agentMachineId.
     */
    public String getKeyData() {
        setKeyColumns("agentId");
        return incrID;
    }
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScaleId() {
        return scaleId;
    }

    public void setScaleId(String scaleId) {
        this.scaleId = scaleId;
    }

    public Double getPresentBasic() {
        return presentBasic;
    }

    public void setPresentBasic(Double presentBasic) {
        this.presentBasic = presentBasic;
    }

    public Double getNewBasicSal() {
        return newBasicSal;
    }

    public void setNewBasicSal(Double newBasicSal) {
        this.newBasicSal = newBasicSal;
    }


    public String getNewDesig() {
        return newDesig;
    }

    public void setNewDesig(String newDesig) {
        this.newDesig = newDesig;
    }

    public Integer getNumOfIncr() {
        return numOfIncr;
    }

    public void setNumOfIncr(Integer numOfIncr) {
        this.numOfIncr = numOfIncr;
    }



    public String getIncrType() {
        return incrType;
    }

    public void setIncrType(String incrType) {
        this.incrType = incrType;
    }


    public Date getLastIncrDate() {
        return lastIncrDate;
    }

    public void setLastIncrDate(Date lastIncrDate) {
        this.lastIncrDate = lastIncrDate;
    }

    public Date getNewIncrDate() {
        return newIncrDate;
    }

    public void setNewIncrDate(Date newIncrDate) {
        this.newIncrDate = newIncrDate;
    }

    public Date getNextIncrDate() {
        return nextIncrDate;
    }

    public void setNextIncrDate(Date nextIncrDate) {
        this.nextIncrDate = nextIncrDate;
    }

    public String getIncrID() {
        return incrID;
    }

    public void setIncrID(String incrID) {
        this.incrID = incrID;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }


    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }


    public Double getPenalInterest() {
        return penalInterest;
    }

    public void setPenalInterest(Double penalInterest) {
        this.penalInterest = penalInterest;
    }

    public Double getPrincipal() {
        return principal;
    }

    public void setPrincipal(Double principal) {
        this.principal = principal;
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

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }



    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCalcUpto() {
        return calcUpto;
    }

    public void setCalcUpto(Date calcUpto) {
        this.calcUpto = calcUpto;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public String getIncrementCount() {
        return incrementCount;
    }

    public void setIncrementCount(String incrementCount) {
        this.incrementCount = incrementCount;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public Integer getRecoveryMonths() {
        return recoveryMonths;
    }

    public void setRecoveryMonths(Integer recoveryMonths) {
        this.recoveryMonths = recoveryMonths;
    }

    
}