/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DepositsThriftBenevolentTO.java
 */

package com.see.truetransact.transferobject.product.deposits;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author nithya
 */
public class DepositsThriftBenevolentTO extends TransferObject implements Serializable{
    //   Added by nithya on 02-03-2016 for 0003897 
    private String productId = "";
    private String operatesLike = "";
    private Double installmentAmount ;
    private Date effectiveDate = null;

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public Double getInstallmentAmount() {
        return installmentAmount;
    }

    public String getOperatesLike() {
        return operatesLike;
    }

    public String getProductId() {
        return productId;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setInstallmentAmount(Double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public void setOperatesLike(String operatesLike) {
        this.operatesLike = operatesLike;
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
        strB.append(getTOString("operatesLike", operatesLike));
        strB.append(getTOString("installmentAmount", installmentAmount));
        strB.append(getTOString("effectiveDate", effectiveDate));
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
        strB.append(getTOXml("operatesLike", operatesLike));
        strB.append(getTOXml("installmentAmount", installmentAmount));
        strB.append(getTOXml("effectiveDate", effectiveDate));       
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
    
}
