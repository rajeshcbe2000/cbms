/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositPeriodwiseSettingTO.java
 *
 * Created on 16 September, 2011, 2:24 PM
 */
package com.see.truetransact.transferobject.supporting.depositperiodwisesetting;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;
import java.util.*;

/**
 *
 * @author aravind
 */
public class DepositPeriodwiseSettingTO extends TransferObject implements Serializable {

    private String id = "";
    private String periodname = "";
    private String periodtype = "";
    private String periodfrom = "";
    private String periodto = "";
    private String priority = "";
    private String periodrange = "";
    private String periodtype1 = "";
    private String periodtype2 = "";
    private String periodfrom1 = "";
    private String periodto1 = "";
    private String priority1 = "";
    private String doubtfrom = "";
    private String doubtto = "";
    private String badfrom = "";
    private String badto = "";
    private String docdoubtfrom = "";
    private String docdoubtto = "";
    private String docbadfrom = "";
    private String docbadto = "";
    private String doubtnarra = "";
    private String badnarra = "";
    private String docdoubtnara = "";
    private String docbadnara = "";
    private String priority2 = "";
    private String desc = "";
    private String periodfrom3 = "";
    private String periodtype3 = "";
    private String periodto4 = "";
    private String periodtype4 = "";
    // private String AID = "";
    private String amountrange = "";
    // private String periodtype = "";
    private String fromamount = "";
    private String toamount = "";
    private String priorityy = "";
    private Integer pan;
    private String amountrange1 = "";
    // private String periodtype = "";
    private String fromamount1 = "";
    private String toamount1 = "";
    private String priorityy1 = "";
    private String fluidType = "";
    private String module = "";
    private String description = "";
    private String percentage = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String status = "";
    private Date createdDt = null;
    private String currBranName = "";
    private String branCode = "";
    private String createdBy = "";
    private HashMap _authorizeMap;
    private Integer result;

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append(getTOStringKey(getKeyData()));

        strB.append(getTOString("id", id));
        strB.append(getTOString("periodname", periodname));
        strB.append(getTOString("periodfrom", periodfrom));
        strB.append(getTOString("periodto", periodto));
        strB.append(getTOString("priority", priority));
        strB.append(getTOString("periodtype", periodtype));

        //  strB.append(getTOString("id", id));
        strB.append(getTOString("amountrange", amountrange));
        strB.append(getTOString("fromamount", fromamount));
        strB.append(getTOString("toamount", toamount));
        strB.append(getTOString("priorityy", priorityy));




        strB.append(getTOString("amountrange1", amountrange1));
        strB.append(getTOString("fromamount1", fromamount1));
        strB.append(getTOString("toamount1", toamount1));
        strB.append(getTOString("priorityy1", priorityy1));

        strB.append(getTOString("doubtfrom", doubtfrom));
        strB.append(getTOString("doubtto", doubtto));
        strB.append(getTOString("badfrom", badfrom));
        strB.append(getTOString("badto", badto));
        strB.append(getTOString("docdoubtfrom", docdoubtfrom));
        strB.append(getTOString("docdoubtto", docdoubtto));
        strB.append(getTOString("docbadfrom", docbadfrom));
        strB.append(getTOString("docbadto", docbadto));
        strB.append(getTOString("doubtnarra", doubtnarra));
        strB.append(getTOString("badnarra", badnarra));
        strB.append(getTOString("docdoubtnara", docdoubtnara));
        strB.append(getTOString("docbadnara", docbadnara));

        strB.append(getTOString("fluidType", fluidType));
        strB.append(getTOString("module", module));
        strB.append(getTOString("description", description));
        strB.append(getTOString("percentage", percentage));

        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("status", status));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("currBranName", currBranName));
        strB.append(getTOString("branCode", branCode));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        //strB.append(getTOXmlKey(getKeyData()));
        System.out.println("toooootooooooooo" + id + periodname);
        strB.append(getTOXml("id", id));
        strB.append(getTOXml("periodname", periodname));
        strB.append(getTOXml("periodfrom", periodfrom));
        strB.append(getTOXml("periodto", periodto));
        strB.append(getTOXml("priority", priority));
        strB.append(getTOXml("periodtype", periodtype));

        // strB.append(getTOXml("id", id));
        strB.append(getTOXml("amountrange", amountrange));
        strB.append(getTOXml("fromamount", fromamount));
        strB.append(getTOXml("toamount", toamount));
        strB.append(getTOXml("priorityy", priorityy));


        strB.append(getTOXml("amountrange1", amountrange1));
        strB.append(getTOXml("fromamount1", fromamount1));
        strB.append(getTOXml("toamount1", toamount1));
        strB.append(getTOXml("priorityy1", priorityy1));

        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("currBranName", currBranName));
        strB.append(getTOXml("branCode", branCode));


        strB.append(getTOXml("doubtfrom", doubtfrom));
        strB.append(getTOXml("doubtto", doubtto));
        strB.append(getTOXml("badfrom", badfrom));
        strB.append(getTOXml("badto", badto));
        strB.append(getTOXml("docdoubtfrom", docdoubtfrom));
        strB.append(getTOXml("docdoubtto", docdoubtto));
        strB.append(getTOXml("docbadfrom", docbadfrom));
        strB.append(getTOXml("docbadto", docbadto));
        strB.append(getTOXml("doubtnarra", doubtnarra));
        strB.append(getTOXml("badnarra", badnarra));
        strB.append(getTOXml("docdoubtnara", docdoubtnara));
        strB.append(getTOXml("docbadnara", docbadnara));

        strB.append(getTOXml("fluidType", fluidType));
        strB.append(getTOXml("module", module));
        strB.append(getTOXml("description", description));
        strB.append(getTOXml("percentage", percentage));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public void setamountrange(String amountrange) {
        this.amountrange = amountrange;
    }

    public String getamountrange() {
        return amountrange;
    }

    /**
     * Setter/Getter for START_DD_NO1 - table Field
     */
    public void setfromamount(String fromamount) {
        this.fromamount = fromamount;
    }

    public String getfromamount() {
        return fromamount;
    }

    /**
     * Setter/Getter for START_DD_NO2 - table Field
     */
    public void settoamount(String toamount) {
        this.toamount = toamount;
    }

    public String gettoamount() {
        return toamount;
    }

    /**
     * Setter/Getter for END_DD_NO1 - table Field
     */
    public void setpriorityy(String priorityy) {
        this.priorityy = priorityy;
    }

    public String getpriorityy() {
        return priorityy;
    }

    public void setamountrange1(String amountrange1) {
        this.amountrange1 = amountrange1;
    }

    public String getamountrange1() {
        return amountrange1;
    }

    /**
     * Setter/Getter for START_DD_NO1 - table Field
     */
    public void setfromamount1(String fromamount1) {
        this.fromamount1 = fromamount1;
    }

    public String getfromamount1() {
        return fromamount1;
    }

    /**
     * Setter/Getter for START_DD_NO2 - table Field
     */
    public void settoamount1(String toamount1) {
        this.toamount1 = toamount1;
    }

    public String gettoamount1() {
        return toamount1;
    }

    /**
     * Setter/Getter for END_DD_NO1 - table Field
     */
    public void setpriorityy1(String priorityy1) {
        this.priorityy1 = priorityy1;
    }

    public String getpriorityy1() {
        return priorityy1;
    }

    /**
     * Getter for property nameAddress.
     *
     * @return Value of property nameAddress.
     */
    public void setperiodtype(String periodtype) {
        this.periodtype = periodtype;
    }

    public String getperiodtype() {
        return periodtype;
    }

    /**
     * Setter/Getter for DD_LEAF_TYPE - table Field
     */
    public void setperiodname(String periodname) {
        this.periodname = periodname;
    }

    public String getperiodname() {
        return periodname;
    }

    /**
     * Setter/Getter for START_DD_NO1 - table Field
     */
    public void setperiodfrom(String periodfrom) {
        this.periodfrom = periodfrom;
    }

    public String getperiodfrom() {
        return periodfrom;
    }

    /**
     * Setter/Getter for START_DD_NO2 - table Field
     */
    public void setperiodto(String periodto) {
        this.periodto = periodto;
    }

    public String getperiodto() {
        return periodto;
    }

    /**
     * Setter/Getter for END_DD_NO1 - table Field
     */
    public void setpriority(String priority) {
        this.priority = priority;
    }

    public String getpriority() {
        return priority;
    }

    /**
     * Getter for property statusBy.
     *
     * @return Value of property statusBy.
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter for property statusBy.
     *
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
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
     * Getter for property createdDt.
     *
     * @return Value of property createdDt.
     */
    /**
     * Getter for property currBranName.
     *
     * @return Value of property currBranName.
     */
    public java.lang.String getCurrBranName() {
        return currBranName;
    }

    /**
     * Setter for property currBranName.
     *
     * @param currBranName New value of property currBranName.
     */
    public void setCurrBranName(java.lang.String currBranName) {
        this.currBranName = currBranName;
    }

    /**
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public Integer getResult() {
        return result;
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(Integer result) {
        this.result = result;
    }

    /**
     * Getter for property dateofVisit.
     *
     * @return Value of property dateofVisit.
     */
    /**
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public java.util.Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
    }

    /**
     * Getter for property cratedDt.
     *
     * @return Value of property cratedDt.
     */
    public java.util.Date getCreatedDt() {
        return createdDt;
    }

    /**
     * Setter for property cratedDt.
     *
     * @param cratedDt New value of property cratedDt.
     */
    public void setCreatedDt(java.util.Date createdDt) {
        this.createdDt = createdDt;
    }

    /**
     * Getter for property createdBy.
     *
     * @return Value of property createdBy.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter for property createdBy.
     *
     * @param createdBy New value of property createdBy.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Getter for property branCode.
     *
     * @return Value of property branCode.
     */
    public java.lang.String getBranCode() {
        return branCode;
    }

    /**
     * Setter for property branCode.
     *
     * @param branCode New value of property branCode.
     */
    public void setBranCode(java.lang.String branCode) {
        this.branCode = branCode;
    }

    public void setperiodtype1(String periodtype1) {
        this.periodtype1 = periodtype1;
    }

    public String getperiodtype1() {
        return periodtype1;
    }

    public void setperiodtype2(String periodtype2) {
        this.periodtype2 = periodtype2;
    }

    public String getperiodtype2() {
        return periodtype2;
    }

    /**
     * Setter/Getter for DD_LEAF_TYPE - table Field
     */
    public void setperiodrange(String periodrange) {
        this.periodrange = periodrange;
    }

    public String getperiodrange() {
        return periodrange;
    }

    /**
     * Setter/Getter for START_DD_NO1 - table Field
     */
    public void setperiodfrom1(String periodfrom1) {
        this.periodfrom1 = periodfrom1;
    }

    public String getperiodfrom1() {
        return periodfrom1;
    }

    /**
     * Setter/Getter for START_DD_NO2 - table Field
     */
    public void setperiodto1(String periodto1) {
        this.periodto1 = periodto1;
    }

    public String getperiodto1() {
        return periodto1;
    }

    /**
     * Setter/Getter for END_DD_NO1 - table Field
     */
    public void setpriority1(String priority1) {
        this.priority1 = priority1;
    }

    public String getpriority1() {
        return priority1;
    }

    /**
     * Getter for property priority2.
     *
     * @return Value of property priority2.
     */
    public java.lang.String getPriority2() {
        return priority2;
    }

    /**
     * Setter for property priority2.
     *
     * @param priority2 New value of property priority2.
     */
    public void setPriority2(java.lang.String priority2) {
        this.priority2 = priority2;
    }

    /**
     * Getter for property desc.
     *
     * @return Value of property desc.
     */
    public java.lang.String getDesc() {
        return desc;
    }

    /**
     * Setter for property desc.
     *
     * @param desc New value of property desc.
     */
    public void setDesc(java.lang.String desc) {
        this.desc = desc;
    }

    /**
     * Getter for property periodfrom3.
     *
     * @return Value of property periodfrom3.
     */
    public java.lang.String getPeriodfrom3() {
        return periodfrom3;
    }

    /**
     * Setter for property periodfrom3.
     *
     * @param periodfrom3 New value of property periodfrom3.
     */
    public void setPeriodfrom3(java.lang.String periodfrom3) {
        this.periodfrom3 = periodfrom3;
    }

    /**
     * Getter for property periodtype3.
     *
     * @return Value of property periodtype3.
     */
    public java.lang.String getPeriodtype3() {
        return periodtype3;
    }

    /**
     * Setter for property periodtype3.
     *
     * @param periodtype3 New value of property periodtype3.
     */
    public void setPeriodtype3(java.lang.String periodtype3) {
        this.periodtype3 = periodtype3;
    }

    /**
     * Getter for property periodto4.
     *
     * @return Value of property periodto4.
     */
    public java.lang.String getPeriodto4() {
        return periodto4;
    }

    /**
     * Setter for property periodto4.
     *
     * @param periodto4 New value of property periodto4.
     */
    public void setPeriodto4(java.lang.String periodto4) {
        this.periodto4 = periodto4;
    }

    /**
     * Getter for property periodtype4.
     *
     * @return Value of property periodtype4.
     */
    public java.lang.String getPeriodtype4() {
        return periodtype4;
    }

    /**
     * Setter for property periodtype4.
     *
     * @param periodtype4 New value of property periodtype4.
     */
    public void setPeriodtype4(java.lang.String periodtype4) {
        this.periodtype4 = periodtype4;
    }

    public java.lang.String getdoubtfrom() {
        return doubtfrom;
    }

    public void setdoubtfrom(java.lang.String doubtfrom) {
        this.doubtfrom = doubtfrom;
    }

    public java.lang.String getdoubtto() {
        return doubtto;
    }

    public void setdoubtto(java.lang.String doubtto) {
        this.doubtto = doubtto;
    }

    public java.lang.String getbadfrom() {
        return badfrom;
    }

    public void setbadfrom(java.lang.String badfrom) {
        this.badfrom = badfrom;
    }

    public java.lang.String getbadto() {
        return badto;
    }

    public void setbadto(java.lang.String badto) {
        this.badto = badto;
    }

    public java.lang.String getdocdoubtfrom() {
        return docdoubtfrom;
    }

    public void setdocdoubtfrom(java.lang.String docdoubtfrom) {
        this.docdoubtfrom = docdoubtfrom;
    }

    public java.lang.String getdocdoubtto() {
        return docdoubtto;
    }

    public void setdocdoubtto(java.lang.String docdoubtto) {
        this.docdoubtto = docdoubtto;
    }

    public java.lang.String getdocbadfrom() {
        return docbadfrom;
    }

    public void setdocbadfrom(java.lang.String docbadfrom) {
        this.docbadfrom = docbadfrom;
    }

    public java.lang.String getdocbadto() {
        return docbadto;
    }

    public void setdocbadto(java.lang.String docbadto) {
        this.docbadto = docbadto;
    }

    public java.lang.String getdoubtnarra() {
        return doubtnarra;
    }

    public void setdoubtnarra(java.lang.String doubtnarra) {
        this.doubtnarra = doubtnarra;
    }

    public java.lang.String getbadnarra() {
        return badnarra;
    }

    public void setbadnarra(java.lang.String badnarra) {
        this.badnarra = badnarra;
    }

    public java.lang.String getdocdoubtnara() {
        return docdoubtnara;
    }

    public void setdocdoubtnara(java.lang.String docdoubtnara) {
        this.docdoubtnara = docdoubtnara;
    }

    public java.lang.String getdocbadnara() {
        return docbadnara;
    }

    public void setdocbadnara(java.lang.String docbadnara) {
        this.docbadnara = docbadnara;
    }

    /**
     * Getter for property fluidType.
     *
     * @return Value of property fluidType.
     */
    public java.lang.String getFluidType() {
        return fluidType;
    }

    /**
     * Setter for property fluidType.
     *
     * @param fluidType New value of property fluidType.
     */
    public void setFluidType(java.lang.String fluidType) {
        this.fluidType = fluidType;
    }

    /**
     * Getter for property module.
     *
     * @return Value of property module.
     */
    public java.lang.String getModule() {
        return module;
    }

    /**
     * Setter for property module.
     *
     * @param module New value of property module.
     */
    public void setModule(java.lang.String module) {
        this.module = module;
    }

    /**
     * Getter for property description.
     *
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * Setter for property description.
     *
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     * Getter for property percentage.
     *
     * @return Value of property percentage.
     */
    public java.lang.String getPercentage() {
        return percentage;
    }

    /**
     * Setter for property percentage.
     *
     * @param percentage New value of property percentage.
     */
    public void setPercentage(java.lang.String percentage) {
        this.percentage = percentage;
    }

    /**
     * Getter for property id.
     *
     * @return Value of property id.
     */
    public java.lang.String getid() {
        return id;
    }

    /**
     * Setter for property id.
     *
     * @param id New value of property id.
     */
    public void setid(java.lang.String id) {
        this.id = id;
    }
}
