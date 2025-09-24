/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ExternalWireTO.java
 * 
 * Created on Mon Jul 05 11:02:49 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PVT_EXTERNAL_WIRE.
 */
public class MDSMasterMaintenaceTO extends TransferObject implements Serializable {

    private String refId = "";
    private String memberId = "";
    private String entitleGrp = "";
    private String portfolioLoc = "";
    private String assetSubClass = "";
    private String debitAcct = "";
    private String wireTransCurrency = "";
    private Date execDt = null;
    private Date settlementDt = null;
    private Double debitAmt = null;
    private Double creditAmt = null;
    private String chrgPaidBy = "";
    private String stdCharges = "";
    private Double chrgAmt = null;
    private String chrgCcy = "";
    private String byOrderOf = "";
    private String routingCode = "";
    private String swiftCode = "";
    private String benefitName = "";
    private String benefitBank = "";
    private String benefitAcctNo = "";
    private String benefitBankCountry = "";
    private String benefitAddress = "";
    private String benefitCity = "";
    private String benefitState = "";
    private String benefitPin = "";
    private String corresBank = "";
    private String corresAddress = "";
    private String corresCity = "";
    private String corresState = "";
    private String corresCountry = "";
    private String corresPin = "";
    private String bankOffInstruct = "";
    private String traderInstruct = "";
    private String creditNotes = "";
    private String clientAdvices = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String authorizeStatus = null;;
    private String paymentDetails = "";

    /**
     * Setter/Getter for REF_ID - table Field
     */
    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getRefId() {
        return refId;
    }

    /**
     * Setter/Getter for MEMBER_ID - table Field
     */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberId() {
        return memberId;
    }

    /**
     * Setter/Getter for ENTITLE_GRP - table Field
     */
    public void setEntitleGrp(String entitleGrp) {
        this.entitleGrp = entitleGrp;
    }

    public String getEntitleGrp() {
        return entitleGrp;
    }

    /**
     * Setter/Getter for PORTFOLIO_LOC - table Field
     */
    public void setPortfolioLoc(String portfolioLoc) {
        this.portfolioLoc = portfolioLoc;
    }

    public String getPortfolioLoc() {
        return portfolioLoc;
    }

    /**
     * Setter/Getter for ASSET_SUB_CLASS - table Field
     */
    public void setAssetSubClass(String assetSubClass) {
        this.assetSubClass = assetSubClass;
    }

    public String getAssetSubClass() {
        return assetSubClass;
    }

    /**
     * Setter/Getter for DEBIT_ACCT - table Field
     */
    public void setDebitAcct(String debitAcct) {
        this.debitAcct = debitAcct;
    }

    public String getDebitAcct() {
        return debitAcct;
    }

    /**
     * Setter/Getter for WIRE_TRANS_CURRENCY - table Field
     */
    public void setWireTransCurrency(String wireTransCurrency) {
        this.wireTransCurrency = wireTransCurrency;
    }

    public String getWireTransCurrency() {
        return wireTransCurrency;
    }

    /**
     * Setter/Getter for EXEC_DT - table Field
     */
    public void setExecDt(Date execDt) {
        this.execDt = execDt;
    }

    public Date getExecDt() {
        return execDt;
    }

    /**
     * Setter/Getter for SETTLEMENT_DT - table Field
     */
    public void setSettlementDt(Date settlementDt) {
        this.settlementDt = settlementDt;
    }

    public Date getSettlementDt() {
        return settlementDt;
    }

    /**
     * Setter/Getter for DEBIT_AMT - table Field
     */
    public void setDebitAmt(Double debitAmt) {
        this.debitAmt = debitAmt;
    }

    public Double getDebitAmt() {
        return debitAmt;
    }

    /**
     * Setter/Getter for CREDIT_AMT - table Field
     */
    public void setCreditAmt(Double creditAmt) {
        this.creditAmt = creditAmt;
    }

    public Double getCreditAmt() {
        return creditAmt;
    }

    /**
     * Setter/Getter for CHRG_PAID_BY - table Field
     */
    public void setChrgPaidBy(String chrgPaidBy) {
        this.chrgPaidBy = chrgPaidBy;
    }

    public String getChrgPaidBy() {
        return chrgPaidBy;
    }

    /**
     * Setter/Getter for STD_CHARGES - table Field
     */
    public void setStdCharges(String stdCharges) {
        this.stdCharges = stdCharges;
    }

    public String getStdCharges() {
        return stdCharges;
    }

    /**
     * Setter/Getter for CHRG_AMT - table Field
     */
    public void setChrgAmt(Double chrgAmt) {
        this.chrgAmt = chrgAmt;
    }

    public Double getChrgAmt() {
        return chrgAmt;
    }

    /**
     * Setter/Getter for CHRG_CCY - table Field
     */
    public void setChrgCcy(String chrgCcy) {
        this.chrgCcy = chrgCcy;
    }

    public String getChrgCcy() {
        return chrgCcy;
    }

    /**
     * Setter/Getter for BY_ORDER_OF - table Field
     */
    public void setByOrderOf(String byOrderOf) {
        this.byOrderOf = byOrderOf;
    }

    public String getByOrderOf() {
        return byOrderOf;
    }

    /**
     * Setter/Getter for ROUTING_CODE - table Field
     */
    public void setRoutingCode(String routingCode) {
        this.routingCode = routingCode;
    }

    public String getRoutingCode() {
        return routingCode;
    }

    /**
     * Setter/Getter for SWIFT_CODE - table Field
     */
    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    /**
     * Setter/Getter for BENEFIT_NAME - table Field
     */
    public void setBenefitName(String benefitName) {
        this.benefitName = benefitName;
    }

    public String getBenefitName() {
        return benefitName;
    }

    /**
     * Setter/Getter for BENEFIT_BANK - table Field
     */
    public void setBenefitBank(String benefitBank) {
        this.benefitBank = benefitBank;
    }

    public String getBenefitBank() {
        return benefitBank;
    }

    /**
     * Setter/Getter for BENEFIT_ACCT_NO - table Field
     */
    public void setBenefitAcctNo(String benefitAcctNo) {
        this.benefitAcctNo = benefitAcctNo;
    }

    public String getBenefitAcctNo() {
        return benefitAcctNo;
    }

    /**
     * Setter/Getter for BENEFIT_BANK_COUNTRY - table Field
     */
    public void setBenefitBankCountry(String benefitBankCountry) {
        this.benefitBankCountry = benefitBankCountry;
    }

    public String getBenefitBankCountry() {
        return benefitBankCountry;
    }

    /**
     * Setter/Getter for BENEFIT_ADDRESS - table Field
     */
    public void setBenefitAddress(String benefitAddress) {
        this.benefitAddress = benefitAddress;
    }

    public String getBenefitAddress() {
        return benefitAddress;
    }

    /**
     * Setter/Getter for BENEFIT_CITY - table Field
     */
    public void setBenefitCity(String benefitCity) {
        this.benefitCity = benefitCity;
    }

    public String getBenefitCity() {
        return benefitCity;
    }

    /**
     * Setter/Getter for BENEFIT_STATE - table Field
     */
    public void setBenefitState(String benefitState) {
        this.benefitState = benefitState;
    }

    public String getBenefitState() {
        return benefitState;
    }

    /**
     * Setter/Getter for BENEFIT_PIN - table Field
     */
    public void setBenefitPin(String benefitPin) {
        this.benefitPin = benefitPin;
    }

    public String getBenefitPin() {
        return benefitPin;
    }

    /**
     * Setter/Getter for CORRES_BANK - table Field
     */
    public void setCorresBank(String corresBank) {
        this.corresBank = corresBank;
    }

    public String getCorresBank() {
        return corresBank;
    }

    /**
     * Setter/Getter for CORRES_ADDRESS - table Field
     */
    public void setCorresAddress(String corresAddress) {
        this.corresAddress = corresAddress;
    }

    public String getCorresAddress() {
        return corresAddress;
    }

    /**
     * Setter/Getter for CORRES_CITY - table Field
     */
    public void setCorresCity(String corresCity) {
        this.corresCity = corresCity;
    }

    public String getCorresCity() {
        return corresCity;
    }

    /**
     * Setter/Getter for CORRES_STATE - table Field
     */
    public void setCorresState(String corresState) {
        this.corresState = corresState;
    }

    public String getCorresState() {
        return corresState;
    }

    /**
     * Setter/Getter for CORRES_COUNTRY - table Field
     */
    public void setCorresCountry(String corresCountry) {
        this.corresCountry = corresCountry;
    }

    public String getCorresCountry() {
        return corresCountry;
    }

    /**
     * Setter/Getter for CORRES_PIN - table Field
     */
    public void setCorresPin(String corresPin) {
        this.corresPin = corresPin;
    }

    public String getCorresPin() {
        return corresPin;
    }

    /**
     * Setter/Getter for BANK_OFF_INSTRUCT - table Field
     */
    public void setBankOffInstruct(String bankOffInstruct) {
        this.bankOffInstruct = bankOffInstruct;
    }

    public String getBankOffInstruct() {
        return bankOffInstruct;
    }

    /**
     * Setter/Getter for TRADER_INSTRUCT - table Field
     */
    public void setTraderInstruct(String traderInstruct) {
        this.traderInstruct = traderInstruct;
    }

    public String getTraderInstruct() {
        return traderInstruct;
    }

    /**
     * Setter/Getter for CREDIT_NOTES - table Field
     */
    public void setCreditNotes(String creditNotes) {
        this.creditNotes = creditNotes;
    }

    public String getCreditNotes() {
        return creditNotes;
    }

    /**
     * Setter/Getter for CLIENT_ADVICES - table Field
     */
    public void setClientAdvices(String clientAdvices) {
        this.clientAdvices = clientAdvices;
    }

    public String getClientAdvices() {
        return clientAdvices;
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
     * Setter/Getter for PAYMENT_DETAILS - table Field
     */
    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("refId", refId));
        strB.append(getTOString("memberId", memberId));
        strB.append(getTOString("entitleGrp", entitleGrp));
        strB.append(getTOString("portfolioLoc", portfolioLoc));
        strB.append(getTOString("assetSubClass", assetSubClass));
        strB.append(getTOString("debitAcct", debitAcct));
        strB.append(getTOString("wireTransCurrency", wireTransCurrency));
        strB.append(getTOString("execDt", execDt));
        strB.append(getTOString("settlementDt", settlementDt));
        strB.append(getTOString("debitAmt", debitAmt));
        strB.append(getTOString("creditAmt", creditAmt));
        strB.append(getTOString("chrgPaidBy", chrgPaidBy));
        strB.append(getTOString("stdCharges", stdCharges));
        strB.append(getTOString("chrgAmt", chrgAmt));
        strB.append(getTOString("chrgCcy", chrgCcy));
        strB.append(getTOString("byOrderOf", byOrderOf));
        strB.append(getTOString("routingCode", routingCode));
        strB.append(getTOString("swiftCode", swiftCode));
        strB.append(getTOString("benefitName", benefitName));
        strB.append(getTOString("benefitBank", benefitBank));
        strB.append(getTOString("benefitAcctNo", benefitAcctNo));
        strB.append(getTOString("benefitBankCountry", benefitBankCountry));
        strB.append(getTOString("benefitAddress", benefitAddress));
        strB.append(getTOString("benefitCity", benefitCity));
        strB.append(getTOString("benefitState", benefitState));
        strB.append(getTOString("benefitPin", benefitPin));
        strB.append(getTOString("corresBank", corresBank));
        strB.append(getTOString("corresAddress", corresAddress));
        strB.append(getTOString("corresCity", corresCity));
        strB.append(getTOString("corresState", corresState));
        strB.append(getTOString("corresCountry", corresCountry));
        strB.append(getTOString("corresPin", corresPin));
        strB.append(getTOString("bankOffInstruct", bankOffInstruct));
        strB.append(getTOString("traderInstruct", traderInstruct));
        strB.append(getTOString("creditNotes", creditNotes));
        strB.append(getTOString("clientAdvices", clientAdvices));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("paymentDetails", paymentDetails));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("refId", refId));
        strB.append(getTOXml("memberId", memberId));
        strB.append(getTOXml("entitleGrp", entitleGrp));
        strB.append(getTOXml("portfolioLoc", portfolioLoc));
        strB.append(getTOXml("assetSubClass", assetSubClass));
        strB.append(getTOXml("debitAcct", debitAcct));
        strB.append(getTOXml("wireTransCurrency", wireTransCurrency));
        strB.append(getTOXml("execDt", execDt));
        strB.append(getTOXml("settlementDt", settlementDt));
        strB.append(getTOXml("debitAmt", debitAmt));
        strB.append(getTOXml("creditAmt", creditAmt));
        strB.append(getTOXml("chrgPaidBy", chrgPaidBy));
        strB.append(getTOXml("stdCharges", stdCharges));
        strB.append(getTOXml("chrgAmt", chrgAmt));
        strB.append(getTOXml("chrgCcy", chrgCcy));
        strB.append(getTOXml("byOrderOf", byOrderOf));
        strB.append(getTOXml("routingCode", routingCode));
        strB.append(getTOXml("swiftCode", swiftCode));
        strB.append(getTOXml("benefitName", benefitName));
        strB.append(getTOXml("benefitBank", benefitBank));
        strB.append(getTOXml("benefitAcctNo", benefitAcctNo));
        strB.append(getTOXml("benefitBankCountry", benefitBankCountry));
        strB.append(getTOXml("benefitAddress", benefitAddress));
        strB.append(getTOXml("benefitCity", benefitCity));
        strB.append(getTOXml("benefitState", benefitState));
        strB.append(getTOXml("benefitPin", benefitPin));
        strB.append(getTOXml("corresBank", corresBank));
        strB.append(getTOXml("corresAddress", corresAddress));
        strB.append(getTOXml("corresCity", corresCity));
        strB.append(getTOXml("corresState", corresState));
        strB.append(getTOXml("corresCountry", corresCountry));
        strB.append(getTOXml("corresPin", corresPin));
        strB.append(getTOXml("bankOffInstruct", bankOffInstruct));
        strB.append(getTOXml("traderInstruct", traderInstruct));
        strB.append(getTOXml("creditNotes", creditNotes));
        strB.append(getTOXml("clientAdvices", clientAdvices));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("paymentDetails", paymentDetails));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}