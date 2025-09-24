/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * MajorHeadTO.java
 *
 * Created on August 25, 2003, 11:47 AM
 */
package com.see.truetransact.transferobject.generalledger;

import java.util.HashMap;

/**
 *
 * @author Annamalai
 */
public class MajorHeadTO implements java.io.Serializable {

    private String accountType;
    private String majorHead;
    private String majorHeadDesc;
    private HashMap subHeadMap;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getMajorHead() {
        return majorHead;
    }

    public void setMajorHead(String majorHead) {
        this.majorHead = majorHead;
    }

    public String getMajorHeadDesc() {
        return majorHeadDesc;
    }

    public void setMajorHeadDesc(String majorHeadDesc) {
        this.majorHeadDesc = majorHeadDesc;
    }

    public HashMap getSubHeadList() {
        return subHeadMap;
    }

    public void setSubHeadList(HashMap subHeadMap) {
        this.subHeadMap = subHeadMap;
    }
}
