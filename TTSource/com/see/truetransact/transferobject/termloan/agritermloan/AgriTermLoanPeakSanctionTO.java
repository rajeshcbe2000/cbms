/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriTermLoanPeakSanctionTO.java
 *
 * Created on February 18, 2009, 1:04 PM
 */
package com.see.truetransact.transferobject.termloan.agritermloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author Administrator
 */
public class AgriTermLoanPeakSanctionTO extends TransferObject implements Serializable {

    private String acctNum = null;
    private Date peakSancFromDt = null;
    private Date peakSancToDt = null;
    private Double peakSanctionAmt = null;

    /**
     * Creates a new instance of TermLoanPeakSanctionTO
     */
    public AgriTermLoanPeakSanctionTO() {
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("peakSancFromDt", peakSancFromDt));
        strB.append(getTOString("peakSancToDt", peakSancToDt));
        strB.append(getTOString("peakSanctionAmt", peakSanctionAmt));
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
        strB.append(getTOXml("peakSancFromDt", peakSancFromDt));
        strB.append(getTOXml("peakSancToDt", peakSancToDt));
        strB.append(getTOXml("peakSanctionAmt", peakSanctionAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getKeyData() {
        setKeyColumns("acctNum");
        return acctNum;
    }

    /**
     * Getter for property peakSancFromDt.
     *
     * @return Value of property peakSancFromDt.
     */
    public java.util.Date getPeakSancFromDt() {
        return peakSancFromDt;
    }

    /**
     * Setter for property peakSancFromDt.
     *
     * @param peakSancFromDt New value of property peakSancFromDt.
     */
    public void setPeakSancFromDt(java.util.Date peakSancFromDt) {
        this.peakSancFromDt = peakSancFromDt;
    }

    /**
     * Getter for property peakSancToDt.
     *
     * @return Value of property peakSancToDt.
     */
    public java.util.Date getPeakSancToDt() {
        return peakSancToDt;
    }

    /**
     * Setter for property peakSancToDt.
     *
     * @param peakSancToDt New value of property peakSancToDt.
     */
    public void setPeakSancToDt(java.util.Date peakSancToDt) {
        this.peakSancToDt = peakSancToDt;
    }

    /**
     * Getter for property peakSanctionAmt.
     *
     * @return Value of property peakSanctionAmt.
     */
    public java.lang.Double getPeakSanctionAmt() {
        return peakSanctionAmt;
    }

    /**
     * Setter for property peakSanctionAmt.
     *
     * @param peakSanctionAmt New value of property peakSanctionAmt.
     */
    public void setPeakSanctionAmt(java.lang.Double peakSanctionAmt) {
        this.peakSanctionAmt = peakSanctionAmt;
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
