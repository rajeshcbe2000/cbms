/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MultipleStandingActDetailsTO.java
 */

package com.see.truetransact.transferobject.transaction.multipleStanding;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class MultipleStandingActDetailsTO extends TransferObject implements Serializable {
    private String standingId = "";   
    private String transType = "";
    private String actProdType = "";
    private String actProdId = "";
    private String actAccNo = "";
    private Date actLastTransDt = null;
    private String isActive = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeBy = "";
    private String authorizedStatus = null;
    private String branchId = ""; 
    private String particulars = "";
    private Date authorizedDt = null; 
    private Double transAmount;

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }
    
    public String getActAccNo() {
        return actAccNo;
    }

    public void setActAccNo(String actAccNo) {
        this.actAccNo = actAccNo;
    }

    public Date getActLastTransDt() {
        return actLastTransDt;
    }

    public void setActLastTransDt(Date actLastTransDt) {
        this.actLastTransDt = actLastTransDt;
    }

    public String getActProdId() {
        return actProdId;
    }

    public void setActProdId(String actProdId) {
        this.actProdId = actProdId;
    }

    public String getActProdType() {
        return actProdType;
    }

    public void setActProdType(String actProdType) {
        this.actProdType = actProdType;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
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

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
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

    public Double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Double transAmount) {
        this.transAmount = transAmount;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

  
    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
	 public String getKeyData() {
		setKeyColumns("standingId");
		return standingId;
	}
//
	/** toString method which returns this TO as a String. */
	public String toString() {
		StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
		strB.append (getTOStringKey(getKeyData()));
		strB.append(getTOString("standingId", standingId));		
		strB.append(getTOString("transType", transType));
		strB.append(getTOString("transAmount", transAmount));
		strB.append(getTOString("statusDt", statusDt));
		strB.append(getTOString("authorizedDt", authorizedDt));
		strB.append(getTOString("actProdType", actProdType));
		strB.append(getTOString("actProdId", actProdId));
		strB.append(getTOString("actAccNo", actAccNo));
		strB.append(getTOString("status", status));
		strB.append(getTOString("statusBy", statusBy));
		strB.append(getTOString("authorizeBy", authorizeBy));
		strB.append(getTOString("statusBy", statusBy));
		strB.append(getTOString("authorizedStatus", authorizedStatus));
                strB.append(getTOString("branchId", branchId));
                strB.append(getTOString("particulars", particulars));
		strB.append(getTOStringEnd());
		return strB.toString();
	}

	/** toXML method which returns this TO as a XML output. */
	public String toXML() {
		StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
		strB.append (getTOXmlKey(getKeyData()));
		strB.append(getTOXml("standingId", standingId));		
		strB.append(getTOXml("transType", transType));
		strB.append(getTOXml("transAmount", transAmount));
		strB.append(getTOXml("statusDt", statusDt));
		strB.append(getTOXml("authorizedDt", authorizedDt));
		strB.append(getTOXml("actProdType", actProdType));
		strB.append(getTOXml("actProdId", actProdId));
		strB.append(getTOXml("actAccNo", actAccNo));
		strB.append(getTOXml("status", status));
		strB.append(getTOXml("statusBy", statusBy));
		strB.append(getTOXml("authorizeBy", authorizeBy));
		strB.append(getTOXml("statusBy", statusBy));
		strB.append(getTOXml("authorizedStatus", authorizedStatus));
                strB.append(getTOXml("branchId", branchId));
                strB.append(getTOXml("particulars", particulars));
		strB.append(getTOXmlEnd());
		return strB.toString();
	}

//        /**
//         * Getter for property empTransferID.
//         * @return Value of property empTransferID.
//         */
//        public java.lang.String getVenu() {
//            return venu;
//        }
//        
//        /**
//         * Setter for property empTransferID.
//         * @param empTransferID New value of property empTransferID.
//         */
//        public void setVenu(java.lang.String venu) {
//            this.venu = venu;
//        }
}
