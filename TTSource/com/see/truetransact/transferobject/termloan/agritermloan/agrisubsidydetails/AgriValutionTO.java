/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriValutionTO.java
 *
 * Created on June 2, 2009, 3:22 PM
 */
package com.see.truetransact.transferobject.termloan.agritermloan.agrisubsidydetails;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;
import java.lang.StringBuffer;

/**
 *
 * @author abi
 */
public class AgriValutionTO extends TransferObject implements Serializable {

    String propertyType = "";
    Date valutionDt = null;
    Double valutionAmt = null;
    String valuatedBy = "";
    String valutionRemarks = "";
    String acctNum = "";
    String status = "";
    String slno = "";

    public String toString() {
        StringBuffer buf = new StringBuffer(getClass().getName());
        buf.append(getTOStringStart(getClass().getName()));
        buf.append(getTOString("propertyType", propertyType));
        buf.append(getTOString("valutionDt", valutionDt));
        buf.append(getTOString("valutionAmt", valutionAmt));
        buf.append(getTOString("valuatedBy", valuatedBy));
        buf.append(getTOString("valutionRemarks", valutionRemarks));
        buf.append(getTOString("acctNum", acctNum));
        buf.append(getTOString("status", status));
        buf.append(getTOString("slno", slno));

        buf.append(getTOStringEnd());
        return buf.toString();
    }

    public String getTOXml() {
        StringBuffer buf = new StringBuffer(getClass().getName());
        buf.append(getTOXmlStart(getClass().getName()));
        buf.append(getTOXml("propertyType", propertyType));
        buf.append(getTOXml("valutionDt", valutionDt));
        buf.append(getTOXml("valutionAmt", valutionAmt));
        buf.append(getTOXml("valuatedBy", valuatedBy));
        buf.append(getTOXml("valutionRemarks", valutionRemarks));
        buf.append(getTOXml("acctNum", acctNum));
        buf.append(getTOXml("status", status));
        buf.append(getTOXml("slno", slno));
        buf.append(getTOXmlEnd());
        return buf.toString();
    }

    /**
     * Getter for property propertyType.
     *
     * @return Value of property propertyType.
     */
    public java.lang.String getPropertyType() {
        return propertyType;
    }

    /**
     * Setter for property propertyType.
     *
     * @param propertyType New value of property propertyType.
     */
    public void setPropertyType(java.lang.String propertyType) {
        this.propertyType = propertyType;
    }

    /**
     * Getter for property valutionDt.
     *
     * @return Value of property valutionDt.
     */
    public java.util.Date getValutionDt() {
        return valutionDt;
    }

    /**
     * Setter for property valutionDt.
     *
     * @param valutionDt New value of property valutionDt.
     */
    public void setValutionDt(java.util.Date valutionDt) {
        this.valutionDt = valutionDt;
    }

    /**
     * Getter for property valutionAmt.
     *
     * @return Value of property valutionAmt.
     */
    public java.lang.Double getValutionAmt() {
        return valutionAmt;
    }

    /**
     * Setter for property valutionAmt.
     *
     * @param valutionAmt New value of property valutionAmt.
     */
    public void setValutionAmt(java.lang.Double valutionAmt) {
        this.valutionAmt = valutionAmt;
    }

    /**
     * Getter for property valuatedBy.
     *
     * @return Value of property valuatedBy.
     */
    public java.lang.String getValuatedBy() {
        return valuatedBy;
    }

    /**
     * Setter for property valuatedBy.
     *
     * @param valuatedBy New value of property valuatedBy.
     */
    public void setValuatedBy(java.lang.String valuatedBy) {
        this.valuatedBy = valuatedBy;
    }

    /**
     * Getter for property valutionRemarks.
     *
     * @return Value of property valutionRemarks.
     */
    public java.lang.String getValutionRemarks() {
        return valutionRemarks;
    }

    /**
     * Setter for property valutionRemarks.
     *
     * @param valutionRemarks New value of property valutionRemarks.
     */
    public void setValutionRemarks(java.lang.String valutionRemarks) {
        this.valutionRemarks = valutionRemarks;
    }

    /**
     * Getter for property acctNum.
     *
     * @return Value of property acctNum.
     */
    public java.lang.String getAcctNum() {
        return acctNum;
    }

    /**
     * Setter for property acctNum.
     *
     * @param acctNum New value of property acctNum.
     */
    public void setAcctNum(java.lang.String acctNum) {
        this.acctNum = acctNum;
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
     * Getter for property slno.
     *
     * @return Value of property slno.
     */
    public java.lang.String getSlno() {
        return slno;
    }

    /**
     * Setter for property slno.
     *
     * @param slno New value of property slno.
     */
    public void setSlno(java.lang.String slno) {
        this.slno = slno;
    }
}
