/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RoleMasterTO.java
 * 
 * Created on Wed Jun 01 11:03:31 GMT+05:30 2005
 */
package com.see.truetransact.transferobject.sysadmin.role;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is ROLE_MASTER.
 */
public class RoleMasterTO extends TransferObject implements Serializable {

    private String roleId = "";
    private String roleName = "";
    private String groupId = "";
    private String status = "";
    private String levelId = "";
    private String accessAllBranch = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String foreignLevelId = "";
    private Double hierarchyId = null;
    private String sameHierarchyAllowed = "";

    /**
     * Setter/Getter for ROLE_ID - table Field
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }

    /**
     * Setter/Getter for ROLE_NAME - table Field
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
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
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for LEVEL_ID - table Field
     */
    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getLevelId() {
        return levelId;
    }

    /**
     * Setter/Getter for ACCESS_ALL_BRANCH - table Field
     */
    public void setAccessAllBranch(String accessAllBranch) {
        this.accessAllBranch = accessAllBranch;
    }

    public String getAccessAllBranch() {
        return accessAllBranch;
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
     * Setter/Getter for FOREIGN_LEVEL_ID - table Field
     */
    public void setForeignLevelId(String foreignLevelId) {
        this.foreignLevelId = foreignLevelId;
    }

    public String getForeignLevelId() {
        return foreignLevelId;
    }

    /**
     * Setter/Getter for HIERARCHY_ID - table Field
     */
    public void setHierarchyId(Double hierarchyId) {
        this.hierarchyId = hierarchyId;
    }

    public Double getHierarchyId() {
        return hierarchyId;
    }

    /**
     * Setter/Getter for SAME_HIERARCHY_ALLOWED - table Field
     */
    public void setSameHierarchyAllowed(String sameHierarchyAllowed) {
        this.sameHierarchyAllowed = sameHierarchyAllowed;
    }

    public String getSameHierarchyAllowed() {
        return sameHierarchyAllowed;
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
        strB.append(getTOString("roleId", roleId));
        strB.append(getTOString("roleName", roleName));
        strB.append(getTOString("groupId", groupId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("levelId", levelId));
        strB.append(getTOString("accessAllBranch", accessAllBranch));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("foreignLevelId", foreignLevelId));
        strB.append(getTOString("hierarchyId", hierarchyId));
        strB.append(getTOString("sameHierarchyAllowed", sameHierarchyAllowed));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("roleId", roleId));
        strB.append(getTOXml("roleName", roleName));
        strB.append(getTOXml("groupId", groupId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("levelId", levelId));
        strB.append(getTOXml("accessAllBranch", accessAllBranch));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("foreignLevelId", foreignLevelId));
        strB.append(getTOXml("hierarchyId", hierarchyId));
        strB.append(getTOXml("sameHierarchyAllowed", sameHierarchyAllowed));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}