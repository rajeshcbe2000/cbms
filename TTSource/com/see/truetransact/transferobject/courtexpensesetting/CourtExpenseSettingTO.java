/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpTransferTO.java
 * 
 * Created on Wed Jun 28  2010 swaroop
 */
package com.see.truetransact.transferobject.courtexpensesetting;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import com.see.truetransact.clientutil.ComboBoxModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Table name for this TO is EMP_TRANSFER.
 */
public class CourtExpenseSettingTO extends TransferObject implements Serializable {

    private String id = "";
    private Date date = null;
    private String fromamt = "";
    private String toamt = "";
    private String percentage = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private Date createdDt = null;
    private String createdBy = "";
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String authorizedStatus = null;
    private String directorBoardNo = "";
    private String BranCode = " ";
    private String pid = "";
    private List list = new ArrayList();

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
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property createdDt.
     *
     * @param createdDt New value of property createdDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
    }

    /**
     * Getter for property createdBy.
     *
     * @return Value of property createdBy.
     */
    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for property createdBy.
     *
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
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
     * Getter for property authorizeDt.
     *
     * @return Value of property authorizeDt.
     */
    public java.util.Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter for property authorizeDt.
     *
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.util.Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    /**
     * Getter for property authorizedStatus.
     *
     * @return Value of property authorizedStatus.
     */
    public java.lang.String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter for property authorizedStatus.
     *
     * @param authorizedStatus New value of property authorizedStatus.
     */
    public void setAuthorizedStatus(java.lang.String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    /**
     * Getter for property BranCode.
     *
     * @return Value of property BranCode.
     */
    public String getBranCode() {
        return BranCode;
    }

    /**
     * Setter for property BranCode.
     *
     * @param BranCode New value of property BranCode.
     */
    public void setBranCode(String BranCode) {
        this.BranCode = BranCode;
    }

    /**
     * Getter for property id.
     *
     * @return Value of property id.
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Setter for property id.
     *
     * @param id New value of property id.
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /**
     * Getter for property fromamt.
     *
     * @return Value of property fromamt.
     */
    public java.lang.String getFromamt() {
        return fromamt;
    }

    /**
     * Setter for property fromamt.
     *
     * @param fromamt New value of property fromamt.
     */
    public void setFromamt(java.lang.String fromamt) {
        this.fromamt = fromamt;
    }

    /**
     * Getter for property toamt.
     *
     * @return Value of property toamt.
     */
    public java.lang.String getToamt() {
        return toamt;
    }

    /**
     * Setter for property toamt.
     *
     * @param toamt New value of property toamt.
     */
    public void setToamt(java.lang.String toamt) {
        this.toamt = toamt;
    }

    /**
     * Getter for property percentage.
     *
     * @return Value of property percentage.
     */
    public java.lang.String getPercentage() {
        return percentage;
    }

    /**
     * Setter for property percentage.
     *
     * @param percentage New value of property percentage.
     */
    public void setPercentage(java.lang.String percentage) {
        this.percentage = percentage;
    }

    /**
     * Getter for property date.
     *
     * @return Value of property date.
     */
    public java.util.Date getDate() {
        return date;
    }

    /**
     * Setter for property date.
     *
     * @param date New value of property date.
     */
    public void setDate(java.util.Date date) {
        this.date = date;
    }

    /**
     * Getter for property pid.
     *
     * @return Value of property pid.
     */
    public String getPid() {
        return pid;
    }

    /**
     * Setter for property pid.
     *
     * @param pid New value of property pid.
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * Getter for property list.
     *
     * @return Value of property list.
     */
    public List getList() {
        return list;
    }

    /**
     * Setter for property list.
     *
     * @param list New value of property list.
     */
    public void setList(List list) {
        this.list = list;
    }
}