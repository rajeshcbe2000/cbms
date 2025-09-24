/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LodgementMasterTO.java
 * 
 * Created on Tue Mar 22 18:06:21 IST 2005
 */
package com.see.truetransact.transferobject.bills.lodgement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BILLS_LODGEMENT_MASTER.
 */
public class LodgementMasterTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String receivedFrom = "";
    private String recBranchId = "";
    private String recOtherBank = "";
    private String recOtherBranch = "";
    private String recName = "";
    private String recAddr = "";
    private String recCity = "";
    private String recState = "";
    private String recCountry = "";
    private String recPincode = "";
    private String reference = "";
    private String prodType = "";
    private String borrowProdId = "";
    private String borrowAcctNum = "";
    private String draweeNo = "";
    private String sendingTo = "";
    private String draweeBankCode = "";
    private String draweeBranchCode = "";
    private String draweeName = "";
    private String draweeBankName = "";
    private String draweeAddr = "";
    private String draweeCity = "";
    private String draweeState = "";
    private String draweeCountry = "";
    private String draweePincode = "";
    private String instrumentType = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String lodgementId = "";
    private String billsType = "";
    private String billsStatus = "";
    private String custCategory = "";
    private String tranType = "";
    private Double rateForDelay = null;
    private String cbpProdID = "";
    private String cbpActNum = "";
    private Date tdtRemittedDt = null;
    private String obcOther = null;
    private String billsClearing = "";
    private String billsNo = "";
    private String branchCode = "";
    private String isMultipleLodgement = ""; // Added by nithya on 27-06-2016 for multiple lodgement 
    public String getBillsNo() {
        return billsNo;
    }

    public void setBillsNo(String billsNo) {
        this.billsNo = billsNo;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBillsClearing() {
        return billsClearing;
    }

    public void setBillsClearing(String billsClearing) {
        this.billsClearing = billsClearing;
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
     * Setter/Getter for RECEIVED_FROM - table Field
     */
    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    /**
     * Setter/Getter for REC_BRANCH_ID - table Field
     */
    public void setRecBranchId(String recBranchId) {
        this.recBranchId = recBranchId;
    }

    public String getRecBranchId() {
        return recBranchId;
    }

    /**
     * Setter/Getter for REC_OTHER_BANK - table Field
     */
    public void setRecOtherBank(String recOtherBank) {
        this.recOtherBank = recOtherBank;
    }

    public String getRecOtherBank() {
        return recOtherBank;
    }

    /**
     * Setter/Getter for REC_OTHER_BRANCH - table Field
     */
    public void setRecOtherBranch(String recOtherBranch) {
        this.recOtherBranch = recOtherBranch;
    }

    public String getRecOtherBranch() {
        return recOtherBranch;
    }

    /**
     * Setter/Getter for REC_NAME - table Field
     */
    public void setRecName(String recName) {
        this.recName = recName;
    }

    public String getRecName() {
        return recName;
    }

    /**
     * Setter/Getter for REC_ADDR - table Field
     */
    public void setRecAddr(String recAddr) {
        this.recAddr = recAddr;
    }

    public String getRecAddr() {
        return recAddr;
    }

    /**
     * Setter/Getter for REC_CITY - table Field
     */
    public void setRecCity(String recCity) {
        this.recCity = recCity;
    }

    public String getRecCity() {
        return recCity;
    }

    /**
     * Setter/Getter for REC_STATE - table Field
     */
    public void setRecState(String recState) {
        this.recState = recState;
    }

    public String getRecState() {
        return recState;
    }

    /**
     * Setter/Getter for REC_COUNTRY - table Field
     */
    public void setRecCountry(String recCountry) {
        this.recCountry = recCountry;
    }

    public String getRecCountry() {
        return recCountry;
    }

    /**
     * Setter/Getter for REC_PINCODE - table Field
     */
    public void setRecPincode(String recPincode) {
        this.recPincode = recPincode;
    }

    public String getRecPincode() {
        return recPincode;
    }

    /**
     * Setter/Getter for REFERENCE - table Field
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    /**
     * Setter/Getter for PROD_TYPE - table Field
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdType() {
        return prodType;
    }

    /**
     * Setter/Getter for BORROW_PROD_ID - table Field
     */
    public void setBorrowProdId(String borrowProdId) {
        this.borrowProdId = borrowProdId;
    }

    public String getBorrowProdId() {
        return borrowProdId;
    }

    /**
     * Setter/Getter for BORROW_ACCT_NUM - table Field
     */
    public void setBorrowAcctNum(String borrowAcctNum) {
        this.borrowAcctNum = borrowAcctNum;
    }

    public String getBorrowAcctNum() {
        return borrowAcctNum;
    }

    /**
     * Setter/Getter for DRAWEE_NO - table Field
     */
    public void setDraweeNo(String draweeNo) {
        this.draweeNo = draweeNo;
    }

    public String getDraweeNo() {
        return draweeNo;
    }

    /**
     * Setter/Getter for SENDING_TO - table Field
     */
    public void setSendingTo(String sendingTo) {
        this.sendingTo = sendingTo;
    }

    public String getSendingTo() {
        return sendingTo;
    }

    /**
     * Setter/Getter for DRAWEE_BANK_CODE - table Field
     */
    public void setDraweeBankCode(String draweeBankCode) {
        this.draweeBankCode = draweeBankCode;
    }

    public String getDraweeBankCode() {
        return draweeBankCode;
    }

    /**
     * Setter/Getter for DRAWEE_BRANCH_CODE - table Field
     */
    public void setDraweeBranchCode(String draweeBranchCode) {
        this.draweeBranchCode = draweeBranchCode;
    }

    public String getDraweeBranchCode() {
        return draweeBranchCode;
    }

    /**
     * Setter/Getter for DRAWEE_NAME - table Field
     */
    public void setDraweeName(String draweeName) {
        this.draweeName = draweeName;
    }

    public String getDraweeName() {
        return draweeName;
    }

    /**
     * Setter/Getter for DRAWEE_ADDR - table Field
     */
    public void setDraweeAddr(String draweeAddr) {
        this.draweeAddr = draweeAddr;
    }

    public String getDraweeAddr() {
        return draweeAddr;
    }

    /**
     * Setter/Getter for DRAWEE_CITY - table Field
     */
    public void setDraweeCity(String draweeCity) {
        this.draweeCity = draweeCity;
    }

    public String getDraweeCity() {
        return draweeCity;
    }

    /**
     * Setter/Getter for DRAWEE_STATE - table Field
     */
    public void setDraweeState(String draweeState) {
        this.draweeState = draweeState;
    }

    public String getDraweeState() {
        return draweeState;
    }

    /**
     * Setter/Getter for DRAWEE_COUNTRY - table Field
     */
    public void setDraweeCountry(String draweeCountry) {
        this.draweeCountry = draweeCountry;
    }

    public String getDraweeCountry() {
        return draweeCountry;
    }

    /**
     * Setter/Getter for DRAWEE_PINCODE - table Field
     */
    public void setDraweePincode(String draweePincode) {
        this.draweePincode = draweePincode;
    }

    public String getDraweePincode() {
        return draweePincode;
    }

    /**
     * Setter/Getter for INSTRUMENT_TYPE - table Field
     */
    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    /**
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter/Getter for LODGEMENT_ID - table Field
     */
    public void setLodgementId(String lodgementId) {
        this.lodgementId = lodgementId;
    }

    public String getLodgementId() {
        return lodgementId;
    }

    /**
     * Setter/Getter for BILLS_TYPE - table Field
     */
    public void setBillsType(String billsType) {
        this.billsType = billsType;
    }

    public String getBillsType() {
        return billsType;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(lodgementId);
        return lodgementId;
    }

    // Added by nithya on 27-06-2016 for multiple lodgement 
    // Variable indicates whether the lodgement is single or multiple
    public String getIsMultipleLodgement() {
        return isMultipleLodgement;
    }

    public void setIsMultipleLodgement(String isMultipleLodgement) {
        this.isMultipleLodgement = isMultipleLodgement;
    }

    // End
    
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("receivedFrom", receivedFrom));
        strB.append(getTOString("recBranchId", recBranchId));
        strB.append(getTOString("recOtherBank", recOtherBank));
        strB.append(getTOString("recOtherBranch", recOtherBranch));
        strB.append(getTOString("recName", recName));
        strB.append(getTOString("recAddr", recAddr));
        strB.append(getTOString("recCity", recCity));
        strB.append(getTOString("recState", recState));
        strB.append(getTOString("recCountry", recCountry));
        strB.append(getTOString("recPincode", recPincode));
        strB.append(getTOString("reference", reference));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("borrowProdId", borrowProdId));
        strB.append(getTOString("borrowAcctNum", borrowAcctNum));
        strB.append(getTOString("draweeNo", draweeNo));
        strB.append(getTOString("sendingTo", sendingTo));
        strB.append(getTOString("draweeBankCode", draweeBankCode));
        strB.append(getTOString("draweeBranchCode", draweeBranchCode));
        strB.append(getTOString("draweeName", draweeName));
        strB.append(getTOString("draweeBankName", draweeBankName));
        strB.append(getTOString("draweeAddr", draweeAddr));
        strB.append(getTOString("draweeCity", draweeCity));
        strB.append(getTOString("draweeState", draweeState));
        strB.append(getTOString("draweeCountry", draweeCountry));
        strB.append(getTOString("draweePincode", draweePincode));
        strB.append(getTOString("instrumentType", instrumentType));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("lodgementId", lodgementId));
        strB.append(getTOString("billsType", billsType));
        strB.append(getTOString("billsStatus", billsStatus));
        strB.append(getTOString("custCategory", custCategory));
        strB.append(getTOString("tranType", tranType));
        strB.append(getTOString("rateForDelay", rateForDelay));
        strB.append(getTOString("cbpProdID", cbpProdID));
        strB.append(getTOString("cbpActNum", cbpActNum));
        strB.append(getTOString("tdtRemittedDt", tdtRemittedDt));
        strB.append(getTOString("obcOther", obcOther));
        strB.append(getTOString("billsClearing", billsClearing));
        strB.append(getTOString("billsNo", billsNo));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("isMultipleLodgement", isMultipleLodgement)); // Added by nithya on 27-06-2016 for multiple lodgement 
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("receivedFrom", receivedFrom));
        strB.append(getTOXml("recBranchId", recBranchId));
        strB.append(getTOXml("recOtherBank", recOtherBank));
        strB.append(getTOXml("recOtherBranch", recOtherBranch));
        strB.append(getTOXml("recName", recName));
        strB.append(getTOXml("recAddr", recAddr));
        strB.append(getTOXml("recCity", recCity));
        strB.append(getTOXml("recState", recState));
        strB.append(getTOXml("recCountry", recCountry));
        strB.append(getTOXml("recPincode", recPincode));
        strB.append(getTOXml("reference", reference));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("borrowProdId", borrowProdId));
        strB.append(getTOXml("borrowAcctNum", borrowAcctNum));
        strB.append(getTOXml("draweeNo", draweeNo));
        strB.append(getTOXml("sendingTo", sendingTo));
        strB.append(getTOXml("draweeBankCode", draweeBankCode));
        strB.append(getTOXml("draweeBranchCode", draweeBranchCode));
        strB.append(getTOXml("draweeName", draweeName));
        strB.append(getTOXml("draweeBankName", draweeBankName));
        strB.append(getTOXml("draweeAddr", draweeAddr));
        strB.append(getTOXml("draweeCity", draweeCity));
        strB.append(getTOXml("draweeState", draweeState));
        strB.append(getTOXml("draweeCountry", draweeCountry));
        strB.append(getTOXml("draweePincode", draweePincode));
        strB.append(getTOXml("instrumentType", instrumentType));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("lodgementId", lodgementId));
        strB.append(getTOXml("billsType", billsType));
        strB.append(getTOXml("billsStatus", billsStatus));
        strB.append(getTOXml("custCategory", custCategory));
        strB.append(getTOXml("tranType", tranType));
        strB.append(getTOXml("rateForDelay", rateForDelay));
        strB.append(getTOXml("cbpProdID", cbpProdID));
        strB.append(getTOXml("cbpActNum", cbpActNum));
        strB.append(getTOXml("tdtRemittedDt", tdtRemittedDt));
        strB.append(getTOXml("obcOther", obcOther));
        strB.append(getTOXml("billsClearing", billsClearing));
        strB.append(getTOXml("billsNo", billsNo));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("isMyltipleLodgement", isMultipleLodgement)); // Added by nithya on 27-06-2016 for multiple lodgement 
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property billsStatus.
     *
     * @return Value of property billsStatus.
     */
    public java.lang.String getBillsStatus() {
        return billsStatus;
    }

    /**
     * Setter for property billsStatus.
     *
     * @param billsStatus New value of property billsStatus.
     */
    public void setBillsStatus(java.lang.String billsStatus) {
        this.billsStatus = billsStatus;
    }

    /**
     * Getter for property custCategory.
     *
     * @return Value of property custCategory.
     */
    public java.lang.String getCustCategory() {
        return custCategory;
    }

    /**
     * Setter for property custCategory.
     *
     * @param custCategory New value of property custCategory.
     */
    public void setCustCategory(java.lang.String custCategory) {
        this.custCategory = custCategory;
    }

    /**
     * Getter for property Trantype.
     *
     * @return Value of property Trantype.
     */
    public java.lang.String getTranType() {
        return tranType;
    }

    /**
     * Setter for property Trantype.
     *
     * @param Trantype New value of property Trantype.
     */
    public void setTranType(java.lang.String tranType) {
        this.tranType = tranType;
    }

    /**
     * Getter for property rateForDelay.
     *
     * @return Value of property rateForDelay.
     */
    public java.lang.Double getRateForDelay() {
        return rateForDelay;
    }

    /**
     * Setter for property rateForDelay.
     *
     * @param rateForDelay New value of property rateForDelay.
     */
    public void setRateForDelay(java.lang.Double rateForDelay) {
        this.rateForDelay = rateForDelay;
    }

    /**
     * Getter for property cbpProdID.
     *
     * @return Value of property cbpProdID.
     */
    public java.lang.String getCbpProdID() {
        return cbpProdID;
    }

    /**
     * Setter for property cbpProdID.
     *
     * @param cbpProdID New value of property cbpProdID.
     */
    public void setCbpProdID(java.lang.String cbpProdID) {
        this.cbpProdID = cbpProdID;
    }

    /**
     * Getter for property cbpActNum.
     *
     * @return Value of property cbpActNum.
     */
    public java.lang.String getCbpActNum() {
        return cbpActNum;
    }

    /**
     * Setter for property cbpActNum.
     *
     * @param cbpActNum New value of property cbpActNum.
     */
    public void setCbpActNum(java.lang.String cbpActNum) {
        this.cbpActNum = cbpActNum;
    }

    /**
     * Getter for property draweeBankName.
     *
     * @return Value of property draweeBankName.
     */
    public java.lang.String getDraweeBankName() {
        return draweeBankName;
    }

    /**
     * Setter for property draweeBankName.
     *
     * @param draweeBankName New value of property draweeBankName.
     */
    public void setDraweeBankName(java.lang.String draweeBankName) {
        this.draweeBankName = draweeBankName;
    }

    /**
     * Getter for property tdtRemittedDt.
     *
     * @return Value of property tdtRemittedDt.
     */
    public java.util.Date getTdtRemittedDt() {
        return tdtRemittedDt;
    }

    /**
     * Setter for property tdtRemittedDt.
     *
     * @param tdtRemittedDt New value of property tdtRemittedDt.
     */
    public void setTdtRemittedDt(java.util.Date tdtRemittedDt) {
        this.tdtRemittedDt = tdtRemittedDt;
    }

    /**
     * Getter for property obcOther.
     *
     * @return Value of property obcOther.
     */
    public java.lang.String getObcOther() {
        return obcOther;
    }

    /**
     * Setter for property obcOther.
     *
     * @param obcOther New value of property obcOther.
     */
    public void setObcOther(java.lang.String obcOther) {
        this.obcOther = obcOther;
    }
}