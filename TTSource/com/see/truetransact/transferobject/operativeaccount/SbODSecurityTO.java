

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * SbODSecurityTO.java
 */

package com.see.truetransact.transferobject.operativeaccount;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class SbODSecurityTO extends TransferObject implements Serializable {
    
    private String acctNum = "";
    private String memberNo = "";
    private String memberName = "";
    private String memberType = "";
    private Long contactNo = null;
    private String memberNetworth = "";
    private Double memberSalary = null;
    private String borrowerNetworth = "";
    private Double borrowerSalary = null;
    private String branchCode = "";
    private String status = "";
    private Date statusDt = null;
    private String statusBy = "";
    private String authorizedStatus = null;
    private Date authorizedDt = null;
    private String authorizedBy = "";
    private String acctstatus = "";
    private Date fromDt = null;
    private Date toDt = null;
    private String borrowerMemberNo = "";
    private String isRenewed = "";
    private String isTODAddedWithAvailBal = "";
    private Double todSanctioned = null;
    private Double suretyEligAmt = null;
    private String sbOdStatus = "";

    public String getSbOdStatus() {
        return sbOdStatus;
    }

    public void setSbOdStatus(String sbOdStatus) {
        this.sbOdStatus = sbOdStatus;
    }

    public Double getSuretyEligAmt() {
        return suretyEligAmt;
    }

    public void setSuretyEligAmt(Double suretyEligAmt) {
        this.suretyEligAmt = suretyEligAmt;
    }

    public Double getTodSanctioned() {
        return todSanctioned;
    }

    public void setTodSanctioned(Double todSanctioned) {
        this.todSanctioned = todSanctioned;
    }  
    
    public String getIsTODAddedWithAvailBal() {
        return isTODAddedWithAvailBal;
    }

    public void setIsTODAddedWithAvailBal(String isTODAddedWithAvailBal) {
        this.isTODAddedWithAvailBal = isTODAddedWithAvailBal;
    }    
    
    public String getIsRenewed() {
        return isRenewed;
    }

    public void setIsRenewed(String isRenewed) {
        this.isRenewed = isRenewed;
    }
    
    public String getBorrowerMemberNo() {
        return borrowerMemberNo;
    }

    public void setBorrowerMemberNo(String borrowerMemberNo) {
        this.borrowerMemberNo = borrowerMemberNo;
    }
    
    public String getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(String acctNum) {
        this.acctNum = acctNum;
    }

    public String getAcctstatus() {
        return acctstatus;
    }

    public void setAcctstatus(String acctstatus) {
        this.acctstatus = acctstatus;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public Date getAuthorizedDt() {
        return authorizedDt;
    }

    public void setAuthorizedDt(Date authorizedDt) {
        this.authorizedDt = authorizedDt;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getBorrowerNetworth() {
        return borrowerNetworth;
    }

    public void setBorrowerNetworth(String borrowerNetworth) {
        this.borrowerNetworth = borrowerNetworth;
    }

    public Double getBorrowerSalary() {
        return borrowerSalary;
    }

    public void setBorrowerSalary(Double borrowerSalary) {
        this.borrowerSalary = borrowerSalary;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public Long getContactNo() {
        return contactNo;
    }

    public void setContactNo(Long contactNo) {
        this.contactNo = contactNo;
    }



    public Date getFromDt() {
        return fromDt;
    }

    public void setFromDt(Date fromDt) {
        this.fromDt = fromDt;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberNetworth() {
        return memberNetworth;
    }

    public void setMemberNetworth(String memberNetworth) {
        this.memberNetworth = memberNetworth;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public Double getMemberSalary() {
        return memberSalary;
    }

    public void setMemberSalary(Double memberSalary) {
        this.memberSalary = memberSalary;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(Date statusDt) {
        this.statusDt = statusDt;
    }

    public Date getToDt() {
        return toDt;
    }

    public void setToDt(Date toDt) {
        this.toDt = toDt;
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
        strB.append(getTOString("memberNo", memberNo));
        strB.append(getTOString("memberName", memberName));
        strB.append(getTOString("memberType", memberType));
        strB.append(getTOString("contactNo", contactNo));
        strB.append(getTOString("membernetworth", memberNetworth));
        strB.append(getTOString("memberSalary", memberSalary));
        strB.append(getTOString("borrowerSalary", borrowerSalary));
        strB.append(getTOString("borrowerNetworth", borrowerNetworth));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedBy", authorizedBy));
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
        strB.append(getTOXml("memberNo", memberNo));
        strB.append(getTOXml("memberName", memberName));
        strB.append(getTOXml("memberType", memberType));
        strB.append(getTOXml("contactNo", contactNo));
        strB.append(getTOString("membernetworth", memberNetworth));
        strB.append(getTOString("memberSalary", memberSalary));
        strB.append(getTOString("borrowerSalary", borrowerSalary));
        strB.append(getTOString("borrowerNetworth", borrowerNetworth));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
    
    
}
