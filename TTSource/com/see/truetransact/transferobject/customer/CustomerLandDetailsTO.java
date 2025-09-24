/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerPassportTO.java
 * 
 * Created on Wed Feb 16 09:38:12 IST 2005 swaroop
 */
package com.see.truetransact.transferobject.customer;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUST_LAND_DETAILS.
 */
public class CustomerLandDetailsTO extends TransferObject implements Serializable {

    private String custId = "";
    private String status = "";
    private String slno = "";
    private String survey_no = "";
    private String irrigated = "";
    private String irrSrc = "";
    private String village = "";
    private String post = "";
    private String hobli = "";
    private String taluk = "";
    private String district = "";
    private String state = "";
    private String pin = "";
    private String location = "";
    private String area = "";

    public String getKeyData() {
        setKeyColumns(custId);
        return custId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));

        strB.append(getTOString("custId", custId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("slno", slno));
        strB.append(getTOString("survey_no", survey_no));
        strB.append(getTOString("irrigated", irrigated));
        strB.append(getTOString("irrSrc", irrSrc));
        strB.append(getTOString("village", village));
        strB.append(getTOString("post", post));
        strB.append(getTOString("hobli", hobli));
        strB.append(getTOString("taluk", taluk));
        strB.append(getTOString("district", district));
        strB.append(getTOString("state", state));
        strB.append(getTOString("pin", pin));
        strB.append(getTOString("location", location));
        strB.append(getTOString("area", area));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));

        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("slno", slno));
        strB.append(getTOXml("survey_no", survey_no));
        strB.append(getTOXml("irrigated", irrigated));
        strB.append(getTOXml("irrSrc", irrSrc));
        strB.append(getTOXml("village", village));
        strB.append(getTOXml("post", post));
        strB.append(getTOXml("hobli", hobli));
        strB.append(getTOXml("taluk", taluk));
        strB.append(getTOXml("district", district));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("pin", pin));
        strB.append(getTOXml("location", location));
        strB.append(getTOXml("area", area));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

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
     * Getter for property slno.
     *
     * @return Value of property slno.
     */
    public java.lang.String getSlno() {
        return slno;
    }

    /**
     * Setter for property slno.
     *
     * @param slno New value of property slno.
     */
    public void setSlno(java.lang.String slno) {
        this.slno = slno;
    }

    /**
     * Getter for property survey_no.
     *
     * @return Value of property survey_no.
     */
    public java.lang.String getSurvey_no() {
        return survey_no;
    }

    /**
     * Setter for property survey_no.
     *
     * @param survey_no New value of property survey_no.
     */
    public void setSurvey_no(java.lang.String survey_no) {
        this.survey_no = survey_no;
    }

    /**
     * Getter for property irrigated.
     *
     * @return Value of property irrigated.
     */
    public java.lang.String getIrrigated() {
        return irrigated;
    }

    /**
     * Setter for property irrigated.
     *
     * @param irrigated New value of property irrigated.
     */
    public void setIrrigated(java.lang.String irrigated) {
        this.irrigated = irrigated;
    }

    /**
     * Getter for property irrSrc.
     *
     * @return Value of property irrSrc.
     */
    public java.lang.String getIrrSrc() {
        return irrSrc;
    }

    /**
     * Setter for property irrSrc.
     *
     * @param irrSrc New value of property irrSrc.
     */
    public void setIrrSrc(java.lang.String irrSrc) {
        this.irrSrc = irrSrc;
    }

    /**
     * Getter for property village.
     *
     * @return Value of property village.
     */
    public java.lang.String getVillage() {
        return village;
    }

    /**
     * Setter for property village.
     *
     * @param village New value of property village.
     */
    public void setVillage(java.lang.String village) {
        this.village = village;
    }

    /**
     * Getter for property post.
     *
     * @return Value of property post.
     */
    public java.lang.String getPost() {
        return post;
    }

    /**
     * Setter for property post.
     *
     * @param post New value of property post.
     */
    public void setPost(java.lang.String post) {
        this.post = post;
    }

    /**
     * Getter for property hobli.
     *
     * @return Value of property hobli.
     */
    public java.lang.String getHobli() {
        return hobli;
    }

    /**
     * Setter for property hobli.
     *
     * @param hobli New value of property hobli.
     */
    public void setHobli(java.lang.String hobli) {
        this.hobli = hobli;
    }

    /**
     * Getter for property taluk.
     *
     * @return Value of property taluk.
     */
    public java.lang.String getTaluk() {
        return taluk;
    }

    /**
     * Setter for property taluk.
     *
     * @param taluk New value of property taluk.
     */
    public void setTaluk(java.lang.String taluk) {
        this.taluk = taluk;
    }

    /**
     * Getter for property district.
     *
     * @return Value of property district.
     */
    public java.lang.String getDistrict() {
        return district;
    }

    /**
     * Setter for property district.
     *
     * @param district New value of property district.
     */
    public void setDistrict(java.lang.String district) {
        this.district = district;
    }

    /**
     * Getter for property state.
     *
     * @return Value of property state.
     */
    public java.lang.String getState() {
        return state;
    }

    /**
     * Setter for property state.
     *
     * @param state New value of property state.
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }

    /**
     * Getter for property pin.
     *
     * @return Value of property pin.
     */
    public java.lang.String getPin() {
        return pin;
    }

    /**
     * Setter for property pin.
     *
     * @param pin New value of property pin.
     */
    public void setPin(java.lang.String pin) {
        this.pin = pin;
    }

    /**
     * Getter for property location.
     *
     * @return Value of property location.
     */
    public java.lang.String getLocation() {
        return location;
    }

    /**
     * Setter for property location.
     *
     * @param location New value of property location.
     */
    public void setLocation(java.lang.String location) {
        this.location = location;
    }

    /**
     * Getter for property area.
     *
     * @return Value of property area.
     */
    public java.lang.String getArea() {
        return area;
    }

    /**
     * Setter for property area.
     *
     * @param area New value of property area.
     */
    public void setArea(java.lang.String area) {
        this.area = area;
    }
}