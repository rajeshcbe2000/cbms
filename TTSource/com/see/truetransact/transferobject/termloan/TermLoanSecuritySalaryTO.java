
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanSecuritySalaryTO.java
 * 
 * Created on Thu Nov 24 14:55:58 IST 2011
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_SECURITY_SALARY.
 */
public class TermLoanSecuritySalaryTO extends TransferObject implements Serializable {

    private String acctNum = "";
    private String salaryCerficateNo = "";
    private String empName = "";
    private String empAddress = "";
    private String city = "";
    private Double pin = null;
    private String designation = "";
    private Long contactNo = null;
    private Date retirementDt = null;
    private String empMemberNo = "";
    private Double totalSalary = null;
    private String networth = "";
    private String salaryRemarks = "";
    private Double slNo = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    /**
     * Setter/Getter for ACCT_NUM - table Field
     */
    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAcctNum() {
        return acctNum;
    }

    /**
     * Setter/Getter for SALARY_CERFICATE_NO - table Field
     */
    public void setSalaryCerficateNo(String salaryCerficateNo) {
        this.salaryCerficateNo = salaryCerficateNo;
    }

    public String getSalaryCerficateNo() {
        return salaryCerficateNo;
    }

    /**
     * Setter/Getter for EMP_NAME - table Field
     */
    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpName() {
        return empName;
    }

    /**
     * Setter/Getter for EMP_ADDRESS - table Field
     */
    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    /**
     * Setter/Getter for CITY - table Field
     */
    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    /**
     * Setter/Getter for PIN - table Field
     */
    public void setPin(Double pin) {
        this.pin = pin;
    }

    public Double getPin() {
        return pin;
    }

    /**
     * Setter/Getter for DESIGNATION - table Field
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignation() {
        return designation;
    }

    public Long getContactNo() {
        return contactNo;
    }

    /**
     * Setter/Getter for CONTACT_NO - table Field
     */
    public void setContactNo(Long contactNo) {    
        this.contactNo = contactNo;
    }

    /**
     * Setter/Getter for RETIREMENT_DT - table Field
     */
    public void setRetirementDt(Date retirementDt) {
        this.retirementDt = retirementDt;
    }

    public Date getRetirementDt() {
        return retirementDt;
    }

    /**
     * Setter/Getter for EMP_MEMBER_NO - table Field
     */
    public void setEmpMemberNo(String empMemberNo) {
        this.empMemberNo = empMemberNo;
    }

    public String getEmpMemberNo() {
        return empMemberNo;
    }

    /**
     * Setter/Getter for TOTAL_SALARY - table Field
     */
    public void setTotalSalary(Double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public Double getTotalSalary() {
        return totalSalary;
    }

    /**
     * Setter/Getter for NETWORTH - table Field
     */
    public void setNetworth(String networth) {
        this.networth = networth;
    }

    public String getNetworth() {
        return networth;
    }

    /**
     * Setter/Getter for SALARY_REMARKS - table Field
     */
    public void setSalaryRemarks(String salaryRemarks) {
        this.salaryRemarks = salaryRemarks;
    }

    public String getSalaryRemarks() {
        return salaryRemarks;
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
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("salaryCerficateNo", salaryCerficateNo));
        strB.append(getTOString("empName", empName));
        strB.append(getTOString("empAddress", empAddress));
        strB.append(getTOString("city", city));
        strB.append(getTOString("pin", pin));
        strB.append(getTOString("designation", designation));
        strB.append(getTOString("contactNo", contactNo));
        strB.append(getTOString("retirementDt", retirementDt));
        strB.append(getTOString("empMemberNo", empMemberNo));
        strB.append(getTOString("totalSalary", totalSalary));
        strB.append(getTOString("networth", networth));
        strB.append(getTOString("salaryRemarks", salaryRemarks));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("salaryCerficateNo", salaryCerficateNo));
        strB.append(getTOXml("empName", empName));
        strB.append(getTOXml("empAddress", empAddress));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("pin", pin));
        strB.append(getTOXml("designation", designation));
        strB.append(getTOXml("contactNo", contactNo));
        strB.append(getTOXml("retirementDt", retirementDt));
        strB.append(getTOXml("empMemberNo", empMemberNo));
        strB.append(getTOXml("totalSalary", totalSalary));
        strB.append(getTOXml("networth", networth));
        strB.append(getTOXml("salaryRemarks", salaryRemarks));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
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

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }    
    
}
