
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * RTGSOtherBankBranchTo.java
 */

package com.see.truetransact.transferobject.product.remittance;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Suresh R
 */
public class RTGSOtherBankBranchTo extends TransferObject implements Serializable {

    private Integer txtBankCode = new Integer(0);
    private String txtIFSCCode = "";
    private String txtMICRCode = "";
    private String txtBranchName = "";
    private String txtAreaAddress = "";
    private String txtContactNo = "";
    private String txtCity = "";
    private String txtDistrict = "";
    private String txtState = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";

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
        strB.append(getTOString("txtBankCode", txtBankCode));
        strB.append(getTOString("txtIFSCCode", txtIFSCCode));
        strB.append(getTOString("txtMICRCode", txtMICRCode));
        strB.append(getTOString("txtBranchName", txtBranchName));
        strB.append(getTOString("txtAreaAddress", txtAreaAddress));
        strB.append(getTOString("txtContactNo", txtContactNo));
        strB.append(getTOString("txtCity", txtCity));
        strB.append(getTOString("txtDistrict", txtDistrict));
        strB.append(getTOString("txtState", txtState));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("txtBankCode", txtBankCode));
        strB.append(getTOXml("txtIFSCCode", txtIFSCCode));
        strB.append(getTOXml("txtMICRCode", txtMICRCode));
        strB.append(getTOXml("txtBranchName", txtBranchName));
        strB.append(getTOXml("txtAreaAddress", txtAreaAddress));
        strB.append(getTOXml("txtContactNo", txtContactNo));
        strB.append(getTOXml("txtCity", txtCity));
        strB.append(getTOXml("txtDistrict", txtDistrict));
        strB.append(getTOXml("txtState", txtState));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public Integer getTxtBankCode() {
        return txtBankCode;
    }

    public void setTxtBankCode(Integer txtBankCode) {
        this.txtBankCode = txtBankCode;
    }


    public String getTxtIFSCCode() {
        return txtIFSCCode;
    }

    public void setTxtIFSCCode(String txtIFSCCode) {
        this.txtIFSCCode = txtIFSCCode;
    }

    public String getTxtMICRCode() {
        return txtMICRCode;
    }

    public void setTxtMICRCode(String txtMICRCode) {
        this.txtMICRCode = txtMICRCode;
    }

    public String getTxtBranchName() {
        return txtBranchName;
    }

    public void setTxtBranchName(String txtBranchName) {
        this.txtBranchName = txtBranchName;
    }

    public String getTxtAreaAddress() {
        return txtAreaAddress;
    }

    public void setTxtAreaAddress(String txtAreaAddress) {
        this.txtAreaAddress = txtAreaAddress;
    }

    public String getTxtContactNo() {
        return txtContactNo;
    }

    public void setTxtContactNo(String txtContactNo) {
        this.txtContactNo = txtContactNo;
    }

    public String getTxtCity() {
        return txtCity;
    }

    public void setTxtCity(String txtCity) {
        this.txtCity = txtCity;
    }

    public String getTxtDistrict() {
        return txtDistrict;
    }

    public void setTxtDistrict(String txtDistrict) {
        this.txtDistrict = txtDistrict;
    }

    public String getTxtState() {
        return txtState;
    }

    public void setTxtState(String txtState) {
        this.txtState = txtState;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }
}