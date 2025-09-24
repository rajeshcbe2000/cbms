/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * FileManagementTO.java
 *
 * Created on 16 September, 2011, 2:24 PM
 */
package com.see.truetransact.transferobject.filemenagement;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
import java.util.*;

/**
 *
 * @author aravind
 */
public class FileManagementTO extends TransferObject implements Serializable {
    
    private Date applnDt = null;
    private String applnNo = "";
    private String fileNo = "";
    private String memberNo = "";
    private String submittedBy = "";
    private String address ="";
    private String particulars = "";
    private String remarks = "";
    private String submittedTo = "";
    private Date submissionDt1 = null;
    private Date submissionDt2 = null;
    private Date submissionDt3 = null;
    private String submissionAction1 = "";
    private String submissionAction2 = "";
    private String submissionAction3 = "";
    private String approvalStatus = "";
    private Date approvalDt1 = null;
    private Date approvalDt2 = null;
    private Date approvalDt3 = null;
    private String approvalAction1 = "";
    private String approvalAction2 = "";
    private String approvalAction3 = "";
    private String branchId = ""; 
    private String statusBy = "";
    private Date statusDt = null;
    private String status = "";
    private Date createdDt = null;   
    private String createdBy = "";
   
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));      
        strB.append(getTOString("applnDt", applnDt));
        strB.append(getTOString("applnNo", applnNo));
        strB.append(getTOString("fileNo", fileNo));      
        strB.append(getTOString("memberNo", memberNo));
        strB.append(getTOString("SubmittedBy", submittedBy));
        strB.append(getTOString("address", address));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("remarks", remarks));        
        strB.append(getTOString("submittedTo", submittedTo));
        strB.append(getTOString("submissionDt1", submissionDt1));
        strB.append(getTOString("submissionDt2", submissionDt2));
        strB.append(getTOString("submissionDt3", submissionDt3));        
        strB.append(getTOString("submissionAction1", submissionAction1));
        strB.append(getTOString("submissionAction2", submissionAction2));
        strB.append(getTOString("submissionAction3", submissionAction3));        
        strB.append(getTOString("approvalStatus", approvalStatus));
        strB.append(getTOString("approvalDt1", approvalDt1));
        strB.append(getTOString("approvalDt2", approvalDt2));
        strB.append(getTOString("approvalDt3", approvalDt3));        
        strB.append(getTOString("approvalAction1", approvalAction1));
        strB.append(getTOString("approvalAction2", approvalAction2));
        strB.append(getTOString("approvalAction3", approvalAction3));      
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("approvalDt2", approvalDt2));
        strB.append(getTOString("approvalDt3", approvalDt3));            
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));        
        strB.append(getTOXml("applnDt", applnDt));
        strB.append(getTOXml("applnNo", applnNo));
        strB.append(getTOXml("fileNo", fileNo));      
        strB.append(getTOXml("memberNo", memberNo));
        strB.append(getTOXml("SubmittedBy", submittedBy));
        strB.append(getTOXml("address", address));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("remarks", remarks));        
        strB.append(getTOXml("submittedTo", submittedTo));
        strB.append(getTOXml("submissionDt1", submissionDt1));
        strB.append(getTOXml("submissionDt2", submissionDt2));
        strB.append(getTOXml("submissionDt3", submissionDt3));        
        strB.append(getTOXml("submissionAction1", submissionAction1));
        strB.append(getTOXml("submissionAction2", submissionAction2));
        strB.append(getTOXml("submissionAction3", submissionAction3));        
        strB.append(getTOXml("approvalStatus", approvalStatus));
        strB.append(getTOXml("approvalDt1", approvalDt1));
        strB.append(getTOXml("approvalDt2", approvalDt2));
        strB.append(getTOXml("approvalDt3", approvalDt3));        
        strB.append(getTOXml("approvalAction1", approvalAction1));
        strB.append(getTOXml("approvalAction2", approvalAction2));
        strB.append(getTOXml("approvalAction3", approvalAction3));      
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("approvalDt2", approvalDt2));
        strB.append(getTOXml("approvalDt3", approvalDt3));        
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getApplnDt() {
        return applnDt;
    }

    public void setApplnDt(Date applnDt) {
        this.applnDt = applnDt;
    }

    public String getApplnNo() {
        return applnNo;
    }

    public void setApplnNo(String applnNo) {
        this.applnNo = applnNo;
    }

    public String getApprovalAction1() {
        return approvalAction1;
    }

    public void setApprovalAction1(String approvalAction1) {
        this.approvalAction1 = approvalAction1;
    }

    public String getApprovalAction2() {
        return approvalAction2;
    }

    public void setApprovalAction2(String approvalAction2) {
        this.approvalAction2 = approvalAction2;
    }

    public String getApprovalAction3() {
        return approvalAction3;
    }

    public void setApprovalAction3(String approvalAction3) {
        this.approvalAction3 = approvalAction3;
    }

    public Date getApprovalDt1() {
        return approvalDt1;
    }

    public void setApprovalDt1(Date approvalDt1) {
        this.approvalDt1 = approvalDt1;
    }

    public Date getApprovalDt2() {
        return approvalDt2;
    }

    public void setApprovalDt2(Date approvalDt2) {
        this.approvalDt2 = approvalDt2;
    }

    public Date getApprovalDt3() {
        return approvalDt3;
    }

    public void setApprovalDt3(Date approvalDt3) {
        this.approvalDt3 = approvalDt3;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public String getSubmissionAction1() {
        return submissionAction1;
    }

    public void setSubmissionAction1(String submissionAction1) {
        this.submissionAction1 = submissionAction1;
    }

    public String getSubmissionAction2() {
        return submissionAction2;
    }

    public void setSubmissionAction2(String submissionAction2) {
        this.submissionAction2 = submissionAction2;
    }

    public String getSubmissionAction3() {
        return submissionAction3;
    }

    public void setSubmissionAction3(String submissionAction3) {
        this.submissionAction3 = submissionAction3;
    }

    public Date getSubmissionDt1() {
        return submissionDt1;
    }

    public void setSubmissionDt1(Date submissionDt1) {
        this.submissionDt1 = submissionDt1;
    }

    public Date getSubmissionDt2() {
        return submissionDt2;
    }

    public void setSubmissionDt2(Date submissionDt2) {
        this.submissionDt2 = submissionDt2;
    }

    public Date getSubmissionDt3() {
        return submissionDt3;
    }

    public void setSubmissionDt3(Date submissionDt3) {
        this.submissionDt3 = submissionDt3;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getSubmittedTo() {
        return submittedTo;
    }

    public void setSubmittedTo(String submittedTo) {
        this.submittedTo = submittedTo;
    }
    
}
