/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PFTransferTO.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.transferobject.payroll.pfMaster;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO is PAY_PF_MASTER.
 */
public class PFMasterTO extends TransferObject implements Serializable {

   private String pfId = "";    
   private String pfActNo = "";
   private String empId = "";
   private Date pfDate = null;
   private Double pfRate = 0.0;
   private String pfNominee = "";
   private Double pfOpenBal = 0.0;
   private Date pfOpenDate = null;
   private Date pfIntDate = null;
   private String pfNomineeRelation = "";
   private String createdBy = "";
   private Date createdDate = null;
   private Double prnBal = 0.0;
   private String branchId = "";
   private Double employerContri = 0.0;
   private String status = "";
   private String statusBy = "";
   private Date statusDate = null;
   
   
    public String getPfId() {
        return pfId;
    }

    public void setPfId(String pfId) {
        this.pfId = pfId;
    }

    public Double getEmployerContri() {
        return employerContri;
    }

    public void setEmployerContri(Double employerContri) {
        this.employerContri = employerContri;
    }

    public Double getPfOpenBal() {
        return pfOpenBal;
    }

    public void setPfOpenBal(Double pfOpenBal) {
        this.pfOpenBal = pfOpenBal;
    }

    public Double getPfRate() {
        return pfRate;
    }

    public void setPfRate(Double pfRate) {
        this.pfRate = pfRate;
    }

    public Double getPrnBal() {
        return prnBal;
    }

    public void setPrnBal(Double prnBal) {
        this.prnBal = prnBal;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

  
    public String getPfActNo() {
        return pfActNo;
    }

    public void setPfActNo(String pfActNo) {
        this.pfActNo = pfActNo;
    }

    public Date getPfDate() {
        return pfDate;
    }

    public void setPfDate(Date pfDate) {
        this.pfDate = pfDate;
    }

    public Date getPfIntDate() {
        return pfIntDate;
    }

    public void setPfIntDate(Date pfIntDate) {
        this.pfIntDate = pfIntDate;
    }

    public String getPfNominee() {
        return pfNominee;
    }

    public void setPfNominee(String pfNominee) {
        this.pfNominee = pfNominee;
    }

    public String getPfNomineeRelation() {
        return pfNomineeRelation;
    }

    public void setPfNomineeRelation(String pfNomineeRelation) {
        this.pfNomineeRelation = pfNomineeRelation;
    }

      public Date getPfOpenDate() {
        return pfOpenDate;
    }

    public void setPfOpenDate(Date pfOpenDate) {
        this.pfOpenDate = pfOpenDate;
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

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }
  
   


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
        strB.append(getTOString("pfId", pfId));
        strB.append(getTOString("pfActNo", pfActNo));
        strB.append(getTOString("empId", empId));
        strB.append(getTOString("pfDate", pfDate));
        strB.append(getTOString("pfRate", pfRate));
        strB.append(getTOString("pfNominee", pfNominee));
        strB.append(getTOString("pfOpenBal", pfOpenBal));
        strB.append(getTOString("pfOpenDate", pfOpenDate));
        strB.append(getTOString("pfIntDate", pfIntDate));
        strB.append(getTOString("pfNomineeRelation", pfNomineeRelation));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDate", createdDate));
        strB.append(getTOString("prnBal", prnBal));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("employerContri", employerContri));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusBy));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

      
    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("pfId", pfId));
        strB.append(getTOXml("pfActNo", pfActNo));
        strB.append(getTOXml("empId", empId));
        strB.append(getTOXml("pfDate", pfDate));
        strB.append(getTOXml("pfRate", pfRate));
        strB.append(getTOXml("pfNominee", pfNominee));
        strB.append(getTOXml("pfOpenBal", pfOpenBal));
        strB.append(getTOXml("pfOpenDate", pfOpenDate));
        strB.append(getTOXml("pfIntDate", pfIntDate));
        strB.append(getTOXml("pfNomineeRelation", pfNomineeRelation));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDate", createdDate));
        strB.append(getTOXml("prnBal", prnBal));
        strB.append(getTOXml("branchId", pfNomineeRelation));
        strB.append(getTOXml("employerContri", employerContri));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}