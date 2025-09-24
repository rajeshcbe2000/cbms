/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanClassificationTO.java
 * @author shanmugavel
 * Created on Thu May 06 10:53:15 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_CLASSIFY_DETAILS.
 */
public class GoldLoanClassificationTO extends TransferObject implements Serializable {

    private String borrowNo = "";
    private String prodId = "";
    private String commodityCode = "";
    private String businessSectorCode = "";
    private String facilityType = "";
    private String purposeCode = "";
    private String industryCode = "";
    private String twentyCode = "";
    private String govtSchemeCode = "";
    private Date npaDt = null;
    private String ecgc = "";
    private String documentComplete = "";
    private String guaranteeCoverCode = "";
    private String healthCode = "";
    private String districtCode = "";
    private String sectorCode = "";
    private String paymentCode = "";
    private String refinancingInstitution = "";
    private String assetStatus = "";
    private String directFinance = "";
    private String prioritySector = "";
    private String qis = "";
    private String authorizeRemarks = "";
    private String authorizeStatus = null;
    private String acctNum = "";
    private String status = "";
    private String command = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String purpose = "";

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    /**
     * Setter/Getter for BORROW_NO - table Field
     */
    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getBorrowNo() {
        return borrowNo;
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
     * Setter/Getter for COMMODITY_CODE - table Field
     */
    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    /**
     * Setter/Getter for BUSINESS_SECTOR_CODE - table Field
     */
    public void setBusinessSectorCode(String businessSectorCode) {
        this.businessSectorCode = businessSectorCode;
    }

    public String getBusinessSectorCode() {
        return businessSectorCode;
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
     * Setter/Getter for NPA_DT - table Field
     */
    public void setNpaDt(Date npaDt) {
        this.npaDt = npaDt;
    }

    public Date getNpaDt() {
        return npaDt;
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
     * Setter/Getter for DOCUMENT_COMPLETE - table Field
     */
    public void setDocumentComplete(String documentComplete) {
        this.documentComplete = documentComplete;
    }

    public String getDocumentComplete() {
        return documentComplete;
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
     * Setter/Getter for DISTRICT_CODE - table Field
     */
    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictCode() {
        return districtCode;
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
     * Setter/Getter for PAYMENT_CODE - table Field
     */
    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentCode() {
        return paymentCode;
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
     * Setter/Getter for ASSET_STATUS - table Field
     */
    public void setAssetStatus(String assetStatus) {
        this.assetStatus = assetStatus;
    }

    public String getAssetStatus() {
        return assetStatus;
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
     * Setter/Getter for PRIORITY_SECTOR - table Field
     */
    public void setPrioritySector(String prioritySector) {
        this.prioritySector = prioritySector;
    }

    public String getPrioritySector() {
        return prioritySector;
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
     * Setter/Getter for AUTHORIZE_REMARKS - table Field
     */
    public void setAuthorizeRemarks(String authorizeRemarks) {
        this.authorizeRemarks = authorizeRemarks;
    }

    public String getAuthorizeRemarks() {
        return authorizeRemarks;
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
     * Setter/Getter for ACCT_NUM - table Field
     */
    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAcctNum() {
        return acctNum;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("acctNum");
        return acctNum;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("borrowNo", borrowNo));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("commodityCode", commodityCode));
        strB.append(getTOString("businessSectorCode", businessSectorCode));
        strB.append(getTOString("facilityType", facilityType));
        strB.append(getTOString("purposeCode", purposeCode));
        strB.append(getTOString("industryCode", industryCode));
        strB.append(getTOString("twentyCode", twentyCode));
        strB.append(getTOString("govtSchemeCode", govtSchemeCode));
        strB.append(getTOString("npaDt", npaDt));
        strB.append(getTOString("ecgc", ecgc));
        strB.append(getTOString("documentComplete", documentComplete));
        strB.append(getTOString("guaranteeCoverCode", guaranteeCoverCode));
        strB.append(getTOString("healthCode", healthCode));
        strB.append(getTOString("districtCode", districtCode));
        strB.append(getTOString("sectorCode", sectorCode));
        strB.append(getTOString("paymentCode", paymentCode));
        strB.append(getTOString("refinancingInstitution", refinancingInstitution));
        strB.append(getTOString("assetStatus", assetStatus));
        strB.append(getTOString("directFinance", directFinance));
        strB.append(getTOString("prioritySector", prioritySector));
        strB.append(getTOString("qis", qis));
        strB.append(getTOString("authorizeRemarks", authorizeRemarks));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("purpose", purpose));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("borrowNo", borrowNo));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("commodityCode", commodityCode));
        strB.append(getTOXml("businessSectorCode", businessSectorCode));
        strB.append(getTOXml("facilityType", facilityType));
        strB.append(getTOXml("purposeCode", purposeCode));
        strB.append(getTOXml("industryCode", industryCode));
        strB.append(getTOXml("twentyCode", twentyCode));
        strB.append(getTOXml("govtSchemeCode", govtSchemeCode));
        strB.append(getTOXml("npaDt", npaDt));
        strB.append(getTOXml("ecgc", ecgc));
        strB.append(getTOXml("documentComplete", documentComplete));
        strB.append(getTOXml("guaranteeCoverCode", guaranteeCoverCode));
        strB.append(getTOXml("healthCode", healthCode));
        strB.append(getTOXml("districtCode", districtCode));
        strB.append(getTOXml("sectorCode", sectorCode));
        strB.append(getTOXml("paymentCode", paymentCode));
        strB.append(getTOXml("refinancingInstitution", refinancingInstitution));
        strB.append(getTOXml("assetStatus", assetStatus));
        strB.append(getTOXml("directFinance", directFinance));
        strB.append(getTOXml("prioritySector", prioritySector));
        strB.append(getTOXml("qis", qis));
        strB.append(getTOXml("authorizeRemarks", authorizeRemarks));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("purpose", purpose));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property purpose.
     *
     * @return Value of property purpose.
     */
    public java.lang.String getPurpose() {
        return purpose;
    }

    /**
     * Setter for property purpose.
     *
     * @param purpose New value of property purpose.
     */
    public void setPurpose(java.lang.String purpose) {
        this.purpose = purpose;
    }
}
