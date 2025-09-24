/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmployeeDetailsTO.java
 * 
 * Created on Sat Apr 09 17:55:24 IST 2005
 */
package com.see.truetransact.transferobject.sysadmin.employee;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is BANK_EMPLOYEE.
 */
public class EmployeeDetailsTO extends TransferObject implements Serializable {

    private String branchCode = "";
    private String employeeCode = "";
    private String title = "";
    private String lname = "";
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
    private String authorizeStatus1 = "";
    private String authorizeStatus2 = "";
    private String authorizeBy1 = "";
    private String authorizeBy2 = "";
    private Date authorizeDt1 = null;
    private Date authorizeDt2 = null;

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
     * Setter/Getter for EMPLOYEE_CODE - table Field
     */
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeCode() {
        return employeeCode;
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
     * Setter/Getter for DESIG_ID - table Field
     */
    public void setDesigId(String desigId) {
        this.desigId = desigId;
    }

    public String getDesigId() {
        return desigId;
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
     * Setter/Getter for MARITAL_STATUS - table Field
     */
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMaritalStatus() {
        return maritalStatus;
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
     * Setter/Getter for DOJ - table Field
     */
    public void setDoj(Date doj) {
        this.doj = doj;
    }

    public Date getDoj() {
        return doj;
    }

    /**
     * Setter/Getter for DOL - table Field
     */
    public void setDol(Date dol) {
        this.dol = dol;
    }

    public Date getDol() {
        return dol;
    }

    /**
     * Setter/Getter for DOW - table Field
     */
    public void setDow(Date dow) {
        this.dow = dow;
    }

    public Date getDow() {
        return dow;
    }

    /**
     * Setter/Getter for DEPTT_ID - table Field
     */
    public void setDepttId(String depttId) {
        this.depttId = depttId;
    }

    public String getDepttId() {
        return depttId;
    }

    /**
     * Setter/Getter for MANAGER_CODE - table Field
     */
    public void setManagerCode(String managerCode) {
        this.managerCode = managerCode;
    }

    public String getManagerCode() {
        return managerCode;
    }

    /**
     * Setter/Getter for OFFICIAL_EMAIL - table Field
     */
    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }

    public String getOfficialEmail() {
        return officialEmail;
    }

    /**
     * Setter/Getter for ALTERNATE_EMAIL - table Field
     */
    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    /**
     * Setter/Getter for OFFICE_PHONE - table Field
     */
    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    /**
     * Setter/Getter for HOME_PHONE - table Field
     */
    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    /**
     * Setter/Getter for CELLULAR - table Field
     */
    public void setCellular(String cellular) {
        this.cellular = cellular;
    }

    public String getCellular() {
        return cellular;
    }

    /**
     * Setter/Getter for PAN_NO - table Field
     */
    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getPanNo() {
        return panNo;
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
     * Setter/Getter for PASSPORT_NO - table Field
     */
    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPassportNo() {
        return passportNo;
    }

    /**
     * Setter/Getter for SKILLS - table Field
     */
    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getSkills() {
        return skills;
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
     * Setter/Getter for EXPERIENCE - table Field
     */
    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getExperience() {
        return experience;
    }

    /**
     * Setter/Getter for PHOTO_FILE - table Field
     */
    public void setPhotoFile(String photoFile) {
        this.photoFile = photoFile;
    }

    public String getPhotoFile() {
        return photoFile;
    }

    /**
     * Setter/Getter for RESPONSIBILITY - table Field
     */
    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getResponsibility() {
        return responsibility;
    }

    /**
     * Setter/Getter for PERFORMANCE - table Field
     */
    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getPerformance() {
        return performance;
    }

    /**
     * Setter/Getter for COMMENTS - table Field
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
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
     * Setter/Getter for CREATED_DT - table Field
     */
    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getCreatedDt() {
        return createdDt;
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
     * Setter/Getter for EMPLOYEE_TYPE - table Field
     */
    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getEmployeeType() {
        return employeeType;
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
     * Setter/Getter for AUTHORIZE_STATUS_1 - table Field
     */
    public void setAuthorizeStatus1(String authorizeStatus1) {
        this.authorizeStatus1 = authorizeStatus1;
    }

    public String getAuthorizeStatus1() {
        return authorizeStatus1;
    }

    /**
     * Setter/Getter for AUTHORIZE_STATUS_2 - table Field
     */
    public void setAuthorizeStatus2(String authorizeStatus2) {
        this.authorizeStatus2 = authorizeStatus2;
    }

    public String getAuthorizeStatus2() {
        return authorizeStatus2;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY_1 - table Field
     */
    public void setAuthorizeBy1(String authorizeBy1) {
        this.authorizeBy1 = authorizeBy1;
    }

    public String getAuthorizeBy1() {
        return authorizeBy1;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY_2 - table Field
     */
    public void setAuthorizeBy2(String authorizeBy2) {
        this.authorizeBy2 = authorizeBy2;
    }

    public String getAuthorizeBy2() {
        return authorizeBy2;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT_1 - table Field
     */
    public void setAuthorizeDt1(Date authorizeDt1) {
        this.authorizeDt1 = authorizeDt1;
    }

    public Date getAuthorizeDt1() {
        return authorizeDt1;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT_2 - table Field
     */
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
        strB.append(getTOString("authorizeStatus1", authorizeStatus1));
        strB.append(getTOString("authorizeStatus2", authorizeStatus2));
        strB.append(getTOString("authorizeBy1", authorizeBy1));
        strB.append(getTOString("authorizeBy2", authorizeBy2));
        strB.append(getTOString("authorizeDt1", authorizeDt1));
        strB.append(getTOString("authorizeDt2", authorizeDt2));
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
        strB.append(getTOXml("authorizeStatus1", authorizeStatus1));
        strB.append(getTOXml("authorizeStatus2", authorizeStatus2));
        strB.append(getTOXml("authorizeBy1", authorizeBy1));
        strB.append(getTOXml("authorizeBy2", authorizeBy2));
        strB.append(getTOXml("authorizeDt1", authorizeDt1));
        strB.append(getTOXml("authorizeDt2", authorizeDt2));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}