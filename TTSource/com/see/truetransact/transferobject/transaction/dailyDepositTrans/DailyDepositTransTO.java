/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DailyDepositTransTO.java
 *
 * Created on December 26, 2007, 5:07 PM
 */
package com.see.truetransact.transferobject.transaction.dailyDepositTrans;

/**
 *
 * @author Bala
 */
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

public class DailyDepositTransTO extends TransferObject implements Serializable {

    private String trans_id = "";
    private String batch_id = "";
    private Date trn_dt = null;
    private Date coll_dt = null;
    private String agent_no = "";
    private String acct_num = "";
    private String trans_mode = "";
    private String trans_type = "";
    private Double amount = null;
    private Double total_bal = null;
    private String particulars = "";
    private String created_by = "";
    private Date created_dt = null;
    private String authorize_by = "";
    private Date authorize_dt = null;
    private String authorize_status = null;
    private String status = "";
    private String initiatedBranch = "";
    private String prod_Type = "";
    private String screenName = "";
    private String consolidated = "";

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
    public String getProd_Type() {
        return prod_Type;
    }

    public void setProd_Type(String prod_Type) {
        this.prod_Type = prod_Type;
    }

    /**
     * Creates a new instance of DailyDepositTransTO
     */
    public DailyDepositTransTO() {
    }

    /**
     * Getter for property trans_id.
     *
     * @return Value of property trans_id.
     */
    public java.lang.String getTrans_id() {
        return trans_id;
    }

    /**
     * Setter for property trans_id.
     *
     * @param trans_id New value of property trans_id.
     */
    public void setTrans_id(java.lang.String trans_id) {
        this.trans_id = trans_id;
    }

    /**
     * Getter for property batch_id.
     *
     * @return Value of property batch_id.
     */
    public java.lang.String getBatch_id() {
        return batch_id;
    }

    /**
     * Setter for property batch_id.
     *
     * @param batch_id New value of property batch_id.
     */
    public void setBatch_id(java.lang.String batch_id) {
        this.batch_id = batch_id;
    }

    /**
     * Getter for property trn_dt.
     *
     * @return Value of property trn_dt.
     */
    public java.util.Date getTrn_dt() {
        return trn_dt;
    }

    /**
     * Setter for property trn_dt.
     *
     * @param trn_dt New value of property trn_dt.
     */
    public void setTrn_dt(java.util.Date trn_dt) {
        this.trn_dt = trn_dt;
    }

    /**
     * Getter for property coll_dt.
     *
     * @return Value of property coll_dt.
     */
    public java.util.Date getColl_dt() {
        return coll_dt;
    }

    /**
     * Setter for property coll_dt.
     *
     * @param coll_dt New value of property coll_dt.
     */
    public void setColl_dt(java.util.Date coll_dt) {
        this.coll_dt = coll_dt;
    }

    /**
     * Getter for property agent_no.
     *
     * @return Value of property agent_no.
     */
    public java.lang.String getAgent_no() {
        return agent_no;
    }

    /**
     * Setter for property agent_no.
     *
     * @param agent_no New value of property agent_no.
     */
    public void setAgent_no(java.lang.String agent_no) {
        this.agent_no = agent_no;
    }

    /**
     * Getter for property acct_num.
     *
     * @return Value of property acct_num.
     */
    public java.lang.String getAcct_num() {
        return acct_num;
    }

    /**
     * Setter for property acct_num.
     *
     * @param acct_num New value of property acct_num.
     */
    public void setAcct_num(java.lang.String acct_num) {
        this.acct_num = acct_num;
    }

    /**
     * Getter for property trans_mode.
     *
     * @return Value of property trans_mode.
     */
    public java.lang.String getTrans_mode() {
        return trans_mode;
    }

    /**
     * Setter for property trans_mode.
     *
     * @param trans_mode New value of property trans_mode.
     */
    public void setTrans_mode(java.lang.String trans_mode) {
        this.trans_mode = trans_mode;
    }

    /**
     * Getter for property trans_type.
     *
     * @return Value of property trans_type.
     */
    public java.lang.String getTrans_type() {
        return trans_type;
    }

    /**
     * Setter for property trans_type.
     *
     * @param trans_type New value of property trans_type.
     */
    public void setTrans_type(java.lang.String trans_type) {
        this.trans_type = trans_type;
    }

    /**
     * Getter for property amount.
     *
     * @return Value of property amount.
     */
//    public double getAmount() {
//        return amount;
//    }
//    
//    /**
//     * Setter for property amount.
//     * @param amount New value of property amount.
//     */
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
//    
//    /**
//     * Getter for property total_bal.
//     * @return Value of property total_bal.
//     */
//    public double getTotal_bal() {
//        return total_bal;
//    }
//    
//    /**
//     * Setter for property total_bal.
//     * @param total_bal New value of property total_bal.
//     */
//    public void setTotal_bal(double total_bal) {
//        this.total_bal = total_bal;
//    }
    /**
     * Getter for property particulars.
     *
     * @return Value of property particulars.
     */
    public java.lang.String getParticulars() {
        return particulars;
    }

    /**
     * Setter for property particulars.
     *
     * @param particulars New value of property particulars.
     */
    public void setParticulars(java.lang.String particulars) {
        this.particulars = particulars;
    }

    /**
     * Getter for property created_by.
     *
     * @return Value of property created_by.
     */
    public java.lang.String getCreated_by() {
        return created_by;
    }

    /**
     * Setter for property created_by.
     *
     * @param created_by New value of property created_by.
     */
    public void setCreated_by(java.lang.String created_by) {
        this.created_by = created_by;
    }

    /**
     * Getter for property created_dt.
     *
     * @return Value of property created_dt.
     */
    public java.util.Date getCreated_dt() {
        return created_dt;
    }

    /**
     * Setter for property created_dt.
     *
     * @param created_dt New value of property created_dt.
     */
    public void setCreated_dt(java.util.Date created_dt) {
        this.created_dt = created_dt;
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
     * Getter for property authorize_dt.
     *
     * @return Value of property authorize_dt.
     */
    public java.util.Date getAuthorize_dt() {
        return authorize_dt;
    }

    /**
     * Setter for property authorize_dt.
     *
     * @param authorize_dt New value of property authorize_dt.
     */
    public void setAuthorize_dt(java.util.Date authorize_dt) {
        this.authorize_dt = authorize_dt;
    }

    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("trans_id", trans_id));
        strB.append(getTOString("batch_id", batch_id));
        strB.append(getTOString("trn_dt", trn_dt));
        strB.append(getTOString("coll_dt", coll_dt));
        strB.append(getTOString("agent_no", agent_no));
        strB.append(getTOString("acct_num", acct_num));
        strB.append(getTOString("trans_mode", trans_mode));
        strB.append(getTOString("trans_type", trans_type));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("total_bal", total_bal));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("created_by", created_by));
        strB.append(getTOString("created_dt", created_dt));
        strB.append(getTOString("authorize_by", authorize_by));
        strB.append(getTOString("authorize_dt", authorize_dt));
        strB.append(getTOString("authorize_status", authorize_status));
        strB.append(getTOString("status", status));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("prod_Type", prod_Type));
        strB.append(getTOString("screenName", screenName));
        strB.append(getTOString("consolidated", consolidated));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOString("trans_id", trans_id));
        strB.append(getTOXml("batch_id", batch_id));
        strB.append(getTOXml("trn_dt", trn_dt));
        strB.append(getTOXml("coll_dt", coll_dt));
        strB.append(getTOXml("agent_no", agent_no));
        strB.append(getTOXml("acct_num", acct_num));
        strB.append(getTOXml("trans_mode", trans_mode));
        strB.append(getTOXml("trans_type", trans_type));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("total_bal", total_bal));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("created_by", created_by));
        strB.append(getTOXml("created_dt", created_dt));
        strB.append(getTOXml("authorize_by", authorize_by));
        strB.append(getTOXml("authorize_dt", authorize_dt));
        strB.append(getTOXml("authorize_status", authorize_status));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXml("prod_Type", prod_Type));
        strB.append(getTOXml("screenName", screenName));
        strB.append(getTOXml("consolidated", consolidated));
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property total_bal.
     *
     * @return Value of property total_bal.
     */
    public java.lang.Double getTotal_bal() {
        return total_bal;
    }

    /**
     * Setter for property total_bal.
     *
     * @param total_bal New value of property total_bal.
     */
    public void setTotal_bal(java.lang.Double total_bal) {
        this.total_bal = total_bal;
    }

    /**
     * Getter for property authorize_status.
     *
     * @return Value of property authorize_status.
     */
    public java.lang.String getAuthorize_status() {
        return authorize_status;
    }

    /**
     * Setter for property authorize_status.
     *
     * @param authorize_status New value of property authorize_status.
     */
    public void setAuthorize_status(java.lang.String authorize_status) {
        this.authorize_status = authorize_status;
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
     * Getter for property initiatedBranch.
     *
     * @return Value of property initiatedBranch.
     */
    public java.lang.String getInitiatedBranch() {
        return initiatedBranch;
    }

    /**
     * Setter for property initiatedBranch.
     *
     * @param initiatedBranch New value of property initiatedBranch.
     */
    public void setInitiatedBranch(java.lang.String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }

    public String getConsolidated() {
        return consolidated;
    }

    public void setConsolidated(String consolidated) {
        this.consolidated = consolidated;
    }

    
    
}
