/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 *
 * GahanCustomerDetailsTO.java
 *
 * Created on April 23, 2012, 5:55 PM
 */
package com.see.truetransact.transferobject.customer.gahan;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;
import java.lang.StringBuffer;

/**
 *
 * @author admin
 */
public class GahanCustomerDetailsTO extends TransferObject implements Serializable {

    String documentNo = null;
    String custId = null;
    String ownerMember = null;
    String constitution = null;
    String status = null;
    String authStatus = null;
    String authorizedBy = null;
    Date authorizedDt = null;
    String statusBy = null;
    Date statusDt = null;
    String documentGenId = null;
    String itemsSubmitting = null;

    public String getKeyData() {
        setKeyColumns("documentNo");
        return documentNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("documentNo", documentNo));
        strB.append(getTOString("documentGenId", documentGenId));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("ownerMember", ownerMember));
        strB.append(getTOString("constitution", constitution));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authStatus", authStatus));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("itemsSubmitting", itemsSubmitting));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("documentNo", documentNo));
        strB.append(getTOXml("documentGenId", documentGenId));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("ownerMember", ownerMember));
        strB.append(getTOXml("constitution", constitution));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authStatus", authStatus));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("itemsSubmitting", itemsSubmitting));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property documentNo.
     *
     * @return Value of property documentNo.
     */
    public java.lang.String getDocumentNo() {
        return documentNo;
    }

    /**
     * Setter for property documentNo.
     *
     * @param documentNo New value of property documentNo.
     */
    public void setDocumentNo(java.lang.String documentNo) {
        this.documentNo = documentNo;
    }
    
    /**
     * Getter for property itemsSubmitting.
     *
     * @return Value of property itemsSubmitting.
     */

    public java.lang.String getItemsSubmitting() {
        return itemsSubmitting;
    }
    
    /**
     * Setter for property itemsSubmitting.
     *
     * @param documentNo New value of property itemsSubmitting.
     */

    public void setItemsSubmitting(java.lang.String itemsSubmitting) {
        this.itemsSubmitting = itemsSubmitting;
    }
    
    /**
     * Getter for property itemsSubmitting.
     *
     * @return Value of property itemsSubmitting.
     */
    
    
    
    

    /**
     * Getter for property custId.
     *
     * @return Value of property custId.
     */
    public java.lang.String getCustId() {
        return custId;
    }

    /**
     * Setter for property custId.
     *
     * @param custId New value of property custId.
     */
    public void setCustId(java.lang.String custId) {
        this.custId = custId;
    }

    /**
     * Getter for property constitution.
     *
     * @return Value of property constitution.
     */
    public java.lang.String getConstitution() {
        return constitution;
    }

    /**
     * Setter for property constitution.
     *
     * @param constitution New value of property constitution.
     */
    public void setConstitution(java.lang.String constitution) {
        this.constitution = constitution;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Getter for property authStatus.
     *
     * @return Value of property authStatus.
     */
    public java.lang.String getAuthStatus() {
        return authStatus;
    }

    /**
     * Setter for property authStatus.
     *
     * @param authStatus New value of property authStatus.
     */
    public void setAuthStatus(java.lang.String authStatus) {
        this.authStatus = authStatus;
    }

    /**
     * Getter for property authorizedBy.
     *
     * @return Value of property authorizedBy.
     */
    public java.lang.String getAuthorizedBy() {
        return authorizedBy;
    }

    /**
     * Setter for property authorizedBy.
     *
     * @param authorizedBy New value of property authorizedBy.
     */
    public void setAuthorizedBy(java.lang.String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    /**
     * Getter for property authorizedDt.
     *
     * @return Value of property authorizedDt.
     */
    public java.util.Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter for property authorizedDt.
     *
     * @param authorizedDt New value of property authorizedDt.
     */
    public void setAuthorizedDt(java.util.Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    /**
     * Getter for property statusBy.
     *
     * @return Value of property statusBy.
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
    }

    /**
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public java.util.Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
    }

    /**
     * Getter for property documentGenId.
     *
     * @return Value of property documentGenId.
     */
    public java.lang.String getDocumentGenId() {
        return documentGenId;
    }

    /**
     * Setter for property documentGenId.
     *
     * @param documentGenId New value of property documentGenId.
     */
    public void setDocumentGenId(java.lang.String documentGenId) {
        this.documentGenId = documentGenId;
    }

    /**
     * Getter for property ownerMember.
     *
     * @return Value of property ownerMember.
     */
    public java.lang.String getOwnerMember() {
        return ownerMember;
    }

    /**
     * Setter for property ownerMember.
     *
     * @param ownerMember New value of property ownerMember.
     */
    public void setOwnerMember(java.lang.String ownerMember) {
        this.ownerMember = ownerMember;
    }
}
