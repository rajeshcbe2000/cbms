/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FixedAssetBreakageTO.java
 * 
 * Created on Tue Jan 25 16:35:15 IST 2011
 */
package com.see.truetransact.transferobject.sysadmin.fixedassets;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is FIXED_ASSET_BREAKAGE.
 */
public class FixedAssetBreakageTO extends TransferObject implements Serializable {

    private String faBreakageId = "";
    private String breakageRegion = "";
    private String slNoBreak = "";
    private String branchIdBreak = "";
    private String departBreak = "";
    private String floorBreak = "";
    private String faceValBreak = "";
    private String currValueBreak = "";
    private String breakBatchId = "";
    private Date breakDate = null;
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";

    /**
     * Setter/Getter for FA_BREAKAGE_ID - table Field
     */
    public void setFaBreakageId(String faBreakageId) {
        this.faBreakageId = faBreakageId;
    }

    public String getFaBreakageId() {
        return faBreakageId;
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
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
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
        strB.append(getTOString("faBreakageId", faBreakageId));
        strB.append(getTOString("breakBatchId", breakBatchId));
        strB.append(getTOString("breakDate", breakDate));
        strB.append(getTOString("breakageRegion", breakageRegion));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("faBreakageId", faBreakageId));
        strB.append(getTOXml("breakBatchId", breakBatchId));
        strB.append(getTOXml("breakDate", breakDate));
        strB.append(getTOXml("breakageRegion", breakageRegion));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property slNoBreak.
     *
     * @return Value of property slNoBreak.
     */
    public java.lang.String getSlNoBreak() {
        return slNoBreak;
    }

    /**
     * Setter for property slNoBreak.
     *
     * @param slNoBreak New value of property slNoBreak.
     */
    public void setSlNoBreak(java.lang.String slNoBreak) {
        this.slNoBreak = slNoBreak;
    }

    /**
     * Getter for property branchIdBreak.
     *
     * @return Value of property branchIdBreak.
     */
    public java.lang.String getBranchIdBreak() {
        return branchIdBreak;
    }

    /**
     * Setter for property branchIdBreak.
     *
     * @param branchIdBreak New value of property branchIdBreak.
     */
    public void setBranchIdBreak(java.lang.String branchIdBreak) {
        this.branchIdBreak = branchIdBreak;
    }

    /**
     * Getter for property departBreak.
     *
     * @return Value of property departBreak.
     */
    public java.lang.String getDepartBreak() {
        return departBreak;
    }

    /**
     * Setter for property departBreak.
     *
     * @param departBreak New value of property departBreak.
     */
    public void setDepartBreak(java.lang.String departBreak) {
        this.departBreak = departBreak;
    }

    /**
     * Getter for property floorBreak.
     *
     * @return Value of property floorBreak.
     */
    public java.lang.String getFloorBreak() {
        return floorBreak;
    }

    /**
     * Setter for property floorBreak.
     *
     * @param floorBreak New value of property floorBreak.
     */
    public void setFloorBreak(java.lang.String floorBreak) {
        this.floorBreak = floorBreak;
    }

    /**
     * Getter for property faceValBreak.
     *
     * @return Value of property faceValBreak.
     */
    public java.lang.String getFaceValBreak() {
        return faceValBreak;
    }

    /**
     * Setter for property faceValBreak.
     *
     * @param faceValBreak New value of property faceValBreak.
     */
    public void setFaceValBreak(java.lang.String faceValBreak) {
        this.faceValBreak = faceValBreak;
    }

    /**
     * Getter for property currValueBreak.
     *
     * @return Value of property currValueBreak.
     */
    public java.lang.String getCurrValueBreak() {
        return currValueBreak;
    }

    /**
     * Setter for property currValueBreak.
     *
     * @param currValueBreak New value of property currValueBreak.
     */
    public void setCurrValueBreak(java.lang.String currValueBreak) {
        this.currValueBreak = currValueBreak;
    }

    /**
     * Getter for property breakBatchId.
     *
     * @return Value of property breakBatchId.
     */
    public java.lang.String getBreakBatchId() {
        return breakBatchId;
    }

    /**
     * Setter for property breakBatchId.
     *
     * @param breakBatchId New value of property breakBatchId.
     */
    public void setBreakBatchId(java.lang.String breakBatchId) {
        this.breakBatchId = breakBatchId;
    }

    /**
     * Getter for property breakDate.
     *
     * @return Value of property breakDate.
     */
    public java.util.Date getBreakDate() {
        return breakDate;
    }

    /**
     * Setter for property breakDate.
     *
     * @param breakDate New value of property breakDate.
     */
    public void setBreakDate(java.util.Date breakDate) {
        this.breakDate = breakDate;
    }

    /**
     * Getter for property breakageRegion.
     *
     * @return Value of property breakageRegion.
     */
    public String getBreakageRegion() {
        return breakageRegion;
    }

    /**
     * Setter for property breakageRegion.
     *
     * @param breakageRegion New value of property breakageRegion.
     */
    public void setBreakageRegion(String breakageRegion) {
        this.breakageRegion = breakageRegion;
    }
}