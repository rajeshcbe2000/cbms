/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ClosingStockType.java
 */

package com.see.truetransact.ui.indend.closing;

/**
 * A model class represents a ClosingStockType.
 * @author user
 */
public class ClosingStockType {
     private String value;
 
    public ClosingStockType(String value) {
        super();
        this.value = value;
    }
 
    public String getName() {
        return value;
    }
 
    public void setName(String value) {
        this.value = value;
    }
     
    public String toString() {
        return this.value;
    }
     
}