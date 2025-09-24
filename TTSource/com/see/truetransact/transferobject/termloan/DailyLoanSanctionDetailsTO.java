/*
 * DailyLoanSanctionDetailsTO.java
 *
 * Created on December 18, 2012, 9:58 AM
 */
package com.see.truetransact.transferobject.termloan;

/**
 *
 * @author admin
 */
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * .
 *
 * TermLoanSecurityTO.java
 *
 * Created on Wed Mar 16 16:19:00 IST 2005
 */
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_SECURITY_DETAILS.
 */
public class DailyLoanSanctionDetailsTO extends TransferObject implements Serializable {

    private String loanAcctNum = "";
    private String accountNum = "";
    private String directPayment = "";
    private String prodType = "";
    private String prodId = "";
    private String acctHead = "";
    private Double loanPeriod = null;
    private String agentId = "";
    private String status = "";
    private String maxPeriodChar = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("loanAcctNum" + KEY_VAL_SEPARATOR + "agentId");
        return loanAcctNum + KEY_VAL_SEPARATOR + agentId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//		strB.append (getTOStringKey(getKeyData()));
        strB.append(getTOString("loanAcctNum", loanAcctNum));
        strB.append(getTOString("accountNum", accountNum));
        strB.append(getTOString("directPayment", directPayment));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("acctHead", acctHead));
        strB.append(getTOString("loanPeriod", loanPeriod));
        strB.append(getTOString("agentId", agentId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("maxPeriodChar", maxPeriodChar));

        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//		strB.append (getTOXmlKey(getKeyData()));
        strB.append(getTOXml("loanAcctNum", loanAcctNum));
        strB.append(getTOXml("accountNum", accountNum));
        strB.append(getTOXml("directPayment", directPayment));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("acctHead", acctHead));
        strB.append(getTOXml("loanPeriod", loanPeriod));
        strB.append(getTOXml("agentId", agentId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("maxPeriodChar", maxPeriodChar));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property loanAcctNum.
     *
     * @return Value of property loanAcctNum.
     */
    public java.lang.String getLoanAcctNum() {
        return loanAcctNum;
    }

    /**
     * Setter for property loanAcctNum.
     *
     * @param loanAcctNum New value of property loanAcctNum.
     */
    public void setLoanAcctNum(java.lang.String loanAcctNum) {
        this.loanAcctNum = loanAcctNum;
    }

    /**
     * Getter for property accountNum.
     *
     * @return Value of property accountNum.
     */
    public java.lang.String getAccountNum() {
        return accountNum;
    }

    /**
     * Setter for property accountNum.
     *
     * @param accountNum New value of property accountNum.
     */
    public void setAccountNum(java.lang.String accountNum) {
        this.accountNum = accountNum;
    }

    /**
     * Getter for property directPayment.
     *
     * @return Value of property directPayment.
     */
    public java.lang.String getDirectPayment() {
        return directPayment;
    }

    /**
     * Setter for property directPayment.
     *
     * @param directPayment New value of property directPayment.
     */
    public void setDirectPayment(java.lang.String directPayment) {
        this.directPayment = directPayment;
    }

    /**
     * Getter for property prodType.
     *
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }

    /**
     * Setter for property prodType.
     *
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }

    /**
     * Getter for property prodId.
     *
     * @return Value of property prodId.
     */
    public java.lang.String getProdId() {
        return prodId;
    }

    /**
     * Setter for property prodId.
     *
     * @param prodId New value of property prodId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }

    /**
     * Getter for property acctHead.
     *
     * @return Value of property acctHead.
     */
    public java.lang.String getAcctHead() {
        return acctHead;
    }

    /**
     * Setter for property acctHead.
     *
     * @param acctHead New value of property acctHead.
     */
    public void setAcctHead(java.lang.String acctHead) {
        this.acctHead = acctHead;
    }

    /**
     * Getter for property loanPeriod.
     *
     * @return Value of property loanPeriod.
     */
    public java.lang.Double getLoanPeriod() {
        return loanPeriod;
    }

    /**
     * Setter for property loanPeriod.
     *
     * @param loanPeriod New value of property loanPeriod.
     */
    public void setLoanPeriod(java.lang.Double loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    /**
     * Getter for property agentId.
     *
     * @return Value of property agentId.
     */
    public java.lang.String getAgentId() {
        return agentId;
    }

    /**
     * Setter for property agentId.
     *
     * @param agentId New value of property agentId.
     */
    public void setAgentId(java.lang.String agentId) {
        this.agentId = agentId;
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
     * Getter for property maxPeriodChar.
     *
     * @return Value of property maxPeriodChar.
     */
    public java.lang.String getMaxPeriodChar() {
        return maxPeriodChar;
    }

    /**
     * Setter for property maxPeriodChar.
     *
     * @param maxPeriodChar New value of property maxPeriodChar.
     */
    public void setMaxPeriodChar(java.lang.String maxPeriodChar) {
        this.maxPeriodChar = maxPeriodChar;
    }
}
