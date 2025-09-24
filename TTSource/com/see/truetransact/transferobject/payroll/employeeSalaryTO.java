/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * employeeSalaryTO.java
 */

package com.see.truetransact.transferobject.payroll;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;

/**
 *
 * @author rishad
 */
public class employeeSalaryTO extends TransferObject implements Serializable {

    private String employeeName = "";
    private String employeeId = "";
    private String netSalaryAccNo = "";
    private String salProdType = "";
    private Double earnings = 0.0;
    private Double deductions = 0.0;
    private Double netSalary = 0.0;
    private String salProdId="";

    public String getSalProdId() {
        return salProdId;
    }

    public void setSalProdId(String salProdId) {
        this.salProdId = salProdId;
    }
    

  

    public Double getDeductions() {
        return deductions;
    }

    public void setDeductions(Double deductions) {
        this.deductions = deductions;
    }

    public Double getEarnings() {
        return earnings;
    }

    public void setEarnings(Double earnings) {
        this.earnings = earnings;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public String getNetSalaryAccNo() {
        return netSalaryAccNo;
    }

    public void setNetSalaryAccNo(String netSalaryAccNo) {
        this.netSalaryAccNo = netSalaryAccNo;
    }

    public String getSalProdType() {
        return salProdType;
    }

    public void setSalProdType(String salProdType) {
        this.salProdType = salProdType;
    }

    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("employeeId", employeeId));
        strB.append(getTOString("employeeName", employeeName));
        strB.append(getTOString("netSalaryAccNo", netSalaryAccNo));
        strB.append(getTOString("salProdType", salProdType));
        strB.append(getTOString("earnings", earnings));
        strB.append(getTOString("deductions", deductions));
        strB.append(getTOString("netSalary", netSalary));
        strB.append(getTOString("salProdId", salProdId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("employeeId", employeeId));
        strB.append(getTOXml("netSalaryAccNo", netSalaryAccNo));
        strB.append(getTOXml("salProdType", salProdType));
        strB.append(getTOXml("earnings", earnings));
        strB.append(getTOXml("deductions", deductions));
        strB.append(getTOXml("employeeName", employeeName));
        strB.append(getTOXml("netSalary", netSalary));
        strB.append(getTOXml("salProdId", salProdId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}
