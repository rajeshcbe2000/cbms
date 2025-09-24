/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * HeadTO.java
 * 
 * Created on Sun Apr 10 14:32:49 IST 2005
 */
package com.see.truetransact.transferobject.generalledger;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;


import java.util.HashMap;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.NoCommandException;

/**
 * Table name for this TO is MJR_AC_HD.
 */
public class HeadTO extends TransferObject implements Serializable {

    private Integer mjrAcHdId = 0;
    private String mjrAcHdDesc = "";
    private String mjrAcHdType = "";
    private String finalActType = "";
    private String subActType = "";
    private String status = "";
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String glConsolidated = null;
    /**
     * UNGENERATED CODE **
     */
    private HashMap subHeadMap;
    private TOHeader toHeader;

    /**
     * ********************
     */
    /**
     * Setter/Getter for MJR_AC_HD_ID - table Field
     */
    public Integer getMjrAcHdId() {
        return mjrAcHdId;
    }

    public void setMjrAcHdId(Integer mjrAcHdId) {
        this.mjrAcHdId = mjrAcHdId;
    }

    

    /**
     * Setter/Getter for MJR_AC_HD_DESC - table Field
     */
    public void setMjrAcHdDesc(String mjrAcHdDesc) {
        this.mjrAcHdDesc = mjrAcHdDesc;
    }

    public String getMjrAcHdDesc() {
        return mjrAcHdDesc;
    }

    /**
     * Setter/Getter for MJR_AC_HD_TYPE - table Field
     */
    public void setMjrAcHdType(String mjrAcHdType) {
        this.mjrAcHdType = mjrAcHdType;
    }

    public String getMjrAcHdType() {
        return mjrAcHdType;
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

    public String getSubActType() {
        return subActType;
    }

    public void setSubActType(String subActType) {
        this.subActType = subActType;
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
        strB.append(getTOString("mjrAcHdId", mjrAcHdId));
        strB.append(getTOString("mjrAcHdDesc", mjrAcHdDesc));
        strB.append(getTOString("mjrAcHdType", mjrAcHdType));
        strB.append(getTOString("glConsolidated", glConsolidated));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("finalActType", finalActType));
        strB.append(getTOString("subActType", subActType));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("mjrAcHdId", mjrAcHdId));
        strB.append(getTOXml("mjrAcHdDesc", mjrAcHdDesc));
        strB.append(getTOXml("mjrAcHdType", mjrAcHdType));
        strB.append(getTOXml("glConsolidated", glConsolidated));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("finalActType", finalActType));
        strB.append(getTOXml("subActType", subActType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * UNGENERATED CODE **
     */
    /**
     * This method is used to perform operations on SubHead based on TO status
     * and command in SubHeadTO If the Sub Head is new (i.e. it is not in the
     * Database but may be entered in the screen), then Sub Head can be only
     * insert irrepective of command in SubHeadTO else if other operations are
     * performed based on command in SubHeadTO
     *
     */
    public void addSubHead(SubHeadTO subHeadTO, String status) throws Exception {
        if (subHeadMap == null) {
            subHeadMap = new HashMap();
        }

        toHeader = new TOHeader();

        if (status.equals(CommonConstants.TOSTATUS_INSERT)) {
            //This branch is used to do insertion -- set Command and operations for new SubHead
            if (chkDeleteHead(subHeadTO)) {
                toHeader.setCommand(CommonConstants.TOSTATUS_UPDATE);
            } else {
                toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);
            }
            setSubHeadMap(subHeadTO);
        } else if (status.equals(CommonConstants.TOSTATUS_UPDATE)) {
            //This branch is used to do insertion or updation based on whether SubHead is new or existing respectively            
            if (chkNewHead(subHeadTO)) {
                toHeader.setCommand(CommonConstants.TOSTATUS_INSERT);
            } else {
                toHeader.setCommand(CommonConstants.TOSTATUS_UPDATE);
            }
            setSubHeadMap(subHeadTO);
        } else if (status.equals(CommonConstants.TOSTATUS_DELETE)) {
            //This branch is used to do removing from Hashmap or deletion (-changing status to DELETE) based on whether SubHead is new or existing respectively
            if (chkNewHead(subHeadTO)) {
                subHeadMap.remove(subHeadTO.getSubAcHdId());
            } else {
                toHeader.setCommand(CommonConstants.TOSTATUS_DELETE);
                setSubHeadMap(subHeadTO);
            }
        } else {
            throw new NoCommandException();
        }
        subHeadTO = null;
        toHeader = null;
    }

    public HashMap getSubHeadList() {
        return subHeadMap;
    }

    /**
     * The method is for adding an SubHeadTO object in HashMap SubHeadMap with
     * SubHeadCode as the key
     */
    private void setSubHeadMap(SubHeadTO subHeadTO) {
        subHeadTO.setTOHeader(toHeader);
        subHeadMap.put(subHeadTO.getSubAcHdId(), subHeadTO);
    }

    /**
     * The method is for checking whether SubHeadTO is new or existing Returns
     * true if the subHead is new
     */
    public boolean chkNewHead(SubHeadTO subHeadTO) {
        boolean newHead = false;
        final boolean existingSubHead = subHeadMap.containsKey(subHeadTO.getSubAcHdId());
        if (existingSubHead) {
            final String command = ((SubHeadTO) subHeadMap.get(subHeadTO.getSubAcHdId())).getCommand();
            newHead = (command != null && command.equals(CommonConstants.TOSTATUS_INSERT));
        }
        return newHead;
    }

    /**
     * The method is for checking whether SubHeadTO is in delete status Returns
     * true if the subHead is in delete
     */
    private boolean chkDeleteHead(SubHeadTO subHeadTO) {
        boolean newHead = false;
        final boolean existingSubHead = subHeadMap.containsKey(subHeadTO.getSubAcHdId());
        if (existingSubHead) {
            final String command = ((SubHeadTO) subHeadMap.get(subHeadTO.getSubAcHdId())).getCommand();
            newHead = (command != null && command.equals(CommonConstants.TOSTATUS_DELETE));
            subHeadMap.remove(subHeadTO.getSubAcHdId());
        }
        return newHead;
    }

    /**
     * Getter for property glConsolidated.
     *
     * @return Value of property glConsolidated.
     */
    public java.lang.String getGlConsolidated() {
        return glConsolidated;
    }

    /**
     * Setter for property glConsolidated.
     *
     * @param glConsolidated New value of property glConsolidated.
     */
    public void setGlConsolidated(java.lang.String glConsolidated) {
        this.glConsolidated = glConsolidated;
    }

    /**
     * Getter for property finalActType.
     *
     * @return Value of property finalActType.
     */
    public String getFinalActType() {
        return finalActType;
    }

    /**
     * Setter for property finalActType.
     *
     * @param finalActType New value of property finalActType.
     */
    public void setFinalActType(String finalActType) {
        this.finalActType = finalActType;
    }
    /**
     * ********************
     */
}