/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * Multiple CustomerId Change.java
 * 
 * Created By Kannan AR Fri Jan 20 16:20:12 IST 2017
 */
package com.see.truetransact.transferobject.customer.multipleCustomerIdChange;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is MULTIPLE_CUST_MERGE
 */
public class MultipleCustomerIdChangeTO extends TransferObject implements Serializable {

    private String txtCustomerName = "";
    private String cboSearchCriteria = "";
    private String txtMemberNo = "";
    private String txtAddress = "";
    private String txtPhoneNumber = "";
    private String txtCareOfName = "";
    private String txtCustomerID = "";
    private String txtEmployeeNo = "";
    private String txtUniqueIdNo = "";
    private String txtPanNO = "";
    private String txtPassPortNo = "";
    private Date tdtDtOfBirth = null;
    private String txtBranchId = "";
    private String lblBranchName = "";
    private String txtAccountNumber = "";
    private String lblNewCustName = "";
    private String txtNewCustomerId = "";
    private String multipleBatchId = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String createdBy = "";
    private Date createdDate = null;
    private String authBy = "";
    private Date authDate = null;
    private String status = "";
    private String authstatus = "";
    private String initiated_branch = "";

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("txtCustomerName", txtCustomerName));
        strB.append(getTOString("cboSearchCriteria", cboSearchCriteria));
        strB.append(getTOString("txtMemberNo", txtMemberNo));
        strB.append(getTOString("txtAddress", txtAddress));
        strB.append(getTOString("txtPhoneNumber", txtPhoneNumber));
        strB.append(getTOString("txtCareOfName", txtCareOfName));
        strB.append(getTOString("txtCustomerID", txtCustomerID));
        strB.append(getTOString("txtEmployeeNo", txtEmployeeNo));
        strB.append(getTOString("txtUniqueIdNo", txtUniqueIdNo));
        strB.append(getTOString("txtPanNO", txtPanNO));
        strB.append(getTOString("txtPassPortNo", txtPassPortNo));
        strB.append(getTOString("tdtDtOfBirth", tdtDtOfBirth));
        strB.append(getTOString("txtBranchId", txtBranchId));
        strB.append(getTOString("lblBranchName", lblBranchName));
        strB.append(getTOString("txtAccountNumber", txtAccountNumber));
        strB.append(getTOString("lblNewCustName", lblNewCustName));
        strB.append(getTOString("txtNewCustomerId", txtNewCustomerId));
        strB.append(getTOString("initiated_branch", initiated_branch));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDate", createdDate));
        strB.append(getTOString("authDate", authDate));
        strB.append(getTOString("initiated_branch", initiated_branch));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authstatus", authstatus));
        strB.append(getTOString("multipleBatchId", multipleBatchId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("txtCustomerName", txtCustomerName));
        strB.append(getTOXml("cboSearchCriteria", cboSearchCriteria));
        strB.append(getTOXml("txtMemberNo", txtMemberNo));
        strB.append(getTOXml("txtAddress", txtAddress));
        strB.append(getTOXml("txtPhoneNumber", txtPhoneNumber));
        strB.append(getTOXml("txtCareOfName", txtCareOfName));
        strB.append(getTOXml("txtCustomerID", txtCustomerID));
        strB.append(getTOXml("txtEmployeeNo", txtEmployeeNo));
        strB.append(getTOXml("txtUniqueIdNo", txtUniqueIdNo));
        strB.append(getTOXml("txtPanNO", txtPanNO));
        strB.append(getTOXml("txtPassPortNo", txtPassPortNo));
        strB.append(getTOXml("tdtDtOfBirth", tdtDtOfBirth));
        strB.append(getTOXml("txtBranchId", txtBranchId));
        strB.append(getTOXml("lblBranchName", lblBranchName));
        strB.append(getTOXml("txtAccountNumber", txtAccountNumber));
        strB.append(getTOXml("lblNewCustName", lblNewCustName));
        strB.append(getTOXml("txtNewCustomerId", txtNewCustomerId));
        strB.append(getTOXml("initiated_branch", initiated_branch));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDate", createdDate));
        strB.append(getTOXml("authDate", authDate));
        strB.append(getTOXml("initiated_branch", initiated_branch));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authstatus", authstatus));
        strB.append(getTOXml("multipleBatchId", multipleBatchId));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property authBy.
     *
     * @return Value of property authBy.
     */
    public java.lang.String getAuthBy() {
        return authBy;
    }

    /**
     * Setter for property authBy.
     *
     * @param authBy New value of property authBy.
     */
    public void setAuthBy(java.lang.String authBy) {
        this.authBy = authBy;
    }

    /**
     * Getter for property authDate.
     *
     * @return Value of property authDate.
     */
    public java.util.Date getAuthDate() {
        return authDate;
    }

    /**
     * Setter for property authDate.
     *
     * @param authDate New value of property authDate.
     */
    public void setAuthDate(java.util.Date authDate) {
        this.authDate = authDate;
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
     * Getter for property authstatus.
     *
     * @return Value of property authstatus.
     */
    public java.lang.String getAuthstatus() {
        return authstatus;
    }

    /**
     * Setter for property authstatus.
     *
     * @param authstatus New value of property authstatus.
     */
    public void setAuthstatus(java.lang.String authstatus) {
        this.authstatus = authstatus;
    }

    /**
     * Getter for property createdDate.
     *
     * @return Value of property createdDate.
     */
    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Setter for property createdDate.
     *
     * @param createdDate New value of property createdDate.
     */
    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getMultipleBatchId() {
        return multipleBatchId;
    }

    public void setMultipleBatchId(String multipleBatchId) {
        this.multipleBatchId = multipleBatchId;
    }

    public String getTxtCustomerName() {
        return txtCustomerName;
    }

    public void setTxtCustomerName(String txtCustomerName) {
        this.txtCustomerName = txtCustomerName;
    }

    public String getCboSearchCriteria() {
        return cboSearchCriteria;
    }

    public void setCboSearchCriteria(String cboSearchCriteria) {
        this.cboSearchCriteria = cboSearchCriteria;
    }

    public String getTxtMemberNo() {
        return txtMemberNo;
    }

    public void setTxtMemberNo(String txtMemberNo) {
        this.txtMemberNo = txtMemberNo;
    }

    public String getTxtAddress() {
        return txtAddress;
    }

    public void setTxtAddress(String txtAddress) {
        this.txtAddress = txtAddress;
    }

    public String getTxtPhoneNumber() {
        return txtPhoneNumber;
    }

    public void setTxtPhoneNumber(String txtPhoneNumber) {
        this.txtPhoneNumber = txtPhoneNumber;
    }

    public String getTxtCareOfName() {
        return txtCareOfName;
    }

    public void setTxtCareOfName(String txtCareOfName) {
        this.txtCareOfName = txtCareOfName;
    }

    public String getTxtCustomerID() {
        return txtCustomerID;
    }

    public void setTxtCustomerID(String txtCustomerID) {
        this.txtCustomerID = txtCustomerID;
    }

    public String getTxtEmployeeNo() {
        return txtEmployeeNo;
    }

    public void setTxtEmployeeNo(String txtEmployeeNo) {
        this.txtEmployeeNo = txtEmployeeNo;
    }

    public String getTxtUniqueIdNo() {
        return txtUniqueIdNo;
    }

    public void setTxtUniqueIdNo(String txtUniqueIdNo) {
        this.txtUniqueIdNo = txtUniqueIdNo;
    }

    public String getTxtPanNO() {
        return txtPanNO;
    }

    public void setTxtPanNO(String txtPanNO) {
        this.txtPanNO = txtPanNO;
    }

    public String getTxtPassPortNo() {
        return txtPassPortNo;
    }

    public void setTxtPassPortNo(String txtPassPortNo) {
        this.txtPassPortNo = txtPassPortNo;
    }

    

    public String getTxtBranchId() {
        return txtBranchId;
    }

    public void setTxtBranchId(String txtBranchId) {
        this.txtBranchId = txtBranchId;
    }

    public Date getTdtDtOfBirth() {
        return tdtDtOfBirth;
    }

    public void setTdtDtOfBirth(Date tdtDtOfBirth) {
        this.tdtDtOfBirth = tdtDtOfBirth;
    }

    public String getLblBranchName() {
        return lblBranchName;
    }

    public void setLblBranchName(String lblBranchName) {
        this.lblBranchName = lblBranchName;
    }

    public String getTxtAccountNumber() {
        return txtAccountNumber;
    }

    public void setTxtAccountNumber(String txtAccountNumber) {
        this.txtAccountNumber = txtAccountNumber;
    }

    public String getLblNewCustName() {
        return lblNewCustName;
    }

    public void setLblNewCustName(String lblNewCustName) {
        this.lblNewCustName = lblNewCustName;
    }

    public String getTxtNewCustomerId() {
        return txtNewCustomerId;
    }

    public void setTxtNewCustomerId(String txtNewCustomerId) {
        this.txtNewCustomerId = txtNewCustomerId;
    }

    public String getInitiated_branch() {
        return initiated_branch;
    }

    public void setInitiated_branch(String initiated_branch) {
        this.initiated_branch = initiated_branch;
    }
}