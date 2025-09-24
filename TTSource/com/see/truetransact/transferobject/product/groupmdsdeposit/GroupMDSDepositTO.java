/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * GroupMDSDepositTO.java
 */

package com.see.truetransact.transferobject.product.groupmdsdeposit;

import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class GroupMDSDepositTO extends TransferObject implements Serializable{
    private String statusBy = "";
    private Date statusDt;
    private String status="";
    private String groupName="";
    private Integer schemeCount=0;
    private String productType="";
    private String interestAmountType="";
    private Double interestAmount=0.0;
    private String penalCalculationType="";
    private Double prizedPenal=0.0;
    private Double nonPrizedPenal=0.0;
    private String interestRecoveryType="";
    private Double interestRecovery=0.0;
    private Date startDate;
    private Date endDate;
    private String groupNo = "";
    private String groupType = "";
    private String authorizedBy ="";
    private Date authorizedDate;
    private String authorizedStatus = null;    //AJITH Changed from ""
    private String createdBy = "";
    private Date createdDate;
    private String branchId = "";
    private String prematureIntRecType="";
    private Double prematureIntRecAmt=0.0;
    private Double depositAmt=0.0;
    private Integer nextGDSNo=0;
    private String isIntRecovery = "";
    private String isIntCalcIfdue = "";
    private Double intCalcRateForDue = 0.0;
    private String isIntCalcIfdueType = "";
    

    
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
//        strB.append(getTOStringKey(getKeyData()));
        
        strB.append(getTOString("groupName", groupName));
        strB.append(getTOString("schemeCount", schemeCount));
        strB.append(getTOString("productType", productType));
        strB.append(getTOString("interestAmountType", interestAmountType));
        strB.append(getTOString("interestAmount", interestAmount));
        strB.append(getTOString("penalCalculationType", penalCalculationType));
        strB.append(getTOString("prizedPenal", prizedPenal));
        strB.append(getTOString("nonPrizedPenal", nonPrizedPenal));
        strB.append(getTOString("interestRecovery", interestRecovery));
        strB.append(getTOString("startDate", startDate));
        strB.append(getTOString("endDate", endDate));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
//        strB.append(getTOString("txtTDSAcHead", txtTDSAcHead));
        strB.append(getTOStringEnd());
        return strB.toString();
    }
    
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
//        strB.append(getTOXmlKey(getKeyData()));
        
        strB.append(getTOXml("groupName", groupName));
        strB.append(getTOXml("schemeCount", schemeCount));
        strB.append(getTOXml("productType", productType));
        strB.append(getTOXml("interestAmountType", interestAmountType));
        strB.append(getTOXml("interestAmount", interestAmount));
        strB.append(getTOXml("penalCalculationType", penalCalculationType));
        strB.append(getTOXml("prizedPenal", prizedPenal));
        strB.append(getTOXml("nonPrizedPenal", nonPrizedPenal));
        strB.append(getTOXml("interestRecovery", interestRecovery));
        strB.append(getTOXml("startDate", startDate));
        strB.append(getTOXml("endDate", endDate));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
//        strB.append(getTOXml("txtTDSAcHead", txtTDSAcHead));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

   

    public java.util.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }

    public java.lang.String getGroupName() {
        return groupName;
    }

    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }

    public Double getInterestRecovery() {
        return interestRecovery;
    }

    public void setInterestRecovery(Double interestRecovery) {
        this.interestRecovery = interestRecovery;
    }

    public Double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(Double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public java.lang.String getInterestAmountType() {
        return interestAmountType;
    }

    public void setInterestAmountType(java.lang.String interestAmountType) {
        this.interestAmountType = interestAmountType;
    }

    public java.lang.String getInterestRecoveryType() {
        return interestRecoveryType;
    }

    public void setInterestRecoveryType(java.lang.String interestRecoveryType) {
        this.interestRecoveryType = interestRecoveryType;
    }

    public java.lang.String getPenalCalculationType() {
        return penalCalculationType;
    }

    public void setPenalCalculationType(java.lang.String penalCalculationType) {
        this.penalCalculationType = penalCalculationType;
    }

    public java.lang.String getProductType() {
        return productType;
    }

    public void setProductType(java.lang.String productType) {
        this.productType = productType;
    }

    public Integer getSchemeCount() {
        return schemeCount;
    }

    public void setSchemeCount(Integer schemeCount) {
        this.schemeCount = schemeCount;
    }

    public java.util.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public java.util.Date getStatusDt() {
        return statusDt;
    }

    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    
    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public Date getAuthorizedDate() {
        return authorizedDate;
    }

    public void setAuthorizedDate(Date authorizedDate) {
        this.authorizedDate = authorizedDate;
    }

   

    public String getAuthorizedStatus() {
        return authorizedStatus;
    }

    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getNonPrizedPenal() {
        return nonPrizedPenal;
    }

    public void setNonPrizedPenal(Double nonPrizedPenal) {
        this.nonPrizedPenal = nonPrizedPenal;
    }

    public Double getPrizedPenal() {
        return prizedPenal;
    }

    public void setPrizedPenal(Double prizedPenal) {
        this.prizedPenal = prizedPenal;
    }

    public Double getDepositAmt() {
        return depositAmt;
    }

    public void setDepositAmt(Double depositAmt) {
        this.depositAmt = depositAmt;
    }

    public Double getPrematureIntRecAmt() {
        return prematureIntRecAmt;
    }

    public void setPrematureIntRecAmt(Double prematureIntRecAmt) {
        this.prematureIntRecAmt = prematureIntRecAmt;
    }

    public String getPrematureIntRecType() {
        return prematureIntRecType;
    }

    public void setPrematureIntRecType(String prematureIntRecType) {
        this.prematureIntRecType = prematureIntRecType;
    }

    public Integer getNextGDSNo() {
        return nextGDSNo;
    }

    public void setNextGDSNo(Integer nextGDSNo) {
        this.nextGDSNo = nextGDSNo;
    }

    public Double getIntCalcRateForDue() {
        return intCalcRateForDue;
    }

    public void setIntCalcRateForDue(Double intCalcRateForDue) {
        this.intCalcRateForDue = intCalcRateForDue;
    }

    public String getIsIntCalcIfdue() {
        return isIntCalcIfdue;
    }

    public void setIsIntCalcIfdue(String isIntCalcIfdue) {
        this.isIntCalcIfdue = isIntCalcIfdue;
    }

    public String getIsIntCalcIfdueType() {
        return isIntCalcIfdueType;
    }

    public void setIsIntCalcIfdueType(String isIntCalcIfdueType) {
        this.isIntCalcIfdueType = isIntCalcIfdueType;
    }

    public String getIsIntRecovery() {
        return isIntRecovery;
    }

    public void setIsIntRecovery(String isIntRecovery) {
        this.isIntRecovery = isIntRecovery;
    }
    
}

 