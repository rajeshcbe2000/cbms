/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TodAllowedTO.java
 *
 * Created on February 9, 2009, 5:42 PM
 */
package com.see.truetransact.transferobject.operativeaccount;

/**
 *
 * @author Swaroop
 */
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

public class TodAllowedTO extends TransferObject implements Serializable {

    /**
     * Creates a new instance of TodAllowedTO
     */
    public TodAllowedTO() {
    }

    /**
     * Getter for property accountNumber.
     *
     * @return Value of property accountNumber.
     */
    public java.lang.String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Setter for property accountNumber.
     *
     * @param accountNumber New value of property accountNumber.
     */
    public void setAccountNumber(java.lang.String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Getter for property acctName.
     *
     * @return Value of property acctName.
     */
    public java.lang.String getAcctName() {
        return acctName;
    }

    /**
     * Setter for property acctName.
     *
     * @param acctName New value of property acctName.
     */
    public void setAcctName(java.lang.String acctName) {
        this.acctName = acctName;
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
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }

    /**
     * Setter for property remarks.
     *
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }

    /**
     * Getter for property productId.
     *
     * @return Value of property productId.
     */
    public java.lang.String getProductId() {
        return productId;
    }

    /**
     * Setter for property productId.
     *
     * @param productId New value of property productId.
     */
    public void setProductId(java.lang.String productId) {
        this.productId = productId;
    }

    /**
     * Getter for property todAllowed.
     *
     * @return Value of property todAllowed.
     */
    public java.lang.String getTodAllowed() {
        return todAllowed;
    }

    /**
     * Setter for property todAllowed.
     *
     * @param todAllowed New value of property todAllowed.
     */
    public void setTodAllowed(java.lang.String todAllowed) {
        this.todAllowed = todAllowed;
    }

    /**
     * Getter for property permitedBy.
     *
     * @return Value of property permitedBy.
     */
    public java.lang.String getPermitedBy() {
        return permitedBy;
    }

    /**
     * Setter for property permitedBy.
     *
     * @param permitedBy New value of property permitedBy.
     */
    public void setPermitedBy(java.lang.String permitedBy) {
        this.permitedBy = permitedBy;
    }

    /**
     * Getter for property permissionRefNo.
     *
     * @return Value of property permissionRefNo.
     */
    public java.lang.String getPermissionRefNo() {
        return permissionRefNo;
    }

    /**
     * Setter for property permissionRefNo.
     *
     * @param permissionRefNo New value of property permissionRefNo.
     */
    public void setPermissionRefNo(java.lang.String permissionRefNo) {
        this.permissionRefNo = permissionRefNo;
    }

    /**
     * Getter for property typeOfTOD.
     *
     * @return Value of property typeOfTOD.
     */
    public java.lang.String getTypeOfTOD() {
        return typeOfTOD;
    }

    /**
     * Setter for property typeOfTOD.
     *
     * @param typeOfTOD New value of property typeOfTOD.
     */
    public void setTypeOfTOD(java.lang.String typeOfTOD) {
        this.typeOfTOD = typeOfTOD;
    }

    public String getKeyData() {
        setKeyColumns("trans_id");
        return trans_id;
    }
    private String accountNumber = "";
    private String acctName = "";
    private Date fromDate = null;
    private Date toDate = null;
    private String statusBy = "";
    private String remarks = "";
    private String productId = "";
    private String todAllowed = "";
    private String permitedBy = "";
    private String permissionRefNo = "";
    private Date permittedDt = null;
    private String typeOfTOD = "";
    private String authorizeStatus = null;;
    private String authorizeUser = "";
    private Date authorizeDt = null;
    private String status = "";
    private Date statusDt = null;
    private Date createdDt = null;
    private String createdBy = "";
    private String trans_id = "";
    private String branchCode = "";
    private String productType = "";
    private Date intCalcDt = null;
    private Double repayPeriod = null;
    private String repayPeriodDDMMYY = "";
    private Date repayDt = null;

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("accountNumber", accountNumber));
        strB.append(getTOString("acctName", acctName));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("productId", productId));
        strB.append(getTOString("todAllowed", todAllowed));
        strB.append(getTOString("permitedBy", permitedBy));
        strB.append(getTOString("permissionRefNo", permissionRefNo));
        strB.append(getTOString("permittedDt", permittedDt));
        strB.append(getTOString("typeOfTOD", typeOfTOD));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeUser", authorizeUser));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("trans_id", trans_id));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("productType", productType));
        strB.append(getTOString("intCalcDt", intCalcDt));
        strB.append(getTOString("repayPeriod", repayPeriod));
        strB.append(getTOString("repayPeriodDDMMYY", repayPeriodDDMMYY));
        strB.append(getTOString("repayDt", repayDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("accountNumber", accountNumber));
        strB.append(getTOXml("acctName", acctName));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("productId", productId));
        strB.append(getTOXml("todAllowed", todAllowed));
        strB.append(getTOXml("permitedBy", permitedBy));
        strB.append(getTOXml("permissionRefNo", permissionRefNo));
        strB.append(getTOXml("permittedDt", permittedDt));
        strB.append(getTOXml("typeOfTOD", typeOfTOD));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeUser", authorizeUser));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("trans_id", trans_id));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("productType", productType));
        strB.append(getTOXml("intCalcDt", intCalcDt));
        strB.append(getTOXml("repayPeriod", repayPeriod));
        strB.append(getTOXml("repayPeriodDDMMYY", repayPeriodDDMMYY));
        strB.append(getTOXml("repayDt", repayDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property authorizeUser.
     *
     * @return Value of property authorizeUser.
     */
    public java.lang.String getAuthorizeUser() {
        return authorizeUser;
    }

    /**
     * Setter for property authorizeUser.
     *
     * @param authorizeUser New value of property authorizeUser.
     */
    public void setAuthorizeUser(java.lang.String authorizeUser) {
        this.authorizeUser = authorizeUser;
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
     * Getter for property trans_id.
     *
     * @return Value of property trans_id.
     */
    public java.lang.String getTrans_id() {
        return trans_id;
    }

    /**
     * Setter for property trans_id.
     *
     * @param trans_id New value of property trans_id.
     */
    public void setTrans_id(java.lang.String trans_id) {
        this.trans_id = trans_id;
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
     * Getter for property fromDate.
     *
     * @return Value of property fromDate.
     */
    public java.util.Date getFromDate() {
        return fromDate;
    }

    /**
     * Setter for property fromDate.
     *
     * @param fromDate New value of property fromDate.
     */
    public void setFromDate(java.util.Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Getter for property toDate.
     *
     * @return Value of property toDate.
     */
    public java.util.Date getToDate() {
        return toDate;
    }

    /**
     * Setter for property toDate.
     *
     * @param toDate New value of property toDate.
     */
    public void setToDate(java.util.Date toDate) {
        this.toDate = toDate;
    }

    /**
     * Getter for property permittedDt.
     *
     * @return Value of property permittedDt.
     */
    public java.util.Date getPermittedDt() {
        return permittedDt;
    }

    /**
     * Setter for property permittedDt.
     *
     * @param permittedDt New value of property permittedDt.
     */
    public void setPermittedDt(java.util.Date permittedDt) {
        this.permittedDt = permittedDt;
    }

    /**
     * Getter for property branchCode.
     *
     * @return Value of property branchCode.
     */
    public java.lang.String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter for property branchCode.
     *
     * @param branchCode New value of property branchCode.
     */
    public void setBranchCode(java.lang.String branchCode) {
        this.branchCode = branchCode;
    }

    /**
     * Getter for property productType.
     *
     * @return Value of property productType.
     */
    public java.lang.String getProductType() {
        return productType;
    }

    /**
     * Setter for property productType.
     *
     * @param productType New value of property productType.
     */
    public void setProductType(java.lang.String productType) {
        this.productType = productType;
    }

    /**
     * Getter for property intCalcDt.
     *
     * @return Value of property intCalcDt.
     */
    public java.util.Date getIntCalcDt() {
        return intCalcDt;
    }

    /**
     * Setter for property intCalcDt.
     *
     * @param intCalcDt New value of property intCalcDt.
     */
    public void setIntCalcDt(java.util.Date intCalcDt) {
        this.intCalcDt = intCalcDt;
    }

    /**
     * Getter for property repayPeriod.
     *
     * @return Value of property repayPeriod.
     */
    public java.lang.Double getRepayPeriod() {
        return repayPeriod;
    }

    /**
     * Setter for property repayPeriod.
     *
     * @param repayPeriod New value of property repayPeriod.
     */
    public void setRepayPeriod(java.lang.Double repayPeriod) {
        this.repayPeriod = repayPeriod;
    }

    /**
     * Getter for property repayPeriodDDMMYY.
     *
     * @return Value of property repayPeriodDDMMYY.
     */
    public java.lang.String getRepayPeriodDDMMYY() {
        return repayPeriodDDMMYY;
    }

    /**
     * Setter for property repayPeriodDDMMYY.
     *
     * @param repayPeriodDDMMYY New value of property repayPeriodDDMMYY.
     */
    public void setRepayPeriodDDMMYY(java.lang.String repayPeriodDDMMYY) {
        this.repayPeriodDDMMYY = repayPeriodDDMMYY;
    }

    /**
     * Getter for property repayDt.
     *
     * @return Value of property repayDt.
     */
    public java.util.Date getRepayDt() {
        return repayDt;
    }

    /**
     * Setter for property repayDt.
     *
     * @param repayDt New value of property repayDt.
     */
    public void setRepayDt(java.util.Date repayDt) {
        this.repayDt = repayDt;
    }
}
