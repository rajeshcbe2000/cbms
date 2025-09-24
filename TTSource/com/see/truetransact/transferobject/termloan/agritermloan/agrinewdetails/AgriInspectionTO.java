/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriInspectionTO.java
 *
 * Created on April 30, 2009, 1:16 PM
 */
package com.see.truetransact.transferobject.termloan.agritermloan.agrinewdetails;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
import java.lang.StringBuffer;

/**
 *
 * @author Administrator
 */
public class AgriInspectionTO extends TransferObject implements Serializable {

    String typeOfInspection = "";
    Date dateOfInspection = null;
    String inspectionDetails = "";
    String inspectBy = "";
    String areaInspectObservation = "";
    String slno = "";
    String status = "";
    String acctNum = "";

    /**
     * Creates a new instance of AgriInspectionTO
     */
    public AgriInspectionTO() {
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(getClass().getName());
        sb.append(getTOStringStart(getClass().getName()));
        sb.append(getTOString("slno", slno));
        sb.append(getTOString("acctNum", acctNum));
        sb.append(getTOString("typeOfInspection", typeOfInspection));
        sb.append(getTOString("dateOfInspection", dateOfInspection));
        sb.append(getTOString("inspectionDetails", inspectionDetails));
        sb.append(getTOString("inspectBy", inspectBy));
        sb.append(getTOString("areaInspectObservation", areaInspectObservation));
        sb.append(getTOString("status", status));
        sb.append(getTOStringEnd());
        return sb.toString();
    }

    public String toXml() {
        StringBuffer sb = new StringBuffer(getClass().getName());
        sb.append(getTOXmlStart(getClass().getName()));
        sb.append(getTOXml("slno", slno));
        sb.append(getTOXml("acctNum", acctNum));
        sb.append(getTOXml("typeOfInspection", typeOfInspection));
        sb.append(getTOXml("dateOfInspection", dateOfInspection));
        sb.append(getTOXml("inspectionDetails", inspectionDetails));
        sb.append(getTOXml("inspectBy", inspectBy));
        sb.append(getTOXml("areaInspectObservation", areaInspectObservation));
        sb.append(getTOXml("status", status));
        sb.append(getTOXmlEnd());
        return sb.toString();
    }

    /**
     * Getter for property typeOfInspection.
     *
     * @return Value of property typeOfInspection.
     */
    public java.lang.String getTypeOfInspection() {
        return typeOfInspection;
    }

    /**
     * Setter for property typeOfInspection.
     *
     * @param typeOfInspection New value of property typeOfInspection.
     */
    public void setTypeOfInspection(java.lang.String typeOfInspection) {
        this.typeOfInspection = typeOfInspection;
    }

    /**
     * Getter for property dateOfInspection.
     *
     * @return Value of property dateOfInspection.
     */
    public java.util.Date getDateOfInspection() {
        return dateOfInspection;
    }

    /**
     * Setter for property dateOfInspection.
     *
     * @param dateOfInspection New value of property dateOfInspection.
     */
    public void setDateOfInspection(java.util.Date dateOfInspection) {
        this.dateOfInspection = dateOfInspection;
    }

    /**
     * Getter for property inspectionDetails.
     *
     * @return Value of property inspectionDetails.
     */
    public java.lang.String getInspectionDetails() {
        return inspectionDetails;
    }

    /**
     * Setter for property inspectionDetails.
     *
     * @param inspectionDetails New value of property inspectionDetails.
     */
    public void setInspectionDetails(java.lang.String inspectionDetails) {
        this.inspectionDetails = inspectionDetails;
    }

    /**
     * Getter for property inspectBy.
     *
     * @return Value of property inspectBy.
     */
    public java.lang.String getInspectBy() {
        return inspectBy;
    }

    /**
     * Setter for property inspectBy.
     *
     * @param inspectBy New value of property inspectBy.
     */
    public void setInspectBy(java.lang.String inspectBy) {
        this.inspectBy = inspectBy;
    }

    /**
     * Getter for property slno.
     *
     * @return Value of property slno.
     */
    public java.lang.String getSlno() {
        return slno;
    }

    /**
     * Setter for property slno.
     *
     * @param slno New value of property slno.
     */
    public void setSlno(java.lang.String slno) {
        this.slno = slno;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Getter for property acctNum.
     *
     * @return Value of property acctNum.
     */
    public java.lang.String getAcctNum() {
        return acctNum;
    }

    /**
     * Setter for property acctNum.
     *
     * @param acctNum New value of property acctNum.
     */
    public void setAcctNum(java.lang.String acctNum) {
        this.acctNum = acctNum;
    }

    /**
     * Getter for property areaInspectObservation.
     *
     * @return Value of property areaInspectObservation.
     */
    public java.lang.String getAreaInspectObservation() {
        return areaInspectObservation;
    }

    /**
     * Setter for property areaInspectObservation.
     *
     * @param areaInspectObservation New value of property
     * areaInspectObservation.
     */
    public void setAreaInspectObservation(java.lang.String areaInspectObservation) {
        this.areaInspectObservation = areaInspectObservation;
    }
}
