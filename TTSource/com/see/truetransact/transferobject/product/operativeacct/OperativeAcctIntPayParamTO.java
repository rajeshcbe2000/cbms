/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctIntPayParamTO.java
 * 
 * Created on Tue Jul 13 16:20:22 IST 2004
 */

package com.see.truetransact.transferobject.product.operativeacct;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/** Table name for this TO is OP_AC_INTPAY_PARAM. */

public class OperativeAcctIntPayParamTO extends TransferObject implements Serializable {
	private String prodId = "";
	private Double minCrIntRate = null;
	private Double maxCrIntRate = null;
	private Double applCrIntRate = null;
	private Double minCrIntAmt = null;
	private Double maxCrIntAmt = null;
	private Double crIntCalcFreq = null;
	private Double crIntApplFreq = null;
	private String startmonIntCalc = "";
	private String endmonIntCalc = "";
	private Date lastIntCalcdtCr = null;
	private Date lastIntAppldtCr = null;
	private String crCompound = "";
	private Double crCompoundCalcFreq = null;
	private String crProductRoundoff = "";
	private String crIntRoundoff = "";
	private Double startdayProdCalc = null;
	private Double enddayProdCalc = null;
	private String calcCriteria = "";
	private String productFreqIntPay = "";
	private String creditIntGiven = "";
	private Double startdayIntCalc = null;
	private Double enddayIntCalc = null;
	private String startmonProdCalc = "";
	private String endmonProdCalc = "";
        private Double minBalforIntCalc=null;

	/** Setter/Getter for PROD_ID - table Field*/
	public void setProdId (String prodId) {
		this.prodId = prodId;
	}
	public String getProdId () {
		return prodId;
	}

	/** Setter/Getter for MIN_CR_INT_RATE - table Field*/
	public void setMinCrIntRate (Double minCrIntRate) {
		this.minCrIntRate = minCrIntRate;
	}
	public Double getMinCrIntRate () {
		return minCrIntRate;
	}

	/** Setter/Getter for MAX_CR_INT_RATE - table Field*/
	public void setMaxCrIntRate (Double maxCrIntRate) {
		this.maxCrIntRate = maxCrIntRate;
	}
	public Double getMaxCrIntRate () {
		return maxCrIntRate;
	}

	/** Setter/Getter for APPL_CR_INT_RATE - table Field*/
	public void setApplCrIntRate (Double applCrIntRate) {
		this.applCrIntRate = applCrIntRate;
	}
	public Double getApplCrIntRate () {
		return applCrIntRate;
	}

	/** Setter/Getter for MIN_CR_INT_AMT - table Field*/
	public void setMinCrIntAmt (Double minCrIntAmt) {
		this.minCrIntAmt = minCrIntAmt;
	}
	public Double getMinCrIntAmt () {
		return minCrIntAmt;
	}

	/** Setter/Getter for MAX_CR_INT_AMT - table Field*/
	public void setMaxCrIntAmt (Double maxCrIntAmt) {
		this.maxCrIntAmt = maxCrIntAmt;
	}
	public Double getMaxCrIntAmt () {
		return maxCrIntAmt;
	}

	/** Setter/Getter for CR_INT_CALC_FREQ - table Field*/
	public void setCrIntCalcFreq (Double crIntCalcFreq) {
		this.crIntCalcFreq = crIntCalcFreq;
	}
	public Double getCrIntCalcFreq () {
		return crIntCalcFreq;
	}

	/** Setter/Getter for CR_INT_APPL_FREQ - table Field*/
	public void setCrIntApplFreq (Double crIntApplFreq) {
		this.crIntApplFreq = crIntApplFreq;
	}
	public Double getCrIntApplFreq () {
		return crIntApplFreq;
	}

	/** Setter/Getter for STARTMON_INT_CALC - table Field*/
	public void setStartmonIntCalc (String startmonIntCalc) {
		this.startmonIntCalc = startmonIntCalc;
	}
	public String getStartmonIntCalc () {
		return startmonIntCalc;
	}

	/** Setter/Getter for ENDMON_INT_CALC - table Field*/
	public void setEndmonIntCalc (String endmonIntCalc) {
		this.endmonIntCalc = endmonIntCalc;
	}
	public String getEndmonIntCalc () {
		return endmonIntCalc;
	}

	/** Setter/Getter for LAST_INT_CALCDT_CR - table Field*/
	public void setLastIntCalcdtCr (Date lastIntCalcdtCr) {
		this.lastIntCalcdtCr = lastIntCalcdtCr;
	}
	public Date getLastIntCalcdtCr () {
		return lastIntCalcdtCr;
	}

	/** Setter/Getter for LAST_INT_APPLDT_CR - table Field*/
	public void setLastIntAppldtCr (Date lastIntAppldtCr) {
		this.lastIntAppldtCr = lastIntAppldtCr;
	}
	public Date getLastIntAppldtCr () {
		return lastIntAppldtCr;
	}

	/** Setter/Getter for CR_COMPOUND - table Field*/
	public void setCrCompound (String crCompound) {
		this.crCompound = crCompound;
	}
	public String getCrCompound () {
		return crCompound;
	}

	/** Setter/Getter for CR_COMPOUND_CALC_FREQ - table Field*/
	public void setCrCompoundCalcFreq (Double crCompoundCalcFreq) {
		this.crCompoundCalcFreq = crCompoundCalcFreq;
	}
	public Double getCrCompoundCalcFreq () {
		return crCompoundCalcFreq;
	}

	/** Setter/Getter for CR_PRODUCT_ROUNDOFF - table Field*/
	public void setCrProductRoundoff (String crProductRoundoff) {
		this.crProductRoundoff = crProductRoundoff;
	}
	public String getCrProductRoundoff () {
		return crProductRoundoff;
	}

	/** Setter/Getter for CR_INT_ROUNDOFF - table Field*/
	public void setCrIntRoundoff (String crIntRoundoff) {
		this.crIntRoundoff = crIntRoundoff;
	}
	public String getCrIntRoundoff () {
		return crIntRoundoff;
	}

	/** Setter/Getter for STARTDAY_PROD_CALC - table Field*/
	public void setStartdayProdCalc (Double startdayProdCalc) {
		this.startdayProdCalc = startdayProdCalc;
	}
	public Double getStartdayProdCalc () {
		return startdayProdCalc;
	}

	/** Setter/Getter for ENDDAY_PROD_CALC - table Field*/
	public void setEnddayProdCalc (Double enddayProdCalc) {
		this.enddayProdCalc = enddayProdCalc;
	}
	public Double getEnddayProdCalc () {
		return enddayProdCalc;
	}

	/** Setter/Getter for CALC_CRITERIA - table Field*/
	public void setCalcCriteria (String calcCriteria) {
		this.calcCriteria = calcCriteria;
	}
	public String getCalcCriteria () {
		return calcCriteria;
	}

	/** Setter/Getter for PRODUCT_FREQ_INT_PAY - table Field*/
	public void setProductFreqIntPay (String productFreqIntPay) {
		this.productFreqIntPay = productFreqIntPay;
	}
	public String getProductFreqIntPay () {
		return productFreqIntPay;
	}

	/** Setter/Getter for CREDIT_INT_GIVEN - table Field*/
	public void setCreditIntGiven (String creditIntGiven) {
		this.creditIntGiven = creditIntGiven;
	}
	public String getCreditIntGiven () {
		return creditIntGiven;
	}

	/** Setter/Getter for STARTDAY_INT_CALC - table Field*/
	public void setStartdayIntCalc (Double startdayIntCalc) {
		this.startdayIntCalc = startdayIntCalc;
	}
	public Double getStartdayIntCalc () {
		return startdayIntCalc;
	}

	/** Setter/Getter for ENDDAY_INT_CALC - table Field*/
	public void setEnddayIntCalc (Double enddayIntCalc) {
		this.enddayIntCalc = enddayIntCalc;
	}
	public Double getEnddayIntCalc () {
		return enddayIntCalc;
	}

	/** Setter/Getter for STARTMON_PROD_CALC - table Field*/
	public void setStartmonProdCalc (String startmonProdCalc) {
		this.startmonProdCalc = startmonProdCalc;
	}
	public String getStartmonProdCalc () {
		return startmonProdCalc;
	}

	/** Setter/Getter for ENDMON_PROD_CALC - table Field*/
	public void setEndmonProdCalc (String endmonProdCalc) {
		this.endmonProdCalc = endmonProdCalc;
	}
	public String getEndmonProdCalc () {
		return endmonProdCalc;
	}

	/** getKeyData returns the Primary Key Columns for this TO
	 *  User needs to add the Key columns as a setter 
	 *  Example : 
	 *            setKeyColumns("col1" + KEY_VAL_SEPARATOR + "col2"); 
	 *            return col1 + KEY_VAL_SEPARATOR + col2; 
	 */
	 public String getKeyData() {
		setKeyColumns("prodId");
		return prodId;
	}

	/** toString method which returns this TO as a String. */
	public String toString() {
		StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
		strB.append (getTOStringKey(getKeyData()));
		strB.append(getTOString("prodId", prodId));
		strB.append(getTOString("minCrIntRate", minCrIntRate));
		strB.append(getTOString("maxCrIntRate", maxCrIntRate));
		strB.append(getTOString("applCrIntRate", applCrIntRate));
		strB.append(getTOString("minCrIntAmt", minCrIntAmt));
		strB.append(getTOString("maxCrIntAmt", maxCrIntAmt));
		strB.append(getTOString("crIntCalcFreq", crIntCalcFreq));
		strB.append(getTOString("crIntApplFreq", crIntApplFreq));
		strB.append(getTOString("startmonIntCalc", startmonIntCalc));
		strB.append(getTOString("endmonIntCalc", endmonIntCalc));
		strB.append(getTOString("lastIntCalcdtCr", lastIntCalcdtCr));
		strB.append(getTOString("lastIntAppldtCr", lastIntAppldtCr));
		strB.append(getTOString("crCompound", crCompound));
		strB.append(getTOString("crCompoundCalcFreq", crCompoundCalcFreq));
		strB.append(getTOString("crProductRoundoff", crProductRoundoff));
		strB.append(getTOString("crIntRoundoff", crIntRoundoff));
		strB.append(getTOString("startdayProdCalc", startdayProdCalc));
		strB.append(getTOString("enddayProdCalc", enddayProdCalc));
		strB.append(getTOString("calcCriteria", calcCriteria));
		strB.append(getTOString("productFreqIntPay", productFreqIntPay));
		strB.append(getTOString("creditIntGiven", creditIntGiven));
		strB.append(getTOString("startdayIntCalc", startdayIntCalc));
		strB.append(getTOString("enddayIntCalc", enddayIntCalc));
		strB.append(getTOString("startmonProdCalc", startmonProdCalc));
		strB.append(getTOString("endmonProdCalc", endmonProdCalc));
                strB.append(getTOString("minBalforIntCalc", minBalforIntCalc));
		strB.append(getTOStringEnd());
		return strB.toString();
	}

	/** toXML method which returns this TO as a XML output. */
	public String toXML() {
		StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
		strB.append (getTOXmlKey(getKeyData()));
		strB.append(getTOXml("prodId", prodId));
		strB.append(getTOXml("minCrIntRate", minCrIntRate));
		strB.append(getTOXml("maxCrIntRate", maxCrIntRate));
		strB.append(getTOXml("applCrIntRate", applCrIntRate));
		strB.append(getTOXml("minCrIntAmt", minCrIntAmt));
		strB.append(getTOXml("maxCrIntAmt", maxCrIntAmt));
		strB.append(getTOXml("crIntCalcFreq", crIntCalcFreq));
		strB.append(getTOXml("crIntApplFreq", crIntApplFreq));
		strB.append(getTOXml("startmonIntCalc", startmonIntCalc));
		strB.append(getTOXml("endmonIntCalc", endmonIntCalc));
		strB.append(getTOXml("lastIntCalcdtCr", lastIntCalcdtCr));
		strB.append(getTOXml("lastIntAppldtCr", lastIntAppldtCr));
		strB.append(getTOXml("crCompound", crCompound));
		strB.append(getTOXml("crCompoundCalcFreq", crCompoundCalcFreq));
		strB.append(getTOXml("crProductRoundoff", crProductRoundoff));
		strB.append(getTOXml("crIntRoundoff", crIntRoundoff));
		strB.append(getTOXml("startdayProdCalc", startdayProdCalc));
		strB.append(getTOXml("enddayProdCalc", enddayProdCalc));
		strB.append(getTOXml("calcCriteria", calcCriteria));
		strB.append(getTOXml("productFreqIntPay", productFreqIntPay));
		strB.append(getTOXml("creditIntGiven", creditIntGiven));
		strB.append(getTOXml("startdayIntCalc", startdayIntCalc));
		strB.append(getTOXml("enddayIntCalc", enddayIntCalc));
		strB.append(getTOXml("startmonProdCalc", startmonProdCalc));
		strB.append(getTOXml("endmonProdCalc", endmonProdCalc));
                strB.append(getTOXml("minBalforIntCalc", minBalforIntCalc));
		strB.append(getTOXmlEnd());
		return strB.toString();
	}

        /**
         * Getter for property minBalforIntCalc.
         * @return Value of property minBalforIntCalc.
         */
        public java.lang.Double getMinBalforIntCalc() {
            return minBalforIntCalc;
        }
        
        /**
         * Setter for property minBalforIntCalc.
         * @param minBalforIntCalc New value of property minBalforIntCalc.
         */
        public void setMinBalforIntCalc(java.lang.Double minBalforIntCalc) {
            this.minBalforIntCalc = minBalforIntCalc;
        }
        
}