/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ExternalWireTO.java
 *
 * Created on Mon Jul 05 11:02:49 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.mdsapplication.mdsreceiptentry;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PVT_EXTERNAL_WIRE.
 */
public class MDSReceiptEntryTO extends TransferObject implements Serializable {

    private String schemeName = "";
    private String chittalNo = null;
    private String memberName = "";
    private Double divisionNo = null;
    private Date chitStartDt = null;
    private Date chitEndDt = null;
    private Integer noOfInst = 0;
    private Double subNo = null;
    private Double currInst = null;
    private Double instAmt = null;
    private Double pendingInst = null;
    private Double totalInstDue = null;
    private Double bonusAmtAvail = null;
    private String prizedMember = "";
    private Double noticeAmt = null;
    private Double arbitrationAmt = null;
    private Integer noOfInstPay = 0;
    private Double instAmtPayable = null;
    private Double penalAmtPayable = null;
    private Double bonusAmtPayable = null;
    private Double discountAmt = null;
    private Double mdsInterset = null;
    private Double netAmt = null;
    private Date paidDate = null;
    private Double paidInst = null;
    private Double paidAmt = null;
    private String thalayal = "";
    private String munnal = "";
    private String memberChanged = "";
    private String earlierMemberNo = "";
    private String earlierMemberName = "";
    private Double changedInstNo = null;
    private Date changedDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchCode = "";
    private String instTransId = "";
    private String penalTransId = "";
    private String bonusTransId = "";
    private String discountTransId = "";
    private String mdsTransId = "";
    private String netTransId = "";
    private String arbitrationId = "";
    private String noticeId = "";
    private String bankPay = "";
    private String narration = "";
    private String initiatedBranch = "";
    private String singleTransId = "";
    private String callingScreen = null;
    private Double serviceTaxAmt=0.0;
    private Double forfeitBonusAmtPayable = 0.0;
    private String forfeitBonusTransId = "";
    private Double bankAdvanceAmt = 0.0;
    private Double penalWaiveAmt = 0.0;
    private Double arcWaiveAmt = 0.0;
    private Double noticeWaiveAmt = 0.0;
    private String penalWaiveId = "";
    private String arcWaiveId = "";
    private String noticeWaiveId = "";
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
        strB.append(getTOString("schemeName", schemeName));
        strB.append(getTOString("chittalNo", chittalNo));
        strB.append(getTOString("memberName", memberName));
        strB.append(getTOString("divisionNo", divisionNo));
        strB.append(getTOString("chitStartDt", chitStartDt));
        strB.append(getTOString("chitEndDt", chitEndDt));
        strB.append(getTOString("noOfInst", noOfInst));
        strB.append(getTOString("subNo", subNo));
        strB.append(getTOString("currInst", currInst));
        strB.append(getTOString("instAmt", instAmt));
        strB.append(getTOString("pendingInst", pendingInst));
        strB.append(getTOString("totalInstDue", totalInstDue));
        strB.append(getTOString("bonusAmtAvail", bonusAmtAvail));
        strB.append(getTOString("prizedMember", prizedMember));
        strB.append(getTOString("noticeAmt", noticeAmt));
        strB.append(getTOString("arbitrationAmt", arbitrationAmt));
        strB.append(getTOString("noOfInstPay", noOfInstPay));
        strB.append(getTOString("instAmtPayable", instAmtPayable));
        strB.append(getTOString("penalAmtPayable", penalAmtPayable));
        strB.append(getTOString("bonusAmtPayable", bonusAmtPayable));
        strB.append(getTOString("discountAmt", discountAmt));
        strB.append(getTOString("mdsInterset", mdsInterset));
        strB.append(getTOString("netAmt", netAmt));
        strB.append(getTOString("paidDate", paidDate));
        strB.append(getTOString("paidInst", paidInst));
        strB.append(getTOString("paidAmt", paidAmt));
        strB.append(getTOString("thalayal", thalayal));
        strB.append(getTOString("munnal", munnal));
        strB.append(getTOString("memberChanged", memberChanged));
        strB.append(getTOString("earlierMemberNo", earlierMemberNo));
        strB.append(getTOString("earlierMemberName", earlierMemberName));
        strB.append(getTOString("changedInstNo", changedInstNo));
        strB.append(getTOString("changedDt", changedDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("instTransId", instTransId));
        strB.append(getTOString("penalTransId", penalTransId));
        strB.append(getTOString("bonusTransId", bonusTransId));
        strB.append(getTOString("discountTransId", discountTransId));
        strB.append(getTOString("mdsTransId", mdsTransId));
        strB.append(getTOString("netTransId", netTransId));
        strB.append(getTOString("bankPay", bankPay));
        strB.append(getTOString("noticeId", noticeId));
        strB.append(getTOString("arbitrationId", arbitrationId));
        strB.append(getTOString("narration", narration));
        strB.append(getTOString("singleTransId",singleTransId));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("callingScreen", callingScreen));
        strB.append(getTOString("serviceTaxAmt", serviceTaxAmt));
        strB.append(getTOString("forfeitBonusAmtPayable", forfeitBonusAmtPayable));
        strB.append(getTOString("forfeitBonusTransId", forfeitBonusTransId));
        strB.append(getTOString("bankAdvanceAmt", bankAdvanceAmt));
        strB.append(getTOString("penalWaiveAmt", penalWaiveAmt));
        strB.append(getTOString("arcWaiveAmt", arcWaiveAmt));
        strB.append(getTOString("noticeWaiveAmt", noticeWaiveAmt));
        strB.append(getTOString("penalWaiveId", penalWaiveId));
        strB.append(getTOString("arcWaiveId", arcWaiveId));
        strB.append(getTOString("noticeWaiveId", noticeWaiveId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("schemeName", schemeName));
        strB.append(getTOXml("chittalNo", chittalNo));
        strB.append(getTOXml("memberName", memberName));
        strB.append(getTOXml("divisionNo", divisionNo));
        strB.append(getTOXml("chitStartDt", chitStartDt));
        strB.append(getTOXml("chitEndDt", chitEndDt));
        strB.append(getTOXml("noOfInst", noOfInst));
        strB.append(getTOXml("subNo", subNo));
        strB.append(getTOXml("currInst", currInst));
        strB.append(getTOXml("instAmt", instAmt));
        strB.append(getTOXml("pendingInst", pendingInst));
        strB.append(getTOXml("totalInstDue", totalInstDue));
        strB.append(getTOXml("bonusAmtAvail", bonusAmtAvail));
        strB.append(getTOXml("prizedMember", prizedMember));
        strB.append(getTOXml("noticeAmt", noticeAmt));
        strB.append(getTOXml("arbitrationAmt", arbitrationAmt));
        strB.append(getTOXml("noOfInstPay", noOfInstPay));
        strB.append(getTOXml("instAmtPayable", instAmtPayable));
        strB.append(getTOXml("penalAmtPayable", penalAmtPayable));
        strB.append(getTOXml("bonusAmtPayable", bonusAmtPayable));
        strB.append(getTOXml("discountAmt", discountAmt));
        strB.append(getTOXml("mdsInterset", mdsInterset));
        strB.append(getTOXml("netAmt", netAmt));
        strB.append(getTOXml("paidDate", paidDate));
        strB.append(getTOXml("paidInst", paidInst));
        strB.append(getTOXml("paidAmt", paidAmt));
        strB.append(getTOXml("thalayal", thalayal));
        strB.append(getTOXml("munnal", munnal));
        strB.append(getTOXml("memberChanged", memberChanged));
        strB.append(getTOXml("earlierMemberNo", earlierMemberNo));
        strB.append(getTOXml("earlierMemberName", earlierMemberName));
        strB.append(getTOXml("changedInstNo", changedInstNo));
        strB.append(getTOXml("changedDt", changedDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("instTransId", instTransId));
        strB.append(getTOXml("penalTransId", penalTransId));
        strB.append(getTOXml("bonusTransId", bonusTransId));
        strB.append(getTOXml("discountTransId", discountTransId));
        strB.append(getTOXml("mdsTransId", mdsTransId));
        strB.append(getTOXml("netTransId", netTransId));
        strB.append(getTOXml("bankPay", bankPay));
        strB.append(getTOXml("arbitrationId", arbitrationId));
        strB.append(getTOXml("noticeId", noticeId));
        strB.append(getTOXml("narration", narration));
        strB.append(getTOXml("singleTransId", singleTransId));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXml("callingScreen", callingScreen));
        strB.append(getTOXml("serviceTaxAmt", serviceTaxAmt));
        strB.append(getTOXml("forfeitBonusAmtPayable", forfeitBonusAmtPayable));
        strB.append(getTOXml("forfeitBonusTransId", forfeitBonusTransId));
        strB.append(getTOXml("bankAdvanceAmt", bankAdvanceAmt));
        strB.append(getTOXml("penalWaiveAmt", penalWaiveAmt));
        strB.append(getTOXml("arcWaiveAmt", arcWaiveAmt));
        strB.append(getTOXml("noticeWaiveAmt", noticeWaiveAmt));
        strB.append(getTOXml("penalWaiveId", penalWaiveId));
        strB.append(getTOXml("arcWaiveId", arcWaiveId));
        strB.append(getTOXml("noticeWaiveId", noticeWaiveId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property schemeName.
     *
     * @return Value of property schemeName.
     */
    public java.lang.String getSchemeName() {
        return schemeName;
    }

    public String getSingleTransId() {
        return singleTransId;
    }

    public void setSingleTransId(String singleTransId) {
        this.singleTransId = singleTransId;
    }

    public Double getServiceTaxAmt() {
        return serviceTaxAmt;
    }

    public void setServiceTaxAmt(Double serviceTaxAmt) {
        this.serviceTaxAmt = serviceTaxAmt;
    }

    /**
     * Setter for property schemeName.
     *
     * @param schemeName New value of property schemeName.
     */
    public void setSchemeName(java.lang.String schemeName) {
        this.schemeName = schemeName;
    }

    /**
     * Getter for property memberName.
     *
     * @return Value of property memberName.
     */
    public java.lang.String getMemberName() {
        return memberName;
    }

    /**
     * Setter for property memberName.
     *
     * @param memberName New value of property memberName.
     */
    public void setMemberName(java.lang.String memberName) {
        this.memberName = memberName;
    }

    /**
     * Getter for property divisionNo.
     *
     * @return Value of property divisionNo.
     */
    public java.lang.Double getDivisionNo() {
        return divisionNo;
    }

    /**
     * Setter for property divisionNo.
     *
     * @param divisionNo New value of property divisionNo.
     */
    public void setDivisionNo(java.lang.Double divisionNo) {
        this.divisionNo = divisionNo;
    }

    /**
     * Getter for property chitStartDt.
     *
     * @return Value of property chitStartDt.
     */
    public java.util.Date getChitStartDt() {
        return chitStartDt;
    }

    /**
     * Setter for property chitStartDt.
     *
     * @param chitStartDt New value of property chitStartDt.
     */
    public void setChitStartDt(java.util.Date chitStartDt) {
        this.chitStartDt = chitStartDt;
    }

    /**
     * Getter for property chitEndDt.
     *
     * @return Value of property chitEndDt.
     */
    public java.util.Date getChitEndDt() {
        return chitEndDt;
    }

    /**
     * Setter for property chitEndDt.
     *
     * @param chitEndDt New value of property chitEndDt.
     */
    public void setChitEndDt(java.util.Date chitEndDt) {
        this.chitEndDt = chitEndDt;
    }

    /**
     * Getter for property currInst.
     *
     * @return Value of property currInst.
     */
    public java.lang.Double getCurrInst() {
        return currInst;
    }

    /**
     * Setter for property currInst.
     *
     * @param currInst New value of property currInst.
     */
    public void setCurrInst(java.lang.Double currInst) {
        this.currInst = currInst;
    }

    /**
     * Getter for property instAmt.
     *
     * @return Value of property instAmt.
     */
    public java.lang.Double getInstAmt() {
        return instAmt;
    }

    /**
     * Setter for property instAmt.
     *
     * @param instAmt New value of property instAmt.
     */
    public void setInstAmt(java.lang.Double instAmt) {
        this.instAmt = instAmt;
    }

    /**
     * Getter for property pendingInst.
     *
     * @return Value of property pendingInst.
     */
    public java.lang.Double getPendingInst() {
        return pendingInst;
    }

    /**
     * Setter for property pendingInst.
     *
     * @param pendingInst New value of property pendingInst.
     */
    public void setPendingInst(java.lang.Double pendingInst) {
        this.pendingInst = pendingInst;
    }

    /**
     * Getter for property totalInstDue.
     *
     * @return Value of property totalInstDue.
     */
    public java.lang.Double getTotalInstDue() {
        return totalInstDue;
    }

    /**
     * Setter for property totalInstDue.
     *
     * @param totalInstDue New value of property totalInstDue.
     */
    public void setTotalInstDue(java.lang.Double totalInstDue) {
        this.totalInstDue = totalInstDue;
    }

    /**
     * Getter for property bonusAmtAvail.
     *
     * @return Value of property bonusAmtAvail.
     */
    public java.lang.Double getBonusAmtAvail() {
        return bonusAmtAvail;
    }

    /**
     * Setter for property bonusAmtAvail.
     *
     * @param bonusAmtAvail New value of property bonusAmtAvail.
     */
    public void setBonusAmtAvail(java.lang.Double bonusAmtAvail) {
        this.bonusAmtAvail = bonusAmtAvail;
    }

    /**
     * Getter for property prizedMember.
     *
     * @return Value of property prizedMember.
     */
    public java.lang.String getPrizedMember() {
        return prizedMember;
    }

    /**
     * Setter for property prizedMember.
     *
     * @param prizedMember New value of property prizedMember.
     */
    public void setPrizedMember(java.lang.String prizedMember) {
        this.prizedMember = prizedMember;
    }

    /**
     * Getter for property noticeAmt.
     *
     * @return Value of property noticeAmt.
     */
    public java.lang.Double getNoticeAmt() {
        return noticeAmt;
    }

    /**
     * Setter for property noticeAmt.
     *
     * @param noticeAmt New value of property noticeAmt.
     */
    public void setNoticeAmt(java.lang.Double noticeAmt) {
        this.noticeAmt = noticeAmt;
    }

    /**
     * Getter for property arbitrationAmt.
     *
     * @return Value of property arbitrationAmt.
     */
    public java.lang.Double getArbitrationAmt() {
        return arbitrationAmt;
    }

    /**
     * Setter for property arbitrationAmt.
     *
     * @param arbitrationAmt New value of property arbitrationAmt.
     */
    public void setArbitrationAmt(java.lang.Double arbitrationAmt) {
        this.arbitrationAmt = arbitrationAmt;
    }

    /**
     * Getter for property instAmtPayable.
     *
     * @return Value of property instAmtPayable.
     */
    public java.lang.Double getInstAmtPayable() {
        return instAmtPayable;
    }

    /**
     * Setter for property instAmtPayable.
     *
     * @param instAmtPayable New value of property instAmtPayable.
     */
    public void setInstAmtPayable(java.lang.Double instAmtPayable) {
        this.instAmtPayable = instAmtPayable;
    }

    /**
     * Getter for property penalAmtPayable.
     *
     * @return Value of property penalAmtPayable.
     */
    public java.lang.Double getPenalAmtPayable() {
        return penalAmtPayable;
    }

    /**
     * Setter for property penalAmtPayable.
     *
     * @param penalAmtPayable New value of property penalAmtPayable.
     */
    public void setPenalAmtPayable(java.lang.Double penalAmtPayable) {
        this.penalAmtPayable = penalAmtPayable;
    }

    /**
     * Getter for property bonusAmtPayable.
     *
     * @return Value of property bonusAmtPayable.
     */
    public java.lang.Double getBonusAmtPayable() {
        return bonusAmtPayable;
    }

    /**
     * Setter for property bonusAmtPayable.
     *
     * @param bonusAmtPayable New value of property bonusAmtPayable.
     */
    public void setBonusAmtPayable(java.lang.Double bonusAmtPayable) {
        this.bonusAmtPayable = bonusAmtPayable;
    }

    /**
     * Getter for property discountAmt.
     *
     * @return Value of property discountAmt.
     */
    public java.lang.Double getDiscountAmt() {
        return discountAmt;
    }

    /**
     * Setter for property discountAmt.
     *
     * @param discountAmt New value of property discountAmt.
     */
    public void setDiscountAmt(java.lang.Double discountAmt) {
        this.discountAmt = discountAmt;
    }

    /**
     * Getter for property mdsInterset.
     *
     * @return Value of property mdsInterset.
     */
    public java.lang.Double getMdsInterset() {
        return mdsInterset;
    }

    /**
     * Setter for property mdsInterset.
     *
     * @param mdsInterset New value of property mdsInterset.
     */
    public void setMdsInterset(java.lang.Double mdsInterset) {
        this.mdsInterset = mdsInterset;
    }

    /**
     * Getter for property netAmt.
     *
     * @return Value of property netAmt.
     */
    public java.lang.Double getNetAmt() {
        return netAmt;
    }

    /**
     * Setter for property netAmt.
     *
     * @param netAmt New value of property netAmt.
     */
    public void setNetAmt(java.lang.Double netAmt) {
        this.netAmt = netAmt;
    }

    /**
     * Getter for property paidDate.
     *
     * @return Value of property paidDate.
     */
    public java.util.Date getPaidDate() {
        return paidDate;
    }

    /**
     * Setter for property paidDate.
     *
     * @param paidDate New value of property paidDate.
     */
    public void setPaidDate(java.util.Date paidDate) {
        this.paidDate = paidDate;
    }

    /**
     * Getter for property paidInst.
     *
     * @return Value of property paidInst.
     */
    public java.lang.Double getPaidInst() {
        return paidInst;
    }

    /**
     * Setter for property paidInst.
     *
     * @param paidInst New value of property paidInst.
     */
    public void setPaidInst(java.lang.Double paidInst) {
        this.paidInst = paidInst;
    }

    /**
     * Getter for property paidAmt.
     *
     * @return Value of property paidAmt.
     */
    public java.lang.Double getPaidAmt() {
        return paidAmt;
    }

    /**
     * Setter for property paidAmt.
     *
     * @param paidAmt New value of property paidAmt.
     */
    public void setPaidAmt(java.lang.Double paidAmt) {
        this.paidAmt = paidAmt;
    }

    /**
     * Getter for property thalayal.
     *
     * @return Value of property thalayal.
     */
    public java.lang.String getThalayal() {
        return thalayal;
    }

    /**
     * Setter for property thalayal.
     *
     * @param thalayal New value of property thalayal.
     */
    public void setThalayal(java.lang.String thalayal) {
        this.thalayal = thalayal;
    }

    /**
     * Getter for property munnal.
     *
     * @return Value of property munnal.
     */
    public java.lang.String getMunnal() {
        return munnal;
    }

    /**
     * Setter for property munnal.
     *
     * @param munnal New value of property munnal.
     */
    public void setMunnal(java.lang.String munnal) {
        this.munnal = munnal;
    }

    /**
     * Getter for property memberChanged.
     *
     * @return Value of property memberChanged.
     */
    public java.lang.String getMemberChanged() {
        return memberChanged;
    }

    /**
     * Setter for property memberChanged.
     *
     * @param memberChanged New value of property memberChanged.
     */
    public void setMemberChanged(java.lang.String memberChanged) {
        this.memberChanged = memberChanged;
    }

    /**
     * Getter for property earlierMemberNo.
     *
     * @return Value of property earlierMemberNo.
     */
    public java.lang.String getEarlierMemberNo() {
        return earlierMemberNo;
    }

    /**
     * Setter for property earlierMemberNo.
     *
     * @param earlierMemberNo New value of property earlierMemberNo.
     */
    public void setEarlierMemberNo(java.lang.String earlierMemberNo) {
        this.earlierMemberNo = earlierMemberNo;
    }

    /**
     * Getter for property earlierMemberName.
     *
     * @return Value of property earlierMemberName.
     */
    public java.lang.String getEarlierMemberName() {
        return earlierMemberName;
    }

    /**
     * Setter for property earlierMemberName.
     *
     * @param earlierMemberName New value of property earlierMemberName.
     */
    public void setEarlierMemberName(java.lang.String earlierMemberName) {
        this.earlierMemberName = earlierMemberName;
    }

    /**
     * Getter for property changedInstNo.
     *
     * @return Value of property changedInstNo.
     */
    public java.lang.Double getChangedInstNo() {
        return changedInstNo;
    }

    /**
     * Setter for property changedInstNo.
     *
     * @param changedInstNo New value of property changedInstNo.
     */
    public void setChangedInstNo(java.lang.Double changedInstNo) {
        this.changedInstNo = changedInstNo;
    }

    /**
     * Getter for property changedDt.
     *
     * @return Value of property changedDt.
     */
    public java.util.Date getChangedDt() {
        return changedDt;
    }

    /**
     * Setter for property changedDt.
     *
     * @param changedDt New value of property changedDt.
     */
    public void setChangedDt(java.util.Date changedDt) {
        this.changedDt = changedDt;
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
     * Getter for property authorizeDt.
     *
     * @return Value of property authorizeDt.
     */
    public java.util.Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter for property authorizeDt.
     *
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.util.Date authorizeDt) {
        this.authorizeDt = authorizeDt;
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
     * Getter for property chittalNo.
     *
     * @return Value of property chittalNo.
     */
    public java.lang.String getChittalNo() {
        return chittalNo;
    }

    /**
     * Setter for property chittalNo.
     *
     * @param chittalNo New value of property chittalNo.
     */
    public void setChittalNo(java.lang.String chittalNo) {
        this.chittalNo = chittalNo;
    }

    /**
     * Getter for property instTransId.
     *
     * @return Value of property instTransId.
     */
    public java.lang.String getInstTransId() {
        return instTransId;
    }

    /**
     * Setter for property instTransId.
     *
     * @param instTransId New value of property instTransId.
     */
    public void setInstTransId(java.lang.String instTransId) {
        this.instTransId = instTransId;
    }

    /**
     * Getter for property penalTransId.
     *
     * @return Value of property penalTransId.
     */
    public java.lang.String getPenalTransId() {
        return penalTransId;
    }

    /**
     * Setter for property penalTransId.
     *
     * @param penalTransId New value of property penalTransId.
     */
    public void setPenalTransId(java.lang.String penalTransId) {
        this.penalTransId = penalTransId;
    }

    /**
     * Getter for property bonusTransId.
     *
     * @return Value of property bonusTransId.
     */
    public java.lang.String getBonusTransId() {
        return bonusTransId;
    }

    /**
     * Setter for property bonusTransId.
     *
     * @param bonusTransId New value of property bonusTransId.
     */
    public void setBonusTransId(java.lang.String bonusTransId) {
        this.bonusTransId = bonusTransId;
    }

    /**
     * Getter for property discountTransId.
     *
     * @return Value of property discountTransId.
     */
    public java.lang.String getDiscountTransId() {
        return discountTransId;
    }

    /**
     * Setter for property discountTransId.
     *
     * @param discountTransId New value of property discountTransId.
     */
    public void setDiscountTransId(java.lang.String discountTransId) {
        this.discountTransId = discountTransId;
    }

    /**
     * Getter for property mdsTransId.
     *
     * @return Value of property mdsTransId.
     */
    public java.lang.String getMdsTransId() {
        return mdsTransId;
    }

    /**
     * Setter for property mdsTransId.
     *
     * @param mdsTransId New value of property mdsTransId.
     */
    public void setMdsTransId(java.lang.String mdsTransId) {
        this.mdsTransId = mdsTransId;
    }

    /**
     * Getter for property netTransId.
     *
     * @return Value of property netTransId.
     */
    public java.lang.String getNetTransId() {
        return netTransId;
    }

    /**
     * Setter for property netTransId.
     *
     * @param netTransId New value of property netTransId.
     */
    public void setNetTransId(java.lang.String netTransId) {
        this.netTransId = netTransId;
    }

    /**
     * Getter for property bankPay.
     *
     * @return Value of property bankPay.
     */
    public java.lang.String getBankPay() {
        return bankPay;
    }

    /**
     * Setter for property bankPay.
     *
     * @param bankPay New value of property bankPay.
     */
    public void setBankPay(java.lang.String bankPay) {
        this.bankPay = bankPay;
    }

    /**
     * Getter for property arbitrationId.
     *
     * @return Value of property arbitrationId.
     */
    public java.lang.String getArbitrationId() {
        return arbitrationId;
    }

    /**
     * Setter for property arbitrationId.
     *
     * @param arbitrationId New value of property arbitrationId.
     */
    public void setArbitrationId(java.lang.String arbitrationId) {
        this.arbitrationId = arbitrationId;
    }

    /**
     * Getter for property noticeId.
     *
     * @return Value of property noticeId.
     */
    public java.lang.String getNoticeId() {
        return noticeId;
    }

    /**
     * Setter for property noticeId.
     *
     * @param noticeId New value of property noticeId.
     */
    public void setNoticeId(java.lang.String noticeId) {
        this.noticeId = noticeId;
    }

    /**
     * Getter for property narration.
     *
     * @return Value of property narration.
     */
    public java.lang.String getNarration() {
        return narration;
    }

    /**
     * Setter for property narration.
     *
     * @param narration New value of property narration.
     */
    public void setNarration(java.lang.String narration) {
        this.narration = narration;
    }

    /**
     * Getter for property initiatedBranch.
     *
     * @return Value of property initiatedBranch.
     */
    public java.lang.String getInitiatedBranch() {
        return initiatedBranch;
    }

    /**
     * Setter for property initiatedBranch.
     *
     * @param initiatedBranch New value of property initiatedBranch.
     */
    public void setInitiatedBranch(java.lang.String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }

    /**
     * Getter for property subNo.
     *
     * @return Value of property subNo.
     */
    public java.lang.Double getSubNo() {
        return subNo;
    }

    /**
     * Setter for property subNo.
     *
     * @param subNo New value of property subNo.
     */
    public void setSubNo(java.lang.Double subNo) {
        this.subNo = subNo;
    }
    
    /**
     * Getter for property callingScreen.
     *
     * @return Value of property callingScreen.
     */
    public java.lang.String getCallingScreen() {
        return callingScreen;
    }

    /**
     * Setter for property callingScreen.
     *
     * @param callingScreen New value of property callingScreen.
     */
    public void setCallingScreen(java.lang.String callingScreen) {
        this.callingScreen = callingScreen;
    }

    public Integer getNoOfInst() {
        return noOfInst;
    }

    public void setNoOfInst(Integer noOfInst) {
        this.noOfInst = noOfInst;
    }

    public Integer getNoOfInstPay() {
        return noOfInstPay;
    }

    public void setNoOfInstPay(Integer noOfInstPay) {
        this.noOfInstPay = noOfInstPay;
    }

    public Double getForfeitBonusAmtPayable() {
        return forfeitBonusAmtPayable;
    }

    public void setForfeitBonusAmtPayable(Double forfeitBonusAmtPayable) {
        this.forfeitBonusAmtPayable = forfeitBonusAmtPayable;
    }

    public String getForfeitBonusTransId() {
        return forfeitBonusTransId;
    }

    public void setForfeitBonusTransId(String forfeitBonusTransId) {
        this.forfeitBonusTransId = forfeitBonusTransId;
    }

    public Double getBankAdvanceAmt() {
        return bankAdvanceAmt;
    }

    public void setBankAdvanceAmt(Double bankAdvanceAmt) {
        this.bankAdvanceAmt = bankAdvanceAmt;
    }    

    public Double getPenalWaiveAmt() {
        return penalWaiveAmt;
    }

    public void setPenalWaiveAmt(Double penalWaiveAmt) {
        this.penalWaiveAmt = penalWaiveAmt;
    }

    public Double getArcWaiveAmt() {
        return arcWaiveAmt;
    }

    public void setArcWaiveAmt(Double arcWaiveAmt) {
        this.arcWaiveAmt = arcWaiveAmt;
    }

    public Double getNoticeWaiveAmt() {
        return noticeWaiveAmt;
    }

    public void setNoticeWaiveAmt(Double noticeWaiveAmt) {
        this.noticeWaiveAmt = noticeWaiveAmt;
    }

    public String getPenalWaiveId() {
        return penalWaiveId;
    }

    public void setPenalWaiveId(String penalWaiveId) {
        this.penalWaiveId = penalWaiveId;
    }

    public String getArcWaiveId() {
        return arcWaiveId;
    }

    public void setArcWaiveId(String arcWaiveId) {
        this.arcWaiveId = arcWaiveId;
    }

    public String getNoticeWaiveId() {
        return noticeWaiveId;
    }

    public void setNoticeWaiveId(String noticeWaiveId) {
        this.noticeWaiveId = noticeWaiveId;
    }
    
     
    
    
}