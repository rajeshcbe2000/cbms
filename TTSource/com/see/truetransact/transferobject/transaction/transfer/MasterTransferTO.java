/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MasterlTransferTO.java
 *
 * Created on August 19, 2003, 10:59 AM
 */
package com.see.truetransact.transferobject.transaction.transfer;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * MasterlTransferTO Bean
 *
 * @author Pranav
 *
 * @modified Pinky
 */
public class MasterTransferTO extends TransferObject implements Serializable {

    private String batchId;
    private String instCr;
    private Double amountCr;
    private String instDr;
    private Double amountDr;
    private String status;
    private String statusBy;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private String authorizeRemarks = "";
    private String createdBy = "";
    private String initBran = "";
    private Date transDt = null;

    /**
     * Creates a new instance of TxTransferTO
     */
    public MasterTransferTO() {
        batchId = "";
        instCr = "";
        amountCr = new Double("0.0");
        instDr = "";
        amountDr = new Double("0.0");
        transDt = null;
        initBran = "";
    }

    /**
     * Getter for property amountCr.
     *
     * @return Value of property amountCr.
     *
     */
    public java.lang.Double getAmountCr() {
        return amountCr;
    }

    /**
     * Setter for property amountCr.
     *
     * @param amountCr New value of property amountCr.
     *
     */
    public void setAmountCr(java.lang.Double amountCr) {
        this.amountCr = amountCr;
    }

    /**
     * Getter for property amountDr.
     *
     * @return Value of property amountDr.
     *
     */
    public java.lang.Double getAmountDr() {
        return amountDr;
    }

    /**
     * Setter for property amountDr.
     *
     * @param amountDr New value of property amountDr.
     *
     */
    public void setAmountDr(java.lang.Double amountDr) {
        this.amountDr = amountDr;
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
     * Getter for property instCr.
     *
     * @return Value of property instCr.
     *
     */
    public java.lang.String getInstCr() {
        return instCr;
    }

    /**
     * Setter for property instCr.
     *
     * @param instCr New value of property instCr.
     *
     */
    public void setInstCr(java.lang.String instCr) {
        this.instCr = instCr;
    }

    /**
     * Getter for property instDr.
     *
     * @return Value of property instDr.
     *
     */
    public java.lang.String getInstDr() {
        return instDr;
    }

    /**
     * Setter for property instDr.
     *
     * @param instDr New value of property instDr.
     *
     */
    public void setInstDr(java.lang.String instDr) {
        this.instDr = instDr;
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
        setKeyColumns("batchId");
        return batchId;
    }

    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("batchId", batchId));
        strB.append(getTOString("instCr", instCr));
        strB.append(getTOString("amountCr", amountCr));
        strB.append(getTOString("instDr", instDr));
        strB.append(getTOString("amountDr", amountDr));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("initBran", initBran));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("batchId", batchId));
        strB.append(getTOXml("amountCr", amountCr));
        strB.append(getTOXml("amountDr", amountDr));
        strB.append(getTOXml("instCr", instCr));
        strB.append(getTOXml("instDr", instDr));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("initBran", initBran));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property createdBy.
     *
     * @return Value of property createdBy.
     *
     */
    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for property createdBy.
     *
     * @param createdBy New value of property createdBy.
     *
     */
    public void setCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Getter for property transDt.
     *
     * @return Value of property transDt.
     */
    public java.util.Date getTransDt() {
        return transDt;
    }

    /**
     * Setter for property transDt.
     *
     * @param transDt New value of property transDt.
     */
    public void setTransDt(java.util.Date transDt) {
        this.transDt = transDt;
    }

    /**
     * Getter for property initBran.
     *
     * @return Value of property initBran.
     */
    public java.lang.String getInitBran() {
        return initBran;
    }

    /**
     * Setter for property initBran.
     *
     * @param initBran New value of property initBran.
     */
    public void setInitBran(java.lang.String initBran) {
        this.initBran = initBran;
    }
}
