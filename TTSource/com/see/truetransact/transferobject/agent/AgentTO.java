/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AgentTO.java
 * 
 * Created on Tue Jul 12 11:44:31 IST 2005
 */
package com.see.truetransact.transferobject.agent;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is AGENT_MASTER.
 */
public class AgentTO extends TransferObject implements Serializable {

    private String agentId = "";
    private String branchId = "";
    private Date appointedDt = null;
    private String remarks = "";
    private String agentType = "";//Added By Revathi.L
    private Date createdDt = null;
    private String createdBy = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String initiatedBranch = "";
    private String operativeAcNo = "";
    private String collSuspProdtype = "";
    private String collSuspProdID = "";
    private String collSuspACNum = "";
    private String dpacnum = "";
    private String dpProdId = "";
    private String dpProdType = "";
    private Date lastComPaidDt = null;
    private String agentMachineId = "";
    private String region = "";
    private String txnType = "";

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }
//        private String OAccId ="";
//        private String DepsoitNo ="";
//        private String ProdIdlName ="";
//        private String depositProdId ="";
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

    /**
     * Setter/Getter for APPOINTED_DT - table Field
     */
    public void setAppointedDt(Date appointedDt) {
        this.appointedDt = appointedDt;
    }

    public Date getAppointedDt() {
        return appointedDt;
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
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter/Getter for INITIATED_BRANCH - table Field
     */
    public void setInitiatedBranch(String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }

    public String getInitiatedBranch() {
        return initiatedBranch;
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
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("appointedDt", appointedDt));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("agentType", agentType));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("operativeAcNo", operativeAcNo));
        strB.append(getTOString("collSuspProdtype", collSuspProdtype));
        strB.append(getTOString("collSuspProdID", collSuspProdID));
        strB.append(getTOString("collSuspACNum", collSuspACNum));
        strB.append(getTOString("dpacnum", dpacnum));
        strB.append(getTOString("dpProdId", dpProdId));
        strB.append(getTOString("dpProdType", dpProdType));
        strB.append(getTOString("lastComPaidDt", lastComPaidDt));
        strB.append(getTOString("agentMachineId", agentMachineId));
        strB.append(getTOString("region", region));
        strB.append(getTOString("txnType", txnType));
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
        strB.append(getTOXml("appointedDt", appointedDt));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("agentType", agentType));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXml("operativeAcNo", operativeAcNo));
        strB.append(getTOXml("collSuspProdtype", collSuspProdtype));
        strB.append(getTOXml("collSuspProdID", collSuspProdID));
        strB.append(getTOXml("collSuspACNum", collSuspACNum));
        strB.append(getTOXml("dpacnum", dpacnum));
        strB.append(getTOXml("dpProdId", dpProdId));
        strB.append(getTOXml("dpProdType", dpProdType));
        strB.append(getTOXml("lastComPaidDt", lastComPaidDt));
        strB.append(getTOXml("agentMachineId", agentMachineId));
        strB.append(getTOXml("region", region));
        strB.append(getTOXml("txnType", txnType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property operativeAcNo.
     *
     * @return Value of property operativeAcNo.
     */
    public java.lang.String getOperativeAcNo() {
        return operativeAcNo;
    }

    /**
     * Setter for property operativeAcNo.
     *
     * @param operativeAcNo New value of property operativeAcNo.
     */
    public void setOperativeAcNo(java.lang.String operativeAcNo) {
        this.operativeAcNo = operativeAcNo;
    }

    /**
     * Getter for property collSuspProdtype.
     *
     * @return Value of property collSuspProdtype.
     */
    public java.lang.String getCollSuspProdtype() {
        return collSuspProdtype;
    }

    /**
     * Setter for property collSuspProdtype.
     *
     * @param collSuspProdtype New value of property collSuspProdtype.
     */
    public void setCollSuspProdtype(java.lang.String collSuspProdtype) {
        this.collSuspProdtype = collSuspProdtype;
    }

    /**
     * Getter for property collSuspProdID.
     *
     * @return Value of property collSuspProdID.
     */
    public java.lang.String getCollSuspProdID() {
        return collSuspProdID;
    }

    /**
     * Setter for property collSuspProdID.
     *
     * @param collSuspProdID New value of property collSuspProdID.
     */
    public void setCollSuspProdID(java.lang.String collSuspProdID) {
        this.collSuspProdID = collSuspProdID;
    }

    /**
     * Getter for property collSuspACNum.
     *
     * @return Value of property collSuspACNum.
     */
    public java.lang.String getCollSuspACNum() {
        return collSuspACNum;
    }

    /**
     * Setter for property collSuspACNum.
     *
     * @param collSuspACNum New value of property collSuspACNum.
     */
    public void setCollSuspACNum(java.lang.String collSuspACNum) {
        this.collSuspACNum = collSuspACNum;
    }

    /**
     * Getter for property dpacnum.
     *
     * @return Value of property dpacnum.
     */
    public java.lang.String getDpacnum() {
        return dpacnum;
    }

    /**
     * Setter for property dpacnum.
     *
     * @param dpacnum New value of property dpacnum.
     */
    public void setDpacnum(java.lang.String dpacnum) {
        this.dpacnum = dpacnum;
    }

    /**
     * Getter for property lastComPaidDt.
     *
     * @return Value of property lastComPaidDt.
     */
    public java.util.Date getLastComPaidDt() {
        return lastComPaidDt;
    }

    /**
     * Setter for property lastComPaidDt.
     *
     * @param lastComPaidDt New value of property lastComPaidDt.
     */
    public void setLastComPaidDt(java.util.Date lastComPaidDt) {
        this.lastComPaidDt = lastComPaidDt;
    }

    /**
     * Getter for property dpProdId.
     *
     * @return Value of property dpProdId.
     */
    public java.lang.String getDpProdId() {
        return dpProdId;
    }

    /**
     * Setter for property dpProdId.
     *
     * @param dpProdId New value of property dpProdId.
     */
    public void setDpProdId(java.lang.String dpProdId) {
        this.dpProdId = dpProdId;
    }

    /**
     * Getter for property dpProdType.
     *
     * @return Value of property dpProdType.
     */
    public java.lang.String getDpProdType() {
        return dpProdType;
    }

    /**
     * Setter for property dpProdType.
     *
     * @param dpProdType New value of property dpProdType.
     */
    public void setDpProdType(java.lang.String dpProdType) {
        this.dpProdType = dpProdType;
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

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }
    
//        /**
//         * Getter for property OAccId.
//         * @return Value of property OAccId.
//         */
//        public java.lang.String getOAccId() {
//            return OAccId;
//        }
//        
//        /**
//         * Setter for property OAccId.
//         * @param OAccId New value of property OAccId.
//         */
//        public void setOAccId(java.lang.String OAccId) {
//            this.OAccId = OAccId;
//        }
//        /**
//         * Getter for property OAccId.
//         * @return Value of property OAccId.
//         */
//        public java.lang.String getOAccId() {
//            return OAccId;
//        }
//        
//        /**
//         * Setter for property OAccId.
//         * @param OAccId New value of property OAccId.
//         */
//        public void setOAccId(java.lang.String OAccId) {
//            this.OAccId = OAccId;
//        }
//        
//        /**
//         * Getter for property DepsoitNo.
//         * @return Value of property DepsoitNo.
//         */
//        public java.lang.String getDepsoitNo() {
//            return DepsoitNo;
//        }
//        
//        /**
//         * Setter for property DepsoitNo.
//         * @param DepsoitNo New value of property DepsoitNo.
//         */
//        public void setDepsoitNo(java.lang.String DepsoitNo) {
//            this.DepsoitNo = DepsoitNo;
//        }
    /**
     * Getter for property ProdIdlName.
     *
     * @return Value of property ProdIdlName.
     */
//        public java.lang.String getProdIdlName() {
//            return ProdIdlName;
//        }
//        
//        /**
//         * Setter for property ProdIdlName.
//         * @param ProdIdlName New value of property ProdIdlName.
//         */
//        public void setProdIdlName(java.lang.String ProdIdlName) {
//            this.ProdIdlName = ProdIdlName;
//        }
//        
//        /**
//         * Getter for property depositProdId.
//         * @return Value of property depositProdId.
//         */
//        public java.lang.String getDepositProdId() {
//            return depositProdId;
//        }
//        
//        /**
//         * Setter for property depositProdId.
//         * @param depositProdId New value of property depositProdId.
//         */
//        public void setDepositProdId(java.lang.String depositProdId) {
//            this.depositProdId = depositProdId;
//        }
}