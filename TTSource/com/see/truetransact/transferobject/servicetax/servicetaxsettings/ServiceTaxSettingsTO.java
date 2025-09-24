/*
 * LockerConfigTO.java
 *
 * Created on May 12, 2010, 4:22 PM
 */
package com.see.truetransact.transferobject.servicetax.servicetaxsettings;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author Administrator
 */
public class ServiceTaxSettingsTO extends TransferObject implements Serializable {

    private String serviceTaxId = "";
    private Date fromDate = new Date();
    private Double taxRate = 0d;
    private Double educationCess = 0d;
    private Double higherEdu_Cess = 0d;
    private String branchId = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String taxHeadId = "";
    private Date endDate = null;
    private String swatchhHeadId = "";
    private String krishiKalyanHeadid = "";
    private Double swachhCess = 0d;
    private Double krishiKalyanCess = 0d;
    private String cessRoundOff = ""; // Added by nithya on 23-04-2020 for KD-1837

    public Double getKrishiKalyanCess() {
        return krishiKalyanCess;
    }

    public void setKrishiKalyanCess(Double krishiKalyanCess) {
        this.krishiKalyanCess = krishiKalyanCess;
    }

    public Double getSwachhCess() {
        return swachhCess;
    }

    public void setSwachhCess(Double swachhCess) {
        this.swachhCess = swachhCess;
    }

   
    public String getKrishiKalyanHeadid() {
        return krishiKalyanHeadid;
    }

    public void setKrishiKalyanHeadid(String krishiKalyanHeadid) {
        this.krishiKalyanHeadid = krishiKalyanHeadid;
    }

    public String getSwatchhHeadId() {
        return swatchhHeadId;
    }

    public void setSwatchhHeadId(String swatchhHeadId) {
        this.swatchhHeadId = swatchhHeadId;
    }
   
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTaxHeadId() {
        return taxHeadId;
    }

    public void setTaxHeadId(String taxHeadId) {
        this.taxHeadId = taxHeadId;
    }

    
    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    
    /**
     * Setter/Getter for CONFIG_ID - table Field
     */
    /**
     * Setter/Getter for BRANCH_ID - table Field
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchId() {
        return branchId;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
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
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    public Double getEducationCess() {
        return educationCess;
    }

    public void setEducationCess(Double educationCess) {
        this.educationCess = educationCess;
    }

    public Double getHigherEdu_Cess() {
        return higherEdu_Cess;
    }

    public void setHigherEdu_Cess(Double higherEdu_Cess) {
        this.higherEdu_Cess = higherEdu_Cess;
    }

    

    public String getServiceTaxId() {
        return serviceTaxId;
    }

    public void setServiceTaxId(String serviceTaxId) {
        this.serviceTaxId = serviceTaxId;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }
    

    
   
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("serviceTaxId", serviceTaxId));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("taxRate", taxRate));
        strB.append(getTOString("educationCess", educationCess));
        strB.append(getTOString("higherEdu_Cess", higherEdu_Cess));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("taxHeadId", taxHeadId));
        strB.append(getTOString("swatchhHeadId", swatchhHeadId));
        strB.append(getTOString("krishiKalyanHeadid", krishiKalyanHeadid));
        strB.append(getTOString("swachhCess", swachhCess));
        strB.append(getTOString("krishiKalyanCess", krishiKalyanCess));
        strB.append(getTOString("endDate", endDate));
        strB.append(getTOString("cessRoundOff", cessRoundOff));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("serviceTaxId", serviceTaxId));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("taxRate", taxRate));
        strB.append(getTOXml("educationCess", educationCess));
        strB.append(getTOXml("higherEdu_Cess", higherEdu_Cess));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("taxHeadId", taxHeadId));
        strB.append(getTOXml("swatchhHeadId", swatchhHeadId));
        strB.append(getTOXml("krishiKalyanHeadid", krishiKalyanHeadid));
        strB.append(getTOXml("swachhCess", swachhCess));
        strB.append(getTOXml("krishiKalyanCess", krishiKalyanCess));
        strB.append(getTOXml("endDate", endDate));
        strB.append(getTOXml("cessRoundOff", cessRoundOff));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getCessRoundOff() {
        return cessRoundOff;
    }

    public void setCessRoundOff(String cessRoundOff) {
        this.cessRoundOff = cessRoundOff;
    }

   
}
