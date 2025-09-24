/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * StandingInstructionTO.java
 * 
 * Created on Tue May 03 17:07:13 IST 2005
 */
package com.see.truetransact.transferobject.supporting.standinginstruction;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is STANDING_INSTRUCTION.
 */
public class StandingInstructionTO extends TransferObject implements Serializable {

    private String siId = "";
    private Date siDt = null;
    private String siType = "";
    private Double multiplesOf = null;
    private Double minBalance = null;
    private Date siStartDt = null;
    private Date siEndDt = null;
    private Date siSuspendDt = null;
    private String frequency = null;
    private Double graceDays = null;
    private String collectSiComm = "";
    private Double siCharges = null;
    private String collectRemitComm = "";
    private Double remitCharges = null;
    private String remitMode = "";
    private String branchCode = "";
    private String beneficiary = "";
    private String status = "";
    private String authorizeStatus = null;
    private Date authorizeDt = null;
    private String authorizeBy = "";
    private String authorizeRemark = "";
    private String weekDay = "";
    private String week = "";
    private Double specificDate = null;
    private String siHolidayExec = "";
    private String automaticPosting = "";
    private Double carriedForwardCount = null;
    private Double count = null;
    private Double execCharge = null;
    private Double failureCharge = null;
    private Double serviceTax = null;
    private Double failureServiceTax = null;
    private String execConfig = "";
    private Double acceptanceCharge = null;
    private String changeHolidayExec = "";
    private Date nextRunDt = null;
    private Date lastRunDt = null;
    private Date forwardRunDt = null;
    private Date execDtHoliday = null;
    private String statusBy = "";
    private Date statusDt = null;
    private Double siAgentComm=null;
    private Double loanBal = null;
    private String commisionAcHd = "";
    private String chkInstalment ="N";
    private Integer noOfInstalments =0;
    private String chkPendingInstalment ="N";

    public String getChkPendingInstalment() {
        return chkPendingInstalment;
    }

    public void setChkPendingInstalment(String chkPendingInstalment) {
        this.chkPendingInstalment = chkPendingInstalment;
    }
    
    public String getChkInstalment() {
        return chkInstalment;
    }

    public void setChkInstalment(String chkInstalment) {
        this.chkInstalment = chkInstalment;
    }

    public Integer getNoOfInstalments() {
        return noOfInstalments;
    }

    public void setNoOfInstalments(Integer noOfInstalments) {
        this.noOfInstalments = noOfInstalments;
    }
    
    public String getCommisionAcHd() {
        return commisionAcHd;
    }

    public void setCommisionAcHd(String commisionAcHd) {
        this.commisionAcHd = commisionAcHd;
    }
    
    public Double getLoanBal() {
        return loanBal;
    }

    public void setLoanBal(Double loanBal) {
        this.loanBal = loanBal;
    }
   

    /**
     * Setter/Getter for SI_ID - table Field
     */
    public void setSiId(String siId) {
        this.siId = siId;
    }

    public String getSiId() {
        return siId;
    }

    /**
     * Setter/Getter for SI_DT - table Field
     */
    public void setSiDt(Date siDt) {
        this.siDt = siDt;
    }

    public Date getSiDt() {
        return siDt;
    }

    /**
     * Setter/Getter for SI_TYPE - table Field
     */
    public void setSiType(String siType) {
        this.siType = siType;
    }

    public String getSiType() {
        return siType;
    }

    /**
     * Setter/Getter for MULTIPLES_OF - table Field
     */
    public void setMultiplesOf(Double multiplesOf) {
        this.multiplesOf = multiplesOf;
    }

    public Double getMultiplesOf() {
        return multiplesOf;
    }

    /**
     * Setter/Getter for MIN_BALANCE - table Field
     */
    public void setMinBalance(Double minBalance) {
        this.minBalance = minBalance;
    }

    public Double getMinBalance() {
        return minBalance;
    }

    /**
     * Setter/Getter for SI_START_DT - table Field
     */
    public void setSiStartDt(Date siStartDt) {
        this.siStartDt = siStartDt;
    }

    public Date getSiStartDt() {
        return siStartDt;
    }

    /**
     * Setter/Getter for SI_END_DT - table Field
     */
    public void setSiEndDt(Date siEndDt) {
        this.siEndDt = siEndDt;
    }

    public Date getSiEndDt() {
        return siEndDt;
    }

    /**
     * Setter/Getter for FREQUENCY - table Field
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFrequency() {
        return frequency;
    }

    /**
     * Setter/Getter for GRACE_DAYS - table Field
     */
    public void setGraceDays(Double graceDays) {
        this.graceDays = graceDays;
    }

    public Double getGraceDays() {
        return graceDays;
    }

    /**
     * Setter/Getter for COLLECT_SI_COMM - table Field
     */
    public void setCollectSiComm(String collectSiComm) {
        this.collectSiComm = collectSiComm;
    }

    public String getCollectSiComm() {
        return collectSiComm;
    }

    /**
     * Setter/Getter for SI_CHARGES - table Field
     */
    public void setSiCharges(Double siCharges) {
        this.siCharges = siCharges;
    }

    public Double getSiCharges() {
        return siCharges;
    }

    /**
     * Setter/Getter for COLLECT_REMIT_COMM - table Field
     */
    public void setCollectRemitComm(String collectRemitComm) {
        this.collectRemitComm = collectRemitComm;
    }

    public String getCollectRemitComm() {
        return collectRemitComm;
    }

    /**
     * Setter/Getter for REMIT_CHARGES - table Field
     */
    public void setRemitCharges(Double remitCharges) {
        this.remitCharges = remitCharges;
    }

    public Double getRemitCharges() {
        return remitCharges;
    }

    /**
     * Setter/Getter for REMIT_MODE - table Field
     */
    public void setRemitMode(String remitMode) {
        this.remitMode = remitMode;
    }

    public String getRemitMode() {
        return remitMode;
    }

    /**
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter/Getter for BENEFICIARY - table Field
     */
    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getBeneficiary() {
        return beneficiary;
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
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
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
     * Setter/Getter for AUTHORIZE_REMARK - table Field
     */
    public void setAuthorizeRemark(String authorizeRemark) {
        this.authorizeRemark = authorizeRemark;
    }

    public String getAuthorizeRemark() {
        return authorizeRemark;
    }

    /**
     * Setter/Getter for WEEK_DAY - table Field
     */
    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getWeekDay() {
        return weekDay;
    }

    /**
     * Setter/Getter for WEEK - table Field
     */
    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeek() {
        return week;
    }

    /**
     * Setter/Getter for SPECIFIC_DATE - table Field
     */
    public void setSpecificDate(Double specificDate) {
        this.specificDate = specificDate;
    }

    public Double getSpecificDate() {
        return specificDate;
    }

    /**
     * Setter/Getter for SI_HOLIDAY_EXEC - table Field
     */
    public void setSiHolidayExec(String siHolidayExec) {
        this.siHolidayExec = siHolidayExec;
    }

    public String getSiHolidayExec() {
        return siHolidayExec;
    }

    /**
     * Setter/Getter for AUTOMATIC_POSTING - table Field
     */
    public void setAutomaticPosting(String automaticPosting) {
        this.automaticPosting = automaticPosting;
    }

    public String getAutomaticPosting() {
        return automaticPosting;
    }

    /**
     * Setter/Getter for CARRIED_FORWARD_COUNT - table Field
     */
    public void setCarriedForwardCount(Double carriedForwardCount) {
        this.carriedForwardCount = carriedForwardCount;
    }

    public Double getCarriedForwardCount() {
        return carriedForwardCount;
    }

    /**
     * Setter/Getter for EXEC_CHARGE - table Field
     */
    public void setExecCharge(Double execCharge) {
        this.execCharge = execCharge;
    }

    public Double getExecCharge() {
        return execCharge;
    }

    /**
     * Setter/Getter for FAILURE_CHARGE - table Field
     */
    public void setFailureCharge(Double failureCharge) {
        this.failureCharge = failureCharge;
    }

    public Double getFailureCharge() {
        return failureCharge;
    }

    /**
     * Setter/Getter for EXEC_CONFIG - table Field
     */
    public void setExecConfig(String execConfig) {
        this.execConfig = execConfig;
    }

    public String getExecConfig() {
        return execConfig;
    }

    /**
     * Setter/Getter for ACCEPTANCE_CHARGE - table Field
     */
    public void setAcceptanceCharge(Double acceptanceCharge) {
        this.acceptanceCharge = acceptanceCharge;
    }

    public Double getAcceptanceCharge() {
        return acceptanceCharge;
    }

    /**
     * Setter/Getter for CHANGE_HOLIDAY_EXEC - table Field
     */
    public void setChangeHolidayExec(String changeHolidayExec) {
        this.changeHolidayExec = changeHolidayExec;
    }

    public String getChangeHolidayExec() {
        return changeHolidayExec;
    }

    /**
     * Setter/Getter for NEXT_RUN_DT - table Field
     */
    public void setNextRunDt(Date nextRunDt) {
        this.nextRunDt = nextRunDt;
    }

    public Date getNextRunDt() {
        return nextRunDt;
    }

    /**
     * Setter/Getter for LAST_RUN_DT - table Field
     */
    public void setLastRunDt(Date lastRunDt) {
        this.lastRunDt = lastRunDt;
    }

    public Date getLastRunDt() {
        return lastRunDt;
    }

    /**
     * Setter/Getter for FORWARD_RUN_DT - table Field
     */
    public void setForwardRunDt(Date forwardRunDt) {
        this.forwardRunDt = forwardRunDt;
    }

    public Date getForwardRunDt() {
        return forwardRunDt;
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
        strB.append(getTOString("siId", siId));
        strB.append(getTOString("siDt", siDt));
        strB.append(getTOString("siType", siType));
        strB.append(getTOString("multiplesOf", multiplesOf));
        strB.append(getTOString("minBalance", minBalance));
        strB.append(getTOString("siStartDt", siStartDt));
        strB.append(getTOString("siEndDt", siEndDt));
        strB.append(getTOString("frequency", frequency));
        strB.append(getTOString("graceDays", graceDays));
        strB.append(getTOString("collectSiComm", collectSiComm));
        strB.append(getTOString("siCharges", siCharges));
        strB.append(getTOString("collectRemitComm", collectRemitComm));
        strB.append(getTOString("remitCharges", remitCharges));
        strB.append(getTOString("remitMode", remitMode));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("beneficiary", beneficiary));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeRemark", authorizeRemark));
        strB.append(getTOString("weekDay", weekDay));
        strB.append(getTOString("week", week));
        strB.append(getTOString("specificDate", specificDate));
        strB.append(getTOString("siHolidayExec", siHolidayExec));
        strB.append(getTOString("automaticPosting", automaticPosting));
        strB.append(getTOString("carriedForwardCount", carriedForwardCount));
        strB.append(getTOString("count", count));
        strB.append(getTOString("execCharge", execCharge));
        strB.append(getTOString("failureCharge", failureCharge));
        strB.append(getTOString("serviceTax", serviceTax));
        strB.append(getTOString("execConfig", execConfig));
        strB.append(getTOString("acceptanceCharge", acceptanceCharge));
        strB.append(getTOString("changeHolidayExec", changeHolidayExec));
        strB.append(getTOString("nextRunDt", nextRunDt));
        strB.append(getTOString("lastRunDt", lastRunDt));
        strB.append(getTOString("forwardRunDt", forwardRunDt));
        strB.append(getTOString("execDtHoliday", execDtHoliday));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("failureServiceTax", failureServiceTax));
        strB.append(getTOString("siSuspendDt", siSuspendDt));
        strB.append(getTOString("siAgentComm", siAgentComm));
        strB.append(getTOString("loanBal", loanBal));
        strB.append(getTOString("commisionAcHd", commisionAcHd));
        strB.append(getTOString("noOfInstalments", noOfInstalments));
        strB.append(getTOString("chkInstalment", chkInstalment)); 
        strB.append(getTOString("chkPendingInstalment", chkPendingInstalment)); 
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("siId", siId));
        strB.append(getTOXml("siDt", siDt));
        strB.append(getTOXml("siType", siType));
        strB.append(getTOXml("multiplesOf", multiplesOf));
        strB.append(getTOXml("minBalance", minBalance));
        strB.append(getTOXml("siStartDt", siStartDt));
        strB.append(getTOXml("siEndDt", siEndDt));
        strB.append(getTOXml("frequency", frequency));
        strB.append(getTOXml("graceDays", graceDays));
        strB.append(getTOXml("collectSiComm", collectSiComm));
        strB.append(getTOXml("siCharges", siCharges));
        strB.append(getTOXml("collectRemitComm", collectRemitComm));
        strB.append(getTOXml("remitCharges", remitCharges));
        strB.append(getTOXml("remitMode", remitMode));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("beneficiary", beneficiary));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeRemark", authorizeRemark));
        strB.append(getTOXml("weekDay", weekDay));
        strB.append(getTOXml("week", week));
        strB.append(getTOXml("specificDate", specificDate));
        strB.append(getTOXml("siHolidayExec", siHolidayExec));
        strB.append(getTOXml("automaticPosting", automaticPosting));
        strB.append(getTOXml("carriedForwardCount", carriedForwardCount));
        strB.append(getTOXml("count", count));
        strB.append(getTOXml("execCharge", execCharge));
        strB.append(getTOXml("failureCharge", failureCharge));
        strB.append(getTOXml("serviceTax", serviceTax));
        strB.append(getTOXml("execConfig", execConfig));
        strB.append(getTOXml("acceptanceCharge", acceptanceCharge));
        strB.append(getTOXml("changeHolidayExec", changeHolidayExec));
        strB.append(getTOXml("nextRunDt", nextRunDt));
        strB.append(getTOXml("lastRunDt", lastRunDt));
        strB.append(getTOXml("forwardRunDt", forwardRunDt));
        strB.append(getTOXml("execDtHoliday", execDtHoliday));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("failureServiceTax", failureServiceTax));
        strB.append(getTOXml("siSuspendDt", siSuspendDt));
        strB.append(getTOXml("siAgentComm", siAgentComm));
        strB.append(getTOXml("loanBal", loanBal));
        strB.append(getTOXml("commisionAcHd", commisionAcHd));
        strB.append(getTOXml("noOfInstalments", noOfInstalments));
        strB.append(getTOXml("chkInstalment", chkInstalment));
        strB.append(getTOXml("chkPendingInstalment", chkPendingInstalment)); 
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property execDtHoliday.
     *
     * @return Value of property execDtHoliday.
     */
    public java.util.Date getExecDtHoliday() {
        return execDtHoliday;
    }

    /**
     * Setter for property execDtHoliday.
     *
     * @param execDtHoliday New value of property execDtHoliday.
     */
    public void setExecDtHoliday(java.util.Date execDtHoliday) {
        this.execDtHoliday = execDtHoliday;
    }

    /**
     * Getter for property count.
     *
     * @return Value of property count.
     */
    public java.lang.Double getCount() {
        return count;
    }

    /**
     * Setter for property count.
     *
     * @param count New value of property count.
     */
    public void setCount(java.lang.Double count) {
        this.count = count;
    }

    /**
     * Getter for property serviceTax.
     *
     * @return Value of property serviceTax.
     */
    public java.lang.Double getServiceTax() {
        return serviceTax;
    }

    /**
     * Setter for property serviceTax.
     *
     * @param serviceTax New value of property serviceTax.
     */
    public void setServiceTax(java.lang.Double serviceTax) {
        this.serviceTax = serviceTax;
    }

    /**
     * Getter for property failureServiceTax.
     *
     * @return Value of property failureServiceTax.
     */
    public java.lang.Double getFailureServiceTax() {
        return failureServiceTax;
    }

    /**
     * Setter for property failureServiceTax.
     *
     * @param failureServiceTax New value of property failureServiceTax.
     */
    public void setFailureServiceTax(java.lang.Double failureServiceTax) {
        this.failureServiceTax = failureServiceTax;
    }

    /**
     * Getter for property siSuspendDt.
     *
     * @return Value of property siSuspendDt.
     */
    public java.util.Date getSiSuspendDt() {
        return siSuspendDt;
    }

    /**
     * Setter for property siSuspendDt.
     *
     * @param siSuspendDt New value of property siSuspendDt.
     */
    public void setSiSuspendDt(java.util.Date siSuspendDt) {
        this.siSuspendDt = siSuspendDt;
    }
    
     public Double getSiAgentComm() {
        return siAgentComm;
    }

    public void setSiAgentComm(Double siAgentComm) {
        this.siAgentComm = siAgentComm;
    }
}