/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryRecoveryListDetailTO.java
 * 
 * Created on Fri Jun 22 16:26:25 IST 2012
 */
package com.see.truetransact.transferobject.transexception;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is SALARY_RECOVERY_LIST_DETAIL.
 */
public class TransExceptionTO extends TransferObject implements Serializable {

    private Date intCalcUptoDt = null;
    private String empRefNo = "";
    private String memberName = "";
    private String schemeName = "";
    private String actNum = "";
    private String prod_ID = "";
    private String prod_Type = "";
    private Double totalDemand = 0.0;
    private Double principal = 0.0;
    private Double interest = 0.0;
    private Double penal = 0.0;
    private Double charges = 0.0;
    private Double penalMonth = 0.0;
    private String status = "";
    private Date instDate = null;
    private String cust_id = null;
    private String transId = null;
    private Integer slno = 0;
    private Double bonus = 0.0;
    private Double actualDemand = 0.0;

    public Double getActualDemand() {
        return actualDemand;
    }

    public void setActualDemand(Double actualDemand) {
        this.actualDemand = actualDemand;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

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
        strB.append(getTOString("penalMonth", penalMonth));
        strB.append(getTOString("status", status));
        strB.append(getTOString("instDate", instDate));
        strB.append(getTOString("cust_id", cust_id));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("slno", slno));
        strB.append(getTOString("bonus", bonus));
        strB.append(getTOString("actualDemand", actualDemand));
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
        strB.append(getTOXml("penalMonth", penalMonth));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("instDate", instDate));
        strB.append(getTOXml("cust_id", cust_id));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXml("bonus", bonus));
        strB.append(getTOXml("actualDemand", actualDemand));
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
     * Getter for property penalMonth.
     *
     * @return Value of property penalMonth.
     */
    public java.lang.Double getPenalMonth() {
        return penalMonth;
    }

    /**
     * Setter for property penalMonth.
     *
     * @param penalMonth New value of property penalMonth.
     */
    public void setPenalMonth(java.lang.Double penalMonth) {
        this.penalMonth = penalMonth;
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
     * Getter for property instDate.
     *
     * @return Value of property instDate.
     */
    public Date getInstDate() {
        return instDate;
    }

    /**
     * Setter for property instDate.
     *
     * @param instDate New value of property instDate.
     */
    public void setInstDate(Date instDate) {
        this.instDate = instDate;
    }

    /**
     * Getter for property cust_id.
     *
     * @return Value of property cust_id.
     */
    public String getCust_id() {
        return cust_id;
    }

    /**
     * Setter for property cust_id.
     *
     * @param cust_id New value of property cust_id.
     */
    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    /**
     * Getter for property transId.
     *
     * @return Value of property transId.
     */
    public String getTransId() {
        return transId;
    }

    /**
     * Setter for property transId.
     *
     * @param transId New value of property transId.
     */
    public void setTransId(String transId) {
        this.transId = transId;
    }

    /**
     * Getter for property slno.
     *
     * @return Value of property slno.
     */
    public Integer getSlno() {
        return slno;
    }

    /**
     * Setter for property slno.
     *
     * @param slno New value of property slno.
     */
    public void setSlno(Integer slno) {
        this.slno = slno;
    }
}