/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CurrencyExchangeTO.java
 *
 * Created on January 12, 2004, 2:12 PM
 */
package com.see.truetransact.transferobject.forex;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 *
 * @author bala
 */
public class CurrencyExchangeTO extends TransferObject implements Serializable {

    private String transId = "";
    private Date transDt;
    private String acctNo = "";
    private String touristName = "";
    private String passportNo = "";
    private String touristNote = "";
    private String instrumentNo = "";
    private Date instrumentDate;
    private String instrumentDetails = "";
    private String transType = "";
    private String transCurrency = "";
    private Double transAmount;
    private String convCurrency = "";
    private Date valueDate;
    private Double exchangeRate;
    private Double commission;
    private Double totalAmount;
    private String remarks = "";
    private String createdBy = "";
    private Date createdDt;
    private String authorizedBy = "";
    private Date authorizedDt;
    private String status = "";
    private String fromBranch = "";
    private String fromTransId = "";

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransDt(Date transDt) {
        this.transDt = transDt;
    }

    public Date getTransDt() {
        return transDt;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setTouristName(String touristName) {
        this.touristName = touristName;
    }

    public String getTouristName() {
        return touristName;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setTouristNote(String touristNote) {
        this.touristNote = touristNote;
    }

    public String getTouristNote() {
        return touristNote;
    }

    public void setInstrumentNo(String instrumentNo) {
        this.instrumentNo = instrumentNo;
    }

    public String getInstrumentNo() {
        return instrumentNo;
    }

    public void setInstrumentDate(Date instrumentDate) {
        this.instrumentDate = instrumentDate;
    }

    public Date getInstrumentDate() {
        return instrumentDate;
    }

    public void setInstrumentDetails(String instrumentDetails) {
        this.instrumentDetails = instrumentDetails;
    }

    public String getInstrumentDetails() {
        return instrumentDetails;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransCurrency(String transCurrency) {
        this.transCurrency = transCurrency;
    }

    public String getTransCurrency() {
        return transCurrency;
    }

    public void setTransAmount(Double transAmount) {
        this.transAmount = transAmount;
    }

    public Double getTransAmount() {
        return transAmount;
    }

    public void setConvCurrency(String convCurrency) {
        this.convCurrency = convCurrency;
    }

    public String getConvCurrency() {
        return convCurrency;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getCommission() {
        return commission;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setFromBranch(String fromBranch) {
        this.fromBranch = fromBranch;
    }

    public String getFromBranch() {
        return fromBranch;
    }

    public void setFromTransId(String fromTransId) {
        this.fromTransId = fromTransId;
    }

    public String getFromTransId() {
        return fromTransId;
    }
}