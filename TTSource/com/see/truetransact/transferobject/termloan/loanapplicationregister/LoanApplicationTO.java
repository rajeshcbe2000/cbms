/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanApplicationTO.java
 *
 * Created on 16 September, 2011, 2:24 PM
 */
package com.see.truetransact.transferobject.termloan.loanapplicationregister;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
import java.util.*;

/**
 *
 * @author aravind
 */
public class LoanApplicationTO extends TransferObject implements Serializable {

    private String purposeCode = "";
    private String custId = "";
    private String memId = "";
    private String prodId = "";
    private String memName = "";
    private String  rePayment= "";
    private Integer noOfInstalmnt;
    private Integer moratoriumPeriod;
    private String applNo = "";
    private Date applDt = null;
    private Date applInwrdDt = null;
    private String schemName = "";
    private Double loanAmt = null;
    private String suretyName = "";
    private String remarks = "";
    private String regstrStatus = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String status = "";
    private Date createdDt = null;
    private String authorizeStatus = null;
    // private String currBranName="";
    private String branCode="";
    private String createdBy = "";
    private String isTransaction = "";
    private HashMap _authorizeMap;
    private Integer result;
    private Date fromDt = null;
    private Date dueDt = null;
    private Double totalSalary=0.0;
    private Double eligibleAmt=0.0;
    private Integer  repaymentType=0;
    private Double costOfVehicle=0.0;
    private Double txtInstallmentAmt=0.0;
    
    

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("rePayment", rePayment));
        strB.append(getTOString("noOfInstalmnt", noOfInstalmnt));
        strB.append(getTOString("memId", memId));
        strB.append(getTOString("memName", memName));
        strB.append(getTOString("applNo", applNo));
        strB.append(getTOString("applDt", applDt));
        strB.append(getTOString("applInwrdDt", applInwrdDt));
        strB.append(getTOString("loanAmt", loanAmt));
        strB.append(getTOString("suretyName", suretyName));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("regstrStatus", regstrStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("fromDt", fromDt));
        strB.append(getTOString("dueDt", dueDt));
        strB.append(getTOString("branCode", branCode));
        strB.append(getTOString("moratoriumPeriod", moratoriumPeriod));
        strB.append(getTOString("isTransaction", isTransaction));
        strB.append(getTOString("totalSalary", totalSalary));
        strB.append(getTOString("eligibleAmt", eligibleAmt));
        strB.append(getTOString("repaymentType", repaymentType));
        strB.append(getTOString("costOfVehicle",costOfVehicle));
        strB.append(getTOString("txtInstallmentAmt",txtInstallmentAmt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        //strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("rePayment", rePayment));
        strB.append(getTOXml("noOfInstalmnt",noOfInstalmnt));
        strB.append(getTOXml("memId", memId));
        strB.append(getTOXml("memName", memName));
        strB.append(getTOXml("applNo", applNo));
        strB.append(getTOXml("applDt", applDt));
        strB.append(getTOXml("applInwrdDt", applInwrdDt));
        strB.append(getTOXml("loanAmt", loanAmt));
        strB.append(getTOXml("suretyName", suretyName));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("regstrStatus", regstrStatus));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("fromDt", fromDt));
        strB.append(getTOXml("dueDt", dueDt));
        strB.append(getTOXml("branCode", branCode));
        strB.append(getTOXml("moratoriumPeriod", moratoriumPeriod));
        strB.append(getTOXml("isTransaction", isTransaction));
        strB.append(getTOXml("totalSalary", totalSalary));
        strB.append(getTOXml("eligibleAmt", eligibleAmt));
        strB.append(getTOXml("repaymentType", repaymentType));
        strB.append(getTOXml("costOfVehicle", costOfVehicle));
        strB.append(getTOXml("txtInstallmentAmt",txtInstallmentAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public Double getTxtInstallmentAmt() {
        return txtInstallmentAmt;
    }

    public void setTxtInstallmentAmt(Double txtInstallmentAmt) {
        this.txtInstallmentAmt = txtInstallmentAmt;
    }
   
    
    public Double getCostOfVehicle() {
        return costOfVehicle;
    }

    public void setCostOfVehicle(Double costOfVehicle) {
        this.costOfVehicle = costOfVehicle;
    }
    
    public Double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(Double totalSalary) {
        this.totalSalary = totalSalary;
    }
   
    public String getIsTransaction() {
        return isTransaction;
    }

    public void setIsTransaction(String isTransaction) {
        this.isTransaction = isTransaction;
    }

    public Integer getMoratoriumPeriod() {
        return moratoriumPeriod;
    }

    public void setMoratoriumPeriod(Integer moratoriumPeriod) {
        this.moratoriumPeriod = moratoriumPeriod;
    }

    public Integer getNoOfInstalmnt() {
        return noOfInstalmnt;
    }

    public void setNoOfInstalmnt(Integer noOfInstalmnt) {
        this.noOfInstalmnt = noOfInstalmnt;
    }

    public String getRePayment() {
        return rePayment;
    }

    public void setRePayment(String rePayment) {
        this.rePayment = rePayment;
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
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property createdDt.
     *
     * @param createdDt New value of property createdDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
    }

    /**
     * Getter for property createdBy.
     *
     * @return Value of property createdBy.
     */
    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for property createdBy.
     *
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public Integer getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(Integer result) {
        this.result = result;
    }

    /**
     * Getter for property statusBy.
     *
     * @return Value of property statusBy.
     */
    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    /**
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    /**
     * Getter for property custId.
     *
     * @return Value of property custId.
     */
    public java.lang.String getCustId() {
        return custId;
    }

    /**
     * Setter for property custId.
     *
     * @param custId New value of property custId.
     */
    public void setCustId(java.lang.String custId) {
        this.custId = custId;
    }

    /**
     * Getter for property memId.
     *
     * @return Value of property memId.
     */
    public java.lang.String getMemId() {
        return memId;
    }

    /**
     * Setter for property memId.
     *
     * @param memId New value of property memId.
     */
    public void setMemId(java.lang.String memId) {
        this.memId = memId;
    }

    /**
     * Getter for property memName.
     *
     * @return Value of property memName.
     */
    public java.lang.String getMemName() {
        return memName;
    }

    /**
     * Setter for property memName.
     *
     * @param memName New value of property memName.
     */
    public void setMemName(java.lang.String memName) {
        this.memName = memName;
    }

    /**
     * Getter for property applNo.
     *
     * @return Value of property applNo.
     */
    public java.lang.String getApplNo() {
        return applNo;
    }

    /**
     * Setter for property applNo.
     *
     * @param applNo New value of property applNo.
     */
    public void setApplNo(java.lang.String applNo) {
        this.applNo = applNo;
    }

    /**
     * Getter for property applDt.
     *
     * @return Value of property applDt.
     */
    public java.util.Date getApplDt() {
        return applDt;
    }

    /**
     * Setter for property applDt.
     *
     * @param applDt New value of property applDt.
     */
    public void setApplDt(java.util.Date applDt) {
        this.applDt = applDt;
    }

    /**
     * Getter for property applInwrdDt.
     *
     * @return Value of property applInwrdDt.
     */
    public java.util.Date getApplInwrdDt() {
        return applInwrdDt;
    }

    /**
     * Setter for property applInwrdDt.
     *
     * @param applInwrdDt New value of property applInwrdDt.
     */
    public void setApplInwrdDt(java.util.Date applInwrdDt) {
        this.applInwrdDt = applInwrdDt;
    }

    /**
     * Getter for property schemName.
     *
     * @return Value of property schemName.
     */
    public java.lang.String getSchemName() {
        return schemName;
    }

    /**
     * Setter for property schemName.
     *
     * @param schemName New value of property schemName.
     */
    public void setSchemName(java.lang.String schemName) {
        this.schemName = schemName;
    }

    /**
     * Getter for property suretyName.
     *
     * @return Value of property suretyName.
     */
    public java.lang.String getSuretyName() {
        return suretyName;
    }

    /**
     * Setter for property suretyName.
     *
     * @param suretyName New value of property suretyName.
     */
    public void setSuretyName(java.lang.String suretyName) {
        this.suretyName = suretyName;
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
     * Getter for property regstrStatus.
     *
     * @return Value of property regstrStatus.
     */
    public java.lang.String getRegstrStatus() {
        return regstrStatus;
    }

    /**
     * Setter for property regstrStatus.
     *
     * @param regstrStatus New value of property regstrStatus.
     */
    public void setRegstrStatus(java.lang.String regstrStatus) {
        this.regstrStatus = regstrStatus;
    }

    /**
     * Getter for property loanAmt.
     *
     * @return Value of property loanAmt.
     */
    public Double getLoanAmt() {
        return loanAmt;
    }

    /**
     * Setter for property loanAmt.
     *
     * @param loanAmt New value of property loanAmt.
     */
    public void setLoanAmt(Double loanAmt) {
        this.loanAmt = loanAmt;
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
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    /**
     * Getter for property fromDt.
     *
     * @return Value of property fromDt.
     */
    public Date getFromDt() {
        return fromDt;
    }

    /**
     * Setter for property fromDt.
     *
     * @param fromDt New value of property fromDt.
     */
    public void setFromDt(Date fromDt) {
        this.fromDt = fromDt;
    }

    /**
     * Getter for property dueDt.
     *
     * @return Value of property dueDt.
     */
    public Date getDueDt() {
        return dueDt;
    }

    /**
     * Setter for property dueDt.
     *
     * @param dueDt New value of property dueDt.
     */
    public void setDueDt(Date dueDt) {
        this.dueDt = dueDt;
    }

    public String getPurposeCode() {
        return purposeCode;
    }

    public void setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public String getBranCode() {
        return branCode;
    }

    public void setBranCode(String branCode) {
        this.branCode = branCode;
    }

    public Double getEligibleAmt() {
        return eligibleAmt;
    }

    public void setEligibleAmt(Double eligibleAmt) {
        this.eligibleAmt = eligibleAmt;
    }

    public Integer getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(Integer repaymentType) {
        this.repaymentType = repaymentType;
    }
    
}
