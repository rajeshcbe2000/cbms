/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 *
 * GahanDocumentDetailsTO.java
 *
 * Created on April 23, 2012, 5:56 PM
 */
package com.see.truetransact.transferobject.customer.gahan;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;
import java.lang.StringBuffer;

/**
 *
 * @author admin
 */
public class GahanDocumentDetailsTO extends TransferObject implements Serializable {

    String documentNo = "";
    String documentGenId = "";
    String documentType = "";
    Date documentDt = null;
    String registredOffice = "";
    String pledge = "";
    Date pledgeDt = null;
    String pledgeNo = "";
    Double pledgeAmt = null;
    Date tdtGahanExpDate = null;
    String gahanRelease = "";
    String village = "";
    Date tdtGahanReleasExpDate = null;
    String surveyNo = "";
    String totalArea = "";
    String sno = "";
    String nature = "";
    String right = "";
    String authorizeStatus = null;
    String status = "";
    String authorizeBy = "";
    Date authorizeDt = null;
    String statusBy = "";
    Date StatusDt = null;
    String branchCode = "";
    String initiatedBranch = "";
    String remarks = "";
    String slno = "";
    String resurveyNo = "";
    String ownerNo = "";
    String ars = "";
    String gahanYesNo = "";
    String ownerNo2 = "";
    String gahanReleaseNo = "";
    //String slno="";

    public String getGahanYesNo() {
        return gahanYesNo;
    }

    public void setGahanYesNo(String gahanYesNo) {
        this.gahanYesNo = gahanYesNo;
    }
    
    public String getArs() {
        return ars;
    }

    public void setArs(String ars) {
        this.ars = ars;
    }

    public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getResurveyNo() {
        return resurveyNo;
    }

    public void setResurveyNo(String ResurveyNo) {
        this.resurveyNo = ResurveyNo;
    }

    public String getSlno() {
        return slno;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public String getKeyData() {
        setKeyColumns("documentNo");
        return documentNo;
    }

    public String getOwnerNo2() {
        return ownerNo2;
    }

    public void setOwnerNo2(String ownerNo2) {
        this.ownerNo2 = ownerNo2;
    }      

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("documentNo", documentNo));
        strB.append(getTOString("documentType", documentType));
        strB.append(getTOString("documentGenId", documentGenId));
        strB.append(getTOString("documentDt", documentDt));
        strB.append(getTOString("registredOffice", registredOffice));
        strB.append(getTOString("pledge", pledge));
        strB.append(getTOString("pledgeDt", pledgeDt));
        strB.append(getTOString("pledgeNo", pledgeNo));
        strB.append(getTOString("pledgeAmt", pledgeAmt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("StatusDt", StatusDt));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("tdtGahanExpDate", tdtGahanExpDate));
        strB.append(getTOString("gahanRelease", gahanRelease));
        strB.append(getTOString("tdtGahanReleasExpDate", tdtGahanReleasExpDate));
        strB.append(getTOString("nature", nature));
        strB.append(getTOString("village", village));
        strB.append(getTOString("surveyNo", surveyNo));
        strB.append(getTOString("totalArea", totalArea));
        strB.append(getTOString("right", right));
        strB.append(getTOString("sno", sno));
        strB.append(getTOString("gahanYesNo", gahanYesNo));
        strB.append(getTOString("ownerNo", ownerNo));
        strB.append(getTOString("ownerNo2", ownerNo2));
        strB.append(getTOString("gahanReleaseNo", gahanReleaseNo));
        return strB.toString();
    }

    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("documentNo", documentNo));
        strB.append(getTOXml("documentType", documentType));
        strB.append(getTOXml("documentGenId", documentGenId));
        strB.append(getTOXml("documentDt", documentDt));
        strB.append(getTOXml("registredOffice", registredOffice));
        strB.append(getTOXml("pledge", pledge));
        strB.append(getTOXml("pledgeDt", pledgeDt));
        strB.append(getTOXml("pledgeNo", pledgeNo));
        strB.append(getTOXml("pledgeAmt", pledgeAmt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("StatusDt", StatusDt));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("tdtGahanExpDate", tdtGahanExpDate));
        strB.append(getTOXml("gahanRelease", gahanRelease));
        strB.append(getTOXml("tdtGahanReleasExpDate", tdtGahanReleasExpDate));
        strB.append(getTOXml("nature", nature));
        strB.append(getTOXml("village", village));
        strB.append(getTOXml("surveyNo", surveyNo));
        strB.append(getTOXml("totalArea", totalArea));
        strB.append(getTOXml("right", right));
        strB.append(getTOXml("sno", sno));
        strB.append(getTOXml("gahanYesNo", gahanYesNo));
        strB.append(getTOXml("ownerNo", ownerNo));
        strB.append(getTOXml("ownerNo2", ownerNo2));
        strB.append(getTOXml("gahanReleaseNo", gahanReleaseNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property documentNo.
     *
     * @return Value of property documentNo.
     */
    public java.lang.String getDocumentNo() {
        return documentNo;
    }

    /**
     * Setter for property documentNo.
     *
     * @param documentNo New value of property documentNo.
     */
    public void setDocumentNo(java.lang.String documentNo) {
        this.documentNo = documentNo;
    }

    /**
     * Getter for property documentType.
     *
     * @return Value of property documentType.
     */
    public java.lang.String getDocumentType() {
        return documentType;
    }

    /**
     * Setter for property documentType.
     *
     * @param documentType New value of property documentType.
     */
    public void setDocumentType(java.lang.String documentType) {
        this.documentType = documentType;
    }

    /**
     * Getter for property documentDt.
     *
     * @return Value of property documentDt.
     */
    public java.util.Date getDocumentDt() {
        return documentDt;
    }

    /**
     * Setter for property documentDt.
     *
     * @param documentDt New value of property documentDt.
     */
    public void setDocumentDt(java.util.Date documentDt) {
        this.documentDt = documentDt;
    }

    /**
     * Getter for property registredOffice.
     *
     * @return Value of property registredOffice.
     */
    public java.lang.String getRegistredOffice() {
        return registredOffice;
    }

    public void setTdtGahanExpDate(Date tdtGahanExpDate) {
        this.tdtGahanExpDate = tdtGahanExpDate;
    }

    /**
     * Getter for property tdtPledgeDate.
     *
     * @return Value of property tdtPledgeDate.
     */
    public Date getTdtGahanExpDate() {
        return tdtGahanExpDate;
    }

    public Date getTdtGahanReleasExpDate() {
        return tdtGahanReleasExpDate;
    }

    /**
     * Setter for property txtDocumentNo.
     *
     * @param txtDocumentNo New value of property txtDocumentNo.
     */
    public void setTdtGahanReleasExpDate(Date tdtGahanReleasExpDate) {
        this.tdtGahanReleasExpDate = tdtGahanReleasExpDate;
    }

    public void setGahanRelease(String gahanRelease) {
        this.gahanRelease = gahanRelease;
    }

    /**
     * Getter for property tdtPledgeDate.
     *
     * @return Value of property tdtPledgeDate.
     */
    public String getGahanRelease() {
        return gahanRelease;
    }

    /**
     * Setter for property registredOffice.
     *
     * @param registredOffice New value of property registredOffice.
     */
    public void setRegistredOffice(java.lang.String registredOffice) {
        this.registredOffice = registredOffice;
    }

    /**
     * Getter for property pledge.
     *
     * @return Value of property pledge.
     */
    public java.lang.String getPledge() {
        return pledge;
    }

    /**
     * Setter for property pledge.
     *
     * @param pledge New value of property pledge.
     */
    public void setPledge(java.lang.String pledge) {
        this.pledge = pledge;
    }

    /**
     * Getter for property pledgeNo.
     *
     * @return Value of property pledgeNo.
     */
    public java.lang.String getPledgeNo() {
        return pledgeNo;
    }

    /**
     * Setter for property pledgeNo.
     *
     * @param pledgeNo New value of property pledgeNo.
     */
    public void setPledgeNo(java.lang.String pledgeNo) {
        this.pledgeNo = pledgeNo;
    }

    /**
     * Getter for property pledgeAmt.
     *
     * @return Value of property pledgeAmt.
     */
    public java.lang.Double getPledgeAmt() {
        return pledgeAmt;
    }

    /**
     * Setter for property pledgeAmt.
     *
     * @param pledgeAmt New value of property pledgeAmt.
     */
    public void setPledgeAmt(java.lang.Double pledgeAmt) {
        this.pledgeAmt = pledgeAmt;
    }

    /**
     * Getter for property village.
     *
     * @return Value of property village.
     */
    public java.lang.String getVillage() {
        return village;
    }

    /**
     * Setter for property village.
     *
     * @param village New value of property village.
     */
    public void setVillage(java.lang.String village) {
        this.village = village;
    }

    /**
     * Getter for property surveyNo.
     *
     * @return Value of property surveyNo.
     */
    public java.lang.String getSurveyNo() {
        return surveyNo;
    }

    /**
     * Setter for property surveyNo.
     *
     * @param surveyNo New value of property surveyNo.
     */
    public void setSurveyNo(java.lang.String surveyNo) {
        this.surveyNo = surveyNo;
    }

    /**
     * Getter for property totalArea.
     *
     * @return Value of property totalArea.
     */
    public java.lang.String getTotalArea() {
        return totalArea;
    }

    /**
     * Setter for property totalArea.
     *
     * @param totalArea New value of property totalArea.
     */
    public void setTotalArea(java.lang.String totalArea) {
        this.totalArea = totalArea;
    }

    public java.lang.String getSno() {
        return sno;
    }

    /**
     * Setter for property totalArea.
     *
     * @param totalArea New value of property totalArea.
     */
    public void setSno(java.lang.String sno) {
        this.sno = sno;
    }

    /**
     * Getter for property nature.
     *
     * @return Value of property nature.
     */
    public java.lang.String getNature() {
        return nature;
    }

    /**
     * Setter for property nature.
     *
     * @param nature New value of property nature.
     */
    public void setNature(java.lang.String nature) {
        this.nature = nature;
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
     * Getter for property authorizeStatus.
     *
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter for property authorizeStatus.
     *
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
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
     * Getter for property authorizeBy.
     *
     * @return Value of property authorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter for property authorizeBy.
     *
     * @param authorizeBy New value of property authorizeBy.
     */
    public void setAuthorizeBy(java.lang.String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    /**
     * Getter for property authorizeDt.
     *
     * @return Value of property authorizeDt.
     */
    public java.util.Date getAuthorizeDt() {
        return authorizeDt;
    }

    /**
     * Setter for property authorizeDt.
     *
     * @param authorizeDt New value of property authorizeDt.
     */
    public void setAuthorizeDt(java.util.Date authorizeDt) {
        this.authorizeDt = authorizeDt;
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
     * Getter for property StatusDt.
     *
     * @return Value of property StatusDt.
     */
    public java.util.Date getStatusDt() {
        return StatusDt;
    }

    /**
     * Setter for property StatusDt.
     *
     * @param StatusDt New value of property StatusDt.
     */
    public void setStatusDt(java.util.Date StatusDt) {
        this.StatusDt = StatusDt;
    }

    /**
     * Getter for property branchCode.
     *
     * @return Value of property branchCode.
     */
    public java.lang.String getBranchCode() {
        return branchCode;
    }

    /**
     * Setter for property branchCode.
     *
     * @param branchCode New value of property branchCode.
     */
    public void setBranchCode(java.lang.String branchCode) {
        this.branchCode = branchCode;
    }

    /**
     * Getter for property initiatedBranch.
     *
     * @return Value of property initiatedBranch.
     */
    public java.lang.String getInitiatedBranch() {
        return initiatedBranch;
    }

    /**
     * Setter for property initiatedBranch.
     *
     * @param initiatedBranch New value of property initiatedBranch.
     */
    public void setInitiatedBranch(java.lang.String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
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
     * Getter for property documentGenId.
     *
     * @return Value of property documentGenId.
     */
    public java.lang.String getDocumentGenId() {
        return documentGenId;
    }

    /**
     * Setter for property documentGenId.
     *
     * @param documentGenId New value of property documentGenId.
     */
    public void setDocumentGenId(java.lang.String documentGenId) {
        this.documentGenId = documentGenId;
    }

    /**
     * Getter for property pledgeDt.
     *
     * @return Value of property pledgeDt.
     */
    public java.util.Date getPledgeDt() {
        return pledgeDt;
    }

    /**
     * Setter for property pledgeDt.
     *
     * @param pledgeDt New value of property pledgeDt.
     */
    public void setPledgeDt(java.util.Date pledgeDt) {
        this.pledgeDt = pledgeDt;
    }

    public String getGahanReleaseNo() {
        return gahanReleaseNo;
    }

    public void setGahanReleaseNo(String gahanReleaseNo) {
        this.gahanReleaseNo = gahanReleaseNo;
    }
    
    
}
