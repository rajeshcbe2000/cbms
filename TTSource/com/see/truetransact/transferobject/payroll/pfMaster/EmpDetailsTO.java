/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * EmpDetailsTO.java
 *
 * Created on July 19, 2005, 12:58 PM
 */
package com.see.truetransact.transferobject.payroll.pfMaster;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author anjuanand
 */
public class EmpDetailsTO extends TransferObject implements Serializable {
    
    
    private String custId;
    private String custFName;
    private String custMName;
    private String custLName;
    private String empStreet;
    private String empArea;
    private String empCity;
    private String empPinCode;
    private String empState;
    private String empCountry;
    private Date empDob;

    public String getCustFName() {
        return custFName;
    }

    public void setCustFName(String custFName) {
        this.custFName = custFName;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustLName() {
        return custLName;
    }

    public void setCustLName(String custLName) {
        this.custLName = custLName;
    }

    public String getCustMName() {
        return custMName;
    }

    public void setCustMName(String custMName) {
        this.custMName = custMName;
    }
   
    public String getEmpArea() {
        return empArea;
    }

    public void setEmpArea(String empArea) {
        this.empArea = empArea;
    }

    public String getEmpCity() {
        return empCity;
    }

    public void setEmpCity(String empCity) {
        this.empCity = empCity;
    }

    public String getEmpCountry() {
        return empCountry;
    }

    public void setEmpCountry(String empCountry) {
        this.empCountry = empCountry;
    }

    public Date getEmpDob() {
        return empDob;
    }

    public void setEmpDob(Date empDob) {
        this.empDob = empDob;
    }

   
    public String getEmpPinCode() {
        return empPinCode;
    }

    public void setEmpPinCode(String empPinCode) {
        this.empPinCode = empPinCode;
    }

    public String getEmpState() {
        return empState;
    }

    public void setEmpState(String empState) {
        this.empState = empState;
    }

    public String getEmpStreet() {
        return empStreet;
    }

    public void setEmpStreet(String empStreet) {
        this.empStreet = empStreet;
    }

   
    /**
     * Creates a new instance of EmpDetailsTO
     */
    public EmpDetailsTO() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
