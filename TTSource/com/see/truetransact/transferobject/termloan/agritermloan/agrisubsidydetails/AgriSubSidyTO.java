/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriSubSidyTO.java
 *
 * Created on June 2, 2009, 2:50 PM
 */
package com.see.truetransact.transferobject.termloan.agritermloan.agrisubsidydetails;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.lang.StringBuffer;
import java.util.Date;

/**
 *
 * @author abi
 */
public class AgriSubSidyTO extends TransferObject implements Serializable {

    private String typeOfSubSidy = "";
    private String depositProdId = "";
    private String depositNo = "";
    private Date subSidyDt = null;
    private String recivedFrom = "";
    private String subSidyAmt = "";
    private String amtAdjusted = "";
    private String amtRefunded = "";
    private Date refundDate = null;
    private String outStandingAmt = "";
    private String acctNum = "";
    private String status = "";
    private String slno = "";

    public String toString() {
        StringBuffer buf = new StringBuffer(getClass().getName());
        buf.append(getTOStringStart(getClass().getName()));
        buf.append(getTOString("typeOfSubSidy", typeOfSubSidy));
        buf.append(getTOString("depositProdId", depositProdId));
        buf.append(getTOString("depositNo", depositNo));
        buf.append(getTOString("subSidyDt", subSidyDt));
        buf.append(getTOString("recivedFrom", recivedFrom));
        buf.append(getTOString("subSidyAmt", subSidyAmt));
        buf.append(getTOString("amtAdjusted", amtAdjusted));
        buf.append(getTOString("amtRefunded", amtRefunded));
        buf.append(getTOString("refundDate", refundDate));
        buf.append(getTOString("outStandingAmt", outStandingAmt));
        buf.append(getTOString("acctNum", acctNum));
        buf.append(getTOString("status", status));
        buf.append(getTOString("slno", slno));
        buf.append(getTOStringEnd());
        return buf.toString();
    }

    public String getTOXml() {

        StringBuffer buf = new StringBuffer(getClass().getName());
        buf.append(getTOXmlStart(getClass().getName()));
        buf.append(getTOXml("typeOfSubSidy", typeOfSubSidy));
        buf.append(getTOXml("depositProdId", depositProdId));
        buf.append(getTOXml("depositNo", depositNo));
        buf.append(getTOXml("subSidyDt", subSidyDt));
        buf.append(getTOXml("recivedFrom", recivedFrom));
        buf.append(getTOXml("subSidyAmt", subSidyAmt));
        buf.append(getTOXml("amtAdjusted", amtAdjusted));
        buf.append(getTOXml("amtRefunded", amtRefunded));
        buf.append(getTOXml("refundDate", refundDate));
        buf.append(getTOXml("outStandingAmt", outStandingAmt));
        buf.append(getTOXml("acctNum", acctNum));
        buf.append(getTOXml("status", status));
        buf.append(getTOXml("slno", slno));
        buf.append(getTOXmlEnd());
        return buf.toString();
    }

    /**
     * Getter for property typeOfSubSidy.
     *
     * @return Value of property typeOfSubSidy.
     */
    public java.lang.String getTypeOfSubSidy() {
        return typeOfSubSidy;
    }

    /**
     * Setter for property typeOfSubSidy.
     *
     * @param typeOfSubSidy New value of property typeOfSubSidy.
     */
    public void setTypeOfSubSidy(java.lang.String typeOfSubSidy) {
        this.typeOfSubSidy = typeOfSubSidy;
    }

    /**
     * Getter for property depositProdId.
     *
     * @return Value of property depositProdId.
     */
    public java.lang.String getDepositProdId() {
        return depositProdId;
    }

    /**
     * Setter for property depositProdId.
     *
     * @param depositProdId New value of property depositProdId.
     */
    public void setDepositProdId(java.lang.String depositProdId) {
        this.depositProdId = depositProdId;
    }

    /**
     * Getter for property depositNo.
     *
     * @return Value of property depositNo.
     */
    public java.lang.String getDepositNo() {
        return depositNo;
    }

    /**
     * Setter for property depositNo.
     *
     * @param depositNo New value of property depositNo.
     */
    public void setDepositNo(java.lang.String depositNo) {
        this.depositNo = depositNo;
    }

    /**
     * Getter for property recivedFrom.
     *
     * @return Value of property recivedFrom.
     */
    public java.lang.String getRecivedFrom() {
        return recivedFrom;
    }

    /**
     * Setter for property recivedFrom.
     *
     * @param recivedFrom New value of property recivedFrom.
     */
    public void setRecivedFrom(java.lang.String recivedFrom) {
        this.recivedFrom = recivedFrom;
    }

    /**
     * Getter for property subSidyAmt.
     *
     * @return Value of property subSidyAmt.
     */
    public java.lang.String getSubSidyAmt() {
        return subSidyAmt;
    }

    /**
     * Setter for property subSidyAmt.
     *
     * @param subSidyAmt New value of property subSidyAmt.
     */
    public void setSubSidyAmt(java.lang.String subSidyAmt) {
        this.subSidyAmt = subSidyAmt;
    }

    /**
     * Getter for property amtAdjusted.
     *
     * @return Value of property amtAdjusted.
     */
    public java.lang.String getAmtAdjusted() {
        return amtAdjusted;
    }

    /**
     * Setter for property amtAdjusted.
     *
     * @param amtAdjusted New value of property amtAdjusted.
     */
    public void setAmtAdjusted(java.lang.String amtAdjusted) {
        this.amtAdjusted = amtAdjusted;
    }

    /**
     * Getter for property amtRefunded.
     *
     * @return Value of property amtRefunded.
     */
    public java.lang.String getAmtRefunded() {
        return amtRefunded;
    }

    /**
     * Setter for property amtRefunded.
     *
     * @param amtRefunded New value of property amtRefunded.
     */
    public void setAmtRefunded(java.lang.String amtRefunded) {
        this.amtRefunded = amtRefunded;
    }

    /**
     * Getter for property outStandingAmt.
     *
     * @return Value of property outStandingAmt.
     */
    public java.lang.String getOutStandingAmt() {
        return outStandingAmt;
    }

    /**
     * Setter for property outStandingAmt.
     *
     * @param outStandingAmt New value of property outStandingAmt.
     */
    public void setOutStandingAmt(java.lang.String outStandingAmt) {
        this.outStandingAmt = outStandingAmt;
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
     * Getter for property refundDate.
     *
     * @return Value of property refundDate.
     */
    public java.util.Date getRefundDate() {
        return refundDate;
    }

    /**
     * Setter for property refundDate.
     *
     * @param refundDate New value of property refundDate.
     */
    public void setRefundDate(java.util.Date refundDate) {
        this.refundDate = refundDate;
    }

    /**
     * Getter for property subSidyDt.
     *
     * @return Value of property subSidyDt.
     */
    public java.util.Date getSubSidyDt() {
        return subSidyDt;
    }

    /**
     * Setter for property subSidyDt.
     *
     * @param subSidyDt New value of property subSidyDt.
     */
    public void setSubSidyDt(java.util.Date subSidyDt) {
        this.subSidyDt = subSidyDt;
    }
}
