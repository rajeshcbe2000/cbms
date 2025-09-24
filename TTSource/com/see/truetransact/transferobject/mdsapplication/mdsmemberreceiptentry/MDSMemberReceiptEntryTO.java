/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSMemberReceiptEntryTO.java
 * 
 * Created on Tue Nov 15 18:33:58 IST 2011
 */
package com.see.truetransact.transferobject.mdsapplication.mdsmemberreceiptentry;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is MDS_MEMBER_RECEIPT_ENTRY.
 */
public class MDSMemberReceiptEntryTO extends TransferObject implements Serializable {

    private String memberReceiptId = "";
    private String memberNo = "";
    private String schemeName = "";
    private String chittalNo = "";
    private Integer subNo = 0;
    private Double pendingInst = null;
    private Integer noOfInstPay = 0;
    private Double instAmt = null;
    private Double bonusAmt = null;
    private Double discountAmt = null;
    private Double totalPayable = null;
    private Double penalAmt = null;
    private Double noticeAmt = null;
    private Double arbitrationAmt = null;
    private Double netAmt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizedStatus = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;

    /**
     * Setter/Getter for MEMBER_RECEIPT_ID - table Field
     */
    public void setMemberReceiptId(String memberReceiptId) {
        this.memberReceiptId = memberReceiptId;
    }

    public String getMemberReceiptId() {
        return memberReceiptId;
    }

    /**
     * Setter/Getter for MEMBER_NO - table Field
     */
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberNo() {
        return memberNo;
    }

    /**
     * Setter/Getter for SCHEME_NAME - table Field
     */
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSchemeName() {
        return schemeName;
    }

    /**
     * Setter/Getter for CHITTAL_NO - table Field
     */
    public void setChittalNo(String chittalNo) {
        this.chittalNo = chittalNo;
    }

    public String getChittalNo() {
        return chittalNo;
    }

    /**
     * Setter/Getter for PENDING_INST - table Field
     */
    public void setPendingInst(Double pendingInst) {
        this.pendingInst = pendingInst;
    }

    public Double getPendingInst() {
        return pendingInst;
    }

    /**
     * Setter/Getter for INST_AMT - table Field
     */
    public void setInstAmt(Double instAmt) {
        this.instAmt = instAmt;
    }

    public Double getInstAmt() {
        return instAmt;
    }

    /**
     * Setter/Getter for BONUS_AMT - table Field
     */
    public void setBonusAmt(Double bonusAmt) {
        this.bonusAmt = bonusAmt;
    }

    public Double getBonusAmt() {
        return bonusAmt;
    }

    /**
     * Setter/Getter for DISCOUNT_AMT - table Field
     */
    public void setDiscountAmt(Double discountAmt) {
        this.discountAmt = discountAmt;
    }

    public Double getDiscountAmt() {
        return discountAmt;
    }

    /**
     * Setter/Getter for TOTAL_PAYABLE - table Field
     */
    public void setTotalPayable(Double totalPayable) {
        this.totalPayable = totalPayable;
    }

    public Double getTotalPayable() {
        return totalPayable;
    }

    /**
     * Setter/Getter for PENAL_AMT - table Field
     */
    public void setPenalAmt(Double penalAmt) {
        this.penalAmt = penalAmt;
    }

    public Double getPenalAmt() {
        return penalAmt;
    }

    /**
     * Setter/Getter for NOTICE_AMT - table Field
     */
    public void setNoticeAmt(Double noticeAmt) {
        this.noticeAmt = noticeAmt;
    }

    public Double getNoticeAmt() {
        return noticeAmt;
    }

    /**
     * Setter/Getter for ARBITRATION_AMT - table Field
     */
    public void setArbitrationAmt(Double arbitrationAmt) {
        this.arbitrationAmt = arbitrationAmt;
    }

    public Double getArbitrationAmt() {
        return arbitrationAmt;
    }

    /**
     * Setter/Getter for NET_AMT - table Field
     */
    public void setNetAmt(Double netAmt) {
        this.netAmt = netAmt;
    }

    public Double getNetAmt() {
        return netAmt;
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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
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
        strB.append(getTOString("memberReceiptId", memberReceiptId));
        strB.append(getTOString("memberNo", memberNo));
        strB.append(getTOString("schemeName", schemeName));
        strB.append(getTOString("chittalNo", chittalNo));
        strB.append(getTOString("subNo", subNo));
        strB.append(getTOString("pendingInst", pendingInst));
        strB.append(getTOString("noOfInstPay", noOfInstPay));
        strB.append(getTOString("instAmt", instAmt));
        strB.append(getTOString("bonusAmt", bonusAmt));
        strB.append(getTOString("discountAmt", discountAmt));
        strB.append(getTOString("totalPayable", totalPayable));
        strB.append(getTOString("penalAmt", penalAmt));
        strB.append(getTOString("noticeAmt", noticeAmt));
        strB.append(getTOString("arbitrationAmt", arbitrationAmt));
        strB.append(getTOString("netAmt", netAmt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("memberReceiptId", memberReceiptId));
        strB.append(getTOXml("memberNo", memberNo));
        strB.append(getTOXml("schemeName", schemeName));
        strB.append(getTOXml("chittalNo", chittalNo));
        strB.append(getTOXml("subNo", subNo));
        strB.append(getTOXml("pendingInst", pendingInst));
        strB.append(getTOXml("noOfInstPay", noOfInstPay));
        strB.append(getTOXml("instAmt", instAmt));
        strB.append(getTOXml("bonusAmt", bonusAmt));
        strB.append(getTOXml("discountAmt", discountAmt));
        strB.append(getTOXml("totalPayable", totalPayable));
        strB.append(getTOXml("penalAmt", penalAmt));
        strB.append(getTOXml("noticeAmt", noticeAmt));
        strB.append(getTOXml("arbitrationAmt", arbitrationAmt));
        strB.append(getTOXml("netAmt", netAmt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public Integer getSubNo() {
        return subNo;
    }

    public void setSubNo(Integer subNo) {
        this.subNo = subNo;
    }

    

    public Integer getNoOfInstPay() {
        return noOfInstPay;
    }

    public void setNoOfInstPay(Integer noOfInstPay) {
        this.noOfInstPay = noOfInstPay;
    }
    
    
}