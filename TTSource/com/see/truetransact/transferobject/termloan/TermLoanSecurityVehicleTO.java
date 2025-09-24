/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanSecurityMemberTO.java
 * 
 * Created on Thu Nov 24 14:00:48 IST 2011
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_SECURITY_MEMBER.
 */
public class TermLoanSecurityVehicleTO extends TransferObject implements Serializable {

    private String acctNum = "";
    private String memberNo = "";
    private String memberName = "";
    private String memberType = "";
    private Integer contactNo = null;
    private String branchCode = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String vehicleNo="";
    private String vehicleType="";
    private String vehichleDetails="";
    private Date vehicleDate;
    private String vehicleRcBookNo="";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    public Double netWorth=0.0;
    public Double memSalary=0.0;
    public String getVehichleDetails() {
        return vehichleDetails;
    }

    public void setVehichleDetails(String vehichleDetails) {
        this.vehichleDetails = vehichleDetails;
    }

    public Date getVehicleDate() {
        return vehicleDate;
    }

    public void setVehicleDate(Date vehicleDate) {
        this.vehicleDate = vehicleDate;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleRcBookNo() {
        return vehicleRcBookNo;
    }

    public void setVehicleRcBookNo(String vehicleRcBookNo) {
        this.vehicleRcBookNo = vehicleRcBookNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    /**
     * Setter/Getter for ACCT_NUM - table Field
     */
    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAcctNum() {
        return acctNum;
    }

    /**
     * Setter/Getter for MEMBER_NO - table Field
     */
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberNo() {
        return memberNo;
    }

    /**
     * Setter/Getter for MEMBER_NAME - table Field
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName() {
        return memberName;
    }

    /**
     * Setter/Getter for MEMBER_TYPE - table Field
     */
    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getMemberType() {
        return memberType;
    }

    /**
     * Setter/Getter for CONTACT_NO - table Field
     */
    public void setContactNo(Integer contactNo) {
        this.contactNo = contactNo;
    }

    public Integer getContactNo() {
        return contactNo;
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

    public Double getMemSalary() {
        return memSalary;
    }

    public void setMemSalary(Double memSalary) {
        this.memSalary = memSalary;
    }

    public Double getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(Double netWorth) {
        this.netWorth = netWorth;
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
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("memberNo", memberNo));
        strB.append(getTOString("memberName", memberName));
        strB.append(getTOString("memberType", memberType));
        strB.append(getTOString("contactNo", contactNo));
        strB.append(getTOString("vehicleNo", vehicleNo));
        strB.append(getTOString("vehicleType", vehicleType));
        strB.append(getTOString("vehicleRcBookNo",vehicleRcBookNo));
        strB.append(getTOString("netWorth",netWorth));
        strB.append(getTOString("costOfVehicle",memSalary));
        strB.append(getTOString("vehicleDate", vehicleDate));
        strB.append(getTOString("vehichleDetails", vehichleDetails));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
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
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("memberNo", memberNo));
        strB.append(getTOXml("memberName", memberName));
        strB.append(getTOXml("memberType", memberType));
        strB.append(getTOXml("contactNo", contactNo));
        strB.append(getTOXml("vehicleNo", vehicleNo));
        strB.append(getTOXml("vehicleType", vehicleType));
        strB.append(getTOXml("vehicleRcBookNo",vehicleRcBookNo));
        strB.append(getTOXml("netWorth",netWorth));
        strB.append(getTOXml("costOfVehicle",memSalary));
        strB.append(getTOXml("vehicleDate", vehicleDate));
        strB.append(getTOXml("vehichleDetails", vehichleDetails));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}