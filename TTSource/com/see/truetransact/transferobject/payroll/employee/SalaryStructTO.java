/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * SalaryStructTO.java
 */

package com.see.truetransact.transferobject.payroll.employee;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author anjuanand
 */
public class SalaryStructTO extends TransferObject implements Serializable {

    private String employeeCode = "";
    private Integer scale_id = 0;
    private Integer versionNo = 0;
    private Integer srlNo = 0;
    private String designation = "";
    private String statusOfEmp;
    private String wfNo = "";
    private Double wfOpeningBalance = 0.0;
    private Date wfOpeningBalanceOn = null;
    private String daApplicable = "";
    private String hraApplicable = "";
    private String stopPayt = "";
    private String netSalaryProductType = "";
    private String netSalaryProductId = "";
    private String netSalaryAccountNo = "";
    private String pensionCodeNo = "";
    private Double pensionOpeningBalance = 0.0;
    private Integer incrementCount = 0;
    private String createdBy = "";
    private Date createdDt = null;
    private String authorizeBy = "";
    private String authorizeStatus = null;;
    private Date authorizeDt = null;
    private String customerName = "";
    private Double presentBasicSalary = 0.0;
    private String statusBy = "";
    private String status = "";
    private Date pensionOpeningBalanceOn = null;
    private Date effectiveDate = null;
    private Date lastIncrementDate = null;
    private Date nextIncrementDate = null;
    private Date dateOfJoin = null;
    private Date probationEndDate = null;
    private Date dateOfRetirement = null;

    public Double getPensionOpeningBalance() {
        return pensionOpeningBalance;
    }

    public void setPensionOpeningBalance(Double pensionOpeningBalance) {
        this.pensionOpeningBalance = pensionOpeningBalance;
    }

    public Double getPresentBasicSalary() {
        return presentBasicSalary;
    }

    public void setPresentBasicSalary(Double presentBasicSalary) {
        this.presentBasicSalary = presentBasicSalary;
    }

    public Double getWfOpeningBalance() {
        return wfOpeningBalance;
    }

    public void setWfOpeningBalance(Double wfOpeningBalance) {
        this.wfOpeningBalance = wfOpeningBalance;
    }

    public Integer getIncrementCount() {
        return incrementCount;
    }

    public void setIncrementCount(Integer incrementCount) {
        this.incrementCount = incrementCount;
    }

    public Integer getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(Integer srlNo) {
        this.srlNo = srlNo;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getDateOfJoin() {
        return dateOfJoin;
    }

    public void setDateOfJoin(Date dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }

    public Date getDateOfRetirement() {
        return dateOfRetirement;
    }

    public void setDateOfRetirement(Date dateOfRetirement) {
        this.dateOfRetirement = dateOfRetirement;
    }

    public Date getProbationEndDate() {
        return probationEndDate;
    }

    public void setProbationEndDate(Date probationEndDate) {
        this.probationEndDate = probationEndDate;
    }

    public Date getLastIncrementDate() {
        return lastIncrementDate;
    }

    public void setLastIncrementDate(Date lastIncrementDate) {
        this.lastIncrementDate = lastIncrementDate;
    }

    public Date getNextIncrementDate() {
        return nextIncrementDate;
    }

    public void setNextIncrementDate(Date nextIncrementDate) {
        this.nextIncrementDate = nextIncrementDate;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getDaApplicable() {
        return daApplicable;
    }

    public void setDaApplicable(String daApplicable) {
        this.daApplicable = daApplicable;
    }

    public String getHraApplicable() {
        return hraApplicable;
    }

    public void setHraApplicable(String hraApplicable) {
        this.hraApplicable = hraApplicable;
    }

    public String getNetSalaryAccountNo() {
        return netSalaryAccountNo;
    }

    public void setNetSalaryAccountNo(String netSalaryAccountNo) {
        this.netSalaryAccountNo = netSalaryAccountNo;
    }

    public String getNetSalaryProductId() {
        return netSalaryProductId;
    }

    public void setNetSalaryProductId(String netSalaryProductId) {
        this.netSalaryProductId = netSalaryProductId;
    }

    public String getNetSalaryProductType() {
        return netSalaryProductType;
    }

    public void setNetSalaryProductType(String netSalaryProductType) {
        this.netSalaryProductType = netSalaryProductType;
    }

    public String getPensionCodeNo() {
        return pensionCodeNo;
    }

    public void setPensionCodeNo(String pensionCodeNo) {
        this.pensionCodeNo = pensionCodeNo;
    }

    public Date getPensionOpeningBalanceOn() {
        return pensionOpeningBalanceOn;
    }

    public void setPensionOpeningBalanceOn(Date pensionOpeningBalanceOn) {
        this.pensionOpeningBalanceOn = pensionOpeningBalanceOn;
    }

    public String getStopPayt() {
        return stopPayt;
    }

    public void setStopPayt(String stopPayt) {
        this.stopPayt = stopPayt;
    }

    public String getWfNo() {
        return wfNo;
    }

    public void setWfNo(String wfNo) {
        this.wfNo = wfNo;
    }

    public Date getWfOpeningBalanceOn() {
        return wfOpeningBalanceOn;
    }

    public void setWfOpeningBalanceOn(Date wfOpeningBalanceOn) {
        this.wfOpeningBalanceOn = wfOpeningBalanceOn;
    }

    public String getStatusOfEmp() {
        return statusOfEmp;
    }

    public void setStatusOfEmp(String statusOfEmp) {
        this.statusOfEmp = statusOfEmp;
    }

    public Integer getScale_id() {
        return scale_id;
    }

    public void setScale_id(Integer scale_id) {
        this.scale_id = scale_id;
    }

    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    @Override
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("employeeCode", employeeCode));
        strB.append(getTOString("scale_id", scale_id));
        strB.append(getTOString("versionNo", versionNo));
        strB.append(getTOString("designation", designation));
        strB.append(getTOString("statusOfEmp", statusOfEmp));
        strB.append(getTOString("wfNo", wfNo));
        strB.append(getTOString("wfOpeningBalance", wfOpeningBalance));
        strB.append(getTOString("wfOpeningBalanceOn", wfOpeningBalanceOn));
        strB.append(getTOString("daApplicable", daApplicable));
        strB.append(getTOString("hraApplicable", hraApplicable));
        strB.append(getTOString("stopPayt", stopPayt));
        strB.append(getTOString("netSalaryProductType", netSalaryProductType));
        strB.append(getTOString("netSalaryProductId", netSalaryProductId));
        strB.append(getTOString("netSalaryAccountNo", netSalaryAccountNo));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("pensionCodeNo", pensionCodeNo));
        strB.append(getTOString("pensionOpeningBalance", pensionOpeningBalance));
        strB.append(getTOString("pensionOpeningBalanceOn", pensionOpeningBalanceOn));
        strB.append(getTOString("dateOfJoin", dateOfJoin));
        strB.append(getTOString("effectiveDate", effectiveDate));
        strB.append(getTOString("lastIncrementDate", lastIncrementDate));
        strB.append(getTOString("nextIncrementDate", nextIncrementDate));
        strB.append(getTOString("probationEndDate", probationEndDate));
        strB.append(getTOString("dateOfRetirement", dateOfRetirement));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("presentBasicSalary", presentBasicSalary));
        strB.append(getTOString("incrementCount", incrementCount));
        strB.append(getTOString("srlNo", srlNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("employeeCode", employeeCode));
        strB.append(getTOXml("scale_id", scale_id));
        strB.append(getTOXml("versionNo", versionNo));
        strB.append(getTOXml("designation", designation));
        strB.append(getTOXml("statusOfEmp", statusOfEmp));
        strB.append(getTOXml("wfNo", wfNo));
        strB.append(getTOXml("wfOpeningBalance", wfOpeningBalance));
        strB.append(getTOXml("wfOpeningBalanceOn", wfOpeningBalanceOn));
        strB.append(getTOXml("daApplicable", daApplicable));
        strB.append(getTOXml("hraApplicable", hraApplicable));
        strB.append(getTOXml("stopPayt", stopPayt));
        strB.append(getTOXml("netSalaryProductType", netSalaryProductType));
        strB.append(getTOXml("netSalaryProductId", netSalaryProductId));
        strB.append(getTOXml("netSalaryAccountNo", netSalaryAccountNo));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("pensionCodeNo", pensionCodeNo));
        strB.append(getTOXml("pensionOpeningBalance", pensionOpeningBalance));
        strB.append(getTOXml("pensionOpeningBalanceOn", pensionOpeningBalanceOn));
        strB.append(getTOXml("dateOfJoin", dateOfJoin));
        strB.append(getTOXml("effectiveDate", effectiveDate));
        strB.append(getTOXml("lastIncrementDate", lastIncrementDate));
        strB.append(getTOXml("nextIncrementDate", nextIncrementDate));
        strB.append(getTOXml("probationEndDate", probationEndDate));
        strB.append(getTOXml("dateOfRetirement", dateOfRetirement));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("presentBasicSalary", presentBasicSalary));
        strB.append(getTOXml("incrementCount", incrementCount));
        strB.append(getTOXml("srlNo", srlNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}
