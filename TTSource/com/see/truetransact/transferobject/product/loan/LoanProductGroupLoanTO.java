/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductAccHeadTO.java
 *
 * Created on Fri Aug 13 11:23:46 IST 2004
 */
package com.see.truetransact.transferobject.product.loan;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_PROD_ACHD.
 */
public class LoanProductGroupLoanTO extends TransferObject implements Serializable {

    private String prodId = "";
    private Integer intCalcDay = new Integer(0);
    private Integer intCalcMonth = new Integer(0);
    private String isDebitAllowedForDueCustomer = "";
    private String interestCalcDay = "";

    public Integer getIntCalcDay() {
        return intCalcDay;
    }

    public void setIntCalcDay(Integer intCalcDay) {
        this.intCalcDay = intCalcDay;
    }

    public Integer getIntCalcMonth() {
        return intCalcMonth;
    }

    public void setIntCalcMonth(Integer intCalcMonth) {
        this.intCalcMonth = intCalcMonth;
    }

    


    public String getInterestCalcDay() {
        return interestCalcDay;
    }

    public void setInterestCalcDay(String interestCalcDay) {
        this.interestCalcDay = interestCalcDay;
    }

    public String getIsDebitAllowedForDueCustomer() {
        return isDebitAllowedForDueCustomer;
    }

    public void setIsDebitAllowedForDueCustomer(String isDebitAllowedForDueCustomer) {
        this.isDebitAllowedForDueCustomer = isDebitAllowedForDueCustomer;
    }

  
    

   

   

    /**
     * Setter/Getter for PROD_ID - table Field
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdId() {
        return prodId;
    }
    public String getKeyData() {
        setKeyColumns("prodId");
        return prodId;
    }
    
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("intCalcDay", intCalcDay));
        strB.append(getTOString("intCalcMonth", intCalcMonth));
        strB.append(getTOString("isDebitAllowedForDueCustomer", isDebitAllowedForDueCustomer));
        strB.append(getTOString("interestCalcDay", interestCalcDay));
        
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("intCalcDay", intCalcDay));
        strB.append(getTOXml("intCalcMonth", intCalcMonth));
        strB.append(getTOXml("isDebitAllowedForDueCustomer", isDebitAllowedForDueCustomer));
        strB.append(getTOXml("interestCalcDay", interestCalcDay));
       
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

  
}