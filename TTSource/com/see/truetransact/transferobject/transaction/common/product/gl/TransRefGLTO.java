/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TransRefGL.java
 * 
 * Created on Sat Aug 13 16:38:08 IST 2005
 */
package com.see.truetransact.transferobject.transaction.common.product.gl;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TRANS_REF_GL.
 */
public class TransRefGLTO extends TransferObject implements Serializable {

    private String transId = "";
    private String transMode = "";
    private String acHdId = "";
    private String actNum = "";
    private Double inpAmount = null;
    private String inpCurr = "";
    private Double amount = null;
    private Date transDt = null;
    private String transType = "";
    private String instType = "";
    private Date instDt = null;
    private String initTransId = "";
    private String initChannType = "";
    private String particulars = "";
    private String status = "";
    private String instrumentNo1 = "";
    private String instrumentNo2 = "";
    private String prodId = "";
    private String branchId = "";
    private Date statusDt = null;
    private String prodType = "";
    private String initiatedBranch = "";
    private String batchId = "";
    private String ibrHierarchy = "";

    public String getIbrHierarchy() {
        return ibrHierarchy;
    }

    public void setIbrHierarchy(String ibrHierarchy) {
        this.ibrHierarchy = ibrHierarchy;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getInitiatedBranch() {
        return initiatedBranch;
    }

    public void setInitiatedBranch(String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
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
     * Setter/Getter for TRANS_MODE - table Field
     */
    public void setTransMode(String transMode) {
        this.transMode = transMode;
    }

    public String getTransMode() {
        return transMode;
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
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    /**
     * Setter/Getter for INP_AMOUNT - table Field
     */
    public void setInpAmount(Double inpAmount) {
        this.inpAmount = inpAmount;
    }

    public Double getInpAmount() {
        return inpAmount;
    }

    /**
     * Setter/Getter for INP_CURR - table Field
     */
    public void setInpCurr(String inpCurr) {
        this.inpCurr = inpCurr;
    }

    public String getInpCurr() {
        return inpCurr;
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
     * Setter/Getter for TRANS_DT - table Field
     */
    public void setTransDt(Date transDt) {
        this.transDt = transDt;
    }

    public Date getTransDt() {
        return transDt;
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
     * Setter/Getter for INST_TYPE - table Field
     */
    public void setInstType(String instType) {
        this.instType = instType;
    }

    public String getInstType() {
        return instType;
    }

    /**
     * Setter/Getter for INST_DT - table Field
     */
    public void setInstDt(Date instDt) {
        this.instDt = instDt;
    }

    public Date getInstDt() {
        return instDt;
    }

    /**
     * Setter/Getter for INIT_TRANS_ID - table Field
     */
    public void setInitTransId(String initTransId) {
        this.initTransId = initTransId;
    }

    public String getInitTransId() {
        return initTransId;
    }

    /**
     * Setter/Getter for INIT_CHANN_TYPE - table Field
     */
    public void setInitChannType(String initChannType) {
        this.initChannType = initChannType;
    }

    public String getInitChannType() {
        return initChannType;
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
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for INSTRUMENT_NO1 - table Field
     */
    public void setInstrumentNo1(String instrumentNo1) {
        this.instrumentNo1 = instrumentNo1;
    }

    public String getInstrumentNo1() {
        return instrumentNo1;
    }

    /**
     * Setter/Getter for INSTRUMENT_NO2 - table Field
     */
    public void setInstrumentNo2(String instrumentNo2) {
        this.instrumentNo2 = instrumentNo2;
    }

    public String getInstrumentNo2() {
        return instrumentNo2;
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
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
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
     * Setter/Getter for PROD_TYPE - table Field
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdType() {
        return prodType;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("transMode", transMode));
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("inpAmount", inpAmount));
        strB.append(getTOString("inpCurr", inpCurr));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("instType", instType));
        strB.append(getTOString("instDt", instDt));
        strB.append(getTOString("initTransId", initTransId));
        strB.append(getTOString("initChannType", initChannType));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("status", status));
        strB.append(getTOString("instrumentNo1", instrumentNo1));
        strB.append(getTOString("instrumentNo2", instrumentNo2));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("batchId", batchId));
        strB.append(getTOString("ibrHierarchy", ibrHierarchy));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("transMode", transMode));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("inpAmount", inpAmount));
        strB.append(getTOXml("inpCurr", inpCurr));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("instType", instType));
        strB.append(getTOXml("instDt", instDt));
        strB.append(getTOXml("initTransId", initTransId));
        strB.append(getTOXml("initChannType", initChannType));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("instrumentNo1", instrumentNo1));
        strB.append(getTOXml("instrumentNo2", instrumentNo2));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXml("batchId", batchId));
        strB.append(getTOXml("ibrHierarchy", ibrHierarchy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}