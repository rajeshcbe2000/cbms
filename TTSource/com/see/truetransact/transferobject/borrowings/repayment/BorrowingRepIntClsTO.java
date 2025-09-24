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
package com.see.truetransact.transferobject.borrowings.repayment;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class BorrowingRepIntClsTO extends TransferObject implements Serializable {

    private String repintclsNo = "";
    private String borrowingrefNo = "";
    private String borrowingNo = null;
    private Date dateIntPaid = null;
    private Double intPayable;
    private Double penalPayable;
    private Double chargesRepaid;
    private Double penalRepaid;
    private Double intRepaid;
    private Double principalRepaid;
    private String closeStatus = "";
    private Date closedDate = null;
    private Double principalBal;
    private Double interestBal;
    private Double penalBal;
    private Double chargesBal, currBal;
    //cheque details
    private Date checkDate = null;
    private String checkNo = "";
    //end...
    private String authorizeStatus = null;
    private String authorizeBy = null;
    private String createdBy = null;
    private Date authorizeDte = null;
    private String status = null;
    private String agencyCode = "";
    private String type = null;
    private String description = null;
    private Date sanctionDate = null;
    private Double sanctionAmt;
    private Double rateofInt;
    private Double noofInstallments;
    private String prinRepFrq = null;
    private String intRepFrq = "";
    private String morotorium = "";
    private Date sanctionExpDate = null;
    private Double amtBorrowed, totalD, totalR;
    private Date lastRepaiddate = null;

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(repintclsNo);
        return repintclsNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("repintclsNo", repintclsNo));
        strB.append(getTOString("borrowingrefNo", borrowingrefNo));
        strB.append(getTOString("borrowingNo", borrowingNo));
        strB.append(getTOString("dateIntPaid", dateIntPaid));
        //cheque details
        strB.append(getTOString("checkDate", checkDate));
        strB.append(getTOString("checkNo", checkNo));
        //end..
        strB.append(getTOString("intPayable", intPayable));
        strB.append(getTOString("penalPayable", penalPayable));
        strB.append(getTOString("chargesRepaid", chargesRepaid));
        strB.append(getTOString("penalRepaid", penalRepaid));
        strB.append(getTOString("intRepaid", intRepaid));
        strB.append(getTOString("principalRepaid", principalRepaid));
        strB.append(getTOString("principalBal", principalBal));
        strB.append(getTOString("interestBal", interestBal));
        strB.append(getTOString("penalBal", penalBal));
        strB.append(getTOString("chargesBal", chargesBal));
        strB.append(getTOString("closeStatus", closeStatus));
        strB.append(getTOString("closedDate", closedDate));


        strB.append(getTOString("agencyCode", agencyCode));
        strB.append(getTOString("type", type));
        strB.append(getTOString("description", description));
        strB.append(getTOString("sanctionDate", sanctionDate));
        strB.append(getTOString("sanctionAmt", sanctionAmt));
        strB.append(getTOString("rateofInt", rateofInt));
        strB.append(getTOString("noofInstallments", noofInstallments));
        strB.append(getTOString("prinRepFrq", prinRepFrq));
        strB.append(getTOString("intRepFrq", intRepFrq));
        strB.append(getTOString("morotorium", morotorium));
        strB.append(getTOString("sanctionExpDate", sanctionExpDate));
        strB.append(getTOString("lastRepaiddate", lastRepaiddate));

        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("authorizeDte", authorizeDte));
        strB.append(getTOString("status", status));
        strB.append(getTOString("amtBorrowed", amtBorrowed));
        //System.out.println(")(*************************************************************===="+currBal);
        strB.append(getTOString("currBal", currBal));
        strB.append(getTOString("totalD", totalD));
        strB.append(getTOString("totalR", totalR));



        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("repintclsNo", repintclsNo));
        strB.append(getTOXml("borrowingrefNo", borrowingrefNo));
        strB.append(getTOXml("borrowingNo", borrowingNo));
        strB.append(getTOXml("dateIntPaid", dateIntPaid));
        //cheque details
        strB.append(getTOXml("checkDate", checkDate));
        strB.append(getTOXml("checkNo", checkNo));
        //end...
        strB.append(getTOXml("intPayable", intPayable));
        strB.append(getTOXml("penalPayable", penalPayable));
        strB.append(getTOXml("chargesRepaid", chargesRepaid));
        strB.append(getTOXml("penalRepaid", penalRepaid));
        strB.append(getTOXml("intRepaid", intRepaid));
        strB.append(getTOXml("principalBal", principalBal));
        strB.append(getTOXml("interestBal", interestBal));
        strB.append(getTOXml("penalBal", penalBal));
        strB.append(getTOXml("chargesBal", chargesBal));

        strB.append(getTOXml("principalRepaid", principalRepaid));
        strB.append(getTOXml("closeStatus", closeStatus));
        strB.append(getTOXml("closedDate", closedDate));


        strB.append(getTOString("agencyCode", agencyCode));
        strB.append(getTOString("type", type));
        strB.append(getTOString("description", description));
        strB.append(getTOString("sanctionDate", sanctionDate));
        strB.append(getTOString("sanctionAmt", sanctionAmt));
        strB.append(getTOString("rateofInt", rateofInt));
        strB.append(getTOString("noofInstallments", noofInstallments));
        strB.append(getTOString("prinRepFrq", prinRepFrq));
        strB.append(getTOString("intRepFrq", intRepFrq));
        strB.append(getTOString("morotorium", morotorium));
        strB.append(getTOString("sanctionExpDate", sanctionExpDate));
        strB.append(getTOString("lastRepaiddate", lastRepaiddate));

        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("authorizeDte", authorizeDte));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("amtBorrowed", amtBorrowed));
        strB.append(getTOXml("currBal", currBal));
        strB.append(getTOXml("totalD", totalD));
        strB.append(getTOXml("totalR", totalR));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    
    public java.lang.String getRepintclsNo() {
        return repintclsNo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

   
    public void setRepintclsNo(java.lang.String repintclsNo) {
        this.repintclsNo = repintclsNo;
    }

    
    public java.lang.String getBorrowingrefNo() {
        return borrowingrefNo;
    }

   
    public void setBorrowingrefNo(java.lang.String borrowingrefNo) {
        this.borrowingrefNo = borrowingrefNo;
    }

   
    public java.lang.String getBorrowingNo() {
        return borrowingNo;
    }

   
    public void setBorrowingNo(java.lang.String borrowingNo) {
        this.borrowingNo = borrowingNo;
    }

    
    public java.util.Date getDateIntPaid() {
        return dateIntPaid;
    }

   
    public void setDateIntPaid(java.util.Date dateIntPaid) {
        this.dateIntPaid = dateIntPaid;
    }

   
    public java.lang.Double getIntPayable() {
        return intPayable;
    }

   
    public void setIntPayable(java.lang.Double intPayable) {
        this.intPayable = intPayable;
    }

   
    public java.lang.Double getPenalPayable() {
        return penalPayable;
    }

   
    public void setPenalPayable(java.lang.Double penalPayable) {
        this.penalPayable = penalPayable;
    }

   
    public java.lang.Double getChargesRepaid() {
        return chargesRepaid;
    }

  
    public void setChargesRepaid(java.lang.Double chargesRepaid) {
        this.chargesRepaid = chargesRepaid;
    }

   
    public java.lang.Double getPenalRepaid() {
        return penalRepaid;
    }

   
    public void setPenalRepaid(java.lang.Double penalRepaid) {
        this.penalRepaid = penalRepaid;
    }

   
    public java.lang.Double getIntRepaid() {
        return intRepaid;
    }

   
    public void setIntRepaid(java.lang.Double intRepaid) {
        this.intRepaid = intRepaid;
    }

   
    public java.lang.Double getPrincipalRepaid() {
        return principalRepaid;
    }

   
    public void setPrincipalRepaid(java.lang.Double principalRepaid) {
        this.principalRepaid = principalRepaid;
    }

    public java.lang.String getCloseStatus() {
        return closeStatus;
    }

   
    public void setCloseStatus(java.lang.String closeStatus) {
        this.closeStatus = closeStatus;
    }

   
    public java.util.Date getClosedDate() {
        return closedDate;
    }

    
    public void setClosedDate(java.util.Date closedDate) {
        this.closedDate = closedDate;
    }

   
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

   
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

   
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

   
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

   
    public java.util.Date getAuthorizeDte() {
        return authorizeDte;
    }

   
    public void setAuthorizeDte(java.util.Date authorizeDte) {
        this.authorizeDte = authorizeDte;
    }

   
    public java.lang.String getStatus() {
        return status;
    }

   
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

  
    public java.lang.Double getPrincipalBal() {
        return principalBal;
    }

    
    public void setPrincipalBal(java.lang.Double principalBal) {
        this.principalBal = principalBal;
    }

   
    public java.lang.Double getInterestBal() {
        return interestBal;
    }

   
    public void setInterestBal(java.lang.Double interestBal) {
        this.interestBal = interestBal;
    }

   
    public java.lang.Double getPenalBal() {
        return penalBal;
    }

    
    public void setPenalBal(java.lang.Double penalBal) {
        this.penalBal = penalBal;
    }

   
    public java.lang.Double getChargesBal() {
        return chargesBal;
    }

  
    public void setChargesBal(java.lang.Double chargesBal) {
        this.chargesBal = chargesBal;
    }

    public java.lang.String getAgencyCode() {
        return agencyCode;
    }

   
    public void setAgencyCode(java.lang.String agencyCode) {
        this.agencyCode = agencyCode;
    }

    
    public java.lang.String getType() {
        return type;
    }

   
    public void setType(java.lang.String type) {
        this.type = type;
    }

  
    public java.lang.String getDescription() {
        return description;
    }

  
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

  
    public java.util.Date getSanctionDate() {
        return sanctionDate;
    }

  
    public void setSanctionDate(java.util.Date sanctionDate) {
        this.sanctionDate = sanctionDate;
    }

   
    public java.lang.Double getSanctionAmt() {
        return sanctionAmt;
    }

    public void setSanctionAmt(java.lang.Double sanctionAmt) {
        this.sanctionAmt = sanctionAmt;
    }

   
    public java.lang.Double getRateofInt() {
        return rateofInt;
    }

    
    public void setRateofInt(java.lang.Double rateofInt) {
        this.rateofInt = rateofInt;
    }

   
    public java.lang.Double getNoofInstallments() {
        return noofInstallments;
    }

  
    public void setNoofInstallments(java.lang.Double noofInstallments) {
        this.noofInstallments = noofInstallments;
    }

   
    public java.lang.String getPrinRepFrq() {
        return prinRepFrq;
    }

    
    public void setPrinRepFrq(java.lang.String prinRepFrq) {
        this.prinRepFrq = prinRepFrq;
    }

   
    public java.lang.String getIntRepFrq() {
        return intRepFrq;
    }

  
    public void setIntRepFrq(java.lang.String intRepFrq) {
        this.intRepFrq = intRepFrq;
    }

   
    public java.lang.String getMorotorium() {
        return morotorium;
    }

   
    public void setMorotorium(java.lang.String morotorium) {
        this.morotorium = morotorium;
    }

    
    public java.util.Date getSanctionExpDate() {
        return sanctionExpDate;
    }

   
    public void setSanctionExpDate(java.util.Date sanctionExpDate) {
        this.sanctionExpDate = sanctionExpDate;
    }

   
    public java.lang.Double getAmtBorrowed() {
        return amtBorrowed;
    }

   
    public void setAmtBorrowed(java.lang.Double amtBorrowed) {
        this.amtBorrowed = amtBorrowed;
    }

  
    public java.util.Date getLastRepaiddate() {
        return lastRepaiddate;
    }

   
    public void setLastRepaiddate(java.util.Date lastRepaiddate) {
        this.lastRepaiddate = lastRepaiddate;
    }

   
    public java.lang.Double getCurrBal() {
        return currBal;
    }

   
    public void setCurrBal(java.lang.Double currBal) {
        this.currBal = currBal;
    }

   
    public java.lang.Double getTotalD() {
        return totalD;
    }

    
    public void setTotalD(java.lang.Double totalD) {
        this.totalD = totalD;
    }

   
    public java.lang.Double getTotalR() {
        return totalR;
    }

   
    public void setTotalR(java.lang.Double totalR) {
        this.totalR = totalR;
    }

   
    public java.util.Date getCheckDate() {
        return checkDate;
    }

   
    public void setCheckDate(java.util.Date checkDate) {
        this.checkDate = checkDate;
    }

    public java.lang.String getCheckNo() {
        return checkNo;
    }

   
    public void setCheckNo(java.lang.String checkNo) {
        this.checkNo = checkNo;
    }
}