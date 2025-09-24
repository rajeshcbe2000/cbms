/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AgentLeaveDetailsTO.java
 * 
 * Created on Fri Feb 19 16:44:31 IST 2016
 */
package com.see.truetransact.transferobject.agent;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is AGENT_LEAVE_DETAILS.
 */
public class AgentLeaveDetailsTO extends TransferObject implements Serializable {
    private String lAgentId = "";
    private String lAgentName = "";
    private String lRegion = "";
    private String cAgentId = "";
    private String cAgentName = "";
    private String cRegion = "";
    private String txnType = "";
    private Date fromDate = null;
    private Date toDate = null;
    private Date statusDt = null;
    private String statusBy = "";
    private String status = "";
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String authorizedStatus = null;
    private String agentLeaveId = "";

    public String getAgentLeaveId() {
        return agentLeaveId;
    }

    public void setAgentLeaveId(String agentLeaveId) {
        this.agentLeaveId = agentLeaveId;
    }
    private Integer slNo = 0;

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(Integer slNo) {
        this.slNo = slNo;
    }
    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getcAgentId() {
        return cAgentId;
    }

    public void setcAgentId(String cAgentId) {
        this.cAgentId = cAgentId;
    }

    public String getcAgentName() {
        return cAgentName;
    }

    public void setcAgentName(String cAgentName) {
        this.cAgentName = cAgentName;
    }

    public String getcRegion() {
        return cRegion;
    }

    public void setcRegion(String cRegion) {
        this.cRegion = cRegion;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public String getlAgentId() {
        return lAgentId;
    }

    public void setlAgentId(String lAgentId) {
        this.lAgentId = lAgentId;
    }

    public String getlAgentName() {
        return lAgentName;
    }

    public void setlAgentName(String lAgentName) {
        this.lAgentName = lAgentName;
    }

    public String getlRegion() {
        return lRegion;
    }

    public void setlRegion(String lRegion) {
        this.lRegion = lRegion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    
    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("agentId");
        return lAgentId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("lAgentId", lAgentId));
        strB.append(getTOString("lAgentName", lAgentName));
        strB.append(getTOString("lRegion", lRegion));
        strB.append(getTOString("cAgentId", cAgentId));
        strB.append(getTOString("cAgentName", cAgentName));
        strB.append(getTOString("cRegion", cRegion));
        strB.append(getTOString("txnType", txnType));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("agentLeaveId", agentLeaveId));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }
    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("lAgentId", lAgentId));
        strB.append(getTOXml("lAgentName", lAgentName));
        strB.append(getTOXml("lRegion", lRegion));
        strB.append(getTOXml("cAgentId", cAgentId));
        strB.append(getTOXml("cAgentName", cAgentName));
        strB.append(getTOXml("cRegion", cRegion));
        strB.append(getTOXml("txnType", txnType));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("agentLeaveId", agentLeaveId));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}