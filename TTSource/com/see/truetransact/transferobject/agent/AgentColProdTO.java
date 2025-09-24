/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AgentColProdTO.java
 * 
 * Created on Tue May 12 11:44:31 IST 2015
 * Created by Sree Krishnan
 */
package com.see.truetransact.transferobject.agent;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is AGENT_MASTER.
 */
public class AgentColProdTO extends TransferObject implements Serializable {

    private String agentId = "";
    private String branchId = "";
    private String prodId = "";
    private String prodType = "";
    private Date lastColDt = null;
    private Date lastIntPaidDt = null;

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

    public Date getLastColDt() {
        return lastColDt;
    }

    public void setLastColDt(Date lastColDt) {
        this.lastColDt = lastColDt;
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
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
    }

   
    public String getKeyData() {
        setKeyColumns("agentId");
        return agentId;
    }

    public Date getLastIntPaidDt() {
        return lastIntPaidDt;
    }

    public void setLastIntPaidDt(Date lastIntPaidDt) {
        this.lastIntPaidDt = lastIntPaidDt;
    }
    

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("agentId", agentId));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("ProdId", prodId));
        strB.append(getTOString("ProdType", prodType));
        strB.append(getTOString("lastColDt", lastColDt));
        strB.append(getTOString("lastIntPaidDt", lastIntPaidDt));
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
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("ProdId", prodId));
        strB.append(getTOXml("ProdType", prodType));
        strB.append(getTOXml("lastColDt", lastColDt));
        strB.append(getTOXml("lastIntPaidDt", lastIntPaidDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}