/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GroupMasterTO.java
 * 
 * Created on Tue Apr 12 11:42:04 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.group;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is GROUP_MASTER.
 */
public class GroupMasterTO extends TransferObject implements Serializable {

    private String branchGroupId = "";
    private String groupId = "";
    private String groupName = "";
    private String status = "";
    private String branchCode = "";
    private String branchGroup = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus1 = null;
    private String authorizeStatus2 = null;
    private String authorizeBy1 = "";
    private String authorizeBy2 = "";
    private Date authorizeDt1 = null;
    private Date authorizeDt2 = null;

    /**
     * Setter/Getter for Branch_GROUP_ID - table Field
     */
    public void setBranchGroupId(String branchGroupId) {
        this.branchGroupId = branchGroupId;
    }

    public String getBranchGroupId() {
        return branchGroupId;
    }

    /**
     * Setter/Getter for GROUP_ID - table Field
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    /**
     * Setter/Getter for GROUP_NAME - table Field
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
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
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter/Getter for BRANCH_GROUP - table Field
     */
    public void setBranchGroup(String branchGroup) {
        this.branchGroup = branchGroup;
    }

    public String getBranchGroup() {
        return branchGroup;
    }

    /**
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
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
     * Setter/Getter for AUTHORIZE_STATUS_1 - table Field
     */
    public void setAuthorizeStatus1(String authorizeStatus1) {
        this.authorizeStatus1 = authorizeStatus1;
    }

    public String getAuthorizeStatus1() {
        return authorizeStatus1;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS_2 - table Field
     */
    public void setAuthorizeStatus2(String authorizeStatus2) {
        this.authorizeStatus2 = authorizeStatus2;
    }

    public String getAuthorizeStatus2() {
        return authorizeStatus2;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY_1 - table Field
     */
    public void setAuthorizeBy1(String authorizeBy1) {
        this.authorizeBy1 = authorizeBy1;
    }

    public String getAuthorizeBy1() {
        return authorizeBy1;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY_2 - table Field
     */
    public void setAuthorizeBy2(String authorizeBy2) {
        this.authorizeBy2 = authorizeBy2;
    }

    public String getAuthorizeBy2() {
        return authorizeBy2;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT_1 - table Field
     */
    public void setAuthorizeDt1(Date authorizeDt1) {
        this.authorizeDt1 = authorizeDt1;
    }

    public Date getAuthorizeDt1() {
        return authorizeDt1;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT_2 - table Field
     */
    public void setAuthorizeDt2(Date authorizeDt2) {
        this.authorizeDt2 = authorizeDt2;
    }

    public Date getAuthorizeDt2() {
        return authorizeDt2;
    }

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
        strB.append(getTOString("groupId", groupId));
        strB.append(getTOString("groupName", groupName));
        strB.append(getTOString("status", status));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("branchGroup", branchGroup));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus1", authorizeStatus1));
        strB.append(getTOString("authorizeStatus2", authorizeStatus2));
        strB.append(getTOString("authorizeBy1", authorizeBy1));
        strB.append(getTOString("authorizeBy2", authorizeBy2));
        strB.append(getTOString("authorizeDt1", authorizeDt1));
        strB.append(getTOString("authorizeDt2", authorizeDt2));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("groupId", groupId));
        strB.append(getTOXml("groupName", groupName));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("branchGroup", branchGroup));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus1", authorizeStatus1));
        strB.append(getTOXml("authorizeStatus2", authorizeStatus2));
        strB.append(getTOXml("authorizeBy1", authorizeBy1));
        strB.append(getTOXml("authorizeBy2", authorizeBy2));
        strB.append(getTOXml("authorizeDt1", authorizeDt1));
        strB.append(getTOXml("authorizeDt2", authorizeDt2));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}