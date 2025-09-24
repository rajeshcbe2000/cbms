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
package com.see.truetransact.transferobject.roomrent.rent;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.*;

/**
 * Table name for this TO is TOKEN_CONFIG.
 */
public class RentTO extends TransferObject implements Serializable {

    private String rmNumber = "", buildingNo = "", buildingDes = "",
            rentAccHead = "", penelAccHead = "", noticeAccHead = "", legalAccHead = "", arbAccHead = "", courtGrpHead = "", exeGrpHead = "", rStatus = "",
            roomNo = "", versNo = "", rentFeq = "", rentDetailsId = "",advHead = "", branchId = "";
    Double rentAmt, penelRate;
    Date effDate;
    private String authorizeStatus = null;
    private String authorizeBy = null;
    private Date authorizeDte = null;
    private String status = null;
    private String status_by = null;
    private Vector dataV = null;

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        //setKeyColumns(borrowingNo);
        return rmNumber;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("rmNumber", rmNumber));
        strB.append(getTOString("buildingNo", buildingNo));
        strB.append(getTOString("buildingDes", buildingDes));
        strB.append(getTOString("rentAccHead", rentAccHead));
        strB.append(getTOString("penelAccHead", penelAccHead));
        strB.append(getTOString("noticeAccHead", noticeAccHead));
        strB.append(getTOString("legalAccHead", legalAccHead));
        strB.append(getTOString("arbAccHead", arbAccHead));
        strB.append(getTOString("courtGrpHead", courtGrpHead));
        strB.append(getTOString("exeGrpHead", exeGrpHead));
        strB.append(getTOString("rStatus", rStatus));
        strB.append(getTOString("roomNo", roomNo));
        strB.append(getTOString("versNo", versNo));
        strB.append(getTOString("rentFeq", rentFeq));
        strB.append(getTOString("rentAmt", rentAmt));
        strB.append(getTOString("penelRate", penelRate));
        strB.append(getTOString("effDate", effDate));

        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDte", authorizeDte));
        strB.append(getTOString("status", status));
        strB.append(getTOString("advHead",advHead));
        strB.append(getTOString("branchId",branchId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("rmNumber", rmNumber));
        strB.append(getTOXml("buildingNo", buildingNo));
        strB.append(getTOXml("buildingDes", buildingDes));
        strB.append(getTOXml("rentAccHead", rentAccHead));
        strB.append(getTOXml("penelAccHead", penelAccHead));
        strB.append(getTOXml("noticeAccHead", noticeAccHead));
        strB.append(getTOXml("legalAccHead", legalAccHead));
        strB.append(getTOXml("arbAccHead", arbAccHead));
        strB.append(getTOXml("courtGrpHead", courtGrpHead));
        strB.append(getTOXml("exeGrpHead", exeGrpHead));
        strB.append(getTOXml("rStatus", rStatus));
        strB.append(getTOXml("roomNo", roomNo));
        strB.append(getTOXml("versNo", versNo));
        strB.append(getTOXml("rentFeq", rentFeq));
        strB.append(getTOXml("rentAmt", rentAmt));
        strB.append(getTOXml("penelRate", penelRate));
        strB.append(getTOXml("effDate", effDate));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDte", authorizeDte));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("advHead", advHead));
        strB.append(getTOXml("branchId", branchId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     *
     * /**
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
     * Getter for property buildingDes.
     *
     * @return Value of property buildingDes.
     */
    public java.lang.String getBuildingDes() {
        return buildingDes;
    }

    /**
     * Setter for property buildingDes.
     *
     * @param buildingDes New value of property buildingDes.
     */
    public void setBuildingDes(java.lang.String buildingDes) {
        this.buildingDes = buildingDes;
    }

    /**
     * Getter for property rentAccHead.
     *
     * @return Value of property rentAccHead.
     */
    public java.lang.String getRentAccHead() {
        return rentAccHead;
    }

    /**
     * Setter for property rentAccHead.
     *
     * @param rentAccHead New value of property rentAccHead.
     */
    public void setRentAccHead(java.lang.String rentAccHead) {
        this.rentAccHead = rentAccHead;
    }

    /**
     * Getter for property penelAccHead.
     *
     * @return Value of property penelAccHead.
     */
    public java.lang.String getPenelAccHead() {
        return penelAccHead;
    }

    /**
     * Setter for property penelAccHead.
     *
     * @param penelAccHead New value of property penelAccHead.
     */
    public void setPenelAccHead(java.lang.String penelAccHead) {
        this.penelAccHead = penelAccHead;
    }

    /**
     * Getter for property noticeAccHead.
     *
     * @return Value of property noticeAccHead.
     */
    public java.lang.String getNoticeAccHead() {
        return noticeAccHead;
    }

    /**
     * Setter for property noticeAccHead.
     *
     * @param noticeAccHead New value of property noticeAccHead.
     */
    public void setNoticeAccHead(java.lang.String noticeAccHead) {
        this.noticeAccHead = noticeAccHead;
    }

    /**
     * Getter for property legalAccHead.
     *
     * @return Value of property legalAccHead.
     */
    public java.lang.String getLegalAccHead() {
        return legalAccHead;
    }

    /**
     * Setter for property legalAccHead.
     *
     * @param legalAccHead New value of property legalAccHead.
     */
    public void setLegalAccHead(java.lang.String legalAccHead) {
        this.legalAccHead = legalAccHead;
    }

    /**
     * Getter for property arbAccHead.
     *
     * @return Value of property arbAccHead.
     */
    public java.lang.String getArbAccHead() {
        return arbAccHead;
    }

    /**
     * Setter for property arbAccHead.
     *
     * @param arbAccHead New value of property arbAccHead.
     */
    public void setArbAccHead(java.lang.String arbAccHead) {
        this.arbAccHead = arbAccHead;
    }

    /**
     * Getter for property courtGrpHead.
     *
     * @return Value of property courtGrpHead.
     */
    public java.lang.String getCourtGrpHead() {
        return courtGrpHead;
    }

    /**
     * Setter for property courtGrpHead.
     *
     * @param courtGrpHead New value of property courtGrpHead.
     */
    public void setCourtGrpHead(java.lang.String courtGrpHead) {
        this.courtGrpHead = courtGrpHead;
    }

    /**
     * Getter for property exeGrpHead.
     *
     * @return Value of property exeGrpHead.
     */
    public java.lang.String getExeGrpHead() {
        return exeGrpHead;
    }

    /**
     * Setter for property exeGrpHead.
     *
     * @param exeGrpHead New value of property exeGrpHead.
     */
    public void setExeGrpHead(java.lang.String exeGrpHead) {
        this.exeGrpHead = exeGrpHead;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    /**
     * Getter for property rStatus.
     *
     * @return Value of property rStatus.
     */
    public java.lang.String getRStatus() {
        return rStatus;
    }

    /**
     * Setter for property rStatus.
     *
     * @param rStatus New value of property rStatus.
     */
    public void setRStatus(java.lang.String rStatus) {
        this.rStatus = rStatus;
    }

    /**
     * Getter for property roomNo1.
     *
     * @return Value of property roomNo1.
     */
    public java.lang.String getRoomNo() {
        return roomNo;
    }

    /**
     * Setter for property roomNo1.
     *
     * @param roomNo1 New value of property roomNo1.
     */
    public void setRoomNo(java.lang.String roomNo1) {
        this.roomNo = roomNo1;
    }

    /**
     * Getter for property versNo.
     *
     * @return Value of property versNo.
     */
    public java.lang.String getVersNo() {
        return versNo;
    }

    /**
     * Setter for property versNo.
     *
     * @param versNo New value of property versNo.
     */
    public void setVersNo(java.lang.String versNo) {
        this.versNo = versNo;
    }

    /**
     * Getter for property rentFeq.
     *
     * @return Value of property rentFeq.
     */
    public java.lang.String getRentFeq() {
        return rentFeq;
    }

    /**
     * Setter for property rentFeq.
     *
     * @param rentFeq New value of property rentFeq.
     */
    public void setRentFeq(java.lang.String rentFeq) {
        this.rentFeq = rentFeq;
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
     * Getter for property penelRate.
     *
     * @return Value of property penelRate.
     */
    public java.lang.Double getPenelRate() {
        return penelRate;
    }

    /**
     * Setter for property penelRate.
     *
     * @param penelRate New value of property penelRate.
     */
    public void setPenelRate(java.lang.Double penelRate) {
        this.penelRate = penelRate;
    }

    /**
     * Getter for property effDate.
     *
     * @return Value of property effDate.
     */
    public java.util.Date getEffDate() {
        return effDate;
    }

    /**
     * Setter for property effDate.
     *
     * @param effDate New value of property effDate.
     */
    public void setEffDate(java.util.Date effDate) {
        this.effDate = effDate;
    }

    /**
     * Getter for property rmNumber.
     *
     * @return Value of property rmNumber.
     */
    public java.lang.String getRmNumber() {
        return rmNumber;
    }

    /**
     * Setter for property rmNumber.
     *
     * @param rmNumber New value of property rmNumber.
     */
    public void setRmNumber(java.lang.String rmNumber) {
        this.rmNumber = rmNumber;
    }

    /**
     * Getter for property dataV.
     *
     * @return Value of property dataV.
     */
    public Vector getDataV() {
        return dataV;
    }

    /**
     * Setter for property dataV.
     *
     * @param dataV New value of property dataV.
     */
    public void setDataV(Vector dataV) {
        this.dataV = dataV;
    }

    /**
     * Getter for property rentDetailsId.
     *
     * @return Value of property rentDetailsId.
     */
    public java.lang.String getRentDetailsId() {
        return rentDetailsId;
    }

    /**
     * Setter for property rentDetailsId.
     *
     * @param rentDetailsId New value of property rentDetailsId.
     */
    public void setRentDetailsId(java.lang.String rentDetailsId) {
        this.rentDetailsId = rentDetailsId;
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
     * Getter for property status_by.
     *
     * @return Value of property status_by.
     */
    public String getStatus_by() {
        return status_by;
    }

    /**
     * Setter for property status_by.
     *
     * @param status_by New value of property status_by.
     */
    public void setStatus_by(String status_by) {
        this.status_by = status_by;
    }
    public String getAdvHead() {
        return advHead;
    }
    public void setAdvHead(String advHead) {
        this.advHead = advHead;
    }
    /**
     * Getter for property depoId.
     *
     * @return Value of property depoId.
     */
}