/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FixedAssetsMovementTO.java
 * 
 * Created on Tue Jan 18 17:22:43 IST 2011
 */
package com.see.truetransact.transferobject.sysadmin.fixedassets;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is FIXED_ASSET_MOVEMENT.
 */
public class FixedAssetMovementTO extends TransferObject implements Serializable {

    private String faMoveId = "";
    private String moveBatchId = "";
    private String sourceBranchId = "";
    private String destinationBranchId = "";
    private String sourceDepartment = "";
    private String destinationDepartment = "";
    private String sourceFloor = "";
    private String destinationFloor = "";
    private String slNo = "";
    private String faceValue = "";
    private String currentValue = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private Date moveDate = null;

    /**
     * Setter/Getter for FA_MOVE_ID - table Field
     */
    public void setFaMoveId(String faMoveId) {
        this.faMoveId = faMoveId;
    }

    public String getFaMoveId() {
        return faMoveId;
    }

    /**
     * Setter/Getter for DESTINATION_BRANCH_ID - table Field
     */
    public void setDestinationBranchId(String destinationBranchId) {
        this.destinationBranchId = destinationBranchId;
    }

    public String getDestinationBranchId() {
        return destinationBranchId;
    }

    /**
     * Setter/Getter for DESTINATION_DEPARTMENT - table Field
     */
    public void setDestinationDepartment(String destinationDepartment) {
        this.destinationDepartment = destinationDepartment;
    }

    public String getDestinationDepartment() {
        return destinationDepartment;
    }

    /**
     * Setter/Getter for DESTINATION_FLOOR - table Field
     */
    public void setDestinationFloor(String destinationFloor) {
        this.destinationFloor = destinationFloor;
    }

    public String getDestinationFloor() {
        return destinationFloor;
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
        strB.append(getTOString("faMoveId", faMoveId));
        strB.append(getTOString("moveBatchId", moveBatchId));
        strB.append(getTOString("moveDate", moveDate));
        strB.append(getTOString("sourceBranchId", sourceBranchId));
        strB.append(getTOString("sourceDepartment", sourceDepartment));
        strB.append(getTOString("sourceFloor", sourceFloor));
        strB.append(getTOString("destinationBranchId", destinationBranchId));
        strB.append(getTOString("destinationDepartment", destinationDepartment));
        strB.append(getTOString("destinationFloor", destinationFloor));
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
        strB.append(getTOXml("faMoveId", faMoveId));
        strB.append(getTOXml("moveBatchId", moveBatchId));
        strB.append(getTOXml("moveDate", moveDate));
        strB.append(getTOXml("sourceBranchId", sourceBranchId));
        strB.append(getTOXml("sourceDepartment", sourceDepartment));
        strB.append(getTOXml("sourceFloor", sourceFloor));
        strB.append(getTOXml("destinationBranchId", destinationBranchId));
        strB.append(getTOXml("destinationDepartment", destinationDepartment));
        strB.append(getTOXml("destinationFloor", destinationFloor));
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
     * Getter for property moveBatchId.
     *
     * @return Value of property moveBatchId.
     */
    public java.lang.String getMoveBatchId() {
        return moveBatchId;
    }

    /**
     * Setter for property moveBatchId.
     *
     * @param moveBatchId New value of property moveBatchId.
     */
    public void setMoveBatchId(java.lang.String moveBatchId) {
        this.moveBatchId = moveBatchId;
    }

    /**
     * Getter for property moveDate.
     *
     * @return Value of property moveDate.
     */
    public java.util.Date getMoveDate() {
        return moveDate;
    }

    /**
     * Setter for property moveDate.
     *
     * @param moveDate New value of property moveDate.
     */
    public void setMoveDate(java.util.Date moveDate) {
        this.moveDate = moveDate;
    }

    /**
     * Getter for property sourceBranchId.
     *
     * @return Value of property sourceBranchId.
     */
    public java.lang.String getSourceBranchId() {
        return sourceBranchId;
    }

    /**
     * Setter for property sourceBranchId.
     *
     * @param sourceBranchId New value of property sourceBranchId.
     */
    public void setSourceBranchId(java.lang.String sourceBranchId) {
        this.sourceBranchId = sourceBranchId;
    }

    /**
     * Getter for property sourceDepartment.
     *
     * @return Value of property sourceDepartment.
     */
    public java.lang.String getSourceDepartment() {
        return sourceDepartment;
    }

    /**
     * Setter for property sourceDepartment.
     *
     * @param sourceDepartment New value of property sourceDepartment.
     */
    public void setSourceDepartment(java.lang.String sourceDepartment) {
        this.sourceDepartment = sourceDepartment;
    }

    /**
     * Getter for property sourceFloor.
     *
     * @return Value of property sourceFloor.
     */
    public java.lang.String getSourceFloor() {
        return sourceFloor;
    }

    /**
     * Setter for property sourceFloor.
     *
     * @param sourceFloor New value of property sourceFloor.
     */
    public void setSourceFloor(java.lang.String sourceFloor) {
        this.sourceFloor = sourceFloor;
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
     * Getter for property faceValue.
     *
     * @return Value of property faceValue.
     */
    public java.lang.String getFaceValue() {
        return faceValue;
    }

    /**
     * Setter for property faceValue.
     *
     * @param faceValue New value of property faceValue.
     */
    public void setFaceValue(java.lang.String faceValue) {
        this.faceValue = faceValue;
    }

    /**
     * Getter for property currentValue.
     *
     * @return Value of property currentValue.
     */
    public java.lang.String getCurrentValue() {
        return currentValue;
    }

    /**
     * Setter for property currentValue.
     *
     * @param currentValue New value of property currentValue.
     */
    public void setCurrentValue(java.lang.String currentValue) {
        this.currentValue = currentValue;
    }
}