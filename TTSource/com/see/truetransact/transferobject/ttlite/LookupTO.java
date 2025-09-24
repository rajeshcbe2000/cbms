package com.see.truetransact.transferobject.ttlite;

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * .
 *
 * LookupTO.java
 *
 * Created on May 3, 2004, 4:49 PM
 */
import com.see.truetransact.transferobject.TransferObject;

/**
 * This class will act as a Bean class to the Lookup details
 *
 * @author Sathiya
 */
public class LookupTO extends TransferObject {

    private String lookupId;
    private String lookupRefId;
    private String lookupDesc;

    public String getLookupDesc() {
        return lookupDesc;
    }

    public void setLookupDesc(String lookupDesc) {
        this.lookupDesc = lookupDesc;
    }

    public String getLookupId() {
        return lookupId;
    }

    public void setLookupId(String lookupId) {
        this.lookupId = lookupId;
    }

    public String getLookupRefId() {
        return lookupRefId;
    }

    public void setLookupRefId(String lookupRefId) {
        this.lookupRefId = lookupRefId;
    }

    /**
     * Creates a new instance of Lookup
     */
    public LookupTO() {
    }
}
