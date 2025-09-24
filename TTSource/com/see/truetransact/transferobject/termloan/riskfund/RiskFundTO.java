/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanFacilityTO.java
 * 
 * Created on Wed Apr 13 17:21:29 IST 2005
 */
package com.see.truetransact.transferobject.termloan.riskfund;

import com.see.truetransact.transferobject.termloan.*;
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_FACILITY_DETAILS.
 */
public class RiskFundTO extends TransferObject implements Serializable {

    private String actNum = "";
    private String custId = "";
    private Double riskFundAmt = 0.0;
    private String memNo = "";
    private String prodId = "";
    private String branchId = "";
    private String narration = "";

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getMemNo() {
        return memNo;
    }

    public void setMemNo(String memNo) {
        this.memNo = memNo;
    }

    public Double getRiskFundAmt() {
        return riskFundAmt;
    }

    public void setRiskFundAmt(Double riskFundAmt) {
        this.riskFundAmt = riskFundAmt;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getActNum() {
        return actNum;
    }

    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }
    
    
}
