/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ExternalWireTO.java
 *
 * Created on Mon Jul 05 11:02:49 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.mdsapplication;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is PVT_EXTERNAL_WIRE.
 */
public class MDSApplicationTO extends TransferObject implements Serializable {

    private String schemeName = "";
    private Integer divisionNo = 0;
    private String chittalNo = null;
    private Integer subNo = 0;
    private Date chitStartDt = null;
    private Date chitEndDt = null;
    private Double instAmt = 0.0;
    private Integer applnNo = 0;
    private Date applnDate = null;
    private String thalayal = "";
    private String munnal = "";
    private String coChittal = "";
    private String membershipNo = null;
    private String membershipType = "";
    private String membershipName = "";
    private String houseStNo = "";
    private String area = "";
    private String city = "";
    private String state = "";
    private Double pin = null;
    private String standingInstn = "";
    private String nominee = "";
    private String remarks = "";
    private String status = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String authorizeStatus = null; //AJITH Changed from ""
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String branchCode = "";
    private String transId = "";
    private Double totalBalance = 0.0;
    private Double clearBalance = 0.0;
    private Double availableBalance = 0.0;
    private Double shadowCredit = 0.0;
    private Double shadowDebit = 0.0;
    private String commencementStatus = null; //AJITH Changed from ""
    private Date commencementDate = null;
    private String commencementAuthStatus = null; //AJITH Changed from ""
    private Date lastTransDt = null;
    private String commencementTransId = "";
    private Integer chitNo = 0;
    private String deletedUsed = "";
    private Integer instCount = 0;
    private String prodType = "";
    private String prodId = "";
    private String drAccNo = "";
    private String salaryRecovery = "";

    public String getCudt_id() {
        return cudt_id;
    }

    public void setCudt_id(String cudt_id) {
        this.cudt_id = cudt_id;
    }
    private String isTran = "";
    private String cudt_id = "";

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
        strB.append(getTOString("divisionNo", divisionNo));
        strB.append(getTOString("chittalNo", chittalNo));
        strB.append(getTOString("subNo", subNo));
        strB.append(getTOString("chitStartDt", chitStartDt));
        strB.append(getTOString("chitEndDt", chitEndDt));
        strB.append(getTOString("instAmt", instAmt));
        strB.append(getTOString("applnNo", applnNo));
        strB.append(getTOString("applnDate", applnDate));
        strB.append(getTOString("thalayal", thalayal));
        strB.append(getTOString("munnal", munnal));
        strB.append(getTOString("coChittal", coChittal));
        strB.append(getTOString("membershipNo", membershipNo));
        strB.append(getTOString("membershipType", membershipType));
        strB.append(getTOString("membershipName", membershipName));
        strB.append(getTOString("houseStNo", houseStNo));
        strB.append(getTOString("area", area));
        strB.append(getTOString("city", city));
        strB.append(getTOString("state", state));
        strB.append(getTOString("pin", pin));
        strB.append(getTOString("standingInstn", standingInstn));
        strB.append(getTOString("nominee", nominee));
        strB.append(getTOString("remarks", remarks));
        strB.append(getTOString("prodType", prodType));
        strB.append(getTOString("prodId", prodId));
        strB.append(getTOString("drAccNo", drAccNo));
        strB.append(getTOString("salaryRecovery", salaryRecovery));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("branchCode", branchCode));
        strB.append(getTOString("transId", transId));
        strB.append(getTOString("totalBalance", totalBalance));
        strB.append(getTOString("clearBalance", clearBalance));
        strB.append(getTOString("availableBalance", availableBalance));
        strB.append(getTOString("shadowCredit", shadowCredit));
        strB.append(getTOString("shadowDebit", shadowDebit));
        strB.append(getTOString("commencementStatus", commencementStatus));
        strB.append(getTOString("commencementDate", commencementDate));
        strB.append(getTOString("commencementAuthStatus", commencementAuthStatus));
        strB.append(getTOString("lastTransDt", lastTransDt));
        strB.append(getTOString("commencementTransId", commencementTransId));
        strB.append(getTOString("chitNo", chitNo));
        strB.append(getTOString("deletedUsed", deletedUsed));
        strB.append(getTOString("instCount", instCount));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOXml("schemeName", schemeName));
        strB.append(getTOXml("divisionNo", divisionNo));
        strB.append(getTOXml("chittalNo", chittalNo));
        strB.append(getTOXml("subNo", subNo));
        strB.append(getTOXml("chitStartDt", chitStartDt));
        strB.append(getTOXml("chitEndDt", chitEndDt));
        strB.append(getTOXml("instAmt", instAmt));
        strB.append(getTOXml("applnNo", applnNo));
        strB.append(getTOXml("applnDate", applnDate));
        strB.append(getTOXml("thalayal", thalayal));
        strB.append(getTOXml("munnal", munnal));
        strB.append(getTOXml("coChittal", coChittal));
        strB.append(getTOXml("membershipNo", membershipNo));
        strB.append(getTOXml("membershipType", membershipType));
        strB.append(getTOXml("membershipName", membershipName));
        strB.append(getTOXml("houseStNo", houseStNo));
        strB.append(getTOXml("area", area));
        strB.append(getTOXml("city", city));
        strB.append(getTOXml("state", state));
        strB.append(getTOXml("pin", pin));
        strB.append(getTOXml("standingInstn", standingInstn));
        strB.append(getTOXml("nominee", nominee));
        strB.append(getTOXml("remarks", remarks));
        strB.append(getTOXml("prodType", prodType));
        strB.append(getTOXml("prodId", prodId));
        strB.append(getTOXml("drAccNo", drAccNo));
        strB.append(getTOXml("salaryRecovery", salaryRecovery));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("branchCode", branchCode));
        strB.append(getTOXml("transId", transId));
        strB.append(getTOXml("totalBalance", totalBalance));
        strB.append(getTOXml("clearBalance", clearBalance));
        strB.append(getTOXml("availableBalance", availableBalance));
        strB.append(getTOXml("shadowCredit", shadowCredit));
        strB.append(getTOXml("shadowDebit", shadowDebit));
        strB.append(getTOXml("commencementStatus", commencementStatus));
        strB.append(getTOXml("lastTransDt", lastTransDt));
        strB.append(getTOXml("commencementTransId", commencementTransId));
        strB.append(getTOXml("chitNo", chitNo));
        strB.append(getTOXml("deletedUsed", deletedUsed));
        strB.append(getTOXml("instCount", instCount));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property schemeName.
     *
     * @return Value of property schemeName.
     */
    public java.lang.String getSchemeName() {
        return schemeName;
    }

    /**
     * Setter for property schemeName.
     *
     * @param schemeName New value of property schemeName.
     */
    public void setSchemeName(java.lang.String schemeName) {
        this.schemeName = schemeName;
    }

    public Integer getDivisionNo() {
        return divisionNo;
    }

    public void setDivisionNo(Integer divisionNo) {
        this.divisionNo = divisionNo;
    }

    /**
     * Getter for property chitStartDt.
     *
     * @return Value of property chitStartDt.
     */
    public java.util.Date getChitStartDt() {
        return chitStartDt;
    }

    /**
     * Setter for property chitStartDt.
     *
     * @param chitStartDt New value of property chitStartDt.
     */
    public void setChitStartDt(java.util.Date chitStartDt) {
        this.chitStartDt = chitStartDt;
    }

    /**
     * Getter for property chitEndDt.
     *
     * @return Value of property chitEndDt.
     */
    public java.util.Date getChitEndDt() {
        return chitEndDt;
    }

    /**
     * Setter for property chitEndDt.
     *
     * @param chitEndDt New value of property chitEndDt.
     */
    public void setChitEndDt(java.util.Date chitEndDt) {
        this.chitEndDt = chitEndDt;
    }

    /**
     * Getter for property instAmt.
     *
     * @return Value of property instAmt.
     */
    public java.lang.Double getInstAmt() {
        return instAmt;
    }

    /**
     * Setter for property instAmt.
     *
     * @param instAmt New value of property instAmt.
     */
    public void setInstAmt(java.lang.Double instAmt) {
        this.instAmt = instAmt;
    }

    public Integer getApplnNo() {
        return applnNo;
    }

    public void setApplnNo(Integer applnNo) {
        this.applnNo = applnNo;
    }

    /**
     * Getter for property applnDate.
     *
     * @return Value of property applnDate.
     */
    public java.util.Date getApplnDate() {
        return applnDate;
    }

    /**
     * Setter for property applnDate.
     *
     * @param applnDate New value of property applnDate.
     */
    public void setApplnDate(java.util.Date applnDate) {
        this.applnDate = applnDate;
    }

    /**
     * Getter for property thalayal.
     *
     * @return Value of property thalayal.
     */
    public java.lang.String getThalayal() {
        return thalayal;
    }

    /**
     * Setter for property thalayal.
     *
     * @param thalayal New value of property thalayal.
     */
    public void setThalayal(java.lang.String thalayal) {
        this.thalayal = thalayal;
    }

    /**
     * Getter for property munnal.
     *
     * @return Value of property munnal.
     */
    public java.lang.String getMunnal() {
        return munnal;
    }

    /**
     * Setter for property munnal.
     *
     * @param munnal New value of property munnal.
     */
    public void setMunnal(java.lang.String munnal) {
        this.munnal = munnal;
    }

    /**
     * Getter for property membershipName.
     *
     * @return Value of property membershipName.
     */
    public java.lang.String getMembershipName() {
        return membershipName;
    }

    /**
     * Setter for property membershipName.
     *
     * @param membershipName New value of property membershipName.
     */
    public void setMembershipName(java.lang.String membershipName) {
        this.membershipName = membershipName;
    }

    /**
     * Getter for property houseStNo.
     *
     * @return Value of property houseStNo.
     */
    public java.lang.String getHouseStNo() {
        return houseStNo;
    }

    /**
     * Setter for property houseStNo.
     *
     * @param houseStNo New value of property houseStNo.
     */
    public void setHouseStNo(java.lang.String houseStNo) {
        this.houseStNo = houseStNo;
    }

    /**
     * Getter for property area.
     *
     * @return Value of property area.
     */
    public java.lang.String getArea() {
        return area;
    }

    /**
     * Setter for property area.
     *
     * @param area New value of property area.
     */
    public void setArea(java.lang.String area) {
        this.area = area;
    }

    /**
     * Getter for property city.
     *
     * @return Value of property city.
     */
    public java.lang.String getCity() {
        return city;
    }

    /**
     * Setter for property city.
     *
     * @param city New value of property city.
     */
    public void setCity(java.lang.String city) {
        this.city = city;
    }

    /**
     * Getter for property state.
     *
     * @return Value of property state.
     */
    public java.lang.String getState() {
        return state;
    }

    /**
     * Setter for property state.
     *
     * @param state New value of property state.
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }

    /**
     * Getter for property pin.
     *
     * @return Value of property pin.
     */
    public java.lang.Double getPin() {
        return pin;
    }

    /**
     * Setter for property pin.
     *
     * @param pin New value of property pin.
     */
    public void setPin(java.lang.Double pin) {
        this.pin = pin;
    }

    /**
     * Getter for property standingInstn.
     *
     * @return Value of property standingInstn.
     */
    public java.lang.String getStandingInstn() {
        return standingInstn;
    }

    /**
     * Setter for property standingInstn.
     *
     * @param standingInstn New value of property standingInstn.
     */
    public void setStandingInstn(java.lang.String standingInstn) {
        this.standingInstn = standingInstn;
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
     * Getter for property membershipType.
     *
     * @return Value of property membershipType.
     */
    public java.lang.String getMembershipType() {
        return membershipType;
    }

    /**
     * Setter for property membershipType.
     *
     * @param membershipType New value of property membershipType.
     */
    public void setMembershipType(java.lang.String membershipType) {
        this.membershipType = membershipType;
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
     * Getter for property statusDt.
     *
     * @return Value of property statusDt.
     */
    public java.util.Date getStatusDt() {
        return statusDt;
    }

    /**
     * Setter for property statusDt.
     *
     * @param statusDt New value of property statusDt.
     */
    public void setStatusDt(java.util.Date statusDt) {
        this.statusDt = statusDt;
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
     * Getter for property transId.
     *
     * @return Value of property transId.
     */
    public java.lang.String getTransId() {
        return transId;
    }

    /**
     * Setter for property transId.
     *
     * @param transId New value of property transId.
     */
    public void setTransId(java.lang.String transId) {
        this.transId = transId;
    }

    /**
     * Getter for property chittalNo.
     *
     * @return Value of property chittalNo.
     */
    public java.lang.String getChittalNo() {
        return chittalNo;
    }

    /**
     * Setter for property chittalNo.
     *
     * @param chittalNo New value of property chittalNo.
     */
    public void setChittalNo(java.lang.String chittalNo) {
        this.chittalNo = chittalNo;
    }

    /**
     * Getter for property totalBalance.
     *
     * @return Value of property totalBalance.
     */
    public java.lang.Double getTotalBalance() {
        return totalBalance;
    }

    /**
     * Setter for property totalBalance.
     *
     * @param totalBalance New value of property totalBalance.
     */
    public void setTotalBalance(java.lang.Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    /**
     * Getter for property clearBalance.
     *
     * @return Value of property clearBalance.
     */
    public java.lang.Double getClearBalance() {
        return clearBalance;
    }

    /**
     * Setter for property clearBalance.
     *
     * @param clearBalance New value of property clearBalance.
     */
    public void setClearBalance(java.lang.Double clearBalance) {
        this.clearBalance = clearBalance;
    }

    /**
     * Getter for property availableBalance.
     *
     * @return Value of property availableBalance.
     */
    public java.lang.Double getAvailableBalance() {
        return availableBalance;
    }

    /**
     * Setter for property availableBalance.
     *
     * @param availableBalance New value of property availableBalance.
     */
    public void setAvailableBalance(java.lang.Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    /**
     * Getter for property shadowCredit.
     *
     * @return Value of property shadowCredit.
     */
    public java.lang.Double getShadowCredit() {
        return shadowCredit;
    }

    /**
     * Setter for property shadowCredit.
     *
     * @param shadowCredit New value of property shadowCredit.
     */
    public void setShadowCredit(java.lang.Double shadowCredit) {
        this.shadowCredit = shadowCredit;
    }

    /**
     * Getter for property shadowDebit.
     *
     * @return Value of property shadowDebit.
     */
    public java.lang.Double getShadowDebit() {
        return shadowDebit;
    }

    /**
     * Setter for property shadowDebit.
     *
     * @param shadowDebit New value of property shadowDebit.
     */
    public void setShadowDebit(java.lang.Double shadowDebit) {
        this.shadowDebit = shadowDebit;
    }

    /**
     * Getter for property commencemetStatus.
     *
     * @return Value of property commencemetStatus.
     */
    public java.lang.String getCommencementStatus() {
        return commencementStatus;
    }

    /**
     * Setter for property commencemetStatus.
     *
     * @param commencemetStatus New value of property commencemetStatus.
     */
    public void setCommencementStatus(java.lang.String commencementStatus) {
        this.commencementStatus = commencementStatus;
    }

    /**
     * Getter for property commencementDate.
     *
     * @return Value of property commencementDate.
     */
    public java.util.Date getCommencementDate() {
        return commencementDate;
    }

    /**
     * Setter for property commencementDate.
     *
     * @param commencementDate New value of property commencementDate.
     */
    public void setCommencementDate(java.util.Date commencementDate) {
        this.commencementDate = commencementDate;
    }

    /**
     * Getter for property lastTransDt.
     *
     * @return Value of property lastTransDt.
     */
    public java.util.Date getLastTransDt() {
        return lastTransDt;
    }

    /**
     * Setter for property lastTransDt.
     *
     * @param lastTransDt New value of property lastTransDt.
     */
    public void setLastTransDt(java.util.Date lastTransDt) {
        this.lastTransDt = lastTransDt;
    }

    /**
     * Getter for property commencementAuthStatus.
     *
     * @return Value of property commencementAuthStatus.
     */
    public java.lang.String getCommencementAuthStatus() {
        return commencementAuthStatus;
    }

    /**
     * Setter for property commencementAuthStatus.
     *
     * @param commencementAuthStatus New value of property
     * commencementAuthStatus.
     */
    public void setCommencementAuthStatus(java.lang.String commencementAuthStatus) {
        this.commencementAuthStatus = commencementAuthStatus;
    }

    /**
     * Getter for property membershipNo.
     *
     * @return Value of property membershipNo.
     */
    public java.lang.String getMembershipNo() {
        return membershipNo;
    }

    /**
     * Setter for property membershipNo.
     *
     * @param membershipNo New value of property membershipNo.
     */
    public void setMembershipNo(java.lang.String membershipNo) {
        this.membershipNo = membershipNo;
    }

    public Integer getChitNo() {
        return chitNo;
    }

    public void setChitNo(Integer chitNo) {
        this.chitNo = chitNo;
    }

    /**
     * Getter for property commencementTransId.
     *
     * @return Value of property commencementTransId.
     */
    public java.lang.String getCommencementTransId() {
        return commencementTransId;
    }

    /**
     * Setter for property commencementTransId.
     *
     * @param commencementTransId New value of property commencementTransId.
     */
    public void setCommencementTransId(java.lang.String commencementTransId) {
        this.commencementTransId = commencementTransId;
    }

    /**
     * Getter for property deletedUsed.
     *
     * @return Value of property deletedUsed.
     */
    public java.lang.String getDeletedUsed() {
        return deletedUsed;
    }

    /**
     * Setter for property deletedUsed.
     *
     * @param deletedUsed New value of property deletedUsed.
     */
    public void setDeletedUsed(java.lang.String deletedUsed) {
        this.deletedUsed = deletedUsed;
    }

    public Integer getInstCount() {
        return instCount;
    }

    public void setInstCount(Integer instCount) {
        this.instCount = instCount;
    }

    /**
     * Getter for property prodType.
     *
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }

    /**
     * Setter for property prodType.
     *
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
    }

    /**
     * Getter for property prodId.
     *
     * @return Value of property prodId.
     */
    public java.lang.String getProdId() {
        return prodId;
    }

    /**
     * Setter for property prodId.
     *
     * @param prodId New value of property prodId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }

    /**
     * Getter for property drAccNo.
     *
     * @return Value of property drAccNo.
     */
    public java.lang.String getDrAccNo() {
        return drAccNo;
    }

    /**
     * Setter for property drAccNo.
     *
     * @param drAccNo New value of property drAccNo.
     */
    public void setDrAccNo(java.lang.String drAccNo) {
        this.drAccNo = drAccNo;
    }

    /**
     * Getter for property coChittal.
     *
     * @return Value of property coChittal.
     */
    public java.lang.String getCoChittal() {
        return coChittal;
    }

    /**
     * Setter for property coChittal.
     *
     * @param coChittal New value of property coChittal.
     */
    public void setCoChittal(java.lang.String coChittal) {
        this.coChittal = coChittal;
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
     * Getter for property isTran.
     *
     * @return Value of property isTran.
     */
    public String getIsTran() {
        return isTran;
    }

    /**
     * Setter for property isTran.
     *
     * @param isTran New value of property isTran.
     */
    public void setIsTran(String isTran) {
        this.isTran = isTran;
    }
}