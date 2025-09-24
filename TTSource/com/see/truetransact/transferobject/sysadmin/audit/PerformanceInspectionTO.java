/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PerformanceInspectionTO.java
 * 
 * Created on Wed Jun 09 15:32:26 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.sysadmin.audit;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is AUDIT_PERFORMANCE.
 */
public class PerformanceInspectionTO extends TransferObject implements Serializable {

    private String auditId = "";
    private String branchCode = "";
    private Date inspectionCommenced = null;
    private Date inspectionConcluded = null;
    private Date positionAsOn = null;
    private Double noOfManDays = null;
    private String branchRating = "";
    private String inspectingOfficers = "";
    private String otherInfo = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private Date authorizeDt = null;
    private String authorizeBy = "";
    private String authorizeStatus = null;

    /**
     * Setter/Getter for AUDIT_ID - table Field
     */
    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public String getAuditId() {
        return auditId;
    }

    /**
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter/Getter for INSPECTION_COMMENCED - table Field
     */
    public void setInspectionCommenced(Date inspectionCommenced) {
        this.inspectionCommenced = inspectionCommenced;
    }

    public Date getInspectionCommenced() {
        return inspectionCommenced;
    }

    /**
     * Setter/Getter for INSPECTION_CONCLUDED - table Field
     */
    public void setInspectionConcluded(Date inspectionConcluded) {
        this.inspectionConcluded = inspectionConcluded;
    }

    public Date getInspectionConcluded() {
        return inspectionConcluded;
    }

    /**
     * Setter/Getter for POSITION_AS_ON - table Field
     */
    public void setPositionAsOn(Date positionAsOn) {
        this.positionAsOn = positionAsOn;
    }

    public Date getPositionAsOn() {
        return positionAsOn;
    }

    /**
     * Setter/Getter for NO_OF_MAN_DAYS - table Field
     */
    public void setNoOfManDays(Double noOfManDays) {
        this.noOfManDays = noOfManDays;
    }

    public Double getNoOfManDays() {
        return noOfManDays;
    }

    /**
     * Setter/Getter for BRANCH_RATING - table Field
     */
    public void setBranchRating(String branchRating) {
        this.branchRating = branchRating;
    }

    public String getBranchRating() {
        return branchRating;
    }

    /**
     * Setter/Getter for INSPECTING_OFFICERS - table Field
     */
    public void setInspectingOfficers(String inspectingOfficers) {
        this.inspectingOfficers = inspectingOfficers;
    }

    public String getInspectingOfficers() {
        return inspectingOfficers;
    }

    /**
     * Setter/Getter for OTHER_INFO - table Field
     */
    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getOtherInfo() {
        return otherInfo;
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
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(auditId);
        return auditId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("auditId", auditId));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("inspectionCommenced", inspectionCommenced));
        strB.append(getTOString("inspectionConcluded", inspectionConcluded));
        strB.append(getTOString("positionAsOn", positionAsOn));
        strB.append(getTOString("noOfManDays", noOfManDays));
        strB.append(getTOString("branchRating", branchRating));
        strB.append(getTOString("inspectingOfficers", inspectingOfficers));
        strB.append(getTOString("otherInfo", otherInfo));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("auditId", auditId));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("inspectionCommenced", inspectionCommenced));
        strB.append(getTOXml("inspectionConcluded", inspectionConcluded));
        strB.append(getTOXml("positionAsOn", positionAsOn));
        strB.append(getTOXml("noOfManDays", noOfManDays));
        strB.append(getTOXml("branchRating", branchRating));
        strB.append(getTOXml("inspectingOfficers", inspectingOfficers));
        strB.append(getTOXml("otherInfo", otherInfo));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}