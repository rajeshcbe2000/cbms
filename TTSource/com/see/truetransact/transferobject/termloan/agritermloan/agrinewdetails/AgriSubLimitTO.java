/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriSubLimitTO.java
 *
 * Created on April 30, 2009, 12:57 PM
 */
package com.see.truetransact.transferobject.termloan.agritermloan.agrinewdetails;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;
import java.lang.StringBuffer;

/**
 *
 * @author Administrator
 */
public class AgriSubLimitTO extends TransferObject implements Serializable {

    Date startDt = null;
    Date endDt = null;
    Double subLimit = null;
    String slno = null;
    String status = null;
    String acctNum = "";

    /**
     * Creates a new instance of AgriSubLimitTO
     */
    public AgriSubLimitTO() {
    }

    public String toString() {
        StringBuffer stb = new StringBuffer(getTOStringStart(getClass().getName()));
        stb.append(getTOString("startDt", startDt));
        stb.append(getTOString("endDt", endDt));
        stb.append(getTOString("subLimit", subLimit));
        stb.append(getTOString("slno", slno));
        stb.append(getTOString("status", status));
        stb.append(getTOString("acctNum", acctNum));
        stb.append(getTOStringEnd());
        return stb.toString();
    }

    public String toXml() {
        StringBuffer stb = new StringBuffer(getTOXmlStart(getClass().getName()));
        stb.append(getTOXml("startDt", startDt));
        stb.append(getTOXml("endDt", endDt));
        stb.append(getTOXml("subLimit", subLimit));
        stb.append(getTOXml("slno", slno));
        stb.append(getTOXml("status", status));
        stb.append(getTOXml("acctNum", acctNum));
        stb.append(getTOXmlEnd());
        return stb.toString();
    }

    /**
     * Getter for property startDt.
     *
     * @return Value of property startDt.
     */
    public java.util.Date getStartDt() {
        return startDt;
    }

    /**
     * Setter for property startDt.
     *
     * @param startDt New value of property startDt.
     */
    public void setStartDt(java.util.Date startDt) {
        this.startDt = startDt;
    }

    /**
     * Getter for property endDt.
     *
     * @return Value of property endDt.
     */
    public java.util.Date getEndDt() {
        return endDt;
    }

    /**
     * Setter for property endDt.
     *
     * @param endDt New value of property endDt.
     */
    public void setEndDt(java.util.Date endDt) {
        this.endDt = endDt;
    }

    /**
     * Getter for property subLimit.
     *
     * @return Value of property subLimit.
     */
    public java.lang.Double getSubLimit() {
        return subLimit;
    }

    /**
     * Setter for property subLimit.
     *
     * @param subLimit New value of property subLimit.
     */
    public void setSubLimit(java.lang.Double subLimit) {
        this.subLimit = subLimit;
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
}
