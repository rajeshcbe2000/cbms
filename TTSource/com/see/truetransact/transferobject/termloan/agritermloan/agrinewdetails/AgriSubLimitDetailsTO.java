/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriSubLimitDetailsTO.java
 *
 * Created on August 6, 2009, 12:57 PM
 */
package com.see.truetransact.transferobject.termloan.agritermloan.agrinewdetails;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;

/**
 *
 * @author abi
 */
public class AgriSubLimitDetailsTO extends TransferObject implements Serializable {

    String acctNum = "";
    String purpose = "";
    String type = "";
    String hectare = "";
    String surveryNo = "";
    String remarks = "";
    String status = "";
    String slno = "";
    String subLimitslno = "";
//    String command="";
//    StringBuffer buf=new StringBuffer();

    public String toString() {
        StringBuffer buf = new StringBuffer(getTOStringStart(this.getClass().getName()));
        buf.append(getTOString("acctNum", acctNum));
        buf.append(getTOString("type", type));
        buf.append(getTOString("hectare", hectare));
        buf.append(getTOString("surveryNo", surveryNo));
        buf.append(getTOString("remarks", remarks));
        buf.append(getTOString("status", status));
        buf.append(getTOString("slno", slno));
        buf.append(getTOString("subLimitslno", subLimitslno));
        buf.append(getTOString("purpose", purpose));
        buf.append(getTOStringEnd());
        return buf.toString();
    }

    public String toXml() {
        StringBuffer buf = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        buf.append(getTOXml("acctNum", acctNum));
        buf.append(getTOXml("type", type));
        buf.append(getTOXml("hectare", hectare));
        buf.append(getTOXml("surveryNo", surveryNo));
        buf.append(getTOXml("remarks", remarks));
        buf.append(getTOXml("status", status));
        buf.append(getTOXml("slno", slno));
        buf.append(getTOXml("subLimitslno", subLimitslno));
        buf.append(getTOXml("purpose", purpose));
        buf.append(getTOXmlEnd());
        return buf.toString();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    /**
     * Getter for property acctNum.
     *
     * @return Value of property acctNum.
     */
    public java.lang.String getAcctNum() {
        return acctNum;
    }

    /**
     * Setter for property acctNum.
     *
     * @param acctNum New value of property acctNum.
     */
    public void setAcctNum(java.lang.String acctNum) {
        this.acctNum = acctNum;
    }

    /**
     * Getter for property type.
     *
     * @return Value of property type.
     */
    public java.lang.String getType() {
        return type;
    }

    /**
     * Setter for property type.
     *
     * @param type New value of property type.
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    /**
     * Getter for property hectare.
     *
     * @return Value of property hectare.
     */
    public java.lang.String getHectare() {
        return hectare;
    }

    /**
     * Setter for property hectare.
     *
     * @param hectare New value of property hectare.
     */
    public void setHectare(java.lang.String hectare) {
        this.hectare = hectare;
    }

    /**
     * Getter for property surveryNo.
     *
     * @return Value of property surveryNo.
     */
    public java.lang.String getSurveryNo() {
        return surveryNo;
    }

    /**
     * Setter for property surveryNo.
     *
     * @param surveryNo New value of property surveryNo.
     */
    public void setSurveryNo(java.lang.String surveryNo) {
        this.surveryNo = surveryNo;
    }

    /**
     * Getter for property remarks.
     *
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }

    /**
     * Setter for property remarks.
     *
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }

    /**
     * Getter for property status.
     *
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Setter for property status.
     *
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Getter for property slno.
     *
     * @return Value of property slno.
     */
    public java.lang.String getSlno() {
        return slno;
    }

    /**
     * Setter for property slno.
     *
     * @param slno New value of property slno.
     */
    public void setSlno(java.lang.String slno) {
        this.slno = slno;
    }

    /**
     * Getter for property subLimitslno.
     *
     * @return Value of property subLimitslno.
     */
    public java.lang.String getSubLimitslno() {
        return subLimitslno;
    }

    /**
     * Setter for property subLimitslno.
     *
     * @param subLimitslno New value of property subLimitslno.
     */
    public void setSubLimitslno(java.lang.String subLimitslno) {
        this.subLimitslno = subLimitslno;
    }

//    /**
//     * Getter for property command.
//     * @return Value of property command.
//     */
//    public java.lang.String getCommand() {
//        return command;
//    }
//    
//    /**
//     * Setter for property command.
//     * @param command New value of property command.
//     */
//    public void setCommand(java.lang.String command) {
//        this.command = command;
//    }
    /**
     * Getter for property purpose.
     *
     * @return Value of property purpose.
     */
    public java.lang.String getPurpose() {
        return purpose;
    }

    /**
     * Setter for property purpose.
     *
     * @param purpose New value of property purpose.
     */
    public void setPurpose(java.lang.String purpose) {
        this.purpose = purpose;
    }
}
