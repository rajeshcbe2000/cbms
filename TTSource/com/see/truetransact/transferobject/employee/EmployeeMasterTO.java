/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 *  CustomerTO.java
 *
 * Created on Wed Jul 27 16:13:11 GMT+05:30 2005
 */
package com.see.truetransact.transferobject.employee;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUSTOMER.
 */
public class EmployeeMasterTO extends TransferObject implements Serializable {

    private String sysId = "";
    private String txtEmpId = "";
    private String cboEmpTitle = "";
    private String txtEmpFirstName = "";
    private String txtEmpMiddleName = "";
    private String txtEmpLastName = "";
    private String txtEmpFatherFirstName = "";
    private String txtEmpFatherMIddleName = "";
    private String txtEmpFatherLasteName = "";
    private String txtEmpMotherFirstName = "";
    private String txtEmpMotherMIddleName = "";
    private String txtEmpMotherLasteName = "";
    private String cboEmpFatheTitle = "";
    private String cboEmpMotherTitle = "";
    private Date tdtEmpDateOfBirth = null;
    private String txtempAge = "";
    private String txtempPlaceOfBirth = "";
    private String cboEmpReligon = "";
    private String cboEmpCaste = "";
    private String txtEmpHomeTown = "";
    private String txtEmpIdCardNo = "";
    private String txtEmpUIdNo = "";
    private String txtEmpPanNo = "";
    private String txtEmpPfNo = "";
    private String cboEmpPfNominee = "";
    private Date tdtEmpJoinDate = null;
    private Date tdtEmpRetirementDate = null;
    private String rdoEmpGender = "";
    private String rdoMaritalStatus = "";
    private String rdoFatherHusband = "";
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String authorizeStatus = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String createdBy = "";
    private Date createdDt = null;
    private String cboBloodGroup = "";
    private String txtMajorHealthProbeem = "";
    private String txtPhysicalHandicap = "";
    private String txtDrivingLicenceNo = "";
    private Date tdtDLRenewalDate = null;
    private String txtEmailId = "";
    private String cboDomicileState = "";

    public String getKeyData() {
        setKeyColumns(sysId);
        return sysId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));

        strB.append(getTOString("sysId", sysId));
        strB.append(getTOString("txtEmpId", txtEmpId));
        strB.append(getTOString("cboEmpTitle", cboEmpTitle));
        strB.append(getTOString("txtEmpFirstName", txtEmpFirstName));
        strB.append(getTOString("txtEmpMiddleName", txtEmpMiddleName));
        strB.append(getTOString("txtEmpLastName", txtEmpLastName));
        strB.append(getTOString("txtEmpFatherFirstName", txtEmpFatherFirstName));
        strB.append(getTOString("txtEmpFatherMIddleName", txtEmpFatherMIddleName));
        strB.append(getTOString("txtEmpFatherLasteName", txtEmpFatherLasteName));
        strB.append(getTOString("txtEmpMotherFirstName", txtEmpMotherFirstName));
        strB.append(getTOString("txtEmpMotherMIddleName", txtEmpMotherMIddleName));
        strB.append(getTOString("txtEmpMotherLasteName", txtEmpMotherLasteName));
        strB.append(getTOString("cboEmpFatheTitle", cboEmpFatheTitle));
        strB.append(getTOString("cboEmpMotherTitle", cboEmpMotherTitle));
        strB.append(getTOString("tdtEmpDateOfBirth", tdtEmpDateOfBirth));
        strB.append(getTOString("txtempAge", txtempAge));
        strB.append(getTOString("txtempPlaceOfBirth", txtempPlaceOfBirth));
        strB.append(getTOString("cboEmpReligon", cboEmpReligon));
        strB.append(getTOString("cboEmpCaste", cboEmpCaste));
        strB.append(getTOString("txtEmpHomeTown", txtEmpHomeTown));
        strB.append(getTOString("txtEmpIdCardNo", txtEmpIdCardNo));
        strB.append(getTOString("txtEmpUIdNo", txtEmpUIdNo));
        strB.append(getTOString("txtEmpPanNo", txtEmpPanNo));
        strB.append(getTOString("txtEmpPfNo", txtEmpPfNo));
        strB.append(getTOString("cboEmpPfNominee", cboEmpPfNominee));
        strB.append(getTOString("tdtEmpJoinDate", tdtEmpJoinDate));
        strB.append(getTOString("tdtEmpRetirementDate", tdtEmpRetirementDate));
        strB.append(getTOString("rdoEmpGender", rdoEmpGender));
        strB.append(getTOString("rdoMaritalStatus", rdoMaritalStatus));
        strB.append(getTOString("rdoFatherHusband", rdoFatherHusband));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("cboBloodGroup", cboBloodGroup));
        strB.append(getTOString("txtMajorHealthProbeem", txtMajorHealthProbeem));
        strB.append(getTOString("txtPhysicalHandicap", txtPhysicalHandicap));
        strB.append(getTOString("txtDrivingLicenceNo", txtDrivingLicenceNo));
        strB.append(getTOString("tdtDLRenewalDate", tdtDLRenewalDate));
        strB.append(getTOString("txtEmailId", txtEmailId));
        strB.append(getTOString("cboDomicileState", cboDomicileState));


        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("sysId", sysId));
        strB.append(getTOXml("txtEmpId", txtEmpId));
        strB.append(getTOXml("cboEmpTitle", cboEmpTitle));
        strB.append(getTOXml("txtEmpFirstName", txtEmpFirstName));
        strB.append(getTOXml("txtEmpMiddleName", txtEmpMiddleName));
        strB.append(getTOXml("txtEmpLastName", txtEmpLastName));
        strB.append(getTOXml("txtEmpFatherFirstName", txtEmpFatherFirstName));
        strB.append(getTOXml("txtEmpFatherMIddleName", txtEmpFatherMIddleName));
        strB.append(getTOXml("txtEmpFatherLasteName", txtEmpFatherLasteName));
        strB.append(getTOXml("txtEmpMotherFirstName", txtEmpMotherFirstName));
        strB.append(getTOXml("txtEmpMotherMIddleName", txtEmpMotherMIddleName));
        strB.append(getTOXml("txtEmpMotherLasteName", txtEmpMotherLasteName));
        strB.append(getTOXml("cboEmpFatheTitle", cboEmpFatheTitle));
        strB.append(getTOXml("cboEmpMotherTitle", cboEmpMotherTitle));
        strB.append(getTOXml("tdtEmpDateOfBirth", tdtEmpDateOfBirth));
        strB.append(getTOXml("txtempAge", txtempAge));
        strB.append(getTOXml("txtempPlaceOfBirth", txtempPlaceOfBirth));
        strB.append(getTOXml("cboEmpReligon", cboEmpReligon));
        strB.append(getTOXml("cboEmpCaste", cboEmpCaste));
        strB.append(getTOXml("txtEmpHomeTown", txtEmpHomeTown));
        strB.append(getTOXml("txtEmpIdCardNo", txtEmpIdCardNo));
        strB.append(getTOXml("txtEmpUIdNo", txtEmpUIdNo));
        strB.append(getTOXml("txtEmpPanNo", txtEmpPanNo));
        strB.append(getTOXml("txtEmpPfNo", txtEmpPfNo));
        strB.append(getTOXml("cboEmpPfNominee", cboEmpPfNominee));
        strB.append(getTOXml("tdtEmpJoinDate", tdtEmpJoinDate));
        strB.append(getTOXml("tdtEmpRetirementDate", tdtEmpRetirementDate));
        strB.append(getTOXml("rdoEmpGender", rdoEmpGender));
        strB.append(getTOXml("rdoMaritalStatus", rdoMaritalStatus));
        strB.append(getTOXml("rdoFatherHusband", rdoFatherHusband));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("cboBloodGroup", cboBloodGroup));
        strB.append(getTOXml("txtMajorHealthProbeem", txtMajorHealthProbeem));
        strB.append(getTOXml("txtPhysicalHandicap", txtPhysicalHandicap));
        strB.append(getTOXml("txtDrivingLicenceNo", txtDrivingLicenceNo));
        strB.append(getTOXml("tdtDLRenewalDate", tdtDLRenewalDate));
        strB.append(getTOXml("txtEmailId", txtEmailId));
        strB.append(getTOXml("cboDomicileState", cboDomicileState));
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
     * Getter for property cboEmpTitle.
     *
     * @return Value of property cboEmpTitle.
     */
    public java.lang.String getCboEmpTitle() {
        return cboEmpTitle;
    }

    /**
     * Setter for property cboEmpTitle.
     *
     * @param cboEmpTitle New value of property cboEmpTitle.
     */
    public void setCboEmpTitle(java.lang.String cboEmpTitle) {
        this.cboEmpTitle = cboEmpTitle;
    }

    /**
     * Getter for property txtEmpFirstName.
     *
     * @return Value of property txtEmpFirstName.
     */
    public java.lang.String getTxtEmpFirstName() {
        return txtEmpFirstName;
    }

    /**
     * Setter for property txtEmpFirstName.
     *
     * @param txtEmpFirstName New value of property txtEmpFirstName.
     */
    public void setTxtEmpFirstName(java.lang.String txtEmpFirstName) {
        this.txtEmpFirstName = txtEmpFirstName;
    }

    /**
     * Getter for property txtEmpMiddleName.
     *
     * @return Value of property txtEmpMiddleName.
     */
    public java.lang.String getTxtEmpMiddleName() {
        return txtEmpMiddleName;
    }

    /**
     * Setter for property txtEmpMiddleName.
     *
     * @param txtEmpMiddleName New value of property txtEmpMiddleName.
     */
    public void setTxtEmpMiddleName(java.lang.String txtEmpMiddleName) {
        this.txtEmpMiddleName = txtEmpMiddleName;
    }

    /**
     * Getter for property txtEmpLastName.
     *
     * @return Value of property txtEmpLastName.
     */
    public java.lang.String getTxtEmpLastName() {
        return txtEmpLastName;
    }

    /**
     * Setter for property txtEmpLastName.
     *
     * @param txtEmpLastName New value of property txtEmpLastName.
     */
    public void setTxtEmpLastName(java.lang.String txtEmpLastName) {
        this.txtEmpLastName = txtEmpLastName;
    }

    /**
     * Getter for property txtEmpFatherFirstName.
     *
     * @return Value of property txtEmpFatherFirstName.
     */
    public java.lang.String getTxtEmpFatherFirstName() {
        return txtEmpFatherFirstName;
    }

    /**
     * Setter for property txtEmpFatherFirstName.
     *
     * @param txtEmpFatherFirstName New value of property txtEmpFatherFirstName.
     */
    public void setTxtEmpFatherFirstName(java.lang.String txtEmpFatherFirstName) {
        this.txtEmpFatherFirstName = txtEmpFatherFirstName;
    }

    /**
     * Getter for property txtEmpFatherMIddleName.
     *
     * @return Value of property txtEmpFatherMIddleName.
     */
    public java.lang.String getTxtEmpFatherMIddleName() {
        return txtEmpFatherMIddleName;
    }

    /**
     * Setter for property txtEmpFatherMIddleName.
     *
     * @param txtEmpFatherMIddleName New value of property
     * txtEmpFatherMIddleName.
     */
    public void setTxtEmpFatherMIddleName(java.lang.String txtEmpFatherMIddleName) {
        this.txtEmpFatherMIddleName = txtEmpFatherMIddleName;
    }

    /**
     * Getter for property txtEmpFatherLasteName.
     *
     * @return Value of property txtEmpFatherLasteName.
     */
    public java.lang.String getTxtEmpFatherLasteName() {
        return txtEmpFatherLasteName;
    }

    /**
     * Setter for property txtEmpFatherLasteName.
     *
     * @param txtEmpFatherLasteName New value of property txtEmpFatherLasteName.
     */
    public void setTxtEmpFatherLasteName(java.lang.String txtEmpFatherLasteName) {
        this.txtEmpFatherLasteName = txtEmpFatherLasteName;
    }

    /**
     * Getter for property txtEmpMotherFirstName.
     *
     * @return Value of property txtEmpMotherFirstName.
     */
    public java.lang.String getTxtEmpMotherFirstName() {
        return txtEmpMotherFirstName;
    }

    /**
     * Setter for property txtEmpMotherFirstName.
     *
     * @param txtEmpMotherFirstName New value of property txtEmpMotherFirstName.
     */
    public void setTxtEmpMotherFirstName(java.lang.String txtEmpMotherFirstName) {
        this.txtEmpMotherFirstName = txtEmpMotherFirstName;
    }

    /**
     * Getter for property txtEmpMotherMIddleName.
     *
     * @return Value of property txtEmpMotherMIddleName.
     */
    public java.lang.String getTxtEmpMotherMIddleName() {
        return txtEmpMotherMIddleName;
    }

    /**
     * Setter for property txtEmpMotherMIddleName.
     *
     * @param txtEmpMotherMIddleName New value of property
     * txtEmpMotherMIddleName.
     */
    public void setTxtEmpMotherMIddleName(java.lang.String txtEmpMotherMIddleName) {
        this.txtEmpMotherMIddleName = txtEmpMotherMIddleName;
    }

    /**
     * Getter for property txtEmpMotherLasteName.
     *
     * @return Value of property txtEmpMotherLasteName.
     */
    public java.lang.String getTxtEmpMotherLasteName() {
        return txtEmpMotherLasteName;
    }

    /**
     * Setter for property txtEmpMotherLasteName.
     *
     * @param txtEmpMotherLasteName New value of property txtEmpMotherLasteName.
     */
    public void setTxtEmpMotherLasteName(java.lang.String txtEmpMotherLasteName) {
        this.txtEmpMotherLasteName = txtEmpMotherLasteName;
    }

    /**
     * Getter for property cboEmpFatheTitle.
     *
     * @return Value of property cboEmpFatheTitle.
     */
    public java.lang.String getCboEmpFatheTitle() {
        return cboEmpFatheTitle;
    }

    /**
     * Setter for property cboEmpFatheTitle.
     *
     * @param cboEmpFatheTitle New value of property cboEmpFatheTitle.
     */
    public void setCboEmpFatheTitle(java.lang.String cboEmpFatheTitle) {
        this.cboEmpFatheTitle = cboEmpFatheTitle;
    }

    /**
     * Getter for property cboEmpMotherTitle.
     *
     * @return Value of property cboEmpMotherTitle.
     */
    public java.lang.String getCboEmpMotherTitle() {
        return cboEmpMotherTitle;
    }

    /**
     * Setter for property cboEmpMotherTitle.
     *
     * @param cboEmpMotherTitle New value of property cboEmpMotherTitle.
     */
    public void setCboEmpMotherTitle(java.lang.String cboEmpMotherTitle) {
        this.cboEmpMotherTitle = cboEmpMotherTitle;
    }

    /**
     * Getter for property txtempAge.
     *
     * @return Value of property txtempAge.
     */
    public java.lang.String getTxtempAge() {
        return txtempAge;
    }

    /**
     * Setter for property txtempAge.
     *
     * @param txtempAge New value of property txtempAge.
     */
    public void setTxtempAge(java.lang.String txtempAge) {
        this.txtempAge = txtempAge;
    }

    /**
     * Getter for property txtempPlaceOfBirth.
     *
     * @return Value of property txtempPlaceOfBirth.
     */
    public java.lang.String getTxtempPlaceOfBirth() {
        return txtempPlaceOfBirth;
    }

    /**
     * Setter for property txtempPlaceOfBirth.
     *
     * @param txtempPlaceOfBirth New value of property txtempPlaceOfBirth.
     */
    public void setTxtempPlaceOfBirth(java.lang.String txtempPlaceOfBirth) {
        this.txtempPlaceOfBirth = txtempPlaceOfBirth;
    }

    /**
     * Getter for property cboEmpReligon.
     *
     * @return Value of property cboEmpReligon.
     */
    public java.lang.String getCboEmpReligon() {
        return cboEmpReligon;
    }

    /**
     * Setter for property cboEmpReligon.
     *
     * @param cboEmpReligon New value of property cboEmpReligon.
     */
    public void setCboEmpReligon(java.lang.String cboEmpReligon) {
        this.cboEmpReligon = cboEmpReligon;
    }

    /**
     * Getter for property cboEmpCaste.
     *
     * @return Value of property cboEmpCaste.
     */
    public java.lang.String getCboEmpCaste() {
        return cboEmpCaste;
    }

    /**
     * Setter for property cboEmpCaste.
     *
     * @param cboEmpCaste New value of property cboEmpCaste.
     */
    public void setCboEmpCaste(java.lang.String cboEmpCaste) {
        this.cboEmpCaste = cboEmpCaste;
    }

    /**
     * Getter for property txtEmpHomeTown.
     *
     * @return Value of property txtEmpHomeTown.
     */
    public java.lang.String getTxtEmpHomeTown() {
        return txtEmpHomeTown;
    }

    /**
     * Setter for property txtEmpHomeTown.
     *
     * @param txtEmpHomeTown New value of property txtEmpHomeTown.
     */
    public void setTxtEmpHomeTown(java.lang.String txtEmpHomeTown) {
        this.txtEmpHomeTown = txtEmpHomeTown;
    }

    /**
     * Getter for property txtEmpIdCardNo.
     *
     * @return Value of property txtEmpIdCardNo.
     */
    public java.lang.String getTxtEmpIdCardNo() {
        return txtEmpIdCardNo;
    }

    /**
     * Setter for property txtEmpIdCardNo.
     *
     * @param txtEmpIdCardNo New value of property txtEmpIdCardNo.
     */
    public void setTxtEmpIdCardNo(java.lang.String txtEmpIdCardNo) {
        this.txtEmpIdCardNo = txtEmpIdCardNo;
    }

    /**
     * Getter for property txtEmpUIdNo.
     *
     * @return Value of property txtEmpUIdNo.
     */
    public java.lang.String getTxtEmpUIdNo() {
        return txtEmpUIdNo;
    }

    /**
     * Setter for property txtEmpUIdNo.
     *
     * @param txtEmpUIdNo New value of property txtEmpUIdNo.
     */
    public void setTxtEmpUIdNo(java.lang.String txtEmpUIdNo) {
        this.txtEmpUIdNo = txtEmpUIdNo;
    }

    /**
     * Getter for property txtEmpPanNo.
     *
     * @return Value of property txtEmpPanNo.
     */
    public java.lang.String getTxtEmpPanNo() {
        return txtEmpPanNo;
    }

    /**
     * Setter for property txtEmpPanNo.
     *
     * @param txtEmpPanNo New value of property txtEmpPanNo.
     */
    public void setTxtEmpPanNo(java.lang.String txtEmpPanNo) {
        this.txtEmpPanNo = txtEmpPanNo;
    }

    /**
     * Getter for property txtEmpPfNo.
     *
     * @return Value of property txtEmpPfNo.
     */
    public java.lang.String getTxtEmpPfNo() {
        return txtEmpPfNo;
    }

    /**
     * Setter for property txtEmpPfNo.
     *
     * @param txtEmpPfNo New value of property txtEmpPfNo.
     */
    public void setTxtEmpPfNo(java.lang.String txtEmpPfNo) {
        this.txtEmpPfNo = txtEmpPfNo;
    }

    /**
     * Getter for property cboEmpPfNominee.
     *
     * @return Value of property cboEmpPfNominee.
     */
    public java.lang.String getCboEmpPfNominee() {
        return cboEmpPfNominee;
    }

    /**
     * Setter for property cboEmpPfNominee.
     *
     * @param cboEmpPfNominee New value of property cboEmpPfNominee.
     */
    public void setCboEmpPfNominee(java.lang.String cboEmpPfNominee) {
        this.cboEmpPfNominee = cboEmpPfNominee;
    }

    /**
     * Getter for property rdoEmpGender.
     *
     * @return Value of property rdoEmpGender.
     */
    public java.lang.String getRdoEmpGender() {
        return rdoEmpGender;
    }

    /**
     * Setter for property rdoEmpGender.
     *
     * @param rdoEmpGender New value of property rdoEmpGender.
     */
    public void setRdoEmpGender(java.lang.String rdoEmpGender) {
        this.rdoEmpGender = rdoEmpGender;
    }

    /**
     * Getter for property rdoMaritalStatus.
     *
     * @return Value of property rdoMaritalStatus.
     */
    public java.lang.String getRdoMaritalStatus() {
        return rdoMaritalStatus;
    }

    /**
     * Setter for property rdoMaritalStatus.
     *
     * @param rdoMaritalStatus New value of property rdoMaritalStatus.
     */
    public void setRdoMaritalStatus(java.lang.String rdoMaritalStatus) {
        this.rdoMaritalStatus = rdoMaritalStatus;
    }

    /**
     * Getter for property rdoFatherHusband.
     *
     * @return Value of property rdoFatherHusband.
     */
    public java.lang.String getRdoFatherHusband() {
        return rdoFatherHusband;
    }

    /**
     * Setter for property rdoFatherHusband.
     *
     * @param rdoFatherHusband New value of property rdoFatherHusband.
     */
    public void setRdoFatherHusband(java.lang.String rdoFatherHusband) {
        this.rdoFatherHusband = rdoFatherHusband;
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
     * Getter for property sysId.
     *
     * @return Value of property sysId.
     */
    public java.lang.String getSysId() {
        return sysId;
    }

    /**
     * Setter for property sysId.
     *
     * @param sysId New value of property sysId.
     */
    public void setSysId(java.lang.String sysId) {
        this.sysId = sysId;
    }

    /**
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property createdDt.
     *
     * @param createdDt New value of property createdDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
    }

    /**
     * Getter for property tdtEmpDateOfBirth.
     *
     * @return Value of property tdtEmpDateOfBirth.
     */
    public java.util.Date getTdtEmpDateOfBirth() {
        return tdtEmpDateOfBirth;
    }

    /**
     * Setter for property tdtEmpDateOfBirth.
     *
     * @param tdtEmpDateOfBirth New value of property tdtEmpDateOfBirth.
     */
    public void setTdtEmpDateOfBirth(java.util.Date tdtEmpDateOfBirth) {
        this.tdtEmpDateOfBirth = tdtEmpDateOfBirth;
    }

    /**
     * Getter for property tdtEmpJoinDate.
     *
     * @return Value of property tdtEmpJoinDate.
     */
    public java.util.Date getTdtEmpJoinDate() {
        return tdtEmpJoinDate;
    }

    /**
     * Setter for property tdtEmpJoinDate.
     *
     * @param tdtEmpJoinDate New value of property tdtEmpJoinDate.
     */
    public void setTdtEmpJoinDate(java.util.Date tdtEmpJoinDate) {
        this.tdtEmpJoinDate = tdtEmpJoinDate;
    }

    /**
     * Getter for property tdtEmpRetirementDate.
     *
     * @return Value of property tdtEmpRetirementDate.
     */
    public java.util.Date getTdtEmpRetirementDate() {
        return tdtEmpRetirementDate;
    }

    /**
     * Setter for property tdtEmpRetirementDate.
     *
     * @param tdtEmpRetirementDate New value of property tdtEmpRetirementDate.
     */
    public void setTdtEmpRetirementDate(java.util.Date tdtEmpRetirementDate) {
        this.tdtEmpRetirementDate = tdtEmpRetirementDate;
    }

    /**
     * Getter for property cboBloodGroup.
     *
     * @return Value of property cboBloodGroup.
     */
    public java.lang.String getCboBloodGroup() {
        return cboBloodGroup;
    }

    /**
     * Setter for property cboBloodGroup.
     *
     * @param cboBloodGroup New value of property cboBloodGroup.
     */
    public void setCboBloodGroup(java.lang.String cboBloodGroup) {
        this.cboBloodGroup = cboBloodGroup;
    }

    /**
     * Getter for property txtMajorHealthProbeem.
     *
     * @return Value of property txtMajorHealthProbeem.
     */
    public java.lang.String getTxtMajorHealthProbeem() {
        return txtMajorHealthProbeem;
    }

    /**
     * Setter for property txtMajorHealthProbeem.
     *
     * @param txtMajorHealthProbeem New value of property txtMajorHealthProbeem.
     */
    public void setTxtMajorHealthProbeem(java.lang.String txtMajorHealthProbeem) {
        this.txtMajorHealthProbeem = txtMajorHealthProbeem;
    }

    /**
     * Getter for property txtDrivingLicenceNo.
     *
     * @return Value of property txtDrivingLicenceNo.
     */
    public java.lang.String getTxtDrivingLicenceNo() {
        return txtDrivingLicenceNo;
    }

    /**
     * Setter for property txtDrivingLicenceNo.
     *
     * @param txtDrivingLicenceNo New value of property txtDrivingLicenceNo.
     */
    public void setTxtDrivingLicenceNo(java.lang.String txtDrivingLicenceNo) {
        this.txtDrivingLicenceNo = txtDrivingLicenceNo;
    }

    /**
     * Getter for property txtEmailId.
     *
     * @return Value of property txtEmailId.
     */
    public java.lang.String getTxtEmailId() {
        return txtEmailId;
    }

    /**
     * Setter for property txtEmailId.
     *
     * @param txtEmailId New value of property txtEmailId.
     */
    public void setTxtEmailId(java.lang.String txtEmailId) {
        this.txtEmailId = txtEmailId;
    }

    /**
     * Getter for property cboDomicileState.
     *
     * @return Value of property cboDomicileState.
     */
    public java.lang.String getCboDomicileState() {
        return cboDomicileState;
    }

    /**
     * Setter for property cboDomicileState.
     *
     * @param cboDomicileState New value of property cboDomicileState.
     */
    public void setCboDomicileState(java.lang.String cboDomicileState) {
        this.cboDomicileState = cboDomicileState;
    }

    /**
     * Getter for property tdtDLRenewalDate.
     *
     * @return Value of property tdtDLRenewalDate.
     */
    public java.util.Date getTdtDLRenewalDate() {
        return tdtDLRenewalDate;
    }

    /**
     * Setter for property tdtDLRenewalDate.
     *
     * @param tdtDLRenewalDate New value of property tdtDLRenewalDate.
     */
    public void setTdtDLRenewalDate(java.util.Date tdtDLRenewalDate) {
        this.tdtDLRenewalDate = tdtDLRenewalDate;
    }

    /**
     * Getter for property txtPhysicalHandicap.
     *
     * @return Value of property txtPhysicalHandicap.
     */
    public java.lang.String getTxtPhysicalHandicap() {
        return txtPhysicalHandicap;
    }

    /**
     * Setter for property txtPhysicalHandicap.
     *
     * @param txtPhysicalHandicap New value of property txtPhysicalHandicap.
     */
    public void setTxtPhysicalHandicap(java.lang.String txtPhysicalHandicap) {
        this.txtPhysicalHandicap = txtPhysicalHandicap;
    }
}