/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ConfigPasswordTO.java
 * 
 * Created on Tue Jul 26 14:39:38 GMT+05:30 2005
 */
package com.see.truetransact.transferobject.sysadmin.config;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PARAMETERS.
 */
public class ConfigPasswordTO extends TransferObject implements Serializable {

    private String passwordNeverExpire = "";
    private Double passwordExpiry = null;
    private Double minLength = null;
    private Double maxLength = null;
    private Double specialChars = null;
    private Double uppercaseChars = null;
    private Double numberChars = null;
    private Double shouldNotLastpwd = null;
    private String changePwdFirst = "";
    private String userCannotChangepwd = "";
    private String userAcctLocked = "";
    /**
     * Parameters newly added for setting rules for age *
     */
    private Double noOfAttempts = null;
    private Double retirementAge = null;
    private Double minorAge = null;
    private Integer seniorCitizenAge;
    /**
     * ****************************************************
     */
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    /**
     * Parameters newly added for setting rules for AC_HD's *
     */
    private String cashAcHd = "";
    private String ibrAcHd = "";
    
    private String app_suspense_achd = "";
    private String rtgs_gl = ""; 
    private String salary_suspense = "";
    private String denomination_allowed = ""; 
    private String payroll_mnt_cnt = "";
    private String exclude_penal_int_from_reports = "";
    /**
     * ******************************************************
     */
    private Date effectiveFrom = null;
    private String servicePeriod = "";
    /*
     * Parameters for setting Head Office and Branches
     */
    private String cboBranches = "";
    private String dayEndType = "";
    private String interBranchOnHoliday = "";
    private Double panAmount = null;
    private Date yearEndProcessDt = null;
    private Double nominalMemFee = null;
    private String allowAuth = "";
    private String cashierAuthAllowed = "";
    private String multiShareAllowed = "";
    private String tokenNoReq = "";
    private String serviceTaxReq="N";
    private Integer gahanPeriod = 0;
    private Date last_financial_year_end = null;
            

    private Date amcFromDt = null;
    private Date amcToDt = null;
    private Integer amcAlertTime = 0;
    private Integer pendingTxnAllowedDays = 0;
    
    public Integer getPendingTxnAllowedDays() {
        return pendingTxnAllowedDays;
    }

    public void setPendingTxnAllowedDays(Integer pendingTxnAllowedDays) {
        this.pendingTxnAllowedDays = pendingTxnAllowedDays;
    }

    private String logOutTime="";

    public String getLogOutTime() {
        return logOutTime;
    }

    public void setLogOutTime(String logOutTime) {
        this.logOutTime = logOutTime;
    }
        
    public Integer getAmcAlertTime() {
        return amcAlertTime;
    }

    public void setAmcAlertTime(Integer amcAlertTime) {
        this.amcAlertTime = amcAlertTime;
    }
    
    public Date getAmcFromDt() {
        return amcFromDt;
    }

    public void setAmcFromDt(Date amcFromDt) {
        this.amcFromDt = amcFromDt;
    }

    public String getApp_suspense_achd() {
        return app_suspense_achd;
    }

    public void setApp_suspense_achd(String app_suspense_achd) {
        this.app_suspense_achd = app_suspense_achd;
    }

    public String getRtgs_gl() {
        return rtgs_gl;
    }

    public void setRtgs_gl(String rtgs_gl) {
        this.rtgs_gl = rtgs_gl;
    }

    public String getSalary_suspense() {
        return salary_suspense;
    }

    public void setSalary_suspense(String salary_suspense) {
        this.salary_suspense = salary_suspense;
    }

    public String getDenomination_allowed() {
        return denomination_allowed;
    }

    public void setDenomination_allowed(String denomination_allowed) {
        this.denomination_allowed = denomination_allowed;
    }

    public String getPayroll_mnt_cnt() {
        return payroll_mnt_cnt;
    }

    public void setPayroll_mnt_cnt(String payroll_mnt_cnt) {
        this.payroll_mnt_cnt = payroll_mnt_cnt;
    }

    public String getExclude_penal_int_from_reports() {
        return exclude_penal_int_from_reports;
    }

    public void setExclude_penal_int_from_reports(String exclude_penal_int_from_reports) {
        this.exclude_penal_int_from_reports = exclude_penal_int_from_reports;
    }

    public Date getLast_financial_year_end() {
        return last_financial_year_end;
    }

    public void setLast_financial_year_end(Date last_financial_year_end) {
        this.last_financial_year_end = last_financial_year_end;
    }

    public Date getAmcToDt() {
        return amcToDt;
    }

    public void setAmcToDt(Date amcToDt) {
        this.amcToDt = amcToDt;
    }
   

  
      
    public Integer getGahanPeriod() {
        return gahanPeriod;
    }

    public void setGahanPeriod(Integer gahanPeriod) {
        this.gahanPeriod = gahanPeriod;
    }
    
    public String getServiceTaxReq() {
        return serviceTaxReq;
    }

    public void setServiceTaxReq(String serviceTaxReq) {
        this.serviceTaxReq = serviceTaxReq;
    }

    
    public String getTokenNoReq() {
        return tokenNoReq;
    }

    public void setTokenNoReq(String tokenNoReq) {
        this.tokenNoReq = tokenNoReq;
    }

    
    public String getMultiShareAllowed() {
        return multiShareAllowed;
    }

    public void setMultiShareAllowed(String multiShareAllowed) {
        this.multiShareAllowed = multiShareAllowed;
    }

    public String getCashierAuthAllowed() {
        return cashierAuthAllowed;
    }

    public void setCashierAuthAllowed(String cashierAuthAllowed) {
        this.cashierAuthAllowed = cashierAuthAllowed;
    }

    public String getAllowAuth() {
        return allowAuth;
    }

    public void setAllowAuth(String allowAuth) {
        this.allowAuth = allowAuth;
    }

    public Double getNominalMemFee() {
        return nominalMemFee;
    }

    public void setNominalMemFee(Double nominalMemFee) {
        this.nominalMemFee = nominalMemFee;
    }

    /**
     * Setter/Getter for PASSWORD_NEVER_EXPIRE - table Field
     */
    public void setPasswordNeverExpire(String passwordNeverExpire) {
        this.passwordNeverExpire = passwordNeverExpire;
    }

    public String getPasswordNeverExpire() {
        return passwordNeverExpire;
    }

    /**
     * Setter/Getter for PASSWORD_EXPIRY - table Field
     */
    public void setPasswordExpiry(Double passwordExpiry) {
        this.passwordExpiry = passwordExpiry;
    }

    public Double getPasswordExpiry() {
        return passwordExpiry;
    }

    /**
     * Setter/Getter for MIN_LENGTH - table Field
     */
    public void setMinLength(Double minLength) {
        this.minLength = minLength;
    }

    public Double getMinLength() {
        return minLength;
    }

    /**
     * Setter/Getter for MAX_LENGTH - table Field
     */
    public void setMaxLength(Double maxLength) {
        this.maxLength = maxLength;
    }

    public Double getMaxLength() {
        return maxLength;
    }

    /**
     * Setter/Getter for SPECIAL_CHARS - table Field
     */
    public void setSpecialChars(Double specialChars) {
        this.specialChars = specialChars;
    }

    public Double getSpecialChars() {
        return specialChars;
    }

    /**
     * Setter/Getter for UPPERCASE_CHARS - table Field
     */
    public void setUppercaseChars(Double uppercaseChars) {
        this.uppercaseChars = uppercaseChars;
    }

    public Double getUppercaseChars() {
        return uppercaseChars;
    }

    /**
     * Setter/Getter for NUMBER_CHARS - table Field
     */
    public void setNumberChars(Double numberChars) {
        this.numberChars = numberChars;
    }

    public Double getNumberChars() {
        return numberChars;
    }

    /**
     * Setter/Getter for SHOULD_NOT_LASTPWD - table Field
     */
    public void setShouldNotLastpwd(Double shouldNotLastpwd) {
        this.shouldNotLastpwd = shouldNotLastpwd;
    }

    public Double getShouldNotLastpwd() {
        return shouldNotLastpwd;
    }

    /**
     * Setter/Getter for CHANGE_PWD_FIRST - table Field
     */
    public void setChangePwdFirst(String changePwdFirst) {
        this.changePwdFirst = changePwdFirst;
    }

    public String getChangePwdFirst() {
        return changePwdFirst;
    }

    /**
     * Setter/Getter for USER_CANNOT_CHANGEPWD - table Field
     */
    public void setUserCannotChangepwd(String userCannotChangepwd) {
        this.userCannotChangepwd = userCannotChangepwd;
    }

    public String getUserCannotChangepwd() {
        return userCannotChangepwd;
    }

    /**
     * Setter/Getter for USER_ACCT_LOCKED - table Field
     */
    public void setUserAcctLocked(String userAcctLocked) {
        this.userAcctLocked = userAcctLocked;
    }

    public String getUserAcctLocked() {
        return userAcctLocked;
    }

    /**
     * Setter/Getter for NO_OF_ATTEMPTS - table Field
     */
    public void setNoOfAttempts(Double noOfAttempts) {
        this.noOfAttempts = noOfAttempts;
    }

    public Double getNoOfAttempts() {
        return noOfAttempts;
    }

    /**
     * Setter/Getter for RETIREMENT_AGE - table Field
     */
    public void setRetirementAge(Double retirementAge) {
        this.retirementAge = retirementAge;
    }

    public Double getRetirementAge() {
        return retirementAge;
    }

    /**
     * Setter/Getter for MINOR_AGE - table Field
     */
    public void setMinorAge(Double minorAge) {
        this.minorAge = minorAge;
    }

    public Double getMinorAge() {
        return minorAge;
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
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
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
     * Setter/Getter for CASH_AC_HD - table Field
     */
    public void setCashAcHd(String cashAcHd) {
        this.cashAcHd = cashAcHd;
    }

    public String getCashAcHd() {
        return cashAcHd;
    }

    public Integer getSeniorCitizenAge() {
        return seniorCitizenAge;
    }

    public void setSeniorCitizenAge(Integer seniorCitizenAge) {
        this.seniorCitizenAge = seniorCitizenAge;
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
     * Getter for property cboBranches.
     *
     * @return Value of property cboBranches.
     */
    public java.lang.String getCboBranches() {
        return cboBranches;
    }

    /**
     * Setter for property cboBranches.
     *
     * @param cboBranches New value of property cboBranches.
     */
    public void setCboBranches(java.lang.String cboBranches) {
        this.cboBranches = cboBranches;
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
        strB.append(getTOString("passwordNeverExpire", passwordNeverExpire));
        strB.append(getTOString("passwordExpiry", passwordExpiry));
        strB.append(getTOString("minLength", minLength));
        strB.append(getTOString("maxLength", maxLength));
        strB.append(getTOString("specialChars", specialChars));
        strB.append(getTOString("uppercaseChars", uppercaseChars));
        strB.append(getTOString("numberChars", numberChars));
        strB.append(getTOString("shouldNotLastpwd", shouldNotLastpwd));
        strB.append(getTOString("changePwdFirst", changePwdFirst));
        strB.append(getTOString("userCannotChangepwd", userCannotChangepwd));
        strB.append(getTOString("userAcctLocked", userAcctLocked));
        strB.append(getTOString("noOfAttempts", noOfAttempts));
        strB.append(getTOString("retirementAge", retirementAge));
        strB.append(getTOString("minorAge", minorAge));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("cashAcHd", cashAcHd));
        strB.append(getTOString("ibrAcHd", ibrAcHd));
        strB.append(getTOString("cboBranches", cboBranches));
        strB.append(getTOString("dayEndType", dayEndType));
        strB.append(getTOString("interBranchOnHoliday", interBranchOnHoliday));
        strB.append(getTOString("panAmount", panAmount));
        strB.append(getTOString("yearEndProcessDt", yearEndProcessDt));
        strB.append(getTOString("nominalMemFee", nominalMemFee));
        strB.append(getTOString("allowAuth", allowAuth));
        strB.append(getTOString("cashierAuthAllowed", cashierAuthAllowed));
        strB.append(getTOString("tokenNoReq", tokenNoReq));
        strB.append(getTOString("serviceTaxReq", serviceTaxReq));
        strB.append(getTOString("gahanPeriod", gahanPeriod));
        strB.append(getTOString("amcFromDt", amcFromDt));
        strB.append(getTOString("amcToDt", amcToDt));
        strB.append(getTOString("amcAlertTime", amcAlertTime));
        strB.append(getTOString("pendingTxnAllowedDays", pendingTxnAllowedDays));
        strB.append(getTOString("LogOutTime", logOutTime));
        strB.append(getTOString("seniorCitizenAge", seniorCitizenAge));
        strB.append(getTOString("multiShareAllowed", multiShareAllowed));
        strB.append(getTOString("salary_suspense", salary_suspense));
        strB.append(getTOString("exclude_penal_int_from_reports", exclude_penal_int_from_reports));
        strB.append(getTOString("denomination_allowed", denomination_allowed));
        strB.append(getTOString("last_financial_year_end", last_financial_year_end));
        strB.append(getTOString("payroll_mnt_cnt", payroll_mnt_cnt));
        strB.append(getTOString("app_suspense_achd", app_suspense_achd));
        strB.append(getTOString("rtgs_gl", rtgs_gl));
        
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("passwordNeverExpire", passwordNeverExpire));
        strB.append(getTOXml("passwordExpiry", passwordExpiry));
        strB.append(getTOXml("minLength", minLength));
        strB.append(getTOXml("maxLength", maxLength));
        strB.append(getTOXml("specialChars", specialChars));
        strB.append(getTOXml("uppercaseChars", uppercaseChars));
        strB.append(getTOXml("numberChars", numberChars));
        strB.append(getTOXml("shouldNotLastpwd", shouldNotLastpwd));
        strB.append(getTOXml("changePwdFirst", changePwdFirst));
        strB.append(getTOXml("userCannotChangepwd", userCannotChangepwd));
        strB.append(getTOXml("userAcctLocked", userAcctLocked));
        strB.append(getTOXml("noOfAttempts", noOfAttempts));
        strB.append(getTOXml("retirementAge", retirementAge));
        strB.append(getTOXml("minorAge", minorAge));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("cashAcHd", cashAcHd));
        strB.append(getTOXml("ibrAcHd", ibrAcHd));
        strB.append(getTOXml("cboBranches", cboBranches));
        strB.append(getTOXml("dayEndType", dayEndType));
        strB.append(getTOXml("interBranchOnHoliday", interBranchOnHoliday));
        strB.append(getTOXml("panAmount", panAmount));
        strB.append(getTOXml("multiShareAllowed", multiShareAllowed));
        strB.append(getTOXml("yearEndProcessDt", yearEndProcessDt));
        strB.append(getTOXml("nominalMemFee", nominalMemFee));
        strB.append(getTOXml("allowAuth", allowAuth));
        strB.append(getTOXml("cashierAuthAllowed", cashierAuthAllowed));
        strB.append(getTOXml("tokenNoReq", tokenNoReq));
        strB.append(getTOXml("serviceTaxReq", serviceTaxReq));
        strB.append(getTOXml("gahanPeriod", gahanPeriod));
        strB.append(getTOXml("amcFromDt", amcFromDt));
        strB.append(getTOXml("amcToDt", amcToDt));
        strB.append(getTOXml("amcAlertTime", amcAlertTime));
        strB.append(getTOXml("LogOutTime", logOutTime));
        strB.append(getTOXml("pendingTxnAllowedDays", pendingTxnAllowedDays)); 
        strB.append(getTOXml("seniorCitizenAge", seniorCitizenAge));
        
        strB.append(getTOXml("salary_suspense", salary_suspense));
        strB.append(getTOXml("exclude_penal_int_from_reports", exclude_penal_int_from_reports));
        strB.append(getTOXml("denomination_allowed", denomination_allowed));
        strB.append(getTOXml("last_financial_year_end", last_financial_year_end));
        strB.append(getTOXml("payroll_mnt_cnt", payroll_mnt_cnt));
        strB.append(getTOXml("app_suspense_achd", app_suspense_achd));
        strB.append(getTOXml("rtgs_gl", rtgs_gl));
        
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property dayEndType.
     *
     * @return Value of property dayEndType.
     */
    public java.lang.String getDayEndType() {
        return dayEndType;
    }

    /**
     * Setter for property dayEndType.
     *
     * @param dayEndType New value of property dayEndType.
     */
    public void setDayEndType(java.lang.String dayEndType) {
        this.dayEndType = dayEndType;
    }

    /**
     * Getter for property interBranchOnHoliday.
     *
     * @return Value of property interBranchOnHoliday.
     */
    public java.lang.String getInterBranchOnHoliday() {
        return interBranchOnHoliday;
    }

    /**
     * Setter for property interBranchOnHoliday.
     *
     * @param interBranchOnHoliday New value of property interBranchOnHoliday.
     */
    public void setInterBranchOnHoliday(java.lang.String interBranchOnHoliday) {
        this.interBranchOnHoliday = interBranchOnHoliday;
    }

//    /**
//     * Getter for property panAmount.
//     *
//     * @return Value of property panAmount.
//     */
//    public java.lang.String getPanAmount() {
//        return panAmount;
//    }
//
//    /**
//     * Setter for property panAmount.
//     *
//     * @param panAmount New value of property panAmount.
//     */
//    public void setPanAmount(java.lang.String panAmount) {
//        this.panAmount = panAmount;
//    }

    public Double getPanAmount() {
        return panAmount;
    }

    public void setPanAmount(Double panAmount) {
        this.panAmount = panAmount;
    }
    
    

    /**
     * Getter for property yearEndProcessDt.
     *
     * @return Value of property yearEndProcessDt.
     */
    public java.util.Date getYearEndProcessDt() {
        return yearEndProcessDt;
    }

    /**
     * Setter for property yearEndProcessDt.
     *
     * @param yearEndProcessDt New value of property yearEndProcessDt.
     */
    public void setYearEndProcessDt(java.util.Date yearEndProcessDt) {
        this.yearEndProcessDt = yearEndProcessDt;
    }

    /**
     * Getter for property effectiveFrom.
     *
     * @return Value of property effectiveFrom.
     */
    public Date getEffectiveFrom() {
        return effectiveFrom;
    }

    /**
     * Setter for property effectiveFrom.
     *
     * @param effectiveFrom New value of property effectiveFrom.
     */
    public void setEffectiveFrom(Date effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    /**
     * Getter for property servicePeriod.
     *
     * @return Value of property servicePeriod.
     */
    public String getServicePeriod() {
        return servicePeriod;
    }

    /**
     * Setter for property servicePeriod.
     *
     * @param servicePeriod New value of property servicePeriod.
     */
    public void setServicePeriod(String servicePeriod) {
        this.servicePeriod = servicePeriod;
    }
}