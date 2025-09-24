/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LodgementBillsTO.java
 * 
 * Created on Tue Apr 13 17:41:37 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.bills;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BILLS_LODGEMENT.
 */
public class LodgementBillsTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String billType = "";
    private String bankDetail = "";
    private String custId = "";
    private String borrowerNo = "";
    private String instrumentDetails = "";
    private String pSbrOther = "";
    private Double billAmt = null;
    private Double commission = null;
    private Double discount = null;
    private Double postage = null;
    private Double margin = null;
    private String ddDrawee = "";
    private String ddName = "";
    private String ddStreet = "";
    private String ddArea = "";
    private String ddCity = "";
    private String ddState = "";
    private String ddCountry = "";
    private String ddPincode = "";
    private String fdDrawee = "";
    private String fdName = "";
    private String fdStreet = "";
    private String fdArea = "";
    private String fdCity = "";
    private String fdState = "";
    private String fdCountry = "";
    private String fdPincode = "";
    private String status = "";
    private String lodgementId = "";

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
     * Setter/Getter for BILL_TYPE - table Field
     */
    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillType() {
        return billType;
    }

    /**
     * Setter/Getter for BANK_DETAIL - table Field
     */
    public void setBankDetail(String bankDetail) {
        this.bankDetail = bankDetail;
    }

    public String getBankDetail() {
        return bankDetail;
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
     * Setter/Getter for BORROWER_NO - table Field
     */
    public void setBorrowerNo(String borrowerNo) {
        this.borrowerNo = borrowerNo;
    }

    public String getBorrowerNo() {
        return borrowerNo;
    }

    /**
     * Setter/Getter for INSTRUMENT_DETAILS - table Field
     */
    public void setInstrumentDetails(String instrumentDetails) {
        this.instrumentDetails = instrumentDetails;
    }

    public String getInstrumentDetails() {
        return instrumentDetails;
    }

    /**
     * Setter/Getter for P_SBR_OTHER - table Field
     */
    public void setPSbrOther(String pSbrOther) {
        this.pSbrOther = pSbrOther;
    }

    public String getPSbrOther() {
        return pSbrOther;
    }

    /**
     * Setter/Getter for BILL_AMT - table Field
     */
    public void setBillAmt(Double billAmt) {
        this.billAmt = billAmt;
    }

    public Double getBillAmt() {
        return billAmt;
    }

    /**
     * Setter/Getter for COMMISSION - table Field
     */
    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getCommission() {
        return commission;
    }

    /**
     * Setter/Getter for DISCOUNT - table Field
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getDiscount() {
        return discount;
    }

    /**
     * Setter/Getter for POSTAGE - table Field
     */
    public void setPostage(Double postage) {
        this.postage = postage;
    }

    public Double getPostage() {
        return postage;
    }

    /**
     * Setter/Getter for MARGIN - table Field
     */
    public void setMargin(Double margin) {
        this.margin = margin;
    }

    public Double getMargin() {
        return margin;
    }

    /**
     * Setter/Getter for DD_DRAWEE - table Field
     */
    public void setDdDrawee(String ddDrawee) {
        this.ddDrawee = ddDrawee;
    }

    public String getDdDrawee() {
        return ddDrawee;
    }

    /**
     * Setter/Getter for DD_NAME - table Field
     */
    public void setDdName(String ddName) {
        this.ddName = ddName;
    }

    public String getDdName() {
        return ddName;
    }

    /**
     * Setter/Getter for DD_STREET - table Field
     */
    public void setDdStreet(String ddStreet) {
        this.ddStreet = ddStreet;
    }

    public String getDdStreet() {
        return ddStreet;
    }

    /**
     * Setter/Getter for DD_AREA - table Field
     */
    public void setDdArea(String ddArea) {
        this.ddArea = ddArea;
    }

    public String getDdArea() {
        return ddArea;
    }

    /**
     * Setter/Getter for DD_CITY - table Field
     */
    public void setDdCity(String ddCity) {
        this.ddCity = ddCity;
    }

    public String getDdCity() {
        return ddCity;
    }

    /**
     * Setter/Getter for DD_STATE - table Field
     */
    public void setDdState(String ddState) {
        this.ddState = ddState;
    }

    public String getDdState() {
        return ddState;
    }

    /**
     * Setter/Getter for DD_COUNTRY - table Field
     */
    public void setDdCountry(String ddCountry) {
        this.ddCountry = ddCountry;
    }

    public String getDdCountry() {
        return ddCountry;
    }

    /**
     * Setter/Getter for DD_PINCODE - table Field
     */
    public void setDdPincode(String ddPincode) {
        this.ddPincode = ddPincode;
    }

    public String getDdPincode() {
        return ddPincode;
    }

    /**
     * Setter/Getter for FD_DRAWEE - table Field
     */
    public void setFdDrawee(String fdDrawee) {
        this.fdDrawee = fdDrawee;
    }

    public String getFdDrawee() {
        return fdDrawee;
    }

    /**
     * Setter/Getter for FD_NAME - table Field
     */
    public void setFdName(String fdName) {
        this.fdName = fdName;
    }

    public String getFdName() {
        return fdName;
    }

    /**
     * Setter/Getter for FD_STREET - table Field
     */
    public void setFdStreet(String fdStreet) {
        this.fdStreet = fdStreet;
    }

    public String getFdStreet() {
        return fdStreet;
    }

    /**
     * Setter/Getter for FD_AREA - table Field
     */
    public void setFdArea(String fdArea) {
        this.fdArea = fdArea;
    }

    public String getFdArea() {
        return fdArea;
    }

    /**
     * Setter/Getter for FD_CITY - table Field
     */
    public void setFdCity(String fdCity) {
        this.fdCity = fdCity;
    }

    public String getFdCity() {
        return fdCity;
    }

    /**
     * Setter/Getter for FD_STATE - table Field
     */
    public void setFdState(String fdState) {
        this.fdState = fdState;
    }

    public String getFdState() {
        return fdState;
    }

    /**
     * Setter/Getter for FD_COUNTRY - table Field
     */
    public void setFdCountry(String fdCountry) {
        this.fdCountry = fdCountry;
    }

    public String getFdCountry() {
        return fdCountry;
    }

    /**
     * Setter/Getter for FD_PINCODE - table Field
     */
    public void setFdPincode(String fdPincode) {
        this.fdPincode = fdPincode;
    }

    public String getFdPincode() {
        return fdPincode;
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
     * Setter/Getter for LODGEMENT_ID - table Field
     */
    public void setLodgementId(String lodgementId) {
        this.lodgementId = lodgementId;
    }

    public String getLodgementId() {
        return lodgementId;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("prodId");
        return prodId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("billType", billType));
        strB.append(getTOString("bankDetail", bankDetail));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("borrowerNo", borrowerNo));
        strB.append(getTOString("instrumentDetails", instrumentDetails));
        strB.append(getTOString("pSbrOther", pSbrOther));
        strB.append(getTOString("billAmt", billAmt));
        strB.append(getTOString("commission", commission));
        strB.append(getTOString("discount", discount));
        strB.append(getTOString("postage", postage));
        strB.append(getTOString("margin", margin));
        strB.append(getTOString("ddDrawee", ddDrawee));
        strB.append(getTOString("ddName", ddName));
        strB.append(getTOString("ddStreet", ddStreet));
        strB.append(getTOString("ddArea", ddArea));
        strB.append(getTOString("ddCity", ddCity));
        strB.append(getTOString("ddState", ddState));
        strB.append(getTOString("ddCountry", ddCountry));
        strB.append(getTOString("ddPincode", ddPincode));
        strB.append(getTOString("fdDrawee", fdDrawee));
        strB.append(getTOString("fdName", fdName));
        strB.append(getTOString("fdStreet", fdStreet));
        strB.append(getTOString("fdArea", fdArea));
        strB.append(getTOString("fdCity", fdCity));
        strB.append(getTOString("fdState", fdState));
        strB.append(getTOString("fdCountry", fdCountry));
        strB.append(getTOString("fdPincode", fdPincode));
        strB.append(getTOString("status", status));
        strB.append(getTOString("lodgementId", lodgementId));
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
        strB.append(getTOXml("billType", billType));
        strB.append(getTOXml("bankDetail", bankDetail));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("borrowerNo", borrowerNo));
        strB.append(getTOXml("instrumentDetails", instrumentDetails));
        strB.append(getTOXml("pSbrOther", pSbrOther));
        strB.append(getTOXml("billAmt", billAmt));
        strB.append(getTOXml("commission", commission));
        strB.append(getTOXml("discount", discount));
        strB.append(getTOXml("postage", postage));
        strB.append(getTOXml("margin", margin));
        strB.append(getTOXml("ddDrawee", ddDrawee));
        strB.append(getTOXml("ddName", ddName));
        strB.append(getTOXml("ddStreet", ddStreet));
        strB.append(getTOXml("ddArea", ddArea));
        strB.append(getTOXml("ddCity", ddCity));
        strB.append(getTOXml("ddState", ddState));
        strB.append(getTOXml("ddCountry", ddCountry));
        strB.append(getTOXml("ddPincode", ddPincode));
        strB.append(getTOXml("fdDrawee", fdDrawee));
        strB.append(getTOXml("fdName", fdName));
        strB.append(getTOXml("fdStreet", fdStreet));
        strB.append(getTOXml("fdArea", fdArea));
        strB.append(getTOXml("fdCity", fdCity));
        strB.append(getTOXml("fdState", fdState));
        strB.append(getTOXml("fdCountry", fdCountry));
        strB.append(getTOXml("fdPincode", fdPincode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("lodgementId", lodgementId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}