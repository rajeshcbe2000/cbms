/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositRolloverTO.java
 * 
 * Created on Fri Jun 18 11:29:56 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.privatebanking.orders;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PVT_ORDER_MASTER.
 */
public class DepositRolloverTO extends TransferObject implements Serializable {

    private String ordId = "";
    private String member = "";
    private String relationship = "";
    private String orderType = "";
    private String contactMode = "";
    private Date contactDt = null;
    private String contactHr = "";
    private String contactMins = "";
    private String clientContact = "";
    private String phoneExt = "";
    private String instructionFrom = "";
    private String solicited = "";
    private Date sourDocDt = null;
    private String description = "";
    private String authSourDoc = "";
    private String sourDocDetails = "";
    private String viewVisual = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private String authorizeUser = "";
    private Date authorizeDt = null;
    private String authorizeRemark = "";

    /**
     * Setter/Getter for ORD_ID - table Field
     */
    public void setOrdId(String ordId) {
        this.ordId = ordId;
    }

    public String getOrdId() {
        return ordId;
    }

    /**
     * Setter/Getter for MEMBER - table Field
     */
    public void setMember(String member) {
        this.member = member;
    }

    public String getMember() {
        return member;
    }

    /**
     * Setter/Getter for RELATIONSHIP - table Field
     */
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRelationship() {
        return relationship;
    }

    /**
     * Setter/Getter for ORDER_TYPE - table Field
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderType() {
        return orderType;
    }

    /**
     * Setter/Getter for CONTACT_MODE - table Field
     */
    public void setContactMode(String contactMode) {
        this.contactMode = contactMode;
    }

    public String getContactMode() {
        return contactMode;
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
     * Setter/Getter for CONTACT_HR - table Field
     */
    public void setContactHr(String contactHr) {
        this.contactHr = contactHr;
    }

    public String getContactHr() {
        return contactHr;
    }

    /**
     * Setter/Getter for CONTACT_MINS - table Field
     */
    public void setContactMins(String contactMins) {
        this.contactMins = contactMins;
    }

    public String getContactMins() {
        return contactMins;
    }

    /**
     * Setter/Getter for CLIENT_CONTACT - table Field
     */
    public void setClientContact(String clientContact) {
        this.clientContact = clientContact;
    }

    public String getClientContact() {
        return clientContact;
    }

    /**
     * Setter/Getter for PHONE_EXT - table Field
     */
    public void setPhoneExt(String phoneExt) {
        this.phoneExt = phoneExt;
    }

    public String getPhoneExt() {
        return phoneExt;
    }

    /**
     * Setter/Getter for INSTRUCTION_FROM - table Field
     */
    public void setInstructionFrom(String instructionFrom) {
        this.instructionFrom = instructionFrom;
    }

    public String getInstructionFrom() {
        return instructionFrom;
    }

    /**
     * Setter/Getter for SOLICITED - table Field
     */
    public void setSolicited(String solicited) {
        this.solicited = solicited;
    }

    public String getSolicited() {
        return solicited;
    }

    /**
     * Setter/Getter for SOUR_DOC_DT - table Field
     */
    public void setSourDocDt(Date sourDocDt) {
        this.sourDocDt = sourDocDt;
    }

    public Date getSourDocDt() {
        return sourDocDt;
    }

    /**
     * Setter/Getter for DESCRIPTION - table Field
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Setter/Getter for AUTH_SOUR_DOC - table Field
     */
    public void setAuthSourDoc(String authSourDoc) {
        this.authSourDoc = authSourDoc;
    }

    public String getAuthSourDoc() {
        return authSourDoc;
    }

    /**
     * Setter/Getter for SOUR_DOC_DETAILS - table Field
     */
    public void setSourDocDetails(String sourDocDetails) {
        this.sourDocDetails = sourDocDetails;
    }

    public String getSourDocDetails() {
        return sourDocDetails;
    }

    /**
     * Setter/Getter for VIEW_VISUAL - table Field
     */
    public void setViewVisual(String viewVisual) {
        this.viewVisual = viewVisual;
    }

    public String getViewVisual() {
        return viewVisual;
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
     * Setter/Getter for AUTHORIZE_USER - table Field
     */
    public void setAuthorizeUser(String authorizeUser) {
        this.authorizeUser = authorizeUser;
    }

    public String getAuthorizeUser() {
        return authorizeUser;
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
        setKeyColumns("ordId");
        return ordId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("ordId", ordId));
        strB.append(getTOString("member", member));
        strB.append(getTOString("relationship", relationship));
        strB.append(getTOString("orderType", orderType));
        strB.append(getTOString("contactMode", contactMode));
        strB.append(getTOString("contactDt", contactDt));
        strB.append(getTOString("contactHr", contactHr));
        strB.append(getTOString("contactMins", contactMins));
        strB.append(getTOString("clientContact", clientContact));
        strB.append(getTOString("phoneExt", phoneExt));
        strB.append(getTOString("instructionFrom", instructionFrom));
        strB.append(getTOString("solicited", solicited));
        strB.append(getTOString("sourDocDt", sourDocDt));
        strB.append(getTOString("description", description));
        strB.append(getTOString("authSourDoc", authSourDoc));
        strB.append(getTOString("sourDocDetails", sourDocDetails));
        strB.append(getTOString("viewVisual", viewVisual));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeUser", authorizeUser));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeRemark", authorizeRemark));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("ordId", ordId));
        strB.append(getTOXml("member", member));
        strB.append(getTOXml("relationship", relationship));
        strB.append(getTOXml("orderType", orderType));
        strB.append(getTOXml("contactMode", contactMode));
        strB.append(getTOXml("contactDt", contactDt));
        strB.append(getTOXml("contactHr", contactHr));
        strB.append(getTOXml("contactMins", contactMins));
        strB.append(getTOXml("clientContact", clientContact));
        strB.append(getTOXml("phoneExt", phoneExt));
        strB.append(getTOXml("instructionFrom", instructionFrom));
        strB.append(getTOXml("solicited", solicited));
        strB.append(getTOXml("sourDocDt", sourDocDt));
        strB.append(getTOXml("description", description));
        strB.append(getTOXml("authSourDoc", authSourDoc));
        strB.append(getTOXml("sourDocDetails", sourDocDetails));
        strB.append(getTOXml("viewVisual", viewVisual));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeUser", authorizeUser));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeRemark", authorizeRemark));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}