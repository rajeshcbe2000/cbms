/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TAMMaintenanceCreateTO.java
 * 
 * Created on Tue Jul 13 10:15:06 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.privatebanking.tammaintenance;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PVT_TAM_MAINTENANCE.
 */
public class TAMMaintenanceCreateTO extends TransferObject implements Serializable {

    private String tamId = "";
    private String assetClsId = "";
    private String assetSubClsId = "";
    private String tamOrderType = "";
    private String tamDefType = "";
    private String tamStatus = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;

    /**
     * Setter/Getter for TAM_ID - table Field
     */
    public void setTamId(String tamId) {
        this.tamId = tamId;
    }

    public String getTamId() {
        return tamId;
    }

    /**
     * Setter/Getter for ASSET_CLS_ID - table Field
     */
    public void setAssetClsId(String assetClsId) {
        this.assetClsId = assetClsId;
    }

    public String getAssetClsId() {
        return assetClsId;
    }

    /**
     * Setter/Getter for ASSET_SUB_CLS_ID - table Field
     */
    public void setAssetSubClsId(String assetSubClsId) {
        this.assetSubClsId = assetSubClsId;
    }

    public String getAssetSubClsId() {
        return assetSubClsId;
    }

    /**
     * Setter/Getter for TAM_ORDER_TYPE - table Field
     */
    public void setTamOrderType(String tamOrderType) {
        this.tamOrderType = tamOrderType;
    }

    public String getTamOrderType() {
        return tamOrderType;
    }

    /**
     * Setter/Getter for TAM_DEF_TYPE - table Field
     */
    public void setTamDefType(String tamDefType) {
        this.tamDefType = tamDefType;
    }

    public String getTamDefType() {
        return tamDefType;
    }

    /**
     * Setter/Getter for TAM_STATUS - table Field
     */
    public void setTamStatus(String tamStatus) {
        this.tamStatus = tamStatus;
    }

    public String getTamStatus() {
        return tamStatus;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("tamId");
        return tamId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("tamId", tamId));
        strB.append(getTOString("assetClsId", assetClsId));
        strB.append(getTOString("assetSubClsId", assetSubClsId));
        strB.append(getTOString("tamOrderType", tamOrderType));
        strB.append(getTOString("tamDefType", tamDefType));
        strB.append(getTOString("tamStatus", tamStatus));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("tamId", tamId));
        strB.append(getTOXml("assetClsId", assetClsId));
        strB.append(getTOXml("assetSubClsId", assetSubClsId));
        strB.append(getTOXml("tamOrderType", tamOrderType));
        strB.append(getTOXml("tamDefType", tamDefType));
        strB.append(getTOXml("tamStatus", tamStatus));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}