/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryStructureTO.java
 * 
 * Created on Thu Aug 14 2014
 * 
 */
package com.see.truetransact.transferobject.payroll.salaryStructure;
/*
 *
 * @author anjuanand
 *
 */

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO is SCALE_MASTER.
 */
public class ScaleMasterTO extends TransferObject implements Serializable {

    private Integer scaleId;
    private Integer versionNo;
    private String designation = "";
    private Double startingAmount;
    private Double endingAmount;
    private String stagReqd = "";
    private String stagPeriod = "";
    private Double stagAmount;
    private Double stagCount;
    private Date fromDate = null;
    private Date toDate = null;
    private String status = "";
    private String statusBy = "";
    private String createdBy = "";
    private Date createdDate = null;
    private Date statusDate = null;

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public Double getEndingAmount() {
        return endingAmount;
    }

    public void setEndingAmount(Double endingAmount) {
        this.endingAmount = endingAmount;
    }

    public Integer getScaleId() {
        return scaleId;
    }

    public void setScaleId(Integer scaleId) {
        this.scaleId = scaleId;
    }

    public Double getStagAmount() {
        return stagAmount;
    }

    public void setStagAmount(Double stagAmount) {
        this.stagAmount = stagAmount;
    }

    public Double getStagCount() {
        return stagCount;
    }

    public void setStagCount(Double stagCount) {
        this.stagCount = stagCount;
    }

    public String getStagPeriod() {
        return stagPeriod;
    }

    public void setStagPeriod(String stagPeriod) {
        this.stagPeriod = stagPeriod;
    }

    public Double getStartingAmount() {
        return startingAmount;
    }

    public void setStartingAmount(Double startingAmount) {
        this.startingAmount = startingAmount;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public String getStagReqd() {
        return stagReqd;
    }

    public void setStagReqd(String stagReqd) {
        this.stagReqd = stagReqd;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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
        strB.append(getTOString("scaleId", scaleId));
        strB.append(getTOString("versionNo", versionNo));
        strB.append(getTOString("designation", designation));
        strB.append(getTOString("fromDate", fromDate));
        strB.append(getTOString("startingAmount", startingAmount));
        strB.append(getTOString("endingAmount", endingAmount));
        strB.append(getTOString("stagReqd", stagReqd));
        strB.append(getTOString("stagPeriod", stagPeriod));
        strB.append(getTOString("stagAmount", stagAmount));
        strB.append(getTOString("stagCount", stagCount));
        strB.append(getTOString("toDate", toDate));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDate", createdDate));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("scaleId", scaleId));
        strB.append(getTOXml("versionNo", versionNo));
        strB.append(getTOXml("designation", designation));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXml("startingAmount", startingAmount));
        strB.append(getTOXml("endingAmount", endingAmount));
        strB.append(getTOXml("stagReqd", stagReqd));
        strB.append(getTOXml("stagPeriod", stagPeriod));
        strB.append(getTOXml("stagAmount", stagAmount));
        strB.append(getTOXml("stagCount", stagCount));
        strB.append(getTOXml("toDate", toDate));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDate", createdDate));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}