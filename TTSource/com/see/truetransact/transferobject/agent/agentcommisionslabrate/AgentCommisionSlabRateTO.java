/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AgentCommisionSlabRateTO.java
 * 
 * Created on Tue Jul 12 11:44:31 IST 2005
 */
package com.see.truetransact.transferobject.agent.agentcommisionslabrate;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is AGENT_PROD_MAPPING.
 */
public class AgentCommisionSlabRateTO extends TransferObject implements Serializable {

    private String prodType = "";
    private String prodId = "";
    private String agentMachineId = "";
    private String colAchdId = "";
    private String agentId = "";
    private Double commperBank = 0.0;
    private Double commperAcHoldr = 0.0;
    private String commcolAchdId = "";
    private String prodExpcode = "";
    private String tdsacHd = "";
    private String acctIntroCommisionHead = "";
    private Double acctIntroCommision = 0.0;
    private Date fromDate = null;
    private Date toDate = null;
    private Double fromAmt = 0.0;
    private Double toAmt = 0.0;
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private Double tdsAmt = 0.0;
    private Integer slNo = 0;
    private String slabId = "";

    public String getSlabId() {
        return slabId;
    }

    public void setSlabId(String slabId) {
        this.slabId = slabId;
    }

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(Integer slNo) {
        this.slNo = slNo;
    }

    public Double getTdsAmt() {
        return tdsAmt;
    }

    public void setTdsAmt(Double tdsAmt) {
        this.tdsAmt = tdsAmt;
    }

    public Double getAcctIntroCommision() {
        return acctIntroCommision;
    }

    public void setAcctIntroCommision(Double acctIntroCommision) {
        this.acctIntroCommision = acctIntroCommision;
    }

    public String getAcctIntroCommisionHead() {
        return acctIntroCommisionHead;
    }

    public void setAcctIntroCommisionHead(String acctIntroCommisionHead) {
        this.acctIntroCommisionHead = acctIntroCommisionHead;
    }

    public String getColAchdId() {
        return colAchdId;
    }

    public void setColAchdId(String colAchdId) {
        this.colAchdId = colAchdId;
    }

    public String getCommcolAchdId() {
        return commcolAchdId;
    }

    public void setCommcolAchdId(String commcolAchdId) {
        this.commcolAchdId = commcolAchdId;
    }

    public Double getCommperAcHoldr() {
        return commperAcHoldr;
    }

    public void setCommperAcHoldr(Double commperAcHoldr) {
        this.commperAcHoldr = commperAcHoldr;
    }

    public Double getCommperBank() {
        return commperBank;
    }

    public void setCommperBank(Double commperBank) {
        this.commperBank = commperBank;
    }

    public Double getFromAmt() {
        return fromAmt;
    }

    public void setFromAmt(Double fromAmt) {
        this.fromAmt = fromAmt;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public String getProdExpcode() {
        return prodExpcode;
    }

    public void setProdExpcode(String prodExpcode) {
        this.prodExpcode = prodExpcode;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getTdsacHd() {
        return tdsacHd;
    }

    public void setTdsacHd(String tdsacHd) {
        this.tdsacHd = tdsacHd;
    }

    public Double getToAmt() {
        return toAmt;
    }

    public void setToAmt(Double toAmt) {
        this.toAmt = toAmt;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * Setter/Getter for AGENT_ID - table Field
     */
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentId() {
        return agentId;
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
     * Setter/Getter for authorizeStatus - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for authorizeDt - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter/Getter for authorizeBy - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("agentId");
        return agentId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("agentId", agentId));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("agentMachineId", agentMachineId));
        strB.append(getTOString("colAchdId", colAchdId));
        strB.append(getTOString("commperBank", commperBank));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("prodExpcode", prodExpcode));
        strB.append(getTOString("commcolAchdId", commcolAchdId));
        strB.append(getTOString("tdsacHd", tdsacHd));
        strB.append(getTOString("acctIntroCommisionHead", acctIntroCommisionHead));
        strB.append(getTOString("acctIntroCommision", acctIntroCommision));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOString("dpProdType", fromAmt));
        strB.append(getTOString("lastComPaidDt", toAmt));
        strB.append(getTOString("agentMachineId", agentMachineId));
        strB.append(getTOString("tdsAmt", tdsAmt));
        strB.append(getTOString("slabId", slabId));
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
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("agentMachineId", agentMachineId));
        strB.append(getTOXml("colAchdId", colAchdId));
        strB.append(getTOXml("commperBank", commperBank));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("prodExpcode", prodExpcode));
        strB.append(getTOXml("commcolAchdId", commcolAchdId));
        strB.append(getTOXml("tdsacHd", tdsacHd));
        strB.append(getTOXml("acctIntroCommisionHead", acctIntroCommisionHead));
        strB.append(getTOXml("acctIntroCommision", acctIntroCommision));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXml("dpProdType", fromAmt));
        strB.append(getTOXml("lastComPaidDt", toAmt));
        strB.append(getTOXml("agentMachineId", agentMachineId));
        strB.append(getTOXml("tdsAmt", tdsAmt));
        strB.append(getTOXml("slabId", slabId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property agentMachineId.
     *
     * @return Value of property agentMachineId.
     */
    public java.lang.String getAgentMachineId() {
        return agentMachineId;
    }

    /**
     * Setter for property agentMachineId.
     *
     * @param agentMachineId New value of property agentMachineId.
     */
    public void setAgentMachineId(java.lang.String agentMachineId) {
        this.agentMachineId = agentMachineId;
    }
}