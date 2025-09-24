/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigTO.java
 * 
 * Created on Thu Jan 20 16:44:08 IST 2005
 */
package com.see.truetransact.transferobject.roomrent.renttrans;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class RentTransTO extends TransferObject implements Serializable {

    private String buildingNo = "", roomNo = "", rrId = "", closure = "", roomStatus = "", rmNumber = "" ,defaulter = "" ,accNumber = "",accStatus= "";
    private Date rentDate, rentPFrm, rentPto, closedDate, rentCDate;
    private Double dueAmt, rentAmt, penelAmt, noticeAmt, legalAmt, arbAmt, courtAmt, exeAmt, rentTotal;
    private String authorizeStatus = null;
    private String authorizeBy = null;
    private Date authorizeDte = null;
    private String status = null,statusBy = "";
    private Date transDate = null, statusDate = null;

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(rrId);
        return rrId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("buildingNo", buildingNo));
        strB.append(getTOString("roomNo", roomNo));
        strB.append(getTOString("rrId", rrId));
        strB.append(getTOString("closure", closure));
        strB.append(getTOString("rentDate", rentDate));
        strB.append(getTOString("transDate", transDate));
        strB.append(getTOString("rentPFrm", rentPFrm));
        strB.append(getTOString("rentPto", rentPto));
        strB.append(getTOString("closedDate", closedDate));
        strB.append(getTOString("dueAmt", dueAmt));
        strB.append(getTOString("rentAmt", rentAmt));
        strB.append(getTOString("penelAmt", penelAmt));
        strB.append(getTOString("noticeAmt", noticeAmt));
        strB.append(getTOString("legalAmt", legalAmt));
        strB.append(getTOString("arbAmt", arbAmt));
        strB.append(getTOString("courtAmt", courtAmt));
        strB.append(getTOString("exeAmt", exeAmt));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDte", authorizeDte));
        strB.append(getTOString("status", status));
        strB.append(getTOString("rentTotal", rentTotal));
        strB.append(getTOString("rentCDate", rentCDate));
        strB.append(getTOString("roomStatus", roomStatus));
        strB.append(getTOString("rmNumber", rmNumber));
        strB.append(getTOString("accNumber", accNumber));
        strB.append(getTOString("accStatus", accStatus));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDate", statusDate));
        strB.append(getTOStringEnd());                            
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("buildingNo", buildingNo));
        strB.append(getTOXml("roomNo", roomNo));
        strB.append(getTOXml("rrId", rrId));
        strB.append(getTOXml("closure", closure));
        strB.append(getTOXml("rentDate", rentDate));
        strB.append(getTOXml("transDate", transDate));
        strB.append(getTOXml("rentPFrm", rentPFrm));
        strB.append(getTOXml("rentPto", rentPto));
        strB.append(getTOXml("closedDate", closedDate));
        strB.append(getTOXml("dueAmt", dueAmt));
        strB.append(getTOXml("rentAmt", rentAmt));
        strB.append(getTOXml("penelAmt", penelAmt));
        strB.append(getTOXml("noticeAmt", noticeAmt));
        strB.append(getTOXml("legalAmt", legalAmt));
        strB.append(getTOXml("arbAmt", arbAmt));
        strB.append(getTOXml("courtAmt", courtAmt));
        strB.append(getTOXml("exeAmt", exeAmt));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDte", authorizeDte));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("rentTotal", rentTotal));
        strB.append(getTOXml("rentCDate", rentCDate));
        strB.append(getTOXml("roomStatus", roomStatus));
        strB.append(getTOXml("rmNumber", rmNumber)); 
        strB.append(getTOXml("accNumber", accNumber)); 
        strB.append(getTOXml("accStatus", accStatus));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDate", statusDate)); 
        strB.append(getTOXmlEnd());
        return strB.toString();
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
     * Getter for property authorizeDte.
     *
     * @return Value of property authorizeDte.
     */
    public java.util.Date getAuthorizeDte() {
        return authorizeDte;
    }

    /**
     * Setter for property authorizeDte.
     *
     * @param authorizeDte New value of property authorizeDte.
     */
    public void setAuthorizeDte(java.util.Date authorizeDte) {
        this.authorizeDte = authorizeDte;
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
     * Getter for property buildingNo.
     *
     * @return Value of property buildingNo.
     */
    public java.lang.String getBuildingNo() {
        return buildingNo;
    }

    /**
     * Setter for property buildingNo.
     *
     * @param buildingNo New value of property buildingNo.
     */
    public void setBuildingNo(java.lang.String buildingNo) {
        this.buildingNo = buildingNo;
    }

    /**
     * Getter for property roomNo.
     *
     * @return Value of property roomNo.
     */
    public java.lang.String getRoomNo() {
        return roomNo;
    }

    /**
     * Setter for property roomNo.
     *
     * @param roomNo New value of property roomNo.
     */
    public void setRoomNo(java.lang.String roomNo) {
        this.roomNo = roomNo;
    }

    /**
     * Getter for property rrId.
     *
     * @return Value of property rrId.
     */
    public java.lang.String getRrId() {
        return rrId;
    }

    /**
     * Setter for property rrId.
     *
     * @param rrId New value of property rrId.
     */
    public void setRrId(java.lang.String rrId) {
        this.rrId = rrId;
    }

    /**
     * Getter for property closure.
     *
     * @return Value of property closure.
     */
    public java.lang.String getClosure() {
        return closure;
    }

    /**
     * Setter for property closure.
     *
     * @param closure New value of property closure.
     */
    public void setClosure(java.lang.String closure) {
        this.closure = closure;
    }

    /**
     * Getter for property rentDate.
     *
     * @return Value of property rentDate.
     */
    public java.util.Date getRentDate() {
        return rentDate;
    }

    /**
     * Setter for property rentDate.
     *
     * @param rentDate New value of property rentDate.
     */
    public void setRentDate(java.util.Date rentDate) {
        this.rentDate = rentDate;
    }

    /**
     * Getter for property transDate.
     *
     * @return Value of property transDate.
     */
    /**
     * Setter for property transDate.
     *
     * @param transDate New value of property transDate.
     */
    /**
     * Getter for property rentPFrm.
     *
     * @return Value of property rentPFrm.
     */
    public java.util.Date getRentPFrm() {
        return rentPFrm;
    }

    /**
     * Setter for property rentPFrm.
     *
     * @param rentPFrm New value of property rentPFrm.
     */
    public void setRentPFrm(java.util.Date rentPFrm) {
        this.rentPFrm = rentPFrm;
    }

    /**
     * Getter for property rentPto.
     *
     * @return Value of property rentPto.
     */
    public java.util.Date getRentPto() {
        return rentPto;
    }

    /**
     * Setter for property rentPto.
     *
     * @param rentPto New value of property rentPto.
     */
    public void setRentPto(java.util.Date rentPto) {
        this.rentPto = rentPto;
    }

    /**
     * Getter for property closedDate.
     *
     * @return Value of property closedDate.
     */
    public java.util.Date getClosedDate() {
        return closedDate;
    }

    /**
     * Setter for property closedDate.
     *
     * @param closedDate New value of property closedDate.
     */
    public void setClosedDate(java.util.Date closedDate) {
        this.closedDate = closedDate;
    }

    /**
     * Getter for property dueAmt.
     *
     * @return Value of property dueAmt.
     */
    public java.lang.Double getDueAmt() {
        return dueAmt;
    }

    /**
     * Setter for property dueAmt.
     *
     * @param dueAmt New value of property dueAmt.
     */
    public void setDueAmt(java.lang.Double dueAmt) {
        this.dueAmt = dueAmt;
    }

    /**
     * Getter for property rentAmt.
     *
     * @return Value of property rentAmt.
     */
    public java.lang.Double getRentAmt() {
        return rentAmt;
    }

    /**
     * Setter for property rentAmt.
     *
     * @param rentAmt New value of property rentAmt.
     */
    public void setRentAmt(java.lang.Double rentAmt) {
        this.rentAmt = rentAmt;
    }

    /**
     * Getter for property penelAmt.
     *
     * @return Value of property penelAmt.
     */
    public java.lang.Double getPenelAmt() {
        return penelAmt;
    }

    /**
     * Setter for property penelAmt.
     *
     * @param penelAmt New value of property penelAmt.
     */
    public void setPenelAmt(java.lang.Double penelAmt) {
        this.penelAmt = penelAmt;
    }

    /**
     * Getter for property noticeAmt.
     *
     * @return Value of property noticeAmt.
     */
    public java.lang.Double getNoticeAmt() {
        return noticeAmt;
    }

    /**
     * Setter for property noticeAmt.
     *
     * @param noticeAmt New value of property noticeAmt.
     */
    public void setNoticeAmt(java.lang.Double noticeAmt) {
        this.noticeAmt = noticeAmt;
    }

    /**
     * Getter for property legalAmt.
     *
     * @return Value of property legalAmt.
     */
    public java.lang.Double getLegalAmt() {
        return legalAmt;
    }

    /**
     * Setter for property legalAmt.
     *
     * @param legalAmt New value of property legalAmt.
     */
    public void setLegalAmt(java.lang.Double legalAmt) {
        this.legalAmt = legalAmt;
    }

    /**
     * Getter for property arbAmt.
     *
     * @return Value of property arbAmt.
     */
    public java.lang.Double getArbAmt() {
        return arbAmt;
    }

    /**
     * Setter for property arbAmt.
     *
     * @param arbAmt New value of property arbAmt.
     */
    public void setArbAmt(java.lang.Double arbAmt) {
        this.arbAmt = arbAmt;
    }

    /**
     * Getter for property courtAmt.
     *
     * @return Value of property courtAmt.
     */
    public java.lang.Double getCourtAmt() {
        return courtAmt;
    }

    /**
     * Setter for property courtAmt.
     *
     * @param courtAmt New value of property courtAmt.
     */
    public void setCourtAmt(java.lang.Double courtAmt) {
        this.courtAmt = courtAmt;
    }

    /**
     * Getter for property exeAmt.
     *
     * @return Value of property exeAmt.
     */
    public java.lang.Double getExeAmt() {
        return exeAmt;
    }

    /**
     * Setter for property exeAmt.
     *
     * @param exeAmt New value of property exeAmt.
     */
    public void setExeAmt(java.lang.Double exeAmt) {
        this.exeAmt = exeAmt;
    }

    /**
     * Getter for property rentTotal.
     *
     * @return Value of property rentTotal.
     */
    public java.lang.Double getRentTotal() {
        return rentTotal;
    }

    /**
     * Setter for property rentTotal.
     *
     * @param rentTotal New value of property rentTotal.
     */
    public void setRentTotal(java.lang.Double rentTotal) {
        this.rentTotal = rentTotal;
    }

    /**
     * Getter for property rentCDate.
     *
     * @return Value of property rentCDate.
     */
    public java.util.Date getRentCDate() {
        return rentCDate;
    }

    /**
     * Setter for property rentCDate.
     *
     * @param rentCDate New value of property rentCDate.
     */
    public void setRentCDate(java.util.Date rentCDate) {
        this.rentCDate = rentCDate;
    }

    /**
     * Getter for property roomStatus.
     *
     * @return Value of property roomStatus.
     */
    /**
     * Getter for property rmNumber.
     *
     * @return Value of property rmNumber.
     */
    public String getRmNumber() {
        return rmNumber;
    }

    /**
     * Setter for property rmNumber.
     *
     * @param rmNumber New value of property rmNumber.
     */
    public void setRmNumber(String rmNumber) {
        this.rmNumber = rmNumber;
    }

    /**
     * Getter for property roomStatus.
     *
     * @return Value of property roomStatus.
     */
    public java.lang.String getRoomStatus() {
        return roomStatus;
    }

    /**
     * Setter for property roomStatus.
     *
     * @param roomStatus New value of property roomStatus.
     */
    public void setRoomStatus(java.lang.String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getDefaulter() {
        return defaulter;
    }

    public void setDefaulter(String defaulter) {
        this.defaulter = defaulter;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    public String getStatusBy() {
        return statusBy;
    }

    public void setStatusBy(String statusBy) {
        this.statusBy = statusBy;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }


  
    /**
     * Getter for property avalbal.
     *
     * @return Value of property avalbal.
     */
    
}