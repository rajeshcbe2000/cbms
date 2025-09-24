/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigTO.java
 * 
 * Created on Thu Jan 20 16:44:08 IST 2005
 */
package com.see.truetransact.transferobject.roomrent.rentregister;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class RentRegisterTO extends TransferObject implements Serializable {

    private String rrId = "", roomNo = "", appNo = "", name = "", houseName = "", place = "", city = "", phNo = "", mobNo = "", emailId = "", guardian = "", nominee = "", recommBy = "", agreNo = "",
            penalGrPeriod = "", aAdvDetails = "", buidingNo, rentDate = "", rentDetails = "", branchCode = "";
    private String rmNumber = "";
    Date occDate, commDate, agrDate;
    Double rentAmt, advAmt;
    private String authorizeStatus = null;
    private String authorizeBy = null;
    private Date authorizeDte = null;
    private String status = null,statusBy = "";
    private Date applDate = null,statusDate = null;

    public Date getApplDate() {
        return applDate;
    }

    public void setApplDate(Date applDate) {
        this.applDate = applDate;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        //setKeyColumns(borrowingNo);
        return rrId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("rrId", rrId));
        strB.append(getTOString("roomNo", roomNo));
        strB.append(getTOString("appNo", appNo));
        strB.append(getTOString("name", name));
        strB.append(getTOString("houseName", houseName));
        strB.append(getTOString("place", place));
        strB.append(getTOString("city", city));
        strB.append(getTOString("phNo", phNo));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDte", authorizeDte));
        strB.append(getTOString("status", status));
        strB.append(getTOString("mobNo", mobNo));
        strB.append(getTOString("emailId", emailId));
        strB.append(getTOString("guardian", guardian));
        strB.append(getTOString("nominee", nominee));
        strB.append(getTOString("recommBy", recommBy));
        strB.append(getTOString("agreNo", agreNo));
        strB.append(getTOString("penalGrPeriod", penalGrPeriod));
        strB.append(getTOString("aAdvDetails", aAdvDetails));
        strB.append(getTOString("applDate", applDate));
        strB.append(getTOString("occDate", occDate));
        strB.append(getTOString("commDate", commDate));
        strB.append(getTOString("agrDate", agrDate));
        strB.append(getTOString("rentDate", rentDate));
        strB.append(getTOString("rentAmt", rentAmt));
        strB.append(getTOString("advAmt", advAmt));
        strB.append(getTOString("rentDetails", rentDetails));
        strB.append(getTOString("rmNumber", rmNumber));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("rrId", rrId));
        strB.append(getTOXml("roomNo", roomNo));
        strB.append(getTOXml("appNo", appNo));
        strB.append(getTOXml("name", name));
        strB.append(getTOXml("houseName", houseName));
        strB.append(getTOXml("place", place));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("phNo", phNo));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDte", authorizeDte));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("mobNo", mobNo));
        strB.append(getTOXml("emailId", emailId));
        strB.append(getTOXml("guardian", guardian));
        strB.append(getTOXml("nominee", nominee));
        strB.append(getTOXml("recommBy", recommBy));
        strB.append(getTOXml("agreNo", agreNo));
        strB.append(getTOXml("penalGrPeriod", penalGrPeriod));
        strB.append(getTOXml("aAdvDetails", aAdvDetails));
        strB.append(getTOXml("applDate", applDate));
        strB.append(getTOXml("occDate", occDate));
        strB.append(getTOXml("commDate", commDate));
        strB.append(getTOXml("agrDate", agrDate));
        strB.append(getTOXml("rentDate", rentDate));
        strB.append(getTOXml("rentAmt", rentAmt));
        strB.append(getTOXml("advAmt", advAmt));
        strB.append(getTOXml("rentDetails", rentDetails));
        strB.append(getTOXml("rmNumber", rmNumber));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property roomNo.
     *
     * @return Value of property roomNo.
     */
    public java.lang.String getRoomNo() {
        return roomNo;
    }

    /**
     * Setter for property roomNo.
     *
     * @param roomNo New value of property roomNo.
     */
    public void setRoomNo(java.lang.String roomNo) {
        this.roomNo = roomNo;
    }

    /**
     * Getter for property appNo.
     *
     * @return Value of property appNo.
     */
    public java.lang.String getAppNo() {
        return appNo;
    }

    /**
     * Setter for property appNo.
     *
     * @param appNo New value of property appNo.
     */
    public void setAppNo(java.lang.String appNo) {
        this.appNo = appNo;
    }

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * Getter for property houseName.
     *
     * @return Value of property houseName.
     */
    public java.lang.String getHouseName() {
        return houseName;
    }

    /**
     * Setter for property houseName.
     *
     * @param houseName New value of property houseName.
     */
    public void setHouseName(java.lang.String houseName) {
        this.houseName = houseName;
    }

    /**
     * Getter for property place.
     *
     * @return Value of property place.
     */
    public java.lang.String getPlace() {
        return place;
    }

    /**
     * Setter for property place.
     *
     * @param place New value of property place.
     */
    public void setPlace(java.lang.String place) {
        this.place = place;
    }

    /**
     * Getter for property city.
     *
     * @return Value of property city.
     */
    public java.lang.String getCity() {
        return city;
    }

    /**
     * Setter for property city.
     *
     * @param city New value of property city.
     */
    public void setCity(java.lang.String city) {
        this.city = city;
    }

    /**
     * Getter for property phNo.
     *
     * @return Value of property phNo.
     */
    public java.lang.String getPhNo() {
        return phNo;
    }

    /**
     * Setter for property phNo.
     *
     * @param phNo New value of property phNo.
     */
    public void setPhNo(java.lang.String phNo) {
        this.phNo = phNo;
    }

    /**
     * Getter for property mobNo.
     *
     * @return Value of property mobNo.
     */
    public java.lang.String getMobNo() {
        return mobNo;
    }

    /**
     * Setter for property mobNo.
     *
     * @param mobNo New value of property mobNo.
     */
    public void setMobNo(java.lang.String mobNo) {
        this.mobNo = mobNo;
    }

    /**
     * Getter for property emailId.
     *
     * @return Value of property emailId.
     */
    public java.lang.String getEmailId() {
        return emailId;
    }

    /**
     * Setter for property emailId.
     *
     * @param emailId New value of property emailId.
     */
    public void setEmailId(java.lang.String emailId) {
        this.emailId = emailId;
    }

    /**
     * Getter for property guardian.
     *
     * @return Value of property guardian.
     */
    public java.lang.String getGuardian() {
        return guardian;
    }

    /**
     * Setter for property guardian.
     *
     * @param guardian New value of property guardian.
     */
    public void setGuardian(java.lang.String guardian) {
        this.guardian = guardian;
    }

    /**
     * Getter for property nominee.
     *
     * @return Value of property nominee.
     */
    public java.lang.String getNominee() {
        return nominee;
    }

    /**
     * Setter for property nominee.
     *
     * @param nominee New value of property nominee.
     */
    public void setNominee(java.lang.String nominee) {
        this.nominee = nominee;
    }

    /**
     * Getter for property recommBy.
     *
     * @return Value of property recommBy.
     */
    public java.lang.String getRecommBy() {
        return recommBy;
    }

    /**
     * Setter for property recommBy.
     *
     * @param recommBy New value of property recommBy.
     */
    public void setRecommBy(java.lang.String recommBy) {
        this.recommBy = recommBy;
    }

    /**
     * Getter for property agreNo.
     *
     * @return Value of property agreNo.
     */
    public java.lang.String getAgreNo() {
        return agreNo;
    }

    /**
     * Setter for property agreNo.
     *
     * @param agreNo New value of property agreNo.
     */
    public void setAgreNo(java.lang.String agreNo) {
        this.agreNo = agreNo;
    }

    /**
     * Getter for property penalGrPeriod.
     *
     * @return Value of property penalGrPeriod.
     */
    public java.lang.String getPenalGrPeriod() {
        return penalGrPeriod;
    }

    /**
     * Setter for property penalGrPeriod.
     *
     * @param penalGrPeriod New value of property penalGrPeriod.
     */
    public void setPenalGrPeriod(java.lang.String penalGrPeriod) {
        this.penalGrPeriod = penalGrPeriod;
    }

    /**
     * Getter for property aAdvDetails.
     *
     * @return Value of property aAdvDetails.
     */
    public java.lang.String getAAdvDetails() {
        return aAdvDetails;
    }

    /**
     * Setter for property aAdvDetails.
     *
     * @param aAdvDetails New value of property aAdvDetails.
     */
    public void setAAdvDetails(java.lang.String aAdvDetails) {
        this.aAdvDetails = aAdvDetails;
    }

    /**
     * Getter for property applDate.
     *
     * @return Value of property applDate.
     */
    /**
     * Setter for property applDate.
     *
     * @param applDate New value of property applDate.
     */
    /**
     * Getter for property occDate.
     *
     * @return Value of property occDate.
     */
    public java.util.Date getOccDate() {
        return occDate;
    }

    /**
     * Setter for property occDate.
     *
     * @param occDate New value of property occDate.
     */
    public void setOccDate(java.util.Date occDate) {
        this.occDate = occDate;
    }

    /**
     * Getter for property commDate.
     *
     * @return Value of property commDate.
     */
    public java.util.Date getCommDate() {
        return commDate;
    }

    /**
     * Setter for property commDate.
     *
     * @param commDate New value of property commDate.
     */
    public void setCommDate(java.util.Date commDate) {
        this.commDate = commDate;
    }

    /**
     * Getter for property agrDate.
     *
     * @return Value of property agrDate.
     */
    public java.util.Date getAgrDate() {
        return agrDate;
    }

    /**
     * Setter for property agrDate.
     *
     * @param agrDate New value of property agrDate.
     */
    public void setAgrDate(java.util.Date agrDate) {
        this.agrDate = agrDate;
    }

    /**
     * Getter for property advAmt.
     *
     * @return Value of property advAmt.
     */
    public java.lang.Double getAdvAmt() {
        return advAmt;
    }

    /**
     * Setter for property advAmt.
     *
     * @param advAmt New value of property advAmt.
     */
    public void setAdvAmt(java.lang.Double advAmt) {
        this.advAmt = advAmt;
    }

    /**
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    /**
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property authorizeDte.
     *
     * @return Value of property authorizeDte.
     */
    public java.util.Date getAuthorizeDte() {
        return authorizeDte;
    }

    /**
     * Setter for property authorizeDte.
     *
     * @param authorizeDte New value of property authorizeDte.
     */
    public void setAuthorizeDte(java.util.Date authorizeDte) {
        this.authorizeDte = authorizeDte;
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
     * Getter for property rentAmt.
     *
     * @return Value of property rentAmt.
     */
    public java.lang.Double getRentAmt() {
        return rentAmt;
    }

    /**
     * Setter for property rentAmt.
     *
     * @param rentAmt New value of property rentAmt.
     */
    public void setRentAmt(java.lang.Double rentAmt) {
        this.rentAmt = rentAmt;
    }

    /**
     * Getter for property rrId.
     *
     * @return Value of property rrId.
     */
    public java.lang.String getRrId() {
        return rrId;
    }

    /**
     * Setter for property rrId.
     *
     * @param rrId New value of property rrId.
     */
    public void setRrId(java.lang.String rrId) {
        this.rrId = rrId;
    }

    /**
     * Getter for property buidingNo.
     *
     * @return Value of property buidingNo.
     */
    public java.lang.String getBuidingNo() {
        return buidingNo;
    }

    /**
     * Setter for property buidingNo.
     *
     * @param buidingNo New value of property buidingNo.
     */
    public void setBuidingNo(java.lang.String buidingNo) {
        this.buidingNo = buidingNo;
    }

    /**
     * Getter for property rentDate.
     *
     * @return Value of property rentDate.
     */
    public String getRentDate() {
        return rentDate;
    }

    /**
     * Setter for property rentDate.
     *
     * @param rentDate New value of property rentDate.
     */
    public void setRentDate(String rentDate) {
        this.rentDate = rentDate;
    }

    /**
     * Getter for property rentDetails.
     *
     * @return Value of property rentDetails.
     */
    /**
     * Getter for property rmNumber.
     *
     * @return Value of property rmNumber.
     */
    /**
     * Getter for property rentDetails.
     *
     * @return Value of property rentDetails.
     */
    public String getRentDetails() {
        return rentDetails;
    }

    /**
     * Setter for property rentDetails.
     *
     * @param rentDetails New value of property rentDetails.
     */
    public void setRentDetails(String rentDetails) {
        this.rentDetails = rentDetails;
    }

    /**
     * Getter for property rmNumber.
     *
     * @return Value of property rmNumber.
     */
    public java.lang.String getRmNumber() {
        return rmNumber;
    }

    /**
     * Setter for property rmNumber.
     *
     * @param rmNumber New value of property rmNumber.
     */
    public void setRmNumber(java.lang.String rmNumber) {
        this.rmNumber = rmNumber;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }
    
}