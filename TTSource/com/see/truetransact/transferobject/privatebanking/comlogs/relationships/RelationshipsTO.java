/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RelationshipsTO.java
 * 
 * Created on Fri Jul 16 15:24:35 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.privatebanking.comlogs.relationships;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PVT_RELATIONSHIP.
 */
public class RelationshipsTO extends TransferObject implements Serializable {

    private String relateId = "";
    private String memberId = "";
    private String bankerName = "";
    private String initiatedBy = "";
    private String leadRso = "";
    private String contactDesc = "";
    private String relateType = "";
    private String relateSource = "";
    private String sourceRef = "";
    private Date contactDt = null;
    private String subType = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String authorizeStatus = null;;

    /**
     * Setter/Getter for RELATE_ID - table Field
     */
    public void setRelateId(String relateId) {
        this.relateId = relateId;
    }

    public String getRelateId() {
        return relateId;
    }

    /**
     * Setter/Getter for MEMBER_ID - table Field
     */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberId() {
        return memberId;
    }

    /**
     * Setter/Getter for BANKER_NAME - table Field
     */
    public void setBankerName(String bankerName) {
        this.bankerName = bankerName;
    }

    public String getBankerName() {
        return bankerName;
    }

    /**
     * Setter/Getter for INITIATED_BY - table Field
     */
    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    /**
     * Setter/Getter for LEAD_RSO - table Field
     */
    public void setLeadRso(String leadRso) {
        this.leadRso = leadRso;
    }

    public String getLeadRso() {
        return leadRso;
    }

    /**
     * Setter/Getter for CONTACT_DESC - table Field
     */
    public void setContactDesc(String contactDesc) {
        this.contactDesc = contactDesc;
    }

    public String getContactDesc() {
        return contactDesc;
    }

    /**
     * Setter/Getter for RELATE_TYPE - table Field
     */
    public void setRelateType(String relateType) {
        this.relateType = relateType;
    }

    public String getRelateType() {
        return relateType;
    }

    /**
     * Setter/Getter for RELATE_SOURCE - table Field
     */
    public void setRelateSource(String relateSource) {
        this.relateSource = relateSource;
    }

    public String getRelateSource() {
        return relateSource;
    }

    /**
     * Setter/Getter for SOURCE_REF - table Field
     */
    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    public String getSourceRef() {
        return sourceRef;
    }

    /**
     * Setter/Getter for CONTACT_DT - table Field
     */
    public void setContactDt(Date contactDt) {
        this.contactDt = contactDt;
    }

    public Date getContactDt() {
        return contactDt;
    }

    /**
     * Setter/Getter for SUB_TYPE - table Field
     */
    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getSubType() {
        return subType;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
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
        strB.append(getTOString("relateId", relateId));
        strB.append(getTOString("memberId", memberId));
        strB.append(getTOString("bankerName", bankerName));
        strB.append(getTOString("initiatedBy", initiatedBy));
        strB.append(getTOString("leadRso", leadRso));
        strB.append(getTOString("contactDesc", contactDesc));
        strB.append(getTOString("relateType", relateType));
        strB.append(getTOString("relateSource", relateSource));
        strB.append(getTOString("sourceRef", sourceRef));
        strB.append(getTOString("contactDt", contactDt));
        strB.append(getTOString("subType", subType));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("relateId", relateId));
        strB.append(getTOXml("memberId", memberId));
        strB.append(getTOXml("bankerName", bankerName));
        strB.append(getTOXml("initiatedBy", initiatedBy));
        strB.append(getTOXml("leadRso", leadRso));
        strB.append(getTOXml("contactDesc", contactDesc));
        strB.append(getTOXml("relateType", relateType));
        strB.append(getTOXml("relateSource", relateSource));
        strB.append(getTOXml("sourceRef", sourceRef));
        strB.append(getTOXml("contactDt", contactDt));
        strB.append(getTOXml("subType", subType));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}