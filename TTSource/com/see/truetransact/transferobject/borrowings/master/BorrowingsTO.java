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
package com.see.truetransact.transferobject.borrowings.master;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class BorrowingsTO extends TransferObject implements Serializable {

    private String borrowingNo = "";
    private String agencyCode = "";
    private String borrowingrefNo = "";
    private String type = null;
    private String description = null;
    private Date sanctionDate = null;
    private Double sanctionAmt = null;
    private Double amtBorrowed = null;
    private Double rateofInt = null;
    private Double noofInstallments = null;
    private String prinRepFrq = null;
    private String intRepFrq = "";
    private String morotorium = "";
    private Date sanctionExpDate = null;
    private String secDetails = null;
    private String prinGrpHead = null;
    private String intGrpHead = null;
    private String penGrpHead = null;
    private String chargeGrpHead = null;
    private Double amount = null; //trans details
    private Double availBalance = null;
    private String sanOrderNo = null;
    private Date sanDate = null;
    private String govtLoan = "";
    //cheque details
       /*
     * private String investmentId = ""; private String slNo = ""; private Date
     * issueDt = null; private String fromNo = ""; private String toNo = "";
     * private String noOfCheques = ""; private String statusBy = ""; private
     * Date statusDt = null; private String authorizedStatus = ""; private
     * String authorizedBy = ""; private Date authorizedDt = null; //end...
     */

    public String getGovtLoan() {
        return govtLoan;
    }

    public void setGovtLoan(String govtLoan) {
        this.govtLoan = govtLoan;
    }

    public Date getSanDate() {
        return sanDate;
    }

    public void setSanDate(Date sanDate) {
        this.sanDate = sanDate;
    }

    public String getSanOrderNo() {
        return sanOrderNo;
    }

    public void setSanOrderNo(String sanOrderNo) {
        this.sanOrderNo = sanOrderNo;
    }
    private String authorizeStatus = null;
    private String authorizeBy = null;
    //Added by Anju 14/05/2014
    private String createdBy = null;
    private Date authorizeDte = null;
    private String status = null;
    private String penalIntRate = null;
    private String multiDis = null;
    private String renReq = null;

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(borrowingNo);
        return borrowingNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowingNo", borrowingNo));
        strB.append(getTOString("agencyCode", agencyCode));
        strB.append(getTOString("borrowingrefNo", borrowingrefNo));
        strB.append(getTOString("type", type));
        strB.append(getTOString("description", description));
        strB.append(getTOString("sanctionDate", sanctionDate));
        strB.append(getTOString("sanctionAmt", sanctionAmt));
        strB.append(getTOString("amtBorrowed", amtBorrowed));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("rateofInt", rateofInt));
        strB.append(getTOString("noofInstallments", noofInstallments));
        strB.append(getTOString("prinRepFrq", prinRepFrq));
        strB.append(getTOString("intRepFrq", intRepFrq));
        strB.append(getTOString("morotorium", morotorium));
        strB.append(getTOString("sanctionExpDate", sanctionExpDate));
        strB.append(getTOString("secDetails", secDetails));
        strB.append(getTOString("prinGrpHead", prinGrpHead));
        strB.append(getTOString("intGrpHead", intGrpHead));
        strB.append(getTOString("penGrpHead", penGrpHead));
        strB.append(getTOString("chargeGrpHead", chargeGrpHead));
        strB.append(getTOString("penalIntRate", penalIntRate));
        strB.append(getTOString("multiDis", multiDis));
        strB.append(getTOString("renReq", renReq));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("authorizeDte", authorizeDte));
        strB.append(getTOString("status", status));
        strB.append(getTOString("govtLoan", govtLoan));
        //cheque details
               /*
         * strB.append(getTOString("investmentId", investmentId));
         * strB.append(getTOString("slNo", slNo));
         * strB.append(getTOString("issueDt", issueDt));
         * strB.append(getTOString("fromNo", fromNo));
         * strB.append(getTOString("toNo", toNo));
         * strB.append(getTOString("noOfCheques", noOfCheques));
         * strB.append(getTOString("statusBy", statusBy));
         * strB.append(getTOString("statusDt", statusDt));
         * strB.append(getTOString("authorizedStatus", authorizedStatus));
         * strB.append(getTOString("authorizedBy", authorizedBy));
         * strB.append(getTOString("authorizedDt", authorizedDt)); //end...
         */
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("borrowingNo ", borrowingNo));
        strB.append(getTOXml("agencyCode", agencyCode));
        strB.append(getTOXml("borrowingrefNo", borrowingrefNo));
        strB.append(getTOXml("type", type));
        strB.append(getTOXml("description", description));
        strB.append(getTOXml("sanctionDate", sanctionDate));
        strB.append(getTOXml("sanctionAmt", sanctionAmt));
        strB.append(getTOXml("amtBorrowed", amtBorrowed));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("rateofInt", rateofInt));
        strB.append(getTOXml("noofInstallments", noofInstallments));
        strB.append(getTOXml("prinRepFrq", prinRepFrq));
        strB.append(getTOXml("intRepFrq", intRepFrq));
        strB.append(getTOXml("morotorium", morotorium));
        strB.append(getTOXml("sanctionExpDate", sanctionExpDate));
        strB.append(getTOXml("secDetails", secDetails));
        strB.append(getTOXml("prinGrpHead", prinGrpHead));
        strB.append(getTOXml("intGrpHead", intGrpHead));
        strB.append(getTOXml("penGrpHead", penGrpHead));
        strB.append(getTOXml("chargeGrpHead", chargeGrpHead));
        strB.append(getTOXml("penalIntRate", penalIntRate));
        strB.append(getTOXml("multiDis", multiDis));
        strB.append(getTOXml("renReq", renReq));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("authorizeDte", authorizeDte));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("govtLoan", govtLoan));
        //cheque details
               /*
         * strB.append(getTOXml("investmentId", investmentId));
         * strB.append(getTOXml("slNo", slNo)); strB.append(getTOXml("issueDt",
         * issueDt)); strB.append(getTOXml("fromNo", fromNo));
         * strB.append(getTOXml("toNo", toNo));
         * strB.append(getTOXml("noOfCheques", noOfCheques));
         * strB.append(getTOXml("status", status));
         * strB.append(getTOXml("statusBy", statusBy));
         * strB.append(getTOXml("statusDt", statusDt));
         * strB.append(getTOXml("authorizedStatus", authorizedStatus));
         * strB.append(getTOXml("authorizedBy", authorizedBy));
         * strB.append(getTOXml("authorizedDt", authorizedDt)); //end...
         */
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property borrowingNo.
     *
     * @return Value of property borrowingNo.
     */
    public java.lang.String getBorrowingNo() {
        return borrowingNo;
    }

    /**
     * Setter for property borrowingNo.
     *
     * @param borrowingNo New value of property borrowingNo.
     */
    public void setBorrowingNo(java.lang.String borrowingNo) {
        this.borrowingNo = borrowingNo;
    }

    /**
     * Getter for property agencyCode.
     *
     * @return Value of property agencyCode.
     */
    public java.lang.String getAgencyCode() {
        return agencyCode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Setter for property agencyCode.
     *
     * @param agencyCode New value of property agencyCode.
     */
    public void setAgencyCode(java.lang.String agencyCode) {
        this.agencyCode = agencyCode;
    }

    /**
     * Getter for property borrowingrefNo.
     *
     * @return Value of property borrowingrefNo.
     */
    public java.lang.String getBorrowingrefNo() {
        return borrowingrefNo;
    }

    /**
     * Setter for property borrowingrefNo.
     *
     * @param borrowingrefNo New value of property borrowingrefNo.
     */
    public void setBorrowingrefNo(java.lang.String borrowingrefNo) {
        this.borrowingrefNo = borrowingrefNo;
    }

    /**
     * Getter for property type.
     *
     * @return Value of property type.
     */
    public java.lang.String getType() {
        return type;
    }

    /**
     * Setter for property type.
     *
     * @param type New value of property type.
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    /**
     * Getter for property description.
     *
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * Setter for property description.
     *
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     * Getter for property sanctionDate.
     *
     * @return Value of property sanctionDate.
     */
    public java.util.Date getSanctionDate() {
        return sanctionDate;
    }

    /**
     * Setter for property sanctionDate.
     *
     * @param sanctionDate New value of property sanctionDate.
     */
    public void setSanctionDate(java.util.Date sanctionDate) {
        this.sanctionDate = sanctionDate;
    }

    /**
     * Getter for property sanctionAmt.
     *
     * @return Value of property sanctionAmt.
     */
    public java.lang.Double getSanctionAmt() {
        return sanctionAmt;
    }

    /**
     * Setter for property sanctionAmt.
     *
     * @param sanctionAmt New value of property sanctionAmt.
     */
    public void setSanctionAmt(java.lang.Double sanctionAmt) {
        this.sanctionAmt = sanctionAmt;
    }

    /**
     * Getter for property amtBorrowed.
     *
     * @return Value of property amtBorrowed.
     */
    public java.lang.Double getAmtBorrowed() {
        return amtBorrowed;
    }

    /**
     * Setter for property amtBorrowed.
     *
     * @param amtBorrowed New value of property amtBorrowed.
     */
    public void setAmtBorrowed(java.lang.Double amtBorrowed) {
        this.amtBorrowed = amtBorrowed;
    }

    /**
     * Getter for property rateofInt.
     *
     * @return Value of property rateofInt.
     */
    public java.lang.Double getRateofInt() {
        return rateofInt;
    }

    /**
     * Setter for property rateofInt.
     *
     * @param rateofInt New value of property rateofInt.
     */
    public void setRateofInt(java.lang.Double rateofInt) {
        this.rateofInt = rateofInt;
    }

    /**
     * Getter for property noofInstallments.
     *
     * @return Value of property noofInstallments.
     */
    public java.lang.Double getNoofInstallments() {
        return noofInstallments;
    }

    /**
     * Setter for property noofInstallments.
     *
     * @param noofInstallments New value of property noofInstallments.
     */
    public void setNoofInstallments(java.lang.Double noofInstallments) {
        this.noofInstallments = noofInstallments;
    }

    /**
     * Getter for property prinRepFrq.
     *
     * @return Value of property prinRepFrq.
     */
    public java.lang.String getPrinRepFrq() {
        return prinRepFrq;
    }

    /**
     * Setter for property prinRepFrq.
     *
     * @param prinRepFrq New value of property prinRepFrq.
     */
    public void setPrinRepFrq(java.lang.String prinRepFrq) {
        this.prinRepFrq = prinRepFrq;
    }

    /**
     * Getter for property intRepFrq.
     *
     * @return Value of property intRepFrq.
     */
    public java.lang.String getIntRepFrq() {
        return intRepFrq;
    }

    /**
     * Setter for property intRepFrq.
     *
     * @param intRepFrq New value of property intRepFrq.
     */
    public void setIntRepFrq(java.lang.String intRepFrq) {
        this.intRepFrq = intRepFrq;
    }

    /**
     * Getter for property morotorium.
     *
     * @return Value of property morotorium.
     */
    public java.lang.String getMorotorium() {
        return morotorium;
    }

    /**
     * Setter for property morotorium.
     *
     * @param morotorium New value of property morotorium.
     */
    public void setMorotorium(java.lang.String morotorium) {
        this.morotorium = morotorium;
    }

    /**
     * Getter for property sanctionExpDate.
     *
     * @return Value of property sanctionExpDate.
     */
    public java.util.Date getSanctionExpDate() {
        return sanctionExpDate;
    }

    /**
     * Setter for property sanctionExpDate.
     *
     * @param sanctionExpDate New value of property sanctionExpDate.
     */
    public void setSanctionExpDate(java.util.Date sanctionExpDate) {
        this.sanctionExpDate = sanctionExpDate;
    }

    /**
     * Getter for property secDetails.
     *
     * @return Value of property secDetails.
     */
    public java.lang.String getSecDetails() {
        return secDetails;
    }

    /**
     * Setter for property secDetails.
     *
     * @param secDetails New value of property secDetails.
     */
    public void setSecDetails(java.lang.String secDetails) {
        this.secDetails = secDetails;
    }

    /**
     * Getter for property prinGrpHead.
     *
     * @return Value of property prinGrpHead.
     */
    public java.lang.String getPrinGrpHead() {
        return prinGrpHead;
    }

    /**
     * Setter for property prinGrpHead.
     *
     * @param prinGrpHead New value of property prinGrpHead.
     */
    public void setPrinGrpHead(java.lang.String prinGrpHead) {
        this.prinGrpHead = prinGrpHead;
    }

    /**
     * Getter for property intGrpHead.
     *
     * @return Value of property intGrpHead.
     */
    public java.lang.String getIntGrpHead() {
        return intGrpHead;
    }

    /**
     * Setter for property intGrpHead.
     *
     * @param intGrpHead New value of property intGrpHead.
     */
    public void setIntGrpHead(java.lang.String intGrpHead) {
        this.intGrpHead = intGrpHead;
    }

    /**
     * Getter for property penGrpHead.
     *
     * @return Value of property penGrpHead.
     */
    public java.lang.String getPenGrpHead() {
        return penGrpHead;
    }

    /**
     * Setter for property penGrpHead.
     *
     * @param penGrpHead New value of property penGrpHead.
     */
    public void setPenGrpHead(java.lang.String penGrpHead) {
        this.penGrpHead = penGrpHead;
    }

    /**
     * Getter for property chargeGrpHead.
     *
     * @return Value of property chargeGrpHead.
     */
    public java.lang.String getChargeGrpHead() {
        return chargeGrpHead;
    }

    /**
     * Setter for property chargeGrpHead.
     *
     * @param chargeGrpHead New value of property chargeGrpHead.
     */
    public void setChargeGrpHead(java.lang.String chargeGrpHead) {
        this.chargeGrpHead = chargeGrpHead;
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
     * Getter for property penalIntRate.
     *
     * @return Value of property penalIntRate.
     */
    public java.lang.String getPenalIntRate() {
        return penalIntRate;
    }

    /**
     * Setter for property penalIntRate.
     *
     * @param penalIntRate New value of property penalIntRate.
     */
    public void setPenalIntRate(java.lang.String penalIntRate) {
        this.penalIntRate = penalIntRate;
    }

    /**
     * Getter for property multiDis.
     *
     * @return Value of property multiDis.
     */
    public java.lang.String getMultiDis() {
        return multiDis;
    }

    /**
     * Setter for property multiDis.
     *
     * @param multiDis New value of property multiDis.
     */
    public void setMultiDis(java.lang.String multiDis) {
        this.multiDis = multiDis;
    }

    /**
     * Getter for property renReq.
     *
     * @return Value of property renReq.
     */
    public java.lang.String getRenReq() {
        return renReq;
    }

    /**
     * Setter for property renReq.
     *
     * @param renReq New value of property renReq.
     */
    public void setRenReq(java.lang.String renReq) {
        this.renReq = renReq;
    }

    /**
     * Getter for property amount.
     *
     * @return Value of property amount.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Setter for property amount.
     *
     * @param amount New value of property amount.
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Getter for property availBalance.
     *
     * @return Value of property availBalance.
     */
    public Double getAvailBalance() {
        return availBalance;
    }

    /**
     * Setter for property availBalance.
     *
     * @param availBalance New value of property availBalance.
     */
    public void setAvailBalance(Double availBalance) {
        this.availBalance = availBalance;
    }
}