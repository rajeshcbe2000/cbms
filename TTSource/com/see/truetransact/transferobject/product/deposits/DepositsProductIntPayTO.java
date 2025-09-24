/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositsProductIntPayTO.java
 *
 * Created on Tue Jul 06 15:15:30 IST 2004
 */
package com.see.truetransact.transferobject.product.deposits;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSITS_PROD_INTPAY.
 */
public class DepositsProductIntPayTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String intMaintainPart = "";
    private Double intCalcMethod = null;
    private String intProvAppl = "";
    private Double intProvFreq = null;
    private String provLevel = "";
    private String categoryBenifitRate = "";
    private Double noDaysYear = null;
    private Double intCompFreq = null;
    private Double intApplFreq = null;
    private Date lastIntProvdt = null;
    private Date nextIntProvdt = null;
    private Date lastIntAppldt = null;
    private Date nextIntAppldt = null;
    private String intRoundoffTerms = "";
    private String prematClosSIRat="";
    private String slabWiseInterest = "";
    private String slabWiseCommision = "";
    private String intRateEditable = "N";
    private String preMatIntType = "";

    public String getPreMatIntType() {
        return preMatIntType;
    }

    public void setPreMatIntType(String preMatIntType) {
        this.preMatIntType = preMatIntType;
    }

    public String getIntRateEditable() {
        return intRateEditable;
    }

    public void setIntRateEditable(String intRateEditable) {
        this.intRateEditable = intRateEditable;
    }
    
    
    public String getSlabWiseCommision() {
        return slabWiseCommision;
    }

    public void setSlabWiseCommision(String slabWiseCommision) {
        this.slabWiseCommision = slabWiseCommision;
    }

    public String getSlabWiseInterest() {
        return slabWiseInterest;
    }

    public void setSlabWiseInterest(String slabWiseInterest) {
        this.slabWiseInterest = slabWiseInterest;
    }
   

    public String getPrematClosSIRat() {
        return prematClosSIRat;
    }

    public void setPrematClosSIRat(String prematClosSIRat) {
        this.prematClosSIRat = prematClosSIRat;
    }

    public String getCbxInterstRoundTime() {

        return cbxInterstRoundTime;
    }

    public void setCbxInterstRoundTime(String cbxInterstRoundTime) {
        this.cbxInterstRoundTime = cbxInterstRoundTime;
    }
    private Double minIntPaid = null;
    private String intType = "";
    private String preMatureClosureApply = "";
    private String fixedDepositProduct = "";
    private String chkROI = "";
    private String cbxInterstRoundTime = "";
    private String appNormRate = "";

    public String getAppNormRate() {
        return appNormRate;
    }

    public void setAppNormRate(String appNormRate) {
        this.appNormRate = appNormRate;
    }
    private Integer normPeriod;

    public Integer getNormPeriod() {
        return normPeriod;
    }

    public void setNormPeriod(Integer normPeriod) {
        this.normPeriod = normPeriod;
    }

    public String getCategoryBenifitRate() {
        return categoryBenifitRate;
    }

    public void setCategoryBenifitRate(String categoryBenifitRate) {
        this.categoryBenifitRate = categoryBenifitRate;
    }

    public String getChkROI() {
        return chkROI;
    }

    public void setChkROI(String chkROI) {
        this.chkROI = chkROI;
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
     * Setter/Getter for INT_MAINTAIN_PART - table Field
     */
    public void setIntMaintainPart(String intMaintainPart) {
        this.intMaintainPart = intMaintainPart;
    }

    public String getIntMaintainPart() {
        return intMaintainPart;
    }

    /**
     * Setter/Getter for INT_CALC_METHOD - table Field
     */
    public void setIntCalcMethod(Double intCalcMethod) {
        this.intCalcMethod = intCalcMethod;
    }

    public Double getIntCalcMethod() {
        return intCalcMethod;
    }

    /**
     * Setter/Getter for INT_PROV_APPL - table Field
     */
    public void setIntProvAppl(String intProvAppl) {
        this.intProvAppl = intProvAppl;
    }

    public String getIntProvAppl() {
        return intProvAppl;
    }

    /**
     * Setter/Getter for INT_PROV_FREQ - table Field
     */
    public void setIntProvFreq(Double intProvFreq) {
        this.intProvFreq = intProvFreq;
    }

    public Double getIntProvFreq() {
        return intProvFreq;
    }

    /**
     * Setter/Getter for PROV_LEVEL - table Field
     */
    public void setProvLevel(String provLevel) {
        this.provLevel = provLevel;
    }

    public String getProvLevel() {
        return provLevel;
    }

    /**
     * Setter/Getter for NO_DAYS_YEAR - table Field
     */
    public void setNoDaysYear(Double noDaysYear) {
        this.noDaysYear = noDaysYear;
    }

    public Double getNoDaysYear() {
        return noDaysYear;
    }

    /**
     * Setter/Getter for INT_COMP_FREQ - table Field
     */
    public void setIntCompFreq(Double intCompFreq) {
        this.intCompFreq = intCompFreq;
    }

    public Double getIntCompFreq() {
        return intCompFreq;
    }

    /**
     * Setter/Getter for INT_APPL_FREQ - table Field
     */
    public void setIntApplFreq(Double intApplFreq) {
        this.intApplFreq = intApplFreq;
    }

    public Double getIntApplFreq() {
        return intApplFreq;
    }

    /**
     * Setter/Getter for LAST_INT_PROVDT - table Field
     */
    public void setLastIntProvdt(Date lastIntProvdt) {
        this.lastIntProvdt = lastIntProvdt;
    }

    public Date getLastIntProvdt() {
        return lastIntProvdt;
    }

    /**
     * Setter/Getter for NEXT_INT_PROVDT - table Field
     */
    public void setNextIntProvdt(Date nextIntProvdt) {
        this.nextIntProvdt = nextIntProvdt;
    }

    public Date getNextIntProvdt() {
        return nextIntProvdt;
    }

    /**
     * Setter/Getter for LAST_INT_APPLDT - table Field
     */
    public void setLastIntAppldt(Date lastIntAppldt) {
        this.lastIntAppldt = lastIntAppldt;
    }

    public Date getLastIntAppldt() {
        return lastIntAppldt;
    }

    /**
     * Setter/Getter for NEXT_INT_APPLDT - table Field
     */
    public void setNextIntAppldt(Date nextIntAppldt) {
        this.nextIntAppldt = nextIntAppldt;
    }

    public Date getNextIntAppldt() {
        return nextIntAppldt;
    }

    /**
     * Setter/Getter for INT_ROUNDOFF_TERMS - table Field
     */
    public void setIntRoundoffTerms(String intRoundoffTerms) {
        this.intRoundoffTerms = intRoundoffTerms;
    }

    public String getIntRoundoffTerms() {
        return intRoundoffTerms;
    }

    /**
     * Setter/Getter for MIN_INT_PAID - table Field
     */
    public void setMinIntPaid(Double minIntPaid) {
        this.minIntPaid = minIntPaid;
    }

    public Double getMinIntPaid() {
        return minIntPaid;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("prodId" + KEY_VAL_SEPARATOR + "intMaintainPart");
        return prodId + KEY_VAL_SEPARATOR + intMaintainPart;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("categoryBenifitRate", categoryBenifitRate));
        strB.append(getTOString("intMaintainPart", intMaintainPart));
        strB.append(getTOString("intCalcMethod", intCalcMethod));
        strB.append(getTOString("intProvAppl", intProvAppl));
        strB.append(getTOString("intProvFreq", intProvFreq));
        strB.append(getTOString("provLevel", provLevel));
        strB.append(getTOString("noDaysYear", noDaysYear));
        strB.append(getTOString("intCompFreq", intCompFreq));
        strB.append(getTOString("intApplFreq", intApplFreq));
        strB.append(getTOString("lastIntProvdt", lastIntProvdt));
        strB.append(getTOString("nextIntProvdt", nextIntProvdt));
        strB.append(getTOString("lastIntAppldt", lastIntAppldt));
        strB.append(getTOString("nextIntAppldt", nextIntAppldt));
        strB.append(getTOString("intRoundoffTerms", intRoundoffTerms));
        strB.append(getTOString("minIntPaid", minIntPaid));
        strB.append(getTOString("intType", intType));
        strB.append(getTOString("preMatureClosureApply", preMatureClosureApply));
        strB.append(getTOString("chkROI", chkROI));
        strB.append(getTOString("appNormRate", appNormRate));
        strB.append(getTOString("normPeriod",normPeriod ));
        strB.append(getTOString("slabWiseInterest",slabWiseInterest ));
        strB.append(getTOString("slabWiseCommision",slabWiseCommision));
        strB.append(getTOString("intRateEditable",intRateEditable)); 
        strB.append(getTOString("preMatIntType",preMatIntType)); 
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("categoryBenifitRate", categoryBenifitRate));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("intMaintainPart", intMaintainPart));
        strB.append(getTOXml("intCalcMethod", intCalcMethod));
        strB.append(getTOXml("intProvAppl", intProvAppl));
        strB.append(getTOXml("intProvFreq", intProvFreq));
        strB.append(getTOXml("provLevel", provLevel));
        strB.append(getTOXml("noDaysYear", noDaysYear));
        strB.append(getTOXml("intCompFreq", intCompFreq));
        strB.append(getTOXml("intApplFreq", intApplFreq));
        strB.append(getTOXml("lastIntProvdt", lastIntProvdt));
        strB.append(getTOXml("nextIntProvdt", nextIntProvdt));
        strB.append(getTOXml("lastIntAppldt", lastIntAppldt));
        strB.append(getTOXml("nextIntAppldt", nextIntAppldt));
        strB.append(getTOXml("intRoundoffTerms", intRoundoffTerms));
        strB.append(getTOXml("minIntPaid", minIntPaid));
        strB.append(getTOXml("intType", intType));
        strB.append(getTOXml("preMatureClosureApply", preMatureClosureApply));
        strB.append(getTOXml("chkROI", chkROI));
        strB.append(getTOXml("appNormRate", appNormRate));
        strB.append(getTOXml("normPeriod", normPeriod));
        strB.append(getTOXml("slabWiseInterest", slabWiseInterest));
        strB.append(getTOXml("slabWiseCommision",slabWiseCommision));
        strB.append(getTOXml("intRateEditable",intRateEditable)); 
        strB.append(getTOXml("preMatIntType",preMatIntType)); 
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property preMatureClosureApply.
     *
     * @return Value of property preMatureClosureApply.
     */
    public java.lang.String getPreMatureClosureApply() {
        return preMatureClosureApply;
    }

    /**
     * Setter for property preMatureClosureApply.
     *
     * @param preMatureClosureApply New value of property preMatureClosureApply.
     */
    public void setPreMatureClosureApply(java.lang.String preMatureClosureApply) {
        this.preMatureClosureApply = preMatureClosureApply;
    }

    /**
     * Getter for property fixedDepositProduct.
     *
     * @return Value of property fixedDepositProduct.
     */
    public String getFixedDepositProduct() {
        return fixedDepositProduct;
    }

    /**
     * Setter for property fixedDepositProduct.
     *
     * @param fixedDepositProduct New value of property fixedDepositProduct.
     */
    public void setFixedDepositProduct(String fixedDepositProduct) {
        this.fixedDepositProduct = fixedDepositProduct;
    }
}