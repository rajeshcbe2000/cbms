/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigTO.java
 *
 * Created on Thu Jan 20 16:44:08 IST 2005
 */
package com.see.truetransact.transferobject.locker.lockersurrender;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class LockerSurrenderTO extends TransferObject implements Serializable {
    //	private String optId = "";

    private String locNum = "";
    //	private Date optDt = null;
    private String custId = "";
    private String branchID = "";
    private String optMode = "";
    private String createdBy = "";
    private String breakOpenRemarks = "";
    //        private String lockerOutBy = "";
    //        private Date lockerOutDt = null;
    private String authorizeBy = "";
    private String authorizeStatus = null;;
    private Date authorizeDt = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private Date breakOpenDt = null;
    private String acctName = "";
    private Double charges = null;
    private Double txtRefund = null;
    private Date surDt = null;
    private String prodId = "";
    private String remarks = "";
    private String surRenew = "";
    private Double serviceTax = null;
    private Date lblNewExpDateVal = null;
    // private Double serviceTax = null;
    private String collectRentMM = "";
    private String collectRentYYYY = "";
    private String callingApplicantName = "";
    private String cbRefundYes = "";
    private String cbCustomer = "";
    private Double penalAmount = 0.0;
    private String actNum = "";
    private String chkDefaluterYes = "";
    private String chkNoTrans = "";
    private String  bforeExpDt ="";

    public String getBforeExpDt() {
        return bforeExpDt;
    }

    public void setBforeExpDt(String bforeExpDt) {
        this.bforeExpDt = bforeExpDt;
    }
    
    public Double getPenalAmount() {
        return penalAmount;
    }

    public void setPenalAmount(Double penalAmount) {
        this.penalAmount = penalAmount;
    }

    /**
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(locNum);
        return locNum;
    }

    public void setCollectRentMM(java.lang.String collectRentMM) {
        this.collectRentMM = collectRentMM;
    }

    public String getCollectRentMM() {
        return collectRentMM;
    }

    public void setCollectRentYYYY(java.lang.String collectRentYYYY) {
        this.collectRentYYYY = collectRentYYYY;

    }

    public String getcollectRentYYYY() {

        return collectRentYYYY;

    }

    public void setBreakOpenRemarks(java.lang.String breakOpenRemarks) {
        this.breakOpenRemarks = breakOpenRemarks;

    }

    public String getBreakOpenRemarks() {

        return breakOpenRemarks;

    }

    public Date getLblNewExpDateVal() {
        return lblNewExpDateVal;
    }

    public void setLblNewExpDateVal(Date lblNewExpDateVal) {
        this.lblNewExpDateVal = lblNewExpDateVal;

    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        //		strB.append(getTOString("optId", optId));
        strB.append(getTOString("locNum", locNum));
        strB.append(getTOString("surDt", surDt));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("branchID", branchID));
        strB.append(getTOString("optMode", optMode));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("charges", charges));
        strB.append(getTOString("txtRefund", txtRefund));
        //                strB.append(getTOString("lockerOutDt", lockerOutDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("acctName", acctName));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("serviceTax", serviceTax));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("collectRentYYYY", collectRentYYYY));
        strB.append(getTOString("collectRentMM", collectRentMM));
        strB.append(getTOString("callingApplicantName", callingApplicantName));
        strB.append(getTOString("lblNewExpDateVal", lblNewExpDateVal));
        strB.append(getTOString("breakOpenRemarks", breakOpenRemarks));
        strB.append(getTOString("breakOpenDt", breakOpenDt));

        strB.append(getTOString("cbCustomer", cbCustomer));
        strB.append(getTOString("penalAmount", penalAmount));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("chkDefaluterYes", chkDefaluterYes));       
        strB.append(getTOString("bforeExpDt", bforeExpDt));       
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        //		strB.append(getTOXml("optId", optId));
        strB.append(getTOXml("locNum", locNum));
        strB.append(getTOXml("surDt", surDt));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("branchID", branchID));
        strB.append(getTOXml("optMode", optMode));
        strB.append(getTOXml("charges", charges));
        //                strB.append(getTOXml("lockerOutDt", lockerOutDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("acctName", acctName));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("serviceTax", serviceTax));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("collectRentMM", collectRentMM));
        strB.append(getTOXml("collectRentYYYY", collectRentYYYY));
        strB.append(getTOXml("callingApplicantName", callingApplicantName));
        strB.append(getTOXml("lblNewExpDateVal", lblNewExpDateVal));
        strB.append(getTOXml("breakOpenDt", breakOpenDt));
        strB.append(getTOXml("txtRefund", txtRefund));
        strB.append(getTOXml("breakOpenRemarks", breakOpenRemarks));
        strB.append(getTOXml("cbCustomer", cbCustomer));
        strB.append(getTOXml("penalAmount", penalAmount));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("chkDefaluterYes", chkDefaluterYes));
        strB.append(getTOXml("bforeExpDt", bforeExpDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property optId.
     *
     * @return Value of property optId.
     */
    //        public java.lang.String getOptId() {
    //            return optId;
    //        }
    /**
     * Setter for property optId.
     *
     * @param optId New value of property optId.
     */
    //        public void setOptId(java.lang.String optId) {
    //            this.optId = optId;
    //        }
    /**
     * Getter for property locNum.
     *
     * @return Value of property locNum.
     */
    public java.lang.String getLocNum() {
        return locNum;
    }

    /**
     * Setter for property locNum.
     *
     * @param locNum New value of property locNum.
     */
    public void setLocNum(java.lang.String locNum) {
        this.locNum = locNum;
    }

    /**
     * Getter for property optDt.
     *
     * @return Value of property optDt.
     */
    //        public Date getOptDt() {
    //            return optDt;
    //        }
    /**
     * Setter for property optDt.
     *
     * @param optDt New value of property optDt.
     */
    //        public void setOptDt(Date optDt) {
    //            this.optDt = optDt;
    //        }
    /**
     * Getter for property custId.
     *
     * @return Value of property custId.
     */
    public java.lang.String getCustId() {
        return custId;
    }

    /**
     * Setter for property custId.
     *
     * @param custId New value of property custId.
     */
    public void setCustId(java.lang.String custId) {
        this.custId = custId;
    }

    /**
     * Getter for property branchID.
     *
     * @return Value of property branchID.
     */
    public java.lang.String getBranchID() {
        return branchID;
    }

    /**
     * Setter for property branchID.
     *
     * @param branchID New value of property branchID.
     */
    public void setBranchID(java.lang.String branchID) {
        this.branchID = branchID;
    }

    /**
     * Getter for property optMode.
     *
     * @return Value of property optMode.
     */
    public java.lang.String getOptMode() {
        return optMode;
    }

    /**
     * Setter for property optMode.
     *
     * @param optMode New value of property optMode.
     */
    public void setOptMode(java.lang.String optMode) {
        this.optMode = optMode;
    }

    /**
     * Getter for property lockerOutBy.
     *
     * @return Value of property lockerOutBy.
     */
    //        public java.lang.String getLockerOutBy() {
    //            return lockerOutBy;
    //        }
    //
    //        /**
    //         * Setter for property lockerOutBy.
    //         * @param lockerOutBy New value of property lockerOutBy.
    //         */
    //        public void setLockerOutBy(java.lang.String lockerOutBy) {
    //            this.lockerOutBy = lockerOutBy;
    //        }
    //
    //          public Date getLockerOutDt() {
    //            return lockerOutDt;
    //        }
    //
    //        /**
    //         * Setter for property optDt.
    //         * @param optDt New value of property optDt.
    //         */
    //        public void setLockerOutDt(Date lockerOutDt) {
    //            this.lockerOutDt = lockerOutDt;
    //        }
    /**
     * Getter for property lockerOutDt.
     *
     * @return Value of property lockerOutDt.
     */
    //        public java.util.Date getLockerOutDt() {
    //            return lockerOutDt;
    //        }
    //
    //        /**
    //         * Setter for property lockerOutDt.
    //         * @param lockerOutDt New value of property lockerOutDt.
    //         */
    //        public void setLockerOutDt(java.util.Date lockerOutDt) {
    //            this.lockerOutDt = lockerOutDt;
    //        }
    /**
     * Getter for property acctName.
     *
     * @return Value of property acctName.
     */
    public java.lang.String getAcctName() {
        return acctName;
    }

    /**
     * Setter for property acctName.
     *
     * @param acctName New value of property acctName.
     */
    public void setAcctName(java.lang.String acctName) {
        this.acctName = acctName;
    }

    public String getCbCustomer() {
        return cbCustomer;
    }

    /**
     * Setter for property acctName.
     *
     * @param acctName New value of property acctName.
     */
    public void setCbCustomer(String cbCustomer) {
        this.cbCustomer = cbCustomer;
    }

    /**
     * Getter for property charges.
     *
     * @return Value of property charges.
     */
    public java.lang.Double getCharges() {
        return charges;
    }

    /**
     * Setter for property charges.
     *
     * @param charges New value of property charges.
     */
    public void setCharges(java.lang.Double charges) {
        this.charges = charges;
    }

    public java.lang.Double getTxtRefund() {
        return txtRefund;
    }

    /**
     * Setter for property charges.
     *
     * @param charges New value of property charges.
     */
    public void setTxtRefund(java.lang.Double txtRefund) {
        this.txtRefund = txtRefund;
    }

    /**
     * Getter for property surDt.
     *
     * @return Value of property surDt.
     */
    public java.util.Date getSurDt() {
        return surDt;
    }

    /**
     * Setter for property surDt.
     *
     * @param surDt New value of property surDt.
     */
    public void setSurDt(java.util.Date surDt) {
        this.surDt = surDt;
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
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }

    /**
     * Setter for property remarks.
     *
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }

    public Date getBreakOpenDt() {
        return breakOpenDt;
    }

    /**
     * Setter for property remarks.
     *
     * @param remarks New value of property remarks.
     */
    public void setBreakOpenDt(Date breakOpenDt) {
        this.breakOpenDt = breakOpenDt;
    }

    /**
     * Getter for property surRenew.
     *
     * @return Value of property surRenew.
     */
    public java.lang.String getSurRenew() {
        return surRenew;
    }

    /**
     * Setter for property surRenew.
     *
     * @param surRenew New value of property surRenew.
     */
    public void setSurRenew(java.lang.String surRenew) {
        this.surRenew = surRenew;
    }

    /**
     * Getter for property serviceTax.
     *
     * @return Value of property serviceTax.
     */
    public java.lang.Double getServiceTax() {
        return serviceTax;
    }

    /**
     * Setter for property serviceTax.
     *
     * @param serviceTax New value of property serviceTax.
     */
    public void setServiceTax(java.lang.Double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public String getActNum() {
        return actNum;
    }

    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getChkDefaluterYes() {
        return chkDefaluterYes;
    }

    public void setChkDefaluterYes(String chkDefaluterYes) {
        this.chkDefaluterYes = chkDefaluterYes;
    }

    public String getChkNoTrans() {
        return chkNoTrans;
    }

    public void setChkNoTrans(String chkNoTrans) {
        this.chkNoTrans = chkNoTrans;
    }
    
}