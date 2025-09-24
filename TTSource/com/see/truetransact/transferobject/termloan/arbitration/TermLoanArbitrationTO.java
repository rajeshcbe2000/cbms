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
package com.see.truetransact.transferobject.termloan.arbitration;

import com.see.truetransact.transferobject.termloan.chargesTo.*;
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_FACILITY_DETAILS.
 */
public class TermLoanArbitrationTO extends TransferObject implements Serializable {

    private String act_num;
    private String mem_no;
    private Double princ_due = null;
    private Double int_due = null;
    private Double penal = null;
    private Double charges = null;
    private Double arcfee = null;
    private Double totalarc = null;
    private String fileNo;
    private Date arbitDate = null;
    private Double arbRate = null;
    private String arbid;
    private String awardno;
    private String inspordno;
    private Date awardDate = null;  
    private String prodId = null;
    private String arcStatus = null;  // added by nithya on 10-03-2016
    private Date reportingDate = null; // Added by nithya on 05-03-2016 for 0003914
    private String branchId = null; // Added by nithya on 18-03-2016 
    //private String prodType = null; // nithya
    
    public Double getMiscCharges() {
        return miscCharges;
    }

    public void setMiscCharges(Double miscCharges) {
        this.miscCharges = miscCharges;
    }

     // added by nithya on 10-03-2016
    
    public String getArcStatus() {
        return arcStatus;
    }

    public void setArcStatus(String arcStatus) {
        this.arcStatus = arcStatus;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

//    public String getProdType() {
//        return prodType;
//    }
//
//    public void setProdType(String prodType) {
//        this.prodType = prodType;
//    }
    
    
    // End
    
    private Date inspordDate = null;
    private Date epDate = null;
    private String epid;
    private Double epfee = null;
    private Double tot_ep = null;
    private Double ep_app_fee = null;
    private String arb_type = null;
    private Double miscCharges=null;
    private String epNo = "";

    // Added by nithya on 05-03-2016 for 0003914
    public Date getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(Date reportingDate) {
        this.reportingDate = reportingDate;
    }
    // End
    
    
    public String getArb_type() {
        return arb_type;
    }

    public void setArb_type(String arb_type) {
        this.arb_type = arb_type;
    }

    public Double getEp_app_fee() {
        return ep_app_fee;
    }

    public void setEp_app_fee(Double ep_app_fee) {
        this.ep_app_fee = ep_app_fee;
    }

    public Double getEp_sales_fee() {
        return ep_sales_fee;
    }

    public void setEp_sales_fee(Double ep_sales_fee) {
        this.ep_sales_fee = ep_sales_fee;
    }

    public Double getEp_postage_fee() {
        return ep_postage_fee;
    }

    public void setEp_postage_fee(Double ep_postage_fee) {
        this.ep_postage_fee = ep_postage_fee;
    }
    private Double ep_sales_fee = null;
    private Double ep_postage_fee = null;

    public Date getEpDate() {
        return epDate;
    }

    public void setEpDate(Date epDate) {
        this.epDate = epDate;
    }

    public String getEpid() {
        return epid;
    }

    public void setEpid(String epid) {
        this.epid = epid;
    }

    public Double getEpfee() {
        return epfee;
    }

    public void setEpfee(Double epfee) {
        this.epfee = epfee;
    }

    public Double getTot_ep() {
        return tot_ep;
    }

    public void setTot_ep(Double tot_ep) {
        this.tot_ep = tot_ep;
    }

    public String getAwardno() {
        return awardno;
    }

    public void setAwardno(String awardno) {
        this.awardno = awardno;
    }

    public String getInspordno() {
        return inspordno;
    }

    public void setInspordno(String inspordno) {
        this.inspordno = inspordno;
    }

    public Date getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(Date awardDate) {
        this.awardDate = awardDate;
    }

    public Date getInspordDate() {
        return inspordDate;
    }

    public void setInspordDate(Date inspordDate) {
        this.inspordDate = inspordDate;
    }

    public String getArbid() {
        return arbid;
    }

    public void setArbid(String arbid) {
        this.arbid = arbid;
    }

    public String getAct_num() {
        return act_num;
    }

    public void setAct_num(String act_num) {
        this.act_num = act_num;
    }

    public String getMem_no() {
        return mem_no;
    }

    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }

    public Double getPrinc_due() {
        return princ_due;
    }

    public void setPrinc_due(Double princ_due) {
        this.princ_due = princ_due;
    }

    public Double getInt_due() {
        return int_due;
    }

    public void setInt_due(Double int_due) {
        this.int_due = int_due;
    }

    public Double getPenal() {
        return penal;
    }

    public void setPenal(Double penal) {
        this.penal = penal;
    }

    public Double getCharges() {
        return charges;
    }

    public void setCharges(Double charges) {
        this.charges = charges;
    }

    public Double getArcfee() {
        return arcfee;
    }

    public void setArcfee(Double arcfee) {
        this.arcfee = arcfee;
    }

    public Double getTotalarc() {
        return totalarc;
    }

    public void setTotalarc(Double totalarc) {
        this.totalarc = totalarc;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public Date getArbitDate() {
        return arbitDate;
    }

    public void setArbitDate(Date arbitDate) {
        this.arbitDate = arbitDate;
    }

    public Double getArbRate() {
        return arbRate;
    }

    public void setArbRate(Double arbRate) {
        this.arbRate = arbRate;
    }
    private Date resDate = null;
    private String resno;

    public Date getResDate() {
        return resDate;
    }

    public void setResDate(Date resDate) {
        this.resDate = resDate;
    }

    public String getResno() {
        return resno;
    }

    public void setResno(String resno) {
        this.resno = resno;
    }

    public String getEpNo() {
        return epNo;
    }

    public void setEpNo(String epNo) {
        this.epNo = epNo;
    }
        

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("act_num", act_num));
        strB.append(getTOString("mem_no", mem_no));
        strB.append(getTOString("princ_due", princ_due));
        strB.append(getTOString("int_due", int_due));
        strB.append(getTOString("penal", penal));
        strB.append(getTOString("charges", charges));
        strB.append(getTOString("arcfee", arcfee));
        strB.append(getTOString("totalarc", totalarc));
        strB.append(getTOString("fileNo", fileNo));
        strB.append(getTOString("arbitDate", arbitDate));
        strB.append(getTOString("arbRate", arbRate));
        strB.append(getTOString("awardno", awardno));
        strB.append(getTOString("awardDate", awardDate));
        strB.append(getTOString("inspordDate", inspordDate));
        strB.append(getTOString("inspordno", inspordno));
        strB.append(getTOString("arb_type", arb_type));
        strB.append(getTOString("miscCharges", miscCharges));
        strB.append(getTOString("arbid", arbid));
        strB.append(getTOString("epid", epid));
        strB.append(getTOString("epNo", epNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("act_num", act_num));
        strB.append(getTOXml("mem_no", mem_no));
        strB.append(getTOXml("princ_due", princ_due));
        strB.append(getTOXml("int_due", int_due));
        strB.append(getTOXml("penal", penal));
        strB.append(getTOXml("charges", charges));
        strB.append(getTOXml("arcfee", arcfee));
        strB.append(getTOXml("totalarc", totalarc));
        strB.append(getTOXml("fileNo", fileNo));
        strB.append(getTOXml("arbitDate", arbitDate));
        strB.append(getTOXml("arbRate", arbRate));
        strB.append(getTOXml("awardno", awardno));
        strB.append(getTOXml("awardDate", awardDate));
        strB.append(getTOXml("inspordDate", inspordDate));
        strB.append(getTOXml("inspordno", inspordno));
        strB.append(getTOXml("arb_type", arb_type));
        strB.append(getTOXml("miscCharges", miscCharges));
        strB.append(getTOXml("arbid", arbid));
        strB.append(getTOXml("epid", epid));
        strB.append(getTOXml("epNo", epNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
    /**
     * Getter for property prod_Type.
     *
     * @return Value of property prod_Type.
     */
}