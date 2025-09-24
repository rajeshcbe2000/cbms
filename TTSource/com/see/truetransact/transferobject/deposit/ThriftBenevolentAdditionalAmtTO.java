/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ThriftBenevolentAdditionalAmtTO.java
 */

package com.see.truetransact.transferobject.deposit;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class ThriftBenevolentAdditionalAmtTO extends TransferObject implements Serializable{
    
    // Added by nithya on 08-03.2016 for 0003920
    private String productId = "";
    private String depositNo = "";
    private Double additionalAmount ;
    private Date effectiveDate = null;

    public Double getAdditionalAmount() {
        return additionalAmount;
    }

    public String getDepositNo() {
        return depositNo;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setAdditionalAmount(Double additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    
     /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("prodId");
        return productId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("prodId", productId));       
        strB.append(getTOString("effectiveDate", effectiveDate));
        strB.append(getTOString("depositNo", depositNo));       
        strB.append(getTOString("additionalAmount", additionalAmount));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("prodId", productId));       
        strB.append(getTOXml("effectiveDate", effectiveDate));
        strB.append(getTOXml("depositNo", depositNo));       
        strB.append(getTOXml("additionalAmount", additionalAmount));       
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
    
}
