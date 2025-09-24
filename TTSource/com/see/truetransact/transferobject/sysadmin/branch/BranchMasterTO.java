/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchMasterTO.java
 * 
 * Created on Tue Apr 12 16:55:47 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.branch;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BRANCH_MASTER.
 */
public class BranchMasterTO extends TransferObject implements Serializable {

    private String branchCode = "";
    private String branchName = "";
    private String street = "";
    private String area = "";
    private String city = "";
    private String state = "";
    private String pinCode = "";
    private String countryCode = "";
    private String areaCode = "";
    private Date createdDt = null;
    private Date deletedDt = null;
    private String status = "";
    private String ipAddr = "";
    private Double port = null;
    private String chkBalanceLimit = "";
    private Double maxCashStock = null;
    private Double avgCashStock = null;
    private String branchDbName = "";
    private String branchDbIp = "";
    private Double branchDbPort = null;
    private String dbUserId = "";
    private String dbPassword = "";
    private String dbDriver = "";
    private String dbUrl = "";
    private String branchShortName = "";
    private Date openingDt = null;
    private String branchManagerNo = "";
    private String ro = "";
    private String micrCode = "";
    private String bsrCode = "";
    private String workingHoursFrom = "";
    private String workingHoursTo = "";
    private String workingMinsFrom = "";
    private String workingMinsTo = "";
    private String bankCode = "";
    private String branchGroup = "";
    private String glGroupId = "";
    private String authorizeStatus1 = "";
    private String authorizeBy1 = "";
    private Date authorizeDt1 = null;
    private String authorizeStatus2 = "";
    private String authorizeBy2 = "";
    private Date authorizeDt2 = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String chkShift = null;
    private String rdoTransAuth = "";

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
     * Setter/Getter for BRANCH_NAME - table Field
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    /**
     * Setter/Getter for STREET - table Field
     */
    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    /**
     * Setter/Getter for AREA - table Field
     */
    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    /**
     * Setter/Getter for CITY - table Field
     */
    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    /**
     * Setter/Getter for STATE - table Field
     */
    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    /**
     * Setter/Getter for PIN_CODE - table Field
     */
    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPinCode() {
        return pinCode;
    }

    /**
     * Setter/Getter for COUNTRY_CODE - table Field
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Setter/Getter for AREA_CODE - table Field
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaCode() {
        return areaCode;
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
     * Setter/Getter for DELETED_DT - table Field
     */
    public void setDeletedDt(Date deletedDt) {
        this.deletedDt = deletedDt;
    }

    public Date getDeletedDt() {
        return deletedDt;
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
     * Setter/Getter for IP_ADDR - table Field
     */
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * Setter/Getter for PORT - table Field
     */
    public void setPort(Double port) {
        this.port = port;
    }

    public Double getPort() {
        return port;
    }

    /**
     * Setter/Getter for CHK_BALANCE_LIMIT - table Field
     */
    public void setChkBalanceLimit(String chkBalanceLimit) {
        this.chkBalanceLimit = chkBalanceLimit;
    }

    public String getChkBalanceLimit() {
        return chkBalanceLimit;
    }

    /**
     * Setter/Getter for MAX_CASH_STOCK - table Field
     */
    public void setMaxCashStock(Double maxCashStock) {
        this.maxCashStock = maxCashStock;
    }

    public Double getMaxCashStock() {
        return maxCashStock;
    }

    /**
     * Setter/Getter for AVG_CASH_STOCK - table Field
     */
    public void setAvgCashStock(Double avgCashStock) {
        this.avgCashStock = avgCashStock;
    }

    public Double getAvgCashStock() {
        return avgCashStock;
    }

    /**
     * Setter/Getter for BRANCH_DB_NAME - table Field
     */
    public void setBranchDbName(String branchDbName) {
        this.branchDbName = branchDbName;
    }

    public String getBranchDbName() {
        return branchDbName;
    }

    /**
     * Setter/Getter for BRANCH_DB_IP - table Field
     */
    public void setBranchDbIp(String branchDbIp) {
        this.branchDbIp = branchDbIp;
    }

    public String getBranchDbIp() {
        return branchDbIp;
    }

    /**
     * Setter/Getter for BRANCH_DB_PORT - table Field
     */
    public void setBranchDbPort(Double branchDbPort) {
        this.branchDbPort = branchDbPort;
    }

    public Double getBranchDbPort() {
        return branchDbPort;
    }

    /**
     * Setter/Getter for DB_USER_ID - table Field
     */
    public void setDbUserId(String dbUserId) {
        this.dbUserId = dbUserId;
    }

    public String getDbUserId() {
        return dbUserId;
    }

    /**
     * Setter/Getter for DB_PASSWORD - table Field
     */
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    /**
     * Setter/Getter for DB_DRIVER - table Field
     */
    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    /**
     * Setter/Getter for DB_URL - table Field
     */
    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    /**
     * Setter/Getter for BRANCH_SHORT_NAME - table Field
     */
    public void setBranchShortName(String branchShortName) {
        this.branchShortName = branchShortName;
    }

    public String getBranchShortName() {
        return branchShortName;
    }

    /**
     * Setter/Getter for OPENING_DT - table Field
     */
    public void setOpeningDt(Date openingDt) {
        this.openingDt = openingDt;
    }

    public Date getOpeningDt() {
        return openingDt;
    }

    /**
     * Setter/Getter for BRANCH_MANAGER_NO - table Field
     */
    public void setBranchManagerNo(String branchManagerNo) {
        this.branchManagerNo = branchManagerNo;
    }

    public String getBranchManagerNo() {
        return branchManagerNo;
    }

    /**
     * Setter/Getter for RO - table Field
     */
    public void setRo(String ro) {
        this.ro = ro;
    }

    public String getRo() {
        return ro;
    }

    /**
     * Setter/Getter for MICR_CODE - table Field
     */
    public void setMicrCode(String micrCode) {
        this.micrCode = micrCode;
    }

    public String getMicrCode() {
        return micrCode;
    }

    /**
     * Setter/Getter for BSR_CODE - table Field
     */
    public void setBsrCode(String bsrCode) {
        this.bsrCode = bsrCode;
    }

    public String getBsrCode() {
        return bsrCode;
    }

    /**
     * Setter/Getter for WORKING_HOURS_FROM - table Field
     */
    public void setWorkingHoursFrom(String workingHoursFrom) {
        this.workingHoursFrom = workingHoursFrom;
    }

    public String getWorkingHoursFrom() {
        return workingHoursFrom;
    }

    /**
     * Setter/Getter for WORKING_HOURS_TO - table Field
     */
    public void setWorkingHoursTo(String workingHoursTo) {
        this.workingHoursTo = workingHoursTo;
    }

    public String getWorkingHoursTo() {
        return workingHoursTo;
    }

    /**
     * Setter/Getter for WORKING_MINS_FROM - table Field
     */
    public void setWorkingMinsFrom(String workingMinsFrom) {
        this.workingMinsFrom = workingMinsFrom;
    }

    public String getWorkingMinsFrom() {
        return workingMinsFrom;
    }

    /**
     * Setter/Getter for WORKING_MINS_TO - table Field
     */
    public void setWorkingMinsTo(String workingMinsTo) {
        this.workingMinsTo = workingMinsTo;
    }

    public String getWorkingMinsTo() {
        return workingMinsTo;
    }

    /**
     * Setter/Getter for BANK_CODE - table Field
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    /**
     * Setter/Getter for BRANCH_GROUP - table Field
     */
    public void setBranchGroup(String branchGroup) {
        this.branchGroup = branchGroup;
    }

    public String getBranchGroup() {
        return branchGroup;
    }

    /**
     * Setter/Getter for GL_GROUP_ID - table Field
     */
    public void setGlGroupId(String glGroupId) {
        this.glGroupId = glGroupId;
    }

    public String getGlGroupId() {
        return glGroupId;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS_1 - table Field
     */
    public void setAuthorizeStatus1(String authorizeStatus1) {
        this.authorizeStatus1 = authorizeStatus1;
    }

    public String getAuthorizeStatus1() {
        return authorizeStatus1;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY_1 - table Field
     */
    public void setAuthorizeBy1(String authorizeBy1) {
        this.authorizeBy1 = authorizeBy1;
    }

    public String getAuthorizeBy1() {
        return authorizeBy1;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT_1 - table Field
     */
    public void setAuthorizeDt1(Date authorizeDt1) {
        this.authorizeDt1 = authorizeDt1;
    }

    public Date getAuthorizeDt1() {
        return authorizeDt1;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS_2 - table Field
     */
    public void setAuthorizeStatus2(String authorizeStatus2) {
        this.authorizeStatus2 = authorizeStatus2;
    }

    public String getAuthorizeStatus2() {
        return authorizeStatus2;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY_2 - table Field
     */
    public void setAuthorizeBy2(String authorizeBy2) {
        this.authorizeBy2 = authorizeBy2;
    }

    public String getAuthorizeBy2() {
        return authorizeBy2;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT_2 - table Field
     */
    public void setAuthorizeDt2(Date authorizeDt2) {
        this.authorizeDt2 = authorizeDt2;
    }

    public Date getAuthorizeDt2() {
        return authorizeDt2;
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
        setKeyColumns("branchCode");
        return branchCode;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("branchName", branchName));
        strB.append(getTOString("street", street));
        strB.append(getTOString("area", area));
        strB.append(getTOString("city", city));
        strB.append(getTOString("state", state));
        strB.append(getTOString("pinCode", pinCode));
        strB.append(getTOString("countryCode", countryCode));
        strB.append(getTOString("areaCode", areaCode));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("deletedDt", deletedDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("ipAddr", ipAddr));
        strB.append(getTOString("port", port));
        strB.append(getTOString("chkBalanceLimit", chkBalanceLimit));
        strB.append(getTOString("maxCashStock", maxCashStock));
        strB.append(getTOString("avgCashStock", avgCashStock));
        strB.append(getTOString("branchDbName", branchDbName));
        strB.append(getTOString("branchDbIp", branchDbIp));
        strB.append(getTOString("branchDbPort", branchDbPort));
        strB.append(getTOString("dbUserId", dbUserId));
        strB.append(getTOString("dbPassword", dbPassword));
        strB.append(getTOString("dbDriver", dbDriver));
        strB.append(getTOString("dbUrl", dbUrl));
        strB.append(getTOString("branchShortName", branchShortName));
        strB.append(getTOString("openingDt", openingDt));
        strB.append(getTOString("branchManagerNo", branchManagerNo));
        strB.append(getTOString("ro", ro));
        strB.append(getTOString("micrCode", micrCode));
        strB.append(getTOString("bsrCode", bsrCode));
        strB.append(getTOString("workingHoursFrom", workingHoursFrom));
        strB.append(getTOString("workingHoursTo", workingHoursTo));
        strB.append(getTOString("workingMinsFrom", workingMinsFrom));
        strB.append(getTOString("workingMinsTo", workingMinsTo));
        strB.append(getTOString("bankCode", bankCode));
        strB.append(getTOString("branchGroup", branchGroup));
        strB.append(getTOString("glGroupId", glGroupId));
        strB.append(getTOString("authorizeStatus1", authorizeStatus1));
        strB.append(getTOString("authorizeBy1", authorizeBy1));
        strB.append(getTOString("authorizeDt1", authorizeDt1));
        strB.append(getTOString("authorizeStatus2", authorizeStatus2));
        strB.append(getTOString("authorizeBy2", authorizeBy2));
        strB.append(getTOString("authorizeDt2", authorizeDt2));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));

        strB.append(getTOString("chkShift", chkShift));
        strB.append(getTOString("rdoTransAuth", rdoTransAuth));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("branchName", branchName));
        strB.append(getTOXml("street", street));
        strB.append(getTOXml("area", area));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("pinCode", pinCode));
        strB.append(getTOXml("countryCode", countryCode));
        strB.append(getTOXml("areaCode", areaCode));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("deletedDt", deletedDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("ipAddr", ipAddr));
        strB.append(getTOXml("port", port));
        strB.append(getTOXml("chkBalanceLimit", chkBalanceLimit));
        strB.append(getTOXml("maxCashStock", maxCashStock));
        strB.append(getTOXml("avgCashStock", avgCashStock));
        strB.append(getTOXml("branchDbName", branchDbName));
        strB.append(getTOXml("branchDbIp", branchDbIp));
        strB.append(getTOXml("branchDbPort", branchDbPort));
        strB.append(getTOXml("dbUserId", dbUserId));
        strB.append(getTOXml("dbPassword", dbPassword));
        strB.append(getTOXml("dbDriver", dbDriver));
        strB.append(getTOXml("dbUrl", dbUrl));
        strB.append(getTOXml("branchShortName", branchShortName));
        strB.append(getTOXml("openingDt", openingDt));
        strB.append(getTOXml("branchManagerNo", branchManagerNo));
        strB.append(getTOXml("ro", ro));
        strB.append(getTOXml("micrCode", micrCode));
        strB.append(getTOXml("bsrCode", bsrCode));
        strB.append(getTOXml("workingHoursFrom", workingHoursFrom));
        strB.append(getTOXml("workingHoursTo", workingHoursTo));
        strB.append(getTOXml("workingMinsFrom", workingMinsFrom));
        strB.append(getTOXml("workingMinsTo", workingMinsTo));
        strB.append(getTOXml("bankCode", bankCode));
        strB.append(getTOXml("branchGroup", branchGroup));
        strB.append(getTOXml("glGroupId", glGroupId));
        strB.append(getTOXml("authorizeStatus1", authorizeStatus1));
        strB.append(getTOXml("authorizeBy1", authorizeBy1));
        strB.append(getTOXml("authorizeDt1", authorizeDt1));
        strB.append(getTOXml("authorizeStatus2", authorizeStatus2));
        strB.append(getTOXml("authorizeBy2", authorizeBy2));
        strB.append(getTOXml("authorizeDt2", authorizeDt2));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));

        strB.append(getTOXml("rdoTransAuth", rdoTransAuth));
        strB.append(getTOXml("chkShift", chkShift));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property chkShift.
     *
     * @return Value of property chkShift.
     */
    public String getChkShift() {
        return chkShift;
    }

    /**
     * Setter for property chkShift.
     *
     * @param chkShift New value of property chkShift.
     */
    public void setChkShift(String chkShift) {
        this.chkShift = chkShift;
    }

    /**
     * Getter for property rdoTransAuth.
     *
     * @return Value of property rdoTransAuth.
     */
    public String getRdoTransAuth() {
        return rdoTransAuth;
    }

    /**
     * Setter for property rdoTransAuth.
     *
     * @param rdoTransAuth New value of property rdoTransAuth.
     */
    public void setRdoTransAuth(String rdoTransAuth) {
        this.rdoTransAuth = rdoTransAuth;
    }
}