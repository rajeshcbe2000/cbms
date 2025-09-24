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
package com.see.truetransact.transferobject.transaction.agentCommisionDisbursal;


import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is AGENT_COMMISION_DISBURSAL.
 */
public class agentCommisionDisbursalTO extends TransferObject implements Serializable {

    private String agentId = "";
    private String productId = "";
    private String productDesc = "";
    private Double commisionRate = 0.0;
    private Double commisionAmount = 0.0;
    private Double collectionAmount = 0.0;
    private Double tdsAmount = 0.0;

    public Double getTdsAmount() {
        return tdsAmount;
    }

    public void setTdsAmount(Double tdsAmount) {
        this.tdsAmount = tdsAmount;
    }
    private Date fromDt = null;
    private Date toDt = null;
    private Date instDt = null;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public Double getCollectionAmount() {
        return collectionAmount;
    }

    public void setCollectionAmount(Double collectionAmount) {
        this.collectionAmount = collectionAmount;
    }

    public Double getCommisionAmount() {
        return commisionAmount;
    }

    public void setCommisionAmount(Double commisionAmount) {
        this.commisionAmount = commisionAmount;
    }

    public Double getCommisionRate() {
        return commisionRate;
    }

    public void setCommisionRate(Double commisionRate) {
        this.commisionRate = commisionRate;
    }

    public Date getFromDt() {
        return fromDt;
    }

    public void setFromDt(Date fromDt) {
        this.fromDt = fromDt;
    }

    public Date getInstDt() {
        return instDt;
    }

    public void setInstDt(Date instDt) {
        this.instDt = instDt;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Date getToDt() {
        return toDt;
    }

    public void setToDt(Date toDt) {
        this.toDt = toDt;
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
        strB.append(getTOString("agentId", agentId));
        strB.append(getTOString("productId", productId));
        strB.append(getTOString("productDesc", productDesc));
        strB.append(getTOString("commisionRate", commisionRate));
        strB.append(getTOString("commisionAmount", commisionAmount));
        strB.append(getTOString("collectionAmount", collectionAmount));
        strB.append(getTOString("fromDt", fromDt));
        strB.append(getTOString("toDt", toDt));   
        strB.append(getTOString("tdsAmount", tdsAmount));
        strB.append(getTOStringEnd());
        return strB.toString();
    }    

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));      
        strB.append(getTOXml("agentId", agentId));
        strB.append(getTOXml("productId", productId));
        strB.append(getTOXml("productDesc", productDesc));
        strB.append(getTOXml("commisionRate", commisionRate));
        strB.append(getTOXml("commisionAmount", commisionAmount));
        strB.append(getTOXml("collectionAmount", collectionAmount));
        strB.append(getTOXml("fromDt", fromDt));
        strB.append(getTOXml("toDt", toDt));  
        strB.append(getTOXml("tdsAmount", tdsAmount));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

   
}