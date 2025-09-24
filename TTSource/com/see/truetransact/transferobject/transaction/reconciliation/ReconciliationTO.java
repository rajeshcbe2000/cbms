/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashTransactionTO.java
 *
 * Created on Thu Aug 12 12:40:27 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.transaction.reconciliation;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CASH_TRANS.
 */
public class ReconciliationTO extends TransferObject implements Serializable {

    private String transId = "";
    private String batchId = "";
    private String acHdId = "";
    private String reconcileTransId = null;
    private String reconcileBatchId = null;
    private Double transAmount = null;
    private Double reconcileAmount = null;
    private Double balanceAmount = null;
    private Date transDt = null;
    private String transType = "";
    private String particulars = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchId = "";
    private String transMode = "";
    private String initiatedBranch = "";
    private String presentBatchId = "";
    private String presentTransId = "";
    private Double presnetAmount = null;
    private Date preTranDt = null;
    private Date recTranDt = null;

    public String getKeyData() {
        setKeyColumns("transId");
        return transId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("batchId", batchId));
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("reconcileTransId", reconcileTransId));
        strB.append(getTOString("reconcileBatchId", reconcileBatchId));
        strB.append(getTOString("transAmount", transAmount));
        strB.append(getTOString("reconcileAmount", reconcileAmount));
        strB.append(getTOString("balanceAmount", balanceAmount));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("branchId", branchId));
        strB.append(getTOString("transMode", transMode));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("presentBatchId", presentBatchId));
        strB.append(getTOString("presentTransId", presentTransId));
        strB.append(getTOString("presnetAmount", presnetAmount));
        strB.append(getTOString("preTranDt", preTranDt));
        strB.append(getTOString("recTranDt", recTranDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("batchId", batchId));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("reconcileTransId", reconcileTransId));
        strB.append(getTOXml("reconcileBatchId", reconcileBatchId));
        strB.append(getTOXml("transAmount", transAmount));
        strB.append(getTOXml("reconcileAmount", reconcileAmount));
        strB.append(getTOXml("balanceAmount", balanceAmount));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("particulars", particulars));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXml("transMode", transMode));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXml("presentBatchId", presentBatchId));
        strB.append(getTOXml("presentTransId", presentTransId));
        strB.append(getTOXml("presnetAmount", presnetAmount));
        strB.append(getTOXml("preTranDt", preTranDt));
        strB.append(getTOXml("recTranDt", recTranDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property transId.
     *
     * @return Value of property transId.
     */
    public java.lang.String getTransId() {
        return transId;
    }

    /**
     * Setter for property transId.
     *
     * @param transId New value of property transId.
     */
    public void setTransId(java.lang.String transId) {
        this.transId = transId;
    }

    /**
     * Getter for property batchId.
     *
     * @return Value of property batchId.
     */
    public java.lang.String getBatchId() {
        return batchId;
    }

    /**
     * Setter for property batchId.
     *
     * @param batchId New value of property batchId.
     */
    public void setBatchId(java.lang.String batchId) {
        this.batchId = batchId;
    }

    /**
     * Getter for property acHdId.
     *
     * @return Value of property acHdId.
     */
    public java.lang.String getAcHdId() {
        return acHdId;
    }

    /**
     * Setter for property acHdId.
     *
     * @param acHdId New value of property acHdId.
     */
    public void setAcHdId(java.lang.String acHdId) {
        this.acHdId = acHdId;
    }

    /**
     * Getter for property reconcileAmount.
     *
     * @return Value of property reconcileAmount.
     */
    public java.lang.Double getReconcileAmount() {
        return reconcileAmount;
    }

    /**
     * Setter for property reconcileAmount.
     *
     * @param reconcileAmount New value of property reconcileAmount.
     */
    public void setReconcileAmount(java.lang.Double reconcileAmount) {
        this.reconcileAmount = reconcileAmount;
    }

    /**
     * Getter for property balanceAmount.
     *
     * @return Value of property balanceAmount.
     */
    public java.lang.Double getBalanceAmount() {
        return balanceAmount;
    }

    /**
     * Setter for property balanceAmount.
     *
     * @param balanceAmount New value of property balanceAmount.
     */
    public void setBalanceAmount(java.lang.Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    /**
     * Getter for property transDt.
     *
     * @return Value of property transDt.
     */
    public java.util.Date getTransDt() {
        return transDt;
    }

    /**
     * Setter for property transDt.
     *
     * @param transDt New value of property transDt.
     */
    public void setTransDt(java.util.Date transDt) {
        this.transDt = transDt;
    }

    /**
     * Getter for property transType.
     *
     * @return Value of property transType.
     */
    public java.lang.String getTransType() {
        return transType;
    }

    /**
     * Setter for property transType.
     *
     * @param transType New value of property transType.
     */
    public void setTransType(java.lang.String transType) {
        this.transType = transType;
    }

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
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
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
     * Getter for property branchId.
     *
     * @return Value of property branchId.
     */
    public java.lang.String getBranchId() {
        return branchId;
    }

    /**
     * Setter for property branchId.
     *
     * @param branchId New value of property branchId.
     */
    public void setBranchId(java.lang.String branchId) {
        this.branchId = branchId;
    }

    /**
     * Getter for property transMode.
     *
     * @return Value of property transMode.
     */
    public java.lang.String getTransMode() {
        return transMode;
    }

    /**
     * Setter for property transMode.
     *
     * @param transMode New value of property transMode.
     */
    public void setTransMode(java.lang.String transMode) {
        this.transMode = transMode;
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

    /**
     * Getter for property reconcileTransId.
     *
     * @return Value of property reconcileTransId.
     */
    public java.lang.String getReconcileTransId() {
        return reconcileTransId;
    }

    /**
     * Setter for property reconcileTransId.
     *
     * @param reconcileTransId New value of property reconcileTransId.
     */
    public void setReconcileTransId(java.lang.String reconcileTransId) {
        this.reconcileTransId = reconcileTransId;
    }

    /**
     * Getter for property reconcileBatchId.
     *
     * @return Value of property reconcileBatchId.
     */
    public java.lang.String getReconcileBatchId() {
        return reconcileBatchId;
    }

    /**
     * Setter for property reconcileBatchId.
     *
     * @param reconcileBatchId New value of property reconcileBatchId.
     */
    public void setReconcileBatchId(java.lang.String reconcileBatchId) {
        this.reconcileBatchId = reconcileBatchId;
    }

    /**
     * Getter for property transAmount.
     *
     * @return Value of property transAmount.
     */
    public java.lang.Double getTransAmount() {
        return transAmount;
    }

    /**
     * Setter for property transAmount.
     *
     * @param transAmount New value of property transAmount.
     */
    public void setTransAmount(java.lang.Double transAmount) {
        this.transAmount = transAmount;
    }

    /**
     * Getter for property presentBatchId.
     *
     * @return Value of property presentBatchId.
     */
    public java.lang.String getPresentBatchId() {
        return presentBatchId;
    }

    /**
     * Setter for property presentBatchId.
     *
     * @param presentBatchId New value of property presentBatchId.
     */
    public void setPresentBatchId(java.lang.String presentBatchId) {
        this.presentBatchId = presentBatchId;
    }

    /**
     * Getter for property presentTransId.
     *
     * @return Value of property presentTransId.
     */
    public java.lang.String getPresentTransId() {
        return presentTransId;
    }

    /**
     * Setter for property presentTransId.
     *
     * @param presentTransId New value of property presentTransId.
     */
    public void setPresentTransId(java.lang.String presentTransId) {
        this.presentTransId = presentTransId;
    }

    /**
     * Getter for property presnetAmount.
     *
     * @return Value of property presnetAmount.
     */
    public java.lang.Double getPresnetAmount() {
        return presnetAmount;
    }

    /**
     * Setter for property presnetAmount.
     *
     * @param presnetAmount New value of property presnetAmount.
     */
    public void setPresnetAmount(java.lang.Double presnetAmount) {
        this.presnetAmount = presnetAmount;
    }

    /**
     * Getter for property preTranDt.
     *
     * @return Value of property preTranDt.
     */
    public java.util.Date getPreTranDt() {
        return preTranDt;
    }

    /**
     * Setter for property preTranDt.
     *
     * @param preTranDt New value of property preTranDt.
     */
    public void setPreTranDt(java.util.Date preTranDt) {
        this.preTranDt = preTranDt;
    }

    /**
     * Getter for property recTranDt.
     *
     * @return Value of property recTranDt.
     */
    public java.util.Date getRecTranDt() {
        return recTranDt;
    }

    /**
     * Setter for property recTranDt.
     *
     * @param recTranDt New value of property recTranDt.
     */
    public void setRecTranDt(java.util.Date recTranDt) {
        this.recTranDt = recTranDt;
    }
}