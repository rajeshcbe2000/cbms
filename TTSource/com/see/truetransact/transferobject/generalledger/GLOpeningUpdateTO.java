/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * GLOpeningUpdateTO.java
 */

package com.see.truetransact.transferobject.generalledger;

import com.see.truetransact.transferobject.supporting.balanceupdate.*;
import com.see.truetransact.transferobject.TransferObject;
import java.io.Serializable;
import java.util.Date;
import java.util.*;

/**
 *
 * @author Administrator1
 */
public class GLOpeningUpdateTO extends TransferObject implements Serializable {

    private String acHd = "";

    public String getAcHd() {
        return acHd;
    }

    public void setAcHd(String acHd) {
        this.acHd = acHd;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getClosBal() {
        return closBal;
    }

    public void setClosBal(String closBal) {
        this.closBal = closBal;
    }

    public Date getFrmDt() {
        return frmDt;
    }

    public void setFrmDt(Date frmDt) {
        this.frmDt = frmDt;
    }

    public String getNewBal() {
        return newBal;
    }

    public void setNewBal(String newBal) {
        this.newBal = newBal;
    }

    public String getOpenBal() {
        return openBal;
    }

    public void setOpenBal(String openBal) {
        this.openBal = openBal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    private String openBal = "";
    private String closBal = "";
    private String newBal = "";
    private String status = "";
    private Date   frmDt  = null;
    private String userId = "";
    private String branch = "";
    
    

    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("acHd",acHd));
        strB.append(getTOString("openBal",openBal));
        strB.append(getTOString("closBal",closBal));
        strB.append(getTOString("newBal",newBal));
        strB.append(getTOString("status",status));
        strB.append(getTOString("frmDt",frmDt));
        strB.append(getTOString("userId",userId));
        strB.append(getTOString("branch",branch));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("acHd",acHd));
        strB.append(getTOXml("openBal",openBal));
        strB.append(getTOXml("closBal",closBal));
        strB.append(getTOXml("newBal",newBal));
        strB.append(getTOXml("status",status));
        strB.append(getTOXml("frmDt",frmDt));
        strB.append(getTOXml("userId",userId));
        strB.append(getTOXml("branch",branch));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}
