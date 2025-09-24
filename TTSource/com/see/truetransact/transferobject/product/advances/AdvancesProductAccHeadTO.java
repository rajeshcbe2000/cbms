/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AdvancesProductAccHeadTO.java
 * 
 * Created on Mon Apr 11 18:00:35 IST 2005
 */
package com.see.truetransact.transferobject.product.advances;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ADVANCES_PROD_ACHD.
 */
public class AdvancesProductAccHeadTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String acClosingChrg = "";
    private String miscServChrg = "";
    private String statChrg = "";
    private String acDebitInt = "";
    private String penalInt = "";
    private String acCreditInt = "";
    private String clearingInt = "";
    private String expiryInt = "";
    private String excessLimit = "";
    private String chqbkIssueChrg = "";
    private String stopPayChrg = "";
    private String chqRetChrgOutward = "";
    private String chqRetChrgInward = "";
    private String folioChrgAc = "";

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
    public void setAcClosingChrg(String acClosingChrg) {
        this.acClosingChrg = acClosingChrg;
    }

    public String getAcClosingChrg() {
        return acClosingChrg;
    }

    /**
     * Setter/Getter for MISC_SERV_CHRG - table Field
     */
    public void setMiscServChrg(String miscServChrg) {
        this.miscServChrg = miscServChrg;
    }

    public String getMiscServChrg() {
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
     * Setter/Getter for AC_DEBIT_INT - table Field
     */
    public void setAcDebitInt(String acDebitInt) {
        this.acDebitInt = acDebitInt;
    }

    public String getAcDebitInt() {
        return acDebitInt;
    }

    /**
     * Setter/Getter for PENAL_INT - table Field
     */
    public void setPenalInt(String penalInt) {
        this.penalInt = penalInt;
    }

    public String getPenalInt() {
        return penalInt;
    }

    /**
     * Setter/Getter for AC_CREDIT_INT - table Field
     */
    public void setAcCreditInt(String acCreditInt) {
        this.acCreditInt = acCreditInt;
    }

    public String getAcCreditInt() {
        return acCreditInt;
    }

    /**
     * Setter/Getter for CLEARING_INT - table Field
     */
    public void setClearingInt(String clearingInt) {
        this.clearingInt = clearingInt;
    }

    public String getClearingInt() {
        return clearingInt;
    }

    /**
     * Setter/Getter for EXPIRY_INT - table Field
     */
    public void setExpiryInt(String expiryInt) {
        this.expiryInt = expiryInt;
    }

    public String getExpiryInt() {
        return expiryInt;
    }

    /**
     * Setter/Getter for EXCESS_LIMIT - table Field
     */
    public void setExcessLimit(String excessLimit) {
        this.excessLimit = excessLimit;
    }

    public String getExcessLimit() {
        return excessLimit;
    }

    /**
     * Setter/Getter for CHQBK_ISSUE_CHRG - table Field
     */
    public void setChqbkIssueChrg(String chqbkIssueChrg) {
        this.chqbkIssueChrg = chqbkIssueChrg;
    }

    public String getChqbkIssueChrg() {
        return chqbkIssueChrg;
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
     * Setter/Getter for CHQ_RET_CHRG_OUTWARD - table Field
     */
    public void setChqRetChrgOutward(String chqRetChrgOutward) {
        this.chqRetChrgOutward = chqRetChrgOutward;
    }

    public String getChqRetChrgOutward() {
        return chqRetChrgOutward;
    }

    /**
     * Setter/Getter for CHQ_RET_CHRG_INWARD - table Field
     */
    public void setChqRetChrgInward(String chqRetChrgInward) {
        this.chqRetChrgInward = chqRetChrgInward;
    }

    public String getChqRetChrgInward() {
        return chqRetChrgInward;
    }

    /**
     * Setter/Getter for FOLIO_CHRG_AC - table Field
     */
    public void setFolioChrgAc(String folioChrgAc) {
        this.folioChrgAc = folioChrgAc;
    }

    public String getFolioChrgAc() {
        return folioChrgAc;
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
        strB.append(getTOString("acDebitInt", acDebitInt));
        strB.append(getTOString("penalInt", penalInt));
        strB.append(getTOString("acCreditInt", acCreditInt));
        strB.append(getTOString("clearingInt", clearingInt));
        strB.append(getTOString("expiryInt", expiryInt));
        strB.append(getTOString("excessLimit", excessLimit));
        strB.append(getTOString("chqbkIssueChrg", chqbkIssueChrg));
        strB.append(getTOString("stopPayChrg", stopPayChrg));
        strB.append(getTOString("chqRetChrgOutward", chqRetChrgOutward));
        strB.append(getTOString("chqRetChrgInward", chqRetChrgInward));
        strB.append(getTOString("folioChrgAc", folioChrgAc));
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
        strB.append(getTOXml("acDebitInt", acDebitInt));
        strB.append(getTOXml("penalInt", penalInt));
        strB.append(getTOXml("acCreditInt", acCreditInt));
        strB.append(getTOXml("clearingInt", clearingInt));
        strB.append(getTOXml("expiryInt", expiryInt));
        strB.append(getTOXml("excessLimit", excessLimit));
        strB.append(getTOXml("chqbkIssueChrg", chqbkIssueChrg));
        strB.append(getTOXml("stopPayChrg", stopPayChrg));
        strB.append(getTOXml("chqRetChrgOutward", chqRetChrgOutward));
        strB.append(getTOXml("chqRetChrgInward", chqRetChrgInward));
        strB.append(getTOXml("folioChrgAc", folioChrgAc));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}