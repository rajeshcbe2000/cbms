/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TermLoanDisburstTO.java
 *
 * Created on April 30, 2009, 12:57 PM
 */
package com.see.truetransact.transferobject.termloan;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;
import java.lang.StringBuffer;

/**
 *
 * @author Administrator
 */
public class TermLoanDisburstTO extends TransferObject implements Serializable {

    Date disburstDt = null;
    String disburstAmt_yes_no = null;
    String remarks = null;
    String disburstStage = null;
    Double subLimitAmtRs = null;
    Double subLimitAmt = null;
    String slno = null;
    String status = null;
    String acctNum = "";

    public String toString() {
        StringBuffer stb = new StringBuffer(getTOStringStart(getClass().getName()));
        stb.append(getTOString("disburstDt", disburstDt));
        stb.append(getTOString("disburstAmt_yes_no", disburstAmt_yes_no));
        stb.append(getTOString("remarks", remarks));
        stb.append(getTOString("disburstStage", disburstStage));
        stb.append(getTOString("subLimitAmtRs", subLimitAmtRs));
        stb.append(getTOString("subLimitAmt", subLimitAmt));
        stb.append(getTOString("slno", slno));
        stb.append(getTOString("status", status));
        stb.append(getTOString("acctNum", acctNum));
        stb.append(getTOStringEnd());
        return stb.toString();
    }

    public String toXml() {
        StringBuffer stb = new StringBuffer(getTOXmlStart(getClass().getName()));
        stb.append(getTOXml("disburstDt", disburstDt));
        stb.append(getTOXml("disburstAmt_yes_no", disburstAmt_yes_no));
        stb.append(getTOXml("remarks", remarks));
        stb.append(getTOXml("disburstStage", disburstStage));
        stb.append(getTOXml("subLimitAmtRs", subLimitAmtRs));
        stb.append(getTOXml("subLimitAmt", subLimitAmt));
        stb.append(getTOXml("slno", slno));
        stb.append(getTOXml("status", status));
        stb.append(getTOXml("acctNum", acctNum));
        stb.append(getTOXmlEnd());
        return stb.toString();
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

    /**
     * Getter for property disburstDt.
     *
     * @return Value of property disburstDt.
     */
    public java.util.Date getDisburstDt() {
        return disburstDt;
    }

    /**
     * Setter for property disburstDt.
     *
     * @param disburstDt New value of property disburstDt.
     */
    public void setDisburstDt(java.util.Date disburstDt) {
        this.disburstDt = disburstDt;
    }

    /**
     * Getter for property disburstAmt_yes_no.
     *
     * @return Value of property disburstAmt_yes_no.
     */
    public java.lang.String getDisburstAmt_yes_no() {
        return disburstAmt_yes_no;
    }

    /**
     * Setter for property disburstAmt_yes_no.
     *
     * @param disburstAmt_yes_no New value of property disburstAmt_yes_no.
     */
    public void setDisburstAmt_yes_no(java.lang.String disburstAmt_yes_no) {
        this.disburstAmt_yes_no = disburstAmt_yes_no;
    }

    /**
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }

    /**
     * Setter for property remarks.
     *
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }

    /**
     * Getter for property disburstStage.
     *
     * @return Value of property disburstStage.
     */
    public java.lang.String getDisburstStage() {
        return disburstStage;
    }

    /**
     * Setter for property disburstStage.
     *
     * @param disburstStage New value of property disburstStage.
     */
    public void setDisburstStage(java.lang.String disburstStage) {
        this.disburstStage = disburstStage;
    }

    /**
     * Getter for property subLimitAmtRs.
     *
     * @return Value of property subLimitAmtRs.
     */
    public java.lang.Double getSubLimitAmtRs() {
        return subLimitAmtRs;
    }

    /**
     * Setter for property subLimitAmtRs.
     *
     * @param subLimitAmtRs New value of property subLimitAmtRs.
     */
    public void setSubLimitAmtRs(java.lang.Double subLimitAmtRs) {
        this.subLimitAmtRs = subLimitAmtRs;
    }

    /**
     * Getter for property subLimitAmt.
     *
     * @return Value of property subLimitAmt.
     */
    public java.lang.Double getSubLimitAmt() {
        return subLimitAmt;
    }

    /**
     * Setter for property subLimitAmt.
     *
     * @param subLimitAmt New value of property subLimitAmt.
     */
    public void setSubLimitAmt(java.lang.Double subLimitAmt) {
        this.subLimitAmt = subLimitAmt;
    }
}
