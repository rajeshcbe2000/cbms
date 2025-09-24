package com.see.truetransact.transferobject.ttlite;

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * .
 *
 * LookupTO.java
 *
 * Created on May 3, 2004, 4:49 PM
 */
import com.see.truetransact.transferobject.TransferObject;

/**
 * This class will act as a Bean class to the Lookup details
 *
 * @author Sathiya
 */
public class SMSUpdationTO extends TransferObject {

    private String chequeBookRequest;
    private String chequeStopRequest;
    private String drCashAmt;
    private String crCashAmt;
    private String drTransferAmt;
    private String crTransferAmt;
    private String availableBalanceLimit;

    public String getAvailableBalanceLimit() {
        return availableBalanceLimit;
    }

    public void setAvailableBalanceLimit(String availableBalanceLimit) {
        this.availableBalanceLimit = availableBalanceLimit;
    }

    public String getChequeBookRequest() {
        return chequeBookRequest;
    }

    public void setChequeBookRequest(String chequeBookRequest) {
        this.chequeBookRequest = chequeBookRequest;
    }

    public String getChequeStopRequest() {
        return chequeStopRequest;
    }

    public void setChequeStopRequest(String chequeStopRequest) {
        this.chequeStopRequest = chequeStopRequest;
    }

    public String getCrCashAmt() {
        return crCashAmt;
    }

    public void setCrCashAmt(String crCashAmt) {
        this.crCashAmt = crCashAmt;
    }

    public String getCrTransferAmt() {
        return crTransferAmt;
    }

    public void setCrTransferAmt(String crTransferAmt) {
        this.crTransferAmt = crTransferAmt;
    }

    public String getDrCashAmt() {
        return drCashAmt;
    }

    public void setDrCashAmt(String drCashAmt) {
        this.drCashAmt = drCashAmt;
    }

    public String getDrTransferAmt() {
        return drTransferAmt;
    }

    public void setDrTransferAmt(String drTransferAmt) {
        this.drTransferAmt = drTransferAmt;
    }
    
    /**
     * Creates a new instance of Lookup
     */
    public SMSUpdationTO() {
    }
}
