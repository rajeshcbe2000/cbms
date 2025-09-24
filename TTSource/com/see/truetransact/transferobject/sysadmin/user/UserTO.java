/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserTO.java
 * 
 * Created on Wed Feb 09 11:04:09 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.user;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is USER_MASTER.
 */
public class UserTO extends TransferObject implements Serializable {

    private String userId = "";
    private String appraiserAllowed = "";
    private String pwd = "";
    private String status = "";
    private String customerId = "";
    private String emailId = "";
    private Date expiryDate = null;
    private String remarks = "";
    private String pinLogin = "";
    private String suspendByUser = "";
    private String suspendUser = "";
    private Date activationDate = null;
    private Date lastLoginDt = null;
    private String branchCode = "";
    private String ipAddr = "";
    private Date lastLogoutDt = null;
    private String loginStatus = "";
    private String userGroup = "";
    private String userRole = "";
    private String foreignGroupId = "";
    private String foreignBranchGroup = "";
    private Date lastPwdChange = null;
    private String createdBy = "";
    private Date createdDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizedStatus = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String suspendReason = "";
    private Date suspendFromDt = null;
    private Date suspendToDt = null;

    /**
     * Setter/Getter for USER_ID - table Field
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String isAppraiserAllowed() {
        return appraiserAllowed;
    }

    public void setAppraiserAllowed(String appraiserAllowed) {
        this.appraiserAllowed = appraiserAllowed;
    }

    /**
     * Setter/Getter for PWD - table Field
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
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
     * Setter/Getter for EMAIL_ID - table Field
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    /**
     * Setter/Getter for EXPIRY_DATE - table Field
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    /**
     * Setter/Getter for PIN_LOGIN - table Field
     */
    public void setPinLogin(String pinLogin) {
        this.pinLogin = pinLogin;
    }

    public String getPinLogin() {
        return pinLogin;
    }

    /**
     * Setter/Getter for SUSPEND_BY_USER - table Field
     */
    public void setSuspendByUser(String suspendByUser) {
        this.suspendByUser = suspendByUser;
    }

    public String getSuspendByUser() {
        return suspendByUser;
    }

    /**
     * Setter/Getter for SUSPEND_USER - table Field
     */
    public void setSuspendUser(String suspendUser) {
        this.suspendUser = suspendUser;
    }

    public String getSuspendUser() {
        return suspendUser;
    }

    /**
     * Setter/Getter for ACTIVATION_DATE - table Field
     */
    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    /**
     * Setter/Getter for LAST_LOGIN_DT - table Field
     */
    public void setLastLoginDt(Date lastLoginDt) {
        this.lastLoginDt = lastLoginDt;
    }

    public Date getLastLoginDt() {
        return lastLoginDt;
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
     * Setter/Getter for IP_ADDR - table Field
     */
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * Setter/Getter for LAST_LOGOUT_DT - table Field
     */
    public void setLastLogoutDt(Date lastLogoutDt) {
        this.lastLogoutDt = lastLogoutDt;
    }

    public Date getLastLogoutDt() {
        return lastLogoutDt;
    }

    /**
     * Setter/Getter for LOGIN_STATUS - table Field
     */
    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    /**
     * Setter/Getter for USER_GROUP - table Field
     */
    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getUserGroup() {
        return userGroup;
    }

    /**
     * Setter/Getter for USER_ROLE - table Field
     */
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserRole() {
        return userRole;
    }

    /**
     * Setter/Getter for LAST_PWD_CHANGE - table Field
     */
    public void setLastPwdChange(Date lastPwdChange) {
        this.lastPwdChange = lastPwdChange;
    }

    public Date getLastPwdChange() {
        return lastPwdChange;
    }

    /**
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter/Getter for SUSPEND_REASON - table Field
     */
    public void setSuspendReason(String suspendReason) {
        this.suspendReason = suspendReason;
    }

    public String getSuspendReason() {
        return suspendReason;
    }

    /**
     * Setter/Getter for SUSPEND_FROM_DT - table Field
     */
    public void setSuspendFromDt(Date suspendFromDt) {
        this.suspendFromDt = suspendFromDt;
    }

    public Date getSuspendFromDt() {
        return suspendFromDt;
    }

    /**
     * Setter/Getter for SUSPEND_TO_DT - table Field
     */
    public void setSuspendToDt(Date suspendToDt) {
        this.suspendToDt = suspendToDt;
    }

    public Date getSuspendToDt() {
        return suspendToDt;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("userId");
        return userId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("userId", userId));
        strB.append(getTOString("pwd", pwd));
        strB.append(getTOString("status", status));
        strB.append(getTOString("customerId", customerId));
        strB.append(getTOString("emailId", emailId));
        strB.append(getTOString("appraiserAllowed", appraiserAllowed));
        strB.append(getTOString("expiryDate", expiryDate));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("pinLogin", pinLogin));
        strB.append(getTOString("suspendByUser", suspendByUser));
        strB.append(getTOString("suspendUser", suspendUser));
        strB.append(getTOString("activationDate", activationDate));
        strB.append(getTOString("lastLoginDt", lastLoginDt));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("ipAddr", ipAddr));
        strB.append(getTOString("lastLogoutDt", lastLogoutDt));
        strB.append(getTOString("loginStatus", loginStatus));
        strB.append(getTOString("userGroup", userGroup));
        strB.append(getTOString("userRole", userRole));
        strB.append(getTOString("lastPwdChange", lastPwdChange));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("suspendReason", suspendReason));
        strB.append(getTOString("suspendFromDt", suspendFromDt));
        strB.append(getTOString("suspendToDt", suspendToDt));
        strB.append(getTOString("foreignGroupId", foreignGroupId));
        strB.append(getTOString("foreignBranchGroup", foreignBranchGroup));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("userId", userId));
        strB.append(getTOXml("pwd", pwd));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("customerId", customerId));
        strB.append(getTOXml("emailId", emailId));
        strB.append(getTOXml("appraiserAllowed", appraiserAllowed));
        strB.append(getTOXml("expiryDate", expiryDate));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("pinLogin", pinLogin));
        strB.append(getTOXml("suspendByUser", suspendByUser));
        strB.append(getTOXml("suspendUser", suspendUser));
        strB.append(getTOXml("activationDate", activationDate));
        strB.append(getTOXml("lastLoginDt", lastLoginDt));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("ipAddr", ipAddr));
        strB.append(getTOXml("lastLogoutDt", lastLogoutDt));
        strB.append(getTOXml("loginStatus", loginStatus));
        strB.append(getTOXml("userGroup", userGroup));
        strB.append(getTOXml("userRole", userRole));
        strB.append(getTOXml("lastPwdChange", lastPwdChange));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("suspendReason", suspendReason));
        strB.append(getTOXml("suspendFromDt", suspendFromDt));
        strB.append(getTOXml("suspendToDt", suspendToDt));
        strB.append(getTOXml("foreignGroupId", foreignGroupId));
        strB.append(getTOXml("foreignBranchGroup", foreignBranchGroup));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property foreignGroupId.
     *
     * @return Value of property foreignGroupId.
     */
    public java.lang.String getForeignGroupId() {
        return foreignGroupId;
    }

    /**
     * Setter for property foreignGroupId.
     *
     * @param foreignGroupId New value of property foreignGroupId.
     */
    public void setForeignGroupId(java.lang.String foreignGroupId) {
        this.foreignGroupId = foreignGroupId;
    }

    /**
     * Getter for property foreignBranchGroup.
     *
     * @return Value of property foreignBranchGroup.
     */
    public java.lang.String getForeignBranchGroup() {
        return foreignBranchGroup;
    }

    /**
     * Setter for property foreignBranchGroup.
     *
     * @param foreignBranchGroup New value of property foreignBranchGroup.
     */
    public void setForeignBranchGroup(java.lang.String foreignBranchGroup) {
        this.foreignBranchGroup = foreignBranchGroup;
    }
    
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
}