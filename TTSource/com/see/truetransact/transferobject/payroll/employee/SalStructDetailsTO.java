/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * SalStructDetailsTO.java
 */

package com.see.truetransact.transferobject.payroll.employee;

/**
 *
 * @author anjuanand
 */
public class SalStructDetailsTO {

    private Integer slNo;
    private Double incAmt;
    private Integer incCount;
    private String countFreq;

    public String getCountFreq() {
        return countFreq;
    }

    public void setCountFreq(String countFreq) {
        this.countFreq = countFreq;
    }

    public Double getIncAmt() {
        return incAmt;
    }

    public void setIncAmt(Double incAmt) {
        this.incAmt = incAmt;
    }

    public Integer getIncCount() {
        return incCount;
    }

    public void setIncCount(Integer incCount) {
        this.incCount = incCount;
    }

    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(Integer slNo) {
        this.slNo = slNo;
    }
}
