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
public class SuspenseAccountMasterTO extends TransferObject implements Serializable {

    private String cboSuspenseProdID = "";
    private String cboAgentID = "";
    private String cboDealer = "";//Added By Revathi.L
    private String txtSuspenseProdDescription = "";
    private String txtSuspenseActNum = "";
    private String txtMemberNumber = "";
    private String txtCustomerId = "";
    private String txtName = "";
    private String txtAddress = "";
    private Date tdtSuspenseOpenDate = null;
    private String txtPrefix = "";
    private String txtAccRefNo = "";
    private String authorizeStatus = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorize = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchCode = "";
    private String rdoSalRecovery = null;
    private String isAuction = null;
    private Double totalBalace = 0.0;   
    private Date cloeseDt = null;
    private String acctStatus = null;
    private Date intCalcUpToDt = null;

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
        strB.append(getTOString("cboSuspenseProdID", cboSuspenseProdID));
        strB.append(getTOString("txtSuspenseProdDescription", txtSuspenseProdDescription));
        strB.append(getTOString("txtSuspenseActNum", txtSuspenseActNum));
        strB.append(getTOString("txtPrefix", txtPrefix));
        strB.append(getTOString("tdtSuspenseOpenDate", tdtSuspenseOpenDate));
        strB.append(getTOString("statustxtMemberNumber", txtMemberNumber));
        strB.append(getTOString("statustxtCustomerId", txtCustomerId));
        strB.append(getTOString("txtName", txtName));
        strB.append(getTOString("txtAddress", txtAddress));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorize", authorize));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("rdoSalRecovery", rdoSalRecovery));
        strB.append(getTOString("cboAgentID", cboAgentID));
        strB.append(getTOString("cboDealer", cboDealer));
        strB.append(getTOString("isAuction", isAuction));
        strB.append(getTOString("totalBalace", totalBalace));
        strB.append(getTOString("intCalcUpToDt", intCalcUpToDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("cboSuspenseProdID", cboSuspenseProdID));
        strB.append(getTOXml("txtSuspenseProdDescription", txtSuspenseProdDescription));
        strB.append(getTOXml("txtSuspenseActNum", txtSuspenseActNum));
        strB.append(getTOXml("txtPrefix", txtPrefix));
        strB.append(getTOXml("tdtSuspenseOpenDate", tdtSuspenseOpenDate));
        strB.append(getTOXml("txtMemberNumber", txtMemberNumber));
        strB.append(getTOXml("txtCustomerId", txtCustomerId));
        strB.append(getTOXml("txtName", txtName));
        strB.append(getTOXml("txtAddress", txtAddress));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorize", authorize));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("rdoSalRecovery", rdoSalRecovery));
        strB.append(getTOXml("cboAgentID", cboAgentID));
        strB.append(getTOXml("cboDealer", cboDealer));
        strB.append(getTOXml("isAuction", isAuction));
        strB.append(getTOXml("totalBalace", totalBalace));
        strB.append(getTOXml("intCalcUpToDt", intCalcUpToDt));
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
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    public String getCboAgentID() {
        return cboAgentID;
    }

    public void setCboAgentID(String cboAgentID) {
        this.cboAgentID = cboAgentID;
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
     * Getter for property txtSuspenseActNum.
     *
     * @return Value of property txtSuspenseActNum.
     */
    public java.lang.String getTxtSuspenseActNum() {
        return txtSuspenseActNum;
    }

    /**
     * Setter for property txtSuspenseActNum.
     *
     * @param txtSuspenseActNum New value of property txtSuspenseActNum.
     */
    public void setTxtSuspenseActNum(java.lang.String txtSuspenseActNum) {
        this.txtSuspenseActNum = txtSuspenseActNum;
    }

    /**
     * Getter for property txtMemberNumber.
     *
     * @return Value of property txtMemberNumber.
     */
    public java.lang.String getTxtMemberNumber() {
        return txtMemberNumber;
    }

    /**
     * Setter for property txtMemberNumber.
     *
     * @param txtMemberNumber New value of property txtMemberNumber.
     */
    public void setTxtMemberNumber(java.lang.String txtMemberNumber) {
        this.txtMemberNumber = txtMemberNumber;
    }

    /**
     * Getter for property txtCustomerId.
     *
     * @return Value of property txtCustomerId.
     */
    public java.lang.String getTxtCustomerId() {
        return txtCustomerId;
    }

    /**
     * Setter for property txtCustomerId.
     *
     * @param txtCustomerId New value of property txtCustomerId.
     */
    public void setTxtCustomerId(java.lang.String txtCustomerId) {
        this.txtCustomerId = txtCustomerId;
    }

    /**
     * Getter for property tdtSuspenseOpenDate.
     *
     * @return Value of property tdtSuspenseOpenDate.
     */
    public java.util.Date getTdtSuspenseOpenDate() {
        return tdtSuspenseOpenDate;
    }

    /**
     * Setter for property tdtSuspenseOpenDate.
     *
     * @param tdtSuspenseOpenDate New value of property tdtSuspenseOpenDate.
     */
    public void setTdtSuspenseOpenDate(java.util.Date tdtSuspenseOpenDate) {
        this.tdtSuspenseOpenDate = tdtSuspenseOpenDate;
    }

    /**
     * Getter for property txtName.
     *
     * @return Value of property txtName.
     */
    public java.lang.String getTxtName() {
        return txtName;
    }

    /**
     * Setter for property txtName.
     *
     * @param txtName New value of property txtName.
     */
    public void setTxtName(java.lang.String txtName) {
        this.txtName = txtName;
    }

    /**
     * Getter for property txtAddress.
     *
     * @return Value of property txtAddress.
     */
    public java.lang.String getTxtAddress() {
        return txtAddress;
    }

    /**
     * Setter for property txtAddress.
     *
     * @param txtAddress New value of property txtAddress.
     */
    public void setTxtAddress(java.lang.String txtAddress) {
        this.txtAddress = txtAddress;
    }

    /**
     * Getter for property cboSuspenseProdID.
     *
     * @return Value of property cboSuspenseProdID.
     */
    public java.lang.String getCboSuspenseProdID() {
        return cboSuspenseProdID;
    }

    /**
     * Setter for property cboSuspenseProdID.
     *
     * @param cboSuspenseProdID New value of property cboSuspenseProdID.
     */
    public void setCboSuspenseProdID(java.lang.String cboSuspenseProdID) {
        this.cboSuspenseProdID = cboSuspenseProdID;
    }

    /**
     * Getter for property txtSuspenseProdDescription.
     *
     * @return Value of property txtSuspenseProdDescription.
     */
    public java.lang.String getTxtSuspenseProdDescription() {
        return txtSuspenseProdDescription;
    }

    /**
     * Setter for property txtSuspenseProdDescription.
     *
     * @param txtSuspenseProdDescription New value of property
     * txtSuspenseProdDescription.
     */
    public void setTxtSuspenseProdDescription(java.lang.String txtSuspenseProdDescription) {
        this.txtSuspenseProdDescription = txtSuspenseProdDescription;
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

    /**
     * Getter for property txtAccRefNo.
     *
     * @return Value of property txtAccRefNo.
     */
    public String getTxtAccRefNo() {
        return txtAccRefNo;
    }

    /**
     * Setter for property txtAccRefNo.
     *
     * @param txtAccRefNo New value of property txtAccRefNo.
     */
    public void setTxtAccRefNo(String txtAccRefNo) {
        this.txtAccRefNo = txtAccRefNo;
    }

    /**
     * Getter for property rdoSalRecovery.
     *
     * @return Value of property rdoSalRecovery.
     */
    public String getRdoSalRecovery() {
        return rdoSalRecovery;
    }

    /**
     * Setter for property rdoSalRecovery.
     *
     * @param rdoSalRecovery New value of property rdoSalRecovery.
     */
    public void setRdoSalRecovery(String rdoSalRecovery) {
        this.rdoSalRecovery = rdoSalRecovery;
    }

    public String getIsAuction() {
        return isAuction;
    }

    public void setIsAuction(String isAuction) {
        this.isAuction = isAuction;
    }

    public Double getTotalBalace() {
        return totalBalace;
    }

    public void setTotalBalace(Double totalBalace) {
        this.totalBalace = totalBalace;
    }

    public String getCboDealer() {
        return cboDealer;
    }

    public void setCboDealer(String cboDealer) {
        this.cboDealer = cboDealer;
    }

    public Date getIntCalcUpToDt() {
        return intCalcUpToDt;
    }

    public void setIntCalcUpToDt(Date intCalcUpToDt) {
        this.intCalcUpToDt = intCalcUpToDt;
    }
    
    
    
}