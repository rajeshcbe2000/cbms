/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LodgementHundiTO.java
 * 
 * Created on Mon Feb 07 12:36:37 IST 2005
 */
package com.see.truetransact.transferobject.bills.lodgement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BILLS_LODGEMENT_HUNDI.
 */
public class LodgementHundiTO extends TransferObject implements Serializable {

    private String lodgementId = "";
    private Double tenorOfBill = null;
    private String acceptanceBill = "";
    private Date billDueDt = null;
    private Date acceptanceDt = null;
    private String hundiNo = "";
    private Date hundiDt = null;
    private String draweeHundi = "";
    private Double hundiAmount = null;
    private String payableAt = "";
    private String remarks = "";
    private String invoiceNo = "";
    private Date invoiceDt = null;
    private Double invoiceAmount = null;
    private String transCompany = "";
    private String lrNo = "";
    private Date lrDt = null;
    private Double goodsValue = null;
    private String goodsAssigned = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;

    /**
     * Setter/Getter for LODGEMENT_ID - table Field
     */
    public void setLodgementId(String lodgementId) {
        this.lodgementId = lodgementId;
    }

    public String getLodgementId() {
        return lodgementId;
    }

    /**
     * Setter/Getter for TENOR_OF_BILL - table Field
     */
    public void setTenorOfBill(Double tenorOfBill) {
        this.tenorOfBill = tenorOfBill;
    }

    public Double getTenorOfBill() {
        return tenorOfBill;
    }

    /**
     * Setter/Getter for ACCEPTANCE_BILL - table Field
     */
    public void setAcceptanceBill(String acceptanceBill) {
        this.acceptanceBill = acceptanceBill;
    }

    public String getAcceptanceBill() {
        return acceptanceBill;
    }

    /**
     * Setter/Getter for BILL_DUE_DT - table Field
     */
    public void setBillDueDt(Date billDueDt) {
        this.billDueDt = billDueDt;
    }

    public Date getBillDueDt() {
        return billDueDt;
    }

    /**
     * Setter/Getter for ACCEPTANCE_DT - table Field
     */
    public void setAcceptanceDt(Date acceptanceDt) {
        this.acceptanceDt = acceptanceDt;
    }

    public Date getAcceptanceDt() {
        return acceptanceDt;
    }

    /**
     * Setter/Getter for HUNDI_NO - table Field
     */
    public void setHundiNo(String hundiNo) {
        this.hundiNo = hundiNo;
    }

    public String getHundiNo() {
        return hundiNo;
    }

    /**
     * Setter/Getter for HUNDI_DT - table Field
     */
    public void setHundiDt(Date hundiDt) {
        this.hundiDt = hundiDt;
    }

    public Date getHundiDt() {
        return hundiDt;
    }

    /**
     * Setter/Getter for DRAWEE_HUNDI - table Field
     */
    public void setDraweeHundi(String draweeHundi) {
        this.draweeHundi = draweeHundi;
    }

    public String getDraweeHundi() {
        return draweeHundi;
    }

    /**
     * Setter/Getter for HUNDI_AMOUNT - table Field
     */
    public void setHundiAmount(Double hundiAmount) {
        this.hundiAmount = hundiAmount;
    }

    public Double getHundiAmount() {
        return hundiAmount;
    }

    /**
     * Setter/Getter for PAYABLE_AT - table Field
     */
    public void setPayableAt(String payableAt) {
        this.payableAt = payableAt;
    }

    public String getPayableAt() {
        return payableAt;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    /**
     * Setter/Getter for INVOICE_NO - table Field
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * Setter/Getter for INVOICE_DT - table Field
     */
    public void setInvoiceDt(Date invoiceDt) {
        this.invoiceDt = invoiceDt;
    }

    public Date getInvoiceDt() {
        return invoiceDt;
    }

    /**
     * Setter/Getter for INVOICE_AMOUNT - table Field
     */
    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    /**
     * Setter/Getter for TRANS_COMPANY - table Field
     */
    public void setTransCompany(String transCompany) {
        this.transCompany = transCompany;
    }

    public String getTransCompany() {
        return transCompany;
    }

    /**
     * Setter/Getter for LR_NO - table Field
     */
    public void setLrNo(String lrNo) {
        this.lrNo = lrNo;
    }

    public String getLrNo() {
        return lrNo;
    }

    /**
     * Setter/Getter for LR_DT - table Field
     */
    public void setLrDt(Date lrDt) {
        this.lrDt = lrDt;
    }

    public Date getLrDt() {
        return lrDt;
    }

    /**
     * Setter/Getter for GOODS_VALUE - table Field
     */
    public void setGoodsValue(Double goodsValue) {
        this.goodsValue = goodsValue;
    }

    public Double getGoodsValue() {
        return goodsValue;
    }

    /**
     * Setter/Getter for GOODS_ASSIGNED - table Field
     */
    public void setGoodsAssigned(String goodsAssigned) {
        this.goodsAssigned = goodsAssigned;
    }

    public String getGoodsAssigned() {
        return goodsAssigned;
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
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(lodgementId);
        return lodgementId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("lodgementId", lodgementId));
        strB.append(getTOString("tenorOfBill", tenorOfBill));
        strB.append(getTOString("acceptanceBill", acceptanceBill));
        strB.append(getTOString("billDueDt", billDueDt));
        strB.append(getTOString("acceptanceDt", acceptanceDt));
        strB.append(getTOString("hundiNo", hundiNo));
        strB.append(getTOString("hundiDt", hundiDt));
        strB.append(getTOString("draweeHundi", draweeHundi));
        strB.append(getTOString("hundiAmount", hundiAmount));
        strB.append(getTOString("payableAt", payableAt));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("invoiceNo", invoiceNo));
        strB.append(getTOString("invoiceDt", invoiceDt));
        strB.append(getTOString("invoiceAmount", invoiceAmount));
        strB.append(getTOString("transCompany", transCompany));
        strB.append(getTOString("lrNo", lrNo));
        strB.append(getTOString("lrDt", lrDt));
        strB.append(getTOString("goodsValue", goodsValue));
        strB.append(getTOString("goodsAssigned", goodsAssigned));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("lodgementId", lodgementId));
        strB.append(getTOXml("tenorOfBill", tenorOfBill));
        strB.append(getTOXml("acceptanceBill", acceptanceBill));
        strB.append(getTOXml("billDueDt", billDueDt));
        strB.append(getTOXml("acceptanceDt", acceptanceDt));
        strB.append(getTOXml("hundiNo", hundiNo));
        strB.append(getTOXml("hundiDt", hundiDt));
        strB.append(getTOXml("draweeHundi", draweeHundi));
        strB.append(getTOXml("hundiAmount", hundiAmount));
        strB.append(getTOXml("payableAt", payableAt));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("invoiceNo", invoiceNo));
        strB.append(getTOXml("invoiceDt", invoiceDt));
        strB.append(getTOXml("invoiceAmount", invoiceAmount));
        strB.append(getTOXml("transCompany", transCompany));
        strB.append(getTOXml("lrNo", lrNo));
        strB.append(getTOXml("lrDt", lrDt));
        strB.append(getTOXml("goodsValue", goodsValue));
        strB.append(getTOXml("goodsAssigned", goodsAssigned));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}