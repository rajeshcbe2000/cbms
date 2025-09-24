/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctChargesParamTO.java
 * 
 * Created on Thu Jul 22 10:30:38 IST 2004
 */
package com.see.truetransact.transferobject.product.operativeacct;

import java.io.Serializable;
import java.util.Date;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OP_AC_CHARGES_PARAM.
 */
public class OperativeAcctChargesParamTO extends TransferObject implements Serializable {

    private String prodId = "";
    private Double inoperativeAcCharges = null;
    private Double inoperativeAcChargePd = null;
    private Double chgPrematureClosure = null;
    private Double acctClosingChg = null;
    private Double miscServiceChg = null;
    private String nonmainMinBalChg = "";
    private Double amtNonmainMinbal = null;
    private String amtNonmainMinbalPd = "";
    private String statCharge = "";
    private Double amtChargeStat = null;
    private String amtChargeStatType = "";
    private String chkIssueChg = "";
    private Double chkIssueChgperleaf = null;
    private String stopPaymentChg = "";
    private Double stopPaymentAmtchg = null;
    private Double chkReturnChgOutward = null;
    private Double chkReturnChgInward = null;
    private Double acctOpeningChg = null;
    private String folioChgApplicable = "";
    private Double noEntriesperFolio = null;
    private Double ratePerFolio = null;
    private String toChargeOn = "";
    private Double folioChgApplFreq = null;
    private String toCollectFolioChg = "";
    private String toChargeOnType = "";
    private String incompleteFolioRoundFreq = "";
    private Double chgExcessfreewdPertrans = null;
    private String outstChkCharges = "";
    private Date lastFolioChargedt = null;
    private Date nextFolioChargedt = null;

    // Added by nithya on 17-03-2016 for 0004021
    
    private String debitWithdrawalChargeType = null;
    private Double debitWithdrawalChargeRate = null;
    private String cboFolioChargeRestrictionFrq="";
    private Integer txtFolioChargeRestrictionPeriod=0;
    private String txtFolioChargeType="";
    

    
    public Double getDebitWithdrawalChargeRate() {
        return debitWithdrawalChargeRate;
    }

    public String getDebitWithdrawalChargeType() {
        return debitWithdrawalChargeType;
    }   

    public void setDebitWithdrawalChargeRate(Double debitWithdrawalChargeRate) {
        this.debitWithdrawalChargeRate = debitWithdrawalChargeRate;
    }

    public void setDebitWithdrawalChargeType(String debitWithdrawalChargeType) {
        this.debitWithdrawalChargeType = debitWithdrawalChargeType;
    }

   
    
    // End
    
    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }

    /**
     * Setter/Getter for INOPERATIVE_AC_CHARGES - table Field
     */
    public void setInoperativeAcCharges(Double inoperativeAcCharges) {
        this.inoperativeAcCharges = inoperativeAcCharges;
    }

    public Double getInoperativeAcCharges() {
        return inoperativeAcCharges;
    }

    /**
     * Setter/Getter for INOPERATIVE_AC_CHARGE_PD - table Field
     */
    public void setInoperativeAcChargePd(Double inoperativeAcChargePd) {
        this.inoperativeAcChargePd = inoperativeAcChargePd;
    }

    public Double getInoperativeAcChargePd() {
        return inoperativeAcChargePd;
    }

    /**
     * Setter/Getter for CHG_PREMATURE_CLOSURE - table Field
     */
    public void setChgPrematureClosure(Double chgPrematureClosure) {
        this.chgPrematureClosure = chgPrematureClosure;
    }

    public Double getChgPrematureClosure() {
        return chgPrematureClosure;
    }

    /**
     * Setter/Getter for ACCT_CLOSING_CHG - table Field
     */
    public void setAcctClosingChg(Double acctClosingChg) {
        this.acctClosingChg = acctClosingChg;
    }

    public Double getAcctClosingChg() {
        return acctClosingChg;
    }

    /**
     * Setter/Getter for MISC_SERVICE_CHG - table Field
     */
    public void setMiscServiceChg(Double miscServiceChg) {
        this.miscServiceChg = miscServiceChg;
    }

    public Double getMiscServiceChg() {
        return miscServiceChg;
    }

    /**
     * Setter/Getter for NONMAIN_MIN_BAL_CHG - table Field
     */
    public void setNonmainMinBalChg(String nonmainMinBalChg) {
        this.nonmainMinBalChg = nonmainMinBalChg;
    }

    public String getNonmainMinBalChg() {
        return nonmainMinBalChg;
    }

    /**
     * Setter/Getter for AMT_NONMAIN_MINBAL - table Field
     */
    public void setAmtNonmainMinbal(Double amtNonmainMinbal) {
        this.amtNonmainMinbal = amtNonmainMinbal;
    }

    public Double getAmtNonmainMinbal() {
        return amtNonmainMinbal;
    }

    /**
     * Setter/Getter for AMT_NONMAIN_MINBAL_PD - table Field
     */
    public void setAmtNonmainMinbalPd(String amtNonmainMinbalPd) {
        this.amtNonmainMinbalPd = amtNonmainMinbalPd;
    }

    public String getAmtNonmainMinbalPd() {
        return amtNonmainMinbalPd;
    }

    /**
     * Setter/Getter for STAT_CHARGE - table Field
     */
    public void setStatCharge(String statCharge) {
        this.statCharge = statCharge;
    }

    public String getStatCharge() {
        return statCharge;
    }

    /**
     * Setter/Getter for AMT_CHARGE_STAT - table Field
     */
    public void setAmtChargeStat(Double amtChargeStat) {
        this.amtChargeStat = amtChargeStat;
    }

    public Double getAmtChargeStat() {
        return amtChargeStat;
    }

    /**
     * Setter/Getter for AMT_CHARGE_STAT_TYPE - table Field
     */
    public void setAmtChargeStatType(String amtChargeStatType) {
        this.amtChargeStatType = amtChargeStatType;
    }

    public String getAmtChargeStatType() {
        return amtChargeStatType;
    }

    /**
     * Setter/Getter for CHK_ISSUE_CHG - table Field
     */
    public void setChkIssueChg(String chkIssueChg) {
        this.chkIssueChg = chkIssueChg;
    }

    public String getChkIssueChg() {
        return chkIssueChg;
    }

    /**
     * Setter/Getter for CHK_ISSUE_CHGPERLEAF - table Field
     */
    public void setChkIssueChgperleaf(Double chkIssueChgperleaf) {
        this.chkIssueChgperleaf = chkIssueChgperleaf;
    }

    public Double getChkIssueChgperleaf() {
        return chkIssueChgperleaf;
    }

    /**
     * Setter/Getter for STOP_PAYMENT_CHG - table Field
     */
    public void setStopPaymentChg(String stopPaymentChg) {
        this.stopPaymentChg = stopPaymentChg;
    }

    public String getStopPaymentChg() {
        return stopPaymentChg;
    }

    /**
     * Setter/Getter for STOP_PAYMENT_AMTCHG - table Field
     */
    public void setStopPaymentAmtchg(Double stopPaymentAmtchg) {
        this.stopPaymentAmtchg = stopPaymentAmtchg;
    }

    public Double getStopPaymentAmtchg() {
        return stopPaymentAmtchg;
    }

    /**
     * Setter/Getter for CHK_RETURN_CHG_OUTWARD - table Field
     */
    public void setChkReturnChgOutward(Double chkReturnChgOutward) {
        this.chkReturnChgOutward = chkReturnChgOutward;
    }

    public Double getChkReturnChgOutward() {
        return chkReturnChgOutward;
    }

    /**
     * Setter/Getter for CHK_RETURN_CHG_INWARD - table Field
     */
    public void setChkReturnChgInward(Double chkReturnChgInward) {
        this.chkReturnChgInward = chkReturnChgInward;
    }

    public Double getChkReturnChgInward() {
        return chkReturnChgInward;
    }

    /**
     * Setter/Getter for ACCT_OPENING_CHG - table Field
     */
    public void setAcctOpeningChg(Double acctOpeningChg) {
        this.acctOpeningChg = acctOpeningChg;
    }

    public Double getAcctOpeningChg() {
        return acctOpeningChg;
    }

    /**
     * Setter/Getter for FOLIO_CHG_APPLICABLE - table Field
     */
    public void setFolioChgApplicable(String folioChgApplicable) {
        this.folioChgApplicable = folioChgApplicable;
    }

    public String getFolioChgApplicable() {
        return folioChgApplicable;
    }

    /**
     * Setter/Getter for NO_ENTRIESPER_FOLIO - table Field
     */
    public void setNoEntriesperFolio(Double noEntriesperFolio) {
        this.noEntriesperFolio = noEntriesperFolio;
    }

    public Double getNoEntriesperFolio() {
        return noEntriesperFolio;
    }

    /**
     * Setter/Getter for RATE_PER_FOLIO - table Field
     */
    public void setRatePerFolio(Double ratePerFolio) {
        this.ratePerFolio = ratePerFolio;
    }

    public Double getRatePerFolio() {
        return ratePerFolio;
    }

    /**
     * Setter/Getter for TO_CHARGE_ON - table Field
     */
    public void setToChargeOn(String toChargeOn) {
        this.toChargeOn = toChargeOn;
    }

    public String getToChargeOn() {
        return toChargeOn;
    }

    /**
     * Setter/Getter for FOLIO_CHG_APPL_FREQ - table Field
     */
    public void setFolioChgApplFreq(Double folioChgApplFreq) {
        this.folioChgApplFreq = folioChgApplFreq;
    }

    public Double getFolioChgApplFreq() {
        return folioChgApplFreq;
    }

    /**
     * Setter/Getter for TO_COLLECT_FOLIO_CHG - table Field
     */
    public void setToCollectFolioChg(String toCollectFolioChg) {
        this.toCollectFolioChg = toCollectFolioChg;
    }

    public String getToCollectFolioChg() {
        return toCollectFolioChg;
    }

    /**
     * Setter/Getter for TO_CHARGE_ON_TYPE - table Field
     */
    public void setToChargeOnType(String toChargeOnType) {
        this.toChargeOnType = toChargeOnType;
    }

    public String getToChargeOnType() {
        return toChargeOnType;
    }

    /**
     * Setter/Getter for INCOMPLETE_FOLIO_ROUND_FREQ - table Field
     */
    public void setIncompleteFolioRoundFreq(String incompleteFolioRoundFreq) {
        this.incompleteFolioRoundFreq = incompleteFolioRoundFreq;
    }

    public String getIncompleteFolioRoundFreq() {
        return incompleteFolioRoundFreq;
    }

    /**
     * Setter/Getter for CHG_EXCESSFREEWD_PERTRANS - table Field
     */
    public void setChgExcessfreewdPertrans(Double chgExcessfreewdPertrans) {
        this.chgExcessfreewdPertrans = chgExcessfreewdPertrans;
    }

    public Double getChgExcessfreewdPertrans() {
        return chgExcessfreewdPertrans;
    }

    /**
     * Setter/Getter for OUTST_CHK_CHARGES - table Field
     */
    public void setOutstChkCharges(String outstChkCharges) {
        this.outstChkCharges = outstChkCharges;
    }

    public String getOutstChkCharges() {
        return outstChkCharges;
    }

    public String getCboFolioChargeRestrictionFrq() {
        return cboFolioChargeRestrictionFrq;
    }

    public void setCboFolioChargeRestrictionFrq(String cboFolioChargeRestrictionFrq) {
        this.cboFolioChargeRestrictionFrq = cboFolioChargeRestrictionFrq;
    }

    public Integer getTxtFolioChargeRestrictionPeriod() {
        return txtFolioChargeRestrictionPeriod;
    }

    public void setTxtFolioChargeRestrictionPeriod(Integer txtFolioChargeRestrictionPeriod) {
        this.txtFolioChargeRestrictionPeriod = txtFolioChargeRestrictionPeriod;
    }


    public String getTxtFolioChargeType() {
        return txtFolioChargeType;
    }

    public void setTxtFolioChargeType(String txtFolioChargeType) {
        this.txtFolioChargeType = txtFolioChargeType;
    }
    
    

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("prodId");
        return prodId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("inoperativeAcCharges", inoperativeAcCharges));
        strB.append(getTOString("inoperativeAcChargePd", inoperativeAcChargePd));
        strB.append(getTOString("chgPrematureClosure", chgPrematureClosure));
        strB.append(getTOString("acctClosingChg", acctClosingChg));
        strB.append(getTOString("miscServiceChg", miscServiceChg));
        strB.append(getTOString("nonmainMinBalChg", nonmainMinBalChg));
        strB.append(getTOString("amtNonmainMinbal", amtNonmainMinbal));
        strB.append(getTOString("amtNonmainMinbalPd", amtNonmainMinbalPd));
        strB.append(getTOString("statCharge", statCharge));
        strB.append(getTOString("amtChargeStat", amtChargeStat));
        strB.append(getTOString("amtChargeStatType", amtChargeStatType));
        strB.append(getTOString("chkIssueChg", chkIssueChg));
        strB.append(getTOString("chkIssueChgperleaf", chkIssueChgperleaf));
        strB.append(getTOString("stopPaymentChg", stopPaymentChg));
        strB.append(getTOString("stopPaymentAmtchg", stopPaymentAmtchg));
        strB.append(getTOString("chkReturnChgOutward", chkReturnChgOutward));
        strB.append(getTOString("chkReturnChgInward", chkReturnChgInward));
        strB.append(getTOString("acctOpeningChg", acctOpeningChg));
        strB.append(getTOString("folioChgApplicable", folioChgApplicable));
        strB.append(getTOString("noEntriesperFolio", noEntriesperFolio));
        strB.append(getTOString("ratePerFolio", ratePerFolio));
        strB.append(getTOString("toChargeOn", toChargeOn));
        strB.append(getTOString("folioChgApplFreq", folioChgApplFreq));
        strB.append(getTOString("toCollectFolioChg", toCollectFolioChg));
        strB.append(getTOString("toChargeOnType", toChargeOnType));
        strB.append(getTOString("incompleteFolioRoundFreq", incompleteFolioRoundFreq));
        strB.append(getTOString("chgExcessfreewdPertrans", chgExcessfreewdPertrans));
        strB.append(getTOString("outstChkCharges", outstChkCharges));
        strB.append(getTOString("lastFolioChargedt", lastFolioChargedt));
        strB.append(getTOString("nextFolioChargedt", nextFolioChargedt));        
        strB.append(getTOString("debitWithdrawalChargeType", debitWithdrawalChargeType)); // Added by nithya on 17-03-2016 for 0004021
        strB.append(getTOString("debitWithdrawalChargeRate", debitWithdrawalChargeRate)); // Added by nithya on 17-03-2016 for 0004021
        strB.append(getTOString("cboFolioChargeRestrictionFrq", cboFolioChargeRestrictionFrq));
        strB.append(getTOString("txtFolioChargeRestrictionPeriod", txtFolioChargeRestrictionPeriod));
        strB.append(getTOString("txtFolioChargeType", txtFolioChargeType));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("inoperativeAcCharges", inoperativeAcCharges));
        strB.append(getTOXml("inoperativeAcChargePd", inoperativeAcChargePd));
        strB.append(getTOXml("chgPrematureClosure", chgPrematureClosure));
        strB.append(getTOXml("acctClosingChg", acctClosingChg));
        strB.append(getTOXml("miscServiceChg", miscServiceChg));
        strB.append(getTOXml("nonmainMinBalChg", nonmainMinBalChg));
        strB.append(getTOXml("amtNonmainMinbal", amtNonmainMinbal));
        strB.append(getTOXml("amtNonmainMinbalPd", amtNonmainMinbalPd));
        strB.append(getTOXml("statCharge", statCharge));
        strB.append(getTOXml("amtChargeStat", amtChargeStat));
        strB.append(getTOXml("amtChargeStatType", amtChargeStatType));
        strB.append(getTOXml("chkIssueChg", chkIssueChg));
        strB.append(getTOXml("chkIssueChgperleaf", chkIssueChgperleaf));
        strB.append(getTOXml("stopPaymentChg", stopPaymentChg));
        strB.append(getTOXml("stopPaymentAmtchg", stopPaymentAmtchg));
        strB.append(getTOXml("chkReturnChgOutward", chkReturnChgOutward));
        strB.append(getTOXml("chkReturnChgInward", chkReturnChgInward));
        strB.append(getTOXml("acctOpeningChg", acctOpeningChg));
        strB.append(getTOXml("folioChgApplicable", folioChgApplicable));
        strB.append(getTOXml("noEntriesperFolio", noEntriesperFolio));
        strB.append(getTOXml("ratePerFolio", ratePerFolio));
        strB.append(getTOXml("toChargeOn", toChargeOn));
        strB.append(getTOXml("folioChgApplFreq", folioChgApplFreq));
        strB.append(getTOXml("toCollectFolioChg", toCollectFolioChg));
        strB.append(getTOXml("toChargeOnType", toChargeOnType));
        strB.append(getTOXml("incompleteFolioRoundFreq", incompleteFolioRoundFreq));
        strB.append(getTOXml("chgExcessfreewdPertrans", chgExcessfreewdPertrans));
        strB.append(getTOXml("outstChkCharges", outstChkCharges));
        strB.append(getTOXml("lastFolioChargedt", lastFolioChargedt));
        strB.append(getTOXml("nextFolioChargedt", nextFolioChargedt));      
        strB.append(getTOXml("debitWithdrawalChargeType", debitWithdrawalChargeType)); // Added by nithya on 17-03-2016 for 0004021
        strB.append(getTOXml("debitWithdrawalChargeRate", debitWithdrawalChargeRate)); // Added by nithya on 17-03-2016 for 0004021
        strB.append(getTOXml("cboFolioChargeRestrictionFrq", cboFolioChargeRestrictionFrq));
        strB.append(getTOXml("txtFolioChargeRestrictionPeriod", txtFolioChargeRestrictionPeriod));
        strB.append(getTOXml("txtFolioChargeType", txtFolioChargeType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property lastFolioChargedt.
     *
     * @return Value of property lastFolioChargedt.
     */
    public Date getLastFolioChargedt() {
        return lastFolioChargedt;
    }

    /**
     * Setter for property lastFolioChargedt.
     *
     * @param lastFolioChargedt New value of property lastFolioChargedt.
     */
    public void setLastFolioChargedt(Date lastFolioChargedt) {
        this.lastFolioChargedt = lastFolioChargedt;
    }

    /**
     * Getter for property nextFolioChargedt.
     *
     * @return Value of property nextFolioChargedt.
     */
    public Date getNextFolioChargedt() {
        return nextFolioChargedt;
    }

    /**
     * Setter for property nextFolioChargedt.
     *
     * @param nextFolioChargedt New value of property nextFolioChargedt.
     */
    public void setNextFolioChargedt(Date nextFolioChargedt) {
        this.nextFolioChargedt = nextFolioChargedt;
    }
}