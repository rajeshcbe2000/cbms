/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CheckableItem.java
 */

package com.see.truetransact.clientutil;

public class CheckableItem {
    private String  str;
    private boolean isSelected;
    
    /**
     * CheckableItem constructor comment.
     */
    public CheckableItem() {
        super();
    }
    public CheckableItem(String str) {
        this.str = str;
        isSelected = false;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean b) {
        isSelected = b;
    }
    public String toString() {
        return str;
    }
}
