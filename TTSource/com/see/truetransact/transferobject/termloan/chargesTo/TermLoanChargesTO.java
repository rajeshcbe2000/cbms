/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanFacilityTO.java
 *
 * Created on Wed Apr 13 17:21:29 IST 2005
 */
package com.see.truetransact.transferobject.termloan.chargesTo;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_FACILITY_DETAILS.
 */
public class TermLoanChargesTO extends TransferObject implements Serializable {

    private String prod_Type = "";
    private String prod_Id = "";
    private String act_num = "";
    private Date chargeDt = null;
    private String charge_Type = "";
    private Double amount = null;
    private Double paidAmount = null;
    private String status = "";
    private String status_By = "";
    private Date status_Dt = null;
    private String authorize_Status = null;
    private String authorize_by = "";
    private Date authorize_Dt = null;
    private Long chargeGenerateNo = null;
    private String screenName = null;
    private String branchId = "";
    private String batchID = "";
    private String narration = "";
    

    public String getKeyData() {
        setKeyColumns("act_num" + KEY_VAL_SEPARATOR + "charge_Type");//+KEY_VAL_SEPARATOR+"slNo");
        return act_num + KEY_VAL_SEPARATOR + charge_Type;//+KEY_VAL_SEPARATOR+slNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("act_num", act_num));
        strB.append(getTOString("prod_Type", prod_Type));
        strB.append(getTOString("prod_Id", prod_Id));
        strB.append(getTOString("chargeDt", chargeDt));
        strB.append(getTOString("charge_Type", charge_Type));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("paidAmount", paidAmount));
        strB.append(getTOString("status", status));
        strB.append(getTOString("status_By", status_By));
        strB.append(getTOString("status_Dt", status_Dt));
        strB.append(getTOString("authorize_Status", authorize_Status));
        strB.append(getTOString("authorize_by", authorize_by));
        strB.append(getTOString("authorize_Dt", authorize_Dt));
        strB.append(getTOString("chargeGenerateNo", chargeGenerateNo));
        strB.append(getTOString("screenName", screenName));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("batchID", batchID));
        strB.append(getTOString("narration", narration));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("act_num", act_num));
        strB.append(getTOXml("prod_Type", prod_Type));
        strB.append(getTOXml("prod_Id", prod_Id));
        strB.append(getTOXml("charge_Type", charge_Type));
        strB.append(getTOXml("chargeDt", chargeDt));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("paidAmount", paidAmount));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("status_By", status_By));
        strB.append(getTOXml("status_Dt", status_Dt));
        strB.append(getTOXml("authorize_Status", authorize_Status));
        strB.append(getTOXml("authorize_by", authorize_by));
        strB.append(getTOXml("authorize_Dt", authorize_Dt));
        strB.append(getTOXml("chargeGenerateNo", chargeGenerateNo));
        strB.append(getTOXml("screenName", screenName));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("batchID", batchID));
        strB.append(getTOXml("narration", narration));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    /**
     * Getter for property prod_Type.
     *
     * @return Value of property prod_Type.
     */
    public java.lang.String getProd_Type() {
        return prod_Type;
    }

    /**
     * Setter for property prod_Type.
     *
     * @param prod_Type New value of property prod_Type.
     */
    public void setProd_Type(java.lang.String prod_Type) {
        this.prod_Type = prod_Type;
    }

    /**
     * Getter for property prod_Id.
     *
     * @return Value of property prod_Id.
     */
    public java.lang.String getProd_Id() {
        return prod_Id;
    }

    /**
     * Setter for property prod_Id.
     *
     * @param prod_Id New value of property prod_Id.
     */
    public void setProd_Id(java.lang.String prod_Id) {
        this.prod_Id = prod_Id;
    }

    /**
     * Getter for property act_num.
     *
     * @return Value of property act_num.
     */
    public java.lang.String getAct_num() {
        return act_num;
    }

    /**
     * Setter for property act_num.
     *
     * @param act_num New value of property act_num.
     */
    public void setAct_num(java.lang.String act_num) {
        this.act_num = act_num;
    }

    /**
     * Getter for property chargeDt.
     *
     * @return Value of property chargeDt.
     */
    public java.util.Date getChargeDt() {
        return chargeDt;
    }

    /**
     * Setter for property chargeDt.
     *
     * @param chargeDt New value of property chargeDt.
     */
    public void setChargeDt(java.util.Date chargeDt) {
        this.chargeDt = chargeDt;
    }

    /**
     * Getter for property charge_Type.
     *
     * @return Value of property charge_Type.
     */
    public java.lang.String getCharge_Type() {
        return charge_Type;
    }

    /**
     * Setter for property charge_Type.
     *
     * @param charge_Type New value of property charge_Type.
     */
    public void setCharge_Type(java.lang.String charge_Type) {
        this.charge_Type = charge_Type;
    }

    /**
     * Getter for property amount.
     *
     * @return Value of property amount.
     */
    public java.lang.Double getAmount() {
        return amount;
    }

    /**
     * Setter for property amount.
     *
     * @param amount New value of property amount.
     */
    public void setAmount(java.lang.Double amount) {
        this.amount = amount;
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
     * Getter for property status_By.
     *
     * @return Value of property status_By.
     */
    public java.lang.String getStatus_By() {
        return status_By;
    }

    /**
     * Setter for property status_By.
     *
     * @param status_By New value of property status_By.
     */
    public void setStatus_By(java.lang.String status_By) {
        this.status_By = status_By;
    }

    /**
     * Getter for property status_Dt.
     *
     * @return Value of property status_Dt.
     */
    public java.util.Date getStatus_Dt() {
        return status_Dt;
    }

    /**
     * Setter for property status_Dt.
     *
     * @param status_Dt New value of property status_Dt.
     */
    public void setStatus_Dt(java.util.Date status_Dt) {
        this.status_Dt = status_Dt;
    }

    /**
     * Getter for property authorize_Status.
     *
     * @return Value of property authorize_Status.
     */
    public java.lang.String getAuthorize_Status() {
        return authorize_Status;
    }

    /**
     * Setter for property authorize_Status.
     *
     * @param authorize_Status New value of property authorize_Status.
     */
    public void setAuthorize_Status(java.lang.String authorize_Status) {
        this.authorize_Status = authorize_Status;
    }

    /**
     * Getter for property authorize_by.
     *
     * @return Value of property authorize_by.
     */
    public java.lang.String getAuthorize_by() {
        return authorize_by;
    }

    /**
     * Setter for property authorize_by.
     *
     * @param authorize_by New value of property authorize_by.
     */
    public void setAuthorize_by(java.lang.String authorize_by) {
        this.authorize_by = authorize_by;
    }

    /**
     * Getter for property authorize_Dt.
     *
     * @return Value of property authorize_Dt.
     */
    public java.util.Date getAuthorize_Dt() {
        return authorize_Dt;
    }

    /**
     * Setter for property authorize_Dt.
     *
     * @param authorize_Dt New value of property authorize_Dt.
     */
    public void setAuthorize_Dt(java.util.Date authorize_Dt) {
        this.authorize_Dt = authorize_Dt;
    }

    /**
     * Getter for property chargeGenerateNo.
     *
     * @return Value of property chargeGenerateNo.
     */
    public java.lang.Long getChargeGenerateNo() {
        return chargeGenerateNo;
    }

    /**
     * Setter for property chargeGenerateNo.
     *
     * @param chargeGenerateNo New value of property chargeGenerateNo.
     */
    public void setChargeGenerateNo(java.lang.Long chargeGenerateNo) {
        this.chargeGenerateNo = chargeGenerateNo;
    }

    /**
     * Getter for property paidAmount.
     *
     * @return Value of property paidAmount.
     */
    public java.lang.Double getPaidAmount() {
        return paidAmount;
    }

    /**
     * Setter for property paidAmount.
     *
     * @param paidAmount New value of property paidAmount.
     */
    public void setPaidAmount(java.lang.Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBatchID() {
        return batchID;
    }

    public void setBatchID(String batchID) {
        this.batchID = batchID;
    }
    
    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }
   
}