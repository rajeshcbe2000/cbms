/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriLoanProductInsuranceTO.java
 *
 * Created on July 2, 2009, 2:45 PM
 */
package com.see.truetransact.transferobject.product.loan.agriculturecard;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author abi
 */
public class AgriLoanProductInsuranceTO extends TransferObject implements Serializable {

    String insuranceType = "";
    String insuranceUnderScheme = "";
    String bankSharePremium = "";
    String customerSharePremium = "";
    String insuranceAmt = "";
    String prodId = "";
    String status = "";
    String slno = "";

    /**
     * Creates a new instance of AgriLoanProductInsuranceTO
     */
    public AgriLoanProductInsuranceTO() {
    }

    public String getKeyData() {
        setKeyColumns("prodId");
        return "prodId";
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(getTOStringStart(this.getClass().getName()));
        buffer.append(getTOStringKey(getKeyData()));
        buffer.append(getTOString("insuranceType", insuranceType));
        buffer.append(getTOString("insuranceUnderScheme", insuranceUnderScheme));
        buffer.append(getTOString("bankSharePremium", bankSharePremium));
        buffer.append(getTOString("customerSharePremium", customerSharePremium));
        buffer.append(getTOString("insuranceAmt", insuranceAmt));
        buffer.append(getTOString("prodId", prodId));
        buffer.append(getTOString("status", status));
        buffer.append(getTOString("slno", slno));
        buffer.append(getTOStringEnd());
        return buffer.toString();
    }

    public String toXML() {
        StringBuffer buffer = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        buffer.append(getTOXmlKey(getKeyData()));
        buffer.append(getTOXml("insuranceType", insuranceType));
        buffer.append(getTOXml("insuranceUnderScheme", insuranceUnderScheme));
        buffer.append(getTOXml("bankSharePremium", bankSharePremium));
        buffer.append(getTOXml("customerSharePremium", customerSharePremium));
        buffer.append(getTOXml("insuranceAmt", insuranceAmt));
        buffer.append(getTOXml("prodId", prodId));
        buffer.append(getTOXml("status", status));
        buffer.append(getTOXml("slno", slno));
        buffer.append(getTOXmlEnd());
        return buffer.toString();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    /**
     * Getter for property insuranceType.
     *
     * @return Value of property insuranceType.
     */
    public java.lang.String getInsuranceType() {
        return insuranceType;
    }

    /**
     * Setter for property insuranceType.
     *
     * @param insuranceType New value of property insuranceType.
     */
    public void setInsuranceType(java.lang.String insuranceType) {
        this.insuranceType = insuranceType;
    }

    /**
     * Getter for property bankSharePremium.
     *
     * @return Value of property bankSharePremium.
     */
    public java.lang.String getBankSharePremium() {
        return bankSharePremium;
    }

    /**
     * Setter for property bankSharePremium.
     *
     * @param bankSharePremium New value of property bankSharePremium.
     */
    public void setBankSharePremium(java.lang.String bankSharePremium) {
        this.bankSharePremium = bankSharePremium;
    }

    /**
     * Getter for property customerSharePremium.
     *
     * @return Value of property customerSharePremium.
     */
    public java.lang.String getCustomerSharePremium() {
        return customerSharePremium;
    }

    /**
     * Setter for property customerSharePremium.
     *
     * @param customerSharePremium New value of property customerSharePremium.
     */
    public void setCustomerSharePremium(java.lang.String customerSharePremium) {
        this.customerSharePremium = customerSharePremium;
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
     * Getter for property insuranceUnderScheme.
     *
     * @return Value of property insuranceUnderScheme.
     */
    public java.lang.String getInsuranceUnderScheme() {
        return insuranceUnderScheme;
    }

    /**
     * Setter for property insuranceUnderScheme.
     *
     * @param insuranceUnderScheme New value of property insuranceUnderScheme.
     */
    public void setInsuranceUnderScheme(java.lang.String insuranceUnderScheme) {
        this.insuranceUnderScheme = insuranceUnderScheme;
    }

    /**
     * Getter for property slno.
     *
     * @return Value of property slno.
     */
    public java.lang.String getSlno() {
        return slno;
    }

    /**
     * Setter for property slno.
     *
     * @param slno New value of property slno.
     */
    public void setSlno(java.lang.String slno) {
        this.slno = slno;
    }

    /**
     * Getter for property insuranceAmt.
     *
     * @return Value of property insuranceAmt.
     */
    public java.lang.String getInsuranceAmt() {
        return insuranceAmt;
    }

    /**
     * Setter for property insuranceAmt.
     *
     * @param insuranceAmt New value of property insuranceAmt.
     */
    public void setInsuranceAmt(java.lang.String insuranceAmt) {
        this.insuranceAmt = insuranceAmt;
    }
}
