/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryMasterDetailsTO.java
 * 
 * Created on Mon Feb 21 13:11:45 IST 2011
 */
package com.see.truetransact.transferobject.common;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SALARY_MASTER_DETAILS.
 */
public class SalaryMasterDetailsTO extends TransferObject implements Serializable {

    private String salaryId = "";
    private String employeeId = "";
    private String salaryType = "";
    private String amount = "";
    private String earningOrDeduction = "";
    private String branchCode = "";
    private String zonalCode = "";

    /**
     * Setter/Getter for SALARY_ID - table Field
     */
    public void setSalaryId(String salaryId) {
        this.salaryId = salaryId;
    }

    public String getSalaryId() {
        return salaryId;
    }

    /**
     * Setter/Getter for EMPLOYEE_ID - table Field
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Setter/Getter for SALARY_TYPE - table Field
     */
    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public String getSalaryType() {
        return salaryType;
    }

    /**
     * Setter/Getter for AMOUNT - table Field
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    /**
     * Setter/Getter for EARNING_OR_DEDUCTION - table Field
     */
    public void setEarningOrDeduction(String earningOrDeduction) {
        this.earningOrDeduction = earningOrDeduction;
    }

    public String getEarningOrDeduction() {
        return earningOrDeduction;
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
        strB.append(getTOString("salaryId", salaryId));
        strB.append(getTOString("employeeId", employeeId));
        strB.append(getTOString("salaryType", salaryType));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("earningOrDeduction", earningOrDeduction));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("salaryId", salaryId));
        strB.append(getTOXml("employeeId", employeeId));
        strB.append(getTOXml("salaryType", salaryType));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("earningOrDeduction", earningOrDeduction));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property zonalCode.
     *
     * @return Value of property zonalCode.
     */
    public java.lang.String getZonalCode() {
        return zonalCode;
    }

    /**
     * Setter for property zonalCode.
     *
     * @param zonalCode New value of property zonalCode.
     */
    public void setZonalCode(java.lang.String zonalCode) {
        this.zonalCode = zonalCode;
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
}