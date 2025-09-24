/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TradingSalesRetTO.java
 */

package com.see.truetransact.transferobject.trading.tradingsales;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author Revathi L
 *
 */
public class TradingSalesRetTO extends TransferObject implements Serializable {

    private String salesRetNo = "";
    private String custID = "";
    private String Name = "";
    private String salesNo = "";
    private Date salesDate = null;
    private String salesType = "";
    private String bankAcHD = "";
    private Date salesRetDt = null;
    private String sales = "";
    private String salReturn = "";
    private String slNo = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public String getSalesNo() {
        return salesNo;
    }

    public void setSalesNo(String salesNo) {
        this.salesNo = salesNo;
    }

    public String getSalesType() {
        return salesType;
    }

    public void setSalesType(String salesType) {
        this.salesType = salesType;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }
    
    

    public String getKeyData() {
        setKeyColumns(salesNo);
        return salesNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("salesRetNo", salesRetNo));
        strB.append(getTOString("custID", custID));
        strB.append(getTOString("Name", Name));
        strB.append(getTOString("salesNo", salesNo));
        strB.append(getTOString("salesDate", salesDate));
        strB.append(getTOString("salesType", salesType));
        strB.append(getTOString("bankAcHD", bankAcHD));
        strB.append(getTOString("salesRetDt", salesRetDt));
        strB.append(getTOString("sales", sales));
        strB.append(getTOString("salReturn", salReturn));
        strB.append(getTOString("slNo", slNo));
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
        strB.append(getTOXml("salesRetNo", salesRetNo));
        strB.append(getTOXml("custID", custID));
        strB.append(getTOXml("Name", Name));
        strB.append(getTOXml("salesNo", salesNo));
        strB.append(getTOXml("salesType", salesType));
        strB.append(getTOXml("salesDate", salesDate));
        strB.append(getTOXml("bankAcHD", bankAcHD));
        strB.append(getTOXml("salesRetDt", salesRetDt));
        strB.append(getTOXml("sales", sales));
        strB.append(getTOXml("salReturn", salReturn));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getSalesRetNo() {
        return salesRetNo;
    }

    public void setSalesRetNo(String salesRetNo) {
        this.salesRetNo = salesRetNo;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getBankAcHD() {
        return bankAcHD;
    }

    public void setBankAcHD(String bankAcHD) {
        this.bankAcHD = bankAcHD;
    }

    public Date getSalesRetDt() {
        return salesRetDt;
    }

    public void setSalesRetDt(Date salesRetDt) {
        this.salesRetDt = salesRetDt;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getSalReturn() {
        return salReturn;
    }

    public void setSalReturn(String salReturn) {
        this.salReturn = salReturn;
    }
}
