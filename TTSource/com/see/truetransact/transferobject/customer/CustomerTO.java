/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 *  CustomerTO.java
 * 
 * Created on Wed Jul 27 16:13:11 GMT+05:30 2005
 */
package com.see.truetransact.transferobject.customer;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is CUSTOMER.
 */
public class CustomerTO extends TransferObject implements Serializable {

    private String custId = "";
    private String branchCode = "";
    private String lname = "";
    private String fname = "";
    private String mname = "";
    private Date dob = null;
    private String mobileAppLoginStatus = ""; 
    private Double networth = 0.0;
    private Date createddt = null;
    private Date deleteddt = null;
    private String gender = "";
    private String profession = "";
    private String minor = "";
    private String annincome = "";
    private String education = "";
    private String vehicle = "";
    private String remarks = "";
    private String compName = "";
    private String commAddrType = "";
    private String emailId = "";
    private String title = "";
    private String residentialstatus = "";
    private String nationality = "";
    private String language = "";
    private String customergroup = "";
    private String relationmanager = "";
    private String primaryOccupation = "";
    private String preferredComm = "";
    private String status = "";
    private String maritalstatus = "";
    private String custTypeId = "";
    private String ssn = "";
    private String custUserid = "";
    private String custPwd = "";
//	private byte[] photoFile;
//	private byte[] signatureFile;
    private String custType = "";
    private String website = "";
    private String authorizeCustId = "";
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String authorizeStatus = null;
    private String transPwd = "";
    private String membershipClass = "";
    private String careOf = "";
    private String careOfName = "";
    private String customerStatus = "";
    private Date crAvailedSince = null;
    private String riskRating = "";
    private String introType = "";
    private String addrVerified = "";
    private String phoneVerified = "";
    private String obtainFinstat = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String createdBy = "";
    private String sendThanksLetter = "";
    private String confirmThanks = "";
    private String staffId = "";
    private String designation = "";
    private Date networthAsOn = null;
    private String initiatedBranch = "";
    private String caste = "";
    private String cboReligion = "";
    private String panNumber = "";
//        private String TinNumber = "";
    private String deleteRemarks = "";
    private String addrProof = "";
    private String idenProof = "";
    private String kartha = "";
    private String farClass = "";
    private Integer age = 0;
    private String bankruptcy = "";
    private String membershipNum = "";
    private String itDec = "";
    private String wardNo;
    private Date joiningDate = null;
    private String amsam = "";
    private String desam = "";
    private String subCaste="";
    private String minority="";
    private String division="";
    private Integer retAge=0;
    private Date retDt = null;
    private String agentCustId = "";

    public String getAgentCustId() {
        return agentCustId;
    }

    public void setAgentCustId(String agentCustId) {
        this.agentCustId = agentCustId;
    }

    public String getMobileAppLoginStatus() {
        return mobileAppLoginStatus;
    }

    public void setMobileAppLoginStatus(String mobileAppLoginStatus) {
        this.mobileAppLoginStatus = mobileAppLoginStatus;
    }

    public Date getRetDt() {
        return retDt;
    }

    public void setRetDt(Date retDt) {
        this.retDt = retDt;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
    
    public String getMinority() {
        return minority;
    }

    public void setMinority(String minority) {
        this.minority = minority;
    }
    
    

    public String getSubCaste() {
        return subCaste;
    }

    public void setSubCaste(String subCaste) {
        this.subCaste = subCaste;
    }
    
    
    /**
     * Setter/Getter for CUST_ID - table Field
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustId() {
        return custId;
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
     * Setter/Getter for LNAME - table Field
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLname() {
        return lname;
    }

    /**
     * Setter/Getter for FNAME - table Field
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {
        return fname;
    }

    /**
     * Setter/Getter for MNAME - table Field
     */
    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMname() {
        return mname;
    }

    /**
     * Setter/Getter for DOB - table Field
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getDob() {
        return dob;
    }

    /**
     * Setter/Getter for NETWORTH - table Field
     */
    public void setNetworth(Double networth) {
        this.networth = networth;
    }

    public Double getNetworth() {
        return networth;
    }

    /**
     * Setter/Getter for CREATEDDT - table Field
     */
    public void setCreateddt(Date createddt) {
        this.createddt = createddt;
    }

    public Date getCreateddt() {
        return createddt;
    }

    /**
     * Setter/Getter for DELETEDDT - table Field
     */
    public void setDeleteddt(Date deleteddt) {
        this.deleteddt = deleteddt;
    }

    public Date getDeleteddt() {
        return deleteddt;
    }

    /**
     * Setter/Getter for GENDER - table Field
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    /**
     * Setter/Getter for PROFESSION - table Field
     */
    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfession() {
        return profession;
    }

    /**
     * Setter/Getter for MINOR - table Field
     */
    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getMinor() {
        return minor;
    }

    /**
     * Setter/Getter for ANNINCOME - table Field
     */
    public void setAnnincome(String annincome) {
        this.annincome = annincome;
    }

    public String getAnnincome() {
        return annincome;
    }

    /**
     * Setter/Getter for EDUCATION - table Field
     */
    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducation() {
        return education;
    }

    /**
     * Setter/Getter for VEHICLE - table Field
     */
    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getVehicle() {
        return vehicle;
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
     * Setter/Getter for COMP_NAME - table Field
     */
    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompName() {
        return compName;
    }

    /**
     * Setter/Getter for COMM_ADDR_TYPE - table Field
     */
    public void setCommAddrType(String commAddrType) {
        this.commAddrType = commAddrType;
    }

    public String getCommAddrType() {
        return commAddrType;
    }

    /**
     * Setter/Getter for EMAIL_ID - table Field
     */
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    /**
     * Setter/Getter for TITLE - table Field
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Setter/Getter for RESIDENTIALSTATUS - table Field
     */
    public void setResidentialstatus(String residentialstatus) {
        this.residentialstatus = residentialstatus;
    }

    public String getResidentialstatus() {
        return residentialstatus;
    }

    /**
     * Setter/Getter for NATIONALITY - table Field
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationality() {
        return nationality;
    }

    /**
     * Setter/Getter for LANGUAGE - table Field
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    /**
     * Setter/Getter for CUSTOMERGROUP - table Field
     */
    public void setCustomergroup(String customergroup) {
        this.customergroup = customergroup;
    }

    public String getCustomergroup() {
        return customergroup;
    }

    /**
     * Setter/Getter for RELATIONMANAGER - table Field
     */
    public void setRelationmanager(String relationmanager) {
        this.relationmanager = relationmanager;
    }

    public String getRelationmanager() {
        return relationmanager;
    }

    /**
     * Setter/Getter for PRIMARY_OCCUPATION - table Field
     */
    public void setPrimaryOccupation(String primaryOccupation) {
        this.primaryOccupation = primaryOccupation;
    }

    public String getPrimaryOccupation() {
        return primaryOccupation;
    }

    /**
     * Setter/Getter for PREFERRED_COMM - table Field
     */
    public void setPreferredComm(String preferredComm) {
        this.preferredComm = preferredComm;
    }

    public String getPreferredComm() {
        return preferredComm;
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
     * Setter/Getter for MARITALSTATUS - table Field
     */
    public void setMaritalstatus(String maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public String getMaritalstatus() {
        return maritalstatus;
    }

    /**
     * Setter/Getter for CUST_TYPE_ID - table Field
     */
    public void setCustTypeId(String custTypeId) {
        this.custTypeId = custTypeId;
    }

    public String getCustTypeId() {
        return custTypeId;
    }

    /**
     * Setter/Getter for SSN - table Field
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getSsn() {
        return ssn;
    }

    /**
     * Setter/Getter for CUST_USERID - table Field
     */
    public void setCustUserid(String custUserid) {
        this.custUserid = custUserid;
    }

    public String getCustUserid() {
        return custUserid;
    }

    /**
     * Setter/Getter for CUST_PWD - table Field
     */
    public void setCustPwd(String custPwd) {
        this.custPwd = custPwd;
    }

    public String getCustPwd() {
        return custPwd;
    }

    /**
     * Setter/Getter for PHOTO_FILE - table Field
     */
//	public void setPhotoFile (byte[] photoFile) {
//		this.photoFile = photoFile;
//	}
//	public byte[] getPhotoFile () {
//		return photoFile;
//	}
    /**
     * Setter/Getter for SIGNATURE_FILE - table Field
     */
//	public void setSignatureFile (byte[] signatureFile) {
//		this.signatureFile = signatureFile;
//	}
//	public byte[] getSignatureFile () {
//		return signatureFile;
//	}
    /**
     * Setter/Getter for CUST_TYPE - table Field
     */
    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getCustType() {
        return custType;
    }

    /**
     * Setter/Getter for WEBSITE - table Field
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    /**
     * Setter/Getter for AUTHORIZE_CUST_ID - table Field
     */
    public void setAuthorizeCustId(String authorizeCustId) {
        this.authorizeCustId = authorizeCustId;
    }

    public String getAuthorizeCustId() {
        return authorizeCustId;
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
     * Setter/Getter for AUTHORIZED_DT - table Field
     */
    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for TRANS_PWD - table Field
     */
    public void setTransPwd(String transPwd) {
        this.transPwd = transPwd;
    }

    public String getTransPwd() {
        return transPwd;
    }

    /**
     * Setter/Getter for MEMBERSHIP_CLASS - table Field
     */
    public void setMembershipClass(String membershipClass) {
        this.membershipClass = membershipClass;
    }

    public String getMembershipClass() {
        return membershipClass;
    }

    /**
     * Setter/Getter for CARE_OF - table Field
     */
    public void setCareOf(String careOf) {
        this.careOf = careOf;
    }

    public String getCareOf() {
        return careOf;
    }

    /**
     * Setter/Getter for CARE_OF_NAME - table Field
     */
    public void setCareOfName(String careOfName) {
        this.careOfName = careOfName;
    }

    public String getCareOfName() {
        return careOfName;
    }

    /**
     * Setter/Getter for CUSTOMER_STATUS - table Field
     */
    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    /**
     * Setter/Getter for CR_AVAILED_SINCE - table Field
     */
    public void setCrAvailedSince(Date crAvailedSince) {
        this.crAvailedSince = crAvailedSince;
    }

    public Date getCrAvailedSince() {
        return crAvailedSince;
    }

    /**
     * Setter/Getter for RISK_RATING - table Field
     */
    public void setRiskRating(String riskRating) {
        this.riskRating = riskRating;
    }

    public String getRiskRating() {
        return riskRating;
    }

    /**
     * Setter/Getter for INTRO_TYPE - table Field
     */
    public void setIntroType(String introType) {
        this.introType = introType;
    }

    public String getIntroType() {
        return introType;
    }

    /**
     * Setter/Getter for ADDR_VERIFIED - table Field
     */
    public void setAddrVerified(String addrVerified) {
        this.addrVerified = addrVerified;
    }

    public String getAddrVerified() {
        return addrVerified;
    }

    /**
     * Setter/Getter for PHONE_VERIFIED - table Field
     */
    public void setPhoneVerified(String phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public String getPhoneVerified() {
        return phoneVerified;
    }

    /**
     * Setter/Getter for OBTAIN_FINSTAT - table Field
     */
    public void setObtainFinstat(String obtainFinstat) {
        this.obtainFinstat = obtainFinstat;
    }

    public String getObtainFinstat() {
        return obtainFinstat;
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
     * Setter/Getter for CREATED_BY - table Field
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Setter/Getter for SEND_THANKS_LETTER - table Field
     */
    public void setSendThanksLetter(String sendThanksLetter) {
        this.sendThanksLetter = sendThanksLetter;
    }

    public String getSendThanksLetter() {
        return sendThanksLetter;
    }

    /**
     * Setter/Getter for CONFIRM_THANKS - table Field
     */
    public void setConfirmThanks(String confirmThanks) {
        this.confirmThanks = confirmThanks;
    }

    public String getConfirmThanks() {
        return confirmThanks;
    }

    /**
     * Setter/Getter for STAFF_ID - table Field
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffId() {
        return staffId;
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
     * Setter/Getter for NETWORTH_AS_ON - table Field
     */
    public void setNetworthAsOn(Date networthAsOn) {
        this.networthAsOn = networthAsOn;
    }

    public Date getNetworthAsOn() {
        return networthAsOn;
    }

    /**
     * Setter/Getter for INITIATED_BRANCH - table Field
     */
    public void setInitiatedBranch(String initiatedBranch) {
        this.initiatedBranch = initiatedBranch;
    }

    public String getInitiatedBranch() {
        return initiatedBranch;
    }
    
     public String getAmsam() {
        return amsam;
    }

    public void setAmsam(String amsam) {
        this.amsam = amsam;
    }

    public String getDesam() {
        return desam;
    }

    public void setDesam(String desam) {
        this.desam = desam;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getRetAge() {
        return retAge;
    }

    public void setRetAge(Integer retAge) {
        this.retAge = retAge;
    }
    
    

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(custId);
        return custId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("lname", lname));
        strB.append(getTOString("fname", fname));
        strB.append(getTOString("mname", mname));
        strB.append(getTOString("dob", dob));
        strB.append(getTOString("networth", networth));
        strB.append(getTOString("createddt", createddt));
        strB.append(getTOString("deleteddt", deleteddt));
        strB.append(getTOString("gender", gender));
        strB.append(getTOString("profession", profession));
        strB.append(getTOString("minor", minor));
        strB.append(getTOString("mobileAppLoginStatus", mobileAppLoginStatus));
        strB.append(getTOString("annincome", annincome));
        strB.append(getTOString("education", education));
        strB.append(getTOString("vehicle", vehicle));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("compName", compName));
        strB.append(getTOString("commAddrType", commAddrType));
        strB.append(getTOString("emailId", emailId));
        strB.append(getTOString("title", title));
        strB.append(getTOString("residentialstatus", residentialstatus));
        strB.append(getTOString("nationality", nationality));
        strB.append(getTOString("language", language));
        strB.append(getTOString("customergroup", customergroup));
        strB.append(getTOString("relationmanager", relationmanager));
        strB.append(getTOString("primaryOccupation", primaryOccupation));
        strB.append(getTOString("preferredComm", preferredComm));
        strB.append(getTOString("status", status));
        strB.append(getTOString("maritalstatus", maritalstatus));
        strB.append(getTOString("custTypeId", custTypeId));
        strB.append(getTOString("ssn", ssn));
        strB.append(getTOString("custUserid", custUserid));
        strB.append(getTOString("custPwd", custPwd));
//		strB.append(getTOString("photoFile", photoFile));
//		strB.append(getTOString("signatureFile", signatureFile));
        strB.append(getTOString("custType", custType));
        strB.append(getTOString("website", website));
        strB.append(getTOString("authorizeCustId", authorizeCustId));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("transPwd", transPwd));
        strB.append(getTOString("membershipClass", membershipClass));
        strB.append(getTOString("careOf", careOf));
        strB.append(getTOString("careOfName", careOfName));
        strB.append(getTOString("customerStatus", customerStatus));
        strB.append(getTOString("crAvailedSince", crAvailedSince));
        strB.append(getTOString("riskRating", riskRating));
        strB.append(getTOString("introType", introType));
        strB.append(getTOString("addrVerified", addrVerified));
        strB.append(getTOString("phoneVerified", phoneVerified));
        strB.append(getTOString("obtainFinstat", obtainFinstat));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("sendThanksLetter", sendThanksLetter));
        strB.append(getTOString("confirmThanks", confirmThanks));
        strB.append(getTOString("staffId", staffId));
        strB.append(getTOString("designation", designation));
        strB.append(getTOString("networthAsOn", networthAsOn));
        strB.append(getTOString("initiatedBranch", initiatedBranch));
        strB.append(getTOString("caste", caste));
        strB.append(getTOString("subCaste", subCaste));  
        strB.append(getTOString("cboReligion", cboReligion));
        strB.append(getTOString("panNumber", panNumber));
        strB.append(getTOString("deleteRemarks", deleteRemarks));
//                strB.append(getTOString("TinNumber", TinNumber));
        strB.append(getTOString("addrProof", addrProof));
        strB.append(getTOString("farClass", farClass));
        strB.append(getTOString("kartha", kartha));
        strB.append(getTOString("age", age));
        strB.append(getTOString("bankruptcy", bankruptcy));
        strB.append(getTOString("membershipNum", membershipNum));
        strB.append(getTOString("itDec", itDec));
        strB.append(getTOString("wardNo", wardNo));
        strB.append(getTOString("minority", minority));
        strB.append(getTOString("division", division));
        strB.append(getTOString("retDt", retDt));
        strB.append(getTOString("retAge", retAge));
        strB.append(getTOString("agentCustId", agentCustId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("lname", lname));
        strB.append(getTOXml("fname", fname));
        strB.append(getTOXml("mname", mname));
        strB.append(getTOXml("dob", dob));
        strB.append(getTOXml("networth", networth));
        strB.append(getTOXml("createddt", createddt));
        strB.append(getTOXml("deleteddt", deleteddt));
        strB.append(getTOXml("gender", gender));
        strB.append(getTOXml("mobileAppLoginStatus", mobileAppLoginStatus));
        strB.append(getTOXml("profession", profession));
        strB.append(getTOXml("minor", minor));
        strB.append(getTOXml("annincome", annincome));
        strB.append(getTOXml("education", education));
        strB.append(getTOXml("vehicle", vehicle));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("compName", compName));
        strB.append(getTOXml("commAddrType", commAddrType));
        strB.append(getTOXml("emailId", emailId));
        strB.append(getTOXml("title", title));
        strB.append(getTOXml("residentialstatus", residentialstatus));
        strB.append(getTOXml("nationality", nationality));
        strB.append(getTOXml("language", language));
        strB.append(getTOXml("customergroup", customergroup));
        strB.append(getTOXml("relationmanager", relationmanager));
        strB.append(getTOXml("primaryOccupation", primaryOccupation));
        strB.append(getTOXml("preferredComm", preferredComm));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("maritalstatus", maritalstatus));
        strB.append(getTOXml("custTypeId", custTypeId));
        strB.append(getTOXml("ssn", ssn));
        strB.append(getTOXml("custUserid", custUserid));
        strB.append(getTOXml("custPwd", custPwd));
//		strB.append(getTOXml("photoFile", photoFile));
//		strB.append(getTOXml("signatureFile", signatureFile));
        strB.append(getTOXml("custType", custType));
        strB.append(getTOXml("website", website));
        strB.append(getTOXml("authorizeCustId", authorizeCustId));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("transPwd", transPwd));
        strB.append(getTOXml("membershipClass", membershipClass));
        strB.append(getTOXml("careOf", careOf));
        strB.append(getTOXml("careOfName", careOfName));
        strB.append(getTOXml("customerStatus", customerStatus));
        strB.append(getTOXml("crAvailedSince", crAvailedSince));
        strB.append(getTOXml("riskRating", riskRating));
        strB.append(getTOXml("introType", introType));
        strB.append(getTOXml("addrVerified", addrVerified));
        strB.append(getTOXml("phoneVerified", phoneVerified));
        strB.append(getTOXml("obtainFinstat", obtainFinstat));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("sendThanksLetter", sendThanksLetter));
        strB.append(getTOXml("confirmThanks", confirmThanks));
        strB.append(getTOXml("staffId", staffId));
        strB.append(getTOXml("designation", designation));
        strB.append(getTOXml("networthAsOn", networthAsOn));
        strB.append(getTOXml("initiatedBranch", initiatedBranch));
        strB.append(getTOXml("caste", caste));
        strB.append(getTOXml("subCaste", subCaste));  
        strB.append(getTOXml("cboReligion", cboReligion));
        strB.append(getTOXml("panNumber", panNumber));
        strB.append(getTOXml("deleteRemarks", deleteRemarks));
//                strB.append(getTOString("TinNumber", TinNumber));
        strB.append(getTOXml("addrProof", addrProof));
        strB.append(getTOXml("farClass", farClass));
        strB.append(getTOXml("kartha", kartha));
        strB.append(getTOXml("age", age));
        strB.append(getTOXml("bankruptcy", bankruptcy));
        strB.append(getTOXml("membershipNum", membershipNum));
        strB.append(getTOXml("itDec", itDec));
        strB.append(getTOXml("wardNo", wardNo));
        strB.append(getTOXml("minority", minority));
        strB.append(getTOXml("division", division));
        strB.append(getTOXml("retDt", retDt));
        strB.append(getTOXml("retAge", retAge));
        strB.append(getTOXml("agentCustId", agentCustId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property caste.
     *
     * @return Value of property caste.
     */
    public java.lang.String getCaste() {
        return caste;
    }

    /**
     * Setter for property caste.
     *
     * @param caste New value of property caste.
     */
    public void setCaste(java.lang.String caste) {
        this.caste = caste;
    }

    /**
     * Getter for property panNumber.
     *
     * @return Value of property panNumber.
     */
    public java.lang.String getPanNumber() {
        return panNumber;
    }

    /**
     * Setter for property panNumber.
     *
     * @param panNumber New value of property panNumber.
     */
    public void setPanNumber(java.lang.String panNumber) {
        this.panNumber = panNumber;
    }

    /**
     * Getter for property deleteRemarks.
     *
     * @return Value of property deleteRemarks.
     */
    public java.lang.String getDeleteRemarks() {
        return deleteRemarks;
    }

    /**
     * Setter for property deleteRemarks.
     *
     * @param deleteRemarks New value of property deleteRemarks.
     */
    public void setDeleteRemarks(java.lang.String deleteRemarks) {
        this.deleteRemarks = deleteRemarks;
    }

    /**
     * Getter for property addrProof.
     *
     * @return Value of property addrProof.
     */
    public java.lang.String getAddrProof() {
        return addrProof;
    }

    /**
     * Setter for property addrProof.
     *
     * @param addrProof New value of property addrProof.
     */
    public void setAddrProof(java.lang.String addrProof) {
        this.addrProof = addrProof;
    }

    /**
     * Getter for property idenProof.
     *
     * @return Value of property idenProof.
     */
    public java.lang.String getIdenProof() {
        return idenProof;
    }

    /**
     * Setter for property idenProof.
     *
     * @param idenProof New value of property idenProof.
     */
    public void setIdenProof(java.lang.String idenProof) {
        this.idenProof = idenProof;
    }

    /**
     * Getter for property kartha.
     *
     * @return Value of property kartha.
     */
    public java.lang.String getKartha() {
        return kartha;
    }

    /**
     * Setter for property kartha.
     *
     * @param kartha New value of property kartha.
     */
    public void setKartha(java.lang.String kartha) {
        this.kartha = kartha;
    }

    /**
     * Getter for property farClass.
     *
     * @return Value of property farClass.
     */
    public java.lang.String getFarClass() {
        return farClass;
    }

    /**
     * Setter for property farClass.
     *
     * @param farClass New value of property farClass.
     */
    public void setFarClass(java.lang.String farClass) {
        this.farClass = farClass;
    }


    /**
     * Getter for property bankruptcy.
     *
     * @return Value of property bankruptcy.
     */
    public java.lang.String getBankruptcy() {
        return bankruptcy;
    }

    /**
     * Setter for property bankruptcy.
     *
     * @param bankruptcy New value of property bankruptcy.
     */
    public void setBankruptcy(java.lang.String bankruptcy) {
        this.bankruptcy = bankruptcy;
    }

    /**
     * Getter for property membershipNum.
     *
     * @return Value of property membershipNum.
     */
    public java.lang.String getMembershipNum() {
        return membershipNum;
    }

    /**
     * Setter for property membershipNum.
     *
     * @param membershipNum New value of property membershipNum.
     */
    public void setMembershipNum(java.lang.String membershipNum) {
        this.membershipNum = membershipNum;
    }

    /**
     * Getter for property itDec.
     *
     * @return Value of property itDec.
     */
    public java.lang.String getItDec() {
        return itDec;
    }

    /**
     * Setter for property itDec.
     *
     * @param itDec New value of property itDec.
     */
    public void setItDec(java.lang.String itDec) {
        this.itDec = itDec;
    }

    /**
     * Getter for property cboReligion.
     *
     * @return Value of property cboReligion.
     */
    public java.lang.String getCboReligion() {
        return cboReligion;
    }

    /**
     * Setter for property cboReligion.
     *
     * @param cboReligion New value of property cboReligion.
     */
    public void setCboReligion(java.lang.String cboReligion) {
        this.cboReligion = cboReligion;
    }

    /**
     * Getter for property wardNo.
     *
     * @return Value of property wardNo.
     */
    public java.lang.String getWardNo() {
        return wardNo;
    }

    /**
     * Setter for property wardNo.
     *
     * @param wardNo New value of property wardNo.
     */
    public void setWardNo(java.lang.String wardNo) {
        this.wardNo = wardNo;
    }

    /**
     * Getter for property joiningDate.
     *
     * @return Value of property joiningDate.
     */
    public Date getJoiningDate() {
        return joiningDate;
    }

    /**
     * Setter for property joiningDate.
     *
     * @param joiningDate New value of property joiningDate.
     */
    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }
    /**
     * Getter for property TinNumber.
     *
     * @return Value of property TinNumber.
     */
//        public java.lang.String getTinNumber() {
//            return TinNumber;
//        }        
    /**
     * Setter for property TinNumber.
     *
     * @param TinNumber New value of property TinNumber.
     */
//        public void setTinNumber(java.lang.String TinNumber) {
//            this.TinNumber = TinNumber;
//        }        
}