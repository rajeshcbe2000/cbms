/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriCardTo.java
 *
 * Created on January 1, 2009, 4:19 PM
 */
package com.see.truetransact.transferobject.product.loan.agriculturecard;

import java.io.Serializable;
import java.util.Date;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author Administrator
 */
public class AgriCardTo extends TransferObject implements Serializable {

    private String agriCardType = "";
    private Double txtNoOfYears = null;
    private String agriCardValidity = "";
    private String prodType = "";
    private String prodId = "";
    private String cRadio_SB_Yes = "";
    private String cRadio_SB_No = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private Double prodNo = null;

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("agriCardType", agriCardType));
        strB.append(getTOString("txtNoOfYears", txtNoOfYears));
        strB.append(getTOString("agriCardValidity", agriCardValidity));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("cRadio_SB_Yes", cRadio_SB_Yes));
        strB.append(getTOString("cRadio_SB_No", cRadio_SB_No));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("prodNo", prodNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("agriCardType");
        return agriCardType;
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("agriCardType", agriCardType));
        strB.append(getTOXml("txtNoOfYears", txtNoOfYears));
        strB.append(getTOXml("agriCardValidity", agriCardValidity));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("cRadio_SB_Yes", cRadio_SB_Yes));
        strB.append(getTOXml("cRadio_SB_Yes", cRadio_SB_Yes));
        strB.append(getTOXml("cRadio_SB_No", cRadio_SB_No));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("prodNo", prodNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property agriCardType.
     *
     * @return Value of property agriCardType.
     */
    public java.lang.String getAgriCardType() {
        return agriCardType;
    }

    /**
     * Setter for property agriCardType.
     *
     * @param agriCardType New value of property agriCardType.
     */
    public void setAgriCardType(java.lang.String agriCardType) {
        this.agriCardType = agriCardType;
    }

    /**
     * Getter for property prodType.
     *
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }

    /**
     * Setter for property prodType.
     *
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
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
     * Getter for property cRadio_SB_Yes.
     *
     * @return Value of property cRadio_SB_Yes.
     */
    public java.lang.String getCRadio_SB_Yes() {
        return cRadio_SB_Yes;
    }

    /**
     * Setter for property cRadio_SB_Yes.
     *
     * @param cRadio_SB_Yes New value of property cRadio_SB_Yes.
     */
    public void setCRadio_SB_Yes(java.lang.String cRadio_SB_Yes) {
        this.cRadio_SB_Yes = cRadio_SB_Yes;
    }

    /**
     * Getter for property cRadio_SB_No.
     *
     * @return Value of property cRadio_SB_No.
     */
    public java.lang.String getCRadio_SB_No() {
        return cRadio_SB_No;
    }

    /**
     * Setter for property cRadio_SB_No.
     *
     * @param cRadio_SB_No New value of property cRadio_SB_No.
     */
    public void setCRadio_SB_No(java.lang.String cRadio_SB_No) {
        this.cRadio_SB_No = cRadio_SB_No;
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
     * Getter for property prodNo.
     *
     * @return Value of property prodNo.
     */
    public java.lang.Double getProdNo() {
        return prodNo;
    }

    /**
     * Setter for property prodNo.
     *
     * @param prodNo New value of property prodNo.
     */
    public void setProdNo(java.lang.Double prodNo) {
        this.prodNo = prodNo;
    }

    /**
     * Getter for property txtNoOfYears.
     *
     * @return Value of property txtNoOfYears.
     */
    public java.lang.Double getTxtNoOfYears() {
        return txtNoOfYears;
    }

    /**
     * Setter for property txtNoOfYears.
     *
     * @param txtNoOfYears New value of property txtNoOfYears.
     */
    public void setTxtNoOfYears(java.lang.Double txtNoOfYears) {
        this.txtNoOfYears = txtNoOfYears;
    }

    /**
     * Getter for property agriCardValidity.
     *
     * @return Value of property agriCardValidity.
     */
    public java.lang.String getAgriCardValidity() {
        return agriCardValidity;
    }

    /**
     * Setter for property agriCardValidity.
     *
     * @param agriCardValidity New value of property agriCardValidity.
     */
    public void setAgriCardValidity(java.lang.String agriCardValidity) {
        this.agriCardValidity = agriCardValidity;
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
}
