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
public class FixedAssetsIndividualTO extends TransferObject implements Serializable {

    private String assetIndID = "";
    private String assetType = "";
    private String assetDesc = "";
    private String quantity = "";
    private String warranty = "";
    private String floor = "";
    private Double faceVal = 0.0;
    private Double currVal = 0.0;
    private Date orderedDt = null;
    private Date purchasedDt = null;
    private String assetNum = "";
    private String branCode = "";
    private String branchId = "";
    private String invoiceNo = "";
    private String cboDepart = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizedStatus = null;
    private String slNo = "";
    private String company = "";
    private String suppliedBran = "";
    private String warrantyVal = "";
    // For Emp Details Table
    private String assetTabStatus = "";
    private Date assetTabStatusDt = null;
    private String assetTabStatusBy = "";
    private Date assetTabCreatedDt = null;
    private String assetTabCreatedBy = "";
    private String assetTabAuthorizeBy = "";
    private Date assetTabAuthorizeDt = null;
    private String assetTabAuthorizedStatus = null;
    private String faIndId = "";
    private String amount = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("assetIndID");
        return assetIndID;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("assetIndID", assetIndID));
        strB.append(getTOString("invoiceNo", invoiceNo));
        strB.append(getTOString("assetType", assetType));
        strB.append(getTOString("assetDesc", assetDesc));
        strB.append(getTOString("quantity", quantity));
        strB.append(getTOString("warranty", warranty));
        strB.append(getTOString("floor", floor));
        strB.append(getTOString("faceVal", faceVal));
        strB.append(getTOString("currVal", currVal));
        strB.append(getTOString("orderedDt", orderedDt));
        strB.append(getTOString("purchasedDt", purchasedDt));
        strB.append(getTOString("assetNum", assetNum));
        strB.append(getTOString("branCode", branCode));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("assetTabStatus", assetTabStatus));
        strB.append(getTOString("assetTabStatusDt", assetTabStatusDt));
        strB.append(getTOString("assetTabStatusBy", assetTabStatusBy));
        strB.append(getTOString("assetTabCreatedDt", assetTabCreatedDt));
        strB.append(getTOString("assetTabCreatedBy", assetTabCreatedBy));
        strB.append(getTOString("assetTabAuthorizeBy", assetTabAuthorizeBy));
        strB.append(getTOString("assetTabAuthorizeDt", assetTabAuthorizeDt));
        strB.append(getTOString("assetTabAuthorizedStatus", assetTabAuthorizedStatus));
        strB.append(getTOString("company", company));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("cboDepart", cboDepart));
        strB.append(getTOString("warrantyVal", warrantyVal));
        strB.append(getTOString("faIndId", faIndId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("assetIndID", assetIndID));
        strB.append(getTOString("invoiceNo", invoiceNo));
        strB.append(getTOXml("assetType", assetType));
        strB.append(getTOXml("assetDesc", assetDesc));
        strB.append(getTOXml("quantity", quantity));
        strB.append(getTOXml("warranty", warranty));
        strB.append(getTOString("floor", floor));
        strB.append(getTOXml("faceVal", faceVal));
        strB.append(getTOXml("currVal", currVal));
        strB.append(getTOXml("orderedDt", orderedDt));
        strB.append(getTOXml("purchasedDt", purchasedDt));
        strB.append(getTOXml("assetNum", assetNum));
        strB.append(getTOXml("branCode", branCode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("assetTabStatus", assetTabStatus));
        strB.append(getTOXml("assetTabStatusDt", assetTabStatusDt));
        strB.append(getTOXml("assetTabStatusBy", assetTabStatusBy));
        strB.append(getTOXml("assetTabCreatedDt", assetTabCreatedDt));
        strB.append(getTOXml("assetTabCreatedBy", assetTabCreatedBy));
        strB.append(getTOXml("assetTabAuthorizeBy", assetTabAuthorizeBy));
        strB.append(getTOXml("assetTabAuthorizeDt", assetTabAuthorizeDt));
        strB.append(getTOXml("assetTabAuthorizedStatus", assetTabAuthorizedStatus));
        strB.append(getTOXml("company", company));
        strB.append(getTOXml("branchId", suppliedBran));
        strB.append(getTOString("cboDepart", cboDepart));
        strB.append(getTOXml("warrantyVal", warrantyVal));
        strB.append(getTOXml("faIndId", faIndId));
        strB.append(getTOXmlEnd());
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
     * Getter for property assetIndID.
     *
     * @return Value of property assetIndID.
     */
    public java.lang.String getAssetIndID() {
        return assetIndID;
    }

    /**
     * Setter for property assetIndID.
     *
     * @param assetIndID New value of property assetIndID.
     */
    public void setAssetIndID(java.lang.String assetIndID) {
        this.assetIndID = assetIndID;
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
     * Getter for property quantity.
     *
     * @return Value of property quantity.
     */
    public java.lang.String getQuantity() {
        return quantity;
    }

    /**
     * Setter for property quantity.
     *
     * @param quantity New value of property quantity.
     */
    public void setQuantity(java.lang.String quantity) {
        this.quantity = quantity;
    }

    /**
     * Getter for property warranty.
     *
     * @return Value of property warranty.
     */
    public java.lang.String getWarranty() {
        return warranty;
    }

    /**
     * Setter for property warranty.
     *
     * @param warranty New value of property warranty.
     */
    public void setWarranty(java.lang.String warranty) {
        this.warranty = warranty;
    }

    public Double getFaceVal() {
        return faceVal;
    }

    public void setFaceVal(Double faceVal) {
        this.faceVal = faceVal;
    }

    public Double getCurrVal() {
        return currVal;
    }

    public void setCurrVal(Double currVal) {
        this.currVal = currVal;
    }

  
    /**
     * Getter for property orderedDt.
     *
     * @return Value of property orderedDt.
     */
    public java.util.Date getOrderedDt() {
        return orderedDt;
    }

    /**
     * Setter for property orderedDt.
     *
     * @param orderedDt New value of property orderedDt.
     */
    public void setOrderedDt(java.util.Date orderedDt) {
        this.orderedDt = orderedDt;
    }

    /**
     * Getter for property purchasedDt.
     *
     * @return Value of property purchasedDt.
     */
    public java.util.Date getPurchasedDt() {
        return purchasedDt;
    }

    /**
     * Setter for property purchasedDt.
     *
     * @param purchasedDt New value of property purchasedDt.
     */
    public void setPurchasedDt(java.util.Date purchasedDt) {
        this.purchasedDt = purchasedDt;
    }

    /**
     * Getter for property assetNum.
     *
     * @return Value of property assetNum.
     */
    public java.lang.String getAssetNum() {
        return assetNum;
    }

    /**
     * Setter for property assetNum.
     *
     * @param assetNum New value of property assetNum.
     */
    public void setAssetNum(java.lang.String assetNum) {
        this.assetNum = assetNum;
    }

    /**
     * Getter for property assetTabStatus.
     *
     * @return Value of property assetTabStatus.
     */
    public java.lang.String getAssetTabStatus() {
        return assetTabStatus;
    }

    /**
     * Setter for property assetTabStatus.
     *
     * @param assetTabStatus New value of property assetTabStatus.
     */
    public void setAssetTabStatus(java.lang.String assetTabStatus) {
        this.assetTabStatus = assetTabStatus;
    }

    /**
     * Getter for property assetTabStatusDt.
     *
     * @return Value of property assetTabStatusDt.
     */
    public java.util.Date getAssetTabStatusDt() {
        return assetTabStatusDt;
    }

    /**
     * Setter for property assetTabStatusDt.
     *
     * @param assetTabStatusDt New value of property assetTabStatusDt.
     */
    public void setAssetTabStatusDt(java.util.Date assetTabStatusDt) {
        this.assetTabStatusDt = assetTabStatusDt;
    }

    /**
     * Getter for property assetTabStatusBy.
     *
     * @return Value of property assetTabStatusBy.
     */
    public java.lang.String getAssetTabStatusBy() {
        return assetTabStatusBy;
    }

    /**
     * Setter for property assetTabStatusBy.
     *
     * @param assetTabStatusBy New value of property assetTabStatusBy.
     */
    public void setAssetTabStatusBy(java.lang.String assetTabStatusBy) {
        this.assetTabStatusBy = assetTabStatusBy;
    }

    /**
     * Getter for property assetTabCreatedDt.
     *
     * @return Value of property assetTabCreatedDt.
     */
    public java.util.Date getAssetTabCreatedDt() {
        return assetTabCreatedDt;
    }

    /**
     * Setter for property assetTabCreatedDt.
     *
     * @param assetTabCreatedDt New value of property assetTabCreatedDt.
     */
    public void setAssetTabCreatedDt(java.util.Date assetTabCreatedDt) {
        this.assetTabCreatedDt = assetTabCreatedDt;
    }

    /**
     * Getter for property assetTabCreatedBy.
     *
     * @return Value of property assetTabCreatedBy.
     */
    public java.lang.String getAssetTabCreatedBy() {
        return assetTabCreatedBy;
    }

    /**
     * Setter for property assetTabCreatedBy.
     *
     * @param assetTabCreatedBy New value of property assetTabCreatedBy.
     */
    public void setAssetTabCreatedBy(java.lang.String assetTabCreatedBy) {
        this.assetTabCreatedBy = assetTabCreatedBy;
    }

    /**
     * Getter for property assetTabAuthorizeBy.
     *
     * @return Value of property assetTabAuthorizeBy.
     */
    public java.lang.String getAssetTabAuthorizeBy() {
        return assetTabAuthorizeBy;
    }

    /**
     * Setter for property assetTabAuthorizeBy.
     *
     * @param assetTabAuthorizeBy New value of property assetTabAuthorizeBy.
     */
    public void setAssetTabAuthorizeBy(java.lang.String assetTabAuthorizeBy) {
        this.assetTabAuthorizeBy = assetTabAuthorizeBy;
    }

    /**
     * Getter for property assetTabAuthorizeDt.
     *
     * @return Value of property assetTabAuthorizeDt.
     */
    public java.util.Date getAssetTabAuthorizeDt() {
        return assetTabAuthorizeDt;
    }

    /**
     * Setter for property assetTabAuthorizeDt.
     *
     * @param assetTabAuthorizeDt New value of property assetTabAuthorizeDt.
     */
    public void setAssetTabAuthorizeDt(java.util.Date assetTabAuthorizeDt) {
        this.assetTabAuthorizeDt = assetTabAuthorizeDt;
    }

    /**
     * Getter for property assetTabAuthorizedStatus.
     *
     * @return Value of property assetTabAuthorizedStatus.
     */
    public java.lang.String getAssetTabAuthorizedStatus() {
        return assetTabAuthorizedStatus;
    }

    /**
     * Setter for property assetTabAuthorizedStatus.
     *
     * @param assetTabAuthorizedStatus New value of property
     * assetTabAuthorizedStatus.
     */
    public void setAssetTabAuthorizedStatus(java.lang.String assetTabAuthorizedStatus) {
        this.assetTabAuthorizedStatus = assetTabAuthorizedStatus;
    }

    /**
     * Getter for property company.
     *
     * @return Value of property company.
     */
    public java.lang.String getCompany() {
        return company;
    }

    /**
     * Setter for property company.
     *
     * @param company New value of property company.
     */
    public void setCompany(java.lang.String company) {
        this.company = company;
    }

    /**
     * Getter for property suppliedBran.
     *
     * @return Value of property suppliedBran.
     */
    public java.lang.String getSuppliedBran() {
        return suppliedBran;
    }

    /**
     * Setter for property suppliedBran.
     *
     * @param suppliedBran New value of property suppliedBran.
     */
    public void setSuppliedBran(java.lang.String suppliedBran) {
        this.suppliedBran = suppliedBran;
    }

    /**
     * Getter for property warrantyVal.
     *
     * @return Value of property warrantyVal.
     */
    public java.lang.String getWarrantyVal() {
        return warrantyVal;
    }

    /**
     * Setter for property warrantyVal.
     *
     * @param warrantyVal New value of property warrantyVal.
     */
    public void setWarrantyVal(java.lang.String warrantyVal) {
        this.warrantyVal = warrantyVal;
    }

    /**
     * Getter for property branchId.
     *
     * @return Value of property branchId.
     */
    public java.lang.String getBranchId() {
        return branchId;
    }

    /**
     * Setter for property branchId.
     *
     * @param branchId New value of property branchId.
     */
    public void setBranchId(java.lang.String branchId) {
        this.branchId = branchId;
    }

    /**
     * Getter for property invoiceNo.
     *
     * @return Value of property invoiceNo.
     */
    public java.lang.String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * Setter for property invoiceNo.
     *
     * @param invoiceNo New value of property invoiceNo.
     */
    public void setInvoiceNo(java.lang.String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    /**
     * Getter for property floor.
     *
     * @return Value of property floor.
     */
    public java.lang.String getFloor() {
        return floor;
    }

    /**
     * Setter for property floor.
     *
     * @param floor New value of property floor.
     */
    public void setFloor(java.lang.String floor) {
        this.floor = floor;
    }

    /**
     * Getter for property cboDepart.
     *
     * @return Value of property cboDepart.
     */
    public java.lang.String getCboDepart() {
        return cboDepart;
    }

    /**
     * Setter for property cboDepart.
     *
     * @param cboDepart New value of property cboDepart.
     */
    public void setCboDepart(java.lang.String cboDepart) {
        this.cboDepart = cboDepart;
    }

    /**
     * Getter for property faIndId.
     *
     * @return Value of property faIndId.
     */
    public java.lang.String getFaIndId() {
        return faIndId;
    }

    /**
     * Setter for property faIndId.
     *
     * @param faIndId New value of property faIndId.
     */
    public void setFaIndId(java.lang.String faIndId) {
        this.faIndId = faIndId;
    }

    /**
     * Getter for property amount.
     *
     * @return Value of property amount.
     */
    public java.lang.String getAmount() {
        return amount;
    }

    /**
     * Setter for property amount.
     *
     * @param amount New value of property amount.
     */
    public void setAmount(java.lang.String amount) {
        this.amount = amount;
    }
}