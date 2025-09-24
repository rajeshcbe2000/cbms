/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTransferTO.java
 * 
 * Created on Wed Jun 28  2010 swaroop
 */
package com.see.truetransact.transferobject.sysadmin.emptraining;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is EMP_TRANSFER.
 */
public class EmpTrainingTO extends TransferObject implements Serializable {

    private String empTrainingID = "";
    private String empID = "";
    private String destination = "";
    private String location = "";
    private String team = "";
    private String teamSize = "";
    private Date trainingFrom = null;
    private Date trainingTo = null;
    private String branCode = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizedStatus = null;
    private String empName = "";
    private String empBran = "";
    private String slNo = "";
    private String noOfEmp = "";
    private String subj = "";
    // For Emp Details Table  
    private String empStatus = "";
    private Date empStatusDt = null;
    private String empStatusBy = "";
    private Date empCreatedDt = null;
    private String empCreatedBy = "";
    private String empAuthorizeBy = "";
    private Date empAuthorizeDt = null;
    private String empAuthorizedStatus = null;

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("empTrainingID");
        return empTrainingID;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("empTrainingID", empTrainingID));
        strB.append(getTOString("empID", empID));
        strB.append(getTOString("destination", destination));
        strB.append(getTOString("location", location));
        strB.append(getTOString("team", team));
        strB.append(getTOString("teamSize", teamSize));
        strB.append(getTOString("trainingFrom", trainingFrom));
        strB.append(getTOString("trainingTo", trainingTo));
        strB.append(getTOString("branCode", branCode));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("empName", empName));
        strB.append(getTOString("empBran", empBran));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("noOfEmp", noOfEmp));
        strB.append(getTOString("empStatus", empStatus));
        strB.append(getTOString("empStatusDt", empStatusDt));
        strB.append(getTOString("empStatusBy", empStatusBy));
        strB.append(getTOString("empCreatedDt", empCreatedDt));
        strB.append(getTOString("empCreatedBy", empCreatedBy));
        strB.append(getTOString("empAuthorizeBy", empAuthorizeBy));
        strB.append(getTOString("empAuthorizeDt", empAuthorizeDt));
        strB.append(getTOString("empAuthorizedStatus", empAuthorizedStatus));
        strB.append(getTOString("subj", subj));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("empTrainingID", empTrainingID));
        strB.append(getTOXml("empID", empID));
        strB.append(getTOXml("destination", destination));
        strB.append(getTOXml("location", location));
        strB.append(getTOXml("team", team));
        strB.append(getTOXml("teamSize", teamSize));
        strB.append(getTOXml("trainingFrom", trainingFrom));
        strB.append(getTOXml("trainingTo", trainingTo));
        strB.append(getTOXml("branCode", branCode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("empName", empName));
        strB.append(getTOXml("empBran", empBran));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("noOfEmp", noOfEmp));
        strB.append(getTOXml("empStatus", empStatus));
        strB.append(getTOXml("empStatusDt", empStatusDt));
        strB.append(getTOXml("empStatusBy", empStatusBy));
        strB.append(getTOXml("empCreatedDt", empCreatedDt));
        strB.append(getTOXml("empCreatedBy", empCreatedBy));
        strB.append(getTOXml("empAuthorizeBy", empAuthorizeBy));
        strB.append(getTOXml("empAuthorizeDt", empAuthorizeDt));
        strB.append(getTOXml("empAuthorizedStatus", empAuthorizedStatus));
        strB.append(getTOXml("subj", subj));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property empTrainingID.
     *
     * @return Value of property empTrainingID.
     */
    public java.lang.String getEmpTrainingID() {
        return empTrainingID;
    }

    /**
     * Setter for property empTrainingID.
     *
     * @param empTrainingID New value of property empTrainingID.
     */
    public void setEmpTrainingID(java.lang.String empTrainingID) {
        this.empTrainingID = empTrainingID;
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
     * Getter for property branCode.
     *
     * @return Value of property branCode.
     */
    public java.lang.String getBranCode() {
        return branCode;
    }

    /**
     * Setter for property branCode.
     *
     * @param branCode New value of property branCode.
     */
    public void setBranCode(java.lang.String branCode) {
        this.branCode = branCode;
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
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property createdDt.
     *
     * @param createdDt New value of property createdDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
    }

    /**
     * Getter for property createdBy.
     *
     * @return Value of property createdBy.
     */
    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for property createdBy.
     *
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property authorizeDt.
     *
     * @return Value of property authorizeDt.
     */
    public java.util.Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter for property authorizeDt.
     *
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.util.Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    /**
     * Getter for property authorizedStatus.
     *
     * @return Value of property authorizedStatus.
     */
    public java.lang.String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter for property authorizedStatus.
     *
     * @param authorizedStatus New value of property authorizedStatus.
     */
    public void setAuthorizedStatus(java.lang.String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    /**
     * Getter for property empName.
     *
     * @return Value of property empName.
     */
    public java.lang.String getEmpName() {
        return empName;
    }

    /**
     * Setter for property empName.
     *
     * @param empName New value of property empName.
     */
    public void setEmpName(java.lang.String empName) {
        this.empName = empName;
    }

    /**
     * Getter for property destination.
     *
     * @return Value of property destination.
     */
    public java.lang.String getDestination() {
        return destination;
    }

    /**
     * Setter for property destination.
     *
     * @param destination New value of property destination.
     */
    public void setDestination(java.lang.String destination) {
        this.destination = destination;
    }

    /**
     * Getter for property location.
     *
     * @return Value of property location.
     */
    public java.lang.String getLocation() {
        return location;
    }

    /**
     * Setter for property location.
     *
     * @param location New value of property location.
     */
    public void setLocation(java.lang.String location) {
        this.location = location;
    }

    /**
     * Getter for property team.
     *
     * @return Value of property team.
     */
    public java.lang.String getTeam() {
        return team;
    }

    /**
     * Setter for property team.
     *
     * @param team New value of property team.
     */
    public void setTeam(java.lang.String team) {
        this.team = team;
    }

    /**
     * Getter for property teamSize.
     *
     * @return Value of property teamSize.
     */
    public java.lang.String getTeamSize() {
        return teamSize;
    }

    /**
     * Setter for property teamSize.
     *
     * @param teamSize New value of property teamSize.
     */
    public void setTeamSize(java.lang.String teamSize) {
        this.teamSize = teamSize;
    }

    /**
     * Getter for property trainingFrom.
     *
     * @return Value of property trainingFrom.
     */
    public java.util.Date getTrainingFrom() {
        return trainingFrom;
    }

    /**
     * Setter for property trainingFrom.
     *
     * @param trainingFrom New value of property trainingFrom.
     */
    public void setTrainingFrom(java.util.Date trainingFrom) {
        this.trainingFrom = trainingFrom;
    }

    /**
     * Getter for property trainingTo.
     *
     * @return Value of property trainingTo.
     */
    public java.util.Date getTrainingTo() {
        return trainingTo;
    }

    /**
     * Setter for property trainingTo.
     *
     * @param trainingTo New value of property trainingTo.
     */
    public void setTrainingTo(java.util.Date trainingTo) {
        this.trainingTo = trainingTo;
    }

    /**
     * Getter for property empBran.
     *
     * @return Value of property empBran.
     */
    public java.lang.String getEmpBran() {
        return empBran;
    }

    /**
     * Setter for property empBran.
     *
     * @param empBran New value of property empBran.
     */
    public void setEmpBran(java.lang.String empBran) {
        this.empBran = empBran;
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
     * Getter for property noOfEmp.
     *
     * @return Value of property noOfEmp.
     */
    public java.lang.String getNoOfEmp() {
        return noOfEmp;
    }

    /**
     * Setter for property noOfEmp.
     *
     * @param noOfEmp New value of property noOfEmp.
     */
    public void setNoOfEmp(java.lang.String noOfEmp) {
        this.noOfEmp = noOfEmp;
    }

    /**
     * Getter for property empStatus.
     *
     * @return Value of property empStatus.
     */
    public java.lang.String getEmpStatus() {
        return empStatus;
    }

    /**
     * Setter for property empStatus.
     *
     * @param empStatus New value of property empStatus.
     */
    public void setEmpStatus(java.lang.String empStatus) {
        this.empStatus = empStatus;
    }

    /**
     * Getter for property empStatusDt.
     *
     * @return Value of property empStatusDt.
     */
    public java.util.Date getEmpStatusDt() {
        return empStatusDt;
    }

    /**
     * Setter for property empStatusDt.
     *
     * @param empStatusDt New value of property empStatusDt.
     */
    public void setEmpStatusDt(java.util.Date empStatusDt) {
        this.empStatusDt = empStatusDt;
    }

    /**
     * Getter for property empStatusBy.
     *
     * @return Value of property empStatusBy.
     */
    public java.lang.String getEmpStatusBy() {
        return empStatusBy;
    }

    /**
     * Setter for property empStatusBy.
     *
     * @param empStatusBy New value of property empStatusBy.
     */
    public void setEmpStatusBy(java.lang.String empStatusBy) {
        this.empStatusBy = empStatusBy;
    }

    /**
     * Getter for property empCreatedDt.
     *
     * @return Value of property empCreatedDt.
     */
    public java.util.Date getEmpCreatedDt() {
        return empCreatedDt;
    }

    /**
     * Setter for property empCreatedDt.
     *
     * @param empCreatedDt New value of property empCreatedDt.
     */
    public void setEmpCreatedDt(java.util.Date empCreatedDt) {
        this.empCreatedDt = empCreatedDt;
    }

    /**
     * Getter for property empCeatedBy.
     *
     * @return Value of property empCeatedBy.
     */
    public java.lang.String getEmpCreatedBy() {
        return empCreatedBy;
    }

    /**
     * Setter for property empCeatedBy.
     *
     * @param empCeatedBy New value of property empCeatedBy.
     */
    public void setEmpCreatedBy(java.lang.String empCreatedBy) {
        this.empCreatedBy = empCreatedBy;
    }

    /**
     * Getter for property empAuthorizeBy.
     *
     * @return Value of property empAuthorizeBy.
     */
    public java.lang.String getEmpAuthorizeBy() {
        return empAuthorizeBy;
    }

    /**
     * Setter for property empAuthorizeBy.
     *
     * @param empAuthorizeBy New value of property empAuthorizeBy.
     */
    public void setEmpAuthorizeBy(java.lang.String empAuthorizeBy) {
        this.empAuthorizeBy = empAuthorizeBy;
    }

    /**
     * Getter for property empAuthorizeDt.
     *
     * @return Value of property empAuthorizeDt.
     */
    public java.util.Date getEmpAuthorizeDt() {
        return empAuthorizeDt;
    }

    /**
     * Setter for property empAuthorizeDt.
     *
     * @param empAuthorizeDt New value of property empAuthorizeDt.
     */
    public void setEmpAuthorizeDt(java.util.Date empAuthorizeDt) {
        this.empAuthorizeDt = empAuthorizeDt;
    }

    /**
     * Getter for property empAuthorizedStatus.
     *
     * @return Value of property empAuthorizedStatus.
     */
    public java.lang.String getEmpAuthorizedStatus() {
        return empAuthorizedStatus;
    }

    /**
     * Setter for property empAuthorizedStatus.
     *
     * @param empAuthorizedStatus New value of property empAuthorizedStatus.
     */
    public void setEmpAuthorizedStatus(java.lang.String empAuthorizedStatus) {
        this.empAuthorizedStatus = empAuthorizedStatus;
    }

    /**
     * Getter for property subj.
     *
     * @return Value of property subj.
     */
    public java.lang.String getSubj() {
        return subj;
    }

    /**
     * Setter for property subj.
     *
     * @param subj New value of property subj.
     */
    public void setSubj(java.lang.String subj) {
        this.subj = subj;
    }
}