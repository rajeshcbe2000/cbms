/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TermLoanAdditionalSanctionTO.java
 *
 * Created on February 18, 2009, 1:04 PM
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author Administrator
 */
public class TermLoanAdditionalSanctionTO extends TransferObject implements Serializable {

    private String acctNum = null;
    private Date additionalSanctionDt = null;
    private String permittedBy = null;
    private Double additionalLimit = null;
    private String status = null;
    private String slno = "";
    private String authorizeStatus = null;
    private String lienNo = "";

    /**
     * Creates a new instance of TermLoanPeakSanctionTO
     */
    public TermLoanAdditionalSanctionTO() {
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("additionalSanctionDt", additionalSanctionDt));
        strB.append(getTOString("permittedBy", permittedBy));
        strB.append(getTOString("additionalLimit", additionalLimit));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("slno", slno));
        strB.append(getTOString("lienNo", lienNo));
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
        strB.append(getTOXml("additionalSanctionDt", additionalSanctionDt));
        strB.append(getTOXml("permittedBy", permittedBy));
        strB.append(getTOXml("additionalLimit", additionalLimit));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXml("lienNo", lienNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getKeyData() {
        setKeyColumns("acctNum");
        return acctNum;
    }

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
     * Getter for property additionalSanctionDt.
     *
     * @return Value of property additionalSanctionDt.
     */
    public java.util.Date getAdditionalSanctionDt() {
        return additionalSanctionDt;
    }

    /**
     * Setter for property additionalSanctionDt.
     *
     * @param additionalSanctionDt New value of property additionalSanctionDt.
     */
    public void setAdditionalSanctionDt(java.util.Date additionalSanctionDt) {
        this.additionalSanctionDt = additionalSanctionDt;
    }

    /**
     * Getter for property additionalLimit.
     *
     * @return Value of property additionalLimit.
     */
    public java.lang.Double getAdditionalLimit() {
        return additionalLimit;
    }

    /**
     * Setter for property additionalLimit.
     *
     * @param additionalLimit New value of property additionalLimit.
     */
    public void setAdditionalLimit(java.lang.Double additionalLimit) {
        this.additionalLimit = additionalLimit;
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

    /**
     * Getter for property permittedBy.
     *
     * @return Value of property permittedBy.
     */
    public java.lang.String getPermittedBy() {
        return permittedBy;
    }

    /**
     * Setter for property permittedBy.
     *
     * @param permittedBy New value of property permittedBy.
     */
    public void setPermittedBy(java.lang.String permittedBy) {
        this.permittedBy = permittedBy;
    }

    /**
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    /**
     * Getter for property lienNo.
     *
     * @return Value of property lienNo.
     */
    public java.lang.String getLienNo() {
        return lienNo;
    }

    /**
     * Setter for property lienNo.
     *
     * @param lienNo New value of property lienNo.
     */
    public void setLienNo(java.lang.String lienNo) {
        this.lienNo = lienNo;
    }
}
