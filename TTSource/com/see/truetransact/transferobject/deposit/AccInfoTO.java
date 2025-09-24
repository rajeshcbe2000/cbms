/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccInfoTO.java
 * 
 * Created on Tue Mar 08 17:34:48 IST 2005
 */
package com.see.truetransact.transferobject.deposit;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_ACINFO.
 */
public class AccInfoTO extends TransferObject implements Serializable {

    private String openingMode = "";
    private String prodId = "";
    private String custType = "";
    private String introRequired = "";
    private String custId = "";
    private String depositNo = "";
    private String settlementMode = "";
    private String poa = "";
    private String standingInstruct = "";
    private String constitution = "";
    private String addressType = "";
    private String category = "";
    private String fifteenhDeclare = "";
    private String remarks = "";
    private String panNumber = "";
    private String commAddress = "";
    private String authorizedSignatory = "";
    private String taxDeductions = "";
    private String nomineeDetails = "";
    private String status = "";
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String authorizeStatus = null;
    private String createdBy = "";
    private Date createdDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String branchId = "";
    private String renewalFromDeposit = null;
    private Double renewalCount = null;
    private String agentId = "";
    private String depositStatus = "";
    private String flexiActNum = "";
    private String deathClaim = "";
    private String autoRenewal = "";
    private String renewWithInt = "";
    private String matAlertRep = "";
    private String member = "";
    private String transOut = "";
    private Integer printingNo = 0;
    private String referenceNo = "";
    private String accZeroBalYN="";
    private String multipleDepositId = null;
    private String mdsGroup=null;
    private String mdsRemarks="";
    private String introducer="";
    private String depositGroup = ""; // Added by nithya on 27-09-2017 for group deposit

    public String getIntroducer() {
        return introducer;
    }

    public void setIntroducer(String introducer) {
        this.introducer = introducer;
    }

    public String getMdsRemarks() {
        return mdsRemarks;
    }

    public void setMdsRemarks(String mdsRemarks) {
        this.mdsRemarks = mdsRemarks;
    }

    public String getMdsGroup() {
        return mdsGroup;
    }

    public void setMdsGroup(String mdsGroup) {
        this.mdsGroup = mdsGroup;
    }

    public String getMultipleDepositId() {
        return multipleDepositId;
    }

    public void setMultipleDepositId(String multipleDepositId) {
        this.multipleDepositId = multipleDepositId;
    }

    public String getAccZeroBalYN() {
        return accZeroBalYN;
    }

    public void setAccZeroBalYN(String accZeroBalYN) {
        this.accZeroBalYN = accZeroBalYN;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    /**
     * Setter/Getter for OPENING_MODE - table Field
     */
    public void setOpeningMode(String openingMode) {
        this.openingMode = openingMode;
    }

    public String getOpeningMode() {
        return openingMode;
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
     * Setter/Getter for CUST_TYPE - table Field
     */
    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getCustType() {
        return custType;
    }

    /**
     * Setter/Getter for INTRO_REQUIRED - table Field
     */
    public void setIntroRequired(String introRequired) {
        this.introRequired = introRequired;
    }

    public String getIntroRequired() {
        return introRequired;
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
     * Setter/Getter for DEPOSIT_NO - table Field
     */
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositNo() {
        return depositNo;
    }

    /**
     * Setter/Getter for SETTLEMENT_MODE - table Field
     */
    public void setSettlementMode(String settlementMode) {
        this.settlementMode = settlementMode;
    }

    public String getSettlementMode() {
        return settlementMode;
    }

    /**
     * Setter/Getter for POA - table Field
     */
    public void setPoa(String poa) {
        this.poa = poa;
    }

    public String getPoa() {
        return poa;
    }

    /**
     * Setter/Getter for STANDING_INSTRUCT - table Field
     */
    public void setStandingInstruct(String standingInstruct) {
        this.standingInstruct = standingInstruct;
    }

    public String getStandingInstruct() {
        return standingInstruct;
    }

    /**
     * Setter/Getter for CONSTITUTION - table Field
     */
    public void setConstitution(String constitution) {
        this.constitution = constitution;
    }

    public String getConstitution() {
        return constitution;
    }

    /**
     * Setter/Getter for CATEGORY - table Field
     */
    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    /**
     * Setter/Getter for FIFTEENH_DECLARE - table Field
     */
    public void setFifteenhDeclare(String fifteenhDeclare) {
        this.fifteenhDeclare = fifteenhDeclare;
    }

    public String getFifteenhDeclare() {
        return fifteenhDeclare;
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
     * Setter/Getter for PAN_NUMBER - table Field
     */
    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    /**
     * Setter/Getter for COMM_ADDRESS - table Field
     */
    public void setCommAddress(String commAddress) {
        this.commAddress = commAddress;
    }

    public String getCommAddress() {
        return commAddress;
    }

    /**
     * Setter/Getter for AUTHORIZED_SIGNATORY - table Field
     */
    public void setAuthorizedSignatory(String authorizedSignatory) {
        this.authorizedSignatory = authorizedSignatory;
    }

    public String getAuthorizedSignatory() {
        return authorizedSignatory;
    }

    /**
     * Setter/Getter for TAX_DEDUCTIONS - table Field
     */
    public void setTaxDeductions(String taxDeductions) {
        this.taxDeductions = taxDeductions;
    }

    public String getTaxDeductions() {
        return taxDeductions;
    }

    /**
     * Setter/Getter for NOMINEE_DETAILS - table Field
     */
    public void setNomineeDetails(String nomineeDetails) {
        this.nomineeDetails = nomineeDetails;
    }

    public String getNomineeDetails() {
        return nomineeDetails;
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
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
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
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

    /**
     * Setter/Getter for RENEWAL_FROM_DEPOSIT - table Field
     */
    public void setRenewalFromDeposit(String renewalFromDeposit) {
        this.renewalFromDeposit = renewalFromDeposit;
    }

    public String getRenewalFromDeposit() {
        return renewalFromDeposit;
    }

    /**
     * Setter/Getter for RENEWAL_COUNT - table Field
     */
    public void setRenewalCount(Double renewalCount) {
        this.renewalCount = renewalCount;
    }

    public Double getRenewalCount() {
        return renewalCount;
    }

    /**
     * Setter/Getter for AGENT_ID - table Field
     */
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentId() {
        return agentId;
    }

    /**
     * Setter/Getter for DEPOSIT_STATUS - table Field
     */
    public void setDepositStatus(String depositStatus) {
        this.depositStatus = depositStatus;
    }

    public String getDepositStatus() {
        return depositStatus;
    }

    /**
     * Setter/Getter for FLEXI_ACT_NUM - table Field
     */
    public void setFlexiActNum(String flexiActNum) {
        this.flexiActNum = flexiActNum;
    }

    public String getFlexiActNum() {
        return flexiActNum;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("openingMode");
        return openingMode;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("openingMode", openingMode));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("custType", custType));
        strB.append(getTOString("introRequired", introRequired));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("settlementMode", settlementMode));
        strB.append(getTOString("poa", poa));
        strB.append(getTOString("standingInstruct", standingInstruct));
        strB.append(getTOString("constitution", constitution));
        strB.append(getTOString("addressType", addressType));
        strB.append(getTOString("category", category));
        strB.append(getTOString("fifteenhDeclare", fifteenhDeclare));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("panNumber", panNumber));
        strB.append(getTOString("commAddress", commAddress));
        strB.append(getTOString("authorizedSignatory", authorizedSignatory));
        strB.append(getTOString("taxDeductions", taxDeductions));
        strB.append(getTOString("nomineeDetails", nomineeDetails));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("renewalFromDeposit", renewalFromDeposit));
        strB.append(getTOString("renewalCount", renewalCount));
        strB.append(getTOString("agentId", agentId));
        strB.append(getTOString("depositStatus", depositStatus));
        strB.append(getTOString("flexiActNum", flexiActNum));
        strB.append(getTOString("deathClaim", deathClaim));
        strB.append(getTOString("autoRenewal", autoRenewal));
        strB.append(getTOString("renewWithInt", renewWithInt));
        strB.append(getTOString("matAlertRep", matAlertRep));
        strB.append(getTOString("member", member));
        strB.append(getTOString("transOut", transOut));
        strB.append(getTOString("printingNo", printingNo));
        strB.append(getTOString("referenceNo", referenceNo));
        strB.append(getTOString("accZeroBalYN", accZeroBalYN));
        strB.append(getTOString("multipleDepositId", multipleDepositId));
        strB.append(getTOString("mdsGroup", mdsGroup));
        strB.append(getTOString("mdsRemarks", mdsRemarks));
        strB.append(getTOString("introducer", introducer));
        strB.append(getTOString("depositGroup", depositGroup));        
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("openingMode", openingMode));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("custType", custType));
        strB.append(getTOXml("introRequired", introRequired));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("settlementMode", settlementMode));
        strB.append(getTOXml("poa", poa));
        strB.append(getTOXml("standingInstruct", standingInstruct));
        strB.append(getTOXml("constitution", constitution));
        strB.append(getTOXml("addressType", addressType));
        strB.append(getTOXml("category", category));
        strB.append(getTOXml("fifteenhDeclare", fifteenhDeclare));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("panNumber", panNumber));
        strB.append(getTOXml("commAddress", commAddress));
        strB.append(getTOXml("authorizedSignatory", authorizedSignatory));
        strB.append(getTOXml("taxDeductions", taxDeductions));
        strB.append(getTOXml("nomineeDetails", nomineeDetails));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("renewalFromDeposit", renewalFromDeposit));
        strB.append(getTOXml("renewalCount", renewalCount));
        strB.append(getTOXml("agentId", agentId));
        strB.append(getTOXml("depositStatus", depositStatus));
        strB.append(getTOXml("flexiActNum", flexiActNum));
        strB.append(getTOXml("deathClaim", deathClaim));
        strB.append(getTOXml("autoRenewal", autoRenewal));
        strB.append(getTOXml("renewWithInt", renewWithInt));
        strB.append(getTOXml("matAlertRep", matAlertRep));
        strB.append(getTOXml("member", member));
        strB.append(getTOXml("transOut", transOut));
        strB.append(getTOXml("printingNo", printingNo));
        strB.append(getTOXml("referenceNo", referenceNo));
        strB.append(getTOXml("accZeroBalYN", accZeroBalYN));
        strB.append(getTOXml("multipleDepositId", multipleDepositId));
        strB.append(getTOXml("mdsGroup", mdsGroup));
        strB.append(getTOXml("mdsRemarks", mdsRemarks));
        strB.append(getTOXml("introducer", introducer));
        strB.append(getTOXml("depositGroup", depositGroup));    
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property deathClaim.
     *
     * @return Value of property deathClaim.
     */
    public java.lang.String getDeathClaim() {
        return deathClaim;
    }

    /**
     * Setter for property deathClaim.
     *
     * @param deathClaim New value of property deathClaim.
     */
    public void setDeathClaim(java.lang.String deathClaim) {
        this.deathClaim = deathClaim;
    }

    /**
     * Getter for property autoRenewal.
     *
     * @return Value of property autoRenewal.
     */
    public java.lang.String getAutoRenewal() {
        return autoRenewal;
    }

    /**
     * Setter for property autoRenewal.
     *
     * @param autoRenewal New value of property autoRenewal.
     */
    public void setAutoRenewal(java.lang.String autoRenewal) {
        this.autoRenewal = autoRenewal;
    }

    /**
     * Getter for property renewWithInt.
     *
     * @return Value of property renewWithInt.
     */
    public java.lang.String getRenewWithInt() {
        return renewWithInt;
    }

    /**
     * Setter for property renewWithInt.
     *
     * @param renewWithInt New value of property renewWithInt.
     */
    public void setRenewWithInt(java.lang.String renewWithInt) {
        this.renewWithInt = renewWithInt;
    }

    /**
     * Getter for property matAlertRep.
     *
     * @return Value of property matAlertRep.
     */
    public java.lang.String getMatAlertRep() {
        return matAlertRep;
    }

    /**
     * Setter for property matAlertRep.
     *
     * @param matAlertRep New value of property matAlertRep.
     */
    public void setMatAlertRep(java.lang.String matAlertRep) {
        this.matAlertRep = matAlertRep;
    }

    /**
     * Getter for property member.
     *
     * @return Value of property member.
     */
    public java.lang.String getMember() {
        return member;
    }

    /**
     * Setter for property member.
     *
     * @param member New value of property member.
     */
    public void setMember(java.lang.String member) {
        this.member = member;
    }

    /**
     * Getter for property transOut.
     *
     * @return Value of property transOut.
     */
    public java.lang.String getTransOut() {
        return transOut;
    }

    /**
     * Setter for property transOut.
     *
     * @param transOut New value of property transOut.
     */
    public void setTransOut(java.lang.String transOut) {
        this.transOut = transOut;
    }

    public Integer getPrintingNo() {
        return printingNo;
    }

    public void setPrintingNo(Integer printingNo) {
        this.printingNo = printingNo;
    }

 

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }
    // Added by nithya on 27-09-2017 for group deposit
    public String getDepositGroup() {
        return depositGroup;
    }

    public void setDepositGroup(String depositGroup) {
        this.depositGroup = depositGroup;
    }
    
    
}
