/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSPrizedMoneyPaymentTO.java
 * 
 * Created on Wed Jun 22 17:24:46 IST 2011
 */
package com.see.truetransact.transferobject.gdsapplication.gdsprizedmoneypayment;

import com.see.truetransact.transferobject.mdsapplication.mdsprizedmoneypayment.*;
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is MDS_MONEY_PAYMENT_DETAILS.
 */
public class GDSPrizedMoneyPaymentTO extends TransferObject implements Serializable {

    private Integer divisionNo = 0;
    private Integer subNo = 0;
    private Integer totalInstallments = 0;
    private Integer noOfInstPaid = 0;
    private Integer noOfOverdueInst = 0;
    private Double overdueAmount = 0.0;
    private Integer prizedInstNo = 0;
    private Double bonusAmount = 0.0;
    private Double commisionAmount = 0.0;
    private Double refundAmount = 0.0;
    private Double discountAmount = 0.0;
    private Double noticeAmount = 0.0;
    private Double chargeAmount = 0.0;
    private Double aribitrationAmount = 0.0;
    private Double prizedAmount = 0.0;
    private Double netAmount = 0.0;
    private String transId = "";
    private String cashId = "";
    private String schemeName = "";
    private Integer schemeCount = 0;
    private String groupName = "";
    private String chittalNo = "";
    private String gdsNo = "";
    private Date drawAuctionDate = null;
    private String memberNo = "";
    private String memberName = "";
    private String branchCode = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String paidStatus = "";
    private String defaulters = "";
    private Double penalAmount = 0.0;
    private Double bonusRecovered = 0.0;
    private Double defaulter_bonus_recoverd = 0.0;
    private double paymentTaxAmt = 0.0;

  

    public String getDefaulter_marked() {
        return defaulter_marked;
    }

    public void setDefaulter_marked(String defaulter_marked) {
        this.defaulter_marked = defaulter_marked;
    }
    private Double defaulter_interst = 0.0;
    private Double defaulter_comm = 0.0;
    private String defaulter_marked = "";

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Setter/Getter for SCHEME_NAME - table Field
     * 
     */
    //Commented by shany
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSchemeName() {
        return schemeName;
    }
    
//    commented by shany

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
     * Setter/Getter for DRAW_AUCTION_DATE - table Field
     */
    public void setDrawAuctionDate(Date drawAuctionDate) {
        this.drawAuctionDate = drawAuctionDate;
    }

    public Date getDrawAuctionDate() {
        return drawAuctionDate;
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
     * Setter/Getter for MEMBER_NAME - table Field
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName() {
        return memberName;
    }

    /**
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
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
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
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
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
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
       // strB.append(getTOString("schemeName", schemeName));
        strB.append(getTOString("groupName", groupName));
       // strB.append(getTOString("chittalNo", chittalNo));
        strB.append(getTOString("gdsNo", gdsNo));
        strB.append(getTOString("divisionNo", divisionNo));
        strB.append(getTOString("subNo", subNo));
        strB.append(getTOString("drawAuctionDate", drawAuctionDate));
        strB.append(getTOString("totalInstallments", totalInstallments));
        strB.append(getTOString("noOfInstPaid", noOfInstPaid));
        strB.append(getTOString("noOfOverdueInst", noOfOverdueInst));
        strB.append(getTOString("memberNo", memberNo));
        strB.append(getTOString("memberName", memberName));
        strB.append(getTOString("overdueAmount", overdueAmount));
        strB.append(getTOString("prizedInstNo", prizedInstNo));
        strB.append(getTOString("bonusAmount", bonusAmount));
        strB.append(getTOString("penalAmount", penalAmount));
        strB.append(getTOString("bonusRecovered", bonusRecovered));
        strB.append(getTOString("commisionAmount", commisionAmount));
        strB.append(getTOString("refundAmount", refundAmount));
        strB.append(getTOString("discountAmount", discountAmount));
        strB.append(getTOString("noticeAmount", noticeAmount));
        strB.append(getTOString("chargeAmount", chargeAmount));
        strB.append(getTOString("aribitrationAmount", aribitrationAmount));
        strB.append(getTOString("defaulters", defaulters));
        strB.append(getTOString("prizedAmount", prizedAmount));
        strB.append(getTOString("netAmount", netAmount));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("cashId", cashId));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("paidStatus", paidStatus));
        strB.append(getTOString("paymentTaxAmt", paymentTaxAmt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
       // strB.append(getTOXml("schemeName", schemeName));
        strB.append(getTOXml("groupName", groupName));
        //strB.append(getTOXml("chittalNo", chittalNo));
        strB.append(getTOXml("gdsNo", gdsNo));
        strB.append(getTOXml("divisionNo", divisionNo));
        strB.append(getTOXml("subNo", subNo));
        strB.append(getTOXml("drawAuctionDate", drawAuctionDate));
        strB.append(getTOXml("totalInstallments", totalInstallments));
        strB.append(getTOXml("noOfInstPaid", noOfInstPaid));
        strB.append(getTOXml("noOfOverdueInst", noOfOverdueInst));
        strB.append(getTOXml("memberNo", memberNo));
        strB.append(getTOXml("memberName", memberName));
        strB.append(getTOXml("overdueAmount", overdueAmount));
        strB.append(getTOXml("prizedInstNo", prizedInstNo));
        strB.append(getTOXml("bonusAmount", bonusAmount));
        strB.append(getTOXml("penalAmount", penalAmount));
        strB.append(getTOXml("bonusRecovered", bonusRecovered));
        strB.append(getTOXml("commisionAmount", commisionAmount));
        strB.append(getTOXml("refundAmount", refundAmount));
        strB.append(getTOXml("discountAmount", discountAmount));
        strB.append(getTOXml("noticeAmount", noticeAmount));
        strB.append(getTOXml("chargeAmount", chargeAmount));
        strB.append(getTOXml("aribitrationAmount", aribitrationAmount));
        strB.append(getTOXml("defaulters", defaulters));
        strB.append(getTOXml("prizedAmount", prizedAmount));
        strB.append(getTOXml("netAmount", netAmount));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("cashId", cashId));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("paidStatus", paidStatus));
        strB.append(getTOString("paymentTaxAmt", paymentTaxAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

   
    /**
     * Getter for property transId.
     *
     * @return Value of property transId.
     */
    public java.lang.String getTransId() {
        return transId;
    }

    /**
     * Setter for property transId.
     *
     * @param transId New value of property transId.
     */
    public void setTransId(java.lang.String transId) {
        this.transId = transId;
    }

    /**
     * Getter for property cashId.
     *
     * @return Value of property cashId.
     */
    public java.lang.String getCashId() {
        return cashId;
    }

    /**
     * Setter for property cashId.
     *
     * @param cashId New value of property cashId.
     */
    public void setCashId(java.lang.String cashId) {
        this.cashId = cashId;
    }

    /**
     * Getter for property paidStatus.
     *
     * @return Value of property paidStatus.
     */
    public java.lang.String getPaidStatus() {
        return paidStatus;
    }

    /**
     * Setter for property paidStatus.
     *
     * @param paidStatus New value of property paidStatus.
     */
    public void setPaidStatus(java.lang.String paidStatus) {
        this.paidStatus = paidStatus;
    }

  
    /**
     * Getter for property defaulters.
     *
     * @return Value of property defaulters.
     */
    public java.lang.String getDefaulters() {
        return defaulters;
    }

    /**
     * Setter for property defaulters.
     *
     * @param defaulters New value of property defaulters.
     */
    public void setDefaulters(java.lang.String defaulters) {
        this.defaulters = defaulters;
    }

   
   
    public String getGdsNo() {
        return gdsNo;
    }

    public void setGdsNo(String gdsNo) {
        this.gdsNo = gdsNo;
    }

    public Integer getDivisionNo() {
        return divisionNo;
    }

    public void setDivisionNo(Integer divisionNo) {
        this.divisionNo = divisionNo;
    }

    public Integer getSubNo() {
        return subNo;
    }

    public void setSubNo(Integer subNo) {
        this.subNo = subNo;
    }

    public Integer getTotalInstallments() {
        return totalInstallments;
    }

    public void setTotalInstallments(Integer totalInstallments) {
        this.totalInstallments = totalInstallments;
    }

    public Integer getNoOfInstPaid() {
        return noOfInstPaid;
    }

    public void setNoOfInstPaid(Integer noOfInstPaid) {
        this.noOfInstPaid = noOfInstPaid;
    }

    public Integer getNoOfOverdueInst() {
        return noOfOverdueInst;
    }

    public void setNoOfOverdueInst(Integer noOfOverdueInst) {
        this.noOfOverdueInst = noOfOverdueInst;
    }

    public Double getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(Double overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public Integer getPrizedInstNo() {
        return prizedInstNo;
    }

    public void setPrizedInstNo(Integer prizedInstNo) {
        this.prizedInstNo = prizedInstNo;
    }

    public Double getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Double bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public Double getCommisionAmount() {
        return commisionAmount;
    }

    public void setCommisionAmount(Double commisionAmount) {
        this.commisionAmount = commisionAmount;
    }

    public Double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getNoticeAmount() {
        return noticeAmount;
    }

    public void setNoticeAmount(Double noticeAmount) {
        this.noticeAmount = noticeAmount;
    }

    public Double getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(Double chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public Double getAribitrationAmount() {
        return aribitrationAmount;
    }

    public void setAribitrationAmount(Double aribitrationAmount) {
        this.aribitrationAmount = aribitrationAmount;
    }

    public Double getPrizedAmount() {
        return prizedAmount;
    }

    public void setPrizedAmount(Double prizedAmount) {
        this.prizedAmount = prizedAmount;
    }

    public Double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public Integer getSchemeCount() {
        return schemeCount;
    }

    public void setSchemeCount(Integer schemeCount) {
        this.schemeCount = schemeCount;
    }

    public Double getPenalAmount() {
        return penalAmount;
    }

    public void setPenalAmount(Double penalAmount) {
        this.penalAmount = penalAmount;
    }

    public Double getBonusRecovered() {
        return bonusRecovered;
    }

    public void setBonusRecovered(Double bonusRecovered) {
        this.bonusRecovered = bonusRecovered;
    }

    public Double getDefaulter_bonus_recoverd() {
        return defaulter_bonus_recoverd;
    }

    public void setDefaulter_bonus_recoverd(Double defaulter_bonus_recoverd) {
        this.defaulter_bonus_recoverd = defaulter_bonus_recoverd;
    }

    public Double getDefaulter_interst() {
        return defaulter_interst;
    }

    public void setDefaulter_interst(Double defaulter_interst) {
        this.defaulter_interst = defaulter_interst;
    }

    public Double getDefaulter_comm() {
        return defaulter_comm;
    }

    public void setDefaulter_comm(Double defaulter_comm) {
        this.defaulter_comm = defaulter_comm;
    }

    public double getPaymentTaxAmt() {
        return paymentTaxAmt;
    }

    public void setPaymentTaxAmt(double paymentTaxAmt) {
        this.paymentTaxAmt = paymentTaxAmt;
    }

    
}