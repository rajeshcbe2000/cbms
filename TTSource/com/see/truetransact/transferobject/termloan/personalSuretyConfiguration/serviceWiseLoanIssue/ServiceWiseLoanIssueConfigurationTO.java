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
package com.see.truetransact.transferobject.termloan.personalSuretyConfiguration.serviceWiseLoanIssue;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Table name for this TO is EMP_TRANSFER.
 */
public class ServiceWiseLoanIssueConfigurationTO extends TransferObject implements Serializable {

    private List selectedList = new ArrayList();
    private Date effectFrom = null;
    private Integer pastServicePeriod = 0;
    private Integer fromAmt = 0;
    private Integer toAmt = 0;
    private Integer noOfsuretiesReq = 0;
    private String status = "";
    private String elsi_id = "";
    private String prodID = "";

    /**
     * Getter for property selectedList.
     *
     * @return Value of property selectedList.
     */
    public List getSelectedList() {
        return selectedList;
    }

    /**
     * Setter for property selectedList.
     *
     * @param selectedList New value of property selectedList.
     */
    public void setSelectedList(List selectedList) {
        this.selectedList = selectedList;
    }

    /**
     * Getter for property effectFrom.
     *
     * @return Value of property effectFrom.
     */
    public Date getEffectFrom() {
        return effectFrom;
    }

    /**
     * Setter for property effectFrom.
     *
     * @param effectFrom New value of property effectFrom.
     */
    public void setEffectFrom(Date effectFrom) {
        this.effectFrom = effectFrom;
    }

    /**
     * Getter for property pastServicePeriod.
     *
     * @return Value of property pastServicePeriod.
     */
    public Integer getPastServicePeriod() {
        return pastServicePeriod;
    }

    /**
     * Setter for property pastServicePeriod.
     *
     * @param pastServicePeriod New value of property pastServicePeriod.
     */
    public void setPastServicePeriod(Integer pastServicePeriod) {
        this.pastServicePeriod = pastServicePeriod;
    }

    /**
     * Getter for property fromAmt.
     *
     * @return Value of property fromAmt.
     */
    public Integer getFromAmt() {
        return fromAmt;
    }

    /**
     * Setter for property fromAmt.
     *
     * @param fromAmt New value of property fromAmt.
     */
    public void setFromAmt(Integer fromAmt) {
        this.fromAmt = fromAmt;
    }

    /**
     * Getter for property toAmt.
     *
     * @return Value of property toAmt.
     */
    public Integer getToAmt() {
        return toAmt;
    }

    /**
     * Setter for property toAmt.
     *
     * @param toAmt New value of property toAmt.
     */
    public void setToAmt(Integer toAmt) {
        this.toAmt = toAmt;
    }

    /**
     * Getter for property noOfsuretiesReq.
     *
     * @return Value of property noOfsuretiesReq.
     */
    public Integer getNoOfsuretiesReq() {
        return noOfsuretiesReq;
    }

    /**
     * Setter for property noOfsuretiesReq.
     *
     * @param noOfsuretiesReq New value of property noOfsuretiesReq.
     */
    public void setNoOfsuretiesReq(Integer noOfsuretiesReq) {
        this.noOfsuretiesReq = noOfsuretiesReq;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Getter for property elsi_id.
     *
     * @return Value of property elsi_id.
     */
    public String getElsi_id() {
        return elsi_id;
    }

    /**
     * Setter for property elsi_id.
     *
     * @param elsi_id New value of property elsi_id.
     */
    public void setElsi_id(String elsi_id) {
        this.elsi_id = elsi_id;
    }

    /**
     * Getter for property prodID.
     *
     * @return Value of property prodID.
     */
    public String getProdID() {
        return prodID;
    }

    /**
     * Setter for property prodID.
     *
     * @param prodID New value of property prodID.
     */
    public void setProdID(String prodID) {
        this.prodID = prodID;
    }
//	/** toString method which returns this TO as a String. */
//	public String toString() {
//		StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//		strB.append (getTOStringKey(getKeyData()));
//		strB.append(getTOString("empTransferID", empTransferID));
//		strB.append(getTOString("empID", empID));
//		strB.append(getTOString("currBran", currBran));
//		strB.append(getTOString("transferBran", transferBran));
//		strB.append(getTOString("lastWorkingDay", lastWorkingDay));
//		strB.append(getTOString("doj", doj));
//		strB.append(getTOString("roleInCurrBran", roleInCurrBran));
//		strB.append(getTOString("roleInTransferBran", roleInTransferBran));
//		strB.append(getTOString("applType", applType));
//		strB.append(getTOString("branCode", branCode));
//		strB.append(getTOString("status", status));
//		strB.append(getTOString("statusDt", statusDt));
//		strB.append(getTOString("statusBy", statusBy));
//		strB.append(getTOString("createdDt", createdDt));
//                strB.append(getTOString("createdBy", createdBy));
//                strB.append(getTOString("authorizeBy", authorizeBy));
//                strB.append(getTOString("authorizeDt", authorizeDt));
//                strB.append(getTOString("authorizedStatus", authorizedStatus));
//                strB.append(getTOString("empName", empName));
//                strB.append(getTOString("currBranName", currBranName));
//		strB.append(getTOStringEnd());
//		return strB.toString();
//	}
//
//	/** toXML method which returns this TO as a XML output. */
//	public String toXML() {
//		StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//		strB.append (getTOXmlKey(getKeyData()));
//		strB.append(getTOXml("empTransferID", empTransferID));
//		strB.append(getTOXml("empID", empID));
//		strB.append(getTOXml("currBran", currBran));
//		strB.append(getTOXml("transferBran", transferBran));
//		strB.append(getTOXml("lastWorkingDay", lastWorkingDay));
//		strB.append(getTOXml("doj", doj));
//		strB.append(getTOXml("roleInCurrBran", roleInCurrBran));
//		strB.append(getTOXml("roleInTransferBran", roleInTransferBran));
//		strB.append(getTOXml("applType", applType));
//		strB.append(getTOXml("branCode", branCode));
//		strB.append(getTOXml("status", status));
//		strB.append(getTOXml("statusDt", statusDt));
//		strB.append(getTOXml("statusBy", statusBy));
//		strB.append(getTOXml("createdDt", createdDt));
//                strB.append(getTOXml("createdBy", createdBy));
//                strB.append(getTOXml("authorizeBy", authorizeBy));
//                strB.append(getTOXml("authorizeDt", authorizeDt));
//                strB.append(getTOXml("authorizedStatus", authorizedStatus));
//                strB.append(getTOXml("empName", empName));
//                strB.append(getTOXml("currBranName", currBranName));
//		strB.append(getTOXmlEnd());
//		return strB.toString();
//	}
}