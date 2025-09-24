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
public class CCAllowanceTO extends TransferObject implements Serializable {

    private String cCslNo = "";
    private String cCgrade = "";
    private String cCCityType = "";
    private Date cCfromDate = null;
    private Date cCtoDate = null;
    private String cCstartingScaleAmt = "";
    private String cCincrementAmt = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDate = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDate = null;
    private String branchCode = "";
    private Double tempSlNo = null;
    private String txtFromAmount = "";
    private String txtToAmount = "";
    private String rdoPercentOrFixed = "";

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
        strB.append(getTOString("cCslNo", cCslNo));
        strB.append(getTOString("cCgrade", cCgrade));
        strB.append(getTOString("cCfromDate", cCfromDate));
        strB.append(getTOString("cCtoDate", cCtoDate));
        strB.append(getTOString("cCCityType", cCCityType));
        strB.append(getTOString("cCstartingScaleAmt", cCstartingScaleAmt));
        strB.append(getTOString("cCincrementAmt", cCincrementAmt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("tempSlNo", tempSlNo));
        strB.append(getTOString("txtFromAmount", txtFromAmount));
        strB.append(getTOString("txtToAmount", txtToAmount));
        strB.append(getTOString("rdoPercentOrFixed", rdoPercentOrFixed));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("cCslNo", cCslNo));
        strB.append(getTOXml("cCgrade", cCgrade));
        strB.append(getTOXml("cCfromDate", cCfromDate));
        strB.append(getTOXml("cCtoDate", cCtoDate));
        strB.append(getTOXml("cCCityType", cCCityType));
        strB.append(getTOXml("cCstartingScaleAmt", cCstartingScaleAmt));
        strB.append(getTOXml("cCincrementAmt", cCincrementAmt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("tempSlNo", tempSlNo));
        strB.append(getTOXml("txtFromAmount", txtFromAmount));
        strB.append(getTOXml("txtToAmount", txtToAmount));
        strB.append(getTOXml("rdoPercentOrFixed", rdoPercentOrFixed));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property cCslNo.
     *
     * @return Value of property cCslNo.
     */
    public java.lang.String getCCslNo() {
        return cCslNo;
    }

    /**
     * Setter for property cCslNo.
     *
     * @param cCslNo New value of property cCslNo.
     */
    public void setCCslNo(java.lang.String cCslNo) {
        this.cCslNo = cCslNo;
    }

    /**
     * Getter for property cCgrade.
     *
     * @return Value of property cCgrade.
     */
    public java.lang.String getCCgrade() {
        return cCgrade;
    }

    /**
     * Setter for property cCgrade.
     *
     * @param cCgrade New value of property cCgrade.
     */
    public void setCCgrade(java.lang.String cCgrade) {
        this.cCgrade = cCgrade;
    }

    /**
     * Getter for property cCCityType.
     *
     * @return Value of property cCCityType.
     */
    public java.lang.String getCCCityType() {
        return cCCityType;
    }

    /**
     * Setter for property cCCityType.
     *
     * @param cCCityType New value of property cCCityType.
     */
    public void setCCCityType(java.lang.String cCCityType) {
        this.cCCityType = cCCityType;
    }

    /**
     * Getter for property cCfromDate.
     *
     * @return Value of property cCfromDate.
     */
    public java.util.Date getCCfromDate() {
        return cCfromDate;
    }

    /**
     * Setter for property cCfromDate.
     *
     * @param cCfromDate New value of property cCfromDate.
     */
    public void setCCfromDate(java.util.Date cCfromDate) {
        this.cCfromDate = cCfromDate;
    }

    /**
     * Getter for property cCtoDate.
     *
     * @return Value of property cCtoDate.
     */
    public java.util.Date getCCtoDate() {
        return cCtoDate;
    }

    /**
     * Setter for property cCtoDate.
     *
     * @param cCtoDate New value of property cCtoDate.
     */
    public void setCCtoDate(java.util.Date cCtoDate) {
        this.cCtoDate = cCtoDate;
    }

    /**
     * Getter for property cCstartingScaleAmt.
     *
     * @return Value of property cCstartingScaleAmt.
     */
    public java.lang.String getCCstartingScaleAmt() {
        return cCstartingScaleAmt;
    }

    /**
     * Setter for property cCstartingScaleAmt.
     *
     * @param cCstartingScaleAmt New value of property cCstartingScaleAmt.
     */
    public void setCCstartingScaleAmt(java.lang.String cCstartingScaleAmt) {
        this.cCstartingScaleAmt = cCstartingScaleAmt;
    }

    /**
     * Getter for property cCincrementAmt.
     *
     * @return Value of property cCincrementAmt.
     */
    public java.lang.String getCCincrementAmt() {
        return cCincrementAmt;
    }

    /**
     * Setter for property cCincrementAmt.
     *
     * @param cCincrementAmt New value of property cCincrementAmt.
     */
    public void setCCincrementAmt(java.lang.String cCincrementAmt) {
        this.cCincrementAmt = cCincrementAmt;
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
     * Getter for property txtFromAmount.
     *
     * @return Value of property txtFromAmount.
     */
    public java.lang.String getTxtFromAmount() {
        return txtFromAmount;
    }

    /**
     * Setter for property txtFromAmount.
     *
     * @param txtFromAmount New value of property txtFromAmount.
     */
    public void setTxtFromAmount(java.lang.String txtFromAmount) {
        this.txtFromAmount = txtFromAmount;
    }

    /**
     * Getter for property txtToAmount.
     *
     * @return Value of property txtToAmount.
     */
    public java.lang.String getTxtToAmount() {
        return txtToAmount;
    }

    /**
     * Setter for property txtToAmount.
     *
     * @param txtToAmount New value of property txtToAmount.
     */
    public void setTxtToAmount(java.lang.String txtToAmount) {
        this.txtToAmount = txtToAmount;
    }

    /**
     * Getter for property rdoPercentOrFixed.
     *
     * @return Value of property rdoPercentOrFixed.
     */
    public java.lang.String getRdoPercentOrFixed() {
        return rdoPercentOrFixed;
    }

    /**
     * Setter for property rdoPercentOrFixed.
     *
     * @param rdoPercentOrFixed New value of property rdoPercentOrFixed.
     */
    public void setRdoPercentOrFixed(java.lang.String rdoPercentOrFixed) {
        this.rdoPercentOrFixed = rdoPercentOrFixed;
    }
}