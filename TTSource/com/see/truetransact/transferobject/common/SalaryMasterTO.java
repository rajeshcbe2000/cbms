/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryMasterTO.java
 * 
 * Created on Mon Feb 21 13:07:50 IST 2011
 */
package com.see.truetransact.transferobject.common;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SALARY_MASTER.
 */
public class SalaryMasterTO extends TransferObject implements Serializable {

    private String salaryId = "";
    private Date salaryDt = null;
    private Date payMentDt = null;
    private Date createdDate = null;
    private String salaryStatus = "";

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
     * Setter/Getter for SALARY_DT - table Field
     */
    public void setSalaryDt(Date salaryDt) {
        this.salaryDt = salaryDt;
    }

    public Date getSalaryDt() {
        return salaryDt;
    }

    /**
     * Setter/Getter for CREATED_DATE - table Field
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Setter/Getter for SALARY_STATUS - table Field
     */
    public void setSalaryStatus(String salaryStatus) {
        this.salaryStatus = salaryStatus;
    }

    public String getSalaryStatus() {
        return salaryStatus;
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
        strB.append(getTOString("salaryDt", salaryDt));
        strB.append(getTOString("createdDate", createdDate));
        strB.append(getTOString("payMentDt", payMentDt));
        strB.append(getTOString("salaryStatus", salaryStatus));
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
        strB.append(getTOXml("salaryDt", salaryDt));
        strB.append(getTOXml("createdDate", createdDate));
        strB.append(getTOXml("payMentDt", payMentDt));
        strB.append(getTOXml("salaryStatus", salaryStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property payMentDt.
     *
     * @return Value of property payMentDt.
     */
    public java.util.Date getPayMentDt() {
        return payMentDt;
    }

    /**
     * Setter for property payMentDt.
     *
     * @param payMentDt New value of property payMentDt.
     */
    public void setPayMentDt(java.util.Date payMentDt) {
        this.payMentDt = payMentDt;
    }
}