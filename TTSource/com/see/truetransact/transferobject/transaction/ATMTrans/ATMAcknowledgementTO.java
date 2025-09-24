/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ATMAcknowledgementTO.java
 * 
 * Created on Thu Aug 12 12:40:27 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.transaction.ATMTrans;

import com.see.truetransact.transferobject.transaction.auditEntry.*;
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CASH_TRANS.
 */
public class ATMAcknowledgementTO extends TransferObject implements Serializable {

    private String transId = "";
    private String status = "";
    private String acctNum = "";
    private String cardAcctNum = null;
    private String sequenceNo = "";
    private Double amount = null;
    private Date transDt = null;
    private String transType = "";
    private String tran_status = "";
    private String error_code = "";
    private String rrn = "";
    private String station_code = "";
    private String atmType = "";
    private String chargeBit = "";
    private String chargeAmt = "";
    private String remarks = "";
    private String messageType= "";
    private String netWorkType= "";
    private String mop= "";
    private String transStatus= "";
    private String file_created = "";
    private Date   file_created_date  =null; 
    private Date   file_ack_dt  =null;         
    private String atmDtStr = "";
    private String atm_postTime = "";
            
    

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("transId");
        return transId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("status", status));
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("cardAcctNum", cardAcctNum));
        strB.append(getTOString("sequenceNo", sequenceNo));
        strB.append(getTOString("amount", amount));
        strB.append(getTOString("transDt", transDt));
        strB.append(getTOString("transType", transType));
        strB.append(getTOString("tran_status", tran_status));
        strB.append(getTOString("error_code", error_code));
        strB.append(getTOString("rrn", rrn));
        strB.append(getTOString("station_code", station_code));
        strB.append(getTOString("atmType", atmType));
        strB.append(getTOString("chargeBit", chargeBit));
        strB.append(getTOString("chargeAmt", chargeAmt));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("messageType", messageType));
        strB.append(getTOString("netWorkType", netWorkType));
        strB.append(getTOString("mop", mop));
        strB.append(getTOString("transStatus", transStatus));
        strB.append(getTOString("file_created", file_created));
        strB.append(getTOString("file_created_date", file_created_date));
        strB.append(getTOString("file_ack_dt", file_ack_dt));
        strB.append(getTOString("atmDtStr", atmDtStr));
        //strB.append(getTOString("networkType", networkType));
        strB.append(getTOString("atm_postTime", atm_postTime));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("cardAcctNum", cardAcctNum));
        strB.append(getTOXml("sequenceNo", sequenceNo));
        strB.append(getTOXml("amount", amount));
        strB.append(getTOXml("transDt", transDt));
        strB.append(getTOXml("transType", transType));
        strB.append(getTOXml("tran_status", tran_status));
        strB.append(getTOXml("error_code", error_code));
        strB.append(getTOXml("rrn", rrn));
        strB.append(getTOXml("station_code", station_code));
        strB.append(getTOXml("atmType", atmType));
        strB.append(getTOXml("chargeBit", chargeBit));
        strB.append(getTOXml("chargeAmt", chargeAmt));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("messageType", messageType));
        strB.append(getTOXml("netWorkType", netWorkType));
        strB.append(getTOXml("mop", mop));
        strB.append(getTOXml("transStatus", transStatus));
        strB.append(getTOXml("file_created", file_created));
        strB.append(getTOXml("file_created_date", file_created_date));
        strB.append(getTOXml("file_ack_dt", file_ack_dt));
        strB.append(getTOXml("atmDtStr", atmDtStr));
       // strB.append(getTOXml("networkType", networkType));
        strB.append(getTOXml("atm_postTime", atm_postTime));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
    
    
    public String getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCardAcctNum() {
        return cardAcctNum;
    }

    public void setCardAcctNum(String cardAcctNum) {
        this.cardAcctNum = cardAcctNum;
    }


    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTransDt() {
        return transDt;
    }

    public void setTransDt(Date transDt) {
        this.transDt = transDt;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
    
        public String getAtmType() {
        return atmType;
    }

    public void setAtmType(String atmType) {
        this.atmType = atmType;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getStation_code() {
        return station_code;
    }

    public void setStation_code(String station_code) {
        this.station_code = station_code;
    }

    public String getTran_status() {
        return tran_status;
    }

    public void setTran_status(String tran_status) {
        this.tran_status = tran_status;
    }


    public String getChargeAmt() {
        return chargeAmt;
    }

    public void setChargeAmt(String chargeAmt) {
        this.chargeAmt = chargeAmt;
    }

    public String getChargeBit() {
        return chargeBit;
    }

    public void setChargeBit(String chargeBit) {
        this.chargeBit = chargeBit;
    }
    
    
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
 
  public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getNetWorkType() {
        return netWorkType;
    }

    public void setNetWorkType(String netWorkType) {
        this.netWorkType = netWorkType;
    }

    public String getMop() {
        return mop;
    }

    public void setMop(String mop) {
        this.mop = mop;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getFile_created() {
        return file_created;
    }

    public void setFile_created(String file_created) {
        this.file_created = file_created;
    }

    public Date getFile_created_date() {
        return file_created_date;
    }

    public void setFile_created_date(Date file_created_date) {
        this.file_created_date = file_created_date;
    }

    public Date getFile_ack_dt() {
        return file_ack_dt;
    }

    public void setFile_ack_dt(Date file_ack_dt) {
        this.file_ack_dt = file_ack_dt;
    }

public String getAtmDtStr() {
        return atmDtStr;
    }

    public void setAtmDtStr(String atmDtStr) {
        this.atmDtStr = atmDtStr;
    }

   

    public String getAtm_postTime() {
        return atm_postTime;
    }

    public void setAtm_postTime(String atm_postTime) {
        this.atm_postTime = atm_postTime;
    }
    
}