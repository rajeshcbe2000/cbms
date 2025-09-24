package com.see.truetransact.transferobject.ttlite;

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * .
 *
 * UserTO.java
 *
 * Created on May 4, 2004, 2:39 PM
 */
import com.see.truetransact.transferobject.TransferObject;
import java.util.Date;

/**
 * This class will act as a Bean class to the User Information
 *
 * @author Pranav
 */
public class UserTO extends TransferObject {

    private String userId;
    private String password;
    private String prodType;
    private Date currDt;

    public Date getCurrDt() {
        return currDt;
    }

    public void setCurrDt(Date currDt) {
        this.currDt = currDt;
    }
    /** Creates a new instance of AccountInfoTO */
    public UserTO() {
    }

    /**
     * Getter for property userId.
     *
     * @return Value of property userId.
     *
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Setter for property userId.
     *
     * @param accountNumber New value of property userId.
     *
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Getter for property password.
     *
     * @return Value of property password.
     *
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for property password.
     *
     * @param accountType New value of property password.
     *
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /** Getter for property prodType.
     * @return Value of property prodType.
     *
     */
    public String getProdType() {
        return prodType;
    }
    
    /** Setter for property prodType.
     * @param accountType New value of property prodType.
     *
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }
}
