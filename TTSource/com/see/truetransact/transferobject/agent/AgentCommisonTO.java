/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * agentCommisonTO.java
 *
 * Created on July 26, 2007, 12:44 PM
 */
package com.see.truetransact.transferobject.agent;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author Bala
 */
public class AgentCommisonTO extends TransferObject implements Serializable {

    private String agentId = "";
    private String commision = "";
    private String commisionDuringThePeriod = "";
    private String commisionForThePeriod = "";
    private Date fromDate = null;
    private Date toDate = null;
    private Double comToOAacc = null;
    private Double comToTDacc = null;
    private Double tdsAmt = null;
    private String transType="";
    /**
     * Creates a new instance of agentCommisonTO
     */
    public AgentCommisonTO() {
    }

    /**
     * Getter for property agentId.
     *
     * @return Value of property agentId.
     */
    public String getAgentId() {
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
     * Getter for property commision.
     *
     * @return Value of property commision.
     */
    public String getCommision() {
        return commision;
    }

    /**
     * Setter for property commision.
     *
     * @param commision New value of property commision.
     */
    public void setCommision(java.lang.String commision) {
        this.commision = commision;
    }

    /**
     * Getter for property commisionDuringThePeriod.
     *
     * @return Value of property commisionDuringThePeriod.
     */
    public String getCommisionDuringThePeriod() {
        return commisionDuringThePeriod;
    }

    /**
     * Setter for property commisionDuringThePeriod.
     *
     * @param commisionDuringThePeriod New value of property
     * commisionDuringThePeriod.
     */
    public void setCommisionDuringThePeriod(java.lang.String commisionDuringThePeriod) {
        this.commisionDuringThePeriod = commisionDuringThePeriod;
    }

    /**
     * Getter for property commisionForThePeriod.
     *
     * @return Value of property commisionForThePeriod.
     */
    public String getCommisionForThePeriod() {
        return commisionForThePeriod;
    }

    /**
     * Setter for property commisionForThePeriod.
     *
     * @param commisionForThePeriod New value of property commisionForThePeriod.
     */
    public void setCommisionForThePeriod(java.lang.String commisionForThePeriod) {
        this.commisionForThePeriod = commisionForThePeriod;
    }

    /**
     * Getter for property fromDate.
     *
     * @return Value of property fromDate.
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * Setter for property fromDate.
     *
     * @param fromDate New value of property fromDate.
     */
    public void setFromDate(java.util.Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Getter for property toDate.
     *
     * @return Value of property toDate.
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * Setter for property toDate.
     *
     * @param toDate New value of property toDate.
     */
    public void setToDate(java.util.Date toDate) {
        this.toDate = toDate;
    }

    public String getKeyData() {
        setKeyColumns("agentId");
        return agentId;
    }

    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("agentId", agentId));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOString("commision", commision));
        strB.append(getTOString("commisionDuringThePeriod", commisionDuringThePeriod));
        strB.append(getTOString("commisionForThePeriod", commisionForThePeriod));
        strB.append(getTOString("comToOAacc", comToOAacc));
        strB.append(getTOString("comToTDacc", comToTDacc));
        strB.append(getTOString("tdsAmt", tdsAmt));
//		strB.append(getTOString("branchId", branchId));
//		strB.append(getTOString("appointedDt", appointedDt));
//		strB.append(getTOString("remarks", remarks));
//		strB.append(getTOString("createdDt", createdDt));
//		strB.append(getTOString("createdBy", createdBy));
//		strB.append(getTOString("status", status));
//		strB.append(getTOString("statusBy", statusBy));
//		strB.append(getTOString("statusDt", statusDt));
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
        strB.append(getTOXml("commision", commision));
        strB.append(getTOXml("commisionDuringThePeriod", commisionDuringThePeriod));
        strB.append(getTOXml("commisionForThePeriod", commisionForThePeriod));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXml("comToOAacc", comToOAacc));
        strB.append(getTOXml("comToTDacc", comToTDacc));
        strB.append(getTOXml("tdsAmt", tdsAmt));

//		strB.append(getTOXml("branchId", branchId));
//		strB.append(getTOXml("appointedDt", appointedDt));
//		strB.append(getTOXml("remarks", remarks));
//		strB.append(getTOXml("createdDt", createdDt));
//		strB.append(getTOXml("authorizedDt", authorizedDt));
//		strB.append(getTOXml("authorizedBy", authorizedBy));
//		strB.append(getTOXml("initiatedBranch", initiatedBranch));
//                strB.append(getTOXml("operativeAcNo", operativeAcNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property comToOAacc.
     *
     * @return Value of property comToOAacc.
     */
    public java.lang.Double getComToOAacc() {
        return comToOAacc;
    }

    /**
     * Setter for property comToOAacc.
     *
     * @param comToOAacc New value of property comToOAacc.
     */
    public void setComToOAacc(java.lang.Double comToOAacc) {
        this.comToOAacc = comToOAacc;
    }

    /**
     * Getter for property comToTDacc.
     *
     * @return Value of property comToTDacc.
     */
    public java.lang.Double getComToTDacc() {
        return comToTDacc;
    }

    /**
     * Setter for property comToTDacc.
     *
     * @param comToTDacc New value of property comToTDacc.
     */
    public void setComToTDacc(java.lang.Double comToTDacc) {
        this.comToTDacc = comToTDacc;
    }

    /**
     * Getter for property tdsAmt.
     *
     * @return Value of property tdsAmt.
     */
    public java.lang.Double getTdsAmt() {
        return tdsAmt;
    }

    /**
     * Setter for property tdsAmt.
     *
     * @param tdsAmt New value of property tdsAmt.
     */
    public void setTdsAmt(java.lang.Double tdsAmt) {
        this.tdsAmt = tdsAmt;
    }
    
     public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
}
