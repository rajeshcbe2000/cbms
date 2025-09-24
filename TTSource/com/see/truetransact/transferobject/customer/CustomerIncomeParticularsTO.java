/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerPassportTO.java
 * 
 * Created on Wed Feb 16 09:38:12 IST 2005 swaroop
 */
package com.see.truetransact.transferobject.customer;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUST_PHONE.
 */
public class CustomerIncomeParticularsTO extends TransferObject implements Serializable {

    private String incName = "";
    private String incRelation = "";
    private String incAmount = "";
    private String incPackage = "";
    private String custId = "";
    private String status = "";
    private String slno = "";
    private String branchCode = "";
    private String incDetProfession = "";
    private String incDetCompany = "";

    public String getKeyData() {
        setKeyColumns(custId);
        return custId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("incName", incName));
        strB.append(getTOString("incRelation", incRelation));
        strB.append(getTOString("incAmount", incAmount));
        strB.append(getTOString("incPackage", incPackage));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("slno", slno));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("incDetProfession", incDetProfession));
        strB.append(getTOString("incDetCompany", incDetCompany));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("incName", incName));
        strB.append(getTOXml("incRelation", incRelation));
        strB.append(getTOXml("incAmount", incAmount));
        strB.append(getTOXml("incPackage", incPackage));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("incDetProfession", incDetProfession));
        strB.append(getTOXml("incDetCompany", incDetCompany));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property custId.
     *
     * @return Value of property custId.
     */
    public java.lang.String getCustId() {
        return custId;
    }

    /**
     * Setter for property custId.
     *
     * @param custId New value of property custId.
     */
    public void setCustId(java.lang.String custId) {
        this.custId = custId;
    }

    /**
     * Getter for property incName.
     *
     * @return Value of property incName.
     */
    public java.lang.String getIncName() {
        return incName;
    }

    /**
     * Setter for property incName.
     *
     * @param incName New value of property incName.
     */
    public void setIncName(java.lang.String incName) {
        this.incName = incName;
    }

    /**
     * Getter for property incRelation.
     *
     * @return Value of property incRelation.
     */
    public java.lang.String getIncRelation() {
        return incRelation;
    }

    /**
     * Setter for property incRelation.
     *
     * @param incRelation New value of property incRelation.
     */
    public void setIncRelation(java.lang.String incRelation) {
        this.incRelation = incRelation;
    }

    /**
     * Getter for property incAmount.
     *
     * @return Value of property incAmount.
     */
    public java.lang.String getIncAmount() {
        return incAmount;
    }

    /**
     * Setter for property incAmount.
     *
     * @param incAmount New value of property incAmount.
     */
    public void setIncAmount(java.lang.String incAmount) {
        this.incAmount = incAmount;
    }

    /**
     * Getter for property incPackage.
     *
     * @return Value of property incPackage.
     */
    public java.lang.String getIncPackage() {
        return incPackage;
    }

    /**
     * Setter for property incPackage.
     *
     * @param incPackage New value of property incPackage.
     */
    public void setIncPackage(java.lang.String incPackage) {
        this.incPackage = incPackage;
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
     * Getter for property incDetProfession.
     *
     * @return Value of property incDetProfession.
     */
    public java.lang.String getIncDetProfession() {
        return incDetProfession;
    }

    /**
     * Setter for property incDetProfession.
     *
     * @param incDetProfession New value of property incDetProfession.
     */
    public void setIncDetProfession(java.lang.String incDetProfession) {
        this.incDetProfession = incDetProfession;
    }

    /**
     * Getter for property incDetCompany.
     *
     * @return Value of property incDetCompany.
     */
    public java.lang.String getIncDetCompany() {
        return incDetCompany;
    }

    /**
     * Setter for property incDetCompany.
     *
     * @param incDetCompany New value of property incDetCompany.
     */
    public void setIncDetCompany(java.lang.String incDetCompany) {
        this.incDetCompany = incDetCompany;
    }
}