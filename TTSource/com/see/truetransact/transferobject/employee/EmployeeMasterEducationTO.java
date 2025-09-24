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
public class EmployeeMasterEducationTO extends TransferObject implements Serializable {

    private String txtEmpId = "";
    private String cboEmpAcademicLevel = "";
    private String txtEmpAcademicSchool = "";
    private Date tdtAcademicYearOfPassing = null;
    private String cboAcademicSpecialization = "";
    private String txtAcademicUniverSity = "";
    private String txtAcademicMarksScored = "";
    private String txtAcademicTotalMarks = "";
    private String txtAcademicPer = "";
    private String cboAcademicGrade = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String academicID = "";

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
        strB.append(getTOString("cboEmpAcademicLevel", cboEmpAcademicLevel));
        strB.append(getTOString("txtEmpAcademicSchool", txtEmpAcademicSchool));
        strB.append(getTOString("tdtAcademicYearOfPassing", tdtAcademicYearOfPassing));
        strB.append(getTOString("cboAcademicSpecialization", cboAcademicSpecialization));
        strB.append(getTOString("txtAcademicUniverSity", txtAcademicUniverSity));
        strB.append(getTOString("txtAcademicMarksScored", txtAcademicMarksScored));
        strB.append(getTOString("txtAcademicTotalMarks", txtAcademicTotalMarks));
        strB.append(getTOString("txtAcademicPer", txtAcademicPer));
        strB.append(getTOString("cboAcademicGrade", cboAcademicGrade));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("academicID", academicID));

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
        strB.append(getTOXml("cboEmpAcademicLevel", cboEmpAcademicLevel));
        strB.append(getTOXml("txtEmpAcademicSchool", txtEmpAcademicSchool));
        strB.append(getTOXml("tdtAcademicYearOfPassing", tdtAcademicYearOfPassing));
        strB.append(getTOXml("cboAcademicSpecialization", cboAcademicSpecialization));
        strB.append(getTOXml("txtAcademicUniverSity", txtAcademicUniverSity));
        strB.append(getTOXml("txtAcademicMarksScored", txtAcademicMarksScored));
        strB.append(getTOXml("txtAcademicTotalMarks", txtAcademicTotalMarks));
        strB.append(getTOXml("txtAcademicPer", txtAcademicPer));
        strB.append(getTOXml("cboAcademicGrade", cboAcademicGrade));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("academicID", academicID));

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
     * Getter for property cboEmpAcademicLevel.
     *
     * @return Value of property cboEmpAcademicLevel.
     */
    public java.lang.String getCboEmpAcademicLevel() {
        return cboEmpAcademicLevel;
    }

    /**
     * Setter for property cboEmpAcademicLevel.
     *
     * @param cboEmpAcademicLevel New value of property cboEmpAcademicLevel.
     */
    public void setCboEmpAcademicLevel(java.lang.String cboEmpAcademicLevel) {
        this.cboEmpAcademicLevel = cboEmpAcademicLevel;
    }

    /**
     * Getter for property txtEmpAcademicSchool.
     *
     * @return Value of property txtEmpAcademicSchool.
     */
    public java.lang.String getTxtEmpAcademicSchool() {
        return txtEmpAcademicSchool;
    }

    /**
     * Setter for property txtEmpAcademicSchool.
     *
     * @param txtEmpAcademicSchool New value of property txtEmpAcademicSchool.
     */
    public void setTxtEmpAcademicSchool(java.lang.String txtEmpAcademicSchool) {
        this.txtEmpAcademicSchool = txtEmpAcademicSchool;
    }

    /**
     * Getter for property cboAcademicSpecialization.
     *
     * @return Value of property cboAcademicSpecialization.
     */
    public java.lang.String getCboAcademicSpecialization() {
        return cboAcademicSpecialization;
    }

    /**
     * Setter for property cboAcademicSpecialization.
     *
     * @param cboAcademicSpecialization New value of property
     * cboAcademicSpecialization.
     */
    public void setCboAcademicSpecialization(java.lang.String cboAcademicSpecialization) {
        this.cboAcademicSpecialization = cboAcademicSpecialization;
    }

    /**
     * Getter for property txtAcademicUniverSity.
     *
     * @return Value of property txtAcademicUniverSity.
     */
    public java.lang.String getTxtAcademicUniverSity() {
        return txtAcademicUniverSity;
    }

    /**
     * Setter for property txtAcademicUniverSity.
     *
     * @param txtAcademicUniverSity New value of property txtAcademicUniverSity.
     */
    public void setTxtAcademicUniverSity(java.lang.String txtAcademicUniverSity) {
        this.txtAcademicUniverSity = txtAcademicUniverSity;
    }

    /**
     * Getter for property txtAcademicMarksScored.
     *
     * @return Value of property txtAcademicMarksScored.
     */
    public java.lang.String getTxtAcademicMarksScored() {
        return txtAcademicMarksScored;
    }

    /**
     * Setter for property txtAcademicMarksScored.
     *
     * @param txtAcademicMarksScored New value of property
     * txtAcademicMarksScored.
     */
    public void setTxtAcademicMarksScored(java.lang.String txtAcademicMarksScored) {
        this.txtAcademicMarksScored = txtAcademicMarksScored;
    }

    /**
     * Getter for property txtAcademicTotalMarks.
     *
     * @return Value of property txtAcademicTotalMarks.
     */
    public java.lang.String getTxtAcademicTotalMarks() {
        return txtAcademicTotalMarks;
    }

    /**
     * Setter for property txtAcademicTotalMarks.
     *
     * @param txtAcademicTotalMarks New value of property txtAcademicTotalMarks.
     */
    public void setTxtAcademicTotalMarks(java.lang.String txtAcademicTotalMarks) {
        this.txtAcademicTotalMarks = txtAcademicTotalMarks;
    }

    /**
     * Getter for property txtAcademicPer.
     *
     * @return Value of property txtAcademicPer.
     */
    public java.lang.String getTxtAcademicPer() {
        return txtAcademicPer;
    }

    /**
     * Setter for property txtAcademicPer.
     *
     * @param txtAcademicPer New value of property txtAcademicPer.
     */
    public void setTxtAcademicPer(java.lang.String txtAcademicPer) {
        this.txtAcademicPer = txtAcademicPer;
    }

    /**
     * Getter for property cboAcademicGrade.
     *
     * @return Value of property cboAcademicGrade.
     */
    public java.lang.String getCboAcademicGrade() {
        return cboAcademicGrade;
    }

    /**
     * Setter for property cboAcademicGrade.
     *
     * @param cboAcademicGrade New value of property cboAcademicGrade.
     */
    public void setCboAcademicGrade(java.lang.String cboAcademicGrade) {
        this.cboAcademicGrade = cboAcademicGrade;
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
     * Getter for property tdtAcademicYearOfPassing.
     *
     * @return Value of property tdtAcademicYearOfPassing.
     */
    public java.util.Date getTdtAcademicYearOfPassing() {
        return tdtAcademicYearOfPassing;
    }

    /**
     * Setter for property tdtAcademicYearOfPassing.
     *
     * @param tdtAcademicYearOfPassing New value of property
     * tdtAcademicYearOfPassing.
     */
    public void setTdtAcademicYearOfPassing(java.util.Date tdtAcademicYearOfPassing) {
        this.tdtAcademicYearOfPassing = tdtAcademicYearOfPassing;
    }

    /**
     * Getter for property academicID.
     *
     * @return Value of property academicID.
     */
    public java.lang.String getAcademicID() {
        return academicID;
    }

    /**
     * Setter for property academicID.
     *
     * @param academicID New value of property academicID.
     */
    public void setAcademicID(java.lang.String academicID) {
        this.academicID = academicID;
    }
    /**
     * Getter for property cboEmpTitle.
     *
     * @return Value of property cboEmpTitle.
     */
}