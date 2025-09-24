/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSPrizedMoneyDetailsEntryTO.java
 * 
 * Created on Thu Jun 09 19:31:08 IST 2011
 */
package com.see.truetransact.transferobject.mdsapplication.mdsprizedmoneydetailsentry;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is MDS_PRIZED_MONEY_DETAILS.
 */
public class MDSPrizedMoneyDetailsEntryTO extends TransferObject implements Serializable {

    private String mdsSchemeName = "";
    private Date drawAuctionDate = null;
    private Integer installmentNo = 0;
    private Integer divisionNo = 0;
    private Integer subNo = 0;
    private String draw = null; //AJITH Changed From ""
    private String auction = null; //AJITH Changed From ""
    private Date nextInstallmentDate = null;
    private Double prizedAmount = 0.0;
    private Double totalBonusAmount = 0.0;
    private Double nextBonusAmount = 0.0;
    private Double commisionAmount = 0.0;
    private Double totalDiscount = 0.0;
    private Double netAmountPayable = 0.0;
    private Integer appNo = null;
    private String chittalNo = null; //AJITH Changed From ""
    private String oldChittalNo = null; //AJITH Changed From ""
    private Integer oldSubNo = 0;
    private String memberType = null; //AJITH Changed From ""
    private String memberName = null; //AJITH Changed From ""
    private Double installmentPaid = 0.0;
    private Double installAmountPaid = 0.0;
    private Integer installmentDue = 0;
    private Double instalOverdueAmt = 0.0;
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null; //AJITH Changed From ""
    private Date authorizedDt = null;
    private String authorizedBy = null; //AJITH Changed From ""
    private Double slNo = 0.0;
    private Date paymentDate = null;
    private String branchId=null;
    private String userDefined="N";  //AJITH Changed From ""
    private String auctionTrans="N";  //AJITH Changed From ""
    private String predefinedInstall="";
    private String witness1CustId = "";
    private String witness2CustId = "";
    private String witness1Chittal = "";
    private String witness2Chittal = "";
    

    public String getPredefinedInstall() {
        return predefinedInstall;
    }

    public void setPredefinedInstall(String predefinedInstall) {
        this.predefinedInstall = predefinedInstall;
    }
    public String getAuctionTrans() {
        return auctionTrans;
    }

    public void setAuctionTrans(String auctionTrans) {
        this.auctionTrans = auctionTrans;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    /**
     * Setter/Getter for SCHEME_NAME - table Field
     */
    public void setMdsSchemeName(String mdsSchemeName) {
        this.mdsSchemeName = mdsSchemeName;
    }

    public String getMdsSchemeName() {
        return mdsSchemeName;
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
     * Setter/Getter for DRAW - table Field
     */
    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getDraw() {
        return draw;
    }

    /**
     * Setter/Getter for AUCTION - table Field
     */
    public void setAuction(String auction) {
        this.auction = auction;
    }

    public String getAuction() {
        return auction;
    }

    /**
     * Setter/Getter for NEXT_INSTALLMENT_DATE - table Field
     */
    public void setNextInstallmentDate(Date nextInstallmentDate) {
        this.nextInstallmentDate = nextInstallmentDate;
    }

    public Date getNextInstallmentDate() {
        return nextInstallmentDate;
    }

    /**
     * Setter/Getter for MEMBER_TYPE - table Field
     */
    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getMemberType() {
        return memberType;
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
        strB.append(getTOString("mdsSchemeName", mdsSchemeName));
        strB.append(getTOString("drawAuctionDate", drawAuctionDate));
        strB.append(getTOString("installmentNo", installmentNo));
        strB.append(getTOString("divisionNo", divisionNo));
        strB.append(getTOString("subNo", subNo));
        strB.append(getTOString("oldSubNo", oldSubNo));
        strB.append(getTOString("draw", draw));
        strB.append(getTOString("auction", auction));
        strB.append(getTOString("nextInstallmentDate", nextInstallmentDate));
        strB.append(getTOString("prizedAmount", prizedAmount));
        strB.append(getTOString("totalBonusAmount", totalBonusAmount));
        strB.append(getTOString("nextBonusAmount", nextBonusAmount));
        strB.append(getTOString("commisionAmount", commisionAmount));
        strB.append(getTOString("totalDiscount", totalDiscount));
        strB.append(getTOString("netAmountPayable", netAmountPayable));
        strB.append(getTOString("appNo", appNo));
        strB.append(getTOString("chittalNo", chittalNo));
        strB.append(getTOString("oldChittalNo", oldChittalNo));
        strB.append(getTOString("memberType", memberType));
        strB.append(getTOString("memberName", memberName));
        strB.append(getTOString("installmentPaid", installmentPaid));
        strB.append(getTOString("installAmountPaid", installAmountPaid));
        strB.append(getTOString("installmentDue", installmentDue));
        strB.append(getTOString("instalOverdueAmt", instalOverdueAmt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("paymentDate", paymentDate));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("userDefined", userDefined));
        strB.append(getTOString("auctionTrans", auctionTrans));
        strB.append(getTOString("predefinedInstall", predefinedInstall));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("mdsSchemeName", mdsSchemeName));
        strB.append(getTOXml("drawAuctionDate", drawAuctionDate));
        strB.append(getTOXml("installmentNo", installmentNo));
        strB.append(getTOXml("divisionNo", divisionNo));
        strB.append(getTOXml("subNo", subNo));
        strB.append(getTOXml("oldSubNo", oldSubNo));
        strB.append(getTOXml("draw", draw));
        strB.append(getTOXml("auction", auction));
        strB.append(getTOXml("nextInstallmentDate", nextInstallmentDate));
        strB.append(getTOXml("prizedAmount", prizedAmount));
        strB.append(getTOXml("totalBonusAmount", totalBonusAmount));
        strB.append(getTOXml("nextBonusAmount", nextBonusAmount));
        strB.append(getTOXml("commisionAmount", commisionAmount));
        strB.append(getTOXml("totalDiscount", totalDiscount));
        strB.append(getTOXml("netAmountPayable", netAmountPayable));
        strB.append(getTOXml("appNo", appNo));
        strB.append(getTOXml("chittalNo", chittalNo));
        strB.append(getTOXml("oldChittalNo", oldChittalNo));
        strB.append(getTOXml("memberType", memberType));
        strB.append(getTOXml("memberName", memberName));
        strB.append(getTOXml("installmentPaid", installmentPaid));
        strB.append(getTOXml("installAmountPaid", installAmountPaid));
        strB.append(getTOXml("installmentDue", installmentDue));
        strB.append(getTOXml("instalOverdueAmt", instalOverdueAmt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("paymentDate", paymentDate));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("userDefined", userDefined));
        strB.append(getTOXml("auctionTrans", auctionTrans));
        strB.append(getTOXml("predefinedInstall", predefinedInstall));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public Integer getDivisionNo() {
        return divisionNo;
    }

    public void setDivisionNo(Integer divisionNo) {
        this.divisionNo = divisionNo;
    }

    public Double getPrizedAmount() {
        return prizedAmount;
    }

    public void setPrizedAmount(Double prizedAmount) {
        this.prizedAmount = prizedAmount;
    }

    public Double getNetAmountPayable() {
        return netAmountPayable;
    }

    public void setNetAmountPayable(Double netAmountPayable) {
        this.netAmountPayable = netAmountPayable;
    }

    public Integer getAppNo() {
        return appNo;
    }

    public void setAppNo(Integer appNo) {
        this.appNo = appNo;
    }

    public Double getInstallmentPaid() {
        return installmentPaid;
    }

    public void setInstallmentPaid(Double installmentPaid) {
        this.installmentPaid = installmentPaid;
    }

    public Double getInstallAmountPaid() {
        return installAmountPaid;
    }

    public void setInstallAmountPaid(Double installAmountPaid) {
        this.installAmountPaid = installAmountPaid;
    }

    public Integer getInstallmentDue() {
        return installmentDue;
    }

    public void setInstallmentDue(Integer installmentDue) {
        this.installmentDue = installmentDue;
    }

    public Double getInstalOverdueAmt() {
        return instalOverdueAmt;
    }

    public void setInstalOverdueAmt(Double instalOverdueAmt) {
        this.instalOverdueAmt = instalOverdueAmt;
    }

    public Integer getInstallmentNo() {
        return installmentNo;
    }

    public void setInstallmentNo(Integer installmentNo) {
        this.installmentNo = installmentNo;
    }

    public Double getTotalBonusAmount() {
        return totalBonusAmount;
    }

    public void setTotalBonusAmount(Double totalBonusAmount) {
        this.totalBonusAmount = totalBonusAmount;
    }

    public Double getCommisionAmount() {
        return commisionAmount;
    }

    public void setCommisionAmount(Double commisionAmount) {
        this.commisionAmount = commisionAmount;
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

    public Double getNextBonusAmount() {
        return nextBonusAmount;
    }

    public void setNextBonusAmount(Double nextBonusAmount) {
        this.nextBonusAmount = nextBonusAmount;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
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
     * Getter for property paymentDate.
     *
     * @return Value of property paymentDate.
     */
    public java.util.Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * Setter for property paymentDate.
     *
     * @param paymentDate New value of property paymentDate.
     */
    public void setPaymentDate(java.util.Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getSubNo() {
        return subNo;
    }

    public void setSubNo(Integer subNo) {
        this.subNo = subNo;
    }

    /**
     * Getter for property oldChittalNo.
     *
     * @return Value of property oldChittalNo.
     */
    public java.lang.String getOldChittalNo() {
        return oldChittalNo;
    }

    /**
     * Setter for property oldChittalNo.
     *
     * @param oldChittalNo New value of property oldChittalNo.
     */
    public void setOldChittalNo(java.lang.String oldChittalNo) {
        this.oldChittalNo = oldChittalNo;
    }

    public Integer getOldSubNo() {
        return oldSubNo;
    }

    public void setOldSubNo(Integer oldSubNo) {
        this.oldSubNo = oldSubNo;
    }

    public String getUserDefined() {
        return userDefined;
    }

    public void setUserDefined(String userDefined) {
        this.userDefined = userDefined;
    }

    public String getWitness1Chittal() {
        return witness1Chittal;
    }

    public void setWitness1Chittal(String witness1Chittal) {
        this.witness1Chittal = witness1Chittal;
    }

    public String getWitness1CustId() {
        return witness1CustId;
    }

    public void setWitness1CustId(String witness1CustId) {
        this.witness1CustId = witness1CustId;
    }

    public String getWitness2Chittal() {
        return witness2Chittal;
    }

    public void setWitness2Chittal(String witness2Chittal) {
        this.witness2Chittal = witness2Chittal;
    }

    public String getWitness2CustId() {
        return witness2CustId;
    }

    public void setWitness2CustId(String witness2CustId) {
        this.witness2CustId = witness2CustId;
    }
    
}