/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MultipleLodgementMasterTO.java
 */

package com.see.truetransact.transferobject.bills.lodgement;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class MultipleLodgementMasterTO extends TransferObject implements Serializable {
    
    private String lodgementId ="";
    private String productId = "";
    private String productType = "";
    private String acctNo = "";
    private String borrowName = "";
    private Double individualAmt ;
    private String command ="";
    private String status = "";
    private String instruction = "";
    private Double instAmount ;
    private Double serviceTax ;

    public Double getInstAmount() {
        return instAmount;
    }

    public void setInstAmount(Double instAmount) {
        this.instAmount = instAmount;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Double getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(Double serviceTax) {
        this.serviceTax = serviceTax;
    }
    
    
    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public Double getIndividualAmt() {
        return individualAmt;
    }

    public void setIndividualAmt(Double individualAmt) {
        this.individualAmt = individualAmt;
    }

    public String getLodgementId() {
        return lodgementId;
    }

    public void setLodgementId(String lodgementId) {
        this.lodgementId = lodgementId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    
    public String getKeyData() {
        setKeyColumns(lodgementId);
        return lodgementId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
     /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("lodgementId", lodgementId));
        strB.append(getTOString("productId", productId));
        strB.append(getTOString("productType", productType));
        strB.append(getTOString("acctNo", acctNo));
        strB.append(getTOString("borrowName", borrowName));
        strB.append(getTOString("individualAmt", individualAmt));
        strB.append(getTOString("command", command));
        strB.append(getTOString("instruction", instruction));
        strB.append(getTOString("instAmount", instAmount));
        strB.append(getTOString("serviceTax", serviceTax));
        return strB.toString();
    }
    
     public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));        
        strB.append(getTOXml("lodgementId", lodgementId));
        strB.append(getTOXml("productId", productId));
        strB.append(getTOXml("productType", productType));
        strB.append(getTOXml("acctNo", acctNo));
        strB.append(getTOXml("borrowName", borrowName));
        strB.append(getTOXml("individualAmt", individualAmt));
        strB.append(getTOXml("command", command));        
        strB.append(getTOXml("instruction", instruction));
        strB.append(getTOXml("instAmount", instAmount));
        strB.append(getTOXml("serviceTax", serviceTax));        
        return strB.toString();
    }   

}
