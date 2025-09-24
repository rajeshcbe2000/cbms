/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LockerConfigDetailsTO.java
 *
 * Created on May 12, 2010, 4:22 PM
 */
package com.see.truetransact.transferobject.locker.lockerconfig;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author Administrator
 */
public class LockerConfigDetailsTO extends TransferObject implements Serializable {

    private Integer slNo = new Integer(0);
    private String prodId = "";
    private Integer fromLocNo = new Integer(0);
    private Integer toLocNo = new Integer(0);
    private Integer totLockers = new Integer(0);
    private Integer masterKeyNo = new Integer(0);
    private String lockerKeyNo = "";
    private String branchId = "";
//	private Date createdDt = null;
//	private String createdBy = "";
    private String status = "";
    private String locStatus = "";
//	private String statusBy = "";
//	private Date statusDt = null;
//	private String authorizeStatus = "";
//	private String authorizeBy = "";
//	private Date authorizeDt = null;

    /**
     * Setter/Getter for CONFIG_ID - table Field
     */
    /**
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

    /**
     * Setter/Getter for CREATED_DT - table Field
     */
//	public void setCreatedDt (Date createdDt) {
//		this.createdDt = createdDt;
//	}
//	public Date getCreatedDt () {
//		return createdDt;
//	}
//
//	/** Setter/Getter for CREATED_BY - table Field*/
//	public void setCreatedBy (String createdBy) {
//		this.createdBy = createdBy;
//	}
//	public String getCreatedBy () {
//		return createdBy;
//	}
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
//	public void setStatusBy (String statusBy) {
//		this.statusBy = statusBy;
//	}
//	public String getStatusBy () {
//		return statusBy;
//	}
//
//	/** Setter/Getter for STATUS_DT - table Field*/
//	public void setStatusDt (Date statusDt) {
//		this.statusDt = statusDt;
//	}
//	public Date getStatusDt () {
//		return statusDt;
//	}
//
//	/** Setter/Getter for AUTHORIZE_STATUS - table Field*/
//	public void setAuthorizeStatus (String authorizeStatus) {
//		this.authorizeStatus = authorizeStatus;
//	}
//	public String getAuthorizeStatus () {
//		return authorizeStatus;
//	}
//
//	/** Setter/Getter for AUTHORIZE_BY - table Field*/
//	public void setAuthorizeBy (String authorizeBy) {
//		this.authorizeBy = authorizeBy;
//	}
//	public String getAuthorizeBy () {
//		return authorizeBy;
//	}
//
//	/** Setter/Getter for AUTHORIZE_DT - table Field*/
//	public void setAuthorizeDt (Date authorizeDt) {
//		this.authorizeDt = authorizeDt;
//	}
//	public Date getAuthorizeDt () {
//		return authorizeDt;
//	}
    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(prodId);
        return prodId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("fromLocNo", fromLocNo));
        strB.append(getTOString("toLocNo", toLocNo));
        strB.append(getTOString("totLockers", totLockers));
        strB.append(getTOString("masterKeyNo", masterKeyNo));
        strB.append(getTOString("lockerKeyNo", lockerKeyNo));
        strB.append(getTOString("branchId", branchId));
//		strB.append(getTOString("createdDt", createdDt));
//		strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("status", status));
//		strB.append(getTOString("statusBy", statusBy));
//		strB.append(getTOString("statusDt", statusDt));
//		strB.append(getTOString("authorizeStatus", authorizeStatus));
//		strB.append(getTOString("authorizeBy", authorizeBy));
//		strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("fromLocNo", fromLocNo));
        strB.append(getTOXml("toLocNo", toLocNo));
        strB.append(getTOXml("totLockers", totLockers));
        strB.append(getTOXml("masterKeyNo", masterKeyNo));
        strB.append(getTOXml("lockerKeyNo", lockerKeyNo));
        strB.append(getTOXml("branchId", branchId));
//		strB.append(getTOXml("createdDt", createdDt));
//		strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("status", status));
//		strB.append(getTOXml("statusBy", statusBy));
//		strB.append(getTOXml("statusDt", statusDt));
//		strB.append(getTOXml("authorizeStatus", authorizeStatus));
//		strB.append(getTOXml("authorizeBy", authorizeBy));
//		strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property lockerKeyNo.
     *
     * @return Value of property lockerKeyNo.
     */
    public java.lang.String getLockerKeyNo() {
        return lockerKeyNo;
    }

    /**
     * Setter for property lockerKeyNo.
     *
     * @param lockerKeyNo New value of property lockerKeyNo.
     */
    public void setLockerKeyNo(java.lang.String lockerKeyNo) {
        this.lockerKeyNo = lockerKeyNo;
    }

    

    /**
     * Getter for property locStatus.
     *
     * @return Value of property locStatus.
     */
    public java.lang.String getLocStatus() {
        return locStatus;
    }

    /**
     * Setter for property locStatus.
     *
     * @param locStatus New value of property locStatus.
     */
    public void setLocStatus(java.lang.String locStatus) {
        this.locStatus = locStatus;
    }

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(Integer slNo) {
        this.slNo = slNo;
    }

    public Integer getFromLocNo() {
        return fromLocNo;
    }

    public void setFromLocNo(Integer fromLocNo) {
        this.fromLocNo = fromLocNo;
    }

    public Integer getToLocNo() {
        return toLocNo;
    }

    public void setToLocNo(Integer toLocNo) {
        this.toLocNo = toLocNo;
    }

    public Integer getTotLockers() {
        return totLockers;
    }

    public void setTotLockers(Integer totLockers) {
        this.totLockers = totLockers;
    }

    public Integer getMasterKeyNo() {
        return masterKeyNo;
    }

    public void setMasterKeyNo(Integer masterKeyNo) {
        this.masterKeyNo = masterKeyNo;
    }
    /**
     * Getter for property LockerKeyNo.
     *
     * @return Value of property LockerKeyNo.
     */
//        public java.lang.String getLockerKeyNo() {
//            return LockerKeyNo;
//        }
//        
//        /**
//         * Setter for property LockerKeyNo.
//         * @param LockerKeyNo New value of property LockerKeyNo.
//         */
//        public void setLockerKeyNo(java.lang.String LockerKeyNo) {
//            this.LockerKeyNo = LockerKeyNo;
//        }
}
