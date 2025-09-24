/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustomerPhoneTO.java
 *
 * Created on Wed Feb 16 09:38:12 IST 2005
 */
package com.see.truetransact.transferobject.employee;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUST_PHONE.
 */
public class EmployeePresentDetailsTO extends TransferObject implements Serializable {

    private String sysId = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String rdoGradutionIncrementYesNo = "";
    private Date tdtGradutionIncrementReleasedDate = null;
    private String rdoCAIIBPART1IncrementYesNo = "";
    private Date tdtCAIIBPART1IncrementReleasedDate = null;
    private String rdoCAIIBPART2IncrementYesNo = "";
    private Date tdtCAIIBPART2IncrementReleasedDate = null;
    private String rdoAnyOtherIncrementYesNo = "";
    private String txtAnyOtherIncrementInstitutionName = "";
    private Date tdtAnyOtherIncrementReleasedDate = null;
    private String txtPresentBasic = "";
    private Date tdtLastIncrmentDate = null;
    private String txtLossPay_Months = "";
    private String txtLossOfpay_Days = "";
    private Date tdtNextIncrmentDate = null;
    private String rdoSigNoYesNo = "";
    private String txtSignatureNo = "";
    private String cboUnionMember = "";
    private String cboSocietyMember = "";
    private String societyMemberNo = "";
    private String clubMembership = "";
    private String clubName = "";
    private String txtProbationPeriod = "";
    private String cboProbationPeriod = "";
    private Date tdtConfirmationDate = null;
    private String tdtDateofRetirement = "";
    private String txtPFNumber = "";
    private String cboPFAcNominee = "";
    private String cboPresentBranchId = "";
    private String cboReginoalOffice = "";
    private String cboZonalOffice = "";
    private Date tdtWorkingSince = null;
    private String cboDesignation = "";
    private String cboPresentGrade = "";

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
     * Setter/Getter for STATUS_BY - table Field
     */
    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(sysId);
        return sysId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("sysId", sysId));
        strB.append(getTOString("rdoGradutionIncrementYesNo", rdoGradutionIncrementYesNo));
        strB.append(getTOString("tdtGradutionIncrementReleasedDate", tdtGradutionIncrementReleasedDate));
        strB.append(getTOString("rdoCAIIBPART1IncrementYesNo", rdoCAIIBPART1IncrementYesNo));
        strB.append(getTOString("tdtCAIIBPART1IncrementReleasedDate", tdtCAIIBPART1IncrementReleasedDate));
        strB.append(getTOString("rdoCAIIBPART2IncrementYesNo", rdoCAIIBPART2IncrementYesNo));
        strB.append(getTOString("tdtCAIIBPART2IncrementReleasedDate", tdtCAIIBPART2IncrementReleasedDate));
        strB.append(getTOString("rdoAnyOtherIncrementYesNo", rdoAnyOtherIncrementYesNo));
        strB.append(getTOString("txtAnyOtherIncrementInstitutionName", txtAnyOtherIncrementInstitutionName));
        strB.append(getTOString("tdtAnyOtherIncrementReleasedDate", tdtAnyOtherIncrementReleasedDate));
        strB.append(getTOString("txtPresentBasic", txtPresentBasic));
        strB.append(getTOString("tdtLastIncrmentDate", tdtLastIncrmentDate));
        strB.append(getTOString("txtLossPay_Months", txtLossPay_Months));
        strB.append(getTOString("txtLossOfpay_Days", txtLossOfpay_Days));
        strB.append(getTOString("tdtNextIncrmentDate", tdtNextIncrmentDate));
        strB.append(getTOString("rdoSigNoYesNo", rdoSigNoYesNo));
        strB.append(getTOString("txtSignatureNo", txtSignatureNo));
        strB.append(getTOString("cboUnionMember", cboUnionMember));
        strB.append(getTOString("cboSocietyMember", cboSocietyMember));
        strB.append(getTOString("clubMembership", clubMembership));
        strB.append(getTOString("clubName", clubName));
        strB.append(getTOString("txtProbationPeriod", txtProbationPeriod));
        strB.append(getTOString("cboProbationPeriod", cboProbationPeriod));
        strB.append(getTOString("tdtConfirmationDate", tdtConfirmationDate));
        strB.append(getTOString("tdtDateofRetirement", tdtDateofRetirement));
        strB.append(getTOString("txtPFNumber", txtPFNumber));
        strB.append(getTOString("cboPFAcNominee", cboPFAcNominee));
        strB.append(getTOString("cboPresentBranchId", cboPresentBranchId));
        strB.append(getTOString("cboReginoalOffice", cboReginoalOffice));
        strB.append(getTOString("cboZonalOffice", cboZonalOffice));
        strB.append(getTOString("tdtWorkingSince", tdtWorkingSince));
        strB.append(getTOString("cboDesignation", cboDesignation));
        strB.append(getTOString("cboPresentGrade", cboPresentGrade));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("sysId", sysId));
        strB.append(getTOXml("rdoGradutionIncrementYesNo", rdoGradutionIncrementYesNo));
        strB.append(getTOXml("tdtGradutionIncrementReleasedDate", tdtGradutionIncrementReleasedDate));
        strB.append(getTOXml("rdoCAIIBPART1IncrementYesNo", rdoCAIIBPART1IncrementYesNo));
        strB.append(getTOXml("tdtCAIIBPART1IncrementReleasedDate", tdtCAIIBPART1IncrementReleasedDate));
        strB.append(getTOXml("rdoCAIIBPART2IncrementYesNo", rdoCAIIBPART2IncrementYesNo));
        strB.append(getTOXml("tdtCAIIBPART2IncrementReleasedDate", tdtCAIIBPART2IncrementReleasedDate));
        strB.append(getTOXml("rdoAnyOtherIncrementYesNo", rdoAnyOtherIncrementYesNo));
        strB.append(getTOXml("txtAnyOtherIncrementInstitutionName", txtAnyOtherIncrementInstitutionName));
        strB.append(getTOXml("tdtAnyOtherIncrementReleasedDate", tdtAnyOtherIncrementReleasedDate));
        strB.append(getTOXml("txtPresentBasic", txtPresentBasic));
        strB.append(getTOXml("tdtLastIncrmentDate", tdtLastIncrmentDate));
        strB.append(getTOXml("txtLossPay_Months", txtLossPay_Months));
        strB.append(getTOXml("txtLossOfpay_Days", txtLossOfpay_Days));
        strB.append(getTOXml("tdtNextIncrmentDate", tdtNextIncrmentDate));
        strB.append(getTOXml("rdoSigNoYesNo", rdoSigNoYesNo));
        strB.append(getTOXml("txtSignatureNo", txtSignatureNo));
        strB.append(getTOXml("cboUnionMember", cboUnionMember));
        strB.append(getTOXml("cboSocietyMember", cboSocietyMember));
        strB.append(getTOXml("clubMembership", clubMembership));
        strB.append(getTOXml("clubName", clubName));
        strB.append(getTOXml("txtProbationPeriod", txtProbationPeriod));
        strB.append(getTOXml("cboProbationPeriod", cboProbationPeriod));
        strB.append(getTOXml("tdtConfirmationDate", tdtConfirmationDate));
        strB.append(getTOXml("tdtDateofRetirement", tdtDateofRetirement));
        strB.append(getTOXml("txtPFNumber", txtPFNumber));
        strB.append(getTOXml("cboPFAcNominee", cboPFAcNominee));
        strB.append(getTOXml("cboPresentBranchId", cboPresentBranchId));
        strB.append(getTOXml("cboReginoalOffice", cboReginoalOffice));
        strB.append(getTOXml("cboZonalOffice", cboZonalOffice));
        strB.append(getTOXml("tdtWorkingSince", tdtWorkingSince));
        strB.append(getTOXml("cboDesignation", cboDesignation));
        strB.append(getTOXml("cboPresentGrade", cboPresentGrade));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property sysId.
     *
     * @return Value of property sysId.
     */
    public java.lang.String getSysId() {
        return sysId;
    }

    /**
     * Setter for property sysId.
     *
     * @param sysId New value of property sysId.
     */
    public void setSysId(java.lang.String sysId) {
        this.sysId = sysId;
    }

    /**
     * Getter for property rdoGradutionIncrementYesNo.
     *
     * @return Value of property rdoGradutionIncrementYesNo.
     */
    public java.lang.String getRdoGradutionIncrementYesNo() {
        return rdoGradutionIncrementYesNo;
    }

    /**
     * Setter for property rdoGradutionIncrementYesNo.
     *
     * @param rdoGradutionIncrementYesNo New value of property
     * rdoGradutionIncrementYesNo.
     */
    public void setRdoGradutionIncrementYesNo(java.lang.String rdoGradutionIncrementYesNo) {
        this.rdoGradutionIncrementYesNo = rdoGradutionIncrementYesNo;
    }

    /**
     * Getter for property tdtGradutionIncrementReleasedDate.
     *
     * @return Value of property tdtGradutionIncrementReleasedDate.
     */
    public java.util.Date getTdtGradutionIncrementReleasedDate() {
        return tdtGradutionIncrementReleasedDate;
    }

    /**
     * Setter for property tdtGradutionIncrementReleasedDate.
     *
     * @param tdtGradutionIncrementReleasedDate New value of property
     * tdtGradutionIncrementReleasedDate.
     */
    public void setTdtGradutionIncrementReleasedDate(java.util.Date tdtGradutionIncrementReleasedDate) {
        this.tdtGradutionIncrementReleasedDate = tdtGradutionIncrementReleasedDate;
    }

    /**
     * Getter for property rdoCAIIBPART1IncrementYesNo.
     *
     * @return Value of property rdoCAIIBPART1IncrementYesNo.
     */
    public java.lang.String getRdoCAIIBPART1IncrementYesNo() {
        return rdoCAIIBPART1IncrementYesNo;
    }

    /**
     * Setter for property rdoCAIIBPART1IncrementYesNo.
     *
     * @param rdoCAIIBPART1IncrementYesNo New value of property
     * rdoCAIIBPART1IncrementYesNo.
     */
    public void setRdoCAIIBPART1IncrementYesNo(java.lang.String rdoCAIIBPART1IncrementYesNo) {
        this.rdoCAIIBPART1IncrementYesNo = rdoCAIIBPART1IncrementYesNo;
    }

    /**
     * Getter for property tdtCAIIBPART1IncrementReleasedDate.
     *
     * @return Value of property tdtCAIIBPART1IncrementReleasedDate.
     */
    public java.util.Date getTdtCAIIBPART1IncrementReleasedDate() {
        return tdtCAIIBPART1IncrementReleasedDate;
    }

    /**
     * Setter for property tdtCAIIBPART1IncrementReleasedDate.
     *
     * @param tdtCAIIBPART1IncrementReleasedDate New value of property
     * tdtCAIIBPART1IncrementReleasedDate.
     */
    public void setTdtCAIIBPART1IncrementReleasedDate(java.util.Date tdtCAIIBPART1IncrementReleasedDate) {
        this.tdtCAIIBPART1IncrementReleasedDate = tdtCAIIBPART1IncrementReleasedDate;
    }

    /**
     * Getter for property rdoCAIIBPART2IncrementYesNo.
     *
     * @return Value of property rdoCAIIBPART2IncrementYesNo.
     */
    public java.lang.String getRdoCAIIBPART2IncrementYesNo() {
        return rdoCAIIBPART2IncrementYesNo;
    }

    /**
     * Setter for property rdoCAIIBPART2IncrementYesNo.
     *
     * @param rdoCAIIBPART2IncrementYesNo New value of property
     * rdoCAIIBPART2IncrementYesNo.
     */
    public void setRdoCAIIBPART2IncrementYesNo(java.lang.String rdoCAIIBPART2IncrementYesNo) {
        this.rdoCAIIBPART2IncrementYesNo = rdoCAIIBPART2IncrementYesNo;
    }

    /**
     * Getter for property tdtCAIIBPART2IncrementReleasedDate.
     *
     * @return Value of property tdtCAIIBPART2IncrementReleasedDate.
     */
    public java.util.Date getTdtCAIIBPART2IncrementReleasedDate() {
        return tdtCAIIBPART2IncrementReleasedDate;
    }

    /**
     * Setter for property tdtCAIIBPART2IncrementReleasedDate.
     *
     * @param tdtCAIIBPART2IncrementReleasedDate New value of property
     * tdtCAIIBPART2IncrementReleasedDate.
     */
    public void setTdtCAIIBPART2IncrementReleasedDate(java.util.Date tdtCAIIBPART2IncrementReleasedDate) {
        this.tdtCAIIBPART2IncrementReleasedDate = tdtCAIIBPART2IncrementReleasedDate;
    }

    /**
     * Getter for property rdoAnyOtherIncrementYesNo.
     *
     * @return Value of property rdoAnyOtherIncrementYesNo.
     */
    public java.lang.String getRdoAnyOtherIncrementYesNo() {
        return rdoAnyOtherIncrementYesNo;
    }

    /**
     * Setter for property rdoAnyOtherIncrementYesNo.
     *
     * @param rdoAnyOtherIncrementYesNo New value of property
     * rdoAnyOtherIncrementYesNo.
     */
    public void setRdoAnyOtherIncrementYesNo(java.lang.String rdoAnyOtherIncrementYesNo) {
        this.rdoAnyOtherIncrementYesNo = rdoAnyOtherIncrementYesNo;
    }

    /**
     * Getter for property txtAnyOtherIncrementInstitutionName.
     *
     * @return Value of property txtAnyOtherIncrementInstitutionName.
     */
    public java.lang.String getTxtAnyOtherIncrementInstitutionName() {
        return txtAnyOtherIncrementInstitutionName;
    }

    /**
     * Setter for property txtAnyOtherIncrementInstitutionName.
     *
     * @param txtAnyOtherIncrementInstitutionName New value of property
     * txtAnyOtherIncrementInstitutionName.
     */
    public void setTxtAnyOtherIncrementInstitutionName(java.lang.String txtAnyOtherIncrementInstitutionName) {
        this.txtAnyOtherIncrementInstitutionName = txtAnyOtherIncrementInstitutionName;
    }

    /**
     * Getter for property tdtAnyOtherIncrementReleasedDate.
     *
     * @return Value of property tdtAnyOtherIncrementReleasedDate.
     */
    public java.util.Date getTdtAnyOtherIncrementReleasedDate() {
        return tdtAnyOtherIncrementReleasedDate;
    }

    /**
     * Setter for property tdtAnyOtherIncrementReleasedDate.
     *
     * @param tdtAnyOtherIncrementReleasedDate New value of property
     * tdtAnyOtherIncrementReleasedDate.
     */
    public void setTdtAnyOtherIncrementReleasedDate(java.util.Date tdtAnyOtherIncrementReleasedDate) {
        this.tdtAnyOtherIncrementReleasedDate = tdtAnyOtherIncrementReleasedDate;
    }

    /**
     * Getter for property txtPresentBasic.
     *
     * @return Value of property txtPresentBasic.
     */
    public java.lang.String getTxtPresentBasic() {
        return txtPresentBasic;
    }

    /**
     * Setter for property txtPresentBasic.
     *
     * @param txtPresentBasic New value of property txtPresentBasic.
     */
    public void setTxtPresentBasic(java.lang.String txtPresentBasic) {
        this.txtPresentBasic = txtPresentBasic;
    }

    /**
     * Getter for property txtLossPay_Months.
     *
     * @return Value of property txtLossPay_Months.
     */
    public java.lang.String getTxtLossPay_Months() {
        return txtLossPay_Months;
    }

    /**
     * Setter for property txtLossPay_Months.
     *
     * @param txtLossPay_Months New value of property txtLossPay_Months.
     */
    public void setTxtLossPay_Months(java.lang.String txtLossPay_Months) {
        this.txtLossPay_Months = txtLossPay_Months;
    }

    /**
     * Getter for property txtLossOfpay_Days.
     *
     * @return Value of property txtLossOfpay_Days.
     */
    public java.lang.String getTxtLossOfpay_Days() {
        return txtLossOfpay_Days;
    }

    /**
     * Setter for property txtLossOfpay_Days.
     *
     * @param txtLossOfpay_Days New value of property txtLossOfpay_Days.
     */
    public void setTxtLossOfpay_Days(java.lang.String txtLossOfpay_Days) {
        this.txtLossOfpay_Days = txtLossOfpay_Days;
    }

    /**
     * Getter for property rdoSigNoYesNo.
     *
     * @return Value of property rdoSigNoYesNo.
     */
    public java.lang.String getRdoSigNoYesNo() {
        return rdoSigNoYesNo;
    }

    /**
     * Setter for property rdoSigNoYesNo.
     *
     * @param rdoSigNoYesNo New value of property rdoSigNoYesNo.
     */
    public void setRdoSigNoYesNo(java.lang.String rdoSigNoYesNo) {
        this.rdoSigNoYesNo = rdoSigNoYesNo;
    }

    /**
     * Getter for property txtSignatureNo.
     *
     * @return Value of property txtSignatureNo.
     */
    public java.lang.String getTxtSignatureNo() {
        return txtSignatureNo;
    }

    /**
     * Setter for property txtSignatureNo.
     *
     * @param txtSignatureNo New value of property txtSignatureNo.
     */
    public void setTxtSignatureNo(java.lang.String txtSignatureNo) {
        this.txtSignatureNo = txtSignatureNo;
    }

    /**
     * Getter for property cboUnionMember.
     *
     * @return Value of property cboUnionMember.
     */
    public java.lang.String getCboUnionMember() {
        return cboUnionMember;
    }

    /**
     * Setter for property cboUnionMember.
     *
     * @param cboUnionMember New value of property cboUnionMember.
     */
    public void setCboUnionMember(java.lang.String cboUnionMember) {
        this.cboUnionMember = cboUnionMember;
    }

    /**
     * Getter for property cboSocietyMember.
     *
     * @return Value of property cboSocietyMember.
     */
    public java.lang.String getCboSocietyMember() {
        return cboSocietyMember;
    }

    /**
     * Setter for property cboSocietyMember.
     *
     * @param cboSocietyMember New value of property cboSocietyMember.
     */
    public void setCboSocietyMember(java.lang.String cboSocietyMember) {
        this.cboSocietyMember = cboSocietyMember;
    }

    /**
     * Getter for property societyMemberNo.
     *
     * @return Value of property societyMemberNo.
     */
    public java.lang.String getSocietyMemberNo() {
        return societyMemberNo;
    }

    /**
     * Setter for property societyMemberNo.
     *
     * @param societyMemberNo New value of property societyMemberNo.
     */
    public void setSocietyMemberNo(java.lang.String societyMemberNo) {
        this.societyMemberNo = societyMemberNo;
    }

    /**
     * Getter for property clubMembership.
     *
     * @return Value of property clubMembership.
     */
    public java.lang.String getClubMembership() {
        return clubMembership;
    }

    /**
     * Setter for property clubMembership.
     *
     * @param clubMembership New value of property clubMembership.
     */
    public void setClubMembership(java.lang.String clubMembership) {
        this.clubMembership = clubMembership;
    }

    /**
     * Getter for property clubName.
     *
     * @return Value of property clubName.
     */
    public java.lang.String getClubName() {
        return clubName;
    }

    /**
     * Setter for property clubName.
     *
     * @param clubName New value of property clubName.
     */
    public void setClubName(java.lang.String clubName) {
        this.clubName = clubName;
    }

    /**
     * Getter for property txtProbationPeriod.
     *
     * @return Value of property txtProbationPeriod.
     */
    public java.lang.String getTxtProbationPeriod() {
        return txtProbationPeriod;
    }

    /**
     * Setter for property txtProbationPeriod.
     *
     * @param txtProbationPeriod New value of property txtProbationPeriod.
     */
    public void setTxtProbationPeriod(java.lang.String txtProbationPeriod) {
        this.txtProbationPeriod = txtProbationPeriod;
    }

    /**
     * Getter for property cboProbationPeriod.
     *
     * @return Value of property cboProbationPeriod.
     */
    public java.lang.String getCboProbationPeriod() {
        return cboProbationPeriod;
    }

    /**
     * Setter for property cboProbationPeriod.
     *
     * @param cboProbationPeriod New value of property cboProbationPeriod.
     */
    public void setCboProbationPeriod(java.lang.String cboProbationPeriod) {
        this.cboProbationPeriod = cboProbationPeriod;
    }

    /**
     * Getter for property tdtDateofRetirement.
     *
     * @return Value of property tdtDateofRetirement.
     */
    public java.lang.String getTdtDateofRetirement() {
        return tdtDateofRetirement;
    }

    /**
     * Setter for property tdtDateofRetirement.
     *
     * @param tdtDateofRetirement New value of property tdtDateofRetirement.
     */
    public void setTdtDateofRetirement(java.lang.String tdtDateofRetirement) {
        this.tdtDateofRetirement = tdtDateofRetirement;
    }

    /**
     * Getter for property txtPFNumber.
     *
     * @return Value of property txtPFNumber.
     */
    public java.lang.String getTxtPFNumber() {
        return txtPFNumber;
    }

    /**
     * Setter for property txtPFNumber.
     *
     * @param txtPFNumber New value of property txtPFNumber.
     */
    public void setTxtPFNumber(java.lang.String txtPFNumber) {
        this.txtPFNumber = txtPFNumber;
    }

    /**
     * Getter for property cboPFAcNominee.
     *
     * @return Value of property cboPFAcNominee.
     */
    public java.lang.String getCboPFAcNominee() {
        return cboPFAcNominee;
    }

    /**
     * Setter for property cboPFAcNominee.
     *
     * @param cboPFAcNominee New value of property cboPFAcNominee.
     */
    public void setCboPFAcNominee(java.lang.String cboPFAcNominee) {
        this.cboPFAcNominee = cboPFAcNominee;
    }

    /**
     * Getter for property cboPresentBranchId.
     *
     * @return Value of property cboPresentBranchId.
     */
    public java.lang.String getCboPresentBranchId() {
        return cboPresentBranchId;
    }

    /**
     * Setter for property cboPresentBranchId.
     *
     * @param cboPresentBranchId New value of property cboPresentBranchId.
     */
    public void setCboPresentBranchId(java.lang.String cboPresentBranchId) {
        this.cboPresentBranchId = cboPresentBranchId;
    }

    /**
     * Getter for property cboReginoalOffice.
     *
     * @return Value of property cboReginoalOffice.
     */
    public java.lang.String getCboReginoalOffice() {
        return cboReginoalOffice;
    }

    /**
     * Setter for property cboReginoalOffice.
     *
     * @param cboReginoalOffice New value of property cboReginoalOffice.
     */
    public void setCboReginoalOffice(java.lang.String cboReginoalOffice) {
        this.cboReginoalOffice = cboReginoalOffice;
    }

    /**
     * Getter for property cboZonalOffice.
     *
     * @return Value of property cboZonalOffice.
     */
    public java.lang.String getCboZonalOffice() {
        return cboZonalOffice;
    }

    /**
     * Setter for property cboZonalOffice.
     *
     * @param cboZonalOffice New value of property cboZonalOffice.
     */
    public void setCboZonalOffice(java.lang.String cboZonalOffice) {
        this.cboZonalOffice = cboZonalOffice;
    }

    /**
     * Getter for property cboDesignation.
     *
     * @return Value of property cboDesignation.
     */
    public java.lang.String getCboDesignation() {
        return cboDesignation;
    }

    /**
     * Setter for property cboDesignation.
     *
     * @param cboDesignation New value of property cboDesignation.
     */
    public void setCboDesignation(java.lang.String cboDesignation) {
        this.cboDesignation = cboDesignation;
    }

    /**
     * Getter for property cboPresentGrade.
     *
     * @return Value of property cboPresentGrade.
     */
    public java.lang.String getCboPresentGrade() {
        return cboPresentGrade;
    }

    /**
     * Setter for property cboPresentGrade.
     *
     * @param cboPresentGrade New value of property cboPresentGrade.
     */
    public void setCboPresentGrade(java.lang.String cboPresentGrade) {
        this.cboPresentGrade = cboPresentGrade;
    }

    /**
     * Getter for property tdtLastIncrmentDate.
     *
     * @return Value of property tdtLastIncrmentDate.
     */
    public java.util.Date getTdtLastIncrmentDate() {
        return tdtLastIncrmentDate;
    }

    /**
     * Setter for property tdtLastIncrmentDate.
     *
     * @param tdtLastIncrmentDate New value of property tdtLastIncrmentDate.
     */
    public void setTdtLastIncrmentDate(java.util.Date tdtLastIncrmentDate) {
        this.tdtLastIncrmentDate = tdtLastIncrmentDate;
    }

    /**
     * Getter for property tdtNextIncrmentDate.
     *
     * @return Value of property tdtNextIncrmentDate.
     */
    public java.util.Date getTdtNextIncrmentDate() {
        return tdtNextIncrmentDate;
    }

    /**
     * Setter for property tdtNextIncrmentDate.
     *
     * @param tdtNextIncrmentDate New value of property tdtNextIncrmentDate.
     */
    public void setTdtNextIncrmentDate(java.util.Date tdtNextIncrmentDate) {
        this.tdtNextIncrmentDate = tdtNextIncrmentDate;
    }

    /**
     * Getter for property tdtConfirmationDate.
     *
     * @return Value of property tdtConfirmationDate.
     */
    public java.util.Date getTdtConfirmationDate() {
        return tdtConfirmationDate;
    }

    /**
     * Setter for property tdtConfirmationDate.
     *
     * @param tdtConfirmationDate New value of property tdtConfirmationDate.
     */
    public void setTdtConfirmationDate(java.util.Date tdtConfirmationDate) {
        this.tdtConfirmationDate = tdtConfirmationDate;
    }

    /**
     * Getter for property tdtWorkingSince.
     *
     * @return Value of property tdtWorkingSince.
     */
    public java.util.Date getTdtWorkingSince() {
        return tdtWorkingSince;
    }

    /**
     * Setter for property tdtWorkingSince.
     *
     * @param tdtWorkingSince New value of property tdtWorkingSince.
     */
    public void setTdtWorkingSince(java.util.Date tdtWorkingSince) {
        this.tdtWorkingSince = tdtWorkingSince;
    }
    /**
     * Getter for property directorTittle.
     *
     * @return Value of property directorTittle.
     */
}