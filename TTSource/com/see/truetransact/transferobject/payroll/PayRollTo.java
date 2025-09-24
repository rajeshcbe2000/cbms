/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PFTransferTO.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.transferobject.payroll;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO is PayRoll_individual.
 */
public class PayRollTo extends TransferObject implements Serializable {

    private String employeeId = "";
    private Date month = null;
    private Double amount = 0.0;
    private String payType = "";
    private Integer scaleId = 0;
    private Integer versionNO = 0;
    private Integer srlNo = 0;
    private String transId = "";
    private String batchId = "";
    private String payCode = "";
    private Integer paySlNo = 0;
    private String payDesc = "";
    private String prodId = "";
    private String prodType = "";
    private String acct_num = "";
    private Double pricipal = 0.0;
    private Double interest = 0.0;
    private Double penal = 0.0;
    private Date transDt = null;
    private Date calUpto = null;
    private Date fromDt = null;
    private Date toDt = null;
    private String remarks = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String status = "";
    private String statusBy = "";
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private String payrollId="";
    private String payEarndedu="";
    private String payModuleType="";
    private String accHd="";
    private String accType="";
    private String employeeName="";
    private String transType="";

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }
    

    public String getAccHd() {
        return accHd;
    }

    public void setAccHd(String accHd) {
        this.accHd = accHd;
    }
    
    public String getPayModuleType() {
        return payModuleType;
    }

    public void setPayModuleType(String payModuleType) {
        this.payModuleType = payModuleType;
    }
    
    public String getPayEarndedu() {
        return payEarndedu;
    }

    public void setPayEarndedu(String payEarndedu) {
        this.payEarndedu = payEarndedu;
    }

    public String getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(String payrollId) {
        this.payrollId = payrollId;
    }
    
    
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAcct_num() {
        return acct_num;
    }

    public void setAcct_num(String acct_num) {
        this.acct_num = acct_num;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Date getCalUpto() {
        return calUpto;
    }

    public void setCalUpto(Date calUpto) {
        this.calUpto = calUpto;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Date getFromDt() {
        return fromDt;
    }

    public void setFromDt(Date fromDt) {
        this.fromDt = fromDt;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPayDesc() {
        return payDesc;
    }

    public void setPayDesc(String payDesc) {
        this.payDesc = payDesc;
    }

    

    public String getPayType() {
        return payType;
    }

    public Integer getScaleId() {
        return scaleId;
    }

    public void setScaleId(Integer scaleId) {
        this.scaleId = scaleId;
    }

    public Integer getVersionNO() {
        return versionNO;
    }

    public void setVersionNO(Integer versionNO) {
        this.versionNO = versionNO;
    }

    public Integer getPaySlNo() {
        return paySlNo;
    }

    public void setPaySlNo(Integer paySlNo) {
        this.paySlNo = paySlNo;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Double getPenal() {
        return penal;
    }

    public void setPenal(Double penal) {
        this.penal = penal;
    }

    public Double getPricipal() {
        return pricipal;
    }

    public void setPricipal(Double pricipal) {
        this.pricipal = pricipal;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(Integer srlNo) {
        this.srlNo = srlNo;
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

    public Date getToDt() {
        return toDt;
    }

    public void setToDt(Date toDt) {
        this.toDt = toDt;
    }

    public Date getTransDt() {
        return transDt;
    }

    public void setTransDt(Date transDt) {
        this.transDt = transDt;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
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
        strB.append(getTOString("month", month));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("payType", payType));
        strB.append(getTOString("scaleId", scaleId));
        strB.append(getTOString("versionNO", versionNO));
        strB.append(getTOString("srlNo", srlNo));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("batchId", batchId));
        strB.append(getTOString("payCode", payCode));
        strB.append(getTOString("paySlNo", paySlNo));
        strB.append(getTOString("payDesc", payDesc));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("acct_num", acct_num));
        strB.append(getTOString("pricipal", pricipal));
        strB.append(getTOString("interest", interest));
        strB.append(getTOString("penal", penal));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("calUpto", calUpto));
        strB.append(getTOString("fromDt", fromDt));
        strB.append(getTOString("toDt", toDt));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("payrollId", payrollId));
        strB.append(getTOString("payEarndedu", payEarndedu));
        strB.append(getTOString("payModuleType", payModuleType));
        strB.append(getTOString("accHd", accHd));
        strB.append(getTOString("accType", accType));
        strB.append(getTOString("employeeName", employeeName));
        strB.append(getTOString("transType", transType));
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
        strB.append(getTOXml("month", month));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("payType", payType));
        strB.append(getTOXml("scaleId", scaleId));
        strB.append(getTOXml("versionNO", versionNO));
        strB.append(getTOXml("srlNo", srlNo));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("batchId", batchId));
        strB.append(getTOXml("payCode", payCode));
        strB.append(getTOXml("paySlNo", paySlNo));
        strB.append(getTOXml("payDesc", payDesc));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("acct_num", acct_num));
        strB.append(getTOXml("pricipal", pricipal));
        strB.append(getTOXml("interest", interest));
        strB.append(getTOXml("penal", penal));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("calUpto", calUpto));
        strB.append(getTOXml("fromDt", fromDt));
        strB.append(getTOXml("toDt", toDt));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("payrollId", payrollId));
        strB.append(getTOXml("payEarndedu", payEarndedu));
        strB.append(getTOXml("payModuleType", payModuleType));
        strB.append(getTOXml("accHd", accHd));
        strB.append(getTOXml("accType", accType));
        strB.append(getTOXml("employeeName", employeeName));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}