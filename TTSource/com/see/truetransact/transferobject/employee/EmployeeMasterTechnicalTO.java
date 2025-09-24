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
public class EmployeeMasterTechnicalTO extends TransferObject implements Serializable {

    private String txtEmpId = "";
    private String cboTechnicalLevel = "";
    private String txtTechnicalSchool = "";
    private Date tdtTechnicalYearOfPassing = null;
    private String cboTechnicalSpecialization = "";
    private String txtTechnicalUniverSity = "";
    private String txtTechnicalMarksScored = "";
    private String txtTechnicalTotalMarks = "";
    private String txtTechnicalPer = "";
    private String cboTechnicalGrade = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String technicalID = "";

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
        strB.append(getTOString("cboTechnicalLevel", cboTechnicalLevel));
        strB.append(getTOString("txtTechnicalSchool", txtTechnicalSchool));
        strB.append(getTOString("tdtTechnicalYearOfPassing", tdtTechnicalYearOfPassing));
        strB.append(getTOString("cboTechnicalSpecialization", cboTechnicalSpecialization));
        strB.append(getTOString("txtTechnicalUniverSity", txtTechnicalUniverSity));
        strB.append(getTOString("txtTechnicalMarksScored", txtTechnicalMarksScored));
        strB.append(getTOString("txtTechnicalTotalMarks", txtTechnicalTotalMarks));
        strB.append(getTOString("txtTechnicalPer", txtTechnicalPer));
        strB.append(getTOString("cboTechnicalGrade", cboTechnicalGrade));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("technicalID", technicalID));
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
        strB.append(getTOXml("cboTechnicalLevel", cboTechnicalLevel));
        strB.append(getTOXml("txtTechnicalSchool", txtTechnicalSchool));
        strB.append(getTOXml("tdtTechnicalYearOfPassing", tdtTechnicalYearOfPassing));
        strB.append(getTOXml("cboTechnicalSpecialization", cboTechnicalSpecialization));
        strB.append(getTOXml("txtTechnicalUniverSity", txtTechnicalUniverSity));
        strB.append(getTOXml("txtTechnicalMarksScored", txtTechnicalMarksScored));
        strB.append(getTOXml("txtTechnicalTotalMarks", txtTechnicalTotalMarks));
        strB.append(getTOXml("txtTechnicalPer", txtTechnicalPer));
        strB.append(getTOXml("cboTechnicalGrade", cboTechnicalGrade));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("technicalID", technicalID));
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
     * Getter for property cboTechnicalLevel.
     *
     * @return Value of property cboTechnicalLevel.
     */
    public java.lang.String getCboTechnicalLevel() {
        return cboTechnicalLevel;
    }

    /**
     * Setter for property cboTechnicalLevel.
     *
     * @param cboTechnicalLevel New value of property cboTechnicalLevel.
     */
    public void setCboTechnicalLevel(java.lang.String cboTechnicalLevel) {
        this.cboTechnicalLevel = cboTechnicalLevel;
    }

    /**
     * Getter for property txtTechnicalSchool.
     *
     * @return Value of property txtTechnicalSchool.
     */
    public java.lang.String getTxtTechnicalSchool() {
        return txtTechnicalSchool;
    }

    /**
     * Setter for property txtTechnicalSchool.
     *
     * @param txtTechnicalSchool New value of property txtTechnicalSchool.
     */
    public void setTxtTechnicalSchool(java.lang.String txtTechnicalSchool) {
        this.txtTechnicalSchool = txtTechnicalSchool;
    }

    /**
     * Getter for property cboTechnicalSpecialization.
     *
     * @return Value of property cboTechnicalSpecialization.
     */
    public java.lang.String getCboTechnicalSpecialization() {
        return cboTechnicalSpecialization;
    }

    /**
     * Setter for property cboTechnicalSpecialization.
     *
     * @param cboTechnicalSpecialization New value of property
     * cboTechnicalSpecialization.
     */
    public void setCboTechnicalSpecialization(java.lang.String cboTechnicalSpecialization) {
        this.cboTechnicalSpecialization = cboTechnicalSpecialization;
    }

    /**
     * Getter for property txtTechnicalUniverSity.
     *
     * @return Value of property txtTechnicalUniverSity.
     */
    public java.lang.String getTxtTechnicalUniverSity() {
        return txtTechnicalUniverSity;
    }

    /**
     * Setter for property txtTechnicalUniverSity.
     *
     * @param txtTechnicalUniverSity New value of property
     * txtTechnicalUniverSity.
     */
    public void setTxtTechnicalUniverSity(java.lang.String txtTechnicalUniverSity) {
        this.txtTechnicalUniverSity = txtTechnicalUniverSity;
    }

    /**
     * Getter for property txtTechnicalMarksScored.
     *
     * @return Value of property txtTechnicalMarksScored.
     */
    public java.lang.String getTxtTechnicalMarksScored() {
        return txtTechnicalMarksScored;
    }

    /**
     * Setter for property txtTechnicalMarksScored.
     *
     * @param txtTechnicalMarksScored New value of property
     * txtTechnicalMarksScored.
     */
    public void setTxtTechnicalMarksScored(java.lang.String txtTechnicalMarksScored) {
        this.txtTechnicalMarksScored = txtTechnicalMarksScored;
    }

    /**
     * Getter for property txtTechnicalTotalMarks.
     *
     * @return Value of property txtTechnicalTotalMarks.
     */
    public java.lang.String getTxtTechnicalTotalMarks() {
        return txtTechnicalTotalMarks;
    }

    /**
     * Setter for property txtTechnicalTotalMarks.
     *
     * @param txtTechnicalTotalMarks New value of property
     * txtTechnicalTotalMarks.
     */
    public void setTxtTechnicalTotalMarks(java.lang.String txtTechnicalTotalMarks) {
        this.txtTechnicalTotalMarks = txtTechnicalTotalMarks;
    }

    /**
     * Getter for property txtTechnicalPer.
     *
     * @return Value of property txtTechnicalPer.
     */
    public java.lang.String getTxtTechnicalPer() {
        return txtTechnicalPer;
    }

    /**
     * Setter for property txtTechnicalPer.
     *
     * @param txtTechnicalPer New value of property txtTechnicalPer.
     */
    public void setTxtTechnicalPer(java.lang.String txtTechnicalPer) {
        this.txtTechnicalPer = txtTechnicalPer;
    }

    /**
     * Getter for property cboTechnicalGrade.
     *
     * @return Value of property cboTechnicalGrade.
     */
    public java.lang.String getCboTechnicalGrade() {
        return cboTechnicalGrade;
    }

    /**
     * Setter for property cboTechnicalGrade.
     *
     * @param cboTechnicalGrade New value of property cboTechnicalGrade.
     */
    public void setCboTechnicalGrade(java.lang.String cboTechnicalGrade) {
        this.cboTechnicalGrade = cboTechnicalGrade;
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
     * Getter for property tdtTechnicalYearOfPassing.
     *
     * @return Value of property tdtTechnicalYearOfPassing.
     */
    public java.util.Date getTdtTechnicalYearOfPassing() {
        return tdtTechnicalYearOfPassing;
    }

    /**
     * Setter for property tdtTechnicalYearOfPassing.
     *
     * @param tdtTechnicalYearOfPassing New value of property
     * tdtTechnicalYearOfPassing.
     */
    public void setTdtTechnicalYearOfPassing(java.util.Date tdtTechnicalYearOfPassing) {
        this.tdtTechnicalYearOfPassing = tdtTechnicalYearOfPassing;
    }

    /**
     * Getter for property technicalID.
     *
     * @return Value of property technicalID.
     */
    public java.lang.String getTechnicalID() {
        return technicalID;
    }

    /**
     * Setter for property technicalID.
     *
     * @param technicalID New value of property technicalID.
     */
    public void setTechnicalID(java.lang.String technicalID) {
        this.technicalID = technicalID;
    }
}