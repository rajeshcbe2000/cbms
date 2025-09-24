/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveSanctionTO.java
 * 
 * Created on  Mar 29 1:29:28  2010
 */
package com.see.truetransact.transferobject.sysadmin.leavemanagement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OP_AC_PRODUCT.
 */
public class LeaveSanctionTO extends TransferObject implements Serializable {

    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String authorizedStatus = null;
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String leaveApplID = "";
    private String command = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String branch = "";
    private String processType = "";
    private Date appl_dt = null;
    private Date req_from = null;
    private Date req_to = null;
    private String noOfdays = "";
    private String leavePurpose = "";
    private String sanNo = "";
    private Date sanDate = null;
    private String leaveType = "";
    private String tableNoOfdays = "";
    private String empID = "";
    private String tabLeaveType = "";
    private String applSan = "";
    private String slNo = "";
    private String transType = "";
    private String oldSanNo = "";
    private String applStatusBy = "";
    private String sanStatusBy = "";
    private Date applCreatedDt = null;
    private Date sanCreatedDt = null;
    private String sanCreatedBy = "";
    private String applCreatedBy = "";
    private Date applStatusDt = null;
    private Date sanStatusDt = null;
    private Date tblReqFrom = null;
    private Date tblReqTo = null;
    private String withLtc = "";
    private String blockType = "";
    private String withLeaveEncashment = "";
    private String leaveEncashmentDays = "";
    private String paymentType = "";
    private String cboEncashmentData = "";
    private String sanStatus = "";
    private String leaveCancel = "";
    private String appTblStatus = "";
    private String sanTblStatus = "";
    private String remarks = "";
    private String sanAuthority = "";

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
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
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
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("leaveApplID");
        return leaveApplID;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("leaveApplID", leaveApplID));
        strB.append(getTOString("command", command));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("branch", branch));
        strB.append(getTOString("processType", processType));
        strB.append(getTOString("appl_dt", appl_dt));
        strB.append(getTOString("req_from", req_from));
        strB.append(getTOString("req_to", req_to));
        strB.append(getTOString("noOfdays", noOfdays));
        strB.append(getTOString("leavePurpose", leavePurpose));
        strB.append(getTOString("sanNo", sanNo));
        strB.append(getTOString("sanDate", sanDate));
        strB.append(getTOString("leaveType", leaveType));
        strB.append(getTOString("tableNoOfdays", tableNoOfdays));
        strB.append(getTOString("empID", empID));
        strB.append(getTOString("tabLeaveType", tabLeaveType));
        strB.append(getTOString("applSan", applSan));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("oldSanNo", oldSanNo));
        strB.append(getTOString("applStatusBy", applStatusBy));
        strB.append(getTOString("sanStatusBy", sanStatusBy));
        strB.append(getTOString("applCreatedDt", applCreatedDt));
        strB.append(getTOString("sanCreatedDt", sanCreatedDt));
        strB.append(getTOString("sanCreatedBy", sanCreatedBy));
        strB.append(getTOString("applCreatedBy", applCreatedBy));
        strB.append(getTOString("applStatusDt", applStatusDt));
        strB.append(getTOString("sanStatusDt", sanStatusDt));
        strB.append(getTOString("tblReqFrom", tblReqFrom));
        strB.append(getTOString("tblReqTo", tblReqTo));
        strB.append(getTOString("withLtc", withLtc));
        strB.append(getTOString("blockType", blockType));
        strB.append(getTOString("withLeaveEncashment", withLeaveEncashment));
        strB.append(getTOString("leaveEncashmentDays", leaveEncashmentDays));
        strB.append(getTOString("paymentType", paymentType));
        strB.append(getTOString("cboEncashmentData", cboEncashmentData));
        strB.append(getTOString("sanStatus", sanStatus));
        strB.append(getTOString("leaveCancel", leaveCancel));
        strB.append(getTOString("appTblStatus", appTblStatus));
        strB.append(getTOString("sanTblStatus", sanTblStatus));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("sanAuthority", sanAuthority));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("leaveApplID", leaveApplID));
        strB.append(getTOXml("command", command));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("branch", branch));
        strB.append(getTOXml("processType", processType));
        strB.append(getTOXml("appl_dt", appl_dt));
        strB.append(getTOXml("req_from", req_from));
        strB.append(getTOXml("req_to", req_to));
        strB.append(getTOXml("noOfdays", noOfdays));
        strB.append(getTOXml("leavePurpose", leavePurpose));
        strB.append(getTOXml("sanNo", sanNo));
        strB.append(getTOXml("sanDate", sanDate));
        strB.append(getTOXml("leaveType", leaveType));
        strB.append(getTOXml("tableNoOfdays", tableNoOfdays));
        strB.append(getTOXml("empID", empID));
        strB.append(getTOXml("tabLeaveType", tabLeaveType));
        strB.append(getTOXml("applSan", applSan));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("oldSanNo", oldSanNo));
        strB.append(getTOXml("applStatusBy", applStatusBy));
        strB.append(getTOXml("sanStatusBy", sanStatusBy));
        strB.append(getTOXml("applCreatedDt", applCreatedDt));
        strB.append(getTOXml("sanCreatedDt", sanCreatedDt));
        strB.append(getTOXml("sanCreatedBy", sanCreatedBy));
        strB.append(getTOXml("applCreatedBy", applCreatedBy));
        strB.append(getTOXml("applStatusDt", applStatusDt));
        strB.append(getTOXml("sanStatusDt", sanStatusDt));
        strB.append(getTOXml("tblReqFrom", tblReqFrom));
        strB.append(getTOXml("tblReqTo", tblReqTo));
        strB.append(getTOXml("withLtc", withLtc));
        strB.append(getTOXml("blockType", blockType));
        strB.append(getTOXml("withLeaveEncashment", withLeaveEncashment));
        strB.append(getTOXml("leaveEncashmentDays", leaveEncashmentDays));
        strB.append(getTOXml("paymentType", paymentType));
        strB.append(getTOXml("cboEncashmentData", cboEncashmentData));
        strB.append(getTOXml("sanStatus", sanStatus));
        strB.append(getTOXml("leaveCancel", leaveCancel));
        strB.append(getTOXml("appTblStatus", appTblStatus));
        strB.append(getTOXml("sanTblStatus", sanTblStatus));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("sanAuthority", sanAuthority));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property leaveApplID.
     *
     * @return Value of property leaveApplID.
     */
    public java.lang.String getleaveApplID() {
        return leaveApplID;
    }

    /**
     * Setter for property lleaveApplID.
     *
     * @param leaveApplID New value of property leaveApplID.
     */
    public void setleaveApplID(java.lang.String leaveApplID) {
        this.leaveApplID = leaveApplID;
    }

    /**
     * Getter for property command.
     *
     * @return Value of property command.
     */
    public java.lang.String getCommand() {
        return command;
    }

    /**
     * Setter for property command.
     *
     * @param command New value of property command.
     */
    public void setCommand(java.lang.String command) {
        this.command = command;
    }

    /**
     * Getter for property statusBy.
     *
     * @return Value of property statusBy.
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
    }

    /**
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public java.util.Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
    }

    /**
     * Getter for property branch.
     *
     * @return Value of property branch.
     */
    public java.lang.String getBranch() {
        return branch;
    }

    /**
     * Setter for property branch.
     *
     * @param branch New value of property branch.
     */
    public void setBranch(java.lang.String branch) {
        this.branch = branch;
    }

    /**
     * Getter for property processType.
     *
     * @return Value of property processType.
     */
    public java.lang.String getProcessType() {
        return processType;
    }

    /**
     * Setter for property processType.
     *
     * @param processType New value of property processType.
     */
    public void setProcessType(java.lang.String processType) {
        this.processType = processType;
    }

    /**
     * Getter for property appl_dt.
     *
     * @return Value of property appl_dt.
     */
    public java.util.Date getAppl_dt() {
        return appl_dt;
    }

    /**
     * Setter for property appl_dt.
     *
     * @param appl_dt New value of property appl_dt.
     */
    public void setAppl_dt(java.util.Date appl_dt) {
        this.appl_dt = appl_dt;
    }

    /**
     * Getter for property req_from.
     *
     * @return Value of property req_from.
     */
    public java.util.Date getReq_from() {
        return req_from;
    }

    /**
     * Setter for property req_from.
     *
     * @param req_from New value of property req_from.
     */
    public void setReq_from(java.util.Date req_from) {
        this.req_from = req_from;
    }

    /**
     * Getter for property req_to.
     *
     * @return Value of property req_to.
     */
    public java.util.Date getReq_to() {
        return req_to;
    }

    /**
     * Setter for property req_to.
     *
     * @param req_to New value of property req_to.
     */
    public void setReq_to(java.util.Date req_to) {
        this.req_to = req_to;
    }

    /**
     * Getter for property noOfdays.
     *
     * @return Value of property noOfdays.
     */
    public java.lang.String getNoOfdays() {
        return noOfdays;
    }

    /**
     * Setter for property noOfdays.
     *
     * @param noOfdays New value of property noOfdays.
     */
    public void setNoOfdays(java.lang.String noOfdays) {
        this.noOfdays = noOfdays;
    }

    /**
     * Getter for property leavePurpose.
     *
     * @return Value of property leavePurpose.
     */
    public java.lang.String getLeavePurpose() {
        return leavePurpose;
    }

    /**
     * Setter for property leavePurpose.
     *
     * @param leavePurpose New value of property leavePurpose.
     */
    public void setLeavePurpose(java.lang.String leavePurpose) {
        this.leavePurpose = leavePurpose;
    }

    /**
     * Getter for property sanNo.
     *
     * @return Value of property sanNo.
     */
    public java.lang.String getSanNo() {
        return sanNo;
    }

    /**
     * Setter for property sanNo.
     *
     * @param sanNo New value of property sanNo.
     */
    public void setSanNo(java.lang.String sanNo) {
        this.sanNo = sanNo;
    }

    /**
     * Getter for property sanDate.
     *
     * @return Value of property sanDate.
     */
    public java.util.Date getSanDate() {
        return sanDate;
    }

    /**
     * Setter for property sanDate.
     *
     * @param sanDate New value of property sanDate.
     */
    public void setSanDate(java.util.Date sanDate) {
        this.sanDate = sanDate;
    }

    /**
     * Getter for property leaveType.
     *
     * @return Value of property leaveType.
     */
    public java.lang.String getLeaveType() {
        return leaveType;
    }

    /**
     * Setter for property leaveType.
     *
     * @param leaveType New value of property leaveType.
     */
    public void setLeaveType(java.lang.String leaveType) {
        this.leaveType = leaveType;
    }

    /**
     * Getter for property tableNoOfdays.
     *
     * @return Value of property tableNoOfdays.
     */
    public java.lang.String getTableNoOfdays() {
        return tableNoOfdays;
    }

    /**
     * Setter for property tableNoOfdays.
     *
     * @param tableNoOfdays New value of property tableNoOfdays.
     */
    public void setTableNoOfdays(java.lang.String tableNoOfdays) {
        this.tableNoOfdays = tableNoOfdays;
    }

    /**
     * Getter for property empID.
     *
     * @return Value of property empID.
     */
    public java.lang.String getEmpID() {
        return empID;
    }

    /**
     * Setter for property empID.
     *
     * @param empID New value of property empID.
     */
    public void setEmpID(java.lang.String empID) {
        this.empID = empID;
    }

    /**
     * Getter for property tabLeaveType.
     *
     * @return Value of property tabLeaveType.
     */
    public java.lang.String getTabLeaveType() {
        return tabLeaveType;
    }

    /**
     * Setter for property tabLeaveType.
     *
     * @param tabLeaveType New value of property tabLeaveType.
     */
    public void setTabLeaveType(java.lang.String tabLeaveType) {
        this.tabLeaveType = tabLeaveType;
    }

    /**
     * Getter for property applSan.
     *
     * @return Value of property applSan.
     */
    public java.lang.String getApplSan() {
        return applSan;
    }

    /**
     * Setter for property applSan.
     *
     * @param applSan New value of property applSan.
     */
    public void setApplSan(java.lang.String applSan) {
        this.applSan = applSan;
    }

    /**
     * Getter for property slNo.
     *
     * @return Value of property slNo.
     */
    public java.lang.String getSlNo() {
        return slNo;
    }

    /**
     * Setter for property slNo.
     *
     * @param slNo New value of property slNo.
     */
    public void setSlNo(java.lang.String slNo) {
        this.slNo = slNo;
    }

    /**
     * Getter for property transType.
     *
     * @return Value of property transType.
     */
    public java.lang.String getTransType() {
        return transType;
    }

    /**
     * Setter for property transType.
     *
     * @param transType New value of property transType.
     */
    public void setTransType(java.lang.String transType) {
        this.transType = transType;
    }

    /**
     * Getter for property oldSanNo.
     *
     * @return Value of property oldSanNo.
     */
    public java.lang.String getOldSanNo() {
        return oldSanNo;
    }

    /**
     * Setter for property oldSanNo.
     *
     * @param oldSanNo New value of property oldSanNo.
     */
    public void setOldSanNo(java.lang.String oldSanNo) {
        this.oldSanNo = oldSanNo;
    }

    /**
     * Getter for property applStatusBy.
     *
     * @return Value of property applStatusBy.
     */
    public java.lang.String getApplStatusBy() {
        return applStatusBy;
    }

    /**
     * Setter for property applStatusBy.
     *
     * @param applStatusBy New value of property applStatusBy.
     */
    public void setApplStatusBy(java.lang.String applStatusBy) {
        this.applStatusBy = applStatusBy;
    }

    /**
     * Getter for property sanStatusBy.
     *
     * @return Value of property sanStatusBy.
     */
    public java.lang.String getSanStatusBy() {
        return sanStatusBy;
    }

    /**
     * Setter for property sanStatusBy.
     *
     * @param sanStatusBy New value of property sanStatusBy.
     */
    public void setSanStatusBy(java.lang.String sanStatusBy) {
        this.sanStatusBy = sanStatusBy;
    }

    /**
     * Getter for property applCreatedDt.
     *
     * @return Value of property applCreatedDt.
     */
    public java.util.Date getApplCreatedDt() {
        return applCreatedDt;
    }

    /**
     * Setter for property applCreatedDt.
     *
     * @param applCreatedDt New value of property applCreatedDt.
     */
    public void setApplCreatedDt(java.util.Date applCreatedDt) {
        this.applCreatedDt = applCreatedDt;
    }

    /**
     * Getter for property sanCreatedDt.
     *
     * @return Value of property sanCreatedDt.
     */
    public java.util.Date getSanCreatedDt() {
        return sanCreatedDt;
    }

    /**
     * Setter for property sanCreatedDt.
     *
     * @param sanCreatedDt New value of property sanCreatedDt.
     */
    public void setSanCreatedDt(java.util.Date sanCreatedDt) {
        this.sanCreatedDt = sanCreatedDt;
    }

    /**
     * Getter for property sanCreatedBy.
     *
     * @return Value of property sanCreatedBy.
     */
    public java.lang.String getSanCreatedBy() {
        return sanCreatedBy;
    }

    /**
     * Setter for property sanCreatedBy.
     *
     * @param sanCreatedBy New value of property sanCreatedBy.
     */
    public void setSanCreatedBy(java.lang.String sanCreatedBy) {
        this.sanCreatedBy = sanCreatedBy;
    }

    /**
     * Getter for property applCreatedBy.
     *
     * @return Value of property applCreatedBy.
     */
    public java.lang.String getApplCreatedBy() {
        return applCreatedBy;
    }

    /**
     * Setter for property applCreatedBy.
     *
     * @param applCreatedBy New value of property applCreatedBy.
     */
    public void setApplCreatedBy(java.lang.String applCreatedBy) {
        this.applCreatedBy = applCreatedBy;
    }

    /**
     * Getter for property applStatusDt.
     *
     * @return Value of property applStatusDt.
     */
    public java.util.Date getApplStatusDt() {
        return applStatusDt;
    }

    /**
     * Setter for property applStatusDt.
     *
     * @param applStatusDt New value of property applStatusDt.
     */
    public void setApplStatusDt(java.util.Date applStatusDt) {
        this.applStatusDt = applStatusDt;
    }

    /**
     * Getter for property sanStatusDt.
     *
     * @return Value of property sanStatusDt.
     */
    public java.util.Date getSanStatusDt() {
        return sanStatusDt;
    }

    /**
     * Setter for property sanStatusDt.
     *
     * @param sanStatusDt New value of property sanStatusDt.
     */
    public void setSanStatusDt(java.util.Date sanStatusDt) {
        this.sanStatusDt = sanStatusDt;
    }

    /**
     * Getter for property tblReqFrom.
     *
     * @return Value of property tblReqFrom.
     */
    public java.util.Date getTblReqFrom() {
        return tblReqFrom;
    }

    /**
     * Setter for property tblReqFrom.
     *
     * @param tblReqFrom New value of property tblReqFrom.
     */
    public void setTblReqFrom(java.util.Date tblReqFrom) {
        this.tblReqFrom = tblReqFrom;
    }

    /**
     * Getter for property tblReqTo.
     *
     * @return Value of property tblReqTo.
     */
    public java.util.Date getTblReqTo() {
        return tblReqTo;
    }

    /**
     * Setter for property tblReqTo.
     *
     * @param tblReqTo New value of property tblReqTo.
     */
    public void setTblReqTo(java.util.Date tblReqTo) {
        this.tblReqTo = tblReqTo;
    }

    /**
     * Getter for property withLtc.
     *
     * @return Value of property withLtc.
     */
    public java.lang.String getWithLtc() {
        return withLtc;
    }

    /**
     * Setter for property withLtc.
     *
     * @param withLtc New value of property withLtc.
     */
    public void setWithLtc(java.lang.String withLtc) {
        this.withLtc = withLtc;
    }

    /**
     * Getter for property blockType.
     *
     * @return Value of property blockType.
     */
    public java.lang.String getBlockType() {
        return blockType;
    }

    /**
     * Setter for property blockType.
     *
     * @param blockType New value of property blockType.
     */
    public void setBlockType(java.lang.String blockType) {
        this.blockType = blockType;
    }

    /**
     * Getter for property withLeaveEncashment.
     *
     * @return Value of property withLeaveEncashment.
     */
    public java.lang.String getWithLeaveEncashment() {
        return withLeaveEncashment;
    }

    /**
     * Setter for property withLeaveEncashment.
     *
     * @param withLeaveEncashment New value of property withLeaveEncashment.
     */
    public void setWithLeaveEncashment(java.lang.String withLeaveEncashment) {
        this.withLeaveEncashment = withLeaveEncashment;
    }

    /**
     * Getter for property leaveEncashmentDays.
     *
     * @return Value of property leaveEncashmentDays.
     */
    public java.lang.String getLeaveEncashmentDays() {
        return leaveEncashmentDays;
    }

    /**
     * Setter for property leaveEncashmentDays.
     *
     * @param leaveEncashmentDays New value of property leaveEncashmentDays.
     */
    public void setLeaveEncashmentDays(java.lang.String leaveEncashmentDays) {
        this.leaveEncashmentDays = leaveEncashmentDays;
    }

    /**
     * Getter for property paymentType.
     *
     * @return Value of property paymentType.
     */
    public java.lang.String getPaymentType() {
        return paymentType;
    }

    /**
     * Setter for property paymentType.
     *
     * @param payMentType New value of property paymentType.
     */
    public void setPaymentType(java.lang.String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * Getter for property cboEncashmentData.
     *
     * @return Value of property cboEncashmentData.
     */
    public java.lang.String getCboEncashmentData() {
        return cboEncashmentData;
    }

    /**
     * Setter for property cboEncashmentData.
     *
     * @param cboEncashmentData New value of property cboEncashmentData.
     */
    public void setCboEncashmentData(java.lang.String cboEncashmentData) {
        this.cboEncashmentData = cboEncashmentData;
    }

    /**
     * Getter for property sanStatus.
     *
     * @return Value of property sanStatus.
     */
    public java.lang.String getSanStatus() {
        return sanStatus;
    }

    /**
     * Setter for property sanStatus.
     *
     * @param sanStatus New value of property sanStatus.
     */
    public void setSanStatus(java.lang.String sanStatus) {
        this.sanStatus = sanStatus;
    }

    /**
     * Getter for property leaveCancel.
     *
     * @return Value of property leaveCancel.
     */
    public java.lang.String getLeaveCancel() {
        return leaveCancel;
    }

    /**
     * Setter for property leaveCancel.
     *
     * @param leaveCancel New value of property leaveCancel.
     */
    public void setLeaveCancel(java.lang.String leaveCancel) {
        this.leaveCancel = leaveCancel;
    }

    /**
     * Getter for property appTblStatus.
     *
     * @return Value of property appTblStatus.
     */
    public java.lang.String getAppTblStatus() {
        return appTblStatus;
    }

    /**
     * Setter for property appTblStatus.
     *
     * @param appTblStatus New value of property appTblStatus.
     */
    public void setAppTblStatus(java.lang.String appTblStatus) {
        this.appTblStatus = appTblStatus;
    }

    /**
     * Getter for property sanTblStatus.
     *
     * @return Value of property sanTblStatus.
     */
    public java.lang.String getSanTblStatus() {
        return sanTblStatus;
    }

    /**
     * Setter for property sanTblStatus.
     *
     * @param sanTblStatus New value of property sanTblStatus.
     */
    public void setSanTblStatus(java.lang.String sanTblStatus) {
        this.sanTblStatus = sanTblStatus;
    }

    /**
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }

    /**
     * Setter for property remarks.
     *
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }

    /**
     * Getter for property sanAuthority.
     *
     * @return Value of property sanAuthority.
     */
    public java.lang.String getSanAuthority() {
        return sanAuthority;
    }

    /**
     * Setter for property sanAuthority.
     *
     * @param sanAuthority New value of property sanAuthority.
     */
    public void setSanAuthority(java.lang.String sanAuthority) {
        this.sanAuthority = sanAuthority;
    }
}