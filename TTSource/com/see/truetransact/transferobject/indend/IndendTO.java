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
package com.see.truetransact.transferobject.indend;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class IndendTO extends TransferObject implements Serializable {

    private String depoId = "", storeName = "", strIRNo = "", genTrans = "",
            transType = "", supplier = "", purchBillNo = "", tinNo = "";
    Date dateIndand = null, salesDate = null, billDate = null;

    private String authorizeStatus = null;
    private String authorizeBy = null;
    private Date authorizeDte = null;
    private String status = null;
    private String statusBy = null;//jiby
    private String branch = null;
    private Integer slNo = 0;
    private String suspenseAcHd = "";
    private String cboIndentHeads = "";
    private String narration = null;
    private String stIndentNo = null;
    private String salesmanName = null;
    private Double cgstAmt = 0.0;
    private Double sgstAmt = 0.0;
    private Double discountAmt = 0.0;
    private Double roundOffAmt = 0.0;
    Double purAmount, amount, vatAmt, othrExpAmt, miscAmt, commRecvdAmt, vatIndAmt,netAmount,otherIncome;
    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }


    public Double getOtherIncome() {
        return otherIncome;
    }

    public void setOtherIncome(Double otherIncome) {
        this.otherIncome = otherIncome;
    }

    public Double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public String getSuspenseAcHd() {
        return suspenseAcHd;
    }

    public void setSuspenseAcHd(String suspenseAcHd) {
        this.suspenseAcHd = suspenseAcHd;
    }
    
    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public String getStIndentNo() {
        return stIndentNo;
    }

    public void setStIndentNo(String stIndentNo) {
        this.stIndentNo = stIndentNo;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getCboIndentHeads() {
        return cboIndentHeads;
    }

    public void setCboIndentHeads(String cboIndentHeads) {
        this.cboIndentHeads = cboIndentHeads;
    }
    
    

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        //setKeyColumns(borrowingNo);
        return strIRNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("strIRNo", strIRNo));
        strB.append(getTOString("depoId", depoId));
        strB.append(getTOString("storeName", storeName));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("dateIndand", dateIndand));
        strB.append(getTOString("purAmount", purAmount));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDte", authorizeDte));
        strB.append(getTOString("status", status));
        strB.append(getTOString("genTrans", genTrans));

        strB.append(getTOString("vatAmt", vatAmt));
        strB.append(getTOString("purchBillNo", purchBillNo));
        strB.append(getTOString("tinNo", tinNo));
        strB.append(getTOString("salesDate", salesDate));
        strB.append(getTOString("billDate", billDate));
        strB.append(getTOString("narration", narration));
        strB.append(getTOString("stIndentNo", stIndentNo));
        strB.append(getTOString("miscAmt", miscAmt));
        strB.append(getTOString("commRecvdAmt", commRecvdAmt));
        strB.append(getTOString("othrExpAmt", othrExpAmt));
        strB.append(getTOString("vatIndAmt", vatIndAmt));
        strB.append(getTOString("branch", branch));
        strB.append(getTOString("suspenseAcHd", suspenseAcHd));
        strB.append(getTOString("netAmount", netAmount));
        strB.append(getTOString("otherIncome", otherIncome));
        strB.append(getTOString("sgstAmt", sgstAmt));
        strB.append(getTOString("cgstAmt", cgstAmt));
        strB.append(getTOString("cboIndentHeads", cboIndentHeads));
        strB.append(getTOString("roundOffAmt", roundOffAmt));
        strB.append(getTOString("discountAmt", discountAmt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("strIRNo ", strIRNo));
        strB.append(getTOXml("depoId", depoId));
        strB.append(getTOXml("storeName", storeName));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("dateIndand", dateIndand));
        strB.append(getTOXml("purAmount", purAmount));
        strB.append(getTOXml("amount", amount));

        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDte", authorizeDte));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("genTrans", genTrans));

        strB.append(getTOXml("vatAmt", vatAmt));
        strB.append(getTOXml("purchBillNo", purchBillNo));
        strB.append(getTOXml("tinNo", tinNo));
        strB.append(getTOXml("salesDate", salesDate));
        strB.append(getTOXml("billDate", billDate));
        strB.append(getTOXml("narration", narration));
        strB.append(getTOXml("stIndentNo", stIndentNo));
        strB.append(getTOXml("miscAmt", miscAmt));
        strB.append(getTOXml("commRecvdAmt", commRecvdAmt));
        strB.append(getTOXml("othrExpAmt", othrExpAmt));
        strB.append(getTOXml("vatIndAmt", vatIndAmt));
        strB.append(getTOXml("branch", branch));
        strB.append(getTOXml("suspenseAcHd", suspenseAcHd));
        strB.append(getTOXml("netAmount", netAmount));
        strB.append(getTOXml("otherIncome", otherIncome));
        strB.append(getTOXml("sgstAmt", sgstAmt));
        strB.append(getTOXml("cgstAmt", cgstAmt));
        strB.append(getTOXml("cboIndentHeads", cboIndentHeads));
        strB.append(getTOXml("roundOffAmt", roundOffAmt));
        strB.append(getTOXml("discountAmt", discountAmt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property depoId.
     *
     * @return Value of property depoId.
     */
    public java.lang.String getDepoId() {
        return depoId;
    }

    /**
     * Setter for property depoId.
     *
     * @param depoId New value of property depoId.
     */
    public void setDepoId(java.lang.String depoId) {
        this.depoId = depoId;
    }

    /**
     * Getter for property storeName.
     *
     * @return Value of property storeName.
     */
    public java.lang.String getStoreName() {
        return storeName;
    }

    /**
     * Setter for property storeName.
     *
     * @param storeName New value of property storeName.
     */
    public void setStoreName(java.lang.String storeName) {
        this.storeName = storeName;
    }

    /**
     * Getter for property strIRNo.
     *
     * @return Value of property strIRNo.
     */
    public java.lang.String getStrIRNo() {
        return strIRNo;
    }

    /**
     * Setter for property strIRNo.
     *
     * @param strIRNo New value of property strIRNo.
     */
    public void setStrIRNo(java.lang.String strIRNo) {
        this.strIRNo = strIRNo;
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
     * Getter for property dateIndand.
     *
     * @return Value of property dateIndand.
     */
    public java.util.Date getDateIndand() {
        return dateIndand;
    }

    /**
     * Setter for property dateIndand.
     *
     * @param dateIndand New value of property dateIndand.
     */
    public void setDateIndand(java.util.Date dateIndand) {
        this.dateIndand = dateIndand;
    }

    /**
     * Getter for property purAmount.
     *
     * @return Value of property purAmount.
     */
    public java.lang.Double getPurAmount() {
        return purAmount;
    }

    /**
     * Setter for property purAmount.
     *
     * @param purAmount New value of property purAmount.
     */
    public void setPurAmount(java.lang.Double purAmount) {
        this.purAmount = purAmount;
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
     * Getter for property authorizeDte.
     *
     * @return Value of property authorizeDte.
     */
    public java.util.Date getAuthorizeDte() {
        return authorizeDte;
    }

    /**
     * Setter for property authorizeDte.
     *
     * @param authorizeDte New value of property authorizeDte.
     */
    public void setAuthorizeDte(java.util.Date authorizeDte) {
        this.authorizeDte = authorizeDte;
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
     * Getter for property genTrans.
     *
     * @return Value of property genTrans.
     */
    public java.lang.String getGenTrans() {
        return genTrans;
    }

    /**
     * Setter for property genTrans.
     *
     * @param genTrans New value of property genTrans.
     */
    public void setGenTrans(java.lang.String genTrans) {
        this.genTrans = genTrans;
    }

    /**
     * Getter for property vatAmt.
     *
     * @return Value of property vatAmt.
     */
    public Double getVatAmt() {
        return vatAmt;
    }

    /**
     * Setter for property vatAmt.
     *
     * @param vatAmt New value of property vatAmt.
     */
    public void setVatAmt(Double vatAmt) {
        this.vatAmt = vatAmt;
    }

    /**
     * Getter for property purchBillNo.
     *
     * @return Value of property purchBillNo.
     */
    public String getPurchBillNo() {
        return purchBillNo;
    }

    /**
     * Setter for property purchBillNo.
     *
     * @param purchBillNo New value of property purchBillNo.
     */
    public void setPurchBillNo(String purchBillNo) {
        this.purchBillNo = purchBillNo;
    }

    /**
     * Getter for property tinNo.
     *
     * @return Value of property tinNo.
     */
    public java.lang.String getTinNo() {
        return tinNo;
    }

    /**
     * Setter for property tinNo.
     *
     * @param tinNo New value of property tinNo.
     */
    public void setTinNo(java.lang.String tinNo) {
        this.tinNo = tinNo;
    }

    /**
     * Getter for property salesDate.
     *
     * @return Value of property salesDate.
     */
    public java.util.Date getSalesDate() {
        return salesDate;
    }

    /**
     * Setter for property salesDate.
     *
     * @param salesDate New value of property salesDate.
     */
    public void setSalesDate(java.util.Date salesDate) {
        this.salesDate = salesDate;
    }

    public Double getOthrExpAmt() {
        return othrExpAmt;
    }

    public void setOthrExpAmt(Double othrExpAmt) {
        this.othrExpAmt = othrExpAmt;
    }

    public Double getMiscAmt() {
        return miscAmt;
    }

    public void setMiscAmt(Double miscAmt) {
        this.miscAmt = miscAmt;
    }

    public Double getCommRecvdAmt() {
        return commRecvdAmt;
    }

    public void setCommRecvdAmt(Double commRecvdAmt) {
        this.commRecvdAmt = commRecvdAmt;
    }

    public Double getVatIndAmt() {
        return vatIndAmt;
    }

    public void setVatIndAmt(Double vatIndAmt) {
        this.vatIndAmt = vatIndAmt;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(Integer slNo) {
        this.slNo = slNo;
    }

    public Double getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(Double cgstAmt) {
        this.cgstAmt = cgstAmt;
    }

    public Double getSgstAmt() {
        return sgstAmt;
    }

    public void setSgstAmt(Double sgstAmt) {
        this.sgstAmt = sgstAmt;
    }

    public Double getRoundOffAmt() {
        return roundOffAmt;
    }

    public void setRoundOffAmt(Double roundOffAmt) {
        this.roundOffAmt = roundOffAmt;
    }  

    public Double getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(Double discountAmt) {
        this.discountAmt = discountAmt;
    }
     
    
    
}
