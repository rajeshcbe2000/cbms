/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SupplierMasterTO.java
 * 
 * Created on Fri Jun 10 17:48:05 IST 2011
 */
package com.see.truetransact.transferobject.suspenseaccount;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SUPPPLIER_MASTER.
 */
public class SuspenseAccountProductTO extends TransferObject implements Serializable {

    private String txtSuspenseProductHead = "";
    private String txtSuspenseProdName = "";
    private String txtSuspenseProdID = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String txtPrefix = "";
    private String authorizeStatus = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorize = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String chkNegBalnce="N";
    private String loanBehaviour = "N";
    private String intAcHd = "";
    private Double intRate = 0.0;

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
        strB.append(getTOString("txtSuspenseProductHead", txtSuspenseProductHead));
        strB.append(getTOString("txtSuspenseProdName", txtSuspenseProdName));
        strB.append(getTOString("txtSuspenseProdID", txtSuspenseProdID));
        strB.append(getTOString("txtPrefix", txtPrefix));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorize", authorize));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("chkNegBalnce", chkNegBalnce));
        strB.append(getTOString("loanBehaviour", loanBehaviour));
        strB.append(getTOString("intAcHd", intAcHd));
        strB.append(getTOString("intRate", intRate));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("txtSuspenseProductHead", txtSuspenseProductHead));
        strB.append(getTOXml("txtSuspenseProdName", txtSuspenseProdName));
        strB.append(getTOXml("txtSuspenseProdID", txtSuspenseProdID));
        strB.append(getTOXml("txtPrefix", txtPrefix));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorize", authorize));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("chkNegBalnce", chkNegBalnce));
        strB.append(getTOXml("loanBehaviour", loanBehaviour));
        strB.append(getTOXml("intAcHd", intAcHd));
        strB.append(getTOXml("intRate", intRate));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getChkNegBalnce() {
        return chkNegBalnce;
    }

    public void setChkNegBalnce(String chkNegBalnce) {
        this.chkNegBalnce = chkNegBalnce;
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
     * Getter for property authorize.
     *
     * @return Value of property authorize.
     */
    public java.lang.String getAuthorize() {
        return authorize;
    }

    /**
     * Setter for property authorize.
     *
     * @param authorize New value of property authorize.
     */
    public void setAuthorize(java.lang.String authorize) {
        this.authorize = authorize;
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
     * Getter for property txtSuspenseProdID.
     *
     * @return Value of property txtSuspenseProdID.
     */
    public java.lang.String getTxtSuspenseProdID() {
        return txtSuspenseProdID;
    }

    /**
     * Setter for property txtSuspenseProdID.
     *
     * @param txtSuspenseProdID New value of property txtSuspenseProdID.
     */
    public void setTxtSuspenseProdID(java.lang.String txtSuspenseProdID) {
        this.txtSuspenseProdID = txtSuspenseProdID;
    }

    /**
     * Getter for property txtSuspenseProdName.
     *
     * @return Value of property txtSuspenseProdName.
     */
    public java.lang.String getTxtSuspenseProdName() {
        return txtSuspenseProdName;
    }

    /**
     * Setter for property txtSuspenseProdName.
     *
     * @param txtSuspenseProdName New value of property txtSuspenseProdName.
     */
    public void setTxtSuspenseProdName(java.lang.String txtSuspenseProdName) {
        this.txtSuspenseProdName = txtSuspenseProdName;
    }

    /**
     * Getter for property txtSuspenseProductHead.
     *
     * @return Value of property txtSuspenseProductHead.
     */
    public java.lang.String getTxtSuspenseProductHead() {
        return txtSuspenseProductHead;
    }

    /**
     * Setter for property txtSuspenseProductHead.
     *
     * @param txtSuspenseProductHead New value of property
     * txtSuspenseProductHead.
     */
    public void setTxtSuspenseProductHead(java.lang.String txtSuspenseProductHead) {
        this.txtSuspenseProductHead = txtSuspenseProductHead;
    }

    /**
     * Getter for property createdBy.
     *
     * @return Value of property createdBy.
     */
    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for property createdBy.
     *
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property createdDt.
     *
     * @param createdDt New value of property createdDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
    }

    /**
     * Getter for property txtPrefix.
     *
     * @return Value of property txtPrefix.
     */
    public String getTxtPrefix() {
        return txtPrefix;
    }

    /**
     * Setter for property txtPrefix.
     *
     * @param txtPrefix New value of property txtPrefix.
     */
    public void setTxtPrefix(String txtPrefix) {
        this.txtPrefix = txtPrefix;
    }

    public String getLoanBehaviour() {
        return loanBehaviour;
    }

    public void setLoanBehaviour(String loanBehaviour) {
        this.loanBehaviour = loanBehaviour;
    }

    public String getIntAcHd() {
        return intAcHd;
    }

    public void setIntAcHd(String intAcHd) {
        this.intAcHd = intAcHd;
    }

    public Double getIntRate() {
        return intRate;
    }

    public void setIntRate(Double intRate) {
        this.intRate = intRate;
    }
    
}