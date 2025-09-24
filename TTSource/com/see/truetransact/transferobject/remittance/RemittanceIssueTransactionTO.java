/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceIssueUI.java
 * 
 * Created on Fri Sep 17 14:53:13 PDT 2004
 */
package com.see.truetransact.transferobject.remittance;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is REMIT_ISSUE_TRANS.
 */
public class RemittanceIssueTransactionTO extends TransferObject implements Serializable {

    private String batchId = "";
    private Date batchDt = null;
    private String transId = "";
    private String applName = "";
    private String transType = "";
    private Double transAmt = null;
    private String productId = "";
    private String transProductId = "";
    private String debitAcctNo = "";
    private String chequeNo = "";
    private Date chequeDt = null;
    private String status = "";
    private String productType = "";
    private String tokenNo = "";

    /**
     * Setter/Getter for BATCH_ID - table Field
     */
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchId() {
        return batchId;
    }

    /**
     * Setter/Getter for BATCH_DT - table Field
     */
    public void setBatchDt(Date batchDt) {
        this.batchDt = batchDt;
    }

    public Date getBatchDt() {
        return batchDt;
    }

    /**
     * Setter/Getter for TRANS_ID - table Field
     */
    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransId() {
        return transId;
    }

    /**
     * Setter/Getter for APPL_NAME - table Field
     */
    public void setApplName(String applName) {
        this.applName = applName;
    }

    public String getApplName() {
        return applName;
    }

    /**
     * Setter/Getter for TRANS_TYPE - table Field
     */
    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransType() {
        return transType;
    }

    /**
     * Setter/Getter for TRANS_AMT - table Field
     */
    public void setTransAmt(Double transAmt) {
        this.transAmt = transAmt;
    }

    public Double getTransAmt() {
        return transAmt;
    }

    /**
     * Setter/Getter for DEBIT_ACCT_HEAD - table Field
     */
    public void setTransProductId(String transProductId) {
        this.transProductId = transProductId;
    }

    public String getTransProductId() {
        return transProductId;
    }

    /**
     * Setter/Getter for DEBIT_ACCT_NO - table Field
     */
    public void setDebitAcctNo(String debitAcctNo) {
        this.debitAcctNo = debitAcctNo;
    }

    public String getDebitAcctNo() {
        return debitAcctNo;
    }

    /**
     * Setter/Getter for CHEQUE_NO - table Field
     */
    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    /**
     * Setter/Getter for CHEQUE_DT - table Field
     */
    public void setChequeDt(Date chequeDt) {
        this.chequeDt = chequeDt;
    }

    public Date getChequeDt() {
        return chequeDt;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("batchId" + KEY_VAL_SEPARATOR + "transId");
        return batchId + KEY_VAL_SEPARATOR + transId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("batchId", batchId));
        strB.append(getTOString("batchDt", batchDt));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("applName", applName));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("transAmt", transAmt));
        strB.append(getTOString("transProductId", transProductId));
        strB.append(getTOString("productType", productType));
        strB.append(getTOString("debitAcctNo", debitAcctNo));
        strB.append(getTOString("chequeNo", chequeNo));
        strB.append(getTOString("chequeDt", chequeDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("tokenNo", tokenNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("batchId", batchId));
        strB.append(getTOXml("batchDt", batchDt));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("applName", applName));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("transAmt", transAmt));
        strB.append(getTOXml("transProductId", transProductId));
        strB.append(getTOXml("productType", productType));
        strB.append(getTOXml("debitAcctNo", debitAcctNo));
        strB.append(getTOXml("chequeNo", chequeNo));
        strB.append(getTOXml("chequeDt", chequeDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("tokenNo", tokenNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property productType.
     *
     * @return Value of property productType.
     */
    public java.lang.String getProductType() {
        return productType;
    }

    /**
     * Setter for property productType.
     *
     * @param productType New value of property productType.
     */
    public void setProductType(java.lang.String productType) {
        this.productType = productType;
    }

    /**
     * Getter for property productId.
     *
     * @return Value of property productId.
     */
    public java.lang.String getProductId() {
        return productId;
    }

    /**
     * Setter for property productId.
     *
     * @param productId New value of property productId.
     */
    public void setProductId(java.lang.String productId) {
        this.productId = productId;
    }

    /**
     * Getter for property tokenNo.
     *
     * @return Value of property tokenNo.
     */
    public java.lang.String getTokenNo() {
        return tokenNo;
    }

    /**
     * Setter for property tokenNo.
     *
     * @param tokenNo New value of property tokenNo.
     */
    public void setTokenNo(java.lang.String tokenNo) {
        this.tokenNo = tokenNo;
    }
}