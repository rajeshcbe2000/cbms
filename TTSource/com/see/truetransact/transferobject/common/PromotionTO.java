/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeductionTO.java
 * 
 * Created on Wed Jun 09 18:50:50 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.common;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is Deduction.
 */
public class PromotionTO extends TransferObject implements Serializable {

    private Double slNo = null;
    private String empId = "";
    private String lastDesignation = "";
    private Date effectiveDate = null;
    private Date createdDate = null;
    private String presentBasic = "";
    private String promotionGrade = "";
    private String promotionDesignation = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String txtNewBasic = "";
    private String txtRemarks = "";
    private Double tempSlNO = null;
    private String txtPromotionEmployeeName = "";
    private String txtPromotionEmpBranch = "";
    private String txtPromotionLastGrade = "";
    private String promotionID = "";
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String authorizeStatus = null;

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
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("empId", empId));
        strB.append(getTOString("lastDesignation", lastDesignation));
        strB.append(getTOString("effectiveDate", effectiveDate));
        strB.append(getTOString("createdDate", createdDate));
        strB.append(getTOString("presentBasic", presentBasic));
        strB.append(getTOString("promotionGrade", promotionGrade));
        strB.append(getTOString("promotionDesignation", promotionDesignation));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("tempSlNO", tempSlNO));
        strB.append(getTOString("txtPromotionEmployeeName", txtPromotionEmployeeName));
        strB.append(getTOString("txtPromotionEmpBranch", txtPromotionEmpBranch));
        strB.append(getTOString("txtPromotionLastGrade", txtPromotionLastGrade));
        strB.append(getTOString("promotionID", promotionID));
        strB.append(getTOString("txtNewBasic", txtNewBasic));
        strB.append(getTOString("txtRemarks", txtRemarks));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("empId", empId));
        strB.append(getTOXml("lastDesignation", lastDesignation));

        strB.append(getTOXml("effectiveDate", effectiveDate));
        strB.append(getTOXml("createdDate", createdDate));
        strB.append(getTOXml("presentBasic", presentBasic));
        strB.append(getTOXml("promotionGrade", promotionGrade));
        strB.append(getTOXml("promotionDesignation", promotionDesignation));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("tempSlNO", tempSlNO));
        strB.append(getTOXml("txtPromotionEmployeeName", txtPromotionEmployeeName));
        strB.append(getTOXml("txtPromotionEmpBranch", txtPromotionEmpBranch));
        strB.append(getTOXml("txtPromotionLastGrade", txtPromotionLastGrade));
        strB.append(getTOXml("promotionID", promotionID));
        strB.append(getTOXml("txtNewBasic", txtNewBasic));
        strB.append(getTOXml("txtRemarks", txtRemarks));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property slNo.
     *
     * @return Value of property slNo.
     */
    public java.lang.Double getSlNo() {
        return slNo;
    }

    /**
     * Setter for property slNo.
     *
     * @param slNo New value of property slNo.
     */
    public void setSlNo(java.lang.Double slNo) {
        this.slNo = slNo;
    }

    /**
     * Getter for property empId.
     *
     * @return Value of property empId.
     */
    public java.lang.String getEmpId() {
        return empId;
    }

    /**
     * Setter for property empId.
     *
     * @param empId New value of property empId.
     */
    public void setEmpId(java.lang.String empId) {
        this.empId = empId;
    }

    /**
     * Getter for property lastDesignation.
     *
     * @return Value of property lastDesignation.
     */
    public java.lang.String getLastDesignation() {
        return lastDesignation;
    }

    /**
     * Setter for property lastDesignation.
     *
     * @param lastDesignation New value of property lastDesignation.
     */
    public void setLastDesignation(java.lang.String lastDesignation) {
        this.lastDesignation = lastDesignation;
    }

    /**
     * Getter for property effectiveDate.
     *
     * @return Value of property effectiveDate.
     */
    public java.util.Date getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Setter for property effectiveDate.
     *
     * @param effectiveDate New value of property effectiveDate.
     */
    public void setEffectiveDate(java.util.Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * Getter for property createdDate.
     *
     * @return Value of property createdDate.
     */
    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Setter for property createdDate.
     *
     * @param createdDate New value of property createdDate.
     */
    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Getter for property presentBasic.
     *
     * @return Value of property presentBasic.
     */
    public java.lang.String getPresentBasic() {
        return presentBasic;
    }

    /**
     * Setter for property presentBasic.
     *
     * @param presentBasic New value of property presentBasic.
     */
    public void setPresentBasic(java.lang.String presentBasic) {
        this.presentBasic = presentBasic;
    }

    /**
     * Getter for property promotionGrade.
     *
     * @return Value of property promotionGrade.
     */
    public java.lang.String getPromotionGrade() {
        return promotionGrade;
    }

    /**
     * Setter for property promotionGrade.
     *
     * @param promotionGrade New value of property promotionGrade.
     */
    public void setPromotionGrade(java.lang.String promotionGrade) {
        this.promotionGrade = promotionGrade;
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
     * Getter for property tempSlNO.
     *
     * @return Value of property tempSlNO.
     */
    public java.lang.Double getTempSlNO() {
        return tempSlNO;
    }

    /**
     * Setter for property tempSlNO.
     *
     * @param tempSlNO New value of property tempSlNO.
     */
    public void setTempSlNO(java.lang.Double tempSlNO) {
        this.tempSlNO = tempSlNO;
    }

    /**
     * Getter for property txtPromotionEmployeeName.
     *
     * @return Value of property txtPromotionEmployeeName.
     */
    public java.lang.String getTxtPromotionEmployeeName() {
        return txtPromotionEmployeeName;
    }

    /**
     * Setter for property txtPromotionEmployeeName.
     *
     * @param txtPromotionEmployeeName New value of property
     * txtPromotionEmployeeName.
     */
    public void setTxtPromotionEmployeeName(java.lang.String txtPromotionEmployeeName) {
        this.txtPromotionEmployeeName = txtPromotionEmployeeName;
    }

    /**
     * Getter for property txtPromotionEmpBranch.
     *
     * @return Value of property txtPromotionEmpBranch.
     */
    public java.lang.String getTxtPromotionEmpBranch() {
        return txtPromotionEmpBranch;
    }

    /**
     * Setter for property txtPromotionEmpBranch.
     *
     * @param txtPromotionEmpBranch New value of property txtPromotionEmpBranch.
     */
    public void setTxtPromotionEmpBranch(java.lang.String txtPromotionEmpBranch) {
        this.txtPromotionEmpBranch = txtPromotionEmpBranch;
    }

    public java.lang.String getPromotionID() {
        return promotionID;
    }

    /**
     * Setter for property promotionID.
     *
     * @param promotionID New value of property promotionID.
     */
    public void setPromotionID(java.lang.String promotionID) {
        this.promotionID = promotionID;
    }

    /**
     * Getter for property promotionDesignation.
     *
     * @return Value of property promotionDesignation.
     */
    public java.lang.String getPromotionDesignation() {
        return promotionDesignation;
    }

    /**
     * Setter for property promotionDesignation.
     *
     * @param promotionDesignation New value of property promotionDesignation.
     */
    public void setPromotionDesignation(java.lang.String promotionDesignation) {
        this.promotionDesignation = promotionDesignation;
    }

    /**
     * Getter for property txtNewBasic.
     *
     * @return Value of property txtNewBasic.
     */
    public java.lang.String getTxtNewBasic() {
        return txtNewBasic;
    }

    /**
     * Setter for property txtNewBasic.
     *
     * @param txtNewBasic New value of property txtNewBasic.
     */
    public void setTxtNewBasic(java.lang.String txtNewBasic) {
        this.txtNewBasic = txtNewBasic;
    }

    /**
     * Getter for property txtRemarks.
     *
     * @return Value of property txtRemarks.
     */
    public java.lang.String getTxtRemarks() {
        return txtRemarks;
    }

    /**
     * Setter for property txtRemarks.
     *
     * @param txtRemarks New value of property txtRemarks.
     */
    public void setTxtRemarks(java.lang.String txtRemarks) {
        this.txtRemarks = txtRemarks;
    }

    /**
     * Getter for property txtPromotionLastGrade.
     *
     * @return Value of property txtPromotionLastGrade.
     */
    public java.lang.String getTxtPromotionLastGrade() {
        return txtPromotionLastGrade;
    }

    /**
     * Setter for property txtPromotionLastGrade.
     *
     * @param txtPromotionLastGrade New value of property txtPromotionLastGrade.
     */
    public void setTxtPromotionLastGrade(java.lang.String txtPromotionLastGrade) {
        this.txtPromotionLastGrade = txtPromotionLastGrade;
    }

    /**
     * Getter for property authorizedBy.
     *
     * @return Value of property authorizedBy.
     */
    public java.lang.String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter for property authorizedBy.
     *
     * @param authorizedBy New value of property authorizedBy.
     */
    public void setAuthorizedBy(java.lang.String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    /**
     * Getter for property authorizedDt.
     *
     * @return Value of property authorizedDt.
     */
    public java.util.Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter for property authorizedDt.
     *
     * @param authorizedDt New value of property authorizedDt.
     */
    public void setAuthorizedDt(java.util.Date authorizedDt) {
        this.authorizedDt = authorizedDt;
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
     * Getter for property TxtPromotionLastGrade.
     *
     * @return Value of property TxtPromotionLastGrade.
     */
//    public java.lang.String getTxtPromotionLastGrade() {
//        return TxtPromotionLastGrade;
//    }
//    
    /**
     * Setter for property TxtPromotionLastGrade.
     *
     * @param TxtPromotionLastGrade New value of property TxtPromotionLastGrade.
     */
//    public void setTxtPromotionLastGrade(java.lang.String TxtPromotionLastGrade) {
//        this.TxtPromotionLastGrade = txtPromotionLastGrade;
//    }
}