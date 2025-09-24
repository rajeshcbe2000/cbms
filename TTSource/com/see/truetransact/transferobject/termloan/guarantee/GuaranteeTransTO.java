/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductLoanTO.java
 *
 * Created on Tue Mar 15 14:08:12 IST 2005
 */
package com.see.truetransact.transferobject.termloan.guarantee;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHARE_PROD_LOANS.
 */
public class GuaranteeTransTO extends TransferObject implements Serializable {

    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String cboPli = "";
    private String guaranteeNo = "";
    private Date transDt = null;
    private String batchID = "";
    private String transID = "";
    private String trnAmt = "";

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
        strB.append(getTOString("cboPli", cboPli));
        strB.append(getTOString("guaranteeNo", guaranteeNo));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("batchID", batchID));
        strB.append(getTOString("transID", transID));
        strB.append(getTOString("statusDt", statusDt));





        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("cboPli", cboPli));
        strB.append(getTOXml("guaranteeNo", guaranteeNo));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("batchID", batchID));
        strB.append(getTOXml("transID", transID));
        strB.append(getTOXml("trnAmt", trnAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property statusBy.
     *
     * @return Value of property statusBy.
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
    }

    /**
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public java.util.Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
    }

    /**
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    /**
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property authorizeDt.
     *
     * @return Value of property authorizeDt.
     */
    public java.util.Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter for property authorizeDt.
     *
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.util.Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    /**
     * Getter for property cboPli.
     *
     * @return Value of property cboPli.
     */
    public java.lang.String getCboPli() {
        return cboPli;
    }

    /**
     * Setter for property cboPli.
     *
     * @param cboPli New value of property cboPli.
     */
    public void setCboPli(java.lang.String cboPli) {
        this.cboPli = cboPli;
    }

    /**
     * Getter for property guaranteeNo.
     *
     * @return Value of property guaranteeNo.
     */
    public java.lang.String getGuaranteeNo() {
        return guaranteeNo;
    }

    /**
     * Setter for property guaranteeNo.
     *
     * @param guaranteeNo New value of property guaranteeNo.
     */
    public void setGuaranteeNo(java.lang.String guaranteeNo) {
        this.guaranteeNo = guaranteeNo;
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
     * Getter for property transID.
     *
     * @return Value of property transID.
     */
    public java.lang.String getTransID() {
        return transID;
    }

    /**
     * Setter for property transID.
     *
     * @param transID New value of property transID.
     */
    public void setTransID(java.lang.String transID) {
        this.transID = transID;
    }

    /**
     * Getter for property trnAmt.
     *
     * @return Value of property trnAmt.
     */
    public java.lang.String getTrnAmt() {
        return trnAmt;
    }

    /**
     * Setter for property trnAmt.
     *
     * @param trnAmt New value of property trnAmt.
     */
    public void setTrnAmt(java.lang.String trnAmt) {
        this.trnAmt = trnAmt;
    }

    /**
     * Getter for property batchID.
     *
     * @return Value of property batchID.
     */
    public java.lang.String getBatchID() {
        return batchID;
    }

    /**
     * Setter for property batchID.
     *
     * @param batchID New value of property batchID.
     */
    public void setBatchID(java.lang.String batchID) {
        this.batchID = batchID;
    }
}