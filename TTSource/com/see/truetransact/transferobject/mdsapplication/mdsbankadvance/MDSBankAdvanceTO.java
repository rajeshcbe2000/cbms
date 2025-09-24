/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * mdsbankadvance.java
 * 
 * Created on Thu Oct 27 18:20:38 IST 2011
 */
package com.see.truetransact.transferobject.mdsapplication.mdsbankadvance;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is MDS_BANK_ADVANCE.
 */
public class MDSBankAdvanceTO extends TransferObject implements Serializable {

    private String bankAdvId = "";
    private Date bankAdvDt = null;
    private String schemeName = "";
    private String chittalNo = "";
    private Integer subNo = 0;
    private Double divisionNo = null;
    private Double installmentNo = null;
    private Date installmentDt = null;
    private Double instAmt = null;
    private Double bonusAmt = null;
    private String repaid = "";
    private Date repaidDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;

    /**
     * Setter/Getter for BANK_ADV_ID - table Field
     */
    public void setBankAdvId(String bankAdvId) {
        this.bankAdvId = bankAdvId;
    }

    public String getBankAdvId() {
        return bankAdvId;
    }

    /**
     * Setter/Getter for BANK_ADV_DT - table Field
     */
    public void setBankAdvDt(Date bankAdvDt) {
        this.bankAdvDt = bankAdvDt;
    }

    public Date getBankAdvDt() {
        return bankAdvDt;
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
     * Setter/Getter for CHITTAL_NO - table Field
     */
    public void setChittalNo(String chittalNo) {
        this.chittalNo = chittalNo;
    }

    public String getChittalNo() {
        return chittalNo;
    }

    /**
     * Setter/Getter for DIVISION_NO - table Field
     */
    public void setDivisionNo(Double divisionNo) {
        this.divisionNo = divisionNo;
    }

    public Double getDivisionNo() {
        return divisionNo;
    }

    /**
     * Setter/Getter for INSTALLMENT_NO - table Field
     */
    public void setInstallmentNo(Double installmentNo) {
        this.installmentNo = installmentNo;
    }

    public Double getInstallmentNo() {
        return installmentNo;
    }

    /**
     * Setter/Getter for INSTALLMENT_DT - table Field
     */
    public void setInstallmentDt(Date installmentDt) {
        this.installmentDt = installmentDt;
    }

    public Date getInstallmentDt() {
        return installmentDt;
    }

    /**
     * Setter/Getter for INST_AMT - table Field
     */
    public void setInstAmt(Double instAmt) {
        this.instAmt = instAmt;
    }

    public Double getInstAmt() {
        return instAmt;
    }

    /**
     * Setter/Getter for BONUS_AMT - table Field
     */
    public void setBonusAmt(Double bonusAmt) {
        this.bonusAmt = bonusAmt;
    }

    public Double getBonusAmt() {
        return bonusAmt;
    }

    /**
     * Setter/Getter for REPAID - table Field
     */
    public void setRepaid(String repaid) {
        this.repaid = repaid;
    }

    public String getRepaid() {
        return repaid;
    }

    /**
     * Setter/Getter for REPAID_DT - table Field
     */
    public void setRepaidDt(Date repaidDt) {
        this.repaidDt = repaidDt;
    }

    public Date getRepaidDt() {
        return repaidDt;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
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
        strB.append(getTOString("bankAdvId", bankAdvId));
        strB.append(getTOString("bankAdvDt", bankAdvDt));
        strB.append(getTOString("schemeName", schemeName));
        strB.append(getTOString("chittalNo", chittalNo));
        strB.append(getTOString("subNo", subNo));
        strB.append(getTOString("divisionNo", divisionNo));
        strB.append(getTOString("installmentNo", installmentNo));
        strB.append(getTOString("installmentDt", installmentDt));
        strB.append(getTOString("instAmt", instAmt));
        strB.append(getTOString("bonusAmt", bonusAmt));
        strB.append(getTOString("repaid", repaid));
        strB.append(getTOString("repaidDt", repaidDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("bankAdvId", bankAdvId));
        strB.append(getTOXml("bankAdvDt", bankAdvDt));
        strB.append(getTOXml("schemeName", schemeName));
        strB.append(getTOXml("chittalNo", chittalNo));
        strB.append(getTOXml("subNo", subNo));
        strB.append(getTOXml("divisionNo", divisionNo));
        strB.append(getTOXml("installmentNo", installmentNo));
        strB.append(getTOXml("installmentDt", installmentDt));
        strB.append(getTOXml("instAmt", instAmt));
        strB.append(getTOXml("bonusAmt", bonusAmt));
        strB.append(getTOXml("repaid", repaid));
        strB.append(getTOXml("repaidDt", repaidDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public Integer getSubNo() {
        return subNo;
    }

    public void setSubNo(Integer subNo) {
        this.subNo = subNo;
    }
}