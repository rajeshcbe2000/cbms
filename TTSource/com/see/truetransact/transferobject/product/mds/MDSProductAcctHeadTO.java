/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ExternalWireTO.java
 *
 * Created on Mon Jul 05 11:02:49 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.product.mds;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PVT_EXTERNAL_WIRE.
 */
public class MDSProductAcctHeadTO extends TransferObject implements Serializable {

    private String schemeName = "";
    private String prodId = "";
    private String receiptHead = "";
    private String paymentHead = "";
    private String suspenseHead = "";
    private String suspenseGLorAccount = "";
    private String suspenseProdId = "";
    private String suspenseAccNo = "";
    private String miscellaneousHead = "";
    private String commisionHead = "";
    private String bonusPayableHead = "";
    private String bonusReceivableHead = "";
    private String penalInterestHead = "";
    private String thalayalRepPayHead = "";
    private String thalayalBonusHead = "";
    private String munnalBonusHead = "";
    private String munnalRepPayHead = "";
    private String bankingRepPayHead = "";
    private String noticeChargesHead = "";
    private String chargeHead = "";
    private String caseExpenseHead = "";
    private String discountHead = "";
    private String mdsPayableHead = "";
    private String mdsReceivableHead = "";
    private String sundryHead = "";
    private String sundryPaymentHead = "";
    private String stampRecoveryHead = "";
    private String arcCostHead = "";
    private String arcExpenseHead = "";
    private String eaCostHead = "";
    private String eaExpenseHead = "";
    private String epCostHead = "";
    private String epExpenseHead = "";
    private String postageHead = "";
    private String postageAdvHead = "";
    private String forFeitedHead = "";
    private String legalChrgActHead = "";
    private String partPayBonusRecoveryHead = "";
    private String otherChargeHead = "";
    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
//    public String getKeyData() {
//        setKeyColumns("");
//        return "";
//    }
    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("schemeName", schemeName));
        strB.append(getTOString("paymentHead", paymentHead));
        strB.append(getTOString("receiptHead", receiptHead));
        strB.append(getTOString("suspenseHead", suspenseHead));
        strB.append(getTOString("suspenseGLorAccount", suspenseGLorAccount));
        strB.append(getTOString("suspenseProdId", suspenseProdId));
        strB.append(getTOString("suspenseAccNo", suspenseAccNo));
        strB.append(getTOString("miscellaneousHead", miscellaneousHead));
        strB.append(getTOString("commisionHead", commisionHead));
        strB.append(getTOString("bonusPayableHead", bonusPayableHead));
        strB.append(getTOString("bonusReceivableHead", bonusReceivableHead));
        strB.append(getTOString("penalInterestHead", penalInterestHead));
        strB.append(getTOString("thalayalRepPayHead", thalayalRepPayHead));
        strB.append(getTOString("thalayalBonusHead", thalayalBonusHead));
        strB.append(getTOString("munnalBonusHead", munnalBonusHead));
        strB.append(getTOString("munnalRepPayHead", munnalRepPayHead));
        strB.append(getTOString("bankingRepPayHead", bankingRepPayHead));
        strB.append(getTOString("noticeChargesHead", noticeChargesHead));
        strB.append(getTOString("chargeHead", chargeHead));
        strB.append(getTOString("caseExpenseHead", caseExpenseHead));
        strB.append(getTOString("discountHead", discountHead));
        strB.append(getTOString("mdsPayableHead", mdsPayableHead));
        strB.append(getTOString("mdsReceivableHead", mdsReceivableHead));
        strB.append(getTOString("sundryHead", sundryHead));
        strB.append(getTOString("sundryPaymentHead", sundryPaymentHead));
        strB.append(getTOString("stampRecoveryHead", stampRecoveryHead));
        strB.append(getTOString("arcCostHead", arcCostHead));
        strB.append(getTOString("arcExpenseHead", arcExpenseHead));
        strB.append(getTOString("eaCostHead", eaCostHead));
        strB.append(getTOString("eaExpenseHead", eaExpenseHead));
        strB.append(getTOString("epCostHead", epCostHead));
        strB.append(getTOString("epExpenseHead", epExpenseHead));
        strB.append(getTOString("postageHead", postageHead));
        strB.append(getTOString("postageAdvHead", postageAdvHead));
        strB.append(getTOString("forFeitedHead", forFeitedHead));
        strB.append(getTOString("legalChrgActHead", legalChrgActHead));
        strB.append(getTOString("partPayBonusRecoveryHead", partPayBonusRecoveryHead));
        strB.append(getTOString("otherChargeHead", otherChargeHead));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("schemeName", schemeName));
        strB.append(getTOXml("receiptHead", receiptHead));
        strB.append(getTOXml("paymentHead", paymentHead));
        strB.append(getTOXml("suspenseHead", suspenseHead));
        strB.append(getTOXml("suspenseGLorAccount", suspenseGLorAccount));
        strB.append(getTOXml("suspenseProdId", suspenseProdId));
        strB.append(getTOXml("suspenseAccNo", suspenseAccNo));
        strB.append(getTOXml("miscellaneousHead", miscellaneousHead));
        strB.append(getTOXml("commisionHead", commisionHead));
        strB.append(getTOXml("bonusPayableHead", bonusPayableHead));
        strB.append(getTOXml("bonusReceivableHead", bonusReceivableHead));
        strB.append(getTOXml("penalInterestHead", penalInterestHead));
        strB.append(getTOXml("thalayalRepPayHead", thalayalRepPayHead));
        strB.append(getTOXml("thalayalBonusHead", thalayalBonusHead));
        strB.append(getTOXml("munnalBonusHead", munnalBonusHead));
        strB.append(getTOXml("munnalRepPayHead", munnalRepPayHead));
        strB.append(getTOXml("bankingRepPayHead", bankingRepPayHead));
        strB.append(getTOXml("noticeChargesHead", noticeChargesHead));
        strB.append(getTOXml("chargeHead", chargeHead));
        strB.append(getTOXml("caseExpenseHead", caseExpenseHead));
        strB.append(getTOXml("discountHead", discountHead));
        strB.append(getTOXml("mdsPayableHead", mdsPayableHead));
        strB.append(getTOXml("mdsReceivableHead", mdsReceivableHead));
        strB.append(getTOXml("sundryHead", sundryHead));
        strB.append(getTOXml("sundryPaymentHead", sundryPaymentHead));
        strB.append(getTOXml("stampRecoveryHead", stampRecoveryHead));
        strB.append(getTOXml("arcCostHead", arcCostHead));
        strB.append(getTOXml("arcExpenseHead", arcExpenseHead));
        strB.append(getTOXml("eaCostHead", eaCostHead));
        strB.append(getTOXml("eaExpenseHead", eaExpenseHead));
        strB.append(getTOXml("epCostHead", epCostHead));
        strB.append(getTOXml("epExpenseHead", epExpenseHead));
        strB.append(getTOXml("postageHead", postageHead));
        strB.append(getTOXml("forFeitedHead", forFeitedHead));
        strB.append(getTOXml("legalChrgActHead", legalChrgActHead));    
        strB.append(getTOXml("partPayBonusRecoveryHead", partPayBonusRecoveryHead));    
        strB.append(getTOXml("otherChargeHead", otherChargeHead));   
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getForFeitedHead() {
        return forFeitedHead;
    }

    public void setForFeitedHead(String forFeitedHead) {
        this.forFeitedHead = forFeitedHead;
    }

    public String getPostageAdvHead() {
        return postageAdvHead;
    }

    public void setPostageAdvHead(String postageAdvHead) {
        this.postageAdvHead = postageAdvHead;
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
     * Getter for property receiptHead.
     *
     * @return Value of property receiptHead.
     */
    public java.lang.String getReceiptHead() {
        return receiptHead;
    }

    /**
     * Setter for property receiptHead.
     *
     * @param receiptHead New value of property receiptHead.
     */
    public void setReceiptHead(java.lang.String receiptHead) {
        this.receiptHead = receiptHead;
    }

    /**
     * Getter for property paymentHead.
     *
     * @return Value of property paymentHead.
     */
    public java.lang.String getPaymentHead() {
        return paymentHead;
    }

    /**
     * Setter for property paymentHead.
     *
     * @param paymentHead New value of property paymentHead.
     */
    public void setPaymentHead(java.lang.String paymentHead) {
        this.paymentHead = paymentHead;
    }

    /**
     * Getter for property suspenseHead.
     *
     * @return Value of property suspenseHead.
     */
    public java.lang.String getSuspenseHead() {
        return suspenseHead;
    }

    /**
     * Setter for property suspenseHead.
     *
     * @param suspenseHead New value of property suspenseHead.
     */
    public void setSuspenseHead(java.lang.String suspenseHead) {
        this.suspenseHead = suspenseHead;
    }

    /**
     * Getter for property miscellaneousHead.
     *
     * @return Value of property miscellaneousHead.
     */
    public java.lang.String getMiscellaneousHead() {
        return miscellaneousHead;
    }

    /**
     * Setter for property miscellaneousHead.
     *
     * @param miscellaneousHead New value of property miscellaneousHead.
     */
    public void setMiscellaneousHead(java.lang.String miscellaneousHead) {
        this.miscellaneousHead = miscellaneousHead;
    }

    /**
     * Getter for property commisionHead.
     *
     * @return Value of property commisionHead.
     */
    public java.lang.String getCommisionHead() {
        return commisionHead;
    }

    /**
     * Setter for property commisionHead.
     *
     * @param commisionHead New value of property commisionHead.
     */
    public void setCommisionHead(java.lang.String commisionHead) {
        this.commisionHead = commisionHead;
    }

    /**
     * Getter for property bonusPayableHead.
     *
     * @return Value of property bonusPayableHead.
     */
    public java.lang.String getBonusPayableHead() {
        return bonusPayableHead;
    }

    /**
     * Setter for property bonusPayableHead.
     *
     * @param bonusPayableHead New value of property bonusPayableHead.
     */
    public void setBonusPayableHead(java.lang.String bonusPayableHead) {
        this.bonusPayableHead = bonusPayableHead;
    }

    /**
     * Getter for property bonusReceivableHead.
     *
     * @return Value of property bonusReceivableHead.
     */
    public java.lang.String getBonusReceivableHead() {
        return bonusReceivableHead;
    }

    /**
     * Setter for property bonusReceivableHead.
     *
     * @param bonusReceivableHead New value of property bonusReceivableHead.
     */
    public void setBonusReceivableHead(java.lang.String bonusReceivableHead) {
        this.bonusReceivableHead = bonusReceivableHead;
    }

    /**
     * Getter for property penalInterestHead.
     *
     * @return Value of property penalInterestHead.
     */
    public java.lang.String getPenalInterestHead() {
        return penalInterestHead;
    }

    /**
     * Setter for property penalInterestHead.
     *
     * @param penalInterestHead New value of property penalInterestHead.
     */
    public void setPenalInterestHead(java.lang.String penalInterestHead) {
        this.penalInterestHead = penalInterestHead;
    }

    /**
     * Getter for property thalayalRepPayHead.
     *
     * @return Value of property thalayalRepPayHead.
     */
    public java.lang.String getThalayalRepPayHead() {
        return thalayalRepPayHead;
    }

    /**
     * Setter for property thalayalRepPayHead.
     *
     * @param thalayalRepPayHead New value of property thalayalRepPayHead.
     */
    public void setThalayalRepPayHead(java.lang.String thalayalRepPayHead) {
        this.thalayalRepPayHead = thalayalRepPayHead;
    }

    /**
     * Getter for property thalayalBonusHead.
     *
     * @return Value of property thalayalBonusHead.
     */
    public java.lang.String getThalayalBonusHead() {
        return thalayalBonusHead;
    }

    /**
     * Setter for property thalayalBonusHead.
     *
     * @param thalayalBonusHead New value of property thalayalBonusHead.
     */
    public void setThalayalBonusHead(java.lang.String thalayalBonusHead) {
        this.thalayalBonusHead = thalayalBonusHead;
    }

    /**
     * Getter for property munnalRepPayHead.
     *
     * @return Value of property munnalRepPayHead.
     */
    public java.lang.String getMunnalRepPayHead() {
        return munnalRepPayHead;
    }

    /**
     * Setter for property munnalRepPayHead.
     *
     * @param munnalRepPayHead New value of property munnalRepPayHead.
     */
    public void setMunnalRepPayHead(java.lang.String munnalRepPayHead) {
        this.munnalRepPayHead = munnalRepPayHead;
    }

    /**
     * Getter for property bankingRepPayHead.
     *
     * @return Value of property bankingRepPayHead.
     */
    public java.lang.String getBankingRepPayHead() {
        return bankingRepPayHead;
    }

    /**
     * Setter for property bankingRepPayHead.
     *
     * @param bankingRepPayHead New value of property bankingRepPayHead.
     */
    public void setBankingRepPayHead(java.lang.String bankingRepPayHead) {
        this.bankingRepPayHead = bankingRepPayHead;
    }

    /**
     * Getter for property noticeChargesHead.
     *
     * @return Value of property noticeChargesHead.
     */
    public java.lang.String getNoticeChargesHead() {
        return noticeChargesHead;
    }

    /**
     * Setter for property noticeChargesHead.
     *
     * @param noticeChargesHead New value of property noticeChargesHead.
     */
    public void setNoticeChargesHead(java.lang.String noticeChargesHead) {
        this.noticeChargesHead = noticeChargesHead;
    }

    /**
     * Getter for property caseExpenseHead.
     *
     * @return Value of property caseExpenseHead.
     */
    public java.lang.String getCaseExpenseHead() {
        return caseExpenseHead;
    }

    /**
     * Setter for property caseExpenseHead.
     *
     * @param caseExpenseHead New value of property caseExpenseHead.
     */
    public void setCaseExpenseHead(java.lang.String caseExpenseHead) {
        this.caseExpenseHead = caseExpenseHead;
    }

    /**
     * Getter for property munnalBonusHead.
     *
     * @return Value of property munnalBonusHead.
     */
    public java.lang.String getMunnalBonusHead() {
        return munnalBonusHead;
    }

    /**
     * Setter for property munnalBonusHead.
     *
     * @param munnalBonusHead New value of property munnalBonusHead.
     */
    public void setMunnalBonusHead(java.lang.String munnalBonusHead) {
        this.munnalBonusHead = munnalBonusHead;
    }

    /**
     * Getter for property schemeName.
     *
     * @return Value of property schemeName.
     */
    public java.lang.String getSchemeName() {
        return schemeName;
    }

    /**
     * Setter for property schemeName.
     *
     * @param schemeName New value of property schemeName.
     */
    public void setSchemeName(java.lang.String schemeName) {
        this.schemeName = schemeName;
    }

    /**
     * Getter for property discountHead.
     *
     * @return Value of property discountHead.
     */
    public java.lang.String getDiscountHead() {
        return discountHead;
    }

    /**
     * Setter for property discountHead.
     *
     * @param discountHead New value of property discountHead.
     */
    public void setDiscountHead(java.lang.String discountHead) {
        this.discountHead = discountHead;
    }

    /**
     * Getter for property mdsPayableHead.
     *
     * @return Value of property mdsPayableHead.
     */
    public java.lang.String getMdsPayableHead() {
        return mdsPayableHead;
    }

    /**
     * Setter for property mdsPayableHead.
     *
     * @param mdsPayableHead New value of property mdsPayableHead.
     */
    public void setMdsPayableHead(java.lang.String mdsPayableHead) {
        this.mdsPayableHead = mdsPayableHead;
    }

    /**
     * Getter for property mdsReceivableHead.
     *
     * @return Value of property mdsReceivableHead.
     */
    public java.lang.String getMdsReceivableHead() {
        return mdsReceivableHead;
    }

    /**
     * Setter for property mdsReceivableHead.
     *
     * @param mdsReceivableHead New value of property mdsReceivableHead.
     */
    public void setMdsReceivableHead(java.lang.String mdsReceivableHead) {
        this.mdsReceivableHead = mdsReceivableHead;
    }

    /**
     * Getter for property sundryHead.
     *
     * @return Value of property sundryHead.
     */
    public java.lang.String getSundryHead() {
        return sundryHead;
    }

    /**
     * Setter for property sundryHead.
     *
     * @param sundryHead New value of property sundryHead.
     */
    public void setSundryHead(java.lang.String sundryHead) {
        this.sundryHead = sundryHead;
    }

    /**
     * Getter for property sundryPaymentHead.
     *
     * @return Value of property sundryPaymentHead.
     */
    public java.lang.String getSundryPaymentHead() {
        return sundryPaymentHead;
    }

    /**
     * Setter for property sundryPaymentHead.
     *
     * @param sundryPaymentHead New value of property sundryPaymentHead.
     */
    public void setSundryPaymentHead(java.lang.String sundryPaymentHead) {
        this.sundryPaymentHead = sundryPaymentHead;
    }

    /**
     * Getter for property suspenseProdId.
     *
     * @return Value of property suspenseProdId.
     */
    public java.lang.String getSuspenseProdId() {
        return suspenseProdId;
    }

    /**
     * Setter for property suspenseProdId.
     *
     * @param suspenseProdId New value of property suspenseProdId.
     */
    public void setSuspenseProdId(java.lang.String suspenseProdId) {
        this.suspenseProdId = suspenseProdId;
    }

    /**
     * Getter for property suspenseAccNo.
     *
     * @return Value of property suspenseAccNo.
     */
    public java.lang.String getSuspenseAccNo() {
        return suspenseAccNo;
    }

    /**
     * Setter for property suspenseAccNo.
     *
     * @param suspenseAccNo New value of property suspenseAccNo.
     */
    public void setSuspenseAccNo(java.lang.String suspenseAccNo) {
        this.suspenseAccNo = suspenseAccNo;
    }

    /**
     * Getter for property suspenseGLorAccount.
     *
     * @return Value of property suspenseGLorAccount.
     */
    public java.lang.String getSuspenseGLorAccount() {
        return suspenseGLorAccount;
    }

    /**
     * Setter for property suspenseGLorAccount.
     *
     * @param suspenseGLorAccount New value of property suspenseGLorAccount.
     */
    public void setSuspenseGLorAccount(java.lang.String suspenseGLorAccount) {
        this.suspenseGLorAccount = suspenseGLorAccount;
    }

    /**
     * Getter for property stampRecoveryhead.
     *
     * @return Value of property stampRecoveryhead.
     */
    public java.lang.String getStampRecoveryHead() {
        return stampRecoveryHead;
    }

    /**
     * Setter for property stampRecoveryhead.
     *
     * @param stampRecoveryhead New value of property stampRecoveryhead.
     */
    public void setStampRecoveryHead(java.lang.String stampRecoveryHead) {
        this.stampRecoveryHead = stampRecoveryHead;
    }

    /**
     * Getter for property arcCostHead.
     *
     * @return Value of property arcCostHead.
     */
    public java.lang.String getArcCostHead() {
        return arcCostHead;
    }

    /**
     * Setter for property arcCostHead.
     *
     * @param arcCostHead New value of property arcCostHead.
     */
    public void setArcCostHead(java.lang.String arcCostHead) {
        this.arcCostHead = arcCostHead;
    }

    /**
     * Getter for property arcExpenseHead.
     *
     * @return Value of property arcExpenseHead.
     */
    public java.lang.String getArcExpenseHead() {
        return arcExpenseHead;
    }

    /**
     * Setter for property arcExpenseHead.
     *
     * @param arcExpenseHead New value of property arcExpenseHead.
     */
    public void setArcExpenseHead(java.lang.String arcExpenseHead) {
        this.arcExpenseHead = arcExpenseHead;
    }

    /**
     * Getter for property eaCostHead.
     *
     * @return Value of property eaCostHead.
     */
    public java.lang.String getEaCostHead() {
        return eaCostHead;
    }

    /**
     * Setter for property eaCostHead.
     *
     * @param eaCostHead New value of property eaCostHead.
     */
    public void setEaCostHead(java.lang.String eaCostHead) {
        this.eaCostHead = eaCostHead;
    }

    /**
     * Getter for property eaExpenseHead.
     *
     * @return Value of property eaExpenseHead.
     */
    public java.lang.String getEaExpenseHead() {
        return eaExpenseHead;
    }

    /**
     * Setter for property eaExpenseHead.
     *
     * @param eaExpenseHead New value of property eaExpenseHead.
     */
    public void setEaExpenseHead(java.lang.String eaExpenseHead) {
        this.eaExpenseHead = eaExpenseHead;
    }

    /**
     * Getter for property epCostHead.
     *
     * @return Value of property epCostHead.
     */
    public java.lang.String getEpCostHead() {
        return epCostHead;
    }

    /**
     * Setter for property epCostHead.
     *
     * @param epCostHead New value of property epCostHead.
     */
    public void setEpCostHead(java.lang.String epCostHead) {
        this.epCostHead = epCostHead;
    }

    /**
     * Getter for property epExpenseHead.
     *
     * @return Value of property epExpenseHead.
     */
    public java.lang.String getEpExpenseHead() {
        return epExpenseHead;
    }

    /**
     * Setter for property epExpenseHead.
     *
     * @param epExpenseHead New value of property epExpenseHead.
     */
    public void setEpExpenseHead(java.lang.String epExpenseHead) {
        this.epExpenseHead = epExpenseHead;
    }

    /**
     * Getter for property postageHead.
     *
     * @return Value of property postageHead.
     */
    public java.lang.String getPostageHead() {
        return postageHead;
    }

    /**
     * Setter for property postageHead.
     *
     * @param postageHead New value of property postageHead.
     */
    public void setPostageHead(java.lang.String postageHead) {
        this.postageHead = postageHead;
    }

    public String getChargeHead() {
        return chargeHead;
    }

    public void setChargeHead(String chargeHead) {
        this.chargeHead = chargeHead;
    }

    public String getLegalChrgActHead() {
        return legalChrgActHead;
    }

    public void setLegalChrgActHead(String legalChrgActHead) {
        this.legalChrgActHead = legalChrgActHead;
    }

    public String getPartPayBonusRecoveryHead() {
        return partPayBonusRecoveryHead;
    }

    public void setPartPayBonusRecoveryHead(String partPayBonusRecoveryHead) {
        this.partPayBonusRecoveryHead = partPayBonusRecoveryHead;
    }

    public String getOtherChargeHead() {
        return otherChargeHead;
    }

    public void setOtherChargeHead(String otherChargeHead) {
        this.otherChargeHead = otherChargeHead;
    }
        
}