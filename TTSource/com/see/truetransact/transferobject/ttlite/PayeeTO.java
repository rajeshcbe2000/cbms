package com.see.truetransact.transferobject.ttlite;

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * .
 *
 * PayeeTO.java
 *
 * Created on May 3, 2004, 4:49 PM
 */
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 * This class will act as a Bean class to the Payee details
 *
 * @author Pranav
 */
public class PayeeTO extends TransferObject {

    private String custUserId;
    private String payeeActNum;
    private String nickName;
    private Double limitAmt;
    private String status;
    private String statusBy;
    private Date satatusDt;
    private String authorizeStatus;
    private String authorizeBy;
    private Date authorizeDt;

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

    public Double getLimitAmt() {
        return limitAmt;
    }

    public void setLimitAmt(Double limitAmt) {
        this.limitAmt = limitAmt;
    }

    public Date getSatatusDt() {
        return satatusDt;
    }

    public void setSatatusDt(Date satatusDt) {
        this.satatusDt = satatusDt;
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
            
    /**
     * Creates a new instance of Payee
     */
    public PayeeTO() {
    }

    /**
     * Getter for property custUserId.
     *
     * @return Value of property custUserId.
     *
     */
    public String getCustUserId() {
        return custUserId;
    }

    /**
     * Setter for property custUserId.
     *
     * @param custUserId New value of property custUserId.
     *
     */
    public void setCustUserId(String custUserId) {
        this.custUserId = custUserId;
    }

    /**
     * Getter for property nickName.
     *
     * @return Value of property nickName.
     *
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Setter for property nickName.
     *
     * @param nickName New value of property nickName.
     *
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Getter for property payeeActNum.
     *
     * @return Value of property payeeActNum.
     *
     */
    public String getPayeeActNum() {
        return payeeActNum;
    }

    /**
     * Setter for property payeeActNum.
     *
     * @param payeeActNum New value of property payeeActNum.
     *
     */
    public void setPayeeActNum(String payeeActNum) {
        this.payeeActNum = payeeActNum;
    }
     /** getKeyData returns the Primary Key Columns for this TO
	 *  User needs to add the Key columns as a setter 
	 *  Example : 
	 *            setKeyColumns("col1" + KEY_VAL_SEPARATOR + "col2"); 
	 *            return col1 + KEY_VAL_SEPARATOR + col2; 
	 */
	 public String getKeyData() {
		setKeyColumns("");
		return "";
	}
    /** toString method which returns this TO as a String. */
    public String toString() {
            StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
	    strB.append (getTOStringKey(getKeyData()));
           
            strB.append(getTOString("custUserId", custUserId));
            strB.append(getTOString("payeeActNum", payeeActNum));
            strB.append(getTOString("nickName", nickName));
            
            strB.append(getTOStringEnd());
	    return strB.toString();
    }
     /** toXML method which returns this TO as a XML output. */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
	strB.append (getTOXmlKey(getKeyData()));
        
        strB.append(getTOXml("custUserId", custUserId));
        strB.append(getTOXml("payeeActNum", payeeActNum));
        strB.append(getTOXml("nickName", nickName));
        
        strB.append(getTOXmlEnd());
	return strB.toString();
    }
    
}
