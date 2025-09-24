/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InterestBatchTO.java
 *
 * Created on Mon Jan 17 15:28:05 IST 2005
 */
package com.see.truetransact.transferobject.batchprocess.interest;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ACT_INTEREST.
 */
public class InterestBatchTO extends TransferObject implements Serializable {

    private String actNum = "";
    private Date intDt = null;
    private String intType = "";
    private String acHdId = "";
    private Date applDt = null;
    private Double intAmt = null;
    private Double intRate = null;
    private Double principleAmt = null;
    private String productId = "";
    private String productType = "";
    private String transLogId = "";
    private String custId = "";
    private String isTdsApplied = "";
    private Double tdsAmt = null;
    private Date trnDt = null;
    private String drCr = "";
    private Double tot_int_amt = null;
    private String taxAmt = null;
    private Double tdsDeductedFromAll = null;
    private Double totalTdsAmt = null;
    private Date lastTdsApplDt = null;
    private String lastTdsRecivedFrom = "";
    private String branch_code=""; //Added by kannan
    private String user_id="";  
    private String particulars ="";
    private Double crAmount = null;
    private Double reserveFundPercent = null;
    private Double reserveFundAmt = null;
    private Double slNo = 0.0;
    private String intPostingPossible = "Y"; // Added by nithya on 19-11-2019 for KD-729

    public Double getSlNo() {
        return slNo;
    }

    public void setSlNo(Double slNo) {
        this.slNo = slNo;
    }
    
    public Double getReserveFundAmt() {
        return reserveFundAmt;
    }

    public void setReserveFundAmt(Double reserveFundAmt) {
        this.reserveFundAmt = reserveFundAmt;
    }
    

    public Double getReserveFundPercent() {
        return reserveFundPercent;
    }

    public void setReserveFundPercent(Double reserveFundPercent) {
        this.reserveFundPercent = reserveFundPercent;
    }
    
    

    public Double getCrAmount() {
        return crAmount;
    }

    public void setCrAmount(Double crAmount) {
        this.crAmount = crAmount;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    
    
    /** Setter/Getter for ACT_NUM - table Field*/
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    /**
     * Setter/Getter for INT_DT - table Field
     */
    public void setIntDt(Date intDt) {
        this.intDt = intDt;
    }

    public Date getIntDt() {
        return intDt;
    }

    /**
     * Setter/Getter for INT_TYPE - table Field
     */
    public void setIntType(String intType) {
        this.intType = intType;
    }

    public String getIntType() {
        return intType;
    }

    /**
     * Setter/Getter for AC_HD_ID - table Field
     */
    public void setAcHdId(String acHdId) {
        this.acHdId = acHdId;
    }

    public String getAcHdId() {
        return acHdId;
    }

    /**
     * Setter/Getter for APPL_DT - table Field
     */
    public void setApplDt(Date applDt) {
        this.applDt = applDt;
    }

    public Date getApplDt() {
        return applDt;
    }

    /**
     * Setter/Getter for INT_AMT - table Field
     */
    public void setIntAmt(Double intAmt) {
        this.intAmt = intAmt;
    }

    public Double getIntAmt() {
        return intAmt;
    }

    /**
     * Setter/Getter for INT_RATE - table Field
     */
    public void setIntRate(Double intRate) {
        this.intRate = intRate;
    }

    public Double getIntRate() {
        return intRate;
    }

    /**
     * Setter/Getter for PRINCIPLE_AMT - table Field
     */
    public void setPrincipleAmt(Double principleAmt) {
        this.principleAmt = principleAmt;
    }

    public Double getPrincipleAmt() {
        return principleAmt;
    }

    /**
     * Setter/Getter for PRODUCT_ID - table Field
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    /**
     * Setter/Getter for PRODUCT_TYPE - table Field
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
    }

    /**
     * Setter/Getter for TRANS_LOG_ID - table Field
     */
    public void setTransLogId(String transLogId) {
        this.transLogId = transLogId;
    }

    public String getTransLogId() {
        return transLogId;
    }

    /**
     * Setter/Getter for CUST_ID - table Field
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
    }

    /**
     * Setter/Getter for IS_TDS_APPLIED - table Field
     */
    public void setIsTdsApplied(String isTdsApplied) {
        this.isTdsApplied = isTdsApplied;
    }

    public String getIsTdsApplied() {
        return isTdsApplied;
    }

    /**
     * Setter/Getter for TDS_AMT - table Field
     */
    public void setTdsAmt(Double tdsAmt) {
        this.tdsAmt = tdsAmt;
    }

    public Double getTdsAmt() {
        return tdsAmt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("actNum");
        return actNum;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("intDt", intDt));
        strB.append(getTOString("intType", intType));
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("applDt", applDt));
        strB.append(getTOString("intAmt", intAmt));
        strB.append(getTOString("intRate", intRate));
        strB.append(getTOString("principleAmt", principleAmt));
        strB.append(getTOString("productId", productId));
        strB.append(getTOString("productType", productType));
        strB.append(getTOString("transLogId", transLogId));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("isTdsApplied", isTdsApplied));
        strB.append(getTOString("tdsAmt", tdsAmt));
        strB.append(getTOString("trnDt", trnDt));
        strB.append(getTOString("drCr", drCr));
        strB.append(getTOString("tot_int_amt", tot_int_amt));
        strB.append(getTOString("taxAmt", taxAmt));
        strB.append(getTOString("tdsDeductedFromAll", tdsDeductedFromAll));
        strB.append(getTOString("totalTdsAmt", totalTdsAmt));
        strB.append(getTOString("lastTdsApplDt", lastTdsApplDt));
        strB.append(getTOString("lastTdsRecivedFrom", lastTdsRecivedFrom));
        strB.append(getTOString("user_id", user_id)); 
        strB.append(getTOString("branch_code", branch_code)); 
        strB.append(getTOString("particulars", particulars));
        strB.append(getTOString("crAmount", crAmount));   
        strB.append(getTOString("reserveFundPercent", reserveFundPercent));
        strB.append(getTOString("reserveFundAmt", reserveFundAmt));  
        strB.append(getTOString("slNo", slNo));  
        strB.append(getTOString("intPostingPossible", intPostingPossible));  
        strB.append(getTOStringEnd());
        return strB.toString();



    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("intDt", intDt));
        strB.append(getTOXml("intType", intType));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("applDt", applDt));
        strB.append(getTOXml("intAmt", intAmt));
        strB.append(getTOXml("intRate", intRate));
        strB.append(getTOXml("principleAmt", principleAmt));
        strB.append(getTOXml("productId", productId));
        strB.append(getTOXml("productType", productType));
        strB.append(getTOXml("transLogId", transLogId));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("isTdsApplied", isTdsApplied));
        strB.append(getTOXml("tdsAmt", tdsAmt));
        strB.append(getTOXml("trnDt", trnDt));
        strB.append(getTOXml("drCr", drCr));
        strB.append(getTOXml("tot_int_amt", tot_int_amt));
        strB.append(getTOXml("taxAmt", taxAmt));
        strB.append(getTOXml("tdsDeductedFromAll", tdsDeductedFromAll));
        strB.append(getTOXml("totalTdsAmt", totalTdsAmt));
        strB.append(getTOXml("lastTdsApplDt", lastTdsApplDt));
        strB.append(getTOXml("lastTdsRecivedFrom", lastTdsRecivedFrom));
        strB.append(getTOXml("user_id", user_id));
        strB.append(getTOXml("branch_code", branch_code));    
        strB.append(getTOXml("particulars", particulars));   
        strB.append(getTOXml("crAmount", crAmount));   
        strB.append(getTOXml("reserveFundPercent", reserveFundPercent));             
        strB.append(getTOXml("reserveFundAmt", reserveFundAmt));  
        strB.append(getTOXml("slNo", slNo));  
        strB.append(getTOXml("intPostingPossible", intPostingPossible));  
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property trnDt.
     *
     * @return Value of property trnDt.
     */
    public java.util.Date getTrnDt() {
        return trnDt;
    }

    /**
     * Setter for property trnDt.
     *
     * @param trnDt New value of property trnDt.
     */
    public void setTrnDt(java.util.Date trnDt) {
        this.trnDt = trnDt;
    }

    /**
     * Getter for property drCr.
     *
     * @return Value of property drCr.
     */
    public java.lang.String getDrCr() {
        return drCr;
    }

    /**
     * Setter for property drCr.
     *
     * @param drCr New value of property drCr.
     */
    public void setDrCr(java.lang.String drCr) {
        this.drCr = drCr;
    }

    /**
     * Getter for property tot_int_amt.
     *
     * @return Value of property tot_int_amt.
     */
    public java.lang.Double getTot_int_amt() {
        return tot_int_amt;
    }

    /**
     * Setter for property tot_int_amt.
     *
     * @param tot_int_amt New value of property tot_int_amt.
     */
    public void setTot_int_amt(java.lang.Double tot_int_amt) {
        this.tot_int_amt = tot_int_amt;
    }

    /**
     * Getter for property taxAmt.
     *
     * @return Value of property taxAmt.
     */
    public java.lang.String getTaxAmt() {
        return taxAmt;
    }

    /**
     * Setter for property taxAmt.
     *
     * @param taxAmt New value of property taxAmt.
     */
    public void setTaxAmt(java.lang.String taxAmt) {
        this.taxAmt = taxAmt;
    }

    /**
     * Getter for property tdsDeductedFromAll.
     *
     * @return Value of property tdsDeductedFromAll.
     */
    public java.lang.Double getTdsDeductedFromAll() {
        return tdsDeductedFromAll;
    }

    /**
     * Setter for property tdsDeductedFromAll.
     *
     * @param tdsDeductedFromAll New value of property tdsDeductedFromAll.
     */
    public void setTdsDeductedFromAll(java.lang.Double tdsDeductedFromAll) {
        this.tdsDeductedFromAll = tdsDeductedFromAll;
    }

    /**
     * Getter for property totalTdsAmt.
     *
     * @return Value of property totalTdsAmt.
     */
    public java.lang.Double getTotalTdsAmt() {
        return totalTdsAmt;
    }

    /**
     * Setter for property totalTdsAmt.
     *
     * @param totalTdsAmt New value of property totalTdsAmt.
     */
    public void setTotalTdsAmt(java.lang.Double totalTdsAmt) {
        this.totalTdsAmt = totalTdsAmt;
    }

    /**
     * Getter for property lastTdsApplDt.
     *
     * @return Value of property lastTdsApplDt.
     */
    public java.util.Date getLastTdsApplDt() {
        return lastTdsApplDt;
    }

    /**
     * Setter for property lastTdsApplDt.
     *
     * @param lastTdsApplDt New value of property lastTdsApplDt.
     */
    public void setLastTdsApplDt(java.util.Date lastTdsApplDt) {
        this.lastTdsApplDt = lastTdsApplDt;
    }

    /**
     * Getter for property lastTdsRecivedFrom.
     *
     * @return Value of property lastTdsRecivedFrom.
     */
    public java.lang.String getLastTdsRecivedFrom() {
        return lastTdsRecivedFrom;
    }

    /**
     * Setter for property lastTdsRecivedFrom.
     *
     * @param lastTdsRecivedFrom New value of property lastTdsRecivedFrom.
     */
    public void setLastTdsRecivedFrom(java.lang.String lastTdsRecivedFrom) {
        this.lastTdsRecivedFrom = lastTdsRecivedFrom;
    }

    public String getIntPostingPossible() {
        return intPostingPossible;
    }

    public void setIntPostingPossible(String intPostingPossible) {
        this.intPostingPossible = intPostingPossible;
    }
        
}