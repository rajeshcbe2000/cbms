/*


 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * EmployeeAddrTO.java
 *
 * Created on February 16, 2004, 11:30 AM
 */
package com.see.truetransact.transferobject.sysadmin.employee;

/**
 *
 * @author rahul
 */
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

public class EmployeeAddrTO extends TransferObject implements Serializable {

    private String employeeId = "";
    private String street = "";
    private String area = "";
    private String city = "";
    private String state = "";
    private String pinCode = "";
    private String countryCode = "";

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }
}