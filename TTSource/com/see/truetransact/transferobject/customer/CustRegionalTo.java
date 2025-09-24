/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 *  CustRegionalTo.java
 * 
 * Created on Wed Jul 27 16:13:11 GMT+05:30 2005
 */
package com.see.truetransact.transferobject.customer;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 14-05-2015
 */
public class CustRegionalTo extends TransferObject implements Serializable {

    private String custId = "";
    private String fname = "";
    private String careOfName = "";
    private String amsam = "";
    private String desam = "";
    private String place="";
    private String village="";
    private String taluk="";
    private String city="";
    private String country="";
    private String state="";
    private String houseName="";
    private String status = "";
    private String branch_code ="";
    private String statusBy = "";
    private Date statusDt = null;
    private String memNo ="";

    public String getMemNo() {
        return memNo;
    }

    public void setMemNo(String memNo) {
        this.memNo = memNo;
    }
    

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
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
    
    
    public String getAmsam() {
        return amsam;
    }

    public void setAmsam(String amsam) {
        this.amsam = amsam;
    }

    public String getCareOfName() {
        return careOfName;
    }

    public void setCareOfName(String careOfName) {
        this.careOfName = careOfName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getDesam() {
        return desam;
    }

    public void setDesam(String desam) {
        this.desam = desam;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }
    

   

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(custId);
        return custId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("fname", fname));
        strB.append(getTOString("houseName", houseName));
        strB.append(getTOString("amsam", amsam));
        strB.append(getTOString("desam", desam));
        strB.append(getTOString("place", place));
        strB.append(getTOString("village", village));
        strB.append(getTOString("city", city));
        strB.append(getTOString("taluk", taluk));
        strB.append(getTOString("country", country));
        strB.append(getTOString("state", state));
        strB.append(getTOString("careOfName", careOfName));
        strB.append(getTOString("branch_code", branch_code));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("memNo", memNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("fname", fname));
        strB.append(getTOXml("houseName", houseName));
        strB.append(getTOXml("amsam", amsam));
        strB.append(getTOXml("desam", desam));
        strB.append(getTOXml("place", place));
        strB.append(getTOXml("village", village));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("taluk", taluk));
        strB.append(getTOXml("country", country));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("careOfName", careOfName));
        strB.append(getTOXml("branch_code", branch_code));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("memNo", memNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    
}