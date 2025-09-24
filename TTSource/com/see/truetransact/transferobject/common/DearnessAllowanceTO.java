/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryStructureTO.java
 * 
 * Created on Wed Jun 09 18:50:50 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.common;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SalaryStructure.
 */
public class DearnessAllowanceTO extends TransferObject implements Serializable {

    private String dAslNo = "";
    private String dAgrade = "";
    private Date dAfromDate = null;
    private Date dAtoDate = null;
    private String dANoOfPointsPerSlab = "";
    private String dAIndex = "";
    private String dAPercentagePerSlab = "";
    private String dATotalNoOfSlab = "";
    private String dATotalDAPercentage = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDate = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDate = null;
    private String branchCode = "";
    private Double tempSlNo = null;
    private Date lastEffectiveDt = null;
    private String rdoIndexOrPercent = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
//    public String getKeyData() {
//        setKeyColumns("dAslNo");
//        return dAslNo;
//    }
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("dAslNo", dAslNo));
        strB.append(getTOString("dAgrade", dAgrade));
        strB.append(getTOString("dAfromDate", dAfromDate));
        strB.append(getTOString("dAtoDate", dAtoDate));
        strB.append(getTOString("dANoOfPointsPerSlab", dANoOfPointsPerSlab));
        strB.append(getTOString("dAIndex", dAIndex));
        strB.append(getTOString("dAPercentagePerSlab", dAPercentagePerSlab));
        strB.append(getTOString("dATotalNoOfSlab", dATotalNoOfSlab));
        strB.append(getTOString("dATotalDAPercentage", dATotalDAPercentage));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("tempSlNo", tempSlNo));
        strB.append(getTOString("lastEffectiveDt", lastEffectiveDt));
        strB.append(getTOString("rdoIndexOrPercent", rdoIndexOrPercent));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("dAslNo", dAslNo));
        strB.append(getTOXml("dAgrade", dAgrade));
        strB.append(getTOXml("dAfromDate", dAfromDate));
        strB.append(getTOXml("dAtoDate", dAtoDate));
        strB.append(getTOXml("dANoOfPointsPerSlab", dANoOfPointsPerSlab));
        strB.append(getTOXml("dAIndex", dAIndex));
        strB.append(getTOXml("dAPercentagePerSlab", dAPercentagePerSlab));
        strB.append(getTOXml("dATotalNoOfSlab", dATotalNoOfSlab));
        strB.append(getTOXml("dATotalDAPercentage", dATotalDAPercentage));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("tempSlNo", tempSlNo));
        strB.append(getTOXml("lastEffectiveDt", lastEffectiveDt));
        strB.append(getTOXml("rdoIndexOrPercent", rdoIndexOrPercent));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property dAslNo.
     *
     * @return Value of property dAslNo.
     */
    public java.lang.String getDAslNo() {
        return dAslNo;
    }

    /**
     * Setter for property dAslNo.
     *
     * @param dAslNo New value of property dAslNo.
     */
    public void setDAslNo(java.lang.String dAslNo) {
        this.dAslNo = dAslNo;
    }

    /**
     * Getter for property dAgrade.
     *
     * @return Value of property dAgrade.
     */
    public java.lang.String getDAgrade() {
        return dAgrade;
    }

    /**
     * Setter for property dAgrade.
     *
     * @param dAgrade New value of property dAgrade.
     */
    public void setDAgrade(java.lang.String dAgrade) {
        this.dAgrade = dAgrade;
    }

    /**
     * Getter for property dAfromDate.
     *
     * @return Value of property dAfromDate.
     */
    public java.util.Date getDAfromDate() {
        return dAfromDate;
    }

    /**
     * Setter for property dAfromDate.
     *
     * @param dAfromDate New value of property dAfromDate.
     */
    public void setDAfromDate(java.util.Date dAfromDate) {
        this.dAfromDate = dAfromDate;
    }

    /**
     * Getter for property dAtoDate.
     *
     * @return Value of property dAtoDate.
     */
    public java.util.Date getDAtoDate() {
        return dAtoDate;
    }

    /**
     * Setter for property dAtoDate.
     *
     * @param dAtoDate New value of property dAtoDate.
     */
    public void setDAtoDate(java.util.Date dAtoDate) {
        this.dAtoDate = dAtoDate;
    }

    /**
     * Getter for property dANoOfPointsPerSlab.
     *
     * @return Value of property dANoOfPointsPerSlab.
     */
    public java.lang.String getDANoOfPointsPerSlab() {
        return dANoOfPointsPerSlab;
    }

    /**
     * Setter for property dANoOfPointsPerSlab.
     *
     * @param dANoOfPointsPerSlab New value of property dANoOfPointsPerSlab.
     */
    public void setDANoOfPointsPerSlab(java.lang.String dANoOfPointsPerSlab) {
        this.dANoOfPointsPerSlab = dANoOfPointsPerSlab;
    }

    /**
     * Getter for property dAIndex.
     *
     * @return Value of property dAIndex.
     */
    public java.lang.String getDAIndex() {
        return dAIndex;
    }

    /**
     * Setter for property dAIndex.
     *
     * @param dAIndex New value of property dAIndex.
     */
    public void setDAIndex(java.lang.String dAIndex) {
        this.dAIndex = dAIndex;
    }

    /**
     * Getter for property dAPercentagePerSlab.
     *
     * @return Value of property dAPercentagePerSlab.
     */
    public java.lang.String getDAPercentagePerSlab() {
        return dAPercentagePerSlab;
    }

    /**
     * Setter for property dAPercentagePerSlab.
     *
     * @param dAPercentagePerSlab New value of property dAPercentagePerSlab.
     */
    public void setDAPercentagePerSlab(java.lang.String dAPercentagePerSlab) {
        this.dAPercentagePerSlab = dAPercentagePerSlab;
    }

    /**
     * Getter for property dATotalNoOfSlab.
     *
     * @return Value of property dATotalNoOfSlab.
     */
    public java.lang.String getDATotalNoOfSlab() {
        return dATotalNoOfSlab;
    }

    /**
     * Setter for property dATotalNoOfSlab.
     *
     * @param dATotalNoOfSlab New value of property dATotalNoOfSlab.
     */
    public void setDATotalNoOfSlab(java.lang.String dATotalNoOfSlab) {
        this.dATotalNoOfSlab = dATotalNoOfSlab;
    }

    /**
     * Getter for property dATotalDAPercentage.
     *
     * @return Value of property dATotalDAPercentage.
     */
    public java.lang.String getDATotalDAPercentage() {
        return dATotalDAPercentage;
    }

    /**
     * Setter for property dATotalDAPercentage.
     *
     * @param dATotalDAPercentage New value of property dATotalDAPercentage.
     */
    public void setDATotalDAPercentage(java.lang.String dATotalDAPercentage) {
        this.dATotalDAPercentage = dATotalDAPercentage;
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
     * Getter for property statusDate.
     *
     * @return Value of property statusDate.
     */
    public java.util.Date getStatusDate() {
        return statusDate;
    }

    /**
     * Setter for property statusDate.
     *
     * @param statusDate New value of property statusDate.
     */
    public void setStatusDate(java.util.Date statusDate) {
        this.statusDate = statusDate;
    }

    /**
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
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
     * Getter for property authorizeDate.
     *
     * @return Value of property authorizeDate.
     */
    public java.util.Date getAuthorizeDate() {
        return authorizeDate;
    }

    /**
     * Setter for property authorizeDate.
     *
     * @param authorizeDate New value of property authorizeDate.
     */
    public void setAuthorizeDate(java.util.Date authorizeDate) {
        this.authorizeDate = authorizeDate;
    }

    /**
     * Getter for property branchCode.
     *
     * @return Value of property branchCode.
     */
    public java.lang.String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter for property branchCode.
     *
     * @param branchCode New value of property branchCode.
     */
    public void setBranchCode(java.lang.String branchCode) {
        this.branchCode = branchCode;
    }

    /**
     * Getter for property tempSlNo.
     *
     * @return Value of property tempSlNo.
     */
    public java.lang.Double getTempSlNo() {
        return tempSlNo;
    }

    /**
     * Setter for property tempSlNo.
     *
     * @param tempSlNo New value of property tempSlNo.
     */
    public void setTempSlNo(java.lang.Double tempSlNo) {
        this.tempSlNo = tempSlNo;
    }

    /**
     * Getter for property lastEffectiveDt.
     *
     * @return Value of property lastEffectiveDt.
     */
    public java.util.Date getLastEffectiveDt() {
        return lastEffectiveDt;
    }

    /**
     * Setter for property lastEffectiveDt.
     *
     * @param lastEffectiveDt New value of property lastEffectiveDt.
     */
    public void setLastEffectiveDt(java.util.Date lastEffectiveDt) {
        this.lastEffectiveDt = lastEffectiveDt;
    }

    /**
     * Getter for property rdoIndexOrPercent.
     *
     * @return Value of property rdoIndexOrPercent.
     */
    public java.lang.String getRdoIndexOrPercent() {
        return rdoIndexOrPercent;
    }

    /**
     * Setter for property rdoIndexOrPercent.
     *
     * @param rdoIndexOrPercent New value of property rdoIndexOrPercent.
     */
    public void setRdoIndexOrPercent(java.lang.String rdoIndexOrPercent) {
        this.rdoIndexOrPercent = rdoIndexOrPercent;
    }
}
