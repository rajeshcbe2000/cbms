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
package com.see.truetransact.transferobject.product.mds;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PVT_EXTERNAL_WIRE.
 */
public class MDSProductTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String prodDesc = "";
    private String acctHead = "";
    private String onlyMember = "";
    private String acceptClassA = "";
    private String acceptClassB = "";
    private String acceptClassC = "";
    private String acceptClassAll = "";
    private String surety = "";
    private String acceptableClassA = "";
    private String acceptableClassB = "";
    private String acceptableClassC = "";
    private String acceptableClassAll = "";
    private String shortFall = "";
    private String defaultChitOwners = "";
    private String nonPrizedChange = "";
    private String bonusExistingChit = "";
    private String bonusRounding = "";
    private String bonusPayForFirstIns = "";
    private String advanceCollection = "";
    private Double auctionMaxamt = 0.0;
    private Double auctionMinamt = 0.0;
    private String bonusAllowed = "";
    private String prizedOwnerBonus = "";
    private String afterAuctionEligible = "";
    private String afterPaymentEligible = "";
    private String prizedDefaulters = "";
    private String holidayInt = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authroizeBy = "";
    private Date authorizeDt = null;
    private String roundoffCriteria = "";
    private String rdoMethod1 = "";
    private String rdoMethod2 = "";
    private String fromAuctionEnrtry = "";
    private String afterCashPayment = "";
    private String splitMDSTransaction = "";
    private String isGDS = "";
    private String isBonusTrans = "";

    public String getIsBonusTrans() {
        return isBonusTrans;
    }

    public void setIsBonusTrans(String isBonusTrans) {
        this.isBonusTrans = isBonusTrans;
    }

    public String getIsGDS() {
        return isGDS;
    }

    public void setIsGDS(String isGDS) {
        this.isGDS = isGDS;
    }

    public String getRdoMethod1() {
        return rdoMethod1;
    }

    public void setRdoMethod1(String rdoMethod1) {
        this.rdoMethod1 = rdoMethod1;
    }

    public String getRdoMethod2() {
        return rdoMethod2;
    }

    public void setRdoMethod2(String rdoMethod2) {
        this.rdoMethod2 = rdoMethod2;
    }

    public String getRdoPending() {
        return rdoPending;
    }

    public void setRdoPending(String rdoPending) {
        this.rdoPending = rdoPending;
    }

    public String getRdoPrized() {
        return rdoPrized;
    }

    public void setRdoPrized(String rdoPrized) {
        this.rdoPrized = rdoPrized;
    }

    public String getRdoPrizdOrInstlAmtIsLess() {
        return rdoPrizdOrInstlAmtIsLess;
    }

    public void setRdoPrizdOrInstlAmtIsLess(String rdoPrizdOrInstlAmtIsLess) {
        this.rdoPrizdOrInstlAmtIsLess = rdoPrizdOrInstlAmtIsLess;
    }
    
    private String transForFirstIns = "";
    private String rdoPending = "";
    private String rdoPrized = "";
    private String rdoPrizdOrInstlAmtIsLess = "";

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
//    public String getKeyData() {
//        setKeyColumns("");
//        return "";
//    }
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("prodDesc", prodDesc));
        strB.append(getTOString("acctHead", acctHead));
        strB.append(getTOString("onlyMember", onlyMember));
        strB.append(getTOString("acceptClassA", acceptClassA));
        strB.append(getTOString("acceptClassB", acceptClassB));
        strB.append(getTOString("acceptClassC", acceptClassC));
        strB.append(getTOString("acceptClassAll", acceptClassAll));
        strB.append(getTOString("surety", surety));
        strB.append(getTOString("acceptableClassA", acceptableClassA));
        strB.append(getTOString("acceptableClassB", acceptableClassB));
        strB.append(getTOString("acceptableClassC", acceptableClassC));
        strB.append(getTOString("acceptableClassAll", acceptableClassAll));
        strB.append(getTOString("shortFall", shortFall));
        strB.append(getTOString("defaultChitOwners", defaultChitOwners));
        strB.append(getTOString("nonPrizedChange", nonPrizedChange));
        strB.append(getTOString("bonusExistingChit", bonusExistingChit));
        strB.append(getTOString("bonusRounding", bonusRounding));
        strB.append(getTOString("roundoffCriteria", roundoffCriteria));
        strB.append(getTOString("bonusPayForFirstIns", bonusPayForFirstIns));
        strB.append(getTOString("advanceCollection", advanceCollection));
        strB.append(getTOString("auctionMaxamt", auctionMaxamt));
        strB.append(getTOString("auctionMinamt", auctionMinamt));
        strB.append(getTOString("bonusAllowed", bonusAllowed));
        strB.append(getTOString("prizedOwnerBonus", prizedOwnerBonus));
        strB.append(getTOString("afterAuctionEligible", afterAuctionEligible));
        strB.append(getTOString("afterPaymentEligible", afterPaymentEligible));
        strB.append(getTOString("prizedDefaulters", prizedDefaulters));
        strB.append(getTOString("holidayInt", holidayInt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authroizeBy", authroizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString(" rdoPrizdOrInstlAmtIsLess",  rdoPrizdOrInstlAmtIsLess));
        strB.append(getTOString(" fromAuctionEnrtry",  fromAuctionEnrtry));
        strB.append(getTOString(" afterCashPayment",  afterCashPayment));
        strB.append(getTOString(" splitMDSTransaction",splitMDSTransaction));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("prodDesc", prodDesc));
        strB.append(getTOXml("acctHead", acctHead));
        strB.append(getTOXml("onlyMember", onlyMember));
        strB.append(getTOXml("acceptClassA", acceptClassA));
        strB.append(getTOXml("acceptClassB", acceptClassB));
        strB.append(getTOXml("acceptClassC", acceptClassC));
        strB.append(getTOXml("acceptClassAll", acceptClassAll));
        strB.append(getTOXml("surety", surety));
        strB.append(getTOXml("acceptableClassA", acceptableClassA));
        strB.append(getTOXml("acceptableClassB", acceptableClassB));
        strB.append(getTOXml("acceptableClassC", acceptableClassC));
        strB.append(getTOXml("acceptableClassAll", acceptableClassAll));
        strB.append(getTOXml("rdoPrizdOrInstlAmtIsLess", rdoPrizdOrInstlAmtIsLess));
        strB.append(getTOXml("shortFall", shortFall));
        strB.append(getTOXml("defaultChitOwners", defaultChitOwners));
        strB.append(getTOXml("nonPrizedChange", nonPrizedChange));
        strB.append(getTOXml("bonusExistingChit", bonusExistingChit));
        strB.append(getTOXml("bonusRounding", bonusRounding));
        strB.append(getTOXml("roundoffCriteria", roundoffCriteria));
        strB.append(getTOXml("bonusPayForFirstIns", bonusPayForFirstIns));
        strB.append(getTOXml("advanceCollection", advanceCollection));
        strB.append(getTOXml("auctionMaxamt", auctionMaxamt));
        strB.append(getTOXml("auctionMinamt", auctionMinamt));
        strB.append(getTOXml("bonusAllowed", bonusAllowed));
        strB.append(getTOXml("prizedOwnerBonus", prizedOwnerBonus));
        strB.append(getTOXml("afterAuctionEligible", afterAuctionEligible));
        strB.append(getTOXml("afterPaymentEligible", afterPaymentEligible));
        strB.append(getTOXml("prizedDefaulters", prizedDefaulters));
        strB.append(getTOXml("holidayInt", holidayInt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authroizeBy", authroizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml(" fromAuctionEnrtry",  fromAuctionEnrtry));
        strB.append(getTOXml(" afterCashPayment",  afterCashPayment));
        strB.append(getTOXml(" splitMDSTransaction",splitMDSTransaction));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property prodDesc.
     *
     * @return Value of property prodDesc.
     */
    public java.lang.String getProdDesc() {
        return prodDesc;
    }

    /**
     * Setter for property prodDesc.
     *
     * @param prodDesc New value of property prodDesc.
     */
    public void setProdDesc(java.lang.String prodDesc) {
        this.prodDesc = prodDesc;
    }

    /**
     * Getter for property acctHead.
     *
     * @return Value of property acctHead.
     */
    public java.lang.String getAcctHead() {
        return acctHead;
    }

    /**
     * Setter for property acctHead.
     *
     * @param acctHead New value of property acctHead.
     */
    public void setAcctHead(java.lang.String acctHead) {
        this.acctHead = acctHead;
    }

    /**
     * Getter for property onlyMember.
     *
     * @return Value of property onlyMember.
     */
    public java.lang.String getOnlyMember() {
        return onlyMember;
    }

    /**
     * Setter for property onlyMember.
     *
     * @param onlyMember New value of property onlyMember.
     */
    public void setOnlyMember(java.lang.String onlyMember) {
        this.onlyMember = onlyMember;
    }

    /**
     * Getter for property surety.
     *
     * @return Value of property surety.
     */
    public java.lang.String getSurety() {
        return surety;
    }

    /**
     * Setter for property surety.
     *
     * @param surety New value of property surety.
     */
    public void setSurety(java.lang.String surety) {
        this.surety = surety;
    }

    /**
     * Getter for property shortFall.
     *
     * @return Value of property shortFall.
     */
    public java.lang.String getShortFall() {
        return shortFall;
    }

    /**
     * Setter for property shortFall.
     *
     * @param shortFall New value of property shortFall.
     */
    public void setShortFall(java.lang.String shortFall) {
        this.shortFall = shortFall;
    }

    /**
     * Getter for property defaultChitOwners.
     *
     * @return Value of property defaultChitOwners.
     */
    public java.lang.String getDefaultChitOwners() {
        return defaultChitOwners;
    }

    /**
     * Setter for property defaultChitOwners.
     *
     * @param defaultChitOwners New value of property defaultChitOwners.
     */
    public void setDefaultChitOwners(java.lang.String defaultChitOwners) {
        this.defaultChitOwners = defaultChitOwners;
    }

    /**
     * Getter for property nonPrizedChange.
     *
     * @return Value of property nonPrizedChange.
     */
    public java.lang.String getNonPrizedChange() {
        return nonPrizedChange;
    }

    /**
     * Setter for property nonPrizedChange.
     *
     * @param nonPrizedChange New value of property nonPrizedChange.
     */
    public void setNonPrizedChange(java.lang.String nonPrizedChange) {
        this.nonPrizedChange = nonPrizedChange;
    }

    /**
     * Getter for property auctionMaxamt.
     *
     * @return Value of property auctionMaxamt.
     */
    public java.lang.Double getAuctionMaxamt() {
        return auctionMaxamt;
    }

    /**
     * Setter for property auctionMaxamt.
     *
     * @param auctionMaxamt New value of property auctionMaxamt.
     */
    public void setAuctionMaxamt(java.lang.Double auctionMaxamt) {
        this.auctionMaxamt = auctionMaxamt;
    }

    /**
     * Getter for property auctionMinamt.
     *
     * @return Value of property auctionMinamt.
     */
    public java.lang.Double getAuctionMinamt() {
        return auctionMinamt;
    }

    /**
     * Setter for property auctionMinamt.
     *
     * @param auctionMinamt New value of property auctionMinamt.
     */
    public void setAuctionMinamt(java.lang.Double auctionMinamt) {
        this.auctionMinamt = auctionMinamt;
    }

    /**
     * Getter for property bonusAllowed.
     *
     * @return Value of property bonusAllowed.
     */
    public java.lang.String getBonusAllowed() {
        return bonusAllowed;
    }

    /**
     * Setter for property bonusAllowed.
     *
     * @param bonusAllowed New value of property bonusAllowed.
     */
    public void setBonusAllowed(java.lang.String bonusAllowed) {
        this.bonusAllowed = bonusAllowed;
    }

    /**
     * Getter for property prizedOwnerBonus.
     *
     * @return Value of property prizedOwnerBonus.
     */
    public java.lang.String getPrizedOwnerBonus() {
        return prizedOwnerBonus;
    }

    /**
     * Setter for property prizedOwnerBonus.
     *
     * @param prizedOwnerBonus New value of property prizedOwnerBonus.
     */
    public void setPrizedOwnerBonus(java.lang.String prizedOwnerBonus) {
        this.prizedOwnerBonus = prizedOwnerBonus;
    }

    /**
     * Getter for property afterAuctionEligible.
     *
     * @return Value of property afterAuctionEligible.
     */
    public java.lang.String getAfterAuctionEligible() {
        return afterAuctionEligible;
    }

    /**
     * Setter for property afterAuctionEligible.
     *
     * @param afterAuctionEligible New value of property afterAuctionEligible.
     */
    public void setAfterAuctionEligible(java.lang.String afterAuctionEligible) {
        this.afterAuctionEligible = afterAuctionEligible;
    }

    /**
     * Getter for property afterPaymentEligible.
     *
     * @return Value of property afterPaymentEligible.
     */
    public java.lang.String getAfterPaymentEligible() {
        return afterPaymentEligible;
    }

    /**
     * Setter for property afterPaymentEligible.
     *
     * @param afterPaymentEligible New value of property afterPaymentEligible.
     */
    public void setAfterPaymentEligible(java.lang.String afterPaymentEligible) {
        this.afterPaymentEligible = afterPaymentEligible;
    }

    /**
     * Getter for property prizedDefaulters.
     *
     * @return Value of property prizedDefaulters.
     */
    public java.lang.String getPrizedDefaulters() {
        return prizedDefaulters;
    }

    /**
     * Setter for property prizedDefaulters.
     *
     * @param prizedDefaulters New value of property prizedDefaulters.
     */
    public void setPrizedDefaulters(java.lang.String prizedDefaulters) {
        this.prizedDefaulters = prizedDefaulters;
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
     * Getter for property authroizeBy.
     *
     * @return Value of property authroizeBy.
     */
    public java.lang.String getAuthroizeBy() {
        return authroizeBy;
    }

    /**
     * Setter for property authroizeBy.
     *
     * @param authroizeBy New value of property authroizeBy.
     */
    public void setAuthroizeBy(java.lang.String authroizeBy) {
        this.authroizeBy = authroizeBy;
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
     * Getter for property holidayInt.
     *
     * @return Value of property holidayInt.
     */
    public java.lang.String getHolidayInt() {
        return holidayInt;
    }

    /**
     * Setter for property holidayInt.
     *
     * @param holidayInt New value of property holidayInt.
     */
    public void setHolidayInt(java.lang.String holidayInt) {
        this.holidayInt = holidayInt;
    }

    /**
     * Getter for property acceptClassA.
     *
     * @return Value of property acceptClassA.
     */
    public java.lang.String getAcceptClassA() {
        return acceptClassA;
    }

    /**
     * Setter for property acceptClassA.
     *
     * @param acceptClassA New value of property acceptClassA.
     */
    public void setAcceptClassA(java.lang.String acceptClassA) {
        this.acceptClassA = acceptClassA;
    }

    /**
     * Getter for property acceptClassB.
     *
     * @return Value of property acceptClassB.
     */
    public java.lang.String getAcceptClassB() {
        return acceptClassB;
    }

    /**
     * Setter for property acceptClassB.
     *
     * @param acceptClassB New value of property acceptClassB.
     */
    public void setAcceptClassB(java.lang.String acceptClassB) {
        this.acceptClassB = acceptClassB;
    }

    /**
     * Getter for property acceptClassC.
     *
     * @return Value of property acceptClassC.
     */
    public java.lang.String getAcceptClassC() {
        return acceptClassC;
    }

    /**
     * Setter for property acceptClassC.
     *
     * @param acceptClassC New value of property acceptClassC.
     */
    public void setAcceptClassC(java.lang.String acceptClassC) {
        this.acceptClassC = acceptClassC;
    }

    /**
     * Getter for property acceptClassAll.
     *
     * @return Value of property acceptClassAll.
     */
    public java.lang.String getAcceptClassAll() {
        return acceptClassAll;
    }

    /**
     * Setter for property acceptClassAll.
     *
     * @param acceptClassAll New value of property acceptClassAll.
     */
    public void setAcceptClassAll(java.lang.String acceptClassAll) {
        this.acceptClassAll = acceptClassAll;
    }

    /**
     * Getter for property acceptableClassA.
     *
     * @return Value of property acceptableClassA.
     */
    public java.lang.String getAcceptableClassA() {
        return acceptableClassA;
    }

    /**
     * Setter for property acceptableClassA.
     *
     * @param acceptableClassA New value of property acceptableClassA.
     */
    public void setAcceptableClassA(java.lang.String acceptableClassA) {
        this.acceptableClassA = acceptableClassA;
    }

    /**
     * Getter for property acceptableClassB.
     *
     * @return Value of property acceptableClassB.
     */
    public java.lang.String getAcceptableClassB() {
        return acceptableClassB;
    }

    /**
     * Setter for property acceptableClassB.
     *
     * @param acceptableClassB New value of property acceptableClassB.
     */
    public void setAcceptableClassB(java.lang.String acceptableClassB) {
        this.acceptableClassB = acceptableClassB;
    }

    /**
     * Getter for property acceptableClassC.
     *
     * @return Value of property acceptableClassC.
     */
    public java.lang.String getAcceptableClassC() {
        return acceptableClassC;
    }

    /**
     * Setter for property acceptableClassC.
     *
     * @param acceptableClassC New value of property acceptableClassC.
     */
    public void setAcceptableClassC(java.lang.String acceptableClassC) {
        this.acceptableClassC = acceptableClassC;
    }

    /**
     * Getter for property acceptableClassAll.
     *
     * @return Value of property acceptableClassAll.
     */
    public java.lang.String getAcceptableClassAll() {
        return acceptableClassAll;
    }

    /**
     * Setter for property acceptableClassAll.
     *
     * @param acceptableClassAll New value of property acceptableClassAll.
     */
    public void setAcceptableClassAll(java.lang.String acceptableClassAll) {
        this.acceptableClassAll = acceptableClassAll;
    }

    /**
     * Getter for property bonusRounding.
     *
     * @return Value of property bonusRounding.
     */
    public java.lang.String getBonusRounding() {
        return bonusRounding;
    }

    /**
     * Setter for property bonusRounding.
     *
     * @param bonusRounding New value of property bonusRounding.
     */
    public void setBonusRounding(java.lang.String bonusRounding) {
        this.bonusRounding = bonusRounding;
    }

    /**
     * Getter for property bonusPayForFirstIns.
     *
     * @return Value of property bonusPayForFirstIns.
     */
    public java.lang.String getBonusPayForFirstIns() {
        return bonusPayForFirstIns;
    }

    /**
     * Setter for property bonusPayForFirstIns.
     *
     * @param bonusPayForFirstIns New value of property bonusPayForFirstIns.
     */
    public void setBonusPayForFirstIns(java.lang.String bonusPayForFirstIns) {
        this.bonusPayForFirstIns = bonusPayForFirstIns;
    }

    /**
     * Getter for property bonusExistingChit.
     *
     * @return Value of property bonusExistingChit.
     */
    public java.lang.String getBonusExistingChit() {
        return bonusExistingChit;
    }

    /**
     * Setter for property bonusExistingChit.
     *
     * @param bonusExistingChit New value of property bonusExistingChit.
     */
    public void setBonusExistingChit(java.lang.String bonusExistingChit) {
        this.bonusExistingChit = bonusExistingChit;
    }

    /**
     * Getter for property advanceCollection.
     *
     * @return Value of property advanceCollection.
     */
    public java.lang.String getAdvanceCollection() {
        return advanceCollection;
    }

    /**
     * Setter for property advanceCollection.
     *
     * @param advanceCollection New value of property advanceCollection.
     */
    public void setAdvanceCollection(java.lang.String advanceCollection) {
        this.advanceCollection = advanceCollection;
    }

    /**
     * Getter for property roundoffCriteria.
     *
     * @return Value of property roundoffCriteria.
     */
    public java.lang.String getRoundoffCriteria() {
        return roundoffCriteria;
    }

    /**
     * Setter for property roundoffCriteria.
     *
     * @param roundoffCriteria New value of property roundoffCriteria.
     */
    public void setRoundoffCriteria(java.lang.String roundoffCriteria) {
        this.roundoffCriteria = roundoffCriteria;
    }

    /**
     * Getter for property transForFirstIns.
     *
     * @return Value of property transForFirstIns.
     */
    public String getTransForFirstIns() {
        return transForFirstIns;
    }

    /**
     * Setter for property transForFirstIns.
     *
     * @param transForFirstIns New value of property transForFirstIns.
     */
    public void setTransForFirstIns(String transForFirstIns) {
        this.transForFirstIns = transForFirstIns;
    }

    public String getAfterCashPayment() {
        return afterCashPayment;
    }

    public void setAfterCashPayment(String afterCashPayment) {
        this.afterCashPayment = afterCashPayment;
    }

    public String getFromAuctionEnrtry() {
        return fromAuctionEnrtry;
    }

    public void setFromAuctionEnrtry(String fromAuctionEnrtry) {
        this.fromAuctionEnrtry = fromAuctionEnrtry;
    }

    public String getSplitMDSTransaction() {
        return splitMDSTransaction;
    }

    public void setSplitMDSTransaction(String splitMDSTransaction) {
        this.splitMDSTransaction = splitMDSTransaction;
    }
    
}