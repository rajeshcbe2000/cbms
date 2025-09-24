/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountTO.java
 * 
 * Created on Fri Apr 15 10:47:20 IST 2005
 */
package com.see.truetransact.transferobject.locker.lockerissue;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ACT_MASTER.
 */
public class LockerIssueTO extends TransferObject implements Serializable {

    private String locNum = "";
    private String prodId = "";
    private String custId = "";
    private String branchCode = "";
    private Date createDt = null;
    private String locStatusId = "";
    private String locCatId = "";
    private String optModeId = "";
//	private Double todLimit = null;
//	private String groupCodeId = "";
    private String settmtModeId = "";
//	private String prevActNum = "";
//	private Double clearBalance = null;
//	private Double unclearBalance = null;
//	private Double floatBalance = null;
//	private Double effectiveBalance = null;
//	private Double availableBalance = null;
    private String createdBy = "";
    private String authorizedBy = "";
    private String authorizationStatus = null;
    private String closedBy = "";
    private Date closedDt = null;
    private String collectRentMM = "";
    private String collectRentYYYY = "";
    private String si = "";
    // private String siNo="";
    private String pwd = "";
    // private String rbtnPwdYes="";
    //private String pwdNo="";
    private String prodType = "";
    private String txtProdId = "";
    private String customerIdCr = "";
    private String customerNameCrValue = "";
//	private String baseCurr = "";
//	private Date lastTransDt = null;
//	private Double shadowCredit = null;
//	private Double shadowDebit = null;
    private Date authorizedDt = null;
//	private Date actStatusDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
//	private Double lienAmt = null;
//	private Double freezeAmt = null;
//	private Double totalBalance = null;
    private String commAddrType = "";
//	private Double productAmt = null;
    private String categoryId = "";
//	private Double flexiDepositAmt = null;
    private String acctName = "";
    private String remarks = "";
    private String depositNo = "";
    private Date expDt = null;
    private String freezeStatus = "";
    private String freezeRemarks = "";
    private Date freezeDt = null;
    private String unFreezeStatus = "";
    private String unFreezeRemarks = "";
    private Date unFreezeDt = null;
    private String slNo = "";
    private String actNum = "";
    //private String customerIdAI="";

    /**
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setLocNum(String locNum) {
        this.locNum = locNum;
    }

    public String getLocNum() {
        return locNum;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for CUST_ID - table Field
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
    }

    /**
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter/Getter for CREATE_DT - table Field
     */
    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getCreateDt() {
        return createDt;
    }

    /**
     * Setter/Getter for ACT_STATUS_ID - table Field
     */
    public void setLocStatusId(String locStatusId) {
        this.locStatusId = locStatusId;
    }

    public String getLocStatusId() {
        return locStatusId;
    }

    /**
     * Setter/Getter for ACT_CAT_ID - table Field
     */
    public void setLocCatId(String locCatId) {
        this.locCatId = locCatId;
    }

    public String getLocCatId() {
        return locCatId;
    }

    /**
     * Setter/Getter for OPT_MODE_ID - table Field
     */
    public void setOptModeId(String optModeId) {
        this.optModeId = optModeId;
    }

    public String getOptModeId() {
        return optModeId;
    }

    /**
     * Setter/Getter for TOD_LIMIT - table Field
     */
//	public void setTodLimit (Double todLimit) {
//		this.todLimit = todLimit;
//	}
//	public Double getTodLimit () {
//		return todLimit;
//	}
    /**
     * Setter/Getter for GROUP_CODE_ID - table Field
     */
//	public void setGroupCodeId (String groupCodeId) {
//		this.groupCodeId = groupCodeId;
//	}
//	public String getGroupCodeId () {
//		return groupCodeId;
//	}
    /**
     * Setter/Getter for SETTMT_MODE_ID - table Field
     */
    public void setSettmtModeId(String settmtModeId) {
        this.settmtModeId = settmtModeId;
    }

    public String getSettmtModeId() {
        return settmtModeId;
    }

    /**
     * Setter/Getter for PREV_ACT_NUM - table Field
     */
//	public void setPrevActNum (String prevActNum) {
//		this.prevActNum = prevActNum;
//	}
//	public String getPrevActNum () {
//		return prevActNum;
//	}
    /**
     * Setter/Getter for CLEAR_BALANCE - table Field
     */
//	public void setClearBalance (Double clearBalance) {
//		this.clearBalance = clearBalance;
//	}
//	public Double getClearBalance () {
//		return clearBalance;
//	}
    /**
     * Setter/Getter for UNCLEAR_BALANCE - table Field
     */
//	public void setUnclearBalance (Double unclearBalance) {
//		this.unclearBalance = unclearBalance;
//	}
//	public Double getUnclearBalance () {
//		return unclearBalance;
//	}
    /**
     * Setter/Getter for FLOAT_BALANCE - table Field
     */
//	public void setFloatBalance (Double floatBalance) {
//		this.floatBalance = floatBalance;
//	}
//	public Double getFloatBalance () {
//		return floatBalance;
////	}
//
//	/** Setter/Getter for EFFECTIVE_BALANCE - table Field*/
//	public void setEffectiveBalance (Double effectiveBalance) {
//		this.effectiveBalance = effectiveBalance;
//	}
//	public Double getEffectiveBalance () {
//		return effectiveBalance;
//	}
    /**
     * Setter/Getter for AVAILABLE_BALANCE - table Field
     */
//	public void setAvailableBalance (Double availableBalance) {
//		this.availableBalance = availableBalance;
//	}
//	public Double getAvailableBalance () {
//		return availableBalance;
//	}
    /**
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }
    //  public void setCustomerIdAI(String customerIdAI)
    //{
    //    this.customerIdAI=customerIdAI;
    //}
    //  public String  getCustomerIdAI()
    //{
    //    return customerIdAI;
    //}

    /**
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter/Getter for AUTHORIZATION_STATUS - table Field
     */
    public void setAuthorizationStatus(String authorizationStatus) {
        this.authorizationStatus = authorizationStatus;
    }

    public String getAuthorizationStatus() {
        return authorizationStatus;
    }

    /**
     * Setter/Getter for CLOSED_BY - table Field
     */
    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public String getClosedBy() {
        return closedBy;
    }

    /**
     * Setter/Getter for CLOSED_DT - table Field
     */
    public void setClosedDt(Date closedDt) {
        this.closedDt = closedDt;
    }

    public Date getClosedDt() {
        return closedDt;
    }

    /**
     * Setter/Getter for BASE_CURR - table Field
     */
//	public void setBaseCurr (String baseCurr) {
//		this.baseCurr = baseCurr;
//	}
//	public String getBaseCurr () {
//		return baseCurr;
//	}
    /**
     * Setter/Getter for LAST_TRANS_DT - table Field
     */
//	public void setLastTransDt (Date lastTransDt) {
//		this.lastTransDt = lastTransDt;
//	}
//	public Date getLastTransDt () {
//		return lastTransDt;
//	}
//
//	/** Setter/Getter for SHADOW_CREDIT - table Field*/
//	public void setShadowCredit (Double shadowCredit) {
//		this.shadowCredit = shadowCredit;
//	}
//	public Double getShadowCredit () {
//		return shadowCredit;
//	}
    /**
     * Setter/Getter for SHADOW_DEBIT - table Field
     */
//	public void setShadowDebit (Double shadowDebit) {
//		this.shadowDebit = shadowDebit;
//	}
//	public Double getShadowDebit () {
//		return shadowDebit;
//	}
    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter/Getter for ACT_STATUS_DT - table Field
     */
//	public void setActStatusDt (Date actStatusDt) {
//		this.actStatusDt = actStatusDt;
//	}
//	public Date getActStatusDt () {
//		return actStatusDt;
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
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter/Getter for LIEN_AMT - table Field
     */
//	public void setLienAmt (Double lienAmt) {
//		this.lienAmt = lienAmt;
//	}
//	public Double getLienAmt () {
//		return lienAmt;
//	}
    /**
     * Setter/Getter for FREEZE_AMT - table Field
     */
//	public void setFreezeAmt (Double freezeAmt) {
//		this.freezeAmt = freezeAmt;
//	}
//	public Double getFreezeAmt () {
//		return freezeAmt;
//	}
    /**
     * Setter/Getter for TOTAL_BALANCE - table Field
     */
//	public void setTotalBalance (Double totalBalance) {
//		this.totalBalance = totalBalance;
//	}
//	public Double getTotalBalance () {
//		return totalBalance;
//	}
    /**
     * Setter/Getter for COMM_ADDR_TYPE - table Field
     */
    public void setCommAddrType(String commAddrType) {
        this.commAddrType = commAddrType;
    }

    public String getCommAddrType() {
        return commAddrType;
    }

    public void setCollectRentMM(java.lang.String collectRentMM) {
        this.collectRentMM = collectRentMM;
    }

    public String getCollectRentMM() {
        return collectRentMM;
    }

    public void setCollectRentYYYY(java.lang.String collectRentYYYY) {
        this.collectRentYYYY = collectRentYYYY;

    }

    public String getcollectRentYYYY() {

        return collectRentYYYY;

    }

    public String getSi() {
        return si;
    }

    public void setSi(java.lang.String si) {
        this.si = si;
    }

    public void setPwd(java.lang.String pwd) {
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;

    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdType() {
        return prodType;
    }

    public void setTxtProdId(String txtProdId) {
        this.txtProdId = txtProdId;
    }

    public String getTxtProdId() {
        return txtProdId;
    }

    public void setCustomerIdCr(String customerIdCr) {
        this.customerIdCr = customerIdCr;
    }

    public String getCustomerIdCr() {
        return customerIdCr;
    }

    public void setCustomerNameCrValue(String customerNameCrValue) {
        this.customerNameCrValue = customerNameCrValue;
    }

    public String getCustomerNameCrValue() {
        return customerNameCrValue;
    }

    //public void setrbtnSiYes(String )
    /**
     * Setter/Getter for PRODUCT_AMT - table Field
     */
//	public void setProductAmt (Double productAmt) {
//		this.productAmt = productAmt;
//	}
//	public Double getProductAmt () {
//		return productAmt;
//	}
    /**
     * Setter/Getter for CATEGORY_ID - table Field
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    /**
     * Setter/Getter for FLEXI_DEPOSIT_AMT - table Field
     */
//	public void setFlexiDepositAmt (Double flexiDepositAmt) {
//		this.flexiDepositAmt = flexiDepositAmt;
//	}
//	public Double getFlexiDepositAmt () {
//		return flexiDepositAmt;
//	}
    /**
     * Setter/Getter for ACCT_NAME - table Field
     */
    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getAcctName() {
        return acctName;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("locNum");
        return locNum;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("locNum", locNum));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("createDt", createDt));
        strB.append(getTOString("locStatusId", locStatusId));
        strB.append(getTOString("locCatId", locCatId));
        strB.append(getTOString("optModeId", optModeId));
        //strB.append(getToString("collectRentMM",collectRentMM));
        //strB.append(getToString("collectRentYYYY",collectRentYYYY));
//		strB.append(getTOString("todLimit", todLimit));
//		strB.append(getTOString("groupCodeId", groupCodeId));
        strB.append(getTOString("settmtModeId", settmtModeId));
//		strB.append(getTOString("prevActNum", prevActNum));
//		strB.append(getTOString("clearBalance", clearBalance));
//		strB.append(getTOString("unclearBalance", unclearBalance));
//		strB.append(getTOString("floatBalance", floatBalance));
//		strB.append(getTOString("effectiveBalance", effectiveBalance));
//		strB.append(getTOString("availableBalance", availableBalance));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizationStatus", authorizationStatus));
        strB.append(getTOString("closedBy", closedBy));
        strB.append(getTOString("closedDt", closedDt));
        strB.append(getTOString("customerNameCrValue", customerNameCrValue));
        strB.append(getTOString("customerIdCr", customerIdCr));
        strB.append(getTOString("txtProdId", txtProdId));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("pwd", pwd));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        //  strB.append(getTOString("pwdYes", pwdYes));
        strB.append(getTOString("si", si));
        //strB.append(getTOString("siYes", siYes));
        strB.append(getTOString("commAddrType", commAddrType));
        strB.append(getTOString("collectRentMM", collectRentMM));
        strB.append(getTOString("categoryId", categoryId));
        strB.append(getTOString("collectRentYYYY", collectRentYYYY));
        strB.append(getTOString("acctName", acctName));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("expDt", expDt));
        strB.append(getTOString("freezeStatus", freezeStatus));
        strB.append(getTOString("freezeRemarks", freezeRemarks));
        strB.append(getTOString("freezeDt", freezeDt));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("unFreezeStatus", unFreezeStatus));
        strB.append(getTOString("unFreezeRemarks", unFreezeRemarks));
        strB.append(getTOString("unFreezeDt", unFreezeDt));
        strB.append(getTOString("actNum", actNum));
        // strB.append(getTOString("customerIdAI", customerIdAI));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("locNum", locNum));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("createDt", createDt));
        strB.append(getTOXml("locStatusId", locStatusId));
        strB.append(getTOXml("locCatId", locCatId));
        strB.append(getTOXml("optModeId", optModeId));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("txtProdId", txtProdId));
        strB.append(getTOXml("settmtModeId", settmtModeId));
        strB.append(getTOXml("si", si));
        //strB.append(getTOXml("siNo", siNo));
        strB.append(getTOXml("pwd", pwd));
        //strB.append(getTOXml("pwdNo", pwdNo));
        // strB.append(getTOXml("customerIdAI", customerIdAI));
//		strB.append(getTOXml("effectiveBalance", effectiveBalance));
//		strB.append(getTOXml("availableBalance", availableBalance));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizationStatus", authorizationStatus));
        strB.append(getTOXml("closedBy", closedBy));
        strB.append(getTOXml("closedDt", closedDt));
        strB.append(getTOXml("customerIdCr", customerIdCr));
        strB.append(getTOXml("collectRentMM", collectRentMM));
        strB.append(getTOXml("collectRentYYYY", collectRentYYYY));
        strB.append(getTOXml("customerNameCrValue", customerNameCrValue));
        strB.append(getTOXml("authorizedDt", authorizedDt));
//		strB.append(getTOXml("actStatusDt", actStatusDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("expDt", expDt));
//		strB.append(getTOXml("freezeAmt", freezeAmt));
//		strB.append(getTOXml("totalBalance", totalBalance));
        strB.append(getTOXml("commAddrType", commAddrType));
//		strB.append(getTOXml("productAmt", productAmt));
        strB.append(getTOXml("categoryId", categoryId));
//		strB.append(getTOXml("flexiDepositAmt", flexiDepositAmt));
        strB.append(getTOXml("acctName", acctName));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("freezeStatus", freezeStatus));
        strB.append(getTOXml("freezeRemarks", freezeRemarks));
        strB.append(getTOXml("freezeDt", freezeDt));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("unFreezeStatus", unFreezeStatus));
        strB.append(getTOXml("unFreezeRemarks", unFreezeRemarks));
        strB.append(getTOXml("unFreezeDt", unFreezeDt));
        strB.append(getTOXml("actNum", actNum));
        //  strB.append(getToXml(""))
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property depositNo.
     *
     * @return Value of property depositNo.
     */
    public java.lang.String getDepositNo() {
        return depositNo;
    }

    /**
     * Setter for property depositNo.
     *
     * @param depositNo New value of property depositNo.
     */
    public void setDepositNo(java.lang.String depositNo) {
        this.depositNo = depositNo;
    }

    /**
     * Getter for property expDt.
     *
     * @return Value of property expDt.
     */
    public java.util.Date getExpDt() {
        return expDt;
    }

    /**
     * Setter for property expDt.
     *
     * @param expDt New value of property expDt.
     */
    public void setExpDt(java.util.Date expDt) {
        this.expDt = expDt;
    }

    public Date getFreezeDt() {
        return freezeDt;
    }

    public void setFreezeDt(Date freezeDt) {
        this.freezeDt = freezeDt;
    }

    public String getFreezeRemarks() {
        return freezeRemarks;
    }

    public void setFreezeRemarks(String freezeRemarks) {
        this.freezeRemarks = freezeRemarks;
    }

    public String getFreezeStatus() {
        return freezeStatus;
    }

    public void setFreezeStatus(String freezeStatus) {
        this.freezeStatus = freezeStatus;
    }

    public Date getUnFreezeDt() {
        return unFreezeDt;
    }

    public void setUnFreezeDt(Date unFreezeDt) {
        this.unFreezeDt = unFreezeDt;
    }

    public String getUnFreezeRemarks() {
        return unFreezeRemarks;
    }

    public void setUnFreezeRemarks(String unFreezeRemarks) {
        this.unFreezeRemarks = unFreezeRemarks;
    }

    public String getUnFreezeStatus() {
        return unFreezeStatus;
    }

    public void setUnFreezeStatus(String unFreezeStatus) {
        this.unFreezeStatus = unFreezeStatus;
    }

    public String getActNum() {
        return actNum;
    }

    public void setActNum(String actNum) {
        this.actNum = actNum;
    }
    
    
}