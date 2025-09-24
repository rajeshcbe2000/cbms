/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LodgementRemitTO.java
 *
 * Created on September 24, 2008, 11:43 AM
 */
package com.see.truetransact.transferobject.bills.lodgement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author Administrator
 */
public class LodgementRemitTO extends TransferObject implements Serializable {

    private String remitProdId = "";
    private String remitCity = "";
    private String remitDraweeBank = "";
    private String remitDraweeBranchCode = "";
    private Date remitInstDt = null;
    private String remitFavouring = "";
    private String remitFavouringIn = "";
    private Double remitInstAmt = 0.0;
    private String remitStatus = "";
    private String remitInst1 = "";
    private String remitInst2 = "";
    private String lodgeID = "";
    private String billActivity = "";

    public String getKeyData() {
        setKeyColumns(lodgeID);
        return lodgeID;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("remitProdId", remitProdId));
        strB.append(getTOString("remitCity", remitCity));
        strB.append(getTOString("remitDraweeBank", remitDraweeBank));
        strB.append(getTOString("remitDraweeBranchCode", remitDraweeBranchCode));
        strB.append(getTOString("remitFavouring", remitFavouring));
        strB.append(getTOString("remitInstAmt", remitInstAmt));
        strB.append(getTOString("remitStatus", remitStatus));
        strB.append(getTOString("remitInst1", remitInst1));
        strB.append(getTOString("remitInst2", remitInst2));
        strB.append(getTOString("lodgeID", lodgeID));
        strB.append(getTOString("remitInstDt", remitInstDt));
        strB.append(getTOString("billActivity", billActivity));
        strB.append(getTOString("remitFavouringIn", remitFavouringIn));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("remitProdId", remitProdId));
        strB.append(getTOXml("remitCity", remitCity));
        strB.append(getTOXml("remitDraweeBank", remitDraweeBank));
        strB.append(getTOXml("remitDraweeBranchCode", remitDraweeBranchCode));
        strB.append(getTOXml("remitFavouring", remitFavouring));
        strB.append(getTOXml("remitInstAmt", remitInstAmt));
        strB.append(getTOXml("remitStatus", remitStatus));
        strB.append(getTOXml("remitInst1", remitInst1));
        strB.append(getTOXml("remitInst2", remitInst2));
        strB.append(getTOXml("lodgeID", lodgeID));
        strB.append(getTOXml("remitInstDt", remitInstDt));
        strB.append(getTOXml("billActivity", billActivity));
        strB.append(getTOXml("remitFavouringIn", remitFavouringIn));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property remitProdId.
     *
     * @return Value of property remitProdId.
     */
    public java.lang.String getRemitProdId() {
        return remitProdId;
    }

    /**
     * Setter for property remitProdId.
     *
     * @param remitProdId New value of property remitProdId.
     */
    public void setRemitProdId(java.lang.String remitProdId) {
        this.remitProdId = remitProdId;
    }

    /**
     * Getter for property remitCity.
     *
     * @return Value of property remitCity.
     */
    public java.lang.String getRemitCity() {
        return remitCity;
    }

    /**
     * Setter for property remitCity.
     *
     * @param remitCity New value of property remitCity.
     */
    public void setRemitCity(java.lang.String remitCity) {
        this.remitCity = remitCity;
    }

    /**
     * Getter for property remitDraweeBank.
     *
     * @return Value of property remitDraweeBank.
     */
    public java.lang.String getRemitDraweeBank() {
        return remitDraweeBank;
    }

    /**
     * Setter for property remitDraweeBank.
     *
     * @param remitDraweeBank New value of property remitDraweeBank.
     */
    public void setRemitDraweeBank(java.lang.String remitDraweeBank) {
        this.remitDraweeBank = remitDraweeBank;
    }

    /**
     * Getter for property remitDraweeBranchCode.
     *
     * @return Value of property remitDraweeBranchCode.
     */
    public java.lang.String getRemitDraweeBranchCode() {
        return remitDraweeBranchCode;
    }

    /**
     * Setter for property remitDraweeBranchCode.
     *
     * @param remitDraweeBranchCode New value of property remitDraweeBranchCode.
     */
    public void setRemitDraweeBranchCode(java.lang.String remitDraweeBranchCode) {
        this.remitDraweeBranchCode = remitDraweeBranchCode;
    }

    /**
     * Getter for property remitFavouring.
     *
     * @return Value of property remitFavouring.
     */
    public java.lang.String getRemitFavouring() {
        return remitFavouring;
    }

    /**
     * Setter for property remitFavouring.
     *
     * @param remitFavouring New value of property remitFavouring.
     */
    public void setRemitFavouring(java.lang.String remitFavouring) {
        this.remitFavouring = remitFavouring;
    }

    /**
     * Getter for property remitStatus.
     *
     * @return Value of property remitStatus.
     */
    public java.lang.String getRemitStatus() {
        return remitStatus;
    }

    /**
     * Setter for property remitStatus.
     *
     * @param remitStatus New value of property remitStatus.
     */
    public void setRemitStatus(java.lang.String remitStatus) {
        this.remitStatus = remitStatus;
    }

    /**
     * Getter for property remitInst1.
     *
     * @return Value of property remitInst1.
     */
    public java.lang.String getRemitInst1() {
        return remitInst1;
    }

    /**
     * Setter for property remitInst1.
     *
     * @param remitInst1 New value of property remitInst1.
     */
    public void setRemitInst1(java.lang.String remitInst1) {
        this.remitInst1 = remitInst1;
    }

    /**
     * Getter for property remitInst2.
     *
     * @return Value of property remitInst2.
     */
    public java.lang.String getRemitInst2() {
        return remitInst2;
    }

    /**
     * Setter for property remitInst2.
     *
     * @param remitInst2 New value of property remitInst2.
     */
    public void setRemitInst2(java.lang.String remitInst2) {
        this.remitInst2 = remitInst2;
    }

    /**
     * Getter for property lodgeID.
     *
     * @return Value of property lodgeID.
     */
    public java.lang.String getLodgeID() {
        return lodgeID;
    }

    /**
     * Setter for property lodgeID.
     *
     * @param lodgeID New value of property lodgeID.
     */
    public void setLodgeID(java.lang.String lodgeID) {
        this.lodgeID = lodgeID;
    }

    public Double getRemitInstAmt() {
        return remitInstAmt;
    }

    public void setRemitInstAmt(Double remitInstAmt) {
        this.remitInstAmt = remitInstAmt;
    }

    

    /**
     * Getter for property remitInstDt.
     *
     * @return Value of property remitInstDt.
     */
    public java.util.Date getRemitInstDt() {
        return remitInstDt;
    }

    /**
     * Setter for property remitInstDt.
     *
     * @param remitInstDt New value of property remitInstDt.
     */
    public void setRemitInstDt(java.util.Date remitInstDt) {
        this.remitInstDt = remitInstDt;
    }

    /**
     * Getter for property billActivity.
     *
     * @return Value of property billActivity.
     */
    public java.lang.String getBillActivity() {
        return billActivity;
    }

    /**
     * Setter for property billActivity.
     *
     * @param billActivity New value of property billActivity.
     */
    public void setBillActivity(java.lang.String billActivity) {
        this.billActivity = billActivity;
    }

    /**
     * Getter for property remitFavouringIn.
     *
     * @return Value of property remitFavouringIn.
     */
    public java.lang.String getRemitFavouringIn() {
        return remitFavouringIn;
    }

    /**
     * Setter for property remitFavouringIn.
     *
     * @param remitFavouringIn New value of property remitFavouringIn.
     */
    public void setRemitFavouringIn(java.lang.String remitFavouringIn) {
        this.remitFavouringIn = remitFavouringIn;
    }
}
