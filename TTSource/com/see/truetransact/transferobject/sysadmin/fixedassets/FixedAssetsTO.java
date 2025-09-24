/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FixedAssetsTO.java
 *
 * Created on Wed Jun 28  2010 swaroop
 */
package com.see.truetransact.transferobject.sysadmin.fixedassets;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is FIXED_ASSETS_PRODUCT.
 */
public class FixedAssetsTO extends TransferObject implements Serializable {

    private String assetsID = "";
    private String assetType = "";
    private String assetDesc = "";
    private String provision = "";
    private String curValRoundOff = "";
    private String roundOffType = "";
    private String purchaseDebit = "";
    private String provisionDebit = "";
    private String sellingAcID = "";
    private String nullifyingCredit = "";
    private String nullifyingDebit = "";
    private String branCode = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizedStatus = null;
    private String Depreciation = "";
    private String rdoDepYesNo = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("assetsID");
        return assetsID;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("assetsID", assetsID));
        strB.append(getTOString("assetType", assetType));
        strB.append(getTOString("assetDesc", assetDesc));
        strB.append(getTOString("provision", provision));
        strB.append(getTOString("curValRoundOff", curValRoundOff));
        strB.append(getTOString("roundOffType", roundOffType));
        strB.append(getTOString("purchaseDebit", purchaseDebit));
        strB.append(getTOString("provisionDebit", provisionDebit));
        strB.append(getTOString("sellingAcID", sellingAcID));
        strB.append(getTOString("nullifyingCredit", nullifyingCredit));
        strB.append(getTOString("nullifyingDebit", nullifyingDebit));
        strB.append(getTOString("branCode", branCode));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("rdoDepYesNo", rdoDepYesNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("assetsID", assetsID));
        strB.append(getTOXml("assetType", assetType));
        strB.append(getTOXml("assetDesc", assetDesc));
        strB.append(getTOXml("provision", provision));
        strB.append(getTOXml("curValRoundOff", curValRoundOff));
        strB.append(getTOXml("roundOffType", roundOffType));
        strB.append(getTOXml("purchaseDebit", purchaseDebit));
        strB.append(getTOXml("provisionDebit", provisionDebit));
        strB.append(getTOXml("sellingAcID", sellingAcID));
        strB.append(getTOXml("nullifyingCredit", nullifyingCredit));
        strB.append(getTOXml("nullifyingDebit", nullifyingDebit));
        strB.append(getTOXml("branCode", branCode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("rdoDepYesNo", rdoDepYesNo));
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
     * Getter for property authorizedStatus.
     *
     * @return Value of property authorizedStatus.
     */
    public java.lang.String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter for property authorizedStatus.
     *
     * @param authorizedStatus New value of property authorizedStatus.
     */
    public void setAuthorizedStatus(java.lang.String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    /**
     * Getter for property assetsID.
     *
     * @return Value of property assetsID.
     */
    public java.lang.String getAssetsID() {
        return assetsID;
    }

    /**
     * Setter for property assetsID.
     *
     * @param assetsID New value of property assetsID.
     */
    public void setAssetsID(java.lang.String assetsID) {
        this.assetsID = assetsID;
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
     * Getter for property assetDesc.
     *
     * @return Value of property assetDesc.
     */
    public java.lang.String getAssetDesc() {
        return assetDesc;
    }

    /**
     * Setter for property assetDesc.
     *
     * @param assetDesc New value of property assetDesc.
     */
    public void setAssetDesc(java.lang.String assetDesc) {
        this.assetDesc = assetDesc;
    }

    /**
     * Getter for property provision.
     *
     * @return Value of property provision.
     */
    public java.lang.String getProvision() {
        return provision;
    }

    /**
     * Setter for property provision.
     *
     * @param provision New value of property provision.
     */
    public void setProvision(java.lang.String provision) {
        this.provision = provision;
    }

    /**
     * Getter for property purchaseDebit.
     *
     * @return Value of property purchaseDebit.
     */
    public java.lang.String getPurchaseDebit() {
        return purchaseDebit;
    }

    /**
     * Setter for property purchaseDebit.
     *
     * @param purchaseDebit New value of property purchaseDebit.
     */
    public void setPurchaseDebit(java.lang.String purchaseDebit) {
        this.purchaseDebit = purchaseDebit;
    }

    /**
     * Getter for property provisionDebit.
     *
     * @return Value of property provisionDebit.
     */
    public java.lang.String getProvisionDebit() {
        return provisionDebit;
    }

    /**
     * Setter for property provisionDebit.
     *
     * @param provisionDebit New value of property provisionDebit.
     */
    public void setProvisionDebit(java.lang.String provisionDebit) {
        this.provisionDebit = provisionDebit;
    }

    /**
     * Getter for property sellingAcID.
     *
     * @return Value of property sellingAcID.
     */
    public java.lang.String getSellingAcID() {
        return sellingAcID;
    }

    /**
     * Setter for property sellingAcID.
     *
     * @param sellingAcID New value of property sellingAcID.
     */
    public void setSellingAcID(java.lang.String sellingAcID) {
        this.sellingAcID = sellingAcID;
    }

    /**
     * Getter for property nullifyingCredit.
     *
     * @return Value of property nullifyingCredit.
     */
    public java.lang.String getNullifyingCredit() {
        return nullifyingCredit;
    }

    /**
     * Setter for property nullifyingCredit.
     *
     * @param nullifyingCredit New value of property nullifyingCredit.
     */
    public void setNullifyingCredit(java.lang.String nullifyingCredit) {
        this.nullifyingCredit = nullifyingCredit;
    }

    /**
     * Getter for property nullifyingDebit.
     *
     * @return Value of property nullifyingDebit.
     */
    public java.lang.String getNullifyingDebit() {
        return nullifyingDebit;
    }

    /**
     * Setter for property nullifyingDebit.
     *
     * @param nullifyingDebit New value of property nullifyingDebit.
     */
    public void setNullifyingDebit(java.lang.String nullifyingDebit) {
        this.nullifyingDebit = nullifyingDebit;
    }

    /**
     * Getter for property Depreciation.
     *
     * @return Value of property Depreciation.
     */
    public java.lang.String getDepreciation() {
        return Depreciation;
    }

    /**
     * Setter for property Depreciation.
     *
     * @param Depreciation New value of property Depreciation.
     */
    public void setDepreciation(java.lang.String Depreciation) {
        this.Depreciation = Depreciation;
    }

    /**
     * Getter for property rdoDepYesNo.
     *
     * @return Value of property rdoDepYesNo.
     */
    public java.lang.String getRdoDepYesNo() {
        return rdoDepYesNo;
    }

    /**
     * Setter for property rdoDepYesNo.
     *
     * @param rdoDepYesNo New value of property rdoDepYesNo.
     */
    public void setRdoDepYesNo(java.lang.String rdoDepYesNo) {
        this.rdoDepYesNo = rdoDepYesNo;
    }

    /**
     * Getter for property curValRoundOff.
     *
     * @return Value of property curValRoundOff.
     */
    public java.lang.String getCurValRoundOff() {
        return curValRoundOff;
    }

    /**
     * Setter for property curValRoundOff.
     *
     * @param curValRoundOff New value of property curValRoundOff.
     */
    public void setCurValRoundOff(java.lang.String curValRoundOff) {
        this.curValRoundOff = curValRoundOff;
    }

    /**
     * Getter for property roundOffType.
     *
     * @return Value of property roundOffType.
     */
    public java.lang.String getRoundOffType() {
        return roundOffType;
    }

    /**
     * Setter for property roundOffType.
     *
     * @param roundOffType New value of property roundOffType.
     */
    public void setRoundOffType(java.lang.String roundOffType) {
        this.roundOffType = roundOffType;
    }
}