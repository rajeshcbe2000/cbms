/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 1-08-2015
 */
package com.see.truetransact.transferobject.payroll.leavesurrender;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is EMP_TRANSFER.
 */
public class LeaveSurrenderTO extends TransferObject implements Serializable {

   
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizeStatus = null;;
    private String BranCode = " ";
    private String empId = "";
    private Integer leaveNo = 0;
    private Double leaveAmt = 0.0;
    private String leaveSurrenderId = "";
    private String branchId="";

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    
    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public Double getLeaveAmt() {
        return leaveAmt;
    }

    public void setLeaveAmt(Double leaveAmt) {
        this.leaveAmt = leaveAmt;
    }

    public Integer getLeaveNo() {
        return leaveNo;
    }

    public void setLeaveNo(Integer leaveNo) {
        this.leaveNo = leaveNo;
    }

 

    public String getLeaveSurrenderId() {
        return leaveSurrenderId;
    }

    public void setLeaveSurrenderId(String leaveSurrenderId) {
        this.leaveSurrenderId = leaveSurrenderId;
    }
    

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

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    /**
     * Getter for property authorizedStatus.
     *
     * @return Value of property authorizedStatus.
     */
    


  

    /**
     * Getter for property BranCode.
     *
     * @return Value of property BranCode.
     */
    public String getBranCode() {
        return BranCode;
    }

    /**
     * Setter for property BranCode.
     *
     * @param BranCode New value of property BranCode.
     */
    public void setBranCode(String BranCode) {
        this.BranCode = BranCode;
    }

   
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("leaveSurrenderId", leaveSurrenderId));
        strB.append(getTOString("empId", empId));
        strB.append(getTOString("leaveNo", leaveNo));
        strB.append(getTOString("leaveAmt", leaveAmt));
        strB.append(getTOString("BranCode", BranCode));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("leaveSurrenderId", leaveSurrenderId));
        strB.append(getTOXml("empId", empId));
        strB.append(getTOXml("leaveNo", leaveNo));
        strB.append(getTOXml("leaveAmt", leaveAmt));
        strB.append(getTOXml("BranCode", BranCode));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

}