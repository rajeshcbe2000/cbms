/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveDetailsTO.java
 * 
 * 
 */
package com.see.truetransact.transferobject.payroll.leaveDetails;

/*
 * @author anjuanand
 *
 */
import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO is LEAVE_DETAILS.
 */
public class LeaveDetailsTO extends TransferObject implements Serializable {

    private String employeeId = "";
    private Date leaveDate = null;
    private Date leaveToDate=null;
    private String leaveDescription = "";
    private String remarks = "";
    private String leaveID = "";
    private Double leaveDedPerDay = 0.0;
    private String leaveDetailsId = "";

    public Date getLeaveToDate() {
        return leaveToDate;
    }

    public void setLeaveToDate(Date leaveToDate) {
        this.leaveToDate = leaveToDate;
    }
    
    public String getLeaveDetailsId() {
        return leaveDetailsId;
    }

    public void setLeaveDetailsId(String leaveDetailsId) {
        this.leaveDetailsId = leaveDetailsId;
    }

    public Double getLeaveDedPerDay() {
        return leaveDedPerDay;
    }

    public void setLeaveDedPerDay(Double leaveDedPerDay) {
        this.leaveDedPerDay = leaveDedPerDay;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getLeaveDescription() {
        return leaveDescription;
    }

    public void setLeaveDescription(String leaveDescription) {
        this.leaveDescription = leaveDescription;
    }

    public String getLeaveID() {
        return leaveID;
    }

    public void setLeaveID(String leaveID) {
        this.leaveID = leaveID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("leaveID");
        return leaveID;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("employeeId", employeeId));
        strB.append(getTOString("leaveDate", leaveDate));
        strB.append(getTOString("leaveToDate", leaveToDate));
        strB.append(getTOString("leaveDescription", leaveDescription));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("leaveID", leaveID));
        strB.append(getTOString("leaveDedPerDay", leaveDedPerDay));
        strB.append(getTOString("leaveDetailsId", leaveDetailsId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("employeeId", employeeId));
        strB.append(getTOXml("leaveDate", leaveDate));
        strB.append(getTOXml("leaveToDate", leaveToDate));
        strB.append(getTOXml("leaveDescription", leaveDescription));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("leaveID", leaveID));
        strB.append(getTOXml("leaveDedPerDay", leaveDedPerDay));
        strB.append(getTOXml("leaveDetailsId", leaveDetailsId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}