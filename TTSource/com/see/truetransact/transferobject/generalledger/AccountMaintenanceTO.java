/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountMaintenanceTO.java
 * 
 * Created on Mon Aug 22 15:30:13 GMT+05:30 2005
 */
package com.see.truetransact.transferobject.generalledger;

import com.see.truetransact.commonutil.CommonConstants;
import java.util.Date;
import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is AC_HD_PARAM.
 */
public class AccountMaintenanceTO extends TransferObject implements Serializable {

    private String acHdId = "";
    private String floatAct = "";
    private String contraAct = "";
    private String crClr = "";
    private String crTrans = "";
    private String crCash = "";
    private String drClr = "";
    private String drTrans = "";
    private String drCash = "";
    private String recons = "";
    private Date fTransDt = null;
    private Date lTransDt = null;
    private String transpost = "";
    private String postmode = "";
    private String balancetype = "";
    private Double glbalance = null;
    private Date acOpenDt = null;
    private Date acCloseDt = null;
    private String status = "";
    private String authorizeStatus = null;;
    private String authorizeBy = "";
    private Date authorizeDt = null;
    private String statusBy = "";
    private Date statusDt = null;
    private String negativeAllowed = "";
    private String hoAcct = "";
    private String reconsAcHdId = "";
    private String dayEndZeroCheck = "";
    private String chkCustomerAllow = "";
    private String cbServiceTax="";
    private String serviceTaxId = "";   // Added by nithya on 12-01-2018 for 7013

    /**
     * Setter/Getter for AC_HD_ID - table Field
     */
    public void setAcHdId(String acHdId) {
        this.acHdId = acHdId;
    }

    public String getAcHdId() {
        return acHdId;
    }

    /**
     * Setter/Getter for FLOAT_ACT - table Field
     */
    public void setFloatAct(String floatAct) {
        this.floatAct = floatAct;
    }

    public String getFloatAct() {
        return floatAct;
    }

    /**
     * Setter/Getter for CONTRA_ACT - table Field
     */
    public void setContraAct(String contraAct) {
        this.contraAct = contraAct;
    }

    public String getContraAct() {
        return contraAct;
    }

    /**
     * Setter/Getter for CR_CLR - table Field
     */
    public void setCrClr(String crClr) {
        this.crClr = crClr;
    }

    public String getCrClr() {
        return crClr;
    }

    /**
     * Setter/Getter for CR_TRANS - table Field
     */
    public void setCrTrans(String crTrans) {
        this.crTrans = crTrans;
    }

    public String getCrTrans() {
        return crTrans;
    }

    /**
     * Setter/Getter for CR_CASH - table Field
     */
    public void setCrCash(String crCash) {
        this.crCash = crCash;
    }

    public String getCrCash() {
        return crCash;
    }

    /**
     * Setter/Getter for DR_CLR - table Field
     */
    public void setDrClr(String drClr) {
        this.drClr = drClr;
    }

    public String getDrClr() {
        return drClr;
    }

    /**
     * Setter/Getter for DR_TRANS - table Field
     */
    public void setDrTrans(String drTrans) {
        this.drTrans = drTrans;
    }

    public String getDrTrans() {
        return drTrans;
    }

    /**
     * Setter/Getter for DR_CASH - table Field
     */
    public void setDrCash(String drCash) {
        this.drCash = drCash;
    }

    public String getDrCash() {
        return drCash;
    }

    /**
     * Setter/Getter for RECONS - table Field
     */
    public void setRecons(String recons) {
        this.recons = recons;
    }

    public String getRecons() {
        return recons;
    }

    /**
     * Setter/Getter for F_TRANS_DT - table Field
     */
    public void setFTransDt(Date fTransDt) {
        this.fTransDt = fTransDt;
    }

    public Date getFTransDt() {
        return fTransDt;
    }

    /**
     * Setter/Getter for L_TRANS_DT - table Field
     */
    public void setLTransDt(Date lTransDt) {
        this.lTransDt = lTransDt;
    }

    public Date getLTransDt() {
        return lTransDt;
    }

    /**
     * Setter/Getter for TRANSPOST - table Field
     */
    public void setTranspost(String transpost) {
        this.transpost = transpost;
    }

    public String getTranspost() {
        return transpost;
    }

    /**
     * Setter/Getter for POSTMODE - table Field
     */
    public void setPostmode(String postmode) {
        this.postmode = postmode;
    }

    public String getPostmode() {
        return postmode;
    }

    /**
     * Setter/Getter for BALANCETYPE - table Field
     */
    public void setBalancetype(String balancetype) {
        this.balancetype = balancetype;
    }

    public String getBalancetype() {
        return balancetype;
    }

    /**
     * Setter/Getter for GLBALANCE - table Field
     */
    public void setGlbalance(Double glbalance) {
        this.glbalance = glbalance;
    }

    public Double getGlbalance() {
        return glbalance;
    }

    /**
     * Setter/Getter for AC_OPEN_DT - table Field
     */
    public void setAcOpenDt(Date acOpenDt) {
        this.acOpenDt = acOpenDt;
    }

    public Date getAcOpenDt() {
        return acOpenDt;
    }

    /**
     * Setter/Getter for AC_CLOSE_DT - table Field
     */
    public void setAcCloseDt(Date acCloseDt) {
        this.acCloseDt = acCloseDt;
    }

    public Date getAcCloseDt() {
        return acCloseDt;
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
     * Setter/Getter for AUTHORIZE_STATUS - table Field
     */
    public void setAuthorizeStatus(String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }

    public String getAuthorizeStatus() {
        return authorizeStatus;
    }

    /**
     * Setter/Getter for AUTHORIZE_BY - table Field
     */
    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    /**
     * Setter/Getter for AUTHORIZE_DT - table Field
     */
    public void setAuthorizeDt(Date authorizeDt) {
        this.authorizeDt = authorizeDt;
    }

    public Date getAuthorizeDt() {
        return authorizeDt;
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
     * Setter/Getter for NEGATIVE_ALLOWED - table Field
     */
    public void setNegativeAllowed(String negativeAllowed) {
        this.negativeAllowed = negativeAllowed;
    }

    public String getNegativeAllowed() {
        return negativeAllowed;
    }

    /**
     * Setter/Getter for HO_ACCT - table Field
     */
    public void setHoAcct(String hoAcct) {
        this.hoAcct = hoAcct;
    }

    public String getHoAcct() {
        return hoAcct;
    }

    /**
     * Setter/Getter for RECONS_AC_HD_ID - table Field
     */
    public void setReconsAcHdId(String reconsAcHdId) {
        this.reconsAcHdId = reconsAcHdId;
    }

    public String getReconsAcHdId() {
        return reconsAcHdId;
    }

    /**
     * Setter/Getter for DAY_END_ZERO_CHECK - table Field
     */
    public void setDayEndZeroCheck(String dayEndZeroCheck) {
        this.dayEndZeroCheck = dayEndZeroCheck;
    }

    public String getDayEndZeroCheck() {
        return dayEndZeroCheck;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns(acHdId);
        return acHdId;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("acHdId", acHdId));
        strB.append(getTOString("floatAct", floatAct));
        strB.append(getTOString("contraAct", contraAct));
        strB.append(getTOString("crClr", crClr));
        strB.append(getTOString("crTrans", crTrans));
        strB.append(getTOString("crCash", crCash));
        strB.append(getTOString("drClr", drClr));
        strB.append(getTOString("drTrans", drTrans));
        strB.append(getTOString("drCash", drCash));
        strB.append(getTOString("recons", recons));
        strB.append(getTOString("fTransDt", fTransDt));
        strB.append(getTOString("lTransDt", lTransDt));
        strB.append(getTOString("transpost", transpost));
        strB.append(getTOString("postmode", postmode));
        strB.append(getTOString("balancetype", balancetype));
        strB.append(getTOString("glbalance", glbalance));
        strB.append(getTOString("acOpenDt", acOpenDt));
        strB.append(getTOString("acCloseDt", acCloseDt));
        strB.append(getTOString("status", status));
        strB.append(getTOString("authorizeStatus", authorizeStatus));
        strB.append(getTOString("authorizeBy", authorizeBy));
        strB.append(getTOString("authorizeDt", authorizeDt));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("statusDt", statusDt));
        strB.append(getTOString("negativeAllowed", negativeAllowed));
        strB.append(getTOString("hoAcct", hoAcct));
        strB.append(getTOString("reconsAcHdId", reconsAcHdId));
        strB.append(getTOString("dayEndZeroCheck", dayEndZeroCheck));
        strB.append(getTOString("chkCustomerAllow", chkCustomerAllow));
        strB.append(getTOString("cbServiceTax", cbServiceTax));
        strB.append(getTOString("serviceTaxId", serviceTaxId));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("acHdId", acHdId));
        strB.append(getTOXml("floatAct", floatAct));
        strB.append(getTOXml("contraAct", contraAct));
        strB.append(getTOXml("crClr", crClr));
        strB.append(getTOXml("crTrans", crTrans));
        strB.append(getTOXml("crCash", crCash));
        strB.append(getTOXml("drClr", drClr));
        strB.append(getTOXml("drTrans", drTrans));
        strB.append(getTOXml("drCash", drCash));
        strB.append(getTOXml("recons", recons));
        strB.append(getTOXml("fTransDt", fTransDt));
        strB.append(getTOXml("lTransDt", lTransDt));
        strB.append(getTOXml("transpost", transpost));
        strB.append(getTOXml("postmode", postmode));
        strB.append(getTOXml("balancetype", balancetype));
        strB.append(getTOXml("glbalance", glbalance));
        strB.append(getTOXml("acOpenDt", acOpenDt));
        strB.append(getTOXml("acCloseDt", acCloseDt));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("authorizeStatus", authorizeStatus));
        strB.append(getTOXml("authorizeBy", authorizeBy));
        strB.append(getTOXml("authorizeDt", authorizeDt));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("statusDt", statusDt));
        strB.append(getTOXml("negativeAllowed", negativeAllowed));
        strB.append(getTOXml("hoAcct", hoAcct));
        strB.append(getTOXml("reconsAcHdId", reconsAcHdId));
        strB.append(getTOXml("dayEndZeroCheck", dayEndZeroCheck));
        strB.append(getTOXml("chkCustomerAllow", chkCustomerAllow));
        strB.append(getTOXml("cbServiceTax", cbServiceTax));
        strB.append(getTOXml("serviceTaxId", serviceTaxId));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    /**
     * Getter for property chkCustomerAllow.
     *
     * @return Value of property chkCustomerAllow.
     */
    public java.lang.String getChkCustomerAllow() {
        return chkCustomerAllow;
    }

    /**
     * Setter for property chkCustomerAllow.
     *
     * @param chkCustomerAllow New value of property chkCustomerAllow.
     */
    public void setChkCustomerAllow(java.lang.String chkCustomerAllow) {
        this.chkCustomerAllow = chkCustomerAllow;
    }

    public String getCbServiceTax() {
        return cbServiceTax;
    }

    public void setCbServiceTax(String cbServiceTax) {
        this.cbServiceTax = cbServiceTax;
    }

    public String getServiceTaxId() {
        return serviceTaxId;
    }

    public void setServiceTaxId(String serviceTaxId) {
        this.serviceTaxId = serviceTaxId;
    }
    
    
    
}