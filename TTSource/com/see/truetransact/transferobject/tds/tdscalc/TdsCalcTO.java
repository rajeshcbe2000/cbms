/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TdsCalcTO.java
 * 
 * Created on Wed Feb 09 08:38:34 IST 2005
 */
package com.see.truetransact.transferobject.tds.tdscalc;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TDS_COLLECTED.
 */
public class TdsCalcTO extends TransferObject implements Serializable {

    private String tdsId = "";
    private Date tdsDt = null;
    private String custId = "";
    private String prodType = "";
    private String prodId = "";
    private Double tdsBaseAmt = null;
    private Double tdsAmt = null;
    private String isSubmitted = "";
    private String acc_num = "";
    private Date intPaidDt = null;
    private String tdsRecivedAcNo = "";

    /**
     * Setter/Getter for TDS_ID - table Field
     */
    public void setTdsId(String tdsId) {
        this.tdsId = tdsId;
    }

    public String getTdsId() {
        return tdsId;
    }

    /**
     * Setter/Getter for TDS_DT - table Field
     */
    public void setTdsDt(Date tdsDt) {
        this.tdsDt = tdsDt;
    }

    public Date getTdsDt() {
        return tdsDt;
    }

    /**
     * Setter/Getter for CUST_ID - table Field
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
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
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for TDS_BASE_AMT - table Field
     */
    public void setTdsBaseAmt(Double tdsBaseAmt) {
        this.tdsBaseAmt = tdsBaseAmt;
    }

    public Double getTdsBaseAmt() {
        return tdsBaseAmt;
    }

    /**
     * Setter/Getter for TDS_AMT - table Field
     */
    public void setTdsAmt(Double tdsAmt) {
        this.tdsAmt = tdsAmt;
    }

    public Double getTdsAmt() {
        return tdsAmt;
    }

    /**
     * Setter/Getter for IS_SUBMITTED - table Field
     */
    public void setIsSubmitted(String isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    public String getIsSubmitted() {
        return isSubmitted;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("tdsId");
        return tdsId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("tdsId", tdsId));
        strB.append(getTOString("tdsDt", tdsDt));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("tdsBaseAmt", tdsBaseAmt));
        strB.append(getTOString("tdsAmt", tdsAmt));
        strB.append(getTOString("isSubmitted", isSubmitted));
        strB.append(getTOString("acc_num", acc_num));
        strB.append(getTOString("intPaidDt", intPaidDt));
        strB.append(getTOString("tdsRecivedAcNo", tdsRecivedAcNo));



        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("tdsId", tdsId));
        strB.append(getTOXml("tdsDt", tdsDt));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("tdsBaseAmt", tdsBaseAmt));
        strB.append(getTOXml("tdsAmt", tdsAmt));
        strB.append(getTOXml("isSubmitted", isSubmitted));
        strB.append(getTOXml("acc_num", acc_num));
        strB.append(getTOXml("intPaidDt", intPaidDt));
        strB.append(getTOXml("tdsRecivedAcNo", tdsRecivedAcNo));
        strB.append(getTOXmlEnd());

        return strB.toString();
    }

    /**
     * Getter for property acc_num.
     *
     * @return Value of property acc_num.
     */
    public java.lang.String getAcc_num() {
        return acc_num;
    }

    /**
     * Setter for property acc_num.
     *
     * @param acc_num New value of property acc_num.
     */
    public void setAcc_num(java.lang.String acc_num) {
        this.acc_num = acc_num;
    }

    /**
     * Getter for property intPaidDt.
     *
     * @return Value of property intPaidDt.
     */
    public java.util.Date getIntPaidDt() {
        return intPaidDt;
    }

    /**
     * Setter for property intPaidDt.
     *
     * @param intPaidDt New value of property intPaidDt.
     */
    public void setIntPaidDt(java.util.Date intPaidDt) {
        this.intPaidDt = intPaidDt;
    }

    /**
     * Getter for property tdsRecivedAcNo.
     *
     * @return Value of property tdsRecivedAcNo.
     */
    public java.lang.String getTdsRecivedAcNo() {
        return tdsRecivedAcNo;
    }

    /**
     * Setter for property tdsRecivedAcNo.
     *
     * @param tdsRecivedAcNo New value of property tdsRecivedAcNo.
     */
    public void setTdsRecivedAcNo(java.lang.String tdsRecivedAcNo) {
        this.tdsRecivedAcNo = tdsRecivedAcNo;
    }
    /**
     * Getter for property Acc_num.
     *
     * @return Value of property Acc_num.
     */
//        public java.lang.String getAcc_num() {
//            return Acc_num;
//        }
//        
//        /**
//         * Setter for property Acc_num.
//         * @param Acc_num New value of property Acc_num.
//         */
//        public void setAcc_num(java.lang.String Acc_num) {
//            this.Acc_num = Acc_num;
//        }
//        
}