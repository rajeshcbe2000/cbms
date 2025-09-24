/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductClassificationsTO.java
 * 
 * Created on Fri Apr 29 17:55:04 IST 2005
 */
package com.see.truetransact.transferobject.product.loan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_CLASSIFICATION.
 */
public class LoanProductClassificationsTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String commodityCode = "";
    private String facilityType = "";
    private String purposeCode = "";
    private String industryCode = "";
    private String twentyCode = "";
    private String govtSchemeCode = "";
    private String ecgc = "";
    private String guaranteeCoverCode = "";
    private String healthCode = "";
    private String sectorCode = "";
    private String refinancingInstitution = "";
    private String directFinance = "";
    private String qis = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String securityDetails = "";

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
     * Setter/Getter for COMMODITY_CODE - table Field
     */
    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    /**
     * Setter/Getter for FACILITY_TYPE - table Field
     */
    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public String getFacilityType() {
        return facilityType;
    }

    /**
     * Setter/Getter for PURPOSE_CODE - table Field
     */
    public void setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public String getPurposeCode() {
        return purposeCode;
    }

    /**
     * Setter/Getter for INDUSTRY_CODE - table Field
     */
    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    /**
     * Setter/Getter for TWENTY_CODE - table Field
     */
    public void setTwentyCode(String twentyCode) {
        this.twentyCode = twentyCode;
    }

    public String getTwentyCode() {
        return twentyCode;
    }

    /**
     * Setter/Getter for GOVT_SCHEME_CODE - table Field
     */
    public void setGovtSchemeCode(String govtSchemeCode) {
        this.govtSchemeCode = govtSchemeCode;
    }

    public String getGovtSchemeCode() {
        return govtSchemeCode;
    }

    /**
     * Setter/Getter for ECGC - table Field
     */
    public void setEcgc(String ecgc) {
        this.ecgc = ecgc;
    }

    public String getEcgc() {
        return ecgc;
    }

    /**
     * Setter/Getter for GUARANTEE_COVER_CODE - table Field
     */
    public void setGuaranteeCoverCode(String guaranteeCoverCode) {
        this.guaranteeCoverCode = guaranteeCoverCode;
    }

    public String getGuaranteeCoverCode() {
        return guaranteeCoverCode;
    }

    /**
     * Setter/Getter for HEALTH_CODE - table Field
     */
    public void setHealthCode(String healthCode) {
        this.healthCode = healthCode;
    }

    public String getHealthCode() {
        return healthCode;
    }

    /**
     * Setter/Getter for SECTOR_CODE - table Field
     */
    public void setSectorCode(String sectorCode) {
        this.sectorCode = sectorCode;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    /**
     * Setter/Getter for REFINANCING_INSTITUTION - table Field
     */
    public void setRefinancingInstitution(String refinancingInstitution) {
        this.refinancingInstitution = refinancingInstitution;
    }

    public String getRefinancingInstitution() {
        return refinancingInstitution;
    }

    /**
     * Setter/Getter for DIRECT_FINANCE - table Field
     */
    public void setDirectFinance(String directFinance) {
        this.directFinance = directFinance;
    }

    public String getDirectFinance() {
        return directFinance;
    }

    /**
     * Setter/Getter for QIS - table Field
     */
    public void setQis(String qis) {
        this.qis = qis;
    }

    public String getQis() {
        return qis;
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
     * Setter/Getter for SECURITY_DETAILS - table Field
     */
    public void setSecurityDetails(String securityDetails) {
        this.securityDetails = securityDetails;
    }

    public String getSecurityDetails() {
        return securityDetails;
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

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("commodityCode", commodityCode));
        strB.append(getTOString("facilityType", facilityType));
        strB.append(getTOString("purposeCode", purposeCode));
        strB.append(getTOString("industryCode", industryCode));
        strB.append(getTOString("twentyCode", twentyCode));
        strB.append(getTOString("govtSchemeCode", govtSchemeCode));
        strB.append(getTOString("ecgc", ecgc));
        strB.append(getTOString("guaranteeCoverCode", guaranteeCoverCode));
        strB.append(getTOString("healthCode", healthCode));
        strB.append(getTOString("sectorCode", sectorCode));
        strB.append(getTOString("refinancingInstitution", refinancingInstitution));
        strB.append(getTOString("directFinance", directFinance));
        strB.append(getTOString("qis", qis));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("securityDetails", securityDetails));
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
        strB.append(getTOXml("commodityCode", commodityCode));
        strB.append(getTOXml("facilityType", facilityType));
        strB.append(getTOXml("purposeCode", purposeCode));
        strB.append(getTOXml("industryCode", industryCode));
        strB.append(getTOXml("twentyCode", twentyCode));
        strB.append(getTOXml("govtSchemeCode", govtSchemeCode));
        strB.append(getTOXml("ecgc", ecgc));
        strB.append(getTOXml("guaranteeCoverCode", guaranteeCoverCode));
        strB.append(getTOXml("healthCode", healthCode));
        strB.append(getTOXml("sectorCode", sectorCode));
        strB.append(getTOXml("refinancingInstitution", refinancingInstitution));
        strB.append(getTOXml("directFinance", directFinance));
        strB.append(getTOXml("qis", qis));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("securityDetails", securityDetails));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}