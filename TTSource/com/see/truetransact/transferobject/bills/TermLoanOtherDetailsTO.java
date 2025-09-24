/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanOtherDetailsTO.java
 * 
 * Created on Sat Apr 02 19:46:55 IST 2005
 */
package com.see.truetransact.transferobject.bills;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ADVANCES_PARAM_DETAILS.
 */
public class TermLoanOtherDetailsTO extends TransferObject implements Serializable {

    private String actNum = "";
    private String intCrBal = "";
    private String intDrBal = "";
    private String chkBook = "";
    private String nroStatus = "";
    private String mobileBanking = "";
    private String atmCard = "";
    private String atmCardNo = "";
    private Date atmCardExprdt = null;
    private String crCard = "";
    private String crCardNo = "";
    private Date crCardExprdt = null;
    private String drCard = "";
    private String drCardNo = "";
    private Date drCardExprdt = null;
    private String abb = "";
    private Double abbChrg = null;
    private String npa = "";
    private Date npaDt = null;
    private String stopPayChrg = "";
    private String modeOfOperation = "";
    private String settlementMode = "";
    private Double actOpenChrg = null;
    private String chkReturn = "";
    private String inopChrg = "";
    private String statChrg = "";
    private String nonmainChrg = "";
    private Date lastDrIntAppldt = null;
    private String custgrpLimitValidation = "";
    private Double minActBal = null;
    private Double chkBookChrg = null;
    private Double reqFlexiPd = null;
    private Double miscServChrg = null;
    private Double folioChrg = null;
    private Double actClosingChrg = null;
    private Double statFreq = null;
    private Double excessWithdChrg = null;
    private Date lastCrIntAppldt = null;
    private Date atmCardValidfrom = null;
    private Date crCardValidfrom = null;
    private Date drCardValidfrom = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;

    /**
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    /**
     * Setter/Getter for INT_CR_BAL - table Field
     */
    public void setIntCrBal(String intCrBal) {
        this.intCrBal = intCrBal;
    }

    public String getIntCrBal() {
        return intCrBal;
    }

    /**
     * Setter/Getter for INT_DR_BAL - table Field
     */
    public void setIntDrBal(String intDrBal) {
        this.intDrBal = intDrBal;
    }

    public String getIntDrBal() {
        return intDrBal;
    }

    /**
     * Setter/Getter for CHK_BOOK - table Field
     */
    public void setChkBook(String chkBook) {
        this.chkBook = chkBook;
    }

    public String getChkBook() {
        return chkBook;
    }

    /**
     * Setter/Getter for NRO_STATUS - table Field
     */
    public void setNroStatus(String nroStatus) {
        this.nroStatus = nroStatus;
    }

    public String getNroStatus() {
        return nroStatus;
    }

    /**
     * Setter/Getter for MOBILE_BANKING - table Field
     */
    public void setMobileBanking(String mobileBanking) {
        this.mobileBanking = mobileBanking;
    }

    public String getMobileBanking() {
        return mobileBanking;
    }

    /**
     * Setter/Getter for ATM_CARD - table Field
     */
    public void setAtmCard(String atmCard) {
        this.atmCard = atmCard;
    }

    public String getAtmCard() {
        return atmCard;
    }

    /**
     * Setter/Getter for ATM_CARD_NO - table Field
     */
    public void setAtmCardNo(String atmCardNo) {
        this.atmCardNo = atmCardNo;
    }

    public String getAtmCardNo() {
        return atmCardNo;
    }

    /**
     * Setter/Getter for ATM_CARD_EXPRDT - table Field
     */
    public void setAtmCardExprdt(Date atmCardExprdt) {
        this.atmCardExprdt = atmCardExprdt;
    }

    public Date getAtmCardExprdt() {
        return atmCardExprdt;
    }

    /**
     * Setter/Getter for CR_CARD - table Field
     */
    public void setCrCard(String crCard) {
        this.crCard = crCard;
    }

    public String getCrCard() {
        return crCard;
    }

    /**
     * Setter/Getter for CR_CARD_NO - table Field
     */
    public void setCrCardNo(String crCardNo) {
        this.crCardNo = crCardNo;
    }

    public String getCrCardNo() {
        return crCardNo;
    }

    /**
     * Setter/Getter for CR_CARD_EXPRDT - table Field
     */
    public void setCrCardExprdt(Date crCardExprdt) {
        this.crCardExprdt = crCardExprdt;
    }

    public Date getCrCardExprdt() {
        return crCardExprdt;
    }

    /**
     * Setter/Getter for DR_CARD - table Field
     */
    public void setDrCard(String drCard) {
        this.drCard = drCard;
    }

    public String getDrCard() {
        return drCard;
    }

    /**
     * Setter/Getter for DR_CARD_NO - table Field
     */
    public void setDrCardNo(String drCardNo) {
        this.drCardNo = drCardNo;
    }

    public String getDrCardNo() {
        return drCardNo;
    }

    /**
     * Setter/Getter for DR_CARD_EXPRDT - table Field
     */
    public void setDrCardExprdt(Date drCardExprdt) {
        this.drCardExprdt = drCardExprdt;
    }

    public Date getDrCardExprdt() {
        return drCardExprdt;
    }

    /**
     * Setter/Getter for ABB - table Field
     */
    public void setAbb(String abb) {
        this.abb = abb;
    }

    public String getAbb() {
        return abb;
    }

    /**
     * Setter/Getter for ABB_CHRG - table Field
     */
    public void setAbbChrg(Double abbChrg) {
        this.abbChrg = abbChrg;
    }

    public Double getAbbChrg() {
        return abbChrg;
    }

    /**
     * Setter/Getter for NPA - table Field
     */
    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getNpa() {
        return npa;
    }

    /**
     * Setter/Getter for NPA_DT - table Field
     */
    public void setNpaDt(Date npaDt) {
        this.npaDt = npaDt;
    }

    public Date getNpaDt() {
        return npaDt;
    }

    /**
     * Setter/Getter for STOP_PAY_CHRG - table Field
     */
    public void setStopPayChrg(String stopPayChrg) {
        this.stopPayChrg = stopPayChrg;
    }

    public String getStopPayChrg() {
        return stopPayChrg;
    }

    /**
     * Setter/Getter for MODE_OF_OPERATION - table Field
     */
    public void setModeOfOperation(String modeOfOperation) {
        this.modeOfOperation = modeOfOperation;
    }

    public String getModeOfOperation() {
        return modeOfOperation;
    }

    /**
     * Setter/Getter for SETTLEMENT_MODE - table Field
     */
    public void setSettlementMode(String settlementMode) {
        this.settlementMode = settlementMode;
    }

    public String getSettlementMode() {
        return settlementMode;
    }

    /**
     * Setter/Getter for ACT_OPEN_CHRG - table Field
     */
    public void setActOpenChrg(Double actOpenChrg) {
        this.actOpenChrg = actOpenChrg;
    }

    public Double getActOpenChrg() {
        return actOpenChrg;
    }

    /**
     * Setter/Getter for CHK_RETURN - table Field
     */
    public void setChkReturn(String chkReturn) {
        this.chkReturn = chkReturn;
    }

    public String getChkReturn() {
        return chkReturn;
    }

    /**
     * Setter/Getter for INOP_CHRG - table Field
     */
    public void setInopChrg(String inopChrg) {
        this.inopChrg = inopChrg;
    }

    public String getInopChrg() {
        return inopChrg;
    }

    /**
     * Setter/Getter for STAT_CHRG - table Field
     */
    public void setStatChrg(String statChrg) {
        this.statChrg = statChrg;
    }

    public String getStatChrg() {
        return statChrg;
    }

    /**
     * Setter/Getter for NONMAIN_CHRG - table Field
     */
    public void setNonmainChrg(String nonmainChrg) {
        this.nonmainChrg = nonmainChrg;
    }

    public String getNonmainChrg() {
        return nonmainChrg;
    }

    /**
     * Setter/Getter for LAST_DR_INT_APPLDT - table Field
     */
    public void setLastDrIntAppldt(Date lastDrIntAppldt) {
        this.lastDrIntAppldt = lastDrIntAppldt;
    }

    public Date getLastDrIntAppldt() {
        return lastDrIntAppldt;
    }

    /**
     * Setter/Getter for CUSTGRP_LIMIT_VALIDATION - table Field
     */
    public void setCustgrpLimitValidation(String custgrpLimitValidation) {
        this.custgrpLimitValidation = custgrpLimitValidation;
    }

    public String getCustgrpLimitValidation() {
        return custgrpLimitValidation;
    }

    /**
     * Setter/Getter for MIN_ACT_BAL - table Field
     */
    public void setMinActBal(Double minActBal) {
        this.minActBal = minActBal;
    }

    public Double getMinActBal() {
        return minActBal;
    }

    /**
     * Setter/Getter for CHK_BOOK_CHRG - table Field
     */
    public void setChkBookChrg(Double chkBookChrg) {
        this.chkBookChrg = chkBookChrg;
    }

    public Double getChkBookChrg() {
        return chkBookChrg;
    }

    /**
     * Setter/Getter for REQ_FLEXI_PD - table Field
     */
    public void setReqFlexiPd(Double reqFlexiPd) {
        this.reqFlexiPd = reqFlexiPd;
    }

    public Double getReqFlexiPd() {
        return reqFlexiPd;
    }

    /**
     * Setter/Getter for MISC_SERV_CHRG - table Field
     */
    public void setMiscServChrg(Double miscServChrg) {
        this.miscServChrg = miscServChrg;
    }

    public Double getMiscServChrg() {
        return miscServChrg;
    }

    /**
     * Setter/Getter for FOLIO_CHRG - table Field
     */
    public void setFolioChrg(Double folioChrg) {
        this.folioChrg = folioChrg;
    }

    public Double getFolioChrg() {
        return folioChrg;
    }

    /**
     * Setter/Getter for ACT_CLOSING_CHRG - table Field
     */
    public void setActClosingChrg(Double actClosingChrg) {
        this.actClosingChrg = actClosingChrg;
    }

    public Double getActClosingChrg() {
        return actClosingChrg;
    }

    /**
     * Setter/Getter for STAT_FREQ - table Field
     */
    public void setStatFreq(Double statFreq) {
        this.statFreq = statFreq;
    }

    public Double getStatFreq() {
        return statFreq;
    }

    /**
     * Setter/Getter for EXCESS_WITHD_CHRG - table Field
     */
    public void setExcessWithdChrg(Double excessWithdChrg) {
        this.excessWithdChrg = excessWithdChrg;
    }

    public Double getExcessWithdChrg() {
        return excessWithdChrg;
    }

    /**
     * Setter/Getter for LAST_CR_INT_APPLDT - table Field
     */
    public void setLastCrIntAppldt(Date lastCrIntAppldt) {
        this.lastCrIntAppldt = lastCrIntAppldt;
    }

    public Date getLastCrIntAppldt() {
        return lastCrIntAppldt;
    }

    /**
     * Setter/Getter for ATM_CARD_VALIDFROM - table Field
     */
    public void setAtmCardValidfrom(Date atmCardValidfrom) {
        this.atmCardValidfrom = atmCardValidfrom;
    }

    public Date getAtmCardValidfrom() {
        return atmCardValidfrom;
    }

    /**
     * Setter/Getter for CR_CARD_VALIDFROM - table Field
     */
    public void setCrCardValidfrom(Date crCardValidfrom) {
        this.crCardValidfrom = crCardValidfrom;
    }

    public Date getCrCardValidfrom() {
        return crCardValidfrom;
    }

    /**
     * Setter/Getter for DR_CARD_VALIDFROM - table Field
     */
    public void setDrCardValidfrom(Date drCardValidfrom) {
        this.drCardValidfrom = drCardValidfrom;
    }

    public Date getDrCardValidfrom() {
        return drCardValidfrom;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("actNum");
        return actNum;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("intCrBal", intCrBal));
        strB.append(getTOString("intDrBal", intDrBal));
        strB.append(getTOString("chkBook", chkBook));
        strB.append(getTOString("nroStatus", nroStatus));
        strB.append(getTOString("mobileBanking", mobileBanking));
        strB.append(getTOString("atmCard", atmCard));
        strB.append(getTOString("atmCardNo", atmCardNo));
        strB.append(getTOString("atmCardExprdt", atmCardExprdt));
        strB.append(getTOString("crCard", crCard));
        strB.append(getTOString("crCardNo", crCardNo));
        strB.append(getTOString("crCardExprdt", crCardExprdt));
        strB.append(getTOString("drCard", drCard));
        strB.append(getTOString("drCardNo", drCardNo));
        strB.append(getTOString("drCardExprdt", drCardExprdt));
        strB.append(getTOString("abb", abb));
        strB.append(getTOString("abbChrg", abbChrg));
        strB.append(getTOString("npa", npa));
        strB.append(getTOString("npaDt", npaDt));
        strB.append(getTOString("stopPayChrg", stopPayChrg));
        strB.append(getTOString("modeOfOperation", modeOfOperation));
        strB.append(getTOString("settlementMode", settlementMode));
        strB.append(getTOString("actOpenChrg", actOpenChrg));
        strB.append(getTOString("chkReturn", chkReturn));
        strB.append(getTOString("inopChrg", inopChrg));
        strB.append(getTOString("statChrg", statChrg));
        strB.append(getTOString("nonmainChrg", nonmainChrg));
        strB.append(getTOString("lastDrIntAppldt", lastDrIntAppldt));
        strB.append(getTOString("custgrpLimitValidation", custgrpLimitValidation));
        strB.append(getTOString("minActBal", minActBal));
        strB.append(getTOString("chkBookChrg", chkBookChrg));
        strB.append(getTOString("reqFlexiPd", reqFlexiPd));
        strB.append(getTOString("miscServChrg", miscServChrg));
        strB.append(getTOString("folioChrg", folioChrg));
        strB.append(getTOString("actClosingChrg", actClosingChrg));
        strB.append(getTOString("statFreq", statFreq));
        strB.append(getTOString("excessWithdChrg", excessWithdChrg));
        strB.append(getTOString("lastCrIntAppldt", lastCrIntAppldt));
        strB.append(getTOString("atmCardValidfrom", atmCardValidfrom));
        strB.append(getTOString("crCardValidfrom", crCardValidfrom));
        strB.append(getTOString("drCardValidfrom", drCardValidfrom));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("intCrBal", intCrBal));
        strB.append(getTOXml("intDrBal", intDrBal));
        strB.append(getTOXml("chkBook", chkBook));
        strB.append(getTOXml("nroStatus", nroStatus));
        strB.append(getTOXml("mobileBanking", mobileBanking));
        strB.append(getTOXml("atmCard", atmCard));
        strB.append(getTOXml("atmCardNo", atmCardNo));
        strB.append(getTOXml("atmCardExprdt", atmCardExprdt));
        strB.append(getTOXml("crCard", crCard));
        strB.append(getTOXml("crCardNo", crCardNo));
        strB.append(getTOXml("crCardExprdt", crCardExprdt));
        strB.append(getTOXml("drCard", drCard));
        strB.append(getTOXml("drCardNo", drCardNo));
        strB.append(getTOXml("drCardExprdt", drCardExprdt));
        strB.append(getTOXml("abb", abb));
        strB.append(getTOXml("abbChrg", abbChrg));
        strB.append(getTOXml("npa", npa));
        strB.append(getTOXml("npaDt", npaDt));
        strB.append(getTOXml("stopPayChrg", stopPayChrg));
        strB.append(getTOXml("modeOfOperation", modeOfOperation));
        strB.append(getTOXml("settlementMode", settlementMode));
        strB.append(getTOXml("actOpenChrg", actOpenChrg));
        strB.append(getTOXml("chkReturn", chkReturn));
        strB.append(getTOXml("inopChrg", inopChrg));
        strB.append(getTOXml("statChrg", statChrg));
        strB.append(getTOXml("nonmainChrg", nonmainChrg));
        strB.append(getTOXml("lastDrIntAppldt", lastDrIntAppldt));
        strB.append(getTOXml("custgrpLimitValidation", custgrpLimitValidation));
        strB.append(getTOXml("minActBal", minActBal));
        strB.append(getTOXml("chkBookChrg", chkBookChrg));
        strB.append(getTOXml("reqFlexiPd", reqFlexiPd));
        strB.append(getTOXml("miscServChrg", miscServChrg));
        strB.append(getTOXml("folioChrg", folioChrg));
        strB.append(getTOXml("actClosingChrg", actClosingChrg));
        strB.append(getTOXml("statFreq", statFreq));
        strB.append(getTOXml("excessWithdChrg", excessWithdChrg));
        strB.append(getTOXml("lastCrIntAppldt", lastCrIntAppldt));
        strB.append(getTOXml("atmCardValidfrom", atmCardValidfrom));
        strB.append(getTOXml("crCardValidfrom", crCardValidfrom));
        strB.append(getTOXml("drCardValidfrom", drCardValidfrom));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}