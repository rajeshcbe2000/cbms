/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceIssueTO.java
 *
 * Created on Fri Feb 10 12:33:13 PDT 2015
 */
package com.see.truetransact.transferobject.remittance.rtgs;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is RTGS_REMIT_ISSUE.
 */
/**
 *
 * @author Suresh R
 */
public class RtgsRemittanceTO extends TransferObject implements Serializable {

    private String rtgs_ID = "";
    private String rtgs_Unique_ID = "";
    private String batchId = "";
    private Date batchDt = null;
    private String prodId = "";
    private String ifsc_Code = "";
    private String beneficiary_Bank = "";
    private String beneficiary_Bank_Name = "";
    private String beneficiary_Branch = "";
    private String beneficiary_Branch_Name = "";
    private String beneficiary_IFSC_Code = "";
    private String beneficiary_Name = "";
    private Double amount = null;
    private String account_No = "";
    private String remarks = "";
    private Double ex_Calculated = null;
    private Double ex_Collected = null;
    private Double service_Tax = null;
    private Double charges = null;
    private Double totalAmt = null;
    private String trans_type = "";
    private String slNo = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String sequnceNo="";
    private String utrNumber="";
    private String fileStatus="";
    private Date n06processDt=null;
    private Date f27ProcessDt=null;
    private Date n09ProcessDt=null;
    private Date n10ProcessDt=null;
    private String debitAcNum="";
    private String debitProdId="";
    private String debitProdType="";
    private Double cgstAmt = 0.0;
    private Double sgstAmt = 0.0;
    private Double totalGstAmt = 0.0;
   

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
        strB.append(getTOString("rtgs_ID", rtgs_ID));
        strB.append(getTOString("rtgs_Unique_ID", rtgs_Unique_ID));
        strB.append(getTOString("batchId", batchId));
        strB.append(getTOString("batchDt", batchDt));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("ifsc_Code", ifsc_Code));
        strB.append(getTOString("beneficiary_Bank", beneficiary_Bank));
        strB.append(getTOString("beneficiary_Branch", beneficiary_Branch));
        strB.append(getTOString("beneficiary_Branch_Name", beneficiary_Branch_Name));
        strB.append(getTOString("beneficiary_IFSC_Code", beneficiary_IFSC_Code));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("account_No", account_No));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("ex_Calculated", ex_Calculated));
        strB.append(getTOString("ex_Collected", ex_Collected));
        strB.append(getTOString("service_Tax", service_Tax));
        strB.append(getTOString("charges", charges));
        strB.append(getTOString("totalAmt", totalAmt));
        strB.append(getTOString("beneficiary_Bank_Name", beneficiary_Bank_Name));
        strB.append(getTOString("beneficiary_Name", beneficiary_Name));
        strB.append(getTOString("trans_type", trans_type));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("sequnceNo", sequnceNo));
        strB.append(getTOString("utrNumber", utrNumber));
        strB.append(getTOString("fileStatus", fileStatus));
        strB.append(getTOString("n06processDt", n06processDt));
        strB.append(getTOString("f27ProcessDt", f27ProcessDt));
        strB.append(getTOString("n09ProcessDt", n09ProcessDt));
        strB.append(getTOString("n10ProcessDt", n10ProcessDt));
        strB.append(getTOString("debitAcNum", debitAcNum));
        strB.append(getTOString("debitProdId", debitProdId));
        strB.append(getTOString("debitProdType", debitProdType));
        strB.append(getTOString("sgstAmt", sgstAmt));
        strB.append(getTOString("cgstAmt", cgstAmt));
        strB.append(getTOString("totalGstAmt", totalGstAmt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("rtgs_ID", rtgs_ID));
        strB.append(getTOXml("rtgs_Unique_ID", rtgs_Unique_ID));
        strB.append(getTOXml("batchId", batchId));
        strB.append(getTOXml("batchDt", batchDt));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("ifsc_Code", ifsc_Code));
        strB.append(getTOXml("beneficiary_Bank", beneficiary_Bank));
        strB.append(getTOXml("beneficiary_Branch", beneficiary_Branch));
        strB.append(getTOXml("beneficiary_Branch_Name", beneficiary_Branch_Name));
        strB.append(getTOXml("beneficiary_IFSC_Code", beneficiary_IFSC_Code));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("account_No", account_No));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("ex_Calculated", ex_Calculated));
        strB.append(getTOXml("ex_Collected", ex_Collected));
        strB.append(getTOXml("service_Tax", service_Tax));
        strB.append(getTOXml("charges", charges));
        strB.append(getTOXml("totalAmt", totalAmt));
        strB.append(getTOXml("beneficiary_Bank_Name", beneficiary_Bank_Name));
        strB.append(getTOXml("beneficiary_Name", beneficiary_Name));
        strB.append(getTOXml("trans_type", trans_type));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("sequnceNo", sequnceNo));
        strB.append(getTOXml("utrNumber", utrNumber));
        strB.append(getTOXml("fileStatus", fileStatus));
        strB.append(getTOXml("n06processDt", n06processDt));
        strB.append(getTOXml("f27ProcessDt", f27ProcessDt));
        strB.append(getTOXml("n09ProcessDt", n09ProcessDt));
        strB.append(getTOXml("n10ProcessDt", n10ProcessDt));
        strB.append(getTOXml("debitAcNum", debitAcNum));
        strB.append(getTOXml("debitProdId", debitProdId));
        strB.append(getTOXml("debitProdType", debitProdType));
        strB.append(getTOXml("sgstAmt", sgstAmt));
        strB.append(getTOXml("cgstAmt", cgstAmt));
        strB.append(getTOXml("totalGstAmt", totalGstAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getRtgs_ID() {
        return rtgs_ID;
    }

    public void setRtgs_ID(String rtgs_ID) {
        this.rtgs_ID = rtgs_ID;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Date getBatchDt() {
        return batchDt;
    }

    public void setBatchDt(Date batchDt) {
        this.batchDt = batchDt;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getIfsc_Code() {
        return ifsc_Code;
    }

    public void setIfsc_Code(String ifsc_Code) {
        this.ifsc_Code = ifsc_Code;
    }

    public String getBeneficiary_Bank() {
        return beneficiary_Bank;
    }

    public void setBeneficiary_Bank(String beneficiary_Bank) {
        this.beneficiary_Bank = beneficiary_Bank;
    }

    public String getBeneficiary_Branch() {
        return beneficiary_Branch;
    }

    public void setBeneficiary_Branch(String beneficiary_Branch) {
        this.beneficiary_Branch = beneficiary_Branch;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAccount_No() {
        return account_No;
    }

    public void setAccount_No(String account_No) {
        this.account_No = account_No;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Double getEx_Calculated() {
        return ex_Calculated;
    }

    public void setEx_Calculated(Double ex_Calculated) {
        this.ex_Calculated = ex_Calculated;
    }

    public Double getEx_Collected() {
        return ex_Collected;
    }

    public void setEx_Collected(Double ex_Collected) {
        this.ex_Collected = ex_Collected;
    }

    public Double getService_Tax() {
        return service_Tax;
    }

    public void setService_Tax(Double service_Tax) {
        this.service_Tax = service_Tax;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(String trans_type) {
        this.trans_type = trans_type;
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

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getBeneficiary_Bank_Name() {
        return beneficiary_Bank_Name;
    }

    public void setBeneficiary_Bank_Name(String beneficiary_Bank_Name) {
        this.beneficiary_Bank_Name = beneficiary_Bank_Name;
    }

    public String getBeneficiary_Branch_Name() {
        return beneficiary_Branch_Name;
    }

    public void setBeneficiary_Branch_Name(String beneficiary_Branch_Name) {
        this.beneficiary_Branch_Name = beneficiary_Branch_Name;
    }

    public String getBeneficiary_IFSC_Code() {
        return beneficiary_IFSC_Code;
    }

    public void setBeneficiary_IFSC_Code(String beneficiary_IFSC_Code) {
        this.beneficiary_IFSC_Code = beneficiary_IFSC_Code;
    }

    public String getBeneficiary_Name() {
        return beneficiary_Name;
    }

    public void setBeneficiary_Name(String beneficiary_Name) {
        this.beneficiary_Name = beneficiary_Name;
    }

    public Double getCharges() {
        return charges;
    }

    public void setCharges(Double charges) {
        this.charges = charges;
    }

    public String getSequnceNo() {
        return sequnceNo;
    }

    public void setSequnceNo(String sequnceNo) {
        this.sequnceNo = sequnceNo;
    }

    public String getUtrNumber() {
        return utrNumber;
    }

    public void setUtrNumber(String utrNumber) {
        this.utrNumber = utrNumber;
    }


    public Date getF27ProcessDt() {
        return f27ProcessDt;
    }

    public void setF27ProcessDt(Date f27ProcessDt) {
        this.f27ProcessDt = f27ProcessDt;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Date getN06processDt() {
        return n06processDt;
    }

    public void setN06processDt(Date n06processDt) {
        this.n06processDt = n06processDt;
    }

    public Date getN09ProcessDt() {
        return n09ProcessDt;
    }

    public void setN09ProcessDt(Date n09ProcessDt) {
        this.n09ProcessDt = n09ProcessDt;
    }

    public Date getN10ProcessDt() {
        return n10ProcessDt;
    }

    public void setN10ProcessDt(Date n10ProcessDt) {
        this.n10ProcessDt = n10ProcessDt;
    }

    public String getRtgs_Unique_ID() {
        return rtgs_Unique_ID;
    }

    public void setRtgs_Unique_ID(String rtgs_Unique_ID) {
        this.rtgs_Unique_ID = rtgs_Unique_ID;
    }

    public String getDebitAcNum() {
        return debitAcNum;
    }

    public void setDebitAcNum(String debitAcNum) {
        this.debitAcNum = debitAcNum;
    }

    public String getDebitProdId() {
        return debitProdId;
    }

    public void setDebitProdId(String debitProdId) {
        this.debitProdId = debitProdId;
    }

    public String getDebitProdType() {
        return debitProdType;
    }

    public void setDebitProdType(String debitProdType) {
        this.debitProdType = debitProdType;
    }

    public Double getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(Double cgstAmt) {
        this.cgstAmt = cgstAmt;
    }

    public Double getSgstAmt() {
        return sgstAmt;
    }

    public void setSgstAmt(Double sgstAmt) {
        this.sgstAmt = sgstAmt;
    }

    public Double getTotalGstAmt() {
        return totalGstAmt;
    }

    public void setTotalGstAmt(Double totalGstAmt) {
        this.totalGstAmt = totalGstAmt;
    }
    
}
