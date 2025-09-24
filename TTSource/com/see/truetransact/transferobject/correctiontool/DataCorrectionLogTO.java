/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanSanctionFacilityTO.java
 * @author shanmugavel
 * Created on Mon May 10 12:48:39 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.correctiontool;

import com.see.truetransact.transferobject.termloan.*;
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_SANCTION_DETAILS.
 */
public class DataCorrectionLogTO extends TransferObject implements Serializable {

  
    private String branchCode = "";
    private Date correctionDt =  null;
    private Date transDt = null;
    private String transId = "";
    private String batchId = "";
    private String modificationType = "";
    private String oldFieldValue = "";
    private String newFieldValue = "";
    private String oldStatusBy = "";
    private String oldAuthorizedBy = "";
    private String correctionUserId = "";
    private String correctionAuthorizeStaff = "";
    private Date correctionAuthorizeDt = null;
    private String transMode = "";
    private String actNum = "";
    private String prodType = "";
    private String remarks = "";
    private String transType = "";
    private Double amount = 0.0;
    private String interBranchAcct = "";
    private String acctBranchId = "";
    private String otherBankAcctHead = "";
    private String otherBankAcctProdId = "";
    
    
     //Data Correction Tool Ver 2.0 changes
    
    private String newProdType = "";
    private String newProdId = "";
    private String newAcctNum = "";
    private String newHeadId = "";
    private String oldHeadId = "";
    private String oldProdId = "";
    private String oldProdType = "";
    private String oldAcctNum = "";
   

  
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("correctionDt", correctionDt));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("batchId", batchId));
        strB.append(getTOString("modificationType", modificationType));
        strB.append(getTOString("oldFieldValue", oldFieldValue));
        strB.append(getTOString("newFieldValue", newFieldValue));
        strB.append(getTOString("oldStatusBy", oldStatusBy));
        strB.append(getTOString("oldAuthorizedBy", oldAuthorizedBy));
        strB.append(getTOString("correctionUserId", correctionUserId));
        strB.append(getTOString("correctionAuthorizeStaff", correctionAuthorizeStaff));
        strB.append(getTOString("correctionAuthorizeDt", correctionAuthorizeDt));
        strB.append(getTOString("transMode", transMode));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("interBranchAcct", interBranchAcct));
        strB.append(getTOString("acctBranchId", acctBranchId));
        strB.append(getTOString("otherBankAcctHead", otherBankAcctHead));
        strB.append(getTOString("newProdType", newProdType));
        strB.append(getTOString("newProdId", newProdId));
        strB.append(getTOString("newAcctNum", newAcctNum));
        strB.append(getTOString("newHeadId", newHeadId));
        strB.append(getTOString("oldProdType", oldProdType));
        strB.append(getTOString("oldProdId", oldProdId));
        strB.append(getTOString("oldAcctNum", oldAcctNum));
        strB.append(getTOString("oldHeadId", oldHeadId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOXml("correctionDt", correctionDt));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("batchId", batchId));
        strB.append(getTOXml("modificationType", modificationType));
        strB.append(getTOXml("oldFieldValue", oldFieldValue));
        strB.append(getTOXml("newFieldValue", newFieldValue));
        strB.append(getTOXml("oldStatusBy", oldStatusBy));
        strB.append(getTOXml("oldAuthorizedBy", oldAuthorizedBy));
        strB.append(getTOXml("correctionUserId", correctionUserId));
        strB.append(getTOXml("correctionAuthorizeStaff", correctionAuthorizeStaff));
        strB.append(getTOXml("correctionAuthorizeDt", correctionAuthorizeDt));
        strB.append(getTOXml("transMode", transMode));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("interBranchAcct", interBranchAcct));
        strB.append(getTOXml("acctBranchId", acctBranchId));
        strB.append(getTOXml("otherBankAcctHead", otherBankAcctHead));
        strB.append(getTOXml("newProdType", newProdType));
        strB.append(getTOXml("newProdId", newProdId));
        strB.append(getTOXml("newAcctNum", newAcctNum));
        strB.append(getTOXml("newHeadId", newHeadId));
        strB.append(getTOXml("oldProdType", oldProdType));
        strB.append(getTOXml("oldProdId", oldProdId));
        strB.append(getTOXml("oldAcctNum", oldAcctNum));
        strB.append(getTOXml("oldHeadId", oldHeadId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

     public String getKeyData() {
        setKeyColumns("transId");
        return transId ;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public Date getCorrectionDt() {
        return correctionDt;
    }

    public void setCorrectionDt(Date correctionDt) {
        this.correctionDt = correctionDt;
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

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getModificationType() {
        return modificationType;
    }

    public void setModificationType(String modificationType) {
        this.modificationType = modificationType;
    }

    public String getOldFieldValue() {
        return oldFieldValue;
    }

    public void setOldFieldValue(String oldFieldValue) {
        this.oldFieldValue = oldFieldValue;
    }

    public String getNewFieldValue() {
        return newFieldValue;
    }

    public void setNewFieldValue(String newFieldValue) {
        this.newFieldValue = newFieldValue;
    }

    public String getOldStatusBy() {
        return oldStatusBy;
    }

    public void setOldStatusBy(String oldStatusBy) {
        this.oldStatusBy = oldStatusBy;
    }

    public String getOldAuthorizedBy() {
        return oldAuthorizedBy;
    }

    public void setOldAuthorizedBy(String oldAuthorizedBy) {
        this.oldAuthorizedBy = oldAuthorizedBy;
    }

    public String getCorrectionUserId() {
        return correctionUserId;
    }

    public void setCorrectionUserId(String correctionUserId) {
        this.correctionUserId = correctionUserId;
    }

    public String getCorrectionAuthorizeStaff() {
        return correctionAuthorizeStaff;
    }

    public void setCorrectionAuthorizeStaff(String correctionAuthorizeStaff) {
        this.correctionAuthorizeStaff = correctionAuthorizeStaff;
    }

    public Date getCorrectionAuthorizeDt() {
        return correctionAuthorizeDt;
    }

    public void setCorrectionAuthorizeDt(Date correctionAuthorizeDt) {
        this.correctionAuthorizeDt = correctionAuthorizeDt;
    }

    public String getTransMode() {
        return transMode;
    }

    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public String getActNum() {
        return actNum;
    }

    public void setActNum(String actNum) {
        this.actNum = actNum;
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

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getInterBranchAcct() {
        return interBranchAcct;
    }

    public void setInterBranchAcct(String interBranchAcct) {
        this.interBranchAcct = interBranchAcct;
    }

    public String getAcctBranchId() {
        return acctBranchId;
    }

    public void setAcctBranchId(String acctBranchId) {
        this.acctBranchId = acctBranchId;
    }

    public String getOtherBankAcctHead() {
        return otherBankAcctHead;
    }

    public void setOtherBankAcctHead(String otherBankAcctHead) {
        this.otherBankAcctHead = otherBankAcctHead;
    }

    public String getOtherBankAcctProdId() {
        return otherBankAcctProdId;
    }

    public void setOtherBankAcctProdId(String otherBankAcctProdId) {
        this.otherBankAcctProdId = otherBankAcctProdId;
    }

    public String getNewProdType() {
        return newProdType;
    }

    public void setNewProdType(String newProdType) {
        this.newProdType = newProdType;
    }

    public String getNewProdId() {
        return newProdId;
    }

    public void setNewProdId(String newProdId) {
        this.newProdId = newProdId;
    }

    public String getNewAcctNum() {
        return newAcctNum;
    }

    public void setNewAcctNum(String newAcctNum) {
        this.newAcctNum = newAcctNum;
    }

    public String getNewHeadId() {
        return newHeadId;
    }

    public void setNewHeadId(String newHeadId) {
        this.newHeadId = newHeadId;
    }

    public String getOldHeadId() {
        return oldHeadId;
    }

    public void setOldHeadId(String oldHeadId) {
        this.oldHeadId = oldHeadId;
    }

    public String getOldProdId() {
        return oldProdId;
    }

    public void setOldProdId(String oldProdId) {
        this.oldProdId = oldProdId;
    }

    public String getOldProdType() {
        return oldProdType;
    }

    public void setOldProdType(String oldProdType) {
        this.oldProdType = oldProdType;
    }

    public String getOldAcctNum() {
        return oldAcctNum;
    }

    public void setOldAcctNum(String oldAcctNum) {
        this.oldAcctNum = oldAcctNum;
    }
    
    
    
    
        
}