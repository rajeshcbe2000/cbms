/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IntroIdentityTO.java
 *
 * Created on Tue Apr 13 14:47:41 GMT+05:30 2004
 */
package com.see.truetransact.transferobject.deposit;

import java.io.Serializable;
import com.see.truetransact.transferobject.TransferObject;

/**
 * Table name for this TO is DEPOSIT_INTRO_PROOF.
 */
public class IntroIdentityTO extends TransferObject implements Serializable {

    private String depositNo = "";
    private String proofTypeId = "";
    private String proofNum = "";
    private String issueAuth = "";

    /**
     * Setter/Getter for DEPOSIT_NO - table Field
     */
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    public String getDepositNo() {
        return depositNo;
    }

    /**
     * Setter/Getter for PROOF_TYPE_ID - table Field
     */
    public void setProofTypeId(String proofTypeId) {
        this.proofTypeId = proofTypeId;
    }

    public String getProofTypeId() {
        return proofTypeId;
    }

    /**
     * Setter/Getter for PROOF_NUM - table Field
     */
    public void setProofNum(String proofNum) {
        this.proofNum = proofNum;
    }

    public String getProofNum() {
        return proofNum;
    }

    /**
     * Setter/Getter for ISSUE_AUTH - table Field
     */
    public void setIssueAuth(String issueAuth) {
        this.issueAuth = issueAuth;
    }

    public String getIssueAuth() {
        return issueAuth;
    }

    /**
     * getKeyData returns the Primary Key Columns for this TO User needs to add
     * the Key columns as a setter Example : setKeyColumns("col1" +
     * KEY_VAL_SEPARATOR + "col2"); return col1 + KEY_VAL_SEPARATOR + col2;
     */
    public String getKeyData() {
        setKeyColumns("depositNo");
        return depositNo;
    }

    /**
     * toString method which returns this TO as a String.
     */
    public String toString() {
        StringBuffer strB = new StringBuffer(getTOStringStart(this.getClass().getName()));
        strB.append(getTOStringKey(getKeyData()));
        strB.append(getTOString("depositNo", depositNo));
        strB.append(getTOString("proofTypeId", proofTypeId));
        strB.append(getTOString("proofNum", proofNum));
        strB.append(getTOString("issueAuth", issueAuth));
        strB.append(getTOStringEnd());
        return strB.toString();
    }

    /**
     * toXML method which returns this TO as a XML output.
     */
    public String toXML() {
        StringBuffer strB = new StringBuffer(getTOXmlStart(this.getClass().getName()));
        strB.append(getTOXmlKey(getKeyData()));
        strB.append(getTOXml("depositNo", depositNo));
        strB.append(getTOXml("proofTypeId", proofTypeId));
        strB.append(getTOXml("proofNum", proofNum));
        strB.append(getTOXml("issueAuth", issueAuth));
        strB.append(getTOXmlEnd());
        return strB.toString();
    }
}