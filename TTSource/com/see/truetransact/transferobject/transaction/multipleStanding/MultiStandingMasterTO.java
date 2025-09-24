/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MultiStandingMasterTO.java
 */

package com.see.truetransact.transferobject.transaction.multipleStanding;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;
/**
 *
 * @author Nithya
 */
public class MultiStandingMasterTO extends TransferObject implements Serializable{

    private String standingId = "";
    private String accountHead = "";
    private String transType = "";
    private String masterProdType = "";
    private String masterProdId = "";
    private String masterActNo = "";
    private Date masterLastTransDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeBy = "";
    private String authorizedStatus = null;
    private Date authorizeDt = null;
    private String particulars = "";
    private String branchId = "";
    private String siDescription = "";

    public String getSiDescription() {
        return siDescription;
    }

    public void setSiDescription(String siDescription) {
        this.siDescription = siDescription;
    }
    
    public String getAccountHead() {
        return accountHead;
    }

    public void setAccountHead(String accountHead) {
        this.accountHead = accountHead;
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

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getMasterActNo() {
        return masterActNo;
    }

    public void setMasterActNo(String masterActNo) {
        this.masterActNo = masterActNo;
    }

    public Date getMasterLastTransDt() {
        return masterLastTransDt;
    }

    public void setMasterLastTransDt(Date masterLastTransDt) {
        this.masterLastTransDt = masterLastTransDt;
    }

    public String getMasterProdId() {
        return masterProdId;
    }

    public void setMasterProdId(String masterProdId) {
        this.masterProdId = masterProdId;
    }

    public String getMasterProdType() {
        return masterProdType;
    }

    public void setMasterProdType(String masterProdType) {
        this.masterProdType = masterProdType;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getStandingId() {
        return standingId;
    }

    public void setStandingId(String standingId) {
        this.standingId = standingId;
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
    
    public String getKeyData() {
		setKeyColumns("standingId");
		return standingId;
    }
    
    /** toString method which returns this TO as a String. */
	public String toString() {
		StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
		strB.append (getTOStringKey(getKeyData()));
		strB.append(getTOString("standingId", standingId));
		strB.append(getTOString("accountHead", accountHead));
		strB.append(getTOString("transType", transType));
		strB.append(getTOString("masterProdType", masterProdType));
		strB.append(getTOString("masterProdId", masterProdId));
		strB.append(getTOString("masterActNo", masterActNo));
		strB.append(getTOString("masterLastTransDt", masterLastTransDt));
		strB.append(getTOString("particulars", particulars));
		strB.append(getTOString("branchId", branchId));	
                strB.append(getTOXml("status", status));
		strB.append(getTOXml("statusBy", statusBy));
		strB.append(getTOXml("authorizeBy", authorizeBy));
		strB.append(getTOXml("statusBy", statusBy));
		strB.append(getTOXml("authorizedStatus", authorizedStatus));      
                strB.append(getTOStringEnd());
		return strB.toString();
	}

	/** toXML method which returns this TO as a XML output. */
	public String toXML() {
		StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
		strB.append (getTOXmlKey(getKeyData()));
		strB.append(getTOXml("standingId", standingId));
		strB.append(getTOXml("accountHead", accountHead));
		strB.append(getTOXml("transType", transType));
		strB.append(getTOXml("masterProdType", masterProdType));
		strB.append(getTOXml("masterProdId", masterProdId));
		strB.append(getTOXml("masterActNo", masterActNo));
		strB.append(getTOXml("masterLastTransDt", masterLastTransDt));
		strB.append(getTOXml("particulars", particulars));
		strB.append(getTOXml("branchId", branchId));
		strB.append(getTOXml("status", status));
		strB.append(getTOXml("statusBy", statusBy));
		strB.append(getTOXml("authorizeBy", authorizeBy));
		strB.append(getTOXml("statusBy", statusBy));
		strB.append(getTOXml("authorizedStatus", authorizedStatus));               
		strB.append(getTOXmlEnd());
		return strB.toString();
	}
}
