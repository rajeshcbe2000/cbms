/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeductionTO.java
 * 
 * Created on Wed Jun 09 18:50:50 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.common;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is Deduction.
 */
public class IncrementTO extends TransferObject implements Serializable {

    private Double slNo = null;
    private String empId = "";
    private String txtIncrementEmpName = "";
    private Date effectiveDate = null;
    private Date createdDate = null;
    private Date tdtIncrementDate = null;
    private String employeeStage = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private Double tempSlNO = null;
    private String txtIncrementAmount = "";
    private String txtNewBasic = "";
    private String txtIncrementDesignation = "";
    private String txtBasicSalary = "";
    private String txtIncrementNo = "";
    private String incrementID = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
//    public String getKeyData() {
//        setKeyColumns("lienNo");
//        return slNo;
//    }
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("txtIncrementEmpName", txtIncrementEmpName));
        strB.append(getTOString("empId", empId));
        strB.append(getTOString("effectiveDate", effectiveDate));
        strB.append(getTOString("createdDate", createdDate));
        strB.append(getTOString("employeeStage", employeeStage));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("tempSlNO", tempSlNO));
        strB.append(getTOString("tdtIncrementDate", tdtIncrementDate));
        strB.append(getTOString("txtIncrementAmount", txtIncrementAmount));
        strB.append(getTOString("txtNewBasic", txtNewBasic));
        strB.append(getTOString("txtIncrementDesignation", txtIncrementDesignation));
        strB.append(getTOString("txtBasicSalary", txtBasicSalary));
        strB.append(getTOString("txtIncrementNo", txtIncrementNo));
        strB.append(getTOString("incrementID", incrementID));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("empId", empId));
        strB.append(getTOXml("txtIncrementEmpName", txtIncrementEmpName));
        strB.append(getTOXml("effectiveDate", effectiveDate));
        strB.append(getTOXml("createdDate", createdDate));
        strB.append(getTOXml("employeeStage", employeeStage));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("tempSlNO", tempSlNO));
        strB.append(getTOXml("tdtIncrementDate", tdtIncrementDate));
        strB.append(getTOXml("txtIncrementAmount", txtIncrementAmount));
        strB.append(getTOXml("txtNewBasic", txtNewBasic));
        strB.append(getTOXml("txtIncrementDesignation", txtIncrementDesignation));
        strB.append(getTOXml("txtBasicSalary", txtBasicSalary));
        strB.append(getTOXml("txtIncrementNo", txtIncrementNo));
        strB.append(getTOXml("incrementID", incrementID));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property slNo.
     *
     * @return Value of property slNo.
     */
    public java.lang.Double getSlNo() {
        return slNo;
    }

    /**
     * Setter for property slNo.
     *
     * @param slNo New value of property slNo.
     */
    public void setSlNo(java.lang.Double slNo) {
        this.slNo = slNo;
    }

    /**
     * Getter for property empId.
     *
     * @return Value of property empId.
     */
    public java.lang.String getEmpId() {
        return empId;
    }

    /**
     * Setter for property empId.
     *
     * @param empId New value of property empId.
     */
    public void setEmpId(java.lang.String empId) {
        this.empId = empId;
    }

    /**
     * Getter for property effectiveDate.
     *
     * @return Value of property effectiveDate.
     */
    public java.util.Date getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Setter for property effectiveDate.
     *
     * @param effectiveDate New value of property effectiveDate.
     */
    public void setEffectiveDate(java.util.Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * Getter for property createdDate.
     *
     * @return Value of property createdDate.
     */
    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Setter for property createdDate.
     *
     * @param createdDate New value of property createdDate.
     */
    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Getter for property employeeStage.
     *
     * @return Value of property employeeStage.
     */
//    public java.lang.String getEmployeeStage() {
//        return employeeStage;
//    }
    /**
     * Setter for property employeeStage.
     *
     * @param employeeStage New value of property employeeStage.
     */
//    public void setEmployeeStage(java.lang.String employeeStage) {
//        this.employeeStage = employeeStage;
//    }
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
     * Getter for property tempSlNO.
     *
     * @return Value of property tempSlNO.
     */
    public java.lang.Double getTempSlNO() {
        return tempSlNO;
    }

    /**
     * Setter for property tempSlNO.
     *
     * @param tempSlNO New value of property tempSlNO.
     */
    public void setTempSlNO(java.lang.Double tempSlNO) {
        this.tempSlNO = tempSlNO;
    }

    /**
     * Getter for property tdtIncrementDate.
     *
     * @return Value of property tdtIncrementDate.
     */
    public java.util.Date getTdtIncrementDate() {
        return tdtIncrementDate;
    }

    /**
     * Setter for property tdtIncrementDate.
     *
     * @param tdtIncrementDate New value of property tdtIncrementDate.
     */
    public void setTdtIncrementDate(java.util.Date tdtIncrementDate) {
        this.tdtIncrementDate = tdtIncrementDate;
    }

    /**
     * Getter for property txtEmployeeGrade.
     *
     * @return Value of property txtEmployeeGrade.
     */
//    public java.lang.String getTxtEmployeeGrade() {
//        return txtEmployeeGrade;
//    }
    /**
     * Setter for property txtEmployeeGrade.
     *
     * @param txtEmployeeGrade New value of property txtEmployeeGrade.
     */
//    public void setTxtEmployeeGrade(java.lang.String txtEmployeeGrade) {
//        this.txtEmployeeGrade = txtEmployeeGrade;
//    }
    /**
     * Getter for property employeeStage.
     *
     * @return Value of property employeeStage.
     */
    public java.lang.String getEmployeeStage() {
        return employeeStage;
    }

    /**
     * Setter for property employeeStage.
     *
     * @param employeeStage New value of property employeeStage.
     */
    public void setEmployeeStage(java.lang.String employeeStage) {
        this.employeeStage = employeeStage;
    }

    /**
     * Getter for property txtIncrementAmount.
     *
     * @return Value of property txtIncrementAmount.
     */
    public java.lang.String getTxtIncrementAmount() {
        return txtIncrementAmount;
    }

    /**
     * Setter for property txtIncrementAmount.
     *
     * @param txtIncrementAmount New value of property txtIncrementAmount.
     */
    public void setTxtIncrementAmount(java.lang.String txtIncrementAmount) {
        this.txtIncrementAmount = txtIncrementAmount;
    }

    /**
     * Getter for property txtNewBasic.
     *
     * @return Value of property txtNewBasic.
     */
    public java.lang.String getTxtNewBasic() {
        return txtNewBasic;
    }

    /**
     * Setter for property txtNewBasic.
     *
     * @param txtNewBasic New value of property txtNewBasic.
     */
    public void setTxtNewBasic(java.lang.String txtNewBasic) {
        this.txtNewBasic = txtNewBasic;
    }

    /**
     * Getter for property txtIncrementEmpName.
     *
     * @return Value of property txtIncrementEmpName.
     */
    public java.lang.String getTxtIncrementEmpName() {
        return txtIncrementEmpName;
    }

    /**
     * Setter for property txtIncrementEmpName.
     *
     * @param txtIncrementEmpName New value of property txtIncrementEmpName.
     */
    public void setTxtIncrementEmpName(java.lang.String txtIncrementEmpName) {
        this.txtIncrementEmpName = txtIncrementEmpName;
    }

    /**
     * Getter for property txtIncrementDesignation.
     *
     * @return Value of property txtIncrementDesignation.
     */
    public java.lang.String getTxtIncrementDesignation() {
        return txtIncrementDesignation;
    }

    /**
     * Setter for property txtIncrementDesignation.
     *
     * @param txtIncrementDesignation New value of property
     * txtIncrementDesignation.
     */
    public void setTxtIncrementDesignation(java.lang.String txtIncrementDesignation) {
        this.txtIncrementDesignation = txtIncrementDesignation;
    }

    /**
     * Getter for property txtBasicSalary.
     *
     * @return Value of property txtBasicSalary.
     */
    public java.lang.String getTxtBasicSalary() {
        return txtBasicSalary;
    }

    /**
     * Setter for property txtBasicSalary.
     *
     * @param txtBasicSalary New value of property txtBasicSalary.
     */
    public void setTxtBasicSalary(java.lang.String txtBasicSalary) {
        this.txtBasicSalary = txtBasicSalary;
    }

    /**
     * Getter for property txtIncrementNo.
     *
     * @return Value of property txtIncrementNo.
     */
    public java.lang.String getTxtIncrementNo() {
        return txtIncrementNo;
    }

    /**
     * Setter for property txtIncrementNo.
     *
     * @param txtIncrementNo New value of property txtIncrementNo.
     */
    public void setTxtIncrementNo(java.lang.String txtIncrementNo) {
        this.txtIncrementNo = txtIncrementNo;
    }

    /**
     * Getter for property incrementID.
     *
     * @return Value of property incrementID.
     */
    public java.lang.String getIncrementID() {
        return incrementID;
    }

    /**
     * Setter for property incrementID.
     *
     * @param incrementID New value of property incrementID.
     */
    public void setIncrementID(java.lang.String incrementID) {
        this.incrementID = incrementID;
    }
}