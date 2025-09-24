/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareAccInfoTO.java
 * 
 * Created on Sat Dec 25 13:11:12 IST 2004
 */
package com.see.truetransact.transferobject.share;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHARE_ACCT.
 */
public class ShareAccInfoTO extends TransferObject implements Serializable {

    private String shareAcctNo = "";
    private String custId = "";
    private String resolutionNo = "";
    private String propertyDetails = "";
    private String relativeMembers = "";
    private String connectedGroup = "";
    private String directorRelative = "";
    private Date idIssueDt = null;
    private String notEligibleLoan = "";
    private Date notEligibleDt = null;
    private Double applFee = null;
    private Double memFee = null;
    private Double shareFee = null;
    private Double shareAmount = null;
    private String welfareFundPaid = "";
    private String acctStatus = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorize = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private Date createdDt = null;
    private String createdBy = "";
    private String constitution = "";
    private String txtRatification = "";
    private Double imbp;
    private String empRefNoNew = "";
    private String empRefNoOld = "";
    private String commAddrType = "";
    private String shareType = "";
    private String remarks = "";
    private String branchCode = "";
    private String cboDivProdType = "";
    private String cboDivProdId = "";
    private String txtDivAcNo = "";
    private String cboDivPayMode = "";
    private String lblBalanceAmt = "";
    private String lblDivAmt = "";
    private String idCardNo = "";
    private String txtApplicationNo = "";
    //        added by Nikhil for duplicate ID
    private String chkDuplicateIDCardYN = "";
    private String txtIDResolutionNo = "";
    private Date tdtIDIssuedDt = null;
    private Date tdtIDResolutionDt = null;
//        added by Nikhil for Drf applicable
    private String chkDrfApplicableYN = "";
    private String drfStatus = "";
    private String cboDrfProdId = "";
    private String initiatedBranch = "";
    private Date closeDt = null;
    public void setInitiatedBranch(String shareAcctNo) {
        this.initiatedBranch = shareAcctNo;
    }

    public String getInitiatedBranch() {
        return initiatedBranch;
    }

    /**
     * Setter/Getter for SHARE_ACCT_NO - table Field
     */
    public void setShareAcctNo(String shareAcctNo) {
        this.shareAcctNo = shareAcctNo;
    }

    public String getShareAcctNo() {
        return shareAcctNo;
    }

    public void setTxtApplicationNo(String txtApplicationNo) {
        this.txtApplicationNo = txtApplicationNo;
    }

    public String getTxtApplicationNo() {
        return txtApplicationNo;
    }

    /**
     * Setter/Getter for CUST_ID - table Field
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
    }

    /**
     * Setter/Getter for RESOLUTION_NO - table Field
     */
    public void setResolutionNo(String resolutionNo) {
        this.resolutionNo = resolutionNo;
    }

    public String getResolutionNo() {
        return resolutionNo;
    }

    /**
     * Setter/Getter for PROPERTY_DETAILS - table Field
     */
    public void setPropertyDetails(String propertyDetails) {
        this.propertyDetails = propertyDetails;
    }

    public String getPropertyDetails() {
        return propertyDetails;
    }

    /**
     * Setter/Getter for RELATIVE_MEMBERS - table Field
     */
    public void setRelativeMembers(String relativeMembers) {
        this.relativeMembers = relativeMembers;
    }

    public String getRelativeMembers() {
        return relativeMembers;
    }

    /**
     * Setter/Getter for CONNECTED_GROUP - table Field
     */
    public void setConnectedGroup(String connectedGroup) {
        this.connectedGroup = connectedGroup;
    }

    public String getConnectedGroup() {
        return connectedGroup;
    }

    /**
     * Setter/Getter for DIRECTOR_RELATIVE - table Field
     */
    public void setDirectorRelative(String directorRelative) {
        this.directorRelative = directorRelative;
    }

    public String getDirectorRelative() {
        return directorRelative;
    }

    /**
     * Setter/Getter for ID_ISSUE_DT - table Field
     */
    public void setIdIssueDt(Date idIssueDt) {
        this.idIssueDt = idIssueDt;
    }

    public Date getIdIssueDt() {
        return idIssueDt;
    }

    /**
     * Setter/Getter for NOT_ELIGIBLE_LOAN - table Field
     */
    public void setNotEligibleLoan(String notEligibleLoan) {
        this.notEligibleLoan = notEligibleLoan;
    }

    public String getNotEligibleLoan() {
        return notEligibleLoan;
    }

    /**
     * Setter/Getter for NOT_ELIGIBLE_DT - table Field
     */
    public void setNotEligibleDt(Date notEligibleDt) {
        this.notEligibleDt = notEligibleDt;
    }

    public Date getNotEligibleDt() {
        return notEligibleDt;
    }

    /**
     * Setter/Getter for APPL_FEE - table Field
     */
    public void setApplFee(Double applFee) {
        this.applFee = applFee;
    }

    public Double getApplFee() {
        return applFee;
    }

    /**
     * Setter/Getter for MEM_FEE - table Field
     */
    public void setMemFee(Double memFee) {
        this.memFee = memFee;
    }

    public Double getMemFee() {
        return memFee;
    }

    /**
     * Setter/Getter for SHARE_FEE - table Field
     */
    public void setShareFee(Double shareFee) {
        this.shareFee = shareFee;
    }

    public Double getShareFee() {
        return shareFee;
    }

    /**
     * Setter/Getter for SHARE_AMOUNT - table Field
     */
    public void setShareAmount(Double shareAmount) {
        this.shareAmount = shareAmount;
    }

    public Double getShareAmount() {
        return shareAmount;
    }

    /**
     * Setter/Getter for WELFARE_FUND_PAID - table Field
     */
    public void setWelfareFundPaid(String welfareFundPaid) {
        this.welfareFundPaid = welfareFundPaid;
    }

    public String getWelfareFundPaid() {
        return welfareFundPaid;
    }

    /**
     * Setter/Getter for ACCT_STATUS - table Field
     */
    public void setAcctStatus(String acctStatus) {
        this.acctStatus = acctStatus;
    }

    public String getAcctStatus() {
        return acctStatus;
    }

    /**
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter/Getter for AUTHORIZE - table Field
     */
    public void setAuthorize(String authorize) {
        this.authorize = authorize;
    }

    public String getAuthorize() {
        return authorize;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Getter for property constitution.
     *
     * @return Value of property constitution.
     */
    public java.lang.String getConstitution() {
        return constitution;
    }

    /**
     * Setter for property constitution.
     *
     * @param constitution New value of property constitution.
     */
    public void setConstitution(java.lang.String constitution) {
        this.constitution = constitution;
    }

    public java.lang.String getTxtRatification() {
        return txtRatification;
    }

    /**
     * Setter for property constitution.
     *
     * @param constitution New value of property constitution.
     */
    public void setTxtRatification(java.lang.String txtRatification) {
        this.txtRatification = txtRatification;
    }

    public Double getImbp() {
        return imbp;
    }

    public void setImbp(Double imbp) {
        this.imbp = imbp;
    }

    public java.lang.String getEmpRefNoNew() {
        return empRefNoNew;
    }

    public void setEmpRefNoNew(java.lang.String empRefNoNew) {
        this.empRefNoNew = empRefNoNew;
    }

    public java.lang.String getEmpRefNoOld() {
        return empRefNoOld;
    }

    public void setEmpRefNoOld(java.lang.String empRefNoOld) {
        this.empRefNoOld = empRefNoOld;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("shareAcctNo");
        return shareAcctNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("shareAcctNo", shareAcctNo));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("resolutionNo", resolutionNo));
        strB.append(getTOString("propertyDetails", propertyDetails));
        strB.append(getTOString("relativeMembers", relativeMembers));
        strB.append(getTOString("connectedGroup", connectedGroup));
        strB.append(getTOString("directorRelative", directorRelative));
        strB.append(getTOString("idIssueDt", idIssueDt));
        strB.append(getTOString("notEligibleLoan", notEligibleLoan));
        strB.append(getTOString("notEligibleDt", notEligibleDt));
        strB.append(getTOString("applFee", applFee));
        strB.append(getTOString("memFee", memFee));
        strB.append(getTOString("shareFee", shareFee));
        strB.append(getTOString("shareAmount", shareAmount));
        strB.append(getTOString("welfareFundPaid", welfareFundPaid));
        strB.append(getTOString("acctStatus", acctStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorize", authorize));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("constitution", constitution));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("cboDivProdType", cboDivProdType));
        strB.append(getTOString("cboDrfProdId", cboDrfProdId));
        strB.append(getTOString("cboDivProdId", cboDivProdId));
        strB.append(getTOString("txtDivAcNo", txtDivAcNo));
        strB.append(getTOString("cboDivPayMode", cboDivPayMode));
        strB.append(getTOString("lblBalanceAmt", lblBalanceAmt));
        strB.append(getTOString("lblDivAmt", lblDivAmt));
        strB.append(getTOString("idCardNo", idCardNo));
        strB.append(getTOString("chkDuplicateIDCardYN", chkDuplicateIDCardYN));
        strB.append(getTOString("chkDrfApplicableYN", chkDrfApplicableYN));
        strB.append(getTOString("txtIDResolutionNo", txtIDResolutionNo));
        strB.append(getTOString("tdtIDIssuedDt", tdtIDIssuedDt));
        strB.append(getTOString("tdtIDResolutionDt", tdtIDResolutionDt));
        strB.append(getTOString("imbp", imbp));
        strB.append(getTOString("empRefNoNew", empRefNoNew));
        strB.append(getTOString("empRefNoOld", empRefNoOld));
        strB.append(getTOString("txtApllicationNo", txtApplicationNo));
        strB.append(getTOString("txtRatification", txtRatification));
        strB.append(getTOString("closeDt", closeDt));        
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("shareAcctNo", shareAcctNo));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("resolutionNo", resolutionNo));
        strB.append(getTOXml("propertyDetails", propertyDetails));
        strB.append(getTOXml("relativeMembers", relativeMembers));
        strB.append(getTOXml("connectedGroup", connectedGroup));
        strB.append(getTOXml("directorRelative", directorRelative));
        strB.append(getTOXml("idIssueDt", idIssueDt));
        strB.append(getTOXml("notEligibleLoan", notEligibleLoan));
        strB.append(getTOXml("notEligibleDt", notEligibleDt));
        strB.append(getTOXml("applFee", applFee));
        strB.append(getTOXml("memFee", memFee));
        strB.append(getTOXml("shareFee", shareFee));
        strB.append(getTOXml("shareAmount", shareAmount));
        strB.append(getTOXml("welfareFundPaid", welfareFundPaid));
        strB.append(getTOXml("acctStatus", acctStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorize", authorize));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("constitution", constitution));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("cboDivProdType", cboDivProdType));
        strB.append(getTOXml("cboDrfProdId", cboDrfProdId));
        strB.append(getTOXml("cboDivProdId", cboDivProdId));
        strB.append(getTOXml("txtDivAcNo", txtDivAcNo));
        strB.append(getTOXml("cboDivPayMode", cboDivPayMode));
        strB.append(getTOXml("lblBalanceAmt", lblBalanceAmt));
        strB.append(getTOXml("lblDivAmt", lblDivAmt));
        strB.append(getTOXml("idCardNo", idCardNo));
        strB.append(getTOXml("chkDuplicateIDCardYN", chkDuplicateIDCardYN));
        strB.append(getTOXml("chkDrfApplicableYN", chkDrfApplicableYN));
        strB.append(getTOXml("txtIDResolutionNo", txtIDResolutionNo));
        strB.append(getTOXml("tdtIDIssuedDt", tdtIDIssuedDt));
        strB.append(getTOXml("tdtIDResolutionDt", tdtIDResolutionDt));
        strB.append(getTOXml("txtApllicationNo", txtApplicationNo));
        strB.append(getTOXml("txtRatification", txtRatification));
        strB.append(getTOXml("imbp", imbp));
        strB.append(getTOXml("empRefNoNew", empRefNoNew));
        strB.append(getTOXml("empRefNoOld", empRefNoOld));
        strB.append(getTOXml("closeDt", closeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property commAddrType.
     *
     * @return Value of property commAddrType.
     */
    public java.lang.String getCommAddrType() {
        return commAddrType;
    }

    /**
     * Setter for property commAddrType.
     *
     * @param commAddrType New value of property commAddrType.
     */
    public void setCommAddrType(java.lang.String commAddrType) {
        this.commAddrType = commAddrType;
    }

    /**
     * Getter for property shareType.
     *
     * @return Value of property shareType.
     */
    public java.lang.String getShareType() {
        return shareType;
    }

    /**
     * Setter for property shareType.
     *
     * @param shareType New value of property shareType.
     */
    public void setShareType(java.lang.String shareType) {
        this.shareType = shareType;
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
     * Getter for property cboDivProdType.
     *
     * @return Value of property cboDivProdType.
     */
    public java.lang.String getCboDivProdType() {
        return cboDivProdType;
    }

    /**
     * Setter for property cboDivProdType.
     *
     * @param cboDivProdType New value of property cboDivProdType.
     */
    public void setCboDivProdType(java.lang.String cboDivProdType) {
        this.cboDivProdType = cboDivProdType;
    }

    /**
     * Getter for property cboDivProdId.
     *
     * @return Value of property cboDivProdId.
     */
    public java.lang.String getCboDivProdId() {
        return cboDivProdId;
    }

    /**
     * Setter for property cboDivProdId.
     *
     * @param cboDivProdId New value of property cboDivProdId.
     */
    public void setCboDivProdId(java.lang.String cboDivProdId) {
        this.cboDivProdId = cboDivProdId;
    }

    /**
     * Getter for property txtDivAcNo.
     *
     * @return Value of property txtDivAcNo.
     */
    public java.lang.String getTxtDivAcNo() {
        return txtDivAcNo;
    }

    /**
     * Setter for property txtDivAcNo.
     *
     * @param txtDivAcNo New value of property txtDivAcNo.
     */
    public void setTxtDivAcNo(java.lang.String txtDivAcNo) {
        this.txtDivAcNo = txtDivAcNo;
    }

    /**
     * Getter for property cboDivPayMode.
     *
     * @return Value of property cboDivPayMode.
     */
    public java.lang.String getCboDivPayMode() {
        return cboDivPayMode;
    }

    /**
     * Setter for property cboDivPayMode.
     *
     * @param cboDivPayMode New value of property cboDivPayMode.
     */
    public void setCboDivPayMode(java.lang.String cboDivPayMode) {
        this.cboDivPayMode = cboDivPayMode;
    }

    /**
     * Getter for property lblBalanceAmt.
     *
     * @return Value of property lblBalanceAmt.
     */
    public java.lang.String getLblBalanceAmt() {
        return lblBalanceAmt;
    }

    /**
     * Setter for property lblBalanceAmt.
     *
     * @param lblBalanceAmt New value of property lblBalanceAmt.
     */
    public void setLblBalanceAmt(java.lang.String lblBalanceAmt) {
        this.lblBalanceAmt = lblBalanceAmt;
    }

    /**
     * Getter for property lblDivAmt.
     *
     * @return Value of property lblDivAmt.
     */
    public java.lang.String getLblDivAmt() {
        return lblDivAmt;
    }

    /**
     * Setter for property lblDivAmt.
     *
     * @param lblDivAmt New value of property lblDivAmt.
     */
    public void setLblDivAmt(java.lang.String lblDivAmt) {
        this.lblDivAmt = lblDivAmt;
    }

    /**
     * Getter for property idCardNo.
     *
     * @return Value of property idCardNo.
     */
    public java.lang.String getIdCardNo() {
        return idCardNo;
    }

    /**
     * Setter for property idCardNo.
     *
     * @param idCardNo New value of property idCardNo.
     */
    public void setIdCardNo(java.lang.String idCardNo) {
        this.idCardNo = idCardNo;
    }

    /**
     * Getter for property chkDuplicateIDCardYN.
     *
     * @return Value of property chkDuplicateIDCardYN.
     */
    public java.lang.String getChkDuplicateIDCardYN() {
        return chkDuplicateIDCardYN;
    }

    /**
     * Setter for property chkDuplicateIDCardYN.
     *
     * @param chkDuplicateIDCardYN New value of property chkDuplicateIDCardYN.
     */
    public void setChkDuplicateIDCardYN(java.lang.String chkDuplicateIDCardYN) {
        this.chkDuplicateIDCardYN = chkDuplicateIDCardYN;
    }

    /**
     * Getter for property txtIDResolutionNo.
     *
     * @return Value of property txtIDResolutionNo.
     */
    public java.lang.String getTxtIDResolutionNo() {
        return txtIDResolutionNo;
    }

    /**
     * Setter for property txtIDResolutionNo.
     *
     * @param txtIDResolutionNo New value of property txtIDResolutionNo.
     */
    public void setTxtIDResolutionNo(java.lang.String txtIDResolutionNo) {
        this.txtIDResolutionNo = txtIDResolutionNo;
    }

    /**
     * Getter for property tdtIDIssuedDt.
     *
     * @return Value of property tdtIDIssuedDt.
     */
    public java.util.Date getTdtIDIssuedDt() {
        return tdtIDIssuedDt;
    }

    /**
     * Setter for property tdtIDIssuedDt.
     *
     * @param tdtIDIssuedDt New value of property tdtIDIssuedDt.
     */
    public void setTdtIDIssuedDt(java.util.Date tdtIDIssuedDt) {
        this.tdtIDIssuedDt = tdtIDIssuedDt;
    }

    /**
     * Getter for property tdtIDResolutionDt.
     *
     * @return Value of property tdtIDResolutionDt.
     */
    public java.util.Date getTdtIDResolutionDt() {
        return tdtIDResolutionDt;
    }

    /**
     * Setter for property tdtIDResolutionDt.
     *
     * @param tdtIDResolutionDt New value of property tdtIDResolutionDt.
     */
    public void setTdtIDResolutionDt(java.util.Date tdtIDResolutionDt) {
        this.tdtIDResolutionDt = tdtIDResolutionDt;
    }

    /**
     * Getter for property chkDrfApplicableYN.
     *
     * @return Value of property chkDrfApplicableYN.
     */
    public java.lang.String getChkDrfApplicableYN() {
        return chkDrfApplicableYN;
    }

    /**
     * Setter for property chkDrfApplicableYN.
     *
     * @param chkDrfApplicableYN New value of property chkDrfApplicableYN.
     */
    public void setChkDrfApplicableYN(java.lang.String chkDrfApplicableYN) {
        this.chkDrfApplicableYN = chkDrfApplicableYN;
    }

    /**
     * Getter for property drfStatus.
     *
     * @return Value of property drfStatus.
     */
    public java.lang.String getDrfStatus() {
        return drfStatus;
    }

    /**
     * Setter for property drfStatus.
     *
     * @param drfStatus New value of property drfStatus.
     */
    public void setDrfStatus(java.lang.String drfStatus) {
        this.drfStatus = drfStatus;
    }

    /**
     * Getter for property cboDrfProdId.
     *
     * @return Value of property cboDrfProdId.
     */
    public java.lang.String getCboDrfProdId() {
        return cboDrfProdId;
    }

    /**
     * Setter for property cboDrfProdId.
     *
     * @param cboDrfProdId New value of property cboDrfProdId.
     */
    public void setCboDrfProdId(java.lang.String cboDrfProdId) {
        this.cboDrfProdId = cboDrfProdId;
    }

    public Date getCloseDt() {
        return closeDt;
    }

    public void setCloseDt(Date closeDt) {
        this.closeDt = closeDt;
    }
    
}
