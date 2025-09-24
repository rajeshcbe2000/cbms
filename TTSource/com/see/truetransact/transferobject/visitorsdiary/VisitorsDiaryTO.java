/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * VisitorsDiaryTO.java
 *
 * Created on 16 September, 2011, 2:24 PM
 */
package com.see.truetransact.transferobject.visitorsdiary;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
import java.util.*;

/**
 *
 * @author aravind
 */
public class VisitorsDiaryTO extends TransferObject implements Serializable {

    private String visitorsid = "";
    private Date dateofVisit = null;
    private String instNameAddress = "";
    private String nameAddress = "";
    private String purposeofVisit = "";
    private String commentsLeft = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String status = "";
    private Date createdDt = null;
    private String currBranName = "";
    private String branCode = "";
    private String createdBy = "";
    private HashMap _authorizeMap;
    private Integer result;

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("visitorsid", visitorsid));
        strB.append(getTOString("dateofVisit", dateofVisit));
        strB.append(getTOString("instNameAddress", instNameAddress));
        strB.append(getTOString("nameAddress", nameAddress));
        strB.append(getTOString("purposeofVisit", purposeofVisit));
        strB.append(getTOString("commentsLeft", commentsLeft));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("currBranName", currBranName));
        strB.append(getTOString("branCode", branCode));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        //strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("visitorsid", visitorsid));
        strB.append(getTOXml("dateofVisit", dateofVisit));
        strB.append(getTOXml("instNameAddress", instNameAddress));
        strB.append(getTOXml("nameAddress", nameAddress));
        strB.append(getTOXml("purposeofVisit", purposeofVisit));
        strB.append(getTOXml("commentsLeft", commentsLeft));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("currBranName", currBranName));
        strB.append(getTOXml("branCode", branCode));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property visitorsid.
     *
     * @return Value of property visitorsid.
     */
    public java.lang.String getVisitorsid() {
        return visitorsid;
    }

    /**
     * Setter for property visitorsid.
     *
     * @param visitorsid New value of property visitorsid.
     */
    public void setVisitorsid(java.lang.String visitorsid) {
        this.visitorsid = visitorsid;
    }

    /**
     * Getter for property nameAddress.
     *
     * @return Value of property nameAddress.
     */
    public java.lang.String getNameAddress() {
        return nameAddress;
    }

    /**
     * Setter for property nameAddress.
     *
     * @param nameAddress New value of property nameAddress.
     */
    public void setNameAddress(java.lang.String nameAddress) {
        this.nameAddress = nameAddress;
    }

    /**
     * Getter for property instNameAddress.
     *
     * @return Value of property instNameAddress.
     */
    public java.lang.String getInstNameAddress() {
        return instNameAddress;
    }

    /**
     * Setter for property instNameAddress.
     *
     * @param instNameAddress New value of property instNameAddress.
     */
    public void setInstNameAddress(java.lang.String instNameAddress) {
        this.instNameAddress = instNameAddress;
    }

    /**
     * Getter for property purposeofVisit.
     *
     * @return Value of property purposeofVisit.
     */
    public java.lang.String getPurposeofVisit() {
        return purposeofVisit;
    }

    /**
     * Setter for property purposeofVisit.
     *
     * @param purposeofVisit New value of property purposeofVisit.
     */
    public void setPurposeofVisit(java.lang.String purposeofVisit) {
        this.purposeofVisit = purposeofVisit;
    }

    /**
     * Getter for property commentsLeft.
     *
     * @return Value of property commentsLeft.
     */
    public java.lang.String getCommentsLeft() {
        return commentsLeft;
    }

    /**
     * Setter for property commentsLeft.
     *
     * @param commentsLeft New value of property commentsLeft.
     */
    public void setCommentsLeft(java.lang.String commentsLeft) {
        this.commentsLeft = commentsLeft;
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
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    /**
     * Getter for property currBranName.
     *
     * @return Value of property currBranName.
     */
    public java.lang.String getCurrBranName() {
        return currBranName;
    }

    /**
     * Setter for property currBranName.
     *
     * @param currBranName New value of property currBranName.
     */
    public void setCurrBranName(java.lang.String currBranName) {
        this.currBranName = currBranName;
    }

    /**
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public Integer getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(Integer result) {
        this.result = result;
    }

    /**
     * Getter for property dateofVisit.
     *
     * @return Value of property dateofVisit.
     */
    public java.util.Date getDateofVisit() {
        return dateofVisit;
    }

    /**
     * Setter for property dateofVisit.
     *
     * @param dateofVisit New value of property dateofVisit.
     */
    public void setDateofVisit(java.util.Date dateofVisit) {
        this.dateofVisit = dateofVisit;
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
     * Getter for property cratedDt.
     *
     * @return Value of property cratedDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property cratedDt.
     *
     * @param cratedDt New value of property cratedDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
    }

    /**
     * Getter for property createdBy.
     *
     * @return Value of property createdBy.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for property createdBy.
     *
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Getter for property branCode.
     *
     * @return Value of property branCode.
     */
    public java.lang.String getBranCode() {
        return branCode;
    }

    /**
     * Setter for property branCode.
     *
     * @param branCode New value of property branCode.
     */
    public void setBranCode(java.lang.String branCode) {
        this.branCode = branCode;
    }
}
