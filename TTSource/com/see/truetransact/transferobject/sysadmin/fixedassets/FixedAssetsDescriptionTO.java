/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTransferTO.java
 *
 * Created on Wed Jun 28  2010 swaroop
 */
package com.see.truetransact.transferobject.sysadmin.fixedassets;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is EMP_TRANSFER.
 */
public class FixedAssetsDescriptionTO extends TransferObject implements Serializable {

    private String assetDescID = "";
    private String assetType = "";
    private String assetSubType = "";
    private String assetStatus = "";
    private Date assetStatusDt = null;
    private String assetStatusBy = "";
    private Date assetCreatedDt = null;
    private String assetCreatedBy = "";
    private String assetAuthorizeBy = "";
    private Date assetAuthorizeDt = null;
    private String assetAuthorizedStatus = null;
    private String branCode = "";
    private String slNo = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("assetDescID");
        return assetDescID;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("assetDescID", assetDescID));
        strB.append(getTOString("assetType", assetType));
        strB.append(getTOString("assetSubType", assetSubType));
        strB.append(getTOString("branCode", branCode));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("assetStatus", assetStatus));
        strB.append(getTOString("assetStatusDt", assetStatusDt));
        strB.append(getTOString("assetStatusBy", assetStatusBy));
        strB.append(getTOString("assetCreatedDt", assetCreatedDt));
        strB.append(getTOString("assetCreatedBy", assetCreatedBy));
        strB.append(getTOString("assetAuthorizeBy", assetAuthorizeBy));
        strB.append(getTOString("assetAuthorizeDt", assetAuthorizeDt));
        strB.append(getTOString("assetAuthorizedStatus", assetAuthorizedStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("assetDescID", assetDescID));
        strB.append(getTOXml("assetType", assetType));
        strB.append(getTOXml("assetSubType", assetSubType));
        strB.append(getTOXml("branCode", branCode));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("assetStatus", assetStatus));
        strB.append(getTOXml("assetStatusDt", assetStatusDt));
        strB.append(getTOXml("assetStatusBy", assetStatusBy));
        strB.append(getTOXml("assetCreatedDt", assetCreatedDt));
        strB.append(getTOXml("assetCreatedBy", assetCreatedBy));
        strB.append(getTOXml("assetAuthorizeBy", assetAuthorizeBy));
        strB.append(getTOXml("assetAuthorizeDt", assetAuthorizeDt));
        strB.append(getTOXml("assetAuthorizedStatus", assetAuthorizedStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property branCode.
     *
     * @return Value of property branCode.
     */
    public java.lang.String getBranCode() {
        return branCode;
    }

    /**
     * Setter for property branCode.
     *
     * @param branCode New value of property branCode.
     */
    public void setBranCode(java.lang.String branCode) {
        this.branCode = branCode;
    }

    /**
     * Getter for property slNo.
     *
     * @return Value of property slNo.
     */
    public java.lang.String getSlNo() {
        return slNo;
    }

    /**
     * Setter for property slNo.
     *
     * @param slNo New value of property slNo.
     */
    public void setSlNo(java.lang.String slNo) {
        this.slNo = slNo;
    }

    /**
     * Getter for property assetDescID.
     *
     * @return Value of property assetDescID.
     */
    public java.lang.String getAssetDescID() {
        return assetDescID;
    }

    /**
     * Setter for property assetDescID.
     *
     * @param assetDescID New value of property assetDescID.
     */
    public void setAssetDescID(java.lang.String assetDescID) {
        this.assetDescID = assetDescID;
    }

    /**
     * Getter for property assetType.
     *
     * @return Value of property assetType.
     */
    public java.lang.String getAssetType() {
        return assetType;
    }

    /**
     * Setter for property assetType.
     *
     * @param assetType New value of property assetType.
     */
    public void setAssetType(java.lang.String assetType) {
        this.assetType = assetType;
    }

    /**
     * Getter for property assetSubType.
     *
     * @return Value of property assetSubType.
     */
    public java.lang.String getAssetSubType() {
        return assetSubType;
    }

    /**
     * Setter for property assetSubType.
     *
     * @param assetSubType New value of property assetSubType.
     */
    public void setAssetSubType(java.lang.String assetSubType) {
        this.assetSubType = assetSubType;
    }

    /**
     * Getter for property assetStatus.
     *
     * @return Value of property assetStatus.
     */
    public java.lang.String getAssetStatus() {
        return assetStatus;
    }

    /**
     * Setter for property assetStatus.
     *
     * @param assetStatus New value of property assetStatus.
     */
    public void setAssetStatus(java.lang.String assetStatus) {
        this.assetStatus = assetStatus;
    }

    /**
     * Getter for property assetStatusDt.
     *
     * @return Value of property assetStatusDt.
     */
    public java.util.Date getAssetStatusDt() {
        return assetStatusDt;
    }

    /**
     * Setter for property assetStatusDt.
     *
     * @param assetStatusDt New value of property assetStatusDt.
     */
    public void setAssetStatusDt(java.util.Date assetStatusDt) {
        this.assetStatusDt = assetStatusDt;
    }

    /**
     * Getter for property assetStatusBy.
     *
     * @return Value of property assetStatusBy.
     */
    public java.lang.String getAssetStatusBy() {
        return assetStatusBy;
    }

    /**
     * Setter for property assetStatusBy.
     *
     * @param assetStatusBy New value of property assetStatusBy.
     */
    public void setAssetStatusBy(java.lang.String assetStatusBy) {
        this.assetStatusBy = assetStatusBy;
    }

    /**
     * Getter for property assetCreatedDt.
     *
     * @return Value of property assetCreatedDt.
     */
    public java.util.Date getAssetCreatedDt() {
        return assetCreatedDt;
    }

    /**
     * Setter for property assetCreatedDt.
     *
     * @param assetCreatedDt New value of property assetCreatedDt.
     */
    public void setAssetCreatedDt(java.util.Date assetCreatedDt) {
        this.assetCreatedDt = assetCreatedDt;
    }

    /**
     * Getter for property assetCreatedBy.
     *
     * @return Value of property assetCreatedBy.
     */
    public java.lang.String getAssetCreatedBy() {
        return assetCreatedBy;
    }

    /**
     * Setter for property assetCreatedBy.
     *
     * @param assetCreatedBy New value of property assetCreatedBy.
     */
    public void setAssetCreatedBy(java.lang.String assetCreatedBy) {
        this.assetCreatedBy = assetCreatedBy;
    }

    /**
     * Getter for property assetAuthorizeBy.
     *
     * @return Value of property assetAuthorizeBy.
     */
    public java.lang.String getAssetAuthorizeBy() {
        return assetAuthorizeBy;
    }

    /**
     * Setter for property assetAuthorizeBy.
     *
     * @param assetAuthorizeBy New value of property assetAuthorizeBy.
     */
    public void setAssetAuthorizeBy(java.lang.String assetAuthorizeBy) {
        this.assetAuthorizeBy = assetAuthorizeBy;
    }

    /**
     * Getter for property assetAuthorizeDt.
     *
     * @return Value of property assetAuthorizeDt.
     */
    public java.util.Date getAssetAuthorizeDt() {
        return assetAuthorizeDt;
    }

    /**
     * Setter for property assetAuthorizeDt.
     *
     * @param assetAuthorizeDt New value of property assetAuthorizeDt.
     */
    public void setAssetAuthorizeDt(java.util.Date assetAuthorizeDt) {
        this.assetAuthorizeDt = assetAuthorizeDt;
    }

    /**
     * Getter for property assetAuthorizedStatus.
     *
     * @return Value of property assetAuthorizedStatus.
     */
    public java.lang.String getAssetAuthorizedStatus() {
        return assetAuthorizedStatus;
    }

    /**
     * Setter for property assetAuthorizedStatus.
     *
     * @param assetAuthorizedStatus New value of property assetAuthorizedStatus.
     */
    public void setAssetAuthorizedStatus(java.lang.String assetAuthorizedStatus) {
        this.assetAuthorizedStatus = assetAuthorizedStatus;
    }
}