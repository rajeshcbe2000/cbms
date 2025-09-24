/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TermLoanInstitGuarantorTO.java
 *
 * Created on March 15, 2010, 5:06 PM
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author abi
 */
public class TermLoanInstitGuarantorTO extends TransferObject implements Serializable {

    /**
     * Creates a new instance of TermLoanInstitGuarantorTO
     */
    private String pliName = "";
    private String pliBranch = "";
    private String guaratNo = "";
    private Date guaranDate = null;
    private Date guaranPeriodFrom = null;
    private Date guaranPeriodTo = null;
    private String guaranCommision = "";
    private String guaranStatus = "";
    private Date statusDate = null;
    private String guarnRemarks = "";
    private String acctNum = "";
    private String slno = "";
    private String status = "";
    private String guarntConstitution = "";

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("pliName", pliName));
        strB.append(getTOString("pliBranch", pliBranch));
        strB.append(getTOString("guaratNo", guaratNo));
        strB.append(getTOString("guaranDate", guaranDate));
        strB.append(getTOString("guaranPeriodFrom", guaranPeriodFrom));
        strB.append(getTOString("guaranPeriodTo", guaranPeriodTo));
        strB.append(getTOString("guaranCommision", guaranCommision));
        strB.append(getTOString("guaranStatus", guaranStatus));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("guarnRemarks", guarnRemarks));
        strB.append(getTOString("guarntConstitution", guarntConstitution));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("slno", slno));
        strB.append(getTOString("status", status));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("acctNum" + KEY_VAL_SEPARATOR + "slno");
        return acctNum + KEY_VAL_SEPARATOR + slno;
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("pliName", pliName));
        strB.append(getTOXml("pliBranch", pliBranch));
        strB.append(getTOXml("guaratNo", guaratNo));
        strB.append(getTOXml("guaranDate", guaranDate));
        strB.append(getTOXml("guaranPeriodFrom", guaranPeriodFrom));
        strB.append(getTOXml("guaranPeriodTo", guaranPeriodTo));
        strB.append(getTOXml("guaranCommision", guaranCommision));
        strB.append(getTOXml("guaranStatus", guaranStatus));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("guarnRemarks", guarnRemarks));
        strB.append(getTOXml("guarntConstitution", guarntConstitution));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXml("status", status));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property guarnRemarks.
     *
     * @return Value of property guarnRemarks.
     */
    public java.lang.String getGuarnRemarks() {
        return guarnRemarks;
    }

    /**
     * Setter for property guarnRemarks.
     *
     * @param guarnRemarks New value of property guarnRemarks.
     */
    public void setGuarnRemarks(java.lang.String guarnRemarks) {
        this.guarnRemarks = guarnRemarks;
    }

    /**
     * Getter for property statusDate.
     *
     * @return Value of property statusDate.
     */
    public java.util.Date getStatusDate() {
        return statusDate;
    }

    /**
     * Setter for property statusDate.
     *
     * @param statusDate New value of property statusDate.
     */
    public void setStatusDate(java.util.Date statusDate) {
        this.statusDate = statusDate;
    }

    /**
     * Getter for property guaranStatus.
     *
     * @return Value of property guaranStatus.
     */
    public java.lang.String getGuaranStatus() {
        return guaranStatus;
    }

    /**
     * Setter for property guaranStatus.
     *
     * @param guaranStatus New value of property guaranStatus.
     */
    public void setGuaranStatus(java.lang.String guaranStatus) {
        this.guaranStatus = guaranStatus;
    }

    /**
     * Getter for property guaranCommision.
     *
     * @return Value of property guaranCommision.
     */
    public java.lang.String getGuaranCommision() {
        return guaranCommision;
    }

    /**
     * Setter for property guaranCommision.
     *
     * @param guaranCommision New value of property guaranCommision.
     */
    public void setGuaranCommision(java.lang.String guaranCommision) {
        this.guaranCommision = guaranCommision;
    }

    /**
     * Getter for property guaranDate.
     *
     * @return Value of property guaranDate.
     */
    public java.util.Date getGuaranDate() {
        return guaranDate;
    }

    /**
     * Setter for property guaranDate.
     *
     * @param guaranDate New value of property guaranDate.
     */
    public void setGuaranDate(java.util.Date guaranDate) {
        this.guaranDate = guaranDate;
    }

    /**
     * Getter for property guaratNo.
     *
     * @return Value of property guaratNo.
     */
    public java.lang.String getGuaratNo() {
        return guaratNo;
    }

    /**
     * Setter for property guaratNo.
     *
     * @param guaratNo New value of property guaratNo.
     */
    public void setGuaratNo(java.lang.String guaratNo) {
        this.guaratNo = guaratNo;
    }

    /**
     * Getter for property pliBranch.
     *
     * @return Value of property pliBranch.
     */
    public java.lang.String getPliBranch() {
        return pliBranch;
    }

    /**
     * Setter for property pliBranch.
     *
     * @param pliBranch New value of property pliBranch.
     */
    public void setPliBranch(java.lang.String pliBranch) {
        this.pliBranch = pliBranch;
    }

    /**
     * Getter for property pliName.
     *
     * @return Value of property pliName.
     */
    public java.lang.String getPliName() {
        return pliName;
    }

    /**
     * Setter for property pliName.
     *
     * @param pliName New value of property pliName.
     */
    public void setPliName(java.lang.String pliName) {
        this.pliName = pliName;
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
     * Getter for property guaranPeriodFrom.
     *
     * @return Value of property guaranPeriodFrom.
     */
    public java.util.Date getGuaranPeriodFrom() {
        return guaranPeriodFrom;
    }

    /**
     * Setter for property guaranPeriodFrom.
     *
     * @param guaranPeriodFrom New value of property guaranPeriodFrom.
     */
    public void setGuaranPeriodFrom(java.util.Date guaranPeriodFrom) {
        this.guaranPeriodFrom = guaranPeriodFrom;
    }

    /**
     * Getter for property guaranPeriodTo.
     *
     * @return Value of property guaranPeriodTo.
     */
    public java.util.Date getGuaranPeriodTo() {
        return guaranPeriodTo;
    }

    /**
     * Setter for property guaranPeriodTo.
     *
     * @param guaranPeriodTo New value of property guaranPeriodTo.
     */
    public void setGuaranPeriodTo(java.util.Date guaranPeriodTo) {
        this.guaranPeriodTo = guaranPeriodTo;
    }

    /**
     * Getter for property guarntConstitution.
     *
     * @return Value of property guarntConstitution.
     */
    public java.lang.String getGuarntConstitution() {
        return guarntConstitution;
    }

    /**
     * Setter for property guarntConstitution.
     *
     * @param guarntConstitution New value of property guarntConstitution.
     */
    public void setGuarntConstitution(java.lang.String guarntConstitution) {
        this.guarntConstitution = guarntConstitution;
    }
}
