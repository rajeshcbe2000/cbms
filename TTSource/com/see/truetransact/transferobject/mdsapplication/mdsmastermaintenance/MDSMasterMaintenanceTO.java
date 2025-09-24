/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSMasterMaintenanceTO.java
 * 
 * Created on Fri Jun 17 13:19:56 IST 2011
 */
package com.see.truetransact.transferobject.mdsapplication.mdsmastermaintenance;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is MDS_MASTER_MAINTENANCE.
 */
public class MDSMasterMaintenanceTO extends TransferObject implements Serializable {

    private Integer divisionNo = 0;  //AJITH
    private Double prizedAmount = 0.0; //AJITH
    private Double totalAmount = 0.0; //AJITH
    private Integer installmentDue = 0;   //AJITH
    private Double instalOverdueAmt = 0.0; //AJITH
    private Integer pin = null; //AJITH
    private String contactNo = null;
    private Double totalSalary = 0.0; //AJITH
    private Integer subNo = 0;    //AJITH
    private String schemeName = "";
    private String chittalNo = "";
    private Date chitStartDt = null;
    private String memberNo = "";
    private String memberName = "";
    private String memberType = "";
    private String nomineeName = "";
    private Date payDt = null;
    private String resolutionNo = "";
    private Date resolutionDt = null;
    private String bondNo = "";
    private Date bondDt = null;
    private String lastInstallmentNo = "";
    private Date lastInstallmentDt = null;
    private String instalRemarks = "";
    private String salaryCerficateNo = "";
    private String empName = "";
    private String empAddress = "";
    private String city = "";
    private String designation = "";
    private Date retirementDt = null;
    private String empMemberNo = "";
    private String networth = "";
    private String salaryRemarks = "";
    private String securityType = "";
    private String securityValue = "";
    private String securityRemarks = "";
    private String jewellaryDetails = "";
    private Double grossWeight = 0.0;   //AJITH
    private Double netWeight = 0.0;    //AJITH
    private String goldValue = "";
    private String goldRemarks = "";
    private String status = "";
    private String branchCode = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null; //AJITH
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String standingIns = "";
    private String nominee = "";
    private String salaryRecovery = "";
    private String lockStatus = "";
    private String slno = "";
    private String onlyApplication = "";
    private String bondSet = "";
    private String applicationSecurityRemarks = "";
    private Date chitCloseDt = null;
    private String memberGDSNo = "";

    public String getApplicationSecurityRemarks() {
        return applicationSecurityRemarks;
    }

    public void setApplicationSecurityRemarks(String applicationSecurityRemarks) {
        this.applicationSecurityRemarks = applicationSecurityRemarks;
    }

    public String getApplicationSet() {
        return applicationSet;
    }

    public void setApplicationSet(String applicationSet) {
        this.applicationSet = applicationSet;
    }
    private String applicationSet = "";

    public String getOnlyApplication() {
        return onlyApplication;
    }

    public void setOnlyApplication(String onlyApplication) {
        this.onlyApplication = onlyApplication;
    }

    public String getDefaulter() {
        return defaulter;
    }

    public void setDefaulter(String defaulter) {
        this.defaulter = defaulter;
    }
    private String applNo = "";
    private Date applDate = null;
    private String defaulter = "";
//private String bondSet="";

    public String getApplNo() {
        return applNo;
    }

    public void setApplNo(String applNo) {
        this.applNo = applNo;
    }

    public Date getApplDate() {
        return applDate;
    }

    public void setApplDate(Date applDate) {
        this.applDate = applDate;
    }

    public String getBondSet() {
        return bondSet;
    }

    public void setBondSet(String bondSet) {
        this.bondSet = bondSet;
    }

    public String getSlno() {
        return slno;
    }

    public void setSlno(String slno) {
        this.slno = slno;
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
     * Setter/Getter for CHITTAL_NO - table Field
     */
    public void setChittalNo(String chittalNo) {
        this.chittalNo = chittalNo;
    }

    public String getChittalNo() {
        return chittalNo;
    }

    /**
     * Setter/Getter for CHIT_START_DT - table Field
     */
    public void setChitStartDt(Date chitStartDt) {
        this.chitStartDt = chitStartDt;
    }

    public Date getChitStartDt() {
        return chitStartDt;
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
     * Setter/Getter for MEMBER_TYPE - table Field
     */
    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getMemberType() {
        return memberType;
    }

    /**
     * Setter/Getter for NOMINEE_NAME - table Field
     */
    public void setNomineeName(String nomineeName) {
        this.nomineeName = nomineeName;
    }

    public String getNomineeName() {
        return nomineeName;
    }

    /**
     * Setter/Getter for PAY_DT - table Field
     */
    public void setPayDt(Date payDt) {
        this.payDt = payDt;
    }

    public Date getPayDt() {
        return payDt;
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
     * Setter/Getter for BOND_NO - table Field
     */
    public void setBondNo(String bondNo) {
        this.bondNo = bondNo;
    }

    public String getBondNo() {
        return bondNo;
    }

    /**
     * Setter/Getter for BOND_DT - table Field
     */
    public void setBondDt(Date bondDt) {
        this.bondDt = bondDt;
    }

    public Date getBondDt() {
        return bondDt;
    }

    /**
     * Setter/Getter for LAST_INSTALLMENT_NO - table Field
     */
    public void setLastInstallmentNo(String lastInstallmentNo) {
        this.lastInstallmentNo = lastInstallmentNo;
    }

    public String getLastInstallmentNo() {
        return lastInstallmentNo;
    }

    /**
     * Setter/Getter for LAST_INSTALLMENT_DT - table Field
     */
    public void setLastInstallmentDt(Date lastInstallmentDt) {
        this.lastInstallmentDt = lastInstallmentDt;
    }

    public Date getLastInstallmentDt() {
        return lastInstallmentDt;
    }

    /**
     * Setter/Getter for INSTAL_REMARKS - table Field
     */
    public void setInstalRemarks(String instalRemarks) {
        this.instalRemarks = instalRemarks;
    }

    public String getInstalRemarks() {
        return instalRemarks;
    }

    /**
     * Setter/Getter for SALARY_CERFICATE_NO - table Field
     */
    public void setSalaryCerficateNo(String salaryCerficateNo) {
        this.salaryCerficateNo = salaryCerficateNo;
    }

    public String getSalaryCerficateNo() {
        return salaryCerficateNo;
    }

    /**
     * Setter/Getter for EMP_NAME - table Field
     */
    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpName() {
        return empName;
    }

    /**
     * Setter/Getter for EMP_ADDRESS - table Field
     */
    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    /**
     * Setter/Getter for CITY - table Field
     */
    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    /**
     * Setter/Getter for DESIGNATION - table Field
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignation() {
        return designation;
    }

    /**
     * Setter/Getter for RETIREMENT_DT - table Field
     */
    public void setRetirementDt(Date retirementDt) {
        this.retirementDt = retirementDt;
    }

    public Date getRetirementDt() {
        return retirementDt;
    }

    /**
     * Setter/Getter for EMP_MEMBER_NO - table Field
     */
    public void setEmpMemberNo(String empMemberNo) {
        this.empMemberNo = empMemberNo;
    }

    public String getEmpMemberNo() {
        return empMemberNo;
    }

    /**
     * Setter/Getter for NETWORTH - table Field
     */
    public void setNetworth(String networth) {
        this.networth = networth;
    }

    public String getNetworth() {
        return networth;
    }

    /**
     * Setter/Getter for SALARY_REMARKS - table Field
     */
    public void setSalaryRemarks(String salaryRemarks) {
        this.salaryRemarks = salaryRemarks;
    }

    public String getSalaryRemarks() {
        return salaryRemarks;
    }

    /**
     * Setter/Getter for SECURITY_TYPE - table Field
     */
    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public String getSecurityType() {
        return securityType;
    }

    /**
     * Setter/Getter for SECURITY_VALUE - table Field
     */
    public void setSecurityValue(String securityValue) {
        this.securityValue = securityValue;
    }

    public String getSecurityValue() {
        return securityValue;
    }

    /**
     * Setter/Getter for SECURITY_REMARKS - table Field
     */
    public void setSecurityRemarks(String securityRemarks) {
        this.securityRemarks = securityRemarks;
    }

    public String getSecurityRemarks() {
        return securityRemarks;
    }

    /**
     * Setter/Getter for JEWELLARY_DETAILS - table Field
     */
    public void setJewellaryDetails(String jewellaryDetails) {
        this.jewellaryDetails = jewellaryDetails;
    }

    public String getJewellaryDetails() {
        return jewellaryDetails;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

    /**
     * Setter/Getter for GOLD_VALUE - table Field
     */
    public void setGoldValue(String goldValue) {
        this.goldValue = goldValue;
    }

    public String getGoldValue() {
        return goldValue;
    }

    /**
     * Setter/Getter for GOLD_REMARKS - table Field
     */
    public void setGoldRemarks(String goldRemarks) {
        this.goldRemarks = goldRemarks;
    }

    public String getGoldRemarks() {
        return goldRemarks;
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
     * Setter/Getter for BRANCH_CODE - table Field
     */
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
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
        strB.append(getTOString("schemeName", schemeName));
        strB.append(getTOString("chittalNo", chittalNo));
        strB.append(getTOString("divisionNo", divisionNo));
        strB.append(getTOString("chitStartDt", chitStartDt));
        strB.append(getTOString("memberNo", memberNo));
        strB.append(getTOString("memberName", memberName));
        strB.append(getTOString("memberType", memberType));
        strB.append(getTOString("nomineeName", nomineeName));
        strB.append(getTOString("salaryRecovery", salaryRecovery));
        strB.append(getTOString("lockStatus", lockStatus));
        strB.append(getTOString("payDt", payDt));
        strB.append(getTOString("prizedAmount", prizedAmount));
        strB.append(getTOString("resolutionNo", resolutionNo));
        strB.append(getTOString("resolutionDt", resolutionDt));
        strB.append(getTOString("bondNo", bondNo));
        strB.append(getTOString("bondDt", bondDt));
        strB.append(getTOString("lastInstallmentNo", lastInstallmentNo));
        strB.append(getTOString("lastInstallmentDt", lastInstallmentDt));
        strB.append(getTOString("totalAmount", totalAmount));
        strB.append(getTOString("installmentDue", installmentDue));
        strB.append(getTOString("instalOverdueAmt", instalOverdueAmt));
        strB.append(getTOString("instalRemarks", instalRemarks));
        strB.append(getTOString("salaryCerficateNo", salaryCerficateNo));
        strB.append(getTOString("empName", empName));
        strB.append(getTOString("empAddress", empAddress));
        strB.append(getTOString("city", city));
        strB.append(getTOString("pin", pin));
        strB.append(getTOString("designation", designation));
        strB.append(getTOString("contactNo", contactNo));
        strB.append(getTOString("retirementDt", retirementDt));
        strB.append(getTOString("empMemberNo", empMemberNo));
        strB.append(getTOString("totalSalary", totalSalary));
        strB.append(getTOString("subNo", subNo));
        strB.append(getTOString("networth", networth));
        strB.append(getTOString("salaryRemarks", salaryRemarks));
        strB.append(getTOString("securityType", securityType));
        strB.append(getTOString("securityValue", securityValue));
        strB.append(getTOString("securityRemarks", securityRemarks));
        strB.append(getTOString("jewellaryDetails", jewellaryDetails));
        strB.append(getTOString("grossWeight", grossWeight));
        strB.append(getTOString("netWeight", netWeight));
        strB.append(getTOString("goldValue", goldValue));
        strB.append(getTOString("goldRemarks", goldRemarks));
        strB.append(getTOString("standingIns", standingIns));
        strB.append(getTOString("nominee", nominee));
        strB.append(getTOString("status", status));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("chitCloseDt", chitCloseDt));
        strB.append(getTOString("memberGDSNo", memberGDSNo));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("schemeName", schemeName));
        strB.append(getTOXml("chittalNo", chittalNo));
        strB.append(getTOXml("divisionNo", divisionNo));
        strB.append(getTOXml("chitStartDt", chitStartDt));
        strB.append(getTOXml("memberNo", memberNo));
        strB.append(getTOXml("memberName", memberName));
        strB.append(getTOXml("memberType", memberType));
        strB.append(getTOXml("nomineeName", nomineeName));
        strB.append(getTOXml("salaryRecovery", salaryRecovery));
        strB.append(getTOXml("lockStatus", lockStatus));
        strB.append(getTOXml("payDt", payDt));
        strB.append(getTOXml("prizedAmount", prizedAmount));
        strB.append(getTOXml("resolutionNo", resolutionNo));
        strB.append(getTOXml("resolutionDt", resolutionDt));
        strB.append(getTOXml("bondNo", bondNo));
        strB.append(getTOXml("bondDt", bondDt));
        strB.append(getTOXml("lastInstallmentNo", lastInstallmentNo));
        strB.append(getTOXml("lastInstallmentDt", lastInstallmentDt));
        strB.append(getTOXml("totalAmount", totalAmount));
        strB.append(getTOXml("installmentDue", installmentDue));
        strB.append(getTOXml("instalOverdueAmt", instalOverdueAmt));
        strB.append(getTOXml("instalRemarks", instalRemarks));
        strB.append(getTOXml("salaryCerficateNo", salaryCerficateNo));
        strB.append(getTOXml("empName", empName));
        strB.append(getTOXml("empAddress", empAddress));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("pin", pin));
        strB.append(getTOXml("designation", designation));
        strB.append(getTOXml("contactNo", contactNo));
        strB.append(getTOXml("retirementDt", retirementDt));
        strB.append(getTOXml("empMemberNo", empMemberNo));
        strB.append(getTOXml("totalSalary", totalSalary));
        strB.append(getTOXml("subNo", subNo));
        strB.append(getTOXml("networth", networth));
        strB.append(getTOXml("salaryRemarks", salaryRemarks));
        strB.append(getTOXml("securityType", securityType));
        strB.append(getTOXml("securityValue", securityValue));
        strB.append(getTOXml("securityRemarks", securityRemarks));
        strB.append(getTOXml("jewellaryDetails", jewellaryDetails));
        strB.append(getTOXml("grossWeight", grossWeight));
        strB.append(getTOXml("netWeight", netWeight));
        strB.append(getTOXml("goldValue", goldValue));
        strB.append(getTOXml("goldRemarks", goldRemarks));
        strB.append(getTOXml("standingIns", standingIns));
        strB.append(getTOXml("nominee", nominee));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("chitCloseDt", chitCloseDt));
        strB.append(getTOXml("memberGDSNo", memberGDSNo));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public Integer getDivisionNo() {
        return divisionNo;
    }

    public void setDivisionNo(Integer divisionNo) {
        this.divisionNo = divisionNo;
    }

    public Double getPrizedAmount() {
        return prizedAmount;
    }

    public void setPrizedAmount(Double prizedAmount) {
        this.prizedAmount = prizedAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getInstallmentDue() {
        return installmentDue;
    }

    public void setInstallmentDue(Integer installmentDue) {
        this.installmentDue = installmentDue;
    }

    public Double getInstalOverdueAmt() {
        return instalOverdueAmt;
    }

    public void setInstalOverdueAmt(Double instalOverdueAmt) {
        this.instalOverdueAmt = instalOverdueAmt;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    /**
     * Getter for property contactNo.
     *
     * @return Value of property contactNo.
     */
    public java.lang.String getContactNo() {
        return contactNo;
    }

    /**
     * Setter for property contactNo.
     *
     * @param contactNo New value of property contactNo.
     */
    public void setContactNo(java.lang.String contactNo) {
        this.contactNo = contactNo;
    }

    public Double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(Double totalSalary) {
        this.totalSalary = totalSalary;
    }

    /**
     * Getter for property nominee.
     *
     * @return Value of property nominee.
     */
    public java.lang.String getNominee() {
        return nominee;
    }

    /**
     * Setter for property nominee.
     *
     * @param nominee New value of property nominee.
     */
    public void setNominee(java.lang.String nominee) {
        this.nominee = nominee;
    }

    /**
     * Getter for property standingIns.
     *
     * @return Value of property standingIns.
     */
    public java.lang.String getStandingIns() {
        return standingIns;
    }

    /**
     * Setter for property standingIns.
     *
     * @param standingIns New value of property standingIns.
     */
    public void setStandingIns(java.lang.String standingIns) {
        this.standingIns = standingIns;
    }

    public Integer getSubNo() {
        return subNo;
    }

    public void setSubNo(Integer subNo) {
        this.subNo = subNo;
    }

    /**
     * Getter for property salaryRecovery.
     *
     * @return Value of property salaryRecovery.
     */
    public java.lang.String getSalaryRecovery() {
        return salaryRecovery;
    }

    /**
     * Setter for property salaryRecovery.
     *
     * @param salaryRecovery New value of property salaryRecovery.
     */
    public void setSalaryRecovery(java.lang.String salaryRecovery) {
        this.salaryRecovery = salaryRecovery;
    }

    /**
     * Getter for property lockStatus.
     *
     * @return Value of property lockStatus.
     */
    public java.lang.String getLockStatus() {
        return lockStatus;
    }

    /**
     * Setter for property lockStatus.
     *
     * @param lockStatus New value of property lockStatus.
     */
    public void setLockStatus(java.lang.String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Date getChitCloseDt() {
        return chitCloseDt;
    }

    public void setChitCloseDt(Date chitCloseDt) {
        this.chitCloseDt = chitCloseDt;
    }

    public String getMemberGDSNo() {
        return memberGDSNo;
    }

    public void setMemberGDSNo(String memberGDSNo) {
        this.memberGDSNo = memberGDSNo;
    }
    
    
    
}