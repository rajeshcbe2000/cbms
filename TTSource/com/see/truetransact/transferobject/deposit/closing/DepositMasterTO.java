/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositMasterTO.java
 * 
 * Created on Thu May 20 15:47:34 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit.closing;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_ACINFO.
 */
public class DepositMasterTO extends TransferObject implements Serializable {

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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("depositNo");
        return "";
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
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}