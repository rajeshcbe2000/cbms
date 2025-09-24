/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSLetterGenerationTo.java
 * 
 * Created on Mon Jun 18 12:31:06 IST 2012
 */
package com.see.truetransact.transferobject.mdsapplication.mdslettergeneration;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is MDS_LETTOR_DETAILS.
 */
public class MDSLetterGenerationTO extends TransferObject implements Serializable {

    private String lettorNo = "";
    private String schemeName = "";
    private String chittalNo = "";
    private Double auctionAmount = null;
    private Date fromDt = null;
    private Date toDt = null;
    private String lettorNoVal1 = "";
    private String schemeName1 = "";
    private String chittalNo1 = "";
    private Double discountUpto = null;
    private Date conductedOnDt = null;
    private Integer noOfConsecMonth = null;
    private Date validUpto = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String cancel = "";
    private Date cancelDt = null;

    /**
     * Setter/Getter for LETTOR_NO - table Field
     */
    public void setLettorNo(String lettorNo) {
        this.lettorNo = lettorNo;
    }

    public String getLettorNo() {
        return lettorNo;
    }

    /**
     * Setter/Getter for SCHEME_NAME - table Field
     */
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSchemeName() {
        return schemeName;
    }

    /**
     * Setter/Getter for CHITTAL_NO - table Field
     */
    public void setChittalNo(String chittalNo) {
        this.chittalNo = chittalNo;
    }

    public String getChittalNo() {
        return chittalNo;
    }

    /**
     * Setter/Getter for AUCTION_AMOUNT - table Field
     */
    public void setAuctionAmount(Double auctionAmount) {
        this.auctionAmount = auctionAmount;
    }

    public Double getAuctionAmount() {
        return auctionAmount;
    }

    /**
     * Setter/Getter for FROM_DT - table Field
     */
    public void setFromDt(Date fromDt) {
        this.fromDt = fromDt;
    }

    public Date getFromDt() {
        return fromDt;
    }

    /**
     * Setter/Getter for TO_DT - table Field
     */
    public void setToDt(Date toDt) {
        this.toDt = toDt;
    }

    public Date getToDt() {
        return toDt;
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

    public String getLettorNoVal1() {
        return lettorNoVal1;
    }

    public void setLettorNoVal1(String lettorNoVal1) {
        this.lettorNoVal1 = lettorNoVal1;
    }

    public String getSchemeName1() {
        return schemeName1;
    }

    public void setSchemeName1(String schemeName1) {
        this.schemeName1 = schemeName1;
    }

    public String getChittalNo1() {
        return chittalNo1;
    }

    public void setChittalNo1(String chittalNo1) {
        this.chittalNo1 = chittalNo1;
    }

    public Date getConductedOnDt() {
        return conductedOnDt;
    }

    public void setConductedOnDt(Date conductedOnDt) {
        this.conductedOnDt = conductedOnDt;
    }

    public Date getValidUpto() {
        return validUpto;
    }

    public void setValidUpto(Date validUpto) {
        this.validUpto = validUpto;
    }

    public Double getDiscountUpto() {
        return discountUpto;
    }

    public void setDiscountUpto(Double discountUpto) {
        this.discountUpto = discountUpto;
    }

    public Integer getNoOfConsecMonth() {
        return noOfConsecMonth;
    }

    public void setNoOfConsecMonth(Integer noOfConsecMonth) {
        this.noOfConsecMonth = noOfConsecMonth;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public Date getCancelDt() {
        return cancelDt;
    }

    public void setCancelDt(Date cancelDt) {
        this.cancelDt = cancelDt;
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
        strB.append(getTOString("lettorNo", lettorNo));
        strB.append(getTOString("schemeName", schemeName));
        strB.append(getTOString("chittalNo", chittalNo));
        strB.append(getTOString("auctionAmount", auctionAmount));
        strB.append(getTOString("fromDt", fromDt));
        strB.append(getTOString("toDt", toDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("lettorNoVal1", lettorNoVal1));
        strB.append(getTOString("schemeName1", schemeName1));
        strB.append(getTOString("chittalNo1", chittalNo1));
        strB.append(getTOString("discountUpto", discountUpto));
        strB.append(getTOString("conductedOnDt", conductedOnDt));
        strB.append(getTOString("noOfConsecMonth", noOfConsecMonth));
        strB.append(getTOString("validUpto", validUpto));
        strB.append(getTOString("cancelDt", cancelDt));
        strB.append(getTOString("cancel", cancel));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("lettorNo", lettorNo));
        strB.append(getTOXml("schemeName", schemeName));
        strB.append(getTOXml("chittalNo", chittalNo));
        strB.append(getTOXml("auctionAmount", auctionAmount));
        strB.append(getTOXml("fromDt", fromDt));
        strB.append(getTOXml("toDt", toDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("lettorNoVal1", lettorNoVal1));
        strB.append(getTOXml("schemeName1", schemeName1));
        strB.append(getTOXml("chittalNo1", chittalNo1));
        strB.append(getTOXml("discountUpto", discountUpto));
        strB.append(getTOXml("conductedOnDt", conductedOnDt));
        strB.append(getTOXml("noOfConsecMonth", noOfConsecMonth));
        strB.append(getTOXml("validUpto", validUpto));
        strB.append(getTOXml("cancelDt", cancelDt));
        strB.append(getTOXml("cancel", cancel));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}