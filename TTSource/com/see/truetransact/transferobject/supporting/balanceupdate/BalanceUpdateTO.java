
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * BalanceUpdateTO.java
 */

package com.see.truetransact.transferobject.supporting.balanceupdate;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;
import java.util.*;

/**
 *
 * @author Administrator1
 */
public class BalanceUpdateTO extends TransferObject implements Serializable {

    private String authorize = "";
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private Date createdDt = null;
    private String createdBy = "";
    private String branchcode = "";
    private Date frmdate = null;
    private Date todate = null;
    private String finalacttype = "";
    private Double amount = new Double(0.0);
    private String accountHeadId = "";
    private String acctHeadDesc = "";
    private Double actualAmount = new Double(0.0);
    private String subacttype = "";
    private Integer recordCount = 0;
    private Integer slNo = 0;
    private String balanceType = "";
    private String balSheetId = "";
    private String entryMode = "";

    public String getEntryMode() {
        return entryMode;
    }

    public void setEntryMode(String entryMode) {
        this.entryMode = entryMode;
    }

    public String getBalSheetId() {
        return balSheetId;
    }

    public void setBalSheetId(String balSheetId) {
        this.balSheetId = balSheetId;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public String getAccountHeadId() {
        return accountHeadId;
    }

    public void setAccountHeadId(String accountHeadId) {
        this.accountHeadId = accountHeadId;
    }

    public String getAcctHeadDesc() {
        return acctHeadDesc;
    }

    public void setAcctHeadDesc(String acctHeadDesc) {
        this.acctHeadDesc = acctHeadDesc;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

   
    
    public Date getTodate() {
        return todate;
    }

    public void setTodate(Date todate) {
        this.todate = todate;
    }

    public String getAuthorize() {
        return authorize;
    }

    public void setAuthorize(String authorize) {
        this.authorize = authorize;
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

    public String getBranchcode() {
        return branchcode;
    }

    public void setBranchcode(String branchcode) {
        this.branchcode = branchcode;
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

    public String getFinalacttype() {
        return finalacttype;
    }

    public void setFinalacttype(String finalacttype) {
        this.finalacttype = finalacttype;
    }

    public Date getFrmdate() {
        return frmdate;
    }

    public void setFrmdate(Date frmdate) {
        this.frmdate = frmdate;
    }

    public String getSubacttype() {
        return subacttype;
    }

    public void setSubacttype(String subacttype) {
        this.subacttype = subacttype;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(Integer slNo) {
        this.slNo = slNo;
    }

    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append (getTOStringKey(getKeyData()));

        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorize", authorize));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("branchcode", branchcode));
        strB.append(getTOString("frmdate", frmdate));
        strB.append(getTOString("finalacttype", finalacttype));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("accountHeadId", accountHeadId));
        strB.append(getTOString("acctHeadDesc", acctHeadDesc));
        strB.append(getTOString("actualAmount", actualAmount));

        //System.out.println("share_acct_num===========" + strB.append(getTOString("shareAcctNum", shareAcctNum)));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        //strB.append (getTOXmlKey(getKeyData()));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorize", authorize));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("branchcode", branchcode));
        strB.append(getTOXml("frmdate", frmdate));
        strB.append(getTOXml("finalacttype", finalacttype));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("accountHeadId", accountHeadId));
        strB.append(getTOXml("acctHeadDesc", acctHeadDesc));
        strB.append(getTOXml("actualAmount", actualAmount));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}
