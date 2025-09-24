/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SHGTO.java
 * 
 * Created on Sat Oct 15 14:06:54 IST 2011
 */
package com.see.truetransact.transferobject.termloan.groupLoan;

import com.see.truetransact.ui.termloan.groupLoan.*;
import com.see.truetransact.transferobject.termloan.SHG.*;
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SHG_MEMBER_DETAILS.
 */
public class GroupLoanCustomerTO extends TransferObject implements Serializable {

    private String groupLoanNO = "";

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getGroupLoanNO() {
        return groupLoanNO;
    }

    public void setGroupLoanNO(String groupLoanNO) {
        this.groupLoanNO = groupLoanNO;
    }

    public String getLimitAmt() {
        return limitAmt;
    }

    public void setLimitAmt(String limitAmt) {
        this.limitAmt = limitAmt;
    }
    private String actNum = "";

    public String getActNum() {
        return actNum;
    }

    public void setActNum(String actNum) {
        this.actNum = actNum;
    }
    private String custId = "";
    private String custName= "";
    private String limitAmt = "";
    private String street = "";
    private String area = "";
    private String city = "";
    private String state = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String creditNo = "";
    private String branchId = "";
    private String customerActNum = "";
    private String customerActProdType = "";
    private String customerActProdId= "";

    public String getCustomerActNum() {
        return customerActNum;
    }

    public void setCustomerActNum(String customerActNum) {
        this.customerActNum = customerActNum;
    }

    public String getCustomerActProdId() {
        return customerActProdId;
    }

    public void setCustomerActProdId(String customerActProdId) {
        this.customerActProdId = customerActProdId;
    }

    public String getCustomerActProdType() {
        return customerActProdType;
    }

    public void setCustomerActProdType(String customerActProdType) {
        this.customerActProdType = customerActProdType;
    }
    
    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }
   
    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    /**
     * Setter/Getter for AREA - table Field
     */
    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
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
     * Setter/Getter for STATE - table Field
     */
    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    /**
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
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
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("custName", custName));
        strB.append(getTOString("limitAmt", limitAmt));
        strB.append(getTOString("street", street));
        strB.append(getTOString("area", area));
        strB.append(getTOString("city", city));
        strB.append(getTOString("state", state));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("creditNo", creditNo));
	strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("customerActNum", customerActNum));
        strB.append(getTOString("customerActProdId", customerActProdId));
        strB.append(getTOString("customerActProdType", customerActProdType));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("custName", custName));
        strB.append(getTOXml("limitAmt", limitAmt));
        strB.append(getTOXml("street", street));
        strB.append(getTOXml("area", area));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("creditNo", creditNo));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("customerActNum", customerActNum));
        strB.append(getTOXml("customerActProdId", customerActProdId));
        strB.append(getTOXml("customerActProdType", customerActProdType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

}