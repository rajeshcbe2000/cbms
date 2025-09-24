/*
 * Copyright 2013 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of Fincuro Solutions (P) Ltd..
 * 
 *
 * LoanSalaryRecoveryListDetailTO.java
 * 
 * Created on Fri jan 22 16:26:25 IST 2012
 */
package com.see.truetransact.transferobject.termloan.loanrecovery;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO SalaryRecoveryListDetail.
 */
public class LoanSalaryRecoveryListDetailTO extends TransferObject implements Serializable {

    private Date intCalcUptoDt = null;
    private String empRefNo = "";
    private String memberName = "";
    private String schemeName = "";
    private String actNum = "";
    private String prod_ID = "";
    private String prod_Type = "";
    private Double totalDemand = null;
    private Double principal = null;
    private Double interest = null;
    private Double penal = null;
    private Double charges = null;
    private String status = "";
    private String particulars = "";
    private String recoveryType = "";
    private String recoveryId = "";
    private Double mdsBonus = 0.0;
    private Integer instNo = 0;
    private Double forfeitBonus = 0.0;

    /**
     * Setter/Getter for INT_CALC_UPTO_DT - table Field
     */
    public void setIntCalcUptoDt(Date intCalcUptoDt) {
        this.intCalcUptoDt = intCalcUptoDt;
    }

    public Date getIntCalcUptoDt() {
        return intCalcUptoDt;
    }

    /**
     * Setter/Getter for EMP_REF_NO - table Field
     */
    public void setEmpRefNo(String empRefNo) {
        this.empRefNo = empRefNo;
    }

    public String getEmpRefNo() {
        return empRefNo;
    }

    /**
     * Setter/Getter for MEMBER_NAME - table Field
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName() {
        return memberName;
    }

    /**
     * Setter/Getter for SCHEME_NAME - table Field
     */
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSchemeName() {
        return schemeName;
    }

    /**
     * Setter/Getter for ACT_NUM - table Field
     */
    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getActNum() {
        return actNum;
    }

    /**
     * Setter/Getter for TOTAL_DEMAND - table Field
     */
    public void setTotalDemand(Double totalDemand) {
        this.totalDemand = totalDemand;
    }

    public Double getTotalDemand() {
        return totalDemand;
    }

    /**
     * Setter/Getter for PRINCIPAL - table Field
     */
    public void setPrincipal(Double principal) {
        this.principal = principal;
    }

    public Double getPrincipal() {
        return principal;
    }

    /**
     * Setter/Getter for INTEREST - table Field
     */
    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Double getInterest() {
        return interest;
    }

    /**
     * Setter/Getter for PENAL - table Field
     */
    public void setPenal(Double penal) {
        this.penal = penal;
    }

    public Double getPenal() {
        return penal;
    }

    /**
     * Setter/Getter for CHARGES - table Field
     */
    public void setCharges(Double charges) {
        this.charges = charges;
    }

    public Double getCharges() {
        return charges;
    }

    public String getRecoveryId() {
        return recoveryId;
    }

    public void setRecoveryId(String recoveryId) {
        this.recoveryId = recoveryId;
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
        strB.append(getTOString("intCalcUptoDt", intCalcUptoDt));
        strB.append(getTOString("empRefNo", empRefNo));
        strB.append(getTOString("memberName", memberName));
        strB.append(getTOString("schemeName", schemeName));
        strB.append(getTOString("prod_ID", prod_ID));
        strB.append(getTOString("prod_Type", prod_Type));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("totalDemand", totalDemand));
        strB.append(getTOString("principal", principal));
        strB.append(getTOString("interest", interest));
        strB.append(getTOString("penal", penal));
        strB.append(getTOString("charges", charges));
        strB.append(getTOString("status", status));
        strB.append(getTOString("recoveryType", recoveryType));;
        strB.append(getTOString("recoveryId", recoveryId));
        strB.append(getTOString("mdsBonus", mdsBonus));
        strB.append(getTOString("instNo", instNo));
        strB.append(getTOString("forfeitBonus", forfeitBonus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("intCalcUptoDt", intCalcUptoDt));
        strB.append(getTOXml("empRefNo", empRefNo));
        strB.append(getTOXml("memberName", memberName));
        strB.append(getTOXml("schemeName", schemeName));
        strB.append(getTOXml("prod_ID", prod_ID));
        strB.append(getTOXml("prod_Type", prod_Type));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("totalDemand", totalDemand));
        strB.append(getTOXml("principal", principal));
        strB.append(getTOXml("interest", interest));
        strB.append(getTOXml("penal", penal));
        strB.append(getTOXml("charges", charges));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("recoveryType", recoveryType));
        strB.append(getTOXml("recoveryId", recoveryId));
        strB.append(getTOXml("mdsBonus", mdsBonus));
        strB.append(getTOXml("instNo", instNo));
        strB.append(getTOXml("forfeitBonus", forfeitBonus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property prod_ID.
     *
     * @return Value of property prod_ID.
     */
    public java.lang.String getProd_ID() {
        return prod_ID;
    }

    /**
     * Setter for property prod_ID.
     *
     * @param prod_ID New value of property prod_ID.
     */
    public void setProd_ID(java.lang.String prod_ID) {
        this.prod_ID = prod_ID;
    }

    /**
     * Getter for property prod_Type.
     *
     * @return Value of property prod_Type.
     */
    public java.lang.String getProd_Type() {
        return prod_Type;
    }

    /**
     * Setter for property prod_Type.
     *
     * @param prod_Type New value of property prod_Type.
     */
    public void setProd_Type(java.lang.String prod_Type) {
        this.prod_Type = prod_Type;
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
     * Getter for property particulars.
     *
     * @return Value of property particulars.
     */
    public String getParticulars() {
        return particulars;
    }

    /**
     * Setter for property particulars.
     *
     * @param particulars New value of property particulars.
     */
    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getRecoveryType() {
        return recoveryType;
    }

    public void setRecoveryType(String recoveryType) {
        this.recoveryType = recoveryType;
    }

    public Double getMdsBonus() {
        return mdsBonus;
    }

    public void setMdsBonus(Double mdsBonus) {
        this.mdsBonus = mdsBonus;
    }

    public Integer getInstNo() {
        return instNo;
    }

    public void setInstNo(Integer instNo) {
        this.instNo = instNo;
    }

    public Double getForfeitBonus() {
        return forfeitBonus;
    }

    public void setForfeitBonus(Double forfeitBonus) {
        this.forfeitBonus = forfeitBonus;
    }
    
}