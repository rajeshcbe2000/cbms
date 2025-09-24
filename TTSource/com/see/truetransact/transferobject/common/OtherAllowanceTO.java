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
public class OtherAllowanceTO extends TransferObject implements Serializable {

    private Double oAslNo = null;
    private String oAgrade = "";
    private Date oAfromDate = null;
    private Date oAtoDate = null;
    private String oAllowanceType = "";
    private String oAParameterBasedOn = "";
    private String oASubParameter = "";
    private String oAType = "";
    private Double oAFixedAmount = null;
    private Double oAPercentageValue = null;
    private Double oAMaximumPerAmt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDate = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDate = null;
    private String branchCode = "";
    private Double tempSlNo = null;
    private String oAbasedOnParameter = "";

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
        strB.append(getTOString("oAslNo", oAslNo));
        strB.append(getTOString("oAgrade", oAgrade));
        strB.append(getTOString("oAfromDate", oAfromDate));
        strB.append(getTOString("oAtoDate", oAtoDate));
        strB.append(getTOString("oAllowanceType", oAllowanceType));
        strB.append(getTOString("oAParameterBasedOn", oAParameterBasedOn));
        strB.append(getTOString("oASubParameter", oASubParameter));
        strB.append(getTOString("oAType", oAType));
        strB.append(getTOString("oAFixedAmount", oAFixedAmount));
        strB.append(getTOString("oAPercentageValue", oAPercentageValue));
        strB.append(getTOString("oAMaximumPerAmt", oAMaximumPerAmt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("tempSlNo", tempSlNo));
        strB.append(getTOString("oAbasedOnParameter ", oAbasedOnParameter));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("oAslNo", oAslNo));
        strB.append(getTOXml("oAgrade", oAgrade));
        strB.append(getTOXml("oAfromDate", oAfromDate));
        strB.append(getTOXml("oAtoDate", oAtoDate));
        strB.append(getTOXml("oAllowanceType", oAllowanceType));
        strB.append(getTOXml("oAParameterBasedOn", oAParameterBasedOn));
        strB.append(getTOXml("oASubParameter", oASubParameter));
        strB.append(getTOXml("oAType", oAType));
        strB.append(getTOXml("oAFixedAmount", oAFixedAmount));
        strB.append(getTOXml("oAPercentageValue", oAPercentageValue));
        strB.append(getTOXml("oAMaximumPerAmt", oAMaximumPerAmt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("tempSlNo", tempSlNo));
        strB.append(getTOXml("oAbasedOnParameter ", oAbasedOnParameter));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property oAslNo.
     *
     * @return Value of property oAslNo.
     */
    public java.lang.Double getOAslNo() {
        return oAslNo;
    }

    /**
     * Setter for property oAslNo.
     *
     * @param oAslNo New value of property oAslNo.
     */
    public void setOAslNo(java.lang.Double oAslNo) {
        this.oAslNo = oAslNo;
    }

    /**
     * Getter for property oAgrade.
     *
     * @return Value of property oAgrade.
     */
    public java.lang.String getOAgrade() {
        return oAgrade;
    }

    /**
     * Setter for property oAgrade.
     *
     * @param oAgrade New value of property oAgrade.
     */
    public void setOAgrade(java.lang.String oAgrade) {
        this.oAgrade = oAgrade;
    }

    /**
     * Getter for property oAfromDate.
     *
     * @return Value of property oAfromDate.
     */
    public java.util.Date getOAfromDate() {
        return oAfromDate;
    }

    /**
     * Setter for property oAfromDate.
     *
     * @param oAfromDate New value of property oAfromDate.
     */
    public void setOAfromDate(java.util.Date oAfromDate) {
        this.oAfromDate = oAfromDate;
    }

    /**
     * Getter for property oAtoDate.
     *
     * @return Value of property oAtoDate.
     */
    public java.util.Date getOAtoDate() {
        return oAtoDate;
    }

    /**
     * Setter for property oAtoDate.
     *
     * @param oAtoDate New value of property oAtoDate.
     */
    public void setOAtoDate(java.util.Date oAtoDate) {
        this.oAtoDate = oAtoDate;
    }

    /**
     * Getter for property oAFixedAmount.
     *
     * @return Value of property oAFixedAmount.
     */
    public java.lang.Double getOAFixedAmount() {
        return oAFixedAmount;
    }

    /**
     * Setter for property oAFixedAmount.
     *
     * @param oAFixedAmount New value of property oAFixedAmount.
     */
    public void setOAFixedAmount(java.lang.Double oAFixedAmount) {
        this.oAFixedAmount = oAFixedAmount;
    }

    /**
     * Getter for property oAPercentageValue.
     *
     * @return Value of property oAPercentageValue.
     */
    public java.lang.Double getOAPercentageValue() {
        return oAPercentageValue;
    }

    /**
     * Setter for property oAPercentageValue.
     *
     * @param oAPercentageValue New value of property oAPercentageValue.
     */
    public void setOAPercentageValue(java.lang.Double oAPercentageValue) {
        this.oAPercentageValue = oAPercentageValue;
    }

    /**
     * Getter for property oAMaximumPerAmt.
     *
     * @return Value of property oAMaximumPerAmt.
     */
    public java.lang.Double getOAMaximumPerAmt() {
        return oAMaximumPerAmt;
    }

    /**
     * Setter for property oAMaximumPerAmt.
     *
     * @param oAMaximumPerAmt New value of property oAMaximumPerAmt.
     */
    public void setOAMaximumPerAmt(java.lang.Double oAMaximumPerAmt) {
        this.oAMaximumPerAmt = oAMaximumPerAmt;
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
     * Getter for property oAllowanceType.
     *
     * @return Value of property oAllowanceType.
     */
    public java.lang.String getOAllowanceType() {
        return oAllowanceType;
    }

    /**
     * Setter for property oAllowanceType.
     *
     * @param oAllowanceType New value of property oAllowanceType.
     */
    public void setOAllowanceType(java.lang.String oAllowanceType) {
        this.oAllowanceType = oAllowanceType;
    }

    /**
     * Getter for property oAParameterBasedOn.
     *
     * @return Value of property oAParameterBasedOn.
     */
    public java.lang.String getOAParameterBasedOn() {
        return oAParameterBasedOn;
    }

    /**
     * Setter for property oAParameterBasedOn.
     *
     * @param oAParameterBasedOn New value of property oAParameterBasedOn.
     */
    public void setOAParameterBasedOn(java.lang.String oAParameterBasedOn) {
        this.oAParameterBasedOn = oAParameterBasedOn;
    }

    /**
     * Getter for property oASubParameter.
     *
     * @return Value of property oASubParameter.
     */
    public java.lang.String getOASubParameter() {
        return oASubParameter;
    }

    /**
     * Setter for property oASubParameter.
     *
     * @param oASubParameter New value of property oASubParameter.
     */
    public void setOASubParameter(java.lang.String oASubParameter) {
        this.oASubParameter = oASubParameter;
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
     * Getter for property oAType.
     *
     * @return Value of property oAType.
     */
    public java.lang.String getOAType() {
        return oAType;
    }

    /**
     * Setter for property oAType.
     *
     * @param oAType New value of property oAType.
     */
    public void setOAType(java.lang.String oAType) {
        this.oAType = oAType;
    }

    /**
     * Getter for property oAbasedOnParameter.
     *
     * @return Value of property oAbasedOnParameter.
     */
    public java.lang.String getOAbasedOnParameter() {
        return oAbasedOnParameter;
    }

    /**
     * Setter for property oAbasedOnParameter.
     *
     * @param oAbasedOnParameter New value of property oAbasedOnParameter.
     */
    public void setOAbasedOnParameter(java.lang.String oAbasedOnParameter) {
        this.oAbasedOnParameter = oAbasedOnParameter;
    }
}