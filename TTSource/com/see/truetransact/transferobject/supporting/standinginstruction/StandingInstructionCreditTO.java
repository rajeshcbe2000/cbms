/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * StandingInstructionCreditTO.java
 * 
 * Created on Fri Mar 04 16:45:01 IST 2005
 */
package com.see.truetransact.transferobject.supporting.standinginstruction;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is STANDING_INSTRUCTION_CREDIT.
 */
public class StandingInstructionCreditTO extends TransferObject implements Serializable {

    private String siId = "";
    private String prodId = "";
    private String acctNo = "";
    private Double amount = null;
    private String particulars = "";
    private String prodType = "";
    private String status = "";
    private String acHdId = "";
    private String branchId="";

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
    /**
     * Setter/Getter for SI_ID - table Field
     */
    public void setSiId(String siId) {
        this.siId = siId;
    }

    public String getSiId() {
        return siId;
    }

    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for ACCT_NO - table Field
     */
    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctNo() {
        return acctNo;
    }

    /**
     * Setter/Getter for AMOUNT - table Field
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    /**
     * Setter/Getter for PARTICULARS - table Field
     */
    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getParticulars() {
        return particulars;
    }

    /**
     * Setter/Getter for PROD_TYPE - table Field
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdType() {
        return prodType;
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
     * Setter/Getter for AC_HD_ID - table Field
     */
    public void setAcHdId(String acHdId) {
        this.acHdId = acHdId;
    }

    public String getAcHdId() {
        return acHdId;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(siId);
        return siId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("siId", siId));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("acctNo", acctNo));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("status", status));
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("siId", siId));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("acctNo", acctNo));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("branchId", branchId));

        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}