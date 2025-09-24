/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositsProductRDTO.java
 *
 * Created on Wed Jul 07 17:49:58 IST 2004
 */
package com.see.truetransact.transferobject.product.deposits;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceRateTO;

/**
 * Table name for this TO is DEPOSITS_PROD_RD.
 */
public class DepositsProductRDTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String lateInstallAllowed = "";
    private String penaltyLateInstall = "";
    private String insBeyondMaturityDt = "";
    private Double maturityDtLastinstall = null;
    private Double agentCommision = null;
    private Double minimumPeriod = null;
    private String convertRdToFixed = "";
    private String installRecurringDepac = "";
    private Double installCharged = null;
    private String changeValue = "";
    private String receiveExcessInstall = "";
    private String intpayExcessInstall = "";
    private Double cutoffPayInstall = null;
    private Date fromDepositDt = null;
    private Double lastDayMonth = null;
    private Date chosenDt = null;
    private String beyondOriginal = "";
    private String cutoffType = "";
    private String agentCommisionMode = "";
    private Double txtInterestNotPayingValue = null;
    private String cboInterestNotPayingValue = "";
    private String extensionPenal = "";
    //added for daily deposit scheme
    private Integer depositFreq = new Integer(0);
    private String txtRDIrregularIfInstallmentDue = "";
    private String rdoIncaseOfIrregular = "";
    private String intAppSlab = "";
    private String rdoPenalRound = "";
    private String cboPenalRound = "";
    private String chkWeeklySpec = "";
    private String inclFullMonth = "";
    private String cboRdSbProduct = "";
    private String chkRdNature ="";
    private Integer gracePeriod = 0;
    
    private String rdCloseOtherProdROI = "";
    private String prmatureCloseRate = "";
    private String prmatureCloseProd = "";
    private String irregulareCloseRate = "";
    private String irregularCloseProduct = "";
  
    private String applyIntForIrregularRD = "Y";// Added by nithya on 18-03-2020 for KD-1535
    private String specialRD = "N";// Added by nithya on 18-03-2020 for KD-1535
    private Integer noOfSpecialRDInstallments = 0; // Added by nithya on 01-04-2020 for KD-1535
   

    public String getCboRdSbProduct() {
        return cboRdSbProduct;
    }

    public void setCboRdSbProduct(String cboRdSbProduct) {
        this.cboRdSbProduct = cboRdSbProduct;
    }

    
    public String getInclFullMonth() {
        return inclFullMonth;
    }

    public void setInclFullMonth(String inclFullMonth) {
        this.inclFullMonth = inclFullMonth;
    }

    public String getChkRdNature() {
        return chkRdNature;
    }

    public void setChkRdNature(String chkRdNature) {
        this.chkRdNature = chkRdNature;
    }

    public String getChkWeeklySpec() {
        return chkWeeklySpec;
    }

    public void setChkWeeklySpec(String chkWeeklySpec) {
        this.chkWeeklySpec = chkWeeklySpec;
    }
    public String getCboPenalRound() {
        return cboPenalRound;
    }

    public void setCboPenalRound(String cboPenalRound) {
        this.cboPenalRound = cboPenalRound;
    }

    public String getRdoPenalRound() {
        return rdoPenalRound;
    }

    public void setRdoPenalRound(String rdoPenalRound) {
        this.rdoPenalRound = rdoPenalRound;
    }

    public String getIntAppSlab() {
        return intAppSlab;
    }

    public void setIntAppSlab(String intAppSlab) {
        this.intAppSlab = intAppSlab;
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
     * Setter/Getter for LATE_INSTALL_ALLOWED - table Field
     */
    public void setLateInstallAllowed(String lateInstallAllowed) {
        this.lateInstallAllowed = lateInstallAllowed;
    }

    public String getLateInstallAllowed() {
        return lateInstallAllowed;
    }

    /**
     * Setter/Getter for PENALTY_LATE_INSTALL - table Field
     */
    public void setPenaltyLateInstall(String penaltyLateInstall) {
        this.penaltyLateInstall = penaltyLateInstall;
    }

    public String getPenaltyLateInstall() {
        return penaltyLateInstall;
    }

    /**
     * Setter/Getter for MATURITY_DT_LASTINSTALL - table Field
     */
    public void setMaturityDtLastinstall(Double maturityDtLastinstall) {
        this.maturityDtLastinstall = maturityDtLastinstall;
    }

    public Double getMaturityDtLastinstall() {
        return maturityDtLastinstall;
    }

    /**
     * Setter/Getter for CONVERT_RD_TO_FIXED - table Field
     */
    public void setConvertRdToFixed(String convertRdToFixed) {
        this.convertRdToFixed = convertRdToFixed;
    }

    public String getConvertRdToFixed() {
        return convertRdToFixed;
    }

    /**
     * Setter/Getter for INSTALL_RECURRING_DEPAC - table Field
     */
    public void setInstallRecurringDepac(String installRecurringDepac) {
        this.installRecurringDepac = installRecurringDepac;
    }

    public String getInstallRecurringDepac() {
        return installRecurringDepac;
    }

    /**
     * Setter/Getter for INSTALL_CHARGED - table Field
     */
    public void setInstallCharged(Double installCharged) {
        this.installCharged = installCharged;
    }

    public Double getInstallCharged() {
        return installCharged;
    }

    /**
     * Setter/Getter for CHANGE_VALUE - table Field
     */
    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }

    public String getChangeValue() {
        return changeValue;
    }

    /**
     * Setter/Getter for RECEIVE_EXCESS_INSTALL - table Field
     */
    public void setReceiveExcessInstall(String receiveExcessInstall) {
        this.receiveExcessInstall = receiveExcessInstall;
    }

    public String getReceiveExcessInstall() {
        return receiveExcessInstall;
    }

    /**
     * Setter/Getter for INTPAY_EXCESS_INSTALL - table Field
     */
    public void setIntpayExcessInstall(String intpayExcessInstall) {
        this.intpayExcessInstall = intpayExcessInstall;
    }

    public String getIntpayExcessInstall() {
        return intpayExcessInstall;
    }

    /**
     * Setter/Getter for CUTOFF_PAY_INSTALL - table Field
     */
    public void setCutoffPayInstall(Double cutoffPayInstall) {
        this.cutoffPayInstall = cutoffPayInstall;
    }

    public Double getCutoffPayInstall() {
        return cutoffPayInstall;
    }

    /**
     * Setter/Getter for FROM_DEPOSIT_DT - table Field
     */
    public void setFromDepositDt(Date fromDepositDt) {
        this.fromDepositDt = fromDepositDt;
    }

    public Date getFromDepositDt() {
        return fromDepositDt;
    }

    /**
     * Setter/Getter for LAST_DAY_MONTH - table Field
     */
    public void setLastDayMonth(Double lastDayMonth) {
        this.lastDayMonth = lastDayMonth;
    }

    public Double getLastDayMonth() {
        return lastDayMonth;
    }

    /**
     * Setter/Getter for CHOSEN_DT - table Field
     */
    public void setChosenDt(Date chosenDt) {
        this.chosenDt = chosenDt;
    }

    public Date getChosenDt() {
        return chosenDt;
    }

    /**
     * Setter/Getter for CUTOFF_TYPE - table Field
     */
    public void setCutoffType(String cutoffType) {
        this.cutoffType = cutoffType;
    }

    public String getCutoffType() {
        return cutoffType;
    }

    public java.lang.String getBeyondOriginal() {
        return beyondOriginal;
    }

    /**
     * Setter for property beyondOriginal.
     *
     * @param beyondOriginal New value of property beyondOriginal.
     */
    public void setBeyondOriginal(java.lang.String beyondOriginal) {
        this.beyondOriginal = beyondOriginal;
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

    //added by Shany
    public Integer getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(Integer gracePeriod) {
        this.gracePeriod = gracePeriod;
    }
    
    

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("lateInstallAllowed", lateInstallAllowed));
        strB.append(getTOString("penaltyLateInstall", penaltyLateInstall));
        strB.append(getTOString("insBeyondMaturityDt", insBeyondMaturityDt));
        strB.append(getTOString("maturityDtLastinstall", maturityDtLastinstall));
        strB.append(getTOString("convertRdToFixed", convertRdToFixed));
        strB.append(getTOString("installRecurringDepac", installRecurringDepac));
        strB.append(getTOString("installCharged", installCharged));
        strB.append(getTOString("changeValue", changeValue));
        strB.append(getTOString("receiveExcessInstall", receiveExcessInstall));
        strB.append(getTOString("intpayExcessInstall", intpayExcessInstall));
        strB.append(getTOString("cutoffPayInstall", cutoffPayInstall));
        strB.append(getTOString("fromDepositDt", fromDepositDt));
        strB.append(getTOString("lastDayMonth", lastDayMonth));
        strB.append(getTOString("chosenDt", chosenDt));
        strB.append(getTOString("cutoffType", cutoffType));
        strB.append(getTOString("depositFreq", depositFreq));
        strB.append(getTOString("agentCommision", agentCommision));
        strB.append(getTOString("minimumPeriod", minimumPeriod));
        strB.append(getTOString("agentCommisionMode", agentCommisionMode));
        strB.append(getTOString("txtInterestNotPayingValue", txtInterestNotPayingValue));
        strB.append(getTOString("cboInterestNotPayingValue", cboInterestNotPayingValue));
        strB.append(getTOString("beyondOriginal", beyondOriginal));
        strB.append(getTOString("extensionPenal", extensionPenal));
        strB.append(getTOString("txtRDIrregularIfInstallmentDue", txtRDIrregularIfInstallmentDue));
        strB.append(getTOString("rdoIncaseOfIrregular", rdoIncaseOfIrregular));
        strB.append(getTOString("intAppSlab", intAppSlab));
        strB.append(getTOString("chkWeeklySpec", chkWeeklySpec));
        strB.append(getTOString("chkRdNature", chkRdNature));
        strB.append(getTOString("inclFullMonth", inclFullMonth));
        strB.append(getTOString("cboRdSbProduct", cboRdSbProduct));
        strB.append(getTOString("gracePeriod", gracePeriod));
        strB.append(getTOString("applyIntForIrregularRD", applyIntForIrregularRD));
        strB.append(getTOString("specialRD", specialRD));
        strB.append(getTOString("noOfSpecialRDInstallments", noOfSpecialRDInstallments));
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
        strB.append(getTOXml("lateInstallAllowed", lateInstallAllowed));
        strB.append(getTOXml("penaltyLateInstall", penaltyLateInstall));
        strB.append(getTOXml("insBeyondMaturityDt", insBeyondMaturityDt));
        strB.append(getTOXml("maturityDtLastinstall", maturityDtLastinstall));
        strB.append(getTOXml("convertRdToFixed", convertRdToFixed));
        strB.append(getTOXml("installRecurringDepac", installRecurringDepac));
        strB.append(getTOXml("installCharged", installCharged));
        strB.append(getTOXml("changeValue", changeValue));
        strB.append(getTOXml("receiveExcessInstall", receiveExcessInstall));
        strB.append(getTOXml("intpayExcessInstall", intpayExcessInstall));
        strB.append(getTOXml("cutoffPayInstall", cutoffPayInstall));
        strB.append(getTOXml("fromDepositDt", fromDepositDt));
        strB.append(getTOXml("lastDayMonth", lastDayMonth));
        strB.append(getTOXml("chosenDt", chosenDt));
        strB.append(getTOXml("cutoffType", cutoffType));
        strB.append(getTOXml("depositFreq", depositFreq));
        strB.append(getTOXml("agentCommision", agentCommision));
        strB.append(getTOXml("minimumPeriod", minimumPeriod));
        strB.append(getTOXml("agentCommisionMode", agentCommisionMode));
        strB.append(getTOXml("txtInterestNotPayingValue", txtInterestNotPayingValue));
        strB.append(getTOXml("cboInterestNotPayingValue", cboInterestNotPayingValue));
        strB.append(getTOXml("beyondOriginal", beyondOriginal));
        strB.append(getTOXml("extensionPenal", extensionPenal));
        strB.append(getTOXml("txtRDIrregularIfInstallmentDue", txtRDIrregularIfInstallmentDue));
        strB.append(getTOXml("rdoIncaseOfIrregular", rdoIncaseOfIrregular));
        strB.append(getTOXml("intAppSlab", intAppSlab));
        strB.append(getTOXml("chkWeeklySpec", chkWeeklySpec));
        strB.append(getTOXml("chkRdNature", chkRdNature));
		strB.append(getTOXml("inclFullMonth", inclFullMonth));
        strB.append(getTOXml("cboRdSbProduct", cboRdSbProduct));
        strB.append(getTOXml("gracePeriod", gracePeriod));
        strB.append(getTOXml("applyIntForIrregularRD", applyIntForIrregularRD));
        strB.append(getTOXml("specialRD", specialRD));
        strB.append(getTOXml("noOfSpecialRDInstallments", noOfSpecialRDInstallments));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public Integer getDepositFreq() {
        return depositFreq;
    }

    public void setDepositFreq(Integer depositFreq) {
        this.depositFreq = depositFreq;
    }

    

   

    /**
     * Getter for property agentCommision.
     *
     * @return Value of property agentCommision.
     */
    public java.lang.Double getAgentCommision() {
        return agentCommision;
    }

    /**
     * Setter for property agentCommision.
     *
     * @param agentCommision New value of property agentCommision.
     */
    public void setAgentCommision(java.lang.Double agentCommision) {
        this.agentCommision = agentCommision;
    }

    /**
     * Getter for property minimumPeriod.
     *
     * @return Value of property minimumPeriod.
     */
    public java.lang.Double getMinimumPeriod() {
        return minimumPeriod;
    }

    /**
     * Setter for property minimumPeriod.
     *
     * @param minimumPeriod New value of property minimumPeriod.
     */
    public void setMinimumPeriod(java.lang.Double minimumPeriod) {
        this.minimumPeriod = minimumPeriod;
    }

    /**
     * Getter for property agentCommisionMode.
     *
     * @return Value of property agentCommisionMode.
     */
    public java.lang.String getAgentCommisionMode() {
        return agentCommisionMode;
    }

    /**
     * Setter for property agentCommisionMode.
     *
     * @param agentCommisionMode New value of property agentCommisionMode.
     */
    public void setAgentCommisionMode(java.lang.String agentCommisionMode) {
        this.agentCommisionMode = agentCommisionMode;
    }

    /**
     * Getter for property txtInterestNotPayingValue.
     *
     * @return Value of property txtInterestNotPayingValue.
     */
    public java.lang.Double getTxtInterestNotPayingValue() {
        return txtInterestNotPayingValue;
    }

    public java.lang.String getExtensionPenal() {
        return extensionPenal;
    }

    /**
     * Setter for property extensionPenal.
     *
     * @param extensionPenal New value of property extensionPenal.
     */
    public void setExtensionPenal(java.lang.String extensionPenal) {
        this.extensionPenal = extensionPenal;
    }

    /**
     * Setter for property txtInterestNotPayingValue.
     *
     * @param txtInterestNotPayingValue New value of property
     * txtInterestNotPayingValue.
     */
    public void setTxtInterestNotPayingValue(java.lang.Double txtInterestNotPayingValue) {
        this.txtInterestNotPayingValue = txtInterestNotPayingValue;
    }

    /**
     * Getter for property cboInterestNotPayingValue.
     *
     * @return Value of property cboInterestNotPayingValue.
     */
    public java.lang.String getCboInterestNotPayingValue() {
        return cboInterestNotPayingValue;
    }

    /**
     * Setter for property cboInterestNotPayingValue.
     *
     * @param cboInterestNotPayingValue New value of property
     * cboInterestNotPayingValue.
     */
    public void setCboInterestNotPayingValue(java.lang.String cboInterestNotPayingValue) {
        this.cboInterestNotPayingValue = cboInterestNotPayingValue;
    }

    /**
     * Getter for property insBeyondMaturityDt.
     *
     * @return Value of property insBeyondMaturityDt.
     */
    public java.lang.String getInsBeyondMaturityDt() {
        return insBeyondMaturityDt;
    }

    /**
     * Setter for property insBeyondMaturityDt.
     *
     * @param insBeyondMaturityDt New value of property insBeyondMaturityDt.
     */
    public void setInsBeyondMaturityDt(java.lang.String insBeyondMaturityDt) {
        this.insBeyondMaturityDt = insBeyondMaturityDt;
    }

    /**
     * Getter for property txtRDIrregularIfInstallmentDue.
     *
     * @return Value of property txtRDIrregularIfInstallmentDue.
     */
    public java.lang.String getTxtRDIrregularIfInstallmentDue() {
        return txtRDIrregularIfInstallmentDue;
    }

    /**
     * Setter for property txtRDIrregularIfInstallmentDue.
     *
     * @param txtRDIrregularIfInstallmentDue New value of property
     * txtRDIrregularIfInstallmentDue.
     */
    public void setTxtRDIrregularIfInstallmentDue(java.lang.String txtRDIrregularIfInstallmentDue) {
        this.txtRDIrregularIfInstallmentDue = txtRDIrregularIfInstallmentDue;
    }

    /**
     * Getter for property rdoIncaseOfIrregular.
     *
     * @return Value of property rdoIncaseOfIrregular.
     */
    public java.lang.String getRdoIncaseOfIrregular() {
        return rdoIncaseOfIrregular;
    }

    /**
     * Setter for property rdoIncaseOfIrregular.
     *
     * @param rdoIncaseOfIrregular New value of property rdoIncaseOfIrregular.
     */
    public void setRdoIncaseOfIrregular(java.lang.String rdoIncaseOfIrregular) {
        this.rdoIncaseOfIrregular = rdoIncaseOfIrregular;
    }

    public String getIrregularCloseProduct() {
        return irregularCloseProduct;
    }

    public void setIrregularCloseProduct(String irregularCloseProduct) {
        this.irregularCloseProduct = irregularCloseProduct;
    }

    public String getIrregulareCloseRate() {
        return irregulareCloseRate;
    }

    public void setIrregulareCloseRate(String irregulareCloseRate) {
        this.irregulareCloseRate = irregulareCloseRate;
    }

    public String getPrmatureCloseProd() {
        return prmatureCloseProd;
    }

    public void setPrmatureCloseProd(String prmatureCloseProd) {
        this.prmatureCloseProd = prmatureCloseProd;
    }

    public String getPrmatureCloseRate() {
        return prmatureCloseRate;
    }

    public void setPrmatureCloseRate(String prmatureCloseRate) {
        this.prmatureCloseRate = prmatureCloseRate;
    }

    public String getRdCloseOtherProdROI() {
        return rdCloseOtherProdROI;
    }

    public void setRdCloseOtherProdROI(String rdCloseOtherProdROI) {
        this.rdCloseOtherProdROI = rdCloseOtherProdROI;
    }

    public String getApplyIntForIrregularRD() {
        return applyIntForIrregularRD;
    }

    public void setApplyIntForIrregularRD(String applyIntForIrregularRD) {
        this.applyIntForIrregularRD = applyIntForIrregularRD;
    }

    public String getSpecialRD() {
        return specialRD;
    }

    public void setSpecialRD(String specialRD) {
        this.specialRD = specialRD;
    }

    public Integer getNoOfSpecialRDInstallments() {
        return noOfSpecialRDInstallments;
    }

    public void setNoOfSpecialRDInstallments(Integer noOfSpecialRDInstallments) {
        this.noOfSpecialRDInstallments = noOfSpecialRDInstallments;
    }
       
    
}