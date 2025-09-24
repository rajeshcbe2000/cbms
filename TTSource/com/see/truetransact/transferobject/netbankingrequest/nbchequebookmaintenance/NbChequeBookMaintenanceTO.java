

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * NbChequeBookMaintenanceTO.java
 */

package com.see.truetransact.transferobject.netbankingrequest.nbchequebookmaintenance;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 *
 * @author Abhishek
 */
public class NbChequeBookMaintenanceTO extends TransferObject implements Serializable {

    private String custId = "";
    private String actNum = "";
    private String cbRequest = "";
    private String noOfCbLeave = "";
    private String usageType = "";
    private String status = "";
    private String statusBy = "";
    private String nbTransId = "";
    private String authBy = "";
    
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOString("custId", custId));
        strB.append(getTOString("actNum", actNum));
        strB.append(getTOString("cbRequest", cbRequest));
        strB.append(getTOString("noOfCbLeave", noOfCbLeave));
        strB.append(getTOString("usageType", usageType));
        strB.append(getTOString("status", status));
        strB.append(getTOString("statusBy", statusBy));
        strB.append(getTOString("nbTransId", nbTransId));
        strB.append(getTOString("authBy", authBy));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXml("custId", custId));
        strB.append(getTOXml("actNum", actNum));
        strB.append(getTOXml("cbRequest", cbRequest));
        strB.append(getTOXml("noOfCbLeave", noOfCbLeave));
        strB.append(getTOXml("usageType", usageType));
        strB.append(getTOXml("status", status));
        strB.append(getTOXml("statusBy", statusBy));
        strB.append(getTOXml("nbTransId", nbTransId));
        strB.append(getTOXml("authBy", authBy));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }

    public String getActNum() {
        return actNum;
    }

    public void setActNum(String actNum) {
        this.actNum = actNum;
    }

    public String getCbRequest() {
        return cbRequest;
    }

    public void setCbRequest(String cbRequest) {
        this.cbRequest = cbRequest;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getNoOfCbLeave() {
        return noOfCbLeave;
    }

    public void setNoOfCbLeave(String noOfCbLeave) {
        this.noOfCbLeave = noOfCbLeave;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
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

    public String getNbTransId() {
        return nbTransId;
    }

    public void setNbTransId(String nbTransId) {
        this.nbTransId = nbTransId;
    }

    public String getAuthBy() {
        return authBy;
    }

    public void setAuthBy(String authBy) {
        this.authBy = authBy;
    }

    
    
}
