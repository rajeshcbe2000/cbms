/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctHeadParamTO.java
 * 
 * Created on Thu Jul 22 10:32:26 IST 2004
 */
package com.see.truetransact.transferobject.product.operativeacct;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OP_AC_ACHEAD_PARAM.
 */
public class OperativeAcctHeadParamTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String inopAcChrg = "";
    private String prematcloseChrg = "";
    private String accloseChrg = "";
    private String misserChrg = "";
    private String statChrg = "";
    private String freewithdChrg = "";
    private String debitInt = "";
    private String creditInt = "";
    private String clrInt = "";
    private String chqIssueChrg = "";
    private String stopPmtChrg = "";
    private String chqRetOut = "";
    private String chqRetIn = "";
    private String actOpChrg = "";
    private String excessFreeWithd = "";
    private String tax = "";
    private String nonmntMinChrg = "";
    private String inopt = "";
    private String folioChrg = "";
    private String debitWithdrawalCharge = ""; // Added by nithya on 17-03-2016 for 0004021
	private String atmGL="";    
    // Added by nithya on 17-03-2016 for 0004021
    public String getDebitWithdrawalCharge() {
        return debitWithdrawalCharge;
    }

    public void setDebitWithdrawalCharge(String debiWithdrawalCharge) {
        this.debitWithdrawalCharge = debiWithdrawalCharge;
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
     * Setter/Getter for INOP_AC_CHRG - table Field
     */
    public void setInopAcChrg(String inopAcChrg) {
        this.inopAcChrg = inopAcChrg;
    }

    public String getInopAcChrg() {
        return inopAcChrg;
    }

    /**
     * Setter/Getter for PREMATCLOSE_CHRG - table Field
     */
    public void setPrematcloseChrg(String prematcloseChrg) {
        this.prematcloseChrg = prematcloseChrg;
    }

    public String getPrematcloseChrg() {
        return prematcloseChrg;
    }

    /**
     * Setter/Getter for ACCLOSE_CHRG - table Field
     */
    public void setAccloseChrg(String accloseChrg) {
        this.accloseChrg = accloseChrg;
    }

    public String getAccloseChrg() {
        return accloseChrg;
    }

    /**
     * Setter/Getter for MISSER_CHRG - table Field
     */
    public void setMisserChrg(String misserChrg) {
        this.misserChrg = misserChrg;
    }

    public String getMisserChrg() {
        return misserChrg;
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
     * Setter/Getter for FREEWITHD_CHRG - table Field
     */
    public void setFreewithdChrg(String freewithdChrg) {
        this.freewithdChrg = freewithdChrg;
    }

    public String getFreewithdChrg() {
        return freewithdChrg;
    }

    /**
     * Setter/Getter for DEBIT_INT - table Field
     */
    public void setDebitInt(String debitInt) {
        this.debitInt = debitInt;
    }

    public String getDebitInt() {
        return debitInt;
    }

    /**
     * Setter/Getter for CREDIT_INT - table Field
     */
    public void setCreditInt(String creditInt) {
        this.creditInt = creditInt;
    }

    public String getCreditInt() {
        return creditInt;
    }

    /**
     * Setter/Getter for CLR_INT - table Field
     */
    public void setClrInt(String clrInt) {
        this.clrInt = clrInt;
    }

    public String getClrInt() {
        return clrInt;
    }

    /**
     * Setter/Getter for CHQ_ISSUE_CHRG - table Field
     */
    public void setChqIssueChrg(String chqIssueChrg) {
        this.chqIssueChrg = chqIssueChrg;
    }

    public String getChqIssueChrg() {
        return chqIssueChrg;
    }

    /**
     * Setter/Getter for STOP_PMT_CHRG - table Field
     */
    public void setStopPmtChrg(String stopPmtChrg) {
        this.stopPmtChrg = stopPmtChrg;
    }

    public String getStopPmtChrg() {
        return stopPmtChrg;
    }

    /**
     * Setter/Getter for CHQ_RET_OUT - table Field
     */
    public void setChqRetOut(String chqRetOut) {
        this.chqRetOut = chqRetOut;
    }

    public String getChqRetOut() {
        return chqRetOut;
    }

    /**
     * Setter/Getter for CHQ_RET_IN - table Field
     */
    public void setChqRetIn(String chqRetIn) {
        this.chqRetIn = chqRetIn;
    }

    public String getChqRetIn() {
        return chqRetIn;
    }

    /**
     * Setter/Getter for ACT_OP_CHRG - table Field
     */
    public void setActOpChrg(String actOpChrg) {
        this.actOpChrg = actOpChrg;
    }

    public String getActOpChrg() {
        return actOpChrg;
    }

    /**
     * Setter/Getter for EXCESS_FREE_WITHD - table Field
     */
    public void setExcessFreeWithd(String excessFreeWithd) {
        this.excessFreeWithd = excessFreeWithd;
    }

    public String getExcessFreeWithd() {
        return excessFreeWithd;
    }

    /**
     * Setter/Getter for TAX - table Field
     */
    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTax() {
        return tax;
    }

    /**
     * Setter/Getter for NONMNT_MIN_CHRG - table Field
     */
    public void setNonmntMinChrg(String nonmntMinChrg) {
        this.nonmntMinChrg = nonmntMinChrg;
    }

    public String getNonmntMinChrg() {
        return nonmntMinChrg;
    }

    /**
     * Setter/Getter for INOPT - table Field
     */
    public void setInopt(String inopt) {
        this.inopt = inopt;
    }

    public String getInopt() {
        return inopt;
    }

    /**
     * Setter/Getter for FOLIO_CHRG - table Field
     */
    public void setFolioChrg(String folioChrg) {
        this.folioChrg = folioChrg;
    }

    public String getFolioChrg() {
        return folioChrg;
    }

 	public String getAtmGL() {
        return atmGL;
    }

    public void setAtmGL(String atmGL) {
        this.atmGL = atmGL;
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
        strB.append(getTOString("inopAcChrg", inopAcChrg));
        strB.append(getTOString("prematcloseChrg", prematcloseChrg));
        strB.append(getTOString("accloseChrg", accloseChrg));
        strB.append(getTOString("misserChrg", misserChrg));
        strB.append(getTOString("statChrg", statChrg));
        strB.append(getTOString("freewithdChrg", freewithdChrg));
        strB.append(getTOString("debitInt", debitInt));
        strB.append(getTOString("creditInt", creditInt));
        strB.append(getTOString("clrInt", clrInt));
        strB.append(getTOString("chqIssueChrg", chqIssueChrg));
        strB.append(getTOString("stopPmtChrg", stopPmtChrg));
        strB.append(getTOString("chqRetOut", chqRetOut));
        strB.append(getTOString("chqRetIn", chqRetIn));
        strB.append(getTOString("actOpChrg", actOpChrg));
        strB.append(getTOString("excessFreeWithd", excessFreeWithd));
        strB.append(getTOString("tax", tax));
        strB.append(getTOString("nonmntMinChrg", nonmntMinChrg));
        strB.append(getTOString("inopt", inopt));
        strB.append(getTOString("folioChrg", folioChrg));        
        strB.append(getTOString("debitWithdrawalCharge", debitWithdrawalCharge)); // Added by nithya on 17-03-2016 for 0004021
        strB.append(getTOString("atmGL", atmGL));
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
        strB.append(getTOXml("inopAcChrg", inopAcChrg));
        strB.append(getTOXml("prematcloseChrg", prematcloseChrg));
        strB.append(getTOXml("accloseChrg", accloseChrg));
        strB.append(getTOXml("misserChrg", misserChrg));
        strB.append(getTOXml("statChrg", statChrg));
        strB.append(getTOXml("freewithdChrg", freewithdChrg));
        strB.append(getTOXml("debitInt", debitInt));
        strB.append(getTOXml("creditInt", creditInt));
        strB.append(getTOXml("clrInt", clrInt));
        strB.append(getTOXml("chqIssueChrg", chqIssueChrg));
        strB.append(getTOXml("stopPmtChrg", stopPmtChrg));
        strB.append(getTOXml("chqRetOut", chqRetOut));
        strB.append(getTOXml("chqRetIn", chqRetIn));
        strB.append(getTOXml("actOpChrg", actOpChrg));
        strB.append(getTOXml("excessFreeWithd", excessFreeWithd));
        strB.append(getTOXml("tax", tax));
        strB.append(getTOXml("nonmntMinChrg", nonmntMinChrg));
        strB.append(getTOXml("inopt", inopt));
        strB.append(getTOXml("folioChrg", folioChrg));        
        strB.append(getTOXml("debitWithdrawalCharge", debitWithdrawalCharge)); // Added by nithya on 17-03-2016 for 0004021
        strB.append(getTOXml("atmGL", atmGL));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}