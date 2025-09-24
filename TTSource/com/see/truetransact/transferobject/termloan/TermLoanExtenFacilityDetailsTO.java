/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TermLoanExtenFacilityDetailsTO.java
 *
 * Created on February 20, 2010, 4:49 PM
 */
package com.see.truetransact.transferobject.termloan;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author abi
 */
public class TermLoanExtenFacilityDetailsTO extends TransferObject implements Serializable {

    private String sourceCode = "";
    private String disbursementMode = "";
    private String acctNum = "";
    private String referalCode = "";

    /**
     * Creates a new instance of TermLoanExtenFacilityDetailsTO
     */
    public TermLoanExtenFacilityDetailsTO() {
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("sourceCode", sourceCode));
        strB.append(getTOString("disbursementMode", disbursementMode));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("referalCode", referalCode));
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));

        strB.append(getTOXml("sourceCode", sourceCode));
        strB.append(getTOXml("disbursementMode", disbursementMode));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("referalCode", referalCode));

        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getKeyData() {
        setKeyColumns("acctNum" + KEY_VAL_SEPARATOR);
        return acctNum + KEY_VAL_SEPARATOR;
    }

    /**
     * Getter for property referalCode.
     *
     * @return Value of property referalCode.
     */
    public java.lang.String getReferalCode() {
        return referalCode;
    }

    /**
     * Setter for property referalCode.
     *
     * @param referalCode New value of property referalCode.
     */
    public void setReferalCode(java.lang.String referalCode) {
        this.referalCode = referalCode;
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
     * Getter for property disbursementMode.
     *
     * @return Value of property disbursementMode.
     */
    public java.lang.String getDisbursementMode() {
        return disbursementMode;
    }

    /**
     * Setter for property disbursementMode.
     *
     * @param disbursementMode New value of property disbursementMode.
     */
    public void setDisbursementMode(java.lang.String disbursementMode) {
        this.disbursementMode = disbursementMode;
    }

    /**
     * Getter for property sourceCode.
     *
     * @return Value of property sourceCode.
     */
    public java.lang.String getSourceCode() {
        return sourceCode;
    }

    /**
     * Setter for property sourceCode.
     *
     * @param sourceCode New value of property sourceCode.
     */
    public void setSourceCode(java.lang.String sourceCode) {
        this.sourceCode = sourceCode;
    }
}
