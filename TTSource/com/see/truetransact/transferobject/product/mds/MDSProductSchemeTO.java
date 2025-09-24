/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSProductSchemeTO.java
 * 
 * Created on Thu Jun 02 17:30:23 IST 2011
 */
package com.see.truetransact.transferobject.product.mds;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is MDS_SCHEME_DETAILS.
 */
public class MDSProductSchemeTO extends TransferObject implements Serializable {

    private String prodId = "";
    private String schemeName = "";
    private String schemeDesc = "";
    private String resolutionNo = "";
    private Date resolutionDt = null;
    private String prodDesc = "";
    private String acctHead = "";
    private Integer noOfDivisions = 0;
    private Integer noOfAuctions = 0;
    private Integer noOfDraws = 0;
    private Integer noOfMemberPerDivision = 0;
    private Integer totalNoOfMembers = 0;
    private Double installmentAmount = 0.0;
    private Integer noOfInstallments = 0;
    private Double totalAmountDivision = 0.0;
    private Double totalAmountScheme = 0.0;
    private String chittal_No_Pattern = null;
    private Integer suffix_No = new Integer(1);
    private Integer next_Chittal_No = 1;
    private String installmentFrequency = "";
    private Date schemeStartDt = null;
    private Date schemeEndDt = null;
    private Integer installmentDay = 0;
    private Integer drawAuctDay = 0;
    private String thalayal = "";
    private String munnal = "";
    private String predefinitionInstallment = "";
    private String allDivision = "";
    private String division1 = null;
    private String division2 = null;
    private String division3 = null;
    private String division4 = null;
    private String afterPayment = "";
    private Integer installments = new Integer(0);  //AJITH Default value changed from null
    private Date schemeDate = null;
    private Integer day = 0;
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String multipleMember = "";
    private Double noOfChittals = 0.0;
    private Integer coNoOfInstallments = 0;
    private Integer maxNoOfMembers = 0;
    private String closureRate = "";
    private String sancNo = "";
    private Date sancDt = null;
    private String remarks = "";
    private String mdsType = "";
    private String branchCode = "";
    private String isRevPostAdv = "";
    private String isSpecialScheme = "N";
    private String chkBonusPrint = "";
    private String bankSettlement = ""; // Added by nithya on 11-08-2017 for 7145
    private String groupNo = "";
    private Integer schemeGracePeriod = 0;
    private String discountFirstInst = "N";
    private Double discountAmt = 0.0;
    private String autionTime = ""; //AJITH Changed location from line no:115 to top
    private String creditStampAdvance = "";
    
    public String getChkBonusPrint() {
        return chkBonusPrint;
    }

    public void setChkBonusPrint(String chkBonusPrint) {
        this.chkBonusPrint = chkBonusPrint;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getIsRevPostAdv() {
        return isRevPostAdv;
    }

    public void setIsRevPostAdv(String isRevPostAdv) {
        this.isRevPostAdv = isRevPostAdv;
    }

    public String getAutionTime() {
        return autionTime;
    }

    public void setAutionTime(String autionTime) {
        this.autionTime = autionTime;
    }
    //private String autionTime = "";

    public String getSancNo() {
        return sancNo;
    }

    public void setSancNo(String sancNo) {
        this.sancNo = sancNo;
    }

    public Date getSancDt() {
        return sancDt;
    }

    public void setSancDt(Date sancDt) {
        this.sancDt = sancDt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getClosureRate() {
        return closureRate;
    }

    public void setClosureRate(String closureRate) {
        this.closureRate = closureRate;
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

    /**
     * Setter/Getter for SCHEME_NAME - table Field
     */
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSchemeName() {
        return schemeName;
    }

    /**
     * Setter/Getter for RESOLUTION_NO - table Field
     */
    public void setResolutionNo(String resolutionNo) {
        this.resolutionNo = resolutionNo;
    }

    public String getResolutionNo() {
        return resolutionNo;
    }

    /**
     * Setter/Getter for RESOLUTION_DT - table Field
     */
    public void setResolutionDt(Date resolutionDt) {
        this.resolutionDt = resolutionDt;
    }

    public Date getResolutionDt() {
        return resolutionDt;
    }

    /**
     * Setter/Getter for PROD_DESC - table Field
     */
    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    /**
     * Setter/Getter for ACCT_HEAD - table Field
     */
    public void setAcctHead(String acctHead) {
        this.acctHead = acctHead;
    }

    public String getAcctHead() {
        return acctHead;
    }

    /**
     * Setter/Getter for INSTALLMENT_FREQUENCY - table Field
     */
    public void setInstallmentFrequency(String installmentFrequency) {
        this.installmentFrequency = installmentFrequency;
    }

    public String getInstallmentFrequency() {
        return installmentFrequency;
    }

    /**
     * Setter/Getter for SCHEME_START_DT - table Field
     */
    public void setSchemeStartDt(Date schemeStartDt) {
        this.schemeStartDt = schemeStartDt;
    }

    public Date getSchemeStartDt() {
        return schemeStartDt;
    }

    /**
     * Setter/Getter for SCHEME_END_DT - table Field
     */
    public void setSchemeEndDt(Date schemeEndDt) {
        this.schemeEndDt = schemeEndDt;
    }

    public Date getSchemeEndDt() {
        return schemeEndDt;
    }

    /**
     * Setter/Getter for THALAYAL - table Field
     */
    public void setThalayal(String thalayal) {
        this.thalayal = thalayal;
    }

    public String getThalayal() {
        return thalayal;
    }

    /**
     * Setter/Getter for MUNNAL - table Field
     */
    public void setMunnal(String munnal) {
        this.munnal = munnal;
    }

    public String getMunnal() {
        return munnal;
    }

    /**
     * Setter/Getter for PREDEFINITION_INSTALLMENT - table Field
     */
    public void setPredefinitionInstallment(String predefinitionInstallment) {
        this.predefinitionInstallment = predefinitionInstallment;
    }

    public String getPredefinitionInstallment() {
        return predefinitionInstallment;
    }

    /**
     * Setter/Getter for ALL_DIVISION - table Field
     */
    public void setAllDivision(String allDivision) {
        this.allDivision = allDivision;
    }

    public String getAllDivision() {
        return allDivision;
    }

    /**
     * Setter/Getter for AFTER_PAYMENT - table Field
     */
    public void setAfterPayment(String afterPayment) {
        this.afterPayment = afterPayment;
    }

    public String getAfterPayment() {
        return afterPayment;
    }

    /**
     * Setter/Getter for SCHEME_DATE - table Field
     */
    public void setSchemeDate(Date schemeDate) {
        this.schemeDate = schemeDate;
    }

    public Date getSchemeDate() {
        return schemeDate;
    }

    /**
     * Setter/Getter for STATUS - table Field
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Setter/Getter for STATUS_DT - table Field
     */
    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    /**
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    /**
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter/Getter for AUTHORIZED_BY - table Field
     */
    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public String getMdsType() {
        return mdsType;
    }

    public void setMdsType(String mdsType) {
        this.mdsType = mdsType;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
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
        strB.append(getTOString("isRevPostAdv", isRevPostAdv));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("schemeName", schemeName));
        strB.append(getTOString("schemeDesc", schemeDesc));
        strB.append(getTOString("resolutionNo", resolutionNo));
        strB.append(getTOString("resolutionDt", resolutionDt));
        strB.append(getTOString("prodDesc", prodDesc));
        strB.append(getTOString("chittal_No_Pattern", chittal_No_Pattern));
        strB.append(getTOString("suffix_No", suffix_No));
        strB.append(getTOString("next_Chittal_No", next_Chittal_No));
        strB.append(getTOString("acctHead", acctHead));
        strB.append(getTOString("noOfDivisions", noOfDivisions));
        strB.append(getTOString("noOfAuctions", noOfAuctions));
        strB.append(getTOString("noOfDraws", noOfDraws));
        strB.append(getTOString("noOfMemberPerDivision", noOfMemberPerDivision));
        strB.append(getTOString("totalNoOfMembers", totalNoOfMembers));
        strB.append(getTOString("installmentAmount", installmentAmount));
        strB.append(getTOString("noOfInstallments", noOfInstallments));
        strB.append(getTOString("totalAmountDivision", totalAmountDivision));
        strB.append(getTOString("totalAmountScheme", totalAmountScheme));
        strB.append(getTOString("installmentFrequency", installmentFrequency));
        strB.append(getTOString("schemeStartDt", schemeStartDt));
        strB.append(getTOString("schemeEndDt", schemeEndDt));
        strB.append(getTOString("installmentDay", installmentDay));
        strB.append(getTOString("drawAuctDay", drawAuctDay));
        strB.append(getTOString("thalayal", thalayal));
        strB.append(getTOString("munnal", munnal));
        strB.append(getTOString("predefinitionInstallment", predefinitionInstallment));
        strB.append(getTOString("allDivision", allDivision));
        strB.append(getTOString("division1", division1));
        strB.append(getTOString("division2", division2));
        strB.append(getTOString("division3", division3));
        strB.append(getTOString("division4", division4));
        strB.append(getTOString("afterPayment", afterPayment));
        strB.append(getTOString("installments", installments));
        strB.append(getTOString("schemeDate", schemeDate));
        strB.append(getTOString("day", day));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("multipleMember", multipleMember));
        strB.append(getTOString("noOfChittals", noOfChittals));
        strB.append(getTOString("coNoOfInstallments", coNoOfInstallments));
        strB.append(getTOString("maxNoOfMembers", maxNoOfMembers));
        strB.append(getTOString("mdsType", mdsType));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("isSpecialScheme", isSpecialScheme));
        strB.append(getTOString("chkBonusPrint", chkBonusPrint));
        strB.append(getTOString("schemeGracePeriod", schemeGracePeriod));
        strB.append(getTOString("discountFirstInst", discountFirstInst));
        strB.append(getTOString("discountAmt", discountAmt));
        strB.append(getTOString("autionTime", autionTime));  //AJITH included the variable to this list on 6/7/2021
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
        strB.append(getTOXml("isRevPostAdv", isRevPostAdv));  //AJITH Changed from getTOString() on 6/7/2021
        strB.append(getTOXml("schemeName", schemeName));
        strB.append(getTOXml("schemeDesc", schemeDesc));
        strB.append(getTOXml("resolutionNo", resolutionNo));
        strB.append(getTOXml("resolutionDt", resolutionDt));
        strB.append(getTOXml("prodDesc", prodDesc));
        strB.append(getTOXml("chittal_No_Pattern", chittal_No_Pattern));
        strB.append(getTOXml("suffix_No", suffix_No));
        strB.append(getTOXml("next_Chittal_No", next_Chittal_No));
        strB.append(getTOXml("acctHead", acctHead));
        strB.append(getTOXml("noOfDivisions", noOfDivisions));
        strB.append(getTOXml("noOfAuctions", noOfAuctions));
        strB.append(getTOXml("noOfDraws", noOfDraws));
        strB.append(getTOXml("noOfMemberPerDivision", noOfMemberPerDivision));
        strB.append(getTOXml("totalNoOfMembers", totalNoOfMembers));
        strB.append(getTOXml("installmentAmount", installmentAmount));
        strB.append(getTOXml("noOfInstallments", noOfInstallments));
        strB.append(getTOXml("totalAmountDivision", totalAmountDivision));
        strB.append(getTOXml("totalAmountScheme", totalAmountScheme));
        strB.append(getTOXml("installmentFrequency", installmentFrequency));
        strB.append(getTOXml("schemeStartDt", schemeStartDt));
        strB.append(getTOXml("schemeEndDt", schemeEndDt));
        strB.append(getTOXml("installmentDay", installmentDay));
        strB.append(getTOXml("drawAuctDay", drawAuctDay));
        strB.append(getTOXml("thalayal", thalayal));
        strB.append(getTOXml("munnal", munnal));
        strB.append(getTOXml("predefinitionInstallment", predefinitionInstallment));
        strB.append(getTOXml("allDivision", allDivision));
        strB.append(getTOXml("division1", division1));
        strB.append(getTOXml("division2", division2));
        strB.append(getTOXml("division3", division3));
        strB.append(getTOXml("division4", division4));
        strB.append(getTOXml("afterPayment", afterPayment));
        strB.append(getTOXml("installments", installments));
        strB.append(getTOXml("schemeDate", schemeDate));
        strB.append(getTOXml("day", day));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("multipleMember", multipleMember));
        strB.append(getTOXml("noOfChittals", noOfChittals));
        strB.append(getTOXml("coNoOfInstallments", coNoOfInstallments));
        strB.append(getTOXml("maxNoOfMembers", maxNoOfMembers));
        strB.append(getTOXml("mdsType", mdsType));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("isSpecialScheme", isSpecialScheme));
        strB.append(getTOXml("chkBonusPrint", chkBonusPrint));
        strB.append(getTOXml("schemeGracePeriod", schemeGracePeriod));
        strB.append(getTOXml("discountFirstInst", discountFirstInst));
        strB.append(getTOXml("discountAmt", discountAmt));
        strB.append(getTOXml("autionTime", autionTime));  //AJITH included the variable to this list on 6/7/2021
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public Integer getNoOfDivisions() {
        return noOfDivisions;
    }

    public void setNoOfDivisions(Integer noOfDivisions) {
        this.noOfDivisions = noOfDivisions;
    }

    public Integer getNoOfAuctions() {
        return noOfAuctions;
    }

    public void setNoOfAuctions(Integer noOfAuctions) {
        this.noOfAuctions = noOfAuctions;
    }

    public Integer getNoOfDraws() {
        return noOfDraws;
    }

    public void setNoOfDraws(Integer noOfDraws) {
        this.noOfDraws = noOfDraws;
    }

    public Integer getNoOfMemberPerDivision() {
        return noOfMemberPerDivision;
    }

    public void setNoOfMemberPerDivision(Integer noOfMemberPerDivision) {
        this.noOfMemberPerDivision = noOfMemberPerDivision;
    }

    public Integer getTotalNoOfMembers() {
        return totalNoOfMembers;
    }

    public void setTotalNoOfMembers(Integer totalNoOfMembers) {
        this.totalNoOfMembers = totalNoOfMembers;
    }

    public Double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(Double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public Integer getNoOfInstallments() {
        return noOfInstallments;
    }

    public void setNoOfInstallments(Integer noOfInstallments) {
        this.noOfInstallments = noOfInstallments;
    }

    public Double getTotalAmountDivision() {
        return totalAmountDivision;
    }

    public void setTotalAmountDivision(Double totalAmountDivision) {
        this.totalAmountDivision = totalAmountDivision;
    }

    public Double getTotalAmountScheme() {
        return totalAmountScheme;
    }

    public void setTotalAmountScheme(Double totalAmountScheme) {
        this.totalAmountScheme = totalAmountScheme;
    }

    /**
     * Getter for property division1.
     *
     * @return Value of property division1.
     */
    public java.lang.String getDivision1() {
        return division1;
    }

    /**
     * Setter for property division1.
     *
     * @param division1 New value of property division1.
     */
    public void setDivision1(java.lang.String division1) {
        this.division1 = division1;
    }

    /**
     * Getter for property division2.
     *
     * @return Value of property division2.
     */
    public java.lang.String getDivision2() {
        return division2;
    }

    /**
     * Setter for property division2.
     *
     * @param division2 New value of property division2.
     */
    public void setDivision2(java.lang.String division2) {
        this.division2 = division2;
    }

    /**
     * Getter for property division3.
     *
     * @return Value of property division3.
     */
    public java.lang.String getDivision3() {
        return division3;
    }

    /**
     * Setter for property division3.
     *
     * @param division3 New value of property division3.
     */
    public void setDivision3(java.lang.String division3) {
        this.division3 = division3;
    }

    /**
     * Getter for property division4.
     *
     * @return Value of property division4.
     */
    public java.lang.String getDivision4() {
        return division4;
    }

    /**
     * Setter for property division4.
     *
     * @param division4 New value of property division4.
     */
    public void setDivision4(java.lang.String division4) {
        this.division4 = division4;
    }

    public Integer getInstallmentDay() {
        return installmentDay;
    }

    public void setInstallmentDay(Integer installmentDay) {
        this.installmentDay = installmentDay;
    }

    public Integer getDrawAuctDay() {
        return drawAuctDay;
    }

    public void setDrawAuctDay(Integer drawAuctDay) {
        this.drawAuctDay = drawAuctDay;
    }

    /**
     * Getter for property chittal_No_Pattern.
     *
     * @return Value of property chittal_No_Pattern.
     */
    public java.lang.String getChittal_No_Pattern() {
        return chittal_No_Pattern;
    }

    /**
     * Setter for property chittal_No_Pattern.
     *
     * @param chittal_No_Pattern New value of property chittal_No_Pattern.
     */
    public void setChittal_No_Pattern(java.lang.String chittal_No_Pattern) {
        this.chittal_No_Pattern = chittal_No_Pattern;
    }

    public Integer getSuffix_No() {
        return suffix_No;
    }

    public void setSuffix_No(Integer suffix_No) {
        this.suffix_No = suffix_No;
    }

    public Integer getNext_Chittal_No() {
        return next_Chittal_No;
    }

    public void setNext_Chittal_No(Integer next_Chittal_No) {
        this.next_Chittal_No = next_Chittal_No;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    

    /**
     * Getter for property schemeDesc.
     *
     * @return Value of property schemeDesc.
     */
    public java.lang.String getSchemeDesc() {
        return schemeDesc;
    }

    /**
     * Setter for property schemeDesc.
     *
     * @param schemeDesc New value of property schemeDesc.
     */
    public void setSchemeDesc(java.lang.String schemeDesc) {
        this.schemeDesc = schemeDesc;
    }

    /**
     * Getter for property multipleMember.
     *
     * @return Value of property multipleMember.
     */
    public java.lang.String getMultipleMember() {
        return multipleMember;
    }

    /**
     * Setter for property multipleMember.
     *
     * @param multipleMember New value of property multipleMember.
     */
    public void setMultipleMember(java.lang.String multipleMember) {
        this.multipleMember = multipleMember;
    }

    public Double getNoOfChittals() {
        return noOfChittals;
    }

    public void setNoOfChittals(Double noOfChittals) {
        this.noOfChittals = noOfChittals;
    }

    public Integer getCoNoOfInstallments() {
        return coNoOfInstallments;
    }

    public void setCoNoOfInstallments(Integer coNoOfInstallments) {
        this.coNoOfInstallments = coNoOfInstallments;
    }

    public Integer getMaxNoOfMembers() {
        return maxNoOfMembers;
    }

    public void setMaxNoOfMembers(Integer maxNoOfMembers) {
        this.maxNoOfMembers = maxNoOfMembers;
    }

    public String getIsSpecialScheme() {
        return isSpecialScheme;
    }

    public void setIsSpecialScheme(String isSpecialScheme) {
        this.isSpecialScheme = isSpecialScheme;
    }

    public String getBankSettlement() {
        return bankSettlement;
    }

    public void setBankSettlement(String bankSettlement) {
        this.bankSettlement = bankSettlement;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public Integer getSchemeGracePeriod() {
        return schemeGracePeriod;
    }

    public void setSchemeGracePeriod(Integer schemeGracePeriod) {
        this.schemeGracePeriod = schemeGracePeriod;
    }

    public Double getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(Double discountAmt) {
        this.discountAmt = discountAmt;
    }

    public String getDiscountFirstInst() {
        return discountFirstInst;
    }

    public void setDiscountFirstInst(String discountFirstInst) {
        this.discountFirstInst = discountFirstInst;
    }

    public String getCreditStampAdvance() {
        return creditStampAdvance;
    }

    public void setCreditStampAdvance(String creditStampAdvance) {
        this.creditStampAdvance = creditStampAdvance;
    }
    
    
    
    
}