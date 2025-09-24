/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmployeeDetailsTO.java
 * 
 * 
 */
package com.see.truetransact.transferobject.payroll.employee;

/**
 *
 * @author anjuanand
 */
import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 * Table name for this TO is EMPLOYEE_MASTER.
 */
public class EmployeeDetailsTO extends TransferObject implements Serializable {

    private String branchCode = "";
    private String employeeCode = "";
    private String title = "";
    private String lname = "";
    private String mname = "";
    private String fname = "";
    private String desigId = "";
    private Date dob = null;
    private String maritalStatus = "";
    private String gender = "";
    private Date doj = null;
    private Date dol = null;
    private Date dow = null;
    private String depttId = "";
    private String managerCode = "";
    private String officialEmail = "";
    private String alternateEmail = "";
    private String officePhone = "";
    private String homePhone = "";
    private String cellular = "";
    private String panNo = "";
    private String ssn = "";
    private String passportNo = "";
    private String skills = "";
    private String education = "";
    private String experience = "";
    private String photoFile = "";
    private String responsibility = "";
    private String performance = "";
    private String comments = "";
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String employeeType = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus1 = null;
    private String authorizeStatus2 = null;
    private String authorizeBy1 = "";
    private String authorizeBy2 = "";
    private Date authorizeDt1 = null;
    private Date authorizeDt2 = null;
    private String customerCode = "";
    private String customerId = "";
    private Integer sortOrder = 0;
    private String fatherName = "";
    private String motherName = "";
    private String spouseName = "";
    private String spouseRelation = "";
    private String placeofBirth = "";
    private String religion = "";
    private String caste = "";
    private String bloodGroup = "";
    private String identificationMark1 = "";
    private String identificationMark2 = "";
    private String physicallyHandicapped = "";
    private String majorHealthProblem = "";
    private String fatherTitle = "";
    private String motherTitle = "";
    private String commAddressType = "";
    private String manager = "";
    private String jobTitle = "";
    private String authorizeBy = "";
    private String authorizeStatus = null;
    private Date authorizeDt = null;

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
    }

    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getCommAddressType() {
        return commAddressType;
    }

    public void setCommAddressType(String commAddressType) {
        this.commAddressType = commAddressType;
    }

    public String getFatherTitle() {
        return fatherTitle;
    }

    public void setFatherTitle(String fatherTitle) {
        this.fatherTitle = fatherTitle;
    }

    public String getMotherTitle() {
        return motherTitle;
    }

    public void setMotherTitle(String motherTitle) {
        this.motherTitle = motherTitle;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getIdentificationMark1() {
        return identificationMark1;
    }

    public void setIdentificationMark1(String identificationMark1) {
        this.identificationMark1 = identificationMark1;
    }

    public String getIdentificationMark2() {
        return identificationMark2;
    }

    public void setIdentificationMark2(String identificationMark2) {
        this.identificationMark2 = identificationMark2;
    }

    public String getMajorHealthProblem() {
        return majorHealthProblem;
    }

    public void setMajorHealthProblem(String majorHealthProblem) {
        this.majorHealthProblem = majorHealthProblem;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getPhysicallyHandicapped() {
        return physicallyHandicapped;
    }

    public void setPhysicallyHandicapped(String physicallyHandicapped) {
        this.physicallyHandicapped = physicallyHandicapped;
    }

    public String getPlaceofBirth() {
        return placeofBirth;
    }

    public void setPlaceofBirth(String placeofBirth) {
        this.placeofBirth = placeofBirth;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }


    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getSpouseRelation() {
        return spouseRelation;
    }

    public void setSpouseRelation(String spouseRelation) {
        this.spouseRelation = spouseRelation;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLname() {
        return lname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {
        return fname;
    }

    public void setDesigId(String desigId) {
        this.desigId = desigId;
    }

    public String getDesigId() {
        return desigId;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getDob() {
        return dob;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setDoj(Date doj) {
        this.doj = doj;
    }

    public Date getDoj() {
        return doj;
    }

    public void setDol(Date dol) {
        this.dol = dol;
    }

    public Date getDol() {
        return dol;
    }

    public void setDow(Date dow) {
        this.dow = dow;
    }

    public Date getDow() {
        return dow;
    }

    public void setDepttId(String depttId) {
        this.depttId = depttId;
    }

    public String getDepttId() {
        return depttId;
    }

    public void setManagerCode(String managerCode) {
        this.managerCode = managerCode;
    }

    public String getManagerCode() {
        return managerCode;
    }

    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }

    public String getOfficialEmail() {
        return officialEmail;
    }

    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setCellular(String cellular) {
        this.cellular = cellular;
    }

    public String getCellular() {
        return cellular;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getSsn() {
        return ssn;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getSkills() {
        return skills;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducation() {
        return education;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getExperience() {
        return experience;
    }

    public void setPhotoFile(String photoFile) {
        this.photoFile = photoFile;
    }

    public String getPhotoFile() {
        return photoFile;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getPerformance() {
        return performance;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setAuthorizeStatus1(String authorizeStatus1) {
        this.authorizeStatus1 = authorizeStatus1;
    }

    public String getAuthorizeStatus1() {
        return authorizeStatus1;
    }

    public void setAuthorizeStatus2(String authorizeStatus2) {
        this.authorizeStatus2 = authorizeStatus2;
    }

    public String getAuthorizeStatus2() {
        return authorizeStatus2;
    }

    public void setAuthorizeBy1(String authorizeBy1) {
        this.authorizeBy1 = authorizeBy1;
    }

    public String getAuthorizeBy1() {
        return authorizeBy1;
    }

    public void setAuthorizeBy2(String authorizeBy2) {
        this.authorizeBy2 = authorizeBy2;
    }

    public String getAuthorizeBy2() {
        return authorizeBy2;
    }

    public void setAuthorizeDt1(Date authorizeDt1) {
        this.authorizeDt1 = authorizeDt1;
    }

    public Date getAuthorizeDt1() {
        return authorizeDt1;
    }

    public void setAuthorizeDt2(Date authorizeDt2) {
        this.authorizeDt2 = authorizeDt2;
    }

    public Date getAuthorizeDt2() {
        return authorizeDt2;
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
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("employeeCode", employeeCode));
        strB.append(getTOString("customerId", customerId));
        strB.append(getTOString("title", title));
        strB.append(getTOString("lname", lname));
        strB.append(getTOString("fname", fname));
        strB.append(getTOString("desigId", desigId));
        strB.append(getTOString("dob", dob));
        strB.append(getTOString("maritalStatus", maritalStatus));
        strB.append(getTOString("gender", gender));
        strB.append(getTOString("doj", doj));
        strB.append(getTOString("dol", dol));
        strB.append(getTOString("dow", dow));
        strB.append(getTOString("depttId", depttId));
        strB.append(getTOString("managerCode", managerCode));
        strB.append(getTOString("officialEmail", officialEmail));
        strB.append(getTOString("alternateEmail", alternateEmail));
        strB.append(getTOString("officePhone", officePhone));
        strB.append(getTOString("homePhone", homePhone));
        strB.append(getTOString("cellular", cellular));
        strB.append(getTOString("panNo", panNo));
        strB.append(getTOString("ssn", ssn));
        strB.append(getTOString("passportNo", passportNo));
        strB.append(getTOString("skills", skills));
        strB.append(getTOString("education", education));
        strB.append(getTOString("experience", experience));
        strB.append(getTOString("photoFile", photoFile));
        strB.append(getTOString("responsibility", responsibility));
        strB.append(getTOString("performance", performance));
        strB.append(getTOString("comments", comments));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("employeeType", employeeType));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeStatus1", authorizeStatus1));
        strB.append(getTOString("authorizeStatus2", authorizeStatus2));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeBy1", authorizeBy1));
        strB.append(getTOString("authorizeBy2", authorizeBy2));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("authorizeDt1", authorizeDt1));
        strB.append(getTOString("authorizeDt2", authorizeDt2));
        strB.append(getTOString("sortOrder", sortOrder));
        strB.append(getTOString("fatherName", fatherName));
        strB.append(getTOString("motherName", motherName));
        strB.append(getTOString("spouseName", spouseName));
        strB.append(getTOString("spouseRelation", spouseRelation));
        strB.append(getTOString("placeofBirth", placeofBirth));
        strB.append(getTOString("religion", religion));
        strB.append(getTOString("caste", caste));
        strB.append(getTOString("bloodGroup", bloodGroup));
        strB.append(getTOString("identificationMark1", identificationMark1));
        strB.append(getTOString("identificationMark2", identificationMark2));
        strB.append(getTOString("physicallyHandicapped", physicallyHandicapped));
        strB.append(getTOString("majorHealthProblem", majorHealthProblem));
        strB.append(getTOString("fatherTitle", fatherTitle));
        strB.append(getTOString("motherTitle", motherTitle));
        strB.append(getTOString("commAddressType", commAddressType));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("employeeCode", employeeCode));
        strB.append(getTOXml("customerId", customerId));
        strB.append(getTOXml("title", title));
        strB.append(getTOXml("lname", lname));
        strB.append(getTOXml("fname", fname));
        strB.append(getTOXml("desigId", desigId));
        strB.append(getTOXml("dob", dob));
        strB.append(getTOXml("maritalStatus", maritalStatus));
        strB.append(getTOXml("gender", gender));
        strB.append(getTOXml("doj", doj));
        strB.append(getTOXml("dol", dol));
        strB.append(getTOXml("dow", dow));
        strB.append(getTOXml("depttId", depttId));
        strB.append(getTOXml("managerCode", managerCode));
        strB.append(getTOXml("officialEmail", officialEmail));
        strB.append(getTOXml("alternateEmail", alternateEmail));
        strB.append(getTOXml("officePhone", officePhone));
        strB.append(getTOXml("homePhone", homePhone));
        strB.append(getTOXml("cellular", cellular));
        strB.append(getTOXml("panNo", panNo));
        strB.append(getTOXml("ssn", ssn));
        strB.append(getTOXml("passportNo", passportNo));
        strB.append(getTOXml("skills", skills));
        strB.append(getTOXml("education", education));
        strB.append(getTOXml("experience", experience));
        strB.append(getTOXml("photoFile", photoFile));
        strB.append(getTOXml("responsibility", responsibility));
        strB.append(getTOXml("performance", performance));
        strB.append(getTOXml("comments", comments));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("employeeType", employeeType));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeStatus1", authorizeStatus1));
        strB.append(getTOXml("authorizeStatus2", authorizeStatus2));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeBy1", authorizeBy1));
        strB.append(getTOXml("authorizeBy2", authorizeBy2));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("authorizeDt1", authorizeDt1));
        strB.append(getTOXml("authorizeDt2", authorizeDt2));
        strB.append(getTOXml("sortOrder", sortOrder));
        strB.append(getTOXml("fatherName", fatherName));
        strB.append(getTOXml("motherName", motherName));
        strB.append(getTOXml("spouseName", spouseName));
        strB.append(getTOXml("spouseRelation", spouseRelation));
        strB.append(getTOXml("placeofBirth", placeofBirth));
        strB.append(getTOXml("religion", religion));
        strB.append(getTOXml("caste", caste));
        strB.append(getTOXml("bloodGroup", bloodGroup));
        strB.append(getTOXml("identificationMark1", identificationMark1));
        strB.append(getTOXml("identificationMark2", identificationMark2));
        strB.append(getTOXml("physicallyHandicapped", physicallyHandicapped));
        strB.append(getTOXml("majorHealthProblem", majorHealthProblem));
        strB.append(getTOXml("fatherTitle", fatherTitle));
        strB.append(getTOXml("motherTitle", motherTitle));
        strB.append(getTOXml("commAddressType", commAddressType));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}