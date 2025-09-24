/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OperativeAcctProductTO.java
 * 
 * Created on Sun May 02 08:59:28 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.product.operativeacct;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OP_AC_PRODUCT.
 */
public class OperativeAcctProductTO extends TransferObject implements Serializable {

    private String acHdId = "";
    private String prodId = "";
    private String prodDesc = "";
    private String behavior = "";
    private String status = "";
    private String suserId = "";
    private Date sDate = null;
    private String sRemarks = "";
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String authorizedStatus = null;
    private String baseCurrency = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String authorizeRemark = "";
    // Added by nithya on 17-03-2016 for 0004021
    private String isdebitWithdrawalCharge = "";
    private Integer chargePeriod = null;
    
    public Integer getChargePeriod() {
        return chargePeriod;
    }

    public void setChargePeriod(Integer chargePeriod) {
        this.chargePeriod = chargePeriod;
    }

   
    public String getIsdebitWithdrawalCharge() {
        return isdebitWithdrawalCharge;
    }

   
    public void setIsdebitWithdrawalCharge(String isdebitWithdrawalCharge) {
        this.isdebitWithdrawalCharge = isdebitWithdrawalCharge;
    }
    
    // End
    /**
     * Setter/Getter for AC_HD_ID - table Field
     */
    public void setAcHdId(String acHdId) {
        this.acHdId = acHdId;
    }

    public String getAcHdId() {
        return acHdId;
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
     * Setter/Getter for PROD_DESC - table Field
     */
    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    /**
     * Setter/Getter for BEHAVIOR - table Field
     */
    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getBehavior() {
        return behavior;
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
     * Setter/Getter for SUSER_ID - table Field
     */
    public void setSuserId(String suserId) {
        this.suserId = suserId;
    }

    public String getSuserId() {
        return suserId;
    }

    /**
     * Setter/Getter for S_DATE - table Field
     */
    public void setSDate(Date sDate) {
        this.sDate = sDate;
    }

    public Date getSDate() {
        return sDate;
    }

    /**
     * Setter/Getter for S_REMARKS - table Field
     */
    public void setSRemarks(String sRemarks) {
        this.sRemarks = sRemarks;
    }

    public String getSRemarks() {
        return sRemarks;
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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter/Getter for BASE_CURRENCY - table Field
     */
    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getBaseCurrency() {
        return baseCurrency;
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
     * Setter/Getter for AUTHORIZE_REMARK - table Field
     */
    public void setAuthorizeRemark(String authorizeRemark) {
        this.authorizeRemark = authorizeRemark;
    }

    public String getAuthorizeRemark() {
        return authorizeRemark;
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
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("prodDesc", prodDesc));
        strB.append(getTOString("behavior", behavior));
        strB.append(getTOString("status", status));
        strB.append(getTOString("suserId", suserId));
        strB.append(getTOString("sDate", sDate));
        strB.append(getTOString("sRemarks", sRemarks));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("baseCurrency", baseCurrency));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("authorizeRemark", authorizeRemark));
        strB.append(getTOString("isdebitWithdrawalCharge", isdebitWithdrawalCharge)); // Added by nithya on 17-03-2016 for 0004021
        strB.append(getTOString("chargePeriod", chargePeriod));   // Added by nithya on 17-03-2016 for 0004021    
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("prodDesc", prodDesc));
        strB.append(getTOXml("behavior", behavior));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("suserId", suserId));
        strB.append(getTOXml("sDate", sDate));
        strB.append(getTOXml("sRemarks", sRemarks));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("baseCurrency", baseCurrency));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("authorizeRemark", authorizeRemark));
        strB.append(getTOXml("isdebitWithdrawalCharge", isdebitWithdrawalCharge));  // Added by nithya on 17-03-2016 for 0004021
        strB.append(getTOXml("chargePeriod", chargePeriod)); // Added by nithya on 17-03-2016 for 0004021       
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}