/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * LookUpTO.java
 *
 * Created on August 18, 2003, 3:07 PM
 */
package com.see.truetransact.transferobject.common.lookup;

/**
 *
 * @author annamalai
 */
public class LookUpTO implements java.io.Serializable {

    private String lookUpID;
    private String lookUpRefID;
    private String lookUpDesc;

    public java.lang.String getLookUpID() {
        return lookUpID;
    }

    public void setLookUpId(java.lang.String lookUpID) {
        this.lookUpID = lookUpID;
    }

    public String getLookUpRefID() {
        return lookUpRefID;
    }

    public void setLookUpRefID(String lookUpRefID) {
        this.lookUpRefID = lookUpRefID;
    }

    public String getLookUpDesc() {
        return lookUpDesc;
    }

    public void setLookUpDesc(String lookUpDesc) {
        this.lookUpDesc = lookUpDesc;
    }
}
