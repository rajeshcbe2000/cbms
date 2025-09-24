/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CObservable.java
 *
 * Created on April 12, 2004, 3:17 PM
 */

package com.see.truetransact.uicomponent;

import java.util.HashMap;

/**
 *
 * @author  bala
 */
public class CObservable extends java.util.Observable {
    
    private String screen; // Screen Name
    
    private String module; // Module Name
    private String selectedBranchID;
    
    private String statusBy;
    private String authorizeStatus;
    private HashMap proxyReturnMap;
    
    /** Creates a new instance of CObservable */
    public CObservable() {
    }
    
    /** Getter for property module.
     * @return Value of property module.
     *
     */
    public java.lang.String getModule() {
        return module;
    }
    
    /** Setter for property module.
     * @param module New value of property module.
     *
     */
    public void setModule(java.lang.String module) {
        this.module = module;
    }
    
    /** Getter for property screen.
     * @return Value of property screen.
     *
     */
    public java.lang.String getScreen() {
        return screen;
    }
    
    /** Setter for property screen.
     * @param screen New value of property screen.
     *
     */
    public void setScreen(java.lang.String screen) {
        this.screen = screen;
    }
    
    /**
     * Getter for property selectedBranchID.
     * @return Value of property selectedBranchID.
     */
    public java.lang.String getSelectedBranchID() {
        return selectedBranchID;
    }
    
    /**
     * Setter for property selectedBranchID.
     * @param selectedBranchID New value of property selectedBranchID.
     */
    public void setSelectedBranchID(java.lang.String selectedBranchID) {
        this.selectedBranchID = selectedBranchID;
        setChanged();
    }
    
    /**
     * Getter for property statusBy.
     * @return Value of property statusBy.
     */
    public java.lang.String getStatusBy() {
        return statusBy;
    }
    
    /**
     * Setter for property statusBy.
     * @param statusBy New value of property statusBy.
     */
    public void setStatusBy(java.lang.String statusBy) {
        this.statusBy = statusBy;
    }
    
    /**
     * Getter for property proxyReturnMap.
     * @return Value of property proxyReturnMap.
     */
    public HashMap getProxyReturnMap() {
        return proxyReturnMap;
    }
    
    /**
     * Setter for property proxyReturnMap.
     * @param proxyReturnMap New value of property proxyReturnMap.
     */
    public void setProxyReturnMap(HashMap proxyReturnMap) {
        this.proxyReturnMap = proxyReturnMap;
    }
    
    /**
     * Getter for property authorizeStatus.
     * @return Value of property authorizeStatus.
     */
    public java.lang.String getAuthorizeStatus() {
        return authorizeStatus;
    }
    
    /**
     * Setter for property authorizeStatus.
     * @param authorizeStatus New value of property authorizeStatus.
     */
    public void setAuthorizeStatus(java.lang.String authorizeStatus) {
        this.authorizeStatus = authorizeStatus;
    }
    
}
