/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LockerProdChargesTO.java
 *
 * Created on August 6, 2007, 4:00 PM
 */
package com.see.truetransact.transferobject.product.locker;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author vinay
 */
public class LockerProdChargesTO extends TransferObject implements Serializable {

    private String prodID = "";
    private String instrumentType = "";
    private String chargeType = "";
    private String custCategory = "";
    private Date startDate = null;
    private Date endDate = null;
    private Double fromSlab = null;
    private Double toSlab = null;
    private Double commision = null;
    private Double serviceTax = null;
    private Double fixedRate = null;
    private Double forEveryAmt = null;
    private Double forEveryRate = null;
    private String rateType = "";
    private String status = "";
    private String authorizeStatus = null;;
    private String authorizeUser = "";
    private Date authorizeDt = null;

    /**
     * Creates a new instance of BillsChargesTO
     */
    public LockerProdChargesTO() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    /**
     * Getter for property instrumentType.
     *
     * @return Value of property instrumentType.
     */
    public java.lang.String getInstrumentType() {
        return instrumentType;
    }

    /**
     * Setter for property instrumentType.
     *
     * @param instrumentType New value of property instrumentType.
     */
    public void setInstrumentType(java.lang.String instrumentType) {
        this.instrumentType = instrumentType;
    }

    /**
     * Getter for property chargeType.
     *
     * @return Value of property chargeType.
     */
    public java.lang.String getChargeType() {
        return chargeType;
    }

    /**
     * Setter for property chargeType.
     *
     * @param chargeType New value of property chargeType.
     */
    public void setChargeType(java.lang.String chargeType) {
        this.chargeType = chargeType;
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
     * Getter for property startDate.
     *
     * @return Value of property startDate.
     */
    public java.util.Date getStartDate() {
        return startDate;
    }

    /**
     * Setter for property startDate.
     *
     * @param startDate New value of property startDate.
     */
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for property endDate.
     *
     * @return Value of property endDate.
     */
    public java.util.Date getEndDate() {
        return endDate;
    }

    /**
     * Setter for property endDate.
     *
     * @param endDate New value of property endDate.
     */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Getter for property fromSlab.
     *
     * @return Value of property fromSlab.
     */
    public java.lang.Double getFromSlab() {
        return fromSlab;
    }

    /**
     * Setter for property fromSlab.
     *
     * @param fromSlab New value of property fromSlab.
     */
    public void setFromSlab(java.lang.Double fromSlab) {
        this.fromSlab = fromSlab;
    }

    /**
     * Getter for property toSlab.
     *
     * @return Value of property toSlab.
     */
    public java.lang.Double getToSlab() {
        return toSlab;
    }

    /**
     * Setter for property toSlab.
     *
     * @param toSlab New value of property toSlab.
     */
    public void setToSlab(java.lang.Double toSlab) {
        this.toSlab = toSlab;
    }

    /**
     * Getter for property commision.
     *
     * @return Value of property commision.
     */
    public java.lang.Double getCommision() {
        return commision;
    }

    /**
     * Setter for property commision.
     *
     * @param commision New value of property commision.
     */
    public void setCommision(java.lang.Double commision) {
        this.commision = commision;
    }

    /**
     * Getter for property fixedRate.
     *
     * @return Value of property fixedRate.
     */
    public java.lang.Double getFixedRate() {
        return fixedRate;
    }

    /**
     * Setter for property fixedRate.
     *
     * @param fixedRate New value of property fixedRate.
     */
    public void setFixedRate(java.lang.Double fixedRate) {
        this.fixedRate = fixedRate;
    }

    /**
     * Getter for property forEveryAmt.
     *
     * @return Value of property forEveryAmt.
     */
    public java.lang.Double getForEveryAmt() {
        return forEveryAmt;
    }

    /**
     * Setter for property forEveryAmt.
     *
     * @param forEveryAmt New value of property forEveryAmt.
     */
    public void setForEveryAmt(java.lang.Double forEveryAmt) {
        this.forEveryAmt = forEveryAmt;
    }

    /**
     * Getter for property forEveryRate.
     *
     * @return Value of property forEveryRate.
     */
    public java.lang.Double getForEveryRate() {
        return forEveryRate;
    }

    /**
     * Setter for property forEveryRate.
     *
     * @param forEveryRate New value of property forEveryRate.
     */
    public void setForEveryRate(java.lang.Double forEveryRate) {
        this.forEveryRate = forEveryRate;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeUser(String authorizeUser) {
        this.authorizeUser = authorizeUser;
    }

    public String getAuthorizeUser() {
        return authorizeUser;
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
        strB.append(getTOString("prodID", prodID));
        strB.append(getTOString("instrumentType", instrumentType));
        strB.append(getTOString("chargeType", chargeType));
        strB.append(getTOString("custCategory", custCategory));
        strB.append(getTOString("startDate", startDate));
        strB.append(getTOString("endDate", endDate));
        strB.append(getTOString("fromSlab", fromSlab));
        strB.append(getTOString("toSlab", toSlab));
        strB.append(getTOString("commision", commision));
        strB.append(getTOString("serviceTax", serviceTax));
        strB.append(getTOString("fixedRate", fixedRate));
        strB.append(getTOString("forEveryAmt", forEveryAmt));
        strB.append(getTOString("forEveryRate", forEveryRate));
        strB.append(getTOString("rateType", rateType));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeUser", authorizeUser));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodID", prodID));
        strB.append(getTOXml("instrumentType", instrumentType));
        strB.append(getTOXml("chargeType", chargeType));
        strB.append(getTOXml("custCategory", custCategory));
        strB.append(getTOXml("startDate", startDate));
        strB.append(getTOXml("endDate", endDate));
        strB.append(getTOXml("fromSlab", fromSlab));
        strB.append(getTOXml("toSlab", toSlab));
        strB.append(getTOXml("fixedRate", fixedRate));
        strB.append(getTOXml("commision", commision));
        strB.append(getTOXml("serviceTax", serviceTax));
        strB.append(getTOXml("forEveryAmt", forEveryAmt));
        strB.append(getTOXml("forEveryRate", forEveryRate));
        strB.append(getTOXml("rateType", rateType));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeUser", authorizeUser));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property prodID.
     *
     * @return Value of property prodID.
     */
    public java.lang.String getProdID() {
        return prodID;
    }

    /**
     * Setter for property prodID.
     *
     * @param prodID New value of property prodID.
     */
    public void setProdID(java.lang.String prodID) {
        this.prodID = prodID;
    }

    /**
     * Getter for property rateType.
     *
     * @return Value of property rateType.
     */
    public java.lang.String getRateType() {
        return rateType;
    }

    /**
     * Setter for property rateType.
     *
     * @param rateType New value of property rateType.
     */
    public void setRateType(java.lang.String rateType) {
        this.rateType = rateType;
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
     * Getter for property serviceTax.
     *
     * @return Value of property serviceTax.
     */
    public java.lang.Double getServiceTax() {
        return serviceTax;
    }

    /**
     * Setter for property serviceTax.
     *
     * @param serviceTax New value of property serviceTax.
     */
    public void setServiceTax(java.lang.Double serviceTax) {
        this.serviceTax = serviceTax;
    }
}
