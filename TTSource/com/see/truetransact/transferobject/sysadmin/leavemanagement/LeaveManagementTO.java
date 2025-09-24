/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveManagementTO.java
 * 
 * Created on  Mar 29 1:29:28  2010
 */
package com.see.truetransact.transferobject.sysadmin.leavemanagement;

import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is OP_AC_PRODUCT.
 */
public class LeaveManagementTO extends TransferObject implements Serializable {

    private String authorizedBy = "";
    private Date authorizedDt = null;
    private String authorizedStatus = null;
    private String createdBy = "";
    private Date createdDt = null;
    private String status = "";
    private String leaveID = "";
    private String typeOfLeave = "";
    private String desc = "";
    private String carryOver = "";
    private String txtAcc = "";
    private String cboAcc = "";
    private String cboParForLeave = "";
    private String txtFixedPar1 = "";
    private String txtFixedPar = "";
    private String txtPro1 = "";
    private String txtPro2 = "";
    private String cboParFixed = "";
    private String txtMaxLeaves = "";
    private String txtMaxEncashment = "";
    private String txtMaternityCountLimit = "";
    private String introReq = "";
    private String acc = "";
    private String encash = "";
    private String leaveEncashmentType = "";
    private String chkMaternityLeave = "";
    private String cboToBeCredited = "";
    private String command = "";
    private String statusBy = "";
    private Date statusDt = null;
    private String branch = "";
    private String slNO = "";
    private String paymentType = "";
    private Date dateOfCrediting = null;

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
     * Setter/Getter for AUTHORIZED_STATUS - table Field
     */
    public void setAuthorizedStatus(String authorizedStatus) {
        this.authorizedStatus = authorizedStatus;
    }

    public String getAuthorizedStatus() {
        return authorizedStatus;
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
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("leaveID");
        return leaveID;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizedBy", authorizedBy));
        strB.append(getTOString("authorizedDt", authorizedDt));
        strB.append(getTOString("authorizedStatus", authorizedStatus));
        strB.append(getTOString("createdBy", createdBy));
        strB.append(getTOString("createdDt", createdDt));
        strB.append(getTOString("leaveID", leaveID));
        strB.append(getTOString("typeOfLeave", typeOfLeave));
        strB.append(getTOString("desc", desc));
        strB.append(getTOString("carryOver", carryOver));
        strB.append(getTOString("txtAcc", txtAcc));
        strB.append(getTOString("cboAcc", cboAcc));
        strB.append(getTOString("txtFixedPar1", txtFixedPar1));
        strB.append(getTOString("cboParForLeave", cboParForLeave));
        strB.append(getTOString("txtFixedPar", txtFixedPar));
        strB.append(getTOString("txtPro1", txtPro1));
        strB.append(getTOString("txtPro2", txtPro2));
        strB.append(getTOString("cboParFixed", cboParFixed));
        strB.append(getTOString("txtMaxLeaves", txtMaxLeaves));
        strB.append(getTOString("txtMaxEncashment", txtMaxEncashment));
        strB.append(getTOString("txtMaternityCountLimit", txtMaternityCountLimit));
        strB.append(getTOString("introReq", introReq));
        strB.append(getTOString("acc", acc));
        strB.append(getTOString("encash", encash));
        strB.append(getTOString("leaveEncashmentType", leaveEncashmentType));
        strB.append(getTOString("chkMaternityLeave", chkMaternityLeave));
        strB.append(getTOString("cboToBeCredited", cboToBeCredited));
        strB.append(getTOString("command", command));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("branch", branch));
        strB.append(getTOString("slNO", slNO));
        strB.append(getTOString("paymentType", paymentType));
        strB.append(getTOString("dateOfCrediting", dateOfCrediting));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizedBy", authorizedBy));
        strB.append(getTOXml("authorizedDt", authorizedDt));
        strB.append(getTOXml("authorizedStatus", authorizedStatus));
        strB.append(getTOXml("createdBy", createdBy));
        strB.append(getTOXml("createdDt", createdDt));
        strB.append(getTOXml("leaveID", leaveID));
        strB.append(getTOXml("typeOfLeave", typeOfLeave));
        strB.append(getTOXml("desc", desc));
        strB.append(getTOXml("carryOver", carryOver));
        strB.append(getTOXml("txtAcc", txtAcc));
        strB.append(getTOXml("cboAcc", cboAcc));
        strB.append(getTOXml("txtFixedPar1", txtFixedPar1));
        strB.append(getTOXml("cboParForLeave", cboParForLeave));
        strB.append(getTOXml("txtFixedPar", txtFixedPar));
        strB.append(getTOXml("txtPro1", txtPro1));
        strB.append(getTOXml("txtPro2", txtPro2));
        strB.append(getTOXml("cboParFixed", cboParFixed));
        strB.append(getTOXml("txtMaxLeaves", txtMaxLeaves));
        strB.append(getTOXml("txtMaxEncashment", txtMaxEncashment));
        strB.append(getTOXml("txtMaternityCountLimit", txtMaternityCountLimit));
        strB.append(getTOXml("introReq", introReq));
        strB.append(getTOXml("acc", acc));
        strB.append(getTOXml("encash", encash));
        strB.append(getTOXml("leaveEncashmentType", leaveEncashmentType));
        strB.append(getTOXml("chkMaternityLeave", chkMaternityLeave));
        strB.append(getTOXml("cboToBeCredited", cboToBeCredited));
        strB.append(getTOXml("command", command));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("branch", branch));
        strB.append(getTOXml("slNO", slNO));
        strB.append(getTOXml("paymentType", paymentType));
        strB.append(getTOXml("dateOfCrediting", dateOfCrediting));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property leaveID.
     *
     * @return Value of property leaveID.
     */
    public java.lang.String getLeaveID() {
        return leaveID;
    }

    /**
     * Setter for property leaveID.
     *
     * @param leaveID New value of property leaveID.
     */
    public void setLeaveID(java.lang.String leaveID) {
        this.leaveID = leaveID;
    }

    /**
     * Getter for property typeOfLeave.
     *
     * @return Value of property typeOfLeave.
     */
    public java.lang.String getTypeOfLeave() {
        return typeOfLeave;
    }

    /**
     * Setter for property typeOfLeave.
     *
     * @param typeOfLeave New value of property typeOfLeave.
     */
    public void setTypeOfLeave(java.lang.String typeOfLeave) {
        this.typeOfLeave = typeOfLeave;
    }

    /**
     * Getter for property desc.
     *
     * @return Value of property desc.
     */
    public java.lang.String getDesc() {
        return desc;
    }

    /**
     * Setter for property desc.
     *
     * @param desc New value of property desc.
     */
    public void setDesc(java.lang.String desc) {
        this.desc = desc;
    }

    /**
     * Getter for property carryOver.
     *
     * @return Value of property carryOver.
     */
    public java.lang.String getCarryOver() {
        return carryOver;
    }

    /**
     * Setter for property carryOver.
     *
     * @param carryOver New value of property carryOver.
     */
    public void setCarryOver(java.lang.String carryOver) {
        this.carryOver = carryOver;
    }

    /**
     * Getter for property txtAcc.
     *
     * @return Value of property txtAcc.
     */
    public java.lang.String getTxtAcc() {
        return txtAcc;
    }

    /**
     * Setter for property txtAcc.
     *
     * @param txtAcc New value of property txtAcc.
     */
    public void setTxtAcc(java.lang.String txtAcc) {
        this.txtAcc = txtAcc;
    }

    /**
     * Getter for property cboAcc.
     *
     * @return Value of property cboAcc.
     */
    public java.lang.String getCboAcc() {
        return cboAcc;
    }

    /**
     * Setter for property cboAcc.
     *
     * @param cboAcc New value of property cboAcc.
     */
    public void setCboAcc(java.lang.String cboAcc) {
        this.cboAcc = cboAcc;
    }

    /**
     * Getter for property cboParForLeave.
     *
     * @return Value of property cboParForLeave.
     */
    public java.lang.String getCboParForLeave() {
        return cboParForLeave;
    }

    /**
     * Setter for property cboParForLeave.
     *
     * @param cboParForLeave New value of property cboParForLeave.
     */
    public void setCboParForLeave(java.lang.String cboParForLeave) {
        this.cboParForLeave = cboParForLeave;
    }

    /**
     * Getter for property txtFixedPar1.
     *
     * @return Value of property txtFixedPar1.
     */
    public java.lang.String getTxtFixedPar1() {
        return txtFixedPar1;
    }

    /**
     * Setter for property txtFixedPar1.
     *
     * @param txtFixedPar1 New value of property txtFixedPar1.
     */
    public void setTxtFixedPar1(java.lang.String txtFixedPar1) {
        this.txtFixedPar1 = txtFixedPar1;
    }

    /**
     * Getter for property txtFixedPar.
     *
     * @return Value of property txtFixedPar.
     */
    public java.lang.String getTxtFixedPar() {
        return txtFixedPar;
    }

    /**
     * Setter for property txtFixedPar.
     *
     * @param txtFixedPar New value of property txtFixedPar.
     */
    public void setTxtFixedPar(java.lang.String txtFixedPar) {
        this.txtFixedPar = txtFixedPar;
    }

    /**
     * Getter for property txtPro1.
     *
     * @return Value of property txtPro1.
     */
    public java.lang.String getTxtPro1() {
        return txtPro1;
    }

    /**
     * Setter for property txtPro1.
     *
     * @param txtPro1 New value of property txtPro1.
     */
    public void setTxtPro1(java.lang.String txtPro1) {
        this.txtPro1 = txtPro1;
    }

    /**
     * Getter for property txtPro2.
     *
     * @return Value of property txtPro2.
     */
    public java.lang.String getTxtPro2() {
        return txtPro2;
    }

    /**
     * Setter for property txtPro2.
     *
     * @param txtPro2 New value of property txtPro2.
     */
    public void setTxtPro2(java.lang.String txtPro2) {
        this.txtPro2 = txtPro2;
    }

    /**
     * Getter for property cboParFixed.
     *
     * @return Value of property cboParFixed.
     */
    public java.lang.String getCboParFixed() {
        return cboParFixed;
    }

    /**
     * Setter for property cboParFixed.
     *
     * @param cboParFixed New value of property cboParFixed.
     */
    public void setCboParFixed(java.lang.String cboParFixed) {
        this.cboParFixed = cboParFixed;
    }

    /**
     * Getter for property txtMaxLeaves.
     *
     * @return Value of property txtMaxLeaves.
     */
    public java.lang.String getTxtMaxLeaves() {
        return txtMaxLeaves;
    }

    /**
     * Setter for property txtMaxLeaves.
     *
     * @param txtMaxLeaves New value of property txtMaxLeaves.
     */
    public void setTxtMaxLeaves(java.lang.String txtMaxLeaves) {
        this.txtMaxLeaves = txtMaxLeaves;
    }

    /**
     * Getter for property txtMaxEncashment.
     *
     * @return Value of property txtMaxEncashment.
     */
    public java.lang.String getTxtMaxEncashment() {
        return txtMaxEncashment;
    }

    /**
     * Setter for property txtMaxEncashment.
     *
     * @param txtMaxEncashment New value of property txtMaxEncashment.
     */
    public void setTxtMaxEncashment(java.lang.String txtMaxEncashment) {
        this.txtMaxEncashment = txtMaxEncashment;
    }

    /**
     * Getter for property introReq.
     *
     * @return Value of property introReq.
     */
    public java.lang.String getIntroReq() {
        return introReq;
    }

    /**
     * Setter for property introReq.
     *
     * @param introReq New value of property introReq.
     */
    public void setIntroReq(java.lang.String introReq) {
        this.introReq = introReq;
    }

    /**
     * Getter for property acc.
     *
     * @return Value of property acc.
     */
    public java.lang.String getAcc() {
        return acc;
    }

    /**
     * Setter for property acc.
     *
     * @param acc New value of property acc.
     */
    public void setAcc(java.lang.String acc) {
        this.acc = acc;
    }

    /**
     * Getter for property encash.
     *
     * @return Value of property encash.
     */
    public java.lang.String getEncash() {
        return encash;
    }

    /**
     * Setter for property encash.
     *
     * @param encash New value of property encash.
     */
    public void setEncash(java.lang.String encash) {
        this.encash = encash;
    }

    /**
     * Getter for property leaveEncashmentType.
     *
     * @return Value of property leaveEncashmentType.
     */
    public java.lang.String getLeaveEncashmentType() {
        return leaveEncashmentType;
    }

    /**
     * Setter for property leaveEncashmentType.
     *
     * @param leaveEncashmentType New value of property leaveEncashmentType.
     */
    public void setLeaveEncashmentType(java.lang.String leaveEncashmentType) {
        this.leaveEncashmentType = leaveEncashmentType;
    }

    /**
     * Getter for property cboToBeCredited.
     *
     * @return Value of property cboToBeCredited.
     */
    public java.lang.String getCboToBeCredited() {
        return cboToBeCredited;
    }

    /**
     * Setter for property cboToBeCredited.
     *
     * @param cboToBeCredited New value of property cboToBeCredited.
     */
    public void setCboToBeCredited(java.lang.String cboToBeCredited) {
        this.cboToBeCredited = cboToBeCredited;
    }

    /**
     * Getter for property command.
     *
     * @return Value of property command.
     */
    public java.lang.String getCommand() {
        return command;
    }

    /**
     * Setter for property command.
     *
     * @param command New value of property command.
     */
    public void setCommand(java.lang.String command) {
        this.command = command;
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
     * Getter for property branch.
     *
     * @return Value of property branch.
     */
    public java.lang.String getBranch() {
        return branch;
    }

    /**
     * Setter for property branch.
     *
     * @param branch New value of property branch.
     */
    public void setBranch(java.lang.String branch) {
        this.branch = branch;
    }

    /**
     * Getter for property slNO.
     *
     * @return Value of property slNO.
     */
    public java.lang.String getSlNO() {
        return slNO;
    }

    /**
     * Setter for property slNO.
     *
     * @param slNO New value of property slNO.
     */
    public void setSlNO(java.lang.String slNO) {
        this.slNO = slNO;
    }

    /**
     * Getter for property paymentType.
     *
     * @return Value of property paymentType.
     */
    public java.lang.String getPaymentType() {
        return paymentType;
    }

    /**
     * Setter for property paymentType.
     *
     * @param paymentType New value of property paymentType.
     */
    public void setPaymentType(java.lang.String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * Getter for property dateOfCrediting.
     *
     * @return Value of property dateOfCrediting.
     */
    public java.util.Date getDateOfCrediting() {
        return dateOfCrediting;
    }

    /**
     * Setter for property dateOfCrediting.
     *
     * @param dateOfCrediting New value of property dateOfCrediting.
     */
    public void setDateOfCrediting(java.util.Date dateOfCrediting) {
        this.dateOfCrediting = dateOfCrediting;
    }

    /**
     * Getter for property chkMaternityLeave.
     *
     * @return Value of property chkMaternityLeave.
     */
    public java.lang.String getChkMaternityLeave() {
        return chkMaternityLeave;
    }

    /**
     * Setter for property chkMaternityLeave.
     *
     * @param chkMaternityLeave New value of property chkMaternityLeave.
     */
    public void setChkMaternityLeave(java.lang.String chkMaternityLeave) {
        this.chkMaternityLeave = chkMaternityLeave;
    }

    /**
     * Getter for property txtMaternityCountLimit.
     *
     * @return Value of property txtMaternityCountLimit.
     */
    public java.lang.String getTxtMaternityCountLimit() {
        return txtMaternityCountLimit;
    }

    /**
     * Setter for property txtMaternityCountLimit.
     *
     * @param txtMaternityCountLimit New value of property
     * txtMaternityCountLimit.
     */
    public void setTxtMaternityCountLimit(java.lang.String txtMaternityCountLimit) {
        this.txtMaternityCountLimit = txtMaternityCountLimit;
    }
}