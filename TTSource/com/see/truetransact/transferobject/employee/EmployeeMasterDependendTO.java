/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerAddressTO.java
 *
 * Created on Fri Feb 11 12:55:29 IST 2005
 */
package com.see.truetransact.transferobject.employee;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUST_ADDR.
 */
public class EmployeeMasterDependendTO extends TransferObject implements Serializable {

    private String txtEmpId = "";
    private String cboDepReleationShip = "";
    private String txtEmpDepFirstName = "";
    private String txtEmpDepMIddleName = "";
    private String txtEmpDepLasteName = "";
    private String cboEmpDepTitle = "";
    private Date tdtDepDateOfBirth = null;
    private String cboDepEducation = "";
    private String cboDepProfession = "";
    private String rdoDepYesNo = "";
    private String rdoLiableYesNo = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String dependentId = "";
    private String txtDepIncomePerannum = "";
    private String txtEmpWith = "";

    public String getKeyData() {
        setKeyColumns(txtEmpId);
        return txtEmpId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("txtEmpId", txtEmpId));
        strB.append(getTOString("cboDepReleationShip", cboDepReleationShip));
        strB.append(getTOString("txtEmpDepFirstName", txtEmpDepFirstName));
        strB.append(getTOString("txtEmpDepMIddleName", txtEmpDepMIddleName));
        strB.append(getTOString("txtEmpDepLasteName", txtEmpDepLasteName));
        strB.append(getTOString("cboEmpDepTitle", cboEmpDepTitle));
        strB.append(getTOString("tdtDepDateOfBirth", tdtDepDateOfBirth));
        strB.append(getTOString("cboDepEducation", cboDepEducation));
        strB.append(getTOString("cboDepProfession", cboDepProfession));
        strB.append(getTOString("rdoDepYesNo", rdoDepYesNo));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("dependentId", dependentId));
        strB.append(getTOString("txtDepIncomePerannum", txtDepIncomePerannum));
        strB.append(getTOString("txtEmpWith", txtEmpWith));
        strB.append(getTOString("rdoLiableYesNo", rdoLiableYesNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("txtEmpId", txtEmpId));
        strB.append(getTOXml("cboDepReleationShip", cboDepReleationShip));
        strB.append(getTOXml("txtEmpDepFirstName", txtEmpDepFirstName));
        strB.append(getTOXml("txtEmpDepMIddleName", txtEmpDepMIddleName));
        strB.append(getTOXml("txtEmpDepLasteName", txtEmpDepLasteName));
        strB.append(getTOXml("cboEmpDepTitle", cboEmpDepTitle));
        strB.append(getTOXml("tdtDepDateOfBirth", tdtDepDateOfBirth));
        strB.append(getTOXml("cboDepEducation", cboDepEducation));
        strB.append(getTOXml("cboDepProfession", cboDepProfession));
        strB.append(getTOXml("rdoDepYesNo", rdoDepYesNo));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("dependentId", dependentId));
        strB.append(getTOXml("txtDepIncomePerannum", txtDepIncomePerannum));
        strB.append(getTOXml("txtEmpWith", txtEmpWith));
        strB.append(getTOXml("rdoLiableYesNo", rdoLiableYesNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
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

    /**
     * Getter for property cboDepReleationShip.
     *
     * @return Value of property cboDepReleationShip.
     */
    public java.lang.String getCboDepReleationShip() {
        return cboDepReleationShip;
    }

    /**
     * Setter for property cboDepReleationShip.
     *
     * @param cboDepReleationShip New value of property cboDepReleationShip.
     */
    public void setCboDepReleationShip(java.lang.String cboDepReleationShip) {
        this.cboDepReleationShip = cboDepReleationShip;
    }

    /**
     * Getter for property txtEmpDepFirstName.
     *
     * @return Value of property txtEmpDepFirstName.
     */
    public java.lang.String getTxtEmpDepFirstName() {
        return txtEmpDepFirstName;
    }

    /**
     * Setter for property txtEmpDepFirstName.
     *
     * @param txtEmpDepFirstName New value of property txtEmpDepFirstName.
     */
    public void setTxtEmpDepFirstName(java.lang.String txtEmpDepFirstName) {
        this.txtEmpDepFirstName = txtEmpDepFirstName;
    }

    /**
     * Getter for property txtEmpDepMIddleName.
     *
     * @return Value of property txtEmpDepMIddleName.
     */
    public java.lang.String getTxtEmpDepMIddleName() {
        return txtEmpDepMIddleName;
    }

    /**
     * Setter for property txtEmpDepMIddleName.
     *
     * @param txtEmpDepMIddleName New value of property txtEmpDepMIddleName.
     */
    public void setTxtEmpDepMIddleName(java.lang.String txtEmpDepMIddleName) {
        this.txtEmpDepMIddleName = txtEmpDepMIddleName;
    }

    /**
     * Getter for property txtEmpDepLasteName.
     *
     * @return Value of property txtEmpDepLasteName.
     */
    public java.lang.String getTxtEmpDepLasteName() {
        return txtEmpDepLasteName;
    }

    /**
     * Setter for property txtEmpDepLasteName.
     *
     * @param txtEmpDepLasteName New value of property txtEmpDepLasteName.
     */
    public void setTxtEmpDepLasteName(java.lang.String txtEmpDepLasteName) {
        this.txtEmpDepLasteName = txtEmpDepLasteName;
    }

    /**
     * Getter for property cboEmpDepTitle.
     *
     * @return Value of property cboEmpDepTitle.
     */
    public java.lang.String getCboEmpDepTitle() {
        return cboEmpDepTitle;
    }

    /**
     * Setter for property cboEmpDepTitle.
     *
     * @param cboEmpDepTitle New value of property cboEmpDepTitle.
     */
    public void setCboEmpDepTitle(java.lang.String cboEmpDepTitle) {
        this.cboEmpDepTitle = cboEmpDepTitle;
    }

    /**
     * Getter for property cboDepEducation.
     *
     * @return Value of property cboDepEducation.
     */
    public java.lang.String getCboDepEducation() {
        return cboDepEducation;
    }

    /**
     * Setter for property cboDepEducation.
     *
     * @param cboDepEducation New value of property cboDepEducation.
     */
    public void setCboDepEducation(java.lang.String cboDepEducation) {
        this.cboDepEducation = cboDepEducation;
    }

    /**
     * Getter for property cboDepProfession.
     *
     * @return Value of property cboDepProfession.
     */
    public java.lang.String getCboDepProfession() {
        return cboDepProfession;
    }

    /**
     * Setter for property cboDepProfession.
     *
     * @param cboDepProfession New value of property cboDepProfession.
     */
    public void setCboDepProfession(java.lang.String cboDepProfession) {
        this.cboDepProfession = cboDepProfession;
    }

    /**
     * Getter for property rdoDepYesNo.
     *
     * @return Value of property rdoDepYesNo.
     */
    public java.lang.String getRdoDepYesNo() {
        return rdoDepYesNo;
    }

    /**
     * Setter for property rdoDepYesNo.
     *
     * @param rdoDepYesNo New value of property rdoDepYesNo.
     */
    public void setRdoDepYesNo(java.lang.String rdoDepYesNo) {
        this.rdoDepYesNo = rdoDepYesNo;
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
     * Getter for property tdtDepDateOfBirth.
     *
     * @return Value of property tdtDepDateOfBirth.
     */
    public java.util.Date getTdtDepDateOfBirth() {
        return tdtDepDateOfBirth;
    }

    /**
     * Setter for property tdtDepDateOfBirth.
     *
     * @param tdtDepDateOfBirth New value of property tdtDepDateOfBirth.
     */
    public void setTdtDepDateOfBirth(java.util.Date tdtDepDateOfBirth) {
        this.tdtDepDateOfBirth = tdtDepDateOfBirth;
    }

    /**
     * Getter for property dependentId.
     *
     * @return Value of property dependentId.
     */
    public java.lang.String getDependentId() {
        return dependentId;
    }

    /**
     * Setter for property dependentId.
     *
     * @param dependentId New value of property dependentId.
     */
    public void setDependentId(java.lang.String dependentId) {
        this.dependentId = dependentId;
    }

    /**
     * Getter for property txtDepIncomePerannum.
     *
     * @return Value of property txtDepIncomePerannum.
     */
    public java.lang.String getTxtDepIncomePerannum() {
        return txtDepIncomePerannum;
    }

    /**
     * Setter for property txtDepIncomePerannum.
     *
     * @param txtDepIncomePerannum New value of property txtDepIncomePerannum.
     */
    public void setTxtDepIncomePerannum(java.lang.String txtDepIncomePerannum) {
        this.txtDepIncomePerannum = txtDepIncomePerannum;
    }

    /**
     * Getter for property txtEmpWith.
     *
     * @return Value of property txtEmpWith.
     */
    public java.lang.String getTxtEmpWith() {
        return txtEmpWith;
    }

    /**
     * Setter for property txtEmpWith.
     *
     * @param txtEmpWith New value of property txtEmpWith.
     */
    public void setTxtEmpWith(java.lang.String txtEmpWith) {
        this.txtEmpWith = txtEmpWith;
    }

    /**
     * Getter for property rdoLiableYesNo.
     *
     * @return Value of property rdoLiableYesNo.
     */
    public java.lang.String getRdoLiableYesNo() {
        return rdoLiableYesNo;
    }

    /**
     * Setter for property rdoLiableYesNo.
     *
     * @param rdoLiableYesNo New value of property rdoLiableYesNo.
     */
    public void setRdoLiableYesNo(java.lang.String rdoLiableYesNo) {
        this.rdoLiableYesNo = rdoLiableYesNo;
    }
}