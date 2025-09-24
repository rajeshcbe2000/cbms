/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductChargesTO.java
 * 
 * Created on Fri May 14 16:02:43 IST 2004
 */
package com.see.truetransact.transferobject.product.loan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_CHARGES.
 */
public class LoanProductChargesTO extends TransferObject implements Serializable {

    private String prodId = "";
    private Double acClosingChrg = null;
    private Double miscServChrg = null;
    private String statChrg = "";
    private Double statChrgRate = null;
    private String folioChrgAppl = "";
    private Date lastFolioChrgon = null;
    private Double noEntriesPerFolio = null;
    private Date nextFolioDuedate = null;
    private Double ratePerFolio = null;
    private Double folioChrgApplfreq = null;
    private String toCollectFoliochrg = "";
    private String toCollectChrgOn = "";
    private Double incompFolioRoundoff = null;
    private String procChrg = "";
    private Double procChrgPer = null;
    private Double procChrgAmt = null;
    private String commitChrg = "";
    private Double commitChrgPer = null;
    private Double commitChrgAmt = null;
    private String folioChargeType = "";
    private String creditStampAdvance = "";
    private String creditNoticeAdvance = "";

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
     * Setter/Getter for AC_CLOSING_CHRG - table Field
     */
    public void setAcClosingChrg(Double acClosingChrg) {
        this.acClosingChrg = acClosingChrg;
    }

    public Double getAcClosingChrg() {
        return acClosingChrg;
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
     * Setter/Getter for STAT_CHRG - table Field
     */
    public void setStatChrg(String statChrg) {
        this.statChrg = statChrg;
    }

    public String getStatChrg() {
        return statChrg;
    }

    /**
     * Setter/Getter for STAT_CHRG_RATE - table Field
     */
    public void setStatChrgRate(Double statChrgRate) {
        this.statChrgRate = statChrgRate;
    }

    public Double getStatChrgRate() {
        return statChrgRate;
    }

    /**
     * Setter/Getter for FOLIO_CHRG_APPL - table Field
     */
    public void setFolioChrgAppl(String folioChrgAppl) {
        this.folioChrgAppl = folioChrgAppl;
    }

    public String getFolioChrgAppl() {
        return folioChrgAppl;
    }

    /**
     * Setter/Getter for LAST_FOLIO_CHRGON - table Field
     */
    public void setLastFolioChrgon(Date lastFolioChrgon) {
        this.lastFolioChrgon = lastFolioChrgon;
    }

    public Date getLastFolioChrgon() {
        return lastFolioChrgon;
    }

    /**
     * Setter/Getter for NO_ENTRIES_PER_FOLIO - table Field
     */
    public void setNoEntriesPerFolio(Double noEntriesPerFolio) {
        this.noEntriesPerFolio = noEntriesPerFolio;
    }

    public Double getNoEntriesPerFolio() {
        return noEntriesPerFolio;
    }

    /**
     * Setter/Getter for NEXT_FOLIO_DUEDATE - table Field
     */
    public void setNextFolioDuedate(Date nextFolioDuedate) {
        this.nextFolioDuedate = nextFolioDuedate;
    }

    public Date getNextFolioDuedate() {
        return nextFolioDuedate;
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
     * Setter/Getter for FOLIO_CHRG_APPLFREQ - table Field
     */
    public void setFolioChrgApplfreq(Double folioChrgApplfreq) {
        this.folioChrgApplfreq = folioChrgApplfreq;
    }

    public Double getFolioChrgApplfreq() {
        return folioChrgApplfreq;
    }

    /**
     * Setter/Getter for TO_COLLECT_FOLIOCHRG - table Field
     */
    public void setToCollectFoliochrg(String toCollectFoliochrg) {
        this.toCollectFoliochrg = toCollectFoliochrg;
    }

    public String getToCollectFoliochrg() {
        return toCollectFoliochrg;
    }

    /**
     * Setter/Getter for TO_COLLECT_CHRG_ON - table Field
     */
    public void setToCollectChrgOn(String toCollectChrgOn) {
        this.toCollectChrgOn = toCollectChrgOn;
    }

    public String getToCollectChrgOn() {
        return toCollectChrgOn;
    }

    /**
     * Setter/Getter for INCOMP_FOLIO_ROUNDOFF - table Field
     */
    public void setIncompFolioRoundoff(Double incompFolioRoundoff) {
        this.incompFolioRoundoff = incompFolioRoundoff;
    }

    public Double getIncompFolioRoundoff() {
        return incompFolioRoundoff;
    }

    /**
     * Setter/Getter for PROC_CHRG - table Field
     */
    public void setProcChrg(String procChrg) {
        this.procChrg = procChrg;
    }

    public String getProcChrg() {
        return procChrg;
    }

    /**
     * Setter/Getter for PROC_CHRG_PER - table Field
     */
    public void setProcChrgPer(Double procChrgPer) {
        this.procChrgPer = procChrgPer;
    }

    public Double getProcChrgPer() {
        return procChrgPer;
    }

    /**
     * Setter/Getter for PROC_CHRG_AMT - table Field
     */
    public void setProcChrgAmt(Double procChrgAmt) {
        this.procChrgAmt = procChrgAmt;
    }

    public Double getProcChrgAmt() {
        return procChrgAmt;
    }

    /**
     * Setter/Getter for COMMIT_CHRG - table Field
     */
    public void setCommitChrg(String commitChrg) {
        this.commitChrg = commitChrg;
    }

    public String getCommitChrg() {
        return commitChrg;
    }

    /**
     * Setter/Getter for COMMIT_CHRG_PER - table Field
     */
    public void setCommitChrgPer(Double commitChrgPer) {
        this.commitChrgPer = commitChrgPer;
    }

    public Double getCommitChrgPer() {
        return commitChrgPer;
    }

    /**
     * Setter/Getter for COMMIT_CHRG_AMT - table Field
     */
    public void setCommitChrgAmt(Double commitChrgAmt) {
        this.commitChrgAmt = commitChrgAmt;
    }

    public Double getCommitChrgAmt() {
        return commitChrgAmt;
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
        strB.append(getTOString("acClosingChrg", acClosingChrg));
        strB.append(getTOString("miscServChrg", miscServChrg));
        strB.append(getTOString("statChrg", statChrg));
        strB.append(getTOString("statChrgRate", statChrgRate));
        strB.append(getTOString("folioChrgAppl", folioChrgAppl));
        strB.append(getTOString("lastFolioChrgon", lastFolioChrgon));
        strB.append(getTOString("noEntriesPerFolio", noEntriesPerFolio));
        strB.append(getTOString("nextFolioDuedate", nextFolioDuedate));
        strB.append(getTOString("ratePerFolio", ratePerFolio));
        strB.append(getTOString("folioChrgApplfreq", folioChrgApplfreq));
        strB.append(getTOString("toCollectFoliochrg", toCollectFoliochrg));
        strB.append(getTOString("toCollectChrgOn", toCollectChrgOn));
        strB.append(getTOString("incompFolioRoundoff", incompFolioRoundoff));
        strB.append(getTOString("procChrg", procChrg));
        strB.append(getTOString("procChrgPer", procChrgPer));
        strB.append(getTOString("procChrgAmt", procChrgAmt));
        strB.append(getTOString("commitChrg", commitChrg));
        strB.append(getTOString("commitChrgPer", commitChrgPer));
        strB.append(getTOString("commitChrgAmt", commitChrgAmt));
        strB.append(getTOString("folioChargeType", folioChargeType));
        strB.append(getTOString("creditStampAdvance", creditStampAdvance));
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
        strB.append(getTOXml("acClosingChrg", acClosingChrg));
        strB.append(getTOXml("miscServChrg", miscServChrg));
        strB.append(getTOXml("statChrg", statChrg));
        strB.append(getTOXml("statChrgRate", statChrgRate));
        strB.append(getTOXml("folioChrgAppl", folioChrgAppl));
        strB.append(getTOXml("lastFolioChrgon", lastFolioChrgon));
        strB.append(getTOXml("noEntriesPerFolio", noEntriesPerFolio));
        strB.append(getTOXml("nextFolioDuedate", nextFolioDuedate));
        strB.append(getTOXml("ratePerFolio", ratePerFolio));
        strB.append(getTOXml("folioChrgApplfreq", folioChrgApplfreq));
        strB.append(getTOXml("toCollectFoliochrg", toCollectFoliochrg));
        strB.append(getTOXml("toCollectChrgOn", toCollectChrgOn));
        strB.append(getTOXml("incompFolioRoundoff", incompFolioRoundoff));
        strB.append(getTOXml("procChrg", procChrg));
        strB.append(getTOXml("procChrgPer", procChrgPer));
        strB.append(getTOXml("procChrgAmt", procChrgAmt));
        strB.append(getTOXml("commitChrg", commitChrg));
        strB.append(getTOXml("commitChrgPer", commitChrgPer));
        strB.append(getTOXml("commitChrgAmt", commitChrgAmt));
        strB.append(getTOXml("folioChargeType", folioChargeType));
        strB.append(getTOXml("creditStampAdvance", creditStampAdvance));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property folioChargeType.
     *
     * @return Value of property folioChargeType.
     */
    public java.lang.String getFolioChargeType() {
        return folioChargeType;
    }

    /**
     * Setter for property folioChargeType.
     *
     * @param folioChargeType New value of property folioChargeType.
     */
    public void setFolioChargeType(java.lang.String folioChargeType) {
        this.folioChargeType = folioChargeType;
    }

    /**
     * Getter for property creditStampAdvance.
     *
     * @return Value of property creditStampAdvance.
     */
    public java.lang.String getCreditStampAdvance() {
        return creditStampAdvance;
    }

    /**
     * Setter for property creditStampAdvance.
     *
     * @param creditStampAdvance New value of property creditStampAdvance.
     */
    public void setCreditStampAdvance(java.lang.String creditStampAdvance) {
        this.creditStampAdvance = creditStampAdvance;
    }

    public String getCreditNoticeAdvance() {
        return creditNoticeAdvance;
    }

    public void setCreditNoticeAdvance(String creditNoticeAdvance) {
        this.creditNoticeAdvance = creditNoticeAdvance;
    }
}