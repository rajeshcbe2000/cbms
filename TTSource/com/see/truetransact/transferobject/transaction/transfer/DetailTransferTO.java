/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DetailTransferTO.java
 *
 * Created on August 19, 2003, 10:59 AM
 */
package com.see.truetransact.transferobject.transaction.transfer;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;

/**
 * DetailTransferTO Bean
 *
 * @author Pranav
 *
 * @modified Pinky
 */
public class DetailTransferTO extends TransferObject implements Serializable {

    private String transId;
    private String batchId;
    private String acHdDesc;
    private String actNum;
    private String actFName;
    private String actMName;
    private String actLName;
    private String transType;
    private Double amount;
    private String status;
    private String statusBy;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private String authorizeRemarks = "";
    private String transDt = "";

    /**
     * Creates a new instance of TxTransferTO
     */
    public DetailTransferTO() {
        transId = "";
        acHdDesc = "";
        actNum = "";
        actFName = "";
        actMName = "";
        actLName = "";
        transType = "";
        amount = new Double("0.0");
        status = "";
        transDt = "";
    }

    /**
     * Getter for property acHdDesc.
     *
     * @return Value of property acHdDesc.
     *
     */
    public java.lang.String getAcHdDesc() {
        return acHdDesc;
    }

    /**
     * Setter for property acHdDesc.
     *
     * @param acHdDesc New value of property acHdDesc.
     *
     */
    public void setAcHdDesc(java.lang.String acHdDesc) {
        this.acHdDesc = acHdDesc;
    }

    /**
     * Getter for property actFName.
     *
     * @return Value of property actFName.
     *
     */
    public java.lang.String getActFName() {
        return actFName;
    }

    /**
     * Setter for property actFName.
     *
     * @param actFName New value of property actFName.
     *
     */
    public void setActFName(java.lang.String actFName) {
        this.actFName = actFName;
    }

    /**
     * Getter for property actLName.
     *
     * @return Value of property actLName.
     *
     */
    public java.lang.String getActLName() {
        return actLName;
    }

    /**
     * Setter for property actLName.
     *
     * @param actLName New value of property actLName.
     *
     */
    public void setActLName(java.lang.String actLName) {
        this.actLName = actLName;
    }

    /**
     * Getter for property actMName.
     *
     * @return Value of property actMName.
     *
     */
    public java.lang.String getActMName() {
        return actMName;
    }

    /**
     * Setter for property actMName.
     *
     * @param actMName New value of property actMName.
     *
     */
    public void setActMName(java.lang.String actMName) {
        this.actMName = actMName;
    }

    /**
     * Getter for property actNum.
     *
     * @return Value of property actNum.
     *
     */
    public java.lang.String getActNum() {
        return actNum;
    }

    /**
     * Setter for property actNum.
     *
     * @param actNum New value of property actNum.
     *
     */
    public void setActNum(java.lang.String actNum) {
        this.actNum = actNum;
    }

    /**
     * Getter for property amount.
     *
     * @return Value of property amount.
     *
     */
    public java.lang.Double getAmount() {
        return amount;
    }

    /**
     * Setter for property amount.
     *
     * @param amount New value of property amount.
     *
     */
    public void setAmount(java.lang.Double amount) {
        this.amount = amount;
    }

    /**
     * Getter for property transId.
     *
     * @return Value of property transId.
     *
     */
    public java.lang.String getTransId() {
        return transId;
    }

    /**
     * Setter for property transId.
     *
     * @param transId New value of property transId.
     *
     */
    public void setTransId(java.lang.String transId) {
        this.transId = transId;
    }

    /**
     * Getter for property transType.
     *
     * @return Value of property transType.
     *
     */
    public java.lang.String getTransType() {
        return transType;
    }

    /**
     * Setter for property transType.
     *
     * @param transType New value of property transType.
     *
     */
    public void setTransType(java.lang.String transType) {
        this.transType = transType;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     *
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     *
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Getter for property batchId.
     *
     * @return Value of property batchId.
     *
     */
    public java.lang.String getBatchId() {
        return batchId;
    }

    /**
     * Setter for property batchId.
     *
     * @param batchId New value of property batchId.
     *
     */
    public void setBatchId(java.lang.String batchId) {
        this.batchId = batchId;
    }

    /**
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     *
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     *
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property authorizeRemarks.
     *
     * @return Value of property authorizeRemarks.
     *
     */
    public java.lang.String getAuthorizeRemarks() {
        return authorizeRemarks;
    }

    /**
     * Setter for property authorizeRemarks.
     *
     * @param authorizeRemarks New value of property authorizeRemarks.
     *
     */
    public void setAuthorizeRemarks(java.lang.String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    /**
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     *
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     *
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    /**
     * Getter for property statusBy.
     *
     * @return Value of property statusBy.
     *
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     *
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
    }

    public String getKeyData() {
        setKeyColumns("transId");
        return transId;
    }

    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("batchId", batchId));
        strB.append(getTOString("acHdDesc", acHdDesc));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("actFName", actFName));
        strB.append(getTOString("actMName", actMName));
        strB.append(getTOString("actLName", actLName));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("batchId", batchId));
        strB.append(getTOXml("acHdDesc", acHdDesc));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("actFName", actFName));
        strB.append(getTOXml("actMName", actMName));
        strB.append(getTOXml("actLName", actLName));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property transDt.
     *
     * @return Value of property transDt.
     */
    public java.lang.String getTransDt() {
        return transDt;
    }

    /**
     * Setter for property transDt.
     *
     * @param transDt New value of property transDt.
     */
    public void setTransDt(java.lang.String transDt) {
        this.transDt = transDt;
    }
}
