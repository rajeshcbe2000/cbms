/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSConfigTO.java
 * 
 * Created on Thu Feb 10 15:07:29 IST 2005
 */
package com.see.truetransact.transferobject.tds.tdsconfig;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TDS_CONFIG.
 */
public class TDSConfigTO extends TransferObject implements Serializable {

    private String tdsId = "";
    private Date finStartDt = null;
    private Date finEndDt = null;
    private String tdsScope = "";
    private Double cutOfAmt = null;
    private Double tdsPercentage = null;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String includeCutof = "";
    private String tdsCrAchdId = "";
    private String custType = "";
    private Double withOutPANPercentage = 0.0;

    /**
     * Setter/Getter for TDS_ID - table Field
     */
    public void setTdsId(String tdsId) {
        this.tdsId = tdsId;
    }

    public String getTdsId() {
        return tdsId;
    }

    /**
     * Setter/Getter for FIN_START_DT - table Field
     */
    public void setFinStartDt(Date finStartDt) {
        this.finStartDt = finStartDt;
    }

    public Date getFinStartDt() {
        return finStartDt;
    }

    /**
     * Setter/Getter for FIN_END_DT - table Field
     */
    public void setFinEndDt(Date finEndDt) {
        this.finEndDt = finEndDt;
    }

    public Date getFinEndDt() {
        return finEndDt;
    }

    /**
     * Setter/Getter for TDS_SCOPE - table Field
     */
    public void setTdsScope(String tdsScope) {
        this.tdsScope = tdsScope;
    }

    public String getTdsScope() {
        return tdsScope;
    }

    /**
     * Setter/Getter for CUT_OF_AMT - table Field
     */
    public void setCutOfAmt(Double cutOfAmt) {
        this.cutOfAmt = cutOfAmt;
    }

    public Double getCutOfAmt() {
        return cutOfAmt;
    }

    /**
     * Setter/Getter for TDS_PERCENTAGE - table Field
     */
    public void setTdsPercentage(Double tdsPercentage) {
        this.tdsPercentage = tdsPercentage;
    }

    public Double getTdsPercentage() {
        return tdsPercentage;
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
     * Setter/Getter for INCLUDE_CUTOF - table Field
     */
    public void setIncludeCutof(String includeCutof) {
        this.includeCutof = includeCutof;
    }

    public String getIncludeCutof() {
        return includeCutof;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(tdsId);
        return tdsId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("tdsId", tdsId));
        strB.append(getTOString("finStartDt", finStartDt));
        strB.append(getTOString("finEndDt", finEndDt));
        strB.append(getTOString("tdsScope", tdsScope));
        strB.append(getTOString("cutOfAmt", cutOfAmt));
        strB.append(getTOString("tdsPercentage", tdsPercentage));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("includeCutof", includeCutof));
        strB.append(getTOString("tdsCrAchdId", tdsCrAchdId));
        strB.append(getTOString("custType", custType));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("tdsId", tdsId));
        strB.append(getTOXml("finStartDt", finStartDt));
        strB.append(getTOXml("finEndDt", finEndDt));
        strB.append(getTOXml("tdsScope", tdsScope));
        strB.append(getTOXml("cutOfAmt", cutOfAmt));
        strB.append(getTOXml("tdsPercentage", tdsPercentage));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("includeCutof", includeCutof));
        strB.append(getTOXml("tdsCrAchdId", tdsCrAchdId));
        strB.append(getTOXml("custType", custType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property tdsCrAchdId.
     *
     * @return Value of property tdsCrAchdId.
     */
    public java.lang.String getTdsCrAchdId() {
        return tdsCrAchdId;
    }

    /**
     * Setter for property tdsCrAchdId.
     *
     * @param tdsCrAchdId New value of property tdsCrAchdId.
     */
    public void setTdsCrAchdId(java.lang.String tdsCrAchdId) {
        this.tdsCrAchdId = tdsCrAchdId;
    }

    /**
     * Getter for property custType.
     *
     * @return Value of property custType.
     */
    public java.lang.String getCustType() {
        return custType;
    }

    /**
     * Setter for property custType.
     *
     * @param custType New value of property custType.
     */
    public void setCustType(java.lang.String custType) {
        this.custType = custType;
    }

    public Double getWithOutPANPercentage() {
        return withOutPANPercentage;
    }

    public void setWithOutPANPercentage(Double withOutPANPercentage) {
        this.withOutPANPercentage = withOutPANPercentage;
    }
    
}