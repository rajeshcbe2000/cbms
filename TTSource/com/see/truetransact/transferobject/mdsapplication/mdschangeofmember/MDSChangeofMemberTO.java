/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSChangeofMemberTo.java
 * 
 * Created on Mon May 30 11:43:05 IST 2011
 */
package com.see.truetransact.transferobject.mdsapplication.mdschangeofmember;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is MDS_CHANGE_MEMBER.
 */
public class MDSChangeofMemberTO extends TransferObject implements Serializable {

    private String oldMemberNo = "";
    private String newMemberNo = "";
    private String newMemberName = "";
    private String oldMemberName = "";
    private String schemeName = "";
    private Integer divisionNo = 0; //AJITH
    private String chitNo = null;
    private Integer subNo = 0;  //AJITH
    private Integer installmentNo = 0;  //AJITH
    private Double totalAmount = 0.0;   //AJITH 
    private String newMemberMunnal = "";
    private Date changeEffectiveDate = null;
    private String remarks = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String branchCode = "";

    /**
     * Setter/Getter for CHITTAL_NO - table Field
     */
    public void setOldMemberNo(String oldMemberNo) {
        this.oldMemberNo = oldMemberNo;
    }

    public String getOldMemberNo() {
        return oldMemberNo;
    }

    /**
     * Setter/Getter for NEW_MEMBER_NO - table Field
     */
    public void setNewMemberNo(String newMemberNo) {
        this.newMemberNo = newMemberNo;
    }

    public String getNewMemberNo() {
        return newMemberNo;
    }

    /**
     * Setter/Getter for NEW_MEMBER_NAME - table Field
     */
    public void setNewMemberName(String newMemberName) {
        this.newMemberName = newMemberName;
    }

    public String getNewMemberName() {
        return newMemberName;
    }

    /**
     * Setter/Getter for SCHEME_NAME - table Field
     */
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSchemeName() {
        return schemeName;
    }

    /**
     * Setter/Getter for NEW_MEMBER_MUNNAL - table Field
     */
    public void setNewMemberMunnal(String newMemberMunnal) {
        this.newMemberMunnal = newMemberMunnal;
    }

    public String getNewMemberMunnal() {
        return newMemberMunnal;
    }

    /**
     * Setter/Getter for CHANGE_EFFECTIVE_DATE - table Field
     */
    public void setChangeEffectiveDate(Date changeEffectiveDate) {
        this.changeEffectiveDate = changeEffectiveDate;
    }

    public Date getChangeEffectiveDate() {
        return changeEffectiveDate;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
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
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

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
        strB.append(getTOString("oldMemberNo", oldMemberNo));
        strB.append(getTOString("newMemberNo", newMemberNo));
        strB.append(getTOString("newMemberName", newMemberName));
        strB.append(getTOString("oldMemberName", oldMemberName));
        strB.append(getTOString("schemeName", schemeName));
        strB.append(getTOString("divisionNo", divisionNo));
        strB.append(getTOString("subNo", subNo));
        strB.append(getTOString("chitNo", chitNo));
        strB.append(getTOString("installmentNo", installmentNo));
        strB.append(getTOString("totalAmount", totalAmount));
        strB.append(getTOString("newMemberMunnal", newMemberMunnal));
        strB.append(getTOString("changeEffectiveDate", changeEffectiveDate));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("oldMemberNo", oldMemberNo));
        strB.append(getTOXml("newMemberNo", newMemberNo));
        strB.append(getTOXml("newMemberName", newMemberName));
        strB.append(getTOXml("oldMemberName", oldMemberName));
        strB.append(getTOXml("schemeName", schemeName));
        strB.append(getTOXml("divisionNo", divisionNo));
        strB.append(getTOXml("subNo", subNo));
        strB.append(getTOXml("chitNo", chitNo));
        strB.append(getTOXml("installmentNo", installmentNo));
        strB.append(getTOXml("totalAmount", totalAmount));
        strB.append(getTOXml("newMemberMunnal", newMemberMunnal));
        strB.append(getTOXml("changeEffectiveDate", changeEffectiveDate));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public Integer getDivisionNo() {
        return divisionNo;
    }

    public void setDivisionNo(Integer divisionNo) {
        this.divisionNo = divisionNo;
    }

    /**
     * Getter for property chitNo.
     *
     * @return Value of property chitNo.
     */
    public java.lang.String getChitNo() {
        return chitNo;
    }

    /**
     * Setter for property chitNo.
     *
     * @param chitNo New value of property chitNo.
     */
    public void setChitNo(java.lang.String chitNo) {
        this.chitNo = chitNo;
    }

    public Integer getInstallmentNo() {
        return installmentNo;
    }

    public void setInstallmentNo(Integer installmentNo) {
        this.installmentNo = installmentNo;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
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
     * Getter for property oldMemberName.
     *
     * @return Value of property oldMemberName.
     */
    public java.lang.String getOldMemberName() {
        return oldMemberName;
    }

    /**
     * Setter for property oldMemberName.
     *
     * @param oldMemberName New value of property oldMemberName.
     */
    public void setOldMemberName(java.lang.String oldMemberName) {
        this.oldMemberName = oldMemberName;
    }

    public Integer getSubNo() {
        return subNo;
    }

    public void setSubNo(Integer subNo) {
        this.subNo = subNo;
    }

}