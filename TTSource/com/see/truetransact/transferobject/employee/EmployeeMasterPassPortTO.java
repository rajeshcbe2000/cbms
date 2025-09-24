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
package com.see.truetransact.transferobject.employee;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUST_PHONE.
 */
public class EmployeeMasterPassPortTO extends TransferObject implements Serializable {

    private String passfname = "";
    private String passmname = "";
    private String passlname = "";
    private String passTitle = "";
    private String passNumber = "";
    private Date issueDt = null;
    private Date validUpto = null;
    private String issueAuth = "";
    private String issuePlace = "";
    private String txtEmpId = "";
    private String status = "";

    public String getKeyData() {
        setKeyColumns(txtEmpId);
        return txtEmpId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("passfname", passfname));
        strB.append(getTOString("passmname", passmname));
        strB.append(getTOString("passlname", passlname));
        strB.append(getTOString("passTitle", passTitle));
        strB.append(getTOString("passNumber", passNumber));
        strB.append(getTOString("issueDt", issueDt));
        strB.append(getTOString("validUpto", validUpto));
        strB.append(getTOString("issueAuth", issueAuth));
        strB.append(getTOString("issuePlace", issuePlace));
        strB.append(getTOString("status", status));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("passfname", passfname));
        strB.append(getTOXml("passmname", passmname));
        strB.append(getTOXml("passlname", passlname));
        strB.append(getTOXml("passTitle", passTitle));
        strB.append(getTOXml("passNumber", passNumber));
        strB.append(getTOXml("issueDt", issueDt));
        strB.append(getTOXml("validUpto", validUpto));
        strB.append(getTOXml("issueAuth", issueAuth));
        strB.append(getTOXml("issuePlace", issuePlace));
        strB.append(getTOXml("status", status));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public java.lang.String getPassfname() {
        return passfname;
    }

    /**
     * Setter for property passfname.
     *
     * @param passfname New value of property passfname.
     */
    public void setPassfname(java.lang.String passfname) {
        this.passfname = passfname;
    }

    /**
     * Getter for property passmname.
     *
     * @return Value of property passmname.
     */
    public java.lang.String getPassmname() {
        return passmname;
    }

    /**
     * Setter for property passmname.
     *
     * @param passmname New value of property passmname.
     */
    public void setPassmname(java.lang.String passmname) {
        this.passmname = passmname;
    }

    /**
     * Getter for property passlname.
     *
     * @return Value of property passlname.
     */
    public java.lang.String getPasslname() {
        return passlname;
    }

    /**
     * Setter for property passlname.
     *
     * @param passlname New value of property passlname.
     */
    public void setPasslname(java.lang.String passlname) {
        this.passlname = passlname;
    }

    /**
     * Getter for property passTitle.
     *
     * @return Value of property passTitle.
     */
    public java.lang.String getPassTitle() {
        return passTitle;
    }

    /**
     * Setter for property passTitle.
     *
     * @param passTitle New value of property passTitle.
     */
    public void setPassTitle(java.lang.String passTitle) {
        this.passTitle = passTitle;
    }

    /**
     * Getter for property passNumber.
     *
     * @return Value of property passNumber.
     */
    public java.lang.String getPassNumber() {
        return passNumber;
    }

    /**
     * Setter for property passNumber.
     *
     * @param passNumber New value of property passNumber.
     */
    public void setPassNumber(java.lang.String passNumber) {
        this.passNumber = passNumber;
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
     * Getter for property validUpto.
     *
     * @return Value of property validUpto.
     */
    public java.util.Date getValidUpto() {
        return validUpto;
    }

    /**
     * Setter for property validUpto.
     *
     * @param validUpto New value of property validUpto.
     */
    public void setValidUpto(java.util.Date validUpto) {
        this.validUpto = validUpto;
    }

    /**
     * Getter for property issueAuth.
     *
     * @return Value of property issueAuth.
     */
    public java.lang.String getIssueAuth() {
        return issueAuth;
    }

    /**
     * Setter for property issueAuth.
     *
     * @param issueAuth New value of property issueAuth.
     */
    public void setIssueAuth(java.lang.String issueAuth) {
        this.issueAuth = issueAuth;
    }

    /**
     * Getter for property issuePlace.
     *
     * @return Value of property issuePlace.
     */
    public java.lang.String getIssuePlace() {
        return issuePlace;
    }

    /**
     * Setter for property issuePlace.
     *
     * @param issuePlace New value of property issuePlace.
     */
    public void setIssuePlace(java.lang.String issuePlace) {
        this.issuePlace = issuePlace;
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
     * Getter for property txtEmpId.
     *
     * @return Value of property txtEmpId.
     */
    public java.lang.String getTxtEmpId() {
        return txtEmpId;
    }

    /**
     * Setter for property txtEmpId.
     *
     * @param txtEmpId New value of property txtEmpId.
     */
    public void setTxtEmpId(java.lang.String txtEmpId) {
        this.txtEmpId = txtEmpId;
    }
}