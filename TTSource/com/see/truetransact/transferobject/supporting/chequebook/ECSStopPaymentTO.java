/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ECSStopPaymentTO.java
 * 
 * Created on Fri Jan 21 11:18:56 IST 2005
 */
package com.see.truetransact.transferobject.supporting.chequebook;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CHEQUE_STOP_PAYMENT.
 */
public class ECSStopPaymentTO extends TransferObject implements Serializable {

    private String ecsStopId = "";
    private Date ecsStopDt = null;
    private String ecsProdId = "";
    private String ecsAcctNo = "";
    private String ecsEndChqNo1 = "";
    private String ecsEndChqNo2 = "";
    private String ecsPayeeName = "";
    private Double ecsAmt = null;
    private Double ecsStopPayChrg = null;
    private String ecsStopPayReason = "";
    private String ecsStopStatus = "";
    private String ecsStatus = "";
    private String ecsAuthorizeStatus = null;
    private Date ecsAuthorizeDt = null;
    private String ecsAuthorizeBy = "";
    private String ecsAuthorizeRemarks = "";
    private String ecsStatusBy = "";
    private Date ecsStatusDt = null;
    private String ecsCreatedBy = "";
    private Date ecsCreatedDt = null;
    private String ecsProdType = "";
    private String ecsBranchId = "";
    private Date ecsRevokeDt = null;
    private Date ecsDt = null;

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("ecsStopId");
        return ecsStopId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("ecsStopId", ecsStopId));
        strB.append(getTOString("ecsStopDt", ecsStopDt));
        strB.append(getTOString("ecsProdId", ecsProdId));
        strB.append(getTOString("ecsAcctNo", ecsAcctNo));
        strB.append(getTOString("ecsEndChqNo1", ecsEndChqNo1));
        strB.append(getTOString("ecsEndChqNo2", ecsEndChqNo2));
        strB.append(getTOString("ecsPayeeName", ecsPayeeName));
        strB.append(getTOString("ecsAmt", ecsAmt));
        strB.append(getTOString("ecsStopPayChrg", ecsStopPayChrg));
        strB.append(getTOString("ecsStopPayReason", ecsStopPayReason));
        strB.append(getTOString("ecsStopStatus", ecsStopStatus));
        strB.append(getTOString("ecsStatus", ecsStatus));
        strB.append(getTOString("ecsAuthorizeStatus", ecsAuthorizeStatus));
        strB.append(getTOString("ecsAuthorizeDt", ecsAuthorizeDt));
        strB.append(getTOString("ecsAuthorizeBy", ecsAuthorizeBy));
        strB.append(getTOString("ecsAuthorizeRemarks", ecsAuthorizeRemarks));
        strB.append(getTOString("ecsStatusBy", ecsStatusBy));
        strB.append(getTOString("ecsStatusDt", ecsStatusDt));
        strB.append(getTOString("ecsCreatedBy", ecsCreatedBy));
        strB.append(getTOString("ecsCreatedDt", ecsCreatedDt));
        strB.append(getTOString("ecsProdType", ecsProdType));
        strB.append(getTOString("ecsBranchId", ecsBranchId));
        strB.append(getTOString("ecsRevokeDt", ecsRevokeDt));
        strB.append(getTOString("ecsDt", ecsDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("ecsStopId", ecsStopId));
        strB.append(getTOXml("ecsStopDt", ecsStopDt));
        strB.append(getTOXml("ecsProdId", ecsProdId));
        strB.append(getTOXml("ecsAcctNo", ecsAcctNo));
        strB.append(getTOXml("ecsEndChqNo1", ecsEndChqNo1));
        strB.append(getTOXml("ecsEndChqNo2", ecsEndChqNo2));
        strB.append(getTOXml("ecsPayeeName", ecsPayeeName));
        strB.append(getTOXml("ecsAmt", ecsAmt));
        strB.append(getTOXml("ecsStopPayChrg", ecsStopPayChrg));
        strB.append(getTOXml("ecsStopPayReason", ecsStopPayReason));
        strB.append(getTOXml("ecsStopStatus", ecsStopStatus));
        strB.append(getTOXml("ecsStatus", ecsStatus));
        strB.append(getTOXml("ecsAuthorizeStatus", ecsAuthorizeStatus));
        strB.append(getTOXml("ecsAuthorizeDt", ecsAuthorizeDt));
        strB.append(getTOXml("ecsAuthorizeBy", ecsAuthorizeBy));
        strB.append(getTOXml("ecsAuthorizeRemarks", ecsAuthorizeRemarks));
        strB.append(getTOXml("ecsStatusBy", ecsStatusBy));
        strB.append(getTOXml("ecsStatusDt", ecsStatusDt));
        strB.append(getTOXml("ecsCreatedBy", ecsCreatedBy));
        strB.append(getTOXml("ecsCreatedDt", ecsCreatedDt));
        strB.append(getTOXml("ecsProdType", ecsProdType));
        strB.append(getTOXml("ecsBranchId", ecsBranchId));
        strB.append(getTOXml("ecsRevokeDt", ecsRevokeDt));
        strB.append(getTOString("ecsDt", ecsDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property ecsStopId.
     *
     * @return Value of property ecsStopId.
     */
    public java.lang.String getEcsStopId() {
        return ecsStopId;
    }

    /**
     * Setter for property ecsStopId.
     *
     * @param ecsStopId New value of property ecsStopId.
     */
    public void setEcsStopId(java.lang.String ecsStopId) {
        this.ecsStopId = ecsStopId;
    }

    /**
     * Getter for property ecsStopDt.
     *
     * @return Value of property ecsStopDt.
     */
    public java.util.Date getEcsStopDt() {
        return ecsStopDt;
    }

    /**
     * Setter for property ecsStopDt.
     *
     * @param ecsStopDt New value of property ecsStopDt.
     */
    public void setEcsStopDt(java.util.Date ecsStopDt) {
        this.ecsStopDt = ecsStopDt;
    }

    /**
     * Getter for property ecsProdId.
     *
     * @return Value of property ecsProdId.
     */
    public java.lang.String getEcsProdId() {
        return ecsProdId;
    }

    /**
     * Setter for property ecsProdId.
     *
     * @param ecsProdId New value of property ecsProdId.
     */
    public void setEcsProdId(java.lang.String ecsProdId) {
        this.ecsProdId = ecsProdId;
    }

    /**
     * Getter for property ecsAcctNo.
     *
     * @return Value of property ecsAcctNo.
     */
    public java.lang.String getEcsAcctNo() {
        return ecsAcctNo;
    }

    /**
     * Setter for property ecsAcctNo.
     *
     * @param ecsAcctNo New value of property ecsAcctNo.
     */
    public void setEcsAcctNo(java.lang.String ecsAcctNo) {
        this.ecsAcctNo = ecsAcctNo;
    }

    /**
     * Getter for property ecsEndChqNo1.
     *
     * @return Value of property ecsEndChqNo1.
     */
    public java.lang.String getEcsEndChqNo1() {
        return ecsEndChqNo1;
    }

    /**
     * Setter for property ecsEndChqNo1.
     *
     * @param ecsEndChqNo1 New value of property ecsEndChqNo1.
     */
    public void setEcsEndChqNo1(java.lang.String ecsEndChqNo1) {
        this.ecsEndChqNo1 = ecsEndChqNo1;
    }

    /**
     * Getter for property ecsEndChqNo2.
     *
     * @return Value of property ecsEndChqNo2.
     */
    public java.lang.String getEcsEndChqNo2() {
        return ecsEndChqNo2;
    }

    /**
     * Setter for property ecsEndChqNo2.
     *
     * @param ecsEndChqNo2 New value of property ecsEndChqNo2.
     */
    public void setEcsEndChqNo2(java.lang.String ecsEndChqNo2) {
        this.ecsEndChqNo2 = ecsEndChqNo2;
    }

    /**
     * Getter for property ecsPayeeName.
     *
     * @return Value of property ecsPayeeName.
     */
    public java.lang.String getEcsPayeeName() {
        return ecsPayeeName;
    }

    /**
     * Setter for property ecsPayeeName.
     *
     * @param ecsPayeeName New value of property ecsPayeeName.
     */
    public void setEcsPayeeName(java.lang.String ecsPayeeName) {
        this.ecsPayeeName = ecsPayeeName;
    }

    /**
     * Getter for property ecsAmt.
     *
     * @return Value of property ecsAmt.
     */
    public java.lang.Double getEcsAmt() {
        return ecsAmt;
    }

    /**
     * Setter for property ecsAmt.
     *
     * @param ecsAmt New value of property ecsAmt.
     */
    public void setEcsAmt(java.lang.Double ecsAmt) {
        this.ecsAmt = ecsAmt;
    }

    /**
     * Getter for property ecsStopPayChrg.
     *
     * @return Value of property ecsStopPayChrg.
     */
    public java.lang.Double getEcsStopPayChrg() {
        return ecsStopPayChrg;
    }

    /**
     * Setter for property ecsStopPayChrg.
     *
     * @param ecsStopPayChrg New value of property ecsStopPayChrg.
     */
    public void setEcsStopPayChrg(java.lang.Double ecsStopPayChrg) {
        this.ecsStopPayChrg = ecsStopPayChrg;
    }

    /**
     * Getter for property ecsStopPayReason.
     *
     * @return Value of property ecsStopPayReason.
     */
    public java.lang.String getEcsStopPayReason() {
        return ecsStopPayReason;
    }

    /**
     * Setter for property ecsStopPayReason.
     *
     * @param ecsStopPayReason New value of property ecsStopPayReason.
     */
    public void setEcsStopPayReason(java.lang.String ecsStopPayReason) {
        this.ecsStopPayReason = ecsStopPayReason;
    }

    /**
     * Getter for property ecsStopStatus.
     *
     * @return Value of property ecsStopStatus.
     */
    public java.lang.String getEcsStopStatus() {
        return ecsStopStatus;
    }

    /**
     * Setter for property ecsStopStatus.
     *
     * @param ecsStopStatus New value of property ecsStopStatus.
     */
    public void setEcsStopStatus(java.lang.String ecsStopStatus) {
        this.ecsStopStatus = ecsStopStatus;
    }

    /**
     * Getter for property ecsStatus.
     *
     * @return Value of property ecsStatus.
     */
    public java.lang.String getEcsStatus() {
        return ecsStatus;
    }

    /**
     * Setter for property ecsStatus.
     *
     * @param ecsStatus New value of property ecsStatus.
     */
    public void setEcsStatus(java.lang.String ecsStatus) {
        this.ecsStatus = ecsStatus;
    }

    /**
     * Getter for property ecsAuthorizeStatus.
     *
     * @return Value of property ecsAuthorizeStatus.
     */
    public java.lang.String getEcsAuthorizeStatus() {
        return ecsAuthorizeStatus;
    }

    /**
     * Setter for property ecsAuthorizeStatus.
     *
     * @param ecsAuthorizeStatus New value of property ecsAuthorizeStatus.
     */
    public void setEcsAuthorizeStatus(java.lang.String ecsAuthorizeStatus) {
        this.ecsAuthorizeStatus = ecsAuthorizeStatus;
    }

    /**
     * Getter for property ecsAuthorizeDt.
     *
     * @return Value of property ecsAuthorizeDt.
     */
    public java.util.Date getEcsAuthorizeDt() {
        return ecsAuthorizeDt;
    }

    /**
     * Setter for property ecsAuthorizeDt.
     *
     * @param ecsAuthorizeDt New value of property ecsAuthorizeDt.
     */
    public void setEcsAuthorizeDt(java.util.Date ecsAuthorizeDt) {
        this.ecsAuthorizeDt = ecsAuthorizeDt;
    }

    /**
     * Getter for property ecsAuthorizeBy.
     *
     * @return Value of property ecsAuthorizeBy.
     */
    public java.lang.String getEcsAuthorizeBy() {
        return ecsAuthorizeBy;
    }

    /**
     * Setter for property ecsAuthorizeBy.
     *
     * @param ecsAuthorizeBy New value of property ecsAuthorizeBy.
     */
    public void setEcsAuthorizeBy(java.lang.String ecsAuthorizeBy) {
        this.ecsAuthorizeBy = ecsAuthorizeBy;
    }

    /**
     * Getter for property ecsAuthorizeRemarks.
     *
     * @return Value of property ecsAuthorizeRemarks.
     */
    public java.lang.String getEcsAuthorizeRemarks() {
        return ecsAuthorizeRemarks;
    }

    /**
     * Setter for property ecsAuthorizeRemarks.
     *
     * @param ecsAuthorizeRemarks New value of property ecsAuthorizeRemarks.
     */
    public void setEcsAuthorizeRemarks(java.lang.String ecsAuthorizeRemarks) {
        this.ecsAuthorizeRemarks = ecsAuthorizeRemarks;
    }

    /**
     * Getter for property ecsStatusBy.
     *
     * @return Value of property ecsStatusBy.
     */
    public java.lang.String getEcsStatusBy() {
        return ecsStatusBy;
    }

    /**
     * Setter for property ecsStatusBy.
     *
     * @param ecsStatusBy New value of property ecsStatusBy.
     */
    public void setEcsStatusBy(java.lang.String ecsStatusBy) {
        this.ecsStatusBy = ecsStatusBy;
    }

    /**
     * Getter for property ecsStatusDt.
     *
     * @return Value of property ecsStatusDt.
     */
    public java.util.Date getEcsStatusDt() {
        return ecsStatusDt;
    }

    /**
     * Setter for property ecsStatusDt.
     *
     * @param ecsStatusDt New value of property ecsStatusDt.
     */
    public void setEcsStatusDt(java.util.Date ecsStatusDt) {
        this.ecsStatusDt = ecsStatusDt;
    }

    /**
     * Getter for property ecsCreatedDt.
     *
     * @return Value of property ecsCreatedDt.
     */
    public java.util.Date getEcsCreatedDt() {
        return ecsCreatedDt;
    }

    /**
     * Setter for property ecsCreatedDt.
     *
     * @param ecsCreatedDt New value of property ecsCreatedDt.
     */
    public void setEcsCreatedDt(java.util.Date ecsCreatedDt) {
        this.ecsCreatedDt = ecsCreatedDt;
    }

    /**
     * Getter for property ecsProdType.
     *
     * @return Value of property ecsProdType.
     */
    public java.lang.String getEcsProdType() {
        return ecsProdType;
    }

    /**
     * Setter for property ecsProdType.
     *
     * @param ecsProdType New value of property ecsProdType.
     */
    public void setEcsProdType(java.lang.String ecsProdType) {
        this.ecsProdType = ecsProdType;
    }

    /**
     * Getter for property ecsBranchId.
     *
     * @return Value of property ecsBranchId.
     */
    public java.lang.String getEcsBranchId() {
        return ecsBranchId;
    }

    /**
     * Setter for property ecsBranchId.
     *
     * @param ecsBranchId New value of property ecsBranchId.
     */
    public void setEcsBranchId(java.lang.String ecsBranchId) {
        this.ecsBranchId = ecsBranchId;
    }

    /**
     * Getter for property ecsRevokeDt.
     *
     * @return Value of property ecsRevokeDt.
     */
    public java.util.Date getEcsRevokeDt() {
        return ecsRevokeDt;
    }

    /**
     * Setter for property ecsRevokeDt.
     *
     * @param ecsRevokeDt New value of property ecsRevokeDt.
     */
    public void setEcsRevokeDt(java.util.Date ecsRevokeDt) {
        this.ecsRevokeDt = ecsRevokeDt;
    }

    /**
     * Getter for property ecsCreatedBy.
     *
     * @return Value of property ecsCreatedBy.
     */
    public java.lang.String getEcsCreatedBy() {
        return ecsCreatedBy;
    }

    /**
     * Setter for property ecsCreatedBy.
     *
     * @param ecsCreatedBy New value of property ecsCreatedBy.
     */
    public void setEcsCreatedBy(java.lang.String ecsCreatedBy) {
        this.ecsCreatedBy = ecsCreatedBy;
    }

    /**
     * Getter for property ecsDt.
     *
     * @return Value of property ecsDt.
     */
    public java.util.Date getEcsDt() {
        return ecsDt;
    }

    /**
     * Setter for property ecsDt.
     *
     * @param ecsDt New value of property ecsDt.
     */
    public void setEcsDt(java.util.Date ecsDt) {
        this.ecsDt = ecsDt;
    }
}