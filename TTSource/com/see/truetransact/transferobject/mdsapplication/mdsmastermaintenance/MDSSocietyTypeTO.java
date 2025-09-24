/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSSocietyTypeTO.java
 *
 * Created on Wed Dec 1 16:19:00 IST 2012
 */
package com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is MDS_SOCIETY_TYPE.
 */
public class MDSSocietyTypeTO extends TransferObject implements Serializable {

    private String chittalNo = "";
    private Integer subNo = 0;   //AJITH
    private String institution = "";
    private String name = "";
    private Double amount;
    private String securityNo = "";
    private String securityType = "";
    private String remarks = "";
    private Double maturityValue;
    private Date issueDt;
    private Date matDt;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String branchCode = "";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";

    /**
     * Setter/Getter for ACCT_NUM - table Field
     */
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("acctNum" + KEY_VAL_SEPARATOR + "slno");
        return KEY_VAL_SEPARATOR;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("chittalNo", chittalNo));
        strB.append(getTOString("subNo", subNo));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("institution", institution));
        strB.append(getTOString("name", name));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("securityNo", securityNo));
        strB.append(getTOString("securityType", securityType));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("maturityValue", maturityValue));
        strB.append(getTOString("issueDt", issueDt));
        strB.append(getTOString("matDt", matDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOString("chittalNo", chittalNo));
        strB.append(getTOString("subNo", subNo));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("institution", institution));
        strB.append(getTOXml("name", name));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("securityNo", securityNo));
        strB.append(getTOXml("securityType", securityType));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("maturityValue", maturityValue));
        strB.append(getTOXml("issueDt", issueDt));
        strB.append(getTOXml("matDt", matDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property txtJewelleryDetails.
     *
     * @return Value of property txtJewelleryDetails.
     */
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
     * Getter for property chittalNo.
     *
     * @return Value of property chittalNo.
     */
    public java.lang.String getChittalNo() {
        return chittalNo;
    }

    /**
     * Setter for property chittalNo.
     *
     * @param chittalNo New value of property chittalNo.
     */
    public void setChittalNo(java.lang.String chittalNo) {
        this.chittalNo = chittalNo;
    }

    //    }
    //    public Integer getSubNo() {
    //        return subNo;
    //    }
    //
    //    public void setSubNo(Integer subNo) {
    //        this.subNo = subNo;
    public Integer getSubNo() {
        return subNo;
    }

    public void setSubNo(Integer subNo) {
        this.subNo = subNo;
    }
    
    
    

    /**
     * Getter for property institution.
     *
     * @return Value of property institution.
     */
    public java.lang.String getInstitution() {
        return institution;
    }

    /**
     * Setter for property institution.
     *
     * @param institution New value of property institution.
     */
    public void setInstitution(java.lang.String institution) {
        this.institution = institution;
    }

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * Getter for property amount.
     *
     * @return Value of property amount.
     */
    public java.lang.Double getAmount() {
        return amount;
    }

    /**
     * Setter for property amount.
     *
     * @param amount New value of property amount.
     */
    public void setAmount(java.lang.Double amount) {
        this.amount = amount;
    }

    /**
     * Getter for property securityNo.
     *
     * @return Value of property securityNo.
     */
    public java.lang.String getSecurityNo() {
        return securityNo;
    }

    /**
     * Setter for property securityNo.
     *
     * @param securityNo New value of property securityNo.
     */
    public void setSecurityNo(java.lang.String securityNo) {
        this.securityNo = securityNo;
    }

    /**
     * Getter for property securityType.
     *
     * @return Value of property securityType.
     */
    public java.lang.String getSecurityType() {
        return securityType;
    }

    /**
     * Setter for property securityType.
     *
     * @param securityType New value of property securityType.
     */
    public void setSecurityType(java.lang.String securityType) {
        this.securityType = securityType;
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
     * Getter for property maturityValue.
     *
     * @return Value of property maturityValue.
     */
    public java.lang.Double getMaturityValue() {
        return maturityValue;
    }

    /**
     * Setter for property maturityValue.
     *
     * @param maturityValue New value of property maturityValue.
     */
    public void setMaturityValue(java.lang.Double maturityValue) {
        this.maturityValue = maturityValue;
    }

    /**
     * Getter for property issueDt.
     *
     * @return Value of property issueDt.
     */
    public java.util.Date getIssueDt() {
        return issueDt;
    }

    /**
     * Setter for property issueDt.
     *
     * @param issueDt New value of property issueDt.
     */
    public void setIssueDt(java.util.Date issueDt) {
        this.issueDt = issueDt;
    }

    /**
     * Getter for property matDt.
     *
     * @return Value of property matDt.
     */
    public java.util.Date getMatDt() {
        return matDt;
    }

    /**
     * Setter for property matDt.
     *
     * @param matDt New value of property matDt.
     */
    public void setMatDt(java.util.Date matDt) {
        this.matDt = matDt;
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
     * Getter for property authorizedDt.
     *
     * @return Value of property authorizedDt.
     */
    public java.util.Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter for property authorizedDt.
     *
     * @param authorizedDt New value of property authorizedDt.
     */
    public void setAuthorizedDt(java.util.Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    /**
     * Getter for property authorizedBy.
     *
     * @return Value of property authorizedBy.
     */
    public java.lang.String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter for property authorizedBy.
     *
     * @param authorizedBy New value of property authorizedBy.
     */
    public void setAuthorizedBy(java.lang.String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }
}