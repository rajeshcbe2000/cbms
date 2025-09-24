/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EarningsDeductionTO.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.transferobject.payroll.earningsDeductions;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO is PAYCODES_MASTER.
 */
public class EarningsDeductionTO extends TransferObject implements Serializable {

    private String paycode_Id = "";
    private String pay_Code = "";
    private String pay_Descri = "";
    private String pay_EarnDedu = "";
    private String pay_Mod_Type = "";
    private String pay_Calc_On = "";
    private String pay_Calc_Type = "";
    private String pay_Prod_Type = "";
    private Double pay_Fix_Amt = 0.0;
    private Double pay_Min_Amt = 0.0;
    private Double pay_Max_Amt = 0.0;
    private Double pay_Percent = 0.0;
    private String taxable = "";
    private String personal_Pay = "";
    private String active = "";
    private String individual_reqd = "";
    private String contra_Only = "";
    private String payment_Voucher = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDate = null;
    private String createdBy = "";
    private Date createdDate = null;
    private String srlNo = "";

    public String getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(String srlNo) {
        this.srlNo = srlNo;
    }

    public String getPay_Prod_Type() {
        return pay_Prod_Type;
    }

    public void setPay_Prod_Type(String pay_Prod_Type) {
        this.pay_Prod_Type = pay_Prod_Type;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getContra_Only() {
        return contra_Only;
    }

    public void setContra_Only(String contra_Only) {
        this.contra_Only = contra_Only;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getIndividual_reqd() {
        return individual_reqd;
    }

    public void setIndividual_reqd(String individual_reqd) {
        this.individual_reqd = individual_reqd;
    }

    public String getPay_Calc_On() {
        return pay_Calc_On;
    }

    public void setPay_Calc_On(String pay_Calc_On) {
        this.pay_Calc_On = pay_Calc_On;
    }

    public String getPay_Calc_Type() {
        return pay_Calc_Type;
    }

    public void setPay_Calc_Type(String pay_Calc_Type) {
        this.pay_Calc_Type = pay_Calc_Type;
    }

    public String getPay_Code() {
        return pay_Code;
    }

    public void setPay_Code(String pay_Code) {
        this.pay_Code = pay_Code;
    }

    public String getPay_Descri() {
        return pay_Descri;
    }

    public void setPay_Descri(String pay_Descri) {
        this.pay_Descri = pay_Descri;
    }

    public String getPay_EarnDedu() {
        return pay_EarnDedu;
    }

    public void setPay_EarnDedu(String pay_EarnDedu) {
        this.pay_EarnDedu = pay_EarnDedu;
    }

    public Double getPay_Fix_Amt() {
        return pay_Fix_Amt;
    }

    public void setPay_Fix_Amt(Double pay_Fix_Amt) {
        this.pay_Fix_Amt = pay_Fix_Amt;
    }

    public Double getPay_Max_Amt() {
        return pay_Max_Amt;
    }

    public void setPay_Max_Amt(Double pay_Max_Amt) {
        this.pay_Max_Amt = pay_Max_Amt;
    }

    public Double getPay_Min_Amt() {
        return pay_Min_Amt;
    }

    public void setPay_Min_Amt(Double pay_Min_Amt) {
        this.pay_Min_Amt = pay_Min_Amt;
    }

    public String getPay_Mod_Type() {
        return pay_Mod_Type;
    }

    public void setPay_Mod_Type(String pay_Mod_Type) {
        this.pay_Mod_Type = pay_Mod_Type;
    }

    public Double getPay_Percent() {
        return pay_Percent;
    }

    public void setPay_Percent(Double pay_Percent) {
        this.pay_Percent = pay_Percent;
    }

    public String getPaycode_Id() {
        return paycode_Id;
    }

    public void setPaycode_Id(String paycode_Id) {
        this.paycode_Id = paycode_Id;
    }

    public String getPayment_Voucher() {
        return payment_Voucher;
    }

    public void setPayment_Voucher(String payment_Voucher) {
        this.payment_Voucher = payment_Voucher;
    }

    public String getPersonal_Pay() {
        return personal_Pay;
    }

    public void setPersonal_Pay(String personal_Pay) {
        this.personal_Pay = personal_Pay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public String getTaxable() {
        return taxable;
    }

    public void setTaxable(String taxable) {
        this.taxable = taxable;
    }

    public String getKeyData() {
        setKeyColumns("");
        return "";
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("paycode_Id", paycode_Id));
        strB.append(getTOString("pay_Code", pay_Code));
        strB.append(getTOString("pay_Descri", pay_Descri));
        strB.append(getTOString("pay_EarnDedu", pay_EarnDedu));
        strB.append(getTOString("pay_Mod_Type", pay_Mod_Type));
        strB.append(getTOString("pay_Calc_On", pay_Calc_On));
        strB.append(getTOString("pay_Calc_Type", pay_Calc_Type));
        strB.append(getTOString("pay_Prod_Type", pay_Prod_Type));
        strB.append(getTOString("pay_Fix_Amt", pay_Fix_Amt));
        strB.append(getTOString("pay_Min_Amt", pay_Min_Amt));
        strB.append(getTOString("pay_Max_Amt", pay_Max_Amt));
        strB.append(getTOString("pay_Percent", pay_Percent));
        strB.append(getTOString("taxable", taxable));
        strB.append(getTOString("personal_Pay", personal_Pay));
        strB.append(getTOString("active", active));
        strB.append(getTOString("individual_reqd", individual_reqd));
        strB.append(getTOString("contra_Only", contra_Only));
        strB.append(getTOString("payment_Voucher", payment_Voucher));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDate", createdDate));
        strB.append(getTOString("srlNo", srlNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("paycode_Id", paycode_Id));
        strB.append(getTOXml("pay_Code", pay_Code));
        strB.append(getTOXml("pay_Descri", pay_Descri));
        strB.append(getTOXml("pay_EarnDedu", pay_EarnDedu));
        strB.append(getTOXml("pay_Mod_Type", pay_Mod_Type));
        strB.append(getTOXml("pay_Calc_On", pay_Calc_On));
        strB.append(getTOXml("pay_Calc_Type", pay_Calc_Type));
        strB.append(getTOXml("pay_Prod_Type", pay_Prod_Type));
        strB.append(getTOXml("pay_Fix_Amt", pay_Fix_Amt));
        strB.append(getTOXml("pay_Min_Amt", pay_Min_Amt));
        strB.append(getTOXml("pay_Max_Amt", pay_Max_Amt));
        strB.append(getTOXml("pay_Percent", pay_Percent));
        strB.append(getTOXml("taxable", taxable));
        strB.append(getTOXml("personal_Pay", personal_Pay));
        strB.append(getTOXml("active", active));
        strB.append(getTOXml("individual_reqd", individual_reqd));
        strB.append(getTOXml("contra_Only", contra_Only));
        strB.append(getTOXml("payment_Voucher", payment_Voucher));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDate", createdDate));
        strB.append(getTOXml("srlNo", srlNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}