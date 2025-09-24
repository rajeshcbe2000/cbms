/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OtherBankTO.java
 * 
 * Created on20-05-2009 IST 
 */
package com.see.truetransact.transferobject.sysadmin.stateTalukMaster;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OTHER_BANK.
 */
public class StateTalukTO extends TransferObject implements Serializable {

    private Integer stateCode = 0;
    private Integer stateName = 0;
    private String talukName = "";
    private String talukCode = "";
    private String disCode = "";
    private String disName = "";
    private String status = "";
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String slno = "";
    private String stateSlno = "";
    private String disStatus = "";
    private String talukStatus = "";
    private String sateStatusBy = "";
    private String talStatusBy = "";
    private String disStatusBy = "";
    private String stateCreatedBy = "";
    private String talCreatedBy = "";
    private String disCreatedBy = "";
    private String disAuthorizeStatus = null;
    private String disAuthorizeBy = "";
    private Date disAuthorizeDt = null;
    private String talAuthorizeStatus = null;
    private String talAuthorizeBy = "";
    private Date talAuthorizeDt = null;
    private Date stateStatusDt = null;
    private String verification = "";
    private String branCode = "";

  
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

    public Integer getStateCode() {
        return stateCode;
    }

    public void setStateCode(Integer stateCode) {
        this.stateCode = stateCode;
    }

    public Integer getStateName() {
        return stateName;
    }

    public void setStateName(Integer stateName) {
        this.stateName = stateName;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("stateCode");
        return stateCode.toString();
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("stateCode", stateCode));
        strB.append(getTOString("stateName", stateName));
        strB.append(getTOString("talukCode", talukCode));
        strB.append(getTOString("talukName", talukName));
        strB.append(getTOString("disCode", disCode));
        strB.append(getTOString("disName", disName));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("slno", slno));
        strB.append(getTOString("stateSlno", stateSlno));
        strB.append(getTOString("disStatus", disStatus));
        strB.append(getTOString("talukStatus", talukStatus));
        strB.append(getTOString("sateStatusBy", sateStatusBy));
        strB.append(getTOString("talStatusBy", talStatusBy));
        strB.append(getTOString("disStatusBy", disStatusBy));
        strB.append(getTOString("stateCreatedBy", stateCreatedBy));
        strB.append(getTOString("talCreatedBy", talCreatedBy));
        strB.append(getTOString("disCreatedBy", disCreatedBy));
        strB.append(getTOString("disAuthorizeStatus", disAuthorizeStatus));
        strB.append(getTOString("disAuthorizeBy", disAuthorizeBy));
        strB.append(getTOString("disAuthorizeDt", disAuthorizeDt));
        strB.append(getTOString("talAuthorizeStatus", talAuthorizeStatus));
        strB.append(getTOString("talAuthorizeBy", talAuthorizeBy));
        strB.append(getTOString("talAuthorizeDt", talAuthorizeDt));
        strB.append(getTOString("stateStatusDt", stateStatusDt));
        strB.append(getTOString("verification", verification));
        strB.append(getTOString("branCode", branCode));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("stateCode", stateCode));
        strB.append(getTOXml("stateName", stateName));
        strB.append(getTOXml("talukCode", talukCode));
        strB.append(getTOXml("talukName", talukName));
        strB.append(getTOXml("disName", disName));
        strB.append(getTOXml("disCode", disCode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXml("stateSlno", stateSlno));
        strB.append(getTOXml("disStatus", disStatus));
        strB.append(getTOXml("talukStatus", talukStatus));
        strB.append(getTOXml("sateStatusBy", sateStatusBy));
        strB.append(getTOXml("disStatusBy", disStatusBy));
        strB.append(getTOXml("stateCreatedBy", stateCreatedBy));
        strB.append(getTOXml("talCreatedBy", talCreatedBy));
        strB.append(getTOXml("disCreatedBy", disCreatedBy));
        strB.append(getTOXml("talStatusBy", talStatusBy));
        strB.append(getTOXml("disAuthorizeStatus", disAuthorizeStatus));
        strB.append(getTOXml("disAuthorizeBy", disAuthorizeBy));
        strB.append(getTOXml("disAuthorizeDt", disAuthorizeDt));
        strB.append(getTOXml("talAuthorizeStatus", talAuthorizeStatus));
        strB.append(getTOXml("talAuthorizeBy", talAuthorizeBy));
        strB.append(getTOXml("talAuthorizeDt", talAuthorizeDt));
        strB.append(getTOXml("stateStatusDt", stateStatusDt));
        strB.append(getTOXml("verification", verification));
        strB.append(getTOXml("branCode", branCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property talukName.
     *
     * @return Value of property talukName.
     */
    public java.lang.String getTalukName() {
        return talukName;
    }

    /**
     * Setter for property talukName.
     *
     * @param talukName New value of property talukName.
     */
    public void setTalukName(java.lang.String talukName) {
        this.talukName = talukName;
    }

    /**
     * Getter for property talukCode.
     *
     * @return Value of property talukCode.
     */
    public java.lang.String getTalukCode() {
        return talukCode;
    }

    /**
     * Setter for property talukCode.
     *
     * @param talukCode New value of property talukCode.
     */
    public void setTalukCode(java.lang.String talukCode) {
        this.talukCode = talukCode;
    }

    /**
     * Getter for property disCode.
     *
     * @return Value of property disCode.
     */
    public java.lang.String getDisCode() {
        return disCode;
    }

    /**
     * Setter for property disCode.
     *
     * @param disCode New value of property disCode.
     */
    public void setDisCode(java.lang.String disCode) {
        this.disCode = disCode;
    }

    /**
     * Getter for property disName.
     *
     * @return Value of property disName.
     */
    public java.lang.String getDisName() {
        return disName;
    }

    /**
     * Setter for property disName.
     *
     * @param disName New value of property disName.
     */
    public void setDisName(java.lang.String disName) {
        this.disName = disName;
    }

    /**
     * Getter for property slno.
     *
     * @return Value of property slno.
     */
    public java.lang.String getSlno() {
        return slno;
    }

    /**
     * Setter for property slno.
     *
     * @param slno New value of property slno.
     */
    public void setSlno(java.lang.String slno) {
        this.slno = slno;
    }

    /**
     * Getter for property stateSlno.
     *
     * @return Value of property stateSlno.
     */
    public java.lang.String getStateSlno() {
        return stateSlno;
    }

    /**
     * Setter for property stateSlno.
     *
     * @param stateSlno New value of property stateSlno.
     */
    public void setStateSlno(java.lang.String stateSlno) {
        this.stateSlno = stateSlno;
    }

    /**
     * Getter for property disstatus.
     *
     * @return Value of property disstatus.
     */
    public java.lang.String getDisStatus() {
        return disStatus;
    }

    /**
     * Setter for property disstatus.
     *
     * @param disstatus New value of property disstatus.
     */
    public void setDisStatus(java.lang.String disStatus) {
        this.disStatus = disStatus;
    }

    /**
     * Getter for property talukstatus.
     *
     * @return Value of property talukstatus.
     */
    public java.lang.String getTalukStatus() {
        return talukStatus;
    }

    /**
     * Setter for property talukstatus.
     *
     * @param talukstatus New value of property talukstatus.
     */
    public void setTalukStatus(java.lang.String talukStatus) {
        this.talukStatus = talukStatus;
    }

    /**
     * Getter for property sateStatusBy.
     *
     * @return Value of property sateStatusBy.
     */
    public java.lang.String getSateStatusBy() {
        return sateStatusBy;
    }

    /**
     * Setter for property sateStatusBy.
     *
     * @param sateStatusBy New value of property sateStatusBy.
     */
    public void setSateStatusBy(java.lang.String sateStatusBy) {
        this.sateStatusBy = sateStatusBy;
    }

    /**
     * Getter for property talStatusBy.
     *
     * @return Value of property talStatusBy.
     */
    public java.lang.String getTalStatusBy() {
        return talStatusBy;
    }

    /**
     * Setter for property talStatusBy.
     *
     * @param talStatusBy New value of property talStatusBy.
     */
    public void setTalStatusBy(java.lang.String talStatusBy) {
        this.talStatusBy = talStatusBy;
    }

    /**
     * Getter for property disStatusBy.
     *
     * @return Value of property disStatusBy.
     */
    public java.lang.String getDisStatusBy() {
        return disStatusBy;
    }

    /**
     * Setter for property disStatusBy.
     *
     * @param disStatusBy New value of property disStatusBy.
     */
    public void setDisStatusBy(java.lang.String disStatusBy) {
        this.disStatusBy = disStatusBy;
    }

    /**
     * Getter for property stateCreatedBy.
     *
     * @return Value of property stateCreatedBy.
     */
    public java.lang.String getStateCreatedBy() {
        return stateCreatedBy;
    }

    /**
     * Setter for property stateCreatedBy.
     *
     * @param stateCreatedBy New value of property stateCreatedBy.
     */
    public void setStateCreatedBy(java.lang.String stateCreatedBy) {
        this.stateCreatedBy = stateCreatedBy;
    }

    /**
     * Getter for property talCreatedBy.
     *
     * @return Value of property talCreatedBy.
     */
    public java.lang.String getTalCreatedBy() {
        return talCreatedBy;
    }

    /**
     * Setter for property talCreatedBy.
     *
     * @param talCreatedBy New value of property talCreatedBy.
     */
    public void setTalCreatedBy(java.lang.String talCreatedBy) {
        this.talCreatedBy = talCreatedBy;
    }

    /**
     * Getter for property disCreatedBy.
     *
     * @return Value of property disCreatedBy.
     */
    public java.lang.String getDisCreatedBy() {
        return disCreatedBy;
    }

    /**
     * Setter for property disCreatedBy.
     *
     * @param disCreatedBy New value of property disCreatedBy.
     */
    public void setDisCreatedBy(java.lang.String disCreatedBy) {
        this.disCreatedBy = disCreatedBy;
    }

    /**
     * Getter for property disAuthorizeStatus.
     *
     * @return Value of property disAuthorizeStatus.
     */
    public java.lang.String getDisAuthorizeStatus() {
        return disAuthorizeStatus;
    }

    /**
     * Setter for property disAuthorizeStatus.
     *
     * @param disAuthorizeStatus New value of property disAuthorizeStatus.
     */
    public void setDisAuthorizeStatus(java.lang.String disAuthorizeStatus) {
        this.disAuthorizeStatus = disAuthorizeStatus;
    }

    /**
     * Getter for property disAuthorizeBy.
     *
     * @return Value of property disAuthorizeBy.
     */
    public java.lang.String getDisAuthorizeBy() {
        return disAuthorizeBy;
    }

    /**
     * Setter for property disAuthorizeBy.
     *
     * @param disAuthorizeBy New value of property disAuthorizeBy.
     */
    public void setDisAuthorizeBy(java.lang.String disAuthorizeBy) {
        this.disAuthorizeBy = disAuthorizeBy;
    }

    /**
     * Getter for property disAuthorizeDt.
     *
     * @return Value of property disAuthorizeDt.
     */
    public java.util.Date getDisAuthorizeDt() {
        return disAuthorizeDt;
    }

    /**
     * Setter for property disAuthorizeDt.
     *
     * @param disAuthorizeDt New value of property disAuthorizeDt.
     */
    public void setDisAuthorizeDt(java.util.Date disAuthorizeDt) {
        this.disAuthorizeDt = disAuthorizeDt;
    }

    /**
     * Getter for property talAuthorizeStatus.
     *
     * @return Value of property talAuthorizeStatus.
     */
    public java.lang.String getTalAuthorizeStatus() {
        return talAuthorizeStatus;
    }

    /**
     * Setter for property talAuthorizeStatus.
     *
     * @param talAuthorizeStatus New value of property talAuthorizeStatus.
     */
    public void setTalAuthorizeStatus(java.lang.String talAuthorizeStatus) {
        this.talAuthorizeStatus = talAuthorizeStatus;
    }

    /**
     * Getter for property talAuthorizeBy.
     *
     * @return Value of property talAuthorizeBy.
     */
    public java.lang.String getTalAuthorizeBy() {
        return talAuthorizeBy;
    }

    /**
     * Setter for property talAuthorizeBy.
     *
     * @param talAuthorizeBy New value of property talAuthorizeBy.
     */
    public void setTalAuthorizeBy(java.lang.String talAuthorizeBy) {
        this.talAuthorizeBy = talAuthorizeBy;
    }

    /**
     * Getter for property talAuthorizeDt.
     *
     * @return Value of property talAuthorizeDt.
     */
    public java.util.Date getTalAuthorizeDt() {
        return talAuthorizeDt;
    }

    /**
     * Setter for property talAuthorizeDt.
     *
     * @param talAuthorizeDt New value of property talAuthorizeDt.
     */
    public void setTalAuthorizeDt(java.util.Date talAuthorizeDt) {
        this.talAuthorizeDt = talAuthorizeDt;
    }

    /**
     * Getter for property stateStatusDt.
     *
     * @return Value of property stateStatusDt.
     */
    public java.util.Date getStateStatusDt() {
        return stateStatusDt;
    }

    /**
     * Setter for property stateStatusDt.
     *
     * @param stateStatusDt New value of property stateStatusDt.
     */
    public void setStateStatusDt(java.util.Date stateStatusDt) {
        this.stateStatusDt = stateStatusDt;
    }

    /**
     * Getter for property verification.
     *
     * @return Value of property verification.
     */
    public java.lang.String getVerification() {
        return verification;
    }

    /**
     * Setter for property verification.
     *
     * @param verification New value of property verification.
     */
    public void setVerification(java.lang.String verification) {
        this.verification = verification;
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
}