/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashTransactionTO.java
 *
 * Created on Thu Aug 12 12:40:27 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.transaction.chargesServiceTax;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CASH_TRANS.
 */
public class ChargesServiceTaxTO extends TransferObject implements Serializable {

    private String prod_type = "";
    private String prod_id = "";
    private String acct_num = "";
    private String particulars = "";
    private Double amount = null;
    private Double service_tax_amt = null;
    private Double total_amt = null;
    private String trans_id = "";
    private Date trans_dt = null;
    private String created_by = "";
    private Date created_dt = null;
    private String status = "";
    private String authorize_status = null;
    private String authorize_by = "";
    private Date authorize_dt = null;
    private String ac_Head = "";
    private String branchCode = "";
    private Double penalAmount = 0.0;

    public String getPenalAcctHead() {
        return penalAcctHead;
    }

    public void setPenalAcctHead(String penalAcctHead) {
        this.penalAcctHead = penalAcctHead;
    }

    public Double getPenalAmount() {
        return penalAmount;
    }

    public void setPenalAmount(Double penalAmount) {
        this.penalAmount = penalAmount;
    }
    private String penalAcctHead = "";

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prod_type", prod_type));
        strB.append(getTOString("prod_id", prod_id));
        strB.append(getTOString("acct_num", acct_num));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("service_tax_amt", service_tax_amt));
        strB.append(getTOString("total_amt", total_amt));
        strB.append(getTOString("trans_id", trans_id));
        strB.append(getTOString("trans_dt", trans_dt));
        strB.append(getTOString("created_by", created_by));
        strB.append(getTOString("created_dt", created_dt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorize_status", authorize_status));
        strB.append(getTOString("authorize_by", authorize_by));
        strB.append(getTOString("ac_Head", ac_Head));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("penalAmount", penalAmount));
        strB.append(getTOString("penalAcctHead", penalAcctHead));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prod_type", prod_type));
        strB.append(getTOXml("prod_id", prod_id));
        strB.append(getTOXml("acct_num", acct_num));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("service_tax_amt", service_tax_amt));
        strB.append(getTOXml("total_amt", total_amt));
        strB.append(getTOXml("trans_id", trans_id));
        strB.append(getTOXml("trans_dt", trans_dt));
        strB.append(getTOXml("created_by", created_by));
        strB.append(getTOXml("created_dt", created_dt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorize_status", authorize_status));
        strB.append(getTOXml("authorize_by", authorize_by));
        strB.append(getTOXml("authorize_dt", authorize_dt));
        strB.append(getTOXml("ac_Head", ac_Head));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("penalAmount", penalAmount));
        strB.append(getTOXml("penalAcctHead", penalAcctHead));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getKeyData() {
        setKeyColumns("transId");
        return trans_id;
    }

    /**
     * Getter for property prod_type.
     *
     * @return Value of property prod_type.
     */
    public java.lang.String getProd_type() {
        return prod_type;
    }

    /**
     * Setter for property prod_type.
     *
     * @param prod_type New value of property prod_type.
     */
    public void setProd_type(java.lang.String prod_type) {
        this.prod_type = prod_type;
    }

    /**
     * Getter for property prod_id.
     *
     * @return Value of property prod_id.
     */
    public java.lang.String getProd_id() {
        return prod_id;
    }

    /**
     * Setter for property prod_id.
     *
     * @param prod_id New value of property prod_id.
     */
    public void setProd_id(java.lang.String prod_id) {
        this.prod_id = prod_id;
    }

    /**
     * Getter for property acct_num.
     *
     * @return Value of property acct_num.
     */
    public java.lang.String getAcct_num() {
        return acct_num;
    }

    /**
     * Setter for property acct_num.
     *
     * @param acct_num New value of property acct_num.
     */
    public void setAcct_num(java.lang.String acct_num) {
        this.acct_num = acct_num;
    }

    /**
     * Getter for property particulars.
     *
     * @return Value of property particulars.
     */
    public java.lang.String getParticulars() {
        return particulars;
    }

    /**
     * Setter for property particulars.
     *
     * @param particulars New value of property particulars.
     */
    public void setParticulars(java.lang.String particulars) {
        this.particulars = particulars;
    }

    /**
     * Getter for property amount.
     *
     * @return Value of property amount.
     */
    public java.lang.Double getAmount() {
        return amount;
    }

    /**
     * Setter for property amount.
     *
     * @param amount New value of property amount.
     */
    public void setAmount(java.lang.Double amount) {
        this.amount = amount;
    }

    /**
     * Getter for property service_tax_amt.
     *
     * @return Value of property service_tax_amt.
     */
    public java.lang.Double getService_tax_amt() {
        return service_tax_amt;
    }

    /**
     * Setter for property service_tax_amt.
     *
     * @param service_tax_amt New value of property service_tax_amt.
     */
    public void setService_tax_amt(java.lang.Double service_tax_amt) {
        this.service_tax_amt = service_tax_amt;
    }

    /**
     * Getter for property total_amt.
     *
     * @return Value of property total_amt.
     */
    public java.lang.Double getTotal_amt() {
        return total_amt;
    }

    /**
     * Setter for property total_amt.
     *
     * @param total_amt New value of property total_amt.
     */
    public void setTotal_amt(java.lang.Double total_amt) {
        this.total_amt = total_amt;
    }

    /**
     * Getter for property trans_id.
     *
     * @return Value of property trans_id.
     */
    public java.lang.String getTrans_id() {
        return trans_id;
    }

    /**
     * Setter for property trans_id.
     *
     * @param trans_id New value of property trans_id.
     */
    public void setTrans_id(java.lang.String trans_id) {
        this.trans_id = trans_id;
    }

    /**
     * Getter for property trans_dt.
     *
     * @return Value of property trans_dt.
     */
    public java.util.Date getTrans_dt() {
        return trans_dt;
    }

    /**
     * Setter for property trans_dt.
     *
     * @param trans_dt New value of property trans_dt.
     */
    public void setTrans_dt(java.util.Date trans_dt) {
        this.trans_dt = trans_dt;
    }

    /**
     * Getter for property created_by.
     *
     * @return Value of property created_by.
     */
    public java.lang.String getCreated_by() {
        return created_by;
    }

    /**
     * Setter for property created_by.
     *
     * @param created_by New value of property created_by.
     */
    public void setCreated_by(java.lang.String created_by) {
        this.created_by = created_by;
    }

    /**
     * Getter for property created_dt.
     *
     * @return Value of property created_dt.
     */
    public java.util.Date getCreated_dt() {
        return created_dt;
    }

    /**
     * Setter for property created_dt.
     *
     * @param created_dt New value of property created_dt.
     */
    public void setCreated_dt(java.util.Date created_dt) {
        this.created_dt = created_dt;
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
     * Getter for property authorize_status.
     *
     * @return Value of property authorize_status.
     */
    public java.lang.String getAuthorize_status() {
        return authorize_status;
    }

    /**
     * Setter for property authorize_status.
     *
     * @param authorize_status New value of property authorize_status.
     */
    public void setAuthorize_status(java.lang.String authorize_status) {
        this.authorize_status = authorize_status;
    }

    /**
     * Getter for property authorize_by.
     *
     * @return Value of property authorize_by.
     */
    public java.lang.String getAuthorize_by() {
        return authorize_by;
    }

    /**
     * Setter for property authorize_by.
     *
     * @param authorize_by New value of property authorize_by.
     */
    public void setAuthorize_by(java.lang.String authorize_by) {
        this.authorize_by = authorize_by;
    }

    /**
     * Getter for property authorize_dt.
     *
     * @return Value of property authorize_dt.
     */
    public java.util.Date getAuthorize_dt() {
        return authorize_dt;
    }

    /**
     * Setter for property authorize_dt.
     *
     * @param authorize_dt New value of property authorize_dt.
     */
    public void setAuthorize_dt(java.util.Date authorize_dt) {
        this.authorize_dt = authorize_dt;
    }

    /**
     * Getter for property ac_Head.
     *
     * @return Value of property ac_Head.
     */
    public java.lang.String getAc_Head() {
        return ac_Head;
    }

    /**
     * Setter for property ac_Head.
     *
     * @param ac_Head New value of property ac_Head.
     */
    public void setAc_Head(java.lang.String ac_Head) {
        this.ac_Head = ac_Head;
    }

    /**
     * Getter for property branchCode.
     *
     * @return Value of property branchCode.
     */
    public java.lang.String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter for property branchCode.
     *
     * @param branchCode New value of property branchCode.
     */
    public void setBranchCode(java.lang.String branchCode) {
        this.branchCode = branchCode;
    }
}