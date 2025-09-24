/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanPowerAttorneyTO.java
 * @author shanmugavel
 * Created on Mon May 10 12:31:59 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.termloan.settlement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_POA.
 */
public class SettlementTO extends TransferObject implements Serializable {
//	private String borrowNo = "";
//        private String acctNum = "";
//	private Double poaNo = null;
//	private String poaHolderName = "";
//	private String street = "";
//	private String area = "";
//	private String city = "";
//	private String state = "";
//	private String countryCode = "";
//	private String pincode = "";
//	private String phone = "";
//	private Date periodFrom = null;
//	private Date periodTo = null;
//	private String remarks = "";
//	private String authorizeRemarks = "";
//	private String authorizeStatus = "";
//	private String status = "";
//        private String command = "";
//	private String toWhom = "";
//	private String addrType = "";
//	private String statusBy = "";
//	private Date statusDt = null;
//	private String custId = "";

    //settlement
    private String bankNameSet = "";
    private String branchNameSet = "";
    private String fromChqNo = "";
    private String toChqNo = "";
    private String qty = "";
    private Date chqDate = null;
    private Double chqAmt = null;
    private Date clearingDt = null;
    private String bounReason = "";
    private String remark = "";
    private String chqBoun = "";
    private String command = "";
    private String status = "";
    private String slNo = "";
    private String returnChq = "";
    private String chqStatus = "";
    private String acctNum = "";

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
//        
//	/** Setter/Getter for BORROW_NO - table Field*/
//	public void setBorrowNo (String borrowNo) {
//		this.borrowNo = borrowNo;
//	}
//	public String getBorrowNo () {
//		return borrowNo;
//	}
//
//	/** Setter/Getter for POA_NO - table Field*/
//	public void setPoaNo (Double poaNo) {
//		this.poaNo = poaNo;
//	}
//	public Double getPoaNo () {
//		return poaNo;
//	}
//
//	/** Setter/Getter for POA_HOLDER_NAME - table Field*/
//	public void setPoaHolderName (String poaHolderName) {
//		this.poaHolderName = poaHolderName;
//	}
//	public String getPoaHolderName () {
//		return poaHolderName;
//	}
//
//	/** Setter/Getter for STREET - table Field*/
//	public void setStreet (String street) {
//		this.street = street;
//	}
//	public String getStreet () {
//		return street;
//	}
//
//	/** Setter/Getter for AREA - table Field*/
//	public void setArea (String area) {
//		this.area = area;
//	}
//	public String getArea () {
//		return area;
//	}
//
//	/** Setter/Getter for CITY - table Field*/
//	public void setCity (String city) {
//		this.city = city;
//	}
//	public String getCity () {
//		return city;
//	}
//
//	/** Setter/Getter for STATE - table Field*/
//	public void setState (String state) {
//		this.state = state;
//	}
//	public String getState () {
//		return state;
//	}
//
//	/** Setter/Getter for COUNTRY_CODE - table Field*/
//	public void setCountryCode (String countryCode) {
//		this.countryCode = countryCode;
//	}
//	public String getCountryCode () {
//		return countryCode;
//	}
//
//	/** Setter/Getter for PINCODE - table Field*/
//	public void setPincode (String pincode) {
//		this.pincode = pincode;
//	}
//	public String getPincode () {
//		return pincode;
//	}
//
//	/** Setter/Getter for PHONE - table Field*/
//	public void setPhone (String phone) {
//		this.phone = phone;
//	}
//	public String getPhone () {
//		return phone;
//	}
//
//	/** Setter/Getter for PERIOD_FROM - table Field*/
//	public void setPeriodFrom (Date periodFrom) {
//		this.periodFrom = periodFrom;
//	}
//	public Date getPeriodFrom () {
//		return periodFrom;
//	}
//
//	/** Setter/Getter for PERIOD_TO - table Field*/
//	public void setPeriodTo (Date periodTo) {
//		this.periodTo = periodTo;
//	}
//	public Date getPeriodTo () {
//		return periodTo;
//	}
//
//	/** Setter/Getter for REMARKS - table Field*/
//	public void setRemarks (String remarks) {
//		this.remarks = remarks;
//	}
//	public String getRemarks () {
//		return remarks;
//	}
//
//	/** Setter/Getter for AUTHORIZE_REMARKS - table Field*/
//	public void setAuthorizeRemarks (String authorizeRemarks) {
//		this.authorizeRemarks = authorizeRemarks;
//	}
//	public String getAuthorizeRemarks () {
//		return authorizeRemarks;
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
//	/** Setter/Getter for STATUS - table Field*/
//	public void setStatus (String status) {
//		this.status = status;
//	}
//	public String getStatus () {
//		return status;
//	}
//
//	/** Setter/Getter for TO_WHOM - table Field*/
//	public void setToWhom (String toWhom) {
//		this.toWhom = toWhom;
//	}
//	public String getToWhom () {
//		return toWhom;
//	}
//
//	/** Setter/Getter for ADDR_TYPE - table Field*/
//	public void setAddrType (String addrType) {
//		this.addrType = addrType;
//	}
//	public String getAddrType () {
//		return addrType;
//	}
//
//	/** Setter/Getter for STATUS_BY - table Field*/
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
//	/** Setter/Getter for CUST_ID - table Field*/
//	public void setCustId (String custId) {
//		this.custId = custId;
//	}
//	public String getCustId () {
//		return custId;
//	}

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("bankNameSet" + KEY_VAL_SEPARATOR + "branchNameSet");
        return bankNameSet + KEY_VAL_SEPARATOR + branchNameSet;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("bankNameSet", bankNameSet));
        strB.append(getTOString("branchNameSet", branchNameSet));
        strB.append(getTOString("fromChqNo", fromChqNo));
        strB.append(getTOString("toChqNo", toChqNo));
        strB.append(getTOString("qty", qty));
        strB.append(getTOString("chqDate", chqDate));
        strB.append(getTOString("chqAmt", chqAmt));
        strB.append(getTOString("clearingDt", clearingDt));
        strB.append(getTOString("bounReason", bounReason));
        strB.append(getTOString("remark", remark));
        strB.append(getTOString("chqBoun", chqBoun));
        strB.append(getTOString("chqStatus", chqStatus));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("returnChq", returnChq));
        strB.append(getTOString("acctNum", acctNum));
//		strB.append(getTOString("phone", phone));
//		strB.append(getTOString("periodFrom", periodFrom));
//		strB.append(getTOString("periodTo", periodTo));
//		strB.append(getTOString("remarks", remarks));
//		strB.append(getTOString("authorizeRemarks", authorizeRemarks));
//		strB.append(getTOString("authorizeStatus", authorizeStatus));
//		strB.append(getTOString("status", status));
//                strB.append(getTOString("toWhom", toWhom));
//		strB.append(getTOString("addrType", addrType));
//		strB.append(getTOString("statusBy", statusBy));
//		strB.append(getTOString("statusDt", statusDt));
//		strB.append(getTOString("custId", custId));
        strB.append(getTOStringEnd());
        System.out.println("!!!!!!!!!!!!toString!!!!!!!!!!!!!strB" + strB);
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("bankNameSet", bankNameSet));
        strB.append(getTOXml("branchNameSet", branchNameSet));
        strB.append(getTOXml("fromChqNo", fromChqNo));
        strB.append(getTOXml("toChqNo", toChqNo));
        strB.append(getTOXml("qty", qty));
        strB.append(getTOXml("chqDate", chqDate));
        strB.append(getTOXml("chqAmt", chqAmt));
        strB.append(getTOXml("clearingDt", clearingDt));
        strB.append(getTOXml("bounReason", bounReason));
        strB.append(getTOXml("remark", remark));
        strB.append(getTOXml("chqBoun", chqBoun));
        strB.append(getTOXml("chqStatus", chqStatus));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("returnChq", returnChq));
        strB.append(getTOXml("acctNum", acctNum));
//		strB.append(getTOXml("pincode", pincode));
//		strB.append(getTOXml("phone", phone));
//		strB.append(getTOXml("periodFrom", periodFrom));
//		strB.append(getTOXml("periodTo", periodTo));
//		strB.append(getTOXml("remarks", remarks));
//		strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
//		strB.append(getTOXml("authorizeStatus", authorizeStatus));
//		strB.append(getTOXml("status", status));
//                strB.append(getTOXml("toWhom", toWhom));
//		strB.append(getTOXml("addrType", addrType));
//		strB.append(getTOXml("statusBy", statusBy));
//		strB.append(getTOXml("statusDt", statusDt));
//		strB.append(getTOXml("custId", custId));
        strB.append(getTOXmlEnd());
        System.out.println("!!!!!!!!!!!!toXML!!!!!!!!!!!!!strB" + strB);
        return strB.toString();
    }

    /**
     * Getter for property fromChqNo.
     *
     * @return Value of property fromChqNo.
     */
    public java.lang.String getFromChqNo() {
        return fromChqNo;
    }

    /**
     * Setter for property fromChqNo.
     *
     * @param fromChqNo New value of property fromChqNo.
     */
    public void setFromChqNo(java.lang.String fromChqNo) {
        this.fromChqNo = fromChqNo;
    }

    /**
     * Getter for property toChqNo.
     *
     * @return Value of property toChqNo.
     */
    public java.lang.String getToChqNo() {
        return toChqNo;
    }

    /**
     * Setter for property toChqNo.
     *
     * @param toChqNo New value of property toChqNo.
     */
    public void setToChqNo(java.lang.String toChqNo) {
        this.toChqNo = toChqNo;
    }

    /**
     * Getter for property qty.
     *
     * @return Value of property qty.
     */
    public java.lang.String getQty() {
        return qty;
    }

    /**
     * Setter for property qty.
     *
     * @param qty New value of property qty.
     */
    public void setQty(java.lang.String qty) {
        this.qty = qty;
    }

    /**
     * Getter for property chqDate.
     *
     * @return Value of property chqDate.
     */
    public java.util.Date getChqDate() {
        return chqDate;
    }

    /**
     * Setter for property chqDate.
     *
     * @param chqDate New value of property chqDate.
     */
    public void setChqDate(java.util.Date chqDate) {
        this.chqDate = chqDate;
    }

    /**
     * Getter for property chqAmt.
     *
     * @return Value of property chqAmt.
     */
    public java.lang.Double getChqAmt() {
        return chqAmt;
    }

    /**
     * Setter for property chqAmt.
     *
     * @param chqAmt New value of property chqAmt.
     */
    public void setChqAmt(java.lang.Double chqAmt) {
        this.chqAmt = chqAmt;
    }

    /**
     * Getter for property clearingDt.
     *
     * @return Value of property clearingDt.
     */
    public java.util.Date getClearingDt() {
        return clearingDt;
    }

    /**
     * Setter for property clearingDt.
     *
     * @param clearingDt New value of property clearingDt.
     */
    public void setClearingDt(java.util.Date clearingDt) {
        this.clearingDt = clearingDt;
    }

    /**
     * Getter for property bounReason.
     *
     * @return Value of property bounReason.
     */
    public java.lang.String getBounReason() {
        return bounReason;
    }

    /**
     * Setter for property bounReason.
     *
     * @param bounReason New value of property bounReason.
     */
    public void setBounReason(java.lang.String bounReason) {
        this.bounReason = bounReason;
    }

    /**
     * Getter for property remark.
     *
     * @return Value of property remark.
     */
    public java.lang.String getRemark() {
        return remark;
    }

    /**
     * Setter for property remark.
     *
     * @param remark New value of property remark.
     */
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }

    /**
     * Getter for property chqBoun.
     *
     * @return Value of property chqBoun.
     */
    public java.lang.String getChqBoun() {
        return chqBoun;
    }

    /**
     * Setter for property chqBoun.
     *
     * @param chqBoun New value of property chqBoun.
     */
    public void setChqBoun(java.lang.String chqBoun) {
        this.chqBoun = chqBoun;
    }

    /**
     * Getter for property bankNameSet.
     *
     * @return Value of property bankNameSet.
     */
    public java.lang.String getBankNameSet() {
        return bankNameSet;
    }

    /**
     * Setter for property bankNameSet.
     *
     * @param bankNameSet New value of property bankNameSet.
     */
    public void setBankNameSet(java.lang.String bankNameSet) {
        this.bankNameSet = bankNameSet;
    }

    /**
     * Getter for property branchNameSet.
     *
     * @return Value of property branchNameSet.
     */
    public java.lang.String getBranchNameSet() {
        return branchNameSet;
    }

    /**
     * Setter for property branchNameSet.
     *
     * @param branchNameSet New value of property branchNameSet.
     */
    public void setBranchNameSet(java.lang.String branchNameSet) {
        this.branchNameSet = branchNameSet;
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
     * Getter for property slNo.
     *
     * @return Value of property slNo.
     */
    public java.lang.String getSlNo() {
        return slNo;
    }

    /**
     * Setter for property slNo.
     *
     * @param slNo New value of property slNo.
     */
    public void setSlNo(java.lang.String slNo) {
        this.slNo = slNo;
    }

    /**
     * Getter for property returnChq.
     *
     * @return Value of property returnChq.
     */
    public java.lang.String getReturnChq() {
        return returnChq;
    }

    /**
     * Setter for property returnChq.
     *
     * @param returnChq New value of property returnChq.
     */
    public void setReturnChq(java.lang.String returnChq) {
        this.returnChq = returnChq;
    }

    /**
     * Getter for property chqStatus.
     *
     * @return Value of property chqStatus.
     */
    public java.lang.String getChqStatus() {
        return chqStatus;
    }

    /**
     * Setter for property chqStatus.
     *
     * @param chqStatus New value of property chqStatus.
     */
    public void setChqStatus(java.lang.String chqStatus) {
        this.chqStatus = chqStatus;
    }

    /**
     * Getter for property acctNum.
     *
     * @return Value of property acctNum.
     */
    public java.lang.String getAcctNum() {
        return acctNum;
    }

    /**
     * Setter for property acctNum.
     *
     * @param acctNum New value of property acctNum.
     */
    public void setAcctNum(java.lang.String acctNum) {
        this.acctNum = acctNum;
    }
}