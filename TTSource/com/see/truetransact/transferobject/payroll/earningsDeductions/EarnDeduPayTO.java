/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EarnDeduPayTO.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.transferobject.payroll.earningsDeductions;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO is PAYMASTER.
 */
public class EarnDeduPayTO extends TransferObject implements Serializable {

    private String employeeId = "";
    private String payCode = "";
    private Double amount = 0.0;
    private String prodType = "";
    private String prodId = "";
    private String accNo = "";
    private Double principal = 0.0;
    private Double interest = 0.0;
    private Double penalInterest = 0.0;
    private Date calcUpto = null;
    private Date fromDate = null;
    private Date toDate = null;
    private String remark = "";
    private String statusBy = "";
    private String createdBy = "";
    private Date createdDate = null;
    private String active = "";
    private String status = "";
    private Date statusDate = null;
    private Integer srlNo = 0;
    private Integer recovryMnth = 0;
    private String employeeName="";
    
    public Integer getRecovryMnth() {
        return recovryMnth;
    }

    public void setRecovryMnth(Integer recovryMnth) {
        this.recovryMnth = recovryMnth;
    }
    
    private String payTrans = "";
    private String payEarndedu="";

    public String getPayEarndedu() {
        return payEarndedu;
    }

    public void setPayEarndedu(String payEarndedu) {
        this.payEarndedu = payEarndedu;
    }
    
    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
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

    public String getPayTrans() {
        return payTrans;
    }

    public void setPayTrans(String payTrans) {
        this.payTrans = payTrans;
    }

    public Integer getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(Integer srlNo) {
        this.srlNo = srlNo;
    }

    

    

    public Date getCalcUpto() {
        return calcUpto;
    }

    public void setCalcUpto(Date calcUpto) {
        this.calcUpto = calcUpto;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
   
    
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
        strB.append(getTOString("employeeId", employeeId));
        strB.append(getTOString("payCode", payCode));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("accNo", accNo));
        strB.append(getTOString("principal", principal));
        strB.append(getTOString("interest", interest));
        strB.append(getTOString("penalInterest", penalInterest));
        strB.append(getTOString("calcUpto", calcUpto));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOString("remark", remark));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDate", createdDate));
        strB.append(getTOString("active", active));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("srlNo", srlNo));
        strB.append(getTOString("recovryMnth", recovryMnth));
        strB.append(getTOString("payTrans", payTrans));
        strB.append(getTOString("payEarndedu",payEarndedu));
        strB.append(getTOString("employeeName",employeeName));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("employeeId", employeeId));
        strB.append(getTOXml("payCode", payCode));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("accNo", accNo));
        strB.append(getTOXml("principal", principal));
        strB.append(getTOXml("interest", interest));
        strB.append(getTOXml("penalInterest", penalInterest));
        strB.append(getTOXml("calcUpto", calcUpto));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXml("remark", remark));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDate", createdDate));
        strB.append(getTOXml("active", active));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("srlNo", srlNo));
        strB.append(getTOXml("recovryMnth", recovryMnth));
        strB.append(getTOXml("payTrans", payTrans));
        strB.append(getTOXml("payEarndedu",payEarndedu));
        strB.append(getTOXml("employeeName",employeeName));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}