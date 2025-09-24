/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 *
 * AuthPersonsTO.java
 *
 * Created on January 2, 2007, 11:30 AM
 */
package com.see.truetransact.transferobject.customer;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 *
 * @author Rajesh
 */
public class AuthPersonsTO extends TransferObject implements Serializable {

    private String custID = "";
    private Date dateCreated = null;
    private String photoFile = "";
    private String signatureFile = "";
    private String authCustID = "";

    /**
     * Getter for property custID.
     *
     * @return Value of property custID.
     */
    public java.lang.String getCustID() {
        return custID;
    }

    /**
     * Setter for property custID.
     *
     * @param custID New value of property custID.
     */
    public void setCustID(java.lang.String custID) {
        this.custID = custID;
    }

    /**
     * Getter for property date.
     *
     * @return Value of property date.
     */
    public java.util.Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Setter for property date.
     *
     * @param date New value of property date.
     */
    public void setDateCreated(java.util.Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Getter for property photoFile.
     *
     * @return Value of property photoFile.
     */
    public java.lang.String getPhotoFile() {
        return photoFile;
    }

    /**
     * Setter for property photoFile.
     *
     * @param photoFile New value of property photoFile.
     */
    public void setPhotoFile(java.lang.String photoFile) {
        this.photoFile = photoFile;
    }

    /**
     * Getter for property signatureFile.
     *
     * @return Value of property signatureFile.
     */
    public java.lang.String getSignatureFile() {
        return signatureFile;
    }

    /**
     * Setter for property signatureFile.
     *
     * @param signatureFile New value of property signatureFile.
     */
    public void setSignatureFile(java.lang.String signatureFile) {
        this.signatureFile = signatureFile;
    }

    /**
     * Getter for property authCustID.
     *
     * @return Value of property authCustID.
     */
    public java.lang.String getAuthCustID() {
        return authCustID;
    }

    /**
     * Setter for property authCustID.
     *
     * @param authCustID New value of property authCustID.
     */
    public void setAuthCustID(java.lang.String authCustID) {
        this.authCustID = authCustID;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("custID", custID));
        strB.append(getTOString("dateCreated", dateCreated));
        strB.append(getTOString("photoFile", photoFile));
        strB.append(getTOString("signatureFile", signatureFile));
        strB.append(getTOString("authCustID", authCustID));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("custID", custID));
        strB.append(getTOXml("dateCreated", dateCreated));
        strB.append(getTOXml("photoFile", photoFile));
        strB.append(getTOXml("signatureFile", signatureFile));
        strB.append(getTOXml("authCustID", authCustID));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}
