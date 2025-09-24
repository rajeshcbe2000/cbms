/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TransferInTO.java
 *
 * Created on Tue Apr 13 14:57:43 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_TRANSIN.
 */
public class TransferInTO extends TransferObject implements Serializable {

    private String depositNo = "";
    private String transBranchCode = "";
    private String originalAcNumber = "";
    private Date originalDepositDt = null;
    private Double printedFdr = null;
    private String interbranchTransNo = "";
    private Date transDt = null;
    private Double transAmt = null;
    private Date lastIntcalcDt = null;
    private Date tdsCollectedUpto = null;
    private Double intProvAmt = null;
    private Double lastTdsCollected = null;
    private Double intPaid = null;
    private Double totNoInstall = null;
    private Double totInstallReceived = null;
    private Date lastInstallRecdt = null;
    private Double balanceIntPayable = null;

    /**
     * Setter/Getter for DEPOSIT_NO - table Field
     */
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositNo() {
        return depositNo;
    }

    /**
     * Setter/Getter for TRANS_BRANCH_CODE - table Field
     */
    public void setTransBranchCode(String transBranchCode) {
        this.transBranchCode = transBranchCode;
    }

    public String getTransBranchCode() {
        return transBranchCode;
    }

    /**
     * Setter/Getter for ORIGINAL_AC_NUMBER - table Field
     */
    public void setOriginalAcNumber(String originalAcNumber) {
        this.originalAcNumber = originalAcNumber;
    }

    public String getOriginalAcNumber() {
        return originalAcNumber;
    }

    /**
     * Setter/Getter for ORIGINAL_DEPOSIT_DT - table Field
     */
    public void setOriginalDepositDt(Date originalDepositDt) {
        this.originalDepositDt = originalDepositDt;
    }

    public Date getOriginalDepositDt() {
        return originalDepositDt;
    }

    /**
     * Setter/Getter for PRINTED_FDR - table Field
     */
    public void setPrintedFdr(Double printedFdr) {
        this.printedFdr = printedFdr;
    }

    public Double getPrintedFdr() {
        return printedFdr;
    }

    /**
     * Setter/Getter for INTERBRANCH_TRANS_NO - table Field
     */
    public void setInterbranchTransNo(String interbranchTransNo) {
        this.interbranchTransNo = interbranchTransNo;
    }

    public String getInterbranchTransNo() {
        return interbranchTransNo;
    }

    /**
     * Setter/Getter for TRANS_DT - table Field
     */
    public void setTransDt(Date transDt) {
        this.transDt = transDt;
    }

    public Date getTransDt() {
        return transDt;
    }

    /**
     * Setter/Getter for TRANS_AMT - table Field
     */
    public void setTransAmt(Double transAmt) {
        this.transAmt = transAmt;
    }

    public Double getTransAmt() {
        return transAmt;
    }

    /**
     * Setter/Getter for LAST_INTCALC_DT - table Field
     */
    public void setLastIntcalcDt(Date lastIntcalcDt) {
        this.lastIntcalcDt = lastIntcalcDt;
    }

    public Date getLastIntcalcDt() {
        return lastIntcalcDt;
    }

    /**
     * Setter/Getter for TDS_COLLECTED_UPTO - table Field
     */
    public void setTdsCollectedUpto(Date tdsCollectedUpto) {
        this.tdsCollectedUpto = tdsCollectedUpto;
    }

    public Date getTdsCollectedUpto() {
        return tdsCollectedUpto;
    }

    /**
     * Setter/Getter for INT_PROV_AMT - table Field
     */
    public void setIntProvAmt(Double intProvAmt) {
        this.intProvAmt = intProvAmt;
    }

    public Double getIntProvAmt() {
        return intProvAmt;
    }

    /**
     * Setter/Getter for LAST_TDS_COLLECTED - table Field
     */
    public void setLastTdsCollected(Double lastTdsCollected) {
        this.lastTdsCollected = lastTdsCollected;
    }

    public Double getLastTdsCollected() {
        return lastTdsCollected;
    }

    /**
     * Setter/Getter for INT_PAID - table Field
     */
    public void setIntPaid(Double intPaid) {
        this.intPaid = intPaid;
    }

    public Double getIntPaid() {
        return intPaid;
    }

    /**
     * Setter/Getter for TOT_NO_INSTALL - table Field
     */
    public void setTotNoInstall(Double totNoInstall) {
        this.totNoInstall = totNoInstall;
    }

    public Double getTotNoInstall() {
        return totNoInstall;
    }

    /**
     * Setter/Getter for TOT_INSTALL_RECEIVED - table Field
     */
    public void setTotInstallReceived(Double totInstallReceived) {
        this.totInstallReceived = totInstallReceived;
    }

    public Double getTotInstallReceived() {
        return totInstallReceived;
    }

    /**
     * Setter/Getter for LAST_INSTALL_RECDT - table Field
     */
    public void setLastInstallRecdt(Date lastInstallRecdt) {
        this.lastInstallRecdt = lastInstallRecdt;
    }

    public Date getLastInstallRecdt() {
        return lastInstallRecdt;
    }

    /**
     * Setter/Getter for BALANCE_INT_PAYABLE - table Field
     */
    public void setBalanceIntPayable(Double balanceIntPayable) {
        this.balanceIntPayable = balanceIntPayable;
    }

    public Double getBalanceIntPayable() {
        return balanceIntPayable;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("depositNo");
        return depositNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("transBranchCode", transBranchCode));
        strB.append(getTOString("originalAcNumber", originalAcNumber));
        strB.append(getTOString("originalDepositDt", originalDepositDt));
        strB.append(getTOString("printedFdr", printedFdr));
        strB.append(getTOString("interbranchTransNo", interbranchTransNo));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("transAmt", transAmt));
        strB.append(getTOString("lastIntcalcDt", lastIntcalcDt));
        strB.append(getTOString("tdsCollectedUpto", tdsCollectedUpto));
        strB.append(getTOString("intProvAmt", intProvAmt));
        strB.append(getTOString("lastTdsCollected", lastTdsCollected));
        strB.append(getTOString("intPaid", intPaid));
        strB.append(getTOString("totNoInstall", totNoInstall));
        strB.append(getTOString("totInstallReceived", totInstallReceived));
        strB.append(getTOString("lastInstallRecdt", lastInstallRecdt));
        strB.append(getTOString("balanceIntPayable", balanceIntPayable));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("transBranchCode", transBranchCode));
        strB.append(getTOXml("originalAcNumber", originalAcNumber));
        strB.append(getTOXml("originalDepositDt", originalDepositDt));
        strB.append(getTOXml("printedFdr", printedFdr));
        strB.append(getTOXml("interbranchTransNo", interbranchTransNo));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("transAmt", transAmt));
        strB.append(getTOXml("lastIntcalcDt", lastIntcalcDt));
        strB.append(getTOXml("tdsCollectedUpto", tdsCollectedUpto));
        strB.append(getTOXml("intProvAmt", intProvAmt));
        strB.append(getTOXml("lastTdsCollected", lastTdsCollected));
        strB.append(getTOXml("intPaid", intPaid));
        strB.append(getTOXml("totNoInstall", totNoInstall));
        strB.append(getTOXml("totInstallReceived", totInstallReceived));
        strB.append(getTOXml("lastInstallRecdt", lastInstallRecdt));
        strB.append(getTOXml("balanceIntPayable", balanceIntPayable));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}