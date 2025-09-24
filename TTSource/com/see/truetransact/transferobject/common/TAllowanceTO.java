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
public class TAllowanceTO extends TransferObject implements Serializable {

    private String tAslNo = "";
    private String tAgrade = "";
    private Date tAfromDate = null;
    private Date tAtoDate = null;
    private String taBasicAmtUpto = "";
    private String tAConveyancePerMonth = "";
    private String taBasicAmtBeyond = "";
    private String tAConveyanceAmt = "";
    private String tANoOflitresPerMonth = "";
    private String tAPricePerLitre = "";
    private String tATotalConveyanceAmt = "";
    private String taType = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDate = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDate = null;
    private String branchCode = "";
    private Double tempSlNo = null;

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
//    public String getKeyData() {
//        setKeyColumns("lienNo");
//        return slNo;
//    }
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("tAslNo", tAslNo));
        strB.append(getTOString("tAgrade", tAgrade));
        strB.append(getTOString("tAfromDate", tAfromDate));
        strB.append(getTOString("tAtoDate", tAtoDate));
        strB.append(getTOString("taBasicAmtUpto", taBasicAmtUpto));
        strB.append(getTOString("tAConveyancePerMonth", tAConveyancePerMonth));
        strB.append(getTOString("taBasicAmtBeyond", taBasicAmtBeyond));
        strB.append(getTOString("tAConveyanceAmt", tAConveyanceAmt));
        strB.append(getTOString("tANoOflitresPerMonth", tANoOflitresPerMonth));
        strB.append(getTOString("tAPricePerLitre", tAPricePerLitre));
        strB.append(getTOString("tATotalConveyanceAmt", tATotalConveyanceAmt));
        strB.append(getTOString("taType", taType));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("tempSlNo", tempSlNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("tAslNo", tAslNo));
        strB.append(getTOXml("tAgrade", tAgrade));
        strB.append(getTOXml("tAfromDate", tAfromDate));
        strB.append(getTOXml("tAtoDate", tAtoDate));
        strB.append(getTOXml("taBasicAmtUpto", taBasicAmtUpto));
        strB.append(getTOXml("tAConveyancePerMonth", tAConveyancePerMonth));
        strB.append(getTOXml("taBasicAmtBeyond", taBasicAmtBeyond));
        strB.append(getTOXml("tAConveyanceAmt", tAConveyanceAmt));
        strB.append(getTOXml("tANoOflitresPerMonth", tANoOflitresPerMonth));
        strB.append(getTOXml("tAPricePerLitre", tAPricePerLitre));
        strB.append(getTOXml("tATotalConveyanceAmt", tATotalConveyanceAmt));
        strB.append(getTOXml("taType", taType));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("tempSlNo", tempSlNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property tAslNo.
     *
     * @return Value of property tAslNo.
     */
    public java.lang.String getTAslNo() {
        return tAslNo;
    }

    /**
     * Setter for property tAslNo.
     *
     * @param tAslNo New value of property tAslNo.
     */
    public void setTAslNo(java.lang.String tAslNo) {
        this.tAslNo = tAslNo;
    }

    /**
     * Getter for property tAgrade.
     *
     * @return Value of property tAgrade.
     */
    public java.lang.String getTAgrade() {
        return tAgrade;
    }

    /**
     * Setter for property tAgrade.
     *
     * @param tAgrade New value of property tAgrade.
     */
    public void setTAgrade(java.lang.String tAgrade) {
        this.tAgrade = tAgrade;
    }

    /**
     * Getter for property tAfromDate.
     *
     * @return Value of property tAfromDate.
     */
    public java.util.Date getTAfromDate() {
        return tAfromDate;
    }

    /**
     * Setter for property tAfromDate.
     *
     * @param tAfromDate New value of property tAfromDate.
     */
    public void setTAfromDate(java.util.Date tAfromDate) {
        this.tAfromDate = tAfromDate;
    }

    /**
     * Getter for property tAtoDate.
     *
     * @return Value of property tAtoDate.
     */
    public java.util.Date getTAtoDate() {
        return tAtoDate;
    }

    /**
     * Setter for property tAtoDate.
     *
     * @param tAtoDate New value of property tAtoDate.
     */
    public void setTAtoDate(java.util.Date tAtoDate) {
        this.tAtoDate = tAtoDate;
    }

    /**
     * Getter for property taBasicAmtUpto.
     *
     * @return Value of property taBasicAmtUpto.
     */
    public java.lang.String getTaBasicAmtUpto() {
        return taBasicAmtUpto;
    }

    /**
     * Setter for property taBasicAmtUpto.
     *
     * @param taBasicAmtUpto New value of property taBasicAmtUpto.
     */
    public void setTaBasicAmtUpto(java.lang.String taBasicAmtUpto) {
        this.taBasicAmtUpto = taBasicAmtUpto;
    }

    /**
     * Getter for property tAConveyancePerMonth.
     *
     * @return Value of property tAConveyancePerMonth.
     */
    public java.lang.String getTAConveyancePerMonth() {
        return tAConveyancePerMonth;
    }

    /**
     * Setter for property tAConveyancePerMonth.
     *
     * @param tAConveyancePerMonth New value of property tAConveyancePerMonth.
     */
    public void setTAConveyancePerMonth(java.lang.String tAConveyancePerMonth) {
        this.tAConveyancePerMonth = tAConveyancePerMonth;
    }

    /**
     * Getter for property taBasicAmtBeyond.
     *
     * @return Value of property taBasicAmtBeyond.
     */
    public java.lang.String getTaBasicAmtBeyond() {
        return taBasicAmtBeyond;
    }

    /**
     * Setter for property taBasicAmtBeyond.
     *
     * @param taBasicAmtBeyond New value of property taBasicAmtBeyond.
     */
    public void setTaBasicAmtBeyond(java.lang.String taBasicAmtBeyond) {
        this.taBasicAmtBeyond = taBasicAmtBeyond;
    }

    /**
     * Getter for property tAConveyanceAmt.
     *
     * @return Value of property tAConveyanceAmt.
     */
    public java.lang.String getTAConveyanceAmt() {
        return tAConveyanceAmt;
    }

    /**
     * Setter for property tAConveyanceAmt.
     *
     * @param tAConveyanceAmt New value of property tAConveyanceAmt.
     */
    public void setTAConveyanceAmt(java.lang.String tAConveyanceAmt) {
        this.tAConveyanceAmt = tAConveyanceAmt;
    }

    /**
     * Getter for property tANoOflitresPerMonth.
     *
     * @return Value of property tANoOflitresPerMonth.
     */
    public java.lang.String getTANoOflitresPerMonth() {
        return tANoOflitresPerMonth;
    }

    /**
     * Setter for property tANoOflitresPerMonth.
     *
     * @param tANoOflitresPerMonth New value of property tANoOflitresPerMonth.
     */
    public void setTANoOflitresPerMonth(java.lang.String tANoOflitresPerMonth) {
        this.tANoOflitresPerMonth = tANoOflitresPerMonth;
    }

    /**
     * Getter for property tAPricePerLitre.
     *
     * @return Value of property tAPricePerLitre.
     */
    public java.lang.String getTAPricePerLitre() {
        return tAPricePerLitre;
    }

    /**
     * Setter for property tAPricePerLitre.
     *
     * @param tAPricePerLitre New value of property tAPricePerLitre.
     */
    public void setTAPricePerLitre(java.lang.String tAPricePerLitre) {
        this.tAPricePerLitre = tAPricePerLitre;
    }

    /**
     * Getter for property tATotalConveyanceAmt.
     *
     * @return Value of property tATotalConveyanceAmt.
     */
    public java.lang.String getTATotalConveyanceAmt() {
        return tATotalConveyanceAmt;
    }

    /**
     * Setter for property tATotalConveyanceAmt.
     *
     * @param tATotalConveyanceAmt New value of property tATotalConveyanceAmt.
     */
    public void setTATotalConveyanceAmt(java.lang.String tATotalConveyanceAmt) {
        this.tATotalConveyanceAmt = tATotalConveyanceAmt;
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
     * Getter for property taType.
     *
     * @return Value of property taType.
     */
    public java.lang.String getTaType() {
        return taType;
    }

    /**
     * Setter for property taType.
     *
     * @param taType New value of property taType.
     */
    public void setTaType(java.lang.String taType) {
        this.taType = taType;
    }
}