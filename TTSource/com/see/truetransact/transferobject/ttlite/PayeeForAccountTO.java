package com.see.truetransact.transferobject.ttlite;

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * .
 *
 * PayeeForAccountTO.java
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
public class PayeeForAccountTO extends TransferObject {

    private String nickname;
    private String name;
    private String accountNumber;
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
    public PayeeForAccountTO() {
    }

    /**
     * Getter for property accountNumber.
     *
     * @return Value of property accountNumber.
     *
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Setter for property accountNumber.
     *
     * @param accountNumber New value of property accountNumber.
     *
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Getter for property name.
     *
     * @return Value of property name.
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for property name.
     *
     * @param name New value of property name.
     *
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for property nickname.
     *
     * @return Value of property nickname.
     *
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Setter for property nickname.
     *
     * @param name New value of property nickname.
     *
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
