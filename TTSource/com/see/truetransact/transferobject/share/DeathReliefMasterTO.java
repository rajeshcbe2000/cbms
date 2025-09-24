/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathReliefMasterTO.java
 * 
 * Created on Fri Aug 05 15:08:19 GMT+05:30 2011
 */
package com.see.truetransact.transferobject.share;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Table name for this TO is DRF_PRODUCT.
 */
public class DeathReliefMasterTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String prodName = "";
    private String drfAchd = "";
    private String drfPaymentAchd = "";
    private String status = "";
    private Date statusDate = null;
    private String statusBy = "";
    private String authorizeBy = "";
    private String authorizeStatus = null;;
    private Date authorizeDate = null;
    private Date tdtDrfFromDt = null;
    private Date tdtDrfToDt = null;
    private String txtDrfAmount = "";
    private String txtPaymentAmount = "";
    private String drfSlNo = "";
    private String recoveryHead = "";
    private String rdAmountRecovery = "";
    private String recoveryAmount = "";
    private String calculationFrequency = "";
    private String calculationCriteria = "";
    private String productFrequency = "";
    private String debitHead = "";
    private Date lastCalculatedDate = null;
    private Date tdtFromDt = null;
    private Date toDt = null;
    private String interestRate = "";
    private String drfInterestID = "";
    private List bufferTO = new ArrayList();
    //Added By Revathi.L
    private String nominee = "";
    private Integer noOfNominee = new Integer(0);

    public void setDrfInterestID(String drfInterestID) {
        this.drfInterestID = drfInterestID;
    }

    public String getDrfInterestID() {
        return drfInterestID;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getInterestRate() {
        return interestRate;
    }

    // ArrayList<String[]> a=new ArrayList<String[]>();
    public List getBufferTO() {
        return bufferTO;
    }

    public void setBufferTO(List buffer) {
        this.bufferTO = buffer;
    }

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
     * Setter/Getter for PROD_NAME - table Field
     */
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdName() {
        return prodName;
    }

    /**
     * Setter/Getter for DRF_ACHD - table Field
     */
    public void setDrfAchd(String drfAchd) {
        this.drfAchd = drfAchd;
    }

    public String getDrfAchd() {
        return drfAchd;
    }

    /**
     * Setter/Getter for DRF_PAYMENT_ACHD - table Field
     */
    public void setDrfPaymentAchd(String drfPaymentAchd) {
        this.drfPaymentAchd = drfPaymentAchd;
    }

    public String getDrfPaymentAchd() {
        return drfPaymentAchd;
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
     * Setter/Getter for STATUS_DATE - table Field
     */
    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public Date getStatusDate() {
        return statusDate;
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
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
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
     * Setter/Getter for AUTHORIZE_DATE - table Field
     */
    public void setAuthorizeDate(Date authorizeDate) {
        this.authorizeDate = authorizeDate;
    }

    public Date getAuthorizeDate() {
        return authorizeDate;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("prodName", prodName));
        strB.append(getTOString("drfAchd", drfAchd));
        strB.append(getTOString("drfPaymentAchd", drfPaymentAchd));
        strB.append(getTOString("calculationCriteria", calculationCriteria));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDate", authorizeDate));
        strB.append(getTOString("tdtDrfFromDt", tdtDrfFromDt));
        strB.append(getTOString("txtDrfAmount", txtDrfAmount));
        strB.append(getTOString("txtPaymentAmount", txtPaymentAmount));
        strB.append(getTOString("tdtDrfToDt", tdtDrfToDt));
        strB.append(getTOString("drfSlNo", drfSlNo));
        strB.append(getTOString("nominee", nominee));
        strB.append(getTOString("noOfNominee", noOfNominee));
        strB.append(getTOString("calculationFrequency", calculationFrequency));
        strB.append(getTOString("productFrequency", productFrequency));
        strB.append(getTOString("debitHead", debitHead));
        strB.append(getTOString("lastCalculatedDate", lastCalculatedDate));
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
        strB.append(getTOXml("prodName", prodName));
        strB.append(getTOXml("drfAchd", drfAchd));
        strB.append(getTOXml("drfPaymentAchd", drfPaymentAchd));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeDate", authorizeDate));
        strB.append(getTOXml("tdtDrfFromDt", tdtDrfFromDt));
        strB.append(getTOXml("txtDrfAmount", txtDrfAmount));
        strB.append(getTOXml("txtPaymentAmount", txtPaymentAmount));
        strB.append(getTOXml("tdtDrfToDt", tdtDrfToDt));
        strB.append(getTOXml("drfSlNo", drfSlNo));
        strB.append(getTOXml("nominee", nominee));
        strB.append(getTOXml("noOfNominee", noOfNominee));
        strB.append(getTOXml("calculationCriteria", calculationCriteria));
        strB.append(getTOXml("calculationFrequency", calculationFrequency));
        strB.append(getTOXml("productFrequency", productFrequency));
        strB.append(getTOXml("debitHead", debitHead));
        strB.append(getTOXml("lastCalculatedDate", lastCalculatedDate));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property tdtDrfFromDt.
     *
     * @return Value of property tdtDrfFromDt.
     */
    public java.util.Date getTdtDrfFromDt() {
        return tdtDrfFromDt;
    }

    /**
     * Setter for property tdtDrfFromDt.
     *
     * @param tdtDrfFromDt New value of property tdtDrfFromDt.
     */
    public void setTdtDrfFromDt(java.util.Date tdtDrfFromDt) {
        this.tdtDrfFromDt = tdtDrfFromDt;
    }

    /**
     * Getter for property txtDrfAmount.
     *
     * @return Value of property txtDrfAmount.
     */
    public java.lang.String getTxtDrfAmount() {
        return txtDrfAmount;
    }

    /**
     * Setter for property txtDrfAmount.
     *
     * @param txtDrfAmount New value of property txtDrfAmount.
     */
    public void setTxtDrfAmount(java.lang.String txtDrfAmount) {
        this.txtDrfAmount = txtDrfAmount;
    }

    /**
     * Getter for property txtPaymentAmount.
     *
     * @return Value of property txtPaymentAmount.
     */
    public java.lang.String getTxtPaymentAmount() {
        return txtPaymentAmount;
    }

    /**
     * Setter for property txtPaymentAmount.
     *
     * @param txtPaymentAmount New value of property txtPaymentAmount.
     */
    public void setTxtPaymentAmount(java.lang.String txtPaymentAmount) {
        this.txtPaymentAmount = txtPaymentAmount;
    }

    /**
     * Getter for property drfSlNo.
     *
     * @return Value of property drfSlNo.
     */
    public java.lang.String getDrfSlNo() {
        return drfSlNo;
    }

    /**
     * Setter for property drfSlNo.
     *
     * @param drfSlNo New value of property drfSlNo.
     */
    public void setDrfSlNo(java.lang.String drfSlNo) {
        this.drfSlNo = drfSlNo;
    }

    /**
     * Getter for property tdtDrfToDt.
     *
     * @return Value of property tdtDrfToDt.
     */
    public java.util.Date getTdtDrfToDt() {
        return tdtDrfToDt;
    }

    /**
     * Setter for property tdtDrfToDt.
     *
     * @param tdtDrfToDt New value of property tdtDrfToDt.
     */
    public void setTdtDrfToDt(java.util.Date tdtDrfToDt) {
        this.tdtDrfToDt = tdtDrfToDt;
    }

    /**
     * Getter for property recoveryHead.
     *
     * @return Value of property recoveryHead.
     */
    public java.lang.String getRecoveryHead() {
        return recoveryHead;
    }

    /**
     * Setter for property recoveryHead.
     *
     * @param recoveryHead New value of property recoveryHead.
     */
    public void setRecoveryHead(java.lang.String recoveryHead) {
        this.recoveryHead = recoveryHead;
    }

    /**
     * Getter for property rdAmountRecovery.
     *
     * @return Value of property rdAmountRecovery.
     */
    public java.lang.String getRdAmountRecovery() {
        return rdAmountRecovery;
    }

    /**
     * Setter for property rdAmountRecovery.
     *
     * @param rdAmountRecovery New value of property rdAmountRecovery.
     */
    public void setRdAmountRecovery(java.lang.String rdAmountRecovery) {
        this.rdAmountRecovery = rdAmountRecovery;
    }

    /**
     * Getter for property tRecoveryAmount.
     *
     * @return Value of property tRecoveryAmount.
     */
    public java.lang.String getRecoveryAmount() {
        return recoveryAmount;
    }

    /**
     * Setter for property tRecoveryAmount.
     *
     * @param tRecoveryAmount New value of property tRecoveryAmount.
     */
    public void setRecoveryAmount(java.lang.String recoveryAmount) {
        this.recoveryAmount = recoveryAmount;
    }

    /**
     * Getter for property toDt.
     *
     * @return Value of property toDt.
     */
    public java.util.Date getToDt() {
        return toDt;
    }

    /**
     * Setter for property toDt.
     *
     * @param toDt New value of property toDt.
     */
    public void setToDt(java.util.Date toDt) {
        this.toDt = toDt;
    }

    /**
     * Getter for property calculationFrequency.
     *
     * @return Value of property calculationFrequency.
     */
    public java.lang.String getCalculationFrequency() {
        return calculationFrequency;
    }

    /**
     * Setter for property calculationFrequency.
     *
     * @param calculationFrequency New value of property calculationFrequency.
     */
    public void setCalculationFrequency(java.lang.String calculationFrequency) {
        this.calculationFrequency = calculationFrequency;
    }

    /**
     * Getter for property calculationCriteria.
     *
     * @return Value of property calculationCriteria.
     */
    public java.lang.String getCalculationCriteria() {
        return calculationCriteria;
    }

    /**
     * Setter for property calculationCriteria.
     *
     * @param calculationCriteria New value of property calculationCriteria.
     */
    public void setCalculationCriteria(java.lang.String calculationCriteria) {
        this.calculationCriteria = calculationCriteria;
    }

    /**
     * Getter for property productFrequency.
     *
     * @return Value of property productFrequency.
     */
    public java.lang.String getProductFrequency() {
        return productFrequency;
    }

    /**
     * Setter for property productFrequency.
     *
     * @param productFrequency New value of property productFrequency.
     */
    public void setProductFrequency(java.lang.String productFrequency) {
        this.productFrequency = productFrequency;
    }

    /**
     * Getter for property debitHead.
     *
     * @return Value of property debitHead.
     */
    public java.lang.String getDebitHead() {
        return debitHead;
    }

    /**
     * Setter for property debitHead.
     *
     * @param debitHead New value of property debitHead.
     */
    public void setDebitHead(java.lang.String debitHead) {
        this.debitHead = debitHead;
    }

    /**
     * Getter for property tdtFromDt.
     *
     * @return Value of property tdtFromDt.
     */
    public java.util.Date getTdtFromDt() {
        return tdtFromDt;
    }

    /**
     * Setter for property tdtFromDt.
     *
     * @param tdtFromDt New value of property tdtFromDt.
     */
    public void setTdtFromDt(java.util.Date tdtFromDt) {
        this.tdtFromDt = tdtFromDt;
    }

    /**
     * Getter for property lastCalculatedDate.
     *
     * @return Value of property lastCalculatedDate.
     */
    public java.util.Date getLastCalculatedDate() {
        return lastCalculatedDate;
    }

    /**
     * Setter for property lastCalculatedDate.
     *
     * @param lastCalculatedDate New value of property lastCalculatedDate.
     */
    public void setLastCalculatedDate(java.util.Date lastCalculatedDate) {
        this.lastCalculatedDate = lastCalculatedDate;
    }

    public String getNominee() {
        return nominee;
    }

    public void setNominee(String nominee) {
        this.nominee = nominee;
    }

    public Integer getNoOfNominee() {
        return noOfNominee;
    }

    public void setNoOfNominee(Integer noOfNominee) {
        this.noOfNominee = noOfNominee;
    }

   
    
    
    /**
     * Getter for property incrementRate.
     *
     * @return Value of property incrementRate.
     */
//        public java.lang.String getIncrementRate() {
//            return incrementRate;
//        }
//        
//        /**
//         * Setter for property incrementRate.
//         * @param incrementRate New value of property incrementRate.
//         */
//        public void setIncrementRate(java.lang.String incrementRate) {
//            this.incrementRate = incrementRate;
//        }
}