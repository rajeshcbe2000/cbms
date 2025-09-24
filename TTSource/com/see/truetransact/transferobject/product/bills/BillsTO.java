/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BillsTO.java
 * 
 * Created on Mon Feb 07 17:35:51 IST 2005
 */
package com.see.truetransact.transferobject.product.bills;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BILLS_PRODUCT.
 */
public class BillsTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String glAcHd = "";
    private String intAcHd = "";
    private String chrgAcHd = "";
    private String contraAcHdYn = "";
    private String ddAcHd = "";
    private Double transPeriod = null;
    private Double noOfIntDays = null;
    private String postDtChqYn = "";
    private String marginAcHd = "";
    private String commAcHd = "";
    private String postageAcHd = "";
    private String contraAcHd = "";
    private String contraDrAcHd = "";
    private String ibrAcHd = "";
    private String atParLimit = "";
    private String status = "";
    private String authorizeStatus = null;;
    private String authorizeUser = "";
    private Date authorizeDt = null;
    private String baseCurrency = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeRemark = "";
    private String prodDesc = "";
    private String billsRealisedHd = "";
    private String telephoneChrgHd = "";
    private String otherHd = "";
    private Double discountRateBd = null;
    private Double overdueRateBd = null;
    private Double interestRateCbp = null;
    private Double overdueRateCbp = null;
    private Double postageRate = null;
    private Double rateForDelay = null;
    private String operatesLike = "";
    private String regType = "";
    private String regSubType = "";
    private String servTaxAcHd = "";
    private String intIcc = "";
    private String otherBankCommFrmCust = "";
    private String creditOtherBankTo = "";
    private String txtOBCCommAcHd = "";
    private String bankChargesAcHd = "";
    private String debitBankChargesAcHd = "";
    private String bankChargesDrAcHd = "";
    private String bankChargesMisAcHd = "";

    public String getBankChargesDrAcHd() {
        return bankChargesDrAcHd;
    }

    public void setBankChargesDrAcHd(String bankChargesDrAcHd) {
        this.bankChargesDrAcHd = bankChargesDrAcHd;
    }

    public String getBankChargesMisAcHd() {
        return bankChargesMisAcHd;
    }

    public void setBankChargesMisAcHd(String bankChargesMisAcHd) {
        this.bankChargesMisAcHd = bankChargesMisAcHd;
    }
    
    public String getDebitBankChargesAcHd() {
        return debitBankChargesAcHd;
    }

    public void setDebitBankChargesAcHd(String debitBankChargesAcHd) {
        this.debitBankChargesAcHd = debitBankChargesAcHd;
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
     * Setter/Getter for GL_AC_HD - table Field
     */
    public void setGlAcHd(String glAcHd) {
        this.glAcHd = glAcHd;
    }

    public String getGlAcHd() {
        return glAcHd;
    }

    /**
     * Setter/Getter for INT_AC_HD - table Field
     */
    public void setIntAcHd(String intAcHd) {
        this.intAcHd = intAcHd;
    }

    public String getIntAcHd() {
        return intAcHd;
    }

    /**
     * Setter/Getter for CHRG_AC_HD - table Field
     */
    public void setChrgAcHd(String chrgAcHd) {
        this.chrgAcHd = chrgAcHd;
    }

    public String getChrgAcHd() {
        return chrgAcHd;
    }

    /**
     * Setter/Getter for CONTRA_AC_HD_YN - table Field
     */
    public void setContraAcHdYn(String contraAcHdYn) {
        this.contraAcHdYn = contraAcHdYn;
    }

    public String getContraAcHdYn() {
        return contraAcHdYn;
    }

    /**
     * Setter/Getter for DD_AC_HD - table Field
     */
    public void setDdAcHd(String ddAcHd) {
        this.ddAcHd = ddAcHd;
    }

    public String getDdAcHd() {
        return ddAcHd;
    }

    /**
     * Setter/Getter for TRANS_PERIOD - table Field
     */
    public void setTransPeriod(Double transPeriod) {
        this.transPeriod = transPeriod;
    }

    public Double getTransPeriod() {
        return transPeriod;
    }

    /**
     * Setter/Getter for POST_DT_CHQ_YN - table Field
     */
    public void setPostDtChqYn(String postDtChqYn) {
        this.postDtChqYn = postDtChqYn;
    }

    public String getPostDtChqYn() {
        return postDtChqYn;
    }

    /**
     * Setter/Getter for MARGIN_AC_HD - table Field
     */
    public void setMarginAcHd(String marginAcHd) {
        this.marginAcHd = marginAcHd;
    }

    public String getMarginAcHd() {
        return marginAcHd;
    }

    /**
     * Setter/Getter for COMM_AC_HD - table Field
     */
    public void setCommAcHd(String commAcHd) {
        this.commAcHd = commAcHd;
    }

    public String getCommAcHd() {
        return commAcHd;
    }

    /**
     * Setter/Getter for POSTAGE_AC_HD - table Field
     */
    public void setPostageAcHd(String postageAcHd) {
        this.postageAcHd = postageAcHd;
    }

    public String getPostageAcHd() {
        return postageAcHd;
    }

    /**
     * Setter/Getter for CONTRA_AC_HD - table Field
     */
    public void setContraAcHd(String contraAcHd) {
        this.contraAcHd = contraAcHd;
    }

    public String getContraAcHd() {
        return contraAcHd;
    }

    /**
     * Setter/Getter for IBR_AC_HD - table Field
     */
    public void setIbrAcHd(String ibrAcHd) {
        this.ibrAcHd = ibrAcHd;
    }

    public String getIbrAcHd() {
        return ibrAcHd;
    }

    /**
     * Setter/Getter for AT_PAR_LIMIT - table Field
     */
    public void setAtParLimit(String atParLimit) {
        this.atParLimit = atParLimit;
    }

    public String getAtParLimit() {
        return atParLimit;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for AUTHORIZE_USER - table Field
     */
    public void setAuthorizeUser(String authorizeUser) {
        this.authorizeUser = authorizeUser;
    }

    public String getAuthorizeUser() {
        return authorizeUser;
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
     * Setter/Getter for BASE_CURRENCY - table Field
     */
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getBaseCurrency() {
        return baseCurrency;
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
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
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
     * Setter/Getter for AUTHORIZE_REMARK - table Field
     */
    public void setAuthorizeRemark(String authorizeRemark) {
        this.authorizeRemark = authorizeRemark;
    }

    public String getAuthorizeRemark() {
        return authorizeRemark;
    }

    /**
     * Setter/Getter for PROD_DESC - table Field
     */
    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    /**
     * Setter/Getter for BILLS_REALISED_HD - table Field
     */
    public void setBillsRealisedHd(String billsRealisedHd) {
        this.billsRealisedHd = billsRealisedHd;
    }

    public String getBillsRealisedHd() {
        return billsRealisedHd;
    }

    /**
     * Setter/Getter for TELEPHONE_CHRG_HD - table Field
     */
    public void setTelephoneChrgHd(String telephoneChrgHd) {
        this.telephoneChrgHd = telephoneChrgHd;
    }

    public String getTelephoneChrgHd() {
        return telephoneChrgHd;
    }

    /**
     * Setter/Getter for OTHER_HD - table Field
     */
    public void setOtherHd(String otherHd) {
        this.otherHd = otherHd;
    }

    public String getOtherHd() {
        return otherHd;
    }

    /**
     * Setter/Getter for DISCOUNT_RATE_BD - table Field
     */
    public void setDiscountRateBd(Double discountRateBd) {
        this.discountRateBd = discountRateBd;
    }

    public Double getDiscountRateBd() {
        return discountRateBd;
    }

    /**
     * Setter/Getter for OVERDUE_RATE_BD - table Field
     */
    public void setOverdueRateBd(Double overdueRateBd) {
        this.overdueRateBd = overdueRateBd;
    }

    public Double getOverdueRateBd() {
        return overdueRateBd;
    }

    /**
     * Setter/Getter for INTEREST_RATE_CBP - table Field
     */
    public void setInterestRateCbp(Double interestRateCbp) {
        this.interestRateCbp = interestRateCbp;
    }

    public Double getInterestRateCbp() {
        return interestRateCbp;
    }

    /**
     * Setter/Getter for OVERDUE_RATE_CBP - table Field
     */
    public void setOverdueRateCbp(Double overdueRateCbp) {
        this.overdueRateCbp = overdueRateCbp;
    }

    public Double getOverdueRateCbp() {
        return overdueRateCbp;
    }

    /**
     * Setter/Getter for POSTAGE_RATE - table Field
     */
    public void setPostageRate(Double postageRate) {
        this.postageRate = postageRate;
    }

    public Double getPostageRate() {
        return postageRate;
    }

    /**
     * Setter/Getter for OPERATES_LIKE - table Field
     */
    public void setOperatesLike(String operatesLike) {
        this.operatesLike = operatesLike;
    }

    public String getOperatesLike() {
        return operatesLike;
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
        strB.append(getTOString("glAcHd", glAcHd));
        strB.append(getTOString("intAcHd", intAcHd));
        strB.append(getTOString("chrgAcHd", chrgAcHd));
        strB.append(getTOString("contraAcHdYn", contraAcHdYn));
        strB.append(getTOString("ddAcHd", ddAcHd));
        strB.append(getTOString("transPeriod", transPeriod));
        strB.append(getTOString("noOfIntDays", noOfIntDays));
        strB.append(getTOString("postDtChqYn", postDtChqYn));
        strB.append(getTOString("marginAcHd", marginAcHd));
        strB.append(getTOString("commAcHd", commAcHd));
        strB.append(getTOString("postageAcHd", postageAcHd));
        strB.append(getTOString("contraAcHd", contraAcHd));
        strB.append(getTOString("contraDrAcHd", contraDrAcHd));
        strB.append(getTOString("ibrAcHd", ibrAcHd));
        strB.append(getTOString("atParLimit", atParLimit));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeUser", authorizeUser));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("baseCurrency", baseCurrency));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeRemark", authorizeRemark));
        strB.append(getTOString("prodDesc", prodDesc));
        strB.append(getTOString("billsRealisedHd", billsRealisedHd));
        strB.append(getTOString("telephoneChrgHd", telephoneChrgHd));
        strB.append(getTOString("otherHd", otherHd));
        strB.append(getTOString("discountRateBd", discountRateBd));
        strB.append(getTOString("overdueRateBd", overdueRateBd));
        strB.append(getTOString("interestRateCbp", interestRateCbp));
        strB.append(getTOString("overdueRateCbp", overdueRateCbp));
        strB.append(getTOString("postageRate", postageRate));
        strB.append(getTOString("operatesLike", operatesLike));
        strB.append(getTOString("regType", regType));
        strB.append(getTOString("regSubType", regSubType));
        strB.append(getTOString("rateForDelay", rateForDelay));
        strB.append(getTOString("servTaxAcHd", servTaxAcHd));
        strB.append(getTOString("intIcc", intIcc));
        strB.append(getTOString("otherBankCommFrmCust", otherBankCommFrmCust));
        strB.append(getTOString("creditOtherBankTo", creditOtherBankTo));
        strB.append(getTOString("txtOBCCommAcHd", txtOBCCommAcHd));
        strB.append(getTOString("bankChargesAcHd", bankChargesAcHd));
        strB.append(getTOString("debitBankChargesAcHd", debitBankChargesAcHd));
        strB.append(getTOString("bankChargesDrAcHd",bankChargesDrAcHd));
        strB.append(getTOString("bankChargesMisAcHd",bankChargesMisAcHd));
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
        strB.append(getTOXml("glAcHd", glAcHd));
        strB.append(getTOXml("intAcHd", intAcHd));
        strB.append(getTOXml("chrgAcHd", chrgAcHd));
        strB.append(getTOXml("contraAcHdYn", contraAcHdYn));
        strB.append(getTOXml("ddAcHd", ddAcHd));
        strB.append(getTOXml("transPeriod", transPeriod));
        strB.append(getTOXml("noOfIntDays", noOfIntDays));
        strB.append(getTOXml("postDtChqYn", postDtChqYn));
        strB.append(getTOXml("marginAcHd", marginAcHd));
        strB.append(getTOXml("commAcHd", commAcHd));
        strB.append(getTOXml("postageAcHd", postageAcHd));
        strB.append(getTOXml("contraAcHd", contraAcHd));
        strB.append(getTOXml("contraDrAcHd", contraDrAcHd));
        strB.append(getTOXml("ibrAcHd", ibrAcHd));
        strB.append(getTOXml("atParLimit", atParLimit));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeUser", authorizeUser));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("baseCurrency", baseCurrency));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeRemark", authorizeRemark));
        strB.append(getTOXml("prodDesc", prodDesc));
        strB.append(getTOXml("billsRealisedHd", billsRealisedHd));
        strB.append(getTOXml("telephoneChrgHd", telephoneChrgHd));
        strB.append(getTOXml("otherHd", otherHd));
        strB.append(getTOXml("discountRateBd", discountRateBd));
        strB.append(getTOXml("overdueRateBd", overdueRateBd));
        strB.append(getTOXml("interestRateCbp", interestRateCbp));
        strB.append(getTOXml("overdueRateCbp", overdueRateCbp));
        strB.append(getTOXml("postageRate", postageRate));
        strB.append(getTOXml("operatesLike", operatesLike));
        strB.append(getTOXml("regType", regType));
        strB.append(getTOXml("regSubType", regSubType));
        strB.append(getTOXml("rateForDelay", rateForDelay));
        strB.append(getTOXml("servTaxAcHd", servTaxAcHd));
        strB.append(getTOXml("intIcc", intIcc));
        strB.append(getTOXml("otherBankCommFrmCust", otherBankCommFrmCust));
        strB.append(getTOXml("creditOtherBankTo", creditOtherBankTo));
        strB.append(getTOXml("txtOBCCommAcHd", txtOBCCommAcHd));
        strB.append(getTOXml("bankChargesAcHd", bankChargesAcHd));
        strB.append(getTOXml("debitBankChargesAcHd", debitBankChargesAcHd));
        strB.append(getTOXml("bankChargesDrAcHd",bankChargesDrAcHd));
        strB.append(getTOXml("bankChargesMisAcHd",bankChargesMisAcHd));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property regType.
     *
     * @return Value of property regType.
     */
    public java.lang.String getRegType() {
        return regType;
    }

    /**
     * Setter for property regType.
     *
     * @param regType New value of property regType.
     */
    public void setRegType(java.lang.String regType) {
        this.regType = regType;
    }

    /**
     * Getter for property regSubType.
     *
     * @return Value of property regSubType.
     */
    public java.lang.String getRegSubType() {
        return regSubType;
    }

    /**
     * Setter for property regSubType.
     *
     * @param regSubType New value of property regSubType.
     */
    public void setRegSubType(java.lang.String regSubType) {
        this.regSubType = regSubType;
    }

    /**
     * Getter for property rateForDelay.
     *
     * @return Value of property rateForDelay.
     */
    public java.lang.Double getRateForDelay() {
        return rateForDelay;
    }

    /**
     * Setter for property rateForDelay.
     *
     * @param rateForDelay New value of property rateForDelay.
     */
    public void setRateForDelay(java.lang.Double rateForDelay) {
        this.rateForDelay = rateForDelay;
    }

    /**
     * Getter for property noOfIntDays.
     *
     * @return Value of property noOfIntDays.
     */
    public java.lang.Double getNoOfIntDays() {
        return noOfIntDays;
    }

    /**
     * Setter for property noOfIntDays.
     *
     * @param noOfIntDays New value of property noOfIntDays.
     */
    public void setNoOfIntDays(java.lang.Double noOfIntDays) {
        this.noOfIntDays = noOfIntDays;
    }

    /**
     * Getter for property servTaxAcHd.
     *
     * @return Value of property servTaxAcHd.
     */
    public java.lang.String getServTaxAcHd() {
        return servTaxAcHd;
    }

    /**
     * Setter for property servTaxAcHd.
     *
     * @param servTaxAcHd New value of property servTaxAcHd.
     */
    public void setServTaxAcHd(java.lang.String servTaxAcHd) {
        this.servTaxAcHd = servTaxAcHd;
    }

    /**
     * Getter for property intIcc.
     *
     * @return Value of property intIcc.
     */
    public java.lang.String getIntIcc() {
        return intIcc;
    }

    /**
     * Setter for property intIcc.
     *
     * @param intIcc New value of property intIcc.
     */
    public void setIntIcc(java.lang.String intIcc) {
        this.intIcc = intIcc;
    }

    /**
     * Getter for property contraDrAcHd.
     *
     * @return Value of property contraDrAcHd.
     */
    public java.lang.String getContraDrAcHd() {
        return contraDrAcHd;
    }

    /**
     * Setter for property contraDrAcHd.
     *
     * @param contraDrAcHd New value of property contraDrAcHd.
     */
    public void setContraDrAcHd(java.lang.String contraDrAcHd) {
        this.contraDrAcHd = contraDrAcHd;
    }

    /**
     * Getter for property otherBankCommFrmCust.
     *
     * @return Value of property otherBankCommFrmCust.
     */
    public java.lang.String getOtherBankCommFrmCust() {
        return otherBankCommFrmCust;
    }

    /**
     * Setter for property otherBankCommFrmCust.
     *
     * @param otherBankCommFrmCust New value of property otherBankCommFrmCust.
     */
    public void setOtherBankCommFrmCust(java.lang.String otherBankCommFrmCust) {
        this.otherBankCommFrmCust = otherBankCommFrmCust;
    }

    /**
     * Getter for property creditObtherBankTo.
     *
     * @return Value of property creditObtherBankTo.
     */
    public java.lang.String getCreditOtherBankTo() {
        return creditOtherBankTo;
    }

    /**
     * Setter for property creditObtherBankTo.
     *
     * @param creditObtherBankTo New value of property creditObtherBankTo.
     */
    public void setCreditOtherBankTo(java.lang.String creditOtherBankTo) {
        this.creditOtherBankTo = creditOtherBankTo;
    }

    /**
     * Getter for property txtOBCCommAcHd.
     *
     * @return Value of property txtOBCCommAcHd.
     */
    public java.lang.String getTxtOBCCommAcHd() {
        return txtOBCCommAcHd;
    }

    /**
     * Setter for property txtOBCCommAcHd.
     *
     * @param txtOBCCommAcHd New value of property txtOBCCommAcHd.
     */
    public void setTxtOBCCommAcHd(java.lang.String txtOBCCommAcHd) {
        this.txtOBCCommAcHd = txtOBCCommAcHd;
    }

    public String getBankChargesAcHd() {
        return bankChargesAcHd;
    }

    public void setBankChargesAcHd(String bankChargesAcHd) {
        this.bankChargesAcHd = bankChargesAcHd;
    }
    
}
