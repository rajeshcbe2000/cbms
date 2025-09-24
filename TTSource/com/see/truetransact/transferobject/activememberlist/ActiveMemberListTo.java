/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ActiveMemberListTo.java
 */

package com.see.truetransact.transferobject.activememberlist;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;
import java.util.*;

/**
 *
 * @author Administrator1
 */
public class ActiveMemberListTo extends TransferObject implements Serializable {

    private String activeMemListId = "";
    private Date meetingDate = null;
    private String meetingId = "";
    private Integer noOfShares = 0;
    private String shareType = "";
    private String authorize = null;
    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String shareAcctNum = "";
    private String memberName = "";
    private Double availableBalance = 0.0;
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private Date createdDt = null;
    private String createdBy = "";
    private String branchcode = "";
    private Integer recordCount = 0;
    private Integer slNo = 0;
    private Date fromDate = null; // Added by nithya on 26-09-2016 for 2775

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
    
    public Integer getSlNo() {
        return slNo;
    }

    public void setSlNo(Integer slNo) {
        this.slNo = slNo;
    }

    public String getActiveMemListId() {
        return activeMemListId;
    }

    public void setActiveMemListId(String activeMemListId) {
        this.activeMemListId = activeMemListId;
    }

    public String getAuthorize() {
        return authorize;
    }

    public void setAuthorize(String authorize) {
        this.authorize = authorize;
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

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getBranchcode() {
        return branchcode;
    }

    public void setBranchcode(String branchcode) {
        this.branchcode = branchcode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Integer getNoOfShares() {
        return noOfShares;
    }

    public void setNoOfShares(Integer noOfShares) {
        this.noOfShares = noOfShares;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public String getShareAcctNum() {
        return shareAcctNum;
    }

    public void setShareAcctNum(String shareAcctNum) {
        this.shareAcctNum = shareAcctNum;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
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

    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        //strB.append (getTOStringKey(getKeyData()));
        strB.append(getTOString("meetingDate", meetingDate));
        strB.append(getTOString("meetingId", meetingId));
        strB.append(getTOString("memberName", memberName));
        strB.append(getTOString("noOfShares", noOfShares));
        strB.append(getTOString("shareType", shareType));
        strB.append(getTOString("availableBalance", availableBalance));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorize", authorize));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("branchcode", branchcode));
        strB.append(getTOString("shareAcctNum", shareAcctNum));
        strB.append(getTOString("recordCount", recordCount));
        strB.append(getTOString("slNo", slNo));
        strB.append(getTOString("fromDate", fromDate));
        //System.out.println("share_acct_num===========" + strB.append(getTOString("shareAcctNum", shareAcctNum)));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        //strB.append (getTOXmlKey(getKeyData()));
        strB.append(getTOXml("meetingDate", meetingDate));
        strB.append(getTOXml("meetingId", meetingId));
        strB.append(getTOXml("memberName", memberName));
        strB.append(getTOXml("noOfShares", noOfShares));
        strB.append(getTOXml("shareType", shareType));
        strB.append(getTOXml("availableBalance", availableBalance));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorize", authorize));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("branchcode", branchcode));
        strB.append(getTOXml("shareAcctNum", shareAcctNum));
        strB.append(getTOXml("recordCount", recordCount));
        strB.append(getTOXml("slNo", slNo));
        strB.append(getTOXml("fromDate", fromDate));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}
