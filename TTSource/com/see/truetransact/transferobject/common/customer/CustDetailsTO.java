/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CustDetailsTO.java
 *
 * Created on July 19, 2005, 12:58 PM
 */
package com.see.truetransact.transferobject.common.customer;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 *
 * @author 152691
 */
public class CustDetailsTO extends TransferObject implements Serializable {

    private String custId;
    private String custFName;
    private String custMName;
    private String custLName;
    private String custStreet;
    private String custArea;
    private String custCity;
    private String custPinCode;
    private String custState;
    private String custCountry;
    private Date custDob;

    /**
     * Creates a new instance of CustDetailsTO
     */
    public CustDetailsTO() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    /**
     * Getter for property custId.
     *
     * @return Value of property custId.
     */
    public java.lang.String getCustId() {
        return custId;
    }

    /**
     * Setter for property custId.
     *
     * @param custId New value of property custId.
     */
    public void setCustId(java.lang.String custId) {
        this.custId = custId;
    }

    /**
     * Getter for property custFName.
     *
     * @return Value of property custFName.
     */
    public java.lang.String getCustFName() {
        return custFName;
    }

    /**
     * Setter for property custFName.
     *
     * @param custFName New value of property custFName.
     */
    public void setCustFName(java.lang.String custFName) {
        this.custFName = custFName;
    }

    /**
     * Getter for property custLName.
     *
     * @return Value of property custLName.
     */
    public java.lang.String getCustLName() {
        return custLName;
    }

    /**
     * Setter for property custLName.
     *
     * @param custLName New value of property custLName.
     */
    public void setCustLName(java.lang.String custLName) {
        this.custLName = custLName;
    }

    /**
     * Getter for property custStreet.
     *
     * @return Value of property custStreet.
     */
    public java.lang.String getCustStreet() {
        return custStreet;
    }

    /**
     * Setter for property custStreet.
     *
     * @param custStreet New value of property custStreet.
     */
    public void setCustStreet(java.lang.String custStreet) {
        this.custStreet = custStreet;
    }

    /**
     * Getter for property custArea.
     *
     * @return Value of property custArea.
     */
    public java.lang.String getCustArea() {
        return custArea;
    }

    /**
     * Setter for property custArea.
     *
     * @param custArea New value of property custArea.
     */
    public void setCustArea(java.lang.String custArea) {
        this.custArea = custArea;
    }

    /**
     * Getter for property custCity.
     *
     * @return Value of property custCity.
     */
    public java.lang.String getCustCity() {
        return custCity;
    }

    /**
     * Setter for property custCity.
     *
     * @param custCity New value of property custCity.
     */
    public void setCustCity(java.lang.String custCity) {
        this.custCity = custCity;
    }

    /**
     * Getter for property custPinCode.
     *
     * @return Value of property custPinCode.
     */
    public java.lang.String getCustPinCode() {
        return custPinCode;
    }

    /**
     * Setter for property custPinCode.
     *
     * @param custPinCode New value of property custPinCode.
     */
    public void setCustPinCode(java.lang.String custPinCode) {
        this.custPinCode = custPinCode;
    }

    /**
     * Getter for property custState.
     *
     * @return Value of property custState.
     */
    public java.lang.String getCustState() {
        return custState;
    }

    /**
     * Setter for property custState.
     *
     * @param custState New value of property custState.
     */
    public void setCustState(java.lang.String custState) {
        this.custState = custState;
    }

    /**
     * Getter for property custCountry.
     *
     * @return Value of property custCountry.
     */
    public java.lang.String getCustCountry() {
        return custCountry;
    }

    /**
     * Setter for property custCountry.
     *
     * @param custCountry New value of property custCountry.
     */
    public void setCustCountry(java.lang.String custCountry) {
        this.custCountry = custCountry;
    }

    /**
     * Getter for property custMName.
     *
     * @return Value of property custMName.
     */
    public java.lang.String getCustMName() {
        return custMName;
    }

    /**
     * Setter for property custMName.
     *
     * @param custMName New value of property custMName.
     */
    public void setCustMName(java.lang.String custMName) {
        this.custMName = custMName;
    }

    /**
     * Getter for property custDob.
     *
     * @return Value of property custDob.
     */
    public Date getCustDob() {
        return custDob;
    }

    /**
     * Setter for property custDob.
     *
     * @param custDob New value of property custDob.
     */
    public void setCustDob(Date custDob) {
        this.custDob = custDob;
    }
}
