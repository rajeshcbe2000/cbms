/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigTO.java
 * 
 * Created on Thu Jan 20 16:44:08 IST 2005
 */
package com.see.truetransact.transferobject.transall;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class TransAllTO extends TransferObject implements Serializable {

    private String transallId = "";
    private String clockNo = "";
    private String memberNo = "";
    private String custName = "";
    private String schName = "";
    private String retired = "";
    private String acNo = null;
    private String prodId = "";
    private Double payingAmt = null;
    private Double principal = null;
    private Double penel = null;
    private Double interest = null;
    private Double totprincipal = null;
    private Double totPenel = null;
    private Double totInterest = null;
    private Double grandTotal = null;
    private Double others = null;
    private Double totOthers = null;
    private Double notice = null;
    private Double arbitration = null;
    private Double bonus = null;
    private String authorizeStatus = null;
    private String authorizeBy = null;
    private Date authorizeDte = null;
    private String status = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String transType = "";
    private Date createdDate = null;
    private String branchId = "";
    private Date authorizeDateTime = null;
    private String description = "";
    private String particulars = "";

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }
    
    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRetired(String retired) {
        this.retired = retired;
    }

    public String getRetired() {
        return retired;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public Date getAuthorizeDateTime() {
        return authorizeDateTime;
    }

    public void setAuthorizeDateTime(Date authorizeDateTime) {
        this.authorizeDateTime = authorizeDateTime;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(transallId);
        return transallId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("transallId", transallId));
        strB.append(getTOString("clockNo", clockNo));
        strB.append(getTOString("memberNo", memberNo));
        strB.append(getTOString("custName", custName));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("schName", schName));
        strB.append(getTOString("acNo", acNo));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("payingAmt", payingAmt));
        strB.append(getTOString("principal", principal));
        strB.append(getTOString("penel", penel));
        strB.append(getTOString("interest", interest));
        strB.append(getTOString("totprincipal", totprincipal));
        strB.append(getTOString("grandTotal", grandTotal));
        strB.append(getTOString("totInterest", totInterest));
        strB.append(getTOString("totPenel", totPenel));
        strB.append(getTOString("others", others));
        strB.append(getTOString("totOthers", totOthers));
        strB.append(getTOString("notice", notice));
        strB.append(getTOString("arbitration", arbitration));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDte", authorizeDte));
        strB.append(getTOString("status", status));
        strB.append(getTOString("retired", retired));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("transType", transType));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("transallId", transallId));
        strB.append(getTOXml("clockNo", clockNo));
        strB.append(getTOXml("memberNo", memberNo));
        strB.append(getTOXml("custName", custName));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("schName", schName));
        strB.append(getTOXml("acNo", acNo));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("payingAmt", payingAmt));
        strB.append(getTOXml("principal", principal));
        strB.append(getTOXml("penel", penel));
        strB.append(getTOXml("interest", interest));
        strB.append(getTOXml("totprincipal", totprincipal));
        strB.append(getTOXml("grandTotal", grandTotal));
        strB.append(getTOXml("totInterest", totInterest));
        strB.append(getTOXml("totPenel", totPenel));
        strB.append(getTOXml("others", others));
        strB.append(getTOXml("totOthers", totOthers));
        strB.append(getTOXml("notice", notice));
        strB.append(getTOXml("arbitration", arbitration));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDte", authorizeDte));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("retired", retired));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property transallId.
     *
     * @return Value of property transallId.
     */
    public java.lang.String getTransallId() {
        return transallId;
    }

    /**
     * Setter for property transallId.
     *
     * @param transallId New value of property transallId.
     */
    public void setTransallId(java.lang.String transallId) {
        this.transallId = transallId;
    }

    /**
     * Getter for property clockNo.
     *
     * @return Value of property clockNo.
     */
    public java.lang.String getClockNo() {
        return clockNo;
    }

    /**
     * Setter for property clockNo.
     *
     * @param clockNo New value of property clockNo.
     */
    public void setClockNo(java.lang.String clockNo) {
        this.clockNo = clockNo;
    }

    /**
     * Getter for property memberNo.
     *
     * @return Value of property memberNo.
     */
    public java.lang.String getMemberNo() {
        return memberNo;
    }

    /**
     * Setter for property memberNo.
     *
     * @param memberNo New value of property memberNo.
     */
    public void setMemberNo(java.lang.String memberNo) {
        this.memberNo = memberNo;
    }

    /**
     * Getter for property prodId.
     *
     * @return Value of property prodId.
     */
    public java.lang.String getProdId() {
        return prodId;
    }

    /**
     * Setter for property prodId.
     *
     * @param prodId New value of property prodId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }

    /**
     * Getter for property payingAmt.
     *
     * @return Value of property payingAmt.
     */
    public Double getPayingAmt() {
        return payingAmt;
    }

    /**
     * Setter for property payingAmt.
     *
     * @param payingAmt New value of property payingAmt.
     */
    public void setPayingAmt(Double payingAmt) {
        this.payingAmt = payingAmt;
    }

    /**
     * Getter for property penel.
     *
     * @return Value of property penel.
     */
    public java.lang.Double getPenel() {
        return penel;
    }

    /**
     * Setter for property penel.
     *
     * @param penel New value of property penel.
     */
    public void setPenel(java.lang.Double penel) {
        this.penel = penel;
    }

    /**
     * Getter for property interest.
     *
     * @return Value of property interest.
     */
    public java.lang.Double getInterest() {
        return interest;
    }

    /**
     * Setter for property interest.
     *
     * @param interest New value of property interest.
     */
    public void setInterest(java.lang.Double interest) {
        this.interest = interest;
    }

    /**
     * Getter for property totPenel.
     *
     * @return Value of property totPenel.
     */
    public java.lang.Double getTotPenel() {
        return totPenel;
    }

    /**
     * Setter for property totPenel.
     *
     * @param totPenel New value of property totPenel.
     */
    public void setTotPenel(java.lang.Double totPenel) {
        this.totPenel = totPenel;
    }

    /**
     * Getter for property totInterest.
     *
     * @return Value of property totInterest.
     */
    public java.lang.Double getTotInterest() {
        return totInterest;
    }

    /**
     * Setter for property totInterest.
     *
     * @param totInterest New value of property totInterest.
     */
    public void setTotInterest(java.lang.Double totInterest) {
        this.totInterest = totInterest;
    }

    /**
     * Getter for property grandTotal.
     *
     * @return Value of property grandTotal.
     */
    public java.lang.Double getGrandTotal() {
        return grandTotal;
    }

    /**
     * Setter for property grandTotal.
     *
     * @param grandTotal New value of property grandTotal.
     */
    public void setGrandTotal(java.lang.Double grandTotal) {
        this.grandTotal = grandTotal;
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
     * Getter for property authorizeDte.
     *
     * @return Value of property authorizeDte.
     */
    public java.util.Date getAuthorizeDte() {
        return authorizeDte;
    }

    /**
     * Setter for property authorizeDte.
     *
     * @param authorizeDte New value of property authorizeDte.
     */
    public void setAuthorizeDte(java.util.Date authorizeDte) {
        this.authorizeDte = authorizeDte;
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
     * Getter for property principal.
     *
     * @return Value of property principal.
     */
    public Double getPrincipal() {
        return principal;
    }

    /**
     * Setter for property principal.
     *
     * @param principal New value of property principal.
     */
    public void setPrincipal(Double principal) {
        this.principal = principal;
    }

    /**
     * Getter for property totprincipal.
     *
     * @return Value of property totprincipal.
     */
    public java.lang.Double getTotprincipal() {
        return totprincipal;
    }

    /**
     * Setter for property totprincipal.
     *
     * @param totprincipal New value of property totprincipal.
     */
    public void setTotprincipal(java.lang.Double totprincipal) {
        this.totprincipal = totprincipal;
    }

    /**
     * Getter for property totOthers.
     *
     * @return Value of property totOthers.
     */
    public java.lang.Double getTotOthers() {
        return totOthers;
    }

    /**
     * Setter for property totOthers.
     *
     * @param totOthers New value of property totOthers.
     */
    public void setTotOthers(java.lang.Double totOthers) {
        this.totOthers = totOthers;
    }

    /**
     * Getter for property others.
     *
     * @return Value of property others.
     */
    public java.lang.Double getOthers() {
        return others;
    }

    /**
     * Setter for property others.
     *
     * @param others New value of property others.
     */
    public void setOthers(java.lang.Double others) {
        this.others = others;
    }

    /**
     * Getter for property notice.
     *
     * @return Value of property notice.
     */
    public Double getNotice() {
        return notice;
    }

    /**
     * Setter for property notice.
     *
     * @param notice New value of property notice.
     */
    public void setNotice(Double notice) {
        this.notice = notice;
    }

    /**
     * Getter for property arbitration.
     *
     * @return Value of property arbitration.
     */
    public Double getArbitration() {
        return arbitration;
    }

    /**
     * Setter for property arbitration.
     *
     * @param arbitration New value of property arbitration.
     */
    public void setArbitration(Double arbitration) {
        this.arbitration = arbitration;
    }

    /**
     * Getter for property custName.
     *
     * @return Value of property custName.
     */
    public String getCustName() {
        return custName;
    }

    /**
     * Setter for property custName.
     *
     * @param custName New value of property custName.
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     * Getter for property schName.
     *
     * @return Value of property schName.
     */
    public java.lang.String getSchName() {
        return schName;
    }

    /**
     * Setter for property schName.
     *
     * @param schName New value of property schName.
     */
    public void setSchName(java.lang.String schName) {
        this.schName = schName;
    }

    /**
     * Getter for property acNo.
     *
     * @return Value of property acNo.
     */
    public java.lang.String getAcNo() {
        return acNo;
    }

    /**
     * Setter for property acNo.
     *
     * @param acNo New value of property acNo.
     */
    public void setAcNo(java.lang.String acNo) {
        this.acNo = acNo;
    }

    /**
     * Getter for property bonus.
     *
     * @return Value of property bonus.
     */
    public Double getBonus() {
        return bonus;
    }

    /**
     * Setter for property bonus.
     *
     * @param bonus New value of property bonus.
     */
    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }
}
