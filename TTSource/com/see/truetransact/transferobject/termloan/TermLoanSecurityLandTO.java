/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanSecurityLandTO.java
 * 
 * Created on Thu Nov 24 14:57:35 IST 2011
 */
package com.see.truetransact.transferobject.termloan;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is LOANS_SECURITY_LAND.
 */
public class TermLoanSecurityLandTO extends TransferObject implements Serializable {

    private String gahanYesNo = "";
    private String acctNum = "";
    private String memberNo = "";
    private String memberName = "";
    private String documentNo = "";
    private String documentType = "";
    private Date documentDt = null;
    private String registeredOffice = "";
    private String pledge = "";
    private Date pledgeDt = null;
    private String pledgeNo = "";
    private Double pledgeAmount = null;
    private String village = "";
    private String surveyNo = "";
    private String totalArea = "";
    private String nature = "";
    private String right = "";
    private String remarks = "";
    private String branchCode = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String docGenId = "";
    private String oldSurvyNo = "";

    public String getOldSurvyNo() {
        return oldSurvyNo;
    }

    public void setOldSurvyNo(String oldSurvyNo) {
        this.oldSurvyNo = oldSurvyNo;
    }

    /**
     * Setter/Getter for ACCT_NUM - table Field
     */
    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAcctNum() {
        return acctNum;
    }

    /**
     * Setter/Getter for MEMBER_NO - table Field
     */
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberNo() {
        return memberNo;
    }

    /**
     * Setter/Getter for MEMBER_NAME - table Field
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName() {
        return memberName;
    }

    /**
     * Setter/Getter for DOCUMENT_NO - table Field
     */
    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    /**
     * Setter/Getter for DOCUMENT_TYPE - table Field
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentType() {
        return documentType;
    }

    /**
     * Setter/Getter for DOCUMENT_DT - table Field
     */
    public void setDocumentDt(Date documentDt) {
        this.documentDt = documentDt;
    }

    public Date getDocumentDt() {
        return documentDt;
    }

    /**
     * Setter/Getter for REGISTERED_OFFICE - table Field
     */
    public void setRegisteredOffice(String registeredOffice) {
        this.registeredOffice = registeredOffice;
    }

    public String getRegisteredOffice() {
        return registeredOffice;
    }

    /**
     * Setter/Getter for PLEDGE - table Field
     */
    public void setPledge(String pledge) {
        this.pledge = pledge;
    }

    public String getPledge() {
        return pledge;
    }

    /**
     * Setter/Getter for PLEDGE_DT - table Field
     */
    public void setPledgeDt(Date pledgeDt) {
        this.pledgeDt = pledgeDt;
    }

    public Date getPledgeDt() {
        return pledgeDt;
    }

    /**
     * Setter/Getter for PLEDGE_NO - table Field
     */
    public void setPledgeNo(String pledgeNo) {
        this.pledgeNo = pledgeNo;
    }

    public String getPledgeNo() {
        return pledgeNo;
    }

    /**
     * Setter/Getter for PLEDGE_AMOUNT - table Field
     */
    public void setPledgeAmount(Double pledgeAmount) {
        this.pledgeAmount = pledgeAmount;
    }

    public Double getPledgeAmount() {
        return pledgeAmount;
    }

    /**
     * Setter/Getter for VILLAGE - table Field
     */
    public void setVillage(String village) {
        this.village = village;
    }

    public String getVillage() {
        return village;
    }

    /**
     * Setter/Getter for SURVEY_NO - table Field
     */
    public void setSurveyNo(String surveyNo) {
        this.surveyNo = surveyNo;
    }

    public String getSurveyNo() {
        return surveyNo;
    }

    /**
     * Setter/Getter for TOTAL_AREA - table Field
     */
    public void setTotalArea(String totalArea) {
        this.totalArea = totalArea;
    }

    public String getTotalArea() {
        return totalArea;
    }

    /**
     * Setter/Getter for NATURE - table Field
     */
    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getNature() {
        return nature;
    }

    /**
     * Setter/Getter for REMARKS - table Field
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    /**
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
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
        strB.append(getTOString("acctNum", acctNum));
        strB.append(getTOString("gahanYesNo", gahanYesNo));
        strB.append(getTOString("memberNo", memberNo));
        strB.append(getTOString("memberName", memberName));
        strB.append(getTOString("documentNo", documentNo));
        strB.append(getTOString("documentType", documentType));
        strB.append(getTOString("documentDt", documentDt));
        strB.append(getTOString("registeredOffice", registeredOffice));
        strB.append(getTOString("pledge", pledge));
        strB.append(getTOString("pledgeDt", pledgeDt));
        strB.append(getTOString("pledgeNo", pledgeNo));
        strB.append(getTOString("pledgeAmount", pledgeAmount));
        strB.append(getTOString("village", village));
        strB.append(getTOString("surveyNo", surveyNo));
        strB.append(getTOString("totalArea", totalArea));
        strB.append(getTOString("nature", nature));
        strB.append(getTOString("right", right));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("docGenId", docGenId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("acctNum", acctNum));
        strB.append(getTOXml("gahanYesNo", gahanYesNo));
        strB.append(getTOXml("memberNo", memberNo));
        strB.append(getTOXml("memberName", memberName));
        strB.append(getTOXml("documentNo", documentNo));
        strB.append(getTOXml("documentType", documentType));
        strB.append(getTOXml("documentDt", documentDt));
        strB.append(getTOXml("registeredOffice", registeredOffice));
        strB.append(getTOXml("pledge", pledge));
        strB.append(getTOXml("pledgeDt", pledgeDt));
        strB.append(getTOXml("pledgeNo", pledgeNo));
        strB.append(getTOXml("pledgeAmount", pledgeAmount));
        strB.append(getTOXml("village", village));
        strB.append(getTOXml("surveyNo", surveyNo));
        strB.append(getTOXml("totalArea", totalArea));
        strB.append(getTOXml("nature", nature));
        strB.append(getTOXml("right", right));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("docGenId", docGenId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property right.
     *
     * @return Value of property right.
     */
    public java.lang.String getRight() {
        return right;
    }

    /**
     * Setter for property right.
     *
     * @param right New value of property right.
     */
    public void setRight(java.lang.String right) {
        this.right = right;
    }

    /**
     * Getter for property gahanYesNo.
     *
     * @return Value of property gahanYesNo.
     */
    public java.lang.String getGahanYesNo() {
        return gahanYesNo;
    }

    /**
     * Setter for property gahanYesNo.
     *
     * @param gahanYesNo New value of property gahanYesNo.
     */
    public void setGahanYesNo(java.lang.String gahanYesNo) {
        this.gahanYesNo = gahanYesNo;
    }

    /**
     * Getter for property docGenId.
     *
     * @return Value of property docGenId.
     */
    public java.lang.String getDocGenId() {
        return docGenId;
    }

    /**
     * Setter for property docGenId.
     *
     * @param docGenId New value of property docGenId.
     */
    public void setDocGenId(java.lang.String docGenId) {
        this.docGenId = docGenId;
    }
}
